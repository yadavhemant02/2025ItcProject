package com.itcbusiness.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.WdInvoice;
import com.itcbusiness.entity.WdInvoiceImage;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.request.WdInvoiceRequest;
import com.itcbusiness.repository.WdInvoiceImageRepository;
import com.itcbusiness.repository.WdInvoiceRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WdInvoiceService {

	private WdInvoiceRepository wdInvoiceRepository;
	private MapSturtMapper mapper;
	private WdInvoiceImageRepository wdInvoiceImageRepository;

	public WdInvoiceService(WdInvoiceRepository wdInvoiceRepository, MapSturtMapper mapper,
			WdInvoiceImageRepository wdInvoiceImageRepository) {
		super();
		this.wdInvoiceRepository = wdInvoiceRepository;
		this.mapper = mapper;
		this.wdInvoiceImageRepository = wdInvoiceImageRepository;
	}

	public String addwdInvoice(WdInvoiceRequest invoiceData) {
		try {
			String invoiceId = LocalDateTime.now().toString().substring(0, 4)
					+ LocalDateTime.now().toString().substring(5, 7) + LocalDateTime.now().toString().substring(8, 13)
					+ LocalDateTime.now().toString().substring(14, 16)
					+ LocalDateTime.now().toString().substring(17, 19);
			WdInvoice entityData = this.mapper.wdInvoiceDataToWdInvoice(invoiceData);
			entityData.setWdInvoiceId(invoiceId);
			entityData = this.wdInvoiceRepository.save(entityData);
			WdInvoiceImage data = new WdInvoiceImage();
			data.setInvoiceFile(invoiceData.getInvoiceFile().getBytes());
			data.setWdInvoiceId(invoiceId);
			data.setContentType(invoiceData.getInvoiceFile().getContentType());
			data = this.wdInvoiceImageRepository.save(data);
			if (data == null) {
				throw new IllegalArgumentException("error| ");
			}
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public WdInvoiceImage getInvoiceImage(String invoiceId) {
		try {
			WdInvoiceImage data = this.wdInvoiceImageRepository.findByWdInvoiceId(invoiceId);
			return data;
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public List<WdInvoice> getAllInvoice() {
		try {
			return this.wdInvoiceRepository.findAll();
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public List<WdInvoice> getAllOneWdInvoice(String wdCode) {
		try {
			return this.wdInvoiceRepository.findByWdCode(wdCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

}
