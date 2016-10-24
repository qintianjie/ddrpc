package com.colorcc.ddrpc.transport.netty.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RpcResponse implements Serializable {
	private static final long serialVersionUID = 7183970964234737098L;
	private String id;
	private Object data;
	
	private Map<String, String>  attachments;
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
