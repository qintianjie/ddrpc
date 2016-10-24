package com.colorcc.ddrpc.proxy;

import com.colorcc.ddrpc.pojo.MethodMeta;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;



public interface ServiceProxy<T> {
	Class<T> getInterface();
	RpcResponse invoke(MethodMeta methodMeta, Object[] args);
}
