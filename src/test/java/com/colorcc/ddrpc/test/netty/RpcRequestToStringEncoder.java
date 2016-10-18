package com.colorcc.ddrpc.test.netty;

import java.net.SocketAddress;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;

public class RpcRequestToStringEncoder extends MessageToMessageEncoder<RpcRequest> {

	@Override
	public boolean acceptOutboundMessage(Object msg) throws Exception {
		System.out.println("RpcRequestToStringEncoder acceptOutboundMessage");
		return RpcRequest.class.equals(msg.getClass());
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("RpcRequestToStringEncoder connect");
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, RpcRequest msg, List<Object> out) throws Exception {
		System.out.println("RpcRequestToStringEncoder encode");
		out.add(JSON.toJSONString(msg));
	}

}
