package com.loukou.order.pay.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.loukou.order.pay.dal.OrderPayContext;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;
import com.loukou.pos.client.vaccount.resp.VaccountUpdateRespVO;
import com.loukou.pos.client.vaccount.resp.VaccountUpdateRespVO.Code;

@Service
public class VAcountPay {

	private final Logger logger = Logger.getLogger(VAcountPay.class);

	// 对订单进行虚拟账户支付, 返回是否还需要支付
	public boolean pay(OrderPayContext context) {
		// 计算需要支付的总额
		double needToPay = context.getAmountToPay();
		if (needToPay <= 0) {
			//如果无需支付
			return true;
		}
		// 支付
		double paid = makeVaPay(context.getUserId(), needToPay, 0, context.getOrderSnMain());
		// 把成功支付的金额分配到各子单 
		context.consume(PaymentEnum.PAY_VACOUNT, paid);
		// 发生了支付就认为成功
		return paid == needToPay || paid > 0;
	}
	
	public boolean makeVaRecharge(int userId, String userName, double amount,
			String orderSnMain) {
		VaccountUpdateRespVO resp = VirtualAccountProcessor.getProcessor()
				.recharge(userId, amount, orderSnMain, userName);
		if (resp == null
				|| !StringUtils
						.equalsIgnoreCase(resp.getCode(), Code.SUCCESS)) {
			logger.error(String
					.format("recharge fail userid[%d] amount[%f] ordersnmain[%s] resp[%s]",
							userId, amount, orderSnMain, (resp == null ? null
									: resp.toString())));
			return false;
		}
		return true;
	}
	
	/*
	 * 调用虚拟账户接口进行订单支付
	 */
	private double makeVaPay(int userId, double amount, int orderId,
			String orderSnMain) {
		double paid = 0;
		double balance = VirtualAccountProcessor.getProcessor()
				.getVirtualBalanceByUserId(userId);
		if(balance <= 0){
			logger.info(String.format(
					"makeVaPay amount balance zero order[%s] user[%d] amount[%f]",
					orderSnMain, userId, amount));
			return 0;
		}
		if(balance < amount){
			amount = balance;
		}
		VaccountUpdateRespVO resp = VirtualAccountProcessor.getProcessor()
				.consume(userId, amount, orderId, orderSnMain);
		if (resp != null) {
			if (StringUtils.endsWithIgnoreCase(resp.getCode(), Code.SUCCESS)) {
				paid = amount;
				logger.info(String.format(
						"makeVaPay done order[%s] user[%d] amount[%f]",
						orderSnMain, userId, amount));
			} 
		} else {
			logger.info(String.format(
					"makeVaPay failed to pay order[%s] user[%d] amount[%f]",
					orderSnMain, userId, amount));
		}
		return paid;
	}

}
