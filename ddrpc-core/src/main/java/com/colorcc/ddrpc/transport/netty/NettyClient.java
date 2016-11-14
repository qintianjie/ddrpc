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

public class NettyClient implements Client {

	private Bootstrap bootstrap;
	private Channel channel;
	private EventLoopGroup group;
	private String hostname;
	private String typeName;
	private int port;
	private URL url;

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public URL getUrl() {
		return url;
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

	public NettyClient() {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(1);
		this.hostname = "127.0.0.1";
		this.port = 9088;
		init();
	}

	public NettyClient(URL url) {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(1);
		this.hostname = url.getHost();
		this.port = url.getPort();
		this.url = url;
		this.typeName = url.getParameter("service");
		init();
	}

	public NettyClient(String hostname, int port) {
		this.bootstrap = new Bootstrap();
		this.group = new NioEventLoopGroup(1);
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
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
	}
	//@formatter:on

	private ChannelInitializer<SocketChannel> constructHandler(final BizChannelHandler bizHandler) {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
					.addLast(new IdleStateHandler(40,50,70, TimeUnit.SECONDS))
					.addLast(new HeartbeatServerHandler())
					.addLast(new StringDecoder(), // in: byte -> string
						 new StringToRpcResponseDecoder(), // in: string -> RpcResponse
						 new StringEncoder(), // out : String -> byte
						 new RpcRequestToStringEncoder())// out : RpcRequest -> string
					.addLast("bizHandler", bizHandler);
			}};
	}

	/**
	 * zk path:   /ddrpc/com.colorcc.ddrpc.sample.service.SampleService/provider/   [ip1:port2] [ip2:port2]
	 * 首先从 ZK 把 service 的 provider 下子节点都拿过来。
	 * 通过 Random 方式 load balance
	 * 如果请求失败，再请求一次。 （还失败就抛出异常）
	 * @param request
	 * @throws InterruptedException 
	 */
	
	public RpcResponse request(final RpcRequest request) throws InterruptedException {
		ChannelFuture future = null;
		ClientCallback<RpcResponse> respCallback = new ClientCallbackImpl<>();
		try {
			future = this.getBootstrap()
					.handler(constructHandler(new BizChannelHandler(respCallback)))
					.connect().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture f) throws Exception {
					if (f.isSuccess()) {
						f.channel().write(request);
					}
				}
			}).sync();
			RpcResponse resp = respCallback.getResult();
			return resp;
		} finally {
			if (future != null) {
				future.channel().closeFuture().sync();
			}
		}
	}

}