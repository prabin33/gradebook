package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Student already exists")
public class StudentExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public StudentExistsException(String exception)
	{
		super(exception);
	}
}
