package com.itcbusiness.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.excel.GenerateExcelForPlanning;
import com.itcbusiness.model.ApprovalModel;
import com.itcbusiness.model.AuditorModel;
import com.itcbusiness.model.LiabilityDataModel;
import com.itcbusiness.model.PaymentModel;
import com.itcbusiness.service.LiabilityDataService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/liability")
@Slf4j
public class LiabilityDataController {

	private LiabilityDataService liabilityService;
	private GenerateExcelForPlanning generateExcelForPlanning;

	public LiabilityDataController(LiabilityDataService liabilityService,
			GenerateExcelForPlanning generateExcelForPlanning) {
		super();
		this.liabilityService = liabilityService;
		this.generateExcelForPlanning = generateExcelForPlanning;
	}

	/**
	 * %=% it all about initalData to add LiabilityData
	 * 
	 * @param libilityData
	 * @return
	 */
	@PostMapping("/add/tatal-liability/data")
	public ResponseEntity<Object> addLiabilityData(@RequestBody List<LiabilityDataModel> libilityData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Liability data add successfully !", HttpStatus.CREATED,
					this.liabilityService.saveLiabilityData(libilityData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	/**
	 * %=% get All data of LiabilityData
	 */
	@GetMapping("/get/tatal-liability/data")
	public Flux<LiabilityData> getLiabilityData() {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return liabilityService.getAllLiabilityData().onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	/**
	 * %=% get All data of LiabilityData
	 */
	@GetMapping("/get/tatal-liability/dataa")
	public ResponseEntity<Object> getLiabilityDataa() {

		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Liability data add successfully !", HttpStatus.CREATED,
					liabilityService.getAllLiabilityData().collectList().block());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/tatal-liability/data/by-years")
	public Flux<LiabilityData> getLiabilityDataByYears(@RequestParam("years") String years) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return liabilityService.getAllLiabilityDataOfYears(years).onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	@GetMapping("/get/all-unique-years")
	public ResponseEntity<Object> getAllYearsData() {
		try {
			return GlobalResponse.responseData("find all unique years data successfully", HttpStatus.CREATED,
					this.liabilityService.getAllYearsData());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	/**
	 * Not use Till Now
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/get/liability-data/by-pagination")
	public ResponseEntity<Object> getLiabilityDataByPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			return GlobalResponse.responseData("find data successfully", HttpStatus.CREATED,
					this.liabilityService.getLiabilityDataByPagination(page, size));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	/**
	 * %=% get All Distributer By DistributerName
	 * 
	 * @param wdName
	 * @return
	 */
	@GetMapping("/get-distributers/by-name")
	public ResponseEntity<Object> getDistributersByName(@RequestParam("wdname") String wdName) {
		try {
			return GlobalResponse.responseData("find distributer's all data successfully", HttpStatus.CREATED,
					this.liabilityService.getLiabilityDataOfEspecificDistributers(wdName));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-all-liabilityData/by-payments-done")
	public ResponseEntity<Object> getliabilityDataByPaymentsDone(@RequestParam("wdCode") String wdCode,
			@RequestParam("type") String status) {
		try {
			return GlobalResponse.responseData("find distributer's all data successfully", HttpStatus.CREATED,
					this.liabilityService.getAllDataOfLiabilityDatabyPaymentsDone(wdCode, status));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	/**
	 * %=% get All LiabilityData For Auditor
	 * 
	 * @param userCode
	 * @return
	 */
	@GetMapping("/get-distributers/for-auditor")
	public ResponseEntity<Object> getAllDistributerForAuditor(@RequestParam("userCode") String userCode) {
		try {
			return GlobalResponse.responseData("find distributer's all data successfully", HttpStatus.CREATED,
					this.liabilityService.getAllDistributerForAuditor(userCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	/**
	 * %=% get all data which is DistrectionPanding For Planning Data
	 * 
	 * @return
	 */
	@GetMapping("/get-liabilityData/which-audited-panding")
	public Flux<LiabilityData> getAllLiabilityDataWhichAuditedPanding(@RequestParam("Aproval") String approval,
			@RequestParam("DestrectionStatus") String destrectionStatus) {
		try {
			return this.liabilityService.getAllLiabilityDataDestructionPending(approval, destrectionStatus)
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

	@GetMapping("/get-liabilityData-in-excel/for-planning-data")
	public ResponseEntity<Object> getAllLiabilityForPlanningData(@RequestParam("Aproval") String approval,
			@RequestParam("DestrectionStatus") String destrectionStatus) {
		try {
			byte[] pdf = this.generateExcelForPlanning.generateExcelFile(this.liabilityService
					.getAllLiabilityDataDestructionPending(approval, destrectionStatus).collectList().block());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "planning.xlsx");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);

		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * for Report
	 * 
	 * @param wdCode
	 * @return
	 */
	@GetMapping("/get-liabilityData/by-wdCode")
	public Flux<LiabilityData> getAllLiabilityDataWhichAuditedPanding(@RequestParam("wdCode") String wdCode) {
		try {
			return this.liabilityService.getAllLiabilityDataByWdCode(wdCode).onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	@PostMapping("/update-liabilityData/approval-sheet")
	public ResponseEntity<Object> updateLiabilityDataByApprovalSheet(@RequestBody List<ApprovalModel> approvalData) {
		try {
			return GlobalResponse.responseData("update Approvalstatus", HttpStatus.CREATED,
					this.liabilityService.approvalLiability(approvalData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@PostMapping("/update-liabilityData/auditor-manually")
	public ResponseEntity<Object> updateLiabilityDataByAuditorManually(@RequestBody List<AuditorModel> auditorData) {
		try {
			return GlobalResponse.responseData("update auditor", HttpStatus.CREATED,
					this.liabilityService.updateAuditors(auditorData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@PostMapping("/update-liabilityData/for-payments")
	public ResponseEntity<Object> updateLiabilityDataForPayments(@RequestBody List<PaymentModel> paymentsData) {
		try {
			return GlobalResponse.responseData("update auditor", HttpStatus.CREATED,
					this.liabilityService.updatePaymentsdata(paymentsData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@PostMapping("/update-sarvage-value")
	public ResponseEntity<Object> updateSarvaageValue(@RequestParam Double salvageValue) {
		try {
			return GlobalResponse.responseData("update auditor", HttpStatus.CREATED,
					this.liabilityService.changeSalvageValue(salvageValue));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/download/excel-sheet")
	public ResponseEntity<Object> downloadExcelSheet(@RequestParam("value") String status) {
		try {
			List<LiabilityData> data = this.liabilityService.getAllDataOfLiabilityData();
			System.out.print(data.size());
			if ("all".equals(status)) {
				byte[] pdf = this.generateExcelForPlanning.generateExcelFile(data);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.setContentDispositionFormData("attachment",
						"post_sheet" + data.get(0).getItemDetailCode() + ".xlsx");
				ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
				return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
			} else {
				if ("yes".equals(status)) {
					data = data.stream().filter((item) -> {
						if (item.getPayments() != null && item.getPayments().equals("DONE")) {
							return true;
						} else {
							return false;
						}
					}).toList();
					byte[] pdf = this.generateExcelForPlanning.generateExcelFile(data);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.setContentDispositionFormData("attachment",
							"post_sheet" + data.get(0).getItemDetailCode() + ".xlsx");
					ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
					return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
				} else {
					data = data.stream().filter((item) -> {
						if (item.getPayments() == null) {
							return true;
						} else {
							return false;
						}
					}).toList();
					byte[] pdf = this.generateExcelForPlanning.generateExcelFile(data);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.setContentDispositionFormData("attachment",
							"post_sheet" + data.get(0).getItemDetailCode() + ".xlsx");
					ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
					return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

//	@GetMapping("/get-distributers/by-branch")
//	public Flux<LiabilityData> getAllDistributerByBranch(@RequestParam("Branch") String branch) {
//		try {
//			log.info(LogContant.logcontrollersuccessadd);
//			return this.liabilityService.getAllDistributerDataByBranch(branch).onErrorResume(e -> {
//				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
//			});
//		} catch (Exception e) {
//			log.error(LogContant.logcontrollererroradd);
//			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
//		}
//	}

//	@GetMapping("/get-distributers/by-category")
//	public Flux<LiabilityData> getAllDistributerByCategory(@RequestParam("category") String category) {
//		try {
//			return this.liabilityService.getAllDistributerDataByCatogary(category).onErrorResume(e -> {
//				log.error(LogContant.logcontrollererroradd);
//				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
//			});
//		} catch (Exception e) {
//			log.error(LogContant.logcontrollererroradd);
//			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
//		}
//	}

	// testing
//	@GetMapping(value = "/customers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	@GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
//	public Flux<LiabilityData> getCustomers() {
//		return Flux.fromIterable(this.repo.findAll());
//	}

}
