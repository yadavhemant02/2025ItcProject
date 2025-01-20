package com.itcbusiness.model;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DistributorOrderModel {

	private String distributorName;
	private String distributorCode;
	private HashMap<String, List<Object>> orderProducts;

}
