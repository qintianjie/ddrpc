package com.colorcc.ddrpc.others;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

import com.colorcc.ddrpc.pojo.MethodCache;

/**
 * msg to object
 *
 * @author Qin Tianjie
 * @version Oct 12, 2016 - 1:21:14 AM
 * Copyright (c) 2016, tianjieqin@126.com All Rights Reserved. 
 * @param <T>
 */
public class ServerChannelHandler<T> extends ChannelInboundHandlerAdapter {
	
	/**
	 * RPC传过来的反序列化对象 
	 */
	private Request request; 
	
	/**
	 * 服务端 method.invoke 得到的结果
	 */
	private Response result;  
	
	/**
	 * service type
	 */
	private Class<T> classType;
	
	/**
	 * service 的具体实现
	 */
	private T impl;
	
	public ServerChannelHandler( Class<T> classType, T impl) {
		this.classType = classType;
		this.impl = impl;
	}
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		this.setRequest((Request) msg);
		
		Method method = MethodCache.getMethod(this.getClassType(), this.getRequest().getMethodName());
		Response response = new Response();
		try {
			Object returnValue = method.invoke(getImpl(), request.getParameterValues());
			response.setResult(returnValue);
		} catch (Exception e) {
			response.setResult(null);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(result);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Response result) {
		this.result = result;
	}

	public Class<T> getClassType() {
		return classType;
	}

	public void setClassType(Class<T> classType) {
		this.classType = classType;
	}

	public T getImpl() {
		return impl;
	}

	public void setImpl(T impl) {
		this.impl = impl;
	}
	
	

}
