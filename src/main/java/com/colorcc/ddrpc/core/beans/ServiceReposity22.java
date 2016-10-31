package com.colorcc.ddrpc.core.beans;


/**
 * Service Reposity，即 Service 对象的 cache 每个 Service 在 bean define 阶段，其
 * className="...ServiceFactoryBean"，其实例化后执行 afterPropertiesSet() 方法，根据
 * context.getBean("impl bean name") 拿到
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:48:30 PM Copyright (c) 2016, tianjieqin@126.com
 *          All Rights Reserved.
 */
public class ServiceReposity22 {

//	private final Map<Class<?>, ServiceProxy<?>> knownMappers = new HashMap<>();
//
//	@SuppressWarnings("unchecked")
//	public <T> T getMapper(Class<T> type, ContainerHook ddrpcFactoryBean) throws Exception {
//		final ProxyFactory mapperProxyFactory = (ProxyFactory) knownMappers.get(type);
//		if (mapperProxyFactory == null) {
//			addMapper(type, ddrpcFactoryBean);
//			mapperProxyFactory = (ProxyFactory) knownMappers.get(type);
//		}
//		try {
////			return mapperProxyFactory.newInstance();
//			return mapperProxyFactory;
//		} catch (Exception e) {
//			throw new Exception("Error getting mapper instance. Cause: " + e, e);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> void addMapper(Class<T> type, ContainerHook ddrpcFactoryBean) throws DdrpcException {
//		if (type.isInterface()) {
//			if (!knownMappers.containsKey(type)) {
//				boolean loadCompleted = false;
//				try {
//					String ibn = "";
//					// use it in java 8
//					// DdrpcService[] ddrpcAnno = type.getAnnotationsByType(DdrpcService.class);
//					// if (ArrayUtils.isNotEmpty(ddrpcAnno)) {
//					// 		ibn = ddrpcAnno[0].ibn();
//					// }
//
//					// use it in java 7
//					DdrpcService ddrpcAnno = type.getAnnotation(DdrpcService.class);
//					if (ddrpcAnno != null) {
//						ibn = ddrpcAnno.ibn();
//					}
//
//					if (StringUtils.isBlank(ibn)) {
//						String simpleName = type.getSimpleName();
//						Character c = simpleName.charAt(0);
//						ibn = simpleName.replace(simpleName.charAt(0), Character.toLowerCase(c)) + "Impl";
//					}
//					T obj = null;
//					if (StringUtils.isNoneBlank(ibn)) {
//						ApplicationContext applicationContext = ddrpcFactoryBean.getApplicationContext();
//						obj = (T) applicationContext.getBean(ibn);
//					}
//					if (obj != null) {
//						ProxyFactory factory = new JdkProxyFactory();
//						try {
//							knownMappers.put(type, factory.getProxy(obj, type, null));
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else {
//						throw new DdrpcException("Service [" + type + "] haven't impl object (beanName is: [" + ibn + "]).");
//					}
//					loadCompleted = true;
//				} finally {
//					if (!loadCompleted) {
//						knownMappers.remove(type);
//					}
//				}
//			}
//		}
//	}
//
//	public <T> boolean hasMapper(Class<T> type) {
//		return knownMappers.containsKey(type);
//	}

}
