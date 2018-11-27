package com.foodtracker.config.security.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * If no token is in the request header, we throw this exception.
 * @author seth.ellison
 *
 */
public class JwtTokenMissingException extends AuthenticationException {

	private static final long serialVersionUID = 6491152141094070084L;

	public JwtTokenMissingException(String msg) {
		super(msg);
	}
}
