package com.foodtracker.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.foodtracker.config.security.jwt.JwtTokenGenerator;
import com.foodtracker.model.daos.authorities.UserAuthority;
import com.foodtracker.model.daos.authorities.UserAuthorityDao;
import com.foodtracker.model.daos.users.User;
import com.foodtracker.model.daos.users.UserDao;

/**
 * Basic web navigation to support a browser-friendly front-end for the API.
 * 
 * @author seth.ellison
 *
 */
@Controller
@PropertySource("classpath:/config/jwt/jwt.properties")
public class FoodTrackerController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserAuthorityDao authDao;
	
	@Value("${${spring.profiles.active}.jwt.secret}")
    private String secret;
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String dashboard(Model model) {
		model.addAttribute("newUser", new User());
		model.addAttribute("jwt", JwtTokenGenerator.generateToken((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(), secret));
		return "dashboard";
	}
	
	@RequestMapping(path = "/admin", method = RequestMethod.GET)
	public String adminDashboard(Model model) {
		model.addAttribute("admin", true);
		model.addAttribute("newUser", new User());
		model.addAttribute("jwt", JwtTokenGenerator.generateToken((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(), secret));
		return "dashboard";
	}
	
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("newUser", new User());
		return "login";
	}
	
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public String submitName(@ModelAttribute User user, Model model) {
		
		// Encrypt Password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		user.setPassword(encoder.encode(user.getPassword()));
		user.setEmail("Fake Email");
		user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
		
		user = userDao.save(user);
		
		UserAuthority auth = new UserAuthority();
		auth.setUser(user);
		auth.setAuthority("ROLE_ADMIN");
		authDao.save(auth);
		
		model.addAttribute("newUser", new User());
		model.addAttribute("jwt", JwtTokenGenerator.generateToken(user, secret));
		model.addAttribute("success", "Record for " + user.getUsername() + " successfully added.");
		return "dashboard";
	}
}
