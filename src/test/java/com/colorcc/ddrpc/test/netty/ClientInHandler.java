package com.colorcc.ddrpc.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.test.meta.MethodMeta;

public class ClientInHandler extends ChannelInboundHandlerAdapter {
	
	private MethodMeta obj;
	public ClientInHandler(MethodMeta obj) {
		this.obj = obj;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		RpcRequest request = new RpcRequest();
		request.setId(UUID.randomUUID().toString());
		request.setMethodMeta(obj);
		ctx.writeAndFlush(request);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcResponse resp = (RpcResponse)JSON.parseObject((String) msg, RpcResponse.class);
		System.out.println("client receive server: " + resp.getData());
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
