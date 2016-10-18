package com.colorcc.ddrpc.core.test.netty;

import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.core.test.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.core.test.netty.pojo.RpcResponse;

public class ServerInHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("server get request: " + JSON.toJSONString(msg));
		RpcRequest request = (RpcRequest) msg;
		// do request process for request
		RpcResponse resp = new RpcResponse();
		resp.setId(UUID.randomUUID().toString());
		resp.setData("server response for [" + JSON.toJSONString(request) + "]"); 
		System.out.println("server send response: " + JSON.toJSONString(resp));
		ctx.writeAndFlush(resp);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
}
