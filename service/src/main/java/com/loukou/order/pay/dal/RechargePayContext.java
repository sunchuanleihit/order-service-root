package com.loukou.order.pay.dal;

import java.util.Date;

import org.apache.log4j.Logger;

import com.loukou.order.pay.processor.GetDaoProcessor;
import com.loukou.order.service.dao.TczcountRechargeDao;
import com.loukou.order.service.entity.TczcountRecharge;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.enums.RechargePayStatusEnum;

public class RechargePayContext extends BasePayContext {

	private final Logger logger = Logger.getLogger(RechargePayContext.class);

	private TczcountRechargeDao rechargeDao;

	private TczcountRecharge rechargeOrder = null;

	public RechargePayContext(int userId, String orderSnMain,GetDaoProcessor getDaoProcessor) {
		super(userId, orderSnMain,getDaoProcessor);
		rechargeDao = getDaoProcessor.getTczcountRechargeDao();
	}

	/**
	 * 初始化支付订单上下文
	 * @return
	 */
	@Override
	public boolean init() {
		rechargeOrder = rechargeDao.findByOrderSnMain(getOrderSnMain());
		if (rechargeOrder != null) {
			logger.debug(String.format(
					"rechargepay done to init order_sn_main[%s] user_id[%d]",
					getOrderSnMain(), getUserId()));
			setUserId(rechargeOrder.getUserId());
			return true;
		} else {
			logger.error(String.format(
					"rechargepay done to init order_sn_main[%s] user_id[%d]",
					getOrderSnMain(), getUserId()));
			return false;
		}
	}

	/**
	 * 获取订单待支付金额
	 */
	@Override
	public double getAmountToPay() {
		//如果已支付完成或者没有这个记录 待支付金额就是0
		return (rechargeOrder == null
				|| rechargeOrder.getStatus() == RechargePayStatusEnum.STATUS_SUCCESS
						.getId() ? 0 : rechargeOrder.getMoney());
	}

	/**
	 * 完成支付
	 * @param paymentEnum
	 * @param totalFee
	 * @return
	 */
	public double consume(PaymentEnum paymentEnum, double totalFee) {
		//支付订单的状态
		if (rechargeOrder.getStatus() != 0) {
			logger.error(String.format(
					"consume invalid recharge status order_sn_main[%s] status[%d] total_fee[%f]",
					getOrderSnMain(), rechargeOrder.getStatus(), totalFee));
			return -1;
		}
		//去充值
		
		//更新充值订单
		rechargeOrder.setFtime((int)(new Date().getTime()/1000));
		rechargeOrder.setReturnMoney(totalFee);
		rechargeOrder.setPayId(paymentEnum.getId());
		rechargeOrder.setStatus(1);
		TczcountRecharge saved = rechargeDao.save(rechargeOrder);
		if (saved == null) {
			logger.error(String.format(
					"consume fail to update recharge order status order_sn_main[%s] status[%d] total_fee[%f]",
					getOrderSnMain(), rechargeOrder.getStatus(), totalFee));
			return -1;
		}
		return 0;
	}
}
