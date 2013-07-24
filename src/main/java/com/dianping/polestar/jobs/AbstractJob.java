package com.dianping.polestar.jobs;

public abstract class AbstractJob implements Job {

	protected volatile JobContext jobContext;

	protected volatile boolean canceled = false;

	public AbstractJob(JobContext jobContext) {
		this.jobContext = jobContext;
	}

	@Override
	public Integer run() throws Exception {
		return 0;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}
}
