package com.dianping.polestar;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class testQuery {

	@Test
	public void testPostQuery() {
		try {
			Client client = Client.create();
			WebResource webResource = client
					.resource("http://localhost:8080/polestar/query/10000");

			webResource = client
					.resource("http://10.1.77.84:8080/polestar/query/post");
			String sql = "select guid, referer from hippolog where dt='2012-09-01'";
			 //sql = "show tables";

			String input = "{\"sql\":\"" + sql + "\",\"mode\":\"hive\","
					+ "\"database\":\"default\","
					+ "\"username\":\"yukang.chen\","
					+ "\"password\":\"yukang.chen\","
					+ "\"storeResult\":\"true\"," + "\"id\":\"10000\"}";

			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, input);

			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
