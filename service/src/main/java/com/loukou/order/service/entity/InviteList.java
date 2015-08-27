package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * 邀请码详情表
 *
 */

@Entity
@Table(name="lk_invite_list")
public class InviteList {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	//用户id
	@Column(name = "user_id")
	private int userId;
	
	//邀请码
	@Column(name = "invite_code")
	private String inviteCode;
	
	//邀请状态
	@Column(name = "invite_status")
	private int inviteStatus;
	
	//最后更新时间
	@Column(name = "lastupdate_time")
	private Date lastupdateTime;
	
	//主订单号
	@Column(name = "order_sn_main")
	private String orderSnMain="";
	
	//手机号
	@Column(name = "phone_mob")
	private String phoneMob;
	
	//奖励状态
	@Column(name = "reward_status")
	private int rewardStatus;
	
	//奖励金额
	@Column(name = "reward")
	private Double reward=0.0;
	
	//是否发放优惠券码
	@Column(name = "if_getcoupon")
	private int ifGetcoupon=0;
	
	//下单时间	
	@Column(name = "created_order_time")
	private Date createdOrderTime;
	
	//创建时间
	@Column(name = "created_time")
	private Date createdTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public int getInviteStatus() {
		return inviteStatus;
	}

	public void setInviteStatus(int inviteStatus) {
		this.inviteStatus = inviteStatus;
	}

	public Date getLastupdateTime() {
		return lastupdateTime;
	}

	public void setLastupdateTime(Date lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}

	public String getOrderSnMain() {
		return orderSnMain;
	}

	public void setOrderSnMain(String orderSnMain) {
		this.orderSnMain = orderSnMain;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}


	public int getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(int rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}


	public int getIfGetcoupon() {
		return ifGetcoupon;
	}

	public void setIfGetcoupon(int ifGetcoupon) {
		this.ifGetcoupon = ifGetcoupon;
	}

	public Date getCreatedOrderTime() {
		return createdOrderTime;
	}

	public void setCreatedOrderTime(Date createdOrderTime) {
		this.createdOrderTime = createdOrderTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	
	
	
}
