package com.authorization.AuthServer;

public enum Role {
	USER(0),
	ADMIN(1);

	@SuppressWarnings("unused")
	private final int role;

	private Role(int role) {
		this.role = role;
	}
}
