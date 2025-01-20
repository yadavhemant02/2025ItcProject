package com.itcbusiness.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itcbusiness.entity.DistributorOrders;

@Repository
public interface DistributorOrdersRepository extends JpaRepository<DistributorOrders, Long> {

}
