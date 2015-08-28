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
import com.loukou.order.service.req.dto.InviteInfoReqdto;
import com.loukou.order.service.req.dto.InviteValidateReqDto;
import com.loukou.order.service.resp.dto.InviteInfoRespDto;
import com.loukou.order.service.resp.dto.InviteListDto;
import com.loukou.order.service.resp.dto.InviteValidateRespDto;
import com.loukou.order.service.resp.dto.ResponseDto;
import com.loukou.sms.sdk.client.SingletonSmsClient;
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
		String lowCode=req.getInviteCode().toLowerCase();
		InviteCode inviteCode=inviteCodeDao.findByInviteCode(lowCode);
		//通过手机号查询用户信息
		List<Member> member= memberDao.findByPhoneMob(req.getPhoneNumber());
		if(null==inviteCode){
			response.setCode(400);
			response.setMessage("抱歉，链接已失效，联系您的邀请人再试一次吧~");
			return response;
		}else {		
			//验证领取状态
			List<InviteList> list =inviteInfoDao.findByPhoneMob(req.getPhoneNumber());
			if(list.size()>0){
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
				ifsuccess=orderService.createCouponCode(inviteduser.getUserId(), InviteConstans.invited_CouponId,  CouponFormType.PRIVATE,false, 1,"", 0);
				if(ifsuccess){
					//标记已发券
					inviteList.setIfGetcoupon(InviteConstans.get_Coupon);
				}else{
					response.setCode(400);
					response.setMessage("抱歉，您不是新用户，不可领取邀请券");
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
		//如果发券成功
		if(ifsuccess){
			//查询返回优惠券信息
			CoupRule coupRule = coupRuleDao.findOne(InviteConstans.invited_CouponId);
			response.setMoney(coupRule.getMoney());
			response.setPhoneNumber(req.getPhoneNumber());
			response.setCode(200);
			String content = "";
			String[] phones = new String[]{req.getPhoneNumber()};
			//调用短信服务，发送短信
			try {
			SingletonSmsClient.getClient().sendSMS(phones,content);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}
	
		return response;
		
	}
	
	public ResponseDto<String> checkAppInviteCode(int userId,String openId,String inviteCode ){
		ResponseDto<String> resp = new ResponseDto<String>(200, "");
		//1 检查 1.邀请码是否有效 2.是否本人
		InviteCode incode=inviteCodeDao.findByInviteCode(inviteCode);
		if(incode==null){
			resp.setCode(400);
			resp.setMessage("验证码错误，请重新填写。");
			return resp;
		}else if(userId==incode.getUserId()){
			resp.setCode(400);
			resp.setMessage("抱歉，您不能领取自己的邀请券。");
			return resp;
		}
		//2 检查是否领取过邀请券 1. 邀请表 2 .优惠券表
		Member member= memberDao.findOne(userId);
		List<InviteList> list =inviteInfoDao.findByPhoneMob(member.getPhoneMob());
		List<CoupList> coList=coupListDao.findByCouponIdAndOpenid(InviteConstans.invited_CouponId, openId);
		//有数据代表发送过
		if(list.size()>0||coList.size()>0){
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
			if(orderService.createCouponCode(userId, InviteConstans.invited_CouponId,  CouponFormType.PRIVATE,false, 1,"", 0)){
				InviteList inviteList=new  InviteList();
				inviteList.setInviteCode(inviteCode);
				inviteList.setInviteStatus(InviteConstans.inviteStatus_registered);
				inviteList.setRewardStatus(InviteConstans.rewardStatus_waiting);
				inviteList.setPhoneMob(member.getPhoneMob());
				//标记已发券
				inviteList.setIfGetcoupon(InviteConstans.get_Coupon);
				inviteList.setUserId(incode.getUserId());//保存邀请人用户ID
				inviteInfoDao.save(inviteList);
				
				resp.setCode(200);
				resp.setMessage("激活成功");
				return  resp;
			}else{
				resp.setCode(400);
				resp.setMessage("抱歉，您不是新用户，不可领取邀请券。");
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
	public boolean checkAndCreateCoupon(String phoneNumber, int userId) {
		List<InviteList> list=inviteInfoDao.findByPhoneMobAndIfGetcoupon(phoneNumber, InviteConstans.notGet_Coupon);
		if(list.size()>0){
			//创建邀请券 成功并修改获取邀请状态
			if(orderService.createCouponCode(userId, InviteConstans.invited_CouponId, CouponFormType.PRIVATE, false, 1, "", 0)){
				inviteInfoDao.updateIfGetcouponByPhone(phoneNumber);
				return true;
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
