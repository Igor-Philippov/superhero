package com.superhero.exception;

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
		ErrorInfo errorInfo = new ErrorInfo(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler( {SuperHeroNotFoundException.class, 
		                MissionNotFoundException.class,
		                MissionUnprocessableException.class} )
	public final ResponseEntity<ErrorInfo> handleUserNotFoundException(Exception ex, WebRequest request) {
		HttpStatus httpStatus = ex.getClass() == MissionUnprocessableException.class ? HttpStatus.UNPROCESSABLE_ENTITY : HttpStatus.NOT_FOUND;
		ErrorInfo errorInfo = new ErrorInfo(new Date(), httpStatus.toString(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorInfo, httpStatus);
	}
}
