package com.itcbusiness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class InventoryModel {

	private String materialCode;
	private String division;
	private String category;
	private String oldMaterialsNo;
	private String numerat;
	private String denom;
	private String aUn;
	private Double weight;

}
