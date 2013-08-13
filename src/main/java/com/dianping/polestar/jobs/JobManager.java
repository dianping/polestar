package com.dianping.polestar.jobs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobManager {
	private final static Map<String, Job> idToJob = new ConcurrentHashMap<String, Job>(
			100);
	private final static Map<String, JobContext> idToJobContext = new ConcurrentHashMap<String, JobContext>(
			100);

	public static Job getJobById(String id) {
		return idToJob.get(id);
	}

	public static JobContext getJobContextById(String id) {
		return idToJobContext.get(id);
	}

	public static void putJob(String id, Job job, JobContext jobctx) {
		synchronized (JobManager.class) {
			idToJob.put(id, job);
			idToJobContext.put(id, jobctx);
		}
	}

	public static void removeJob(String id) {
		synchronized (JobManager.class) {
			idToJob.remove(id);
			idToJobContext.remove(id);
		}
	}
}
