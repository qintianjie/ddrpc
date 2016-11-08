package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
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
//		MethodCache.registerMethodMetas(ifs, impl, this);
	} 
	
	@Override
	public RpcResponse invoke(RpcRequest request) {
		MethodMeta methodMeta = request.getMethodMeta();
		Class<?>[] parameterTypes = methodMeta.getParameterTypes();
		Object[] paramValues = getParameterValues(parameterTypes, request.getParamValues());
		
//        Method m = MethodCache.getMethod(getInterface(), methodMeta.getMethodName());
        Method m = MethodCache.getMethod(getInterface(), methodMeta);
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
	
	private Object[] getParameterValues(Class<?>[] parameterTypes, Object[] paramValues) {
		if (paramValues == null || paramValues.length < 1 || paramValues.length != parameterTypes.length) {
			return null;
		} else {
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> c = parameterTypes[i];
				if (c.equals(Date.class) && (paramValues[i].getClass().equals(long.class) || paramValues[i].getClass().equals(Long.class))) {
					paramValues[i] = new Date((Long)paramValues[i]); 
				}
			}
		}
		
		return paramValues;
	}
}
