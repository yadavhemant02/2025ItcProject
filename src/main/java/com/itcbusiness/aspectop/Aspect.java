package com.itcbusiness.aspectop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.After;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.itcbusiness.entity.CategoryDivision;
import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.DistributorsItemDetailsModel;
import com.itcbusiness.model.LiabilityDataModel;
import com.itcbusiness.repository.CategoryDivisionRepository;
import com.itcbusiness.repository.DistributorsItemDetailsRepository;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itcbusiness.repository.InventoryRepository;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@ComponentScan
@org.aspectj.lang.annotation.Aspect
@Component
@Slf4j
public class Aspect {

	private DistributorsItemRepository distributorsItemRepo;
	private MapSturtMapper mapSturtMapper;
	private CategoryDivisionRepository categoryDivisionRepository;
	private InventoryRepository inventoryRepository;
	private LiabilityDataRepository liabilityDataRepository;
	private DistributorsItemDetailsRepository distributorsItemDetailsRepository;
	public static List<LiabilityDataModel> liabilityDataModel = Collections.synchronizedList(new ArrayList<>());
	public static Map<String, HashSet<String>> categoryDivision = new HashMap<>();
	public static List<DistributorsItemDetailsModel> distributorItemDetails = Collections
			.synchronizedList(new ArrayList<>());

	public Aspect(DistributorsItemRepository distributorsItemRepo, MapSturtMapper mapSturtMapper,
			CategoryDivisionRepository categoryDivisionRepository, LiabilityDataRepository liabilityDataRepository,
			InventoryRepository inventoryRepository,
			DistributorsItemDetailsRepository distributorsItemDetailsRepository) {
		super();
		this.distributorsItemRepo = distributorsItemRepo;
		this.mapSturtMapper = mapSturtMapper;
		this.categoryDivisionRepository = categoryDivisionRepository;
		this.liabilityDataRepository = liabilityDataRepository;
		this.inventoryRepository = inventoryRepository;
	}

//	@After("execution(* com.itcbusiness.service.LiabilityDataService.saveLiabilityData(..))")
//	public void beforeSaveLiabilityData() {
//		HashMap<String, DistributorsItem> mapData = new HashMap<>();
//		try {
//			liabilityDataModel.stream().forEach((item) -> {
//				DistributorsItem distributorItemData = this.distributorsItemRepo.findByWdCode(item.getWdCode());
//				if (distributorItemData == null) {
//					if (mapData.containsKey(item.getWdCode())) {
//						DistributorsItem data = mapData.get(item.getWdCode());
//						if ("YES".equals(item.getApproval())) {
//							double newDoubleValue = 0.0;
//							if (item.getLiability() != null && !item.getLiability().equals("N/A")) {
//								newDoubleValue = parseTotalLiability(item.getLiability());
//								data.setApprovalLiability(data.getApprovalLiability() + newDoubleValue);
//								if ("DONE".equals(item.getDestructionStatus())) {
//									data.setDestructionLiability(data.getDestructionLiability() + newDoubleValue);
//								}
//								data.setLiability(data.getLiability() + newDoubleValue);
//							}
//						}
//						mapData.put(item.getWdCode(), data);
//					} else {
//						DistributorsItem data = this.mapSturtMapper.liabilityDataModelToDistributorsItem(item);
//						double newDoubleValue = 0.0;
//						data.setApprovalLiability(newDoubleValue);
//						data.setDestructionLiability(newDoubleValue);
//						data.setLiability(newDoubleValue);
//						if (item.getLiability() != null && !item.getLiability().equals("N/A")) {
//							newDoubleValue = parseTotalLiability(item.getLiability());
//							if ("YES".equals(item.getApproval())) {
//								data.setApprovalLiability(newDoubleValue);
//								if ("DONE".equals(item.getDestructionStatus())) {
//									data.setDestructionLiability(newDoubleValue);
//								}
//							}
//							data.setLiability(newDoubleValue);
//						}
//						mapData.put(item.getWdCode(), data);
//					}
//				} else {
//					distributorItemData.setLiability(parseTotalLiability(item.getLiability()));
//					if (item.getApproval().equals("YES")) {
//						distributorItemData.setApprovalLiability(parseTotalLiability(item.getLiability()));
//					}
//					if (item.getDestructionStatus().equals("DONE")) {
//						distributorItemData.setDestructionLiability(parseTotalLiability(item.getLiability()));
//					}
//					mapData.put(item.getWdCode(), distributorItemData);
//				}
//			});
//			List<DistributorsItem> entityData = new ArrayList<>(mapData.values());
//			log.info(LogContant.logcontrollersuccessadd);
//			this.distributorsItemRepo.saveAllAndFlush(entityData);
//		} catch (Exception e) {
//			e.getStackTrace();
//			log.error(LogContant.logcontrollererroradd + e.getMessage());
//		}
//	}

//	private double parseTotalLiability(Double totalLiability) {
//		if (totalLiability == null || "N/A".equals(totalLiability)) {
//			log.warn("Received invalid total liability value: {}", totalLiability);
//			return 0.0;
//		}
//		try {
//			return totalLiability;
//		} catch (NumberFormatException e) {
//			log.warn("Invalid total liability value: {}", totalLiability, e);
//			return 0.0;
//		}
//	}

//	private boolean isNumeric(Double str) {
//		if (str == null || str.isEmpty()) {
//			return false;
//		}
//		try {
//			Double.parseDouble(str);
//			return true;
//		} catch (NumberFormatException e) {
//			return false;
//		}
//	}

	@After("execution(* com.itcbusiness.service.LiabilityDataService.saveLiabilityData(..))")
	public void beforeSaveLiabilityData() {
		System.out.print("kk" + liabilityDataModel.size() + "kk");
		HashMap<String, DistributorsItem> mapData = new HashMap<>();
		try {
			liabilityDataModel.stream().forEach((item) -> {
				if (mapData.containsKey(item.getWdCode())) {
					DistributorsItem data = mapData.get(item.getWdCode());
					data.setLiability(data.getLiability() + (item.getLiability() == null ? 0.0 : item.getLiability()));
					data.setApprovalLiability(item.getApproval().toUpperCase().contains("YES")
							|| item.getApproval().toUpperCase().contains("APPROVED")
									? data.getApprovalLiability()
											+ (item.getLiability() == null ? 0.0 : item.getLiability())
									: data.getApprovalLiability());
					data.setDestructionLiability(item.getDestructionStatus().toUpperCase().contains("DONE")
							? data.getDestructionLiability() + (item.getLiability() == null ? 0.0 : item.getLiability())
							: data.getDestructionLiability());
					mapData.put(item.getWdCode(), data);
				} else {
					DistributorsItem data = new DistributorsItem();
					data.setLiability(item.getLiability() == null ? 0.0 : item.getLiability());
					data.setApprovalLiability(item.getApproval().toUpperCase().contains("YES")
							|| item.getApproval().toUpperCase().contains("APPROVED") ? item.getLiability() : 0.0);
					data.setDestructionLiability(
							item.getDestructionStatus().toUpperCase().contains("DONE") ? item.getLiability() : 0.0);
					mapData.put(item.getWdCode(), data);

				}
			});
			mapData.entrySet().stream().forEach((item) -> {
				DistributorsItem data = this.distributorsItemRepo.findByWdCode(item.getKey());
				if (data != null) {
					this.distributorsItemRepo.updateApprovalLiabilityAndAll(item.getKey(),
							item.getValue().getLiability() + (data.getLiability() == null ? 0.0 : data.getLiability()),
							item.getValue().getApprovalLiability()
									+ (data.getApprovalLiability() == null ? 0.0 : data.getApprovalLiability()),
							item.getValue().getDestructionLiability()
									+ (data.getDestructionLiability() == null ? 0.0 : data.getDestructionLiability()));
				}
			});
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd + e.getMessage());
		}
	}

	@After("execution(* com.itcbusiness.service.InventoryService.saveInventoryData(..))")
	public void addCategoryDivision() {
		try {
			for (Map.Entry<String, HashSet<String>> entry : categoryDivision.entrySet()) {
				entry.getValue().stream().forEach((item) -> {
					Optional<CategoryDivision> data = this.categoryDivisionRepository.findByDivision(item);
					if (data.isEmpty()) {
						if (entry.getKey().equals("Food") || entry.getKey().equals("FOOD")) {
							this.categoryDivisionRepository
									.save(this.mapSturtMapper.valuesToCategoryDivision("FBD", item));
						} else {
							this.categoryDivisionRepository
									.save(this.mapSturtMapper.valuesToCategoryDivision(entry.getKey(), item));
						}
					}
				});
			}
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd + e.getMessage());
		}
	}

	private static int a = 0;

	@After("execution(* com.itcbusiness.service.DistributorsItemDetailsService.addDistributorsItemDetails(..))")
	public void saveLiabilityData() {

		try {
			System.out.print(distributorItemDetails.size());
			HashMap<String, LiabilityData> liabilityMap = new HashMap<>();
			HashMap<String, Double> map = new HashMap<>();
			distributorItemDetails.stream().forEach((item -> {
				if (item.getCategory().equals("FOOD") && item.getDivision().equals("AT")) {
					item.setLiability(item.getLiability() - (item.getSalvageValue() * item.getPerItemSalvage()));
				} else if (item.getCategory().equals("FOOD") && item.getDivision().equals("BC")) {
					item.setLiability(item.getLiability() - (item.getSalvageValue1() * item.getPerItemSalvage()));
				}
				if (map.containsKey(item.getWdCode())) {
					map.put(item.getWdCode(), map.get(item.getWdCode()) + item.getLiability());
				} else {
					map.put(item.getWdCode(), item.getLiability());

				}
				// executorService.submit(() -> {
//				Optional<CategoryDivision> categotyData = this.categoryDivisionRepository
//						.findByDivision(item.getDivision());
//				String category = "FBD";
//				if (!categotyData.isEmpty()) {
//					category = categotyData.get().getCategory();
//				}
//				LiabilityData liabilityData = this.liabilityDataRepository
//						.findByWdCodeAndMonthAndCategory(item.getWdCode(), item.getMonth(), item.getCategory());
				if (!liabilityMap
						.containsKey(item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""))) {
					LiabilityData newLiabilityData = this.mapSturtMapper
							.distributorsItemDetailsModelToLiabilityData(item);
					newLiabilityData
							.setYears(item.getMonth().split(" ").length == 2 ? item.getMonth().split(" ")[1] : "N/A");
					// newLiabilityData.setCategory(category);
					newLiabilityData.setLiabilityCode(UUID.randomUUID().toString().replace("-", ""));
					newLiabilityData.setItemDetailCode(
							item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""));
					newLiabilityData.setQuater(generateOuater(item.getMonth()));
					newLiabilityData.setTotalWeight(item.getPerItemSalvage());
					liabilityMap.put(item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""),
							newLiabilityData);
//					Optional.ofNullable(this.liabilityDataRepository.save(newLiabilityData))
//							.orElseThrow(() -> new IllegalArgumentException("Error | unable store libility data"));
				} else {
					LiabilityData liabilityData = liabilityMap
							.get(item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""));
					liabilityData.setLiability(liabilityData.getLiability() + item.getLiability());
					liabilityData.setTotalWeight(item.getPerItemSalvage() + liabilityData.getTotalWeight());
					liabilityMap.put(item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""),
							liabilityData);

//					Optional.ofNullable(this.liabilityDataRepository.save(liabilityData)).orElseThrow(
//							() -> new IllegalArgumentException("Error | unable store libility data update "));
				}
				// });
			}));
			// executorService.shutdown();

			liabilityMap.entrySet().stream().forEach((item) -> {
				try {
					LiabilityData liabilityData = this.liabilityDataRepository.findByWdCodeAndMonthAndCategory(
							item.getValue().getWdCode(), item.getValue().getMonth(), item.getValue().getCategory());
					if (liabilityData == null) {
						LiabilityData saveData = item.getValue();
						DistributorsItem data = this.distributorsItemRepo.findByWdCode(item.getValue().getWdCode());
						if (data != null) {
							saveData.setTown(data.getTown());
							saveData.setWdName(data.getWdName());
							this.liabilityDataRepository.save(saveData);
//							this.distributorsItemRepo.updateLiability(item.getValue().getWdCode(),
//									item.getValue().getLiability()
//											+ (data.getLiability() == null ? 0.0 : data.getLiability()));
							data.setLiability(data.getLiability() + item.getValue().getLiability());
							this.distributorsItemRepo.save(data);
						} else {
							DistributorsItem distributorsItem = new DistributorsItem();
							distributorsItem.setBranch(item.getValue().getBranch());
							distributorsItem.setTown(item.getValue().getTown());
							distributorsItem.setWdCode(item.getValue().getWdCode());
							distributorsItem.setWdName(item.getValue().getWdName());
							distributorsItem.setLiability(item.getValue().getLiability());
							this.liabilityDataRepository.save(saveData);
							this.distributorsItemRepo.save(distributorsItem);
						}
					}
				} catch (Exception e) {
					log.error("Error| unable to store distributer data");
				}
			});

		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd + e.getMessage());
		}
	}

	public String generateOuater(String month) {
		try {
			String[] str = month.split(" ");
			if (str[0].equals("April") || str[0].equals("May") || str[0].equals("June")) {
				return "Q1";
			} else {
				if (str[0].equals("July") || str[0].equals("August") || str[0].equals("September")) {
					return "Q2";
				} else {
					if (str[0].equals("October") || str[0].equals("November") || str[0].equals("December")) {
						return "Q3";
					} else {
						return "Q4";
					}
				}
			}
		} catch (Exception e) {
			return "N/A";
		}
	}

//	item.getValue().setQuantityPac(this.inventoryRepository
//			.findByMaterialCode(item.getValue().getMaterialCode()) != null
//					? (item.getValue().getWeight() / this.inventoryRepository
//							.findByMaterialCode(item.getValue().getMaterialCode()).getWeight())
//					: 0.0);

//	@After("execution(* com.itcbusiness.service.DistributorsItemDetailsService.addDistributorsItemDetails(..))")
//	public void calculateQiantityPac() {
//		try {
//			System.out.print("calculationkkkkkkkjjjjjjjjjjjj");
//			distributorItemDetails.stream().forEach((item) -> {
//
//				try {
//					Inventory inventoryData = this.inventoryRepository.findByMaterialCode(item.getMaterialCode());
//					if (inventoryData != null) {
//						String itemDetailsCode = item.getWdCode() + item.getCategory()
//								+ item.getMonth().replace(" ", "");
//						this.distributorsItemDetailsRepository.updateQuantityPac(item.getMaterialCode(),
//								itemDetailsCode, inventoryData.getWeight());
//					}
//				} catch (Exception e) {
//					System.out.println("1++");
//				}
//
//			});
//
//		} catch (Exception e) {
//			e.getStackTrace();
//			log.error(LogContant.logcontrollererroradd + e.getMessage());
//		}
//
//	}

	@After("execution(* com.itcbusiness.service.InventoryService.saveInventoryData(..))")
	public void setCalculationOfMEndDPart() {
		try {
			for (Map.Entry<String, HashSet<String>> entry : categoryDivision.entrySet()) {
				entry.getValue().stream().forEach((item) -> {
					Optional<CategoryDivision> data = this.categoryDivisionRepository.findByDivision(item);
					if (data.isEmpty()) {
						if (entry.getKey().equals("Food") || entry.getKey().equals("FOOD")) {
							this.categoryDivisionRepository
									.save(this.mapSturtMapper.valuesToCategoryDivision("FBD", item));
						} else {
							this.categoryDivisionRepository
									.save(this.mapSturtMapper.valuesToCategoryDivision(entry.getKey(), item));
						}
					}
				});
			}
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logcontrollererroradd + e.getMessage());
		}
	}

}
