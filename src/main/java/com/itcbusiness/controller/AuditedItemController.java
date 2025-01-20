package com.itcbusiness.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

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
import com.itcbusiness.model.AdminCheckerAuditedModel;
import com.itcbusiness.model.AuditedItemModel;
import com.itcbusiness.pdf.GeneratePostSheetPdf;
import com.itcbusiness.service.AuditedItemService;
import com.itcbusiness.service.DistributorsItemDetailsService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auditedItem")
@Slf4j
public class AuditedItemController {

	private AuditedItemService auditedItemService;
	private GeneratePostSheet generatePostSheet;
	private DistributorsItemDetailsService distributorsItemDetailsService;
	private GeneratePostSheetPdf generatePostSheetPdf;

	public AuditedItemController(AuditedItemService auditedItemService, GeneratePostSheet generatePostSheet,
			DistributorsItemDetailsService distributorsItemDetailsService, GeneratePostSheetPdf generatePostSheetPdf) {
		super();
		this.auditedItemService = auditedItemService;
		this.generatePostSheet = generatePostSheet;
		this.distributorsItemDetailsService = distributorsItemDetailsService;
		this.generatePostSheetPdf = generatePostSheetPdf;
	}

	// @PostMapping(value = "/sumbition-audited-item", consumes =
	// MediaType.MULTIPART_FORM_DATA_VALUE, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/sumbition-audited-item")
	public ResponseEntity<Object> sumbitionAuditedItem(@RequestBody AuditedItemModel auditedItemModel) {
		try {
			this.auditedItemService.auditedItemSumbition(auditedItemModel);
			List<DistributorsItemDetails> data = distributorsItemDetailsService
					.getAllWdItemOfMonth(auditedItemModel.getItemDetailsCode()).collectList().block();

			data = data.stream().filter((item) -> {
				if (item.getLiability() != 0.0) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());

			byte[] pdf = this.generatePostSheetPdf.generatePdfFile(data);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment",
					"post_sheet_" + data.get(0).getItemDetailCode() + ".pdf");
			ByteArrayInputStream stream = new ByteArrayInputStream(pdf);
			return new ResponseEntity<>(new InputStreamResource(stream), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST,
					e.getMessage());
		}
	}

	@GetMapping("/get-audited-item/by-liability-code")
	public ResponseEntity<Object> getAuditeditemByliabilityCode(@RequestParam("liabilityCode") String liabilityCode) {
		try {
			return GlobalResponse.responseData(" get Audited Qunatity Successfully", HttpStatus.CREATED,
					this.auditedItemService.getAuditedItemsByLiabilityData(liabilityCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-audited-item/by-wdCode")
	public ResponseEntity<Object> getAuditeditemBywdcode(@RequestParam("wdCode") String wdCode) {
		try {
			return GlobalResponse.responseData(" get Audited Qunatity Successfully", HttpStatus.CREATED,
					this.auditedItemService.getAuditedItemsByWdCode(wdCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-audited-item/by-itemDetailsCode")
	public ResponseEntity<Object> getAuditeditemByitemDetailsCode(
			@RequestParam("itemDetailsCode") String itemDetailsCode) {
		try {
			return GlobalResponse.responseData(" get Audited Qunatity Successfully", HttpStatus.CREATED,
					this.auditedItemService.getAuditedItemsByItemDetailsCode(itemDetailsCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

	@PostMapping("/check-by/admin-audited/for-status")
	public ResponseEntity<Object> postStatusByAdminForAudited(
			@RequestBody AdminCheckerAuditedModel adminCheckerAuditedModel) {
		try {
			return GlobalResponse.responseData(" get Audited Qunatity Successfully", HttpStatus.CREATED,
					this.auditedItemService.statusOfAdminForAuditedItem(adminCheckerAuditedModel.getItemDetailsCode(),
							adminCheckerAuditedModel.getStatus(), adminCheckerAuditedModel.getCode(),
							adminCheckerAuditedModel.getAdminResion()));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd, HttpStatus.BAD_REQUEST, null);
		}
	}

}
