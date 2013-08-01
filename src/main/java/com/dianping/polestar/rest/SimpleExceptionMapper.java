package com.dianping.polestar.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SimpleExceptionMapper implements
		ExceptionMapper<SimpleWebException> {

	@Override
	public Response toResponse(SimpleWebException exception) {
		return exception.getResponse();
	}
}
