package com.colorcc.ddrpc.service.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.colorcc.ddrpc.service.tools.URI;

public class NettyClient implements Client {

	private Bootstrap bootstrap;
	private volatile Channel channel;
	private ChannelHandler channelHandler; 
	public NettyClient(ChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public void send(Object obj, URI uri) {

	}

	@Override
	public void connect(final URI uri) throws Exception {
		if (uri == null) {
			throw new IllegalArgumentException("uri is null");
		}

		bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
		.remoteAddress(new InetSocketAddress(uri.getHost(), uri.getPort())).option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500000);

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
				.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)), new ObjectEncoder())
				.addLast(new NettyClientHandler(uri, channelHandler));
			}
		});

		ChannelFuture f = bootstrap.connect().sync();
		if (f.isSuccess()) {
			Channel newChannel = f.channel();
			Channel oldChannel = this.channel;
			try {
				if (oldChannel != null) {
					oldChannel.close();
				}
			} finally {
				this.channel = newChannel;
			}
		} else {
			throw new RuntimeException("client connect to server failed.");
		}
		
		f.channel().closeFuture().sync();
	}
}
