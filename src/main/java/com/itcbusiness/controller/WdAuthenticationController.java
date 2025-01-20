package com.itcbusiness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.service.WdAuthenticationService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/wd-auth")
@Slf4j
public class WdAuthenticationController {

	private WdAuthenticationService wdAuthenticationService;

	public WdAuthenticationController(WdAuthenticationService wdAuthenticationService) {
		super();
		this.wdAuthenticationService = wdAuthenticationService;
	}

	@PostMapping("/add-wd-api-key")
	public ResponseEntity<Object> saveApiKeyOfWd(@RequestParam("wdCode") String wdCode,
			@RequestParam("apiKey") String apiKey) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Itc User of Auditor add successfully !", HttpStatus.CREATED,
					this.wdAuthenticationService.addApikeyOfWd(wdCode, apiKey));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-wd-api-key")
	public ResponseEntity<Object> getApiKey(@RequestParam("wdCode") String wdCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Itc User of Auditor add successfully !", HttpStatus.CREATED,
					this.wdAuthenticationService.getapiOfWd(wdCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-all-wd-api-key")
	public ResponseEntity<Object> getApiKey() {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Itc User of Auditor add successfully !", HttpStatus.CREATED,
					this.wdAuthenticationService.getAllapiKeyOfWd());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	// updateStatus

	@PutMapping("/disable-api-key")
	public ResponseEntity<Object> updateStatus(@RequestParam("wdCode") String wdCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Itc User of Auditor add successfully !", HttpStatus.CREATED,
					this.wdAuthenticationService.updateStatus(wdCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

}
