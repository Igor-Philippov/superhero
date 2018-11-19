package com.superhero.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.When;

public class MissionSteps extends SuperHeroFeatureTest {

    private static final Logger LOGGER = getLogger(MissionSteps.class);

    @When("^User gets one* arrival by id (\\d+)$")
    public void userGetsOneArrivalById(int id) throws Throwable {
        LOGGER.info("When - User gets one arrival by id [{}]", id);
    }
}