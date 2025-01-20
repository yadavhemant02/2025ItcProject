package com.itcbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.InvoiceData;

@Repository
public interface InvoiceDataRepository extends JpaRepository<InvoiceData, Long> {

}
