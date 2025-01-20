package com.itcbusiness.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LiabilityDataModel {

	private String branch;
	private String wdCode;
	private String wdName;
	private String town;
	private String month;
	private String quater;
	private String years;
	private Double liability;
	private List<String> auditor;
	private String approval;
	private String destructionStatus;
	private String sheetName;

}
