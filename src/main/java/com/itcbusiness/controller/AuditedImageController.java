package com.itcbusiness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.model.request.AuditedImageRequest;
import com.itcbusiness.service.AuditedImageService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auditedItemImage")
@Slf4j
public class AuditedImageController {

	private AuditedImageService auditedImageService;

	public AuditedImageController(AuditedImageService auditedImageService) {
		super();
		this.auditedImageService = auditedImageService;
	}

	@PostMapping(value = "/sumbition-audited-item-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sumbitionAuditedItem(@ModelAttribute AuditedImageRequest auditedImageRequest) {
		try {
			return GlobalResponse.responseData("Audited Images Submitted Successfully", HttpStatus.CREATED,
					this.auditedImageService.addImagesForAudited(auditedImageRequest));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd, e);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-audited-item-images")
	public ResponseEntity<Object> sumbitionAuditedItem(@RequestParam String itemDetailsCode) {
		try {
			return GlobalResponse.responseData("Audited Images Submitted Successfully", HttpStatus.CREATED,
					this.auditedImageService.getImagesForAudited(itemDetailsCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd, e);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

}
