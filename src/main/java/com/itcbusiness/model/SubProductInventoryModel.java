package com.itcbusiness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class SubProductInventoryModel {

	private String productSubName;
	private String productSubCode;
	private int quntity;
	private Double price;

}
