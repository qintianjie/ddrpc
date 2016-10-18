package com.colorcc.ddrpc.service.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.core.test.netty.pojo.RpcRequest;

public class StringToRpcRequestDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		RpcRequest request = JSON.parseObject(msg, RpcRequest.class);
		out.add(request);
	}

	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
		return String.class.equals(msg.getClass());
	}
	
	

}
