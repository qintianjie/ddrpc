package com.colorcc.ddrpc.core.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;

public class DdrpcProxyFactory<T>  {
	
	@Autowired
	private DdrpcFactoryBean ddrpcFactoryBean;

	public void setDdrpcFactoryBean(DdrpcFactoryBean ddrpcFactoryBean) {
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
