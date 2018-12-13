package com.superhero.test.unit;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.superhero.test.category.TestCategoryUnit;
import com.superhero.test.unit.repository.MissionRepositoryUnitTest;
import com.superhero.test.unit.repository.SuperHeroRepositoryUnitTest;
import com.superhero.test.unit.rest.MissionEndpointUnitTest;
import com.superhero.test.unit.rest.SuperHeroEndpointUnitTest;
import com.superhero.test.unit.service.SuperHeroServiceUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MissionRepositoryUnitTest.class, 
	SuperHeroRepositoryUnitTest.class,
	SuperHeroServiceUnitTest.class,
	MissionEndpointUnitTest.class,
	SuperHeroEndpointUnitTest.class,
})
@Category(TestCategoryUnit.class)
public class ApplicationUnitTestSuite {

}
