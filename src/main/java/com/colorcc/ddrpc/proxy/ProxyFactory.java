package com.colorcc.ddrpc.proxy;


public interface ProxyFactory {
	<T> T getProxy(ServiceProxy<T> serviceProxy) throws Exception;

    <T> T getAsyncProxy(ServiceProxy<?> serviceProxy, Class<T> asynInterface) throws Exception;

}
