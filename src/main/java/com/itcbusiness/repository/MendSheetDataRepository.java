package com.itcbusiness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.MendSheetData;

@Repository
public interface MendSheetDataRepository extends JpaRepository<MendSheetData, Long> {

	MendSheetData findByGlCodeAndMonth(String glCode, String month);

	List<MendSheetData> findByGlCodeAndCodeId(String glCode, String codeId);

}
