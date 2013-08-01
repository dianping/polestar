package com.dianping.polestar.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;
import com.dianping.polestar.entity.QueryStatus;
import com.dianping.polestar.jobs.Utilities;
import com.dianping.polestar.service.DefaultQueryService;
import com.dianping.polestar.service.IQueryService;

@Path("/query")
public class PolestarController {
	private IQueryService queryService = DefaultQueryService.getInstance();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getQueryId() {
		return Utilities.genUniqueID();
	}

	@GET
	@Path("/status/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryStatus getQueryStatus(@PathParam("id") String id) {
		return queryService.getStatusInfo(id);
	}

	@GET
	@Path("/download/{filename}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response get(@PathParam("filename") String filename) {
		return Response.ok(queryService.getDataFile(filename)).build();
	}

	@GET
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean cancelQuery(@PathParam("id") String id) {
		return queryService.cancel(id);
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postQuery(Query query) throws BadParamException {
		verifyQueryParam(query);
		QueryResult result = queryService.postQuery(query);
		return Response.status(Status.CREATED).entity(result).build();
	}

	public void verifyQueryParam(Query query) throws BadParamException {
		if (query == null || StringUtils.isBlank(query.getDatabase())
				|| StringUtils.isBlank(query.getId())
				|| StringUtils.isBlank(query.getSql())
				|| StringUtils.isBlank(query.getMode())
				|| StringUtils.isBlank(query.getUsername())
				|| StringUtils.isBlank(query.getPassword())) {
			throw new BadParamException("Missing Query Parameters");
		}
	}
}
