package com.colorcc.ddrpc.service.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

import com.colorcc.ddrpc.service.tools.URI;

public class TransportServer<T> implements Transport {

	private URI uri;
	private Class<T> classType; 
	private T impl;
	

	public URI getUri() {
		return uri;
	}

	public TransportServer(URI uri, Class<T> classType, T impl) {
		this.uri = uri;
		this.classType = classType;
		this.impl = impl;
	}

	public Class<T> getClassType() {
		return classType;
	}

	public T getImpl() {
		return impl;
	}

//	public static void main(String[] args) {
//		Request request = new RpcRequest();
//		TransportServer<Request> ts = new TransportServer<>();
//		
//		doBizProcess();
//
//	}

	public void doBizProcess() throws InterruptedException {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(parentGroup, childGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(getUri().getPort()))
				.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new ObjectEncoder()) // out
					.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null))) // in
					.addLast(new ServerChannelHandler<T>(getClassType(), getImpl())); // in
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			parentGroup.shutdownGracefully().sync();
			childGroup.shutdownGracefully().sync();
		}
	}

}
