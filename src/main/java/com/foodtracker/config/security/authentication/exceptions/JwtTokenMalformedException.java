package com.foodtracker.config.security.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * When a token cannot be parsed, we throw this exception.
 * @author seth.ellison
 *
 */
public class JwtTokenMalformedException extends AuthenticationException {

	private static final long serialVersionUID = -1127540181665906958L;

	public JwtTokenMalformedException(String msg) {
		super(msg);
	}
}
