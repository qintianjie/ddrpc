package com.colorcc.ddrpc.service.netty.handler;

import java.nio.CharBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.service.netty.pojo.RpcRequest;


public class RpcClientInHandler extends ChannelInboundHandlerAdapter {
	
	private RpcRequest rpcRequest;
	
	public RpcClientInHandler(RpcRequest rpcRequest) {
		this.rpcRequest = rpcRequest;
	}

	public RpcRequest getRpcRequest() {
		return rpcRequest;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 if (rpcRequest == null) {
			 throw new NullPointerException("rpcRequest is null.");
		 }
		 
		 // @TODO obj -> ByteBuf
		 ByteBuf objBuf = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(JSON.toJSONString(rpcRequest)), CharsetUtil.UTF_8);
		 ctx.write(objBuf);  // 直接写入 bytebuf 返回调用者
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	

}
