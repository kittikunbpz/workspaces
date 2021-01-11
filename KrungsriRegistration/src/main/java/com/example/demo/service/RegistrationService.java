package com.example.demo.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.demo.model.ResponseObj;

@Service
public class RegistrationService {

	public String generateReferenceCode(Date regisDate, String phone) {
		String dateFormat = new SimpleDateFormat("YYYYMMDD", Locale.ENGLISH).format(regisDate);
		String last4Digit = phone.substring(6);
		return dateFormat.concat(last4Digit);
	}

	public String getMemberType(BigDecimal salary) {
		double value = salary.doubleValue();
		String memberType;
		if (value > 50000) {
			memberType = "Platinum";
		} else if (value >= 30000 && value <= 50000) {
			memberType = "Gold";
		} else if (value >= 15000 && value < 30000) {
			memberType = "Silver";
		} else {
			memberType = "Reject";
		}
		return memberType;
	}

	public ResponseObj validatePhoneNumber(String phone, ResponseObj resObj) {
		try {
			if (phone == null || "".equalsIgnoreCase(phone)) {
				resObj.setResponseStatus("INVALID");
				resObj.setResponseMessage("phone not found.");
			} else if (phone.length() != 10) {
				resObj.setResponseStatus("INVALID");
				resObj.setResponseMessage("phone length not equal 10.");
			} else {
				Integer.parseInt(phone);
				resObj.setResponseStatus("VALID");
				resObj.setResponseMessage("phone is valid.");
			}
		} catch (Exception e) {
			resObj.setResponseStatus("INVALID");
			resObj.setResponseMessage("phone must be numbers only.");
		}
		return resObj;
	}
}
