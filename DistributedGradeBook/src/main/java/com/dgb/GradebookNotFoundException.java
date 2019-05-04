package com.dgb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Gradebook not found")
public class GradebookNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GradebookNotFoundException(String exception)
	{
		super(exception);
	}
}