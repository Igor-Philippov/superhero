package com.superhero.exception;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorInfo {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd hh:mm:ss")
	private Date timestamp;
	private String httpStatus;
	private String message;
	private String details;

	public ErrorInfo(Date timestamp, String httpStatus, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.httpStatus = httpStatus;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
