package com.colorcc.ddrpc.service.proxy;

import com.colorcc.ddrpc.service.tools.URI;

public class AbstractServiceProxy<T> implements ServiceProxy<T> {
	private final Class<T> ifs;
	private final URI uri;
	
	public AbstractServiceProxy(Class<T> ifs, URI uri) {
		this.ifs = ifs;
		this.uri = uri;
	}

	@Override
	public Class<T> getInterface() {
		return ifs;
	}

	@Override
	public URI getUri() {
		return uri;
	}

}
