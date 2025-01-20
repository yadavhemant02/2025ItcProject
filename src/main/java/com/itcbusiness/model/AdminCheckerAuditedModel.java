package com.itcbusiness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AdminCheckerAuditedModel {

	private String itemDetailsCode;
	private String status;
	private String code;
	private String adminResion;

}
