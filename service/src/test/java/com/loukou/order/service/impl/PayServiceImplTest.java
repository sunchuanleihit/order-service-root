package com.loukou.order.service.impl;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;

/**
 * 支付逻辑测试用例
 * @author YY
 *
 */
public class PayServiceImplTest extends AbstractTestObject{

	@Autowired
	private PayServiceImpl payServiceImpl;
	
	@Test
	public void testPayOrder() {
		int userId = 60094;
		int payType = 2;
		int paymentId = 4;
		String orderSnMain = "121012145816991";
		int isTaoxinka = 0;
		int isVcount = 0;
		AbstractPayOrderRespDto resp = payServiceImpl.payOrder(userId, payType, paymentId, orderSnMain, isTaoxinka, isVcount);
		System.out.println(resp.getCode());
	}

	@Test
	public void testFinishOrderPay() {
		fail("Not yet implemented");
	}

}
