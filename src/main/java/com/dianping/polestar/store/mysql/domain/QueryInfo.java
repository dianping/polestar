package com.dianping.polestar.store.mysql.domain;

import java.util.Date;

public class QueryInfo {
	private String sql;
	private String mode;
	private String username;
	private String resultFilePath;
	private long execTime;
	private Date addtime;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getResultFilePath() {
		return resultFilePath;
	}
	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}
	public long getExecTime() {
		return execTime;
	}
	public void setExecTime(long execTime) {
		this.execTime = execTime;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	@Override
	public String toString() {
		return "QueryInfo [sql=" + sql + ", mode=" + mode + ", username="
				+ username + ", resultFilePath=" + resultFilePath
				+ ", execTime=" + execTime + ", addtime=" + addtime + "]";
	}
}
