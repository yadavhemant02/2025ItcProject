package com.itcbusiness.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class XcelReadController {

	@PostMapping("/get-data-xcel")
	private List<List<String>> getecelData(@RequestParam("file") MultipartFile file) {
		try {
			List<List<String>> dataa = new ArrayList<>();
			try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
				XSSFSheet sheet = workbook.getSheetAt(0);

				for (Row row : sheet) {
					List<String> rowData = new ArrayList<>();
					row.forEach(cell -> rowData.add(cell.toString()));
					dataa.add(rowData);
				}
			}
			return dataa;
		} catch (Exception e) {
			e.getStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}

	}

}
