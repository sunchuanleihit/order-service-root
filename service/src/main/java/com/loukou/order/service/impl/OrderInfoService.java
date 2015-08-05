package com.loukou.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
import com.loukou.order.service.enums.OrderTypeEnums;
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
        if(order.getType() !=OrderTypeEnums.TYPE_BOOKING.getType()){
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
            OrderAction orderAction = orderActions.get(0);
            orderInfoDto.setCancelTime(DateUtils.date2DateStr2(orderAction.getActionTime()));
            // 添加退货状态
            List<OrderReturn> returns = orderRDao.findByOrderSnMain(order.getOrderSnMain());
            // good_status只要不是４　就是待退货
            if (returns.get(0).getGoodsStatus() != ReturnGoodsStatus.BACKED.getId()) {
                orderInfoDto.setGoodsReturnStatus(1);
            } else {
                orderInfoDto.setGoodsReturnStatus(2);
            }
        } else if (order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
            orderInfoDto.setFinishTime(SDF.format(new Date((long) (order.getFinishedTime()) * 1000)));
        }
        oResultDto.setCode(200);
        oResultDto.setResult(orderInfoDto);
        return oResultDto;
    }

  
}
