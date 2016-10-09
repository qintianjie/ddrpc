package com.colorcc.ddrpc.service.proxy;

import com.colorcc.ddrpc.service.tools.URI;


public class ServerServiceProxy<T> extends AbstractServiceProxy<T> {
	
	private T impl;
	public ServerServiceProxy(Class<T> ifs, URI uri, T impl) {
		super(ifs, uri);
		this.impl = impl;
		
		MethodCache.registerMethod(ifs, impl);
	}
	
	

}
