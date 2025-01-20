package com.itcbusiness.repositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.itcbusiness.repository.CustomRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;

@Repository
public class CustomRepositoryImpl implements CustomRepository {

	private final EntityManager entityManager;

	public CustomRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public void deleteAllData() {

		List<String> tableNames = entityManager.getMetamodel().getEntities().stream().map(EntityType::getName)
				.collect(Collectors.toList());

//		tableNames = tableNames.stream().filter((item)->{
//			if("ItcUser".equals(item)) {
//				return false;
//			}
//			else {
//				return true;
//			}
//		}).toList();

		tableNames.forEach(tableName -> {
			String deleteQuery = "DELETE FROM " + tableName;
			entityManager.createQuery(deleteQuery).executeUpdate();
		});
	}

	@Override
	@Transactional
	public void deleteAllDataWithOutUser() {

		List<String> tableNames = entityManager.getMetamodel().getEntities().stream().map(EntityType::getName)
				.collect(Collectors.toList());

		tableNames = tableNames.stream().filter((item) -> {
			if ("ItcUser".equals(item)) {
				return false;
			} else {
				return true;
			}
		}).toList();

		tableNames.forEach(tableName -> {
			String deleteQuery = "DELETE FROM " + tableName;
			entityManager.createQuery(deleteQuery).executeUpdate();
		});
	}

}
