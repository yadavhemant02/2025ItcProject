package com.itcbusiness.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itcbusiness.repository.CustomRepository;

@Service
public class CustomService {

	private final CustomRepository customRepository;

	@Autowired
	public CustomService(CustomRepository customRepository) {
		this.customRepository = customRepository;
	}

	public void deleteAllData() {
		customRepository.deleteAllData();
	}

	public void deleteAllDataWithOutUser() {
		customRepository.deleteAllDataWithOutUser();

	}
}
