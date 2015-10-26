package com.loukou.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Splitter;
import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.req.dto.OrderListParamDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.OrderListInfoDto;

public class OrderServiceOperationImpTest extends AbstractTestObject {

    @Autowired
    private OrderService orderService;

    @Test
    public void testPacking() {
        OResponseDto<String> t = orderService.finishPackagingOrder("150707163715990", "王自成", 1);
        System.out.println(t);
    }

    @Test
    public void testRefuseOrder() {
        OResponseDto<String> t = orderService.refuseOrder("150707163715990", "王自成", 1, "我就是不想送");
        System.out.println(t);
    }

    @Test
    public void testReceiving() {
        OResponseDto<String> t = orderService.confirmRevieveOrder("150707163715990", "12,41", "王自成");
    }

    @Test
    public void testBookOrder() {
        orderService.confirmBookOrder("150707163715990", "自成", 1);
    }

    @Test
    public void testGetOrder() {
        OResponseDto<OrderInfoDto> t = orderService.getOrderGoodsInfo("150707163715990");
        System.out.println(t.getResult());
    }

    @Test
    public void testGetOrderListInfo() {
        OrderListParamDto param = new OrderListParamDto();
        param.setOrderStatus(OrderStatusEnum.STATUS_CANCELED.getId());
        param.setOrderType(1);
        param.setPageNum(1);
        param.setPageSize(4);
        param.setStoreId(20184);
        OResponseDto<OrderListInfoDto> list = orderService.getOrderListInfo(param);
        for(OrderInfoDto t :list.getResult().getOrders()){
            System.out.println(t.getTaoOrderSn());
        }
    }

    @Test
    public void testFinishedDate() {
        String t = "2015-08-27";
        OrderListParamDto param = new OrderListParamDto();
        param.setOrderStatus(OrderStatusEnum.STATUS_FINISHED.getId());
        param.setOrderType(1);
        param.setPageNum(0);
        param.setPageSize(4);
        param.setStoreId(18017);
        param.setFinishedTime("2015-06-17");
        OResponseDto<OrderListInfoDto> list = orderService.getOrderListInfo(param);
        System.out.println(list.getResult().getTotalNum());
    }

    @Test
    public void testIsIntime() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        String t = "2015-06-17";
        String slot = "8:00-9:00";
        Iterable<String> times = Splitter.on("-").split(slot);
        List<String> timeslots = IteratorUtils.toList(times.iterator());
        if (timeslots.size() < 2) {
            System.out.println(0);
        }

        DateTime startDate = DateTime.parse((t + " " + timeslots.get(0)), formatter);
        DateTime endDate = DateTime.parse((t + " " + timeslots.get(1)), formatter);
        DateTime finishDate = DateTime.parse(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((Long.valueOf(0) * 1000))),
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(finishDate);
        // 提早送达
        if (finishDate.isBefore(startDate.getMillis())) {
            System.out.println(1);
        } else if (finishDate.isAfter(endDate.getMillis())) {
            System.out.println(2);
        } else {
            System.out.println(3);
        }

    }
}
