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
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.excel.GeneratePostSheet;
import com.itcbusiness.excel.GeneratePreSheet;
import com.itcbusiness.model.DistributorsItemDetailsModel;
import com.itcbusiness.pdf.GeneratePostSheetPdf;
import com.itcbusiness.pdf.GeneratePreSheetPdf;
import com.itcbusiness.service.DistributorsItemDetailsService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/distributorsItemDetails")
@Slf4j
public class DistributorsItemDetailsController {

	private DistributorsItemDetailsService distributorsItemDetailsService;
	private GeneratePreSheet generatePreSheet;
	private GeneratePostSheet generatePostSheet;
	private GeneratePreSheetPdf generatePreSheetPdf;
	private GeneratePostSheetPdf generatePostSheetPdf;

//	@Lookup
//	public GeneratePreSheetPdf generatePreSheetPdf() {
//		return null;
//	}

	public DistributorsItemDetailsController(DistributorsItemDetailsService distributorsItemDetailsService,
			GeneratePreSheet generatePreSheet, GeneratePostSheet generatePostSheet,
			GeneratePreSheetPdf generatePreSheetPdf, GeneratePostSheetPdf generatePostSheetPdf) {
		super();
		this.distributorsItemDetailsService = distributorsItemDetailsService;
		this.generatePreSheet = generatePreSheet;
		this.generatePostSheet = generatePostSheet;
		this.generatePreSheetPdf = generatePreSheetPdf;
		this.generatePostSheetPdf = generatePostSheetPdf;

	}

	@PostMapping("/add/item-details")
	public ResponseEntity<Object> addDistributorsItemDetails(
			@RequestBody List<DistributorsItemDetailsModel> distributerItemDetailsData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemDetailsService.addDistributorsItemDetails(distributerItemDetailsData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/item-details")
	public ResponseEntity<Object> getDistributorsItemDetails() {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemDetailsService.getAllDistributersItemDetails());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/all-item/of-especific-wd-month")
	public Flux<DistributorsItemDetails> getDistributorsItemDetails(@RequestParam("itemDetailsCode") String itemCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return distributorsItemDetailsService.getAllWdItemOfMonth(itemCode).onErrorResume(e -> {
				log.error(LogContant.logcontrollererroradd);
				return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
			});
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return Flux.error(new RuntimeException(ExceptionConstant.exceptioncontrolleradd + e.getMessage()));
		}
	}

	@GetMapping("/cancel/espesific-item/line-data")
	public ResponseEntity<Object> cancelEspesificLineItemData(@RequestParam("MaterialCode") String materialCode,
			@RequestParam("itemDetailsCode") String itemCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemDetailsService.cancelEspecificItem(materialCode, itemCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/cancel/espesific-item/line-test")
	public ResponseEntity<Object> cancelEspesificLineItemtest() {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemDetailsService.getLastUpdatedData());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/espesific-wd/get-audited-item")
	public ResponseEntity<Object> getAllAuditedItem(@RequestParam("materialCode") List<String> materialCode,
			@RequestParam("itemCode") String itemCode) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.distributorsItemDetailsService.getAllAuditedItemOfOOneWd(materialCode, itemCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/download/excel-pre-sheet/for-auditor")
	public ResponseEntity<Object> getAllMonthDownloadExcel(@RequestParam("itemDetailsCode") String itemDetailsCode) {
		try {
			List<DistributorsItemDetails> data = distributorsItemDetailsService.getAllWdItemOfMonth(itemDetailsCode)
					.collectList().block();
			byte[] pdf = this.generatePreSheet.generateExcelFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "pre_sheet" + data.get(0).getItemDetailCode() + ".pdf");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download/pdf-pre-sheet/for-auditor")
	public ResponseEntity<Object> getAllMonthDownloadpdf(@RequestParam("itemDetailsCode") String itemDetailsCode) {
		try {
			List<DistributorsItemDetails> data = distributorsItemDetailsService.getAllWdItemOfMonth(itemDetailsCode)
					.collectList().block();

			byte[] pdf = this.generatePreSheetPdf.generateExcelFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment",
					"pre_sheet_" + data.get(0).getItemDetailCode() + ".pdf");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while generating PDF:", e);
			return new ResponseEntity<>("Failed to generate PDF", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download/excel-post-sheet/for-auditor")
	public ResponseEntity<Object> getPostSheetExcel(@RequestParam("itemDetailsCode") String itemDetailsCode) {
		try {
			List<DistributorsItemDetails> data = distributorsItemDetailsService.getAllWdItemOfMonth(itemDetailsCode)
					.collectList().block();
			byte[] pdf = this.generatePostSheet.generateExcelFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment",
					"post_sheet" + data.get(0).getItemDetailCode() + ".pdf");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download/pdf-post-sheet/for-auditor")
	public ResponseEntity<Object> getPostSheetPdf(@RequestParam("itemDetailsCode") String itemDetailsCode) {
		try {
			List<DistributorsItemDetails> data = distributorsItemDetailsService.getAllWdItemOfMonth(itemDetailsCode)
					.collectList().block();
			byte[] pdf = this.generatePostSheetPdf.generatePdfFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment",
					"post_sheet_" + data.get(0).getItemDetailCode() + ".pdf");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.OK);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind, e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

}
