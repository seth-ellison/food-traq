package com.foodtracker.config.security.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class FoodTrackerAuthenticationFailureHandler implements AuthenticationFailureHandler {
		
	// Used to send a redirect to the client.
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        handle(request, response, exception); // Handle the authentication failure        
    }
 
	/**
	 * Retrieves the URL needed for the redirect, determines if a redirect is a valid action, and sends the redirect.
	 * 
	 * @param request The current request linked to submission of the login form.
	 * @param response The response which will be redirected.
	 * @param authentication Represents the authenticated user.
	 * @throws IOException
	 */
    protected void handle(HttpServletRequest request,HttpServletResponse response, AuthenticationException ex) throws IOException {    	
    	//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    	
    	// Redirect the user if they failed to login, right on to the login page.
    	redirectStrategy.sendRedirect(request, response, "/login");
    }
}
