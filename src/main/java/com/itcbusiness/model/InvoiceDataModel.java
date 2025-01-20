package com.itcbusiness.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class InvoiceDataModel {

	private String invoiceNo;
	private String itemDetailsCode;
	private MultipartFile invoiceImage;

}
