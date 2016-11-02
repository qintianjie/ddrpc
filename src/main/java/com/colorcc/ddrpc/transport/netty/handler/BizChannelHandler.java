package com.colorcc.ddrpc.transport.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.common.pojo.MethodMeta;
import com.colorcc.ddrpc.core.beans.ServiceReposity;
import com.colorcc.ddrpc.core.proxy.ServiceProxy;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallback;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class BizChannelHandler extends ChannelDuplexHandler {

	private ClientCallback<RpcResponse> callback;

	public BizChannelHandler(ClientCallback<RpcResponse> callback) {
		this.callback = callback;
	}

	public ClientCallback<RpcResponse> getCallback() {
		return callback;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("read: " + JSON.toJSONString(msg));
		if (msg instanceof RpcRequest) { // server receive the request, process request - > response
			RpcRequest request = (RpcRequest) msg;
			
			MethodMeta methodMeta = request.getMethodMeta();
			 
			ServiceProxy<?> serviceProxy = ServiceReposity.serviceProxyMappers.get(request.getClassType());
			methodMeta.setServiceProxy(serviceProxy);
			RpcResponse resp = serviceProxy.invoke(request);
			// do biz process
			// 1. get methodName & paramTypes & paramValues
			// 2. use one get method from repository
			// 3. process the result
//			RpcResponse resp = new RpcResponse();
			resp.setId(UUID.randomUUID().toString());
			resp.setData(request);
			resp.setAttachmentItem("KEY_RESP", "SERVER_RESPONSE_READ");
			ctx.writeAndFlush(resp);
		} else if (msg instanceof RpcResponse) { // client reveive the response, process the result
			RpcResponse response = (RpcResponse) msg;

			this.getCallback().processResponse(response);
			// RpcResponse result = this.getCallback().getResult();
			// System.out.println(JSON.toJSONString(result));
			ctx.channel().disconnect();
		} else {
			System.out.println(JSON.toJSONString(msg));
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof RpcRequest) {
			RpcRequest request = (RpcRequest) msg;
			request.setAttachmentItem("key", "CLIENT_REQUEST");
			ctx.writeAndFlush(request);
		} else if (msg instanceof RpcResponse) {
			RpcResponse response = (RpcResponse) msg;
			response.setAttachmentItem("key", "SERVER_RESPONSE_WRITE");
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		} else {
			ctx.writeAndFlush(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("userEventTriggered ");
		super.userEventTriggered(ctx, evt);
	}
}
