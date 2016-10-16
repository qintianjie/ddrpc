package com.colorcc.ddrpc.test.netty.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;

/**
 * channelActive 用于发送客户端请求
 * 
 * channelRead 主要用于 处理client accept response
 *
 * @author Qin Tianjie
 * @version Oct 16, 2016 - 11:40:59 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class ClientInHandler2 extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientInHandler2 - channelActive");
		ByteBuf ss = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap("wahha"), CharsetUtil.UTF_8);
		ctx.write(ss);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("ClientInHandler2 - channelRead, msg: " + msg);
	}
	

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientInHandler2 - channelRegistered");
		super.channelRegistered(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientInHandler2 - handlerAdded");
		super.handlerAdded(ctx);
	}
	
	

}
