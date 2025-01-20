package com.itcbusiness.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.DistributorsItemDetails;

import jakarta.transaction.Transactional;

@Repository
public interface DistributorsItemDetailsRepository extends JpaRepository<DistributorsItemDetails, Long> {

	List<DistributorsItemDetails> findByItemDetailCode(String itemDetailCode);

	Optional<DistributorsItemDetails> findByMaterialCodeAndItemDetailCode(String materialCode, String itemDetailCode);

	@Transactional
	@Query("SELECT d FROM DistributorsItemDetails d WHERE d.materialCode = :materialCode AND d.itemDetailCode = :itemDetailCode")
	List<DistributorsItemDetails> findByMaterialCodeAndItemDetailCodee(@Param("materialCode") String materialCode,
			@Param("itemDetailCode") String itemDetailCode);

	@Modifying
	@Transactional
	@Query("UPDATE DistributorsItemDetails d " + "SET d.quantityPac = d.weight / :inventoryWeight "
			+ "WHERE d.materialCode = :materialCode AND d.itemDetailCode = :itemDetailCode")
	void updateQuantityPac(@Param("materialCode") String materialCode, @Param("itemDetailCode") String itemDetailCode,
			@Param("inventoryWeight") double inventoryWeight);

}
