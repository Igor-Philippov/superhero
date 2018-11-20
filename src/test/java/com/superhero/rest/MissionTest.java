package com.superhero.rest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Test;

public class MissionTest extends BaseTest {

	@Test
	public void checkMissionEndpointStatus() {
		prepareGet(MISSIONS).statusCode(SC_OK);
	}

	@Test
	public void checkSchemaValidity() {
		prepareGet(MISSIONS).assertThat().body(matchesJsonSchemaInClasspath("schemas/mission_schema.json"));
	}

	@Test
	public void checkResponseTimeAll() {
		prepareGet(MISSIONS).time(lessThan(ENDPOINT_RESPONSE_TIME));
	}

	@Test
	public void checkResponseTimeById() {
		prepareGet(MISSION_BY_ID).time(lessThan(ENDPOINT_RESPONSE_TIME));
	}

	@Test
	public void checkPutMethod() {
		preparePut(MISSIONS, DUMMY_TEST_JSON).then().statusCode(SC_METHOD_NOT_ALLOWED);
	}

	@Test
	public void checkPostMethod() {
		preparePost(MISSIONS, DUMMY_TEST_JSON).then().statusCode(SC_UNSUPPORTED_MEDIA_TYPE);
	}

	@Test
	public void checkDeleteMethod() {
		prepareDelete(MISSION_BY_ID).statusCode(SC_UNAUTHORIZED);
	}
}
