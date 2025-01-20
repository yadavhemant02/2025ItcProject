package com.itcbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.WdAuthentication;

@Repository
public interface WdAuthenticationRepository extends JpaRepository<WdAuthentication, Long> {

	WdAuthentication findByWdCode(String wdCode);

}
