package com.loukou.order.pay.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.pay.dal.RechargePayContext;
import com.loukou.order.pay.impl.AliPay;
import com.loukou.order.pay.impl.WeiXinPay;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.resp.dto.ALiPayOrderResultDto;
import com.loukou.order.service.resp.dto.WeixinPayOrderResultDto;

public class RechargePayProcessor {

	private final Logger logger = Logger.getLogger(RechargePayProcessor.class);

	@Autowired
	private AliPay aliPay;

	@Autowired
	private WeiXinPay wxPay;

	/**
	 * 充值订单的微信支付
	 * @param userId 支付用户
	 * @param orderSnMain 支付订单
	 * @return
	 */
	public WeixinPayOrderResultDto payWx(int userId, String orderSnMain) {
		RechargePayContext context = new RechargePayContext(userId, orderSnMain);
		if (context.init()) {
			context.recordAction(OrderActionTypeEnum.TYPE_CHOOSE_PAY, "选择支付方式在线支付");
			//如果相继支付成功
			return wxPay.preparePay(context);
		} else {
			logger.error(String
					.format("payWx fail user_id[%d] order_sn_main[%s]",
							userId, orderSnMain));
			return null;
		}
	}

	/**
	 * 充值订单的支付宝支付
	 * @param userId 支付用户
	 * @param orderSnMain 支付订单
	 * @return
	 */
	public ALiPayOrderResultDto payAli(int userId, String orderSnMain) {
		RechargePayContext context = new RechargePayContext(userId, orderSnMain);
		if (context.init()) {
			context.recordAction(OrderActionTypeEnum.TYPE_CHOOSE_PAY, "选择支付方式在线支付");
			//如果相继支付成功
			return aliPay.preparePay(context);
		} else {
			logger.error(String
					.format("payAli fail user_id[%d] order_sn_main[%s]",
							userId, orderSnMain));
			return null;
		}
	}

}
