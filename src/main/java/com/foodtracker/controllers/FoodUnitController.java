package com.foodtracker.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.foodtracker.model.daos.foods.FoodUnit;
import com.foodtracker.model.daos.foods.FoodUnitAttribute;
import com.foodtracker.model.daos.foods.FoodUnitAttributeDao;
import com.foodtracker.model.daos.foods.FoodUnitDao;
import com.foodtracker.model.daos.products.ProductType;
import com.foodtracker.model.daos.products.ProductTypeDao;
import com.foodtracker.model.daos.products.ProductTypeRequiredAttribute;
import com.foodtracker.model.daos.products.ProductTypeRequiredAttributeDao;
import com.foodtracker.model.daos.users.User;

@RepositoryRestController
public class FoodUnitController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    FoodUnitDao foodDao;
    
    @Autowired
    ProductTypeDao productTypeDao;
    
    @Autowired
    ProductTypeRequiredAttributeDao productTypeRequiredAttributeDao;
    
    @Autowired
    FoodUnitAttributeDao foodUnitAttributeDao;
    
    /**
     * Defines custom behavior for handling POSTs to the /foodUnits endpoint.
     * The default Spring Data REST behaviors work for many things, but this endpoint goes
     * above and beyond the basic inclusions you get for free.
     * 
     * It validates input, returns descriptive error messages, and ensures that attributes are
     * uploaded as a bundle with the food units they describe.
     * 
     * @param unit The food unit to upload.
     * @return A persisted food unit, or an error entity
     */
    @RequestMapping(value = "/foodUnits", method = RequestMethod.POST)
    public ResponseEntity<?> insertFoodUnit(@RequestBody FoodUnit unit) {
    	logger.info("Processing POST to insertFoodUnit: " + unit.getDescription());
    	
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	unit.setUser(user);
    	
    	// Extract Product Code
    	String productTypeId = unit.getProductType().getProductId();
    	Optional<ProductType> type = productTypeDao.findById(productTypeId);
    	
    	if(!type.isPresent()) {
    		return new ResponseEntity<String>("Invalid Product Type ID: " + productTypeId, HttpStatus.BAD_REQUEST);
    	}
    	
    	unit.setProductType(type.get());
    	
    	// Get Required Attribs
    	Set<ProductTypeRequiredAttribute> required = productTypeRequiredAttributeDao.findAllByProductTypeProductId(productTypeId);
    	Set<FoodUnitAttribute> foodAttrs = unit.getAttributes();
    	
    	for(ProductTypeRequiredAttribute requiredAttr : required) {
    		boolean matched = false;
    		
    		// Verify a def'n for each attribute exists.
    		for(FoodUnitAttribute foodAttr : foodAttrs) {
    			if(requiredAttr.getName().equals(foodAttr.getName())) {
    				matched = true;
    			}
    		}
    		
    		// Match for required -> foodUnit found! 
    		if(matched) {
    			matched = false;
    		} else {
    			// No match found, break out with error response!
    			return new ResponseEntity<String>("Required food unit attribute not found: " + requiredAttr.getName(), HttpStatus.BAD_REQUEST);
    		}
    	}
    	
    	// Persist
    	unit.setCreated(Timestamp.valueOf(LocalDateTime.now()));
    	unit.setAttributes(null);
    	unit = foodDao.save(unit);
    	
    	// Update all attributes to point back to their newly persisted parent.
    	for(FoodUnitAttribute foodAttr : foodAttrs) {
    		foodAttr.setFoodUnit(unit);
    	}
    	
    	// Persist FoodUnitAttributes
    	foodUnitAttributeDao.saveAll(foodAttrs);
    	
    	
    	return new ResponseEntity<FoodUnit>(unit, HttpStatus.CREATED);
    }
}
