package com.colorcc.ddrpc.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
	
	public void init() {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
				.localAddress("127.0.0.1", 9088)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
//						ch.pipeline()
//						.addLast(new StringDecoder(), new ServerInHandler());
//						ch.pipeline()
//							.addLast(new StringDecoder(), new RpcResponseEncoder(), new ServerInHandler());
					}
					
				});
			
			ChannelFuture f = b.bind().sync();
			System.out.println("server started.");
			f.channel().closeFuture().sync();
		}catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
		}
	}

//	public static void main(String[] args) throws InterruptedException {
//		EventLoopGroup parentGroup = new NioEventLoopGroup();
//		EventLoopGroup childGroup = new NioEventLoopGroup();
//		try {
//			ServerBootstrap b = new ServerBootstrap();
//			
//			b.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
//				.localAddress("127.0.0.1", 9088)
//				.option(ChannelOption.SO_BACKLOG, 1024)
//				.childHandler(new ChannelInitializer<SocketChannel>() {
//					@Override
//					protected void initChannel(SocketChannel ch) throws Exception {
////						ch.pipeline()
////						.addLast(new StringDecoder(), new ServerInHandler());
//						ch.pipeline()
//							.addLast(new StringDecoder(), new RpcResponseEncoder(), new ServerInHandler());
//					}
//					
//				});
//			
//			ChannelFuture f = b.bind().sync();
//			System.out.println("server started.");
//			f.channel().closeFuture().sync();
//		} finally {
//			childGroup.shutdownGracefully();
//			parentGroup.shutdownGracefully();
//		}
//	}

}
