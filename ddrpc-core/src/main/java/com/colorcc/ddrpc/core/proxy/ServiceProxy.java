package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public interface ServiceProxy<T> {
	/**
	 * Service 接口
	 * @return
	 */
	Class<T> getInterface();
	
	/**
	 * 发送请求，得到 response
	 * @param request 
	 * @return
	 * @throws InterruptedException
	 */
	RpcResponse invoke(RpcRequest request) throws InterruptedException;
}
