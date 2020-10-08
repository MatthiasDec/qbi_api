package com.qbi.model;

public class User {
	
	private int id;
	private String username;
	private String password;
	private boolean active;
	private String role;
	
	
	
	public User(int id, String username, String password, boolean active, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.active = active;
		this.role = role;
	}

	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public boolean getActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
			return "User{"+
					"id=" + id +
					", username=" + this.username +
					", password=" + this.password +
					", active=" + this.active +
					", role=" + this.role +
					"}";
	}
}
