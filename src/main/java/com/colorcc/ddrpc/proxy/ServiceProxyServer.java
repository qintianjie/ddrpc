package com.colorcc.ddrpc.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import com.colorcc.ddrpc.others.Client;
import com.colorcc.ddrpc.pojo.MethodCache;
import com.colorcc.ddrpc.pojo.MethodMeta;
import com.colorcc.ddrpc.tools.URL;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;


public class ServiceProxyServer<T> extends AbstractServiceProxy<T> {
	private T impl;
	
	public T getImpl() {
		return impl;
	}

	public ServiceProxyServer(Class<T> ifs, URL url, T impl, Client client) {
		super(ifs, url);
		this.impl = impl;
		
		MethodCache.registerMethod(ifs, impl);
	} 
	
	@Override
	public RpcResponse invoke(RpcRequest request) {
		MethodMeta methodMeta = request.getMethodMeta();
		Object[] paramValues = request.getParamValues();
		
        Method m = MethodCache.getMethod(getInterface(), methodMeta.getMethodName());
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
	
//	@Override
//	public RpcResponse invoke(MethodMeta methodMeta, Object[] args) {
//        Method m = MethodCache.getMethod(getInterface(), methodMeta.getMethodName());
//        Object returnValue = null;
//        try {
//			returnValue = m.invoke(impl, args);
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
//        
//        RpcResponse response = new RpcResponse();
//        response.setId(UUID.randomUUID().toString());
//        response.setData(returnValue);
//		return response;
//	}
	
	

}
