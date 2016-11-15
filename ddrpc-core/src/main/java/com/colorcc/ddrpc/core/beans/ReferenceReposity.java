package com.colorcc.ddrpc.core.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.cluster.FailfastClusterNettyClient;
import com.colorcc.ddrpc.core.cluster.FailoverClusterNettyClient;
import com.colorcc.ddrpc.core.define.DdrpcException;
import com.colorcc.ddrpc.core.proxy.JdkProxyFactory;
import com.colorcc.ddrpc.core.proxy.ProxyFactory;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.Client;
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
					// 首先拿到 cluster, 根据URL确定一种 cluster
					// 然后在 cluster 中进行 load balance， 选择一个 ServiceProxy
					// 最后对 ServiceProxy　进行 filter　包装，每个请求调用时用 filter　处理 request
					Client clusterClient = initCluster(type, url);
					ProxyFactory jdkProxy = new JdkProxyFactory();
					ServiceProxy<T> clusterProxy = jdkProxy.getProxy(type, clusterClient);
					
					ProxyFactory factory = new JdkProxyFactory();
					try {
						knownMappers.put(type, factory.getProxy(clusterProxy));
					} catch (Exception e) {
						e.printStackTrace();
					}
					loadCompleted = true;
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					if (!loadCompleted) {
						knownMappers.remove(type);
					}
				}
			}
		}
	}

	// 根据 url里 cluster 参数选择
	private <T> Client initCluster(Class<T> type, final URL url) {
		Client clusterClient = null;
		String clusterType = url.getParameter("cluster");
		if ("failfast".equals(clusterType)) {
			clusterClient = new FailfastClusterNettyClient(type, url);
		} else if ("failsafe".equals(clusterType)) {
			clusterClient = new FailfastClusterNettyClient(type, url);
		} else { // failover
			clusterClient = new FailoverClusterNettyClient(type, url);
		}
		return clusterClient;
	}

	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}

}
