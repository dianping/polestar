package com.dianping.polestar.store.mysql.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
		// try {
		// Class.forName("com.mysql.jdbc.Driver");
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// Connection conn = DriverManager
		// .getConnection(
		// "jdbc:mysql://192.168.7.80:3306/polestar?useUnicode=true&characterEncoding=UTF-8",
		// "root", "root");
		// Statement st = conn.createStatement();
		// String sql =
		// "INSERT INTO QueryInfo (username, SQL, MODE, ADDTIME, exectime, 	path) VALUES('yukang.chen', 'select *', 'hive', NOW(), '542451', '/user/local')";
		// System.out.println(st.execute(sql));
		//
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Test
	public void testInsertQueryInfo() {
		QueryInfo q = new QueryInfo();
		Date addtime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		q.setUsername("xxx");
		q.setMode("hive");
		q.setResultFilePath("hdfs:////test");
		q.setSql("show tables;");
		q.setExecTime(600L);
		q.setAddtime(sdf.format(addtime));
		queryDAOImp.insert(q);
	}

	@Test
	public void testGetQueryByUsername() {
		List<QueryInfo> qs = queryDAOImp.findByUsername("yukang.chen");
		System.out.println(qs.size());
		for (int i = 0; i < qs.size(); i++) {
			System.out.println(qs.get(i));
		}
	}

}
