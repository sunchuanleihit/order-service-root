package com.loukou.order.service.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderAction;


public interface OrderActionDao extends CrudRepository<OrderAction, Integer>{

	List<OrderAction> findByOrderSnMainAndAction(String orderSnMain, int action);

	@Query("SELECT orderSnMain FROM OrderAction WHERE action=?1 AND actionTime>?2")
	List<String> findOrderSnMainByActionAndActionTimeGreaterThan(int action, Date date);
	
	List<OrderAction> findByActionAndOrderSnMainIn(int action, List<String> orderSnMain);

	List<OrderAction> findByOrderSnMain(String orderSnMain);
	
	List<OrderAction> findByTaoOrderSnAndAction(String taoOrderSn,int action);

}
