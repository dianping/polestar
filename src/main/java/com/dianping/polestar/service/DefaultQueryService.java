package com.dianping.polestar.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import com.dianping.polestar.EnvironmentConstants;
import com.dianping.polestar.PolestarException;
import com.dianping.polestar.engine.CommandQueryEngine;
import com.dianping.polestar.engine.IQueryEngine;
import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;
import com.dianping.polestar.entity.QueryStatus;
import com.dianping.polestar.jobs.Job;
import com.dianping.polestar.jobs.JobContext;
import com.dianping.polestar.jobs.JobManager;
import com.dianping.polestar.store.HDFSManager;

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
		if ("hive".equalsIgnoreCase(query.getMode())
				|| "shark".equalsIgnoreCase(query.getMode())) {
			queryRes = cmdEngine.postQuery(query);
			queryRes.setExecTime((System.currentTimeMillis() - startTime) / 1000);
		} else {
			throw new IllegalArgumentException("unsupported query mode:"
					+ query.getMode());
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
		Boolean canceled = false;
		Job job = JobManager.getJobById(id);
		if (job != null) {
			job.cancel();
			canceled = job.isCanceled();
			if (canceled) {
				JobManager.removeJob(id);
			}
		}
		return canceled;
	}

	@Override
	public StreamingOutput getDataFile(String filename) {
		final InputStream is = HDFSManager
				.openFSDataInputStream(EnvironmentConstants.HDFS_DATA_ROOT_PATH
						+ File.separator + filename);
		StreamingOutput stream = null;
		stream = new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				try {
					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = is.read(bytes)) != -1) {
						output.write(bytes, 0, read);
					}
				} catch (IOException e) {
					throw new PolestarException(e);
				}
			}
		};
		return stream;
	}
}
