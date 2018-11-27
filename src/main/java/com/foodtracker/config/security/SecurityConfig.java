package com.foodtracker.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.foodtracker.config.security.authentication.JwtAuthenticationEntryPoint;
import com.foodtracker.config.security.authentication.JwtAuthenticationFailureHandler;
import com.foodtracker.config.security.authentication.JwtAuthenticationProvider;
import com.foodtracker.config.security.authentication.JwtAuthenticationSuccessHandler;
import com.foodtracker.config.security.authentication.FoodTrackerAuthenticationFailureHandler;
import com.foodtracker.config.security.authentication.FoodTrackerAuthenticationProvider;
import com.foodtracker.config.security.authentication.FoodTrackerAuthenticationSuccessHandler;
import com.foodtracker.config.security.filters.JwtAuthenticationFilter;
import com.foodtracker.config.security.services.FoodTrackerUserDetailsService;

/**
 * Java Configuration for our Spring Security package.
 * 
 * @author seth.ellison
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
	
    @Autowired
    private JwtAuthenticationProvider authenticationProvider;
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			//.and()
			.formLogin()
		        .loginPage("/login") // The specific location of the login page. This is what PrettyFaces rewrites /login to behind the scenes.
		        .usernameParameter("username")
		        .passwordParameter("password")
		        .successHandler(this.FoodTrackerAuthenticationSuccessHandler())
		        .failureHandler(this.FoodTrackerAuthenticationFailureHandler())
		        .permitAll() // We must grant all users (i.e. unauthenticated users) access to our log in page. The formLogin().permitAll() method allows granting access to all users for all URLs associated with form based log in.
	  
			.and()
			.logout()
	        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	           	.deleteCookies("JSESSIONID")
	        	.invalidateHttpSession(true)
			.and()
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				.antMatchers("/login").anonymous()
				.antMatchers("/javax.faces.resource/**").permitAll()
            	.antMatchers("/css/**").permitAll()
            	.antMatchers("/js/**").permitAll()
            	.antMatchers("/fonts/**").permitAll()
            	.antMatchers("/img/**").permitAll()
            	.antMatchers("/font-awesome/**").permitAll()
            	.antMatchers("/content/tmp/**").permitAll()
				.anyRequest().authenticated()
			//.and()
			//	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.headers()
				.cacheControl().disable();
			
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) 
      throws Exception {
        auth.authenticationProvider(FoodTrackerAuthenticationProvider());
   
    }
	
	@Bean
	public PasswordEncoder FoodTrackerPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	/**
     * Create the authentication success handler as a Spring Bean, this way autowiring will run.
     * @return An autowired-up spring bean
     */
    @Bean
    public AuthenticationSuccessHandler FoodTrackerAuthenticationSuccessHandler() {
    	return new FoodTrackerAuthenticationSuccessHandler();
    }
    
    /**
     * Create the authentication success handler as a Spring Bean, this way autowiring will run.
     * @return An autowired-up spring bean
     */
    @Bean
    public AuthenticationFailureHandler FoodTrackerAuthenticationFailureHandler() {
    	return new FoodTrackerAuthenticationFailureHandler();
    }
    
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
		filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
		return filter;
		
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(FoodTrackerAuthenticationProvider(), authenticationProvider));
    }
	
	// Handles authenticating users. Wires up our custom authentication provider to override the Spring Security default, and links up the user detail service.
    @Bean
    public AuthenticationProvider FoodTrackerAuthenticationProvider() {
    	FoodTrackerAuthenticationProvider provider = new FoodTrackerAuthenticationProvider();
    	
    	provider.setUserDetailsService(this.FoodTrackerUserDetailsService());
    	
    	return provider;
    }
	
	// Used ONLY for providing user data. This does NOT authenticate anything.
	@Bean
    public UserDetailsService FoodTrackerUserDetailsService() {
    	
    	UserDetailsService service = new FoodTrackerUserDetailsService();
    	
    	return service;
    }
}