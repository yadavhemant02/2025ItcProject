package com.itcbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.DistributorsItem;

import jakarta.transaction.Transactional;

@Repository
public interface DistributorsItemRepository extends JpaRepository<DistributorsItem, Long> {

	DistributorsItem findByWdCode(String wdCode);

	@Modifying
	@Transactional
	@Query("UPDATE DistributorsItem u SET u.approvalLiability = :liability WHERE u.wdCode = :wdCode")
	void updateApprovalLiability(@Param("wdCode") String wdCode, @Param("liability") Double liability);

	@Modifying
	@Transactional
	@Query("UPDATE DistributorsItem u SET u.liability = :liability WHERE u.wdCode = :wdCode")
	void updateLiability(@Param("wdCode") String wdCode, @Param("liability") Double liability);

	@Modifying
	@Transactional
	@Query("UPDATE DistributorsItem u SET u.liability = :liability, u.approvalLiability = :approvalLiability, u.destructionLiability = :destructionLiability WHERE u.wdCode = :wdCode")
	void updateApprovalLiabilityAndAll(@Param("wdCode") String wdCode, @Param("liability") Double liability,
			@Param("approvalLiability") Double approvalLiability,
			@Param("destructionLiability") Double destructionLiability);

}
