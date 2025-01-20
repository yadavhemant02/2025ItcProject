package com.itcbusiness.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.entity.PlanningData;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.PlanningDataModel;
import com.itcbusiness.model.TransientDataModel;
import com.itcbusiness.repository.LiabilityDataRepository;
import com.itcbusiness.repository.PlanningDataRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class PlanningDataService {

	private PlanningDataRepository planningDataRepository;
	private MapSturtMapper mapper;
	private LiabilityDataRepository liabilityDataRepo;
	private ModelMapper modelMapper;

	public PlanningDataService(PlanningDataRepository planningDataRepository, MapSturtMapper mapper,
			LiabilityDataRepository liabilityDataRepo, ModelMapper modelMapper) {
		super();
		this.planningDataRepository = planningDataRepository;
		this.mapper = mapper;
		this.liabilityDataRepo = liabilityDataRepo;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public String savePlanningData(List<PlanningDataModel> planningData) {
		try {
			for (PlanningDataModel item : planningData) {
				PlanningData data1 = this.planningDataRepository.findByLiabilityCode(item.getLiabilityCode());
				if (data1 == null) {
					PlanningData entityData = this.mapper.planingModeToPlanning(item);
					entityData = this.planningDataRepository.save(entityData);
					if (entityData == null) {
						throw new IllegalArgumentException("PlanningData not save");
					}
				} else {
					data1.setAuditor(item.getAuditor());
					data1.setPlanning(item.getPlanning());
					data1.setRemaining(item.getRemaining());
					this.planningDataRepository.save(data1);
				}
			}
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public Flux<TransientDataModel> getAllPlanningData() {
		try {
			List<PlanningData> response = this.planningDataRepository.findAll();
			List<TransientDataModel> responseData = new ArrayList<>();
			for (PlanningData item : response) {
				TransientDataModel data = new TransientDataModel();
				try {
					LiabilityData liabilityData = this.liabilityDataRepo.findByLiabilityCode(item.getLiabilityCode());
					// data.setAuditor(liabilityData.getAuditor());
					if (liabilityData != null) {
						data = this.modelMapper.map(liabilityData, TransientDataModel.class);
					}
				} catch (Exception e) {
					log.warn("Could not fetch liability data for code: " + item.getLiabilityCode(), e);
				}
				data.setAuditor(item.getAuditor());
				data.setPlanning(item.getPlanning());
				data.setRemaining(item.getRemaining());
				data.setTodayDate(item.getTodayDate());
				responseData.add(data);
			}
			return Flux.fromIterable(responseData);
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public Flux<TransientDataModel> getAllRecordsOfOneAuditor(String auditorCode) {
		try {
			List<PlanningData> response = this.planningDataRepository.findByAuditorCode(auditorCode);
			List<TransientDataModel> responseData = new ArrayList<>();
			for (PlanningData item : response) {
				TransientDataModel data = new TransientDataModel();
				try {
					LiabilityData liabilityData = this.liabilityDataRepo.findByLiabilityCode(item.getLiabilityCode());
					if (liabilityData != null) {
						data = this.modelMapper.map(liabilityData, TransientDataModel.class);
					}
				} catch (Exception e) {
					log.warn("Could not fetch liability data for code: " + item.getLiabilityCode(), e);
				}
				data.setAuditor(item.getAuditor());
				data.setPlanning(item.getPlanning());
				data.setRemaining(item.getRemaining());
				data.setTodayDate(item.getTodayDate());
				responseData.add(data);
			}
			return Flux.fromIterable(responseData);
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	public String uupdateDataOfPrePlanning(PlanningDataModel liabilityData) {
		try {
			PlanningData responseData = this.planningDataRepository
					.findByLiabilityCode(liabilityData.getLiabilityCode());
			responseData.setPlanning(liabilityData.getPlanning());
			responseData.setRemaining(liabilityData.getRemaining());
			responseData.setAuditor(liabilityData.getAuditor());
			this.planningDataRepository.save(responseData);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorupdate, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceupdate + e.getMessage());
		}
	}

//	public List<TransientDataModel> getPlaningDataByPagination(int page, int size) {
//		ArrayList<TransientDataModel> response = new ArrayList<>();
//		try {
//			List<PlanningData> responseData = this.planningDataRepository.findAll(PageRequest.of(page, size))
//					.getContent();
//
//			for (PlanningData item : responseData) {
//				LiabilityData data1 = null;
//				TransientDataModel transientData = new TransientDataModel();
//				try {
//					data1 = this.liabilityDataRepo.findByLiabilityCode(item.getLiabilityCode());
//					transientData = this.mapper.liabilityDataToTransientDataModel(data1);
//				} catch (Exception e) {
//					data1 = null;
//				}
//				transientData.setAuditor(item.getAuditor());
//				transientData.setPlanning(item.getPlanning());
//				transientData.setRemaining(item.getRemaining());
//				transientData.setTodayDate(item.getTodayDate());
//				response.add(transientData);
//			}
//			return response;
////			return responseData.stream().map((item) -> {
////				LiabilityData data1 = null;
////				try {
////					data1 = this.liabilityDataRepo.findByLiabilityCode(item.getLiabilityCode());
////				} catch (Exception e) {
////					data1 = null;
////				}
////				TransientDataModel transientData = this.mapper.liabilityDataToTransientDataModel(data1);
////				transientData.setAuditor(item.getAuditor());
////				transientData.setPlanning(item.getPlanning());
////				transientData.setRemaining(item.getRemaining());
////				transientData.setTodayDate(item.getTodayDate());
////				return transientData;
////			}).collect(Collectors.toList());
//		} catch (Exception e) {
//			log.error(LogContant.logserviceerrorfind, e);
//			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
//		}
//	}

	public List<TransientDataModel> getPlaningDataByPagination(int page, int size) {
		try {
			List<PlanningData> responseData = this.planningDataRepository.findAll(PageRequest.of(page, size))
					.getContent();
			List<TransientDataModel> response = new ArrayList<>();
			for (PlanningData item : responseData) {
				TransientDataModel transientData = new TransientDataModel();
				try {
					LiabilityData liabilityData = this.liabilityDataRepo.findByLiabilityCode(item.getLiabilityCode());
					// transientData.setAuditor(liabilityData.getAuditor());
					if (liabilityData != null) {
						transientData = this.mapper.liabilityDataToTransientDataModel(liabilityData);
					}
				} catch (Exception e) {
					log.warn("Could not fetch liability data for code: " + item.getLiabilityCode(), e);
				}
				transientData.setAuditor(item.getAuditor());
				transientData.setPlanning(item.getPlanning());
				transientData.setRemaining(item.getRemaining());
				transientData.setTodayDate(item.getTodayDate());
				response.add(transientData);
			}

			return response;
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	@Transactional
	public List<TransientDataModel> getPlanningDataByQuater(String quater) {
		try {
			List<LiabilityData> liabilityData = this.liabilityDataRepo.findByQuater(quater);
			List<TransientDataModel> response = new ArrayList<>();
			for (LiabilityData item : liabilityData) {
				TransientDataModel transientData = new TransientDataModel();
				transientData = this.mapper.liabilityDataToTransientDataModel(item);
				PlanningData planningData = null;
				try {
					planningData = this.planningDataRepository.findByLiabilityCode(item.getLiabilityCode());
					transientData.setAuditor(planningData.getAuditor());
					transientData.setPlanning(planningData.getPlanning());
					transientData.setRemaining(planningData.getRemaining());
					transientData.setTodayDate(planningData.getTodayDate());
				} catch (Exception e) {
					planningData = null;
				}
				response.add(transientData);
			}
			return response;
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind, e);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}

	}

}
