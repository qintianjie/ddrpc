package com.colorcc.ddrpc.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NClient {
	
	

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			
			b.group(group).channel(NioSocketChannel.class)
				.remoteAddress("127.0.0.1", 9088)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
						.addLast(new StringDecoder(), new ClientInHandler(null));
//						.addLast(new StringDecoder(), new ClientInHandler(null), new RpcRequestEncoder());
					}
					
				});
			
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
		

	}

}
