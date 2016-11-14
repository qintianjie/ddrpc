package com.colorcc.ddrpc.core.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.define.DdrpcException;
import com.colorcc.ddrpc.core.proxy.JdkProxyFactory;
import com.colorcc.ddrpc.core.proxy.ProxyFactory;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.core.proxy.ServiceProxyClient;
import com.colorcc.ddrpc.core.proxy.filter.Filter;
import com.colorcc.ddrpc.core.proxy.filter.FilterFactory;
import com.colorcc.ddrpc.core.proxy.filter.PrintFilter;
import com.colorcc.ddrpc.core.proxy.filter.TimeFilter;
import com.colorcc.ddrpc.transport.netty.Client;
import com.colorcc.ddrpc.transport.netty.FailoverClusterNettyClient;
import com.colorcc.ddrpc.transport.netty.NettyClient;
import com.colorcc.ddrpc.transport.netty.NettyServer;

/**
 * Service Reposity，即 Service 对象的 cache 每个 Service 在 bean define 阶段，其
 * className="...ServiceFactoryBean"，其实例化后执行 afterPropertiesSet() 方法，根据
 * context.getBean("impl bean name") 拿到
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:48:30 PM Copyright (c) 2016, tianjieqin@126.com
 *          All Rights Reserved.
 */
public class ReferenceReposity {

	public static final Map<Class<?>, Object> knownMappers = new HashMap<>();
	public static final Map<String, NettyServer> serverMap = new HashMap<>(); 

	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> type, ContainerHook ddrpcFactoryBean) throws Exception {
		T obj = (T) knownMappers.get(type);
		if (obj == null) {
			addMapper(type, ddrpcFactoryBean);
			obj = (T) knownMappers.get(type);
		}
		return obj;
	}

	public <T> void addMapper(Class<T> type, ContainerHook ddrpcFactoryBean) throws DdrpcException {
		if (type.isInterface()) {
			if (!knownMappers.containsKey(type)) {
				boolean loadCompleted = false;
				try {
					final URL url = new URL.Builder("ddrpc", "127.0.0.1", 9088)
					.param("service", type.getName())
					.param("uid", UUID.randomUUID().toString()).build();
					Client client =  new NettyClient(url);
					Client clusterClient = new FailoverClusterNettyClient(client);
					
					ServiceProxyClient<T> clientProxy = new ServiceProxyClient<>(type, url, clusterClient);
					Filter timeFilter = new TimeFilter();
					Filter printFilter = new PrintFilter();
					List<Filter> filters = new LinkedList<>();
					filters.add(timeFilter);
					filters.add(printFilter);
					ServiceProxy<T> clientProxyWithFilter = FilterFactory.buildInvokerChain(clientProxy, filters);
					
					ProxyFactory factory = new JdkProxyFactory();
					try {
						knownMappers.put(type, factory.getProxy(clientProxyWithFilter));
					} catch (Exception e) {
						e.printStackTrace();
					}
					loadCompleted = true;
				} finally {
					if (!loadCompleted) {
						knownMappers.remove(type);
					}
				}
			}
		}
	}

	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}

}
