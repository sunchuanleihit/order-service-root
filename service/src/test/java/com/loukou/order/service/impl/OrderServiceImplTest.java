package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.OrderListResultDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;
import com.loukou.order.service.resp.dto.UserOrderNumRespDto;

public class OrderServiceImplTest extends AbstractTestObject {

	@Autowired
	private OrderService orderService;
	
	@Test
	public void submitOrder() {
		SubmitOrderReqDto req = new SubmitOrderReqDto();
		req.setUserId(1032752);
		req.setOpenId("414F8167B0CF4C3AA3603C7CF63365DD");
		req.setStoreId(18047);
		req.setCityId(1);
		req.setAddressId(128);
		req.setOs("ios");
		req.getShippingTimes().getMaterial().add("2015-07-28 09:00:00");
		
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
	
	@Test
	public void getOrder(){
		int userId = 48635;
		String orderSnMain = "120108035625905";
		orderService.getPayOrderMsg(userId, orderSnMain);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void getOrderInfoTest() {
		OrderListRespDto resp = orderService.getOrderInfo(1156347, "150629161315204", 1, 3430340);//3430340, 1
		OrderListResultDto result = resp.getResult();
	}
	
	@Test
	public void getPayOrderMsgTest() {
		PayOrderResultRespDto resp = orderService.getPayOrderMsg(1156347, "150707164298209");
		resp.getCode();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void shareAfterPayTest() {
		ShareRespDto resp = orderService.shareAfterPay("150707164298209");
	}
	
	@Test
	public void getOrderList() {
		OrderListRespDto  resp = orderService.getOrderList(1032752, 1, 1, 10);
		System.out.println(resp.getCode());
		
	}
	
	@Test
	public void getOrderNumTest() {
		UserOrderNumRespDto resp = orderService.getOrderNum(1032752);
		System.out.println(String.format("delieverNum = %d",  resp.getDeliveryNum()));
		System.out.println(String.format("payNum = %d",  resp.getPayNum()));
		System.out.println(String.format("refundNum = %d",  resp.getRefundNum()));
		
	}
	
	@Test 
	public void getOrderInfoTest2() {
		OrderListRespDto resp = orderService.getOrderInfo(1032752, "150804120270754", 1, 3430486);
		resp.getCode();
	}
}
