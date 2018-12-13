package com.superhero.test.integration;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.superhero.test.category.TestCategoryIntegration;
import com.superhero.test.integration.rest.MissionControllerIntegrationTest;
import com.superhero.test.integration.rest.SuperHeroControllerIntegrationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MissionControllerIntegrationTest.class,
	SuperHeroControllerIntegrationTest.class,
})
@Category(TestCategoryIntegration.class)
public class ApplicationIntegrationTestSuite {

}
