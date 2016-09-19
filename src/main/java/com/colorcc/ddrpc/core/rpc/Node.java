package com.colorcc.ddrpc.core.rpc;

public interface Node {
	URL getUrl();

	boolean isAvailable();

	void destroy();
}
