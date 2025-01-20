package com.itcbusiness.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Mend2CalculationRequest {

	private String glCode;
	private String month;
	private String provision;

}
