package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;


public interface ProxyFactory {
	<T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception;
	
	<T> ServiceProxy<T> getProxy(T proxy, Class<T> type, URL url) throws Exception;

    <T> T getAsyncProxy(ServiceProxy<?> serviceProxy, Class<T> asynInterface) throws Exception;

}
