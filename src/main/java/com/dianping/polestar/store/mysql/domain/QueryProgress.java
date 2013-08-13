package com.dianping.polestar.store.mysql.domain;

public class QueryProgress {
	private String id;
	private String progressInfo;

	public QueryProgress() {
	}

	public QueryProgress(String id, String progressInfo) {
		this.id = id;
		this.progressInfo = progressInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProgressInfo() {
		return progressInfo;
	}

	public void setProgressInfo(String progressInfo) {
		this.progressInfo = progressInfo;
	}

	@Override
	public String toString() {
		return "QueryProgress [id=" + id + ", progressInfo=" + progressInfo
				+ "]";
	}
}
