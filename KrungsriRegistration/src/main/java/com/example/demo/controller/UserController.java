package com.example.demo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.model.ResponseObj;
import com.example.demo.model.SecureObj;
import com.example.demo.model.UserRequest;
import com.example.demo.service.UserService;
import com.example.demo.util.EncryptorAesUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(value = "/rest/user", method = RequestMethod.POST)
@RestController
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	ObjectMapper objectMapper;

	@RequestMapping(value = "/getUserInfos", method = RequestMethod.POST)
	public Object getUserInfos() {
		log.info("################### Enter UserController{getUserInfos} ###################");
		SecureObj secureObj = new SecureObj();
		ResponseObj resObj = new ResponseObj();
		try {
			List<User> users = userService.getAllUserInfo();

			if (users == null || users.size() == 0) {
				resObj.setResponseStatus("NOT_FOUND");
				resObj.setResponseMessage("User not found.");
				return resObj;
			} else {
				resObj.setResponseStatus("SUCCESS");
				resObj.setResponseMessage("Success");
				resObj.setData(users);

				String input = objectMapper.writeValueAsString(resObj);
				log.info("input -> " + input);
				secureObj.setSecureObj(EncryptorAesUtil.encrypt(input));
			}

		} catch (Exception e) {
			log.error(e);
			resObj.setResponseStatus("ERROR");
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");

			return resObj;
		}
		log.info("################### Exit UserController{getUserInfos} ###################");
		return secureObj;
	}

	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public Object getUserInfo(@RequestBody UserRequest userReq) {
		log.info("################### Enter UserController{getUserInfo} ###################");
		SecureObj secureObj = new SecureObj();
		ResponseObj resObj = new ResponseObj();
		String username = userReq.getUsername();
		User user = userService.getUserInfo(username);
		try {
			if (user == null) {
				resObj.setResponseStatus("NOT_FOUND");
				resObj.setResponseMessage("User not found.");

				return resObj;
			} else {
				resObj.setResponseStatus("SUCCESS");
				resObj.setResponseMessage("Success");
				resObj.setData(user);

				String input = objectMapper.writeValueAsString(resObj);
				log.info("input -> " + input);
				secureObj.setSecureObj(EncryptorAesUtil.encrypt(input));
			}
		} catch (Exception e) {
			resObj.setResponseStatus("ERROR");
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");

			return resObj;
		}
		log.info("################### Exit UserController{getUserInfo} ###################");
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
