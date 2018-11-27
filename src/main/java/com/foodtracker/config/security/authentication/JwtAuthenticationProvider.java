package com.foodtracker.config.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.foodtracker.config.security.authentication.exceptions.JwtTokenMalformedException;
import com.foodtracker.config.security.jwt.JwtAuthenticationToken;
import com.foodtracker.config.security.jwt.JwtTokenValidator;
import com.foodtracker.model.daos.users.User;

/**
 * Used for checking the token from the request and supply the UserDetails if the token is valid
 */
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
    @Autowired
    private JwtTokenValidator jwtTokenValidator;
    
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
    	// Extra checks can go here.
    }
    
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();
        User parsedUser = jwtTokenValidator.parseToken(token);
        
        if (parsedUser == null) {
            throw new JwtTokenMalformedException("JWT token is not valid. Ensure the token passed to retrieveUser represents a signed User object..");
        }
        
        return parsedUser;
    }
}
