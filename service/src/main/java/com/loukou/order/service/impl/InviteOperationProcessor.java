package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.constants.CouponFormType;
import com.loukou.order.service.constants.InviteConstans;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.InviteCodeDao;
import com.loukou.order.service.dao.InviteInfoDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.InviteCode;
import com.loukou.order.service.entity.InviteList;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.req.dto.InviteInfoReqdto;
import com.loukou.order.service.req.dto.InviteValidateReqDto;
import com.loukou.order.service.resp.dto.InviteInfoRespDto;
import com.loukou.order.service.resp.dto.InviteListDto;
import com.loukou.order.service.resp.dto.InviteValidateRespDto;
import com.serverstarted.user.api.PhoneVeriCodeService;

import org.apache.commons.collections.CollectionUtils;

@Service
public class InviteOperationProcessor {
	// 邀请码长度
	private static final int LENGTH_INVITE = 8; 
	
	@Autowired
	private  PhoneVeriCodeService  phoneVeriCodeService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private InviteCodeDao inviteCodeDao;
	
	@Autowired
	private InviteInfoDao inviteInfoDao;
	
	@Autowired
	private  MemberDao memberDao;
	
	@Autowired
	private CoupRuleDao coupRuleDao;
	
	public InviteInfoRespDto getInviteInfo(InviteInfoReqdto req) {
		
		InviteInfoRespDto resp=new InviteInfoRespDto();
		String inviteCodeStr="";
		int userId=req.getUserId();
		//判断查询类型
		if(InviteConstans.queryType_code.equals(req.getQueryType())){
			//查询邀请表中是否有数据
			InviteCode inviteCode=inviteCodeDao.findByUserId(req.getUserId());
			if(null!=inviteCode){
				inviteCodeStr=inviteCode.getInviteCode();
			}
			//如果邀请码表中没有则生成
			if(StringUtils.isEmpty(inviteCodeStr)){
				inviteCodeStr=	createInviteCode(req.getUserId());
				//生成并保存邀请码
				InviteCode in=new InviteCode();
				in.setUserId(req.getUserId());
				in.setInviteCode(inviteCodeStr);
				inviteCodeDao.save(in);
			}
			resp.setInviteCode(inviteCodeStr);
		}else{
			List<InviteListDto>  inviteList = new ArrayList<InviteListDto>();
			List<InviteList> Ilist=new ArrayList<InviteList>();
			String  moblie=null;
			int pageSize=0;
			//前段不填查全部
			if(req.getPageSize()<1){
				Ilist=	inviteInfoDao.findByUserId(userId);
			}else{
				pageSize=req.getPageSize();
				//分页查询
				PageRequest p=new PageRequest(0,pageSize, new Sort(
		                Sort.Direction.ASC, "createdTime"));
				Page<InviteList> pList= inviteInfoDao.findByUserId(userId, p);	
				Ilist=pList.getContent();
			}
			//实体转换
			if(CollectionUtils.isNotEmpty(Ilist)){
				for(InviteList in:Ilist){
					InviteListDto inviteListDto = new InviteListDto();
					inviteListDto.setInviteStatus(in.getInviteStatus());
					moblie=in.getPhoneMob();
					//手机号加密
					if(moblie.length()>7){
						moblie=moblie.replace(moblie.substring(3, 7), "****");
					} 
					inviteListDto.setMoblie(moblie);
					inviteListDto.setRewardStaus(in.getRewardStatus());
					inviteListDto.setReward(in.getReward());
					inviteList.add(inviteListDto);
				}
				resp.setInviteList(inviteList);
			}
		}
		//计算奖励金额
		double totalReward=inviteInfoDao.sumRewardByUserId(req.getUserId());
		resp.setTotalReward(totalReward);
		if(InviteConstans.reward_limit<=totalReward){
			resp.setIsOver(1);
		}
	
		resp.setCode(0);
		return resp;
		
	}
	
	/**
	 * 通邀请码获取优惠券
	 * @param req
	 * @return
	 */
	public InviteValidateRespDto getCouponByInvite(InviteValidateReqDto req){
		InviteValidateRespDto response=new InviteValidateRespDto();
		InviteList inviteList=new  InviteList();
		//被邀请人会员信息
		Member inviteduser = null;
		boolean ifsuccess=false;
		//1 验证验证码是否正确
		if(!phoneVeriCodeService.verify(req.getPhoneNumber(), req.getValidateCode())){
			response.setCode(10);
			response.setMessage("验证码错误，请重新填写");
			return response;
		}
		//2 验证邀请码
		InviteCode inviteCode=inviteCodeDao.findByInviteCode(req.getInviteCode());
		//通过手机号查询用户信息
		List<Member> member= memberDao.findByPhoneMob(req.getPhoneNumber());
		if(null==inviteCode){
			response.setCode(20);
			response.setMessage("抱歉，链接已失效，联系您的邀请人再试一次吧~");
			return response;
		}else {		
			//验证领取状态
			List<InviteList> list =inviteInfoDao.findByPhoneMob(req.getPhoneNumber());
			if(list.size()>0){
				response.setCode(30);
				response.setMessage("抱歉，您已领取过邀请券。");
				return  response;
			}
		//3 验证手机在member表中是否存在  不存在直接发券 
			if(member.size()>0){
				inviteduser=member.get(0);
				//验证邀请码是否本人的
				if(inviteduser.getUserId()==inviteCode.getUserId()){
					response.setCode(40);
					response.setMessage("抱歉，您不能领取自己的邀请券。");
					return response;
				}
				//createCouponCode 方法中含有验证是否是新用户
				ifsuccess=orderService.createCouponCode(inviteduser.getUserId(), InviteConstans.invited_CouponId,  CouponFormType.PRIVATE,false, 1,"", 0);
				if(ifsuccess){
					inviteList.setIfGetcoupon(InviteConstans.get_Coupon);
				}else{
					response.setCode(31);
					response.setMessage("抱歉，您已领取过邀请券。");
					return  response;
				}
			}else{//新用户 标记未发券
				inviteList.setIfGetcoupon(InviteConstans.notGet_Coupon);
			}
			inviteList.setInviteCode(req.getInviteCode());
			inviteList.setInviteStatus(InviteConstans.inviteStatus_registered);
			inviteList.setRewardStatus(InviteConstans.rewardStatus_waiting);
			inviteList.setPhoneMob(req.getPhoneNumber());
			inviteList.setUserId(inviteCode.getUserId());//保存邀请人用户ID
			if( null!=inviteInfoDao.save(inviteList)){
				ifsuccess=true;
			}
		
		}

//		//如果发券成功
		if(ifsuccess){
			//查询返回优惠券信息
			CoupRule coupRule = coupRuleDao.findOne(InviteConstans.invited_CouponId);
			response.setMoney(coupRule.getMoney());
			response.setPhoneNumber(req.getPhoneNumber());
			response.setCode(0);
		}
	
		return response;
		
	}
	
	public boolean checkAndCreateCoupon(String phoneNumber, int userId) {
		
		return false;
		
	}
	/**
	 * 生成邀请码
	 * @param userId
	 * @return 
	 */
	private String createInviteCode(int userId){
		StringBuffer strBuf = new StringBuffer();
		//0开头邀请码
		strBuf.append("0");
		String a =	Integer.toHexString(userId);
		//填充长度
		int fillLength=LENGTH_INVITE-1-a.length();
		String chars = "ghijklmnopqrstuvwxyz";
		//长度补全
		for(int i=0;i<fillLength;i++){
			strBuf.append( chars.charAt((int)(Math.random() * 20)));
		}
		strBuf.append(a);
		return strBuf.toString();
	}
	
}
