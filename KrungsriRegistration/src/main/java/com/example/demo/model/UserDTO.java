package com.example.demo.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class UserDTO {

	@ApiModelProperty(example = "kittikun")
	private String username;

	@ApiModelProperty(example = "password")
	private String password;

	@ApiModelProperty(example = "Kittikun")
	private String firstname;

	@ApiModelProperty(example = "Boonpho")
	private String lastname;

	@ApiModelProperty(example = "0981058805")
	private String phone;

	@ApiModelProperty(example = "40 ladphrao")
	private String address;

	@ApiModelProperty(example = "15000")
	private BigDecimal salary;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

}
