package com.itcbusiness.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ProductInventoryModel {

	private String productName;
	private String productCode;
	private List<SubProductInventoryModel> subProduct;

}
