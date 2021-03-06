package com.superhero.test.integration.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.test.integration.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonSteps extends SuperHeroFeatureTest {

    private static final Logger logger = getLogger(CommonSteps.class);
    
    @Given("^(.+) rest endpoint is up$")
    public void restEndpointIsUp(String endpointType) throws Throwable {
    	logger.info("Given - [{}] rest endpoint is up", endpointType);
    }

    @Then("^Returned JSON object is not null$")
    public void returnedJSONObjectIsNotNull() {
        logger.info("Then - Returned JSON object is not null");
    }
    
    
    @Then("^The HTTP response status code is (\\d+)$")
    public void userGetsOneMissionById(int id) throws Throwable {
        logger.info("Then - The HTTP response status code is [{}]", id);
    }
}
