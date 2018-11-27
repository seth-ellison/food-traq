package com.foodtracker.config.security.jwt;

import java.util.Date;
import java.util.UUID;

import com.foodtracker.model.daos.users.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenGenerator {
	
	private JwtTokenGenerator() {
	    throw new IllegalAccessError("This class cannot be instantiated.");
	}
	
    /**
     * Generates a JWT token containing username as subject, and authorities as additional claims. These properties are taken from the specified
     * User object.
     * 
     * Token lasts forever.
     *
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public static String generateToken(User u, String secret) {
        Claims claims = Jwts.claims().setSubject(u.getUsername());
        claims.put("userId", u.getUserId());
        claims.put("authorities", u.getAuthorities());
        claims.put("accountNonLocked", u.isAccountNonLocked());
        claims.put("accountNonExpired", u.isAccountNonExpired());
        claims.put("credentialsNonExpired", u.isCredentialsNonExpired());
        claims.put("enabled", u.isEnabled());
        
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}