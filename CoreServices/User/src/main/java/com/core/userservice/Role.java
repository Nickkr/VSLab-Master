package com.core.userservice;

public enum Role{
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    private Role(String role) {
        this.role = role;
    }
}