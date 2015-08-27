package com.loukou.order.service.api;

import com.loukou.order.service.req.dto.InviteInfoReqdto;
import com.loukou.order.service.req.dto.InviteValidateReqDto;
import com.loukou.order.service.resp.dto.InviteInfoRespDto;
import com.loukou.order.service.resp.dto.InviteValidateRespDto;

public interface InviteService {
	/**
	 * 查询邀请码信息
	 * @param req
	 * @return
	 */
	public InviteInfoRespDto getInviteInfo(InviteInfoReqdto req);
	/**
	 * 通邀请码获取优惠券
	 * @param req
	 * @return
	 */
	public InviteValidateRespDto getCouponByInvite(InviteValidateReqDto req);
	/**
	 * 新用户注册时检查是否发放邀请券
	 * @param phoneNumber
	 * @param userId
	 * @return
	 */
	public boolean checkAndCreateCoupon(String phoneNumber,int userId); 
}
