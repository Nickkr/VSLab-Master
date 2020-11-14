package com.core.userservice;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    public User(){

    }
    
    public User(int id, String username, String firstname, String lastname, String password, Role role) {
		this.id = id;
        this.username= username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.role = role;
	}

	public int getId() {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
    
}