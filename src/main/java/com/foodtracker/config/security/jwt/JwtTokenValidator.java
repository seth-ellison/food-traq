package com.foodtracker.config.security.jwt;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.foodtracker.model.daos.authorities.UserAuthority;
import com.foodtracker.model.daos.users.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
@PropertySource("classpath:/config/jwt/jwt.properties")
public class JwtTokenValidator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenValidator.class);
	
    @Value("${${spring.profiles.active}.jwt.secret}")
    private String secret;
    
    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public User parseToken(String token) {
    	User u = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            
            u = new User();
            u.setUsername(body.getSubject());
            
            
        	u.setUserId((String)body.get("userId"));
            u.setAccountNonExpired((boolean)body.get("accountNonExpired"));
            u.setAccountNonLocked((boolean)body.get("accountNonLocked"));
            u.setEnabled((boolean)body.get("enabled"));
            u.setCredentialsNonExpired((boolean)body.get("credentialsNonExpired"));
              
            Gson gson = new Gson();
            
            // I am using an anonymous class to get the type of TypeToken (as a Type), which is the type of list I actually want.
            Type listType = new TypeToken<List<UserAuthority>>() {}.getType();
            List<UserAuthority> auths = gson.fromJson(gson.toJson(body.get("authorities")), listType);
            
            Set<UserAuthority> authSet = new HashSet<>(auths);
            
            u.setAuthorities(authSet);
        } catch (JwtException e) {
        	LOGGER.error("JWT token parsing failed!", e);
        }
        return u;
    }
}
