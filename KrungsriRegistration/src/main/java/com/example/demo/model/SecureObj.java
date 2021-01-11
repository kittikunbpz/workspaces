package com.example.demo.model;

import java.io.Serializable;

public class SecureObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2870235095822706229L;
	private String secureObj;

	public String getSecureObj() {
		return secureObj;
	}

	public void setSecureObj(String secureObj) {
		this.secureObj = secureObj;
	}

}
