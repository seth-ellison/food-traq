package com.foodtracker.config.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * The only field we care about is the token itself. We have a contract to satisfy, though.
 * Thus, the extras. (AbstractUserDetailsAuthenticationProvider)
 * 
 * @author seth.ellison
 *
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -6886841844116103947L;
	
	private String token;
	
    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
    
    @Override
    public Object getCredentials() {
        return null;
    }
    
    @Override
    public Object getPrincipal() {
        return null;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JwtAuthenticationToken other = (JwtAuthenticationToken) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
    
    
    
}
