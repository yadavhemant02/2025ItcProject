package com.itcbusiness.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.model.PlanningDataModel;
import com.itcbusiness.model.TransientDataModel;
import com.itcbusiness.service.PlanningDataService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/planning")
@Slf4j
public class PlanningDataController {

	private PlanningDataService planningService;

	public PlanningDataController(PlanningDataService planningService) {
		super();
		this.planningService = planningService;
	}

	@PostMapping("/add/tatal-planning/data")
	public ResponseEntity<Object> addPlanningData(@RequestBody List<PlanningDataModel> planningData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Planning data add successfully !", HttpStatus.CREATED,
					this.planningService.savePlanningData(planningData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/tatal-planning/data")
	public Flux<TransientDataModel> getPlanningData() {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return planningService.getAllPlanningData().onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	@GetMapping("/get/tatal-liability-of/auditors")
	public Flux<TransientDataModel> getTotalLiabilityOfAuditors(@RequestParam("UserCode") String auditorCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return this.planningService.getAllRecordsOfOneAuditor(auditorCode).onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	@PutMapping("/update/pre-Planning-data")
	public ResponseEntity<Object> updatePlanningData(@RequestBody PlanningDataModel planningData) {
		try {
			return GlobalResponse.responseData("planning Data get successfully !", HttpStatus.CREATED,
					this.planningService.uupdateDataOfPrePlanning(planningData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorupdate);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerupdate + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/planning-data/by-pagination")
	public ResponseEntity<Object> getPlanningDataByPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			return GlobalResponse.responseData("find data successfully", HttpStatus.CREATED,
					this.planningService.getPlaningDataByPagination(page, size));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}

	}

	@GetMapping("/get/planning-data/by-Quaters")
	public ResponseEntity<Object> getPlanningDataByQuater(@RequestParam("Quater") String quater) {
		try {
			return GlobalResponse.responseData("find data successfully", HttpStatus.CREATED,
					this.planningService.getPlanningDataByQuater(quater));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

}
