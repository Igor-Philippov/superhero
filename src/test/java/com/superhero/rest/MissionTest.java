package com.superhero.rest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Test;

public class MissionTest extends BaseTest {

	@Test
	public void checkMissionEndpointStatus() {
		//prepareGet(MISSIONS).statusCode(SC_OK);
	}

	@Test
	public void checkSchemaValidity() {
		//prepareGet(MISSIONS).assertThat().body(matchesJsonSchemaInClasspath("schemas/mission_schema.json"));
	}

	@Test
	public void checkResponseTimeAll() {
		//prepareGet(MISSIONS).time(lessThan(2000L));
	}

	@Test
	public void checkResponseTimeById() {
		//prepareGet(SUPERHERO_BY_ID).time(lessThan(ENDPOINT_RESPONSE_TIME));
	}

	@Test
	public void checkPutMethod() {
		//preparePut(SUPERHERO_BY_ID, DUMMY_TEST_JSON).then().statusCode(SC_METHOD_NOT_ALLOWED);
	}

	@Test
	public void checkPostMethod() {
		//preparePost(MISSIONS, DUMMY_TEST_JSON).then().statusCode(SC_METHOD_NOT_ALLOWED);
	}

	@Test
	public void checkDeleteMethod() {
		//prepareDelete(SUPERHERO_BY_ID).statusCode(SC_METHOD_NOT_ALLOWED);
	}
}
