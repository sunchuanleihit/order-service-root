package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PayBeforeResultDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3731552768063373025L;
	
	private PayOrderMsgDto orderMsg = new PayOrderMsgDto();
	private CouponListDto recommend = new CouponListDto();
	private List<PayOrderGoodsListDto> goodsList = new ArrayList<PayOrderGoodsListDto>();
	
	public PayOrderMsgDto getOrderMsg() {
		return orderMsg;
	}

	public void setOrderMsg(PayOrderMsgDto orderMsg) {
		this.orderMsg = orderMsg;
	}

	public CouponListDto getRecommend() {
		return recommend;
	}

	public void setRecommend(CouponListDto recommend) {
		this.recommend = recommend;
	}

	public List<PayOrderGoodsListDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<PayOrderGoodsListDto> goodsList) {
		this.goodsList = goodsList;
	}

}
