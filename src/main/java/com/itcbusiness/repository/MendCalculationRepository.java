package com.itcbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.MendCalculation;

@Repository
public interface MendCalculationRepository extends JpaRepository<MendCalculation, Long> {

	MendCalculation findByCodeIdAndGlCode(String codeId, String glCode);

}
