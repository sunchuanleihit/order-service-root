package com.loukou.order.service.resp.dto;

import java.util.ArrayList;
import java.util.List;

public class CouponListResultDto {
	private List<CouponListDto> recommend = new ArrayList<CouponListDto>();
	private List<CouponListDto> couponList = new ArrayList<CouponListDto>();
	private int canUse;
	private String everydayNum = "2";
	private String everydayMsg = "每天限使用2张优惠券，明天再来吧";

	public List<CouponListDto> getRecommend() {
		return recommend;
	}

	public void setRecommend(List<CouponListDto> recommend) {
		this.recommend = recommend;
	}

	public List<CouponListDto> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponListDto> couponList) {
		this.couponList = couponList;
	}

	public int getCanUse() {
		return canUse;
	}

	public void setCanUse(int canUse) {
		this.canUse = canUse;
	}

	public String getEverydayNum() {
		return everydayNum;
	}

	public void setEverydayNum(String everydayNum) {
		this.everydayNum = everydayNum;
	}

	public String getEverydayMsg() {
		return everydayMsg;
	}

	public void setEverydayMsg(String everydayMsg) {
		this.everydayMsg = everydayMsg;
	}

}
