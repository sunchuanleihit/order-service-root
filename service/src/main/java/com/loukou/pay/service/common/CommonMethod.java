package com.loukou.pay.service.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderPaySignDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.OrderPayStatusEnum;
import com.loukou.order.service.enums.PayStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.impl.OrderModels;
import com.loukou.order.service.util.DoubleUtils;

public class CommonMethod {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderPayDao orderPayDao;
	
	@Autowired
	private OrderPaySignDao orderPaySignDao;

	private static CommonMethod commonMethod = new CommonMethod();

	// 单例
	private CommonMethod() {

	}

	public static CommonMethod getCommonMethod() {
		return commonMethod;
	}

	public void distributePayedMoneyToOrder(double paid, PayReqContent content) {
		// 把成功支付的金额分配到各子单
		double left = paid;

		for (OrderModels model : content.getAllModels()) {
			// 确定需要支付金额和可以支付金额
			// 子订单需要支付的金额
			double oneToPay = DoubleUtils.add(
					model.getOrder().getGoodsAmount(), model.getOrder()
							.getShippingFee());
			if (oneToPay == 0) {
				continue;
			}

			if (left >= oneToPay) {
				updateOrderPayed(model.getOrder().getOrderId(), oneToPay,
						PayStatusEnum.STATUS_PAYED.getId());
			} else {
				updateOrderPayed(model.getOrder().getOrderId(), oneToPay,
						PayStatusEnum.STATUS_PART_PAYED.getId());
			}
			double onePaid = (left > oneToPay ? oneToPay : left);
			// 更新余额
			left = DoubleUtils.sub(left, onePaid);
			if (onePaid > 0) {
				// 支付成功，起码是部分成功
				insertOrderPay(onePaid,
						OrderPayStatusEnum.STATUS_SUCC.getStatus(), model
								.getOrder().getOrderId(),
						content.getOrderSnMain(), PaymentEnum.PAY_TXK.getId());
			} else {
				// 支付失败
				insertOrderPay(onePaid,
						OrderPayStatusEnum.STATUS_CANCEL.getStatus(), model
								.getOrder().getOrderId(),
						content.getOrderSnMain(), PaymentEnum.PAY_TXK.getId());
			}
		}
	}

	public Order updateOrderPayed(int orderId, double payedMoney, int status) {

		return orderDao.updateOrderPayedAndStatus(orderId, payedMoney, status);
	}

	// 插入一条新的orderPay记录
	public OrderPay insertOrderPay(double onePaid, String status, int orderId,
			String orderSnMain, int paymentId) {
		OrderPay vaPay = new OrderPay();
		vaPay.setMoney(onePaid);
		vaPay.setStatus(status);
		vaPay.setOrderId(orderId);
		vaPay.setOrderSnMain(orderSnMain);
		vaPay.setPaymentId(paymentId);
		vaPay.setPayTime(new Date().getTime() / 1000);
		return orderPayDao.save(vaPay);
	}
	
	/**
	 * 
	 * @param userId
	 * @param orderSnMain
	 * @return 构建微信支付请求体
	 */
	public PayReqContent getPayReqContent(int userId, String orderSnMain) {
		PayReqContent content = new PayReqContent();
		content.setUserId(userId);
		List<OrderModels> orderModelsList = fillOrderModels(orderSnMain);
		content.setAllModels(orderModelsList);
		content.setNeedToPay(getTotalFee(orderModelsList));
		content.setOrderSnMain(orderSnMain);
		return content;
	}

	/**
	 * 
	 * @param orderModelsList
	 * @return 需要支付的总金额
	 */
	public double getTotalFee(List<OrderModels> orderModelsList) {
		// 计算需要支付的总额
		double toPay = 0;
		for (OrderModels orderModels : orderModelsList) {
			toPay = DoubleUtils.add(orderModels.getOrder().getGoodsAmount(),
					toPay);
			toPay = DoubleUtils.add(toPay, orderModels.getOrder()
					.getShippingFee());
		}
		double payedMoney = 0;
		for (OrderModels model : orderModelsList) {
			for (OrderPay pay : model.getPays()) {
				if (StringUtils.equals(pay.getStatus(),
						OrderPayStatusEnum.STATUS_SUCC.getStatus())) {
					payedMoney = DoubleUtils.add(pay.getMoney(), toPay);
				}
			}
		}
		toPay = DoubleUtils.sub(toPay, payedMoney);
		return toPay;
	}

	/**
	 * 
	 * @param orderSnMain
	 * @return 构建orderModels
	 */
	public List<OrderModels> fillOrderModels(String orderSnMain) {
		List<OrderModels> orderModelsList = new ArrayList<OrderModels>();
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		if (CollectionUtils.isEmpty(orders)) {
			return null;
		}
		for (Order order : orders) {
			OrderModels orderModels = new OrderModels();
			orderModels.setOrder(order);
			List<OrderPay> orderPayList = orderPayDao.findByOrderId(order
					.getOrderId());
			orderModels.setPays(orderPayList);
			orderModelsList.add(orderModels);
		}

		return orderModelsList;
	}


}
