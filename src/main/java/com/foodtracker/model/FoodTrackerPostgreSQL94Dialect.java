package com.foodtracker.model;

import java.sql.Types;

import org.hibernate.spatial.dialect.postgis.PostgisPG94Dialect;

/**
 * This custom dialect exists to allow Hibernate to recognize jsonb
 * columns. 
 *
 */
public class FoodTrackerPostgreSQL94Dialect extends PostgisPG94Dialect {
	private static final long serialVersionUID = 4865656366900056895L;

	public FoodTrackerPostgreSQL94Dialect() {
		this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}
}
