package com.dianping.polestar.store.mysql.dao;

import java.util.List;

import com.dianping.polestar.store.mysql.domain.QueryCancel;
import com.dianping.polestar.store.mysql.domain.QueryInfo;
import com.dianping.polestar.store.mysql.domain.QueryProgress;

public interface QueryDAO {

	public void insertQueryInfo(QueryInfo queryInfo);

	public List<QueryInfo> findQueryInfoByUsername(String username);

	public void insertQueryProgress(QueryProgress queryProgress);

	public QueryProgress findQueryProgressById(String id);

	public void insertQueryCancel(QueryCancel qc);

	public List<QueryCancel> findQueryCancelWithouHost(String host);

	public void deleteQueryCancelById(String id);
}
