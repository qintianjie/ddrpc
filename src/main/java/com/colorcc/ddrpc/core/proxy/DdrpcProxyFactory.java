package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;

import com.colorcc.ddrpc.core.beans.ContainerHook;

/**
 * Ddrpc 框架生成的每个 type 的  proxy
 * 因此 type --> ServiceFactoryBean --> DdrpcProxyFactory 这个流程下来,目前 proxy 是用 jdk 默认实现
 *
 * @author Qin Tianjie
 * @version Sep 19, 2016 - 11:20:12 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved. 
 * @param <T>
 */
public class DdrpcProxyFactory<T>  {
	
	@Autowired
	private ContainerHook ddrpcFactoryBean;

	public void setDdrpcFactoryBean(ContainerHook ddrpcFactoryBean) {
		this.ddrpcFactoryBean = ddrpcFactoryBean;
	}

	private final Class<T> mapperInterface;
	private T implObj;

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public DdrpcProxyFactory(Class<T> mapperInterface, T implObj) {
		this.mapperInterface = mapperInterface;
		this.implObj = implObj;
	}

	@SuppressWarnings("unchecked")
	// @TODO: write service proxy by javassist
	public T newInstance() {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(implObj, args);
			}
		});
	}

}
