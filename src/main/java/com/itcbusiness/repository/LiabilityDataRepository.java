package com.itcbusiness.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.DistributorsItemDetailsModel;

import jakarta.transaction.Transactional;

@Repository
public interface LiabilityDataRepository extends JpaRepository<LiabilityData, Long> {

	LiabilityData findByLiabilityCode(String liabilityCode);

	LiabilityData findByWdCodeAndWdNameAndMonthAndQuaterAndYears(String wdCode, String wdName, String month,
			String quater, String years);

	@Transactional
	@Query(value = "SELECT * FROM \"liability_data\" ld WHERE ld.id IN (SELECT MIN(id) FROM \"liability_data\" GROUP BY wd_name)", nativeQuery = true)
	List<LiabilityData> findByDistributerName();

	List<LiabilityData> findByWdName(String wdName);

	List<LiabilityData> findByQuater(String quater);

	List<LiabilityData> findByBranch(String branch);

	List<LiabilityData> findByCategory(String catagory);

	List<LiabilityData> findByDestructionStatus(String string);

	List<LiabilityData> findByApprovalOrDestructionStatus(String approval, String destractionStatus);

	default void updateQuntityAndDestractionStatus(double auditedQuantity, String destractionStatus,
			String liabilityCode) {
		try {
			LiabilityData data = findByLiabilityCode(liabilityCode);
			double totalLiability = data.getLiability(); // parseTotalLiability(data.getLiability());
			data.setLiability((totalLiability - auditedQuantity));
			data.setDestructionStatus(destractionStatus);
			save(data);
		} catch (Exception e) {
			throw new DataRetrievalFailureException(e.getMessage());
		}
	}

	private static double parseTotalLiability(String totalLiability) {
		if (totalLiability == null || "N/A".equals(totalLiability)) {
			return 0.0;
		}
		try {
			return Double.parseDouble(totalLiability);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	List<LiabilityData> findByWdCode(String wdCode);

	LiabilityData findByWdCodeAndMonth(String wdCode, String month);

	LiabilityData findByWdCodeAndMonthAndCategory(String wdCode, String month, String category);

	List<LiabilityData> findByYears(String years);

	List<LiabilityData> findByModifyAtAfter(LocalDateTime yesterday);

	default void updateLiability(DistributorsItemDetailsModel item, String category, MapSturtMapper mapSturtMapper) {
		HashMap<String, Double> map = new HashMap<>();
		if (map.containsKey(item.getWdCode())) {
			map.put(item.getWdCode(), map.get(item.getWdCode()) + item.getLiability());
		} else {
			map.put(item.getWdCode(), item.getLiability());
		}
		// executorService.submit(() -> {
		LiabilityData liabilityData = findByWdCodeAndMonthAndCategory(item.getWdCode(), item.getMonth(), category);
		if (liabilityData == null) {
			LiabilityData newLiabilityData = mapSturtMapper.distributorsItemDetailsModelToLiabilityData(item);
			newLiabilityData.setYears(item.getMonth().split(" ").length == 2 ? item.getMonth().split(" ")[1] : "N/A");
			newLiabilityData.setCategory(category);
			newLiabilityData.setLiabilityCode(UUID.randomUUID().toString().replace("-", ""));
			newLiabilityData.setItemDetailCode(item.getWdCode() + category + item.getMonth().replace(" ", ""));
			newLiabilityData.setQuater(generateOuater(item.getMonth()));
			Optional.ofNullable(save(newLiabilityData))
					.orElseThrow(() -> new IllegalArgumentException("Error | unable store libility data"));
		} else {
			liabilityData.setLiability(liabilityData.getLiability() + item.getLiability());
			Optional.ofNullable(save(liabilityData))
					.orElseThrow(() -> new IllegalArgumentException("Error | unable store libility data update "));
		}

	}

	public static String generateOuater(String month) {
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

	LiabilityData findByBranchAndWdCodeAndMonthAndCategory(String branch, String wdCode, String month, String string);

//	List<LiabilityDataModel> findByApprovalAndDestructionStatus(String approval, String destractionStatus);

	@Transactional
	@Query("SELECT ld FROM LiabilityData ld WHERE ld.approval = :approval AND ld.destructionStatus = :destructionStatus")
	List<LiabilityData> findByApprovalAndDestructionStatus(@Param("approval") String approval,
			@Param("destructionStatus") String destructionStatus);

	LiabilityData findByItemDetailCode(String itemDetailCode);

	List<LiabilityData> findByTodayDate(LocalDate now);

	LiabilityData findByWdCodeAndInvoiceNo(String wdCode, String invoiceNo);

	List<LiabilityData> findByWdCodeAndPayments(String wdCode, String string);

	@Transactional
	@Query("SELECT DISTINCT l.years FROM LiabilityData l ORDER BY l.years")
	List<String> findDistinctYears();

}