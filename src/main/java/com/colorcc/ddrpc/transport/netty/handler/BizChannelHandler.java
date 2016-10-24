package com.colorcc.ddrpc.transport.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.transport.netty.callback.ClientCallback;
import com.colorcc.ddrpc.transport.netty.pojo.RpcRequest;
import com.colorcc.ddrpc.transport.netty.pojo.RpcResponse;

public class BizChannelHandler extends ChannelDuplexHandler {
	
	private ClientCallback<RpcResponse> callback;
	
	public BizChannelHandler(ClientCallback<RpcResponse> callback) {
		this.callback = callback;
	}
	
	
	
//
//	@Override
//	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
//		// TODO Auto-generated method stub
//		super.bind(ctx, localAddress, promise);
//	}
//
//	@Override
//	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
//		// TODO Auto-generated method stub
//		super.connect(ctx, remoteAddress, localAddress, promise);
//	}
//
//	@Override
//	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
//		// TODO Auto-generated method stub
//		super.disconnect(ctx, promise);
//	}
	
	

	public ClientCallback<RpcResponse> getCallback() {
		return callback;
	}



	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("read: " + JSON.toJSONString(msg));
		if (msg instanceof RpcRequest) { // server receive the request, process request - > response
			RpcRequest request = (RpcRequest) msg;
			
			RpcResponse resp = new RpcResponse();
			resp.setId(UUID.randomUUID().toString());
			resp.setData(request); 
			resp.setAttachmentItem("KEY_RESP", "SERVER_RESPONSE_READ");
			ctx.writeAndFlush(resp);
		} else if (msg instanceof RpcResponse) { // client reveive the response, process the result
			RpcResponse response = (RpcResponse) msg;
			
			this.getCallback().processResponse(response);
			RpcResponse result = this.getCallback().getResult();
			System.out.println(JSON.toJSONString(result));
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
			ctx.writeAndFlush(response);
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

//	public void send(MethodMeta metod, Object[] args) {
//		
//	}

}
