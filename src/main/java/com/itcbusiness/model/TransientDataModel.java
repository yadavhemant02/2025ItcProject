package com.itcbusiness.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
public class TransientDataModel {

	private String branch;
	private String wdCode;
	private String wdName;
	private String town;
	private String month;
	private String quater;
	private String years;
	private String tatalLiability;
	private String category;
	private String approval;
	private String destructionStatus;
	private String planning;
	private String remaining;
	// private List<String> auditor;
	private Map<String, List<String>> auditor;
	private String liabilityCode;
	private LocalDate todayDate;

}
