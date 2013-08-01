package com.dianping.polestar.rest;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

public class SimpleWebException extends Throwable {
	private static final long serialVersionUID = 1L;

	public int httpCode;
	public Map<String, Object> params;

	public SimpleWebException(int httpCode, String msg) {
		super(msg);
		this.httpCode = httpCode;
	}

	public SimpleWebException(int httpCode, String msg,
			Map<String, Object> params) {
		super(msg);
		this.httpCode = httpCode;
		this.params = params;
	}

	public Response getResponse() {
		return buildMessage(httpCode, params, getMessage());
	}

	public static Response buildMessage(int httpCode,
			Map<String, Object> params, String msg) {
		HashMap<String, Object> err = new HashMap<String, Object>();
		err.put("error", msg);
		if (params != null)
			err.putAll(params);

		String json = "\"error\"";
		try {
			json = new ObjectMapper().writeValueAsString(err);
		} catch (IOException e) {
		}

		return Response.status(httpCode).entity(json)
				.type(MediaType.APPLICATION_JSON).build();
	}
}
