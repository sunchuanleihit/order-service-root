package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;

public class OrderServiceImpTest extends AbstractTestObject{
	
	@Autowired
	private OrderService orderService;
	
	@Test
	public void getOrder(){
		int userId = 48635;
		String orderSnMain = "120108035625905";
		orderService.getPayOrderMsg(userId, orderSnMain);
	}
}
