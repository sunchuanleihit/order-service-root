package com.loukou.order.service.api;

import com.loukou.order.service.resp.dto.AbstractPayOrderRespDto;
import com.loukou.order.service.resp.dto.OrderCancelRespDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShippingResultDto;

public interface OrderService {
	
	/**
	 * 
	 * @param userId,//用户ID
	 * @param flag ,//flag 1:全部 2:待付款 3:待收货 4:退货
	 * @return 订单列表
	 */
	public OrderListRespDto getOrderList(int userId, int flag);
	
	/**
	 * 
	 * @param cityId 城市ID
	 * @param userId 用户ID
	 * @param storeId 店铺ID
	 * @param openId 唯一设备号
	 */
//	public CouponListRespDto getCouponList(int cityId, int userId, int storeId, String openId);
//	
	
	/**
	 * 
	 * @param userId 用户ID
	 * @param orderSnMain 订单ID
	 * @param flag flag 1:全部 2:待付款 3:待收货 4:退货
	 * @return 订单详情
	 */
	public OrderListRespDto getOrderInfo(int userId, String orderSnMain, int flag);
	

	/**
	 * 
	 * @param userId
	 * @param orderSnMain
	 * @return 取消订单
	 */
	public OrderCancelRespDto cancelOrder(int userId, String orderSnMain);

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
	 * @param userId 用户ID
	 * @param orderSnMain 订单ID
	 * @return 支付页订单详情
	 */
	public PayOrderResultRespDto getPayOrderMsg(int userId, String orderSnMain);
	
	/**
	 * 
	 * @param shippingResultDto 物流详情
	 * @param taoOrderSn 订单ID
	 * @return 物流详情
	 */
	public ShippingResultDto getShippingResult(ShippingResultDto shippingResultDto, String taoOrderSn);
	
	/**
	 * 
	 * @param orderSnMain
	 * @return 下单后分享
	 */
	public ShareRespDto shareAfterPay(String orderSnMain);

}
