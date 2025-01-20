package com.itcbusiness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.AuditedImage;

@Repository
public interface AuditedImageRepository extends JpaRepository<AuditedImage, Long> {

	List<AuditedImage> findByItemDetailsCode(String itemDetailsCode);

	void deleteByItemDetailsCode(String itemDetailsCode);

}
