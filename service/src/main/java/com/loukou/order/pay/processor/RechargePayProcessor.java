package com.loukou.order.pay.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.pay.dal.RechargePayContext;
import com.loukou.order.pay.impl.AliPay;
import com.loukou.order.pay.impl.WeiXinPay;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.resp.dto.ALiPayOrderResultDto;
import com.loukou.order.service.resp.dto.WeixinPayOrderResultDto;

@Service
public class RechargePayProcessor {

	private final Logger logger = Logger.getLogger(RechargePayProcessor.class);

	@Autowired
	private AliPay aliPay;

	@Autowired
	private WeiXinPay wxPay;

	@Autowired
	private GetDaoProcessor getDaoProcessor;
	/**
	 * 充值订单的微信支付
	 * 
	 * @param userId
	 *            支付用户
	 * @param orderSnMain
	 *            支付订单
	 * @return
	 */
	public WeixinPayOrderResultDto payWx(int userId, String orderSnMain) {
		RechargePayContext context = new RechargePayContext(userId, orderSnMain,getDaoProcessor);
		if (context.init()) {
			context.recordAction(OrderActionTypeEnum.TYPE_CHOOSE_PAY,
					"选择支付方式在线支付");
			// 如果相继支付成功
			return wxPay.preparePay(context);
		} else {
			logger.error(String.format(
					"payWx fail user_id[%d] order_sn_main[%s]", userId,
					orderSnMain));
			return null;
		}
	}

	/**
	 * 充值订单的支付宝支付
	 * 
	 * @param userId
	 *            支付用户
	 * @param orderSnMain
	 *            支付订单
	 * @return
	 */
	public ALiPayOrderResultDto payAli(int userId, String orderSnMain) {
		RechargePayContext context = new RechargePayContext(userId, orderSnMain,getDaoProcessor);
		if (context.init()) {
			context.recordAction(OrderActionTypeEnum.TYPE_CHOOSE_PAY,
					"选择支付方式在线支付");
			// 如果相继支付成功
			return aliPay.preparePay(context);
		} else {
			logger.error(String.format(
					"payAli fail user_id[%d] order_sn_main[%s]", userId,
					orderSnMain));
			return null;
		}
	}

	/**
	 * 完成充值订单
	 * 把支付金额分配到各子单
	 * 修改支付单paysign状态
	 * @param paymentEnum
	 * @param totalFee
	 * @param orderSnMain
	 * @return
	 */
	public boolean finishRecharge(PaymentEnum paymentEnum, double totalFee,
			String orderSnMain) {
		RechargePayContext context = new RechargePayContext(0, orderSnMain,getDaoProcessor);
		if (!context.init()) {
			logger.error(String
					.format("finishRecharge fail to init order_sn_main[%s] total_fee[%f]",
							orderSnMain, totalFee));
			return false;
		}
		//通过context完成支付单子
		return context.finishPayment(paymentEnum, totalFee);
	}
}
