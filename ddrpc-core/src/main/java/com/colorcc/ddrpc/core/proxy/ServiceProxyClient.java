package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.cluster.FailfastClusterNettyClient;
import com.colorcc.ddrpc.core.cluster.FailoverClusterNettyClient;
import com.colorcc.ddrpc.transport.netty.Client;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ServiceProxyClient<T> extends AbstractServiceProxy<T> {
	private Client client;

	public Client getClient() {
		return client;
	}

	public ServiceProxyClient(Class<T> ifs, URL url) {
		super(ifs, url);
		this.client = initCluster(ifs, url);
	}

	public ServiceProxyClient(Class<T> ifs, Client client) {
		super(ifs, client.getUrl());
		this.client = client;
	}

	@Override
	public RpcResponse invoke(RpcRequest request) throws InterruptedException {
		return client.request(request);
	}

	// 根据 url里 cluster 参数选择
	private Client initCluster(Class<T> type, final URL url) {
		Client clusterClient = null;
		String clusterType = url.getParameter("cluster");
		if ("failfast".equals(clusterType)) {
			clusterClient = new FailfastClusterNettyClient(type, url);
		} else if ("failsafe".equals(clusterType)) {
			clusterClient = new FailfastClusterNettyClient(type, url);
		} else { // failover
			clusterClient = new FailoverClusterNettyClient(type, url);
		}
		return clusterClient;
	}

}
