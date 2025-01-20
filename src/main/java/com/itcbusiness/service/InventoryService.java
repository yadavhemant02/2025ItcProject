package com.itcbusiness.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itcbusiness.aspectop.Aspect;
import com.itcbusiness.entity.Inventory;
import com.itcbusiness.mapper.MapSturtMapper;
import com.itcbusiness.model.InventoryModel;
import com.itcbusiness.repository.CategoryDivisionRepository;
import com.itcbusiness.repository.InventoryRepository;
import com.itcbusiness.util.ExceptionConstant;
import com.itcbusiness.util.LogContant;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class InventoryService {

	private InventoryRepository inventoryRepo;
	private MapSturtMapper mapper;
	private CategoryDivisionRepository categoryDivisionRepo;

	public InventoryService(InventoryRepository inventoryRepo, MapSturtMapper mapper,
			CategoryDivisionRepository categoryDivisionRepo) {
		super();
		this.inventoryRepo = inventoryRepo;
		this.mapper = mapper;
		this.categoryDivisionRepo = categoryDivisionRepo;
	}

	/**
	 * @param inventoryData
	 * @return
	 */
	@Transactional(rollbackOn = RuntimeException.class)
	public String saveInventoryData(List<InventoryModel> inventoryData) {
		int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		HashMap<String, HashSet<String>> map = new HashMap<>();
		try {
			inventoryData.stream().forEach((item) -> {
				if (map.containsKey(item.getCategory())) {
					HashSet<String> set = new HashSet<>(map.get(item.getCategory()));
					set.add(item.getDivision());
					map.put(item.getCategory(), set);
				} else {
					HashSet<String> set = new HashSet<>();
					set.add(item.getDivision());
					map.put(item.getCategory(), set);
				}
				executorService.submit(() -> {
					Inventory enventoryData = this.inventoryRepo.findByMaterialCode(item.getMaterialCode());
					if (enventoryData == null) {
						Inventory entityData = this.mapper.inventoryModelToInventory(item);
						entityData.setProductCode(generateCode(item.getMaterialCode(), 1L, item.getDivision()));
						Inventory responseData = this.inventoryRepo.save(entityData);
						if (responseData == null) {
							var error = new IllegalArgumentException("error | unable to add data in inventryList");
							error.initCause(new NullPointerException(error.getMessage()));
							throw error;
						}
					} else {
						Inventory entityData = this.mapper.inventoryModelToInventory(item);
						entityData.setProductCode(enventoryData.getProductCode());
						entityData.setId(enventoryData.getId());
						entityData.setCreatedAt(enventoryData.getCreatedAt());
						Inventory responseData = this.inventoryRepo.save(entityData);
						if (responseData == null) {
							var error = new IllegalArgumentException("error | unable to add data in inventryList");
							error.initCause(new NullPointerException(error.getMessage()));
							throw error;
						}
					}
				});
			});

//			for (InventoryModel item : inventoryData) {
//				Inventory enventoryData = this.inventoryRepo.findByMaterialCode(item.getMaterialCode());
//				if (enventoryData == null) {
//					Inventory entityData = this.mapper.inventoryModelToInventory(item);
//					entityData.setProductCode(generateCode(item.getMaterialCode(), 1L, item.getDivision()));
//					executorService.submit(() -> {
//						Inventory responseData = this.inventoryRepo.save(entityData);
//						if (responseData == null) {
//							var error = new IllegalArgumentException("error | unable to add data in inventryList");
//							error.initCause(new NullPointerException(error.getMessage()));
//							throw error;
//						}
//					});
//				} else {
//					Inventory entityData = this.mapper.inventoryModelToInventory(item);
//					entityData.setProductCode(enventoryData.getProductCode());
//					entityData.setId(enventoryData.getId());
//					entityData.setCreatedAt(enventoryData.getCreatedAt());
//					executorService.submit(() -> {
//						Inventory responseData = this.inventoryRepo.save(entityData);
//						if (responseData == null) {
//							var error = new IllegalArgumentException("error | unable to add data in inventryList");
//							error.initCause(new NullPointerException(error.getMessage()));
//							throw error;
//						}
//					});
//				}
//			}
			executorService.shutdown();
			Aspect.categoryDivision.clear();
			Aspect.categoryDivision.putAll(map);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerroradd);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionserviceadd + e.getMessage());
		}
	}

	/**
	 * 
	 * 
	 * @param page
	 * @param size : for pagination
	 * @return
	 */
	public List<Inventory> getInventoryDataByPagination(int page, int size) {
		try {
			return this.inventoryRepo.findAll(PageRequest.of(page, size)).getContent();
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param page
	 * @param size
	 * @param category
	 * @return
	 */
	public List<Inventory> getInventoryDataOfCategoryByPagination(int page, int size, String category) {
		try {
			return this.inventoryRepo.findAllByCategory(category, PageRequest.of(page, size)).getContent();
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param page
	 * @param size
	 * @param division
	 * @return
	 */
	public List<Inventory> getInventoryDataOfdivisionByPagination(int page, int size, String division) {
		try {
			return this.inventoryRepo.findAllByDivision(division, PageRequest.of(page, size)).getContent();
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * @return : return all data
	 */
	@Transactional
	public Flux<Inventory> getAllInventoryData() {
		try {
			return Flux.fromIterable(this.inventoryRepo.findAll());
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public Inventory getInventoryData(String materialCode) {
		try {
			return this.inventoryRepo.findByMaterialCode(materialCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public String updateInventoryData(Inventory inventoryData) {
		try {
			Inventory data = this.inventoryRepo.findByMaterialCode(inventoryData.getMaterialCode());
			inventoryData.setId(data.getId());
			this.inventoryRepo.save(inventoryData);
			return "success";
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param division
	 * @return : filter by division
	 */
	public List<Inventory> getAllInventoryDataByDivision(List<String> division) {
		try {
			return this.inventoryRepo.findByDivisionIn(division);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * 
	 * @param materialCode
	 * @return : find by materialCode
	 */
	public Inventory geteInventoryDataByMaterialCode(String materialCode) {
		try {
			return this.inventoryRepo.findByMaterialCode(materialCode);
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	/**
	 * @return : count length of Inventory Data
	 */
	public Long countofInventoryData() {
		try {
			return this.inventoryRepo.count();
		} catch (Exception e) {
			log.error(LogContant.logserviceerrorfind);
			throw new DataRetrievalFailureException(ExceptionConstant.exceptionservicefind + e.getMessage());
		}
	}

	public String generateCode(String name, Long no, String subName) {
		String result = String.join("", name.split(" ")).toUpperCase();
		return result + no + subName;
	}

}
