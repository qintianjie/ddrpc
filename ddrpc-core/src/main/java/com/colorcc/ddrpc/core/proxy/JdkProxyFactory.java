package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.Proxy;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.Client;

public class JdkProxyFactory implements ProxyFactory {

	/**
	 * For client, get service impl by proxy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] { serviceProxy.getInterface() }, new ServiceProxyInvocationHandler( serviceProxy));
	}
	/**
	 * For Server, use service & impl   ==> ServiceProxy
	 */
	@Override
	public <T> ServiceProxy<T> getProxy(T proxy, Class<T> type, URL url) throws Exception {
		ServiceProxy<T> proxyServer = new ServiceProxyServer<T>(type, url, proxy);
		return proxyServer;
	}
	

	@Override
	public <T> ServiceProxy<T> getProxy(Class<T> type, URL url) throws Exception {
		ServiceProxy<T> proxyClient = new ServiceProxyClient<>(type, url);
		return proxyClient;
	} 

	/**
	 * For client, use service & netty client ==> ServiceProxy
	 */
	@Override
	public <T> ServiceProxy<T> getProxy(Class<T> type, Client client) throws Exception {
		ServiceProxy<T> proxyClient = new ServiceProxyClient<>(type, client);
		return proxyClient;
	}
}
