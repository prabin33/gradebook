package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Grade is invalid")
public class GradeInvalidException extends Exception{

	private static final long serialVersionUID = 1L;

	public GradeInvalidException(String exception)
	{
		super(exception);
	}
}
