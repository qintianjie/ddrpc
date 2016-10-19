package com.colorcc.ddrpc.transport.netty.callback;

import java.util.concurrent.CountDownLatch;


public class ClientCallbackImpl<T> implements ClientCallback<T> {
	
	private final CountDownLatch latch = new CountDownLatch(1);
	private Object data;
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void callback(T t) {
		// biz process from t to data
		setData(t);
		latch.countDown();
	}
	
	public Object getResult() {
		try {
			latch.await();
		} catch (InterruptedException e) {
		}
		return getData();
	}

	@Override
	public void processResponse(T t) {
		setData(t);
		latch.countDown();
		
	}
	
//	private RpcResponse rpcResponse;
//
//	public RpcResponse getRpcResponse() {
//		return rpcResponse;
//	}
//
//	public void setRpcResponse(RpcResponse rpcResponse) {
//		this.rpcResponse = rpcResponse;
//	}


}
