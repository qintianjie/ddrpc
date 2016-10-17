package com.colorcc.ddrpc.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class RpcResponseToStringEncoder extends MessageToMessageEncoder<RpcResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, RpcResponse msg, List<Object> out) throws Exception {
		out.add(JSON.toJSONString(msg));
	}
	
	@Override
	public boolean acceptOutboundMessage(Object msg) throws Exception {
        return RpcResponse.class.equals(msg.getClass());
    }

}
