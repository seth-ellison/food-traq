package com.foodtracker.model.daos.authorities;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface UserAuthorityDao extends PagingAndSortingRepository<UserAuthority, String> {
	// Function-derived DB queries go here.
	
	@SuppressWarnings("unchecked")
	@Override
    @RestResource(exported = false)
    public UserAuthority save(UserAuthority s);
	
	@Override
    @RestResource(exported = false)
    public void deleteById(String id);
	
	@Override
    @RestResource(exported = false)
    public void deleteAll();
	
	@Override
	@RestResource(exported = false)
	public void deleteAll(Iterable<? extends UserAuthority> entities);
	
    @Override
    @RestResource(exported = false)
    public void delete(UserAuthority t);
}
