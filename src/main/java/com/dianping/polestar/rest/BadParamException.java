package com.dianping.polestar.rest;

import javax.ws.rs.core.Response.Status;

public class BadParamException extends SimpleWebException {
	private static final long serialVersionUID = 6788167073159708825L;

	public BadParamException(String msg) {
		super(Status.BAD_REQUEST.getStatusCode(), msg);
	}
}
