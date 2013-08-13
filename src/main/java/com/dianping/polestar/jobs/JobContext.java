package com.dianping.polestar.jobs;

import java.util.Properties;

public final class JobContext {
	private String workDir;
	private String localDataPath;
	private Properties properties = new Properties();
	private String[] commands;
	private String username;
	private String passwd;
	private boolean storeResult;
	private String id;
	private int exitCode;
	private boolean isDone = false;
	private StringBuffer stderr = new StringBuffer(2000);
	private StringBuffer stdout = new StringBuffer(2000);

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean isStoreResult() {
		return storeResult;
	}

	public void setStoreResult(boolean storeResult) {
		this.storeResult = storeResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getExitCode() {
		return exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	public String getLocalDataPath() {
		return localDataPath;
	}

	public void setLocalDataPath(String localDataPath) {
		this.localDataPath = localDataPath;
	}

	public StringBuffer getStderr() {
		return stderr;
	}

	public void setStderr(StringBuffer stderr) {
		this.stderr = stderr;
	}

	public StringBuffer getStdout() {
		return stdout;
	}

	public void setStdout(StringBuffer stdout) {
		this.stdout = stdout;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone() {
		isDone = true;
	}
}
