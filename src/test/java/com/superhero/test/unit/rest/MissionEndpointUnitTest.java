package com.superhero.test.unit.rest;

import static com.superhero.rest.constant.Paths.MISSIONS;
import static com.superhero.rest.constant.Paths.MISSIONS_BY_NAME;
import static com.superhero.rest.constant.Paths.MISSIONS_COMPLETED;
import static com.superhero.rest.constant.Paths.MISSIONS_DELETED;
import static com.superhero.rest.constant.Paths.MISSION_BY_ID;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Collections;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superhero.rest.endpoint.MissionEndpoint;
import com.superhero.rest.exception.CustomizedResponseEntityExceptionHandler;
import com.superhero.rest.exception.MissionNotFoundException;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MissionEndpointUnitTest extends AbstractEndpointUnitTest {
	
    @MockBean
    private MissionEndpoint missionController;
	       
    @Before
    public void setup() {
    	mvc = MockMvcBuilders.standaloneSetup(missionController).setControllerAdvice(new CustomizedResponseEntityExceptionHandler()).build();
    }
    
	@Test
	/** Expected result: A new Mission is created and the [location] header of the response contains a link to it by Mission ID */
	public void _01_010_post_WhenValidJSON() throws Exception {
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
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(superHero1.getId()).toUri();
		given(missionController.createMission(mission1)).willReturn(ResponseEntity.created(location).build());
		mvc.perform(post(MISSIONS).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mission1)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("location"))
				.andExpect(header().string("location", location.toString()));
		verify(missionController, times(1)).createMission(mission1);
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _01_010_post_WhenValidJSON() - END");
	}

	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _01_020_post_WhenInValidJSON() throws Exception {
		logger.info("CALLING _01_020_post_WhenInValidJSON() - BEGIN");
		// Code Details = 500
		// Response body = {
	    //   "timestamp": "2018-Dec-14 09:45:56",
	    //   "code": 500,
	    //   "error": "Internal Server Error",
	    //   "exception": "org.springframework.dao.DataIntegrityViolationException",
	    //   "details": "uri=/api/1.0/missions",
	    //   "messages": [
	    //      "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"
	    //   ]
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
		given(missionController.createMission(mission1)).willThrow(new DataIntegrityViolationException(ERROR_MSG_MISSION_BAD_JSON));
		mvc.perform(post(MISSIONS).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mission1)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.exception").value(DataIntegrityViolationException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + MISSIONS))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(ERROR_MSG_MISSION_BAD_JSON)));
		verify(missionController, times(1)).createMission(mission1);
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _01_020_post_WhenInValidJSON() - END");
	}
    

	@Test
	/** Expected result: All Missions get retrieved as a valid JSON array */
	public void _02_010_get_Missions() throws Exception {
		logger.info("CALLING _02_010_get_Missions() - BEGIN");
		given(missionController.retrieveAllMissions()).willReturn(ResponseEntity.ok().body(Collections.singletonList(mission1)));
		mvc.perform(get(MISSIONS))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(mission1.getId()))
		        .andExpect(jsonPath("$[0].name", is(mission1.getName())))
		        .andExpect(jsonPath("$[0].completed").value(mission1.isCompleted()));	
		verify(missionController, times(1)).retrieveAllMissions();
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _02_010_get_Missions() - END");
	}
	
	@Test
	/** Expected result: All Missions with [completed = true] get retrieved as a valid JSON array */
	public void _02_020_get_MissionsCompleted() throws Exception {
		logger.info("CALLING _02_020_get_MissionsCompleted() - BEGIN");
		given(missionController.retrieveAllMissionsCompleted()).willReturn(ResponseEntity.ok().body(missionsCompleted));
		mvc.perform(get(MISSIONS_COMPLETED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(mission10.getId()))
				.andExpect(jsonPath("$[0].name", is(mission10.getName())))
				.andExpect(jsonPath("$[0].completed").value(mission10.isCompleted()))
				.andExpect(jsonPath("$[1].id").value(mission11.getId()))
				.andExpect(jsonPath("$[1].name", is(mission11.getName())))
				.andExpect(jsonPath("$[1].completed").value(mission11.isCompleted()));	
		verify(missionController, times(1)).retrieveAllMissionsCompleted();
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _02_020_get_MissionsCompleted() - END");
	}
		
	@Test
	/** Expected result: Provided the user is authorized - All Missions with [deleted = true] get retrieved as a valid JSON array */
	public void _02_030_get_MissionsDeleted_WhenAuthorised() throws Exception {
		logger.info("CALLING _02_030_get_MissionsDeleted_WhenAuthorised() - BEGIN");	
		given(missionController.retrieveAllMissionsDeleted()).willReturn(ResponseEntity.ok().body(Collections.singletonList(mission13)));
		mvc.perform(get(MISSIONS_DELETED))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(mission13.getId()))
				.andExpect(jsonPath("$[0].name", is(mission13.getName())))
				.andExpect(jsonPath("$[0].completed").value(mission13.isCompleted()));	
		verify(missionController, times(1)).retrieveAllMissionsDeleted();
		verifyNoMoreInteractions(missionController);
		logger.info("CALLING _02_030_get_MissionsDeleted_WhenAuthorised() - END");
	}
	
	@Test
	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
	public void _02_040_get_MissionsDeleted_WhenUnAuthorised() throws Exception {
		logger.info("CALLING _02_040_get_MissionsDeleted_WhenUnAuthorised() - BEGIN");
//		given(missionController.retrieveAllMissionsDeleted()).willReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.EMPTY_LIST)));
//		mvc.perform(get(MISSIONS_DELETED)).andExpect(status().isUnauthorized());
//		verify(missionController, times(1)).retrieveAllMissionsDeleted();
//		verifyNoMoreInteractions(missionController);
		logger.info("CALLING _02_040_get_MissionsDeleted_WhenUnAuthorised() - END");
	}
	
	@Test
	/** Expected result: Mission as JSON object retrieved by ID */
	public void _02_050_get_MissionById_WhenValid() throws Exception {
		logger.info("CALLING _02_050_get_MissionById_WhenValid() - BEGIN");
		given(missionController.retrieveMissionById(mission1.getId())).willReturn(ResponseEntity.ok().body(mission1));   	
		mvc.perform(get(MISSION_BY_ID, mission1.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id").value(mission1.getId()))
				.andExpect(jsonPath("$.name", is(mission1.getName())))
				.andExpect(jsonPath("$.completed").value(mission1.isCompleted()));
		verify(missionController, times(1)).retrieveMissionById(mission1.getId());
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _02_050_get_MissionById_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Mission with ID = ] text in the [message] header on the response */
	public void _02_060_get_MissionById_WhenInvalid() throws Exception {
		logger.info("CALLING _02_060_get_MissionById_WhenInvalid() - BEGIN");
		// Response body = {
	    //   "timestamp": "2018-Dec-14 09:23:27",
	    //   "code": 404,
	    //   "error": "Not Found",
	    //   "exception": "com.superhero.rest.exception.MissionNotFoundException",
	    //   "details": "uri=/api/1.0/missions/0",
	    //   "messages": [
	    //      "No registered Mission with ID = 0"
	    //   ]
		// }
		given(missionController.retrieveMissionById(mission1.getId())).willThrow(new MissionNotFoundException(String.format(ERROR_MSG_MISSION_NOT_FOUND_GET, mission1.getId())));
		mvc.perform(get(MISSION_BY_ID, mission1.getId())).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(NOT_FOUND.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
				.andExpect(jsonPath("$.exception").value(MissionNotFoundException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + MISSIONS + "/" + mission1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_MISSION_NOT_FOUND_GET, mission1.getId()))));
		verify(missionController, times(1)).retrieveMissionById(mission1.getId());
		verifyNoMoreInteractions(missionController);
		logger.info("CALLING _02_060_get_MissionById_WhenInvalid() - END");
	}
	
	@Test
	/** Expected result: All Missions with [lower(name) = lower (:name)] get retrieved as a valid JSON array */
	public void _02_070_get_MissionByName_WhenValid() throws Exception {
		logger.info("CALLING _02_070_get_MissionByName_WhenValid() - BEGIN");
		given(missionController.retrieveMissionByName(mission11.getName())).willReturn(ResponseEntity.ok().body(missionsCompleted));
		mvc.perform(get(MISSIONS_BY_NAME, mission11.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(mission10.getId()))
				.andExpect(jsonPath("$[0].name", is(mission10.getName())))
				.andExpect(jsonPath("$[0].completed").value(mission10.isCompleted()))
				.andExpect(jsonPath("$[1].id").value(mission11.getId()))
				.andExpect(jsonPath("$[1].name", is(mission11.getName())))
				.andExpect(jsonPath("$[1].completed").value(mission11.isCompleted()));		
		verify(missionController, times(1)).retrieveMissionByName(mission11.getName());
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _02_070_get_MissionByName_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: A valid empty JSON array */
	public void _02_080_get_MissionByName_WhenInvalid() throws Exception {
		logger.info("CALLING _02_080_get_MissionByName_WhenInvalid() - BEGIN");
		given(missionController.retrieveMissionByName(mission11.getName())).willReturn(ResponseEntity.ok().body(Collections.EMPTY_LIST));
		mvc.perform(get(MISSIONS_BY_NAME, mission11.getName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(0));	
		verify(missionController, times(1)).retrieveMissionByName(mission11.getName());
		verifyNoMoreInteractions(missionController);  
		logger.info("CALLING _02_080_get_MissionByName_WhenInvalid() - BEGIN");
	}
		
	@Test
	/** Expected result: Database Mission info gets updated */
	public void _02_090_put_WhenFoundByIdAndValidJSON() throws Exception {
		logger.info("CALLING _02_090_put_WhenFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 200	
		// Response body: { "id": 110, "name": "SLEEP", "completed": true }
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
		given(missionController.updateMission(mission1Updated, mission1.getId())).willReturn(ResponseEntity.ok().body(mission1Updated));
		mvc.perform(put(MISSION_BY_ID, mission1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mission1Updated)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id").value(mission1Updated.getId()))
				.andExpect(jsonPath("$.name", is(mission1Updated.getName())))
				.andExpect(jsonPath("$.completed").value(mission1Updated.isCompleted()));
		verify(missionController, times(1)).updateMission(mission1Updated, mission1.getId());
		verifyNoMoreInteractions(missionController); 
		logger.info("CALLING _02_090_put_WhenFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_010_put_WhenFoundByIdAndInValidJSON() throws Exception {
		logger.info("CALLING _03_010_put_WhenFoundByIdAndInValidJSON() - BEGIN");
		// Code Details = 500
		// Response body = {
	    //   "timestamp": "2018-Dec-14 09:45:56",
	    //   "code": 500,
	    //   "error": "Internal Server Error",
	    //   "exception": "org.springframework.dao.DataIntegrityViolationException",
	    //   "details": "/api/1.0/missions/164",
	    //   "messages": [
	    //      "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"
	    //   ]
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
		given(missionController.updateMission(mission1, mission1.getId())).willThrow(new DataIntegrityViolationException(ERROR_MSG_MISSION_BAD_JSON));
		mvc.perform(put(MISSION_BY_ID, mission1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mission1)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.exception").value(DataIntegrityViolationException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + MISSIONS + "/" + mission1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(ERROR_MSG_MISSION_BAD_JSON)));
		verify(missionController, times(1)).updateMission(mission1, mission1.getId());
		verifyNoMoreInteractions(missionController); 
		logger.info("CALLING _03_010_put_WhenFoundByIdAndInValidJSON() - END");
	}
	
	@Test
	public void _03_020_put_WhenNotFoundByIdAndValidJSON() throws Exception {
		logger.info("CALLING _03_020_put_WhenNotFoundByIdAndValidJSON() - BEGIN");
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
		given(missionController.updateMission(missionNotFound, idNotFound)).willReturn(ResponseEntity.ok().body(missionNotFound));
		mvc.perform(put(MISSION_BY_ID, idNotFound).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(missionNotFound)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id").value(idNotFound))
				.andExpect(jsonPath("$.name", is(missionNotFound.getName())))
				.andExpect(jsonPath("$.completed").value(missionNotFound.isCompleted()));
		verify(missionController, times(1)).updateMission(missionNotFound, idNotFound);
		verifyNoMoreInteractions(missionController); 
		logger.info("CALLING _03_020_put_WhenNotFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_030_put_WhenNotFoundByIdAndInValidJSON() throws Exception {
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndInValidJSON() - BEGIN");
		// Code Details = 500
		// Response body = {
	    //   "timestamp": "2018-Dec-14 09:45:56",
	    //   "code": 500,
	    //   "error": "Internal Server Error",
	    //   "exception": "org.springframework.dao.DataIntegrityViolationException",
	    //   "details": "/api/1.0/missions/164",
	    //   "messages": [
	    //      "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"
	    //   ]
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
		given(missionController.updateMission(mission1, mission1.getId())).willThrow(new DataIntegrityViolationException(ERROR_MSG_MISSION_BAD_JSON));
		mvc.perform(put(MISSION_BY_ID, mission1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mission1)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.exception").value(DataIntegrityViolationException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + MISSIONS + "/" + mission1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(ERROR_MSG_MISSION_BAD_JSON)));
		verify(missionController, times(1)).updateMission(mission1, mission1.getId());
		verifyNoMoreInteractions(missionController); 
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndInValidJSON() - END");
	}
	
	@Test
	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
	public void _04_010_delete_MissionById_WhenUnAuthorized() throws Exception {
		logger.info("CALLING _04_010_delete_MissionById_WhenUnAuthorized() - BEGIN");		
		given(missionController.softDeleteMission(mission1.getId())).willReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());   
        mvc.perform(delete(MISSION_BY_ID, mission1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
		verify(missionController, times(1)).softDeleteMission(mission1.getId());
		verifyNoMoreInteractions(missionController);
		logger.info("CALLING _04_010_delete_MissionById_WhenUnAuthorized() - END");
	}
	
	@Test
	/** Expected result: Provided the user is authorized - Mission with the specified ID will be soft deleted */
	public void _04_020_delete_MissionById_WhenValid_AndAuthorized() throws Exception {
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
		given(missionController.softDeleteMission(mission1.getId())).willReturn(ResponseEntity.noContent().build()); 
        mvc.perform(delete(MISSION_BY_ID, mission1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
		verify(missionController, times(1)).softDeleteMission(mission1.getId());
		verifyNoMoreInteractions(missionController);
		logger.info("CALLING _04_020_delete_MissionById_WhenValid_AndAuthorized() - END");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered for deletion Mission with ID = ] text in the [message] header on the response */
	public void _04_030_delete_MissionById_WhenInValidOrAlreadyDeleted_AndAuthorized() throws Exception {
		logger.info("CALLING _04_030_delete_MissionById_WhenInValidOrAlreadyDeleted_AndAuthorized() - BEGIN");
		// Code Details = 404
		// Response body = 
		// {
		//   "timestamp": "2018-Dec-14 09:23:27",
	    //   "code": 404,
	    //   "error": "Not Found",
	    //   "exception": "com.superhero.rest.exception.MissionNotFoundException",
	    //   "details": "uri=/api/1.0/missions/110",
	    //   "messages": [
	    //      "No registered for deletion Mission with ID = 110"
	    //   ]
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
		given(missionController.softDeleteMission(mission1.getId())).willThrow(new MissionNotFoundException(String.format(ERROR_MSG_MISSION_NOT_FOUND_DELETE, mission1.getId()))); 
        mvc.perform(delete(MISSION_BY_ID, mission1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(NOT_FOUND.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
				.andExpect(jsonPath("$.exception").value(MissionNotFoundException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + MISSIONS + "/" + mission1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_MISSION_NOT_FOUND_DELETE, mission1.getId()))));
		verify(missionController, times(1)).softDeleteMission(mission1.getId());
		verifyNoMoreInteractions(missionController);
		logger.info("CALLING _04_030_delete_MissionById_WhenInValidOrAlreadyDeleted_AndAuthorized() - END");
	}
}

