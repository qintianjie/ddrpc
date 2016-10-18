package com.colorcc.ddrpc.service.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.colorcc.ddrpc.service.netty.callback.ClientCallback;
import com.colorcc.ddrpc.service.netty.pojo.RpcRequest;


public class RpcClientInHandler<T> extends ChannelInboundHandlerAdapter {
	
	private RpcRequest rpcRequest;
	private ClientCallback<T> callback;
	
	public RpcClientInHandler(RpcRequest rpcRequest, ClientCallback<T> callback) {
		this.rpcRequest = rpcRequest;
		this.callback = callback;
	}

	public RpcRequest getRpcRequest() {
		return rpcRequest;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 if (rpcRequest == null) {
			 throw new NullPointerException("rpcRequest is null.");
		 }
		 
		 ctx.writeAndFlush(getRpcRequest());
		 
//		 // @TODO obj -> ByteBuf
//		 ByteBuf objBuf = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(JSON.toJSONString(rpcRequest)), CharsetUtil.UTF_8);
//		 ctx.write(objBuf);  // 直接写入 bytebuf 返回调用者
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		@SuppressWarnings("unchecked")
		T response = (T) msg;
		callback.processResponse(response);
		
	}

//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.close();
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	

}
