package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderExtm;


public interface OrderExtmDao extends CrudRepository<OrderExtm, Integer>,JpaSpecificationExecutor<OrderExtm>{

	List<OrderExtm> findByOrderSnMain(String orderSnMain);
	
	OrderExtm findByOrderId(Integer orderId);

	List<OrderExtm> findByOrderSnMainIn(List<String> orderSnMainList);
	
	List<OrderExtm> findByConsignee(String consignee);
	
	List<OrderExtm> findByPhoneMob(String phoneMob);
	
	List<OrderExtm> findByPhoneTel(String phoneTel);

}
