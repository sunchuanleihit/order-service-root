package com.loukou.order.service.api;

import java.util.List;
import java.util.Map;

import com.loukou.order.service.req.dto.OrderListParamDto;
import com.loukou.order.service.req.dto.ReturnStorageReqDto;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.LkStatusItemDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderBonusRespDto;
import com.loukou.order.service.resp.dto.OrderCancelRespDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.OrderListInfoDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.OrderStatusCountRespDto;
import com.loukou.order.service.resp.dto.PayBeforeRespDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ResponseDto;
import com.loukou.order.service.resp.dto.ReturnStorageRespDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShippingMsgRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;
import com.loukou.order.service.resp.dto.UserOrderNumRespDto;
import com.loukou.order.service.resp.dto.basic.RespDto;

public interface OrderService {
	
	public UserOrderNumRespDto getOrderNum(int userId);
	
	/**
	 * 
	 * @param userId,//用户ID
	 * @param flag ,//flag 1:全部 2:待付款 3:待收货 4:退货
	 * @param pageSize 
	 * @param pageNum 
	 * @return 订单列表
	 */
	public OrderListRespDto getOrderList(int userId, int flag, int pageNum, int pageSize);
	
	/**
	 * 
	 * @param cityId 城市ID
	 * @param userId 用户ID
	 * @param storeId 店铺ID
	 * @param openId 唯一设备号
	 * @param isUsable 是否可用（0: 不限——过期和不过期的优惠券；1：可用优惠券）
	 */
	public CouponListRespDto getCouponList(int cityId, int userId, int storeId, String openId, int isUsable);
	
	
	public ResponseDto<String> activateCoupon(int userId, String openId, String commoncode);
	
	
	/**
	 * 
	 * @param userId 用户ID
	 * @param orderSnMain 主单号
	 * @param flag flag 1:全部 2:待付款 3:待收货 4:退货
	 * @param orderId @param userId 用户ID
	 * @return 订单详情
	 */
	public OrderListRespDto getOrderInfo(int userId, String orderSnMain, int flag, int orderId);
		
	public SubmitOrderRespDto submitOrder(SubmitOrderReqDto req);

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
//	public AbstractPayOrderRespDto payOrder(int userId, int payType, int paymentId, String orderSnMain, 
//			int isTaoxinka, int isVcount);
	/**
	 * 
	 * @param userId 用户ID
	 * @param orderSnMain 订单ID
	 * @return 支付页订单详情
	 */
	public PayOrderResultRespDto getPayOrderMsg(int userId, String orderSnMain);
	
	/**
	 * 
	 * @param taoOrderSn 订单ID
	 * @return 物流详情
	 */
	public ShippingMsgRespDto getShippingResult(String taoOrderSn);
	
	/**
	 * 
	 * @param orderSnMain
	 * @return 下单后分享
	 */
	public ShareRespDto shareAfterPay(String orderSnMain);
	
	/**
	 * 订单详情
	 */
	public  OResponseDto<OrderInfoDto> getOrderGoodsInfo(String taoOrderSn);
	
	/**
	 * 订单　预售订单列表
	 */
	public OResponseDto<OrderListInfoDto> getOrderListInfo(OrderListParamDto param);
	
	/**
	 * 打包完成
	 */
	public OResponseDto<String> finishPackagingOrder(String taoOrderSn,String userName,int senderId);

	/**

	 * 生成订单前支付信息页面
	 * @param userId
	 * @param openId
	 * @param cityId
	 * @param storeId
	 * @param couponId
	 */
	public PayBeforeRespDto getPayInfoBeforeOrder(int userId, String openId, int cityId,
			int storeId, int couponId);
	/**
	 * 拒绝订单
	 */
	public OResponseDto<String> refuseOrder(String taoOrderSn,String userName,int refuseId,String refuseReason);
	
	/**
	 * 确认收货
	 */
	public OResponseDto<String> confirmRevieveOrder(String taoOrderSn,String gps,String userName);
	
	/**
	 *     确认预售订单到货
	 */
	public OResponseDto<String> confirmBookOrder(String taoOrderSn,String userName,int senderId);
	
	public OResponseDto<OrderListInfoDto> getWhAvailableOrders(OrderListParamDto params);

	public OResponseDto<OrderStatusCountRespDto> getOrderCount(int storeId);
	/** 
	 * @return 
	 */
	public ReturnStorageRespDto returnStorage(ReturnStorageReqDto returnStorageReqDto);

	/*
	 * 统计每月统计提成奖励信息
	 * @param: storeId
	 * @return RespDto<OrderBonusRespDto>
	 */
	public RespDto<OrderBonusRespDto> getCurrentMonthBonusInfo(int storeId);

	
	public Map<String,List<LkStatusItemDto>> getLkStatusItemMap();
	
	public Map<String,Object> getLkConfigureMap();

	public boolean createCouponCode(int userId, int couponId, int type, boolean check,
			int num, String openId, double money);
	
	public void sendNewUserRegisterCoupon(int userId, String phone);
	
	/**
	 * 获取用户当天购买某商品的个数 (全部的有效订单，排除订单状态为无效、取消、拒单的订单)
	 * @param userId
	 * @param specId 
	 * @return
	 */
	public int getTodayGoodsCount(int userId, int specId);
	
	/**
	 * 获取店铺当天某商品的销量 (全部的订单)
	 * @param storeId
	 * @param specId
	 * @return
	 */
	public int getTodayStoreGoodsCount(int storeId, int specId);

}
