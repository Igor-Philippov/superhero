package com.superhero.test.integration.rest;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.superhero.test.BaseIntegrationTest;

import gherkin.deps.com.google.gson.JsonObject;

/** A test class to test [HTTP status code, response time, headers/body] of the HTTP responses when calling REST API for SuperHero object */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SuperHeroControllerIntegrationTest extends BaseIntegrationTest {
	
	private static JsonObject superHeroNew;
	private static JsonObject superHeroUpdate;
	
    @BeforeClass
    public static void setUp() {
    	superHeroNew = new JsonObject();
    	superHeroNew.addProperty("firstName", "FIRST_NAME_NEW");
    	superHeroNew.addProperty("lastName", "LAST_NAME_NEW");
    	superHeroNew.addProperty("superHeroName", "SUPERHERO_NAME@" + (System.currentTimeMillis()));
		
    	superHeroUpdate = new JsonObject();
    	superHeroUpdate.addProperty("firstName", "FIRST_NAME_UPDATE");
    	superHeroUpdate.addProperty("lastName", "LAST_NAME_UPDATE");
    	superHeroUpdate.addProperty("superHeroName", "SUPERHERO_NAME@" + (System.currentTimeMillis() - 1000000));
    }
    
	@Test
	/** Expected result: A new SuperHero is created and the [location] header of the response contains a link to it by SuperHero ID */
	public void _01_010_post_WhenValidJSON() {
		logger.info("CALLING _01_010_post_WhenValidJSON() - BEGIN");
		// Code Details = 201
		// Response headers =
		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
		//  content-length: 0 
		//  date: Fri, 23 Nov 2018 16:15:30 GMT 
		//  expires: 0 
		//  location: http://localhost:8080/api/1.0/superheros/19 
		//  pragma: no-cache 
		//  x-content-type-options: nosniff 
		//  x-frame-options: DENY 
		//  x-xss-protection: 1; mode=block 
		superHeroNew.addProperty("id", Long.valueOf(preparePost(SUPERHEROS, superHeroNew.toString()).then()
				.statusCode(SC_CREATED)
				//.time(lessThan(ENDPOINT_RESPONSE_TIME))
				.header("location", containsString(SUPERHEROS))
				.extract().header("location").split("/")[6]));		
		logger.info("CALLING _01_010_post_WhenValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _01_020_post_WhenInValidJSON() {
		logger.info("CALLING _01_020_post_WhenInValidJSON() - BEGIN");
		// Code Details = 500
		// Response body = 
		// {
        //   "timestamp": "2018-Nov-26 08:54:13",
        //   "httpStatus": "500 INTERNAL_SERVER_ERROR",
        //   "message": "Validation failed for classes [com.superhero.model.SuperHero] during persist time for groups [javax.validation.groups.Default, ]\nList of constraint violations:[\n\tConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=firstName, rootBeanClass=class com.superhero.model.SuperHero, messageTemplate='{javax.validation.constraints.NotEmpty.message}'}\n\tConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=lastName, rootBeanClass=class com.superhero.model.SuperHero, messageTemplate='{javax.validation.constraints.NotEmpty.message}'}\n\tConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=superHeroName, rootBeanClass=class com.superhero.model.SuperHero, messageTemplate='{javax.validation.constraints.NotEmpty.message}'}\n]",
        //   "details": "uri=/api/1.0/superheros"
		// {
		// Response headers =
		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
		// connection: close 
		// content-type: application/json;charset=UTF-8 
		// date: Fri, 23 Nov 2018 16:45:52 GMT 
		// expires: 0 
		// pragma: no-cache 
		// transfer-encoding: chunked 
		// x-content-type-options: nosniff 
		// x-frame-options: DENY 
		// x-xss-protection: 1; mode=block 
		preparePost(SUPERHEROS, DUMMY_TEST_JSON.toString()).then()
		.statusCode(SC_INTERNAL_SERVER_ERROR)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		//.body(not(containsString("org.springframework.dao.DataIntegrityViolationException")));
		.body(containsString("Validation failed for classes [com.superhero.model.SuperHero]"));
		logger.info("CALLING _01_020_post_WhenInValidJSON() - END");
	}
	
	@Test
	/** Expected result: All SuperHeros get retrieved as a valid JSON array */
	public void _02_010_get_SuperHeros() {
		logger.info("CALLING _02_010_get_SuperHeros() - BEGIN");
		prepareGet(SUPERHEROS)
        .statusCode(SC_OK)
        //.time(lessThan(ENDPOINT_RESPONSE_TIME))
        .body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHEROS))
        .body("$", not(empty()));
		logger.info("CALLING _02_010_get_SuperHeros() - END");
	}
	
	@Test
	/** Expected result: All SuperHeros with [lower(firstName) = lower (:firstName)] get retrieved as a valid JSON array */
	public void _02_020_get_SuperHerosByFirstName_WhenValid() {
		logger.info("CALLING _02_020_get_SuperHerosByFirstName_WhenValid() - BEGIN");
		prepareGet(String.format(SUPERHEROS_BY_FIRSTNAME, SUPERHERO_NAMES_FIRST_VALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body("$", hasSize(2))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHEROS));
		logger.info("CALLING _02_020_get_SuperHerosByFirstName_WhenValid() - END");
	}
	
	@Test
	/** Expected result: A valid empty JSON array */
	public void _02_030_get_SuperHerosByFirstName_WhenInValid() {
		logger.info("CALLING _02_030_get_SuperHerosByFirstName_WhenInValid() - BEGIN");
		prepareGet(String.format(SUPERHEROS_BY_FIRSTNAME, SUPERHERO_NAMES_FIRST_INVALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body("$", hasSize(0))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHEROS));
		logger.info("CALLING _02_030_get_SuperHerosByFirstName_WhenInValid() - END");
	}
	
	@Test
	/** Expected result: All SuperHeros with [lower(lastName) = lower (:lastName)] get retrieved as a valid JSON array */
	public void _02_040_get_SuperHerosByLastName_WhenValid() {
		logger.info("CALLING _02_040_get_SuperHerosByLastName_WhenValid() - BEGIN");
		prepareGet(String.format(SUPERHEROS_BY_LASTNAME, SUPERHERO_NAMES_LAST_VALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body("$", hasSize(2))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHEROS));
		logger.info("CALLING _02_040_get_SuperHerosByLastName_WhenValid() - END");
	}
	
	@Test
	/** Expected result: A valid empty JSON array */
	public void _02_050_get_SuperHerosByLastName_WhenInValid() {
		logger.info("CALLING _02_050_get_SuperHerosByLastName_WhenInValid() - BEGIN");
		prepareGet(String.format(SUPERHEROS_BY_LASTNAME, SUPERHERO_NAMES_LAST_INVALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body("$", hasSize(0))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHEROS));
		logger.info("CALLING _02_050_get_SuperHerosByLastName_WhenInValid() - END");
	}
	
		
	@Test
	/** Expected result: SuperHero as JSON object retrieved by ID */
	public void _02_060_get_SuperHeroById_WhenValid() {
		logger.info("CALLING _02_060_get_SuperHeroById_WhenValid() - BEGIN");
		prepareGet(String.format(SUPERHERO_BY_ID, SUPERHERO_ID_VALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHERO));
		logger.info("CALLING _02_060_get_SuperHeroById_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Super Hero with ID = ] text in the [message] header on the response */
	public void _02_070_get_SuperHeroById_WhenInvalid() {
		logger.info("CALLING _02_070_get_SuperHeroById_WhenInvalid() - BEGIN");
		// {
		//   "timestamp": "2018-Nov-26 09:07:30",
		//   "httpStatus": "404 NOT_FOUND",
		//   "message": "No registered Super Hero with ID = 0",
		//   "details": "uri=/api/1.0/superheros/0"
        // {
		prepareGet(String.format(SUPERHERO_BY_ID, SUPERHERO_ID_INVALID))
		.statusCode(SC_NOT_FOUND)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No registered Super Hero with ID = " + SUPERHERO_ID_INVALID));
		logger.info("CALLING _02_070_get_SuperHeroById_WhenInvalid() - END");
	}

	@Test
	/** Expected result: SuperHero as JSON object retrieved by a unique SuperHeroName */
	public void _02_080_get_SuperHeroBySuperHeroName_WhenValid() {
		logger.info("CALLING _02_080_get_SuperHeroBySuperHeroName_WhenValid() - BEGIN");
		prepareGet(String.format(SUPERHERO_BY_SUPERHERONAME, SUPERHERO_NAMES_SUPERHERO_VALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHERO));
		logger.info("CALLING _02_080_get_SuperHeroBySuperHeroName_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Super Hero by a superHeroName = ] text in the [message] header on the response */
	public void _02_090_get_SuperHeroBySuperHeroName_WhenInValid() {
		logger.info("CALLING _02_090_get_SuperHeroBySuperHeroName_WhenInValid() - BEGIN");
		// {
		//   "timestamp": "2018-Nov-26 10:06:45",
		//   "httpStatus": "404 NOT_FOUND",
		//   "message": "No registered Super Hero by a superHeroName = QQQ",
		//   "details": "uri=/api/1.0/superheros/names/superhero/QQQ"
		// {
		prepareGet(String.format(SUPERHERO_BY_SUPERHERONAME, SUPERHERO_NAMES_SUPERHERO_INVALID))
		.statusCode(SC_NOT_FOUND)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No registered Super Hero by a superHeroName = " + SUPERHERO_NAMES_SUPERHERO_INVALID));
		logger.info("CALLING _02_090_get_SuperHeroBySuperHeroName_WhenInValid() - BEGIN");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_010_put_WhenFoundByIdAndValidJSON() {
		logger.info("CALLING _03_010_put_WhenFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 200
		// Response headers =
		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
		//  content-type: application/json;charset=UTF-8 
		//  date: Fri, 07 Dec 2018 15:38:40 GMT 
		//  expires: 0 
		//  pragma: no-cache 
		//  transfer-encoding: chunked 
		//  x-content-type-options: nosniff 
		//  x-frame-options: DENY 
		//  x-xss-protection: 1; mode=block 
		preparePut(String.format(SUPERHERO_BY_ID, superHeroNew.get("id").getAsLong()), Collections.singletonMap("id", superHeroNew.get("id").getAsLong()), superHeroUpdate.toString()).then()
		.statusCode(SC_OK);
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		//.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_SUPERHERO));
		logger.info("CALLING _03_010_put_WhenFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_020_put_WhenFoundByIdAndInValidJSON() {
		logger.info("CALLING _03_020_put_WhenFoundByIdAndInValidJSON() - BEGIN");
		//JPA Exception
		
		// Code Details = 500
		// Response body
		// {
		//   "timestamp": "2018-Nov-27 06:12:13",
		//   "httpStatus": "500 INTERNAL_SERVER_ERROR",
		//   "message": "Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction",
		//   "details": "uri=/api/1.0/superheros/70"
		// }
		// Response headers
		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
		// connection: close 
		// content-type: application/json;charset=UTF-8 
		// date: Thu, 22 Nov 2018 19:01:27 GMT 
		// expires: 0 
		// pragma: no-cache 
		// transfer-encoding: chunked 
		// x-content-type-options: nosniff 
		// x-frame-options: DENY 
		// x-xss-protection: 1; mode=block 	
		preparePut(String.format(SUPERHERO_BY_ID, superHeroNew.get("id").getAsLong()), Collections.singletonMap("id", superHeroNew.get("id").getAsLong()), DUMMY_TEST_JSON.toString()).then()
		.statusCode(SC_INTERNAL_SERVER_ERROR)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("Could not commit JPA transaction"));
		logger.info("CALLING _03_020_put_WhenFoundByIdAndInValidJSON() - END");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Super Hero with ID = 9 available for update] text in the [message] header on the response */
	public void _03_030_put_WhenNotFoundByIdAndValidJSON() {
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 404	
		// Response body = 
		// {
		//   "timestamp": "2018-Nov-27 06:58:11",
		//   "httpStatus": "404 NOT_FOUND",
		//   "message": "No registered Super Hero with ID = 0 available for update",
		//   "details": "uri=/api/1.0/superheros/9"
		// }
		// Response headers
		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
		// content-type: application/json;charset=UTF-8 
		// date: Thu, 22 Nov 2018 19:05:25 GMT 
		// expires: 0 
		// pragma: no-cache 
		// transfer-encoding: chunked 
		// x-content-type-options: nosniff 
		// x-frame-options: DENY 
		// x-xss-protection: 1; mode=block 
		
		preparePut(String.format(SUPERHERO_BY_ID, SUPERHERO_ID_INVALID), Collections.singletonMap("id", SUPERHERO_ID_INVALID), superHeroUpdate.toString()).then()
		.statusCode(SC_NOT_FOUND)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No registered Super Hero with ID = " + SUPERHERO_ID_INVALID + " available for update"));
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_040_put_WhenNotFoundByIdAndInValidJSON() {
		logger.info("CALLING _03_040_put_WhenNotFoundByIdAndInValidJSON() - BEGIN");
		// Code Details = 404	
		// Response body = 
		// {
		//   "timestamp": "2018-Nov-27 06:58:11",
		//   "httpStatus": "404 NOT_FOUND",
		//   "message": "No registered Super Hero with ID = 0 available for update",
		//   "details": "uri=/api/1.0/superheros/9"
		// }
		// Response headers
		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
		// connection: close 
		// content-type: application/json;charset=UTF-8 
		// date: Thu, 22 Nov 2018 19:01:27 GMT 
		// expires: 0 
		// pragma: no-cache 
		// transfer-encoding: chunked 
		// x-content-type-options: nosniff 
		// x-frame-options: DENY 
		// x-xss-protection: 1; mode=block 	
		preparePut(String.format(SUPERHERO_BY_ID, SUPERHERO_ID_INVALID), Collections.singletonMap("id", SUPERHERO_ID_INVALID), DUMMY_TEST_JSON.toString()).then()
		.statusCode(SC_NOT_FOUND)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No registered Super Hero with ID = " + SUPERHERO_ID_INVALID + " available for update"));
		logger.info("CALLING _03_040_put_WhenNotFoundByIdAndInValidJSON() - END");
	}

	@Test
	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
	public void _04_010_delete_SuperHeroById_WhenUnAuthorized() {
		logger.info("CALLING _04_010_delete_SuperHeroById_WhenUnAuthorized() - BEGIN");		
		prepareDelete(String.format(SUPERHERO_BY_ID, superHeroNew.get("id").getAsLong()))
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.statusCode(SC_UNAUTHORIZED)
		.body(containsString("Unauthorized"));
		logger.info("CALLING _04_010_delete_SuperHeroById_WhenUnAuthorized() - END");
	}
	
	@Test
	/** Expected result: Provided the user is authorized - SuperHero with the specified ID will be deleted */
	public void _04_020_delete_SuperHeroById_WhenValid_AndAuthorized() {
		logger.info("CALLING _04_020_delete_SuperHeroById_WhenValid_AndAuthorized() - BEGIN");
		// Code Details = 204
		// Response headers = 
		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
		// date: Tue, 27 Nov 2018 17:13:33 GMT 
		// expires: 0 
		// pragma: no-cache 
		// x-content-type-options: nosniff 
		// x-frame-options: DENY 
		// x-xss-protection: 1; mode=block 
		prepareDeleteSecured(String.format(SUPERHERO_BY_ID, superHeroNew.get("id").getAsLong()))
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.statusCode(SC_NO_CONTENT);
		logger.info("CALLING _04_020_delete_SuperHeroById_WhenValid_AndAuthorized() - END");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No class com.superhero.model.SuperHero entity with id N exists!] text in the [message] header on the response */
	public void _04_030_delete_SuperHeroById_WhenInValidOrAlreadyDeleted_AndAuthorized() {
		logger.info("CALLING _04_030_delete_SuperHeroById_WhenInValidOrAlreadyDeleted_AndAuthorized() - BEGIN");
		// Code Details = 500
		// Response body = 
		// {
		//   "timestamp": "2018-Nov-27 05:58:21",
		//   "httpStatus": "500 INTERNAL_SERVER_ERROR",
		//   "message": "No class com.superhero.model.SuperHero entity with id 68 exists!",
		//   "details": "uri=/api/1.0/superheros/68"
		// }
		// Response headers = 
		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
		// content-type: application/json;charset=UTF-8 
		// date: Fri, 23 Nov 2018 20:14:43 GMT 
		// expires: 0 
		// pragma: no-cache 
		// transfer-encoding: chunked 
		// x-content-type-options: nosniff 
		// x-frame-options: DENY 
		// x-xss-protection: 1; mode=block 
		prepareDeleteSecured(String.format(SUPERHERO_BY_ID, superHeroNew.get("id").getAsLong()))
		.statusCode(SC_INTERNAL_SERVER_ERROR)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No class com.superhero.model.SuperHero entity with id " + superHeroNew.get("id").getAsLong() + " exists!"));
		logger.info("CALLING _04_030_delete_SuperHeroById_WhenInValidOrAlreadyDeleted_AndAuthorized() - END");
	}
}
