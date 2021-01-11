package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.model.JwtRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.ResponseObj;
import com.example.demo.model.UserDTO;
import com.example.demo.model.UserRequest;
import com.example.demo.service.JwtUserDetailsService;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(value = "/test", method = RequestMethod.POST)
@RestController
public class TestController {

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private RegistrationService service;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/getUserInfos", method = RequestMethod.POST)
	public ResponseObj getUserInfos() {
		log.info("################### Enter UserController{getUserInfos} ###################");
		ResponseObj resObj = new ResponseObj();
		try {
			List<User> users = userService.getAllUserInfo();

			if (users == null || users.size() == 0) {
				resObj.setResponseStatus("NOT_FOUND");
				resObj.setResponseMessage("User not found.");
			} else {
				resObj.setResponseStatus("SUCCESS");
				resObj.setResponseMessage("Success");
				resObj.setData(users);
			}

		} catch (Exception e) {
			log.error(e);
			resObj.setResponseStatus("ERROR");
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");
		}
		log.info("################### Exit UserController{getUserInfos} ###################");
		return resObj;
	}

	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public ResponseObj getUserInfo(@RequestBody UserRequest userReq) {
		log.info("################### Enter UserController{getUserInfo} ###################");
		ResponseObj resObj = new ResponseObj();
		String username = userReq.getUsername();
		User user = userService.getUserInfo(username);
		try {
			if (user == null) {
				resObj.setResponseStatus("NOT_FOUND");
				resObj.setResponseMessage("User not found.");
			} else {
				resObj.setResponseStatus("SUCCESS");
				resObj.setResponseMessage("Success");
				resObj.setData(user);
			}
		} catch (Exception e) {
			resObj.setResponseStatus("ERROR");
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");
		}
		log.info("################### Exit UserController{getUserInfo} ###################");
		return resObj;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public Object registration(@RequestBody UserDTO userDto) {
		log.info("################### Enter RegistrationController{registration} ###################");
		Date now = new Date();
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
			} else {
				resObj.setResponseStatus("ERROR");
				resObj.setResponseMessage("Phone number invalid.");
			}
		} catch (Exception e) {
			log.error(e);
			resObj.setResponseStatus("ERROR");
			resObj.setResponseMessage("INTERNAL_SERVER_ERROR");
		}

		log.info("################### Exit RegistrationController{registration} ###################");
		return resObj;
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse("Bearer " + token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
