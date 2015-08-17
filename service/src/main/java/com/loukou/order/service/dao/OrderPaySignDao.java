package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderPaySign;


public interface OrderPaySignDao extends CrudRepository<OrderPaySign, Integer>{

	OrderPaySign findByOutOrderSn(String outTradeSn);

	List<OrderPaySign> findByOrderSnMainAndPayId(String orderSnMain, int payId);
	
}
