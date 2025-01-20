package com.itcbusiness.service;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.InvoiceData;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.model.InvoiceDataModel;
import com.itcbusiness.repository.InvoiceDataRepository;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvoiceDataService {

	private InvoiceDataRepository invoiceDataRepository;
	private LiabilityDataRepository liabilityDataRepository;

	public InvoiceDataService(InvoiceDataRepository invoiceDataRepository,
			LiabilityDataRepository liabilityDataRepository) {
		super();
		this.invoiceDataRepository = invoiceDataRepository;
		this.liabilityDataRepository = liabilityDataRepository;
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String updateInvoiceData(InvoiceDataModel invoiceData) {
		try {
			InvoiceData data = new InvoiceData();
			data.setInvoiceImage(invoiceData.getInvoiceImage().getBytes());
			data.setInvoiceNo(invoiceData.getInvoiceNo());
			data.setItemDetailsCode(invoiceData.getItemDetailsCode());

			LiabilityData libilityData = this.liabilityDataRepository
					.findByItemDetailCode(invoiceData.getItemDetailsCode());
			libilityData.setInvoiceNo(invoiceData.getInvoiceNo());
			libilityData = this.liabilityDataRepository.save(libilityData);
			if (libilityData == null) {
				throw new IllegalArgumentException("Error|");
			}
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
