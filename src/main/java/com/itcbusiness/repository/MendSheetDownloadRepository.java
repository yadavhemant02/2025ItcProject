package com.itcbusiness.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.MendSheetDownload;

@Repository
public interface MendSheetDownloadRepository extends JpaRepository<MendSheetDownload, Long> {

	Optional<MendSheetDownload> findByMonth(String string);

}
