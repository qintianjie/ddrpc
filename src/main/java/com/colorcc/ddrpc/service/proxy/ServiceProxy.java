package com.colorcc.ddrpc.service.proxy;

import com.colorcc.ddrpc.service.tools.URI;

public interface ServiceProxy<T> {
	
	Class<T> getInterface();
	
	URI getUri();

}
