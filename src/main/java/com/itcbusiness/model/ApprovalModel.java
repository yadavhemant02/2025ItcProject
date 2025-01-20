package com.itcbusiness.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ApprovalModel {

	private String wdCode;
	private String month;
	private String category;
	private String approvalStatus;
	private Double approvalAmount;
	private List<String> auditor;
	private LocalDate auditedDate;

}
