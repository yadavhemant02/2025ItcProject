package com.itcbusiness.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itcbusiness.custome.status.GlobalResponse;
import com.itcbusiness.entity.MendSheetData;
import com.itcbusiness.entity.MendSheetDownload;
import com.itcbusiness.excel.GenerateExcelForMend;
import com.itcbusiness.model.MendProvisionCalculationModel;
import com.itcbusiness.model.response.MendSheetResponse;
import com.itcbusiness.repository.MendSheetDownloadRepository;
import com.itcbusiness.service.MendSheetDataService;
import com.itcbusiness.service.MendSheetDownloadService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/m&d")
@Slf4j
public class MendSheetDataController {

	private MendSheetDataService mendSheetDataService;
	private final GenerateExcelForMend generateExcelForMend;
	private final MendSheetDownloadRepository mendSheetDownloadRepository;
	private MendSheetDownloadService mendSheetDownloadService;

	public MendSheetDataController(MendSheetDataService mendSheetDataService, GenerateExcelForMend generateExcelForMend,
			MendSheetDownloadRepository mendSheetDownloadRepository,
			MendSheetDownloadService mendSheetDownloadService) {
		super();
		this.mendSheetDataService = mendSheetDataService;
		this.generateExcelForMend = generateExcelForMend;
		this.mendSheetDownloadRepository = mendSheetDownloadRepository;
		this.mendSheetDownloadService = mendSheetDownloadService;
	}

	@PostMapping("/add/intial-sheet/for-m&d")
	public ResponseEntity<Object> sumbitionAuditedItem(@RequestBody List<MendSheetData> mendSheetData) {
		try {
			return GlobalResponse.responseData("M&D sheet UPloaded Successfully", HttpStatus.CREATED,
					this.mendSheetDataService.addMandDdataInitail(mendSheetData));
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-sheet/for-m&d")
	public ResponseEntity<Object> getSheetForMD() {
		try {
			this.mendSheetDataService.getMendSheetResponseData().stream().forEach((item) -> {
				if (item.getGlCode() != null && item.getGlCode().equals("4319200013")) {
					System.out.print(item);
				}
			});
			return GlobalResponse.responseData("M&D sheet fetch Successfully", HttpStatus.CREATED,
					this.mendSheetDataService.getMendSheetResponseData());
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/add/provision-sheet/for-m&d/{date}")
	public ResponseEntity<Object> updateAndCalculationMend(
			@RequestBody List<MendProvisionCalculationModel> mendSheetData, @PathVariable("date") LocalDate date) {
		try {
			String data = this.mendSheetDataService.updateProvisionValueAndCalculation(mendSheetData, date);
			List<MendSheetResponse> forByte = this.mendSheetDataService.getMendSheetResponseData();
			forByte = forByte.stream().filter(item -> item.getCodeId().equals(LocalDate.now().toString()))
					.collect(Collectors.toList());
			byte[] pdf = this.generateExcelForMend.generateExcelFile(forByte);
			Optional<MendSheetDownload> mendSheetDownload = this.mendSheetDownloadRepository
					.findByMonth(date.toString());
			if (mendSheetDownload.isEmpty()) {
				MendSheetDownload dd = new MendSheetDownload();
				dd.setMonth(date.toString());
				dd.setYears(date.toString().substring(0, 4));
				dd.setSheetData(pdf);
				this.mendSheetDownloadRepository.save(dd);
			} else {
				MendSheetDownload dd = mendSheetDownload.get();
				dd.setSheetData(pdf);
				this.mendSheetDownloadRepository.save(dd);
			}
			return GlobalResponse.responseData("M&D sheet UPloaded Successfully", HttpStatus.CREATED, data);
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-liabilityData-in-excel/for-Mend-cal-/By-financialYear")
	public ResponseEntity<Object> getAllLiabilityForMendCalculation(
			@RequestParam("financialYear") String financialYear) {
		try {
			List<MendSheetResponse> data = this.mendSheetDataService.getMendSheetResponseData();
			if (financialYear.equals("all")) {
				data = data.stream().filter((item) -> {
					if (item.getCodeId().equals("N/A")) {
						return false;
					} else {
						return true;
					}
				}).collect(Collectors.toList());
			} else {
				data = data.stream().filter(item -> item.getCodeId().equals(financialYear))
						.collect(Collectors.toList());
			}
			byte[] pdf = this.generateExcelForMend.generateExcelFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "M&DCalculation.xlsx");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);

		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-all/financial-year")
	public ResponseEntity<Object> getAllFinancialYears() {
		try {
			return new ResponseEntity<>(this.mendSheetDataService.getAllFinancialYears(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-all/months-for-download-excel")
	public ResponseEntity<Object> getAllMonthDownload() {
		try {
			return new ResponseEntity<>(this.mendSheetDownloadService.getAllDateWhichContainMendSheet(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/download/mend-sheet/by-month")
	public ResponseEntity<Object> getAllMonthDownload(@RequestParam("month") String month) {
		try {
			byte[] pdf = this.mendSheetDownloadService.getMendSheetDownloadByMonth(month);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "M&DCalculation.xlsx");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

}
