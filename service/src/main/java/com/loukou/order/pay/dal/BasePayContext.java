package com.loukou.order.pay.dal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.pay.processor.GetDaoProcessor;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderPaySignDao;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderPaySign;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.PaySignStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;

/**
 * paycontext 用于负责封装与数据库的数据交互 basepaycontext 抽取生产外部交易号/分配订单金额等基本操作
 * 
 * @author nonggia
 *
 */
public abstract class BasePayContext {

	private final Logger logger = Logger.getLogger(BasePayContext.class);

	private int userId;

	private String userName;

	private String orderSnMain;

	protected MemberDao memberDao;

	protected OrderPaySignDao orderPaySignDao;

	protected OrderActionDao orderActionDao;

	public BasePayContext(int userId, String orderSnMain,GetDaoProcessor getDaoProcessor) {
		this.userId = userId;
		this.orderSnMain = orderSnMain;
		memberDao = getDaoProcessor.getMemberDao();
		orderPaySignDao = getDaoProcessor.getOrderPaySignDao();
		orderActionDao = getDaoProcessor.getOrderActionDao();
	}

	public boolean recordAction(OrderActionTypeEnum actionType, String notes) {
		OrderAction action = new OrderAction();
		action.setAction(actionType.getId());
		action.setActionTime(new Date());
		action.setActor(getUserName());
		action.setNotes(notes);
		action.setOrderSnMain(orderSnMain);
		// action.setOrderId(orderId);
		// action.setTaoOrderSn(taoOrderSn);
		if (orderActionDao.save(action) != null) {
			logger.debug(String
					.format("recordAction done to record order_sn_main[%s] action[%d] user_id[%d] notes[%s]",
							orderSnMain, actionType.getId(), userId, notes));
			return true;
		} else {
			logger.error(String
					.format("recordAction fail to record order_sn_main[%s] action[%d] user_id[%d] notes[%s]",
							orderSnMain, actionType.getId(), userId, notes));
			return false;
		}
	}

	/**
	 * 生成外部交易号
	 * 
	 * @param userId
	 *            交易的用户id
	 * @param orderSnMain
	 *            交易的内部单号
	 * @param money
	 *            交易的金额
	 * @param paymentId
	 *            支付方式
	 * @return null－失败 非null－成功
	 */
	// TODO:支持事务
	public String makeOutTrade(int userId, String orderSnMain, double money,
			int paymentId) {
		if (paymentId <= 0 || StringUtils.isBlank(orderSnMain) || userId <= 0) {
			logger.error(String
					.format("makeOutTrade invalid params user_id[%d] order_sn_main[%s] payment_id[%d]",
							userId, orderSnMain, paymentId));
			return null;
		}
		//如果该订单该支付方式已经有对应的记录，使用旧记录
		List<OrderPaySign> orderPaySignList = orderPaySignDao.findByOrderSnMainAndPayId(orderSnMain, paymentId);
		int size = orderPaySignList.size();
		if (size > 0) {
			// 如果paysign status 不是ready，返回失败
			OrderPaySign oldOutTradeNo = orderPaySignList.get(size-1);
			if (!PaySignStatusEnum.STATUS_READY.getStatus().equals(oldOutTradeNo.getStatus())) {
				return null;
			}
			
			if (oldOutTradeNo.getMoney() != money) {
				oldOutTradeNo.setMoney(money);
				orderPaySignDao.save(oldOutTradeNo);
			}
			
			return oldOutTradeNo.getOutOrderSn();
		}
		//如果没有旧记录，则生成(其实不会用了！！！！先留着)
		String outTradeNo = generateTradeNo();
		int retry = 10;
		while (retry > 0
				&& orderPaySignDao.findByOutOrderSn(outTradeNo) != null) {
			outTradeNo = generateTradeNo();
			retry--;
		}
		if (retry == 0) {
			logger.error(String
					.format("makeOutTrade fail to generate uniq out_trade_no user_id[%d] order_sn_main[%s] payment_id[%d]",
							userId, orderSnMain, paymentId));
			return null;
		}
		// 生成数据库记录
		OrderPaySign orderPaySign = new OrderPaySign();
		orderPaySign.setOrderSnMain(orderSnMain);
		orderPaySign.setOutOrderSn(outTradeNo);
		orderPaySign.setPayId(paymentId);
		orderPaySign.setMoney(money);
		orderPaySign.setcTime(new Date().getTime() / 1000);
		orderPaySign.setStatus(PaySignStatusEnum.STATUS_READY.getStatus());
		// 插入记录
		if (orderPaySignDao.save(orderPaySign) != null) {
			logger.info(String
					.format("makeOutTrade done to insert out_trade_no user_id[%d] order_sn_main[%s] payment_id[%d] out_trade_no[%s]",
							userId, orderSnMain, paymentId, outTradeNo));
			return outTradeNo;
		} else {
			logger.error(String
					.format("makeOutTrade fail to insert out_trade_no user_id[%d] order_sn_main[%s] payment_id[%d] out_trade_no[%s]",
							userId, orderSnMain, paymentId, outTradeNo));
			return null;
		}
	}
	

	/**
	 * 完成支付的共同逻辑
	 * @param paymentEnum 支付方式
	 * @param totalFee 支付金额
	 * @return
	 */
	public boolean finishPayment(PaymentEnum paymentEnum, double totalFee) {
		// 计算需要支付的总额
		double needToPay = getAmountToPay();
		if (needToPay <= 0) {
			//如果无需支付
			return true;
		}
		// 把成功支付的金额分配到各子单
		double rest = consume(paymentEnum, totalFee);
		if (rest < 0) { 
			//支付失败
			logger.error(String
					.format("finishPayment fail to pay order_sn_main[%s] total_fee[%f]",
							orderSnMain, totalFee));
			return false;
		} else if (rest > 0) {
			//支付有盈余！！！warn
			logger.warn(String
					.format("finishPayment rest money order_sn_main[%s] total_fee[%f] rest[%f]",
							orderSnMain, totalFee, rest));
		}
		//更新支付订单状态
		if (!finishPaySign(paymentEnum)) {
			logger.error(String.format(
					"finishPayment fail to update recharge order status order_sn_main[%s] total_fee[%f]",
					orderSnMain, totalFee));
			return false;
		}
		return true;
	}
	
	/**
	 * 完成paysign的状态更新
	 * @param payment
	 * @return
	 */
	private boolean finishPaySign(PaymentEnum payment) {
		// 生成数据库记录
		List<OrderPaySign> orderPaySignList = orderPaySignDao.findByOrderSnMainAndPayId(orderSnMain, payment.getId());
		int size = orderPaySignList.size();
		if (size == 0) {
			logger.error(String
					.format("finishPaySign fail to find paysign order_sn_main[%s] payment_id[%d]",
							orderSnMain, payment.getId()));
			return false;
		}
		OrderPaySign orderPaySign = orderPaySignList.get(size-1);
		orderPaySign.setfTime(new Date().getTime() / 1000);
		orderPaySign.setStatus(PaySignStatusEnum.STATUS_SUCC.getStatus());
		// 更新记录
		if (orderPaySignDao.save(orderPaySign) != null) {
			logger.info(String
					.format("finishPaySign done to finish paysign order_sn_main[%s] payment_id[%d]",
							orderSnMain, payment.getId()));
			return true;
		} else {
			logger.error(String
					.format("finishPaySign fail to finish paysign order_sn_main[%s] payment_id[%d]",
							orderSnMain, payment.getId()));
			return false;
		}
	}

	/**
	 * 生成随机的out_trade_no
	 * 
	 * @return
	 */
	private String generateTradeNo() {
		return new SimpleDateFormat("yyyyMM").format(new Date())
				+ StringUtils.leftPad(
						String.valueOf(new Random().nextInt(999999) + 1), 6,
						"0");
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	/**
	 * 获取支付用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		if (StringUtils.isEmpty(userName)) {
			Member m = memberDao.findOne(userId);
			if (m != null) {
				userName = m.getUserName();
			}
		}
		return userName;
	}

	public abstract double getAmountToPay();

	public abstract boolean init();
	
	public abstract double consume(PaymentEnum paymentEnum, double totalFee);
}
