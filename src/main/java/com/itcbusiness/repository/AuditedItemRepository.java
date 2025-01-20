package com.itcbusiness.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.AuditedItem;

@Repository
public interface AuditedItemRepository extends JpaRepository<AuditedItem, Long> {

	List<AuditedItem> findByLiabilityCode(String liabilityCode);

	List<AuditedItem> findByWdCode(String wdCode);

	List<AuditedItem> findByItemDetailsCode(String itemDetailsCode);

	List<AuditedItem> findByItemDetailsCodeAndCode(String itemDetailsCode, String code);

	@Query("SELECT DISTINCT a.wdCode FROM AuditedItem a")
	Set<String> findAllDistinctWdCodes();

	@Query("SELECT DISTINCT a.wdCode FROM AuditedItem a WHERE a.createdAt BETWEEN :startDateTime AND :endDateTime")
	Set<String> findWdCodesBetweenDates(@Param("startDateTime") LocalDateTime startDateTime,
			@Param("endDateTime") LocalDateTime endDateTime);

}
