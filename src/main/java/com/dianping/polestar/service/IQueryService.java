package com.dianping.polestar.service;

import javax.ws.rs.core.StreamingOutput;

import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;
import com.dianping.polestar.entity.QueryStatus;
import com.dianping.polestar.rest.BadParamException;

public interface IQueryService {

	QueryResult postQuery(Query query) throws BadParamException;

	QueryStatus getStatusInfo(String id);

	Boolean cancel(String id) throws BadParamException;

	StreamingOutput getDataFile(String id) throws BadParamException;
}
