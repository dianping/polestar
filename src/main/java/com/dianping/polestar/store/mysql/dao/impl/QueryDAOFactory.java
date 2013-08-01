package com.dianping.polestar.store.mysql.dao.impl;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dianping.polestar.store.mysql.dao.QueryDAO;

public final class QueryDAOFactory {

	public static QueryDAO queryDao;

	public static QueryDAO getInstance() {
		if (queryDao == null) {
			synchronized (QueryDAOFactory.class) {
				if (queryDao == null) {
					AbstractApplicationContext cxt = new ClassPathXmlApplicationContext(
							"applicationContext.xml");
					queryDao = (QueryDAO) cxt.getBean("queryDaoImpl");
				}
			}
		}
		return queryDao;
	}
}
