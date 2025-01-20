package com.itcbusiness.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.itcbusiness.model.response.MendSheetResponse;

@Component
public class GenerateExcelForMend {

//	public byte[] generateExcelFile(List<MendSheetResponse> liabilityDatas) throws IOException {
//		System.out.print(liabilityDatas.size());
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("MendSheet");
//		Row headerRow = sheet.createRow(0);
//
//		CellStyle headerStyle = workbook.createCellStyle();
//		Font font = workbook.createFont();
//		font.setBold(true);
//		headerStyle.setFont(font);
//
//		String[] headers = { "GlCode", "GlDescription", "years/months", "01", "02", "03", "04", "04", "06", "07", "08",
//				"09", "10", "11", "12", "GrandTotal", "Provision", "CurrentMonths", "Average", "Difference",
//				"% IN Difference " };
//		for (int i = 0; i < headers.length; i++) {
//			Cell cell = headerRow.createCell(i);
//			cell.setCellValue(headers[i]);
//			cell.setCellStyle(headerStyle);
//		}
//
//		// Write data to the Excel sheet
//		int rowNum = 1;
//		for (MendSheetResponse item : liabilityDatas) {
//			Row row = sheet.createRow(rowNum++);
//			row.createCell(0).setCellValue(item.getGlCode());
//			row.createCell(1).setCellValue(item.getGlDescription());
//			row.createCell(2).setCellValue(item.getCodeId());			
//			row.createCell(3).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(0) != null ? item.getMonthAmount().get(0).getAmount() : 0.0);
//			row.createCell(4).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(1) != null ? item.getMonthAmount().get(1).getAmount() : 0.0);
//			row.createCell(5).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(2) != null ? item.getMonthAmount().get(2).getAmount() : 0.0);
//			row.createCell(6).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(3) != null ? item.getMonthAmount().get(3).getAmount() : 0.0);
//			row.createCell(7).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(4) != null ? item.getMonthAmount().get(4).getAmount() : 0.0);
//			row.createCell(8).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(5) != null ? item.getMonthAmount().get(5).getAmount() : 0.0);
//			row.createCell(9).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(6) != null ? item.getMonthAmount().get(6).getAmount() : 0.0);
//			row.createCell(10).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(7) != null ? item.getMonthAmount().get(7).getAmount() : 0.0);
//			row.createCell(11).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(8) != null ? item.getMonthAmount().get(8).getAmount() : 0.0);
//			row.createCell(12).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(9) != null ? item.getMonthAmount().get(9).getAmount() : 0.0);
//			row.createCell(13).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(10) != null ? item.getMonthAmount().get(10).getAmount() : 0.0);
//			row.createCell(14).setCellValue(item.getMonthAmount() != null && !item.getMonthAmount().isEmpty()
//					&& item.getMonthAmount().get(11) != null ? item.getMonthAmount().get(11).getAmount() : 0.0);
//			row.createCell(15).setCellValue(item.getAmount());
//			row.createCell(16).setCellValue(item.getProvision());
//			row.createCell(17).setCellValue(item.getLastMonth());
//			row.createCell(18).setCellValue(item.getAverage());
//			row.createCell(19).setCellValue(item.getDiffernce());
//			row.createCell(20).setCellValue(item.getPerDifference());
//		}
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		workbook.write(bos);
//		workbook.close();
//		return bos.toByteArray();
//	}

	public byte[] generateExcelFile(List<MendSheetResponse> liabilityDatas) throws IOException {
		System.out.print(liabilityDatas.size());
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("MendSheet");
		Row headerRow = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		ArrayList<String> da = new ArrayList<>();

		liabilityDatas.stream().forEach((item) -> {
			if (da.size() < item.getMonthAmount().size()) {
				da.clear();
				for (int i = 0; i < item.getMonthAmount().size(); i++) {
					da.add(item.getMonthAmount().get(i).getMonth());
				}
			}
		});
		ArrayList<String> daa = new ArrayList<>(Arrays.asList("GlCode", "GlDescription", "years/months"));
		daa.addAll(da);
		daa.addAll(Arrays.asList("GrandTotal", "Provision", LocalDate.now().toString(), "Average", "Difference",
				"% IN Difference "));

		String[] headers = daa.toArray(new String[0]);

//		String[] headers = { "GlCode", "GlDescription", "years/months", "01", "02", "03", "04", "04", "06", "07", "08",
//				"09", "10", "11", "12", "GrandTotal", "Provision", LocalDate.now().toString(), "Average", "Difference",
//				"% IN Difference " };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		// Write data to the Excel sheet
		int rowNum = 1;
		for (MendSheetResponse item : liabilityDatas) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(item.getGlCode());
			row.createCell(1).setCellValue(item.getGlDescription());
			row.createCell(2).setCellValue(item.getCodeId());

			for (int i = 0; i < item.getMonthAmount().size(); i++) {
				System.out.println(item.getAmount());
				double amount = 0.0;
				if (item.getAmount() == null) {
					continue;
				}
				if (item.getMonthAmount() != null && item.getMonthAmount().size() > i
						&& item.getMonthAmount().get(i) != null) {
					amount = item.getMonthAmount().get(i).getAmount();
				}
				row.createCell(3 + i).setCellValue(amount);
			}
			row.createCell(15).setCellValue(item.getAmount() != null ? item.getAmount() : 0.0);
			row.createCell(16).setCellValue(item.getProvision());
			row.createCell(17).setCellValue(item.getLastMonth());
			row.createCell(18).setCellValue(item.getAverage());
			row.createCell(19).setCellValue(item.getDiffernce());
			row.createCell(20).setCellValue(item.getPerDifference());
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

}
