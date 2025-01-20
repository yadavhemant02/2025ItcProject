package com.itcbusiness.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.repository.DistributorsItemRepository;

@Component
public class GeneratePostSheet {

	private DistributorsItemRepository distributorsItemRepository;

	public GeneratePostSheet(DistributorsItemRepository distributorsItemRepository) {
		super();
		this.distributorsItemRepository = distributorsItemRepository;
	}

	public byte[] generateExcelFile(List<DistributorsItemDetails> distributorsItemDetails) throws IOException {

		DistributorsItem data = this.distributorsItemRepository
				.findByWdCode(distributorsItemDetails.get(0).getItemDetailCode().substring(0, 6));
		Set<String> set = new HashSet<>();
		distributorsItemDetails = distributorsItemDetails.stream().filter((item) -> {
			set.add(item.getDivision());
			if (item.getLiability() == 0.0) {
				return false;
			} else {
				return true;
			}
		}).collect(Collectors.toList());

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");

		// Define the merged region
		CellRangeAddress mergedRegion = new CellRangeAddress(0, 7, 0, 4);
		sheet.addMergedRegion(mergedRegion);

		// Create styles for the text
		CellStyle mergedStyle = workbook.createCellStyle();
		mergedStyle.setWrapText(true);

		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		boldFont.setFontHeightInPoints((short) 14);

		Font regularFont = workbook.createFont();
		regularFont.setFontHeightInPoints((short) 12);

		// Create the row and cell for the merged region
		Row mergedRow = sheet.createRow(0);
		Cell mergedCell = mergedRow.createCell(0);

		// Create a rich text string to mix bold and regular text
		String sheetName = "Pre Sheet";
		String range = "01/08/2022 to 31/01/2023";
		String wdName = data.getWdName();
		String wdAddress = data.getAddress();
		String divisionItems = "Division Item :- " + set;
		String date = "" + java.time.LocalDate.now();

		XSSFRichTextString richText = new XSSFRichTextString();
		richText.append(sheetName + "\n", (XSSFFont) boldFont);
		richText.append(range + "\n");
		richText.append("WdCode :", (XSSFFont) boldFont);
		richText.append(wdName + "\n");
		richText.append("WdAddress :", (XSSFFont) boldFont);
		richText.append(wdAddress + "\n");
		richText.append("Division Item :", (XSSFFont) boldFont);
		richText.append(set + "\n");
		richText.append("Date :", (XSSFFont) boldFont);
		richText.append(date + "\n");
		mergedCell.setCellValue(richText);
		mergedCell.setCellStyle(mergedStyle);

		// Adjust row height and column width
//		mergedRow.setHeight((short) 600); // Adjust the row height
//		for (int col = 0; col <= 4; col++) {
//		    sheet.setColumnWidth(col, 6000); // Adjust column width
//		}

		mergedRow.setHeight((short) 600);
		for (int col = 0; col <= 6; col++) {
			sheet.setColumnWidth(col, 6000);
		}

		Row headerRow = sheet.createRow(11);
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

//		String[] headers = { "materialCode", "itemDetailsCode", "name", "division", "category", "liability", "salvage",
//				"netLiability" };
		String[] headers = { "Division", "Category", "Item Code", "Item Name", "UOM", "Total Qty", "Value",
				"Audited Qty", "Audited Value", "Salv Value", " Salv UOM" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		int rowNum = 12;
		for (DistributorsItemDetails item : distributorsItemDetails) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(item.getDivision());
			row.createCell(1).setCellValue(item.getCategory());
			row.createCell(2).setCellValue(item.getMaterialCode());
			row.createCell(3).setCellValue(item.getName());
			row.createCell(4).setCellValue("PAC");
			row.createCell(5).setCellValue(item.getWeight());
			row.createCell(6).setCellValue(item.getLiability());
			row.createCell(7).setCellValue(item.getAuditedLiability());
			row.createCell(8).setCellValue(item.getAuditedLiability() / (item.getLiability() / item.getWeight()));
			row.createCell(9).setCellValue("PAC");
			row.createCell(10).setCellValue(item.getSalvag());
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

}
