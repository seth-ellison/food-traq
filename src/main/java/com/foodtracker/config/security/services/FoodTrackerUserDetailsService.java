package com.foodtracker.config.security.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.foodtracker.model.daos.users.User;
import com.foodtracker.model.daos.users.UserDao;

@Service
public class FoodTrackerUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserDao userDao;

	/**
	 * Gets a user's information from their username.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		
		if(user == null) {
			user = new User();
		}
		
		return user;
	}
	
	//  A collection of granted authorities for a given user.
	public Collection<? extends GrantedAuthority> getAuthorities(String username) {
		User user = userDao.findByUsername(username); 
		
		return user.getAuthorities();
	}
	
}