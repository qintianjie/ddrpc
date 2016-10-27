package com.colorcc.ddrpc.proxy;

import java.lang.reflect.Proxy;

public class JdkProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { serviceProxy.getInterface() }, new ServiceProxyInvocationHandler( serviceProxy));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAsyncProxy(ServiceProxy<?> serviceProxy, Class<T> asynInterface) throws Exception {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { asynInterface }, new ServiceProxyInvocationHandler( serviceProxy));
	}

}
