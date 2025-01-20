package com.itcbusiness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.model.InvoiceDataModel;
import com.itcbusiness.service.InvoiceDataService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/invoiceData")
@Slf4j
public class InvoiceDataController {

	private InvoiceDataService invoiceDataService;

	public InvoiceDataController(InvoiceDataService invoiceDataService) {
		super();
		this.invoiceDataService = invoiceDataService;
	}

	@PostMapping(value = "/update-invoice-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sumbitionAuditedItem(@ModelAttribute InvoiceDataModel invoiceData) {
		try {
			return GlobalResponse.responseData("invoice Update Successfully", HttpStatus.CREATED,
					this.invoiceDataService.updateInvoiceData(invoiceData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd, e);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

}
