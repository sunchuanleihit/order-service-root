package com.loukou.order.service.impl;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.loukou.order.service.api.BkOrderService;
import com.loukou.order.service.bo.BaseRes;
import com.loukou.order.service.bo.ReturnOrderGoodsBo;
import com.loukou.order.service.bo.ReturnOrderPayBo;
import com.loukou.order.service.constants.OrderStateReturn;
import com.loukou.order.service.constants.ShippingMsgDesc;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.CoupTypeDao;
import com.loukou.order.service.dao.ExpressDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.GoodsDao;
import com.loukou.order.service.dao.GoodsSpecDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.OrderGoodsRDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderPayRDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.PaymentDao;
import com.loukou.order.service.dao.SiteCityDao;
import com.loukou.order.service.dao.StoreDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.CoupType;
import com.loukou.order.service.dao.TosuDao;
import com.loukou.order.service.dao.TosuHandleDao;
import com.loukou.order.service.dao.WeiCangGoodsStoreDao;
import com.loukou.order.service.entity.Express;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.entity.Goods;
import com.loukou.order.service.entity.GoodsSpec;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.OrderGoodsR;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.OrderPayR;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.Payment;
import com.loukou.order.service.entity.SiteCity;
import com.loukou.order.service.entity.Store;
import com.loukou.order.service.enums.BkOrderPayTypeEnum;
import com.loukou.order.service.entity.Tosu;
import com.loukou.order.service.entity.TosuHandle;
import com.loukou.order.service.enums.BkOrderSourceEnum;
import com.loukou.order.service.enums.BkOrderStatusEnum;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.OrderPayTypeEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.PayStatusEnum;
import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.BkCouponListDto;
import com.loukou.order.service.resp.dto.BkCouponListRespDto;
import com.loukou.order.service.resp.dto.BkCouponListResultDto;
import com.loukou.order.service.resp.dto.BkExtmMsgDto;
import com.loukou.order.service.resp.dto.BkOrderActionRespDto;
import com.loukou.order.service.resp.dto.BkOrderListBaseDto;
import com.loukou.order.service.resp.dto.BkOrderListDto;
import com.loukou.order.service.resp.dto.BkOrderListRespDto;
import com.loukou.order.service.resp.dto.BkOrderListResultDto;
import com.loukou.order.service.resp.dto.BkOrderReturnDto;
import com.loukou.order.service.resp.dto.BkOrderReturnListDto;
import com.loukou.order.service.resp.dto.BkOrderReturnListRespDto;
import com.loukou.order.service.resp.dto.BkTxkDto;
import com.loukou.order.service.resp.dto.BkTxkRecordDto;
import com.loukou.order.service.resp.dto.BkTxkRecordListRespDto;
import com.loukou.order.service.resp.dto.BkVaccountListResultRespDto;
import com.loukou.order.service.resp.dto.BkVaccountRespDto;
import com.loukou.order.service.resp.dto.CouponListDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.CouponListResultDto;
import com.loukou.order.service.resp.dto.BkOrderPayDto;
import com.loukou.order.service.resp.dto.CssOrderRespDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.ShippingListDto;
import com.loukou.order.service.resp.dto.ShippingListResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.ShippingMsgRespDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardRecordDetailRespVO;
import com.loukou.pos.client.txk.req.TxkCardRecordRespVO;
import com.loukou.pos.client.txk.req.TxkCardRowRespVO;
import com.loukou.pos.client.txk.req.TxkMemberCardsRespVO;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;
import com.loukou.pos.client.vaccount.resp.VaccountWaterBillQueryRespVO;
import com.loukou.sms.sdk.client.SingletonSmsClient;
import com.serverstarted.cart.service.constants.PackageType;
import com.serverstarted.goods.service.api.GoodsSpecService;
import com.serverstarted.goods.service.resp.dto.GoodsStockInfoRespDto;

@Service("bkOrderService")
public class BkOrderServiceImpl implements BkOrderService{
	@Autowired
	private GoodsSpecService goodsSpecService;
	
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
	private OrderGoodsDao orderGoodsDao;
    
    @Autowired
	private OrderPayDao orderPayDao;

    @Autowired
	private OrderGoodsRDao orderGoodsRDao;
    
    @Autowired
	private OrderReturnDao orderReturnDao;
    
    @Autowired
	private OrderPayRDao orderPayRDao;
    
    @Autowired
	private PaymentDao paymentDao;
    
    @Autowired
	private OrderExtmDao orderExtmDao;
    
    @Autowired
	private StoreDao storeDao;
    
    @Autowired
	private ExpressDao expressDao;// 快递公司代码及名称
    
    @Autowired
	private OrderActionDao orderActionDao;
    
    @Autowired
    private SiteCityDao siteCityDao;
    
    @Autowired
    private WeiCangGoodsStoreDao weiCangGoodsStoreDao;
    
    @Autowired
    private GoodsSpecDao goodsSpecDao;
    
    @Autowired
    private GoodsDao goodsDao;
    
    @Autowired
    private TosuDao tosuDao;
    
    @Autowired
    private TosuHandleDao tosuHandleDao;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private MemberDao memberDao;
    
    @Autowired
    private CoupListDao coupListDao;
    
    @Autowired
    private CoupTypeDao coupTypeDao;
    
    @Autowired
    private CoupRuleDao coupRuleDao;
	
	//订单详情
	@Override
	public BkOrderListRespDto orderDetail(String orderSnMain) {
		BkOrderListRespDto resp = new BkOrderListRespDto(200, "");//创建返回
		
		if (StringUtils.isEmpty(orderSnMain)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		
		List<Order> orderList = orderDao.findByOrderSnMain(orderSnMain);//获取订单列表信息
		if (CollectionUtils.isEmpty(orderList)) {
			resp.setCode(400);
			resp.setMessage("订单为空");
			return resp;
		}
		
		BkOrderListResultDto resultDto = new BkOrderListResultDto();//创建订单返回
		List<BkOrderListDto> orderListResult = new ArrayList<BkOrderListDto>();//创建订单列表返回
		List<Integer> orderIds = new ArrayList<Integer>();
		for(Order order : orderList) {
			orderIds.add(order.getOrderId());
		}
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderIdIn(orderIds);//获取订单商品列表
		
		//HASH
		Map<String, BkOrderListDto> orderListMap = new HashMap<String, BkOrderListDto>();

		for(Order order : orderList) {
			BkOrderListDto bkOrderListDto = new BkOrderListDto();
			
			//基础信息
			BkOrderListBaseDto baseDto = new BkOrderListBaseDto();
			baseDto.setStatusName(BkOrderStatusEnum.pasrseStatus(order.getStatus()).getStatus());
			baseDto.setAddTimeStr(DateUtils.dateTimeToStr(order.getAddTime()));
			String shippingtype = "便利店";
			if (order.getShippingId() == 0) {
				shippingtype = "小黄蜂";
			}else if (order.getShippingId() == 1) {
				shippingtype = "商家";
			}
			baseDto.setShippingType(shippingtype);
			baseDto.setPayTypeToString(OrderPayTypeEnum.parseType(order.getPayType()).getType());
			String payStatus = "未支付";
			if (order.getPayStatus() == 1) {
				payStatus = "已支付";
			}else if (order.getPayStatus() == 2) {
				payStatus = "部分支付";
			}
			baseDto.setPayStatusToString(payStatus);
			baseDto.setNeedShipTime(DateUtils.date2DateStr(order.getNeedShiptime()));
			baseDto.setNeedShipTimeSlot(order.getNeedShiptimeSlot());
			
			Store storeMsg = storeDao.findOne(order.getSellerId());
			String taxApply = "商家";
			if(storeMsg.getTaxApply()==1){
				taxApply = "淘常州";
			}
			baseDto.setTaxApply(taxApply);
			baseDto.setSourceName(BkOrderSourceEnum.parseSource(order.getSource()).getSource());
			baseDto.setOrderPaid(order.getOrderPayed());
			BeanUtils.copyProperties(order, baseDto);
			bkOrderListDto.setBase(baseDto);
			
			//商品信息
			List<GoodsListDto> goodsListDtoList = new ArrayList<GoodsListDto>();
			for(OrderGoods og : orderGoodsList) {
				if(og.getOrderId() == order.getOrderId()) {
					GoodsListDto goodsListDto = new GoodsListDto();
					BeanUtils.copyProperties(og, goodsListDto);
					goodsListDtoList.add(goodsListDto);
				}
			}
			bkOrderListDto.setGoodsList(goodsListDtoList);
			
			//收货信息
			List<OrderExtm> extmList = orderExtmDao.findByOrderSnMain(orderSnMain);
			BkExtmMsgDto extmMsgDto = new BkExtmMsgDto();
			if(!CollectionUtils.isEmpty(extmList)) {
				OrderExtm extm = extmList.get(0);
				extmMsgDto.setAddress(trimall(extm.getAddress()));
				if(StringUtils.isNotBlank(extm.getConsignee())) {
					extmMsgDto.setConsignee(trimall(extm.getConsignee()));
				}
				
				if(StringUtils.isNotBlank(extm.getPhoneMob())) {
					extmMsgDto.setPhoneMob(trimall(extm.getPhoneMob()));
				}
			}
			bkOrderListDto.setExtmMsg(extmMsgDto);
			//物流信息
			if(order.getStatus() >= OrderStatusEnum.STATUS_REVIEWED.getId()
					|| (order.getStatus() == OrderStatusEnum.STATUS_NEW.getId() 
						&& order.getPayStatus() == PayStatusEnum.STATUS_PAYED.getId())) {
				getLogistics(order, bkOrderListDto);
			}
			
			orderListMap.put(order.getTaoOrderSn(), bkOrderListDto);
		}
		
		if(orderListMap.isEmpty()) {
			return resp;
		} else {
			orderListResult.addAll(orderListMap.values());
			resultDto.setOrderList(orderListResult);
			resultDto.setOrderCount(1);
			
			resp.setCode(200);
			resp.setResult(resultDto);
		}
		return resp;
	}
	
	//获取包裹商品列表
	public List<GoodsListDto> getOrderGoodsList(int orderId){
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(orderId);//获取订单商品列表
		List<GoodsListDto> resp = new ArrayList<GoodsListDto>();
		for(OrderGoods og : orderGoodsList) {
			GoodsListDto goodsListDto = new GoodsListDto();
			BeanUtils.copyProperties(og, goodsListDto);
			resp.add(goodsListDto);
		}
		return resp;
	}
	
	//退货订单详情
	@Override
	public BkOrderListRespDto orderReturnMsg(String orderSnMain) {
		BkOrderListRespDto resp = new BkOrderListRespDto(200, "");//创建返回
		
		if (StringUtils.isEmpty(orderSnMain)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		
		List<Order> orderList = orderDao.getFininshedOrders(orderSnMain);//获取订单列表信息
		if (CollectionUtils.isEmpty(orderList)) {
			resp.setCode(400);
			resp.setMessage("订单为空");
			return resp;
		}
		
		BkOrderListResultDto resultDto = new BkOrderListResultDto();//创建订单返回
		List<BkOrderListDto> orderListResult = new ArrayList<BkOrderListDto>();//创建订单列表返回
		List<Integer> orderIds = new ArrayList<Integer>();
		for(Order order : orderList) {
			orderIds.add(order.getOrderId());
		}
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderIdIn(orderIds);//获取订单商品列表
		
		//HASH
		Map<String, BkOrderListDto> orderListMap = new HashMap<String, BkOrderListDto>();

		for(Order order : orderList) {
			BkOrderListDto bkOrderListDto = new BkOrderListDto();
			
			//基础信息
			BkOrderListBaseDto baseDto = new BkOrderListBaseDto();
			baseDto.setStatusName(createState(order));
			baseDto.setAddTimeStr(DateUtils.dateTimeToStr(order.getAddTime()));
			String shippingtype = "淘常州自送";
			if (order.getShippingId() != 0) {
				shippingtype = "第三方配送";
			}
			baseDto.setShippingType(shippingtype);
			baseDto.setPayTypeToString(OrderPayTypeEnum.parseType(order.getPayType()).getType());
			String payStatus = "未支付";
			if (order.getShippingId() != 0) {
				payStatus = "已支付";
			}
			baseDto.setPayStatusToString(payStatus);
			baseDto.setNeedShipTime(DateUtils.date2DateStr(order.getNeedShiptime()));
			baseDto.setNeedShipTimeSlot(order.getNeedShiptimeSlot());
			
			Store storeMsg = storeDao.findOne(order.getSellerId());
			String taxApply = "商家";
			if(storeMsg.getTaxApply()==1){
				taxApply = "淘常州";
			}
			baseDto.setTaxApply(taxApply);
			baseDto.setSourceName(BkOrderSourceEnum.parseSource(order.getSource()).getSource());
			baseDto.setOrderPaid(order.getOrderPayed());
			BeanUtils.copyProperties(order, baseDto);
			bkOrderListDto.setBase(baseDto);
			
			//商品信息
			List<GoodsListDto> goodsListDtoList = new ArrayList<GoodsListDto>();
			for(OrderGoods og : orderGoodsList) {
				if(og.getOrderId() == order.getOrderId()) {
					GoodsListDto goodsListDto = new GoodsListDto();
					List<OrderReturn> orderRList = orderReturnDao.findByOrderIdAndOrderStatus(og.getOrderId(),0);
					for(OrderReturn orderR:orderRList){
						List<OrderGoodsR> orderGoodsRList = orderGoodsRDao.findByOrderIdR(orderR.getOrderIdR());
						if(CollectionUtils.isEmpty(orderGoodsRList)){
							goodsListDto.setReturnQuantity(og.getQuantity());
							goodsListDto.setReturnMoney(Math.floor(og.getQuantity()*og.getPriceDiscount()*10)/10);
						}else{
							for(OrderGoodsR ogr : orderGoodsRList){
								if(og.getSpecId()==ogr.getSpecId() && og.getProType()==ogr.getProType()){
									if(goodsListDto.getReturnQuantity()==null){
										goodsListDto.setReturnQuantity(og.getQuantity()-ogr.getGoodsNum());
										goodsListDto.setReturnMoney(Math.floor(og.getQuantity()*og.getPriceDiscount()*10)/10-ogr.getGoodsAmount());
									}else{
										goodsListDto.setReturnQuantity(goodsListDto.getReturnQuantity()-ogr.getGoodsNum());
										goodsListDto.setReturnMoney(goodsListDto.getReturnMoney()-ogr.getGoodsAmount());
									}
								}
							}
						}
					}
					
					if(goodsListDto.getReturnQuantity()==null){
						goodsListDto.setReturnQuantity(og.getQuantity());
						goodsListDto.setReturnMoney(Math.floor(og.getQuantity()*og.getPriceDiscount()*10)/10);
					}
					
					BeanUtils.copyProperties(og, goodsListDto);
					goodsListDtoList.add(goodsListDto);
				}
			}
			bkOrderListDto.setGoodsList(goodsListDtoList);
			
			//收货信息
			List<OrderExtm> extmList = orderExtmDao.findByOrderSnMain(orderSnMain);
			BkExtmMsgDto extmMsgDto = new BkExtmMsgDto();
			if(!CollectionUtils.isEmpty(extmList)) {
				OrderExtm extm = extmList.get(0);
				extmMsgDto.setAddress(trimall(extm.getAddress()));
				if(StringUtils.isNotBlank(extm.getConsignee())) {
					extmMsgDto.setConsignee(trimall(extm.getConsignee()));
				}
				
				if(StringUtils.isNotBlank(extm.getPhoneMob())) {
					extmMsgDto.setPhoneMob(trimall(extm.getPhoneMob()));
				}
			}
			bkOrderListDto.setExtmMsg(extmMsgDto);
			//物流信息
			if(order.getStatus() >= OrderStatusEnum.STATUS_REVIEWED.getId()
					|| (order.getStatus() == OrderStatusEnum.STATUS_NEW.getId() 
						&& order.getPayStatus() == PayStatusEnum.STATUS_PAYED.getId())) {
				getLogistics(order, bkOrderListDto);
			}
			
			orderListMap.put(order.getTaoOrderSn(), bkOrderListDto);
		}
		
		if(orderListMap.isEmpty()) {
			return resp;
		} else {
			orderListResult.addAll(orderListMap.values());
			resultDto.setOrderList(orderListResult);
			resultDto.setOrderCount(1);
			
			resp.setCode(200);
			resp.setResult(resultDto);
		}
		return resp;
	}
	
	//获取支付信息
	public List<BkOrderPayDto> getOrderPayList(String orderSnMain){
		List<OrderPay> orderPayList = orderPayDao.findByOrderSnMain(orderSnMain);
		
		Map<Integer,Double> orderPayMap = new HashMap<Integer,Double>();
		for(OrderPay op : orderPayList) {
			if(orderPayMap.get(op.getPaymentId())==null){
				orderPayMap.put(op.getPaymentId(),op.getMoney());
			}else{
				orderPayMap.put(op.getPaymentId(),op.getMoney()+orderPayMap.get(op.getPaymentId()));
			}
		}
		
		List<BkOrderPayDto> resp = new ArrayList<BkOrderPayDto>();
		
		Iterator<Entry<Integer, Double>> iter = orderPayMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Integer key = (Integer) entry.getKey();
			Double val = (Double) entry.getValue();
			
			BkOrderPayDto orderPayDto = new BkOrderPayDto();
			String paymentName=paymentDao.getPaymentNameByPaymentId(key);
			orderPayDto.setPaymentName(paymentName);
			orderPayDto.setPaymentId(key);
			orderPayDto.setMoney(val);
			resp.add(orderPayDto);
		}
		return resp;
	}
	
	//生成退款单
	public BaseRes<String> generateReturn(String actor,int orderId,String postScript,String orderSnMain,int returnType,int payId,double shippingFee,
			int[] goodsIdList,
			int[] specIdList,
			int[] proTypeList,
			int[] recIdList,
			int[] goodsReturnNumList,
			double[] goodsReturnAmountList,
			int[] goodsReasonList,
			String[] goodsNameList,
			int[] paymentIdList,
			double[] returnAmountList){
		BaseRes<String> result=new BaseRes<String>();
		
		if(orderId==0 || orderSnMain.isEmpty()){
			result.setCode("400");
			result.setMessage("未获取订单ID");
			return result;
		}
		
		if(goodsIdList.length<=0 || specIdList.length<=0){
			result.setCode("400");
			result.setMessage("未选择订单商品列表");
			return result;
		}
		
		//处理退款单商品
		List<ReturnOrderGoodsBo> returnOrderGoodsList=new ArrayList<ReturnOrderGoodsBo>();
		for(int i=0;i<goodsIdList.length;i++){
			ReturnOrderGoodsBo returnOrderGoods=new ReturnOrderGoodsBo();
			returnOrderGoods.setGoodsId(goodsIdList[i]);
			returnOrderGoods.setSpecId(specIdList[i]);
			returnOrderGoods.setProType(proTypeList[i]);
			returnOrderGoods.setRecId(recIdList[i]);
			returnOrderGoods.setGoodsNum(goodsReturnNumList[i]);
			returnOrderGoods.setGoodsAmount(goodsReturnAmountList[i]);
			returnOrderGoods.setGoodsReasonId(goodsReasonList[i]);
			returnOrderGoods.setGoodsName(goodsNameList[i]);
			returnOrderGoodsList.add(returnOrderGoods);
		}
		
		for(ReturnOrderGoodsBo returnOrderGoods:returnOrderGoodsList){
			if(returnOrderGoods.getGoodsNum()==0){
				result.setCode("400");
				result.setMessage("退货数量不能为0");
				return result;
			}
		}
		
		//处理退款单支付记录
		List<ReturnOrderPayBo> returnOrderPayList=new ArrayList<ReturnOrderPayBo>();
		for(int i=0;i<paymentIdList.length;i++){
			ReturnOrderPayBo returnOrderPay=new ReturnOrderPayBo();
			returnOrderPay.setPaymentId(paymentIdList[i]);
			returnOrderPay.setPaymentAmount(returnAmountList[i]);
			returnOrderPayList.add(returnOrderPay);
		}
		
		//校验退货商品数量
		List<GoodsListDto> goodsCouldReturn = dealReturnOrderGoods(orderId);
		for(GoodsListDto gcr:goodsCouldReturn){
			for(ReturnOrderGoodsBo rog:returnOrderGoodsList){
				if(gcr.getSpecId()==rog.getSpecId() && gcr.getProType()==rog.getProType() && (gcr.getReturnQuantity()-rog.getGoodsNum()<0)){
					result.setCode("400");
					result.setMessage("所填商品个数超出购买商品个数");
					return result;
				}
			}
		}
		
		//校验退货商品金额
		double inputAmount = 0;
		for(ReturnOrderPayBo rop:returnOrderPayList){
			inputAmount+=rop.getPaymentAmount();
		}
		double couldAmount = 0;
		for(ReturnOrderGoodsBo rog:returnOrderGoodsList){
			couldAmount+=rog.getGoodsAmount();
		}
		double f1 = Math.round(inputAmount*100);
		double f2 = Math.round(couldAmount*100);
        if(f1 > f2){
        	result.setCode("400");
			result.setMessage("金额超出需退金额");
			return result;
        }
        
        //主退款单
        String addTime = DateUtils.date2DateStr2(new Date());
        Order orderMsg=orderDao.findByOrderId(orderId); 
        OrderReturn orderReturnData=new OrderReturn();
        orderReturnData.setOrderId(orderId);
        orderReturnData.setPostscript(postScript);
        orderReturnData.setOrderType(returnType);
        orderReturnData.setGoodsStatus(0);
        orderReturnData.setRefundStatus(0);
        orderReturnData.setStatementStatus(0);
        orderReturnData.setActor(actor);
        orderReturnData.setAddTime(addTime);
        orderReturnData.setOrderStatus(0);
        orderReturnData.setOrderSnMain(orderSnMain);
        orderReturnData.setGoodsType(0);
        orderReturnData.setBuyerId(orderMsg.getBuyerId());
        orderReturnData.setSellerId(orderMsg.getSellerId());
        orderReturnData.setRepayTime(null);
		double returnAmount = 0;
		for(ReturnOrderPayBo rop:returnOrderPayList){
			returnAmount+=rop.getPaymentAmount();
		}
		if(payId==31){
			orderReturnData.setReturnAmount(couldAmount);
		}else{
			orderReturnData.setReturnAmount(returnAmount);
		}
		orderReturnData.setShippingFee(shippingFee);
		OrderReturn orderReturnResult=orderReturnDao.save(orderReturnData);
		if(orderReturnResult==null){
			result.setCode("400");
			result.setMessage("生成退款单失败");
			return result;
		}
		
		//退货商品单
		int orderIdR=orderReturnResult.getOrderIdR();
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(orderId);//获取订单商品列表
		OrderGoodsR orderGoodsRData=new OrderGoodsR();
		for(ReturnOrderGoodsBo rog:returnOrderGoodsList){
			orderGoodsRData.setGoodsNum(rog.getGoodsNum());
			orderGoodsRData.setGoodsAmount(rog.getGoodsAmount());
			orderGoodsRData.setGoodsId(rog.getGoodsId());
			orderGoodsRData.setSpecId(rog.getSpecId());
			orderGoodsRData.setOrderId(orderId);
			for(OrderGoods og:orderGoodsList){
				if(rog.getSpecId()==og.getSpecId() && rog.getProType()==og.getProType()){
					orderGoodsRData.setPrice(og.getPriceDiscount());
					orderGoodsRData.setGoodsName(og.getGoodsName());
				}
			}
			orderGoodsRData.setAddTime(addTime);
			orderGoodsRData.setOrderIdR(orderIdR);
			orderGoodsRData.setRecId(rog.getRecId());
			orderGoodsRData.setProType(rog.getProType());
			OrderGoodsR orderGoodsRResult=orderGoodsRDao.save(orderGoodsRData);
			if(orderGoodsRResult==null){
				result.setCode("400");
				result.setMessage("生成退货商品单失败");
				return result;
			}
		}
		
		//支付退款单
		for(ReturnOrderPayBo rop:returnOrderPayList){
			OrderPayR orderPayRData=new OrderPayR();
			orderPayRData.setPaymentId(rop.getPaymentId());
			orderPayRData.setValue(rop.getPaymentAmount());
			orderPayRData.setOrderIdR(orderIdR);
			OrderPayR orderPayRResult=orderPayRDao.save(orderPayRData);
			if(orderPayRResult==null){
				result.setCode("400");
				result.setMessage("生成退款支付单失败");
				return result;
			}
		}
		
		if(payId==31){
			OrderPayR orderPayRData=new OrderPayR();
			orderPayRData.setPaymentId(31);
			orderPayRData.setValue(couldAmount);
			orderPayRData.setOrderIdR(orderIdR);
			OrderPayR orderPayRResult=orderPayRDao.save(orderPayRData);
			if(orderPayRResult==null){
				result.setCode("400");
				result.setMessage("生成退款支付单失败");
				return result;
			}
		}
		
		result.setCode("200");
		result.setMessage("退货成功");
		return result;
	}
	
	
	//处理退款订单商品
	private List<GoodsListDto> dealReturnOrderGoods(int orderId){
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(orderId);//获取订单商品列表
		List<OrderReturn> orderRList = orderReturnDao.findByOrderIdAndOrderStatus(orderId,0);//获取未有效退款订单
		List<ReturnOrderGoodsBo> returnGoodsList=new ArrayList<ReturnOrderGoodsBo>();
		for(OrderReturn orderR:orderRList){
			List<OrderGoodsR> orderGoodsRList = orderGoodsRDao.findByOrderIdR(orderR.getOrderIdR());//获取退款订单商品列表
			if(!CollectionUtils.isEmpty(orderGoodsRList)){
				for(OrderGoodsR orderGoodsR:orderGoodsRList){
					ReturnOrderGoodsBo returnGoods=new ReturnOrderGoodsBo();
					returnGoods.setSpecId(orderGoodsR.getSpecId());
					returnGoods.setGoodsNum(orderGoodsR.getGoodsNum());
					returnGoods.setProType(orderGoodsR.getProType());
					returnGoodsList.add(returnGoods);
				}
			}
		}
		
		List<GoodsListDto> result = new ArrayList<GoodsListDto>();
		if(CollectionUtils.isEmpty(returnGoodsList)){
			for(OrderGoods og:orderGoodsList){
				GoodsListDto baseDto = new GoodsListDto();
				baseDto.setReturnQuantity(og.getQuantity());
				BeanUtils.copyProperties(og, baseDto);
				result.add(baseDto);
			}
			return result;
		}else{
			for(OrderGoods og:orderGoodsList){
				GoodsListDto baseDto = new GoodsListDto();
				BeanUtils.copyProperties(og, baseDto);
				result.add(baseDto);
			}
		}
		
		for(GoodsListDto og:result){
			for(ReturnOrderGoodsBo rg:returnGoodsList){
				if(og.getSpecId()!=rg.getSpecId() || og.getProType()!=rg.getProType()){
					continue;
				}
				if(og.getReturnQuantity()==null){
					og.setReturnQuantity(og.getQuantity()-rg.getGoodsNum());
				}else{
					og.setReturnQuantity(og.getReturnQuantity()-rg.getGoodsNum());
				}
			}
			
			if(og.getReturnQuantity()==null){
				og.setReturnQuantity(og.getQuantity());
			}
		}
		
		return result;
	}
	
	//作废订单
	public BaseRes<String> cancelOrder(String orderSnMain,String actor){
		BaseRes<String> result=new BaseRes<String>();
		List<Order> orderList = orderDao.findByOrderSnMain(orderSnMain);//获取订单列表信息
		
		for(Order o:orderList){
			if(o.getOrderId()<1 || o.getType()=="" || o.getSellerId()<1 || orderSnMain==""){
				result.setCode("400");
				result.setMessage("订单数据错误");
				return result;
			}
			
			if(o.getStatus()>=8 || o.getStatus()==1 || o.getStatus()==2){
				result.setCode("400");
				result.setMessage("不可作废");
				return result;
			}
		}
		
		double discount=0;
		List<OrderPay> orderPayList = orderPayDao.findByOrderSnMain(orderSnMain);
		for(OrderPay op:orderPayList){
			if(op.getPaymentId()==14){
				discount+=op.getMoney();
			}
		}
		
		int orderResult= orderDao.updateOrderStatusByOrderSnMainAndNotStatus(orderSnMain,2,2);
		if(orderResult<1){
			result.setCode("400");
			result.setMessage("已作废，不可再次作废");
			return result;
		}
		
		//取消订单返还库存
		for(Order o:orderList){
			if(o.getStatus()!=1){//已取消订单不退库存
				releaseFreezStock(o.getOrderId(),2);
			}
		}
		
		if(discount>0){
			String couponNo=orderList.get(0).getUseCouponNo();
			int userId=orderList.get(0).getBuyerId();
			coupListDao.refundCouponList(couponNo, userId);
		}
		
		for(Order o:orderList){
			OrderAction orderAction=new OrderAction();
			orderAction.setAction(2);
			orderAction.setActionTime(new Date());
			orderAction.setTaoOrderSn(o.getTaoOrderSn());
			orderAction.setOrderId(o.getOrderId());
			orderAction.setOrderSnMain(o.getOrderSnMain());
			orderAction.setActor(actor);
			orderAction.setNotes("作废");
			orderActionDao.save(orderAction);
		}		
		
		List<OrderExtm> orderExtmMsg=orderExtmDao.findByOrderSnMain(orderSnMain);
		String phone=orderExtmMsg.get(0).getPhoneMob();
		
		//调用短信接口发送短信通知
		String[] phones = new String[]{phone};
		String content = "亲，您的订单"+orderSnMain+"已经取消，订单金额将会返回您的账户，客服电话400-678-0519";
		//调用短信服务，发送短信
		try {
			SingletonSmsClient.getClient().sendSMS(phones, content);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		result.setCode("200");
		result.setMessage("作废成功");
		return result;
	}
	
	//取消作废订单
	public BaseRes<String> resetCancelOrder(String orderSnMain,String actor){
		BaseRes<String> result=new BaseRes<String>();
		List<Order> orderList = orderDao.findByOrderSnMain(orderSnMain);//获取订单列表信息
		
		String errorMessage="";
		for(Order o:orderList){
			List<OrderGoods> orderGoodsList=orderGoodsDao.findByOrderId(o.getOrderId());
			for(OrderGoods og:orderGoodsList){
				Goods goodsMsg=goodsDao.findByGoodsId(og.getGoodsId());
				int stock=goodsSpecService.getStock(og.getGoodsId(),og.getSpecId(),goodsMsg.getStoreId());
				if(stock<og.getQuantity()){
					errorMessage+=og.getGoodsName()+" 剩余库存"+stock;
				}
			}
		}
		
		if(errorMessage!=""){
			errorMessage="取消作废失败，"+errorMessage;
			result.setCode("400");
			result.setMessage(errorMessage);
			return result;
		}
		
		List<OrderReturn> orderRList = orderReturnDao.findByOrderSnMainAndOrderStatus(orderSnMain,0);
		if(!CollectionUtils.isEmpty(orderRList)){
			result.setCode("400");
			result.setMessage("已生成退款单，不能取消作废");
			return result;
		}
		
		for(Order o:orderList){
			addFreezStock(o.getOrderId());
		}
		
		if(orderList.get(0).getUseCouponNo()!=""){
			coupListDao.useCouponByCommoncode(orderList.get(0).getBuyerId(),orderList.get(0).getUseCouponNo());
		}
		
		for(Order o:orderList){
			OrderAction orderAction=new OrderAction();
			orderAction.setAction(31);
			orderAction.setActionTime(new Date());
			orderAction.setTaoOrderSn(o.getTaoOrderSn());
			orderAction.setOrderId(o.getOrderId());
			orderAction.setOrderSnMain(o.getOrderSnMain());
			orderAction.setActor(actor);
			orderAction.setNotes("system取消作废");
			orderActionDao.save(orderAction);
		}
		
		
		int orderResult=orderDao.updateStatusByOrderSnMain(orderSnMain,0);
		if(orderResult<1){
			result.setCode("400");
			result.setMessage("取消作废失败");
			return result;
		}
		result.setCode("200");
		result.setMessage("取消作废成功");
		return result;
	}
	
	//增加冻结库存
	/*
	 * orderId	  	订单ID
	 * operateType 操作类型,1取消作废
	 */
	private void addFreezStock(int orderId){
		Order orderMsg=orderDao.findByOrderId(orderId);
		List<OrderGoods> orderGoodsList=orderGoodsDao.findByOrderId(orderId);
		if(CollectionUtils.isEmpty(orderGoodsList)){
			return;
		}
		for(OrderGoods og:orderGoodsList){
			if(orderMsg.getType() == "wei_wh" || orderMsg.getType() == "wei_self"){
				weiCangGoodsStoreDao.updateAddBySpecIdAndStoreId(og.getSpecId(), og.getStoreId(), og.getQuantity());
			}else{
				goodsSpecDao.updateAddBySpecId(og.getSpecId(), og.getQuantity());
			}
		}
		return;
	}
	
	//释放冻结库存
	/*
	 * orderId	  	订单ID
	 * operateType 操作类型,1取消,2作废,6核验/发货
	 */
	private void releaseFreezStock(int orderId,int operateType){
		Order orderMsg=orderDao.findByOrderId(orderId);
		List<OrderGoods> orderGoodsList=orderGoodsDao.findByOrderId(orderId);
		if(CollectionUtils.isEmpty(orderGoodsList)){
			return;
		}
		
		for(OrderGoods og:orderGoodsList){
			if(orderMsg.getType() == "wei_wh" || orderMsg.getType() == "wei_self"){
				if(operateType==6){//发货
					weiCangGoodsStoreDao.updateBySpecIdAndStoreId(og.getSpecId(), og.getStoreId(), og.getQuantity(), og.getQuantity());
				}else{//取消
					weiCangGoodsStoreDao.updateBySpecIdAndStoreId(og.getSpecId(), og.getStoreId(), og.getQuantity());
				}
			}else{
				if(operateType==6){//发货
					goodsSpecDao.updateBySpecId(og.getSpecId(), og.getQuantity(), og.getQuantity());
				}else{//取消
					goodsSpecDao.updateBySpecId(og.getSpecId(), og.getQuantity());
				}
			}
		}
		
		return;
	}
	
	//获取全部支付信息
	public List<BkOrderPayDto> getAllOrderPayList(String orderSnMain){
		Iterable<Payment> paymentList=paymentDao.findAll();
		List<OrderPay> orderPayList = orderPayDao.findByOrderSnMain(orderSnMain);
		List<BkOrderPayDto> resp = new ArrayList<BkOrderPayDto>();
		
		for(Payment p:paymentList){
			BkOrderPayDto orderPayDto = new BkOrderPayDto();
			orderPayDto.setPaymentName(p.getPaymentName());
			orderPayDto.setPaymentId(p.getPaymentId());
			resp.add(orderPayDto);
		}
		
		for(BkOrderPayDto p:resp){
			for(OrderPay op:orderPayList){
				if(p.getPaymentId()==op.getPaymentId()){
					if(p.getMoney()!=0){
						p.setMoney(p.getMoney()+op.getMoney());
					}else{
						p.setMoney(op.getMoney());
					}
				}
			}
		}
		
		return resp;
	}
	
	//获取多付款退款信息
	public double getMultiplePaymentRefundMsg(String orderSnMain){
		List<OrderPay> orderPayList = orderPayDao.findByOrderSnMain(orderSnMain);
		double couldReturn=0;
		
		double hasPaid=0;
		for(OrderPay op:orderPayList){
			hasPaid+=op.getMoney();
		}
		
		List<OrderReturn> orderReturnMsg=orderReturnDao.findByOrderSnMainAndOrderStatus(orderSnMain, 0);
		double hasReturn=0;
		for(OrderReturn or:orderReturnMsg){
			hasReturn+=or.getReturnAmount();
		}
		
		if(hasPaid-hasReturn>0){
			couldReturn=hasPaid-hasReturn;
		}
		
		return couldReturn;
	}
	
	//生成退款单
	public BaseRes<String> generatePaymentRefund(int reason,String actor,String orderSnMain,String postScript,int[] paymentIdList,double[] returnAmountList){
		BaseRes<String> result=new BaseRes<String>();
		if(orderSnMain.isEmpty()){
			result.setCode("400");
			result.setMessage("未获取订单ID");
			return result;
		}
		
		if(returnAmountList.length<=0 || paymentIdList.length<=0){
			result.setCode("400");
			result.setMessage("请选择退款方式");
			return result;
		}
		
		List<Order> orderMsg=orderDao.findByOrderSnMain(orderSnMain);
		//处理退款单支付记录
		List<ReturnOrderPayBo> returnOrderPayList=new ArrayList<ReturnOrderPayBo>();
		for(int i=0;i<paymentIdList.length;i++){
			if(returnAmountList[i]>0){
				ReturnOrderPayBo returnOrderPay=new ReturnOrderPayBo();
				returnOrderPay.setPaymentId(paymentIdList[i]);
				returnOrderPay.setPaymentAmount(returnAmountList[i]);
				returnOrderPayList.add(returnOrderPay);
			}
		}
		
		double returnAmount = 0;
		for(ReturnOrderPayBo rop:returnOrderPayList){
			returnAmount+=rop.getPaymentAmount();
		}
		
		if(returnAmount<=0){
			result.setCode("400");
			result.setMessage("请选择退款方式");
			return result;
		}

		//主退款单
		String addTime = DateUtils.date2DateStr2(new Date());
        OrderReturn orderReturnData=new OrderReturn();
        orderReturnData.setOrderSnMain(orderSnMain);
        orderReturnData.setBuyerId(orderMsg.get(0).getBuyerId());
        orderReturnData.setSellerId(orderMsg.get(0).getSellerId());
		orderReturnData.setReturnAmount(returnAmount);
		orderReturnData.setActor(actor);
		orderReturnData.setAddTime(addTime);
		orderReturnData.setGoodsType(0);
		orderReturnData.setOrderType(2);
		orderReturnData.setGoodsStatus(4);
        orderReturnData.setRefundStatus(0);
        orderReturnData.setStatementStatus(0);
        orderReturnData.setPostscript(postScript);
        orderReturnData.setOrderStatus(0);
        orderReturnData.setRepayTime(null);
        orderReturnData.setReason(reason);
		OrderReturn orderReturnResult=orderReturnDao.save(orderReturnData);
		
		//支付退款单
		for(ReturnOrderPayBo rop:returnOrderPayList){
			OrderPayR orderPayRData=new OrderPayR();
			orderPayRData.setPaymentId(rop.getPaymentId());
			orderPayRData.setValue(rop.getPaymentAmount());
			orderPayRData.setOrderIdR(orderReturnResult.getOrderIdR());
			OrderPayR orderPayRResult=orderPayRDao.save(orderPayRData);
			if(orderPayRResult==null){
				result.setCode("400");
				result.setMessage("生成退款支付单失败");
				return result;
			}
		}
		
		result.setCode("200");
		result.setMessage("退款成功");
		return result;
	}
	
	//生成退款单
	public BaseRes<String> generateSpecialPaymentRefund(int reason,String actor,String orderSnMain,String postScript,int[] paymentIdList,double[] returnAmountList){
		BaseRes<String> result=new BaseRes<String>();
		if(orderSnMain.isEmpty()){
			result.setCode("400");
			result.setMessage("未获取订单ID");
			return result;
		}
		
		if(returnAmountList.length<=0 || paymentIdList.length<=0){
			result.setCode("400");
			result.setMessage("请选择退款方式");
			return result;
		}
		
		List<Order> orderMsg=orderDao.findByOrderSnMain(orderSnMain);
		//处理退款单支付记录
		List<ReturnOrderPayBo> returnOrderPayList=new ArrayList<ReturnOrderPayBo>();
		for(int i=0;i<paymentIdList.length;i++){
			if(returnAmountList[i]>0){
				ReturnOrderPayBo returnOrderPay=new ReturnOrderPayBo();
				returnOrderPay.setPaymentId(paymentIdList[i]);
				returnOrderPay.setPaymentAmount(returnAmountList[i]);
				returnOrderPayList.add(returnOrderPay);
			}
		}
		
		double returnAmount = 0;
		for(ReturnOrderPayBo rop:returnOrderPayList){
			returnAmount+=rop.getPaymentAmount();
		}
		
		if(returnAmount<=0){
			result.setCode("400");
			result.setMessage("请选择退款方式");
			return result;
		}
		
		double shippingFee=0;
		for(Order o:orderMsg){
			shippingFee+=o.getShippingFee();
		}
		
		if(reason==3){
			if(shippingFee<returnAmount){
				result.setCode("400");
				result.setMessage("退的运费金额不能大于原运费金额");
				return result;
			}
		}

		//主退款单
		String addTime = DateUtils.date2DateStr2(new Date());
        OrderReturn orderReturnData=new OrderReturn();
        orderReturnData.setOrderSnMain(orderSnMain);
        orderReturnData.setBuyerId(orderMsg.get(0).getBuyerId());
        orderReturnData.setSellerId(orderMsg.get(0).getSellerId());
		orderReturnData.setReturnAmount(returnAmount);
		orderReturnData.setActor(actor);
		orderReturnData.setAddTime(addTime);
		orderReturnData.setGoodsType(0);
		orderReturnData.setOrderType(7);
		orderReturnData.setGoodsStatus(4);
        orderReturnData.setRefundStatus(0);
        orderReturnData.setStatementStatus(0);
        orderReturnData.setPostscript(postScript);
        orderReturnData.setOrderStatus(0);
        orderReturnData.setRepayTime(null);
        orderReturnData.setReason(reason);
		OrderReturn orderReturnResult=orderReturnDao.save(orderReturnData);
		
		//支付退款单
		for(ReturnOrderPayBo rop:returnOrderPayList){
			OrderPayR orderPayRData=new OrderPayR();
			orderPayRData.setPaymentId(rop.getPaymentId());
			orderPayRData.setValue(rop.getPaymentAmount());
			orderPayRData.setOrderIdR(orderReturnResult.getOrderIdR());
			OrderPayR orderPayRResult=orderPayRDao.save(orderPayRData);
			if(orderPayRResult==null){
				result.setCode("400");
				result.setMessage("生成退款支付单失败");
				return result;
			}
		}
		
		result.setCode("200");
		result.setMessage("退款成功");
		return result;
	}
	
	
	//提交投诉
	public BaseRes<String> generateComplaint(String actor,String orderSnMain,String content1,String addTime,String userName,String mobile,int type,int status,String content2,String[] sellerNameList,String[] goodsNameList){
		BaseRes<String> result=new BaseRes<String>();
		
		String sellerName="";
		for(String sn:sellerNameList){
			sellerName+=sn+",";
		}
		sellerName=sellerName.substring(0, sellerName.length()-1);
		
		String goodsName="";
		for(String sn:goodsNameList){
			goodsName+=sn+",";
		}
		goodsName=goodsName.substring(0,goodsName.length()-1);
		
		Tosu tosuData=new Tosu();
		tosuData.setDateline1((int)(DateUtils.str2Date(addTime).getTime()/1000));
		tosuData.setUserName(userName);
		tosuData.setMobile(mobile);
		tosuData.setType(type);
		tosuData.setStatus(status);
		tosuData.setOrderSnMain(orderSnMain);
		tosuData.setDateline2(DateUtils.getTime());
		tosuData.setSellerName(sellerName);
		tosuData.setGoodsName(goodsName);
		Tosu tosuResult=tosuDao.save(tosuData);
		if(tosuResult==null){
			result.setCode("400");
			result.setMessage("生成退款支付单失败");
			return result;
		}
		
		TosuHandle tosuHandle1=new TosuHandle();
		tosuHandle1.setTid(tosuResult.getId());
		tosuHandle1.setContent(content1);
		tosuHandle1.setType(0);
		tosuHandle1.setActor(actor);
		tosuHandle1.setDateline(DateUtils.getTime());
		tosuHandleDao.save(tosuHandle1);
		
		if(content2!=""){
			TosuHandle tosuHandle2=new TosuHandle();
			tosuHandle2.setTid(tosuResult.getId());
			tosuHandle2.setContent(content2);
			tosuHandle2.setType(1);
			tosuHandle2.setActor(actor);
			tosuHandle2.setDateline(DateUtils.getTime());
			tosuHandleDao.save(tosuHandle2);
		}
		
		result.setCode("200");
		result.setMessage("提交成功");
		return result;
	}

	private String trimall(String str)// 删除空格
	{
		String[] searchList = { " ", "  ", "\t", "\n", "\r" };
		String[] replacementList = { "", "", "", "", "" };
		return StringUtils.replaceEach(str, searchList, replacementList);
	}
	
	//获取物流信息
	private void getLogistics(Order order, BkOrderListDto bkOrderListDto) {
		ShippingMsgRespDto shippingDto = getShippingResult(order.getTaoOrderSn());
		ShippingMsgDto shippingMsgDto = new ShippingMsgDto();
		if(shippingDto.getInnerCode() == 0) {
			List<ShippingListDto> shippingList = shippingDto.getResult().getShippingList();
			if(!CollectionUtils.isEmpty(shippingList)) {
				ShippingListDto dto = shippingList.get(0);
					if(order.getStatus() == OrderStatusEnum.STATUS_DELIVERIED.getId()
							&& order.getStatus() == OrderStatusEnum.STATUS_14.getId()
							) {
						shippingMsgDto.setDescription(ShippingMsgDesc.DELIEVER);
						if(StringUtils.isNotBlank(dto.getCreatTime())) {
							shippingMsgDto.setCreatTime(dto.getCreatTime());
						}
					} else if (order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
						shippingMsgDto.setDescription(ShippingMsgDesc.FINISH);
						if(StringUtils.isNotBlank(dto.getCreatTime())) {
							shippingMsgDto.setCreatTime(dto.getCreatTime());
						}
					} else {
						shippingMsgDto.setDescription(dto.getDescription());
						if(StringUtils.isNotBlank(dto.getCreatTime())) {
							shippingMsgDto.setCreatTime(dto.getCreatTime());
						}
					}
			}
			
		} 
		bkOrderListDto.setShippingmsg(shippingMsgDto);
	}
	
	//生成订单返回状态state
	private String createState(Order order) {
		int status = order.getStatus();
		String state = "";
		if(status == OrderStatusEnum.STATUS_CANCELED.getId()) {
			state = OrderStateReturn.CANCELED;
		} else if (status == OrderStatusEnum.STATUS_INVALID.getId()) {
			state = OrderStateReturn.INVALID;
		} else if (status == OrderStatusEnum.STATUS_FINISHED.getId()) {
			state = OrderStateReturn.RECEIVED;
		} else if ((status >= OrderStatusEnum.STATUS_REVIEWED.getId() && 
				status <= OrderStatusEnum.STATUS_14.getId()) 
				|| (status == OrderStatusEnum.STATUS_NEW.getId() 
						&& order.getPayStatus() == PayStatusEnum.STATUS_PAYED.getId())) {
			state = OrderStateReturn.TO_RECIEVE;
		} else if ((status == OrderStatusEnum.STATUS_NEW.getId() 
				&& order.getPayStatus() == PayStatusEnum.STATUS_UNPAY.getId()
				) || (status == OrderStatusEnum.STATUS_NEW.getId() && 
						order.getPayStatus() == PayStatusEnum.STATUS_PART_PAYED.getId()) ) {
			state = OrderStateReturn.UN_PAY;
		}
		return state;
	}
	
	// 物流信息
	// 传参数 列表详情调用
	// 不传参数 接口调用
	public ShippingMsgRespDto getShippingResult(String taoOrderSn) {
		ShippingMsgRespDto resp = new ShippingMsgRespDto(200, "");
		ShippingListResultDto resultDto = new ShippingListResultDto();
		if (StringUtils.isEmpty(taoOrderSn)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
		List<ShippingListDto> shippingList = new ArrayList<ShippingListDto>();
		
		if(order != null) {
			String shippingCompany = order.getShippingCompany();
			StringBuilder sb = new StringBuilder();
			if(StringUtils.isNotBlank(shippingCompany)) {
				Express express = expressDao.findByCodeNum(shippingCompany);
				if (order.getShippingId() == 0 || order.getShippingId() > 5) {
					sb.append("淘常州小黄蜂配送");
				} else {
					sb.append("商家自送");
				}
				if ( express != null && StringUtils.isNotBlank(express.getExpressName())) {
					sb.append("(").append(express.getExpressName()).append(")");
				}
			}

			resultDto.setShippingName(sb.toString());
			
			if (order.getPayType() == OrderPayTypeEnum.TYPE_ARRIVAL.getId()) {
				resultDto.setPayType("货到付款");
			} else if (order.getPayType() == OrderPayTypeEnum.TYPE_ONLINE.getId()) {
				resultDto.setPayType("在线支付");
			}
			
			List<OrderAction> orderActionList = orderActionDao.findByOrderSnMainDesc(order.getOrderSnMain());
			if(!CollectionUtils.isEmpty(orderActionList)) {
				for (OrderAction orderAction : orderActionList) {
					if (!(orderAction.getAction() == OrderActionTypeEnum.TYPE_33.getId()
							|| orderAction.getAction() == OrderActionTypeEnum.TYPE_CHOOSE_PAY.getId() 
							|| orderAction.getAction() == OrderActionTypeEnum.TYPE_INSPECTED.getId())) {
						ShippingListDto shippingListDto = new ShippingListDto();
						
						if(StringUtils.isNotBlank(orderAction.getTaoOrderSn())) {
							if(StringUtils.equals(orderAction.getTaoOrderSn(), taoOrderSn)) {
								shippingListDto.setCreatTime(DateUtils.date2DateStr2(orderAction.getTimestamp()));
								shippingListDto.setDescription(orderAction.getNotes());
								shippingListDto.setTaoOrderSn(taoOrderSn);
								shippingList.add(shippingListDto);
							}
						} else {
							shippingListDto.setCreatTime(DateUtils.date2DateStr2(orderAction.getTimestamp()));
							shippingListDto.setDescription(orderAction.getNotes());
							shippingListDto.setTaoOrderSn(taoOrderSn);
							shippingList.add(shippingListDto);
						}
					}
				}
			}
		}

		resultDto.setShippingList(shippingList);
		resp.setResult(resultDto);
		resp.setCode(200);
		resp.setInnerCode(0);
		return resp;
	}

	@Override
	public BkOrderListRespDto queryBkOrderList(int pageNum, int pageSize, final CssOrderReqDto cssOrderReqDto) {
		BkOrderListRespDto resp = new BkOrderListRespDto(200, "");
		List<Order> orderList = new ArrayList<Order>();
		//先从收货人中查出所有相关的订单
		String cityCode = "";
		List<OrderExtm> orderExtmResultList = null;
		final Set<String> orderSnMainSet = new HashSet<String>();
		if(StringUtils.isNoneBlank(cssOrderReqDto.getQueryContent()) && StringUtils.isNoneBlank(cssOrderReqDto.getQueryType())){
			if(cssOrderReqDto.getQueryType().equals("consignee")){
				orderExtmResultList = orderExtmDao.findByConsignee(cssOrderReqDto.getQueryContent());
			}else if(cssOrderReqDto.getQueryType().equals("phoneMob")){
				orderExtmResultList = orderExtmDao.findByPhoneMob(cssOrderReqDto.getQueryContent());
			}else if(cssOrderReqDto.getQueryType().equals("phoneTel")){
				orderExtmResultList = orderExtmDao.findByPhoneTel(cssOrderReqDto.getQueryContent());
			}
			if(orderExtmResultList != null && orderExtmResultList.size() > 0){
				for(OrderExtm tmp: orderExtmResultList){
					orderSnMainSet.add(tmp.getOrderSnMain());
				}
			}else if(!cssOrderReqDto.getQueryType().equals("city")){
				//如果根据收货人没查到则订单也没有
				return resp;
			}
		}
		pageNum = pageNum-1;
		//获取所有的城市
		Iterator<SiteCity> siteCityInterator = siteCityDao.findAll().iterator();
		Map<String,String> cityMap = new HashMap<String,String>();
		while(siteCityInterator.hasNext()){
			SiteCity siteCity = siteCityInterator.next();
			cityMap.put(siteCity.getSiteCityId(), siteCity.getSiteCityName());
			if("city".equals(cssOrderReqDto.getQueryType()) && siteCity.getSiteCityName().equals(cssOrderReqDto.getQueryContent())){
				cityCode = siteCity.getSiteCityId();
			}
		}
		final String cityCodeFinal = cityCode;
		//不好意思了，这么写也是醉了
		String qlStr = "select distinct(orderSnMain) from Order o where 1=1 ";
		if(cssOrderReqDto.getIsDel()!=null){
			qlStr += " and o.isDel = " + cssOrderReqDto.getIsDel();
		}
		if(cssOrderReqDto.getTimeLimit()!=null){
			qlStr += " and o.addTime > " + cssOrderReqDto.getTimeLimit();
		}
		if(StringUtils.isNotBlank(cssOrderReqDto.getOrderSnMain())){
			qlStr += " and o.orderSnMain = '"+cssOrderReqDto.getOrderSnMain()+"'";
		}
		if(StringUtils.isNotBlank(cssOrderReqDto.getBuyerName())){
			qlStr += " and o.buyerName = '"+cssOrderReqDto.getBuyerName()+"'";
		}
		if(cssOrderReqDto.getBuyerId() != null ){
			qlStr += " and o.buyerId = "+cssOrderReqDto.getBuyerId();
		}
		if(StringUtils.isNotBlank(cssOrderReqDto.getStartTime())){
			qlStr += " and o.addTime >= "+(int)(DateUtils.str2Date(cssOrderReqDto.getStartTime()).getTime()/1000);
		}
		if(StringUtils.isNotBlank(cssOrderReqDto.getEndTime())){
			qlStr += " and o.addTime <= "+(int)(DateUtils.str2Date(cssOrderReqDto.getEndTime()).getTime()/1000);
		}
		if(cssOrderReqDto.getStatus()!=null){
			qlStr += " and o.status = "+cssOrderReqDto.getStatus();
		}
		if(cssOrderReqDto.getPayStatus()!=null){
			qlStr += " and o.payStatus = " + cssOrderReqDto.getPayStatus();
		}
		if(StringUtils.isNotBlank(cityCodeFinal) && StringUtils.isNotBlank(cssOrderReqDto.getQueryContent())){
			qlStr += " and o.sellSite = '"+cityCodeFinal+"'";
		}
		if(orderSnMainSet.size()>0){
			String orderSns = "";
			for(String str : orderSnMainSet){
				orderSns += ",'"+str+"'";
			}
			orderSns = orderSns.substring(1);
			qlStr += " and o.orderSnMain in ("+orderSns+")";
		}
		qlStr += " order by o.orderId desc";
		
		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em.createQuery(qlStr);
		List<String> orderSnMainAll = query.getResultList();
		List<String> orderSnMains = new ArrayList<String>();
		for(int i=pageNum*pageSize; i<(pageNum+1)*pageSize; i++){
			if(i<orderSnMainAll.size()){
				orderSnMains.add(orderSnMainAll.get(i));
			}
		}
		List<Order> orderListAll = new ArrayList<Order>();
		if(orderSnMains.size()>0){
			orderListAll = orderDao.findByOrderSnMainIn(orderSnMains);
		}
		Map<String, Order> orderMap = new HashMap<String, Order>();
		for(Order tmp: orderListAll){
			Order orderTmp = orderMap.get(tmp.getOrderSnMain());
			if(orderTmp == null){
				orderMap.put(tmp.getOrderSnMain(), tmp);
			}else{
				orderTmp.setOrderAmount(orderTmp.getOrderAmount() + tmp.getOrderAmount());
				orderTmp.setGoodsAmount(orderTmp.getGoodsAmount() + tmp.getGoodsAmount());
				orderTmp.setOrderPayed(orderTmp.getOrderPayed() + tmp.getOrderPayed());
				orderTmp.setShippingFee(orderTmp.getShippingFee() + tmp.getShippingFee());
			}
		}
		for(String tmp: orderSnMains){
			Order orderTmp = orderMap.get(tmp);
			if(orderTmp!=null){
				orderList.add(orderTmp);
			}
		}
		if(CollectionUtils.isEmpty(orderList)) {
			resp.setMessage("订单列表为空");
			return resp;
		}
		for(Order tmp: orderList){
			orderSnMains.add(tmp.getOrderSnMain());
		}
		List<OrderExtm> orderExtmList = orderExtmDao.findByOrderSnMainIn(orderSnMains);
		Map<String, OrderExtm> orderExtmMap = new HashMap<String, OrderExtm>();
		for(OrderExtm orderExtm: orderExtmList){
			orderExtmMap.put(orderExtm.getOrderSnMain(), orderExtm);
		}
		
		List<BkOrderListDto> bkOrderList = new ArrayList<BkOrderListDto>();
		for(Order tmp : orderList) {
			BkOrderListDto orderListDto = new BkOrderListDto();
			BkOrderListBaseDto baseDto = createBkOrderBaseDto(tmp);
			OrderExtm orderExtm = orderExtmMap.get(baseDto.getOrderSnMain());
			BkExtmMsgDto bkOrderExtm = createExtmMsg(orderExtm);
			baseDto.setSellSite(cityMap.get(tmp.getSellSite()));//城市
			orderListDto.setExtmMsg(bkOrderExtm);
			orderListDto.setBase(baseDto);
			bkOrderList.add(orderListDto);
		}
		BkOrderListResultDto resultDto = new BkOrderListResultDto();
		resultDto.setOrderCount(orderSnMainAll.size());
		resultDto.setOrderList(bkOrderList);
		resp.setResult(resultDto);
		return resp;
	}
	private BkExtmMsgDto createExtmMsg(OrderExtm orderExtm) {
		BkExtmMsgDto dto = new BkExtmMsgDto();
		if(orderExtm!=null){
			dto.setAddress(orderExtm.getAddress());
			dto.setConsignee(orderExtm.getConsignee());
			dto.setPhoneMob(orderExtm.getPhoneMob());
			dto.setRegionName(orderExtm.getRegionName());
		}
		return dto;
	}
	/**
	 * 根据原始订单信息返回处理后的订单信息
	 * @param order
	 * @return
	 */
	private BkOrderListBaseDto createBkOrderBaseDto(Order order){
		BkOrderListBaseDto baseDto = new BkOrderListBaseDto();
		baseDto.setOrderId(order.getOrderId());
		baseDto.setOrderSn(order.getOrderSn());
		baseDto.setOrderSnMain(order.getOrderSnMain());
		baseDto.setTaoOrderSn(order.getTaoOrderSn());
		baseDto.setSourceName(BkOrderSourceEnum.parseSource(order.getSource()).getSource());
		String needShipTime = "";
		if(order.getNeedShiptime()!=null){
			needShipTime += DateUtils.date2DateStr(order.getNeedShiptime());
		}
		if(StringUtils.isNoneBlank(order.getNeedShiptimeSlot())){
			needShipTime += " "+order.getNeedShiptimeSlot();
		}
		baseDto.setNeedShipTime(needShipTime);
		baseDto.setStatus(order.getStatus());
		baseDto.setStatusName(BkOrderStatusEnum.pasrseStatus(order.getStatus()).getStatus());
		baseDto.setNeedInvoice(order.getNeedInvoice());
		baseDto.setInvoiceNo(order.getInvoiceNo());
		baseDto.setInvoiceHeader(order.getInvoiceHeader());
		baseDto.setBuyerName(order.getBuyerName());
		baseDto.setPayType(order.getPayType());
		baseDto.setOrderAmount(order.getOrderAmount());
		baseDto.setGoodsAmount(order.getGoodsAmount());
		baseDto.setOrderPaid(order.getOrderPayed());
		baseDto.setOrderNotPaid(order.getGoodsAmount()+order.getShippingFee()-order.getOrderPayed());
		baseDto.setPrinted(order.getPrinted());
		baseDto.setPayStatus(order.getPayStatus());
		baseDto.setSellerName(order.getSellerName());
		baseDto.setShippingFee(order.getShippingFee());
		if(order.getFinishedTime()>0){
			baseDto.setFinishedTimeStr(DateUtils.dateTimeToStr(order.getFinishedTime()));
		}
		baseDto.setAddTimeStr(DateUtils.dateTimeToStr(order.getAddTime()));
		baseDto.setPayMessage(order.getPayMessage());
		return baseDto;
	}
	/**
	 * 查找未生成退款单
	 */
	@Override
	public BkOrderListRespDto queryBkOrderNoReturnList(String sort, String order, int pageNum, int pageSize,
			final CssOrderReqDto cssOrderReqDto) {
		BkOrderListRespDto resp = new BkOrderListRespDto(200, "");
		
		Sort pageSort = new Sort(Sort.Direction.DESC,"orderId");
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)){
			if(order.toLowerCase().equals("asc")){
				pageSort = new Sort(Sort.Direction.ASC,sort);
			}else if(order.toLowerCase().equals("desc")){
				pageSort = new Sort(Sort.Direction.DESC,sort);
			}
		}
		pageNum--;
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		Page<Order> noreturnOrderPage = orderDao.findAll(new Specification<Order>(){
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				Integer lastTime = getLastFourMonth();
				predicate.add(cb.greaterThan(root.get("addTime").as(Integer.class), lastTime));
				
				if(StringUtils.isNotBlank(cssOrderReqDto.getOrderSnMain())){
					predicate.add(cb.equal(root.get("orderSnMain"), cssOrderReqDto.getOrderSnMain()));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getBuyerName())){
					predicate.add(cb.equal(root.get("buyerName").as(String.class), cssOrderReqDto.getBuyerName()));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getSellerName())){
					predicate.add(cb.equal(root.get("sellerName"), cssOrderReqDto.getSellerName()));
				}
				
				Integer startTime = null;
				if(StringUtils.isNotBlank(cssOrderReqDto.getStartTime())){
					startTime = (int)(DateUtils.str2Date(cssOrderReqDto.getStartTime()).getTime()/1000);
				}
				Integer endTime = null;
				if(StringUtils.isNotBlank(cssOrderReqDto.getEndTime())){
					endTime = (int)(DateUtils.str2Date(cssOrderReqDto.getEndTime()).getTime()/1000);
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getStartTime())){
					predicate.add(cb.greaterThanOrEqualTo(root.get("addTime").as(Integer.class), startTime));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getEndTime())){
					predicate.add(cb.lessThanOrEqualTo(root.get("addTime").as(Integer.class), endTime));
				}
				predicate.add(cb.equal(root.get("isDel"), 0));
				predicate.add(cb.greaterThan(root.<Double>get("orderPayed"), 0.0));
				
				Predicate payedMoreThanDisaccount = cb.greaterThan(root.<Double>get("orderPayed"), root.<Double>get("discount"));
				Predicate statusCancel = cb.equal(root.get("status"), BkOrderStatusEnum.STATUS_CANCEL.getId());
				Predicate statusInvalid = cb.equal(root.get("status"), BkOrderStatusEnum.STATUS_INVALID.getId());
				Predicate payedMoreThanAmount = cb.greaterThan(root.<Double>get("orderPayed"), 
						cb.sum(root.<Double>get("goodsAmount"), root.<Double>get("shippingFee")));
				Predicate noreturnPredicate = cb.or(cb.and(payedMoreThanDisaccount,cb.or(statusCancel,statusInvalid)),payedMoreThanAmount);
				
				predicate.add(noreturnPredicate);
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		}, pageable);
		List<Order> orderList = new ArrayList<Order>();
		orderList.addAll(noreturnOrderPage.getContent());
		if(CollectionUtils.isEmpty(orderList)) {
			resp.setMessage("订单列表为空");
			return resp;
		}
		
		List<BkOrderListDto> bkOrderList = new ArrayList<BkOrderListDto>();
		for(Order tmp : orderList) {
			BkOrderListDto orderListDto = new BkOrderListDto();
			BkOrderListBaseDto baseDto = createBkOrderBaseDto(tmp);
			orderListDto.setBase(baseDto);
			bkOrderList.add(orderListDto);
		}
		BkOrderListResultDto resultDto = new BkOrderListResultDto();
		resultDto.setOrderCount((int)noreturnOrderPage.getTotalElements());
		resultDto.setOrderList(bkOrderList);
		resp.setResult(resultDto);
		return resp;
	}
	
	private Integer getLastFourMonth(){
		return DateUtils.getTime()-10368000;//返回120天前的时间戳
	}

	@Override
	public BkOrderReturnListRespDto queryBkOrderToReturn(String sort, String order, int pageNum, int pageSize,
			final CssOrderReqDto cssOrderReqDto) {
		BkOrderReturnListRespDto resp = new BkOrderReturnListRespDto(200, "");
		Sort pageSort = new Sort(Sort.Direction.DESC,"orderIdR");
		pageNum--;
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		Page<OrderReturn> page = orderReturnDao.findAll(new Specification<OrderReturn>(){
			@Override
			public Predicate toPredicate(Root<OrderReturn> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicate = new ArrayList<>();
				predicate.add(cb.equal(root.get("orderStatus"), 0));
				if(StringUtils.isNotBlank(cssOrderReqDto.getOrderSnMain())){
					predicate.add(cb.equal(root.get("orderSnMain").as(String.class), cssOrderReqDto.getOrderSnMain()));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getStartTime())){
					predicate.add(cb.greaterThanOrEqualTo(root.get("addTime").as(String.class), cssOrderReqDto.getStartTime()));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getEndTime())){
					predicate.add(cb.lessThanOrEqualTo(root.get("addTime").as(String.class), cssOrderReqDto.getEndTime()));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getQueryType())){
					predicate.add(cb.equal(root.get("orderType"), cssOrderReqDto.getQueryType()));
				}
				if(cssOrderReqDto.getStatus() != null ){
					predicate.add(cb.equal(root.get("orderStatus"), cssOrderReqDto.getStatus()));
				}
				if(cssOrderReqDto.getRefundStatus()!=null){
					predicate.add(cb.equal(root.get("refundStatus"), cssOrderReqDto.getRefundStatus()));
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		},pageable);
		List<OrderReturn> orderReturnList = page.getContent();
		BkOrderReturnListDto bkOrderReturnListDto = new BkOrderReturnListDto();
		bkOrderReturnListDto.setCount((int)page.getTotalElements());
		List<BkOrderReturnDto> returnList = new ArrayList<BkOrderReturnDto>();
		for(OrderReturn tmp: orderReturnList){
			BkOrderReturnDto dto = createBkOrderReturn(tmp);
			returnList.add(dto);
		}
		bkOrderReturnListDto.setOrderReturnList(returnList);
		resp.setResult(bkOrderReturnListDto);
		return resp;
	}

	@Override
	public String cancelOrderReturn(Integer orderIdR) {
		OrderReturn orderReturn = orderReturnDao.findOne(orderIdR);
		if(orderReturn.getRefundStatus()==1){
			return "has_refund";
		}
		int count = orderReturnDao.updateOrderStatusByOrderIdR(orderIdR, 1);
		if(count>0){
			return "cancel_success"; 
		}
		return "cancel_fail";
	}

	@Override
	public List<BkOrderReturnDto> getOrderReturnsByIds(List<Integer> ids) {
		List<OrderReturn> orderReturns = orderReturnDao.findByOrderIdRIn(ids);
		List<BkOrderReturnDto> resultList = new ArrayList<BkOrderReturnDto>();
		for(OrderReturn tmp: orderReturns){
			resultList.add(createBkOrderReturn(tmp));
		}
		Collections.sort(resultList,new Comparator<BkOrderReturnDto>(){
			@Override
			public int compare(BkOrderReturnDto o1, BkOrderReturnDto o2) {
				return o2.getOrderIdR() - o1.getOrderIdR();
			}
		});
		return resultList;
	}
	/**
	 * 根据待退款订单的原始信息组装成返回到页面的信息
	 * @param orderReturn
	 * @return
	 */
	private BkOrderReturnDto createBkOrderReturn(OrderReturn orderReturn){
		BkOrderReturnDto dto = new BkOrderReturnDto();
		dto.setOrderIdR(orderReturn.getOrderIdR());
		dto.setOrderSnMain(orderReturn.getOrderSnMain());
		dto.setOrderId(orderReturn.getOrderId());
		dto.setBuyerId(orderReturn.getBuyerId());
		dto.setSellerId(orderReturn.getSellerId());
		dto.setReturnAmount(orderReturn.getReturnAmount());
		dto.setAddTime(orderReturn.getAddTime());
		dto.setGoodsType(orderReturn.getGoodsType());
		dto.setOrderStatus(orderReturn.getOrderStatus());
		dto.setOrderType(orderReturn.getOrderType());
		dto.setGoodsStatus(orderReturn.getGoodsStatus());
		dto.setRefundStatus(orderReturn.getRefundStatus());
		dto.setStatementStatus(orderReturn.getStatementStatus());
		dto.setPostscript(orderReturn.getPostscript());
		return dto;
	}

	@Override
	public List<BkOrderActionRespDto> getOrderActions(String orderSnMain) {
		//查找所有的订单操作
		List<OrderAction> orderActionList = orderActionDao.findByOrderSnMain(orderSnMain);
		List<BkOrderActionRespDto> resultList = new ArrayList<BkOrderActionRespDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(OrderAction tmp: orderActionList){
			BkOrderActionRespDto dto = new BkOrderActionRespDto();
			String actionTime = dateFormat.format(tmp.getActionTime());
			dto.setActionTime(actionTime);
			dto.setNote(tmp.getNotes());
			dto.setActor(tmp.getActor());
			resultList.add(dto);
		}
		//查找支付信息
		List<OrderPay> orderPayList = orderPayDao.findByOrderSnMain(orderSnMain);
		List<Order> orderList = orderDao.findByOrderSnMain(orderSnMain);
		String buyerName = "";
		if(orderList!=null && orderList.size()>0){
			buyerName = orderList.get(0).getBuyerName();
		}
		
		for(OrderPay orderPay: orderPayList){
			BkOrderActionRespDto dto = new BkOrderActionRespDto();
			String payTime = DateUtils.dateTimeToStr((int)orderPay.getPayTime());
			String payType = BkOrderPayTypeEnum.parseType(orderPay.getPaymentId()).getPayType();
			Double money = orderPay.getMoney();
			String mobile = orderPay.getMobil();
			dto.setActionTime(payTime);
			dto.setActor(buyerName);
			String note = "您通过 "+ payType +"方式付款 "+money+" 元";
			if(StringUtils.isNotBlank(mobile)){
				note += " 手机号："+money;
			}
			dto.setNote(note);
			resultList.add(dto);
		}
		Collections.sort(resultList, new Comparator<BkOrderActionRespDto>(){
			@Override
			public int compare(BkOrderActionRespDto o1, BkOrderActionRespDto o2) {
				return o1.getActionTime().compareTo(o2.getActionTime());
			}
		});
		return resultList;
	}

	@Override
	public BkOrderListRespDto queryBkOrderListByBuyerId(int pageNum, int pageSize, Integer buyerId) {
		Sort pageSort = new Sort(Sort.Direction.DESC,"orderId");
		pageNum--;
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		Page<Order> orderPage = orderDao.findByBuyerId(buyerId, pageable);
		List<Order> orderList = orderPage.getContent();
		List<BkOrderListBaseDto> resultList = new ArrayList<BkOrderListBaseDto>();
		for(Order order: orderList){
			BkOrderListBaseDto dto = this.createBkOrderBaseDto(order);
			resultList.add(dto);
		}
		BkOrderListRespDto resp = new BkOrderListRespDto(200, "");
		Iterator<SiteCity> siteCityInterator = siteCityDao.findAll().iterator();
		Map<String,String> cityMap = new HashMap<String,String>();
		while(siteCityInterator.hasNext()){
			SiteCity siteCity = siteCityInterator.next();
			cityMap.put(siteCity.getSiteCityId(), siteCity.getSiteCityName());
		}
		List<String> orderSnMains = new ArrayList<String>();
		for(Order order: orderList){
			if(StringUtils.isNotBlank(order.getOrderSnMain())){
				orderSnMains.add(order.getOrderSnMain());
			}
		}
		//找出收货信息
		List<OrderExtm> orderExtmList = orderExtmDao.findByOrderSnMainIn(orderSnMains);
		Map<String, OrderExtm> orderExtmMap = new HashMap<String, OrderExtm>();
		for(OrderExtm orderExtm: orderExtmList){
			orderExtmMap.put(orderExtm.getOrderSnMain(), orderExtm);
		}
		List<BkOrderListDto> bkOrderList = new ArrayList<BkOrderListDto>();
		for(Order tmp : orderList) {
			BkOrderListDto orderListDto = new BkOrderListDto();
			BkOrderListBaseDto baseDto = createBkOrderBaseDto(tmp);
			OrderExtm orderExtm = orderExtmMap.get(baseDto.getOrderSnMain());
			BkExtmMsgDto bkOrderExtm = createExtmMsg(orderExtm);
			baseDto.setSellSite(cityMap.get(tmp.getSellSite()));//城市
			orderListDto.setExtmMsg(bkOrderExtm);
			orderListDto.setBase(baseDto);
			bkOrderList.add(orderListDto);
		}
		BkOrderListResultDto resultDto = new BkOrderListResultDto();
		resultDto.setOrderCount((int)orderPage.getTotalElements());
		resultDto.setOrderList(bkOrderList);
		resp.setResult(resultDto);
		return resp;
	}

	@Override
	public BkVaccountListResultRespDto queryBkVaccountResult(int pageNum, int pageSize, Integer buyerId) {
		BkVaccountListResultRespDto bkVaccountListResultRespDto = new BkVaccountListResultRespDto(200,"");
		Member member = memberDao.findOne(buyerId);
		if(buyerId == 0 || member == null){
			return bkVaccountListResultRespDto;
		}
		String buyerName = member.getUserName();
		VirtualAccountProcessor virtualAccountProcessor = VirtualAccountProcessor.getProcessor();
		VaccountWaterBillQueryRespVO resp = virtualAccountProcessor.queryWaterBillByUserId(pageNum, pageSize,buyerId);
		String result = resp.getResult();
		JSONObject json = new JSONObject(result);
		Integer totalElement = json.getInt("total");
		JSONArray jsonArray = json.getJSONArray("rows");
		List<BkVaccountRespDto> vaccountList = new ArrayList<BkVaccountRespDto>();
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject temp = jsonArray.getJSONObject(i);
			BkVaccountRespDto dto = new BkVaccountRespDto();
			dto.setOrderSnMain(temp.getString("order_sn_main"));
			Double countValue = temp.getDouble("countvalue");
			if(countValue < 0){
				dto.setOutAmount(countValue);
			}else{
				dto.setInAmount(countValue);
			}
			dto.setBuyerName(buyerName);
			String note = temp.getString("note");
			try {
				dto.setNote(new String(note.getBytes("ISO-8859-1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			dto.setAddTime(temp.getString("add_time"));
			vaccountList.add(dto);
		}
		bkVaccountListResultRespDto.setTotalElement(totalElement);
		bkVaccountListResultRespDto.setBkVaccountRespDtoList(vaccountList);
		return bkVaccountListResultRespDto;
	}

	@Override
	public BkCouponListRespDto queryCouponListByUserId(int pageNum, int pageSize, Integer buyerId) {
		BkCouponListRespDto couponListRespDto = new BkCouponListRespDto(200,"");
		if(buyerId == 0){
			return couponListRespDto;
		}
		Sort pageSort = new Sort(Sort.Direction.DESC,"id");
		pageNum--;
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		//获取相应的优惠券
		Page<CoupList> coupListPage = coupListDao.findByUserId(buyerId, pageable);
		List<CoupList> coupList = coupListPage.getContent();
		BkCouponListResultDto resultDto = new BkCouponListResultDto();
		//获取优惠券使用规则
		List<Integer> couponIds = new ArrayList<Integer>();
		List<String> usedCommonCodes = new ArrayList<String>();
		for(CoupList tmp: coupList){
			if(!couponIds.contains(tmp.getCouponId())){
				couponIds.add(tmp.getCouponId());
			}
			if(tmp.getIschecked() == 1){
				usedCommonCodes.add(tmp.getCommoncode());
			}
		}
		
		List<CoupRule> rules = coupRuleDao.findByIdIn(couponIds);
		Map<Integer, CoupRule> ruleMap = new HashMap<Integer, CoupRule>();
		for(CoupRule tmp: rules){
			ruleMap.put(tmp.getId(), tmp);
		}
		//获取使用过优惠券的订单
		List<Order> orderList = orderDao.findByUseCouponNoIn(usedCommonCodes);
		Map<String, Order> orderMap = new HashMap<String, Order>();
		for(Order order: orderList){
			orderMap.put(order.getUseCouponNo(), order);
		}
		//获取优惠券类别
		List<Integer> typeIds = new ArrayList<Integer>();
		for(CoupRule tmp: rules){
			if(!typeIds.contains(tmp.getTypeid())){
				typeIds.add(tmp.getTypeid());
			}
		}
		List<CoupType> types = coupTypeDao.findByIdIn(typeIds);
		Map<Integer, CoupType> typeMap = new HashMap<Integer, CoupType>();
		for(CoupType tmp: types){
			typeMap.put(tmp.getId(), tmp);
		}
		List<BkCouponListDto> couponList = new ArrayList<BkCouponListDto>();
		for(CoupList coup : coupList){
			CoupRule rule = ruleMap.get(coup.getCouponId());
			CoupType type = typeMap.get(rule.getTypeid());
			Order order = orderMap.get(coup.getCommoncode());
			BkCouponListDto dto = new BkCouponListDto();
			dto.setCommonCode(coup.getCommoncode());
			dto.setMoney(coup.getMoney());
			dto.setCouponName(rule.getCouponName());
			dto.setCouponTypeId(rule.getCoupontypeid());
			dto.setCouponFormId(type.getTypeid());
			dto.setIsSue(coup.getIssue());
			dto.setIsChecked(coup.getIschecked());
			if(order!=null){
				dto.setOrderSnMain(order.getOrderSnMain());
			}
			if(coup.getUsedtime() != null){
				dto.setUsedTime(DateUtils.date2DateStr(coup.getUsedtime()));
			}
			if(coup.getCreatetime() != null){
				dto.setCreateTime(DateUtils.date2DateStr(coup.getCreatetime()));
			}
			if(coup.getEndtime()!=null){
				dto.setEndTime(DateUtils.date2DateStr(coup.getEndtime()));
			}
			couponList.add(dto);
		}
		resultDto.setBkCouponList(couponList);
		Integer total = coupListPage.getNumberOfElements();
		resultDto.setTotal(total);
		couponListRespDto.setResult(resultDto);
		return couponListRespDto;
	}

	@Override
	public List<BkTxkDto> queryTxkListByUserId(Integer userId) {
		TxkMemberCardsRespVO resp = AccountTxkProcessor.getProcessor().getTxkListByUserId(userId);
		List<TxkCardRowRespVO> txkList = resp.getRows();
		List<BkTxkDto> resultList = new ArrayList<BkTxkDto>();
		for(TxkCardRowRespVO tmp: txkList){
			BkTxkDto dto = new BkTxkDto();
			dto.setActiveTime(tmp.getActiveTime());
			dto.setAmount(tmp.getAmount());
			dto.setCardnum(tmp.getCardnum());
			dto.setEndTime(tmp.getEndTime());
			dto.setId(tmp.getId());
			dto.setResidueAmount(tmp.getResidueAmount());
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public BkTxkRecordListRespDto queryTxkRecordListByUserId(Integer pageNum, Integer pageSize, Integer buyerId) {
		BkTxkRecordListRespDto respDto = new BkTxkRecordListRespDto(200,"");
		List<BkTxkRecordDto> recordList = new ArrayList<BkTxkRecordDto>();
		TxkCardRecordRespVO respVO = AccountTxkProcessor.getProcessor().findTxkRecords(buyerId, pageNum, pageSize);
		if(respVO == null){
			return respDto;
		}
		List<TxkCardRecordDetailRespVO> recordDetailList = respVO.getRows();
		for(TxkCardRecordDetailRespVO tmp: recordDetailList){
			BkTxkRecordDto dto = new BkTxkRecordDto();
			dto.setCardNum(tmp.getCardNum());
			dto.setCurrentAmount(tmp.getCurrentAmount());
			dto.setOrderCode(tmp.getOrderCode());
			dto.setUseAmount(tmp.getUseAmount());
			dto.setUseTime(tmp.getUseTime());
			recordList.add(dto);
		}
		respDto.setRecordList(recordList);
		respDto.setTotal(respVO.getTotal());
		return respDto;
	}
}
