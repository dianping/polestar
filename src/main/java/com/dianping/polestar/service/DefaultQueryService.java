package com.dianping.polestar.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.dianping.polestar.jobs.Utilities;
import com.dianping.polestar.rest.BadParamException;
import com.dianping.polestar.store.HDFSManager;
import com.dianping.polestar.store.mysql.dao.QueryDAO;
import com.dianping.polestar.store.mysql.dao.impl.QueryDAOFactory;
import com.dianping.polestar.store.mysql.domain.QueryCancel;
import com.dianping.polestar.store.mysql.domain.QueryInfo;
import com.dianping.polestar.store.mysql.domain.QueryProgress;

public class DefaultQueryService implements IQueryService {
	private static final Log LOG = LogFactory.getLog(DefaultQueryService.class);
	private static final int EOF = -1;

	private static final DefaultQueryService INSTANCE = new DefaultQueryService();

	private IQueryEngine cmdEngine = CommandQueryEngine.getInstance();
	private QueryDAO queryDao = QueryDAOFactory.getInstance();

	public static IQueryService getInstance() {
		return INSTANCE;
	}

	@Override
	public QueryResult postQuery(Query query) throws BadParamException {
		QueryResult queryRes = new QueryResult();
		long startTime = Utilities.getCurrentTime();
		if ("hive".equalsIgnoreCase(query.getMode())
				|| "shark".equalsIgnoreCase(query.getMode())) {
			queryRes = cmdEngine.postQuery(query);
			queryRes.setExecTime((Utilities.getCurrentTime() - startTime) / 1000);
			if (queryRes.getSuccess()) {
				queryDao.insertQueryInfo(new QueryInfo(query, queryRes,
						startTime));
			}
		} else {
			throw new BadParamException("unsupported query mode:"
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
			QueryProgress qp = queryDao.findQueryProgressById(id);
			if (qp != null) {
				status.setSuccess(true);
				status.setMessage(qp.getProgressInfo());
			} else {
				status.setSuccess(false);
			}
		}
		return status;
	}

	@Override
	public Boolean cancel(String id) throws BadParamException {
		Boolean canceled = false;
		Job job = JobManager.getJobById(id);
		if (job == null) {
			queryDao.insertQueryCancel(new QueryCancel(id,
					EnvironmentConstants.LOCAL_HOST_ADDRESS));
			LOG.info("insert into querycancel to db, id: " + id + " host: "
					+ EnvironmentConstants.LOCAL_HOST_ADDRESS);
			canceled = true;
		} else {
			job.cancel();
			canceled = job.isCanceled();
			if (canceled) {
				JobManager.removeJob(id);
			} else {
				throw new BadParamException("job " + id + " cancelled failed !");
			}
		}
		return canceled;
	}

	@Override
	public StreamingOutput getDataFile(String filename)
			throws BadParamException {
		final String absolutePath = EnvironmentConstants.HDFS_DATA_ROOT_PATH
				+ File.separator + filename;
		final InputStream is = HDFSManager.openFSDataInputStream(absolutePath);
		StreamingOutput stream = new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				try {
					long count = 0;
					int read = 0;
					byte[] bytes = new byte[1024 * 4];
					while (EOF != (read = is.read(bytes))) {
						output.write(bytes, 0, read);
						count += read;
					}
					LOG.info("totally read " + count
							+ " bytes for download file: " + absolutePath);
				} catch (IOException e) {
					throw new PolestarException("Read File: " + absolutePath
							+ " failed! ", e);
				}
			}
		};
		return stream;
	}
}
