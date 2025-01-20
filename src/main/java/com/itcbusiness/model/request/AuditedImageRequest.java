package com.itcbusiness.model.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AuditedImageRequest {

	private MultipartFile auditorImge;
	private List<MultipartFile> itemImage;
	private String itemDetailsCode;

}
