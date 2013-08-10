package com.dianping.polestar.store.mysql.dao;

import java.util.List;

import com.dianping.polestar.store.mysql.domain.QueryInfo;
import com.dianping.polestar.store.mysql.domain.QueryProgress;

public interface QueryDAO {

	public void insert(QueryInfo queryInfo);

	public List<QueryInfo> findByUsername(String username);

	public void insertQueryProgress(QueryProgress queryProgress);

	public QueryProgress findQueryProgressById(String id);
}
