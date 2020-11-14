package com.core.userservice;

public enum Role{
    USER(0),
    ADMIN(1);

    private final int role;

    private Role(int role) {
        this.role = role;
    }
}