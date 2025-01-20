package com.itcbusiness.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.Inventory;

import jakarta.transaction.Transactional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

//	@Transactional
//	@Query("SELECT DISTINCT ON (productName) * FROM Inventory")
//	List<Inventory> findAllByName();

//	@Transactional
//	@Query("SELECT i FROM Inventory i WHERE i.id IN (SELECT MIN(sub.id) FROM Inventory sub GROUP BY sub.productName)")
//	List<Inventory> findAllByName();

	List<Inventory> findByProductCode(String productCode);

	Inventory findByMaterialCode(String materialCode);

	List<Inventory> findByDivision(String division);

	@Transactional
	@Query("SELECT i FROM Inventory i WHERE i.division IN (:divisions)")
	List<Inventory> findByDivisionIn(@Param("divisions") List<String> divisions);

	Slice<Inventory> findAllByCategory(String category, PageRequest of);

	Slice<Inventory> findAllByDivision(String division, PageRequest of);

}
