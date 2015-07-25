package com.loukou.order.pay.dal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.TczcountRechargeDao;
import com.loukou.order.service.entity.TczcountRecharge;
import com.loukou.order.service.enums.RechargePayStatusEnum;

public class RechargePayContext extends BasePayContext {

	private final Logger logger = Logger.getLogger(RechargePayContext.class);

	@Autowired
	private TczcountRechargeDao rechargeDao;

	private TczcountRecharge rechargeOrder = null;

	public RechargePayContext(int userId, String orderSnMain) {
		super(userId, orderSnMain);
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
}
