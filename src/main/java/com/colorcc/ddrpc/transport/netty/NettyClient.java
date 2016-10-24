package com.colorcc.ddrpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.UUID;

import com.colorcc.ddrpc.transport.netty.callback.ClientCallback;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallbackImpl;
import com.colorcc.ddrpc.transport.netty.decoder.StringToRpcResponseDecoder;
import com.colorcc.ddrpc.transport.netty.encoder.RpcRequestToStringEncoder;
import com.colorcc.ddrpc.transport.netty.handler.BizChannelHandler;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

/**
 * Netty Client 用于方法调用时发起一个  connect 请求, 转发 server 返回的结果 
 * 使用步骤:
 * 	NettyClient client = new NettyClient(RpcRequest, ClientCallback); // that include init()
 *  client.request(MethodMeta methodMeta, Object[] paramValues); 
 *  Object result = callback.getResult();
 *
 * @author Qin Tianjie
 * @version Oct 17, 2016 - 1:21:48 AM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class NettyClient {

	private Bootstrap bootstrap;
	private BizChannelHandler handler;
	private Channel channel;
	private EventLoopGroup group;
	private ClientCallback<RpcResponse> callback;

	public BizChannelHandler getHandler() {
		return handler;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}
	
	public NettyClient() {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup();
		this.callback = new ClientCallbackImpl<>();
		this.handler = new BizChannelHandler(callback);
		init();
	}

	public void init() {
		bootstrap = bootstrap
			.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress("127.0.0.1", 9088)
//			.option(ChannelOption.SO_BACKLOG, 1024)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new StringDecoder(), 
							 new StringToRpcResponseDecoder(), 
							 new StringEncoder(), // String -> byte
							 new RpcRequestToStringEncoder(),
							 handler);
				}
		});
	}
	
	public void close() {
		try {
			if (channel != null) {
				channel.closeFuture().sync();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (group != null) {
				group.shutdownGracefully();
			}
		}
	}
	
	/**
	 * 每个请求一个  connect
	 * @param methodMeta 请求方法元信息,如 name, paramTypes
	 * @param paramValues 请求参数 
	 * @return
	 */
	public void request(final RpcRequest request) {
		try {
			this.getBootstrap().connect().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					System.out.println("connect complete");
					channel = future.channel();
					System.out.println("connect request");
					channel.writeAndFlush(request);
				}
			}).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Object getResult() {
		RpcResponse result = this.getHandler().getCallback().getResult();
		close();
		return result;
	}
	
	public static void main(String[] args) throws InterruptedException {
		NettyClient client = new NettyClient();
		
		RpcRequest request = new RpcRequest();
		request.setId(UUID.randomUUID().toString());
		request.setMethodMeta(null);
		client.request(request);
	}

}
