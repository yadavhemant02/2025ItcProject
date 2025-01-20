package com.itcbusiness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.ItcUser;

@Repository
public interface ItcUserRepository extends JpaRepository<ItcUser, Long> {

	UserDetails findByEmail(String username);

	List<ItcUser> findByRole(String string);

	List<ItcUser> findByNameAndRole(String string, String string2);

}
