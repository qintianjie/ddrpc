package com.colorcc.ddrpc.others;

import java.io.IOException;

import com.colorcc.ddrpc.tools.URL;

public interface Client {
	
	public void connect(final URL url) throws Exception;

    void close() throws IOException;

    public void send(Object obj, URL url);
    
    

//    public abstract void transceive(Request request, Callback<Response> callback) throws RpcException;
//
//    boolean isClosed();

}