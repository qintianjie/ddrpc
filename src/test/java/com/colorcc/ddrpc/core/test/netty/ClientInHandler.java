package com.colorcc.ddrpc.core.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.core.test.netty.pojo.MethodMeta;
import com.colorcc.ddrpc.core.test.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.core.test.netty.pojo.RpcResponse;

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
		System.out.println("client send request: " + JSON.toJSONString(request));
		ctx.writeAndFlush(request);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcResponse resp = (RpcResponse) msg;
		// do response process for client
		System.out.println("client receive server: " + JSON.toJSONString(resp));
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
