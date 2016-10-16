package com.colorcc.ddrpc.service.netty.pojo;

import java.io.Serializable;
import java.util.UUID;

public class RpcRequest implements Serializable {
	private static final long serialVersionUID = -8441351841109201058L;
	private String id;
	private MethodMeta methodMeta;
	private Object[] paramValues;

	public RpcRequest() {

	}

	public RpcRequest(MethodMeta methodMeta, Object[] paramValues) {
		this.id = UUID.randomUUID().toString();
		this.methodMeta = methodMeta;
		this.paramValues = paramValues;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MethodMeta getMethodMeta() {
		return methodMeta;
	}

	public void setMethodMeta(MethodMeta methodMeta) {
		this.methodMeta = methodMeta;
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}

}
