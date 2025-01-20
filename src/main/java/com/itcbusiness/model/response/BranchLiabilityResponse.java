package com.itcbusiness.model.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class BranchLiabilityResponse {

	private String branch;
	private Double liability;

}
