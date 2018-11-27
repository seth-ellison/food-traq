package com.foodtracker.model.daos.products;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductTypeRequiredAttributeDao extends PagingAndSortingRepository<ProductTypeRequiredAttribute, String>{
	public Set<ProductTypeRequiredAttribute> findAllByProductTypeProductId(String productId);
}
