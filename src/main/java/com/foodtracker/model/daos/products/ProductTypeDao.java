package com.foodtracker.model.daos.products;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductTypeDao extends PagingAndSortingRepository<ProductType, String>{
	public ProductType findByProductType(String productType);
}
