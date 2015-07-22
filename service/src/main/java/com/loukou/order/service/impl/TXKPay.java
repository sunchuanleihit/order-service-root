package com.loukou.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.PayStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.util.DoubleUtils;
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
	public boolean payTXK(int userId, String orderSnMain, List<OrderModels> allModels) {
		boolean needToPay = false;
		// 计算需要支付的总额
		double toPay = 0;
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		for(Order order : orders) {
		//	if(order.getPayId() == PaymentEnum.PAY_VACOUNT.getId()) {
				toPay = DoubleUtils.add(order.getGoodsAmount(), toPay);
				toPay = DoubleUtils.add(toPay, order.getShippingFee());
		//	}
		}
		double payedMoney = 0;
		for (OrderModels model : allModels) {
			for(OrderPay pay : model.getPays()) {
				if(StringUtils.equals(pay.getStatus(), OrderPayStatusEnum.STATUS_SUCC.getStatus())) {
					payedMoney = DoubleUtils.add(pay.getMoney(), toPay);
				}
			}
		}
		toPay = DoubleUtils.sub(toPay, payedMoney);
		if (toPay == 0) {
			needToPay = false;
			return needToPay;
		}

		// 支付主订单, orderId=0
		double paid = makeTXKPay(userId, toPay, 0, orderSnMain, true);
		
		// 把成功支付的金额分配到各子单
		double left = paid;
		
		for (OrderModels model : allModels) {
			// 确定需要支付金额和可以支付金额
			//子订单需要支付的金额
			double oneToPay = DoubleUtils.add(model.getOrder().getGoodsAmount(), model.getOrder().getShippingFee());
			if (oneToPay == 0) {
				continue;
			}
			
			if(left >= oneToPay) {
				updateOrderPayed(model.getOrder().getOrderId(), oneToPay, PayStatusEnum.STATUS_PAYED.getId());
			} else {
				updateOrderPayed(model.getOrder().getOrderId(), oneToPay, PayStatusEnum.STATUS_PART_PAYED.getId());
			}
			double onePaid = (left > oneToPay ? oneToPay : left);
			// 更新余额
			left = DoubleUtils.sub(left, onePaid);
			if (onePaid > 0) {
				// 支付成功，起码是部分成功
				insertOrderPay(onePaid, OrderPayStatusEnum.STATUS_SUCC.getStatus(), model.getOrder().getOrderId(), 
						orderSnMain, PaymentEnum.PAY_TXK.getId());
			} else {
				// 支付失败
				insertOrderPay(onePaid, OrderPayStatusEnum.STATUS_CANCEL.getStatus(), model.getOrder().getOrderId(), 
						orderSnMain, PaymentEnum.PAY_TXK.getId());
				needToPay = true;
				return needToPay;
			}
		}
		return needToPay;
	}
	
	private Order updateOrderPayed(int orderId, double payedMoney, int status) {
		
		return orderDao.updateOrderPayedAndStatus(orderId, payedMoney, status);
	}

	//插入一条新的orderPay记录
	private OrderPay insertOrderPay(double onePaid, String status, int orderId, String orderSnMain, int paymentId) {
		OrderPay vaPay = new OrderPay();
		vaPay.setMoney(onePaid);
		vaPay.setStatus(status);
		vaPay.setOrderId(orderId);
		vaPay.setOrderSnMain(orderSnMain);
		vaPay.setPaymentId(paymentId);
		vaPay.setPayTime(new Date().getTime() / 1000);
		return orderPayDao.save(vaPay);
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
