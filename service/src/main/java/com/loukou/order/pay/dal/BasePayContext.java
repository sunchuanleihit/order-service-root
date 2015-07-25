package com.loukou.order.pay.dal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderPaySignDao;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderPaySign;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.PaySignStatusEnum;

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

	@Autowired
	protected MemberDao memberDao;

	@Autowired
	protected OrderPaySignDao orderPaySignDao;

	@Autowired
	protected OrderActionDao orderActionDao;

	public BasePayContext(int userId, String orderSnMain) {
		this.userId = userId;
		this.orderSnMain = orderSnMain;
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
		String outTradeNo = generateTradeNo();
		int retry = 10;
		while (retry > 0
				&& orderPaySignDao.findByOutOrderSn(outTradeNo) != null) {
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

	public boolean init() {
		return false;
	}
}