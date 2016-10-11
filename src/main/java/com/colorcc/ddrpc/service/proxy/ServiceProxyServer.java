package com.colorcc.ddrpc.service.proxy;

import java.lang.reflect.Method;

import com.colorcc.ddrpc.service.tools.URI;


public class ServiceProxyServer<T> extends AbstractServiceProxy<T> {
	private Client client;
	private T impl;
	
	public T getImpl() {
		return impl;
	}

	public ServiceProxyServer(Class<T> ifs, URI uri, T impl, Client client) {
		super(ifs, uri);
		this.impl = impl;
		this.client = client;
		
		MethodCache.registerMethod(ifs, impl);
	}
	
	public Response invoke(Request request) {
		RpcRequest rpcRequest = (RpcRequest) request;
		Method method = MethodCache.getMethod(this.getInterface(), rpcRequest.getMethodName());
		
		
		Response response = new Response();
		try {
			Object returnValue = method.invoke(getImpl(), request.getParameterValues());
			response.setResult(returnValue);
		} catch (Exception e) {
			response.setResult(null);
		}
		
		return response;
	}
	
	

}
