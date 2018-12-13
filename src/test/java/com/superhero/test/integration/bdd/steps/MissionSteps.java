package com.superhero.test.integration.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.test.integration.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.When;

public class MissionSteps extends SuperHeroFeatureTest {

    private static final Logger logger = getLogger(MissionSteps.class);

    @When("^User gets one* mission by id (\\d+)$")
    public void userGetsOneMissionById(int id) throws Throwable {
        logger.info("When - User gets one mission by id [{}]", id);
    }
}