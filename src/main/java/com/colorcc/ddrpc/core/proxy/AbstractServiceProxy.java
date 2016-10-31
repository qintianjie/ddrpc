package com.colorcc.ddrpc.core.proxy;

import com.colorcc.ddrpc.common.tools.URL;

public abstract class AbstractServiceProxy<T> implements ServiceProxy<T> {
	private final Class<T> ifs;
	private final URL url;
	
	public AbstractServiceProxy(Class<T> ifs, URL url) {
		this.ifs = ifs;
		this.url = url;
	}

	@Override
	public Class<T> getInterface() {
		return ifs;
	}

	public URL getUrl() {
		return url;
	}
}
