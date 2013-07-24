package com.dianping.polestar.engine;

import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;

public interface IQueryEngine {
	
	QueryResult postQuery(Query query);
}
