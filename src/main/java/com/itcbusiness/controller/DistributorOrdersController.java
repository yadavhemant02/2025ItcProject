package com.itcbusiness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.model.DistributorOrderModel;
import com.itcbusiness.service.DistributorOrdersService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/order")
@Slf4j
public class DistributorOrdersController {

	private DistributorOrdersService distributorOrdersService;

	public DistributorOrdersController(DistributorOrdersService distributorOrdersService) {
		super();
		this.distributorOrdersService = distributorOrdersService;
	}

	@PostMapping("/add/order-of-distributors")
	public ResponseEntity<Object> addDistributors(@RequestBody DistributorOrderModel distributorsData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Distributor add successfully !", HttpStatus.CREATED,
					this.distributorOrdersService.addOrderOdDistributors(distributorsData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

}
