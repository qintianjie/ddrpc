package com.colorcc.ddrpc.core.test.netty;

import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Server 启动后信息:
 * 1 --> server handler init where server start. [only one time during start]
 * 2 --> server started.
 * 
 * 接收第一个请求
 * 3 --> server childhandler init when build connect to each client. [a time for a client connection]
 * 6 --> server out bound read   // connect / channelActive 时触发第一次
 * 4 --> server receive request from client. data:  client send request to server
 * 5 --> server send data to client. data
 * 6 --> server out bound read  // 接收请求处理后,进行 channelReadComplete / read 触发第二次
 * 6 --> server out bound read  // 客户端 disconnect 后触发第三次
 * 
 * 接收第二个请求
 * 3 --> server childhandler init when build connect to each client. [a time for a client connection]
 * 6 --> server out bound read
 * 4 --> server receive request from client. data:  client send request to server
 * 5 --> server send data to client. data
 * 6 --> server out bound read
 * 6 --> server out bound read
 *
 * @author Qin Tianjie
 * @version Oct 26, 2016 - 11:37:00 PM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class NServer {

	public static void main(String[] args) {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(parentGroup, childGroup).option(ChannelOption.TCP_NODELAY, true).channel(NioServerSocketChannel.class).handler(new ChannelInitializer<ServerSocketChannel>() {
				@Override
				protected void initChannel(ServerSocketChannel arg0) throws Exception {
					System.out.println("1 --> server handler init where server start. [only one time during start]");
				}

			}).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					System.out.println("3 --> server childhandler init when build connect to each client. [a time for a client connection]");
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							ByteBuf buf = (ByteBuf) msg;
							byte[] req = new byte[buf.readableBytes()];
							buf.readBytes(req);
							String body = new String(req, "UTF-8");
							System.out.println("4 --> server receive request from client. data:  " + body);

							byte[] bytes = ("server send response for [" + body + "]").getBytes();
							ByteBuf buffer = Unpooled.buffer(bytes.length);
							buffer.writeBytes(bytes);
							
							System.out.println("5 --> server send data to client. data");
							ctx.writeAndFlush(buffer);
						}
					}).addLast(new ChannelOutboundHandlerAdapter(){

						@Override
						public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
							System.out.println("server out bound disconnect");
							super.disconnect(ctx, promise);
						}

						@Override
						public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
							System.out.println("server out bound close");
							super.close(ctx, promise);
						}

						@Override
						public void read(ChannelHandlerContext ctx) throws Exception {
							System.out.println("6 --> server out bound read");
							super.read(ctx);
						}

						@Override
						public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
							System.out.println("server out bound read");
							byte[] bytes = ("server send response for [ client ]").getBytes();
							ByteBuf buffer = Unpooled.buffer(bytes.length);
							buffer.writeBytes(bytes);
							ctx.writeAndFlush(buffer);
						}
						
						@Override
						public void flush(ChannelHandlerContext ctx) throws Exception {
							System.out.println("server out bound flush");
							super.flush(ctx);
						}
						
					});
				}

			});

			ChannelFuture future = bootstrap.bind("127.0.0.1", 8022).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture f) throws Exception {
					if (f.isSuccess()) {
						System.out.println("2 --> server started.");
					} else {
						System.out.println("server start failed.");
					}
				}
			}).sync();

			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}

	}

}