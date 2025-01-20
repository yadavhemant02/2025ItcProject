package com.itcbusiness.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.aspectop.Aspect;
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.DistributorsItemDetailsModel;
import com.itcbusiness.repository.CategoryDivisionRepository;
import com.itcbusiness.repository.DistributorsItemDetailsRepository;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itcbusiness.repository.InventoryRepository;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class DistributorsItemDetailsService {

	private DistributorsItemDetailsRepository distributorsItemDetailsRepo;
	private LiabilityDataRepository liabilityDataRepo;
	private MapSturtMapper mapSturtMapper;
	private CategoryDivisionRepository categoryDivisionRepository;
	private DistributorsItemRepository distributorsItemRepo;
	private InventoryRepository inventoryRepository;
	private static boolean flag = false;

	public DistributorsItemDetailsService(DistributorsItemDetailsRepository distributorsItemDetailsRepo,
			LiabilityDataRepository liabilityDataRepo, MapSturtMapper mapSturtMapper,
			CategoryDivisionRepository categoryDivisionRepository, DistributorsItemRepository distributorsItemRepo,
			InventoryRepository inventoryRepository) {
		super();
		this.distributorsItemDetailsRepo = distributorsItemDetailsRepo;
		this.liabilityDataRepo = liabilityDataRepo;
		this.mapSturtMapper = mapSturtMapper;
		this.categoryDivisionRepository = categoryDivisionRepository;
		this.distributorsItemRepo = distributorsItemRepo;
		this.inventoryRepository = inventoryRepository;
	}

	@SuppressWarnings("unused")
	public String addDistributorsItemDetails(List<DistributorsItemDetailsModel> distributorsItemDetailsData) {
		int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		Aspect.distributorItemDetails.clear();
		try {
			HashMap<String, DistributorsItemDetails> map1 = new HashMap<>();
			distributorsItemDetailsData.stream().forEach((item -> {
				String mainKey = item.getMaterialCode() + item.getWdCode() + item.getCategory()
						+ item.getMonth().replace(" ", "");

				if (map1.containsKey(mainKey)) {
					DistributorsItemDetails ss = map1.get(mainKey);
					ss.setLiability(ss.getLiability() + item.getLiability());
					ss.setWeight(ss.getWeight() + item.getPerItemSalvage());

					if (item.getCategory().equals("FOOD") && item.getDivision().equals("AT")) {
						if (item.getDivision().equals("AT")) {
							ss.setNetLiability(ss.getNetLiability()
									+ (item.getLiability() - (item.getSalvageValue() * item.getPerItemSalvage())));
							ss.setSalvag(
									item.getDivision().equals("AT") ? (item.getSalvageValue() * ss.getWeight()) : 0.0);
						} else if (item.getDivision().equals("BC")) {
							ss.setNetLiability(ss.getNetLiability()
									+ (item.getLiability() - (item.getSalvageValue1() * item.getPerItemSalvage())));
							ss.setSalvag(
									item.getDivision().equals("BC") ? (item.getSalvageValue1() * ss.getWeight()) : 0.0);
						} else {
							ss.setNetLiability(ss.getNetLiability() + item.getLiability());
						}
					} else {
						ss.setNetLiability(ss.getNetLiability() + item.getLiability());
					}
					map1.put(mainKey, ss);
				} else {
					DistributorsItemDetails entityInfo = this.mapSturtMapper
							.DistributorsItemDetailsModelToDistributorsItemDetails(item);
					entityInfo.setItemDetailCode(
							item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""));
					entityInfo.setWeight(item.getPerItemSalvage());
					if (item.getCategory().equals("FOOD")) {
						if (item.getDivision().equals("AT")) {
							entityInfo.setNetLiability(
									item.getLiability() - (item.getSalvageValue() * item.getPerItemSalvage()));
							entityInfo.setSalvag(
									item.getDivision().equals("AT") ? (item.getSalvageValue() * entityInfo.getWeight())
											: 0.0);
						} else if (item.getDivision().equals("BC")) {
							entityInfo.setNetLiability(
									item.getLiability() - (item.getSalvageValue1() * item.getPerItemSalvage()));
							entityInfo.setSalvag(
									item.getDivision().equals("AT") ? (item.getSalvageValue1() * entityInfo.getWeight())
											: 0.0);
						} else {
							entityInfo.setNetLiability(item.getLiability());
						}
					} else {
						entityInfo.setNetLiability(item.getLiability());
					}
					map1.put(mainKey, entityInfo);
				}

				// executorService.submit(() -> {
//					Optional<CategoryDivision> categotyData = this.categoryDivisionRepository
//							.findByDivision(item.getDivision());
//					String category = "FBD";
//					if (!categotyData.isEmpty()) {
//						category = categotyData.get().getCategory();
//					}
				// ..................

//				Optional<DistributorsItemDetails> checker = this.distributorsItemDetailsRepo
//						.findByMaterialCodeAndItemDetailCode(item.getMaterialCode(),
//								item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""));
//				if (checker.isEmpty()) {
//					DistributorsItemDetails entityInfo = this.mapSturtMapper
//							.DistributorsItemDetailsModelToDistributorsItemDetails(item);
//					entityInfo.setItemDetailCode(
//							item.getWdCode() + item.getCategory() + item.getMonth().replace(" ", ""));
//					if (item.getCategory().equals("FOOD")) {
//						entityInfo.setNetLiability(item.getLiability() - item.getSalveg());
//					} else {
//						entityInfo.setNetLiability(item.getLiability());
//					}
//					flag = true;
//					Optional.ofNullable(this.distributorsItemDetailsRepo.save(entityInfo)).orElseThrow(
//							() -> new IllegalArgumentException("Error | unavle store data of DistributerItemData"));
//				} else {
//					DistributorsItemDetails data = checker.get();
//					data.setLiability(data.getLiability() + item.getLiability());
//					data.setNetLiability(data.getNetLiability() + item.getLiability());
//					this.distributorsItemDetailsRepo.save(data);
//				}

				// ....................
				// });
			}));

			System.out.print(map1.size());

			map1.entrySet().stream().forEach(item -> {
				executorService.submit(() -> {
					try {
						Optional<DistributorsItemDetails> checker = this.distributorsItemDetailsRepo
								.findByMaterialCodeAndItemDetailCode(item.getValue().getMaterialCode(),
										item.getValue().getItemDetailCode());
						if (checker.isEmpty()) {
							this.distributorsItemDetailsRepo.save(item.getValue());
						}
						flag = true;
					} catch (Exception e) {
						e.printStackTrace();
						System.out.print(e.getMessage());
					}
				});
			});

			executorService.shutdown();
//			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//				throw new RuntimeException("Task execution did not complete in time.");
//			}
			if (flag) {
				System.out.print("kkkkkkkkkk");

				distributorsItemDetailsData.forEach(item -> {
					Aspect.distributorItemDetails.add(item);
				});
			}
			flag = false;
			System.out.print("finish");
			return "success";
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerroradd + e.getMessage());
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public List<LiabilityData> getLastUpdatedData() {
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
		return this.liabilityDataRepo.findByModifyAtAfter(yesterday);
	}

	// ................................................................................

//	@SuppressWarnings("unused")
//	public String addDistributorsItemDetails(List<DistributorsItemDetailsModel> distributorsItemDetailsData) {
////		int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
////		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//		try {
//			distributorsItemDetailsData.stream().forEach((item -> {
//				Optional<CategoryDivision> categotyData = this.categoryDivisionRepository
//						.findByDivision(item.getDivision());
//				String category = "FOOD";
//				if (!categotyData.isEmpty()) {
//					category = categotyData.get().getCategory();
//				}
//				LiabilityData liabilityData = this.liabilityDataRepo.findByWdCodeAndMonthAndCategory(item.getWdCode(),
//						item.getMonth(), category);
//				DistributorsItemDetails entityInfo = this.mapSturtMapper
//						.DistributorsItemDetailsModelToDistributorsItemDetails(item);
//
//				boolean flag = false;
//				if (liabilityData == null) {
//					String itemDetailCode = item.getWdCode() + category + item.getMonth().replace(" ", "");
//					entityInfo.setItemDetailCode(itemDetailCode);
//					entityInfo.setCategory(category);
//					entityInfo.setNetLiability(item.getLiability());
//					Optional.ofNullable(this.distributorsItemDetailsRepo.save(entityInfo))
//							.orElseThrow(() -> new IllegalArgumentException("Error | "));
//					flag = true;
//					liabilityData = this.mapSturtMapper.distributorsItemDetailsModelToLiabilityData(item);
//					liabilityData
//							.setYears(item.getMonth().split(" ").length == 2 ? item.getMonth().split(" ")[1] : "N/A");
//					liabilityData.setCategory(category);
//					liabilityData.setLiabilityCode(UUID.randomUUID().toString().replace("-", ""));
//					liabilityData.setItemDetailCode(itemDetailCode);
//					liabilityData.setQuater(generateOuater(item.getMonth()));
//					Optional.ofNullable(this.liabilityDataRepo.save(liabilityData))
//							.orElseThrow(() -> new IllegalArgumentException("Error | "));
//				} else {
//					Optional<DistributorsItemDetails> checker = this.distributorsItemDetailsRepo
//							.findByMaterialCodeAndItemDetailCode(item.getMaterialCode(),
//									liabilityData.getItemDetailCode());
//					if (checker.isEmpty()) {
//						entityInfo.setItemDetailCode(liabilityData.getItemDetailCode());
//						entityInfo.setLiability(item.getLiability());
//						entityInfo.setCategory(category);
//						entityInfo.setNetLiability(item.getLiability());
//						Optional.ofNullable(this.distributorsItemDetailsRepo.save(entityInfo))
//								.orElseThrow(() -> new IllegalArgumentException("Error | "));
//						liabilityData.setLiability(liabilityData.getLiability() + item.getLiability());
//						Optional.ofNullable(this.liabilityDataRepo.save(liabilityData))
//								.orElseThrow(() -> new IllegalArgumentException("Error |  "));
//						flag = true;
//					}
//				}
//				if (flag) {
//					try {
//						DistributorsItem distributorsItem = this.distributorsItemRepo.findByWdCode(item.getWdCode());
//						if (distributorsItem != null) {
//							distributorsItem
//									.setLiability(distributorsItem.getLiability() == null ? 0.0 + item.getLiability()
//											: distributorsItem.getLiability() + item.getLiability());
//							distributorsItem = this.distributorsItemRepo.save(distributorsItem);
//						}
//					} catch (Exception e) {
//						log.error("unable to find Distributers");
//					}
//				}
//			}));
//			return "success";
//		} catch (Exception e) {
//			e.getStackTrace();
//			log.error(LogContant.logserviceerroradd + e.getMessage());
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
//		}
//	}

	// ................................................................................

	public String generateOuater(String month) {
		String[] str = month.split(" ");
		if (str[0].equals("April") || str.equals("May") || str.equals("June")) {
			return "Q1";
		} else {
			if (str.equals("July") || str.equals("August") || str.equals("September")) {
				return "Q2";
			} else {
				if (str.equals("October") || str.equals("November") || str.equals("December")) {
					return "Q3";
				} else {
					return "Q4";
				}
			}
		}
	}

	public List<DistributorsItemDetails> getAllDistributersItemDetails() {
		try {
			return this.distributorsItemDetailsRepo.findAll();
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public Flux<DistributorsItemDetails> getAllWdItemOfMonth(String itemDetailsCode) {
		try {

			List<DistributorsItemDetails> data = this.distributorsItemDetailsRepo.findByItemDetailCode(itemDetailsCode);
			data = data.stream().filter((item) -> {
//				if (this.inventoryRepository.findByMaterialCode(item.getMaterialCode()) != null) {
//					System.out.println("h");
//				}
				if (item.getLiability() != 0) {
					item.setQuantityPac(this.inventoryRepository.findByMaterialCode(item.getMaterialCode()) != null
							? ((int) (item.getWeight()
									/ this.inventoryRepository.findByMaterialCode(item.getMaterialCode()).getWeight()))
							: 0.0);

					return true;
				}
				return false;
			}).toList();
			return Flux.fromIterable(data);
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public String cancelEspecificItem(String materialCode, String itemCode) {
		try {
			Optional<DistributorsItemDetails> data = this.distributorsItemDetailsRepo
					.findByMaterialCodeAndItemDetailCode(materialCode, itemCode);
			if (data.isEmpty()) {
				throw new IllegalArgumentException("Error| unable to find ItemData");
			}
			DistributorsItemDetails entityData = data.get();
			entityData.setStatus("CANCEL");
			this.distributorsItemDetailsRepo.save(entityData);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorupdate);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

	public List<DistributorsItemDetails> getAllAuditedItemOfOOneWd(List<String> materialCode, String itemDetailsCode) {
		try {
			List<DistributorsItemDetails> data = this.distributorsItemDetailsRepo.findByItemDetailCode(itemDetailsCode);
			return data.stream().filter(item -> {
				if (materialCode.contains(item.getMaterialCode())) {
					int ind = materialCode.indexOf(item.getMaterialCode());
					materialCode.remove(ind);
					return true;
				} else {
					return false;
				}
			}).collect(Collectors.toList());
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

}
