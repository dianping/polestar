package com.dianping.polestar.engine;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dianping.polestar.EnvironmentConstants;
import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;
import com.dianping.polestar.jobs.Job;
import com.dianping.polestar.jobs.JobAdapter;
import com.dianping.polestar.jobs.JobContext;
import com.dianping.polestar.jobs.JobManager;
import com.dianping.polestar.jobs.Utilities;
import com.dianping.polestar.store.HDFSManager;

public class CommandQueryEngine implements IQueryEngine {
	public static final Log LOG = LogFactory.getLog(CommandQueryEngine.class);

	private static final String HIVE_COMMAND_FORMAT = "set hive.cli.print.header=true;use %s;%s;";
	private static final IQueryEngine INSTANCE = new CommandQueryEngine();

	public static IQueryEngine getInstance() {
		return INSTANCE;
	}

	@Override
	public QueryResult postQuery(Query query) {
		JobContext jobCtx = new JobContext();
		jobCtx.setId(query.getId());
		jobCtx.setUsername(query.getUsername());
		jobCtx.setPasswd(query.getPassword());
		jobCtx.setStoreResult(query.isStoreResult());
		jobCtx.setCommands(new String[] {
				getCommandByMode(query.getMode()),
				"-e",
				String.format(HIVE_COMMAND_FORMAT, query.getDatabase(),
						query.getSql()) });
		jobCtx.setWorkDir(EnvironmentConstants.WORKING_DIRECTORY_ROOT
				+ File.separator + query.getId());

		Job job = JobAdapter.createJob(query.getMode(), jobCtx);
		JobManager.putJob(query.getId(), job, jobCtx);

		QueryResult queryRes = new QueryResult();
		queryRes.setId(query.getId());
		try {
			int exitCode = job.run();
			LOG.info("exitcode:" + exitCode + " ,id:" + jobCtx.getId());
			if (0 == exitCode) {
				queryRes.setSuccess(true);
				if (jobCtx.isStoreResult()) {
					String hdfsDataFileAbsolutePath = EnvironmentConstants.HDFS_DATA_ROOT_PATH
							+ File.separator
							+ Utilities.getLastPartFileName(jobCtx
									.getLocalDataPath());
					HDFSManager.putFileToHDFS(jobCtx.getLocalDataPath(),
							hdfsDataFileAbsolutePath);
					queryRes.setResultFilePath(hdfsDataFileAbsolutePath);
				} else {
					Utilities.fillInColumnsAndData(jobCtx.getStdout()
							.toString(), queryRes);
				}
			} else {
				queryRes.setErrorMsg(jobCtx.getStderr().toString());
			}
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			FileUtils.deleteQuietly(new File(jobCtx.getWorkDir()));
			JobManager.removeJob(query.getId());
		}
		return queryRes;
	}

	public static String getCommandByMode(String mode) {
		if ("hive".equalsIgnoreCase(mode)) {
			return "hive";
		} else if ("shark".equalsIgnoreCase(mode)) {
			return "shark-witherror";
		}
		return mode;
	}
}
