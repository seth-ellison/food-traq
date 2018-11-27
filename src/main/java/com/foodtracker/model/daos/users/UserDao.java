package com.foodtracker.model.daos.users;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface UserDao extends PagingAndSortingRepository<User, String> {
	// Function-derived DB queries go here.
	public User findByUsername(String username);
	
	@SuppressWarnings("unchecked")
	@Override
    @RestResource(exported = false)
    public User save(User s);
	
	@Override
    @RestResource(exported = false)
    public void deleteById(String id);
	
	@Override
    @RestResource(exported = false)
    public void deleteAll();
	
	@Override
	@RestResource(exported = false)
	public void deleteAll(Iterable<? extends User> entities);
	
    @Override
    @RestResource(exported = false)
    public void delete(User t);
}
