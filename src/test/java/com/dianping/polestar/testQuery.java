package com.dianping.polestar;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class testQuery {
	@Test
	public void testGetQuery() {
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://localhost:8080/polestar/query/10000");
	}
	
	@Test
	public void testGetQueryProgress() {
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://10.1.77.84:8090/polestar/query/post");
	}
	
	@Test
	public void testCancelQuery() {
//		Client client = Client.create();
//		WebResource webResource = client.resource("http://10.1.77.84:8090/polestar/query/post");
//		LOG.info("sending cancel job request:" + cancelUri);
//		ClientResponse response = webResource.type(
//				MediaType.APPLICATION_JSON).get(ClientResponse.class);
//		if (response.getEntity(Boolean.class) == true) {
	}

	@Test
	public void testPostQuery() {
		try {
			Client client = Client.create();
			WebResource webResource = client
					.resource("http://10.1.77.84:8090/polestar/query/post");
			
//			webResource = client
//			.resource("http://10.2.6.155:8080/polestar/query/post");

			String sql = "";
			sql = "show tables";
			//sql = "select key from test";
			String input = "{\"sql\":\"" + sql + "\",\"mode\":\"hive\","
					+ "\"database\":\"default\","
					+ "\"username\":\"yukang.chen\","
					+ "\"password\":\"yukang.chen\","
					+ "\"storeResult\":\"true\"," + "\"id\":\"11111\"}";
			ClientResponse response = webResource.cookie(new NewCookie("realuser","yukang.chen")).type("application/json")
					.post(ClientResponse.class, input);

			System.out.println("Output from Server .... \n");
			System.out.println("HTTP code:" + response.getStatus());
			String output = response.getEntity(String.class);
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
