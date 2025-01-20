package com.itcbusiness.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.MendCalculation;
import com.itcbusiness.entity.MendSheetData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.MendProvisionCalculationModel;
import com.itcbusiness.model.response.MendSheetResponse;
import com.itcbusiness.model.response.MonthAmountResponse;
import com.itcbusiness.repository.MendCalculationRepository;
import com.itcbusiness.repository.MendSheetDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MendSheetDataService {

	private MendSheetDataRepository mendSheetDataRepo;
	private MapSturtMapper mapSturtMapper;
	private MendCalculationRepository mendCalculationRepository;

	public MendSheetDataService(MendSheetDataRepository mendSheetDataRepo, MapSturtMapper mapSturtMapper,
			MendCalculationRepository mendCalculationRepository) {
		super();
		this.mendSheetDataRepo = mendSheetDataRepo;
		this.mapSturtMapper = mapSturtMapper;
		this.mendCalculationRepository = mendCalculationRepository;
	}

	public String addMandDdataInitail(List<MendSheetData> mendSheetData) {
		HashMap<String, MendSheetData> map = new HashMap<>();
		try {
			mendSheetData.stream().forEach((item) -> {
				if (map.containsKey(item.getGlCode() + item.getMonth())) {
					MendSheetData data = map.get(item.getGlCode() + item.getMonth());
					data.setAmount(data.getAmount() + item.getAmount());
					map.put(item.getGlCode() + item.getMonth(), data);
				} else {
					map.put(item.getGlCode() + item.getMonth(), item);
				}
			});

			map.entrySet().stream().forEach((item) -> {
				MendSheetData dd = this.mendSheetDataRepo.findByGlCodeAndMonth(item.getValue().getGlCode(),
						item.getValue().getMonth());
				if (dd == null) {
					MendSheetData data = item.getValue();
					// String codeId = generateCodeId(data.getYear(), data.getMonth());
					String codeId = LocalDate.now().toString();
					data.setCodeId(codeId);
					this.mendSheetDataRepo.save(data);
					MendCalculation mendCalculationCheck = this.mendCalculationRepository
							.findByCodeIdAndGlCode(item.getValue().getCodeId(), item.getValue().getGlCode());
					if (mendCalculationCheck == null) {
						MendCalculation mendCalculation = new MendCalculation();
						mendCalculation.setGlCode(data.getGlCode());
						mendCalculation.setCodeId(codeId);
						this.mendCalculationRepository.save(mendCalculation);
					}
				}
			});
			return "success";
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public List<MendSheetResponse> getMendSheetResponseData() {
		Map<String, MendSheetResponse> map = new TreeMap<>();
		try {
			List<MendSheetData> data = this.mendSheetDataRepo.findAll();
			// List<MendSheetData> ff =
			// data.stream().sorted().collect(Collections.toList());
			data.stream().forEach((item) -> {
				MendCalculation mendCalculation = this.mendCalculationRepository.findByCodeIdAndGlCode(item.getCodeId(),
						item.getGlCode());
				String checkKey = item.getGlCode() + item.getCodeId();
				// String checkKey1 = item.getGlCode() + item.getYear();
				if (map.containsKey(checkKey)) {
					MonthAmountResponse monthDataObj = new MonthAmountResponse();
					monthDataObj.setMonth(item.getMonth());
					monthDataObj.setAmount(item.getAmount());
					try {
						int val = Integer.parseInt(item.getMonth().substring(5, 7));
//						int monthInt = (item.getMonth() != null && item.getMonth().length() == 7 ? val : 00);
//						int monthInt = (item.getMonth() != null && item.getMonth().length() == 7
//								? (val > 6 ? val - 6 : val + 6)
//								: 00);
						monthDataObj.setMonthInt(val);
					} catch (Exception e) {
						monthDataObj.setMonthInt(00);
					}
					ArrayList<MonthAmountResponse> monthData = new ArrayList<>();
					MendSheetResponse data1 = map.get(checkKey);
					monthData.addAll(data1.getMonthAmount());
					monthData.add(monthDataObj);
					data1.setMonthAmount(monthData);
					data1.setAmount(map.get(checkKey).getAmount() + item.getAmount());
					Comparator<MonthAmountResponse> ll = (MonthAmountResponse o1, MonthAmountResponse o2) -> {
						return o1.getMonth().compareTo(o2.getMonth());
					};
					Collections.sort(monthData, ll);
					data1.setProvision(mendCalculation.getProvision());
					data1.setAverage(mendCalculation.getAverage());
					data1.setLastMonth(mendCalculation.getLastMonth());
					data1.setDiffernce(mendCalculation.getLastMonth() - mendCalculation.getAverage());
					data1.setPerDifference((mendCalculation.getDiffernce() * 100) / mendCalculation.getLastMonth());
					map.put(checkKey, data1);
				} else {
					MendSheetResponse data1 = this.mapSturtMapper.mendSheetDataToMendSheetResponse(item);
					ArrayList<MonthAmountResponse> monthData = new ArrayList<>();
					MonthAmountResponse monthDataObj = new MonthAmountResponse();
					monthDataObj.setMonth(item.getMonth());
					monthDataObj.setAmount(item.getAmount());
					try {
						int val = Integer.parseInt(item.getMonth().substring(5, 7));
//						int monthInt = (item.getMonth() != null && item.getMonth().length() == 7 ? val : 00);
//						int monthInt = (item.getMonth() != null && item.getMonth().length() == 7
//								? (val > 6 ? val - 6 : val + 6)
//								: 00);
						monthDataObj.setMonthInt(val);
					} catch (Exception e) {
						monthDataObj.setMonthInt(00);
					}
					monthData.add(monthDataObj);
					data1.setMonthAmount(monthData);
					data1.setAmount(item.getAmount());
					data1.setProvision(mendCalculation.getProvision());
					data1.setAverage(mendCalculation.getAverage());
					data1.setLastMonth(mendCalculation.getLastMonth());
					data1.setDiffernce(mendCalculation.getLastMonth() - mendCalculation.getAverage());
					data1.setPerDifference((mendCalculation.getDiffernce() * 100) / mendCalculation.getLastMonth());
					map.put(checkKey, data1);
				}
			});
			return new ArrayList<>(map.values());
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public String generateCodeId(String year, String month) {
		try {
			int val = Integer.parseInt(month.substring(5, 7));
			int yearInt = Integer.parseInt(year.substring(2, 4));
			int monthInt = (month != null && month.length() == 7 ? val : 00);
			if (monthInt > 6) {
				return yearInt + "-" + (yearInt + 1);
			}
			return yearInt - 1 + "-" + yearInt;
		} catch (Exception e) {
			return "N/A";
		}
	}

	public String updateProvisionValueAndCalculation(List<MendProvisionCalculationModel> provisionData,
			LocalDate date) {
		try {
			provisionData.stream().forEach((item) -> {
				String codeId = generateCodeId(date);
				MendCalculation data = this.mendCalculationRepository.findByCodeIdAndGlCode(date.toString(),
						item.getGlCode());
				if (data != null) {
					data.setProvision(item.getProvision());
					// otherCalculation................

					List<MendSheetData> sheetData = this.mendSheetDataRepo.findByGlCodeAndCodeId(item.getGlCode(),
							LocalDate.now().toString());
					Comparator<MendSheetData> ll = (MendSheetData o1, MendSheetData o2) -> {
						return o1.getMonth().compareTo(o2.getMonth());
					};
					Collections.sort(sheetData, ll);
					Double currentMonth = sheetData.get(sheetData.size() - 1).getAmount();
					Double sumTotalMonth = 0.0;
//					int val = 0;
//					for (int i = 0; i < sheetData.size(); i++) {
//						int currMonth = Integer.parseInt(sheetData.get(i).getMonth().substring(5, 7));
//						if (currMonth >= val) {
//							currentMonth = sheetData.get(i).getAmount();
//						}
//						sumTotalMonth += sheetData.get(i).getAmount();
//						val = currMonth;
//					}
					for (int i = 0; i < sheetData.size() - 1; i++) {
						sumTotalMonth += sheetData.get(i).getAmount();
						currentMonth = sheetData.get(i).getAmount();
					}
					data.setLastMonth(currentMonth + item.getProvision());
					data.setAverage(sumTotalMonth / (sheetData.size() - 1));
					// System.out.println(data.getAverage());
					data.setDiffernce(sumTotalMonth - currentMonth);
					// ..........
					this.mendCalculationRepository.save(data);
				}
			});
			return "success";
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public String generateCodeId(LocalDate Date) {
		String currdata = Date.toString();
		int month = Integer.parseInt(currdata.substring(8, 10));
		String st1 = currdata.substring(2, 4);
		if (month > 6) {
			return st1 + "-" + (Integer.parseInt(st1) + 1);
		} else {
			return (Integer.parseInt(st1) - 1) + "-" + st1;
		}
	}

	public Set<String> getAllFinancialYears() {
		try {
			List<MendCalculation> data = this.mendCalculationRepository.findAll();
			Set<String> reaponseData = new HashSet<>();
			data.stream().forEach((item) -> {
				if (!item.getCodeId().equals("N/A")) {
					reaponseData.add(item.getCodeId());
				}
			});
			return reaponseData;
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
