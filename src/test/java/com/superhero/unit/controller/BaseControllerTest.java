package com.superhero.unit.controller;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
public abstract class BaseControllerTest {

	public static String toJsonString(final Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
