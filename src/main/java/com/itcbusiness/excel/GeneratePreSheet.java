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
public class GeneratePreSheet {

	private DistributorsItemRepository distributorsItemRepository;

	public GeneratePreSheet(DistributorsItemRepository distributorsItemRepository) {
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
			row.createCell(7).setCellValue("");
			row.createCell(8).setCellValue("");
			row.createCell(9).setCellValue("");
			row.createCell(10).setCellValue("");
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();

	}

}

//package com.itcbusiness.excel;
//
//import java.io.ByteArrayOutputStream;
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Component;
//
//import com.itcbusiness.entity.DistributorsItem;
//import com.itcbusiness.entity.DistributorsItemDetails;
//import com.itcbusiness.repository.DistributorsItemRepository;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Cell;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.element.Table;
//import com.itextpdf.layout.property.TextAlignment;
//
//@Component
//public class GeneratePreSheet {
//
//	private final DistributorsItemRepository distributorsItemRepository;
//
//	public GeneratePreSheet(DistributorsItemRepository distributorsItemRepository) {
//		this.distributorsItemRepository = distributorsItemRepository;
//	}
//
//	public byte[] generateExcelFile(List<DistributorsItemDetails> distributorsItemDetails) throws Exception {
//		if (distributorsItemDetails == null || distributorsItemDetails.isEmpty()) {
//			throw new IllegalArgumentException("The distributorsItemDetails list is empty.");
//		}
//
//		// Fetch distributor item data
//		String wdCode = distributorsItemDetails.get(0).getItemDetailCode().substring(0, 6);
//		DistributorsItem data = this.distributorsItemRepository.findByWdCode(wdCode);
//
//		if (data == null) {
//			throw new IllegalArgumentException("No distributor found for WD Code: " + wdCode);
//		}
//
//		// Filter and process the details
//		Set<String> divisions = new HashSet<>();
//		distributorsItemDetails = distributorsItemDetails.stream().filter(item -> {
//			divisions.add(item.getDivision());
//			return item.getLiability() != 0.0;
//		}).collect(Collectors.toList());
//
//		// Create PDF document
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		PdfWriter writer = new PdfWriter(bos);
//		PdfDocument pdfDocument = new PdfDocument(writer);
//		Document document = new Document(pdfDocument);
//
//		// Add header
//		String sheetName = "Pre Sheet";
//		String range = "01/08/2022 to 31/01/2023";
//		String wdName = data.getWdName();
//		String wdAddress = data.getAddress();
//		String divisionItems = "Division Items: " + divisions;
//		String date = LocalDate.now().toString();
//
//		Paragraph header = new Paragraph().add(sheetName + "\n").add(range + "\n").add("WdCode: " + wdName + "\n")
//				.add("WdAddress: " + wdAddress + "\n").add(divisionItems + "\n").add("Date: " + date + "\n")
//				.setTextAlignment(TextAlignment.CENTER);
//		document.add(header);
//
//		// Define table headers
//		String[] headers = { "Division", "Category", "Item Code", "Item Name", "UOM", "Total Qty", "Value",
//				"Audited Qty", "Audited Value", "Salv Value", "Salv UOM" };
//		Table table = new Table(headers.length);
//		table.setWidthPercent(100);
//
//		// Add table header cells with styling
//		for (String headerTitle : headers) {
//			Cell headerCell = new Cell().add(new Paragraph(headerTitle));
//			headerCell.setBackgroundColor(com.itextpdf.kernel.color.DeviceGray.GRAY);
//			headerCell.setTextAlignment(TextAlignment.CENTER);
//			table.addHeaderCell(headerCell);
//		}
//
//		// Populate table rows
//		for (DistributorsItemDetails item : distributorsItemDetails) {
//			table.addCell(new Cell().add(new Paragraph(item.getDivision())));
//			table.addCell(new Cell().add(new Paragraph(item.getCategory())));
//			table.addCell(new Cell().add(new Paragraph(item.getMaterialCode())));
//			table.addCell(new Cell().add(new Paragraph(item.getName())));
//			table.addCell(new Cell().add(new Paragraph("PAC")));
//			table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
//			table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getLiability()))));
//			table.addCell(new Cell().add(new Paragraph("")));
//			table.addCell(new Cell().add(new Paragraph("")));
//			table.addCell(new Cell().add(new Paragraph("")));
//			table.addCell(new Cell().add(new Paragraph("")));
//		}
//
//		// Add table to document
//		document.add(table);
//
//		// Close document
//		document.close();
//
//		return bos.toByteArray();
//	}
//}
