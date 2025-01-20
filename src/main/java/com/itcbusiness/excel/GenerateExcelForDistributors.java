package com.itcbusiness.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.itcbusiness.entity.DistributorsItem;

@Component
public class GenerateExcelForDistributors {

	public byte[] generateExcelFile(List<DistributorsItem> distributorsItem) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");
		Row headerRow = sheet.createRow(0);
		System.out.print("dddddddddddddddd");
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		String[] headers = { "Branch", "WdCode", "WdName", "Town", "contactName1", "contactNo1", "contactName1",
				"contactNo2", "address", "Liability", "Appr. Lbt", "Non-Appr. Lbt", "Destr. Lbt", "Rem.Destr. Lbt",
				"Surrender Lbt(op)" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		// Write data to the Excel sheet
		int rowNum = 1;
		for (DistributorsItem item : distributorsItem) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(item.getBranch());
			row.createCell(1).setCellValue(item.getWdCode());
			row.createCell(2).setCellValue(item.getWdName());
			row.createCell(3).setCellValue(item.getTown());
			row.createCell(4).setCellValue(item.getContactName1());
			row.createCell(5).setCellValue(item.getContactNumber1());
			row.createCell(6).setCellValue(item.getContactName2());
			row.createCell(7).setCellValue(item.getContactNumber2());
			row.createCell(8).setCellValue(item.getAddress());
			row.createCell(9).setCellValue(item.getLiability());
			row.createCell(10).setCellValue(item.getApprovalLiability());
			row.createCell(11).setCellValue(item.getLiability() - item.getApprovalLiability());
			row.createCell(12).setCellValue(item.getDestructionLiability());
			row.createCell(13).setCellValue(item.getApprovalLiability() - item.getDestructionLiability());
			row.createCell(14).setCellValue(item.getLiability() - item.getDestructionLiability());
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

}
