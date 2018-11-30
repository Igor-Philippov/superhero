package com.superhero.rest;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
	
	protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

	@LocalServerPort
	private int port;

	private static final String VERSION = "/api/1.0/";

	/// ** http://localhost:8080/api/1.0/ */
	private static final String HOST_ROOT = "http://localhost" + VERSION;

	/** superheros/ */
	protected static final String SUPERHEROS = "superheros";
	
	protected static final Long SUPERHERO_ID_VALID = 1L;
	protected static final Long SUPERHERO_ID_INVALID = 0L;
	protected static final String SUPERHERO_NAMES_SUPERHERO_VALID = "john.smith@shc.com";
	protected static final String SUPERHERO_NAMES_SUPERHERO_INVALID = "nnn.nnn@shc.com";
	protected static final String SUPERHERO_NAMES_FIRST_VALID = "James";
	protected static final String SUPERHERO_NAMES_FIRST_INVALID = "AAAAAAAAA";
	protected static final String SUPERHERO_NAMES_LAST_VALID = "Dupuis";
	protected static final String SUPERHERO_NAMES_LAST_INVALID = "AAAAAAAAA";
	
	/** superheros/{id} - Make sure against the database the id is an appropriate one */
	protected static final String SUPERHERO_BY_ID = SUPERHEROS + "/%s";
	
	/** superheros/names/superhero/{name} */
	protected static final String SUPERHERO_BY_SUPERHERONAME = SUPERHEROS + "/names/superhero/%s";
		
	/** superheros/names/first/{firstName} */
	protected static final String SUPERHEROS_BY_FIRSTNAME = SUPERHEROS + "/names/first/%s";
	
	/** superheros/names/last/{lasttName} */
	protected static final String SUPERHEROS_BY_LASTNAME = SUPERHEROS + "/names/last/%s";

	/** missions/ */
	protected static final String MISSIONS = "missions";
	
	/** missions/{id} - Make sure against the database the id is an appropriate one */
	protected static final String MISSION_BY_ID = MISSIONS + "/%s";
	
	/** missions/name/{name} */
	protected static final String MISSION_BY_NAME = MISSIONS + "/name/%s";
	
	/** missions/completed/ */
	protected static final String MISSIONS_COMPLETED = MISSIONS + "/completed";

	/** missions/deleted/ */
	protected static final String MISSIONS_DELETED = MISSIONS + "/deleted";
	
	protected static final Long MISSION_ID_VALID = 90L;
	protected static final Long MISSION_ID_INVALID = 0L;
	protected static final String MISSION_NAME_VALID = "UNCOMPLETED_UNDELETED";
    protected static final String MISSION_NAME_INVALID = "QQQ";

	protected static final String DUMMY_TEST_JSON = "{ \"Test\": \"Test\" }";

	protected static final long ENDPOINT_RESPONSE_TIME = 1000L;//200L;
	protected static final long ENDPOINT_RESPONSE_SECURED = 600L;

	protected static final String JSON_SCHEMA_PATH_MISSION = "schemas/schema_mission.json";
	protected static final String JSON_SCHEMA_PATH_MISSIONS = "schemas/schema_missions.json";
	protected static final String JSON_SCHEMA_PATH_SUPERHERO = "schemas/schema_superhero.json";
	protected static final String JSON_SCHEMA_PATH_SUPERHEROS = "schemas/schema_superheros.json";
	
    @Value("${spring.security.username}")
    private String username;

    @Value("${spring.security.password}")
    private String password;

//    @Value("${spring.security.user.roles}")
//    private List<String> roles;
	
	protected ValidatableResponse prepareGet(String path) {
		return prepare().get(HOST_ROOT + path).then();
	}

	protected ValidatableResponse prepareGetSecured(String path) {
		return prepareSecured().get(HOST_ROOT + path).then();
	}

	protected ValidatableResponse prepareDelete(String path) {
		return prepare().delete(HOST_ROOT + path).then();
	}
	
	protected ValidatableResponse prepareDeleteSecured(String path) {
		return prepareSecured().delete(HOST_ROOT + path).then();
	}
	
	protected Response preparePut(String path, String body) {
		return prepare(body).put(HOST_ROOT + path);
	}

	protected Response preparePut(String path, Map<String, ?> params, String body) {
		return prepare(body).params(params).put(HOST_ROOT + path);
	}
	
	protected Response preparePost(String path, String body) {
		return prepare(body).post(HOST_ROOT + path);
	}
  
  private RequestSpecification prepare(String body) {
	  return prepare().contentType(String.valueOf(APPLICATION_JSON)).body(body).when();
  }
  
  private RequestSpecification prepare() {
      return given().port(port).when();
  }
  
  private RequestSpecification prepareSecured() {
      return prepare().auth().basic(username, password).when();
  }
  
  private RequestSpecification prepareSecured(String body) {
	  return prepare(body).auth().basic(username, password).when();
  }
}