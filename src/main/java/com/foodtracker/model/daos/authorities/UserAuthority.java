package com.foodtracker.model.daos.authorities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodtracker.model.daos.users.User;

@Entity
@Table(name="user_authorities")
@GenericGenerator(name = "uuid", strategy = "uuid2")
public class UserAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 6178536910712467607L;
	
	private String authorityId;
	
	@JsonIgnore // Serialization infinite loop.
	private User user;
	private String authority;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="authority_id", unique = true, nullable = false)
	public String getAuthorityId() {
		return authorityId;
	}
	
	public void setAuthorityId(String id) {
		authorityId = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER) // Eager fetching ensures user data is always present.
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore // Don't blow up on infinite loops.
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Name of the authority, i.e ROLE_ADMIN, ROLE_DEV, ROLE_SUPPORT etc...
	 */
	@Column(name="authority", length = 64, unique=true, nullable=false)
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
