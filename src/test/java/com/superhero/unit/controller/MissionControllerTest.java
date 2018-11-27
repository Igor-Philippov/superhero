package com.superhero.unit.controller;

import static com.superhero.rest.constant.Paths.MISSIONS;
import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.superhero.unit.controller.BaseControllerTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superhero.model.Mission;
import com.superhero.rest.controller.MissionController;

@WebMvcTest(MissionController.class)
public class MissionControllerTest extends BaseControllerTest {
    Logger logger = LoggerFactory.getLogger(MissionControllerTest.class); 
	
	@Autowired
    protected MockMvc mvc;

    @MockBean
    private MissionController missionController;
    
    Mission mission = new Mission(); {
    	mission.setName("mission1000");
    	mission.setCompleted(true);
    	mission.setId(1000L);
    }
    
//	@Test
//	public void createMissionTest() throws Exception {
//		given(missionController.createMission(mission)).willReturn(ResponseEntity.ok().body(mission));
//		mvc.perform(post(MISSIONS)
//				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.content(toJsonString(mission)))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$[0].name", is(mission.getName())))
//				.andExpect(jsonPath("$[0].completed", is(mission.isCompleted())));
//	}
	
    
    @Test
    public void retrieveMissionByIdTest() throws Exception {
		given(missionController.retrieveMissionById(mission.getId())).willReturn(ResponseEntity.ok().body(mission));   	
		mvc.perform(get(MISSION_BY_ID, mission.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.name", is(mission.getName())))
				.andExpect(jsonPath("$.completed", is(mission.isCompleted())));
    	
		verify(missionController, times(1)).retrieveMissionById(mission.getId());
		verifyNoMoreInteractions(missionController);    	
    }
    
	@Test
	public void retrieveAllMissionsTest() throws Exception {
		List<Mission> missions = singletonList(mission);
		given(missionController.retrieveAllMissions()).willReturn(missions);
		mvc.perform(get(MISSIONS).
				contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is(mission.getName())))
				.andExpect(jsonPath("$[0].completed", is(mission.isCompleted())));
		
		verify(missionController, times(1)).retrieveAllMissions();
		verifyNoMoreInteractions(missionController);  		
	}
    

//	@Test
//	public void retrieveAllMissionsTest() throws Exception {
//
//		List<Mission> missions = new ArrayList<Mission>();
//		missions.add(mission);
//
//		when(missionResource.retrieveAllMissions()).thenReturn(missions);
//
//		mvc.perform(get("/missions/list").
//				contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(1)))
//				.andExpect(jsonPath("$[0].name", is("mission")))
//				.andExpect(jsonPath("$[0].isCompleted", is(false)))
//				.andExpect(jsonPath("$[0].isDeleted", is(false)));
//		
//		verify(missionResource, times(1)).retrieveAllMissions();
//		verifyNoMoreInteractions(missionResource);  		
//
//	}
    
    
//	@Test
//	public void updateMissionTest() throws Exception {
//
//		doNothing().when(missionResource).updateMission(mission, mission.getId());
//
//		mvc.perform(put("/missions/update/{id}", mission.getId())
//				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//				.content(asJsonString(mission)))
//				.andExpect(status().isOk());
//
//	} 
    
	@Test
	public void deleteMissionTest() throws Exception {	
		given(missionController.softDeleteMission(mission.getId())).willReturn(ResponseEntity.noContent().build());
        mvc.perform(delete(MISSION_BY_ID, mission.getId())
        		.with(user("shc_user").password("password"))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
		
		verify(missionController, times(1)).softDeleteMission(mission.getId());
		verifyNoMoreInteractions(missionController);
	}
}
//
//package com.superhero.rest;
//
//import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
//import static org.apache.http.HttpStatus.SC_CREATED;
//import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
//import static org.apache.http.HttpStatus.SC_NOT_FOUND;
//import static org.apache.http.HttpStatus.SC_OK;
//import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.empty;
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.hasItems;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.not;
//
//import java.util.Collections;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import gherkin.deps.com.google.gson.JsonObject;
//
///** A test class to test [HTTP status code, response time, headers/body] of the HTTP responses when calling REST API for Mission object */
//public class MissionTest extends BaseTest {
//	
//	static JsonObject mission;
//	
//    @BeforeClass
//    public static void setUp() {
//		mission = new JsonObject();
//		mission.addProperty("name", "TEST_MISSION_" + System.currentTimeMillis());
//		mission.addProperty("completed", true);
//    }
//    
//	@Test
//	/** Expected result: A new Mission is created and the [location] header of the response contains a link to it by Mission ID */
//	public void check_POST_WhenValidJSON() {
//		logger.debug("CALLING check_POST_WhenValidJSON() - BEGIN");
//		// Code Details = 201
//		// Response headers =
//		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
//		//  content-length: 0 
//		//  date: Fri, 23 Nov 2018 16:15:30 GMT 
//		//  expires: 0 
//		//  location: http://localhost:8080/api/1.0/missions/177 
//		//  pragma: no-cache 
//		//  x-content-type-options: nosniff 
//		//  x-frame-options: DENY 
//		//  x-xss-protection: 1; mode=block 
//		mission.addProperty("id", Long.valueOf(preparePost(MISSIONS, mission.toString()).then()
//				.statusCode(SC_CREATED)
//				//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//				.header("location", containsString(MISSIONS))
//				.extract().header("location").split("/")[6]));
//		logger.debug("CALLING check_POST_WhenValidJSON() - END");
//	}
//
//	@Test
//	/** Expected result: A server exception is thrown and the response body contains its description */
//	public void check_Post_WhenInValidJSON() {
//		logger.debug("CALLING check_Post_WhenInValidJSON() - BEGIN");
//		// Code Details = 500
//		// Response body = 
//		// {
//		//   "timestamp": "2018-Nov-23 04:45:52",
//		//   "httpStatus": "500 INTERNAL_SERVER_ERROR",
//		//   "message": "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
//		//   "details": "uri=/api/1.0/missions"
//		// }
//		// Response headers =
//		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
//		// connection: close 
//		// content-type: application/json;charset=UTF-8 
//		// date: Fri, 23 Nov 2018 16:45:52 GMT 
//		// expires: 0 
//		// pragma: no-cache 
//		// transfer-encoding: chunked 
//		// x-content-type-options: nosniff 
//		// x-frame-options: DENY 
//		// x-xss-protection: 1; mode=block 
//		preparePost(MISSIONS, DUMMY_TEST_JSON.toString()).then()
//		.statusCode(SC_INTERNAL_SERVER_ERROR)
//		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//		//.body(not(containsString("org.springframework.dao.DataIntegrityViolationException")));
//		.body(containsString("could not execute statement"));
//		logger.debug("CALLING check_Post_WhenInValidJSON() - END");
//	}
//    
//
//	@Test
//	/** Expected result: All Missions get retrieved as a valid JSON array */
//	public void check_GET_Missions() {
//		logger.debug("CALLING check_GET_Missions() - BEGIN");
//		prepareGet(MISSIONS)
//        .statusCode(SC_OK)
//        //.time(lessThan(ENDPOINT_RESPONSE_TIME))
//        .body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
//        .body("$", not(empty()))
//        //.body("id", hasItems(mission.get("id")))
//        .body("name", hasItems(mission.get("name").getAsString()));
//		logger.debug("CALLING check_GET_Missions() - END");
//	}
//	
//	@Test
//	/** Expected result: All Missions with [completed = true] get retrieved as a valid JSON array */
//	public void check_Get_MissionsCompleted() {
//		logger.debug("CALLING check_Get_MissionsCompleted() - BEGIN");
//		prepareGet(MISSIONS_COMPLETED)
//		.statusCode(SC_OK)
//        //.time(lessThan(ENDPOINT_RESPONSE_TIME))
//        .body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
//        .body("$", not(empty()))
//        .body("name", hasItems(mission.get("name").getAsString()));
//		logger.debug("CALLING check_Get_MissionsCompleted() - END");
//	}
//		
//	@Test
//	/** Expected result: Provided the user is authorized - All Missions with [deleted = true] get retrieved as a valid JSON array */
//	public void check_Get_MissionsDeleted_WhenAuthorised() {
//		logger.debug("CALLING check_Get_MissionsDeleted_WhenAuthorised() - BEGIN");
//		prepareGetSecured(MISSIONS_DELETED)
//		.statusCode(SC_OK)
//		//.time(lessThan(ENDPOINT_RESPONSE_SECURED))
//		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
//        .body("$", not(empty()));
//		logger.debug("CALLING check_Get_MissionsDeleted_WhenAuthorised() - END");
//	}
//	
//	@Test
//	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
//	public void check_Get_MissionsDeleted_WhenUnAuthorised() {
//		logger.debug("CALLING check_Get_MissionsDeleted_WhenUnAuthorised() - BEGIN");
//		prepareGet(MISSIONS_DELETED)
//		.statusCode(SC_UNAUTHORIZED)
//		//.time(lessThan(ENDPOINT_RESPONSE_SECURED))
//		.body(containsString("Unauthorized"));
//		logger.debug("CALLING check_Get_MissionsDeleted_WhenUnAuthorised() - END");
//	}
//	
//	@Test
//	/** Expected result: Mission as JSON object retrieved by ID */
//	public void check_Get_MissionById_WhenValid() {
//		logger.debug("CALLING check_Get_MissionById_WhenValid() - BEGIN");
//		prepareGet(String.format(MISSION_BY_ID, mission.get("id").getAsLong()))
//		.statusCode(SC_OK)
//		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION))
//		//.body("id", equalTo(mission.get("id").getAsNumber()))
//		.body("name", equalTo(mission.get("name").getAsString()))
//		.body("completed", equalTo(mission.get("completed").getAsBoolean()));
//		
////		map(Long::valueOf), Matchers.is(expected)
////		.body("id", map(Long::valueOf), equalTo(mission.get("id").getAsLong()))
//		logger.debug("CALLING check_Get_MissionById_WhenValid() - BEGIN");
//	}
//	
//	@Test
//	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Mission with ID = ] text in the [message] header on the response */
//	public void check_Get_MissionById_WhenInvalid() {
//		logger.debug("CALLING check_Get_MissionById_WhenInvalid() - BEGIN");
//		// {
//		//	 "timestamp": "2018-Nov-21 06:44:24",
//		//	 "httpStatus": "404 NOT_FOUND",
//		//	 "message": "No registered Mission with ID = 1",
//		//	 "details": "uri=/api/1.0/missions/1"
//		// }
//		prepareGet(String.format(MISSION_BY_ID, MISSION_ID_INVALID))
//		.statusCode(SC_NOT_FOUND)
//		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//		.body(containsString("No registered Mission with ID = " + MISSION_ID_INVALID));
//		logger.debug("CALLING check_Get_MissionById_WhenInvalid() - END");
//	}
//	
//	@Test
//	/** Expected result: All Missions with [lower(name) = lower (:name)] get retrieved as a valid JSON array */
//	public void check_Get_MissionByName_WhenValid() {
//		logger.debug("CALLING check_Get_MissionByName_WhenValid() - BEGIN");
//		prepareGet(String.format(MISSION_BY_NAME, mission.get("name").getAsString()))
//		.statusCode(SC_OK)
//		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
//        .body("$", not(empty()))
//        .body("name", hasItems(mission.get("name").getAsString()));
//		logger.debug("CALLING check_Get_MissionByName_WhenValid() - BEGIN");
//	}
//	
//	@Test
//	/** Expected result: A valid empty JSON array */
//	public void check_Get_MissionByName_WhenInvalid() {
//		logger.debug("CALLING check_Get_MissionByName_WhenInvalid() - BEGIN");
//		prepareGet(String.format(MISSION_BY_NAME, MISSION_NAME_INVALID))
//		.statusCode(SC_OK)
//		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSIONS))
//		.body("$", hasSize(0));
//		logger.debug("CALLING check_Get_MissionByName_WhenInvalid() - BEGIN");
//	}
//		
//	@Test
//	/** Expected result: Database Mission info gets updated */
//	public void checkPutMethod_WhenFoundByIdAndValidJSON() {
//		// Code Details = 200	
//		// Response body: { "id": 164, "name": "MMMMMMMMMMMMMMMMMMMMMM", "completed": false }
//		// Response headers
//		//  cache-control: no-cache, no-store, max-age=0, must-revalidate 
//		//  content-type: application/json;charset=UTF-8 
//		//  date: Thu, 22 Nov 2018 19:02:50 GMT 
//		//  expires: 0 
//		//  pragma: no-cache 
//		//  transfer-encoding: chunked 
//		//  x-content-type-options: nosniff 
//		//  x-frame-options: DENY 
//		//  x-xss-protection: 1; mode=block 
//		// 	}
//
//		mission.addProperty("name", mission.get("name").getAsString().toLowerCase());
//		mission.addProperty("completed", false);
//		
//		preparePut(MISSION_BY_ID, Collections.singletonMap("id", mission.get("id").getAsLong()), mission.toString()).then()
//		.statusCode(SC_OK)
//		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
//		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION))
//        .body("$", not(empty()))
//        .body("name", hasItems(mission.get("name").getAsString()))
//		.body("completed", equalTo(mission.get("completed").getAsBoolean()));
//	}
//	
////	@Test
////	public void checkPutMethod_WhenFoundByIdAndInValidJSON() {
////		//JPA Exception
////		
////		// Code Details = 500
////		// Response body
////		// {
////		// "timestamp": "2018-Nov-22 07:01:27",
////		// "httpStatus": "500 INTERNAL_SERVER_ERROR",
////		// "message": "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
////		// "details": "uri=/api/1.0/missions/164"
////		// }
////		// Response headers
////		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
////		// connection: close 
////		// content-type: application/json;charset=UTF-8 
////		// date: Thu, 22 Nov 2018 19:01:27 GMT 
////		// expires: 0 
////		// pragma: no-cache 
////		// transfer-encoding: chunked 
////		// x-content-type-options: nosniff 
////		// x-frame-options: DENY 
////		// x-xss-protection: 1; mode=block 	
////		preparePut(GET_MISSION_BY_ID_VALID, Collections.singletonMap("id", mission.get("id").getAsLong()), DUMMY_TEST_JSON.toString()).then()
////		.statusCode(SC_INTERNAL_SERVER_ERROR)
////		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
////		.body(containsString("could not execute statement"));
////	}
////	
////	@Test
////	public void checkPutMethod_WhenNotFoundByIdAndValidJSON() {
////		// Database mission info gets created with a new Id
////		
////		// Code Details = 200	
////		// Response body: { "id": 165, "name": "MMMMMMMMMMMMMMMMMMMMMM_200", "completed": false }
////		// Response headers
////		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
////		// content-type: application/json;charset=UTF-8 
////		// date: Thu, 22 Nov 2018 19:05:25 GMT 
////		// expires: 0 
////		// pragma: no-cache 
////		// transfer-encoding: chunked 
////		// x-content-type-options: nosniff 
////		// x-frame-options: DENY 
////		// x-xss-protection: 1; mode=block 
////		
////		// Create new JSON Object
////		JsonObject mission = new JsonObject();
////		mission.addProperty("name", "TEST_NEWLY_CREATED_MISSION_BY_RESTput");
////		mission.addProperty("completed", true);
////		
////		preparePut(GET_MISSION_BY_ID_VALID, Collections.singletonMap("id", MISSION_ID_VALID), mission.toString()).then()
////		.statusCode(SC_OK)
////		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
////		.body(matchesJsonSchemaInClasspath(JSON_SCHEMA_PATH_MISSION))
////		.body(containsString("TEST_NEWLY_CREATED_MISSION_BY_RESTput"));
////	}
////	
////	@Test
////	public void checkPutMethod_WhenNotFoundByIdAndInValidJSON() {
////		//JPA Exception
////		
////		// Code Details = 500
////		// Response body
////		// {
////		// "timestamp": "2018-Nov-22 07:01:27",
////		// "httpStatus": "500 INTERNAL_SERVER_ERROR",
////		// "message": "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
////		// "details": "uri=/api/1.0/missions/164"
////		// }
////		// Response headers
////		// cache-control: no-cache, no-store, max-age=0, must-revalidate 
////		// connection: close 
////		// content-type: application/json;charset=UTF-8 
////		// date: Thu, 22 Nov 2018 19:01:27 GMT 
////		// expires: 0 
////		// pragma: no-cache 
////		// transfer-encoding: chunked 
////		// x-content-type-options: nosniff 
////		// x-frame-options: DENY 
////		// x-xss-protection: 1; mode=block 	
////		preparePut(GET_MISSION_BY_ID_VALID, Collections.singletonMap("id", MISSION_ID_INVALID), DUMMY_TEST_JSON.toString()).then()
////		.statusCode(SC_INTERNAL_SERVER_ERROR)
////		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
////		.body(containsString("could not execute statement"));
////	}
////	
////	@Test
////	public void check_Delete_MissionById_WhenUnAuthorized() {
////		prepareDelete(DELETE_MISSION_BY_ID_VALID)
////		//.time(lessThan(ENDPOINT_RESPONSE_TIME))
////		.statusCode(SC_UNAUTHORIZED)
////		.body(containsString("Unauthorized"));
////	}
////	
////	@Test
////	public void check_Delete_MissionById_WhenValid_AndAuthorized() {
////		// {
////		//	 "timestamp": "2018-Nov-21 06:44:24",
////		//	 "httpStatus": "404 NOT_FOUND",
////		//	 "message": "No registered Mission with ID = 1",
////		//	 "details": "uri=/api/1.0/missions/1"
////		// }
////		prepareDeleteSecured(GET_MISSION_BY_ID_INVALID)
////		.statusCode(SC_UNAUTHORIZED)
////		.time(lessThan(ENDPOINT_RESPONSE_TIME))
////		.body(containsString("No registered Mission with ID = 1"));
////	}
//}
//
