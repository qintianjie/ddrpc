package com.colorcc.ddrpc.core.test.netty;

import com.colorcc.ddrpc.core.test.netty.decoder.StringToRpcRequestDecoder;
import com.colorcc.ddrpc.core.test.netty.encoder.RpcResponseToStringEncoder;
import com.colorcc.ddrpc.core.test.netty.encoder.StringToByteEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NServer {

	public static void main(String[] args) throws InterruptedException {
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
						// in:   decoder: [byte] --StringDecoder--> [string] --StringToRpcRequestDecoder--> [RpcRequest] --ServerInHandler--> [RpcResponse] --> write
						// out:  encoder: byte <--StringToByteEncoder-- [string] <--RpcResponseToStringEncoder-- [RpcResponse]
						ch.pipeline()
							.addLast(new StringDecoder(),  //in.1:  byte -> String
									new StringToRpcRequestDecoder(), //in.2:  String -> RpcRequest
//									new RpcResponseEncoder(), 
									new StringToByteEncoder(),  // out.2: String -> byte
									new RpcResponseToStringEncoder(), // out.1: RpcResponse -> String
									new ServerInHandler()); // in.3: biz process  handler -> RpcResponse
					}
					
				});
			
			ChannelFuture f = b.bind().sync();
			System.out.println("server started.");
			f.channel().closeFuture().sync();
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
		}
	}

}
