package com.loukou.order.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.impl.VAcountPay.OrderModels;
import com.loukou.order.service.req.dto.PayOrderReqContent;
import com.loukou.order.service.resp.dto.ResponseCodeDto;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardPayRespVO;

public class TXKPay {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	// 对订单进行淘心卡支付
	private boolean payTxk(String orderSnMain, PayOrderReqContent orderDto,
			List<OrderModels> allModels, ResponseCodeDto response) {
		// 计算总额
		double toPay = 0;
		for (OrderModels model : allModels) {
			OrderPay txkPay = model.findPay(PaymentEnum.PAY_TXK);
			if (txkPay != null && txkPay.getMoney() > 0) {
				toPay = DoubleUtils.add(txkPay.getMoney(), toPay);
			}
		}
		if (toPay == 0) {
			return true;
		}
		// 完成支付
		double allPaid = makeTxkPay(orderSnMain, toPay, orderDto.getUserId(),
				StringUtils.trim(orderDto.getUserName()), true, response);
		if (response.getCode() != 200) {
			return false;
		}
		// 更新订单和支付记录状态
		for (OrderModels model : allModels) {
			OrderPay txkPay = model.findPay(PaymentEnum.PAY_TXK);
			double txkToPay = (txkPay != null ? txkPay.getMoney() : 0);
			if (txkToPay == 0) {
				continue;
			}
			// 实际可支付
			double paid = (allPaid > txkToPay ? txkToPay : allPaid);
			allPaid = DoubleUtils.sub(allPaid, paid);
			// 按实际支付记录
			if (paid > 0) {
				// txk支付
				txkPay.setMoney(paid);
				txkPay.setStatus(OrderPayStatusEnum.STATUS_SUCC.getStatus());
			} else {
				txkPay.setStatus(OrderPayStatusEnum.STATUS_CANCEL.getStatus());
			}
			if (txkToPay > paid) {
				// 取消txk支付并转移到现金支付
				OrderPay cashPay = model.findOrCreatePay(PaymentEnum.PAY_CASH);
				cashPay.setMoney(DoubleUtils.add(cashPay.getMoney(),
						DoubleUtils.sub(txkToPay, paid)));
			}
		}
		return true;
	}

	private double makeTxkPay(String orderSnMain, double amount, int userId,
			String userName, boolean trySufficient, ResponseCodeDto response) {
		double paid = 0;
		TxkCardPayRespVO resp = AccountTxkProcessor.getProcessor().consume(
				orderSnMain, amount, userId, userName);
		if (resp != null && resp.isSuccess()) {
			paid = amount;
			logger.info(String.format(
					"makeTxkPay done order[%s] user[%d] amount[%f]",
					orderSnMain, userId, amount));
		} else if (trySufficient) {
			double sufficient = AccountTxkProcessor.getProcessor()
					.getTxkBalanceByUserId(userId);
			if (sufficient > 0 && sufficient < amount) { // 因為返回null不一定是錢不夠，所以只有錢的確不夠才打日誌
				logger.info(String
						.format("makeVaccountPay insufficient order[%s] user[%d] amount[%f] available[%f]",
								orderSnMain, userId, amount, sufficient));
			}
			double toPay = Math.min(sufficient, amount);
			if (toPay > 0) {
				return makeTxkPay(orderSnMain, toPay, userId, userName, false,
						response);
			} else {
				response.setCode(500);
				response.setDesc("淘心卡扣款失败");
			}
		} else {
			response.setCode(500);
			response.setDesc("淘心卡扣款失败");
		}
		return paid;
	}


}
