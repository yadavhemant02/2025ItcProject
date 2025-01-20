package com.itcbusiness.model.response;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Data
@Setter
@Getter
public class MendSheetResponse {

	private String glCode;
	private String glDescription;
	private List<MonthAmountResponse> monthAmount;
	private Double amount;
	private String codeId;
	private Double provision;
	private Double lastMonth;
	private Double average;
	private Double differnce;
	private Double perDifference;

}
