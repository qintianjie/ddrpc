package com.colorcc.ddrpc.service.proxy;

import java.io.IOException;

import com.colorcc.ddrpc.service.tools.URI;

public interface Client {
	
	public void connect(final URI uri) throws Exception;

    void close() throws IOException;

    public void send(Object obj, URI uri);
    
    

//    public abstract void transceive(Request request, Callback<Response> callback) throws RpcException;
//
//    boolean isClosed();

}
