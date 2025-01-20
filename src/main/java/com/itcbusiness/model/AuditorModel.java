package com.itcbusiness.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AuditorModel {

	private String auditorName;
	private String itemDetailCode;
	private LocalDate auditedDate;
}
