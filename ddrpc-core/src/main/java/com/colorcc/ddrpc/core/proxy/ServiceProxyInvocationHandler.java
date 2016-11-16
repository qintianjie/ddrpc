package com.colorcc.ddrpc.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.colorcc.ddrpc.common.pojo.MethodMeta;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ServiceProxyInvocationHandler implements InvocationHandler {
	
	private ServiceProxy<?> serviceProxy;
	
	public ServiceProxyInvocationHandler(ServiceProxy<?> serviceProxy) {
		this.serviceProxy = serviceProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] paramValues) throws Throwable {
		RpcRequest request = new RpcRequest();
		
		MethodMeta methodMeta = new MethodMeta(method.getName(), method.getParameterTypes());
		request.setMethodMeta(methodMeta);
		request.setParamValues(paramValues);
		request.setId(UUID.randomUUID().toString());
		request.setClassType(serviceProxy.getInterface());
//		request.setMethod(method); // fastjson 不能序列化
		RpcResponse response = serviceProxy.invoke(request);
		return response.getData();
	}

}
