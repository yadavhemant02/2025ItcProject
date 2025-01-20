package com.itcbusiness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.WdInvoice;

@Repository
public interface WdInvoiceRepository extends JpaRepository<WdInvoice, Long> {

	List<WdInvoice> findByWdCode(String wdCode);

}
