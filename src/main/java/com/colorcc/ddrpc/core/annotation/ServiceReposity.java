package com.colorcc.ddrpc.core.annotation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Duoduo
 *
 */
public class ServiceReposity {

	private final Map<Class<?>, DdrpcProxyFactory<?>> knownMappers = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> type,DdrpcFactoryBean ddrpcFactoryBean) throws Exception {
		final DdrpcProxyFactory<T> mapperProxyFactory = (DdrpcProxyFactory<T>) knownMappers.get(type);
		if (mapperProxyFactory == null) {
			addMapper(type,ddrpcFactoryBean);
		}
		try {
			return mapperProxyFactory.newInstance();
		} catch (Exception e) {
			throw new Exception("Error getting mapper instance. Cause: " + e, e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void addMapper(Class<T> type, DdrpcFactoryBean ddrpcFactoryBean) {
		if (type.isInterface()) {
			if (!knownMappers.containsKey(type)) {
				boolean loadCompleted = false;
				try {
					DdrpcService[] ddrpcAnno = type.getAnnotationsByType(DdrpcService.class);
					String ibn = "";
					if (ArrayUtils.isNotEmpty(ddrpcAnno)) {
						ibn = ddrpcAnno[0].ibn();
					}
					if (StringUtils.isBlank(ibn)) {
						String simpleName = type.getSimpleName();
						Character  c = simpleName.charAt(0);
						ibn = simpleName.replace(simpleName.charAt(0), Character.toLowerCase(c)) + "Impl";
					}
					T obj = null;
					if (StringUtils.isNoneBlank(ibn)) {
						ApplicationContext applicationContext = ddrpcFactoryBean.getApplicationContext();
						obj = (T)applicationContext.getBean(ibn);
					} 
					knownMappers.put(type, new DdrpcProxyFactory<T>(type, obj));
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
