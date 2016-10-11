package com.colorcc.ddrpc.service.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import com.colorcc.ddrpc.service.tools.URI;

public class TransportClient<T> implements Transport {

	private T result;
	private URI uri;

	public TransportClient(T result, URI uri) {
		this.result = result;
		this.uri = uri;
	}

	public final T getResult() {
		return result;
	}

	public URI getUri() {
		return uri;
	}

	public static void main(String[] args) {
		Request request = new RpcRequest();
		URI uri = new URI("http", null, null, "127.0.0.1", 8080, null, null);
		TransportClient<Request> tc = new TransportClient<>(request, uri);
		tc.doRequest();
	}

	private void doRequest() {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.remoteAddress(getUri().getHost(), getUri().getPort())
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
						.addLast(new ObjectEncoder()) // out
						.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null))) // in
						.addLast(new ClientChannelHandler<T>(getResult())); // in
					}
				});
		try {
			ChannelFuture f = bootstrap.connect().sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

}
