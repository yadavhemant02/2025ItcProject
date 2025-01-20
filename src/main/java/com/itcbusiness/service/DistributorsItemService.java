package com.itcbusiness.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.DistributorsItemModel;
import com.itcbusiness.repository.AuditedItemRepository;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class DistributorsItemService {

	private DistributorsItemRepository distributorsItemRepo;
	private MapSturtMapper mapSturtMapper;
	private AuditedItemRepository auditedItemRepository;

	public DistributorsItemService(DistributorsItemRepository distributorsItemRepo, MapSturtMapper mapSturtMapper,
			AuditedItemRepository auditedItemRepository) {
		super();
		this.distributorsItemRepo = distributorsItemRepo;
		this.mapSturtMapper = mapSturtMapper;
		this.auditedItemRepository = auditedItemRepository;
	}

	public String addDistributers(List<DistributorsItemModel> distributorData) {
		try {
			distributorData.stream().forEach((item) -> {
				if (!"".equals(item.getWdCode()) || item.getWdCode() != null) {
					DistributorsItem checkerData = this.distributorsItemRepo.findByWdCode(item.getWdCode());
					if (checkerData == null) {
						DistributorsItem entityData = this.mapSturtMapper.distributorsItemModelToDistributorItem(item);
						this.distributorsItemRepo.save(entityData);
					}
				}
			});
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public Flux<DistributorsItem> getAllDistributers() {
		try {
			return Flux.fromIterable(this.distributorsItemRepo.findAll());
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public DistributorsItem getOneDistributorsItem(String wdCode) {
		try {
			return this.distributorsItemRepo.findByWdCode(wdCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public Flux<DistributorsItem> getallWdWhichAreAuditedAtLestOneTime(LocalDate startDate, LocalDate endDate,
			String type) {
		try {
			List<DistributorsItem> data = this.distributorsItemRepo.findAll();
			if ("ALL".equals(type)) {
				Set<String> wdSet = this.auditedItemRepository.findAllDistinctWdCodes();
				data = data.stream().filter((item) -> wdSet.contains(item.getWdCode())).toList();
				return Flux.fromIterable(data);
			} else {
				LocalDateTime startDateTime = startDate.atStartOfDay();
				LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
				Set<String> wdSet = this.auditedItemRepository.findWdCodesBetweenDates(startDateTime, endDateTime);
				data = data.stream().filter((item) -> wdSet.contains(item.getWdCode())).toList();
				return Flux.fromIterable(data);
			}
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String updateMasterWDData(DistributorsItemModel distributorsItemData) {
		try {
			DistributorsItem entityData = this.mapSturtMapper
					.distributorsItemModelToDistributorItem(distributorsItemData);
			DistributorsItem data = this.distributorsItemRepo.findByWdCode(distributorsItemData.getWdCode());
			entityData.setId(data.getId());
			this.distributorsItemRepo.save(entityData);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
