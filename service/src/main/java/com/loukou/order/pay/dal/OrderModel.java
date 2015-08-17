package com.loukou.order.pay.dal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.util.DoubleUtils;

public class OrderModel {

	private final Logger logger = Logger.getLogger(OrderModel.class);

	private Order order;
	private OrderDao orderDao;
	private OrderPayDao orderPayDao;
	private List<OrderPay> pays = null;

	public OrderModel(Order order,OrderDao orderDao,OrderPayDao orderPayDao) {
		this.order = order;
		this.orderDao = orderDao;
		this.orderPayDao = orderPayDao;
		pays = orderPayDao.findByOrderId(order.getOrderId());
		if (pays == null) {
			pays = new ArrayList<OrderPay>();
		}
	}

	/**
	 * 计算本子单的待支付金额
	 * 
	 * @return
	 */
	public double getAmountToPay() {
		if (order == null) {
			return 0;
		}
		double oneToPay = DoubleUtils.add(order.getGoodsAmount(),
				order.getShippingFee());
		oneToPay = DoubleUtils.sub(oneToPay, order.getOrderPayed());
		if (oneToPay < 0) {
			logger.warn(String.format(
					"order has negative to pay amount order_id[%d] to_pay[%f]",
					order.getOrderId(), oneToPay));
			oneToPay = 0;
		}
		return oneToPay;
	}

	/**
	 * 按指定支付方式用可用金额available去支付本子单
	 * 
	 * @param payment
	 * @param available
	 * @return 返回剩余金额，如果是负值表示出错
	 */
	// TODO:事务
	public double consume(PaymentEnum payment, double available) {
		//计算可支付金额
		double toPay = getAmountToPay();
		double canPay = available >= toPay ? toPay : available;
		if (addOrderPay(payment, canPay) && updateOrderPayed(canPay, payment)) {
			logger.info(String.format(
					"consume done to finish pay order_id[%d] payment_id[%d] amount[%f]",
					order.getOrderId(), payment.getId(), canPay));
			return DoubleUtils.sub(available, canPay);
		} else {
			return -1;
		}
	}

	/**
	 * 记录支付
	 * @param payment 需要记录的支付类型
	 * @param amount 需要记录的支付金额
	 * @return
	 */
	//TODO：事务
	private boolean addOrderPay(PaymentEnum payment, double amount) {
		//未存在该支付类型的记录
		OrderPay orderPay = new OrderPay();
		orderPay.setOrderSnMain(order.getOrderSnMain());
		orderPay.setOrderId(order.getOrderId());
		orderPay.setPaymentId(payment.getId());
		orderPay.setMoney(amount);
		orderPay.setPayTime(new Date().getTime()/1000);
		orderPay.setMobil(null);
		orderPay.setPwd(null);
		orderPay.setMobileOrderId(null);
		orderPay.setStatus(OrderPayStatusEnum.STATUS_SUCC.getStatus());
		//保存到数据库
		OrderPay saved = orderPayDao.save(orderPay);
		if (saved  != null) {
			//同时保持在本地
			pays.add(orderPay); 
			logger.info(String.format(
					"addOrderPay done to insert order_id[%d] payment_id[%d] amount[%f]",
					order.getOrderId(), payment.getId(), amount));
			return true;
		} else {
			logger.error(String.format(
					"addOrderPay fail to insert order_id[%d] payment_id[%d] amount[%f]",
					order.getOrderId(), payment.getId(), amount));
			return false;
		}
	}

	/**
	 * 根据已支付金额计算订单当前状态
	 * 并更新状态和order_payed
	 * @param amount 已支付的金额
	 * @param payment 
	 * @return
	 */
	//TODO:事务
	private boolean updateOrderPayed(double amount, PaymentEnum payment) {
		//订单总共已付
		double paid = DoubleUtils.add(order.getOrderPayed(), amount);
		double allNeed = DoubleUtils.add(order.getGoodsAmount(), order.getShippingFee());
		// 0未付款 1已付款 2部分付款，整单待支付
		if (paid >= allNeed) {
			order.setPayStatus(1);
			order.setPayId(payment.getId());
			order.setPayName(payment.getName());
			order.setPayTime((int) (new Date().getTime()/1000));
		} else if (paid > 0) {
			order.setPayStatus(2);
		} else {
			order.setPayStatus(0);
		}
		order.setOrderPayed(paid);
		//保存到数据库
		Order saved = orderDao.save(order);
		if (saved  != null) {
			//同时保持在本地
			order = saved;
			logger.info(String.format(
					"updateOrderPayed done to update order_id[%d] amount[%f]",
					order.getOrderId(), amount));
			return true;
		} else {
			logger.error(String.format(
					"updateOrderPayed fail to update order_id[%d] amount[%f]",
					order.getOrderId(), amount));
			return false;
		}
	}
	
	public List<OrderPay> getPays() {
		return pays;
	}
}
