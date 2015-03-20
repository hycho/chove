package com.cho.chove.core.security.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	public User() {
	}

	public User(String userId, String id, String password, String name) {
		this.userId = userId;
		this.id = id;
		this.password = password;
		this.name = name;
	}

	private String userId;

	private String id;

	private String password;

	private String name;

	private String description;

	private String email;

	private String useFlag;

	private Date createdDate;

	private Date updatedDate;
	
	private List<Authority> authorities;

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}
}