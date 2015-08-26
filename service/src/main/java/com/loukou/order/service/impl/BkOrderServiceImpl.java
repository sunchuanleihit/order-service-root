package com.loukou.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.loukou.order.service.api.BkOrderService;
import com.loukou.order.service.constants.BaseDtoType;
import com.loukou.order.service.constants.OrderStateReturn;
import com.loukou.order.service.constants.ShippingMsgDesc;
import com.loukou.order.service.dao.ExpressDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.SiteCityDao;
import com.loukou.order.service.dao.SiteDao;
import com.loukou.order.service.dao.StoreDao;
import com.loukou.order.service.entity.Express;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.Site;
import com.loukou.order.service.entity.SiteCity;
import com.loukou.order.service.entity.Store;
import com.loukou.order.service.enums.BkOrderSourceEnum;
import com.loukou.order.service.enums.BkOrderStatusEnum;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.OrderPayTypeEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.OrderTypeEnums;
import com.loukou.order.service.enums.PayStatusEnum;
import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.BkExtmMsgDto;
import com.loukou.order.service.resp.dto.BkOrderListBaseDto;
import com.loukou.order.service.resp.dto.BkOrderListDto;
import com.loukou.order.service.resp.dto.BkOrderListRespDto;
import com.loukou.order.service.resp.dto.BkOrderListResultDto;
import com.loukou.order.service.resp.dto.BkOrderReturnDto;
import com.loukou.order.service.resp.dto.BkOrderReturnListDto;
import com.loukou.order.service.resp.dto.BkOrderReturnListRespDto;
import com.loukou.order.service.resp.dto.CssOrderRespDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.ShippingListDto;
import com.loukou.order.service.resp.dto.ShippingListResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.ShippingMsgRespDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.order.service.util.DoubleUtils;

@Service("bkOrderService")
public class BkOrderServiceImpl implements BkOrderService{
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
	private OrderGoodsDao orderGoodsDao;
    
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
    private OrderReturnDao orderReturnDao;
    
	@Override
	public List<CssOrderRespDto> queryOrderList(int page, int rows, final CssOrderReqDto cssOrderReqDto) {
		
		Page<Order> orderPage = orderDao.findAll(new Specification<Order>(){
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Integer startTime = null;
				if(StringUtils.isNoneBlank(cssOrderReqDto.getStartTime())){
					startTime = (int) (DateUtils.str2Date(cssOrderReqDto.getStartTime()).getTime()/1000);
				}
				Integer endTime = null;
				if(StringUtils.isNoneBlank(cssOrderReqDto.getEndTime())){
					endTime = (int) (DateUtils.str2Date(cssOrderReqDto.getEndTime()).getTime()/1000);
				}
				List<Predicate> predicate = new ArrayList<>();
				if(StringUtils.isNoneBlank(cssOrderReqDto.getOrderSnMain())){
					predicate.add(cb.equal(root.get("orderSnMain").as(String.class), cssOrderReqDto.getOrderSnMain()));
				}
				if(cssOrderReqDto.getStatus()!=null){
					predicate.add(cb.equal(root.get("status").as(Integer.class), cssOrderReqDto.getStatus()));
				}
				if(StringUtils.isNoneBlank(cssOrderReqDto.getStartTime())){
					predicate.add(cb.greaterThanOrEqualTo(root.get("addTime").as(Integer.class), startTime));
				}
				if(StringUtils.isNoneBlank(cssOrderReqDto.getEndTime())){
					predicate.add(cb.lessThanOrEqualTo(root.get("addTime").as(Integer.class), endTime));
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		}, new PageRequest(0, 10, Sort.Direction.DESC, "addTime"));
		List<CssOrderRespDto> resultList = new ArrayList<CssOrderRespDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(Order order: orderPage){
			CssOrderRespDto cssOrderRespDto = new CssOrderRespDto();
			cssOrderRespDto.setOrderId(order.getOrderId());
			cssOrderRespDto.setOrderSnMain(order.getOrderSnMain());
			cssOrderRespDto.setSource(order.getSource());
			if(order.getNeedShiptime()!=null){
				cssOrderRespDto.setNeedShiptime(dateFormat.format(order.getNeedShiptime()) + " " + order.getNeedShiptimeSlot());
			}
			cssOrderRespDto.setStatus(order.getStatus());
			cssOrderRespDto.setNeedInvoice(order.getNeedInvoice());
			cssOrderRespDto.setInvoiceNo(order.getInvoiceNo());
			cssOrderRespDto.setBuyerName(order.getBuyerName());
			resultList.add(cssOrderRespDto); 
		}
		return resultList;
/*		
		List<CssOrderRespDto> resultList = new ArrayList<CssOrderRespDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(orderList!=null){
			for(Order order: orderList){
				CssOrderRespDto cssOrderRespDto = new CssOrderRespDto();
				cssOrderRespDto.setOrderId(order.getOrderId());
				cssOrderRespDto.setOrderSnMain(order.getOrderSnMain());
				cssOrderRespDto.setSource(order.getSource());
				if(order.getNeedShiptime()!=null){
					cssOrderRespDto.setNeedShiptime(dateFormat.format(order.getNeedShiptime()) + " " + order.getNeedShiptimeSlot());
				}
				cssOrderRespDto.setStatus(order.getStatus());
				cssOrderRespDto.setNeedInvoice(order.getNeedInvoice());
				cssOrderRespDto.setInvoiceNo(order.getInvoiceNo());
				cssOrderRespDto.setBuyerName(order.getBuyerName());
				resultList.add(cssOrderRespDto); 
			}
		}
		return resultList;
		*/
	}
	
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
	
//	/**
//	 * 判断是否显示 确认收货
//	 *
//	 * 返回 1 显示 ==商家已发货 且 没有回单(商家自己配送的) 0 不显示 2 显示等待收货==审单通过且商家未发货
//	 *
//	 * status : ok
//	 */
//	private int getReciveStatus(Order order) {
////		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
//		if (null == order) {
//			return 0;
//		}
//
//		if (order.getStatus() >= OrderStatusEnum.STATUS_REVIEWED.getId() && 
//				order.getStatus() <= OrderStatusEnum.STATUS_DELIVERIED.getId()) {// 审核通过 未发货
//			return 2;
//		} else if (order.getStatus() >= OrderStatusEnum.STATUS_14.getId() 
//				&& order.getStatus() < OrderStatusEnum.STATUS_FINISHED.getId()
//				&& order.getShippingId() == 1) {
//			// 商家已发货 且 没有回单(商家自己配送的)
//			return 1;
//		}
//		return 0;
//	}
	
	/**
	 * 默认taoOrderSn传入“”
	 */
	private String getShippingMsg(Order order) {
//		List<Order> orders = new ArrayList<Order>();
		StringBuilder shippingMsg = new StringBuilder();
		
		if(order != null) {
			if (StringUtils.equals(order.getType(), OrderTypeEnums.TYPE_BOOKING.getType())) {
				shippingMsg.append("预售商品").append(DateUtils.date2DateStr(order.getNeedShiptime()))
						.append(" ").append(order.getNeedShiptimeSlot())
						.append("、");
			} else if (StringUtils.equals(order.getType(), OrderTypeEnums.TYPE_WEI_WH.getType())
					|| StringUtils.equals(order.getType(), OrderTypeEnums.TYPE_WEI_SELF.getType())) {
				shippingMsg.append("普通商品").append(DateUtils.date2DateStr(order.getNeedShiptime()))
						.append(" ").append(order.getNeedShiptimeSlot())
						.append("、");
			} else if (StringUtils.equals(order.getType(), OrderTypeEnums.TYPE_SELF_SALES.getType())) {
				shippingMsg.append("配送单预计1-3日内送达  、");
			}
		}

		return StringUtils.removeEnd(shippingMsg.toString(), "、");
	}
	
	private BkOrderListBaseDto createBaseDto(Order order, int type) {
		BkOrderListBaseDto baseDto = new BkOrderListBaseDto();
		baseDto.setOrderId(order.getOrderId());
		baseDto.setOrderSnMain(order.getOrderSnMain());
		baseDto.setSellerId(order.getSellerId());
		baseDto.setSourceName(BkOrderSourceEnum.parseSource(order.getSource()).getSource());
		if (order.getAddTime() != null && order.getAddTime() != 0) {
			String addTime = DateUtils.dateTimeToStr(order.getAddTime());
			baseDto.setAddTimeStr(addTime);
		}
		if (order.getPayTime() != null && order.getPayTime() != 0) {
			String payTime = DateUtils.dateTimeToStr(order.getPayTime());
			baseDto.setPayTimeToString(payTime);
		}
		if (order.getShipTime() != null && order.getShipTime() != 0) {
			String shipTime = DateUtils.dateTimeToStr(order.getShipTime());
			baseDto.setShipTimeToString(shipTime);
		}
		baseDto.setPayStatus(order.getPayStatus());
		baseDto.setStatus(order.getStatus());
		baseDto.setTaoOrderSn(order.getTaoOrderSn());
//		baseDto.setIsshouhuo(getReciveStatus(order));// 确认收货的判断
		double totalPrice = DoubleUtils.add(order.getGoodsAmount(), order.getShippingFee());
		baseDto.setTotalPrice(totalPrice);
		double needToPay = DoubleUtils.sub(totalPrice, order.getOrderPayed());
		baseDto.setNeedPayPrice(needToPay);// 还需支付金额
		
		baseDto.setStatusName(createState(order));
		
		baseDto.setShippingFee(order.getShippingFee());// 订单运费
//		baseDto.setPackageStatus(ReturnStatusEnum.parseType(order.getStatus()).getComment());// 包裹的状态
		baseDto.setShipping(getShippingMsg(order));
		if(order.getShippingNo() == null) {
			baseDto.setShippingNo("");
		} else {
			baseDto.setShippingNo(order.getShippingNo());
		}
		
		baseDto.setDiscount(order.getDiscount());
//		baseDto.setIsOrder(BaseDtoIsOrderType.NO);
//		String owerphone = "";
		if(type == BaseDtoType.INFO) {
			Store store = storeDao.findOne(order.getSellerId());
//			if(store != null) {
//				if (StringUtils.isBlank(store.getOwnerMob())) {
//					if (StringUtils.isNotBlank(store.getOwnerTel())) {
//						store.setOwnerMob(store.getOwnerTel());
//					}
//				} else {
//					owerphone = store.getOwnerMob();
//				}
//			}
//			baseDto.setStorePhone(owerphone);
//			baseDto.setRefundStatus("");//TODO线上是否需要
			int freight = 0;
			if(store == null) {
				freight = 0;
			} else {
				freight = store.getFreight();
			}
			String shippingtype = "淘常州自送";

			if (freight != 0) {
				freight = 1;
				shippingtype = "第三方配送";
			}
			baseDto.setShippingType(shippingtype);
			baseDto.setPayTypeToString(OrderPayTypeEnum.parseType(order.getPayType()).getType());
			baseDto.setInvoiceHeader(order.getInvoiceHeader());
			baseDto.setPostscript(order.getPostscript());
		}
		return baseDto;
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
		resp.setInnerCode(0);//内部调用需要 FIXME
		return resp;
	}

	@Override
	public BkOrderListRespDto queryBkOrderList(String sort,String order,int pageNum, int pageSize, final CssOrderReqDto cssOrderReqDto) {
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
			}else{
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
		Page<Order> orderPageList = null;
		
		Sort pageSort = new Sort(Sort.Direction.DESC, "orderId");
		if(StringUtils.isNoneBlank(sort) && StringUtils.isNoneBlank(order)){
			if(order.equals("asc")){
				pageSort = new Sort(Sort.Direction.ASC, sort);
			}else{
				pageSort = new Sort(Sort.Direction.DESC, sort);
			}
		}
		
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		orderPageList = orderDao.findAll(new Specification<Order>(){
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Integer startTime = null;
				if(StringUtils.isNotBlank(cssOrderReqDto.getStartTime())){
					startTime = (int)(DateUtils.str2Date(cssOrderReqDto.getStartTime()).getTime()/1000);
				}
				Integer endTime = null;
				if(StringUtils.isNotBlank(cssOrderReqDto.getEndTime())){
					endTime = (int)(DateUtils.str2Date(cssOrderReqDto.getEndTime()).getTime()/1000);
				}
				List<Predicate> predicate = new ArrayList<>();
				if(StringUtils.isNotBlank(cssOrderReqDto.getOrderSnMain())){
					predicate.add(cb.equal(root.get("orderSnMain").as(String.class), cssOrderReqDto.getOrderSnMain()));
				}
				if(cssOrderReqDto.getStatus()!=null){
					predicate.add(cb.equal(root.get("status").as(Integer.class), cssOrderReqDto.getStatus()));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getStartTime())){
					predicate.add(cb.greaterThanOrEqualTo(root.get("addTime").as(Integer.class), startTime));
				}
				if(StringUtils.isNotBlank(cssOrderReqDto.getEndTime())){
					predicate.add(cb.lessThanOrEqualTo(root.get("addTime").as(Integer.class), endTime));
				}
				if(cssOrderReqDto.getPayStatus()!=null){
					predicate.add(cb.equal(root.get("payStatus").as(Integer.class), cssOrderReqDto.getPayStatus()));
				}
				if(StringUtils.isNoneBlank(cssOrderReqDto.getBuyerName())){
					predicate.add(cb.equal(root.get("buyerName").as(String.class), cssOrderReqDto.getBuyerName()));
				}
				if(StringUtils.isNoneBlank(cityCodeFinal) && StringUtils.isNoneBlank(cssOrderReqDto.getQueryContent())){
					predicate.add(cb.equal(root.get("sellSite").as(String.class), cssOrderReqDto.getQueryContent()));
				}
				if(orderSnMainSet.size()>0){
					In in = cb.in(root.get("orderSnMain"));
					for(String tmp: orderSnMainSet){
						in.value(tmp);
					}
					predicate.add(in);
				}
				predicate.add(cb.equal(root.get("isDel").as(Integer.class), 0));
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		}, pageable);
		orderList.addAll(orderPageList.getContent());
		if(CollectionUtils.isEmpty(orderList)) {
			resp.setMessage("订单列表为空");
			return resp;
		}
		
		List<String> orderSnMains = new ArrayList<String>();
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
		resultDto.setOrderCount((int)orderPageList.getTotalElements());
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
			BkOrderReturnDto dto = new BkOrderReturnDto();
			dto.setOrderIdR(tmp.getOrderIdR());
			dto.setOrderSnMain(tmp.getOrderSnMain());
			dto.setOrderId(tmp.getOrderId());
			dto.setBuyerId(tmp.getBuyerId());
			dto.setSellerId(tmp.getSellerId());
			dto.setReturnAmount(tmp.getReturnAmount());
			dto.setAddTime(tmp.getAddTime());
			dto.setGoodsType(tmp.getGoodsType());
			dto.setOrderStatus(tmp.getOrderStatus());
			dto.setOrderType(tmp.getOrderType());
			dto.setGoodsStatus(tmp.getGoodsStatus());
			dto.setRefundStatus(tmp.getRefundStatus());
			dto.setStatementStatus(tmp.getStatementStatus());
			dto.setPostscript(tmp.getPostscript());
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
}
