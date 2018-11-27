package com.foodtracker.config.security.authentication;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.foodtracker.config.security.services.FoodTrackerUserDetailsService;
import com.foodtracker.model.daos.authorities.UserAuthority;
import com.foodtracker.model.daos.users.User;

/**
 * T his custom authentication provider exists to override the Spring Security authenticate()
 * <br/>
 * functionality, rebuilding it to query our unique structure instead of using the default assumptions for authentication.
 * <p>
 * This class also handles assigning authorities (roles) to a user if their username + pass combo is valid.
 *   
 * @author script.wizard
 */
public class FoodTrackerAuthenticationProvider extends DaoAuthenticationProvider {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// When the login form is submitted, Spring Security provides an Authentication object with the username and credentials.
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		logger.info("Authing " + name);
		//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		
		FoodTrackerUserDetailsService FoodTrackerUserDetailsService = (FoodTrackerUserDetailsService) this.getUserDetailsService();
		User user = (User) FoodTrackerUserDetailsService.loadUserByUsername(name);
		
		Set<UserAuthority> auths = user.getAuthorities();
		UserAuthority authenticated = new UserAuthority();
		
		if(auths == null) {
			auths = new HashSet<UserAuthority>();
			authenticated.setAuthority("ROLE_USER");
			auths.add(authenticated);
		}
		
		authenticated = new UserAuthority();
		authenticated.setAuthority("ROLE_AUTHENTICATED");
		auths.add(authenticated);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user, password, auths);
		return auth;
	}
	
	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
