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
import com.dianping.polestar.store.mysql.domain.QueryProgress;

public class QueryDAOImpTest {
	static QueryDAO queryDAOImp;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AbstractApplicationContext cxt = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		queryDAOImp = (QueryDAO) cxt.getBean("queryDaoImpl");
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

	@Test
	public void testGetQueryProgress() {
		queryDAOImp.insertQueryProgress(new QueryProgress("11111",
				"test query progress info 000"));
		QueryProgress qp = queryDAOImp.findQueryProgressById("11111");
		qp = queryDAOImp.findQueryProgressById("22222");
	}

}
