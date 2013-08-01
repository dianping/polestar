package com.dianping.polestar.store.mysql.domain;

import java.util.Date;

import com.dianping.polestar.EnvironmentConstants;
import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;

public class QueryInfo {
	private String sql;
	private String mode;
	private String username;
	private String resultFilePath;
	private long execTime;
	private String addtime;

	public QueryInfo() {
	}

	public QueryInfo(Query q, QueryResult rs, long starttime) {
		setSql(q.getSql());
		setMode(q.getMode());
		setUsername(q.getUsername());
		setResultFilePath(rs.getResultFilePath());
		setExecTime(rs.getExecTime());
		setAddtime(EnvironmentConstants.DATE_FORMAT.format(new Date(starttime)));
	}

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

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "QueryInfo [sql=" + sql + ", mode=" + mode + ", username="
				+ username + ", resultFilePath=" + resultFilePath
				+ ", execTime=" + execTime + ", addtime=" + addtime + "]";
	}
}
