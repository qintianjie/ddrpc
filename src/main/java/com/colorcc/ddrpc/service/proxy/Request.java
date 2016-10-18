package com.colorcc.ddrpc.service.proxy;

import java.util.Map;

import com.colorcc.ddrpc.service.tools.URL;

public interface Request {

	public String getId();
	
	public Class<?> getClassInterface();

	public String getMethodName();

	public Class<?>[] getParameterTypes();

	public Object[] getParameterValues();

	public Map<String, Object> getAttachments();

	public URL getUrl();

}
