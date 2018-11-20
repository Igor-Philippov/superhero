package com.superhero.rest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Test;

public class SuperHeroTest extends BaseTest {
		
	@Test
	public void checkSuperHeroEndpointStatus() {
		prepareGet(SUPERHEROS).statusCode(SC_OK);
	}

	@Test
	public void checkSchemaValidity() {
		prepareGet(SUPERHEROS).assertThat().body(matchesJsonSchemaInClasspath("schemas/superhero_schema.json"));
	}

	@Test
	public void checkResponseTimeAll() {
		prepareGet(SUPERHEROS).time(lessThan(ENDPOINT_RESPONSE_TIME));
	}

	@Test
	public void checkResponseTimeById() {
		prepareGet(SUPERHERO_BY_ID).time(lessThan(ENDPOINT_RESPONSE_TIME));
	}

	// Negative testing section

	@Test
	public void checkPutMethod() {
		preparePut(SUPERHERO_BY_ID, DUMMY_TEST_JSON).then().statusCode(SC_UNSUPPORTED_MEDIA_TYPE);
	}

	@Test
	public void checkPostMethod() {
		preparePost(SUPERHEROS, DUMMY_TEST_JSON).then().statusCode(SC_UNSUPPORTED_MEDIA_TYPE);
	}

	@Test
	public void checkDeleteMethod() {
		prepareDelete(SUPERHERO_BY_ID).statusCode(SC_UNAUTHORIZED);
	}
}
