package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.NettyClient;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ServiceProxyClient<T> extends AbstractServiceProxy<T> {

	private NettyClient client;

	public NettyClient getClient() {
		return client;
	}
	
	public ServiceProxyClient(Class<T> ifs, URL url, NettyClient client) {
		super(ifs, url);
		this.client = client;
	}
	
	@Override
	public RpcResponse invoke(RpcRequest request) {
		client.request(request);
		return client.getResult();
	} 
}
