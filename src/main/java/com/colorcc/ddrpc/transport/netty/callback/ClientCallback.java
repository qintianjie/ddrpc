package com.colorcc.ddrpc.transport.netty.callback;

public interface ClientCallback<T> {
	
	public void callback(T t);
	
	public void processResponse(T t);
	
	public Object getResult();

}
