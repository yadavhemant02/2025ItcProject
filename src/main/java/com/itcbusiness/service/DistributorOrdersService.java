package com.itcbusiness.service;

import java.util.HashMap;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.DistributorOrders;
import com.itcbusiness.exception.MyDataRetriveException;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.DistributorOrderModel;
import com.itcbusiness.repository.DistributorOrdersRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DistributorOrdersService {

	private DistributorOrdersRepository distributorOrdersRepo;
	private MapSturtMapper mapper;

	public DistributorOrdersService(DistributorOrdersRepository distributorOrdersRepo, MapSturtMapper mapper) {
		super();
		this.distributorOrdersRepo = distributorOrdersRepo;
		this.mapper = mapper;
	}

	@Transactional
	public String addOrderOdDistributors(DistributorOrderModel distributorOrderData) {
		try {
			DistributorOrders entityData = this.mapper.distributorsOrderModelToDistributorsorder(distributorOrderData);
			entityData.setAuditorCode(null);
			entityData.setAuditorName(null);
			entityData.setLiability(new HashMap<>());
			entityData = this.distributorOrdersRepo.saveAndFlush(entityData);
			if (entityData == null) {
				throw new MyDataRetriveException("Error | ");
			}
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
