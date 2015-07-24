package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.util.DoubleUtils;

public class OrderModels {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderPayDao orderPayDao;

	@Autowired
	private OrderActionDao orderActionDao;

	private Order order;
	private List<OrderPay> pays = new ArrayList<OrderPay>();
//	private List<OrderGoods> goods = new ArrayList<OrderGoods>();

//	public List<OrderGoods> getGoods() {
//		return goods;
//	}

	public List<OrderPay> getPays() {
		return pays;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	// public OrderPay findPay(PaymentEnum payment) {
	// for (OrderPay pay : pays) {
	// if (pay.getPaymentId() == payment.getId()) {
	// return pay;
	// }
	// }
	// return null;
	// }

	public void setPays(List<OrderPay> pays) {
		this.pays = pays;
	}

//	public void setGoods(List<OrderGoods> goods) {
//		this.goods = goods;
//	}

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

	public OrderPay findPay(PaymentEnum payment) {
		for (OrderPay pay : pays) {
			if (pay.getPaymentId() == payment.getId()) {
				return pay;
			}
		}
		return null;
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
				.findByOrderSnMainAndAction(order.getOrderSnMain(), e.getId());
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
