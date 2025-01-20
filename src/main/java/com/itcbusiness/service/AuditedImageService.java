package com.itcbusiness.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itcbusiness.entity.AuditedImage;
import com.itcbusiness.model.request.AuditedImageRequest;
import com.itcbusiness.model.response.AuditedImageResponse;
import com.itcbusiness.repository.AuditedImageRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditedImageService {

	private AuditedImageRepository auditedImageRepository;

	public AuditedImageService(AuditedImageRepository auditedImageRepository) {
		super();
		this.auditedImageRepository = auditedImageRepository;
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public Object addImagesForAudited(AuditedImageRequest imageData) {
		try {
			AuditedImage entityData = new AuditedImage();
			entityData.setAuditorImge(imageData.getAuditorImge().getBytes());
			List<byte[]> itemImages = new ArrayList<>();
			for (MultipartFile item : imageData.getItemImage()) {
				itemImages.add(item.getBytes());
			}
			entityData.setItemImage(itemImages);
			entityData.setItemDetailsCode(imageData.getItemDetailsCode());
			this.auditedImageRepository.save(entityData);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage(), e);
		}
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public List<AuditedImageResponse> getImagesForAudited(String itemDetailsCode) {
		try {
			List<AuditedImage> data = this.auditedImageRepository.findByItemDetailsCode(itemDetailsCode);

			List<AuditedImageResponse> responseData = new ArrayList<>();
			for (AuditedImage item : data) {
				AuditedImageResponse data1 = new AuditedImageResponse();
				data1.setAuditorImge(Base64.getEncoder().encodeToString(item.getAuditorImge()));
				List<String> itemImages = new ArrayList<>();
				for (byte[] i : item.getItemImage()) {
					itemImages.add(Base64.getEncoder().encodeToString(i));
				}
				data1.setItemImage(itemImages);
				data1.setItemDetailsCode(itemDetailsCode);
				responseData.add(data1);
			}
			return responseData;

		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage(), e);
		}
	}

}
