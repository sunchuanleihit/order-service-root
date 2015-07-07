package com.loukou.order.service.api;

import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;

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
	public CouponListRespDto getCouponList(int cityId, int userId, int storeId, int openId);
	
	
	/**
	 * 
	 * @param userId 用户ID
	 * @param orderSnMain 订单ID
	 * @param flag flag 1:全部 2:待付款 3:待收货 4:退货
	 * @return 订单详情
	 */
	public OrderListRespDto getOrderInfo(int userId, String orderSnMain, int flag);
}
