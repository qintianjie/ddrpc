package com.colorcc.ddrpc.service.netty.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.core.test.netty.pojo.RpcResponse;

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
