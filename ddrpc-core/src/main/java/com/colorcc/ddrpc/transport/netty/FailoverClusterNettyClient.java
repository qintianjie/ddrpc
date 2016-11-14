package com.colorcc.ddrpc.transport.netty;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class FailoverClusterNettyClient extends ClusterNettyClient {

	public FailoverClusterNettyClient(Client client) {
		super(client);
	}

	@Override
	protected RpcResponse doRequest(RpcRequest request) {
		try {
			for (int i = 0; i<3;) {
				i++;
				System.out.println("try time: " + i);
				Client client = loadbananceSelect(request.getClassType().getName());
				RpcResponse resp = client.request(request);
				return resp;
			}
		} catch (Exception e) {
			System.out.println("FailoverClusterNettyClient failed: " + JSON.toJSONString(request));
			e.printStackTrace();
		}
		
		RpcResponse resp = new RpcResponse();
		resp.setId(UUID.randomUUID().toString());
		resp.setStatus(5001);
		return resp;
	}

}
