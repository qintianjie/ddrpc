package com.colorcc.ddrpc.core.proxy.filter;

import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public interface Filter {
	
//	Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;
	RpcResponse invoke(ServiceProxy<?> serviceProxy, RpcRequest request);

}
