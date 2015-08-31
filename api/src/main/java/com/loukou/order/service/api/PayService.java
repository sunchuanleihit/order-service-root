package com.loukou.order.service.api;

import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;

public interface PayService {


	/**
	 * 
	 * @param userId
	 * @param payType 1:货到付款，2:在线支付
	 * @param paymentId 4:支付宝，207:微信支付
	 * @param orderSnMain
	 * @param isTaoxinka 1:是，0:否
	 * @param isVcount 1:是，0:否
	 * @return
	 */
	public AbstractPayOrderRespDto payOrder(int userId, int payType, int paymentId, String orderSnMain, 
			int isTaoxinka, int isVcount);
	

	/**
	 * 完成在线支付回调的订单支付逻辑
	 * @param orderSnMain 
	 * @param paymentId 支付完成方式 4:支付宝 207:微信
	 * @param totalFee 支付完成金额
	 * @return 参考FinishPayResultEnum
	 */
	public int finishOrderPay(int paymentId, double totalFee,
			String orderSnMain);

}
