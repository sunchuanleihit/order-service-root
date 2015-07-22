package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderPaySign;


public interface OrderPaySignDao extends CrudRepository<OrderPaySign, Integer>{

	OrderPaySign findByOutOrderSn(String outTradeSn);

	
}
