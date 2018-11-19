package com.superhero.bdd.steps;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.superhero.bdd.SuperHeroFeatureTest;

import cucumber.api.java.en.When;

public class SuperHeroSteps extends SuperHeroFeatureTest {
    private static final Logger LOGGER = getLogger(SuperHeroSteps.class);

    @When("^User gets one user by id (\\d+)$")
    public void userGetsOneUserById(int id) throws Throwable {
        LOGGER.info("When - User gets one user by id [{}]", id);
    }
}
