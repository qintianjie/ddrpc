package com.colorcc.ddrpc.core.cluster;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class FailfastClusterNettyClient extends ClusterNettyClient { 
	
	public FailfastClusterNettyClient(Class<?> type, URL url) {
		super(type, url);
	}

	@Override
	protected RpcResponse doRequest(RpcRequest request) {
		try {
				ServiceProxy<?> proxy = loadbananceSelect(request.getClassType().getName());
				RpcResponse resp = proxy.invoke(request);
				return resp;
		} catch (Exception e) {
			System.out.println("FailfastClusterNettyClient failed: " + JSON.toJSONString(request));
			e.printStackTrace();
			RpcResponse resp = new RpcResponse();
			resp.setId(UUID.randomUUID().toString());
			resp.setStatus(200);
			return resp;
		}
		
	}

}
