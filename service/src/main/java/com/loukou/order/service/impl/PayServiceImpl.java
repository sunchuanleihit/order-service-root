package com.loukou.order.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.pay.processor.OrderPayProcessor;
import com.loukou.order.pay.processor.RechargePayProcessor;
import com.loukou.order.service.api.PayService;
import com.loukou.order.service.dao.OrderPaySignDao;
import com.loukou.order.service.entity.OrderPaySign;
import com.loukou.order.service.enums.FinishPayResultEnum;
import com.loukou.order.service.enums.OrderPayTypeEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.resp.dto.ALiPayOrderRespDto;
import com.loukou.order.service.resp.dto.ALiPayOrderResultDto;
import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;
import com.loukou.order.service.resp.dto.WeixinPayOrderRespDto;
import com.loukou.order.service.resp.dto.WeixinPayOrderResultDto;

@Service("payService")
public class PayServiceImpl implements PayService {

	private final Logger logger = Logger.getLogger(PayServiceImpl.class);

	@Autowired
	protected OrderPaySignDao orderPaySignDao;
	
	@Autowired
	private OrderPayProcessor orderPayProcessor;

	@Autowired
	private RechargePayProcessor rechargePayProcessor;

	@Override
	public AbstractPayOrderRespDto payOrder(int userId, int payType,
			int paymentId, String orderSnMain, int isTaoxinka, int isVcount) {
		AbstractPayOrderRespDto resp = null;
		//不支持货到付款，只支持在线支付
		if (payType != OrderPayTypeEnum.TYPE_ONLINE.getId()) {
			logger.error(String.format("payOrder unsupported paytype[%d]", payType));
			resp = new AbstractPayOrderRespDto();
			resp.setCode(400);
			resp.setMessage("不支持货到付款");
			return resp;
		}
		//在线支付
		switch (PaymentEnum.parseType(paymentId)) {
		
			case PAY_APP_WEIXIN:
				WeixinPayOrderRespDto respWx = new WeixinPayOrderRespDto();
				WeixinPayOrderResultDto resultWx = null;
				if (StringUtils.startsWithIgnoreCase(orderSnMain, "CZR")) {
					//充值订单
					resultWx = rechargePayProcessor.payWx(userId, orderSnMain);
				} else {
					//普通订单
					resultWx = orderPayProcessor.payWx(userId,
						orderSnMain, isVcount != 0, isTaoxinka != 0);
				}
				if (resultWx == null) {
					respWx.setCode(400);
				} else {
					respWx.setCode(200);
					respWx.setResult(resultWx);
				}
				resp = respWx; 
				break;
				
			case PAY_ALI:
				ALiPayOrderRespDto respAli = new ALiPayOrderRespDto();
				ALiPayOrderResultDto resultAli = null;
				if (StringUtils.startsWithIgnoreCase(orderSnMain, "CZR")) {
					//充值订单
					resultAli = rechargePayProcessor.payAli(userId, orderSnMain);
				} else {
					//普通订单
					resultAli = orderPayProcessor.payAli(userId,
						orderSnMain, isVcount != 0, isTaoxinka != 0);
				}
				if (resultAli == null) {
					respAli.setCode(400);
				} else {
					respAli.setCode(200);
					respAli.setResult(resultAli);
				}
				resp = respAli;
				break;
				
			default:
				resp = new AbstractPayOrderRespDto();
				resp.setCode(400);
		}
		return resp;
	}

	/**
	 * 实现支付回调逻辑
	 */
	@Override
	public int finishOrderPay(int paymentId, double totalFee,
			String orderSnMain) {
		if (!verifyRechargePayInfo(paymentId, totalFee, orderSnMain)) {
			return FinishPayResultEnum.RESULT_VERIFY_FAILED.getId();
		}
		PaymentEnum paymentEnum = PaymentEnum.parseType(paymentId);
		//在线支付
		switch (paymentEnum) {
				
			case PAY_APP_WEIXIN:
			case PAY_ALI:
				if (StringUtils.startsWithIgnoreCase(orderSnMain, "CZR")) {
					if(!rechargePayProcessor.finishRecharge(paymentEnum, totalFee, orderSnMain)) {
						return FinishPayResultEnum.RESULT_INTERNAL_ERROR.getId();
					}
				} else {
					if (!orderPayProcessor.finishOrder(paymentEnum, totalFee, orderSnMain)) {
						return FinishPayResultEnum.RESULT_INTERNAL_ERROR.getId();
					}
				}
				return FinishPayResultEnum.RESULT_SUCC.getId();
//				break;
				
			default:
				return FinishPayResultEnum.RESULT_UNSUPPORT_PAYMENT.getId();
//				break;
		}
	}
	
	
	private boolean verifyRechargePayInfo(int paymentId, double totalFee,
			String orderSnMain) {
		OrderPaySign paySign = orderPaySignDao.findByOrderSnMainAndPayId(
				orderSnMain, paymentId);
		//支付单要有的吧
		if (paySign == null) {
			logger.error(String
					.format("verifyRechargePayInfo fail to find paysign order_sn_main[%s] payment_id[%d]",
							orderSnMain, paymentId));
			return false;
		}
		//金额要一致吧
		if (paySign.getMoney() != totalFee) {
			logger.error(String
					.format("verifyRechargePayInfo paysign money not match order_sn_main[%s] payment_id[%d] total_fee[%f] money[%f]",
							orderSnMain, paymentId, totalFee,
							paySign.getMoney()));
			return false;
		}
		return true;
	}
}
