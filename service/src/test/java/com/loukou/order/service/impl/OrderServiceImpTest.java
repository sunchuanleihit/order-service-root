package com.loukou.order.service.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.OrderListBaseDto;
import com.loukou.order.service.resp.dto.OrderListDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.PayOrderMsgDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShareResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;

public class OrderServiceImpTest extends AbstractTestObject{
	
	@Autowired
	private OrderService orderService;
	
	@Test
	public void getOrder(){
		int userId = 48635;
		String orderSnMain = "120108035625905";
		orderService.getPayOrderMsg(userId, orderSnMain);
	}
	
	@Test
	public void getOrderInfoTest() {
		@SuppressWarnings("unused")
		OrderListRespDto resp = orderService.getOrderInfo(1156347, "150629161315204", 1);
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
		orderService.getOrderList(1156347, 1);
	}
}
