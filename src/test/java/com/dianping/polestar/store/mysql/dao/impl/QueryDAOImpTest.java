package com.dianping.polestar.store.mysql.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.httpclient.ConnectMethod;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dianping.polestar.store.mysql.dao.QueryDAO;
import com.dianping.polestar.store.mysql.domain.QueryInfo;

public class QueryDAOImpTest {
	static QueryDAO queryDAOImp;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AbstractApplicationContext cxt = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		queryDAOImp = (QueryDAO) cxt.getBean("queryDaoImpl");
	}

	@Test
	public void tttt() {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			Connection conn = DriverManager
//					.getConnection(
//							"jdbc:mysql://192.168.7.80:3306/polestar?useUnicode=true&characterEncoding=UTF-8",
//							"root", "root");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Test
	public void testInsertQueryInfo() {
		QueryInfo q = new QueryInfo();
		q.setUsername("xxx");
		q.setMode("hive");
		q.setResultFilePath("hdfs:////test");
		q.setSql("show tables;");
		q.setExecTime(600L);
		//q.setAddtime(new Date());
		queryDAOImp.insert(q);
	}

}
