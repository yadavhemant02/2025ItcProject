package com.itcbusiness.repository;

import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.PlanningData;

import jakarta.transaction.Transactional;

@Repository
public interface PlanningDataRepository extends JpaRepository<PlanningData, Long> {

	@Transactional
	@Query("SELECT p FROM PlanningData p JOIN p.auditor a WHERE KEY(a) = :auditorCode")
	List<PlanningData> findByAuditorCode(@Param("auditorCode") String auditorCode);

	PlanningData findByLiabilityCode(String liabilityCode);

	default void updateQuantity(double auditedQuantity, String liabilityCode) {
		try {
			PlanningData data = this.findByLiabilityCode(liabilityCode);
			double totalLiability = parseTotalLiability(data.getLiability());
			data.setLiability((totalLiability - auditedQuantity) + "");
			data.setPlanning(((parseTotalLiability(data.getPlanning())) - auditedQuantity) + "");
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

//	private String branch;
//	private String wdCode;
//	private String wdName;
//	private String town;
//	private String month;
//	private String quater;
//	private String years;
//	private String tatalLiability;
//	private String liability;
//	private String planning;
//	private String remaining;
//	private Map<String, List<String>> auditor;
//	private String liabilityCode;
//	private LocalDate todayDate;

//	@Transactional
//	@Query("SELECT * com.itcbusiness.model.TransientDataModel(a.branch, a.wdCode, a.wdName, a.town, "
//			+ "a.month, a.quater, a.years, a.tatalLiability, b.liability, b.planning, "
//			+ "b.remaining, b.auditor, a.liabilityCode, a.todayDate) "
//			+ "FROM LiabilityData a JOIN PlanningData b ON a.liabilityCode = b.liabilityCode "
//			+ "WHERE a.liabilityCode = :b.liabilityCode")
//	List<TransientDataModel> findCombinedDataByLiabilityCode();

}
