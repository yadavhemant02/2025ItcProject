package com.itcbusiness.util;

import org.springframework.stereotype.Component;

@Component
public class Constant {

	private static final String signupresponsestatus = "User Register successfully !";

	public static final String[] allowedUrl = { "/v3/api-docs/**", "/api-docs.yaml", "/swagger-ui/**",
			"/swagger-resources/**", "/webjars/**", "/create/**", "/details/**",
			"/inventory/get/data-of-inventory-product/**" };

}
