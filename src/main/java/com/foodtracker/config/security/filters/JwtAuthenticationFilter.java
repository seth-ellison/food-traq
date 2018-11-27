package com.foodtracker.config.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.foodtracker.config.security.authentication.exceptions.JwtTokenMissingException;
import com.foodtracker.config.security.jwt.JwtAuthenticationToken;

@PropertySource("classpath:/config/jwt/jwt.properties")
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	public JwtAuthenticationFilter() {
		super("/api/**");
	}

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    	
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }

        String authToken = header.substring(7);

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);

        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    	
        super.successfulAuthentication(request, response, chain, authResult);

        // Simply move on with request processing, as though nothing fancy happened at all.
        chain.doFilter(request, response);
    }
	
}
