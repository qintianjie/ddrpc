package com.colorcc.ddrpc.test.netty.pojo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

public class ServerInHandler extends ChannelInboundHandlerAdapter {

//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("ServerInHandler active");
//		super.channelActive(ctx);
//	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("server read msg: " + msg);
		String str = "server response for msg: " + msg;
		
		RpcResponse resp = new RpcResponse();
		resp.setId(UUID.randomUUID().toString());
		resp.setData(str);
		
//		ByteBuf out = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(str), CharsetUtil.UTF_8);
		ctx.writeAndFlush(resp);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
}
