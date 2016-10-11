package com.colorcc.ddrpc.service.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientChannelHandler<T> extends SimpleChannelInboundHandler<T> {
	
	private T obj;
	
	public ClientChannelHandler(T obj) {
		this.obj = obj;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("I am active ...");
		ctx.writeAndFlush(obj);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
		System.out.println("I am receive: " + msg.getClass().getName() + " : " + msg);
	}

}
