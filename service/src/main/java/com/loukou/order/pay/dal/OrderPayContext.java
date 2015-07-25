package com.loukou.order.pay.dal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.util.DoubleUtils;

public class OrderPayContext extends BasePayContext {

	private final Logger logger = Logger.getLogger(OrderPayContext.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderPayDao orderPayDao;
	
	//子单列表
	private List<OrderModel> allModels = null;

	public OrderPayContext(int userId, String orderSnMain) {
		super(userId, orderSnMain);
	}

	/**
	 * 初始化支付订单上下文
	 * 包括加载订单所有支付相关的信息
	 * @return
	 */
	@Override
	public boolean init() {
		List<OrderModel> allModels = new ArrayList<OrderModel>();
		List<Order> orders = orderDao.findByOrderSnMain(getOrderSnMain());
		if (CollectionUtils.isEmpty(orders)) {
			return false;
		}
		for (Order order : orders) {
			OrderModel oneModel = new OrderModel(order);
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
		if (allModels != null) {
			for (OrderModel oneModel : allModels) {
				available = oneModel.consume(payment, available);
				if (available < 0) {
					logger.info(String.format(
							"consume faile to pay order_sn_main[%s] payment_id[%d]",
							getOrderSnMain(), payment.getId()));
					return -1;
				}
			}
		}
		return available;
	}
}
