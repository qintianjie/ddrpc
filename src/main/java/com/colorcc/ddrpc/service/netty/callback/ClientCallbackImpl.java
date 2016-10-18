package com.colorcc.ddrpc.service.netty.callback;


public class ClientCallbackImpl<T> implements ClientCallback<T> {
	
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
	}
	
	public Object getResult() {
		return getData();
	}

	@Override
	public void processResponse(T t) {
		setData(t);
		
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
