package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderGoodsR;

public interface OrderGoodsRDao extends CrudRepository<OrderGoodsR, Integer> {
	List<OrderGoodsR> findByOrderId(int orderId);
	
	List<OrderGoodsR> findByOrderIdR(int orderIdR);
}
