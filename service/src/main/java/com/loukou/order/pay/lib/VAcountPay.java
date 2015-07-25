package com.loukou.order.pay.lib;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.pay.common.CommonMethod;
import com.loukou.order.pay.common.PayReqContent;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;
import com.loukou.pos.client.vaccount.resp.VaccountUpdateRespVO;
import com.loukou.pos.client.vaccount.resp.VaccountUpdateRespVO.Code;

public class VAcountPay {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderPayDao orderPayDao;

	@Autowired
	private OrderActionDao orderActionDao;

	// 对订单进行虚拟账户支付, 返回是否还需要支付
	public PayReqContent payVaccount(PayReqContent content) {
		// 需要支付的总额
		double toPay = content.getNeedToPay();

		// 支付主订单, orderId=0
		double paid = makeVaPay(content.getUserId(), toPay, 0, content.getOrderSnMain(), true);
		
		// 把成功支付的金额分配到各子单
		CommonMethod common = CommonMethod.getCommonMethod();
		common.distributePayedMoneyToOrder(paid, content);
		content.setNeedToPay(DoubleUtils.sub(toPay, paid));
		return content;
	}
	

	private double makeVaPay(int userId, double amount, int orderId,
			String orderSnMain, boolean trySufficient) {
		double paid = 0;
		VaccountUpdateRespVO resp = VirtualAccountProcessor.getProcessor()
				.consume(userId, amount, orderId, orderSnMain);
		if (resp != null) {
			if (StringUtils.endsWithIgnoreCase(resp.getCode(), Code.SUCCESS)) {
				paid = amount;
				logger.info(String.format(
						"makeVaccountPay done order[%s] user[%d] amount[%f]",
						orderSnMain, userId, amount));
			} else if (StringUtils.endsWithIgnoreCase(resp.getCode(),
					Code.INSUFFICIENT) && trySufficient) {
				double balance = VirtualAccountProcessor.getProcessor()
						.getVirtualBalanceByUserId(userId);
				logger.info(String
						.format("makeVaccountPay insufficient order[%s] user[%d] amount[%f] available[%f]",
								orderSnMain, userId, amount, balance));
				if (balance > 0) {
					return makeVaPay(userId, balance, orderId, orderSnMain,
							false);
				} else {
					logger.info(String.format(
							"amount balance zero order[%s] user[%d]",
							orderSnMain, userId));
				}
			}
		} else {
			logger.info("failed to pay");
		}
		return paid;
	}

}
