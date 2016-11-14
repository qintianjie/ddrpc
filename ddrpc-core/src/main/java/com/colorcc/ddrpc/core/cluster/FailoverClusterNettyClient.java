package com.colorcc.ddrpc.core.cluster;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class FailoverClusterNettyClient extends ClusterNettyClient {

	public FailoverClusterNettyClient(Class<?> type, URL url) {
		super(type, url);
	}

	@Override
	protected RpcResponse doRequest(RpcRequest request) {
		for (int i = 0; i < 3;) {
			try {
				i++;
				System.out.println("try time: " + i);
				ServiceProxy<?> proxy = loadbananceSelect(request.getClassType().getName());
				RpcResponse resp = proxy.invoke(request);
				return resp;
			} catch (Exception e) {
				System.out.println("FailoverClusterNettyClient failed. Times : [" + i + "]. Request : " + JSON.toJSONString(request));
				e.printStackTrace();
			}
		}

		RpcResponse resp = new RpcResponse();
		resp.setId(UUID.randomUUID().toString());
		resp.setStatus(5001);
		return resp;
	}

}
