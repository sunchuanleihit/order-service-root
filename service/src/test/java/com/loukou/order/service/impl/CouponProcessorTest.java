package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.resp.dto.CouponListRespDto;

public class CouponProcessorTest extends AbstractTestObject {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CouponOperationProcessor couponOperationProcessor;
	
	@Autowired
	private CoupRuleDao coupRuleDao;
	

	@Test
	public void couponListTest() {
		CouponListRespDto dto = orderService.getCouponList(1, 113981, 18055, "test-openId", 0);
		dto.getCode();
	}
	
	@Test 
	public void activateCoupon() {
		orderService.activateCoupon(113981, "test-openId", "SD39305189626");
	}
	
	@Test 
	public void generateCouponRangeTest() {
		CoupRule coupRule = coupRuleDao.findOne(933);
		couponOperationProcessor.generateCouponRange(coupRule);
	}
	
}
