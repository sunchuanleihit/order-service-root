package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.loukou.order.service.entity.OrderExtm;


public interface OrderExtmDao extends CrudRepository<OrderExtm, Integer>,JpaSpecificationExecutor<OrderExtm>{

	List<OrderExtm> findByOrderSnMain(String orderSnMain);
	
	OrderExtm findByOrderId(Integer orderId);

	List<OrderExtm> findByOrderSnMainIn(List<String> orderSnMainList);
	
	List<OrderExtm> findByConsignee(String consignee);
	
	List<OrderExtm> findByPhoneMob(String phoneMob);
	
	List<OrderExtm> findByPhoneTel(String phoneTel);
	
	@Transactional(value="transactionManagerMall")
	@Modifying
	@Query("UPDATE OrderExtm set phoneMob = ?2 where orderSnMain = ?1")
	int updateExtmByOrderSnMain(String orderSnMain,String phoneMob);

	List<OrderExtm> findByAddressLike(String queryContent,  Pageable pageable);
}
