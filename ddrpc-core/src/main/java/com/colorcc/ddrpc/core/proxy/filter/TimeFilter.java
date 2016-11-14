package com.colorcc.ddrpc.core.proxy.filter;

import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class TimeFilter implements Filter {

	@Override
	public RpcResponse invoke(ServiceProxy<?> serviceProxy, RpcRequest request) throws InterruptedException {
		long startTime = System.nanoTime();
		RpcResponse response = serviceProxy.invoke(request);
		System.out.println("====filter =====> time: " + (System.nanoTime() - startTime));
		return response;
	}

}
