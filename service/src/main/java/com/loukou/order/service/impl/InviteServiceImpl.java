package com.loukou.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.api.InviteService;
import com.loukou.order.service.req.dto.InviteInfoReqdto;
import com.loukou.order.service.req.dto.InviteValidateReqDto;
import com.loukou.order.service.resp.dto.InviteInfoRespDto;
import com.loukou.order.service.resp.dto.InviteValidateRespDto;

@Service("InviteService")
public class InviteServiceImpl implements InviteService {
	
	@Autowired
	private InviteOperationProcessor inviteProcessor;
	
	/**
	 * 查询邀请码信息
	 * @param req
	 * @return
	 */
	@Override
	public InviteInfoRespDto getInviteInfo(InviteInfoReqdto req) {
	
		return inviteProcessor.getInviteInfo(req);
	}
	
	/**
	 * 通过微信端邀请码获取优惠券
	 * @param req
	 * @return
	 */
	@Override
	public InviteValidateRespDto getCouponByInvite(InviteValidateReqDto req) {

		return inviteProcessor.getCouponByInvite(req);
	}
	/**
	 * 新用户注册时检查是否发放邀请券
	 * @param phoneNumber
	 * @param userId
	 * @return
	 */
	@Override
	public boolean checkAndCreateCoupon(String phoneNumber, int userId,String openId) {
		
		return inviteProcessor.checkAndCreateCoupon(phoneNumber, userId,openId);
	}
}
