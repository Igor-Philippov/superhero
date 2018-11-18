package com.superhero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MissionUnprocessableException extends RuntimeException {
	public MissionUnprocessableException(String exception) {
		super(exception);
	}
}
