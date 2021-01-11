package com.example.demo.model;

import io.swagger.annotations.ApiModelProperty;

public class UserRequest {
	
	@ApiModelProperty(example = "test01")
	String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
