package com.superhero.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.When;

public class SuperHeroSteps extends SuperHeroFeatureTest {
    private static final Logger logger = getLogger(SuperHeroSteps.class);

    @When("^User gets one super hero by id (\\d+)$")
    public void userGetsOneSuperHeroById(int id) throws Throwable {
    	logger.info("When - User gets one super hero by id [{}]", id);
    }
}
