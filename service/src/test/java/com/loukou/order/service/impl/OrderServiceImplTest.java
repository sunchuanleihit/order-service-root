package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;

public class OrderServiceImplTest extends AbstractTestObject {

	@Autowired
	private OrderService orderService;
	
	@Test
	public void submitOrder() {
		SubmitOrderReqDto req = new SubmitOrderReqDto();
		req.setUserId(1032752);
		SubmitOrderRespDto resp = orderService.submitOrder(req);
		System.out.println(object2String(resp));
	}
	
	@Test
	public void getCouponList() {
		int cityId = 1;
		int userId = 113981;
		int storeId = 18055;
		String openId = "test-openId";
		CouponListRespDto resp = orderService.getCouponList(cityId, userId, storeId, openId);
		System.out.println(object2String(resp));
	}
}
