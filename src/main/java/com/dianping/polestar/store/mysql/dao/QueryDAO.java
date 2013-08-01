package com.dianping.polestar.store.mysql.dao;

import java.util.List;

import com.dianping.polestar.store.mysql.domain.QueryInfo;

public interface QueryDAO {

	public void insert(QueryInfo queryInfo);

	public List<QueryInfo> findByUsername(String username);
}
