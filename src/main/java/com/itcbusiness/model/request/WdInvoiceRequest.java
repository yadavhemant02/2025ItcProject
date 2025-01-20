package com.itcbusiness.model.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class WdInvoiceRequest {

	private String branch;
	private String wdName;
	private String wdCode;
	private String month;
	private String years;
	private String town;
	private String category;
	private String invoiceNo;
	private MultipartFile invoiceFile;

}
