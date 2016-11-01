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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.decoder.StringToRpcRequestDecoder;
import com.colorcc.ddrpc.transport.netty.encoder.RpcResponseToStringEncoder;
import com.colorcc.ddrpc.transport.netty.encoder.StringToByteEncoder;
import com.colorcc.ddrpc.transport.netty.handler.BizChannelHandler;
import com.colorcc.ddrpc.transport.netty.handler.HeartbeatServerHandler;

public class NettyServer {

	private ServerBootstrap bootstrap;
	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;
	private Channel channel;
	private String hostname;
	private int port;
	

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

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
		parentGroup = new NioEventLoopGroup(4);
		childGroup = new NioEventLoopGroup(16);
		this.hostname = "127.0.0.1";
		this.port = 9088;
	}
	
	public NettyServer(URL url) {
		bootstrap = new ServerBootstrap();
		parentGroup = new NioEventLoopGroup(4);
		childGroup = new NioEventLoopGroup(16);
		this.hostname = url.getHost();
		this.port = url.getPort();
	}
	
	public NettyServer(String hostname, int port) {
		bootstrap = new ServerBootstrap();
		parentGroup = new NioEventLoopGroup(4);
		childGroup = new NioEventLoopGroup(16);
		this.hostname = hostname;
		this.port = port;
	}

	public void start() {
		try {
			//@formatter:off
			bootstrap.group(parentGroup, childGroup)
				.localAddress(this.getHostname(), this.port)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
						.addLast(new IdleStateHandler(40,50,70, TimeUnit.SECONDS))
						.addLast(new HeartbeatServerHandler())
						.addLast(new StringDecoder(),  //in.1:  byte -> String
								new StringToRpcRequestDecoder(), //in.2:  String -> RpcRequest
								new StringToByteEncoder(),  // out.2: String -> byte
								new RpcResponseToStringEncoder()) // out.1: RpcResponse -> String
						.addLast(new BizChannelHandler(null)); // in.3: biz process  handler -> RpcResponse
					}
					
				}).channel(NioServerSocketChannel.class);
			// @formatter:on
			ChannelFuture f = bootstrap.bind().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						channel = future.channel();
						System.out.println("server started.");
					} else {
						future.channel().close();
						System.out.println("server start failed.");
					}
				}
			}).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		NettyServer server = new NettyServer();
		server.start();
	}

}