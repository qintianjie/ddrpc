package com.colorcc.ddrpc.service.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.colorcc.ddrpc.service.tools.URI;

public class NettyClient implements Client {
	
	private Bootstrap bootstrap;
	private URI uri;

	@Override
	public void connect(URI uri) throws Exception {
		if (uri == null) {
            throw new IllegalArgumentException("uri is null");
        }
		this.uri = uri;
		
		bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.remoteAddress(new InetSocketAddress(uri.getHost(), uri.getPort()))
				.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast( new ObjectDecoder(ClassResolvers.cacheDisabled(null)), new ObjectEncoder())
//							.addLast(new ClientHandler());
				}
			});
			
			ChannelFuture f = bootstrap.connect().sync();
			f.channel().closeFuture().sync();
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public URI getUri() {
		return null;
	}

	@Override
	public void send() {

	}

}
