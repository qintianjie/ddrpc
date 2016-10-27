package com.colorcc.ddrpc.main;

import com.colorcc.ddrpc.transport.netty.NettyServer;

public class ServerMain {

	public static void main(String[] args) {
		NettyServer server = new NettyServer();
		server.start();

	}

}
