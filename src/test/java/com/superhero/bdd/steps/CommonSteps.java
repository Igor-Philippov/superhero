package com.superhero.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

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
}
