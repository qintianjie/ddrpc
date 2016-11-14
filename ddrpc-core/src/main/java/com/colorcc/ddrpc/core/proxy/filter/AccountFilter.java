package com.colorcc.ddrpc.core.proxy.filter;

import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class AccountFilter implements Filter {
	
	public int account = 0;

	@Override
	public RpcResponse invoke(ServiceProxy<?> serviceProxy, RpcRequest request) throws InterruptedException {
		account ++;
		RpcResponse response;
		if (account > 500) {
			response = new RpcResponse();
			response.setStatus(1000);
			response.setData(null);
		} else {
			response = serviceProxy.invoke(request);
			response.setStatus(200);
		}
		System.out.println("=========> account: " + account + ", status: " + response.getStatus());
		return response;
	}

}
