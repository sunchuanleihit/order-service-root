package com.loukou.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.loukou.order.service.constants.ShortMessage;
import com.loukou.order.service.dao.LkWhDeliveryDao;
import com.loukou.order.service.dao.LkWhDeliveryOrderDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderRefuseDao;
import com.loukou.order.service.dao.WeiCangGoodsStoreDao;
import com.loukou.order.service.entity.LkWhDelivery;
import com.loukou.order.service.entity.LkWhDeliveryOrder;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderRefuse;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.sms.sdk.client.MultiClient;

@Service
public class OrderOperationProcessor {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderActionDao orderActionDao;

    @Autowired
    private OrderExtmDao orderExtmDao;

    @Autowired
    private OrderGoodsDao orderGoodsDao;

    @Autowired
    private LkWhDeliveryDao lkWhDeliveryDao;

    @Autowired
    private WeiCangGoodsStoreDao lkWhGoodsStoreDao;

    @Autowired
    private LkWhDeliveryOrderDao lkWhDeliveryOrderDao;

    @Autowired
    private OrderRefuseDao orderRefuseDao;

    public OResponseDto<String> confirmBookOrder(String taoOrderSn, String userName, int senderId) {
       List<Order> orders = orderDao.findByTaoOrderSn(taoOrderSn);
       
        if (CollectionUtils.isEmpty(orders) || orders.get(0).getStatus() != OrderStatusEnum.STATUS_REVIEWED.getId()) {
            return new OResponseDto<String>(500, "错误的订单号");
        }
        Order order = orders.get(0);
        LkWhDelivery lkDelivery = lkWhDeliveryDao.findByDId(senderId);
        if (lkDelivery == null) {
            return new OResponseDto<String>(500, "错误的快递员");
        }
        String receiveNo = new String("" + (int) (Math.random() * 10000));
        orderDao.updateOrderStatusAndreceiveNo(order.getOrderId(), OrderStatusEnum.STATUS_14.getId(), receiveNo);

        createAction(order, OrderStatusEnum.STATUS_14.getId(), userName, "配送员" + lkDelivery.getdName() + ",手机号"
                + lkDelivery.getdMobile());
        sendMessage(order, String.format(ShortMessage.FINISH_PACKAGE_MESSAGE_MODEL, order.getTaoOrderSn(),
                lkDelivery.getdName(), lkDelivery.getdMobile()));
        return new OResponseDto<String>(200, "成功");
    }

    public OResponseDto<String> confirmRevieveOrder(String taoOrderSn, String gps, String userName) {
        List<Order> orders = orderDao.findByTaoOrderSn(taoOrderSn);
        
        if (CollectionUtils.isEmpty(orders) || orders.get(0).getStatus() != OrderStatusEnum.STATUS_14.getId()) {
            return new OResponseDto<String>(500, "错误的订单号");
        }
        Order order = orders.get(0);
        orderDao.updateStatusAndFinishedTime(OrderStatusEnum.STATUS_FINISHED.getId(), DateUtils.getTime(),
                order.getOrderId());

        createAction(order, OrderStatusEnum.STATUS_FINISHED.getId(), userName, "仓库回单" + gps);

        sendMessage(order, String.format(ShortMessage.FINISH_ORDER_MESSAGE_MODEL, order.getTaoOrderSn()));

        return new OResponseDto<String>(200, "确认成功");
    }

    @Transactional
    public OResponseDto<String> finishPackagingOrder(String taoOrderSn, String userName, int senderId) {
        List<Order> orders = orderDao.findByTaoOrderSn(taoOrderSn);
        if (CollectionUtils.isEmpty(orders)|| orders.get(0).getStatus() != OrderStatusEnum.STATUS_REVIEWED.getId()) {
            return new OResponseDto<String>(500, "错误的订单号");
        }
        Order order = orders.get(0);
        LkWhDelivery lkDelivery = lkWhDeliveryDao.findByDId(senderId);
        if (lkDelivery == null) {
            return new OResponseDto<String>(500, "错误的快递员");
        }

        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for (OrderGoods good : goods) {
            lkWhGoodsStoreDao.updateBySpecIdAndStoreIdAndUpdateTime(good.getSpecId(), good.getStoreId(), new Date(),
                    good.getQuantity(), good.getQuantity());
        }
        LkWhDeliveryOrder lkWhDeliveryOrder = new LkWhDeliveryOrder();
        lkWhDeliveryOrder.setOrderId(order.getOrderId());
        lkWhDeliveryOrder.setOrderNo(order.getOrderSnMain());
        lkWhDeliveryOrder.setdId(senderId);
        lkWhDeliveryOrder.setRemark("仓库发货");
        lkWhDeliveryOrderDao.save(lkWhDeliveryOrder);

        String receiveNo = new String("" + (int) (Math.random() * 10000));
        orderDao.updateOrderStatusAndreceiveNo(order.getOrderId(), OrderStatusEnum.STATUS_14.getId(), receiveNo);

        createAction(order, OrderStatusEnum.STATUS_14.getId(), userName, "配送员" + lkDelivery.getdName() + ",手机号"
                + lkDelivery.getdMobile());
        sendMessage(order, String.format(ShortMessage.FINISH_PACKAGE_MESSAGE_MODEL, order.getTaoOrderSn(),
                lkDelivery.getdName(), lkDelivery.getdMobile()));
        return new OResponseDto<String>(200, "成功");
    }

    @Transactional
    public OResponseDto<String> refuseOrder(String taoOrderSn, String userName, int refuseId, String refuseReason) {
        List<Order> orders = orderDao.findByTaoOrderSn(taoOrderSn);

        if (CollectionUtils.isEmpty(orders) || orders.get(0).getStatus() != OrderStatusEnum.STATUS_REVIEWED.getId()) {
            return new OResponseDto<String>(500, "失败");
        }
        Order order  = orders.get(0);
        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for (OrderGoods good : goods) {
            lkWhGoodsStoreDao.updateBySpecIdAndStoreIdAndUpdateTime(good.getSpecId(), good.getStoreId(), new Date(),
                    good.getQuantity(), good.getQuantity());
        }
        orderDao.updateOrderStatus(order.getOrderId(), OrderStatusEnum.STATUS_REFUSED.getId());

        OrderRefuse orderRefuse = new OrderRefuse();
        // 拒绝原因是其他 refuseId=0
        if (refuseId == 0) {
            orderRefuse.setRefuseId(0);
            orderRefuse.setRefuseReason(refuseReason);
            orderRefuse.setTaoOrderSn(taoOrderSn);
        } else {
            orderRefuse.setRefuseId(1);
            orderRefuse.setRefuseReason(refuseReason);
            orderRefuse.setTaoOrderSn(taoOrderSn);
        }
        orderRefuseDao.save(orderRefuse);

        createAction(order, OrderStatusEnum.STATUS_REFUSED.getId(), userName, "拒收");

        sendMessage(order, String.format(ShortMessage.REFUSE_MESSAGE_MODEL, order.getTaoOrderSn()));

        // 退款暂时不考虑 直接设置拒收状态 客服处理

        return new OResponseDto<String>(200, "成功");
    }

    private OrderAction createAction(Order order, int orderActionID, String actor, String notes) {
        // order_action 只插入一条记录
        OrderAction orderAction = new OrderAction();
        orderAction.setAction(orderActionID);
        orderAction.setOrderSnMain(order.getOrderSnMain());
        orderAction.setTaoOrderSn(order.getTaoOrderSn());
        orderAction.setOrderId(order.getOrderId());
        orderAction.setActor(actor);
        orderAction.setActionTime(new Date());
        orderAction.setNotes(notes);
        orderActionDao.save(orderAction);
        return orderAction;
    }

    private boolean sendMessage(Order order, String messageString) {
        // 发送短信
        List<OrderExtm> orderExm = orderExtmDao.findByOrderSnMain(order.getOrderSnMain());
        if (orderExm != null &&orderExm.size()!=0&& !StringUtils.isEmpty(orderExm.get(0).getPhoneMob())) {
            MultiClient.getProvider(MultiClient.CHUANGLAN_PROVIDER).sendMessage(
                    Lists.newArrayList(orderExm.get(0).getPhoneMob()), messageString);
        }
        return true;
    }
}
