package com.colorcc.ddrpc.service.proxy;

import io.netty.channel.ChannelHandler;

import com.colorcc.ddrpc.service.tools.URI;

public class ClientServiceProxy<T> extends AbstractServiceProxy<T> {
	
	private Client client;
	private ChannelHandler channelHandler;
	

	public ClientServiceProxy(Class<T> ifs, URI uri, ChannelHandler channelHandler) {
		super(ifs, uri);
		this.channelHandler = channelHandler;
		this.client = new NettyClient(channelHandler);
	}

}
