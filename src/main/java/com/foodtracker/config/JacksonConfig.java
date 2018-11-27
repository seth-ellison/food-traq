package com.foodtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 
 * Configuration for the main serializer/deserializer used for our APIs.
 * 
 * @author seth.ellison
 *
 */
@Configuration
public class JacksonConfig {
	
	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
		b.indentOutput(true)
		.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		return b;
	}
	
	/**
	 * Needed to allow Jackson to serialize/deserialize java 8 date/time objects.
	 * @return
	 */
	@Bean
	public Module java8DateTime() {
		return new JavaTimeModule();
	}
	
	@Bean
	public JtsModule jtsModule()
	{
	    return new JtsModule();
	}
}