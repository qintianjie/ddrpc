package com.colorcc.ddrpc.core.proxy.filter;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class PrintFilter implements Filter {

	@Override
	public RpcResponse invoke(ServiceProxy<?> serviceProxy, RpcRequest request) {
		RpcResponse response = serviceProxy.invoke(request);
		System.out.println("====print log: =====> time: " + JSON.toJSONString(request));
		return response;
	}

}
