package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.Client;


public interface ProxyFactory {
	// for Consumer 
	<T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception;
	
	// for ServiceProxyServer
	<T> ServiceProxy<T> getProxy(T proxy, Class<T> type, URL url) throws Exception;
	
	// for ServiceProxyClient
	<T> ServiceProxy<T> getProxy(Class<T> type, URL url) throws Exception;
	<T> ServiceProxy<T> getProxy(Class<T> type, Client client) throws Exception;
}
