package com.itcbusiness.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LiabilityData")
public class LiabilityData {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String branch;
	private String wdCode;
	private String wdName;
	private String town;
	private String month;
	private String quater;
	private String years;
	private Double liability;
	private String liabilityCode;
	private List<String> auditor;
	private LocalDate auditedDate;
	private String category;
	private String approval;
	private String destructionStatus;
	private Double approvalLiability = 0.0;
	private Double destructionLiability = 0.0;
	private Double totalWeight = 0.0;
	private Double salvageValue = 0.0;
	private Double salvageValue1 = 0.0;
	private String invoiceNo;
	private String poNumber;
	private String voucherNo;
	private Double invoiceAmount = 0.0;
	private String status;
	private String payments;
	private LocalDate paymentDate;
	private String itemDetailCode;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@CreationTimestamp
	private LocalDate todayDate;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime modifyAt;

}
