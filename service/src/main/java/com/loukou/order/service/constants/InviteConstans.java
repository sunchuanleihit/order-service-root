package com.loukou.order.service.constants;

public class InviteConstans {
	//查询邀请详细列表
	public static final String  queryType_List="List";
	//邀请上限值
	public static final double  reward_limit=200.0;
	//查询邀请码
	public static final String  queryType_code="code";
	//被邀请人优惠券ID
	public static final int invited_CouponId=962;
	//已发放优惠券
	public static final int get_Coupon =1;
	//已发放优惠券
	public static final int notGet_Coupon =0;
	
	//邀请状态(1:已注册 2:已下单 3:邀请成功4:已退货)
	//已注册
	public static final int inviteStatus_registered=1;
	//已下单
	public static final int inviteStatus_gotOrder=2;
	//邀请成功
	public static final int inviteStatus_success=3;
	//已退货
	public static final int inviteStatus_cancel=4;
	//奖励（5等待中 6 已取消 7 已奖励  8到达上限）
	//等待中
	public static final int rewardStatus_waiting=5;
	//已取消
	public static final int rewardStatus_cancel=6;
	//已奖励
	public static final int rewardStatus_rewarded=7;
	//到达上限
	public static final int rewardStatus_limit=8;
}
