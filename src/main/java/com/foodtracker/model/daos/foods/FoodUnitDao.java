package com.foodtracker.model.daos.foods;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodUnitDao extends PagingAndSortingRepository<FoodUnit, String>{
	public FoodUnit findByDescription(String description);
}
