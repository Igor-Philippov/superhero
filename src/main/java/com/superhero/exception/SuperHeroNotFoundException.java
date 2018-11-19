package com.superhero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SuperHeroNotFoundException extends RuntimeException {
	public SuperHeroNotFoundException(String exception) {
		super(exception);
	}
}
