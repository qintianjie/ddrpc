package com.colorcc.ddrpc.test.netty;

import java.util.List;
import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class StringToRpcRequestDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		RpcResponse resp = new RpcResponse();
		resp.setId(UUID.randomUUID().toString());
		resp.setData(msg);
		out.add(resp);
	}

	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
		return String.class.equals(msg.getClass());
	}
	
	

}
