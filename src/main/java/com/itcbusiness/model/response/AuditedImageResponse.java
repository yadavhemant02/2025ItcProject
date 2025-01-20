package com.itcbusiness.model.response;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AuditedImageResponse {

	private String auditorImge;
	private List<String> itemImage;
	private String itemDetailsCode;

}
