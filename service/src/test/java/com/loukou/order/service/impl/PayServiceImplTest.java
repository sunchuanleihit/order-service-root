package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.PayService;
import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;

/**
 * 支付逻辑测试用例
 * @author YY
 *
 */
public class PayServiceImplTest extends AbstractTestObject{

	@Autowired
	private PayService payServiceImpl;
	
	@Test
	public void testPayOrder() {
		int userId = 1032752;
		int payType = 2;
		int paymentId = 207;
		String orderSnMain = "150807194023437";
		int isTaoxinka = 0;
		int isVcount = 0;
		AbstractPayOrderRespDto resp = payServiceImpl.payOrder(userId, payType, paymentId, orderSnMain, isTaoxinka, isVcount);
		System.out.println(resp.getCode());
	}

	@Test
	public void testFinishOrderPay() {
		int paymentId = 4;
		double totalFee = 0.1;
		String orderSnMain = "121012145816991";
		int result = payServiceImpl.finishOrderPay(paymentId, totalFee, orderSnMain);
		System.out.println(result);
	}

}
