package com.colorcc.ddrpc.sample.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

/**
 * Client:
 * 1 --> client handler init.
 * 2 --> client connect
 * 3 --> client connected.
 * 4 --> client send request data.
 * 5 --> client out bound write
 * 6 --> client out bound flush
 * 7 --> client get response for: server send response for [client send request to server]
 * 8 --> client out bound close
 * 9 --> client disconnect to server.
 *
 * @author Qin Tianjie
 * @version Oct 26, 2016 - 11:40:17 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class SampleNettyClient {
	
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();
			
			bootstrap.group(group).option(ChannelOption.TCP_NODELAY, true)
			.channel(NioSocketChannel.class)
			.remoteAddress("127.0.0.1", 8022)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					System.out.println("1 --> client handler init.");
					
					ch.pipeline()
					.addLast(channelInHandler)
					.addLast(channelOutHandler);
				}
			});
			
			ChannelFuture future = bootstrap.connect()
				.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if (f.isSuccess()) {
							System.out.println("3 --> client connected.");
							byte[] bytes = "client send request to server".getBytes();
							ByteBuf buffer = Unpooled.buffer(bytes.length);
							buffer.writeBytes(bytes);
							System.out.println("4 --> client send request data.");
							f.channel().writeAndFlush(buffer);
						} else {
							System.out.println("client connect failed.");
						}
					}
				});
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}
	
	static final ChannelInboundHandlerAdapter channelInHandler = new ChannelInboundHandlerAdapter() {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "UTF-8");
			System.out.println("7 --> client get response for: " + body);
			ctx.channel().disconnect().addListener(new ChannelFutureListener(){
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("9 --> client disconnect to server.");
					}
				}
			});
		}
	};
	
	static final ChannelOutboundHandlerAdapter channelOutHandler = new ChannelOutboundHandlerAdapter() {

		@Override
		public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
			System.out.println("2 --> client connect");
			super.connect(ctx, remoteAddress, localAddress, promise);
		}

		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			
			System.out.println("5 --> client out bound write");
			
			super.write(ctx, msg, promise);
		}

		@Override
		public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
			System.out.println("client out bound disconnect");
			super.disconnect(ctx, promise);
		}

		@Override
		public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
			System.out.println("8 --> client out bound close");
			super.close(ctx, promise);
		}

		@Override
		public void flush(ChannelHandlerContext ctx) throws Exception {
			System.out.println("6 --> client out bound flush");
			super.flush(ctx);
		}
		
	};

}
