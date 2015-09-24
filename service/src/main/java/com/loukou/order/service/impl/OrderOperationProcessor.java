package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.loukou.order.service.constants.ShortMessage;
import com.loukou.order.service.dao.LKWhStockInDao;
import com.loukou.order.service.dao.LKWhStockInGoodsDao;
import com.loukou.order.service.dao.LkWhDeliveryDao;
import com.loukou.order.service.dao.LkWhDeliveryOrderDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderRefuseDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.WeiCangGoodsStoreDao;
import com.loukou.order.service.entity.LKWhStockIn;
import com.loukou.order.service.entity.LKWhStockInGoods;
import com.loukou.order.service.entity.LkWhDelivery;
import com.loukou.order.service.entity.LkWhDeliveryOrder;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderRefuse;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.WeiCangGoodsStore;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.OrderReturnGoodsStatusEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.WeiCangGoodsStoreStatusEnum;
import com.loukou.order.service.req.dto.ReturnStorageGoodsReqDto;
import com.loukou.order.service.req.dto.ReturnStorageReqDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.ReturnStorageRespDto;
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
    @Autowired
	private OrderReturnDao orderRDao;
	@Autowired
	private LKWhStockInDao whStockInDao;
	
	@Autowired
	private LKWhStockInGoodsDao whStockInGoodsDao;

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
        int num  = orderDao.updateOrderStatusAndreceiveNo(order.getOrderId(), OrderStatusEnum.STATUS_14.getId(), receiveNo);
        if(num <1 ){
            return new OResponseDto<String>(500, "错误的订单号");
        }
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
        int num = orderDao.updateStatusAndFinishedTime(OrderStatusEnum.STATUS_FINISHED.getId(), DateUtils.getTime(),
                order.getOrderId());
        if(num < 1){
            return new OResponseDto<String>(500, "错误的订单号成功");
        }
        createAction(order, OrderStatusEnum.STATUS_FINISHED.getId(), userName, "仓库回单" + gps);

        sendMessage(order, String.format(ShortMessage.FINISH_ORDER_MESSAGE_MODEL, order.getTaoOrderSn()));

        return new OResponseDto<String>(200, "确认成功");
    }

    @Transactional
    public OResponseDto<String> finishPackagingOrder(String taoOrderSn, String userName, int senderId) {
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
        int num = orderDao.updateOrderStatusAndreceiveNo(order.getOrderId(), OrderStatusEnum.STATUS_14.getId(),
                receiveNo);
        if (num < 1) {
            return new OResponseDto<String>(500, "错误的订单号");
        }

        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for (OrderGoods good : goods) {
            lkWhGoodsStoreDao.updateFreezstockAndStockSBySiteskuIdAndStoreId(good.getSiteskuId(), good.getStoreId(), new Date(),
                    good.getQuantity(), good.getQuantity());
        }
        LkWhDeliveryOrder lkWhDeliveryOrder = new LkWhDeliveryOrder();
        lkWhDeliveryOrder.setOrderId(order.getOrderId());
        lkWhDeliveryOrder.setOrderNo(order.getOrderSnMain());
        lkWhDeliveryOrder.setdId(senderId);
        lkWhDeliveryOrder.setRemark("仓库发货");
        lkWhDeliveryOrderDao.save(lkWhDeliveryOrder);

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

        Order order = orders.get(0);

        int num = orderDao.updateOrderStatus(order.getOrderId(), OrderStatusEnum.STATUS_INVALID.getId());
        if (num < 1) {
            return new OResponseDto<String>(500, "错误的订单号");
        }

        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for (OrderGoods good : goods) {
            lkWhGoodsStoreDao.updateFreezStockBySiteSkuIdAndStoreId(good.getSiteskuId(), good.getStoreId(), good.getQuantity(),new Date());
        }

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

        createAction(order, OrderStatusEnum.STATUS_INVALID.getId(), userName, "作废订单");

        sendMessage(order, String.format(ShortMessage.REFUSE_MESSAGE_MODEL, order.getTaoOrderSn()));


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
        if (orderExm != null && orderExm.size() != 0 && !StringUtils.isEmpty(orderExm.get(0).getPhoneMob())) {
            MultiClient.getProvider(MultiClient.CHUANGLAN_PROVIDER).sendMessage(
                    Lists.newArrayList(orderExm.get(0).getPhoneMob()), messageString);
        }
        return true;
    }
    
	public ReturnStorageRespDto returnStorage(
			ReturnStorageReqDto returnStorageReqDto) {
		// 预售商品退货时，修改订单状态，新建退款单
		// 操作库存，包括库存操作流水（退货状态）
		// 触发退款（退款状态）
		Order order = null;
		List<Order> orders = orderDao.findByTaoOrderSn(returnStorageReqDto
				.getTaoOrderSn());
		if (!CollectionUtils.isEmpty(orders)) {
			order = orders.get(0);
		}

		if (order == null) {
			return new ReturnStorageRespDto(402, "订单不存在");
		}

		if (order.getSellerId() != returnStorageReqDto.getStoreId()) {
			return new ReturnStorageRespDto(403, "订单与微仓不一致");
		}

		List<OrderReturn> orderReturnList = getGoodsReturnList(
				order.getOrderId(), order.getTaoOrderSn(),
				returnStorageReqDto.getStoreId());
		if (orderReturnList.size() == 0) {
			return new ReturnStorageRespDto(404, "退货单不存在");
		}

		if (isGoodsReturned(orderReturnList)) {
			return new ReturnStorageRespDto();
		}

		// 修改订单退货状态
		updateOrderReturnGoodsStatus(orderReturnList,
				OrderReturnGoodsStatusEnum.STATUS_RETURNED);

		// 创建操作日志
		createAction(order, OrderActionTypeEnum.TYPE_RETURN_STORAGE, "", "退货入库");

		// 生成退货库存记录，增加库存
		LKWhStockIn whStockIn = createLKWhStockIn(order);

		List<LKWhStockInGoods> stockInGoodsList = createLKWhStockInGoodsList(
				whStockIn, returnStorageReqDto);

		// 增加库存
		updateGoodsStock(whStockIn, stockInGoodsList);

		return new ReturnStorageRespDto();
	}
    
    private List<OrderReturn> getGoodsReturnList(int orderId,String orderSnMain,int storeId){
		List<OrderReturn> orderReturnList = orderRDao.findByOrderId(orderId);
		
		if(orderReturnList.size() == 0){
			orderReturnList = orderRDao.findByOrderSnMainAndSellerId(orderSnMain, storeId);
		}
		
		return orderReturnList;
	}
	
	private boolean isGoodsReturned(List<OrderReturn> orderReturnList){
		for (OrderReturn orderReturn : orderReturnList) {
			if(orderReturn.getGoodsStatus()!=OrderReturnGoodsStatusEnum.STATUS_RETURNED.getId()){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 修改退货状态
	 * @param orderId 订单id
	 * @param returnStatus 退货状态
	 * @return
	 */
	private int updateOrderReturnGoodsStatus(List<OrderReturn> orderReturnList,OrderReturnGoodsStatusEnum goodsStatus){
		List<Integer> orderIdRList = new ArrayList<Integer>();
		for (OrderReturn orderReturn : orderReturnList) {
			orderIdRList.add(orderReturn.getOrderIdR());
		}

		if(orderIdRList.size()==0){
			return 0;
		}
		
		return orderRDao.updateGoodsStatusByOrderIdRList(orderIdRList,goodsStatus.getId());
	}
		
	private LKWhStockIn createLKWhStockIn(Order order){
		LKWhStockIn whStockIn = new LKWhStockIn();
		whStockIn.setStoreId(order.getSellerId());
		//order_id_r
		whStockIn.setType(2);
		whStockIn.setCreateTime(new Date());
		whStockInDao.save(whStockIn);
		
		return whStockIn;
	}
	
	private List<LKWhStockInGoods> createLKWhStockInGoodsList(LKWhStockIn whStockIn ,ReturnStorageReqDto returnStorageReqDto){
		List<LKWhStockInGoods> stockInGoodsList = new ArrayList<LKWhStockInGoods>();
		
		for (ReturnStorageGoodsReqDto returnStorageGoods : returnStorageReqDto.getSpecList()) {
			LKWhStockInGoods stockInGoods = new LKWhStockInGoods();
			stockInGoods.setSiteSkuId(returnStorageGoods.getSpecId());
			stockInGoods.setStock(returnStorageGoods.getConfirmNum());
			stockInGoods.setInId(whStockIn.getInId());
			whStockInGoodsDao.save(stockInGoods);
			stockInGoodsList.add(stockInGoods);
		}
		
		return stockInGoodsList;
	}
	
	/**
	 * 增加库存
	 * @param order
	 * @param goodsList
	 */
	private void updateGoodsStock(LKWhStockIn whStockIn,List<LKWhStockInGoods> stockInGoodsList){
		if(whStockIn==null || stockInGoodsList==null){
			return ;
		}
		
		for (LKWhStockInGoods stockInGoods : stockInGoodsList) {
			int tempCount = lkWhGoodsStoreDao.updateBySiteskuIdAndStoreId(stockInGoods.getSpecId(),
					whStockIn.getStoreId(),-stockInGoods.getStock(),0);
			
			if(tempCount==0){
				createWeiCangGoodsStore(whStockIn,stockInGoods);
			}
		}
		
		return;
	}
	
	private WeiCangGoodsStore createWeiCangGoodsStore(LKWhStockIn whStockIn,LKWhStockInGoods stockInGoods){
		WeiCangGoodsStore weiCangGoodsStore = new WeiCangGoodsStore();
		weiCangGoodsStore.setStockS(stockInGoods.getStock());
		weiCangGoodsStore.setSiteskuId(stockInGoods.getSpecId());
		weiCangGoodsStore.setProductId(stockInGoods.getGoodsId());
		weiCangGoodsStore.setStatus(WeiCangGoodsStoreStatusEnum.STATUS_ONSHELVES.getId());
		weiCangGoodsStore.setStoreId(whStockIn.getStoreId());
		weiCangGoodsStore.setUpdateTime(new Date());
		
		return lkWhGoodsStoreDao.save(weiCangGoodsStore);
	}
	
	/**
	 * 创建操作日志
	 * @param order
	 * @param action
	 * @param actor
	 * @param notes
	 */
	private OrderAction createAction(Order order,OrderActionTypeEnum action,String actor,String notes){
		//order_action 只插入一条记录
		OrderAction orderAction = new OrderAction();
		orderAction.setAction(action.getId());
		orderAction.setOrderSnMain(order.getOrderSnMain());
		orderAction.setTaoOrderSn(order.getTaoOrderSn());
		orderAction.setOrderId(order.getOrderId());
		orderAction.setActor(actor);
		orderAction.setActionTime(new Date());
		orderAction.setNotes(notes);
		orderActionDao.save(orderAction);
		
		return orderAction;
	}
	
}
