package com.server.service.Gateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User  {
	
    @Id
	private String userId;
	private String firstName;
    private String surname;
    private String userName;
    private String email;
    private String password;
	private String role;

	public User(String firstName, String surname, String userName,
  	String email, String password, String role)
	{
		this.firstName = firstName;
		this.surname = surname;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}
    
	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getSurname() {
		return this.surname;
	}
    
	public void setSurname(String name) {
		this.surname = name;
	}

    public String getUserName() {
		return this.userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

    public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}