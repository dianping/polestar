package com.dianping.polestar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.dianping.polestar.lang.ClassUtils;

public final class EnvironmentConstants {
	public static String DATA_FILE_EXTENSION = ".gz";
	public static String WORKING_DIRECTORY_ROOT = "/tmp";
	public static int DEFAULT_RESULT_DATA_NUMBER = 500;
	public static int MAX_RESULT_DATA_NUMBER = 1000000;
	public static String HDFS_DATA_ROOT_PATH = "hdfs://10.1.77.86/data/polestar";

	static {
		InputStream is = ClassUtils.getResourceAsStream("polestar.properties");
		Properties pros = new Properties();
		try {
			pros.load(is);
			if (pros.containsKey("DATA_FILE_EXTENSION")) {
				DATA_FILE_EXTENSION = pros.getProperty("DATA_FILE_EXTENSION");
			}
			if (pros.containsKey("WORKING_DIRECTORY_ROOT")) {
				WORKING_DIRECTORY_ROOT = pros
						.getProperty("WORKING_DIRECTORY_ROOT");
			}
			if (pros.containsKey("DEFAULT_RESULT_DATA_NUMBER")) {
				DEFAULT_RESULT_DATA_NUMBER = Integer.valueOf(pros
						.getProperty("DEFAULT_RESULT_DATA_NUMBER"));
			}
			if (pros.containsKey("MAX_RESULT_DATA_NUMBER")) {
				MAX_RESULT_DATA_NUMBER = Integer.valueOf(pros
						.getProperty("MAX_RESULT_DATA_NUMBER"));
			}
			if (pros.containsKey("HDFS_DATA_ROOT_PATH")) {
				HDFS_DATA_ROOT_PATH = pros.getProperty("HDFS_DATA_ROOT_PATH");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
