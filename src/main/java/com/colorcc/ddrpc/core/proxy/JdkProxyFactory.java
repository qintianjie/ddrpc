package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.Proxy;

import com.colorcc.ddrpc.common.tools.URL;

public class JdkProxyFactory implements ProxyFactory {

	@Override
	public <T> ServiceProxy<T> getProxy(T proxy, Class<T> type, URL url) throws Exception {
		ServiceProxy<T> proxyServer = new ServiceProxyServer<T>(type, url, proxy);
		return proxyServer;
	} 

	/**
	 * For client, use netty client & InvocationHandler
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { serviceProxy.getInterface() }, new ServiceProxyInvocationHandler( serviceProxy));
	}
	
	

}
