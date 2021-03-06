package com.loukou.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
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
import com.loukou.order.service.enums.PayStatusEnum;
import com.loukou.order.service.enums.ReturnGoodsStatus;
import com.loukou.order.service.req.dto.OrderListParamDto;
import com.loukou.order.service.resp.dto.DeliveryInfo;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsInfoDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.OrderListInfoDto;
import com.loukou.order.service.resp.dto.OrderStatusCountRespDto;
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
        List<Order> taoOrders = orderDao.findByTaoOrderSn(orderNo);
        if (CollectionUtils.isEmpty(taoOrders)) {
            return new OResponseDto<OrderInfoDto>(500, new OrderInfoDto());
        }
        Order order = taoOrders.get(0);
        OResponseDto<OrderInfoDto> oResultDto = new OResponseDto<OrderInfoDto>();
        OrderInfoDto orderInfoDto = new OrderInfoDto();
        List<SpecDto> specList = new ArrayList<SpecDto>();
        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for (OrderGoods good : goods) {
            SpecDto spec = new SpecDto();
            spec.setGoodsInfo(new GoodsInfoDto(good.getProductId(), good.getGoodsName(), good.getGoodsImage()));
            spec.setSpecId(good.getSiteskuId());
            spec.setBuyNum(good.getQuantity());
            spec.setSpecName(good.getGoodsName());
            spec.setSellPrice(good.getPricePurchase());
            specList.add(spec);
        }
        if (order.getType().equals("booking")) {
            orderInfoDto.setIsBooking(1);
            orderInfoDto.setShippingNo(order.getShippingNo());
        } else {
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
            deliveryInfo.setNeedShippingTime(DateUtils.date2DateStr(order.getNeedShiptime()) + " "
                    + order.getNeedShiptimeSlot());
        }

        orderInfoDto.setCreateTime(SDF.format(new Date((long) (order.getAddTime()) * 1000)));
        orderInfoDto.setGoodsAmount(order.getOrderAmount());
        orderInfoDto.setTaoOrderSn(order.getTaoOrderSn());
        orderInfoDto.setOrderStatus(order.getStatus());
        orderInfoDto.setShippingFee(order.getShippingFee());
        orderInfoDto.setSpecList(specList);
        orderInfoDto.setDeliveryInfo(deliveryInfo);
        orderInfoDto.setStoreId(order.getSellerId());
        orderInfoDto.setPayStatus(order.getPayStatus());

        if (!Strings.isNullOrEmpty(order.getShippingNo())) {
            orderInfoDto.setShippingNo(order.getShippingNo());
        }
        // 各个状态需要加一些特殊字段
        if (order.getStatus() == OrderStatusEnum.STATUS_INVALID.getId()) {
            OrderRefuse orderRefuse = orderRefuseDao.findByTaoOrderSn(order.getTaoOrderSn());
            if (orderRefuse != null) {
                orderInfoDto.setRejectReason(orderRefuse.getRefuseReason());
                orderInfoDto.setRejectTime(DateUtils.date2DateStr(orderRefuse.getRefuseTime()));
            }
        } else if (order.getStatus() == OrderStatusEnum.STATUS_CANCELED.getId()) {
            List<OrderAction> orderActions = orderActionDao.findByTaoOrderSnAndAction(order.getTaoOrderSn(),
                    OrderStatusEnum.STATUS_CANCELED.getId());
            if (!CollectionUtils.isEmpty(orderActions)) {
                OrderAction orderAction = orderActions.get(0);
                orderInfoDto.setCancelTime(DateUtils.date2DateStr2(orderAction.getActionTime()));
                // 添加退货状态
                List<OrderReturn> returns = orderRDao.findByOrderSnMain(order.getOrderSnMain());
                if (!CollectionUtils.isEmpty(returns)) {
                    // good_status只要不是４　就是待退货
                    if (returns.get(0).getGoodsStatus() != ReturnGoodsStatus.BACKED.getId()) {
                        orderInfoDto.setGoodsReturnStatus(1);
                    } else {
                        orderInfoDto.setGoodsReturnStatus(2);
                    }
                } else {
                    // 没有状态　　直接就是已退货
                    orderInfoDto.setGoodsReturnStatus(2);
                }

            }

        } else if (order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
            orderInfoDto.setFinishTime(SDF.format(new Date((long) (order.getFinishedTime()) * 1000)));
            orderInfoDto.setDeliverResult(calDelivertResult(order.getNeedShiptime(), order.getNeedShiptimeSlot(),
                    order.getFinishedTime()));
        }
        if(orderInfoDto.getOrderStatus()==OrderStatusEnum.STATUS_PACKAGED.getId()){
            //打包状态只有在预售单存在，直接置成待审核,方便做map
            orderInfoDto.setOrderStatus(OrderStatusEnum.STATUS_REVIEWED.getId());
        }
        oResultDto.setCode(200);
        oResultDto.setResult(orderInfoDto);
        return oResultDto;
    }

    public OResponseDto<OrderListInfoDto> getOrderListInfo(OrderListParamDto param) {
        // 按照正常的页码1,2,3,4,5传入 1,2,3,4,5传出
        PageRequest pagenation = new PageRequest((Math.max(1, param.getPageNum()) - 1), param.getPageSize(), new Sort(
                Sort.Direction.DESC, "orderId"));
        List<String> types = new ArrayList<String>();
        // 待送货和已完成　　都需要预售订单
        if (param.getOrderStatus() == OrderStatusEnum.STATUS_14.getId()
                || param.getOrderStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
            types.add("booking");
        }
        switch (param.getOrderType()) {
        case 1:// 微仓
            types.add("wei_wh");
            types.add("wei_self");
            break;
        case 2:// 预售
            types.add("booking");
            break;
        default:
            types.add("wei_wh");
            types.add("wei_self");
            break;
        }
        Page<Order> orders;
        Page<OrderReturn> orderReturns = new PageImpl<OrderReturn>(new ArrayList<OrderReturn>());
        if (param.getOrderType() == 2) {
            //待处理的预售订单包含　打包和审核好的
            if(param.getOrderStatus() == OrderStatusEnum.STATUS_REVIEWED.getId()){
                List<Integer> status = Lists.newArrayList( OrderStatusEnum.STATUS_REVIEWED.getId(), OrderStatusEnum.STATUS_PACKAGED.getId());
                orders = orderDao.findBySellerIdAndStatusInAndTypeIn(param.getStoreId(), status, types,
                        pagenation);
            }else{
                orders = orderDao.findBySellerIdAndStatusAndTypeIn(param.getStoreId(), param.getOrderStatus(), types,
                        pagenation);
            }
        } else if(param.getOrderStatus() == OrderStatusEnum.STATUS_CANCELED.getId()){
            //用户取消的需要通过客服系统退货 走退货表 (订单表中状态依旧是回单)　然后通过orderId反查订单表
            orderReturns = orderRDao.findBySellerIdAndGoodsStatusIn(param.getStoreId(), Lists.newArrayList(0,1,2,3),pagenation);
            List<Order> orderDaoList = new ArrayList<Order>();
            List<Order> orderList = new ArrayList<Order>();
            List<Integer> orderIds = new ArrayList<Integer>();
            for(OrderReturn r :orderReturns.getContent()){
                if(r.getOrderId()!=null){
                    orderIds.add(r.getOrderId());
                }
               
            }
            if(!CollectionUtils.isEmpty(orderIds)){
                orderDaoList = orderDao.findByOrderIdIn(orderIds);
            }
            //因为可以多次退单，这里需要重新build一遍
            for(Integer i:orderIds){
                for(Order o: orderDaoList){
                    if(i==o.getOrderId()){
                        orderList.add(o);
                    }
                }
            }
            orders =new PageImpl<Order>(orderList);
        }else {
            if (param.getOrderStatus() == OrderStatusEnum.STATUS_FINISHED.getId()
                    && !StringUtils.isBlank(param.getFinishedTime())) {
                long startTime = 0;
                long endTime = 0;
                try {
                    startTime = DateUtils.str2Date(param.getFinishedTime()).getTime() / 1000;
                    endTime = startTime + 86400;
                } catch (Exception e) {
                    LogFactory.getLog(OrderInfoService.class).error("parsing time error ", e);
                }
                orders = orderDao.findBySellerIdAndStatusAndFinishedTimeBetweenAndTypeIn(param.getStoreId(),
                        param.getOrderStatus(), (int) startTime, (int) endTime, types, pagenation);
            }else if(param.getOrderStatus() == OrderStatusEnum.STATUS_INVALID.getId()){
                //无效状态  分为 
                //1.   2小时未付款被job自动设置为无效
                //2.   商家拒绝订单，设置为无效 
                //这里只为商家展示拒绝的订单，所以使用付款状态
                List<Integer> payed = Lists.newArrayList(PayStatusEnum.STATUS_PART_PAYED.getId(),
                        PayStatusEnum.STATUS_PAYED.getId());
                orders = orderDao.findBysellerIdAndStatusAndPayStatusInAndTypeIn(param.getStoreId(),
                        param.getOrderStatus(), payed, types, pagenation);
            }else {
                orders = orderDao.findBySellerIdAndStatusAndTypeIn(param.getStoreId(), param.getOrderStatus(), types,
                        pagenation);
            }
        }
        OrderListInfoDto orderListInfoDto = new OrderListInfoDto();
        List<OrderInfoDto> orderInfoDtos = new ArrayList<OrderInfoDto>();

        if (CollectionUtils.isEmpty(orders.getContent())) {
            orderListInfoDto.setOrders(orderInfoDtos);
            orderListInfoDto.setStoreId(param.getStoreId());
            orderListInfoDto.setTotalNum(orders.getTotalElements());
            orderListInfoDto.setPageNum(param.getPageNum());
            return new OResponseDto<OrderListInfoDto>(200, orderListInfoDto);
        }
        // 订单id
        List<Integer> orderIds = new ArrayList<Integer>();
        // 订单id 对应的tao_order_sn可反向查询
        HashBiMap<Integer, String> biMap = HashBiMap.create();
        for (Order o : orders.getContent()) {
            orderIds.add(o.getOrderId());
            biMap.put(o.getOrderId(), o.getTaoOrderSn());
        }
        // 封装基本信息
        Map<Integer, OrderInfoDto> map = new HashMap<Integer, OrderInfoDto>();
        for (Order o : orders.getContent()) {
            OrderInfoDto value = new OrderInfoDto();
            value.setCreateTime(SDF.format(new Date((long) (o.getAddTime()) * 1000)));
            value.setGoodsAmount(o.getOrderAmount());
            value.setTaoOrderSn(o.getTaoOrderSn());
            value.setOrderStatus(o.getStatus());

            if (o.getType().equals("booking")) {
                value.setIsBooking(1);
                value.setShippingNo(o.getShippingNo());
            } else {
                value.setIsBooking(0);
            }

            map.put(o.getOrderId(), value);
        }

        List<OrderGoods> goods = orderGoodsDao.findByOrderIdIn(orderIds);
        if (!CollectionUtils.isEmpty(goods)) {
            for (OrderGoods ordergood : goods) {
                OrderInfoDto value = map.get(ordergood.getOrderId());
                if (value == null) {
                    continue;
                }
                SpecDto spec = new SpecDto();
                spec.setGoodsInfo(new GoodsInfoDto(ordergood.getProductId(), ordergood.getGoodsName(), ordergood.getGoodsImage()));
                spec.setSpecId(ordergood.getSiteskuId());
                spec.setBuyNum(ordergood.getQuantity());
                spec.setSellPrice(ordergood.getPricePurchase());
                value.getSpecList().add(spec);
            }
        }

        // 根据请求的订单状态添加附加属性
        if (param.getOrderStatus() == OrderStatusEnum.STATUS_REVIEWED.getId()) {

        } else if (param.getOrderStatus() == OrderStatusEnum.STATUS_CANCELED.getId()) {
        } else if (param.getOrderStatus() == OrderStatusEnum.STATUS_14.getId()) {

        } else if (param.getOrderStatus() == OrderStatusEnum.STATUS_INVALID.getId()) {
            List<OrderRefuse> orderRefuses = orderRefuseDao.findByTaoOrderSnIn(biMap.values());

            if (!CollectionUtils.isEmpty(orderRefuses)) {
                for (OrderRefuse refuse : orderRefuses) {
                    OrderInfoDto value = map.get(biMap.inverse().get(refuse.getTaoOrderSn()));
                    value.setRejectReason(refuse.getRefuseReason());
                    value.setRejectTime(DateUtils.date2DateStr(refuse.getRefuseTime()));
                }
            }
        } else if (param.getOrderStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
            for (Order order : orders.getContent()) {
                OrderInfoDto value = map.get(order.getOrderId());
                value.setFinishTime(DateUtils.date2DateStr2(new Date((long) (order.getFinishedTime()) * 1000)));
                value.setDeliverResult(calDelivertResult(order.getNeedShiptime(), order.getNeedShiptimeSlot(),
                        order.getFinishedTime()));
            }
        }

        List<String> orderSnMainList = new ArrayList<String>();
        for (Order o : orders.getContent()) {
            orderSnMainList.add(o.getOrderSnMain());
        }
        List<OrderExtm> orderExtmLists = orderExtmDao.findByOrderSnMainIn(orderSnMainList);
        if (!CollectionUtils.isEmpty(orderExtmLists)) {
            for (Order o : orders.getContent()) {
                for (OrderExtm m : orderExtmLists) {
                    if (o.getOrderSnMain().equals(m.getOrderSnMain())) {
                        OrderInfoDto value = map.get(o.getOrderId());
                        DeliveryInfo deliveryInfo = new DeliveryInfo();
                        deliveryInfo.setAddress(m.getRegionName() + m.getAddress());
                        deliveryInfo.setConsignee(m.getConsignee());
                        deliveryInfo.setTel(m.getPhoneMob());
                        deliveryInfo.setNeedShippingTime(DateUtils.date2DateStr(o.getNeedShiptime()) + " "
                                + o.getNeedShiptimeSlot());
                        value.setDeliveryInfo(deliveryInfo);
                    }
                }
            }
        }

        for(Order  o :orders.getContent()){
            OrderInfoDto dto = map.get(o.getOrderId());
            if(dto !=null){
                orderInfoDtos.add(dto);
                //内部状态要重置掉
                dto.setOrderStatus(param.getOrderStatus());
                if(param.getOrderStatus()  == OrderStatusEnum.STATUS_CANCELED.getId()){
                    List<OrderReturn> copyList = Lists.newArrayList(orderReturns.getContent());
                    int removeId=-1 ;
                    for(int i =0;i<copyList.size();i++){
                            if(o.getOrderId() ==copyList.get(i).getOrderId()){
                                if(copyList.get(i).getGoodsStatus()!=4){
                                   dto.setGoodsReturnStatus(1);
                                }else{
                                    dto.setGoodsReturnStatus(2);
                                }
                                dto.setCancelTime(copyList.get(i).getAddTime());
                               removeId =i;
                               break;
                            }
                    }
                 
                    if(removeId!=-1){
                        copyList.remove(removeId);
                    }
                }
            }
        }
        orderListInfoDto.setOrders(orderInfoDtos);
        orderListInfoDto.setStoreId(param.getStoreId());
        orderListInfoDto.setTotalNum(orders.getTotalElements());
        orderListInfoDto.setPageNum(param.getPageNum());

        return new OResponseDto<OrderListInfoDto>(200, orderListInfoDto);
    }

    private int calDelivertResult(Date needShipTime, String needShipSlot, int finishedTime) {
        if(needShipTime ==null || needShipSlot ==null){
            return 0;
        }
        String timeString = new SimpleDateFormat("yyyy-MM-dd").format(needShipTime);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        Iterable<String> times = Splitter.on("-").split(needShipSlot);
        @SuppressWarnings("unchecked")
        List<String> timeslots = IteratorUtils.toList(times.iterator());
        if (timeslots.size() < 2) {
            return 0;
        }

        try {
        	// FIXME 这里有个trick, 数据库里有配送时间区间为23:00-24:00，24:00 解析有错，所以用前面的时间+1小时
//        	DateTime endDate = DateTime.parse((timeString + " " + timeslots.get(1)), formatter);
            DateTime endDate = DateTime.parse((timeString + " " + timeslots.get(0)), formatter).plusHours(1);
            DateTime finishDate = DateTime.parse(SDF.format(new Date((Long.valueOf(finishedTime) * 1000))),
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

            if (finishDate.isAfter(endDate.getMillis())) {
                return 3; // 延迟送达
            } else {
                return 2; // 及时送达
            }
        } catch (Exception e) {
            LogFactory.getLog(OrderInfoService.class).error("parsing time error", e);
        }
        return 0;
    }
    public OResponseDto<OrderStatusCountRespDto> getOrderCount(int storeId){
        OrderStatusCountRespDto dto = new OrderStatusCountRespDto();
        dto.setOrderStatusBookingRecieve(orderDao.findCount(storeId, 3, Lists.newArrayList("booking")));
        dto.setOrderStatusReviewed(orderDao.findCount(storeId, 3, Lists.newArrayList("wei_wh","wei_self")));;
        return new OResponseDto<OrderStatusCountRespDto>(200, dto);
    }
    
    /**
     * 默认0时返回5种查询状态
     */
    public OResponseDto<OrderListInfoDto> getWhAvailableOrders(OrderListParamDto param){
        PageRequest pagenation = new PageRequest((Math.max(1, param.getPageNum()) - 1), param.getPageSize(), new Sort(
                Sort.Direction.DESC, "orderId"));
        List<Integer> status = new ArrayList<Integer>();
        if(param.getOrderStatus() == 0){
            status.add(OrderStatusEnum.STATUS_REVIEWED.getId());
            status.add(OrderStatusEnum.STATUS_14.getId());
            status.add(OrderStatusEnum.STATUS_CANCELED.getId());
            status.add(OrderStatusEnum.STATUS_FINISHED.getId());
        }else{
            status.add(param.getOrderStatus());
        }
     
        Page<Order> orders = orderDao.findBySellerIdAndStatusIn(param.getStoreId(), status,pagenation);
        
        OrderListInfoDto orderListInfoDto = new OrderListInfoDto();
        List<OrderInfoDto> orderInfoDtos = new ArrayList<OrderInfoDto>();
        
        if (CollectionUtils.isEmpty(orders.getContent())) {
            orderListInfoDto.setOrders(orderInfoDtos);
            orderListInfoDto.setStoreId(param.getStoreId());
            orderListInfoDto.setTotalNum(orders.getTotalElements());
            orderListInfoDto.setPageNum(param.getPageNum());
            return new OResponseDto<OrderListInfoDto>(200, orderListInfoDto);
        }
        
     // 封装基本信息
        List<Integer> orderIds = new ArrayList<Integer>();
        Map<Integer, OrderInfoDto> map = new HashMap<Integer, OrderInfoDto>();
        HashBiMap<Integer, String> biMap = HashBiMap.create();
        for (Order o : orders.getContent()) {
            orderIds.add(o.getOrderId());
            biMap.put(o.getOrderId(), o.getTaoOrderSn());
        }
        for (Order o : orders.getContent()) {
            OrderInfoDto value = new OrderInfoDto();
            value.setCreateTime(SDF.format(new Date((long) (o.getAddTime()) * 1000)));
            value.setGoodsAmount(o.getOrderAmount());
            value.setTaoOrderSn(o.getTaoOrderSn());
            value.setOrderStatus(o.getStatus());
            value.setPayStatus(o.getPayStatus());
            if (o.getType().equals("booking")) {
                value.setIsBooking(1);
                value.setShippingNo(o.getShippingNo());
            } else {
                value.setIsBooking(0);
            }

            map.put(o.getOrderId(), value);
        }
        
        List<OrderGoods> goods = orderGoodsDao.findByOrderIdIn(orderIds);
        if (!CollectionUtils.isEmpty(goods)) {
            for (OrderGoods ordergood : goods) {
                OrderInfoDto value = map.get(ordergood.getOrderId());
                if (value == null) {
                    continue;
                }
                SpecDto spec = new SpecDto();
                spec.setGoodsInfo(new GoodsInfoDto(ordergood.getProductId(), ordergood.getGoodsName(), ordergood.getGoodsImage()));
                spec.setSpecId(ordergood.getSiteskuId());
                spec.setBuyNum(ordergood.getQuantity());
                spec.setSellPrice(ordergood.getPricePurchase());
                value.getSpecList().add(spec);
            }
        }
        List<String> orderSnMainList = new ArrayList<String>();
        for (Order o : orders.getContent()) {
            orderSnMainList.add(o.getOrderSnMain());
        }
        List<OrderExtm> orderExtmLists = orderExtmDao.findByOrderSnMainIn(orderSnMainList);
        if (!CollectionUtils.isEmpty(orderExtmLists)) {
            for (Order o : orders.getContent()) {
                for (OrderExtm m : orderExtmLists) {
                    if (o.getOrderSnMain().equals(m.getOrderSnMain())) {
                        OrderInfoDto value = map.get(o.getOrderId());
                        DeliveryInfo deliveryInfo = new DeliveryInfo();
                        deliveryInfo.setAddress(m.getRegionName() + m.getAddress());
                        deliveryInfo.setConsignee(m.getConsignee());
                        deliveryInfo.setTel(m.getPhoneMob());
                        deliveryInfo.setNeedShippingTime(DateUtils.date2DateStr(o.getNeedShiptime()) + " "
                                + o.getNeedShiptimeSlot());
                        value.setDeliveryInfo(deliveryInfo);
                    }
                }
            }
        }

        for(Order  o :orders.getContent()){
            OrderInfoDto dto = map.get(o.getOrderId());
            if(dto !=null){
                orderInfoDtos.add(dto);
            }
        }

        orderListInfoDto.setOrders(orderInfoDtos);
        orderListInfoDto.setStoreId(param.getStoreId());
        orderListInfoDto.setTotalNum(orders.getTotalElements());
        orderListInfoDto.setPageNum(param.getPageNum());
        return new OResponseDto<OrderListInfoDto>(200, orderListInfoDto);
        
    }
}
