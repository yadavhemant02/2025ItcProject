package com.itcbusiness.dtoforentity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ItemModel {

	private String materialCode;
	private String division;
	private Double liability;
	private Double salvag;
	private Double netLiability;
	private Double auditedLiability;
	private String destrectionStatus;
	private String status;

}
