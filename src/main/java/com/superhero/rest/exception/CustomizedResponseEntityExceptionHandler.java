package com.superhero.rest.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.Collections;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorInfo> handleAllExceptions(Exception ex, WebRequest request) {
		return getResponseEntity(ex, request, INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler( {SuperHeroNotFoundException.class, 
		                MissionNotFoundException.class,
		                MissionUnprocessableException.class} )
	public final ResponseEntity<ErrorInfo> handleUserNotFoundException(Exception ex, WebRequest request) {
		return getResponseEntity(ex, request, ex.getClass() == MissionUnprocessableException.class ? UNPROCESSABLE_ENTITY : NOT_FOUND);
	}
	
	private ResponseEntity<ErrorInfo> getResponseEntity(Exception ex, WebRequest request, HttpStatus httpStatus) {
		return new ResponseEntity<>(
				ErrorInfo.builder()
				.timestamp(new Date())
				.code(httpStatus.value())
				.error(httpStatus.getReasonPhrase())
				.exception(ex.getClass().getName())
				.details(request.getDescription(false))
				.messages(Collections.singletonList(ex.getMessage()))
				.build(), 
				httpStatus);
	}
}
