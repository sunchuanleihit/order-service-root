package com.loukou.pay.service.common;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
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
	
	public void distributePayedMoneyToOrder(double paid, PayReqContent content) {
		// 把成功支付的金额分配到各子单
				double left = paid;
				
				for (OrderModels model : content.getAllModels()) {
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
								content.getOrderSnMain(), PaymentEnum.PAY_TXK.getId());
					} else {
						// 支付失败
						insertOrderPay(onePaid, OrderPayStatusEnum.STATUS_CANCEL.getStatus(), model.getOrder().getOrderId(), 
								content.getOrderSnMain(), PaymentEnum.PAY_TXK.getId());
					}
				}
	}
	
	
	public Order updateOrderPayed(int orderId, double payedMoney, int status) {
		
		return orderDao.updateOrderPayedAndStatus(orderId, payedMoney, status);
	}

	//插入一条新的orderPay记录
	public OrderPay insertOrderPay(double onePaid, String status, int orderId, String orderSnMain, int paymentId) {
		OrderPay vaPay = new OrderPay();
		vaPay.setMoney(onePaid);
		vaPay.setStatus(status);
		vaPay.setOrderId(orderId);
		vaPay.setOrderSnMain(orderSnMain);
		vaPay.setPaymentId(paymentId);
		vaPay.setPayTime(new Date().getTime() / 1000);
		return orderPayDao.save(vaPay);
	}
	
}
