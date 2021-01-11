package com.example.demo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@Entity
@Table(name = "USER")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 267314111411752301L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "hibernate_sequence")
	@Column(name = "USERID", unique = true)
	private int userid;

	@Column(name = "USERNAME", unique = true)
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "FIRSTNAME")
	private String firstname;

	@Column(name = "LASTNAME")
	private String lastname;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "REFCODE")
	private String refCode;

	@Column(name = "MEMBERTYPE")
	private String memberType;

	@Column(name = "SALARY")
	private BigDecimal salary;

	@JsonFormat(shape = Shape.STRING, pattern = "YYYYMMDD")
	@Temporal(TemporalType.DATE)
	@Column(name = "REGISTER_DATE")
	private Date regisDate;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

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

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Date getRegisDate() {
		return regisDate;
	}

	public void setRegisDate(Date regisDate) {
		this.regisDate = regisDate;
	}

}
