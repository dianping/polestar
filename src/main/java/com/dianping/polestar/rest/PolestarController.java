package com.dianping.polestar.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dianping.polestar.entity.Query;
import com.dianping.polestar.entity.QueryResult;
import com.dianping.polestar.entity.QueryStatus;
import com.dianping.polestar.service.DefaultQueryService;
import com.dianping.polestar.service.IQueryService;

@Path("/query")
public class PolestarController {
	private IQueryService queryService = DefaultQueryService.getInstance();

	@GET
	@Path("/status/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public QueryStatus getQueryStatus(@PathParam("id") String id) {
		return queryService.getStatusInfo(id);
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
	public Response postQuery(Query query) {
		QueryResult result = queryService.postQuery(query);
		return Response.status(201).entity(result).build();
	}
}
