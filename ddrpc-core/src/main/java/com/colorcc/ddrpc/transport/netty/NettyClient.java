package com.colorcc.ddrpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallback;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallbackImpl;
import com.colorcc.ddrpc.transport.netty.decoder.StringToRpcResponseDecoder;
import com.colorcc.ddrpc.transport.netty.encoder.RpcRequestToStringEncoder;
import com.colorcc.ddrpc.transport.netty.handler.BizChannelHandler;
import com.colorcc.ddrpc.transport.netty.handler.HeartbeatServerHandler;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class NettyClient {

	private Bootstrap bootstrap;
	private BizChannelHandler handler;
	private Channel channel;
	private EventLoopGroup group;
	private ClientCallback<RpcResponse> callback;
	private String hostname;
	private int port;
	
	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public BizChannelHandler getHandler() {
		return handler;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public Channel getChannel() {
		return channel;
	}

	public EventLoopGroup getGroup() {
		return group;
	}

	public ClientCallback<RpcResponse> getCallback() {
		return callback;
	}

	public NettyClient() {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(1);
		this.callback = new ClientCallbackImpl<>();
		this.handler = new BizChannelHandler(callback);
		this.hostname = "127.0.0.1";
		this.port = 9088;
		init();
	}
	
	public NettyClient(URL url) {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(1);
		this.callback = new ClientCallbackImpl<>();
		this.handler = new BizChannelHandler(callback);
		this.hostname = url.getHost();
		this.port = url.getPort();
		init();
	}
	
	public NettyClient(String hostname, int port) {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(1);
		this.callback = new ClientCallbackImpl<>();
		this.handler = new BizChannelHandler(callback);
		this.hostname = hostname;
		this.port = port;
		init();
	}

	//@formatter:off
	public void init() {
		bootstrap = bootstrap
			.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress(this.getHostname(), this.getPort())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
						.addLast(new IdleStateHandler(40,50,70, TimeUnit.SECONDS))
						.addLast(new HeartbeatServerHandler())
						.addLast(new StringDecoder(), // in: byte -> string
							 new StringToRpcResponseDecoder(), // in: string -> RpcResponse
							 new StringEncoder(), // out : String -> byte
							 new RpcRequestToStringEncoder())// out : RpcRequest -> string
						.addLast("bizHandler", handler);
				}
		});
	}
	//@formatter:on

	public void request(final RpcRequest request) {
		try {
			ChannelFuture future = this.getBootstrap().connect().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture f) throws Exception {
					if (f.isSuccess()) {
						channel = f.channel();
						channel.writeAndFlush(request);
						System.out.println();
					} else {
						// log it
						Throwable cause = f.cause();
						System.out.println("error: " + cause);
					}
				}
			});
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
//			this.getGroup().shutdownGracefully();
		}
	}

	public RpcResponse getResult() {
		RpcResponse result = this.getHandler().getCallback().getResult();
		return result;
	}

//	public static void main(String[] args) {
//		NettyClient client = new NettyClient();
//		RpcRequest request = new RpcRequest();
//		client.request(request);
//		System.out.println(JSON.toJSONString(client.getResult()));
//	}

}