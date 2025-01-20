package com.itcbusiness.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.MendSheetDownload;
import com.itcbusiness.repository.MendSheetDownloadRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MendSheetDownloadService {

	private MendSheetDownloadRepository mendSheetDownloadRepository;

	public MendSheetDownloadService(MendSheetDownloadRepository mendSheetDownloadRepository) {
		super();
		this.mendSheetDownloadRepository = mendSheetDownloadRepository;
	}

	public byte[] getMendSheetDownloadByMonth(String month) {
		try {
			return this.mendSheetDownloadRepository.findByMonth(month).get().getSheetData();
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public HashSet<String> getAllDateWhichContainMendSheet() {
		HashSet<String> set = new HashSet<>();
		try {
			List<MendSheetDownload> data = this.mendSheetDownloadRepository.findAll();
			data.stream().forEach((item) -> {
				set.add(item.getMonth());
			});
			return set;
		} catch (Exception e) {
			e.getStackTrace();
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

}
