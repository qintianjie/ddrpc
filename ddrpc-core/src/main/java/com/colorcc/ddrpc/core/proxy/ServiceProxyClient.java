package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.Client;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class ServiceProxyClient<T> extends AbstractServiceProxy<T> {
	

	private Client client;

	public Client getClient() {
		return client;
	}
	
	public ServiceProxyClient(Class<T> ifs, URL url, Client client) {
		super(ifs, url);
		this.client = client;
	}
	
	@Override
	public RpcResponse invoke(RpcRequest request) throws InterruptedException {
//		client.request(request);
//		return client.getResult();
		return client.request(request);
	} 
	
//	public void request(final RpcRequest request) {
//		String[] availableProviders = initServiceProviders();
//		Random random = new Random();
//		int curProviderIndex = random.nextInt(availableProviders.length) % availableProviders.length;
//		String nodeName = availableProviders[curProviderIndex];
//		
//		client.request(request);
//		return client.getResult();
//
////		System.out.println("==========================================> use provider: [" + nodeName + "]");
////		try {
////			doRequest(request, nodeName);
////		} catch (Exception e) {
////			// log the exception
////			int newProviderIndex = random.nextInt(availableProviders.length) % availableProviders.length;
////			String newNodeName = availableProviders[newProviderIndex];
////			System.out.println("==========================================> " + nodeName + " failed, use: " + newNodeName);
////			try {
////				doRequest(request, newNodeName);
////			} catch (InterruptedException e1) {
////				e1.printStackTrace();
////			}
////		} finally {
////			// this.getGroup().shutdownGracefully();
////		}
//	}
	
	
}
