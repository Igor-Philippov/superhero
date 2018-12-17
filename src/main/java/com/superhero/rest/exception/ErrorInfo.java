package com.superhero.rest.exception;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
//@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class ErrorInfo {
	// Response body = {
    //   "timestamp": "2018-Dec-14 09:23:27",
    //   "code": 404,
    //   "error": "Not Found",
    //   "exception": "com.superhero.rest.exception.MissionNotFoundException",
    //   "details": "uri=/api/1.0/missions/0",
    //   "messages": [
    //      "No registered Mission with ID = 0"
    //   ]
	// }
	// Response body = {
    //   "timestamp": "2018-Dec-14 09:45:56",
    //   "code": 500,
    //   "error": "Internal Server Error",
    //   "exception": "org.springframework.dao.DataIntegrityViolationException",
    //   "details": "uri=/api/1.0/missions",
    //   "messages": [
    //      "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"
    //   ]
	// }

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd hh:mm:ss")
	private Date timestamp;
	private int code;
	private String error;
	private String exception;
	private String details;
	private List<String> messages;
}
