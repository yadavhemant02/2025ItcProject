package com.itcbusiness.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itcbusiness.aspectop.Aspect;
import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.entity.ItcUser;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.ApprovalModel;
import com.itcbusiness.model.AuditorModel;
import com.itcbusiness.model.LiabilityDataModel;
import com.itcbusiness.model.PaymentModel;
import com.itcbusiness.repository.DistributorsItemDetailsRepository;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itcbusiness.repository.ItcUserRepository;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@ComponentScan
@Service
@Slf4j
public class LiabilityDataService {

	private LiabilityDataRepository liabilityDataRepo;
	private MapSturtMapper mapper;
	private ItcUserRepository itcUserRepo;
	private DistributorsItemRepository DistributorsItemRepository;
	private DistributorsItemDetailsRepository distributorsItemDetailsRepository;
	private static boolean flag = false;

	public LiabilityDataService(LiabilityDataRepository liabilityDataRepo, MapSturtMapper mapper,
			ItcUserRepository itcUserRepo, DistributorsItemRepository DistributorsItemRepository,
			DistributorsItemDetailsRepository distributorsItemDetailsRepository) {
		super();
		this.liabilityDataRepo = liabilityDataRepo;
		this.mapper = mapper;
		this.itcUserRepo = itcUserRepo;
		this.DistributorsItemRepository = DistributorsItemRepository;
		this.distributorsItemDetailsRepository = distributorsItemDetailsRepository;
	}

	/**
	 * 
	 * @param liabilityData
	 * @return
	 */
//	@SuppressWarnings("null")
//	@Transactional(rollbackOn = RuntimeException.class)
//	public String saveLiabilityData(List<LiabilityDataModel> liabilityData) {
//		System.out.print(liabilityData.size());
//		try {
//			com.itcbusiness.aspectop.Aspect.liabilityDataModel.clear();
//			com.itcbusiness.aspectop.Aspect.liabilityDataModel.addAll(liabilityData);
//			int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
//			ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//			for (LiabilityDataModel item : liabilityData) {
//				executorService.submit(() -> {
//					LiabilityData data1 = this.liabilityDataRepo.findByWdCodeAndWdNameAndMonthAndQuaterAndYears(
//							item.getWdCode(), item.getWdName(), item.getMonth(), item.getQuater(), item.getYears());
//					if (data1 == null) {
//						LiabilityData entityData = this.mapper.liabilityModelToLibility(item);
//						if (item.getAuditor().size() == 1) {
//							List<ItcUser> userData = this.itcUserRepo.findByNameAndRole(item.getAuditor().get(0),
//									"AUDITOR");
//							if (userData.size() > 1) {
//								throw new IllegalArgumentException("find two same name auditor need unic");
//							}
//							if (userData.size() == 1) {
//								item.getAuditor().add(userData.get(0).getUserCode());
//							}
//						}
//						entityData.setAuditor(item.getAuditor());
//						entityData.setLiabilityCode(UUID.randomUUID().toString().replace("-", ""));
//						String[] sheetName = item.getSheetName().split(" ");
//						if (sheetName.length > 1) {
//							entityData.setCategory(sheetName[1].equals("PCP") ? "PCP" : "FBD");
//						}
//						entityData.setBranch(sheetName[0]);
//						entityData.setYears(item.getMonth().split(" ")[1]);
//						entityData.setQuater(item.getQuater().substring(0, 2));
//						entityData = this.liabilityDataRepo.save(entityData);
//						if (entityData == null) {
//							throw new IllegalArgumentException("unable to  save liability data");
//							// return null;
//						}
//					} else {
//						data1.setTown(item.getTown());
//						data1.setLiability(item.getLiability());
//						data1.setAuditor(item.getAuditor());
//						data1.setApproval(item.getApproval());
//						if (item.getAuditor().size() == 1) {
//							List<ItcUser> userData = null;
//							try {
//								userData = this.itcUserRepo.findByNameAndRole(item.getAuditor().get(0), "AUDITOR");
//							} catch (NoResultException e) {
//								log.error("unable to find Auditor");
//								userData = null;
//							}
//							if (userData == null && userData.size() > 1) {
//								throw new IllegalArgumentException("find two same name or null auditor need unic");
//							}
//							if (userData.size() == 1) {
//								item.getAuditor().add(userData.get(0).getUserCode());
//							}
//						}
//						data1.setAuditor(item.getAuditor());
//						String[] sheetName = item.getSheetName().split(" ");
//						if (sheetName.length > 1) {
//							data1.setCategory(sheetName[1].equals("PCP") ? "PCP" : "FBD");
//						}
//						data1.setBranch(sheetName[0]);
//						this.liabilityDataRepo.save(data1);
//					}
//				});
//			}
//			return "success";
//		} catch (Exception e) {
//			e.getStackTrace();
//			log.error(LogContant.logserviceerroradd);
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
//		}
//	}

	@SuppressWarnings("null")
	@Transactional(rollbackOn = RuntimeException.class)
	public String saveLiabilityData(List<LiabilityDataModel> liabilityData) {
		try {
			com.itcbusiness.aspectop.Aspect.liabilityDataModel.clear();
			int numberOfThreads = Runtime.getRuntime().availableProcessors() * 4;
			ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
			for (LiabilityDataModel item : liabilityData) {
				// System.out.print(item);
				// executorService.submit(() -> {

				LiabilityData data1 = this.liabilityDataRepo.findByWdCodeAndMonthAndCategory(item.getWdCode(),
						item.getMonth(), item.getSheetName().split(" ")[1].toUpperCase().equals("FBD") ? "FOOD"
								: item.getSheetName().split(" ")[1].toUpperCase());
//					LiabilityData data1 = this.liabilityDataRepo.findByBranchAndWdCodeAndMonthAndCategory(
//							item.getBranch(), item.getWdCode(), item.getMonth(), item.getSheetName().split(" ")[1]);
				if (data1 == null) {
					flag = true;
					LiabilityData entityData = this.mapper.liabilityModelToLibility(item);
					entityData.setLiabilityCode(UUID.randomUUID().toString().replace("-", ""));
					entityData.setCategory(item.getSheetName().split(" ")[1].toUpperCase().equals("FBD") ? "FOOD"
							: item.getSheetName().split(" ")[1].toUpperCase());
//						entityData.setQuater(item.getQuater() != null && item.getQuater().length() >= 2
//								? item.getQuater().substring(0, 2)
//								: "N/A");
					entityData.setQuater(getQuater(item.getQuater()));
					entityData.setYears(item.getMonth() != null && item.getMonth().split(" ").length > 1
							? item.getMonth().split(" ")[1]
							: "N/A");
					entityData.setBranch(item.getSheetName() != null && item.getSheetName().split(" ").length > 0
							? item.getSheetName().split(" ")[0]
							: "N/A");

					entityData.setApproval(item.getApproval().toUpperCase().contains("YES")
							|| item.getApproval().toUpperCase().contains("APPROVED") ? "YES" : "NO");
					if (entityData.getApproval().equals("YES")) {
						entityData.setApprovalLiability(entityData.getApprovalLiability() + item.getLiability());
					}

					entityData.setDestructionStatus(
							item.getDestructionStatus().toUpperCase().contains("DONE") ? "DONE" : "PENDING");
					if (entityData.getDestructionStatus().equals("DONE")) {
						entityData.setDestructionLiability(
								entityData.getDestructionLiability() + entityData.getApprovalLiability());
					}

					entityData.setItemDetailCode(
							item.getWdCode() + item.getSheetName().split(" ")[1] + item.getMonth().replace(" ", ""));
					try {
						entityData.setLiability(item.getLiability() == null ? 0.0 : item.getLiability());
					} catch (Exception e) {
						entityData.setLiability(0.0);
					}
					entityData = this.liabilityDataRepo.save(entityData);
					if (entityData == null) {
						throw new IllegalArgumentException("unable to  save liability data");
					}
				}
				// });
			}
			executorService.shutdown();
//			executorService.awaitTermination(60, TimeUnit.SECONDS);
//			
			if (flag) {
				liabilityData.forEach(item -> {
					Aspect.liabilityDataModel.add(item);
				});
			}
			flag = false;
			return "success";
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public String getQuater(String quater) {
		try {
			if (quater.contains("Q1")) {
				return "Q1";
			} else {
				if (quater.contains("Q2")) {
					return "Q2";
				} else {
					if (quater.contains("Q3")) {
						return "Q3";
					} else {
						if (quater.contains("Q4")) {
							return "Q4";
						}
					}
				}
			}
			return "N/A";
		} catch (Exception e) {
			return "N/A";
		}
	}

	/**
	 * 
	 * @return
	 */
	public Flux<LiabilityData> getAllLiabilityData() {
		try {
			log.info(LogContant.logservicesuccessfind);
//			return Flux.fromIterable(this.liabilityDataRepo.findAll());
			return Flux.fromIterable(this.liabilityDataRepo.findAll().stream()
					.filter(item -> item.getPayments() == null || !item.getPayments().equals("DONE")) // Exclude "DONE"
					.collect(Collectors.toList()));
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public Flux<LiabilityData> getAllLiabilityDataOfYears(String years) {
		try {
			log.info(LogContant.logservicesuccessfind);
			return Flux.fromIterable(this.liabilityDataRepo.findByYears(years).stream()
					.filter(item -> item.getPayments() == null || !item.getPayments().equals("DONE")) // Exclude "DONE"
					.collect(Collectors.toList()));
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public List<String> getAllYearsData() {
		try {
			return this.liabilityDataRepo.findDistinctYears();
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	/**
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	public List<LiabilityData> getLiabilityDataByPagination(int page, int size) {
		try {
			log.info(LogContant.logservicesuccessfind);
			return this.liabilityDataRepo.findAll(PageRequest.of(page, size)).getContent();
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

//	public List<LiabilityData> getAllDistributers() {
//		try {
//			List<LiabilityData> responseData = this.liabilityDataRepo.findAll();
//			HashMap<String, LiabilityData> map = new HashMap<>();
//			for (LiabilityData item : responseData) {
//				if (map.containsKey(item.getWdName())) {
//					double liability = Double.parseDouble(map.get(item.getWdName()).getLiabilityCode())
//							+ Double.parseDouble(item.getTatalLiability());
//					item.setTatalLiability(liability + "");
//					map.put(item.getWdName(), item);
//				} else {
//					map.put(item.getWdName(), item);
//				}
//			}
//			List<LiabilityData> response = new ArrayList<>(map.values());
//			log.info(LogContant.logservicesuccessfind);
//			return response;
//		} catch (Exception e) {
//			e.getStackTrace();
//			log.error(LogContant.logserviceerrorfind);
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
//		}
//	}

//	public List<LiabilityData> getAllDistributers() {
//		try {
//			List<LiabilityData> responseData = this.liabilityDataRepo.findAll();
//			HashMap<String, LiabilityData> map = new HashMap<>();
//			for (LiabilityData item : responseData) {
//				double currentLiability = 0.0;
//				String tatalLiability = item.getTatalLiability();
//				if (tatalLiability != null && !tatalLiability.equals("N/A") && isNumeric(tatalLiability)) {
//					currentLiability = Double.parseDouble(tatalLiability);
//				} else {
//					log.warn("Invalid TatalLiability value for item: " + item.getWdName() + ", value: "
//							+ tatalLiability);
//				}
//				if (map.containsKey(item.getWdName())) {
//					double existingLiability = Double.parseDouble(map.get(item.getWdName()).getLiability());
//					double totalLiability = existingLiability + currentLiability;
//					item.setLiability(totalLiability));
//				} else {
//					item.setLiability(currentLiability));
//				}
//				map.put(item.getWdName(), item);
//			}
//			List<LiabilityData> response = new ArrayList<>(map.values());
//			log.info(LogContant.logservicesuccessfind);
//			return response;
//		} catch (Exception e) {
//			log.error(LogContant.logserviceerrorfind, e);
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
//		}
//	}

	private boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
//	public List<LiabilityData> getAllDistributers() {
//		try {
//			List<LiabilityData> responseData = this.liabilityDataRepo.findAll();
//			Map<String, LiabilityData> aggregatedData = responseData.stream()
//					.collect(Collectors.toMap(LiabilityData::getWdName, (item) -> {
//						item.setTatalLiability(item.getTatalLiability());
//						return item;
//					}, (existingItem, newItem) -> {
//						double updatedLiability = Double.parseDouble(existingItem.getTatalLiability())
//								+ Double.parseDouble(newItem.getTatalLiability());
//						existingItem.setTatalLiability(String.valueOf(updatedLiability));
//						return existingItem;
//					}));
//			List<LiabilityData> response = new ArrayList<>(aggregatedData.values());
//			log.info(LogContant.logservicesuccessfind);
//			return response;
//		} catch (Exception e) {
//			log.error(LogContant.logserviceerrorfind, e);
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
//		}
//	}

	/**
	 * 
	 * @param branch
	 * @return
	 */
	public Flux<LiabilityData> getAllDistributerDataByBranch(String branch) {
		try {
			Flux<LiabilityData> responseData = Flux.fromIterable(this.liabilityDataRepo.findByBranch(branch).stream()
					.filter((item) -> item.getPayments() == null || !item.getPayments().equals("DONE"))
					.collect(Collectors.toList()));
			log.info(LogContant.logservicesuccessfind);
			return responseData;
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param category
	 * @return
	 */
	public Flux<LiabilityData> getAllDistributerDataByCatogary(String category) {
		try {
			Flux<LiabilityData> responseData = Flux.fromIterable(this.liabilityDataRepo.findByCategory(category)
					.stream().filter((item) -> item.getPayments() == null || !item.getPayments().equals("DONE"))
					.collect(Collectors.toList()));
			log.info(LogContant.logservicesuccessfind);
			return responseData;
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param wdName
	 * @return
	 */
	public List<LiabilityData> getLiabilityDataOfEspecificDistributers(String wdName) {
		try {
			log.info(LogContant.logservicesuccessfind);
			return this.liabilityDataRepo.findByWdName(wdName).stream()
					.filter((item) -> item.getPayments() == null || !item.getPayments().equals("DONE"))
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param auditorCode
	 * @return
	 */
	public List<LiabilityData> getAllDistributerForAuditor(String auditorCode) {
		try {
			List<LiabilityData> responseData = this.liabilityDataRepo.findAll().stream()
					.filter((item) -> item.getPayments() == null || !item.getPayments().equals("DONE"))
					.collect(Collectors.toList());
			log.info(LogContant.logservicesuccessfind);
			return responseData.stream().filter((item) -> {
				try {
					if (item.getAuditor().size() > 1 && item.getAuditor().get(1).equals(auditorCode)) {
						return true;
					}
					return false;
				} catch (Exception e) {
					return false;
				}
			}).collect(Collectors.toList());
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	public Flux<LiabilityData> getAllLiabilityDataDestructionPending(String approval, String destractionStatus) {
		try {
			System.out.println(
					this.liabilityDataRepo.findByApprovalAndDestructionStatus(approval, destractionStatus).size());
			return Flux
					.fromIterable(this.liabilityDataRepo.findByApprovalAndDestructionStatus(approval, destractionStatus)
							.stream().filter((item) -> item.getPayments() == null || !item.getPayments().equals("DONE"))
							.collect(Collectors.toList()));
		} catch (DataRetrievalFailureException e) {
			log.error("Failed to retrieve liability data due to a data retrieval issue: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param wdCode
	 * @return
	 */
	public Flux<LiabilityData> getAllLiabilityDataByWdCode(String wdCode) {
		try {
			return Flux.fromIterable(this.liabilityDataRepo.findByWdCode(wdCode).stream()
					.filter((item) -> item.getPayments() == null || !item.getPayments().equals("DONE"))
					.collect(Collectors.toList()));
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	// Extra
	// work.......................................................................
	// for liability approval

	public HashSet<String> approvalLiability(List<ApprovalModel> approvalData) {
		List<ApprovalModel> responseData = new ArrayList<>();
		HashSet<String> response = new HashSet<>();
		try {
			approvalData.stream().forEach((item) -> {
				try {
					System.out.print(item.getAuditor());
					LiabilityData liabilityData = this.liabilityDataRepo.findByWdCodeAndMonthAndCategory(
							item.getWdCode(), item.getMonth(),
							(item.getCategory().equals("FBD") ? "FOOD" : item.getCategory()));

					if (liabilityData.getApproval() == null || liabilityData.getApproval().equals("NO")) {
						liabilityData.setApproval(item.getApprovalStatus().toUpperCase().contains("APPROVED")
								|| item.getApprovalStatus().toUpperCase().contains("DONE") ? "YES" : "NO");
						liabilityData.setDestructionStatus(liabilityData.getDestructionStatus() == null ? "PENDING"
								: liabilityData.getDestructionStatus());
						liabilityData.setStatus(liabilityData.getStatus() == null ? "N/A" : liabilityData.getStatus());
						try {
							if (!item.getAuditor().isEmpty() && item.getAuditedDate() != null) {
								System.out.print("kkkkkkkkk");
								List<String> auditor = new ArrayList<>();
								List<ItcUser> userData = this.itcUserRepo.findByNameAndRole(item.getAuditor().get(0),
										"AUDITOR");
								if (userData.isEmpty()) {
									response.add(item.getAuditor().get(0));
								}
								auditor.add(item.getAuditor().get(0));
								auditor.add(userData.size() > 1 ? null : userData.get(0).getUserCode());
								liabilityData.setAuditor(auditor);
								liabilityData.setAuditedDate(item.getAuditedDate());
							}
						} catch (Exception e) {
							log.equals("ERROR| unable to put auditor");
						}

						liabilityData
								.setApprovalLiability(liabilityData.getApprovalLiability() + item.getApprovalAmount());
						this.liabilityDataRepo.save(liabilityData);
						DistributorsItem data = this.DistributorsItemRepository.findByWdCode(item.getWdCode());
						if (data.getApprovalLiability() == null) {
							data.setApprovalLiability(item.getApprovalAmount());
						} else {
							data.setApprovalLiability(item.getApprovalAmount() + data.getApprovalLiability());
						}
						data = this.DistributorsItemRepository.save(data);
					} else {
						System.out.print("2");
						if (liabilityData.getApproval().equals("YES")) {
							try {
								if (!item.getAuditor().isEmpty() && item.getAuditedDate() != null) {
									System.out.print("1");
									List<String> auditor = new ArrayList<>();
									List<ItcUser> userData = this.itcUserRepo
											.findByNameAndRole(item.getAuditor().get(0), "AUDITOR");
									if (userData.isEmpty()) {
										response.add(item.getAuditor().get(0));
									}
									auditor.add(item.getAuditor().get(0));
									auditor.add(userData.size() > 1 ? null : userData.get(0).getUserCode());
									liabilityData.setAuditor(auditor);
									liabilityData.setApprovalLiability(item.getApprovalAmount());
									liabilityData.setAuditedDate(item.getAuditedDate());
								}
							} catch (Exception e) {
								log.equals("ERROR| unable to put auditor");
							}
//							liabilityData.setApprovalLiability(
//									liabilityData.getApprovalLiability() + item.getApprovalAmount());
							this.liabilityDataRepo.save(liabilityData);
						}
					}
				} catch (Exception e) {
					responseData.add(item);
				}

			});

			// return responseData;
			System.out.print(response);
			return response;
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String updateAuditors(List<AuditorModel> auditorData) {

		System.out.println(auditorData);
		System.out.print("[[[[[[[[[[[[[[[[[[[[[[[[[[");
		try {
			auditorData.stream().forEach((item) -> {
				LiabilityData liabilityData = this.liabilityDataRepo.findByItemDetailCode(item.getItemDetailCode());
				if (liabilityData != null && liabilityData.getApproval().equals("YES")) {
					List<String> auditor = new ArrayList<>();
					List<ItcUser> userData = this.itcUserRepo.findByNameAndRole(item.getAuditorName(), "AUDITOR");
					auditor.add(item.getAuditorName());
					auditor.add(userData.size() > 1 ? null : userData.get(0).getUserCode());
					liabilityData.setAuditor(auditor);
					if (liabilityData.getApproval() == null) {
						liabilityData.setApproval("YES");
					}
					if (liabilityData.getDestructionStatus() == null) {
						liabilityData.setDestructionStatus("PENDING");
					}
					liabilityData.setAuditedDate(item.getAuditedDate());
					this.liabilityDataRepo.save(liabilityData);
				}
			});
			return "success";
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(
					ExceptionConstant.exceptionserviceupdate + e.getMessage() + "Approval is pending");
		}
	}

	private static int totalvalueOfSalvage = 0;

	@Transactional(rollbackOn = RuntimeException.class)
	public String changeSalvageValue(Double value) {
		try {
			List<LiabilityData> data = this.liabilityDataRepo.findByTodayDate(LocalDate.now());
			data.stream().forEach((item) -> {
				List<DistributorsItemDetails> distributorsItemDetails = this.distributorsItemDetailsRepository
						.findByItemDetailCode(item.getItemDetailCode());
				totalvalueOfSalvage = 0;
				distributorsItemDetails.stream().forEach((inner) -> {
					if (inner.getDivision().equals("AT")) {
						Double newSalvageValue = item.getTotalWeight() * value;
						Double vall = newSalvageValue - inner.getSalvag();
						totalvalueOfSalvage += newSalvageValue - inner.getSalvag();
						inner.setSalvag(newSalvageValue);
						inner.setNetLiability(inner.getLiability() - vall);
						this.distributorsItemDetailsRepository.save(inner);
					}
				});
				item.setLiability(item.getLiability() - totalvalueOfSalvage);
				this.liabilityDataRepo.save(item);
			});
			return "success";
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public String updatePaymentsdata(List<PaymentModel> paymentsData) {
		try {
			paymentsData.stream().forEach((item) -> {
				if (item.getVoucherNo() != null) {
					LiabilityData liabilityData = this.liabilityDataRepo.findByWdCodeAndInvoiceNo(item.getWdCode(),
							item.getInvoiceNo());
					if (liabilityData != null) {
						liabilityData.setPoNumber(item.getPoNumber());
						liabilityData.setPayments("DONE");
						liabilityData.setInvoiceAmount(item.getInvoiceAmount());
						liabilityData.setVoucherNo(item.getVoucherNo());
						liabilityData.setPaymentDate(item.getPaymentDate());
						this.liabilityDataRepo.save(liabilityData);
						DistributorsItem distributorsItem = this.DistributorsItemRepository
								.findByWdCode(item.getWdCode());
						distributorsItem.setLiability(distributorsItem.getLiability() - liabilityData.getLiability());
						distributorsItem.setApprovalLiability(
								distributorsItem.getApprovalLiability() - liabilityData.getApprovalLiability());
						distributorsItem.setDestructionLiability(
								distributorsItem.getDestructionLiability() - liabilityData.getDestructionLiability());
						this.DistributorsItemRepository.save(distributorsItem);
					}
				}
			});
			return "success";
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

	@Transactional(rollbackOn = RuntimeException.class)
	public List<LiabilityData> getAllDataOfLiabilityData() {
		try {
			return this.liabilityDataRepo.findAll();
		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

	public List<LiabilityData> getAllDataOfLiabilityDatabyPaymentsDone(String wdCode, String type) {
		try {
//			if ("DONE".equals(type)) {
//				return this.liabilityDataRepo.findByDestructionStatus("DONE");
//			}
			System.out.print(wdCode);
			List<LiabilityData> data = this.liabilityDataRepo.findByWdCodeAndPayments(wdCode, "DONE");
			System.out.print(data);
			return data;

		} catch (Exception e) {
			log.error("An unexpected error occurred while retrieving liability data: {}", e.getMessage(), e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

}
