package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.colorcc.ddrpc.common.pojo.MethodCache;
import com.colorcc.ddrpc.common.pojo.MethodMeta;
import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;


public class ServiceProxyServer<T> extends AbstractServiceProxy<T> {
	private T impl;
	
	public T getImpl() {
		return impl;
	}

	public ServiceProxyServer(Class<T> ifs, URL url, T impl) {
		super(ifs, url);
		this.impl = impl;
		 
		MethodCache.registerMethod(ifs, impl);
	} 
	
	@Override
	public RpcResponse invoke(RpcRequest request) {
		MethodMeta methodMeta = request.getMethodMeta();
		Object[] paramValues = request.getParamValues();
		
		
		
        Method m = MethodCache.getMethod(getInterface(), methodMeta.getMethodName());
//		Method m = request.getMethod();
        Object returnValue = null;
        try {
			returnValue = m.invoke(impl, paramValues);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
        
        RpcResponse response = new RpcResponse();
        response.setId(UUID.randomUUID().toString());
        response.setData(returnValue);
		return response;
	}
}
