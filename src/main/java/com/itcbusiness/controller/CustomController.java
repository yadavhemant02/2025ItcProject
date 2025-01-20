package com.itcbusiness.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.service.CustomService;

@RestController
@RequestMapping("/api")
public class CustomController {

	private final CustomService customService;

	@Autowired
	public CustomController(CustomService customService) {
		this.customService = customService;
	}

	@DeleteMapping("/delete-all-data")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteAllData() {
		customService.deleteAllData();
		return ResponseEntity.ok("All data deleted successfully!");
	}

	@DeleteMapping("/delete-all-data-with-user")
	// @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteAllDataWithOutUser() {
		customService.deleteAllDataWithOutUser();
		return ResponseEntity.ok("All data deleted successfully!");
	}
}
