package com.colorcc.ddrpc.service.proxy;

import com.colorcc.ddrpc.service.tools.URL;

public interface ServiceProxy<T> {
	
	Class<T> getInterface();
	
	URL getUrl();

}
