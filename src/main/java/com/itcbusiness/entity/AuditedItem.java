package com.itcbusiness.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.annotation.Nonnull;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class AuditedItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String wdCode;
	private String time;
	private LocalDate currentdate;
	private double longitude;
	private double latitude;
	private String liabilityCode;
	private double auditedQuantity;
	private int alert;
	private String alertSpecification;
	private String destrectionStatus;
	@ElementCollection
	private Map<String, List<String>> mCodesAuditedValue;
	private String audiTranscationId;
	private String itemDetailsCode;
	private String adminStatus;
	private String adminResion;
	private int timeDuration;
	private String auditedAddress;
	@Nonnull
	private String code;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime modifyAt;

}
