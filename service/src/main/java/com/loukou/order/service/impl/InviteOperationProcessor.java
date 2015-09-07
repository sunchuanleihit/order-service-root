package com.loukou.order.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.constants.CouponFormType;
import com.loukou.order.service.constants.InviteConstans;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.InviteCodeDao;
import com.loukou.order.service.dao.InviteInfoDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderUserDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.InviteCode;
import com.loukou.order.service.entity.InviteList;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.enums.InviteStatusEnum;
import com.loukou.order.service.enums.RewardStatusEnum;
import com.loukou.order.service.req.dto.InviteInfoReqdto;
import com.loukou.order.service.req.dto.InviteValidateReqDto;
import com.loukou.order.service.resp.dto.InviteInfoRespDto;
import com.loukou.order.service.resp.dto.InviteListDto;
import com.loukou.order.service.resp.dto.InviteValidateRespDto;
import com.loukou.order.service.resp.dto.ResponseDto;
import com.loukou.sms.sdk.client.MultiClient;
import com.loukou.sms.sdk.client.SingletonSmsClient;
import com.serverstarted.user.api.PhoneVeriCodeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

@Service
public class InviteOperationProcessor {
	// 邀请码长度
	private static final int LENGTH_INVITE = 8; 

	private static final Logger LOGGER = Logger
			.getLogger(InviteOperationProcessor.class);
	
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
	
	@Autowired
	private CoupListDao coupListDao;
	
	@Autowired
	private OrderUserDao orderUserDao ;
	@Autowired
	private OrderDao orderDao;
	
	public InviteInfoRespDto getInviteInfo(InviteInfoReqdto req) {
		
		InviteInfoRespDto resp=new InviteInfoRespDto();
		String inviteCodeStr="";
		int userId=req.getUserId();
		//判断查询类型

		if(InviteConstans.QUERYTYPE_CODE.equals(req.getQueryType())){
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
					inviteListDto.setInviteStatus(InviteStatusEnum.parseType(in.getInviteStatus()).getStatus());
					moblie=in.getPhoneMob();
					//手机号加密
					if(moblie.length()>7){
						moblie=moblie.replace(moblie.substring(3, 7), "****");
					} 
					inviteListDto.setMoblie(moblie);
					if(RewardStatusEnum.REWARDSTATUS_REWARDED.getId()==in.getRewardStatus()){
						inviteListDto.setRewardStaus(in.getReward().toString()+"元");
					}else{
						inviteListDto.setRewardStaus(RewardStatusEnum.parseType(in.getRewardStatus()).getStatus());
					}
					inviteListDto.setReward(in.getReward());
					inviteList.add(inviteListDto);
				}
				resp.setInviteList(inviteList);
			}
		}
		//计算奖励金额
		double totalReward=inviteInfoDao.sumRewardByUserId(req.getUserId());
		resp.setTotalReward(totalReward);
		if(InviteConstans.REWARD_LIMIT<=totalReward){
			resp.setIsOver(1);
		}
	
		resp.setCode(200);
		return resp;
		
	}
	
	/**
	 * 通过微信端邀请码获取优惠券
	 * @param req
	 * @return
	 */
	@Transactional
	public InviteValidateRespDto getCouponByInvite(InviteValidateReqDto req){
		InviteValidateRespDto response=new InviteValidateRespDto();
		InviteList inviteList=new  InviteList();
		//被邀请人会员信息
		Member inviteduser = null;
		boolean ifsuccess=false;
		//1 验证验证码是否正确
		if(!phoneVeriCodeService.verify(req.getPhoneNumber(), req.getValidateCode())){
			response.setCode(400);
			response.setMessage("验证码错误，请重新填写。");
			return response;
		}
		//2 验证邀请码
		String lowCode=req.getInviteCode().toUpperCase();
		InviteCode inviteCode=inviteCodeDao.findByInviteCode(lowCode);
		//通过手机号查询用户信息
		List<Member> member= memberDao.findByCheckedPhoneMob(req.getPhoneNumber());
		if(null==inviteCode){
			response.setCode(400);
			response.setMessage("抱歉，链接已失效，联系您的邀请人再试一次吧~");
			return response;
		}else {		
			//验证领取状态
			InviteList list =inviteInfoDao.findByPhoneMob(req.getPhoneNumber());
			if(null!=list){
				response.setCode(400);
				response.setMessage("抱歉，您已领取过邀请券。");
				return  response;
			}
		//3 验证手机在member表中是否存在  不存在直接发券 
			if(member.size()>0){
				inviteduser=member.get(0);
				//验证邀请码是否本人的
				if(inviteduser.getUserId()==inviteCode.getUserId()){
					response.setCode(400);
					response.setMessage("抱歉，您不能领取自己的邀请券。");
					return response;
				}
				//createCouponCode 
				if(orderDao.IfExistOrder(inviteduser.getUserId())!=null){
					response.setCode(400);
					response.setMessage("抱歉，您不是新用户，不可领取邀请券");
					return  response;
				}
				ifsuccess=orderService.createCouponCode(inviteduser.getUserId(), InviteConstans.INVITED_COUPONID,  CouponFormType.PRIVATE,false, 2,"", 0);
				if(ifsuccess){
					//标记已发券
					inviteList.setIfGetcoupon(InviteConstans.GET_COUPON);

				}else{
					response.setCode(400);
					response.setMessage("抱歉，领取失败，请稍候再试。");
					return  response;
				}
			}else{//新用户 标记未发券
				inviteList.setIfGetcoupon(InviteConstans.NOTGET_COUPON);
			}
			inviteList.setInviteCode(req.getInviteCode());
			inviteList.setInviteStatus(InviteStatusEnum.INVITESTATUS_REGISTERED.getId());
			inviteList.setRewardStatus(RewardStatusEnum.REWARDSTATUS_WAITING.getId());
			inviteList.setPhoneMob(req.getPhoneNumber());
			inviteList.setUserId(inviteCode.getUserId());//保存邀请人用户ID
			if( null!=inviteInfoDao.save(inviteList)){
				ifsuccess=true;
			}
		}
		//如果发券成功
		if(ifsuccess){
			//查询返回优惠券信息
			CoupRule coupRule = coupRuleDao.findOne(InviteConstans.INVITED_COUPONID);
			response.setMoney(coupRule.getMoney()*2);//两张
			response.setPhoneNumber(req.getPhoneNumber());
			response.setCode(200);
			String content = "亲，10元红包塞进你的账户。自从用楼口，惊喜天天有！快去查看账户钱包吧！http://t.cn/R2mStax";
//			String[] phones = new String[]{req.getPhoneNumber()};
			//调用短信服务，发送短信
//			try {
//			SingletonSmsClient.getClient().sendSMS(phones,content);
//			} catch (RemoteException e) {
//				LOGGER.error(e);
//
//			}
			MultiClient.getProvider(MultiClient.CHUANGLAN_PROVIDER).sendMessage(
                    Lists.newArrayList(req.getPhoneNumber()), content);

		}
	
		return response;
		
	}
	
	public ResponseDto<String> checkAppInviteCode(int userId,String openId,String inviteCode ){
		ResponseDto<String> resp = new ResponseDto<String>(200, "");
		//1 检查 1.邀请码是否有效 2.是否本人
		InviteCode incode=inviteCodeDao.findByInviteCode(inviteCode);
		if(incode==null){
			resp.setCode(400);
			resp.setMessage("邀请码错误，请重新填写。");
			return resp;
		}else if(userId==incode.getUserId()){
			resp.setCode(400);
			resp.setMessage("抱歉，您不能领取自己的邀请券。");
			return resp;
		}
		//2 检查是否领取过邀请券 1. 邀请表 2 .优惠券表
		Member member= memberDao.findOne(userId);
		InviteList list =inviteInfoDao.findByPhoneMob(member.getPhoneMob());
		//有数据代表发送过
		if(list!=null||coupListDao.findByCouponIdAndOpenid(InviteConstans.INVITED_COUPONID, openId).size()>1){
			resp.setCode(400);
			resp.setMessage("抱歉，您已领取过邀请券。");
			return  resp;
		}
		//3 是否新用户  openId是否下单      创建优惠券（手机号是否下过单）
		if(orderUserDao.getOrderCountByOpenId(openId)>0||orderDao.IfExistOrder(userId)!=null){
			resp.setCode(400);
			resp.setMessage("抱歉，您不是新用户，不可领取邀请券。");
			return  resp;
		}else{
			if(orderService.createCouponCode(userId, InviteConstans.INVITED_COUPONID,  CouponFormType.PRIVATE,false, 2, openId, 0)){
				InviteList inviteList=new  InviteList();
				inviteList.setInviteCode(inviteCode);
				inviteList.setInviteStatus(InviteStatusEnum.INVITESTATUS_REGISTERED.getId());
				inviteList.setRewardStatus(RewardStatusEnum.REWARDSTATUS_WAITING.getId());
				inviteList.setPhoneMob(member.getPhoneMob());
				//标记已发券
				inviteList.setIfGetcoupon(InviteConstans.GET_COUPON);
				inviteList.setUserId(incode.getUserId());//保存邀请人用户ID
				inviteInfoDao.save(inviteList);
				
				resp.setCode(200);
				resp.setMessage("激活成功");
				return  resp;
			}else{
				resp.setCode(400);
				resp.setMessage("抱歉，领取失败，请稍候再试。");
				return  resp;
			}
		}

		
	}
	/**
	 * 检查手机号是否在邀请列表并未发放优惠券
	 * @param phoneNumber
	 * @param userId
	 * @return
	 */
	@Transactional
	public boolean checkAndCreateCoupon(String phoneNumber, int userId,String  openId) {
		List<InviteList> list=inviteInfoDao.findByPhoneMobAndIfGetcoupon(phoneNumber, InviteConstans.NOTGET_COUPON);
		if(list.size()>0){
			List<CoupList> coList=coupListDao.findByCouponIdAndOpenid(InviteConstans.INVITED_COUPONID, openId);
			if(coList.size()<2){
				//创建邀请券 成功并修改获取邀请状态
				if(orderService.createCouponCode(userId, InviteConstans.INVITED_COUPONID, CouponFormType.PRIVATE, false, 2, "", 0)){
					inviteInfoDao.updateIfGetcouponByPhone(phoneNumber);
					return true;
				}
			}
			
		}
		return false;
		
	}
	
	/**
	 * 生成邀请码
	 * @param userId
	 * @return 
	 */
	private String createInviteCode(int userId){
		StringBuffer strBuf = new StringBuffer();
		//7开头邀请码
		strBuf.append("7");
		String a =	Integer.toHexString(userId);
		//填充长度
		int fillLength=LENGTH_INVITE-1-a.length();
		String chars = "ghijklmnopqrstuvwxyz";
		//长度补全
		for(int i=0;i<fillLength;i++){
			strBuf.append( chars.charAt((int)(Math.random() * 20)));
		}
		strBuf.append(a);
		return strBuf.toString().toUpperCase();

	}
	
}
