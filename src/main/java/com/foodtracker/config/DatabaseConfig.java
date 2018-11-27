package com.foodtracker.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * Handles creation of a new database connection pool.
 * We use C3P0 as our custom datasource.
 */
@Configuration
public class DatabaseConfig { 
	
	/*
	 * Return a configured C3P0 Data Source.
	 */
	@Bean
	public DataSource dataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		try {
			// General DB settings
			dataSource.setDriverClass("org.postgresql.Driver");
			dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/foodtracker");
			dataSource.setUser("foodtracker");
			dataSource.setPassword("f00dtrack");
			
			// C3P0 Settings
			dataSource.setAcquireIncrement(5);
			dataSource.setInitialPoolSize(5);
			dataSource.setMinPoolSize(5);
			dataSource.setMaxPoolSize(20);
			dataSource.setMaxIdleTime(300); // Five Minutes
			
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
		
		return dataSource;
	}
	
}