package com.colorcc.ddrpc.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.test.meta.MethodMeta;

public class ClientInHandler extends ChannelInboundHandlerAdapter {
	
	private MethodMeta obj;
	public ClientInHandler(MethodMeta obj) {
		this.obj = obj;
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String str = "client connect to server and send data.";
		if (obj != null) {
			str = JSON.toJSONString(obj);
		} 
		
		ByteBuf out = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(str), CharsetUtil.UTF_8);
		ctx.writeAndFlush(out);
		
//		RpcRequest request = new RpcRequest();
//		request.setId(UUID.randomUUID().toString());
//		request.setMethodMeta(obj);
//		ctx.write(request);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		RpcResponse resp = (RpcResponse)msg;
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