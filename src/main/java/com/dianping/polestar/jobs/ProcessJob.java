package com.dianping.polestar.jobs;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dianping.polestar.EnvironmentConstants;

public class ProcessJob extends AbstractJob {
	public final static Log LOG = LogFactory.getLog(ProcessJob.class);

	private final static String KRB5CCNAME = "KRB5CCNAME";
	private final static String TICKET_CACHE_EXTENTION = ".ticketcache";

	protected final Map<String, String> envMap;
	protected volatile Boolean killedWhenExceedMaxLines = false;
	protected volatile Process process;

	public ProcessJob(JobContext jobContext) {
		super(jobContext);
		envMap = new HashMap<String, String>(System.getenv());
	}

	@Override
	public Integer run() throws Exception {
		int exitCode = -999;
		File workDir = new File(jobContext.getWorkDir());
		if (!workDir.exists()) {
			workDir.mkdirs();
		}
		final File dataFile = new File(workDir,
				Utilities.generateUniqueDataFileName());
		dataFile.createNewFile();
		jobContext.setLocalDataPath(dataFile.getAbsolutePath());

		String ticketCachePath = workDir.getAbsolutePath() + File.separator
				+ jobContext.getUsername() + TICKET_CACHE_EXTENTION;
		Utilities.hadoopKerberosLogin(jobContext.getUsername(),
				jobContext.getPasswd(), ticketCachePath);
		jobContext.getProperties().setProperty(KRB5CCNAME, ticketCachePath);

		setEnvironmentVariables();

		final Boolean storeResult = jobContext.isStoreResult();
		final int resultLimit = storeResult ? EnvironmentConstants.MAX_RESULT_DATA_NUMBER
				: EnvironmentConstants.DEFAULT_RESULT_DATA_NUMBER;

		ProcessBuilder builder = new ProcessBuilder(jobContext.getCommands());
		builder.directory(workDir);
		builder.environment().putAll(envMap);
		process = builder.start();

		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();
		String threadName = "job-" + jobContext.getId();
		Thread outThread = new Thread(threadName + "-stdout") {

			@Override
			public void run() {
				OutputStream os = null;
				BufferedOutputStream bos = null;
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					if (storeResult) {
						os = Utilities.openOutputStream(dataFile, false, true);
						bos = new BufferedOutputStream(os);
					}
					int currLineNum = 0;
					String line = reader.readLine();
					while (line != null && currLineNum++ <= resultLimit
							&& !isInterrupted()) {
						if (storeResult) {
							bos.write(line.getBytes());
							bos.write(Utilities.LINE_SEPARATOR);
						} else {
							jobContext.getStdout().append(line)
									.append(Utilities.LINE_SEPARATOR);
						}
						line = reader.readLine();
					}

					if (currLineNum > resultLimit) {
						LOG.info("data result lines limit exceed max value:"
								+ resultLimit
								+ ",start to destroy query process, id:"
								+ jobContext.getId());
						killedWhenExceedMaxLines = true;
						process.destroy();
					}
				} catch (Exception e) {
					LOG.error(e);
				} finally {
					try {
						if (bos != null) {
							bos.flush();
							bos.close();
						}
						IOUtils.closeQuietly(os);
						IOUtils.closeQuietly(inputStream);
					} catch (IOException ex) {
					}
				}
			}
		};

		Thread errThread = new Thread(threadName + "-stderr") {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(errorStream));
					String line = reader.readLine();
					while (line != null && !isInterrupted()) {
						jobContext.getStderr().append(line)
								.append(Utilities.LINE_SEPARATOR);
						line = reader.readLine();
					}
				} catch (IOException e) {
					LOG.warn("Error reading the error stream", e);
				} finally {
					IOUtils.closeQuietly(errorStream);
				}
			}
		};

		try {
			outThread.start();
			errThread.start();
		} catch (IllegalStateException ise) {
		}

		try {
			LOG.info("start to execute query commands:"
					+ StringUtils.join(jobContext.getCommands(), ' ')
					+ " ,username:" + jobContext.getUsername() + " ,query-id:"
					+ jobContext.getId());

			exitCode = process.waitFor();
			try {
				outThread.join();
				errThread.join();
			} catch (InterruptedException ie) {
				LOG.warn("Interrupted while reading the stdout/stderr stream", ie);
			}
			// force return exitcode 0 when query was killed because of
			// exceedance of max line number
			if (killedWhenExceedMaxLines) {
				exitCode = 0;
			}
		} catch (InterruptedException e) {
			LOG.error(e);
		} finally {
			process = null;
		}
		return exitCode;
	}

	@Override
	public void cancel() {
		process.destroy();
		process = null;
		canceled = true;
		LOG.info("query was canceled, id:" + jobContext.getId());
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	private void setEnvironmentVariables() {
		// set environment variables
		for (Object key : jobContext.getProperties().keySet()) {
			envMap.put((String) key,
					(String) jobContext.getProperties().get(key));
		}
	}

}
