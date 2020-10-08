package com.qbi.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUserDetails implements UserDetails {
	private static final long serialVersionUID = -4333020103397153136L;
	private String username;
	private String password;
	private boolean active;
	private List<GrantedAuthority> authorities;
	
	public AuthUserDetails() {
	}
	
	public AuthUserDetails(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.active = user.getActive();
		this.authorities = Arrays.stream(user.getRole().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this.authorities;
	}
	
	@Override
	public String getPassword() {
		return this.password	;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return this.active;
	}
}
