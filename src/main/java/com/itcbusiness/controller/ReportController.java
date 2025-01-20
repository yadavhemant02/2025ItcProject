package com.itcbusiness.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.service.ReportService;
import com.itcbusiness.service.ReportService2;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/report")
@Slf4j
public class ReportController {

	private ReportService reportService;
	private ReportService2 reportService2;

	public ReportController(ReportService reportService, ReportService2 reportService2) {
		super();
		this.reportService = reportService;
		this.reportService2 = reportService2;
	}

	@GetMapping("/get-liability-data/of-branch")
	public ResponseEntity<Object> getBranchLiabilityInController() {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getBranchLiability());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-liability-data/by-years-of-month")
	public ResponseEntity<Object> getBranchLiabilityInController(@RequestParam("years") String year) {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getliabilityOfMonthsByYears(year));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-liability-data/all")
	public ResponseEntity<Object> getliabilityDataOfAll() {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getAllLiabilityAndApprovalLiability());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-liability-data/of-years")
	public ResponseEntity<Object> getliabilityDataOfYears() {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getliabilityByYears());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-all-wd-data/years-wise")
	public ResponseEntity<Object> getWdDataYearsWise(@RequestParam("wdCode") String wdCode) {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getAllWdDataYearswise(wdCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-all-wd-data/category-wise")
	public ResponseEntity<Object> getWdDatacategoryWise() {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getAllLibilityCategoryWise());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-all-data/quater&month-wise")
	public ResponseEntity<Object> getWdDatacategoryWise(@RequestParam("destrectionStatus") String destrectionStatus,
			@RequestParam("type") String type) {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService.getAllDataByDestrectionStatus(destrectionStatus, type));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get2-all-data/quater&month-wise")
	public ResponseEntity<Object> getWdData2() {
		try {
			return GlobalResponse.responseData("get all liability data by branch successfully !", HttpStatus.CREATED,
					this.reportService2.allSolution());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

}
