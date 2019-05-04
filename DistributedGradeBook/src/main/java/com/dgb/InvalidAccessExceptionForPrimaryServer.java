package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Cannot create on primary server")
public class InvalidAccessExceptionForPrimaryServer extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidAccessExceptionForPrimaryServer(String exception)
	{
		super(exception);
	}
}
