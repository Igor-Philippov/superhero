package com.superhero.test.unit.rest;

import static com.superhero.rest.constant.Paths.SUPERHEROS;
import static com.superhero.rest.constant.Paths.SUPERHEROS_BY_FIRSTNAME;
import static com.superhero.rest.constant.Paths.SUPERHEROS_BY_LASTNAME;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_ID;
import static com.superhero.rest.constant.Paths.SUPERHERO_BY_SUPERHERONAME;
import static org.hamcrest.Matchers.hasSize;
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

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superhero.rest.endpoint.SuperHeroEndpoint;
import com.superhero.rest.exception.CustomizedResponseEntityExceptionHandler;
import com.superhero.rest.exception.SuperHeroNotFoundException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SuperHeroEndpointUnitTest extends AbstractEndpointUnitTest {
    
    @MockBean
    private SuperHeroEndpoint superHeroController;
	       
    @Before
    public void setup() {
    	mvc = MockMvcBuilders.standaloneSetup(superHeroController).setControllerAdvice(new CustomizedResponseEntityExceptionHandler()).build();
    }
	        
	@Test
	/** Expected result: A new SuperHero is created and the [location] header of the response contains a link to it by SuperHero ID */
	public void _01_010_post_WhenValidJSON() throws Exception {
		logger.info("CALLING _01_010_post_WhenValidJSON() - BEGIN");
		// Code Details = 201
		// Response headers =
		//   cache-control: no-cache, no-store, max-age=0, must-revalidate 
		//   content-length: 0 
		//   date: Tue, 04 Dec 2018 20:29:27 GMT 
		//   expires: 0 
		//   location: http://localhost:8080/api/1.0/superheros/490 
		//   pragma: no-cache 
		//   x-content-type-options: nosniff 
		//   x-frame-options: DENY 
		//   x-xss-protection: 1; mode=block 
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(superHero1.getId()).toUri();
		given(superHeroController.createSuperHero(superHero1)).willReturn(ResponseEntity.created(location).build());
		
		mvc.perform(post(SUPERHEROS).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(superHero1)))
				.andExpect(status().isCreated())
				.andExpect(header().exists("location"))
				.andExpect(header().string("location", location.toString()));
		verify(superHeroController, times(1)).createSuperHero(superHero1);
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _01_010_post_WhenValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _01_020_post_WhenInValidJSON() throws Exception {
		logger.info("CALLING _01_020_post_WhenMissingJSON() - BEGIN");
		// Code Details = 500	
		// Response body = 
		// {
        //   "timestamp": "2018-Dec-17 04:51:13",
        //   "code": 500,
        //   "error": "Internal Server Error",
        //   "exception": "javax.validation.ConstraintViolationException",
        //   "details": "uri=/api/1.0/superheros",
        //   "messages": [
	    //      "Validation failed for classes [com.superhero.model.SuperHero] during persist time for groups [javax.validation.groups.Default, ]\nList of constraint violations:[\n\tConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=superHeroName, rootBeanClass=class com.superhero.model.SuperHero, messageTemplate='{javax.validation.constraints.NotEmpty.message}'}\n\tConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=firstName, rootBeanClass=class com.superhero.model.SuperHero, messageTemplate='{javax.validation.constraints.NotEmpty.message}'}\n\tConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=lastName, rootBeanClass=class com.superhero.model.SuperHero, messageTemplate='{javax.validation.constraints.NotEmpty.message}'}\n]"
        //   ]
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
		given(superHeroController.createSuperHero(superHero1)).willThrow(new ConstraintViolationException(ERROR_MSG_SUPERHERO_BAD_JSON, null));
		mvc.perform(post(SUPERHEROS).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(superHero1)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.exception").value(ConstraintViolationException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(ERROR_MSG_SUPERHERO_BAD_JSON)));
		verify(superHeroController, times(1)).createSuperHero(superHero1);
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _01_020_post_WhenMissingJSON() - END");
	}
	
	@Test
	/** Expected result: All SuperHeros get retrieved as a valid JSON array */
	public void _02_010_get_SuperHeros() throws Exception {
		logger.info("CALLING _02_010_get_SuperHeros() - BEGIN");
		given(superHeroController.retrieveAllSuperHeros()).willReturn(ResponseEntity.ok().body(Collections.singletonList(superHero1)));
		mvc.perform(get(SUPERHEROS))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(superHero1.getId()))
		        .andExpect(jsonPath("$[0].firstName", is(superHero1.getFirstName())))
		        .andExpect(jsonPath("$[0].lastName", is(superHero1.getLastName())))
		        .andExpect(jsonPath("$[0].superHeroName", is(superHero1.getSuperHeroName())))
				.andExpect(jsonPath("$[0].missions", hasSize(superHero1.getMissions().size())))
				.andExpect(jsonPath("$[0].missions[0].id").value(mission1.getId()))
				.andExpect(jsonPath("$[0].missions[0].name", is(mission1.getName())))
				.andExpect(jsonPath("$[0].missions[0].completed").value(mission1.isCompleted()))
				.andExpect(jsonPath("$[0].missions[1].id").value(mission2.getId()))
				.andExpect(jsonPath("$[0].missions[1].name", is(mission2.getName())))
				.andExpect(jsonPath("$[0].missions[1].completed").value(mission2.isCompleted()));	
		verify(superHeroController, times(1)).retrieveAllSuperHeros();
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_010_get_SuperHeros() - END");
	}
	
	@Test
	/** Expected result: All SuperHeros with [lower(firstName) = lower (:firstName)] get retrieved as a valid JSON array */
	public void _02_020_get_SuperHerosByFirstName_WhenValid() throws Exception {
		logger.info("CALLING _02_020_get_SuperHerosByFirstName_WhenValid() - BEGIN");
		given(superHeroController.retrieveSuperHerosByFirstName(superHero2.getFirstName())).willReturn(ResponseEntity.ok().body(superHeros));
		mvc.perform(get(SUPERHEROS_BY_FIRSTNAME, superHero2.getFirstName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(superHero1.getId()))
		        .andExpect(jsonPath("$[0].firstName", is(superHero1.getFirstName())))
		        .andExpect(jsonPath("$[0].lastName", is(superHero1.getLastName())))
		        .andExpect(jsonPath("$[0].superHeroName", is(superHero1.getSuperHeroName())))
				.andExpect(jsonPath("$[0].missions", hasSize(superHero1.getMissions().size())))
				.andExpect(jsonPath("$[0].missions[0].id").value(mission1.getId()))
				.andExpect(jsonPath("$[0].missions[0].name", is(mission1.getName())))
				.andExpect(jsonPath("$[0].missions[0].completed").value(mission1.isCompleted()))
				.andExpect(jsonPath("$[0].missions[1].id").value(mission2.getId()))
				.andExpect(jsonPath("$[0].missions[1].name", is(mission2.getName())))
				.andExpect(jsonPath("$[0].missions[1].completed").value(mission2.isCompleted()))
				.andExpect(jsonPath("$[1].id").value(superHero2.getId()))
		        .andExpect(jsonPath("$[1].firstName", is(superHero2.getFirstName())))
		        .andExpect(jsonPath("$[1].lastName", is(superHero2.getLastName())))
		        .andExpect(jsonPath("$[1].superHeroName", is(superHero2.getSuperHeroName())))
				.andExpect(jsonPath("$[1].missions", hasSize(superHero2.getMissions().size())))
				.andExpect(jsonPath("$[1].missions[0].id").value(mission3.getId()))
				.andExpect(jsonPath("$[1].missions[0].name", is(mission3.getName())))
				.andExpect(jsonPath("$[1].missions[0].completed").value(mission3.isCompleted()));	
		verify(superHeroController, times(1)).retrieveSuperHerosByFirstName(superHero2.getFirstName());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_020_get_SuperHerosByFirstName_WhenValid() - END");
	}
	
	@Test
	/** Expected result: A valid empty JSON array */
	public void _02_030_get_SuperHerosByFirstName_WhenInValid() throws Exception {
		logger.info("CALLING _02_030_get_SuperHerosByFirstName_WhenInValid() - BEGIN");
		given(superHeroController.retrieveSuperHerosByFirstName(superHero2.getSuperHeroName())).willReturn(ResponseEntity.ok().body(Collections.EMPTY_LIST));
		mvc.perform(get(SUPERHEROS_BY_FIRSTNAME, superHero2.getSuperHeroName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(0));	
		verify(superHeroController, times(1)).retrieveSuperHerosByFirstName(superHero2.getSuperHeroName());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_030_get_SuperHerosByFirstName_WhenInValid() - END");
	}
	
	@Test
	/** Expected result: All SuperHeros with [lower(lastName) = lower (:lastName)] get retrieved as a valid JSON array */
	public void _02_040_get_SuperHerosByLastName_WhenValid() throws Exception {
		logger.info("CALLING _02_040_get_SuperHerosByLastName_WhenValid() - BEGIN");
		given(superHeroController.retrieveSuperHerosByLastName(superHero2.getLastName())).willReturn(ResponseEntity.ok().body(superHeros));
		mvc.perform(get(SUPERHEROS_BY_LASTNAME, superHero2.getLastName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(superHero1.getId()))
		        .andExpect(jsonPath("$[0].firstName", is(superHero1.getFirstName())))
		        .andExpect(jsonPath("$[0].lastName", is(superHero1.getLastName())))
		        .andExpect(jsonPath("$[0].superHeroName", is(superHero1.getSuperHeroName())))
				.andExpect(jsonPath("$[0].missions", hasSize(superHero1.getMissions().size())))
				.andExpect(jsonPath("$[0].missions[0].id").value(mission1.getId()))
				.andExpect(jsonPath("$[0].missions[0].name", is(mission1.getName())))
				.andExpect(jsonPath("$[0].missions[0].completed").value(mission1.isCompleted()))
				.andExpect(jsonPath("$[0].missions[1].id").value(mission2.getId()))
				.andExpect(jsonPath("$[0].missions[1].name", is(mission2.getName())))
				.andExpect(jsonPath("$[0].missions[1].completed").value(mission2.isCompleted()))
				.andExpect(jsonPath("$[1].id").value(superHero2.getId()))
		        .andExpect(jsonPath("$[1].firstName", is(superHero2.getFirstName())))
		        .andExpect(jsonPath("$[1].lastName", is(superHero2.getLastName())))
		        .andExpect(jsonPath("$[1].superHeroName", is(superHero2.getSuperHeroName())))
				.andExpect(jsonPath("$[1].missions", hasSize(superHero2.getMissions().size())))
				.andExpect(jsonPath("$[1].missions[0].id").value(mission3.getId()))
				.andExpect(jsonPath("$[1].missions[0].name", is(mission3.getName())))
				.andExpect(jsonPath("$[1].missions[0].completed").value(mission3.isCompleted()));	
		verify(superHeroController, times(1)).retrieveSuperHerosByLastName(superHero2.getLastName());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_040_get_SuperHerosByLastName_WhenValid() - END");
	}
	
	@Test
	/** Expected result: A valid empty JSON array */
	public void _02_050_get_SuperHerosByLastName_WhenInValid() throws Exception {
		logger.info("CALLING _02_050_get_SuperHerosByLastName_WhenInValid() - BEGIN");
		given(superHeroController.retrieveSuperHerosByLastName(superHero2.getSuperHeroName())).willReturn(ResponseEntity.ok().body(Collections.EMPTY_LIST));
		mvc.perform(get(SUPERHEROS_BY_LASTNAME, superHero2.getSuperHeroName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.length()").value(0));	
		verify(superHeroController, times(1)).retrieveSuperHerosByLastName(superHero2.getSuperHeroName());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_050_get_SuperHerosByLastName_WhenInValid() - END");
	}
		
	@Test
	/** Expected result: SuperHero as JSON object retrieved by ID */
	public void _02_060_get_SuperHeroById_WhenValid() throws Exception {
		logger.info("CALLING _02_060_get_SuperHeroById_WhenValid() - BEGIN");
		// MockHttpServletRequest:
		// 		      HTTP Method = GET
		// 		      Request URI = /api/1.0/superheros/-100
		// 		       Parameters = {}
		// 		          Headers = {}
		// 		             Body = <no character encoding set>
		// 		    Session Attrs = {}
		// 
		// 		MockHttpServletResponse:
		// 		           Status = 200
		// 		    Error message = null
		// 		          Headers = {Content-Type=[application/json;charset=UTF-8], X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY]}
		// 		     Content type = application/json;charset=UTF-8
		// 		             Body = {"id":-100,"firstName":"firstName_NEW","lastName":"lastNameNEW","superHeroName":"superHeroName_NEW@shc.com","missions":[{"id":-1,"name":"missionName1","completed":false},{"id":-2,"name":"missionName2","completed":false}]}
		// 		    Forwarded URL = null
		// 		   Redirected URL = null
		// 		          Cookies = []
		given(superHeroController.retrieveSuperHeroById(superHero1.getId())).willReturn(ResponseEntity.ok().body(superHero1));   	
		mvc.perform(get(SUPERHERO_BY_ID, superHero1.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id").value(superHero1.getId()))
				.andExpect(jsonPath("$.firstName", is(superHero1.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(superHero1.getLastName())))
				.andExpect(jsonPath("$.superHeroName", is(superHero1.getSuperHeroName())))
				.andExpect(jsonPath("$.missions", hasSize(superHero1.getMissions().size())));
		verify(superHeroController, times(1)).retrieveSuperHeroById(superHero1.getId());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_060_get_SuperHeroById_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Super Hero with ID = ] text in the [message] header on the response */
	public void _02_070_get_SuperHeroById_WhenInvalid() throws Exception {
		logger.info("CALLING _02_070_get_SuperHeroById_WhenInvalid() - BEGIN");
		// Code Details = 404
		// Response body = 
		// {
		//   "timestamp": "2018-Dec-17 05:11:52",
		//   "code": 404,
		//   "error": "Not Found",
		//   "exception": "com.superhero.rest.exception.SuperHeroNotFoundException",
		//   "details": "uri=/api/1.0/superheros/0",
		//   "messages": [
		//      "No registered Super Hero with ID = 0"
		//   ]
		// {
		given(superHeroController.retrieveSuperHeroById(superHero1.getId())).willThrow(new SuperHeroNotFoundException(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_ID_GET, superHero1.getId())));
		mvc.perform(get(SUPERHERO_BY_ID, superHero1.getId())).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(NOT_FOUND.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
				.andExpect(jsonPath("$.exception").value(SuperHeroNotFoundException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS + "/" + superHero1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_ID_GET, superHero1.getId()))));
		verify(superHeroController, times(1)).retrieveSuperHeroById(superHero1.getId());
		verifyNoMoreInteractions(superHeroController);
		logger.info("CALLING _02_070_get_SuperHeroById_WhenInvalid() - END");
	}

	@Test
	/** Expected result: SuperHero as JSON object retrieved by a unique SuperHeroName */
	public void _02_080_get_SuperHeroBySuperHeroName_WhenValid() throws Exception {
		// MockHttpServletRequest:
		// 		      HTTP Method = GET
		// 		      Request URI = /api/1.0/superheros/-100
		// 		       Parameters = {}
		// 		          Headers = {}
		// 		             Body = <no character encoding set>
		// 		    Session Attrs = {}
		// 
		// 		MockHttpServletResponse:
		// 		           Status = 200
		// 		    Error message = null
		// 		          Headers = {Content-Type=[application/json;charset=UTF-8], X-Content-Type-Options=[nosniff], X-XSS-Protection=[1; mode=block], Cache-Control=[no-cache, no-store, max-age=0, must-revalidate], Pragma=[no-cache], Expires=[0], X-Frame-Options=[DENY]}
		// 		     Content type = application/json;charset=UTF-8
		// 		             Body = {"id":-100,"firstName":"firstName_NEW","lastName":"lastNameNEW","superHeroName":"superHeroName_NEW@shc.com","missions":[{"id":-1,"name":"missionName1","completed":false},{"id":-2,"name":"missionName2","completed":false}]}
		// 		    Forwarded URL = null
		// 		   Redirected URL = null
		// 		          Cookies = []
		logger.info("CALLING _02_080_get_SuperHeroBySuperHeroName_WhenValid() - BEGIN");
		given(superHeroController.retrieveSuperHeroBySuperHeroName(superHero1.getSuperHeroName())).willReturn(ResponseEntity.ok().body(superHero1));   	
		mvc.perform(get(SUPERHERO_BY_SUPERHERONAME, superHero1.getSuperHeroName()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(superHero1.getId()))
				.andExpect(jsonPath("$.firstName", is(superHero1.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(superHero1.getLastName())))
				.andExpect(jsonPath("$.superHeroName", is(superHero1.getSuperHeroName())))
				.andExpect(jsonPath("$.missions", hasSize(superHero1.getMissions().size())));
		verify(superHeroController, times(1)).retrieveSuperHeroBySuperHeroName(superHero1.getSuperHeroName());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_080_get_SuperHeroBySuperHeroName_WhenValid() - BEGIN");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Super Hero by a superHeroName = ] text in the [message] header on the response */
	public void _02_090_get_SuperHeroBySuperHeroName_WhenInValid() throws Exception {
		logger.info("CALLING _02_090_get_SuperHeroBySuperHeroName_WhenInValid() - BEGIN");
		// Code Details = 404
		// Response body = 
		// {
		//   "timestamp": "2018-Dec-17 08:34:12",
		//   "code": 404,
		//   "error": "Not Found",
		//   "exception": "com.superhero.rest.exception.SuperHeroNotFoundException",
		//   "details": "uri=/api/1.0/superheros/names/superhero/QQQ",
		//   "messages": [
		//      "No registered Super Hero by a superHeroName = QQQ"
		//   ]
		// {
		given(superHeroController.retrieveSuperHeroBySuperHeroName(superHero1.getSuperHeroName())).willThrow(new SuperHeroNotFoundException(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_SUPERHERO_NAME_GET, superHero1.getSuperHeroName())));
		mvc.perform(get(SUPERHERO_BY_SUPERHERONAME, superHero1.getSuperHeroName()))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(NOT_FOUND.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
				.andExpect(jsonPath("$.exception").value(SuperHeroNotFoundException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS + "/names/superhero/" + superHero1.getSuperHeroName()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_SUPERHERO_NAME_GET, superHero1.getSuperHeroName()))));
		verify(superHeroController, times(1)).retrieveSuperHeroBySuperHeroName(superHero1.getSuperHeroName());
		verifyNoMoreInteractions(superHeroController);  
		logger.info("CALLING _02_090_get_SuperHeroBySuperHeroName_WhenInValid() - BEGIN");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_010_put_WhenFoundByIdAndValidJSON() throws Exception {
		logger.info("CALLING _03_010_put_WhenFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 200	
		// Response body =
		// {
		//   "id": 15,
		//   "firstName": "Fomba",
		//   "lastName": "Mamadou",
		//   "superHeroName": "fomba.mamadou@shc.com",
		//   "missions": []
		// }
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
		given(superHeroController.updateSuperHero(superHero1Updated, superHero1.getId())).willReturn(ResponseEntity.ok().body(superHero1Updated));
		mvc.perform(put(SUPERHERO_BY_ID, superHero1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(superHero1Updated)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id").value(superHero1Updated.getId()))
				.andExpect(jsonPath("$.firstName", is(superHero1Updated.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(superHero1Updated.getLastName())))
				.andExpect(jsonPath("$.superHeroName", is(superHero1Updated.getSuperHeroName())))
				.andExpect(jsonPath("$.missions", hasSize(superHero1Updated.getMissions().size())));
		verify(superHeroController, times(1)).updateSuperHero(superHero1Updated, superHero1.getId());
		verifyNoMoreInteractions(superHeroController); 
		logger.info("CALLING _03_010_put_WhenFoundByIdAndValidJSON() - END");
	}

	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_020_put_WhenFoundByIdAndInValidJSON() throws Exception {
		logger.info("CALLING _03_020_put_WhenFoundByIdAndInValidJSON() - BEGIN");
		// Code Details = 500
		// Response body
		// {
		//   "timestamp": "2018-Dec-17 08:40:53",
		//   "code": 500,
		//   "error": "Internal Server Error",
		//   "exception": "org.springframework.transaction.TransactionSystemException",
		//   "details": "uri=/api/1.0/superheros/5",
		//   "messages": [
		//      "Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction"
		//   ]
		// }
		given(superHeroController.updateSuperHero(superHero1, superHero1.getId())).willThrow(new TransactionSystemException(ERROR_MSG_SUPERHERO_BAD_JSON_PUT));
		mvc.perform(put(SUPERHERO_BY_ID, superHero1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(superHero1)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.exception").value(TransactionSystemException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS + "/" + superHero1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(ERROR_MSG_SUPERHERO_BAD_JSON_PUT)));
		verify(superHeroController, times(1)).updateSuperHero(superHero1, superHero1.getId());
		verifyNoMoreInteractions(superHeroController); 
		logger.info("CALLING _03_020_put_WhenFoundByIdAndInValidJSON() - END");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No registered Super Hero with ID = 9 available for update] text in the [message] header on the response */
	public void _03_030_put_WhenNotFoundByIdAndValidJSON() throws Exception {
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndValidJSON() - BEGIN");
		// Code Details = 404	
		// Response body = 
		// {
		//   "timestamp": "2018-Dec-17 08:21:50",
		//   "code": 404,
		//   "error": "Not Found",
		//   "exception": "com.superhero.rest.exception.SuperHeroNotFoundException",
		//   "details": "uri=/api/1.0/superheros/0",
		//   "messages": [
		//      "No registered Super Hero with ID = 0 available for update"
		//   ]
		// }
		given(superHeroController.updateSuperHero(superHero1, superHero1.getId())).willThrow(new SuperHeroNotFoundException(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_ID_PUT, superHero1.getId())));
		mvc.perform(put(SUPERHERO_BY_ID, superHero1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(superHero1)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(NOT_FOUND.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
				.andExpect(jsonPath("$.exception").value(SuperHeroNotFoundException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS + "/" + superHero1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_ID_PUT, superHero1.getId()))));
		verify(superHeroController, times(1)).updateSuperHero(superHero1, superHero1.getId());
		verifyNoMoreInteractions(superHeroController); 
		logger.info("CALLING _03_030_put_WhenNotFoundByIdAndValidJSON() - END");
	}
	
	@Test
	/** Expected result: A server exception is thrown and the response body contains its description */
	public void _03_040_put_WhenNotFoundByIdAndInValidJSON() throws Exception {
		logger.info("CALLING _03_040_put_WhenNotFoundByIdAndInValidJSON() - BEGIN");
		// Code Details = 404	
		// Response body = 
		// {
		//   "timestamp": "2018-Dec-17 08:21:50",
		//   "code": 404,
		//   "error": "Not Found",
		//   "exception": "com.superhero.rest.exception.SuperHeroNotFoundException",
		//   "details": "uri=/api/1.0/superheros/0",
		//   "messages": [
		//      "No registered Super Hero with ID = 0 available for update"
		//   ]
		// }
		given(superHeroController.updateSuperHero(superHero1, superHero1.getId())).willThrow(new SuperHeroNotFoundException(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_ID_PUT, superHero1.getId())));
		mvc.perform(put(SUPERHERO_BY_ID, superHero1.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(superHero1)))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(NOT_FOUND.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
				.andExpect(jsonPath("$.exception").value(SuperHeroNotFoundException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS + "/" + superHero1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_BY_ID_PUT, superHero1.getId()))));
		verify(superHeroController, times(1)).updateSuperHero(superHero1, superHero1.getId());
		verifyNoMoreInteractions(superHeroController); 
		logger.info("CALLING _03_040_put_WhenNotFoundByIdAndInValidJSON() - END");
	}

	@Test
	/** Expected result: Provided the user is unauthorized - HTTP 401 Unauthorized code returned by server */
	//@WithMockUser(username = "", password = "", roles = "")
	public void _04_010_delete_SuperHeroById_WhenUnAuthorized() throws Exception {
		logger.info("CALLING _04_010_delete_SuperHeroById_WhenUnAuthorized() - BEGIN");
		given(superHeroController.deleteSuperHero(superHero1.getId())).willReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());   
        mvc.perform(delete(SUPERHERO_BY_ID, superHero1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
		verify(superHeroController, times(1)).deleteSuperHero(superHero1.getId());
		verifyNoMoreInteractions(superHeroController);
        logger.info("CALLING _04_010_delete_SuperHeroById_WhenUnAuthorized() - END");
	}
	
	@Test
	/** Expected result: Provided the user is authorized - SuperHero with the specified ID will be deleted */
	public void _04_020_delete_SuperHeroById_WhenValid_AndAuthorized() throws Exception {
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
		given(superHeroController.deleteSuperHero(superHero1.getId())).willReturn(ResponseEntity.noContent().build()); 
        mvc.perform(delete(SUPERHERO_BY_ID, superHero1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
		verify(superHeroController, times(1)).deleteSuperHero(superHero1.getId());
		verifyNoMoreInteractions(superHeroController);
		logger.info("CALLING _04_020_delete_SuperHeroById_WhenValid_AndAuthorized() - END");
	}
	
	@Test
	/** Expected result: HTTP 404 Not Found code returned by server and [No class com.superhero.model.SuperHero entity with id N exists!] text in the [message] header on the response */
	public void _04_030_delete_SuperHeroById_WhenInValidOrAlreadyDeleted_AndAuthorized() throws Exception {
		logger.info("CALLING _04_030_delete_SuperHeroById_WhenInValidOrAlreadyDeleted_AndAuthorized() - BEGIN");
		// Code Details = 500
		// Response body = 
		// {
		//   "timestamp": "2018-Dec-17 04:59:25",
		//   "code": 500,
		//   "error": "Internal Server Error",
		//   "exception": "org.springframework.dao.EmptyResultDataAccessException",
		//   "details": "uri=/api/1.0/superheros/0",
		//   "messages": [
		//      "No class com.superhero.model.SuperHero entity with id 0 exists!"
		//   ]
		// }
		given(superHeroController.deleteSuperHero(superHero1.getId())).willThrow(new EmptyResultDataAccessException(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_DELETE, superHero1.getId()), 0)); 
        mvc.perform(delete(SUPERHERO_BY_ID, superHero1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.error", is(INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(jsonPath("$.code").value(INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.exception").value(EmptyResultDataAccessException.class.getName()))
				.andExpect(jsonPath("$.details").value("uri=" + SUPERHEROS + "/" + superHero1.getId()))
				.andExpect(jsonPath("$.messages.length()").value(1))
				.andExpect(jsonPath("$.messages[0]", is(String.format(ERROR_MSG_SUPERHERO_NOT_FOUND_DELETE, superHero1.getId()))));
		verify(superHeroController, times(1)).deleteSuperHero(superHero1.getId());
		verifyNoMoreInteractions(superHeroController);
		logger.info("CALLING _04_030_delete_SuperHeroById_WhenInValidOrAlreadyDeleted_AndAuthorized() - END");
	}
}
