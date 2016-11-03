package com.colorcc.ddrpc.core.beans;

import java.lang.reflect.Method;
import java.util.HashMap;
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

	public static final Map<Class<?>, Object> knownMappers = new HashMap<>();
	public static final Map<Class<?>, ServiceProxy<?>> serviceProxyMappers = new HashMap<>();
	public static final Map<Method, MethodMeta> methodMappers = new HashMap<>();
	public static final Map<String, NettyServer> serverMap = new HashMap<>(); 
	private final Lock lock = new ReentrantLock();

	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> type, T impl, ContainerHook ddrpcFactoryBean) throws Exception {
		T obj = (T) knownMappers.get(type);
		if (obj == null) {
			addMapper(type, impl, ddrpcFactoryBean);
			obj = (T) knownMappers.get(type);
		}
		return obj;
	}

	public <T> void addMapper(final Class<T> type, T impl, ContainerHook ddrpcFactoryBean) throws DdrpcException {
		if (type.isInterface()) {
			if (!knownMappers.containsKey(type)) {
				boolean loadCompleted = false;
				try {
					if (impl != null) {
						knownMappers.put(type, impl);
						try {
							ProxyFactory factory = new JdkProxyFactory();
							final URL url = new URL.Builder("ddrpc", "127.0.0.1", 9088)
								.param("service", type.getName())
								.param("uid", UUID.randomUUID().toString()).build();
							System.out.println(" ==> service: " + url);
							ServiceProxy<T> proxy = factory.getProxy(impl, type, url);
							serviceProxyMappers.put(type, proxy); 
							
							// open the server
							final String key = url.getHost() + "_" + url.getPort();
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
									}
								} finally {
									lock.unlock();
								}
							} else {
								System.out.println("=====> server has start. " + type.getName());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						throw new DdrpcException("Service [" + type + "] haven't impl object (beanName is: [" + (impl != null ? impl.getClass().getName() : "") + "]).");
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
