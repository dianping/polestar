package com.dianping.polestar.store.mysql.domain;

import java.util.Date;

public class QueryCancel {
	private String id;
	private String host;

	public QueryCancel() {
	}

	public QueryCancel(String id, String host) {
		this.id = id;
		this.host = host;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
