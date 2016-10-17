package com.colorcc.ddrpc.service.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientInHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		long startTime = System.nanoTime();
		ctx.fireChannelActive(); // 调用其后 channelActive
		long t = System.nanoTime() - startTime;
		ctx.writeAndFlush("$$$using:" + t);
	}

//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelRead(ctx, msg);
//	}
//
//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelReadComplete(ctx);
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		// TODO Auto-generated method stub
//		super.exceptionCaught(ctx, cause);
//	}

}
