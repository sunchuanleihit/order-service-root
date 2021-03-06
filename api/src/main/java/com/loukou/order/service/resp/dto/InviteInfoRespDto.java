package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class InviteInfoRespDto implements Serializable {

	
	/**
	 * 邀请信息返回实体
	 */
	private static final long serialVersionUID = 4970260051198737474L;
	//邀请码
	private String inviteCode = "";
	//奖励总金额
	private Double totalReward = 0.0;
	//邀请列表
	private List<InviteListDto>  InviteList = Lists.newArrayList();
	//消息code
	private  int code = 200;
	//是否到达上限
	private int isOver=0;
	
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	
	public Double getTotalReward() {
		return totalReward;
	}
	public void setTotalReward(Double totalReward) {
		this.totalReward = totalReward;
	}

	public List<InviteListDto> getInviteList() {
		return InviteList;
	}
	public void setInviteList(List<InviteListDto> inviteList) {
		InviteList = inviteList;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getIsOver() {
		return isOver;
	}
	public void setIsOver(int isOver) {
		this.isOver = isOver;
	}
	
	

	
	
}
