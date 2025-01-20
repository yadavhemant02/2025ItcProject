package com.itcbusiness.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Setter
@Getter
public class DistributorsItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String branch;
	private String wdCode;
	private String wdName;
	private String town;
	private String contactNumber1;
	private String contactName1;
	private String contactNumber2;
	private String contactName2;
	private String address;
	private double longitude;
	private double latitude;
	private Double liability = 0.0;
	private Double approvalLiability = 0.0;
	private Double destructionLiability = 0.0;
	private Double surrenderValue = 0.0;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime modifyAt;

}
