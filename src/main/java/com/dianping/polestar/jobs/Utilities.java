package com.dianping.polestar.jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dianping.polestar.EnvironmentConstants;
import com.dianping.polestar.PolestarException;
import com.dianping.polestar.entity.QueryResult;
import com.dp.cosmos.hadoopKerberosLogin;
import com.google.common.base.Preconditions;

public class Utilities {
	private static final Log LOG = LogFactory.getLog(Utilities.class);

	public static final char LINE_SEPARATOR = '\n';
	public static final char FIELD_SEPARATOR = '\t';
	public static final int COMPRESSION_BUFFER_SIZE = 4096;
	public static final int COMPRESSION_LEVEL = Deflater.BEST_COMPRESSION;

	public static void hadoopKerberosLogin(String username, String passwd,
			String ticketCachePath) throws PolestarException {
		LOG.info("start to kinit, username:" + username
				+ ",ticketcache location:" + ticketCachePath);
		try {
			hadoopKerberosLogin.loginFromPassword(username, passwd,
					ticketCachePath);
		} catch (IOException e) {
			LOG.error("kinit failed " + e);
			throw new PolestarException("kinit login failed, username:"
					+ username + " passwd:" + passwd, e);
		}
	}

	public static OutputStream openOutputStream(File file, boolean append,
			boolean gzip) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (!file.canWrite()) {
				throw new IOException("File '" + file
						+ "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent
							+ "' could not be created");
				}
			}
		}
		if (gzip) {
			return new GZIPOutputStream(new FileOutputStream(file, append),
					COMPRESSION_BUFFER_SIZE) {
				{
					def.setLevel(COMPRESSION_LEVEL);
				};
			};
		}
		return new FileOutputStream(file, append);
	}

	public static void fillInColumnsAndData(String content, QueryResult jobctx) {
		if (!StringUtils.isEmpty(content)) {
			String[] lines = StringUtils.split(content, LINE_SEPARATOR);
			Preconditions.checkArgument(lines.length >= 1,
					"query standout lines should not be under 1, content:"
							+ content);
			String[] columnNames = StringUtils.splitPreserveAllTokens(lines[0],
					FIELD_SEPARATOR);
			jobctx.setColumnNames(columnNames);
			for (int i = 1; i < lines.length; i++) {
				String[] columns = StringUtils.splitPreserveAllTokens(lines[i],
						FIELD_SEPARATOR);
				if (columns.length == columnNames.length) {
					jobctx.getData().add(columns);
				} else {
					LOG.warn("unformatted column data:" + lines[i]
							+ ",discard it");
				}
			}
		}
	}

	public static String genUniqueDataFileName() {
		return genUniqueID() + EnvironmentConstants.DATA_FILE_EXTENSION;
	}

	public static String genUniqueID() {
		return StringUtils.replaceChars(UUID.randomUUID().toString(), "-", "");
	}

	public static String getLastPartFileName(String absolutePath) {
		File file = new File(absolutePath);
		return file.getName();
	}

	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}
}
