package com.dianping.polestar.store;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;

import com.dianping.polestar.EnvironmentConstants;
import com.dianping.polestar.PolestarException;

public class HDFSManager {
	private static Log LOG = LogFactory.getLog(HDFSManager.class);

	private static FileSystem fs = null;

	private static FileSystem getFs() {
		if (fs == null) {
			try {
				fs = FileSystem.get(getDefaultConfiguration());
			} catch (IOException e) {
				LOG.error("filesystem is not initialized correctly!" + e);
				e.printStackTrace();
			}
		}
		return fs;
	}

	public static void putFileToHDFS(String src, String dest) {
		try {
			getFs().copyFromLocalFile(false, true, new Path(src),
					new Path(dest));
			LOG.info("put file " + src + " to " + dest + " succeed !");
		} catch (IOException e) {
			LOG.error("put file " + src + " to " + dest + " failed !", e);
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

			try {
				// 设置服务申请者的Principle
				conf.set("hadoop.principal",
						EnvironmentConstants.HADOOP_PRINCIPAL);
				// 设置keytab file的路径
				conf.set("hadoop.keytab.file",
						EnvironmentConstants.HADOOP_KEYTAB_FILE);
				// Kerberos Authentication
				UserGroupInformation.setConfiguration(conf);
				SecurityUtil.login(conf, "hadoop.keytab.file",
						"hadoop.principal");
			} catch (IOException e) {
				LOG.error(e);
				e.printStackTrace();
			}
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

	public static String getHiveHome() {
		return System.getenv("HIVE_HOME");
	}

	public static InputStream openFSDataInputStream(String absolutePath) {
		Path p = new Path(absolutePath);
		try {
			if (!getFs().exists(p)) {
				throw new PolestarException(p.toString() + " doesn't exist !");
			}
			if (!getFs().isFile(p)) {
				throw new PolestarException(p.toString() + " is not a file !");
			}
			return getFs().open(p);
		} catch (IOException e) {
			throw new PolestarException(e);
		}
	}
}
