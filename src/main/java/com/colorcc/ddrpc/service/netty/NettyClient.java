package com.colorcc.ddrpc.service.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import com.colorcc.ddrpc.service.netty.callback.ClientCallback;
import com.colorcc.ddrpc.service.netty.decoder.StringToRpcResponseDecoder;
import com.colorcc.ddrpc.service.netty.encoder.RpcRequestToStringEncoder;
import com.colorcc.ddrpc.service.netty.handler.RpcClientInHandler;
import com.colorcc.ddrpc.service.netty.pojo.MethodMeta;
import com.colorcc.ddrpc.service.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.service.netty.pojo.RpcResponse;

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

	private RpcRequest rpcRequest;
	private ClientCallback<RpcResponse> callback;
	private Bootstrap bootstrap;

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public RpcRequest getRpcRequest() {
		return rpcRequest;
	}

	public ClientCallback<RpcResponse> getCallback() {
		return callback;
	}

	public NettyClient(RpcRequest rpcRequest, ClientCallback<RpcResponse> callback) {
		this.rpcRequest = rpcRequest;
		this.callback = callback;
		init();
	}

	public void init() {
		bootstrap.channel(NioSocketChannel.class).remoteAddress("127.0.0.1", 9088).option(ChannelOption.SO_BACKLOG, 1024).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
				// connect 建立连接,调用  channelActive 发送消息,其步骤为:
				// 1. client.connect()
				// 2. ClientInHandler.channelActive  
			    //       do work
				//       fireChanelActive()  ---> RpcClientInHandler
				// 3. RpcClientInHandler.channelActive
				// 		 	RpcRequest -->  JSONString  --> ByteBuf
				//          ctx.write(ByteBuf.data1)
				//       do ClientInHandler next work.
				//       ctx.writeAndFlush(ByteBuf.data2)
				// 最终数据   ByteBuf.data1+ByteBuf.data2
				
				//  接受 server response 时
				//  StringDecoder --> 将 ByteBuf --> String
				//  StringToRpcRequestDecoder --> 将 String --> RpcRequest
				//  ClientInHandler --> 对 RpcRequest 处理 , 可以进行 ctx.flush(); 返回
				//  RpcClientInHandler --> 如果需要,继续对 ClientInHandler 进行处理
				.addLast(new StringDecoder(), 
						 new StringToRpcResponseDecoder(), 
						 new StringEncoder(), // String -> byte
						 new RpcRequestToStringEncoder(),
						 new RpcClientInHandler<RpcResponse>(getRpcRequest(), getCallback()));
			}

		});
	}
	
	/**
	 * 每个请求一个  connect
	 * @param methodMeta 请求方法元信息,如 name, paramTypes
	 * @param paramValues 请求参数 
	 * @return
	 */
	public void request(MethodMeta methodMeta, Object[] paramValues) {
		this.getRpcRequest().setMethodMeta(methodMeta);
		this.getRpcRequest().setParamValues(paramValues);
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ChannelFuture f = this.getBootstrap().group(group).connect().sync();
//			// 根据 channelRead 处理 reslt
//			f.addListener(new ChannelFutureListener() {
//				@Override
//				public void operationComplete(ChannelFuture future) throws Exception {
//					if (future.isSuccess()) {
//						callback.processResponse(t);
//					} else {
//						// log it
//					}
//				}
//			});
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public Object getResult() {
		return callback.getResult();
	}

}
