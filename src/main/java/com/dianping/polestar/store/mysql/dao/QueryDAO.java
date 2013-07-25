package com.dianping.polestar.store.mysql.dao;

import com.dianping.polestar.store.mysql.domain.QueryInfo;

public interface QueryDAO {
	
	public void insert(QueryInfo queryInfo);
	
	public QueryInfo findByUsername(String username);
}
