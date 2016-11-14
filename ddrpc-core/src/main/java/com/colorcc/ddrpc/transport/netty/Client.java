package com.colorcc.ddrpc.transport.netty;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public interface Client {
	
	public String getTypeName();

	public URL getUrl();
	
	public RpcResponse request(final RpcRequest request) throws InterruptedException;
	

}
