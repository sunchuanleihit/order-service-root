package com.loukou.order.service.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.OrderListBaseDto;
import com.loukou.order.service.resp.dto.OrderListDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.PayOrderMsgDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShareResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.OrderListResultDto;
import com.loukou.order.service.resp.dto.ResponseDto;

public class OrderServiceImpTest extends AbstractTestObject {

    @Autowired
    private OrderService orderService;

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
        ResponseDto<OrderListResultDto> result = orderService.getOrderInfo("120108035625905");
        System.out.println(result);
    }
}
