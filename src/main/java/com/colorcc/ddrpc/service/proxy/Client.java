package com.colorcc.ddrpc.service.proxy;

import java.io.IOException;

import com.colorcc.ddrpc.service.tools.URI;

public interface Client {
	
	void connect(URI uri) throws Exception;

    void close() throws IOException;

    URI getUri();
    
    public void send();
    
    

//    public abstract void transceive(Request request, Callback<Response> callback) throws RpcException;
//
//    boolean isClosed();

}
