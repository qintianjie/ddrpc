package com.colorcc.ddrpc.proxy;

import com.colorcc.ddrpc.tools.URL;

public class AbstractServiceProxy<T> implements ServiceProxy<T> {
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
