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

/** A test class to test [HTTP status code, response time, headers/body] of the HTTP responses when calling REST API for Mission object */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MissionControllerIntegrationTest extends BaseIntegrationTest {
	
	private static JsonObject missionNew;
	private static JsonObject missionUpdate;
	
    @BeforeClass
    public static void setUp() {
		missionNew = new JsonObject();
		missionNew.addProperty("name", "TEST_MISSION_NEW_" + System.currentTimeMillis());
		missionNew.addProperty("completed", true);
		
		missionUpdate = new JsonObject();
		missionUpdate.addProperty("name", "TEST_MISSION_UPDATE_" + (System.currentTimeMillis() - 1000000));
		missionUpdate.addProperty("completed", false);
    }
    
	@Test
	/** Expected result: A new Mission is created and the [location] header of the response contains a link to it by Mission ID */
	public void _01_010_post_WhenValidJSON() {
		logger.info("CALLING _01_010_post_WhenValidJSON() - BEGIN");
		// Code Details = 201
		// Response headers =
		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
		//  content-length: 0 
		//  date: Fri, 23 Nov 2018 16:15:30 GMT 
		//  expires: 0 
		//  location: http://localhost:8080/api/1.0/missions/177 
		//  pragma: no-cache 
		//  x-content-type-options: nosniff 
		//  x-frame-options: DENY 
		//  x-xss-protection: 1; mode=block 
		missionNew.addProperty("id", Long.valueOf(preparePost(MISSIONS, missionNew.toString()).then()
				.statusCode(SC_CREATED)
				//.time(lessThan(ENDPOINT_RESPONSE_TIME))
				.header("location", containsString(MISSIONS))
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
		//   "timestamp": "2018-Nov-23 04:45:52",
		//   "httpStatus": "500 INTERNAL_SERVER_ERROR",
		//   "message": "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
		//   "details": "uri=/api/1.0/missions"
		// }
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
		preparePost(MISSIONS, DUMMY_TEST_JSON.toString()).then()
		.statusCode(SC_INTERNAL_SERVER_ERROR)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		//.body(not(containsString("org.springframework.dao.DataIntegrityViolationException")));
		.body(containsString("could not execute statement"));
		logger.info("CALLING _01_020_post_WhenInValidJSON() - END");
	}
    

	@Test
	/** Expected result: All Missions get retrieved as a valid JSON array */
	public void _02_010_get_Missions() {
		logger.info("CALLING _02_010_get_Missions() - BEGIN");
		prepareGet(MISSIONS)
        .statusCode(SC_OK)
        //.time(lessThan(ENDPOINT_RESPONSE_TIME))
        .body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
        .body("$", not(empty()));
		logger.info("CALLING _02_010_get_Missions() - END");
	}
	
	@Test
	/** Expected result: All Missions with [completed = true] get retrieved as a valid JSON array */
	public void _02_020_get_MissionsCompleted() {
		logger.info("CALLING _02_020_get_MissionsCompleted() - BEGIN");
		prepareGet(MISSIONS_COMPLETED)
		.statusCode(SC_OK)
        //.time(lessThan(ENDPOINT_RESPONSE_TIME))
        .body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS));
		logger.info("CALLING _02_020_get_MissionsCompleted() - END");
	}
		
	@Test
	/** Expected result: Provided the user is authorized - All Missions with [deleted = true] get retrieved as a valid JSON array */
	public void _02_030_get_MissionsDeleted_WhenAuthorised() {
		logger.info("CALLING _02_030_get_MissionsDeleted_WhenAuthorised() - BEGIN");
		prepareGetSecured(MISSIONS_DELETED)
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_SECURED))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
        .body("$", not(empty()));
		logger.info("CALLING _02_030_get_MissionsDeleted_WhenAuthorised() - END");
	}
	
	@Test
	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
	public void _02_040_get_MissionsDeleted_WhenUnAuthorised() {
		logger.info("CALLING _02_040_get_MissionsDeleted_WhenUnAuthorised() - BEGIN");
		prepareGet(MISSIONS_DELETED)
		.statusCode(SC_UNAUTHORIZED)
		//.time(lessThan(ENDPOINT_RESPONSE_SECURED))
		.body(containsString("Unauthorized"));
		logger.info("CALLING _02_040_get_MissionsDeleted_WhenUnAuthorised() - END");
	}
	
	@Test
	/** Expected result: Mission as JSON object retrieved by ID */
	public void _02_050_get_MissionById_WhenValid() {
		logger.info("CALLING _02_050_get_MissionById_WhenValid() - BEGIN");
		prepareGet(String.format(MISSION_BY_ID, MISSION_ID_VALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION));
		//.body("$", hasSize(1));
		logger.info("CALLING _02_050_get_MissionById_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Mission with ID = ] text in the [message] header on the response */
	public void _02_060_get_MissionById_WhenInvalid() {
		logger.info("CALLING _02_060_get_MissionById_WhenInvalid() - BEGIN");
		// {
		//	 "timestamp": "2018-Nov-21 06:44:24",
		//	 "httpStatus": "404 NOT_FOUND",
		//	 "message": "No registered Mission with ID = 1",
		//	 "details": "uri=/api/1.0/missions/1"
		// }
		prepareGet(String.format(MISSION_BY_ID, MISSION_ID_INVALID))
		.statusCode(SC_NOT_FOUND)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No registered Mission with ID = " + MISSION_ID_INVALID));
		logger.info("CALLING _02_060_get_MissionById_WhenInvalid() - END");
	}
	
	@Test
	/** Expected result: All Missions with [lower(name) = lower (:name)] get retrieved as a valid JSON array */
	public void _02_070_get_MissionByName_WhenValid() {
		logger.info("CALLING _02_070_get_MissionByName_WhenValid() - BEGIN");
		prepareGet(String.format(MISSION_BY_NAME, MISSION_NAME_VALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
		.body("$", hasSize(1));
		logger.info("CALLING _02_070_get_MissionByName_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: A valid empty JSON array */
	public void _02_080_get_MissionByName_WhenInvalid() {
		logger.info("CALLING _02_080_get_MissionByName_WhenInvalid() - BEGIN");
		prepareGet(String.format(MISSION_BY_NAME, MISSION_NAME_INVALID))
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
		.body("$", hasSize(0));
		logger.info("CALLING _02_080_get_MissionByName_WhenInvalid() - BEGIN");
	}
		
	@Test
	/** Expected result: Database Mission info gets updated */
	public void _02_090_put_WhenFoundByIdAndValidJSON() {
		logger.info("CALLING _02_090_put_WhenFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 200	
		// Response body: { "id": 164, "name": "MMMMMMMMMMMMMMMMMMMMMM", "completed": false }
		// Response headers
		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
		//  content-type: application/json;charset=UTF-8 
		//  date: Thu, 22 Nov 2018 19:02:50 GMT 
		//  expires: 0 
		//  pragma: no-cache 
		//  transfer-encoding: chunked 
		//  x-content-type-options: nosniff 
		//  x-frame-options: DENY 
		//  x-xss-protection: 1; mode=block 
		// 	}

		missionNew.addProperty("name", missionNew.get("name").getAsString().toLowerCase());
		missionNew.addProperty("completed", false);
		
		preparePut(String.format(MISSION_BY_ID, missionNew.get("id").getAsLong()), Collections.singletonMap("id", missionNew.get("id").getAsLong()), missionNew.toString()).then()
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION))
        .body("$", not(empty()));
		logger.info("CALLING _02_090_put_WhenFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_010_put_WhenFoundByIdAndValidJSON() {
		logger.info("CALLING _03_010_put_WhenFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 200
		// Response body = 	
		// {
		//   "id": 110,
		//   "name": "S_L_E_E_P",
		//   "completed": true
		// {
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
		preparePut(String.format(MISSION_BY_ID, missionNew.get("id").getAsLong()), Collections.singletonMap("id", missionNew.get("id").getAsLong()), missionUpdate.toString()).then()
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION));
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
		// "timestamp": "2018-Nov-22 07:01:27",
		// "httpStatus": "500 INTERNAL_SERVER_ERROR",
		// "message": "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
		// "details": "uri=/api/1.0/missions/164"
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
		preparePut(String.format(MISSION_BY_ID, missionNew.get("id").getAsLong()), Collections.singletonMap("id", missionNew.get("id").getAsLong()), DUMMY_TEST_JSON.toString()).then()
		.statusCode(SC_INTERNAL_SERVER_ERROR)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("could not execute statement"));
		logger.info("CALLING _03_020_put_WhenFoundByIdAndInValidJSON() - END");
	}
	
	@Test
	public void _03_030_put_WhenNotFoundByIdAndValidJSON() {
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 200	
		// Response body: { "id": 165, "name": "MMMMMMMMMMMMMMMMMMMMMM_200", "completed": false }
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
		preparePut(String.format(MISSION_BY_ID, MISSION_ID_INVALID), Collections.singletonMap("id", MISSION_ID_INVALID), missionUpdate.toString()).then()
		.statusCode(SC_OK)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION))
        .body("$", not(empty()));
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_040_put_WhenNotFoundByIdAndInValidJSON() {
		logger.info("CALLING _03_040_put_WhenNotFoundByIdAndInValidJSON() - BEGIN");
		// Code Details = 500
		// Response body = 
		// {
		// "timestamp": "2018-Nov-22 07:01:27",
		// "httpStatus": "500 INTERNAL_SERVER_ERROR",
		// "message": "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
		// "details": "uri=/api/1.0/missions/164"
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
		preparePut(String.format(MISSION_BY_ID, MISSION_ID_INVALID), Collections.singletonMap("id", MISSION_ID_INVALID), DUMMY_TEST_JSON.toString()).then()
		.statusCode(SC_INTERNAL_SERVER_ERROR)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("could not execute statement"));
		logger.info("CALLING _03_040_put_WhenNotFoundByIdAndInValidJSON() - END");
	}
	
	@Test
	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
	public void _04_010_delete_MissionById_WhenUnAuthorized() {
		logger.info("CALLING _04_010_delete_MissionById_WhenUnAuthorized() - BEGIN");		
		prepareDelete(String.format(MISSION_BY_ID, missionNew.get("id").getAsLong()))
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.statusCode(SC_UNAUTHORIZED)
		.body(containsString("Unauthorized"));
		logger.info("CALLING _04_010_delete_MissionById_WhenUnAuthorized() - END");
	}
	
	@Test
	/** Expected result: Provided the user is authorized - Mission with the specified ID will be soft deleted */
	public void _04_020_delete_MissionById_WhenValid_AndAuthorized() {
		logger.info("CALLING _04_020_delete_MissionById_WhenValid_AndAuthorized() - BEGIN");
		// Code Details = 204
		// Response headers = 
		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
		//  date: Fri, 23 Nov 2018 20:11:02 GMT 
		//  expires: 0 
		//  pragma: no-cache 
		//  x-content-type-options: nosniff 
		//  x-frame-options: DENY 
		//  x-xss-protection: 1; mode=block 
		prepareDeleteSecured(String.format(MISSION_BY_ID, missionNew.get("id").getAsLong()))
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.statusCode(SC_NO_CONTENT);
		logger.info("CALLING _04_020_delete_MissionById_WhenValid_AndAuthorized() - END");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered for deletion Mission with ID = ] text in the [message] header on the response */
	public void _04_030_delete_MissionById_WhenInValidOrAlreadyDeleted_AndAuthorized() {
		logger.info("CALLING _04_030_delete_MissionById_WhenInValidOrAlreadyDeleted_AndAuthorized() - BEGIN");
		// Code Details = 404
		// Response body = 
		// {
		//   "timestamp": "2018-Nov-23 08:14:43",
		//   "httpStatus": "404 NOT_FOUND",
		//   "message": "No registered for deletion Mission with ID = 110",
		//   "details": "uri=/api/1.0/missions/110"
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
		prepareDeleteSecured(String.format(MISSION_BY_ID, missionNew.get("id").getAsLong()))
		.statusCode(SC_NOT_FOUND)
		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
		.body(containsString("No registered for deletion Mission with ID = " + missionNew.get("id").getAsLong()));
		logger.info("CALLING _04_030_delete_MissionById_WhenInValidOrAlreadyDeleted_AndAuthorized() - END");
	}
}
