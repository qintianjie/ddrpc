package com.colorcc.ddrpc.transport.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class StringToRpcResponseDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		System.out.println("response str: " + msg);
		RpcResponse request = JSON.parseObject(msg, RpcResponse.class);
		out.add(request);
	}

	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
		return String.class.equals(msg.getClass());
	}
	
	

}
