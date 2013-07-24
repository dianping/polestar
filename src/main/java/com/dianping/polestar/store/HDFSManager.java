package com.dianping.polestar.store;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.dianping.polestar.PolestarException;

public class HDFSManager {
	private static Log LOG = LogFactory.getLog(HDFSManager.class);

	private static FileSystem fs;

	static {
		try {
			fs = FileSystem.get(getDefaultConfiguration());
		} catch (IOException e) {
			LOG.error("Create FileSystem Error !" + e);
			e.printStackTrace();
		}
	}

	public static void putFileToHDFS(String src, String dest) {
		if (fs != null) {
			try {
				fs.copyFromLocalFile(false, true, new Path(src), new Path(dest));
				LOG.info("copy file src:" + src + " to dest:" + dest
						+ " succeed !");
			} catch (IOException e) {
				LOG.error("copy file src:" + src + " to dest:" + dest
						+ " failed !", e);
			}
		} else {
			throw new PolestarException("filesystem is not initialized!");
		}
	}

	public static Configuration getDefaultConfiguration() {
		Configuration conf = null;
		File coreSite = new File(getHadoopConfDir(), "core-site.xml");
		File hdfsSite = new File(getHadoopConfDir(), "hdfs-site.xml");
		if (coreSite.exists() && hdfsSite.exists()) {
			conf = new Configuration(true);
			conf.addResource(new Path(coreSite.getAbsolutePath()));
			conf.addResource(new Path(hdfsSite.getAbsolutePath()));
		} else {
			LOG.error("core-site.xml or hdfs-site.xml is not found in the system environment!");
		}
		return conf;
	}

	public static String getHadoopConfDir() {
		String confDir = System.getenv("HADOOP_CONF_DIR");
		if (StringUtils.isBlank(confDir)) {
			confDir = getHadoopHome() + File.separator + "conf";
		}
		return confDir;
	}

	public static String getHadoopHome() {
		return System.getenv("HADOOP_HOME");
	}
}
