package com.colorcc.ddrpc.test.netty.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.test.meta.MethodMeta;

/**
 * channelActive 用于发送客户端请求
 * 
 * channelRead 主要用于 处理client accept response
 *
 * @author Qin Tianjie
 * @version Oct 16, 2016 - 11:40:59 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class ClientInHandler extends ChannelInboundHandlerAdapter {
	
	private MethodMeta obj;
	public ClientInHandler(MethodMeta obj) {
		this.obj = obj;
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientInHandler1 - channelRegistered");
		super.channelRegistered(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientInHandler1 - handlerAdded");
		super.handlerAdded(ctx);
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientInHandler1 before - channelActive");
		String str = "client connect to server and send data.";
		if (obj != null) {
			str = JSON.toJSONString(obj);
		} 
		ByteBuf out = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(str), CharsetUtil.UTF_8);
		ctx.fireChannelActive();
		System.out.println("ClientInHandler1 after- channelActive");
		ctx.writeAndFlush(out);
		
		
		
//		RpcRequest request = new RpcRequest();
//		request.setId(UUID.randomUUID().toString());
//		request.setMethodMeta(obj);
//		ctx.write(request);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("ClientInHandler1 - channelRead");
		RpcResponse resp = (RpcResponse)JSON.parseObject((String) msg, RpcResponse.class);
		System.out.println("client receive server: " + resp.getData());
		ctx.fireChannelRead(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client received done.");
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
