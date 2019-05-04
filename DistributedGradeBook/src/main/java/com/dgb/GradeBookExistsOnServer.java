package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "GradeBook excists already on secondary server")
public class GradeBookExistsOnServer extends Exception{

	private static final long serialVersionUID = 1L;

	public GradeBookExistsOnServer(String exception)
	{
		super(exception);
	}
}
