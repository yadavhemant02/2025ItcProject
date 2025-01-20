package com.itcbusiness.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.repository.DistributorsItemRepository;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {

	private LiabilityDataRepository liabilityDataRepo;
	private final DistributorsItemRepository distributorsItemRepository;

	public ReportService(LiabilityDataRepository liabilityDataRepo,
			DistributorsItemRepository distributorsItemRepository) {
		super();
		this.liabilityDataRepo = liabilityDataRepo;
		this.distributorsItemRepository = distributorsItemRepository;
	}

	public List<BranchLiability> getBranchLiability() {
		try {
			List<LiabilityData> liabilityData = this.liabilityDataRepo.findAll();
			liabilityData = liabilityData.stream().filter((item) -> {
				if (item.getPayments() != null && item.getPayments().equals("DONE")) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());

			HashMap<String, BranchLiability> map = new HashMap<>();
			liabilityData.stream().forEach((item) -> {

				if (map.containsKey(item.getBranch())) {
					BranchLiability branchLiability = map.get(item.getBranch());
					branchLiability.setLiability(branchLiability.getLiability() + item.getLiability());
					branchLiability
							.setApprovalLiability(branchLiability.getApprovalLiability() + item.getApprovalLiability());
					branchLiability.setDestrectionLiability(
							branchLiability.getDestrectionLiability() + item.getDestructionLiability());
					map.put(item.getBranch(), branchLiability);
				} else {
					BranchLiability branchLiability = new BranchLiability();
					branchLiability.setBranch(item.getBranch());
					branchLiability.setLiability(item.getLiability());
					branchLiability.setApprovalLiability(item.getApprovalLiability());
					branchLiability.setDestrectionLiability(item.getDestructionLiability());
					map.put(item.getBranch(), branchLiability);
				}
			});
			List<BranchLiability> result = map.entrySet().stream().map(entry -> entry.getValue())
					.collect(Collectors.toList());
			return result;
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public List<MonthLiability> getliabilityOfMonthsByYears(String years) {
		try {
			HashMap<String, MonthLiability> map = new HashMap<>();
			List<LiabilityData> data = this.liabilityDataRepo.findByYears(years);

			data = data.stream().filter((item) -> {
				if (item.getPayments() != null && item.getPayments().equals("DONE")) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());

			data.stream().forEach((item) -> {
				String monthKey = item.getMonth().replace(" ", "");

				if (map.containsKey(monthKey)) {
					MonthLiability monthLiability = map.get(monthKey);
					monthLiability.setLiability(monthLiability.getLiability() + item.getLiability());
					monthLiability
							.setApprovalLiability(monthLiability.getApprovalLiability() + item.getApprovalLiability());
					monthLiability.setDestrectionLiability(
							monthLiability.getDestrectionLiability() + item.getDestructionLiability());
					map.put(monthKey, monthLiability);
				} else {
					MonthLiability monthLiability = new MonthLiability();
					monthLiability.setMonth(monthKey);
					monthLiability.setLiability(item.getLiability());
					monthLiability.setApprovalLiability(item.getApprovalLiability());
					monthLiability.setDestrectionLiability(item.getDestructionLiability());
					map.put(monthKey, monthLiability);
				}
			});
			List<MonthLiability> result = map.entrySet().stream().map(entry -> entry.getValue())
					.collect(Collectors.toList());
			return result;

		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public List<YearsLiability> getliabilityByYears() {
		try {
			HashMap<String, YearsLiability> map = new HashMap<>();
			List<LiabilityData> data = this.liabilityDataRepo.findAll();

			data = data.stream().filter((item) -> {
				if (item.getPayments() != null && item.getPayments().equals("DONE")) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());

			data.stream().forEach((item) -> {
				if (map.containsKey(item.getYears())) {
					YearsLiability yearsLiability = map.get(item.getYears());
					yearsLiability.setLiability(yearsLiability.getLiability() + item.getLiability());
					yearsLiability
							.setApprovalLiability(yearsLiability.getApprovalLiability() + item.getApprovalLiability());
					yearsLiability.setDestrectionLiability(
							yearsLiability.getDestrectionLiability() + item.getDestructionLiability());
					map.put(item.getYears(), yearsLiability);
				} else {
					YearsLiability yearsLiability = new YearsLiability();
					yearsLiability.setYears(item.getYears());
					yearsLiability.setLiability(item.getLiability());
					yearsLiability.setApprovalLiability(item.getApprovalLiability());
					yearsLiability.setDestrectionLiability(item.getDestructionLiability());
					map.put(item.getYears(), yearsLiability);
				}
			});
			List<YearsLiability> result = map.entrySet().stream()
					.filter(entry -> !entry.getValue().getYears().equals("N/A")).map(Map.Entry::getValue)
					.collect(Collectors.toList());

			return result;

		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public AllLiabilityData getAllLiabilityAndApprovalLiability() {
		try {
			List<DistributorsItem> distributorItemData = this.distributorsItemRepository.findAll();
			AllLiabilityData responseData = new AllLiabilityData();
			distributorItemData.stream().forEach(item -> {
				if (item != null) {
					if (item.getLiability() != null) {
						responseData.setTotalLiability(responseData.getTotalLiability() + item.getLiability());
					}
					if (item.getApprovalLiability() != null) {
						responseData.setApprovalLiability(
								responseData.getApprovalLiability() + item.getApprovalLiability());
					}
					if (item.getLiability() != null && item.getApprovalLiability() != null) {
						responseData.setAuditedLiability(
								responseData.getAuditedLiability() + item.getDestructionLiability());
					}
				}
			});
			return responseData;
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public List<responseWdAllData> getAllWdDataYearswise(String wdCode) {
		List<responseWdAllData> responseData = new ArrayList<>();
		HashMap<String, List<LiabilityData>> map = new HashMap<>();
		try {
			List<LiabilityData> data = this.liabilityDataRepo.findByWdCode(wdCode);

			data = data.stream().filter((item) -> {
				if (item.getPayments() != null && item.getPayments().equals("DONE")) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());

			data.stream().forEach((item) -> {
				if (map.containsKey(item.getYears())) {
					List<LiabilityData> list = new ArrayList<>(map.get(item.getYears()));
					list.add(item);
					map.put(item.getYears(), list);
				} else {
					List<LiabilityData> list = new ArrayList<>();
					list.add(item);
					map.put(item.getYears(), list);
				}
			});
			for (Map.Entry<String, List<LiabilityData>> item : map.entrySet()) {
				responseData.add(new responseWdAllData(item.getKey(), item.getValue()));
			}
			return responseData;
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public categoryLiabilityRes getAllLibilityCategoryWise() {
		categoryLiabilityRes responseData = new categoryLiabilityRes();
		try {
			List<LiabilityData> dataNEUP = this.liabilityDataRepo.findByBranch("NEUP");
			dataNEUP = dataNEUP.stream().filter((item) -> {
				if (item.getPayments() != null && item.getPayments().equals("DONE")) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());
			List<LiabilityData> dataNLUC = this.liabilityDataRepo.findByBranch("NLUC");
			dataNLUC = dataNLUC.stream().filter((item) -> {
				if (item.getPayments() != null && item.getPayments().equals("DONE")) {
					return false;
				}
				return true;
			}).collect(Collectors.toList());

			CategoryData categoryDataNEUP = processBranchData(dataNEUP);
			CategoryData categoryDataNLUC = processBranchData(dataNLUC);

			responseData.setNEUP(categoryDataNEUP);
			responseData.setNLUC(categoryDataNLUC);

			return responseData;

		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	private CategoryData processBranchData(List<LiabilityData> data) {
		HashMap<String, HashMap<String, Double>> map = new HashMap<>();
		data.forEach(item -> {
			map.computeIfAbsent(item.getCategory(), k -> new HashMap<>()).merge(item.getYears(), item.getLiability(),
					Double::sum);
		});

		ArrayList<YearsLiability> fbdList = new ArrayList<>();
		ArrayList<YearsLiability> pcpList = new ArrayList<>();
		for (Map.Entry<String, HashMap<String, Double>> entry : map.entrySet()) {
			for (Map.Entry<String, Double> innerEntry : entry.getValue().entrySet()) {
				if (entry.getKey().equals("FOOD")) {
					fbdList.add(new YearsLiability(innerEntry.getKey(), innerEntry.getValue(), 0.0, 0.0));
				} else {
					pcpList.add(new YearsLiability(innerEntry.getKey(), innerEntry.getValue(), 0.0, 0.0));
				}
			}
		}
		CategoryData categoryData = new CategoryData();
		categoryData.setFBD(fbdList);
		categoryData.setPCP(pcpList);

		return categoryData;
	}

	public List<responseWdAllData> getAllDataByDestrectionStatus(String destrectionStatus, String monthAndQuater) {
		if (!monthAndQuater.equals("month")) {
			List<responseWdAllData> responseData = new ArrayList<>();
			HashMap<String, List<LiabilityData>> map = new HashMap<>();
			try {
				System.out.print("hhh");
				List<LiabilityData> data = this.liabilityDataRepo.findByDestructionStatus(destrectionStatus);
				System.out.println(data.size());
				data = data.stream().filter((item) -> {
					if (item.getPayments() != null && item.getPayments().equals("DONE")) {
						return false;
					}
					return true;
				}).collect(Collectors.toList());

				data.stream().forEach((item) -> {
					if (map.containsKey(item.getYears())) {
						List<LiabilityData> list = new ArrayList<>(map.get(item.getYears()));
						boolean flag = false;
						for (LiabilityData inner : list) {
							if (inner.getQuater().equals(item.getQuater())) {
								inner.setLiability(inner.getLiability() + item.getLiability());
								inner.setApprovalLiability(inner.getApprovalLiability() + item.getApprovalLiability());
								inner.setDestructionLiability(
										inner.getDestructionLiability() + item.getDestructionLiability());
								flag = true;
							}
						}
						if (flag == false) {
							list.add(item);
						}
						map.put(item.getYears(), list);
					} else {
						List<LiabilityData> list = new ArrayList<>();
						list.add(item);
						map.put(item.getYears(), list);
					}
				});
				for (Map.Entry<String, List<LiabilityData>> item : map.entrySet()) {
					responseData.add(new responseWdAllData(item.getKey(), item.getValue()));
				}
				return responseData;
			} catch (Exception e) {
				log.error(LogContant.logserviceerrorfind, e);
				throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
			}
		} else {
			List<responseWdAllData> responseData = new ArrayList<>();
			HashMap<String, List<LiabilityData>> map = new HashMap<>();
			try {
				System.out.print("kkk");
				List<LiabilityData> data = this.liabilityDataRepo.findByDestructionStatus(destrectionStatus);
				System.out.println(data.size());
				data = data.stream().filter((item) -> {
					if (item.getPayments() != null && item.getPayments().equals("DONE")) {
						return false;
					}
					return true;
				}).collect(Collectors.toList());

				data.stream().forEach((item) -> {
					if (map.containsKey(item.getYears())) {
						List<LiabilityData> list = new ArrayList<>(map.get(item.getYears()));
						list.add(item);
						map.put(item.getYears(), list);
					} else {
						List<LiabilityData> list = new ArrayList<>();
						list.add(item);
						map.put(item.getYears(), list);
					}
				});
				for (Map.Entry<String, List<LiabilityData>> item : map.entrySet()) {
					responseData.add(new responseWdAllData(item.getKey(), item.getValue()));
				}
				return responseData;
			} catch (Exception e) {
				log.error(LogContant.logserviceerrorfind, e);
				throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
			}

		}
	}

//	public categoryLiabilityRes getAllLibilityCategoryWise() {
//		categoryLiabilityRes responseData = new categoryLiabilityRes();
//		try {
//			List<LiabilityData> dataNEUP = this.liabilityDataRepo.findByBranch("NEUP");
//			List<LiabilityData> dataNLUC = this.liabilityDataRepo.findByBranch("NLUC");
//			categoryLiabilityRes responsedata = new categoryLiabilityRes();
//
//			// ....................
//			HashMap<String, HashMap<String, Double>> map = new HashMap<>();
//			dataNEUP.stream().forEach((item) -> {
//				if (map.containsKey(item.getCategory())) {
//					if (map.get(item.getCategory()).containsKey(item.getYears())) {
//						HashMap<String, Double> yearMap = map.get(item.getCategory());
//						yearMap.put(item.getYears(), yearMap.get(item.getYears()) + item.getLiability());
//						map.put(item.getCategory(), yearMap);
//					} else {
//						HashMap<String, Double> yearMap = new HashMap<>();
//						yearMap.put(item.getCategory(), item.getLiability());
//						map.put(item.getCategory(), yearMap);
//					}
//				}
//			});
//
//			CategoryData innserData = new CategoryData();
//			ArrayList<YearsLiability> fbd = new ArrayList<>();
//			ArrayList<YearsLiability> pcp = new ArrayList<>();
//			for (Map.Entry<String, HashMap<String, Double>> item : map.entrySet()) {
//				for (Map.Entry<String, Double> innerItem : item.getValue().entrySet()) {
//					if (item.getKey().equals("FBD")) {
//						fbd.add(new YearsLiability(innerItem.getKey(), innerItem.getValue()));
//					} else {
//						pcp.add(new YearsLiability(innerItem.getKey(), innerItem.getValue()));
//					}
//				}
//			}
//			innserData.setFBD(fbd);
//			innserData.setPCP(pcp);
//			responsedata.setNEUP(innserData);
//			// .....................................
//
//			HashMap<String, HashMap<String, Double>> map1 = new HashMap<>();
//			dataNEUP.stream().forEach((item) -> {
//				if (map1.containsKey(item.getCategory())) {
//					if (map1.get(item.getCategory()).containsKey(item.getYears())) {
//						HashMap<String, Double> yearMap = map1.get(item.getCategory());
//						yearMap.put(item.getYears(), yearMap.get(item.getYears()) + item.getLiability());
//						map1.put(item.getCategory(), yearMap);
//					} else {
//						HashMap<String, Double> yearMap = new HashMap<>();
//						yearMap.put(item.getCategory(), item.getLiability());
//						map1.put(item.getCategory(), yearMap);
//					}
//				}
//			});
//
//			CategoryData innserData1 = new CategoryData();
//			ArrayList<YearsLiability> fbd1 = new ArrayList<>();
//			ArrayList<YearsLiability> pcp1 = new ArrayList<>();
//			for (Map.Entry<String, HashMap<String, Double>> item : map1.entrySet()) {
//				for (Map.Entry<String, Double> innerItem : item.getValue().entrySet()) {
//					if (item.getKey().equals("FBD")) {
//						fbd1.add(new YearsLiability(innerItem.getKey(), innerItem.getValue()));
//					} else {
//						pcp1.add(new YearsLiability(innerItem.getKey(), innerItem.getValue()));
//					}
//				}
//			}
//			innserData1.setFBD(fbd1);
//			innserData1.setPCP(pcp1);
//
//			responsedata.setNEUP(innserData1);
//			return responseData;
//
//			// .................
////		YearsLiability fbdEntry1 = new YearsLiability("2023", 1000.0);
////		YearsLiability fbdEntry2 = new YearsLiability("2024", 1500.0);
////		YearsLiability pcpEntry1 = new YearsLiability("2023", 2000.0);
////		YearsLiability pcpEntry2 = new YearsLiability("2024", 2500.0);
////		List<YearsLiability> fbdListNEUP = new ArrayList<>(Arrays.asList(fbdEntry1, fbdEntry2));
////		List<YearsLiability> pcpListNEUP = new ArrayList<>(Arrays.asList(pcpEntry1, pcpEntry2));
////		CategoryData categoryDataNEUP = new CategoryData();
////		categoryDataNEUP.setFBD(fbdListNEUP);
////		categoryDataNEUP.setPCP(pcpListNEUP);
////		YearsLiability fbdEntryNLUC1 = new YearsLiability("2023", 1200.0);
////		YearsLiability pcpEntryNLUC1 = new YearsLiability("2023", 2200.0);
////		List<YearsLiability> fbdListNLUC = new ArrayList<>(Collections.singletonList(fbdEntryNLUC1));
////		List<YearsLiability> pcpListNLUC = new ArrayList<>(Collections.singletonList(pcpEntryNLUC1));
////		CategoryData categoryDataNLUC = new CategoryData();
////		categoryDataNLUC.setFBD(fbdListNLUC);
////		categoryDataNLUC.setPCP(pcpListNLUC);
////
////		// Creating categoryLiabilityRes instance
////		categoryLiabilityRes response = new categoryLiabilityRes();
////		response.setNEUP(categoryDataNEUP);
////		response.setNLUC(categoryDataNLUC);
////		return response;
//
//		} catch (Exception e) {
//			log.error(LogContant.logserviceerrorfind, e);
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
//		}
//	}

}

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
class BranchLiability {
	private String branch;
	private Double liability;
	private Double approvalLiability;
	private Double destrectionLiability;

}

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
class MonthLiability {
	private String month;
	private Double liability;
	private Double approvalLiability;
	private Double destrectionLiability;
}

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
class YearsLiability {
	private String years;
	private Double liability;
	private Double approvalLiability;
	private Double destrectionLiability;
}

@Setter
@Getter
class AllLiabilityData {
	private Double totalLiability = 0.0;
	private Double approvalLiability = 0.0;
	private Double auditedLiability = 0.0;
}

@Data
@Setter
@Getter
@AllArgsConstructor
class responseWdAllData {

	private String years;
	private List<LiabilityData> libilityData;

}

@Data
@Setter
@Getter
class categoryLiabilityRes {
	private CategoryData NEUP;
	private CategoryData NLUC;
}

@Setter
@Getter
class CategoryData {
	private List<YearsLiability> PCP;
	private List<YearsLiability> FBD;
}
