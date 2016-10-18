package com.colorcc.ddrpc.core.test.netty.pojo;

import java.io.Serializable;

public class RpcRequest implements Serializable {
	private static final long serialVersionUID = -2120242606253544407L;
	private String id;
	private MethodMeta methodMeta;

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

}
