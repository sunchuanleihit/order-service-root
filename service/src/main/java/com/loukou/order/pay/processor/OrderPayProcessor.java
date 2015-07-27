package com.loukou.order.pay.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.pay.dal.OrderPayContext;
import com.loukou.order.pay.impl.AliPay;
import com.loukou.order.pay.impl.TXKPay;
import com.loukou.order.pay.impl.VAcountPay;
import com.loukou.order.pay.impl.WeiXinPay;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.resp.dto.ALiPayOrderResultDto;
import com.loukou.order.service.resp.dto.WeixinPayOrderResultDto;

/**
 * 负责普通订单的微信支付和支付宝支付优先级等业务逻辑
 * 
 * @author nonggia
 *
 */
public class OrderPayProcessor {

	private final Logger logger = Logger.getLogger(OrderPayProcessor.class);

	@Autowired
	private VAcountPay vaPay;

	@Autowired
	private TXKPay txkPay;

	@Autowired
	private AliPay aliPay;

	@Autowired
	private WeiXinPay wxPay;
	
	/**
	 * 支付宝支付普通订单
	 * 
	 * @param userId
	 *            支付用户的id
	 * @param orderSnMain
	 *            主订单号
	 * @param useVcount
	 *            是否使用虚拟账户
	 * @param useTxk
	 *            是否使用掏心卡
	 * @return null－失败 非null－成功
	 */
	public ALiPayOrderResultDto payAli(int userId, String orderSnMain,
			boolean useVcount, boolean useTxk) {
		OrderPayContext context = new OrderPayContext(userId, orderSnMain);
		if (context.init() && 
				(!useVcount || vaPay.pay(context)) && 
				(!useTxk || txkPay.pay(context))) {
			//如果相继支付成功
			return aliPay.preparePay(context);
		} else {
			logger.error(String
					.format("payAli fail user_id[%d] order_sn_main[%s] use_va[%s] use_txk[%s]",
							userId, orderSnMain, String.valueOf(useVcount),
							String.valueOf(useTxk)));
			return null;
		}
	}

	/**
	 * 微信支付普通订单
	 * 
	 * @param userId
	 *            支付用户的id
	 * @param orderSnMain
	 *            主订单号
	 * @param useVcount
	 *            是否使用虚拟账户
	 * @param useTxk
	 *            是否使用掏心卡
	 * @return null－失败 非null－成功
	 */
	public WeixinPayOrderResultDto payWx(int userId, String orderSnMain,
			boolean useVcount, boolean useTxk) {
		OrderPayContext context = new OrderPayContext(userId, orderSnMain);
		if (context.init() && 
				(!useVcount || vaPay.pay(context)) && 
				(!useTxk || txkPay.pay(context))) {
			//如果相继支付成功
			return wxPay.preparePay(context);
		} else {
			logger.error(String
					.format("payWx fail user_id[%d] order_sn_main[%s] use_va[%s] use_txk[%s]",
							userId, orderSnMain, String.valueOf(useVcount),
							String.valueOf(useTxk)));
			return null;
		}
	}

	/**
	 * 完成普通订单 
	 * 把支付金额分配到各子单
	 * 修改支付单paysign状态
	 * @param paymentEnum
	 * @param totalFee
	 * @param orderSnMain
	 * @return
	 */
	public boolean finishOrder(PaymentEnum paymentEnum, double totalFee, String orderSnMain) {
		//注意⚠这里使用userid=0
		OrderPayContext context = new OrderPayContext(0, orderSnMain);
		if (!context.init()) {
			logger.error(String
					.format("finishOrder fail to init order_sn_main[%s] total_fee[%f]",
							orderSnMain, totalFee));
			return false;
		}
		//通过context完成支付单子
		return context.finishPayment(paymentEnum, totalFee);
	}
}
