package com.core.userservice;

public enum Role{
    USER, ADMIN
}

public class User {

	private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private Role role;


    public User(long id, String username, String firstname, String lastname, String password, Role role) {
		this.id = id;
        this.username= username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.role = role;
	}

	public long getId() {
		return id;
    }
    
    public void setId(int id) {
		this.id = id;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassowrd(String password){
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
    
}