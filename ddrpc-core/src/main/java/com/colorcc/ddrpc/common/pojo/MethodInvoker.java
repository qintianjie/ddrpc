package com.colorcc.ddrpc.common.pojo;

import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;


public interface MethodInvoker<T> {
	
	 Class<T> getInterface();
	 
	 RpcResponse invoke(MethodMeta methodMeta);
	 
}
