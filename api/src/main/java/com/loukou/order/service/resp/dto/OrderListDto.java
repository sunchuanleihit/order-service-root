package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.List;

public class OrderListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5016662529411604501L;
	private OrderListBaseDto base;
	private ShippingMsgDto shippingmsg;
	private List<GoodsListDto> goodsList;
	private ExtmMsgDto extmMsg;

	
	public OrderListBaseDto getBase() {
		return base;
	}

	public void setBase(OrderListBaseDto base) {
		this.base = base;
	}

	public ShippingMsgDto getShippingmsg() {
		return shippingmsg;
	}

	public void setShippingmsg(ShippingMsgDto shippingmsg) {
		this.shippingmsg = shippingmsg;
	}

	public List<GoodsListDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsListDto> goodsList) {
		this.goodsList = goodsList;
	}

	public ExtmMsgDto getExtmMsg() {
		return extmMsg;
	}

	public void setExtmMsg(ExtmMsgDto extmMsg) {
		this.extmMsg = extmMsg;
	}

    @Override
    public String toString() {
        return "OrderListDto [base=" + base + ", shippingmsg=" + shippingmsg + ", goodsList=" + goodsList
                + ", extmMsg=" + extmMsg + "]";
    }

}
