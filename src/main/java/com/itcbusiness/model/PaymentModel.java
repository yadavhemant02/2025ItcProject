package com.itcbusiness.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PaymentModel {

	private String wdCode;
	private String invoiceNo;
	private String poNumber;
	private Double invoiceAmount;
	private String voucherNo;
	private LocalDate paymentDate;

}
