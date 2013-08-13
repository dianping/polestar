package com.dianping.polestar;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dianping.polestar.lang.ClassUtils;

public final class EnvironmentConstants {
	public final static Log LOG = LogFactory.getLog(EnvironmentConstants.class);

	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static String DATA_FILE_EXTENSION = ".gz";
	public static String WORKING_DIRECTORY_ROOT = "/tmp";
	public static int DEFAULT_RESULT_DATA_NUMBER = 500;
	public static int MAX_RESULT_DATA_NUMBER = 1000000;
	// result data will be stored in this location
	public static String HDFS_DATA_ROOT_PATH = "hdfs://10.1.77.86/data/polestar";

	// hadoop security
	public static String HADOOP_PRINCIPAL = "hadoop@DIANPING.COM";
	public static String HADOOP_KEYTAB_FILE = "/home/hadoop/.keytab";

	public static String LOCAL_HOST_ADDRESS;

	static {
		try {
			LOCAL_HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
			LOG.info("local host address: " + LOCAL_HOST_ADDRESS);
		} catch (UnknownHostException uhe) {
			LOG.error("get local host address failed, " + uhe);
		}

		InputStream is = ClassUtils.getResourceAsStream("polestar.properties");
		Properties pros = new Properties();
		try {
			pros.load(is);

			DATA_FILE_EXTENSION = pros.getProperty("DATA_FILE_EXTENSION",
					DATA_FILE_EXTENSION);

			WORKING_DIRECTORY_ROOT = pros.getProperty("WORKING_DIRECTORY_ROOT",
					WORKING_DIRECTORY_ROOT);

			DEFAULT_RESULT_DATA_NUMBER = Integer.valueOf(pros.getProperty(
					"DEFAULT_RESULT_DATA_NUMBER",
					String.valueOf(DEFAULT_RESULT_DATA_NUMBER)));

			MAX_RESULT_DATA_NUMBER = Integer.valueOf(pros.getProperty(
					"MAX_RESULT_DATA_NUMBER",
					String.valueOf(MAX_RESULT_DATA_NUMBER)));

			HDFS_DATA_ROOT_PATH = pros.getProperty("HDFS_DATA_ROOT_PATH",
					HDFS_DATA_ROOT_PATH);

			HADOOP_PRINCIPAL = pros.getProperty(
					pros.getProperty("HADOOP_PRINCIPAL"), HADOOP_PRINCIPAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
