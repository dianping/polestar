package com.dianping.polestar.entity;

public class QueryStatus extends Entity {
	private String message;
	private Boolean success;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	@Override
	public String toString() {
		return "QueryStatus [message=" + message + ", success=" + success + "]";
	}

}
