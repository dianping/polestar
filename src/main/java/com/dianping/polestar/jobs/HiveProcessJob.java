package com.dianping.polestar.jobs;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

public class HiveProcessJob extends ProcessJob {
	private static final String HADOOP_KILL_COMMAND_PREFIX = "Kill Command = ";
	private static final int HADOOP_KILL_COMMAND_PREFIX_LENGTH = HADOOP_KILL_COMMAND_PREFIX
			.length();

	public HiveProcessJob(JobContext jobContext) {
		super(jobContext);
	}

	@Override
	public void cancel() {
		super.cancel();

		// kill hadoop jobs
		String stderr = jobContext.getStderr().toString();
		String[] lines = StringUtils.split(stderr, Utilities.LINE_SEPARATOR);
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (StringUtils.isBlank(line)) {
				continue;
			} else if (line.startsWith(HADOOP_KILL_COMMAND_PREFIX)) {
				String killCommand = line.substring(
						HADOOP_KILL_COMMAND_PREFIX_LENGTH).trim();
				LOG.info("start to kill hadoop job:" + killCommand);
				try {
					Process proc = Runtime.getRuntime().exec(killCommand);
					proc.waitFor();
				} catch (IOException e) {
					LOG.error(e);
				} catch (InterruptedException ie) {
					LOG.error(ie);
				}
			}
		}
	}
}
