package com.itcbusiness.model;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AuditedItemModel {

	private String wdCode;
	private double longitude;
	private double latitude;
	private String liabilityCode;
	private double auditedQuantity;
	private int alert;
	private String alertSpecification;
	private String destrectionStatus;
	private String itemDetailsCode;
	private int timeDuration;
	private String auditedAddress;
	@NotNull
	@Valid
	private Map<String, List<String>> mCodesAuditedValue;
}
