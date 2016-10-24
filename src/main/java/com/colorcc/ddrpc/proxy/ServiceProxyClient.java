package com.colorcc.ddrpc.proxy;

import com.colorcc.ddrpc.pojo.MethodMeta;
import com.colorcc.ddrpc.tools.URL;
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
	public RpcResponse invoke(MethodMeta methodMeta, Object[] args) {
		RpcRequest request = new RpcRequest(methodMeta, args, null);
		client.request(request);
		return client.getResult();
	} 

}
