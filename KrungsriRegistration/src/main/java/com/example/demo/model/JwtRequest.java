package com.example.demo.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	@ApiModelProperty(example = "krungsri", value = "krungsri", accessMode = AccessMode.READ_ONLY)
	private String username;
	
	@ApiModelProperty(example = "password" , value = "password", accessMode = AccessMode.READ_ONLY)
	private String password;

	// need default constructor for JSON Parsing
	public JwtRequest() {

	}

	public JwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
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

	public void setPassword(String password) {
		this.password = password;
	}
}
