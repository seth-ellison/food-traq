package com.foodtracker.entities;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.foodtracker.config.security.authentication.JwtAuthenticationProvider;
import com.foodtracker.config.security.jwt.JwtAuthenticationToken;
import com.foodtracker.model.daos.authorities.UserAuthority;
import com.foodtracker.resultHandlers.RefreshAuthResultHandler;

/**
 * This class provides a boilerplate setup for writing documentation-generating tests.
 * It also provides a series of must-override functions which enforce a naming standard for test
 * functions.
 * 
 * @author seth.ellison
 *
 */
public abstract class AbstractDocumentationTest {
	
	private String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZXRoIiwidXNlcklkIjoiOWYxMzlkMWYtZjI5Zi00ZWM1LWI3ZTktNjQ1NjdkOGYzZDdjIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eUlkIjpudWxsLCJhdXRob3JpdHkiOiJST0xFX0FVVEhFTlRJQ0FURUQifSx7ImF1dGhvcml0eUlkIjoiOWYxMzlkMWYtZjI5Zi00ZWM1LWI3ZTktNjQ1NjdkOGY0NDQ0IiwiYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiZW5hYmxlZCI6dHJ1ZSwianRpIjoiNjQ5MDU5MTgtOTAyMy00NjFkLTk0NjQtZjViOGU2NGRlYjk1IiwiaWF0IjoxNTQwMjM1NjE2fQ.DRE5NNUDPLxTZXl5omAcdwgRo_2f7mPKR5kNF9C0WDxew2h4OFLpfoTXybYnwEHeD9gP4XH18-227Za3Gmydsg";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private JwtAuthenticationProvider provider;
	
	@Rule
	public JUnitRestDocumentation restDocumentation =
			new JUnitRestDocumentation("target/generated-snippets");
	
	private MockMvc mvc;
	
	private RestDocumentationResultHandler documentationHandler;
	
	protected abstract void getOne() throws Exception;
	protected abstract void getAll() throws Exception;
	protected abstract void create() throws Exception;
	protected abstract void update() throws Exception;
	protected abstract void remove() throws Exception;
	
	/**
	 * This code runs before EACH test, setting up some basic rules & config.
	 */
	@Before
	public void setup() {
		
		// By default (can be overridden on function level) document all mvc service calls
		// and strip out authorization tokens.
		this.documentationHandler = document("{class-name}/{method-name}",
				preprocessRequest(removeHeaders("Authorization")));
		
		String authToken = jwt.substring(7);
		JwtAuthenticationToken token = new JwtAuthenticationToken(authToken);
		Authentication auth = provider.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth); // Set up test user credentials.
				
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
						.snippets().withTemplateFormat(TemplateFormats.markdown())) // Emit Markdown Docs
				.apply(springSecurity())
				.alwaysDo(this.documentationHandler)
				.alwaysDo(this.refreshAuthentication()) // Make sure credentials are reset after a request.
				.build();	
	}
	
	public ResultHandler refreshAuthentication() {
		
		String authToken = jwt.substring(7);
		JwtAuthenticationToken token = new JwtAuthenticationToken(authToken);
		Authentication auth = provider.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return new RefreshAuthResultHandler(auth);
	}
	
	/**
	 * Whenever an mvc.perform() function executes a request, the security context gets
	 * smashed. This function exists to ease the recreation of mocked user credentials
	 * for the thread-bound security context.
	 * 
	 * @param username The user's username
	 * @param password The password to put into the user record.
	 * @param roles A list of roles to grant the mocked user, like, "ROLE_ADMIN"
	 * @return A mocked-in authentication token.
	 */
	public Authentication createAuthToken(String username, String password, String... roles) {
		Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
	    UserAuthority auth = new UserAuthority();
	    auth.setAuthority("ROLE_ADMIN");
	    auths.add(auth);
	    
	    
		Authentication token = new UsernamePasswordAuthenticationToken(username, "Password", auths);
		SecurityContextHolder.getContext().setAuthentication(token);
		return token;
	}
	
	public String getJwt() {
		return this.jwt;
	}
	
	public MockMvc getMvc() {
		return this.mvc;
	}
}
