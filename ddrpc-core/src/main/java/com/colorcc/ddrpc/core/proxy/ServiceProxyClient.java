package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.transport.netty.Client;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ServiceProxyClient<T> extends AbstractServiceProxy<T> {
	private Client client;

	public Client getClient() {
		return client;
	}
	
	public ServiceProxyClient(Class<T> ifs, Client client) {
		super(ifs, client.getUrl());
		this.client = client;
	}
	
	@Override
	public RpcResponse invoke(RpcRequest request) throws InterruptedException {
		return client.request(request);
	} 
	
}
