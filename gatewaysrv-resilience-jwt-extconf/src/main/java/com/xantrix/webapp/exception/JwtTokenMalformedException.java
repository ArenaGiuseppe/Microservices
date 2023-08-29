package com.xantrix.webapp.exception;

import javax.naming.AuthenticationException;

import lombok.extern.java.Log;

@Log
public class JwtTokenMalformedException extends AuthenticationException 
{

	private static final long serialVersionUID = 1L;

	public JwtTokenMalformedException(String msg) 
	{
		super(msg);
		log.warning(msg);
	}

}
