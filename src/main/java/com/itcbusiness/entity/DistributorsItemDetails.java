package com.itcbusiness.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class DistributorsItemDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String materialCode;
	private String itemDetailCode;
	private String name;
	private String division;
	private String category;
	private Double liability;
	private Double salvag = 0.0;
	private Double weight = 0.0;
	private Double quantityPac = 0.0;
	private Double netLiability;
	private Double auditedLiability = 0.0;
	private String destrectionStatus;
	private String status;

}
