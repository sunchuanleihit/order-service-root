package com.loukou.order.pay.dal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.loukou.order.pay.processor.GetDaoProcessor;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.util.DoubleUtils;

public class OrderPayContext extends BasePayContext {

	private final Logger logger = Logger.getLogger(OrderPayContext.class);

	private OrderDao orderDao;

	private OrderPayDao orderPayDao;
	
	//子单列表
	private List<OrderModel> allModels = null;

	/**
	 * @param userId 可能为空，因为支付完成时不需要user信息
	 * @param orderSnMain
	 */
	public OrderPayContext(int userId, String orderSnMain,GetDaoProcessor getDaoProcessor) {
		super(userId, orderSnMain,getDaoProcessor);
		this.orderDao = getDaoProcessor.getOrderDao();
		this.orderPayDao = getDaoProcessor.getOrderPayDao();
	}

	/**
	 * 初始化支付订单上下文
	 * 包括加载订单所有支付相关的信息
	 * @return
	 */
	@Override
	public boolean init() {
		allModels = new ArrayList<OrderModel>();
		List<Order> orders = orderDao.findByOrderSnMain(getOrderSnMain());
		if (CollectionUtils.isEmpty(orders)) {
			return false;
		}
		for (Order order : orders) {
			OrderModel oneModel = new OrderModel(order,orderDao,orderPayDao);
			allModels.add(oneModel);
		}
		return true;
	}
	
	/**
	 * 计算普通订单的待支付金额
	 * 所有子单待支付金额之和
	 */
	@Override
	public double getAmountToPay() {
		double allToPay = 0;
		if (allModels != null) {
			for (OrderModel model : allModels) {
				allToPay = DoubleUtils.add(allToPay, model.getAmountToPay());
			}
		}
		return allToPay;
	}

	/**
	 * 按指定支付类型分配支付金额到各子单
	 * 普通子单才有的操作 充值订单没有
	 * @param payment 支付类型 
	 * @param available 可分配金额
	 * @return 剩余的金额，负数表示失败
	 */
	//TODO:事务
	public double consume(PaymentEnum payment, double available) {
		double left = available;	// 剩余
		if (allModels != null && available > 0) {
			double totalToPay = getAmountToPay();
			int size = allModels.size();
			for (int i = 0; i < size; i++) {
				OrderModel oneModel = allModels.get(i);
				double toConsume = 0;
				if (i == size-1) {
					// 如果是最后一个子单
					toConsume = left;
					left = 0;
				}
				else {
					// 支付金额拆到每个子单
					double orderAmount = oneModel.getAmountToPay();
					toConsume = DoubleUtils.mul(DoubleUtils.div(orderAmount, totalToPay), available, 2);
					left = DoubleUtils.sub(left, toConsume);
				}
				
				if (toConsume <= 0) {
					logger.warn("consumer get toConsume <= 0");
					continue;
				}
				double ret = oneModel.consume(payment, toConsume);
				if (ret < 0) {
					logger.warn(String.format(
							"consume faile to pay order_sn_main[%s] payment_id[%d]",
							getOrderSnMain(), payment.getId()));
					return -1;
				}
			}
		}
		return left;
	}
	
	/**
	 * 是否用某种支付方式支付过，比如 账号余额、淘心卡 等
	 * @param paymentEnum
	 * @return
	 */
	public boolean isPaid (PaymentEnum paymentEnum) {
		for (OrderModel m: allModels) {
			List<OrderPay> orderPays = m.getPays();
			for (OrderPay orderPay: orderPays) {
				if (orderPay.getPaymentId() == paymentEnum.getId()) {
					return true;
				}
			}
		}
		return false;
	}
}
