package com.qbi.model;

public enum RoleEnum {

	ADMIN("admin", 0),
	SALES("sales", 1),
	USER("user", 2);
	
	private final String roleName;
	private final int roleNumber;
	
	private RoleEnum(String name, int number) {
		this.roleName = name;
		this.roleNumber = number;
	}

	public String getRoleName() {
		return roleName;
	}

	public int getRoleNumber() {
		return roleNumber;
	}
	
}
