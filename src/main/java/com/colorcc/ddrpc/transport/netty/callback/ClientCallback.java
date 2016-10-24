package com.colorcc.ddrpc.transport.netty.callback;


public interface ClientCallback<T> { 
	public ClientCallback<T> filter(T respone);
	
	public void processResponse(T response);
	
	public T getResult();

}
