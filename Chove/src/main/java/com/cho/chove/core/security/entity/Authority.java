package com.cho.chove.core.security.entity;

import lombok.Data;

import org.springframework.security.core.GrantedAuthority;

@Data
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	public Authority() {
	}

	public Authority(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	private String authorityCode;

	private String name;

	@Override
	public String getAuthority() {
		return authorityCode;
	}
}