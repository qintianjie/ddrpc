package com.colorcc.ddrpc.proxy;

import com.colorcc.ddrpc.tools.URL;

public interface ServiceProxy<T> {
	
	Class<T> getInterface();
	
	URL getUrl();

}
