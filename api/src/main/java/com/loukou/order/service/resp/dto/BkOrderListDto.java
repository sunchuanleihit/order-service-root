package com.loukou.order.service.resp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BkOrderListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5016662529411604501L;
	private BkOrderListBaseDto base = new BkOrderListBaseDto();
	private ShippingMsgDto shippingmsg = new ShippingMsgDto();
	private List<BkPackageInfoDto> packageList = new ArrayList<BkPackageInfoDto>();
	private ExtmMsgDto extmMsg = new ExtmMsgDto();

	
	public BkOrderListBaseDto getBase() {
		return base;
	}

	public void setBase(BkOrderListBaseDto base) {
		this.base = base;
	}

	public ShippingMsgDto getShippingmsg() {
		return shippingmsg;
	}

	public void setShippingmsg(ShippingMsgDto shippingmsg) {
		this.shippingmsg = shippingmsg;
	}

	public List<BkPackageInfoDto> getGoodsList() {
		return packageList;
	}

	public void setGoodsList(List<BkPackageInfoDto> packageList) {
		this.packageList = packageList;
	}

	public ExtmMsgDto getExtmMsg() {
		return extmMsg;
	}

	public void setExtmMsg(ExtmMsgDto extmMsg) {
		this.extmMsg = extmMsg;
	}

    @Override
    public String toString() {
        return "OrderInfoDto [base=" + base + ", shippingmsg=" + shippingmsg + ", packageList=" + packageList
                + ", extmMsg=" + extmMsg + "]";
    }

}
