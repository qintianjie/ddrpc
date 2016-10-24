package com.colorcc.ddrpc.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import com.colorcc.ddrpc.transport.netty.decoder.StringToRpcRequestDecoder;
import com.colorcc.ddrpc.transport.netty.encoder.RpcResponseToStringEncoder;
import com.colorcc.ddrpc.transport.netty.encoder.StringToByteEncoder;
import com.colorcc.ddrpc.transport.netty.handler.BizChannelHandler;

public class NettyServer {
	private ServerBootstrap bootstrap;
	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;
	private Channel channel;

	public ServerBootstrap getBootstrap() {
		return bootstrap;
	}

	public EventLoopGroup getParentGroup() {
		return parentGroup;
	}

	public EventLoopGroup getChildGroup() {
		return childGroup;
	}

	public Channel getChannel() {
		return channel;
	}

	public NettyServer() {
		bootstrap = new ServerBootstrap();
		parentGroup = new NioEventLoopGroup();
		childGroup = new NioEventLoopGroup();
	}
	
	public void start() {
		try {			
			bootstrap.group(parentGroup, childGroup)
				.localAddress("127.0.0.1", 9088)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
						.addLast(new StringDecoder(),  //in.1:  byte -> String
								new StringToRpcRequestDecoder(), //in.2:  String -> RpcRequest
								new StringToByteEncoder(),  // out.2: String -> byte
								new RpcResponseToStringEncoder(), // out.1: RpcResponse -> String
								new BizChannelHandler(null)); // in.3: biz process  handler -> RpcResponse
					}
					
				}).channel(NioServerSocketChannel.class);
			
			ChannelFuture f = bootstrap.bind().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					channel = future.channel();
					System.out.println("server started.");
				}
			}).sync();
			f.channel().closeFuture().sync();
		}catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
		}
	}

	public void close() {
		try {
			this.getChannel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		NettyServer server = new NettyServer();
		server.start();
	}

	// public static void main(String[] args) throws InterruptedException {
	// EventLoopGroup parentGroup = new NioEventLoopGroup();
	// EventLoopGroup childGroup = new NioEventLoopGroup();
	// try {
	// ServerBootstrap b = new ServerBootstrap();
	//
	// b.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
	// .localAddress("127.0.0.1", 9088)
	// .option(ChannelOption.SO_BACKLOG, 1024)
	// .childHandler(new ChannelInitializer<SocketChannel>() {
	// @Override
	// protected void initChannel(SocketChannel ch) throws Exception {
	// // ch.pipeline()
	// // .addLast(new StringDecoder(), new ServerInHandler());
	// ch.pipeline()
	// .addLast(new StringDecoder(), new RpcResponseEncoder(), new
	// ServerInHandler());
	// }
	//
	// });
	//
	// ChannelFuture f = b.bind().sync();
	// System.out.println("server started.");
	// f.channel().closeFuture().sync();
	// } finally {
	// childGroup.shutdownGracefully();
	// parentGroup.shutdownGracefully();
	// }
	// }

}
