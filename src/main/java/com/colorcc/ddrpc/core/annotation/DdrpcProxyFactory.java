package com.colorcc.ddrpc.core.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

import com.colorcc.sample.service.SampleService;
import com.colorcc.sample.service.impl.SampleServiceImpl;

public class DdrpcProxyFactory<T> {

	private final Class<T> mapperInterface;

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public DdrpcProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@SuppressWarnings("unchecked")
	public T newInstance() {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				SampleService ss = new SampleServiceImpl();
				ss.say((String)args[0], (Integer)args[1], (Date)args[2]);
				
				System.out.println("dododododo..." + args);
				return null;
			}

		});
	}

}
