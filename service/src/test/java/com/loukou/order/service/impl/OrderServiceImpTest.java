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
		OrderListRespDto resp = orderService.getOrderInfo(1156347, "150707164298209", 1);
		System.out.println(String.format("resp code = %d", resp.getCode()));
		System.out.println(String.format("main order count = %d", resp.getResult().getOrderCount()));
		for(OrderListDto dto : resp.getResult().getOrderList()) {
			OrderListBaseDto base = dto.getBase();
			List<GoodsListDto> goodsList = dto.getGoodsList();
			ExtmMsgDto extmMsg = dto.getExtmMsg();
			ShippingMsgDto shippingMsg = dto.getShippingmsg();
		}
	}
}
