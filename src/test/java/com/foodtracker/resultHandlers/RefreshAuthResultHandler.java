package com.foodtracker.resultHandlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * After every successful request, we re-authenticate as our test user.
 * This is because MockMvc.perform() purges the security context.
 * 
 * @author seth.ellison
 *
 */
public class RefreshAuthResultHandler implements ResultHandler {
	
	private Authentication auth;
	
	public RefreshAuthResultHandler(Authentication auth) {
		this.auth = auth;
	}
	
	@Override
	public void handle(MvcResult result) throws Exception {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
