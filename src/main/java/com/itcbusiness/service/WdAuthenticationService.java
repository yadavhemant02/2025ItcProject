package com.itcbusiness.service;

import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.WdAuthentication;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.repository.WdAuthenticationRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WdAuthenticationService {

	private WdAuthenticationRepository wdAuthenticationRepository;
	private MapSturtMapper mapper;

	public WdAuthenticationService(WdAuthenticationRepository wdAuthenticationRepository, MapSturtMapper mapper) {
		super();
		this.wdAuthenticationRepository = wdAuthenticationRepository;
		this.mapper = mapper;
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String addApikeyOfWd(String wdCode, String apiKey) {
		try {
			WdAuthentication data = this.wdAuthenticationRepository.findByWdCode(wdCode);
			if (data == null) {
				data = this.mapper.wdCodeAndApiKeytoWdAuthentiication(wdCode, apiKey);
				data = this.wdAuthenticationRepository.save(data);
				if (data == null) {
					throw new IllegalArgumentException("ERROR | unable to store data of wd apiKey ");
				}
			} else {
				data.setApiKey(apiKey);
				data.setStatus("ACTIVE");
				data = this.wdAuthenticationRepository.save(data);
				if (data == null) {
					throw new IllegalArgumentException("ERROR | unable to update data of wd apiKey ");
				}
			}
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public WdAuthentication getapiOfWd(String wdCode) {
		try {
			WdAuthentication data = this.wdAuthenticationRepository.findByWdCode(wdCode);
			if (data == null) {
				throw new IllegalArgumentException("ERROR | unable to find data");
			}
			return data;
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public List<WdAuthentication> getAllapiKeyOfWd() {
		try {
			return this.wdAuthenticationRepository.findAll();
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public WdAuthentication updateStatus(String wdCode) {
		try {
			WdAuthentication data = this.wdAuthenticationRepository.findByWdCode(wdCode);

			if (data == null) {
				throw new IllegalArgumentException("ERROR | unable to find data");
			}
			data.setStatus("DISABLE");
			return this.wdAuthenticationRepository.save(data);
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}
}
