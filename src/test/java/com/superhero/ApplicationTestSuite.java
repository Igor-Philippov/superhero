package com.superhero;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.superhero.test.integration.ApplicationIntegrationTestSuite;
import com.superhero.test.unit.ApplicationUnitTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ApplicationUnitTestSuite.class,
	ApplicationIntegrationTestSuite.class
})
public class ApplicationTestSuite {

}
