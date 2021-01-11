package com.example.demo.controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.model.ResponseObj;
import com.example.demo.model.SecureObj;
import com.example.demo.model.UserDTO;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.UserService;
import com.example.demo.util.EncryptorAesUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(value = "/rest", method = RequestMethod.POST)
@RestController
public class RegistrationController {

	private static final Logger log = LogManager.getLogger(RegistrationController.class);

	@Autowired
	private RegistrationService service;

	@Autowired
	private UserService userService;

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	ObjectMapper objectMapper;

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public Object registration(@RequestBody UserDTO userDto) {
		log.info("################### Enter RegistrationController{registration} ###################");
		Date now = new Date();
		SecureObj secureObj = new SecureObj();
		ResponseObj resObj = new ResponseObj();
		try {
			String phone = userDto.getPhone();
			String memberType = service.getMemberType(userDto.getSalary());

			if ("Reject".equalsIgnoreCase(memberType)) {
				resObj.setResponseStatus("ERROR");
				resObj.setResponseMessage("Salary must be greater than or equal to 15000.");
				return resObj;
			}

			service.validatePhoneNumber(phone, resObj);
			if ("VALID".equals(resObj.getResponseStatus())) {
				String refCode = service.generateReferenceCode(now, phone);
				log.info("generate reference code : " + refCode);

				log.info("memberType : " + memberType);

				User user = modelMapper.map(userDto, User.class);
				user.setRefCode(refCode);
				user.setRegisDate(now);
				user.setMemberType(memberType);

				log.info("Saving..." + refCode);
				boolean isSaved = userService.save(user);
				log.info("Saved SUCCESS : " + isSaved);

				resObj.setResponseStatus("SUCCESS");
				resObj.setResponseMessage("Registration success.");
				resObj.setData(user);
				String input = objectMapper.writeValueAsString(resObj);
				log.info("input -> " + input);
				secureObj.setSecureObj(EncryptorAesUtil.encrypt(input));
			} else {
				resObj.setResponseStatus("ERROR");
				resObj.setResponseMessage("Phone number invalid.");
				return resObj;
			}
		} catch (Exception e) {
			log.error(e);
			resObj.setResponseStatus("ERROR");
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");
			return resObj;
		}

		log.info("################### Exit RegistrationController{registration} ###################");
		return secureObj;
	}

	@RequestMapping(value = "/decrypt", method = RequestMethod.POST)
	public Object decrypt(@RequestBody SecureObj secureObj) {
		log.info("################### Enter RegistrationController{decrypt} ###################");
		ResponseObj resObj = new ResponseObj();
		try {
			String cipherText = secureObj.getSecureObj();
			String data = EncryptorAesUtil.decrypt(cipherText);
			JsonNode treeNode = objectMapper.valueToTree(data);
			resObj = objectMapper.readValue(treeNode.asText(), ResponseObj.class);
		} catch (Exception e) {
			log.error(e);
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");
			resObj.setResponseStatus("ERROR");
		}
		log.info("################### Enter RegistrationController{decrypt} ###################");
		return resObj;
	}
}
