package com.dianping.polestar.engine;

import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;

public class DummyQueryEngine implements IQueryEngine {

	@Override
	public QueryResult postQuery(Query query) {
		return new QueryResult();
	}
}
