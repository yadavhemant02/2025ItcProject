package com.itcbusiness.controller;

import java.util.Base64;

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
import com.itcbusiness.entity.WdInvoiceImage;
import com.itcbusiness.model.request.WdInvoiceRequest;
import com.itcbusiness.service.WdInvoiceService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/wdInvoice")
@Slf4j
public class WdInvoiceController {

	private WdInvoiceService wdInvoiceService;

	public WdInvoiceController(WdInvoiceService wdInvoiceService) {
		super();
		this.wdInvoiceService = wdInvoiceService;
	}

	// @PostMapping("/add-wd-invoice/data")
	@PostMapping(value = "/add-wd-invoice/data", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, "image/jpeg",
			"image/png", "application/pdf" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getBranchLiabilityInController(@ModelAttribute WdInvoiceRequest wdInvoiceImage) {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.wdInvoiceService.addwdInvoice(wdInvoiceImage));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping(value = "/get-all-wd-invoice")
	public ResponseEntity<Object> getAllWdInvoiceDaat() {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.wdInvoiceService.getAllInvoice());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping(value = "/get-one-wd-invoice/")
	public ResponseEntity<Object> getAllWdInvoiceData(@RequestParam("wdCode") String wdCode) {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.wdInvoiceService.getAllOneWdInvoice(wdCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping(value = "/get-invoice-image/base-64")
	public ResponseEntity<Object> getInvoiceImage(@RequestParam("invoiceId") String invoiceId) {
		try {
			WdInvoiceImage imageBytes = wdInvoiceService.getInvoiceImage(invoiceId);
			if (imageBytes == null || imageBytes.getInvoiceFile().length == 0) {
				return GlobalResponse.responseData("Invoice image not found", HttpStatus.NOT_FOUND, null);
			}
			String base64Image = Base64.getEncoder().encodeToString(imageBytes.getInvoiceFile());
			imageBytes.setBaseInvoiceFile(base64Image);
			imageBytes.setInvoiceFile(null);
			return GlobalResponse.responseData("Invoice image retrieved successfully!", HttpStatus.OK, imageBytes);
		} catch (Exception e) {
			log.error("Error retrieving invoice image: {}", e.getMessage(), e);
			return GlobalResponse.responseData("Failed to retrieve invoice image: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
