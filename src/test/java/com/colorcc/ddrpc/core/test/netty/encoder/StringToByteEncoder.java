package com.colorcc.ddrpc.core.test.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class StringToByteEncoder extends MessageToByteEncoder<String> {

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		out.writeCharSequence(msg, CharsetUtil.UTF_8);
	}

}
