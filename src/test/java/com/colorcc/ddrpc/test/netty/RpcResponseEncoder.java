package com.colorcc.ddrpc.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import com.alibaba.fastjson.JSON;

public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, RpcResponse msg, ByteBuf out) throws Exception {
		out.writeCharSequence(JSON.toJSONString(msg), CharsetUtil.UTF_8);
	}

}
