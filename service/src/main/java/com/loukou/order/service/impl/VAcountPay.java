package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.resp.dto.ResponseCodeDto;
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

	class OrderModels {

		private Order order;
		private List<OrderPay> pays = new ArrayList<OrderPay>();
		private List<OrderGoods> goods = new ArrayList<OrderGoods>();

		public List<OrderGoods> getGoods() {
			return goods;
		}

		public List<OrderPay> getPays() {
			return pays;
		}

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
		}

		public OrderPay findPay(PaymentEnum payment) {
			for (OrderPay pay : pays) {
				if (pay.getPaymentId() == payment.getId()) {
					return pay;
				}
			}
			return null;
		}

		public OrderPay findOrCreatePay(PaymentEnum payment) {
			OrderPay payRet = null;
			for (OrderPay pay : pays) {
				if (pay.getPaymentId() == payment.getId()) {
					payRet = pay;
					break;
				}
			}
			if (payRet == null) {
				payRet = new OrderPay();
				payRet.setOrderSnMain(order.getOrderSnMain());
				payRet.setOrderId(order.getOrderId());
				payRet.setPaymentId(payment.getId());
				payRet.setMoney(0);
				payRet.setPayTime(order.getAddTime());
				payRet.setMobil(null);
				payRet.setPwd(null);
				payRet.setMobileOrderId(null);
				payRet.setStatus(OrderPayStatusEnum.STATUS_PROGRESS.getStatus()); // 都处于待支付状态
				pays.add(payRet); // 未保存到数据库
			}
			return payRet;
		}

		/*
		 * 更新订单支付相关状态
		 */
		public void updateOrderAndPay() {
			double paid = 0;
			for (OrderPay pay : pays) {
				if (StringUtils.equalsIgnoreCase(pay.getStatus(),
						OrderPayStatusEnum.STATUS_SUCC.getStatus())) {
					paid = DoubleUtils.add(paid, pay.getMoney());
				}
			}
			if (paid >= order.getOrderAmount()) { // 无物流费用
				order.setPayStatus(1); // 0未付款1已付款2部分付款，整单待支付
				order.setStatus(OrderStatusEnum.STATUS_FINISHED.getId());
			} else if (paid > 0) {
				order.setPayStatus(2);
			} else {
				order.setPayStatus(0);
			}
			order.setOrderPayed(paid);
			orderDao.save(order);
			orderPayDao.save(pays);
		}

		/*
		 * 插入order
		 */
		public boolean insertOrderIfNotExists(Order order) {
			List<Order> ordersExisted = orderDao.findByOrderSnMainAndSellerId(
					order.getOrderSnMain(), order.getSellerId());
			Order orderExisted = null;
			if (CollectionUtils.isNotEmpty(ordersExisted)) {
				orderExisted = ordersExisted.get(0);
			}

			if (orderExisted == null) {
				Order orderInserted = orderDao.save(order);
				this.order = orderInserted;
			} else {
				this.order = orderExisted;
			}
			return true;
		}

		/*
		 * 插入action记录
		 */
		public boolean insertOrderActionIfNotExist(OrderActionTypeEnum e) {
			List<OrderAction> actionsExisted = orderActionDao
					.findByOrderSnMainAndAction(order.getOrderSnMain(),
							e.getId());
			if (!CollectionUtils.isEmpty(actionsExisted)) {
				return true;
			}
			// 准备order_action
			OrderAction action = new OrderAction();
			action.setAction(e.getId()); // 先下单为审核状态
			action.setOrderSnMain(order.getOrderSnMain());
			action.setTaoOrderSn(order.getOrderSnMain()); // 包裹单
			action.setOrderId(order.getOrderId());
			action.setActor(null);
			action.setActionTime(order.getTimestamp());
			action.setTimestamp(null);
			orderActionDao.save(action);

			return true;
		}

		public void updateTaoOrderSn() {
			order.setTaoOrderSn(order.getOrderSnMain());
			orderDao.save(order);
		}
	}

	// 对订单进行虚拟账户支付
	private boolean payVaccount(int userId, String orderSnMain,
			List<OrderModels> allModels, ResponseCodeDto response) {

		// 计算需要支付的总额
		double toPay = 0;
		for (OrderModels model : allModels) {
			OrderPay vaPay = model.findPay(PaymentEnum.PAY_VACOUNT);
			if (vaPay != null && vaPay.getMoney() > 0) {
				toPay = DoubleUtils.add(vaPay.getMoney(), toPay);
			}
		}
		if (toPay == 0) {
			return true;
		}
		// 支付主订单, orderId=0
		double paid = makeVaPay(userId, toPay, 0, orderSnMain, true, response);
		if (response.getCode() != 200) {
			return false;
		}
		// 把成功支付的金额分配到各子单
		double left = paid;
		for (OrderModels model : allModels) {
			// 确定需要支付金额和可以支付金额
			OrderPay vaPay = model.findPay(PaymentEnum.PAY_VACOUNT);
			double oneToPay = (vaPay != null ? vaPay.getMoney() : 0);
			if (oneToPay == 0) {
				continue;
			}
			double onePaid = (left > oneToPay ? oneToPay : left);
			// 更新余额
			left = DoubleUtils.sub(left, onePaid);
			if (onePaid > 0) {
				// 支付成功，起码是部分成功
				vaPay.setMoney(onePaid);
				vaPay.setStatus(OrderPayStatusEnum.STATUS_SUCC.getStatus());
			} else {
				// 支付失败
				vaPay.setStatus(OrderPayStatusEnum.STATUS_CANCEL.getStatus());
			}
			// 如果是部分成功或者失败，吧剩余金额转为现金
			if (onePaid < oneToPay) {
				OrderPay cashPay = model.findOrCreatePay(PaymentEnum.PAY_CASH);
				cashPay.setMoney(DoubleUtils.add(cashPay.getMoney(),
						DoubleUtils.sub(oneToPay, onePaid)));
			}
		}
		return true;
	}

	private double makeVaPay(int userId, double amount, int orderId,
			String orderSnMain, boolean trySufficient, ResponseCodeDto response) {
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
							false, response);
				} else {
					logger.info(String.format(
							"amount balance zero order[%s] user[%d]",
							orderSnMain, userId));
				}
			}
		} else {
			response.setCode(500);
			response.setDesc("会员卡余额扣款失败");
		}
		return paid;
	}

}
