package com.itcbusiness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DistributorsItemDetailsModel {

	private String branch;
	private String category;
	private String wdCode;
	private String wdName;
	private String town;
	private String month;
	private String materialCode;
	private String name;
	private String division;
	private Double liability;
	private Double perItemSalvage; // wieght
	private Double salvageValue;
	private Double salvageValue1;

}
