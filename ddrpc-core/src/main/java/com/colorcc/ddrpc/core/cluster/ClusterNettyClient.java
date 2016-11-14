package com.colorcc.ddrpc.core.cluster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.core.proxy.ServiceProxyClient;
import com.colorcc.ddrpc.core.proxy.filter.Filter;
import com.colorcc.ddrpc.core.proxy.filter.FilterFactory;
import com.colorcc.ddrpc.core.proxy.filter.PrintFilter;
import com.colorcc.ddrpc.core.proxy.filter.TimeFilter;
import com.colorcc.ddrpc.core.zk.curator.ZkUtils;
import com.colorcc.ddrpc.transport.netty.Client;
import com.colorcc.ddrpc.transport.netty.NettyClient;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public abstract class ClusterNettyClient implements Client {
	
	public ConcurrentMap<String, String[]> serviceProviders = new ConcurrentHashMap<>();
	public ConcurrentMap<String, List<ServiceProxy<?>>> serviceProxyMap = new ConcurrentHashMap<>();
	
	
	public ConcurrentMap<String, String[]> getServiceProviders() {
		return serviceProviders;
	}

	public ConcurrentMap<String, List<ServiceProxy<?>>> getServiceProxyMap() {
		return serviceProxyMap;
	}

	private Object lock = new Object(); 
	
	private Class<?> type;
	private URL url; 
	
	public ClusterNettyClient(Class<?> type, URL url) {
		this.type = type;
		this.url = url;
		initServiceProviders();
	}
	
	private void initServiceProviders() {
		URL url = this.getUrl();
		String serviceName = getTypeName();
		String[] availableProviders = serviceProviders.get(serviceName);
		List<ServiceProxy<?>> serviceProxyList = new ArrayList<>();
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
							Client lbClient = new NettyClient(reqUrl);
							ServiceProxy<?> proxy = new ServiceProxyClient<>(this.type, lbClient);
							
							Filter timeFilter = new TimeFilter();
							Filter printFilter = new PrintFilter();
							List<Filter> filters = new LinkedList<>();
							filters.add(timeFilter);
							filters.add(printFilter);
							ServiceProxy<?> clientProxyWithFilter = FilterFactory.buildInvokerChain(proxy, filters);
							
							serviceProxyList.add(clientProxyWithFilter);
							
						}
						serviceProviders.put(serviceName, providerArray);
						serviceProxyMap.put(serviceName, serviceProxyList);
					}
				}
				
			}
		}
	}

	@Override
	public String getTypeName() {
		return this.url.getParameter("service");
	}

	@Override
	public URL getUrl() {
		return this.url;
	}

	@Override
	public RpcResponse request(RpcRequest request) {
		return doRequest(request);
	}
	
	protected abstract RpcResponse doRequest(RpcRequest request);   
	
	protected ServiceProxy<?> loadbananceSelect(String serviceName) {
		List<ServiceProxy<?>> clientList = serviceProxyMap.get(serviceName);
		ServiceProxy<?>[] array = clientList.toArray(new ServiceProxy<?>[] {});
		Random random = new Random();
		int curProviderIndex = random.nextInt(array.length) % array.length;
		ServiceProxy<?> proxy = array[curProviderIndex];
		return proxy;
	}

}
