package com.itcbusiness.custome.status;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ComponentScan
public class GlobalResponse {

	public static ResponseEntity<Object> responseData(String message, HttpStatus status, Object object) {
		Map<String, Object> data = new HashMap<>();
		data.put("message", message);
		data.put("status", status.value());
		data.put("result", object);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "Application/json");
		return new ResponseEntity<>(data, headers, status.value());
	}

}
