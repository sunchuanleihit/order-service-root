package com.loukou.order.pay.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.loukou.order.pay.dal.OrderPayContext;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardPayRespVO;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;

@Service
public class TXKPay {

	private final Logger logger = Logger.getLogger(TXKPay.class);

	// 对订单进行taoxinka支付
	public boolean pay(OrderPayContext context) {
		// 计算需要支付的总额
		double needToPay = context.getAmountToPay();
		if (needToPay <= 0) {
			//如果无需支付
			return true;
		}
		// 支付
		double paid = makeTXKPay(context.getUserId(), context.getUserName(),
				needToPay, 0, context.getOrderSnMain());
		// 把成功支付的金额分配到各子单
		context.consume(PaymentEnum.PAY_TXK, paid);
		// 发生了支付就认为成功
		return paid == needToPay || paid > 0;
	}

	/*
	 * 调用掏心卡接口进行订单支付
	 */
	private double makeTXKPay(int userId, String userName, double amount,
			int orderId, String orderSnMain) {
		double paid = 0;
		double balance = AccountTxkProcessor.getProcessor().getTxkBalanceByUserId(userId);
		if(balance <= 0){
			logger.info(String.format(
					"makeTXKPay amount balance zero order[%s] user[%d] amount[%f]",
					orderSnMain, userId, amount));
			return 0;
		}
		if(balance < amount){
			amount = balance;
		}
		TxkCardPayRespVO resp = AccountTxkProcessor.getProcessor().consume(
				orderSnMain, amount, userId, userName);
		if (resp != null) {
			if (resp != null && resp.isSuccess()) {
				paid = amount;
				logger.info(String.format(
						"makeTXKPay done order[%s] user[%d] amount[%f]",
						orderSnMain, userId, amount));
			} 
		} else {
			logger.info(String.format(
					"makeTXKPay failed to pay order[%s] user[%d] amount[%f]",
					orderSnMain, userId, amount));
		}
		return paid;
	}

}
