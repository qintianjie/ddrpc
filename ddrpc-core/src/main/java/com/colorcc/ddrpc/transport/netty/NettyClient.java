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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.colorcc.ddrpc.common.tools.URL;
import com.colorcc.ddrpc.core.zk.curator.ZkUtils;
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

	private Object lock = new Object();

	public ConcurrentMap<String, String[]> serviceProviders = new ConcurrentHashMap<>();

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
//			.remoteAddress(this.getHostname(), this.getPort())
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

	/**
	 * zk path:   /ddrpc/com.colorcc.ddrpc.sample.service.SampleService/provider/   [ip1:port2] [ip2:port2]
	 * 首先从 ZK 把 service 的 provider 下子节点都拿过来。
	 * 通过 Random 方式 load balance
	 * 如果请求失败，再请求一次。 （还失败就抛出异常）
	 * @param request
	 */
	public void request(final RpcRequest request) {
		String[] availableProviders = initServiceProviders(request);
		Random random = new Random();
		int curProviderIndex = random.nextInt(availableProviders.length) % availableProviders.length;
		String nodeName = availableProviders[curProviderIndex];

		System.out.println("==========================================> use provider: [" + nodeName + "]");
		try {
			doRequest(request, nodeName);
		} catch (Exception e) {
			// log the exception
			int newProviderIndex = random.nextInt(availableProviders.length) % availableProviders.length;
			String newNodeName = availableProviders[newProviderIndex];
			System.out.println("==========================================> " + nodeName + " failed, use: " + newNodeName);
			try {
				doRequest(request, newNodeName);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} finally {
			// this.getGroup().shutdownGracefully();
		}
	}

	private void doRequest(final RpcRequest request, final String nodeName) throws InterruptedException {
		String[] nameNodeArr = nodeName.split(":");
		ChannelFuture future = this.getBootstrap().remoteAddress(nameNodeArr[0], Integer.valueOf(nameNodeArr[1]))
				.connect().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					f.channel().write(request);
				}
			}
		}).sync();
		future.channel().closeFuture().sync();

	}

	private String[] initServiceProviders(final RpcRequest request) {
		String serviceName = request.getClassType().getName();
		String[] availableProviders = serviceProviders.get(serviceName);
		if (availableProviders == null) {
			synchronized (lock) {
				if (availableProviders == null) {
					String zkProviderPath = "/" + serviceName + "/provider";
					Map<String, String> providers = ZkUtils.getNodeChildren(zkProviderPath);
					String[] providerArray = new String[providers.size()];
					if (providers != null) {
						int i = 0;
						for (Entry<String, String> entry : providers.entrySet()) {
							providerArray[i++] = entry.getKey();
						}
						serviceProviders.put(serviceName, providerArray);
					}
				}
			}

			availableProviders = serviceProviders.get(serviceName);
		}
		return availableProviders;
	}

	public RpcResponse getResult() {
		RpcResponse result = this.getHandler().getCallback().getResult();
		return result;
	}

	// public static void main(String[] args) {
	// NettyClient client = new NettyClient();
	// RpcRequest request = new RpcRequest();
	// client.request(request);
	// System.out.println(JSON.toJSONString(client.getResult()));
	// }

}