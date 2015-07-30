package com.loukou.order.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loukou.order.AbstractTestObject;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.RefuseReasonListDto;

public class OrderServiceOperationImpTest extends AbstractTestObject {

  @Autowired
  private OrderService orderService;
  
  @Test
  public void testPacking(){
      OResponseDto<String> t = orderService.finishPackagingOrder("150707163715990", "王自成", 1);
      System.out.println(t);
  }
  
  @Test
  public void getRefuseConfig(){
      OResponseDto<RefuseReasonListDto> list = orderService.getRefuseReasonList();
      System.out.println(list.getResult().getReasons().get(0));
  }
  
  
  @Test
  public void testRefuseOrder(){
      OResponseDto<String> t = orderService.refuseOrder("150707163715990","王自成",1,"我就是不想送");
      System.out.println(t);
  }
  
  @Test
  public void testReceiving(){
      OResponseDto<String> t = orderService.confirmRevieveOrder("150707163715990", "12,41", "王自成");
  }
  
  @Test
  public void testBookOrder(){
      orderService.confirmBookOrder("150707163715990", "自成");
  }
  
  @Test
  public void testGetOrder(){
      OResponseDto<OrderInfoDto> t =orderService.getOrderGoodsInfo("150707163715990");
      System.out.println(t.getResult());
  }
}
