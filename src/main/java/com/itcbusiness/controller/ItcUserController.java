package com.itcbusiness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.entity.ItcUser;
import com.itcbusiness.helper.JwtHelper;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.request.LoginRequestModel;
import com.itcbusiness.model.request.SignUpRequestModel;
import com.itcbusiness.service.ItcUserService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@Slf4j
public class ItcUserController {

	private ItcUserService itcUserService;
	private AuthenticationManager authenticationManager;
	private JwtHelper jwtHelper;
	private MapSturtMapper mapper;

	public ItcUserController(ItcUserService itcUserService, AuthenticationManager authenticationManager,
			JwtHelper jwtHelper, MapSturtMapper mapper) {
		super();
		this.itcUserService = itcUserService;
		this.authenticationManager = authenticationManager;
		this.jwtHelper = jwtHelper;
		this.mapper = mapper;
	}

	@PostMapping("/save/itc-admin-user")
	public ResponseEntity<Object> saveUserData(@RequestBody SignUpRequestModel signUpData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Itc User add successfully !", HttpStatus.CREATED,
					this.itcUserService.saveItcUser(signUpData, signUpData.getRole()));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@PostMapping("/login/itc-user")
	public ResponseEntity<Object> loginUserApi(@RequestBody LoginRequestModel loginData) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));

			if (authentication.isAuthenticated()) {

				UserDetails userDetailsData = this.itcUserService.loadUserByUsername(loginData.getEmail());
				ItcUser itcUserData = this.itcUserService.getSingleUserByEmail(loginData.getEmail());
				String token = this.jwtHelper.generateToken(userDetailsData);

				return GlobalResponse.responseData("Itc User login successfully !", HttpStatus.CREATED,
						this.mapper.itcUserToLoginResponseMode(itcUserData, token));
			}
			return GlobalResponse.responseData("Itc User login successfully !", HttpStatus.UNAUTHORIZED, null);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/list-of-Auditor")
	public ResponseEntity<Object> saveListOfAuditor() {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Itc User of Auditor add successfully !", HttpStatus.CREATED,
					this.itcUserService.getAllAuditorUser());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

}
