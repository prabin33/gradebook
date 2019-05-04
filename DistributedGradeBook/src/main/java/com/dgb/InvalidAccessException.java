package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Cannot create on secondary server")
public class InvalidAccessException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidAccessException(String exception)
	{
		super(exception);
	}
}
