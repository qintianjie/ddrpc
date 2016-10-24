package com.colorcc.ddrpc.others;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.colorcc.ddrpc.tools.URL;

public class RpcRequest implements Request {

	private String id;
	
	private Class<?> classInterface;

	private String methodName;

	private Class<?>[] parameterTypes;
	private Object[] parameterValues;

	private Map<String, Object> attachments;

	private URL url;

	public RpcRequest() {

	}

	public RpcRequest(URL url, Class<?> classInterface, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
		this.id = UUID.randomUUID().toString();
		this.url = url;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameterValues = parameterValues;
		this.classInterface = classInterface;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Class<?> getClassInterface() {
		return classInterface;
	}

	public void setClassInterface(Class<?> classInterface) {
		this.classInterface = classInterface;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(Object[] parameterValues) {
		this.parameterValues = parameterValues;
	}

	public Map<String, Object> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, Object> attachments) {
		this.attachments = attachments;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String toString() {
        return "RpcRequest [methodName=" + methodName + ", parameterTypes=" + Arrays.toString(parameterTypes)
                + ", parameterValues=" + Arrays.toString(parameterValues);
    }
}