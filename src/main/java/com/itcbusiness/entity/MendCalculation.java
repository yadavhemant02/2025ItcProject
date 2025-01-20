package com.itcbusiness.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MendCalculation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Double provision = 0.0;
	private Double lastMonth = 0.0;
	private Double average = 0.0;
	private Double differnce = 0.0;
	private Double perDifference = 0.0;
	private String codeId;
	private String glCode;

}
