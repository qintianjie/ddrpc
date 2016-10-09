package com.colorcc.ddrpc.service.proxy;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import com.colorcc.ddrpc.service.tools.URI;

public class NettyClientHandler<T> extends ChannelDuplexHandler {
	
	 private final ChannelHandler handler;
	 private final URI url;
	 
	 public NettyClientHandler(URI url, ChannelHandler handler){
	        if (url == null) {
	            throw new IllegalArgumentException("url == null");
	        }
	        if (handler == null) {
	            throw new IllegalArgumentException("handler == null");
	        }
	        this.url = url;
	        this.handler = handler;
	    }

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.disconnect(ctx, promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.read(ctx);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.write(ctx, msg, promise);
	}
	
	

//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
//		
//	}
//	
//	
//
//	@Override
//	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelRegistered(ctx);
//	}
//
//
//
//	@Override
//	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelUnregistered(ctx);
//	}
//
//
//
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelActive(ctx);
//	}
//
//
//
//	@Override
//	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		super.channelInactive(ctx);
//	}
//
//
//
//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		cause.printStackTrace();
//		ctx.close();
//	}
	

}
