package com.loukou.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Splitter;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderRefuseDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderRefuse;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.ReturnGoodsStatus;
import com.loukou.order.service.req.dto.OrderListParamDto;
import com.loukou.order.service.resp.dto.DeliveryInfo;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsInfoDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.OrderListInfoDto;
import com.loukou.order.service.resp.dto.SpecDto;
import com.loukou.order.service.util.DateUtils;

@Service
public class OrderInfoService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderGoodsDao orderGoodsDao;

    @Autowired
    private OrderExtmDao orderExtmDao;

    private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private OrderRefuseDao orderRefuseDao;

    @Autowired
    private OrderActionDao orderActionDao;

    @Autowired
    private OrderReturnDao orderRDao;

    public OResponseDto<OrderInfoDto> getOrderGoodsInfo(String orderNo) {
        Order order = orderDao.findByTaoOrderSn(orderNo);
        if (order == null) {
            return new OResponseDto<OrderInfoDto>(500, new OrderInfoDto());
        }
        OResponseDto<OrderInfoDto> oResultDto = new OResponseDto<OrderInfoDto>();
        OrderInfoDto orderInfoDto = new OrderInfoDto();
        List<SpecDto> specList = new ArrayList<SpecDto>();
        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for (OrderGoods good : goods) {
            SpecDto spec = new SpecDto();
            spec.setGoodsInfo(new GoodsInfoDto(good.getGoodsId(), good.getGoodsName(), good.getGoodsImage()));
            spec.setSpecId(good.getSpecId());
            spec.setBuyNum(good.getQuantity());
            spec.setSpecName(good.getGoodsName());
            spec.setSellPrice(good.getPricePurchase());
            specList.add(spec);
        }
        if(order.getType().equals("booking")){
            orderInfoDto.setIsBooking(1);
        }else{
            orderInfoDto.setIsBooking(0);
        }
        // 实际上一个主单只有一个收货人
        List<OrderExtm> orderExtmList = orderExtmDao.findByOrderSnMain(order.getOrderSnMain());
        ExtmMsgDto extmMsgDto = new ExtmMsgDto();
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        if (!CollectionUtils.isEmpty(orderExtmList)) {
            OrderExtm orderExtm = orderExtmList.get(0);
            // 封装收货人等信息
            extmMsgDto.setAddress(orderExtm.getAddress());
            extmMsgDto.setConsignee(orderExtm.getConsignee());
            extmMsgDto.setPhoneMob(orderExtm.getPhoneMob());

            deliveryInfo.setAddress(orderExtm.getRegionName() + orderExtm.getAddress());
            deliveryInfo.setConsignee(orderExtm.getConsignee());
            deliveryInfo.setTel(orderExtm.getPhoneMob());
            deliveryInfo.setNeedShippingTime(DateUtils.date2DateStr(order.getNeedShiptime())+" "+order.getNeedShiptimeSlot());
        }

        orderInfoDto.setCreateTime(SDF.format(new Date((long) (order.getAddTime()) * 1000)));
        orderInfoDto.setGoodsAmount(order.getOrderAmount());
        orderInfoDto.setTaoOrderSn(order.getTaoOrderSn());
        orderInfoDto.setOrderStatus(order.getStatus());
        orderInfoDto.setShippingFee(order.getShippingFee());
        orderInfoDto.setSpecList(specList);
        orderInfoDto.setDeliveryInfo(deliveryInfo);

        
        // 各个状态需要加一些特殊字段
        if (order.getStatus() == OrderStatusEnum.STATUS_REFUSED.getId()) {
            OrderRefuse orderRefuse = orderRefuseDao.findByTaoOrderSn(order.getTaoOrderSn());
            if(orderRefuse !=null){
                orderInfoDto.setRejectReason(orderRefuse.getRefuseReason());
                orderInfoDto.setRejectTime(DateUtils.date2DateStr(orderRefuse.getRefuseTime()));
            }
        } else if (order.getStatus() == OrderStatusEnum.STATUS_CANCELED.getId()) {
            List<OrderAction> orderActions = orderActionDao.findByTaoOrderSnAndAction(order.getTaoOrderSn(),
                    OrderStatusEnum.STATUS_CANCELED.getId());
            if(!CollectionUtils.isEmpty(orderActions)){
                OrderAction orderAction = orderActions.get(0);
                orderInfoDto.setCancelTime(DateUtils.date2DateStr2(orderAction.getActionTime()));
                // 添加退货状态
                List<OrderReturn> returns = orderRDao.findByOrderSnMain(order.getOrderSnMain());
                // good_status只要不是４　就是待退货
                if (returns.get(0).getGoodsStatus() != ReturnGoodsStatus.BACKED.getId()) {
                    orderInfoDto.setGoodsReturnStatus("待退货");
                } else {
                    orderInfoDto.setGoodsReturnStatus("已退货");
                }
            }
            
        } else if (order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
            orderInfoDto.setFinishTime(SDF.format(new Date((long) (order.getFinishedTime()) * 1000)));
            orderInfoDto.setDeliverResult(isIntime(order.getNeedShiptime(),order.getNeedShiptimeSlot(),order.getFinishedTime()));
        }
        oResultDto.setCode(200);
        oResultDto.setResult(orderInfoDto);
        return oResultDto;
    }
    public OResponseDto<OrderListInfoDto> getOrderListInfo(OrderListParamDto param) {
        //按照正常的页码1,2,3,4,5传入 1,2,3,4,5传出
        PageRequest pagenation = new PageRequest((Math.max(1,param.getPageNum())-1),param.getPageSize());
        List<String> types = new ArrayList<String>();
        switch (param.getOrderType()) {
        case 1://微仓
            types.add("wei_wh");
            types.add("wei_self");
            break;
        case 2://预售
            types.add("booking");
            break;
        default:
            types.add("wei_wh");
            types.add("wei_self");
            break;
        }
        Page<Order> orders ;
        if(param.getOrderType() == 2){
            orders = orderDao.findBySellerIdAndStatusAndTypeIn(param.getStoreId(),param.getOrderStatus(),types,pagenation);
        }else{
            if(param.getOrderStatus() == OrderStatusEnum.STATUS_FINISHED.getId()&&!StringUtils.isBlank(param.getFinishedTime())){
                long startTime =0;
                long endTime =0;
                try {
                    startTime = DateUtils.str2Date(param.getFinishedTime()).getTime()/1000;
                    endTime = startTime+86400;
                } catch (Exception e) {
                    // TODO: handle exception
                }
               orders = orderDao.findBySellerIdAndStatusAndFinishedTimeBetweenAndTypeIn(param.getStoreId(),param.getOrderStatus(),(int)startTime,(int)endTime,types,pagenation);
            }else {
                orders  =orderDao.findBySellerIdAndStatusAndTypeIn(param.getStoreId(),param.getOrderStatus(),types,pagenation);
            }
        }
        OrderListInfoDto orderListInfoDto = new OrderListInfoDto();
        List<OrderInfoDto> orderInfoDtos = new ArrayList<OrderInfoDto>();
        
        if(CollectionUtils.isEmpty(orders.getContent())){
            orderListInfoDto.setOrders(orderInfoDtos);
            orderListInfoDto.setStoreId(param.getStoreId());
            orderListInfoDto.setTotalNum(orders.getTotalElements());
            orderListInfoDto.setPageNum(param.getPageNum());
            return new OResponseDto<OrderListInfoDto>(200,orderListInfoDto);
        }
        for(Order order :orders.getContent()){
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setCreateTime(SDF.format(new Date((long)(order.getAddTime())*1000)));
            orderInfoDto.setGoodsAmount(order.getOrderAmount());
            orderInfoDto.setTaoOrderSn(order.getTaoOrderSn());
            orderInfoDto.setOrderStatus(order.getStatus());
            
            List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
            List<SpecDto> specList = new ArrayList<SpecDto>();
            for(OrderGoods good :goods){
                SpecDto spec = new SpecDto();
                spec.setGoodsInfo(new GoodsInfoDto(good.getGoodsId(), good.getGoodsName(), good.getGoodsImage()));
                spec.setSpecId(good.getSpecId());
                spec.setBuyNum(good.getQuantity());
                specList.add(spec);
            }
            orderInfoDto.setSpecList(specList);
           //是否时预售商品
            if(order.getType().equals("booking")){
                orderInfoDto.setIsBooking(1);
            }else{
                orderInfoDto.setIsBooking(0);
            }
           if(order.getStatus() == OrderStatusEnum.STATUS_REVIEWED.getId()){
               
           }else if(order.getStatus() ==OrderStatusEnum.STATUS_CANCELED.getId()){
               List<OrderAction> orderActions =  orderActionDao.findByTaoOrderSnAndAction(order.getTaoOrderSn(),OrderStatusEnum.STATUS_CANCELED.getId());
               if(!CollectionUtils.isEmpty(orderActions)){
                   OrderAction orderAction =orderActions.get(0);
                   orderInfoDto.setCancelTime(DateUtils.date2DateStr2(orderAction.getActionTime()));
                   //添加退货状态
                   List<OrderReturn> returns =  orderRDao.findByOrderSnMain(order.getOrderSnMain());
                   //good_status只要不是４　就是待退货
                   if(returns.get(0).getGoodsStatus()!=4){
                       orderInfoDto.setGoodsReturnStatus("待退货");
                   }else{
                       orderInfoDto.setGoodsReturnStatus("已退货");
                   }
               }
            
           }else if(order.getStatus() == OrderStatusEnum.STATUS_14.getId()) {
             
           }else if(order.getStatus() == OrderStatusEnum.STATUS_REFUSED.getId()){
               OrderRefuse orderRefuse =  orderRefuseDao.findByTaoOrderSn(order.getTaoOrderSn());
               if(orderRefuse != null){
                   orderInfoDto.setRejectReason(orderRefuse.getRefuseReason());
                   orderInfoDto.setRejectTime(DateUtils.date2DateStr(orderRefuse.getRefuseTime()));
               }
               
           }else if(order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()){
               orderInfoDto.setFinishTime(DateUtils.date2DateStr2(new Date((long)(order.getFinishedTime())*1000)));
               orderInfoDto.setDeliverResult(isIntime(order.getNeedShiptime(),order.getNeedShiptimeSlot(),order.getFinishedTime()));
           }
           
           
           List<OrderExtm> orderExtmList = orderExtmDao.findByOrderSnMain(order.getOrderSnMain());
           DeliveryInfo deliveryInfo = new DeliveryInfo();
           if (!CollectionUtils.isEmpty(orderExtmList)) {
               OrderExtm orderExtm = orderExtmList.get(0);
               // 封装收货人等信息

               deliveryInfo.setAddress(orderExtm.getRegionName() + orderExtm.getAddress());
               deliveryInfo.setConsignee(orderExtm.getConsignee());
               deliveryInfo.setTel(orderExtm.getPhoneMob());
               deliveryInfo.setNeedShippingTime(DateUtils.date2DateStr(order.getNeedShiptime())+" "+order.getNeedShiptimeSlot());
           }
           orderInfoDto.setDeliveryInfo(deliveryInfo);
           
           
           
           orderInfoDtos.add(orderInfoDto);
        }
        orderListInfoDto.setOrders(orderInfoDtos);
        orderListInfoDto.setStoreId(param.getStoreId());
        orderListInfoDto.setTotalNum(orders.getTotalElements());
        orderListInfoDto.setPageNum(param.getPageNum());
        
        return new OResponseDto<OrderListInfoDto>(200,orderListInfoDto);
    }
  
    private int isIntime(Date needShipTime,String needShipSlot,int  finishedTime){
        String timeString  = new SimpleDateFormat("yyyy-MM-dd").format(needShipTime);
        DateTimeFormatter formatter = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm");
        Iterable<String> times = Splitter.on("-").split(needShipSlot);
        List<String> timeslots = IteratorUtils.toList(times.iterator());
        if(timeslots.size()<2){
            return 0;
        }
        
        try{
            DateTime startDate = DateTime.parse((timeString+" "+timeslots.get(0)),formatter);
            DateTime endDate = DateTime.parse((timeString+" "+timeslots.get(1)),formatter);
            DateTime finishDate = DateTime.parse(SDF.format(new Date((Long.valueOf(finishedTime)*1000))));
            //提早送达
            if(finishDate.isBefore(startDate.getMillis())){
                return 1;
            }else if(finishDate.isAfter(endDate.getMillis())){
                return 2;
            }else {
                return 3;
            }
        }catch(Exception e){
            
        }
        return 0;
    }
}
