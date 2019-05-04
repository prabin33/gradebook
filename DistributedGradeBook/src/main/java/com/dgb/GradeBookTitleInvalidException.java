package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Gradebook title is invalid")
public class GradeBookTitleInvalidException extends Exception {

	private static final long serialVersionUID = 1L;

	public GradeBookTitleInvalidException(String exception)
	{
		super(exception);
	}
}
