package com.colorcc.ddrpc.core.annotation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DdrpcFactoryBean<T> implements FactoryBean<T>, InitializingBean {

	private Class<T> mapperInterface;

	private boolean addToConfig = true;

	private final Map<Class<?>, DdrpcProxyFactory<T>> knownMappers = new HashMap<>();

	public DdrpcFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public DdrpcFactoryBean() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!knownMappers.containsKey(this.mapperInterface)) {
			addMapper(this.mapperInterface);
		}

	}

	public void addMapper(Class<T> type) {
		if (type.isInterface()) {
			if (!knownMappers.containsKey(type)) {

				boolean loadCompleted = false;
				try {
					knownMappers.put(type, new DdrpcProxyFactory<T>(type));
					loadCompleted = true;
				} finally {
					if (!loadCompleted) {
						knownMappers.remove(type);
					}
				}
			}
		}
	}

	@Override
	public T getObject() throws Exception {
		final DdrpcProxyFactory<T> mapperProxyFactory = (DdrpcProxyFactory<T>) knownMappers.get(this.mapperInterface);
		if (mapperProxyFactory != null) {
			try {
				return mapperProxyFactory.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("No object ........", e);
			}
		} else {
			throw new Exception("No object ........");
		}
	}

	@Override
	public Class<?> getObjectType() {
		return this.mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public boolean isAddToConfig() {
		return addToConfig;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

}
