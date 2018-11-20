package com.superhero.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CommonSteps extends SuperHeroFeatureTest {

    private static final Logger logger = getLogger(CommonSteps.class);

    @Then("^Returned JSON object is not null$")
    public void returnedJSONObjectIsNotNull() {
        logger.info("Then - Returned JSON object is not null");
    }
}
