package com.foodtracker.model.daos.foods;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodUnitTrackDao extends PagingAndSortingRepository<FoodUnitTrack, String>{
	FoodUnitTrack findByFoodUnit(FoodUnit unit);
}
