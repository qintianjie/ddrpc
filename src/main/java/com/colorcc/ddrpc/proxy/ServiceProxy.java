package com.colorcc.ddrpc.proxy;

import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public interface ServiceProxy<T> {
	Class<T> getInterface();
	RpcResponse invoke(RpcRequest request);
}
