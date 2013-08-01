package com.dianping.polestar;

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
	public void testPostQuery() {
		try {
			Client client = Client.create();
			WebResource webResource = client
					.resource("http://10.1.77.84:8080/polestar/query/post");

			String sql = "";
			sql = "show tables";
			sql = "select guid, referer from hippolog where dt='2012-09-01'";
			String input = "{\"sql\":\"" + sql + "\",\"mode\":\"hive\","
					+ "\"database\":\"default\","
					+ "\"username\":\"yukang.chen\","
					+ "\"password\":\"yukang.chen\","
					+ "\"storeResult\":\"false\"," + "\"id\":\"5685854852\"}";

			ClientResponse response = webResource.type("application/json")
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
