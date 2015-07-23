package com.loukou.pay.lib;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pay.service.common.CommonMethod;
import com.loukou.pay.service.common.PayReqContent;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardPayRespVO;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;

public class TXKPay {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderPayDao orderPayDao;

	@Autowired
	private OrderActionDao orderActionDao;

	// 对订单进行taoxinka支付
	public PayReqContent payTXK(PayReqContent content) {
		// 计算需要支付的总额
		double toPay = content.getNeedToPay();
	
		// 支付主订单, orderId=0
		double paid = makeTXKPay(content.getUserId(), toPay, 0, content.getOrderSnMain(), true);
		
		// 把成功支付的金额分配到各子单 
		CommonMethod common = CommonMethod.getCommonMethod();
		common.distributePayedMoneyToOrder(paid, content);
		content.setNeedToPay(DoubleUtils.sub(toPay, paid));
		return content;
	}
	

	private double makeTXKPay(int userId, double amount, int orderId,
			String orderSnMain, boolean trySufficient) {
		double paid = 0;
		
		TxkCardPayRespVO resp = AccountTxkProcessor.getProcessor()
				.consume(orderSnMain,  amount, userId, memberDao.findOne(userId).getUserName());
		if (resp != null) {
			if (resp != null && resp.isSuccess()) {
				paid = amount;
				logger.info(String.format(
						"makeTXKPay done order[%s] user[%d] amount[%f]",
						orderSnMain, userId, amount));
			} else if (trySufficient) {
				double balance = VirtualAccountProcessor.getProcessor()
						.getVirtualBalanceByUserId(userId);
				logger.info(String
						.format("makeTXKPay insufficient order[%s] user[%d] amount[%f] available[%f]",
								orderSnMain, userId, amount, balance));
				if (balance > 0) {
					return makeTXKPay(userId, balance, orderId, orderSnMain,
							false);
				} else {
					logger.info(String.format(
							"amount balance zero order[%s] user[%d]",
							orderSnMain, userId));
				}
			}
		} else {
			logger.info(String.format(
					"failed to pay order[%s] user[%d]",
					orderSnMain, userId));
		}
		return paid;
	}

}
