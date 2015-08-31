package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class InviteListDto implements Serializable {

	/**
	 * 邀请列表
	 */
	private static final long serialVersionUID = 4480412945023376467L;
	//手机号
	private String moblie;
	//邀请状态
	private String inviteStatus;
	//奖励
	private double reward;
	//奖励状态
	private String rewardStaus;

	
	public String getMoblie() {
		return moblie;
	}
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}

	public String getInviteStatus() {
		return inviteStatus;
	}
	public void setInviteStatus(String inviteStatus) {
		this.inviteStatus = inviteStatus;
	}

	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}

	public String getRewardStaus() {
		return rewardStaus;
	}
	public void setRewardStaus(String rewardStaus) {
		this.rewardStaus = rewardStaus;
	}


	
	

}
