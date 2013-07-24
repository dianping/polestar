package com.dianping.polestar.jobs;

public final class JobAdapter {

	public static Job createJob(String mode, JobContext jobCtx) {
		Job job = null;
		if ("hive".equalsIgnoreCase(mode)) {
			job = new HiveProcessJob(jobCtx);
		} else if ("shark".equalsIgnoreCase(mode)) {
			job = new ProcessJob(jobCtx);
		}
		return job;
	}
}
