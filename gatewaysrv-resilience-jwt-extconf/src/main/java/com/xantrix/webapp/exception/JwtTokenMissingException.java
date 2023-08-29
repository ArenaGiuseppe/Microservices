package com.xantrix.webapp.exception;

import javax.naming.AuthenticationException;
import lombok.extern.java.Log;

@Log
public class JwtTokenMissingException extends AuthenticationException 
{

	private static final long serialVersionUID = 1L;

	public JwtTokenMissingException(String msg) 
	{
		super(msg);
		log.warning(msg);
	}

}
