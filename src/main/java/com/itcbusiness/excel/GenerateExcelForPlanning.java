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

import com.itcbusiness.entity.LiabilityData;

@Component
public class GenerateExcelForPlanning {

	public byte[] generateExcelFile(List<LiabilityData> liabilityDatas) throws IOException {
		System.out.print(liabilityDatas.size());
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");
		Row headerRow = sheet.createRow(0);
		System.out.print("dddddddddddddddd");
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		String[] headers = { "Branch", "WdCode", "WdName", "Town", "mouth", "Quater", "Year", "Categoty", "Liability",
				"Approval", "Approval Liability", "Destrection Satus", "Destrection Liability", "Date", "Auditor",
				"Total wieght", "Salvage value", "invoiceNo", "Payment Status", "voucherNo", "PO Number",
				"Invoice Amount" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		// Write data to the Excel sheet
		int rowNum = 1;
		for (LiabilityData item : liabilityDatas) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(item.getBranch());
			row.createCell(1).setCellValue(item.getWdCode());
			row.createCell(2).setCellValue(item.getWdName());
			row.createCell(3).setCellValue(item.getTown());
			row.createCell(4).setCellValue(item.getMonth());
			row.createCell(5).setCellValue(item.getQuater());
			row.createCell(6).setCellValue(item.getYears());
			row.createCell(7).setCellValue(item.getCategory());
			row.createCell(8).setCellValue(item.getLiability());
			row.createCell(9).setCellValue(item.getApproval());
			row.createCell(10).setCellValue(item.getApprovalLiability());
			row.createCell(11).setCellValue(item.getDestructionStatus());
			row.createCell(12).setCellValue(item.getDestructionLiability());
			row.createCell(13).setCellValue(item.getTodayDate());
			if (item.getAuditor() != null && !item.getAuditor().isEmpty()) {
				row.createCell(14).setCellValue(item.getAuditor().get(0));
			}
			row.createCell(15).setCellValue(item.getTotalWeight());
			row.createCell(16).setCellValue(item.getSalvageValue());
			row.createCell(17).setCellValue(item.getInvoiceNo());
			row.createCell(18).setCellValue(item.getPayments());
			row.createCell(19).setCellValue(item.getVoucherNo());
			row.createCell(19).setCellValue(item.getPoNumber());
			row.createCell(20).setCellValue(item.getInvoiceAmount());
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

}
