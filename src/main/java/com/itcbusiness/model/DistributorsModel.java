package com.itcbusiness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DistributorsModel {

	private String distributorName;
	private String address1;
	private String address2;
	private String country;
	private String state;
	private String city;
	private int pinCode;

}
