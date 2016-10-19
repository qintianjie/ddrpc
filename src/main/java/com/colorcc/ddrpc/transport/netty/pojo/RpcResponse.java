package com.colorcc.ddrpc.transport.netty.pojo;

import java.io.Serializable;

public class RpcResponse implements Serializable {
	private static final long serialVersionUID = 7183970964234737098L;
	private String id;
	private Object data;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

}
