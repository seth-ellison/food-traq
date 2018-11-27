package com.foodtracker.entities;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodtracker.FoodTrackerApplication;
import com.foodtracker.constants.MediaTypes;
import com.foodtracker.model.daos.foods.FoodUnit;
import com.foodtracker.model.daos.foods.FoodUnitDao;

/**
 * Tests POST, PUT, GET, and DELETE endpoints for this entity.
 * 
 * @author seth.ellison
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties={"spring.profiles.active = tst"}, classes=FoodTrackerApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class FoodUnitTest extends AbstractDocumentationTest {
	
	private String uri = "/api/foodUnits";
	private String foodUnitId = "5e982fc6-49ac-47b9-b6b1-07f65d277ee8";
	private String foodUnitContent = "{\r\n" + 
			"	\"foodUnitId\": \"null\",\r\n" + 
			"	\"productType\": {\r\n" + 
			"		\"productId\": \"1119d1f-f29f-4ec5-b7e9-64567d8f3d7c\"\r\n" + 
			"	},\r\n" + 
			"	\"description\": \"Sketchy Coffee - Test\",\r\n" + 
			"	\"mass\": 2.0,\r\n" + 
			"	\"expiry\": \"2018-12-30\",\r\n" + 
			"	\"attributes\": [{\r\n" + 
			"		\"name\": \"cupping\",\r\n" + 
			"		\"value\": \"10\"\r\n" + 
			"	}]\r\n" + 
			"}";
	
	@Autowired
	private FoodUnitDao unitDao;
	
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	
	@Test
	@Override
	public void getAll() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.getJwt());
		
		this.getMvc().perform(get(this.uri).header("Authorization",  this.getJwt()).accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isOk())
		.andDo(document("{class-name}/{method-name}", preprocessRequest(removeHeaders("Authorization")), links(halLinks(),
				linkWithRel("self").description("Reference to self in repository."),
				linkWithRel("search").description("Search through this endpoint"),
				linkWithRel("profile").description("Overview of this endpoint"))));
	}
	
	@Test
	@Override
	public void getOne() throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.getJwt());
		
		this.getMvc().perform(get(this.uri + "/" + foodUnitId)
				.header("Authorization",  this.getJwt())
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isOk()) 
		.andDo(document("{class-name}/{method-name}", preprocessRequest(removeHeaders("Authorization")), links(halLinks(),
				linkWithRel("self").description("Reference to self in repository."),
				linkWithRel("user").description("The user who uploaded this food unit"),
				linkWithRel("productType").description("The product type this food unit falls under"),
				linkWithRel("attributes").description("Attributes for this food unit (custom data!)"),
				linkWithRel("foodUnit").description("Optional reference to self in repository"))));
	}
	
	@Test
	@Transactional // Keep session open for this function.
	@Override
	public void update() throws Exception {
		
		FoodUnit f = this.createFoodUnit(); // Create record.
		f.setDescription("Sketchy Coffee - Test");
		
		String data = this.jacksonObjectMapper.writeValueAsString(f);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.getJwt());
		headers.add("Content-Type", "application/json");
		
		this.getMvc().perform(put(this.uri + "/" + f.getFoodUnitId())
				.content(data)
				.headers(headers))
		.andExpect(status().isNoContent());
		
		unitDao.delete(f); // Cleanup leftover record.
	}
	
	@Test
	@Transactional // Keep session open for this function.
	@Override
	public void create() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.getJwt());
		headers.add("Content-Type", "application/json");		
		
		this.getMvc().perform(post(this.uri)
				.content(foodUnitContent)
				.headers(headers)
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isCreated())
		.andDo(document("{class-name}/{method-name}", preprocessRequest(removeHeaders("Authorization")), links(halLinks())));
		
		FoodUnit f = unitDao.findByDescription("Sketchy Coffee - Test");
		unitDao.delete(f); // Clean up test record
	}
	
	/**
	 * Helper function for creating a record outside of the API
	 * @return Persisted record
	 */
	public FoodUnit createFoodUnit() {
		
		FoodUnit f = new FoodUnit();
		Optional<FoodUnit> temp = unitDao.findById(foodUnitId);
		
		BeanUtils.copyProperties(temp.get(), f);
		
		f.setDescription("Sketchy Coffee - Test");
		
		return unitDao.save(f);
		
	}

	@Override
	protected void remove() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
