package com.itcbusiness.pdf;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

@Component
public class GeneratePostSheetPdf {

	private final DistributorsItemRepository distributorsItemRepository;

	public GeneratePostSheetPdf(DistributorsItemRepository distributorsItemRepository) {
		this.distributorsItemRepository = distributorsItemRepository;
	}

	public byte[] generatePdfFile(List<DistributorsItemDetails> distributorsItemDetails) throws Exception {
		if (distributorsItemDetails == null || distributorsItemDetails.isEmpty()) {
			throw new IllegalArgumentException("The distributorsItemDetails list is empty.");
		}

		// Fetch distributor item data
		String wdCode = distributorsItemDetails.get(0).getItemDetailCode().substring(0, 6);
		DistributorsItem data = this.distributorsItemRepository.findByWdCode(wdCode);

		if (data == null) {
			throw new IllegalArgumentException("No distributor found for WD Code: " + wdCode);
		}

		// Filter and process the details
		Set<String> divisions = new HashSet<>();
		distributorsItemDetails = distributorsItemDetails.stream().filter(item -> {
			if (item.getLiability() != 0.0) {
				divisions.add(item.getDivision());
			}
			return item.getLiability() != 0.0;
		}).collect(Collectors.toList());

		// Create PDF document
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(bos);
		PdfDocument pdfDocument = new PdfDocument(writer);
		Document document = new Document(pdfDocument);

		// Add header
		String sheetName = "Post Sheet";
		String range = "01/08/2022 to 31/01/2023";
		String wdName = data.getWdName();
		String wdAddress = data.getAddress();
		String divisionItems = "Division Items: " + divisions;
		String date = LocalDate.now().toString();
		String category = distributorsItemDetails.get(0).getCategory();

		Paragraph header = new Paragraph().add(sheetName + "\n").add(range + "\n").add("WdCode: " + wdName + "\n")
				.add("WdAddress: " + wdAddress + "\n").add(divisionItems + "\n").add("Date: " + date + "   ")
				.add("Category: " + category + "\n").setTextAlignment(TextAlignment.CENTER);
		document.add(header);

		// Define table headers
		String[] headers = { "Division", "Item Code", "Item Name", "UOM", "Total wieght", "Qty", "Value",
				"Audited weight", "Audited Value", "Salv Value" };

		// Table table = new Table(new float[] { 1, 1.5f, 2, 1, 1, 1.5f, 1.5f, 1.5f,
		// 1.5f });
		Table table = new Table(new float[] { 1, 2, 2, 1, 1, 1f, 1f, 1f, 1f, 1f });
		table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100)); // Table spans the page width

		for (String headerTitle : headers) {
			Cell headerCell = new Cell().add(new Paragraph(headerTitle)).setBackgroundColor(DeviceGray.GRAY)
					.setTextAlignment(TextAlignment.CENTER).setFontSize(8);
			table.addHeaderCell(headerCell);
		}

		for (DistributorsItemDetails item : distributorsItemDetails) {
			table.addCell(new Cell().add(new Paragraph(item.getDivision()).setFontSize(8)));
			table.addCell(new Cell().add(new Paragraph(item.getMaterialCode()).setFontSize(8)));
			table.addCell(new Cell().add(new Paragraph(item.getName()).setFontSize(8)));
			table.addCell(new Cell().add(new Paragraph("PAC").setFontSize(8)));
			table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getWeight())).setFontSize(8)));
			table.addCell(
					new Cell().add(new Paragraph(String.valueOf(item.getQuantityPac().intValue())).setFontSize(8)));
			table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getLiability())).setFontSize(8)));
			table.addCell(new Cell().add(
					new Paragraph(String.valueOf(item.getAuditedLiability() / (item.getLiability() / item.getWeight())))
							.setFontSize(8)));
			table.addCell(
					new Cell().add(new Paragraph(String.format("%.2f", item.getAuditedLiability())).setFontSize(8)));
			table.addCell(new Cell().add(new Paragraph(("FOOD".equals(item.getCategory())
					&& ("AT".equals(item.getDivision()) && "AT".equals(item.getDivision()))
							? String.valueOf(item.getSalvag())
							: ""))
					.setFontSize(8)));
		}
		document.add(table);

		document.close();

		return bos.toByteArray();
	}
}
