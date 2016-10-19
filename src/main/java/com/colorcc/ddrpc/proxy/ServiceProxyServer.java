package com.colorcc.ddrpc.proxy;

import java.lang.reflect.Method;

import com.colorcc.ddrpc.others.Client;
import com.colorcc.ddrpc.others.Request;
import com.colorcc.ddrpc.others.Response;
import com.colorcc.ddrpc.others.RpcRequest;
import com.colorcc.ddrpc.pojo.MethodCache;
import com.colorcc.ddrpc.tools.URL;


public class ServiceProxyServer<T> extends AbstractServiceProxy<T> {
	private Client client;
	private T impl;
	
	public T getImpl() {
		return impl;
	}

	public ServiceProxyServer(Class<T> ifs, URL url, T impl, Client client) {
		super(ifs, url);
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
