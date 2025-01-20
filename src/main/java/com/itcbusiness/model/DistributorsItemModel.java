package com.itcbusiness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DistributorsItemModel {

	private String branch;
	private String wdCode;
	private String wdName;
	private String town;
	private String contactNumber1;
	private String contactName1;
	private String contactNumber2;
	private String contactName2;
	private String address;
	private double longitude;
	private double latitude;

}
