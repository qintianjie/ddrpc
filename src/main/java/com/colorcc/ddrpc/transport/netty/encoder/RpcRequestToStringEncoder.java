package com.colorcc.ddrpc.transport.netty.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;

public class RpcRequestToStringEncoder extends MessageToMessageEncoder<RpcRequest> {

	@Override
	public boolean acceptOutboundMessage(Object msg) throws Exception {
		return RpcRequest.class.equals(msg.getClass());
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, RpcRequest msg, List<Object> out) throws Exception {
		out.add(JSON.toJSONString(msg));
	}

}
