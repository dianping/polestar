package com.dianping.polestar;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.dianping.polestar.lang.ClassUtils;

public final class EnvironmentConstants {
	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static String DATA_FILE_EXTENSION = ".gz";
	public static String WORKING_DIRECTORY_ROOT = "/tmp";
	public static int DEFAULT_RESULT_DATA_NUMBER = 500;
	public static int MAX_RESULT_DATA_NUMBER = 1000000;
	public static String HDFS_DATA_ROOT_PATH = "hdfs://10.1.77.86/data/polestar";

	public static String HADOOP_PRINCIPAL = "hadoop@DIANPING.COM";
	public static String HADOOP_KEYTAB_FILE = "/home/hadoop/.keytab";

	static {
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
