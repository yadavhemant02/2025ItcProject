package com.itcbusiness.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.excel.GenerateExcelForDistributors;
import com.itcbusiness.model.DistributorsItemModel;
import com.itcbusiness.service.DistributorsItemService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/distributorsItem")
@Slf4j
public class DistributorsItemController {

	private DistributorsItemService distributorsItemService;
	private GenerateExcelForDistributors generateExcelForDistributors;

	public DistributorsItemController(DistributorsItemService distributorsItemService,
			GenerateExcelForDistributors generateExcelForDistributors) {
		super();
		this.distributorsItemService = distributorsItemService;
		this.generateExcelForDistributors = generateExcelForDistributors;
	}

	@PostMapping("/add/all-distributers")
	public ResponseEntity<Object> addAllDistributers(@RequestBody List<DistributorsItemModel> distributorsData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemService.addDistributers(distributorsData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/all-distributers")
	public Flux<DistributorsItem> getAllDistributers() {
		try {
//			List<DistributorsItem> data = this.distributorsItemService.getAllDistributers().collectList().block();
//			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED, data);
			return this.distributorsItemService.getAllDistributers().onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
//			log.error(LogContant.logcontrollererroradd);
//			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
//					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/download/excel-sheet")
	public ResponseEntity<Object> downloadExcelSheet() {
		try {
			List<DistributorsItem> data = this.distributorsItemService.getAllDistributers().collectList().block();
			System.out.print(data.size());

			byte[] pdf = this.generateExcelForDistributors.generateExcelFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "DistributorsLiability.xlsx");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/all-distributers/which-have-audited/details")
	public Flux<DistributorsItem> getAllDistributersWhichHaveAuditedDetails(
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
			@RequestParam("type") String type) {
		try {
			return this.distributorsItemService.getallWdWhichAreAuditedAtLestOneTime(startDate, endDate, type)
					.onErrorResume(e -> {
						log.error(LogContant.logcontrollererroradd);
						return Flux
								.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
					});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	@GetMapping("/get/especific-distributers-data")
	public ResponseEntity<Object> getEspecificDistributosData(@RequestParam("wdCode") String wdCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemService.getOneDistributorsItem(wdCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@PutMapping("/update/wd-master-data")
	public ResponseEntity<Object> getEspecificDistributosData(@RequestBody DistributorsItemModel data) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemService.updateMasterWDData(data));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

}
