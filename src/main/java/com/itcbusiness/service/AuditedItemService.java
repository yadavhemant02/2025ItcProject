package com.itcbusiness.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.AuditedItem;
import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.AuditedItemModel;
import com.itcbusiness.repository.AuditedImageRepository;
import com.itcbusiness.repository.AuditedItemRepository;
import com.itcbusiness.repository.DistributorsItemDetailsRepository;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.repository.PlanningDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditedItemService {

	private AuditedItemRepository auditedItemRepo;
	private MapSturtMapper mapSturtMapper;
	private final LiabilityDataRepository liabilityDataRepo;
	private PlanningDataRepository planningDataRepo;
	private final DistributorsItemDetailsRepository distributorsItemDetailsRepository;
	private DistributorsItemRepository distributorsItemRepository;
	private AuditedImageRepository auditedImageRepository;

	public AuditedItemService(AuditedItemRepository auditedItemRepo, MapSturtMapper mapSturtMapper,
			LiabilityDataRepository liabilityDataRepo, PlanningDataRepository planningDataRepo,
			DistributorsItemDetailsRepository distributorsItemDetailsRepository,
			DistributorsItemRepository distributorsItemRepository, AuditedImageRepository auditedImageRepository) {
		super();
		this.auditedItemRepo = auditedItemRepo;
		this.mapSturtMapper = mapSturtMapper;
		this.liabilityDataRepo = liabilityDataRepo;
		this.planningDataRepo = planningDataRepo;
		this.distributorsItemDetailsRepository = distributorsItemDetailsRepository;
		this.distributorsItemRepository = distributorsItemRepository;
		this.auditedImageRepository = auditedImageRepository;
	}

//	@Transactional(rollbackOn = RuntimeException.class)
//	public String auditedItemSumbition(AuditedItemModel auditedItemData) {
//		try {
//			AuditedItem auditedItem = this.mapSturtMapper.auditedItemModelToAuditedItem(auditedItemData);
//			auditedItem.setTime(LocalDateTime.now().toString().substring(11, 19));
//			auditedItem.setCurrentdate(LocalDate.now());
//			auditedItem.setAudiTranscationId(LocalDateTime.now().toString().substring(0, 4)
//					+ LocalDateTime.now().toString().substring(5, 7) + LocalDateTime.now().toString().substring(8, 13)
//					+ LocalDateTime.now().toString().substring(14, 16)
//					+ LocalDateTime.now().toString().substring(17, 19));
//
//			for (Map.Entry<String, List<String>> item : auditedItemData.getMCodesAuditedValue().entrySet()) {
//				Optional<DistributorsItemDetails> details = this.distributorsItemDetailsRepository
//						.findByMaterialCodeAndItemDetailCode(item.getKey(), item.getValue().get(1));
//				if (details.isPresent()) {
//					DistributorsItemDetails data = details.get();
//					data.setAuditedLiability(parseTotalLiability(item.getValue().get(0)));
//					data.setDestrectionStatus(
//							parseTotalLiability(item.getValue().get(0)) == data.getLiability() ? "COMPLETED"
//									: "AUDITED");
//					this.distributorsItemDetailsRepository.save(data);
//				}
//			}
////			auditedItem.setMCodesAuditedValue(auditedItemData.getMCodesAuditedValue());
//			this.auditedItemRepo.saveAndFlush(auditedItem);
//			try {
//				this.liabilityDataRepo.updateQuntityAndDestractionStatus(auditedItemData.getAuditedQuantity(), "DONE",
//						auditedItemData.getLiabilityCode());
//				this.planningDataRepo.updateQuantity(auditedItemData.getAuditedQuantity(),
//						auditedItemData.getLiabilityCode());
//			} catch (Exception e) {
//				log.error("unable to update liability Data");
//				// throw new IllegalArgumentException("Error unable to update liabilityData and
//				// Planning Data");
//			}
//			return "success";
//		} catch (Exception e) {
//			e.getStackTrace();
//			log.error(LogContant.logserviceerroradd + e.getMessage());
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
//		}
//	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String auditedItemSumbition(AuditedItemModel auditedItemData) {
		try {
			System.out.print("1111111111");
			AuditedItem auditedItem = this.mapSturtMapper.auditedItemModelToAuditedItem(auditedItemData);
			List<AuditedItem> aa = this.auditedItemRepo.findByItemDetailsCode(auditedItemData.getItemDetailsCode());
			if (aa.size() == 0) {
				auditedItem.setCode("1");
			} else {
				auditedItem.setCode(aa.get(aa.size() - 1).getCode());
			}
			System.out.print("22222222222");
			System.out.print(auditedItem);
			auditedItem.setTime(LocalDateTime.now().toString().substring(11, 19));
			auditedItem.setCurrentdate(LocalDate.now());
			auditedItem.setAudiTranscationId(LocalDateTime.now().toString().substring(0, 4)
					+ LocalDateTime.now().toString().substring(5, 7) + LocalDateTime.now().toString().substring(8, 13)
					+ LocalDateTime.now().toString().substring(14, 16)
					+ LocalDateTime.now().toString().substring(17, 19));
			Map<String, List<String>> mCodesAuditedValue = auditedItemData.getMCodesAuditedValue();
			System.out.print(mCodesAuditedValue);
			if (mCodesAuditedValue != null) {
				for (Map.Entry<String, List<String>> item : mCodesAuditedValue.entrySet()) {
//					List<DistributorsItemDetails> dd = this.distributorsItemDetailsRepository
//							.findByMaterialCodeAndItemDetailCodee(item.getKey(), item.getValue().get(1));

					Optional<DistributorsItemDetails> details = this.distributorsItemDetailsRepository
							.findByMaterialCodeAndItemDetailCode(item.getKey(), item.getValue().get(1));
					if (details.isPresent() && checker(details.get().getDestrectionStatus())) {
						DistributorsItemDetails data = details.get();
						if (!item.getValue().get(2).isBlank() && item.getValue().get(2).equals("quantity")) {
							Double value = ((data.getLiability()) / (data.getWeight()))
									* parseTotalLiability(item.getValue().get(0));
							data.setAuditedLiability(value);
							// data.setQuantity(data.getQuantity() -
							// parseTotalLiability(item.getValue().get(0)));
							if (parseTotalLiability(item.getValue().get(0)) == (data.getWeight()
									- parseTotalLiability(item.getValue().get(0)))) {
								data.setDestrectionStatus("COMPLETED");
							} else {
								data.setDestrectionStatus("AUDITED");
							}
							this.distributorsItemDetailsRepository.save(data);
						} else {
							if (details.get().getAuditedLiability() == null) {
								data.setAuditedLiability(parseTotalLiability(item.getValue().get(0)));
							} else {
								data.setAuditedLiability(details.get().getAuditedLiability()
										+ parseTotalLiability(item.getValue().get(0)));
							}
							data.setDestrectionStatus(
									data.getAuditedLiability().equals(data.getLiability()) ? "COMPLETED" : "AUDITED");

							this.distributorsItemDetailsRepository.save(data);
						}
					} else {
						auditedItemData.setAuditedQuantity(
								auditedItemData.getAuditedQuantity() - parseTotalLiability(item.getValue().get(0)));
					}
				}
			} else {
				log.warn("MCodesAuditedValue is null. Skipping the processing of entries.");
			}
			this.auditedItemRepo.saveAndFlush(auditedItem);
			try {
				LiabilityData liabilityData = this.liabilityDataRepo
						.findByLiabilityCode(auditedItemData.getLiabilityCode());

				liabilityData.setDestructionStatus(auditedItemData.getDestrectionStatus());
				// liabilityData.setLiability(liabilityData.getLiability() -
				// auditedItemData.getAuditedQuantity());
				liabilityData.setDestructionLiability(
						liabilityData.getDestructionLiability() + auditedItemData.getAuditedQuantity());
				this.liabilityDataRepo.save(liabilityData);
				DistributorsItem distributorsItem = this.distributorsItemRepository
						.findByWdCode(auditedItemData.getWdCode());
				// distributorsItem.setLiability(distributorsItem.getLiability() -
				// auditedItemData.getAuditedQuantity());
//				distributorsItem.setApprovalLiability(
//						distributorsItem.getApprovalLiability() - auditedItemData.getAuditedQuantity());
				if (distributorsItem.getDestructionLiability() == null) {
					distributorsItem.setDestructionLiability(auditedItemData.getAuditedQuantity());
				} else {
					distributorsItem.setDestructionLiability(
							distributorsItem.getDestructionLiability() + auditedItemData.getAuditedQuantity());
				}
				this.distributorsItemRepository.save(distributorsItem);
				if (auditedItemData.getDestrectionStatus().equals("DONE")) {
					DistributorsItem distributorsItemData = this.distributorsItemRepository
							.findByWdCode(auditedItemData.getWdCode());
					distributorsItemData.setSurrenderValue(distributorsItemData.getSurrenderValue()
							+ (liabilityData.getLiability() - liabilityData.getDestructionLiability()));
					this.distributorsItemRepository.save(distributorsItemData);
				}
			} catch (Exception e) {
				log.error("Unable to update liability Data: " + e.getMessage());
			}
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	private double parseTotalLiability(String totalLiability) {
		if (totalLiability == null || "N/A".equals(totalLiability)) {
			log.warn("Received invalid total liability value: {}", totalLiability);
			return 0.0;
		}
		try {
			return Double.parseDouble(totalLiability);
		} catch (NumberFormatException e) {
			log.warn("Invalid total liability value: {}", totalLiability, e);
			return 0.0;
		}
	}

	private boolean checker(String str) {
		if (str == null) {
			return true;
		}
		if (str.equals("AUDITED")) {
			return true;
		} else {
			return false;
		}
	}

	public List<AuditedItem> getAuditedItemsByLiabilityData(String liabilityCode) {
		try {
			return this.auditedItemRepo.findByLiabilityCode(liabilityCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public List<AuditedItem> getAuditedItemsByWdCode(String wdCode) {
		try {
			return this.auditedItemRepo.findByWdCode(wdCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public List<AuditedItem> getAuditedItemsByItemDetailsCode(String itemDetailsCode) {
		try {
			return this.auditedItemRepo.findByItemDetailsCode(itemDetailsCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String statusOfAdminForAuditedItem(String itemDetailsCode, String status, String code, String adminResion) {
		try {
			List<AuditedItem> data = this.auditedItemRepo.findByItemDetailsCodeAndCode(itemDetailsCode, code);
			if (status.equals("success")) {
				List<AuditedItem> data1 = data.stream().map((item) -> {
					item.setAdminStatus("VARIFIED");
					item.setAdminResion("SUCCESS");
					return item;
				}).collect(Collectors.toList());
				this.auditedItemRepo.saveAll(data1);
			} else {
				List<AuditedItem> data1 = data.stream().map((item) -> {
					item.setAdminStatus("CANCEL");
					item.setAdminResion(adminResion);
					return item;
				}).collect(Collectors.toList());
				this.auditedItemRepo.saveAll(data1);

				LiabilityData liabilitydata = this.liabilityDataRepo.findByItemDetailCode(itemDetailsCode);
				DistributorsItem distributorsItem = this.distributorsItemRepository
						.findByWdCode(liabilitydata.getWdCode());
				distributorsItem.setDestructionLiability(
						distributorsItem.getDestructionLiability() - liabilitydata.getDestructionLiability());
				liabilitydata.setDestructionLiability(0.0);
				liabilitydata.setDestructionStatus("CANCEL");
				this.liabilityDataRepo.save(liabilitydata);
				this.distributorsItemRepository.save(distributorsItem);
			}

			this.auditedImageRepository.deleteByItemDetailsCode(itemDetailsCode);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
