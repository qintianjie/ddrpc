package com.colorcc.ddrpc.transport.netty.sample;

import com.colorcc.ddrpc.transport.netty.NettyServer;

public class ServerMain {

	public static void main(String[] args) {
		NettyServer server = new NettyServer();
		server.start();

	}

}
