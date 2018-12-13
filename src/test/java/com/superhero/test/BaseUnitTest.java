package com.superhero.test;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.superhero.test.category.TestCategoryUnit;

@Category(TestCategoryUnit.class)
public abstract class BaseUnitTest extends BaseTest{
}
