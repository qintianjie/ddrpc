package com.colorcc.ddrpc.service.netty.handler;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.colorcc.ddrpc.service.netty.pojo.RpcRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 将 String --> RpcRequest
 *
 * @author Qin Tianjie
 * @version Oct 17, 2016 - 1:08:28 AM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved.
 */
public class StringToRpcRequestDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		
		RpcRequest rpcRequest = JSON.parseObject(msg, RpcRequest.class);
		out.add(rpcRequest);

	}

}
