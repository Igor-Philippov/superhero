package com.superhero.rest;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.superhero.SuperHeroApi;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuperHeroApi.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseTest {

	@LocalServerPort
	private int port;
	
    private final String VERSION = "/api/1.0/";
	
    /** http://localhost:8080/api/1.0/ */
	private String HOST_ROOT = "http://localhost:8080" + VERSION; 
	
    /** superheros/ */
    protected static final String SUPERHEROS = "superheros";
    
    /** superheros/{id} */
    protected static final String SUPERHERO_BY_ID = SUPERHEROS + "/{id}";
    
    /** superheros/names/superhero/{superHeroName} */
    protected static final String SUPERHERO_BY_SUPERHERONAME = SUPERHEROS + "/names/superhero/{superHeroName}";
    
    /** superheros/names/first/{firstName} */
    protected static final String SUPERHEROS_BY_FIRSTNAME = SUPERHEROS + "/names/first/{firstName}";
    
    /** superheros/names/last/{lastName} */
    protected static final String SUPERHEROS_BY_LASTNAME = SUPERHEROS + "/names/last/{lastName}";
    
    /** missions/ */
    protected static final String MISSIONS = "missions";
	
    /** missions/{id} */
    protected static final String MISSION_BY_ID = MISSIONS + "/{id}";
    
    /** missions/name/{name} */
    protected static final String MISSION_BY_NAME = MISSIONS + "/name/{name}";
    
    /** missions/completed/ */
    protected static final String MISSIONS_COMPLETED = MISSIONS + "/completed";
    
    /** missions/deleted/ */
    protected static final String MISSIONS_DELETED = MISSIONS + "/deleted";
    
    protected static final String DUMMY_TEST_JSON = "{ \"Test\": \"Test\" }";

//	static final String ARRIVAL_ALL = "arrival/all";
//	static final String ARRIVAL_ALL_BY_ID = "arrival/1";
//	static final String DEPARTURE_ALL = "departure/all";
//	static final String DEPARTURE_ALL_BY_ID = "departure/1";
//	static final String FLIGHT_ALL = "flight/all";
//	static final String FLIGHT_ALL_BY_ID = "flight/1";
//	static final String USERS_ALL = "users/all";
//	static final String USERS_ALL_BY_ID = "users/1";
	protected static final long ENDPOINT_RESPONSE_TIME = 200L;

	private static final String USER_NAME = "blaze";
	private static final String PASSWORD = "Q1w2e3r4";

	protected ValidatableResponse prepareGet(String path) {
		return prepareGetDeleteWhen().get(HOST_ROOT + path).then();
	}

	protected ValidatableResponse prepareDelete(String path) {
		return prepareGetDeleteWhen().delete(HOST_ROOT + path).then();
	}

	protected Response preparePut(String path, String body) {
		return preparePostPutWhen(body).put(HOST_ROOT + path);
	}

	protected Response preparePost(String path, String body) {
		return preparePostPutWhen(body).post(HOST_ROOT + path);
	}

	private RequestSpecification preparePostPutWhen(String body) {
		return given().port(port).auth().basic(USER_NAME, PASSWORD).contentType(String.valueOf(APPLICATION_JSON))
				.body(body).when();
	}

	private RequestSpecification prepareGetDeleteWhen() {
		return given().port(port).auth().basic(USER_NAME, PASSWORD).when();
	}
}