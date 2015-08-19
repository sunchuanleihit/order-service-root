package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.resp.dto.CouponListRespDto;

public class CouponProcessorTest extends AbstractTestObject {

	@Autowired
	private OrderService orderService;

	@Test
	public void couponListTest() {
		CouponListRespDto dto = orderService.getCouponList(1, 113981, 18055, "test-openId", 0);
		dto.getCode();
	}
	
	@Test 
	public void activateCoupon() {
		orderService.activateCoupon(113981, "test-openId", "SHARE93475896228");
	}
	
}
