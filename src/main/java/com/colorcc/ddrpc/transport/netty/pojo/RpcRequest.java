package com.colorcc.ddrpc.transport.netty.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.colorcc.ddrpc.pojo.MethodMeta;

public class RpcRequest implements Serializable {
	private static final long serialVersionUID = -8441351841109201058L;
	private String id;
	private MethodMeta methodMeta;
	private Object[] paramValues;
	private Map<String, String>  attachments;

	public RpcRequest() {

	} 

	public RpcRequest(MethodMeta methodMeta, Object[] paramValues, Map<String, String>  attachments) {
		this.id = UUID.randomUUID().toString();
		this.methodMeta = methodMeta;
		this.paramValues = paramValues;
		this.attachments = attachments;
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

	public Map<String, String> getAttachments() {
		if (attachments == null) {
			attachments = new HashMap<>();
		}
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		if (attachments == null) {
			this.attachments = new HashMap<> ();
		} else {
			this.attachments = attachments;
		}
	}
	
	public String getAttachmentItem(String key) {
		if (this.attachments == null) {
			return null;
		} else {
			return this.attachments.get(key);
		}
	}
	
	public void setAttachmentItem(String key, String value) {
		if (this.attachments == null) {
			this.attachments = new HashMap<>();
		}
		this.getAttachments().put(key, value);
	} 
	
	

}
