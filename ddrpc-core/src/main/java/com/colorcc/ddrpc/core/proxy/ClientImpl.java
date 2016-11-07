package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ClientImpl<T> {
	
	public T getClientImpl(Class<T> type, ServiceProxy<?> proxy) {
		Class<?>[] infs = {type};
		@SuppressWarnings("unchecked")
		T clientImpl = (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), infs, new InvocationHandler(){
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(proxy, args);
			}});
		return clientImpl;
	}

}
