package com.colorcc.ddrpc.core.beans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 每个Service对应的FactoryBean
 * 实例化后，通过 afterPropertiesSet() 方法，做了：
 * 	1. 根据  mapperInterface， 通过 context.getBean(xxx) 找到其实现类
 *  2. 将 type 及其 impl object 一起，构造一个 proxy
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 10:59:55 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved. 
 * @param <T>
 */
public class ServiceFactoryBean<T> implements FactoryBean<T>, InitializingBean {

	private Class<T> mapperInterface;
	private boolean addToConfig = true;
	ServiceReposity reposity = new ServiceReposity();
	private T impl;
	
	

	public T getImpl() {
		return impl;
	}

	public void setImpl(T impl) {
		this.impl = impl;
	}

	/**
	 * Spring container 工具，拿到 context 可做很多事
	 */
	private ContainerHook containerHook;

	public ContainerHook getContainerHook() {
		return containerHook;
	}

	public void setContainerHook(ContainerHook containerHook) {
		this.containerHook = containerHook;
	}

	public ServiceFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public ServiceFactoryBean() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!reposity.hasMapper(this.mapperInterface)) {
			reposity.addMapper(this.mapperInterface, impl, containerHook);
		}
	}

	@Override
	public T getObject() throws Exception {
		return reposity.getMapper(this.mapperInterface, impl, containerHook);
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
