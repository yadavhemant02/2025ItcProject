package com.itcbusiness.model.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class MonthAmountResponse implements Comparable<MonthAmountResponse> {

	private String month;
	private int monthInt;
	private Double amount;

	@Override
	public int compareTo(MonthAmountResponse o) {
		return this.monthInt - o.getMonthInt();
	}

}
