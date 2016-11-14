package com.colorcc.ddrpc.transport.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.zk.curator.ZkUtils;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public abstract class ClusterNettyClient implements Client {
	
	public ConcurrentMap<String, String[]> serviceProviders = new ConcurrentHashMap<>();
	public ConcurrentMap<String, List<Client>> clientMap = new ConcurrentHashMap<>();
	
	
	public ConcurrentMap<String, String[]> getServiceProviders() {
		return serviceProviders;
	}

	public ConcurrentMap<String, List<Client>> getClientMap() {
		return clientMap;
	}

	private Object lock = new Object();
	
	private Client client;
	
	public ClusterNettyClient(Client client) {
		this.client = client;
		initServiceProviders();
	}
	
	private void initServiceProviders() {
		URL url = client.getUrl();
		String serviceName = getTypeName();
		String[] availableProviders = serviceProviders.get(serviceName);
		List<Client> clientList = new ArrayList<>();
		if (availableProviders == null) {
			synchronized (lock) {
				if (availableProviders == null) {
					String zkProviderPath = "/" + serviceName + "/provider";
					Map<String, String> providers = ZkUtils.getNodeChildren(zkProviderPath);
					String[] providerArray = new String[providers.size()];
					if (providers != null) {
						int i = 0;
						for (Entry<String, String> entry : providers.entrySet()) {
							String hostAndPort = entry.getKey();
							providerArray[i++] = hostAndPort;
							String[] hp = hostAndPort.split(":");
							URL reqUrl = url.resetHost(hp[0]).resetPort(Integer.valueOf(hp[1]));
							clientList.add(new NettyClient(reqUrl));
							
						}
						serviceProviders.put(serviceName, providerArray);
						clientMap.put(serviceName, clientList);
					}
				}
				
			}
		}
	}

	@Override
	public String getTypeName() {
		return client.getTypeName();
	}

	@Override
	public URL getUrl() {
		return client.getUrl();
	}

	@Override
	public RpcResponse request(RpcRequest request) {
		return doRequest(request);
	}
	
	protected abstract RpcResponse doRequest(RpcRequest request);  

	protected Client loadbananceSelect(String serviceName) {
		List<Client> clientList = clientMap.get(serviceName);
		Client[] array = clientList.toArray(new Client[] {});
		Random random = new Random();
		int curProviderIndex = random.nextInt(array.length) % array.length;
		Client client = array[curProviderIndex];
		return client;
	}

}
