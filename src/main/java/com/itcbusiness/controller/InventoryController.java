package com.itcbusiness.controller;

import java.util.List;

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
import com.itcbusiness.entity.Inventory;
import com.itcbusiness.model.InventoryModel;
import com.itcbusiness.service.InventoryService;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

	private InventoryService inventoryService;

	public InventoryController(InventoryService inventoryService) {
		super();
		this.inventoryService = inventoryService;
	}

	@PostMapping("/add/data-of-inventory-product")
	public ResponseEntity<Object> addInventoryData(@RequestBody List<InventoryModel> inventoryData) {
		try {
			log.info(LogContant.logcontrollersuccessadd);
			return GlobalResponse.responseData("Inventory data add successfully !", HttpStatus.CREATED,
					this.inventoryService.saveInventoryData(inventoryData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererroradd);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrolleradd + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping(value = "/get/data-of-inventory-product", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Inventory> addInventoryData() {
		// return this.inventoryService.getAllInventoryData();
		return inventoryService.getAllInventoryData().doOnError(e -> {
			log.error("Error retrieving inventory data: {}", e.getMessage());
		}).onErrorResume(e -> {
			return Flux.empty();
		});
	}

	@PostMapping("/get/data-of-inventory-by-division")
	public ResponseEntity<Object> getInventoryDataFilterByDivison(@RequestParam("division") List<String> division) {
		try {
			return GlobalResponse.responseData(" find Inventory data by division successfully !", HttpStatus.CREATED,
					this.inventoryService.getAllInventoryDataByDivision(division));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/Inventory-data/by-pagination")
	public ResponseEntity<Object> getInventoryDataByPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			return GlobalResponse.responseData("find data by pagination successfully", HttpStatus.CREATED,
					this.inventoryService.getInventoryDataByPagination(page, size));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/Inventory-data-of-category/by-pagination")
	public ResponseEntity<Object> getInventoryDataOfCategoryByPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("category") String category) {
		try {
			return GlobalResponse.responseData("find data by pagination successfully", HttpStatus.CREATED,
					this.inventoryService.getInventoryDataOfCategoryByPagination(page, size, category));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get/Inventory-data-of-divesion/by-pagination")
	public ResponseEntity<Object> getInventoryDataOfDivisionByPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam("division") String division) {
		try {
			return GlobalResponse.responseData("find data by pagination successfully", HttpStatus.CREATED,
					this.inventoryService.getInventoryDataOfdivisionByPagination(page, size, division));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/count/no-of-row/in-inventory-data")
	public ResponseEntity<Object> countNoOfRowInInventoryData() {
		try {
			return GlobalResponse.responseData("count inventory data successfully", HttpStatus.CREATED,
					this.inventoryService.countofInventoryData());
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@GetMapping("/get-inventor-data/by-material-code")
	public ResponseEntity<Object> getInventoryDataByMaterialCode(@RequestParam("materialCode") String materialCode) {
		try {
			return GlobalResponse.responseData("count inventory data successfully", HttpStatus.CREATED,
					this.inventoryService.getInventoryData(materialCode));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

	@PutMapping("/update-inventor-data")
	public ResponseEntity<Object> updateInventoryData(@RequestBody Inventory inventoryData) {
		try {
			return GlobalResponse.responseData("count inventory data successfully", HttpStatus.CREATED,
					this.inventoryService.updateInventoryData(inventoryData));
		} catch (Exception e) {
			log.error(LogContant.logcontrollererrorfind);
			return GlobalResponse.responseData(ExceptionConstant.exceptioncontrollerfind + e.getMessage(),
					HttpStatus.BAD_REQUEST, null);
		}
	}

//	@GetMapping(value = "/customerss", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<Inventory> getCustomers() {
//		return Flux.fromIterable(this.repo.findAll());
//	}

}
