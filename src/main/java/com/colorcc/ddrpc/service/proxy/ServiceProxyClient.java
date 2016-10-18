package com.colorcc.ddrpc.service.proxy;

import io.netty.channel.ChannelHandler;

import com.colorcc.ddrpc.service.tools.URL;

public class ServiceProxyClient<T> extends AbstractServiceProxy<T> {
	
	private Client client;
	private ChannelHandler channelHandler;
	

	public ServiceProxyClient(Class<T> ifs, URL url, ChannelHandler channelHandler) {
		super(ifs, url);
		this.channelHandler = channelHandler;
		this.client = new NettyClient(channelHandler);
	}

}
