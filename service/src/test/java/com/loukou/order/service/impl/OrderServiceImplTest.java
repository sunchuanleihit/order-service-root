package com.loukou.order.service.impl;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.InviteService;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.constants.CouponFormType;
import com.loukou.order.service.constants.InviteConstans;
import com.loukou.order.service.req.dto.InviteInfoReqdto;
import com.loukou.order.service.req.dto.ReturnStorageGoodsReqDto;
import com.loukou.order.service.req.dto.ReturnStorageReqDto;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.InviteInfoRespDto;
import com.loukou.order.service.resp.dto.InviteListDto;
import com.loukou.order.service.resp.dto.OrderCancelRespDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.OrderListResultDto;
import com.loukou.order.service.resp.dto.ReturnStorageRespDto;
import com.loukou.order.service.resp.dto.ShippingMsgRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;
import com.loukou.order.service.resp.dto.UserOrderNumRespDto;
import com.loukou.order.service.util.DateUtils;

public class OrderServiceImplTest extends AbstractTestObject {

	@Autowired
	private OrderService orderService;
	@Autowired
	private InviteService inviteService;

	@Test
	public void cancelOrder() {
		OrderCancelRespDto resp = orderService.cancelOrder(1032752, "150807095023438");
		
		resp.getCode();
	}
	
	@Test
	public void submitOrder() {
		SubmitOrderReqDto req = new SubmitOrderReqDto();
		req.setUserId(1032752);
		req.setOpenId("414F8167B0CF4C3AA3603C7CF63365DD");
		req.setStoreId(18042);
		req.setCityId(1);
		req.setAddressId(128);
		req.setOs("ios");
		req.getShippingTimes().getMaterial().add("2015-09-20 13:32:00");
		
		SubmitOrderRespDto resp = orderService.submitOrder(req);
		System.out.println(object2String(resp));
	}

	@Test
	public void getCouponList() {
		int cityId = 1;
		int userId = 113981;
		int storeId = 18055;
		String openId = "test-openId";
		CouponListRespDto resp = orderService.getCouponList(cityId, userId,
				storeId, openId, 1);
		System.out.println(object2String(resp));
	}


	@Test
	public void getOrder() {
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
	public void returnStorage() {
		ReturnStorageReqDto req = new ReturnStorageReqDto();
		req.setStoreId(1432);
		req.setTaoOrderSn("nj0131021214077861");

		ReturnStorageGoodsReqDto[] goodsList = new ReturnStorageGoodsReqDto[3];

		ReturnStorageGoodsReqDto goo = new ReturnStorageGoodsReqDto();
		goo.setSpecId(608017);
		goo.setConfirmNum(10);

		ReturnStorageGoodsReqDto goo1 = new ReturnStorageGoodsReqDto();
		goo1.setSpecId(610927);
		goo1.setConfirmNum(11);

		ReturnStorageGoodsReqDto goo2 = new ReturnStorageGoodsReqDto();
		goo2.setSpecId(622294);
		goo2.setConfirmNum(12);

		goodsList[0] = goo;
		goodsList[1] = goo1;
		goodsList[2] = goo2;
		req.setSpecList(goodsList);
		System.out.println(object2String(req));
		ReturnStorageRespDto resp = orderService.returnStorage(req);
		System.out.println(object2String(resp));
	}

	@Test
	public void getLkStatusItemMap() {
		System.out.println(object2String(orderService.getLkStatusItemMap()));
	}

	@Test
	public void getOrderList() {
		OrderListRespDto  resp = orderService.getOrderList(1032752, 2, 6, 10);
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
		OrderListRespDto resp = orderService.getOrderInfo(1032752, "150807102826381", 1, 0);
		resp.getCode();
	}
	
	@Test
	public void shippingMsgTest() {
		ShippingMsgRespDto  resp = orderService.getShippingResult("150807100189816");
		resp.getCode();
	}
	
	@Test
	public void createCouponCode() {
		// 新人券
		int userId = 1156390;
		int couponId = 932;
		boolean resp = true;;
//		resp = orderService.createCouponCode(userId, couponId, 1, false, 1, "openId", 0);
//		System.out.println(resp);
//		
//		couponId = 933;
//		resp = orderService.createCouponCode(userId, couponId, 1, false, 1, "openId", 0);
//		System.out.println(resp);
		
		// 微信红包
//		resp = orderService.createCouponCode(userId, 1, 1, false, 
//	    		 0, "", 1);
//		resp=orderService.createCouponCode(1156410 , InviteConstans.invited_CouponId,  CouponFormType.PRIVATE,false, 1,"", 0);
		resp=orderService.createCouponCode(1156410 , InviteConstans.INVITED_COUPONID,  CouponFormType.PRIVATE,false, 1,"", 0);
		System.out.println(resp);
		
	}
	/*
	 * 查询邀请码
	 */
	@Test
	public void getInviteCode(){
		InviteInfoReqdto req=new InviteInfoReqdto();
		req.setUserId(1032752);
		req.setQueryType("code");
		InviteInfoRespDto r  =new InviteInfoRespDto();
		r=inviteService.getInviteInfo(req);
	System.out.println(r.getInviteCode()+"已奖励"+r.getTotalReward());	
		
	}
	
	@Test
	public void getInviteList(){
		InviteInfoReqdto req=new InviteInfoReqdto();
		req.setUserId(10);
		req.setQueryType("list");
		req.setPageSize(0);
		InviteInfoRespDto r  =new InviteInfoRespDto();
		r=inviteService.getInviteInfo(req);
		for(InviteListDto d:r.getInviteList()){
			System.out.println(d.getMoblie()+"----"+d.getInviteStatus()+"----"+d.getReward());
			System.out.println(r.getInviteCode()+"已奖励"+r.getTotalReward());	
		}
	}
	
	@Test
	public void getTodayGoodsCount() {
		int userId = 1156403;
		int specId = 7957;
		int storeId = 18047;
		System.out.println(orderService.getTodayGoodsCount(userId, specId));
		
		System.out.println(orderService.getTodayStoreGoodsCount(storeId, specId));
	}
	
}
