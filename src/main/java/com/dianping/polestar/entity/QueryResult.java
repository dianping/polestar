package com.dianping.polestar.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class QueryResult extends Entity {
	private String[] columnNames = new String[]{};
	private List<String[]> data = new ArrayList<String[]>();
	private long execTime = -1L;
	private String errorMsg = "";
	private String resultFilePath = "";
	private Boolean success = false;
	
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public List<String[]> getData() {
		return data;
	}
	public void setData(List<String[]> data) {
		this.data = data;
	}
	public long getExecTime() {
		return execTime;
	}
	public void setExecTime(long execTime) {
		this.execTime = execTime;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getResultFilePath() {
		return resultFilePath;
	}
	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	@Override
	public String toString() {
		return "QueryResult [columnNames=" + Arrays.toString(columnNames)
				+ ", data=" + data + ", execTime=" + execTime + ", errorMsg="
				+ errorMsg + ", resultFilePath=" + resultFilePath
				+ ", success=" + success + "]";
	}
}
