package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.Proxy;

import com.colorcc.ddrpc.common.tools.URL;

public class JdkProxyFactory implements ProxyFactory {

	/**
	 * For client, use netty client & InvocationHandler
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { serviceProxy.getInterface() }, new ServiceProxyInvocationHandler( serviceProxy));
	}

	/**
	 * For Server, uee service & impl
	 */
	@Override
	public <T> ServiceProxy<T> getProxy(T proxy, Class<T> type, URL url) throws Exception {
		ServiceProxy<T> proxyServer = new ServiceProxyServer<T>(type, url, proxy);
		return proxyServer;
	} 

//	@SuppressWarnings("unchecked")
//	@Override
//	public <T> T getAsyncProxy(ServiceProxy<?> serviceProxy, Class<T> asynInterface) throws Exception {
//		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
//                new Class[] { asynInterface }, new ServiceProxyInvocationHandler( serviceProxy));
//	}
	
	

}
