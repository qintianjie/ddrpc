package com.colorcc.ddrpc.transport.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}
			
			System.out.println("timeout .... kakakka");
			ctx.channel().close();
			ctx.close();

//			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
//					ChannelFutureListener.CLOSE_ON_FAILURE);
 
			System.out.println( ctx.channel().remoteAddress()+"超时类型：" + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
