package com.colorcc.ddrpc.core.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import com.colorcc.ddrpc.core.test.netty.decoder.StringToRpcResponseDecoder;
import com.colorcc.ddrpc.core.test.netty.encoder.RpcRequestToStringEncoder;

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
						.addLast(new StringDecoder(),  // in.1  --> byte to string
								new StringToRpcResponseDecoder(), // in.2 string to rpcresponse
								new StringEncoder(),  // out.2 --> String  to byte
								new RpcRequestToStringEncoder(),  // out.1 --> RpcRequest to string
								new ClientInHandler(null)); // biz process, write request in channelActive
					}
					
				});
			
			ChannelFuture f = b.connect().sync();
			f.addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						System.out.println("success after connect");
					}
				}
				
			});
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
		

	}

}
