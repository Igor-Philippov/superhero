package com.superhero.test.integration.bdd;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.superhero.SuperHeroApi;

@ContextConfiguration
@SpringBootTest(
        classes = SuperHeroApi.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class SuperHeroFeatureTest {
}