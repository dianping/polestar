package com.dianping.polestar.service;

import com.dianping.polestar.engine.CommandQueryEngine;
import com.dianping.polestar.engine.IQueryEngine;
import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;
import com.dianping.polestar.entity.QueryStatus;
import com.dianping.polestar.jobs.Job;
import com.dianping.polestar.jobs.JobContext;
import com.dianping.polestar.jobs.JobManager;

public class DefaultQueryService implements IQueryService {

	private static final DefaultQueryService INSTANCE = new DefaultQueryService();

	private IQueryEngine cmdEngine = CommandQueryEngine.getInstance();

	public static IQueryService getInstance() {
		return INSTANCE;
	}

	@Override
	public QueryResult postQuery(Query query) {
		QueryResult queryRes = new QueryResult();
		long startTime = System.currentTimeMillis();
		if ("hive".equalsIgnoreCase(query.getMode()) || "shark".equalsIgnoreCase(query.getMode())) {
			queryRes = cmdEngine.postQuery(query);
			queryRes.setExecTime((System.currentTimeMillis() - startTime) / 1000);
		} else {
			throw new IllegalArgumentException("unsupported query mode:" + query.getMode());
		}
		return queryRes;
	}

	@Override
	public QueryStatus getStatusInfo(String id) {
		QueryStatus status = new QueryStatus();
		status.setId(id);
		JobContext jobctx = JobManager.getJobContextById(id);
		if (jobctx != null) {
			status.setMessage(jobctx.getStderr().toString());
			status.setSuccess(true);
		} else {
			status.setSuccess(false);
		}
		return status;
	}

	@Override
	public Boolean cancel(String id) {
		Job job = JobManager.getJobById(id);
		job.cancel();
		Boolean canceled = job.isCanceled();
		if (canceled) {
			JobManager.removeJob(id);
		}
		return canceled;
	}
}
