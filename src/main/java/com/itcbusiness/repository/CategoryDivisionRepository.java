package com.itcbusiness.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.CategoryDivision;

@Repository
public interface CategoryDivisionRepository extends JpaRepository<CategoryDivision, Long> {

	Optional<CategoryDivision> findByDivision(String division);

}
