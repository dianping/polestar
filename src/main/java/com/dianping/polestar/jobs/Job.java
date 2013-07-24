package com.dianping.polestar.jobs;

public interface Job {

	Integer run() throws Exception;
	
	void cancel();

	boolean isCanceled();

}
