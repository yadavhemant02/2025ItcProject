package com.itcbusiness.model;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PlanningDataModel {

	private String liability;
	private String planning;
	private String remaining;
	private String liabilityCode;
	private Map<String, List<String>> auditor;

}
