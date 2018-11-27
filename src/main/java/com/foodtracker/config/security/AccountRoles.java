package com.foodtracker.config.security;

public enum AccountRoles {
	ADMIN("ROLE_ADMIN"),
	DEV("ROLE_DEV"),
	OWNER("ROLE_OWNER"),
	USER("ROLE_USER");
	
	private String role;
	
	private AccountRoles(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return this.role;
	}
}
