package com.loukou.order.service.api;

import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;

public interface PayService {


	/**
	 * 
	 * @param userId
	 * @param payType 1:货到付款，2:在线支付
	 * @param paymentId 4:支付宝，207:微信支付
	 * @param orderSnMain
	 * @param isTaoxinka 1:是，2:否
	 * @param isVcount 1:是，2:否
	 * @return
	 */
	public AbstractPayOrderRespDto payOrder(int userId, int payType, int paymentId, String orderSnMain, 
			int isTaoxinka, int isVcount);
	/**
	 * 
	 * @param orderSnMain
	 * @return 下单后分享
	 */
//	public ShareRespDto shareAfterPay(String orderSnMain);

}
