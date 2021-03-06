package com.colorcc.ddrpc.core.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.colorcc.ddrpc.common.pojo.MethodMeta;
import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.define.DdrpcException;
import com.colorcc.ddrpc.core.proxy.JdkProxyFactory;
import com.colorcc.ddrpc.core.proxy.ProxyFactory;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.core.proxy.filter.AccountFilter;
import com.colorcc.ddrpc.core.proxy.filter.Filter;
import com.colorcc.ddrpc.core.proxy.filter.FilterFactory;
import com.colorcc.ddrpc.core.proxy.filter.PrintFilter;
import com.colorcc.ddrpc.core.proxy.filter.TimeFilter;
import com.colorcc.ddrpc.core.zk.curator.ZkUtils;
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
public class ServiceReposity {
	public static final Map<Class<?>, ServiceProxy<?>> serviceProxyMappers = new HashMap<>();
	public static final Map<String, NettyServer> serverMap = new HashMap<>(); 
	private final Lock lock = new ReentrantLock();
	
	public static final Map<String, MethodMeta> methodProxyMappers = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> ServiceProxy<T>  getMapper(Class<T> type, T impl, ContainerHook ddrpcFactoryBean) throws Exception {
		ServiceProxy<T>  obj = (ServiceProxy<T>) serviceProxyMappers.get(type);
		if (obj == null) {
			addMapper(type, impl, ddrpcFactoryBean);
			obj = (ServiceProxy<T> ) serviceProxyMappers.get(type);
		}
		return obj;
	}

	public <T> void addMapper(final Class<T> type, T impl, ContainerHook ddrpcFactoryBean) throws DdrpcException {
		if (type.isInterface()) {
			if (!serviceProxyMappers.containsKey(type)) {
				boolean loadCompleted = false;
				try {
					if (impl != null) {
						try {
							ProxyFactory factory = new JdkProxyFactory();
							String port = System.getProperty("ddrpc.netty.server.port", "9088");
							final URL url = new URL.Builder("ddrpc", "127.0.0.1", port)
								.param("service", type.getName())
								.param("uid", UUID.randomUUID().toString()).build();
							System.out.println(" ==> service: " + url);
							ServiceProxy<T> proxy = factory.getProxy(impl, type, url);
							
							Filter timeFilter = new TimeFilter();
							Filter printFilter = new PrintFilter();
							Filter accountFilter = new AccountFilter();
							List<Filter> filters = new LinkedList<>();
							filters.add(timeFilter);
							filters.add(printFilter);
							filters.add(accountFilter);
							ServiceProxy<T> proxyWithFilter = FilterFactory.buildInvokerChain(proxy, filters);
							
							serviceProxyMappers.put(type, proxyWithFilter); 
							
							openServer(type, url);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						throw new DdrpcException("Service [" + type + "] haven't impl object (beanName is: [" + (impl != null ? impl.getClass().getName() : "") + "]).");
					}
					loadCompleted = true;
				} finally {
					if (!loadCompleted) {
						serviceProxyMappers.remove(type);
					}
				}
			}
		}
	}

	private <T> void openServer(final Class<T> type, final URL url) {
		// open the server
		final String key = url.getHost() + "_" + url.getPort();
		//  启动 netty server
		if (!serverMap.containsKey(key)) {
			lock.lock();
			try {
				if (!serverMap.containsKey(key)) {
					System.out.println("=====> server init. " + type.getName());
					serverMap.put(key, null); // 占位符，实际考虑同步机制
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							if(serverMap.containsKey(key)) {
								NettyServer server = new NettyServer(url);
								serverMap.put(key, server);
								server.start();
							}
						}
					});
					t.start(); 
					//  服务注册到 ZK 节点
				}
			} finally {
				lock.unlock();
			}
		} else {
			System.out.println("=====> server has start. " + type.getName());
		}
		ZkUtils.registerProvider(url);
	}

	public <T> boolean hasMapper(Class<T> type) {
		return serviceProxyMappers.containsKey(type);
	}

}
