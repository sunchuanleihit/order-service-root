package com.loukou.order.service.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.req.dto.ReturnStorageGoodsReqDto;
import com.loukou.order.service.req.dto.ReturnStorageReqDto;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.OrderListBaseDto;
import com.loukou.order.service.resp.dto.OrderListDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.PayOrderMsgDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ReturnStorageRespDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShareResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;

public class OrderServiceImplTest extends AbstractTestObject {

	@Autowired
	private OrderService orderService;
	
	@Test
	public void submitOrder() {
		SubmitOrderReqDto req = new SubmitOrderReqDto();
		req.setUserId(1032752);
		req.setOpenId("414F8167B0CF4C3AA3603C7CF63365DD");
		req.setStoreId(18047);
		req.setCityId(1);
		req.setAddressId(128);
		req.setOs("ios");
		req.getShippingTimes().getMaterial().add("2015-07-28 09:00:00");
		
		SubmitOrderRespDto resp = orderService.submitOrder(req);
		System.out.println(object2String(resp));
	}
	
	@Test
	public void getCouponList() {
		int cityId = 1;
		int userId = 113981;
		int storeId = 18055;
		String openId = "test-openId";
		CouponListRespDto resp = orderService.getCouponList(cityId, userId, storeId, openId);
		System.out.println(object2String(resp));
	}
	
	@Test
	public void getOrderList() {
		orderService.getOrderList(1156347, 1, 0, 10);
	}

    @Test
    public void getOrder() {
        int userId = 48635;
        String orderSnMain = "120108035625905";
        orderService.getPayOrderMsg(userId, orderSnMain);
    }

    @Test
    public void getOrderInfoTest() {
        OrderListRespDto resp = orderService.getOrderInfo(1156347, "150707164298209", 1);
        System.out.println(String.format("resp code = %d", resp.getCode()));
        System.out.println(String.format("main order count = %d", resp.getResult().getOrderCount()));
        for (OrderListDto dto : resp.getResult().getOrderList()) {
            OrderListBaseDto base = dto.getBase();
            List<GoodsListDto> goodsList = dto.getGoodsList();
            ExtmMsgDto extmMsg = dto.getExtmMsg();
            ShippingMsgDto shippingMsg = dto.getShippingmsg();
        }
    }

    @Test
    public void getPayOrderMsgTest() {
        PayOrderResultRespDto resp = orderService.getPayOrderMsg(1156347, "150707164298209");
        int code = resp.getCode();
        PayOrderMsgDto dto = resp.getResult();
    }

    @Test
    public void shareAfterPayTest() {
        ShareRespDto resp = orderService.shareAfterPay("150707164298209");
        int code = resp.getCode();
        ShareResultDto dto = resp.getShareResultDto();
    }

    @Test
    public void testGetOrderInfo() {
//        ResponseDto<OrderListResultDto> result = orderService.getOrderInfo("120108035625905");
//        System.out.println(result);
    }
    
    @Test
	public void returnStorage(){
		ReturnStorageReqDto req = new ReturnStorageReqDto();
		req.setStoreId(1432);
		req.setTaoOrderSn("nj0131021214077861");
		
		ReturnStorageGoodsReqDto[] goodsList = new ReturnStorageGoodsReqDto[3];
		
		ReturnStorageGoodsReqDto goo=new ReturnStorageGoodsReqDto();
		goo.setSpecId(608017);
		goo.setQuantity(10);
		
		ReturnStorageGoodsReqDto goo1=new ReturnStorageGoodsReqDto();
		goo1.setSpecId(610927);
		goo1.setQuantity(11);
		
		ReturnStorageGoodsReqDto goo2=new ReturnStorageGoodsReqDto();
		goo2.setSpecId(622294);
		goo2.setQuantity(12);
		
		goodsList[0]=goo;
		goodsList[1]=goo1;
		goodsList[2]=goo2;
		req.setGoodsList(goodsList);
		
		ReturnStorageRespDto resp = orderService.returnStorage(req);
		System.out.println(object2String(resp));
	}
}
