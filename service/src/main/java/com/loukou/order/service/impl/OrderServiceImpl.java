package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.constants.BaseDtoIsOrderType;
import com.loukou.order.service.constants.BaseDtoType;
import com.loukou.order.service.constants.CouponType;
import com.loukou.order.service.constants.FlagType;
import com.loukou.order.service.constants.OS;
import com.loukou.order.service.constants.OrderPayType;
import com.loukou.order.service.constants.OrderStateReturn;
import com.loukou.order.service.constants.ReturnGoodsType;
import com.loukou.order.service.constants.ShippingMsgDesc;
import com.loukou.order.service.dao.AddressDao;
import com.loukou.order.service.dao.AsyncTaskDao;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.CoupTypeDao;
import com.loukou.order.service.dao.ExpressDao;
import com.loukou.order.service.dao.GoodsSpecDao;
import com.loukou.order.service.dao.LKWhStockInDao;
import com.loukou.order.service.dao.LKWhStockInGoodsDao;
import com.loukou.order.service.dao.LkConfigureDao;
import com.loukou.order.service.dao.LkStatusDao;
import com.loukou.order.service.dao.LkStatusItemDao;
import com.loukou.order.service.dao.LkWhDeliveryDao;
import com.loukou.order.service.dao.LkWhDeliveryOrderDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderGoodsRDao;
import com.loukou.order.service.dao.OrderLnglatDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderPayRDao;
import com.loukou.order.service.dao.OrderRefuseDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.PaymentDao;
import com.loukou.order.service.dao.SiteDao;
import com.loukou.order.service.dao.StoreDao;
import com.loukou.order.service.dao.TczcountRechargeDao;
import com.loukou.order.service.dao.WeiCangGoodsStoreDao;
import com.loukou.order.service.entity.Address;
import com.loukou.order.service.entity.AsyncTask;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.Express;
import com.loukou.order.service.entity.LKWhStockIn;
import com.loukou.order.service.entity.LKWhStockInGoods;
import com.loukou.order.service.entity.LkConfigure;
import com.loukou.order.service.entity.LkStatus;
import com.loukou.order.service.entity.LkStatusItem;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderLnglat;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.OrderPayR;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.Site;
import com.loukou.order.service.entity.Store;
import com.loukou.order.service.entity.WeiCangGoodsStore;
import com.loukou.order.service.enums.AsyncTaskActionEnum;
import com.loukou.order.service.enums.AsyncTaskStatusEnum;
import com.loukou.order.service.enums.OpearteTypeEnum;
import com.loukou.order.service.enums.OrderActionTypeEnum;
import com.loukou.order.service.enums.OrderPayTypeEnum;
import com.loukou.order.service.enums.OrderReturnGoodsStatusEnum;
import com.loukou.order.service.enums.OrderReturnGoodsType;
import com.loukou.order.service.enums.OrderSourceEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.OrderTypeEnums;
import com.loukou.order.service.enums.PayStatusEnum;
import com.loukou.order.service.enums.PaymentEnum;
import com.loukou.order.service.enums.ReturnGoodsStatus;
import com.loukou.order.service.enums.ReturnOrderStatus;
import com.loukou.order.service.enums.ReturnStatusEnum;
import com.loukou.order.service.enums.WeiCangGoodsStoreStatusEnum;
import com.loukou.order.service.req.dto.OrderListParamDto;
import com.loukou.order.service.req.dto.ReturnStorageGoodsReqDto;
import com.loukou.order.service.req.dto.ReturnStorageReqDto;
import com.loukou.order.service.req.dto.SpecShippingTime;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.CouponListResultDto;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.LkStatusItemDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderBonusRespDto;
import com.loukou.order.service.resp.dto.OrderCancelRespDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.OrderListBaseDto;
import com.loukou.order.service.resp.dto.OrderListDto;
import com.loukou.order.service.resp.dto.OrderListInfoDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.OrderListResultDto;
import com.loukou.order.service.resp.dto.PayBeforeRespDto;
import com.loukou.order.service.resp.dto.PayOrderMsgDto;
import com.loukou.order.service.resp.dto.PayOrderMsgRespDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ReturnStorageRespDto;
import com.loukou.order.service.resp.dto.ShareDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShareResultDto;
import com.loukou.order.service.resp.dto.ShippingListDto;
import com.loukou.order.service.resp.dto.ShippingListResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.ShippingMsgRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderResultDto;
import com.loukou.order.service.resp.dto.UserOrderNumRespDto;
import com.loukou.order.service.resp.dto.basic.RespDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.txk.req.TxkCardRefundRespVO;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;
import com.loukou.pos.client.vaccount.resp.VaccountUpdateRespVO;
import com.loukou.search.service.api.GoodsSearchService;
import com.loukou.search.service.dto.GoodsCateDto;
import com.serverstarted.cart.service.api.CartService;
import com.serverstarted.cart.service.constants.PackageType;
import com.serverstarted.cart.service.resp.dto.CartGoodsRespDto;
import com.serverstarted.cart.service.resp.dto.CartRespDto;
import com.serverstarted.cart.service.resp.dto.PackageRespDto;
import com.serverstarted.goods.service.api.GoodsService;
import com.serverstarted.goods.service.api.GoodsSpecService;
import com.serverstarted.goods.service.resp.dto.GoodsRespDto;
import com.serverstarted.goods.service.resp.dto.GoodsSpecRespDto;
import com.serverstarted.goods.service.resp.dto.GoodsStockInfoRespDto;
import com.serverstarted.store.service.api.StoreService;
import com.serverstarted.store.service.resp.dto.StoreRespDto;
import com.serverstarted.user.api.UserService;
import com.serverstarted.user.resp.dto.UserRespDto;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = Logger
			.getLogger(OrderServiceImpl.class);

//	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat(
//			"###,###.##");
	private VirtualAccountProcessor virtualAccountProcessor = VirtualAccountProcessor
			.getProcessor();
	private AccountTxkProcessor accountTxkProcessor = AccountTxkProcessor
			.getProcessor();

	private static final int LIMIT_COUPON_PER_DAY = 20; // 每天限用优惠券张数

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderReturnDao orderRDao;

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private OrderPayDao orderPayDao;

	@Autowired
	private OrderPayRDao orderPayRDao;

	@Autowired
	private OrderGoodsDao orderGoodsDao;

	@Autowired
	private ExpressDao expressDao;// 快递公司代码及名称

	@Autowired
	private OrderActionDao orderActionDao;

	@Autowired
	private OrderExtmDao orderExtmDao;

	@Autowired
	private CoupListDao coupListDao;

	@Autowired
	private CoupRuleDao coupRuleDao;

	@Autowired
	private CoupTypeDao coupTypeDao;

	@Autowired
	private SiteDao siteDao;

	@Autowired
	private GoodsSpecDao goodsSpecDao;

	@Autowired
	private WeiCangGoodsStoreDao lkWhGoodsStoreDao;

	@Autowired
	private CartService cartService;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private TczcountRechargeDao tczcountRechargeDao;

	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private AddressDao addressDao;

	@Autowired
	private StoreService storeService;

	@Autowired 
	private GoodsService goodsService;
	
	@Autowired 
	private GoodsSpecService goodsSpecService;
	
	@Autowired 
	private CoupListService coupListService;
	
	@Autowired 
	private OrderLnglatDao orderLnglatDao;
	
	@Autowired 
	private GoodsSearchService goodsSearchService;
	@Autowired
	private OrderGoodsRDao orderGoodsRDao;
	@Autowired
	private LkWhDeliveryOrderDao lkWhDeliveryOrderDao;
	
	@Autowired
	private LkWhDeliveryDao lkWhDeliveryDao;
	

	@Autowired
	private OrderRefuseDao orderRefuseDao;
	
	
	@Autowired
	private OrderOperationProcessor orderOperationProcessor;
	
	@Autowired
	private OrderInfoService OrderInfoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LkStatusDao lkStatusDao;
	
	@Autowired
	private LkStatusItemDao lkStatusItemDao;
	
	@Autowired
	private LkConfigureDao lkConfigureDao;
	
	@Override
	public UserOrderNumRespDto getOrderNum(int userId) {
		UserOrderNumRespDto resp = new UserOrderNumRespDto();
		Pageable pageable = new PageRequest(0, 100000);
		Page<Order> orderList = orderDao.findByBuyerIdAndIsDel(userId, 0, pageable);
		int toPayNum = 0;
		int toRecieve = 0;
		int refund = 0;
		List<Order> orders = orderList.getContent();
		for(Order order : orders) {
			if ((order.getStatus() == OrderStatusEnum.STATUS_NEW.getId() && 
					order.getPayStatus() == PayStatusEnum.STATUS_UNPAY.getId()) 
					|| (order.getStatus() == OrderStatusEnum.STATUS_NEW.getId() && 
					(order.getPayStatus() == PayStatusEnum.STATUS_PART_PAYED.getId()))) {
				toPayNum++;
			} else if ((order.getStatus() == OrderStatusEnum.STATUS_REVIEWED.getId()
					|| order.getStatus() == OrderStatusEnum.STATUS_PICKED.getId()
					|| order.getStatus() == OrderStatusEnum.STATUS_ALLOCATED.getId()
					|| order.getStatus() == OrderStatusEnum.STATUS_PACKAGED.getId()
					|| order.getStatus() == OrderStatusEnum.STATUS_DELIVERIED.getId())
					&& order.getPayStatus() == PayStatusEnum.STATUS_PAYED.getId()
					|| order.getStatus() == OrderStatusEnum.STATUS_NEW.getId()) {
				toRecieve++;
			}	
		}
		
		Page<OrderReturn> orderReturns = orderRDao.findByBuyerIdAndOrderStatus(userId, 0, pageable);
		refund = (int) orderReturns.getTotalElements();
		resp.setPayNum(toPayNum);
		resp.setDeliveryNum(toRecieve);
		resp.setRefundNum(refund);
		return resp;
	}

	@Autowired
	private AsyncTaskDao asyncTaskDao;
	
	@Autowired
	private LKWhStockInDao whStockInDao;
	
	@Autowired
	private LKWhStockInGoodsDao whStockInGoodsDao;
	
	@Override
	public OrderListRespDto getOrderList(int userId, int flag,
			int pageNum, int pageSize) {
		OrderListRespDto resp = new OrderListRespDto(200, "");
		if (userId <= 0 || flag <= 0 || pageNum < 1) {
			resp.setCode(400);
			resp.setMessage("参数错误");
			return resp;
		}
		
		Page<Order> orderPageList = null;
		Page<Order> partPayList = null;
		List<Order> orderList = new ArrayList<Order>();
		List<Integer> statusList = new ArrayList<Integer>();
		Page<OrderReturn> orderReturns = null;
		Set<String> orderSnMains = new HashSet<String>();
		Sort sort = new Sort(Direction.DESC, "orderId");
		Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
		if(flag == FlagType.ALL) {
			orderPageList = orderDao.findByBuyerIdAndIsDel(userId, 0, pageable);
			orderList.addAll(orderPageList.getContent());
		} else if (flag == FlagType.TO_PAY) {
			statusList.add(OrderStatusEnum.STATUS_NEW.getId());
			orderPageList = orderDao.findByBuyerIdAndIsDelAndPayStatusAndStatusIn(userId, 0, 
					PayStatusEnum.STATUS_UNPAY.getId(), statusList, pageable);
			partPayList = orderDao.findByBuyerIdAndIsDelAndPayStatusAndStatusIn(
					userId, 0, PayStatusEnum.STATUS_PART_PAYED.getId(), statusList, pageable);
			orderList.addAll(orderPageList.getContent());
			if(!CollectionUtils.isEmpty(partPayList.getContent())) {
				orderList.addAll(partPayList.getContent());
			}
			
		} else if (flag == FlagType.TO_RECIEVE) {
			statusList.add(OrderStatusEnum.STATUS_REVIEWED.getId());
			statusList.add(OrderStatusEnum.STATUS_PICKED.getId());
			statusList.add(OrderStatusEnum.STATUS_ALLOCATED.getId());
			statusList.add(OrderStatusEnum.STATUS_PACKAGED.getId());
			statusList.add(OrderStatusEnum.STATUS_DELIVERIED.getId());
			statusList.add(OrderStatusEnum.STATUS_NEW.getId());
			orderPageList = orderDao.findByBuyerIdAndIsDelAndPayStatusAndStatusIn(userId, 0, 
					PayStatusEnum.STATUS_PAYED.getId(),statusList, pageable);
			orderList = orderPageList.getContent();
	
		} else if (flag == FlagType.REFUND) {
			orderReturns = orderRDao.findByBuyerIdAndOrderStatus(userId, 0, pageable);
			List<OrderReturn> returns = orderReturns.getContent();
			if( !CollectionUtils.isEmpty(returns)) {
				for (OrderReturn orderReturn : returns) {
					orderSnMains.add(orderReturn.getOrderSnMain());
				}
				orderPageList = orderDao.findByOrderSnMainIn(orderSnMains, pageable);
				orderList = orderPageList.getContent();
			}
		}
		if ((int)orderPageList.getTotalElements() == 0) {
			if(partPayList == null || (int)partPayList.getTotalElements() == 0) {
				resp.setMessage("订单列表为空");
				return resp;
			}
		}

		OrderListResultDto resultDto = new OrderListResultDto();
		List<OrderListDto> orderListResult = new ArrayList<OrderListDto>();
		List<Integer> orderIds = new ArrayList<Integer>();
		for(Order order : orderList) {
			orderIds.add(order.getOrderId());
		}
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderIdIn(orderIds);
		
		Map<String, OrderListDto> orderListMap = new HashMap<String, OrderListDto>();
		
		for(Order order : orderList) {
			OrderListDto orderListDto = new OrderListDto();
			OrderListBaseDto baseDto = createBaseDto(order, BaseDtoType.LIST);
			orderListDto.setBase(baseDto);
	
			List<GoodsListDto> goodsListDtoList = new ArrayList<GoodsListDto>();
			
			for(OrderGoods og : orderGoodsList) {
				if(og.getOrderId() == order.getOrderId()) {
					GoodsListDto goodsListDto = new GoodsListDto();
					BeanUtils.copyProperties(og, goodsListDto);
					goodsListDtoList.add(goodsListDto);
				}
			}
			orderListDto.setGoodsList(goodsListDtoList);
			orderListMap.put(order.getTaoOrderSn(), orderListDto);
		}
		if(orderListMap.isEmpty()) {
			resp.setMessage("订单商品为空");
			return resp;
		} else {
			orderListResult.addAll(orderListMap.values());
			boolean forceMerge = false;
			//合并未支付的子单
			List<OrderListDto> finalListDto = mergeUnpayOrderDto(orderListResult, forceMerge);
			//对合并后的订单降序排序
			List<OrderListDto> sortListDto = sortAfterMerge(finalListDto);
			//合并未支付的子单
			resultDto.setOrderList(sortListDto);
			resultDto.setOrderCount((int)orderPageList.getTotalElements());
			resp.setResult(resultDto);
		}
		return resp;
	}
	
	private List<OrderListDto> sortAfterMerge(List<OrderListDto> orderListResult) {
		List<OrderListDto> result = new ArrayList<OrderListDto>();
		Map<Integer, OrderListDto> orderIdListDto = new TreeMap<Integer, OrderListDto>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);//降序
			}
			
		});
		
		for(OrderListDto dto : orderListResult) {
			orderIdListDto.put(dto.getBase().getOrderId(), dto);
		}
		
		for(Map.Entry<Integer, OrderListDto> dto : orderIdListDto.entrySet()) {
			result.add(dto.getValue());
		}
		return result;
	}
	
	private List<OrderListDto> mergeUnpayOrderDto(List<OrderListDto> orderListResult, boolean forceMerge) {
		List<OrderListDto> result = new ArrayList<OrderListDto>();
		Map<String, OrderListDto> toMerge = new HashMap<String, OrderListDto>();
		String orderSnMain = null;
		OrderListBaseDto baseDto = null;
		for(OrderListDto listDto : orderListResult) {
			if(listDto.getBase().getStatus() < OrderStatusEnum.STATUS_REVIEWED.getId() || forceMerge == true) {
				orderSnMain = listDto.getBase().getOrderSnMain();
				baseDto = listDto.getBase();
				if(toMerge.containsKey(orderSnMain)) {
					
					OrderListDto existDto = toMerge.get(orderSnMain);
					//merge base
					OrderListBaseDto baseExist = existDto.getBase();
					baseExist.setTotalPrice(DoubleUtils.add(baseExist.getTotalPrice(), baseDto.getTotalPrice()));
					baseExist.setNeedPayPrice(DoubleUtils.add(baseExist.getNeedPayPrice(), 
							baseDto.getNeedPayPrice()));
					baseExist.setShippingFee(DoubleUtils.add(baseExist.getShippingFee(), baseDto.getShippingFee()));
					baseExist.setDiscount(DoubleUtils.add(baseExist.getDiscount(), baseDto.getDiscount()));
					baseExist.setTaoOrderSn(baseExist.getOrderSnMain());
					baseExist.setIsOrder(BaseDtoIsOrderType.YES);
					baseExist.setShipping(baseExist.getShipping().concat("、").concat(baseDto.getShipping()));
					existDto.setBase(baseExist);
					//merge goodslist
					List<GoodsListDto> existGoodsDto = existDto.getGoodsList();
					existGoodsDto.addAll(listDto.getGoodsList());
					existDto.setGoodsList(existGoodsDto);
					toMerge.put(orderSnMain, existDto);
					
				} else {
					toMerge.put(orderSnMain, listDto);
				}	
			} else {
				//不存在未支付的则不需要合并
				result.add(listDto);
			}
		}
		result.addAll(toMerge.values());
		return result;
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
	
	private OrderListBaseDto createBaseDto(Order order, int type) {
		OrderListBaseDto baseDto = new OrderListBaseDto();
		baseDto.setOrderId(order.getOrderId());
		baseDto.setOrderSnMain(order.getOrderSnMain());
		baseDto.setSellerId(order.getSellerId());
		baseDto.setSource(OrderSourceEnum.parseSource(order.getSource()).getSource());
		if (order.getAddTime() != null && order.getAddTime() != 0) {
			String addTime = DateUtils.dateTimeToStr(order.getAddTime());
			baseDto.setAddTime(addTime);
		}
		if (order.getPayTime() != null && order.getPayTime() != 0) {
			String payTime = DateUtils.dateTimeToStr(order.getPayTime());
			baseDto.setPayTime(payTime);
		}
		if (order.getShipTime() != null && order.getShipTime() != 0) {
			String shipTime = DateUtils.dateTimeToStr(order.getShipTime());
			baseDto.setShipTime(shipTime);
		}
		baseDto.setPayStatus(order.getPayStatus());
		baseDto.setStatus(order.getStatus());
		baseDto.setTaoOrderSn(order.getTaoOrderSn());
		baseDto.setIsshouhuo(getReciveStatus(order));// 确认收货的判断
		double totalPrice = DoubleUtils.add(order.getGoodsAmount(), order.getShippingFee());
		baseDto.setTotalPrice(totalPrice);
		double needToPay = DoubleUtils.sub(totalPrice, order.getOrderPayed());
		baseDto.setNeedPayPrice(needToPay);// 还需支付金额
		
		baseDto.setState(createState(order));
		
		baseDto.setShippingFee(order.getShippingFee());// 订单运费
		baseDto.setPackageStatus(ReturnStatusEnum.parseType(order.getStatus()).getComment());// 包裹的状态
		baseDto.setShipping(getShippingMsg(order));
		if(order.getShippingNo() == null) {
			baseDto.setArrivalCode("");
		} else {
			baseDto.setArrivalCode(order.getShippingNo());
		}
		
		baseDto.setDiscount(order.getDiscount());
		baseDto.setIsOrder(BaseDtoIsOrderType.NO);
		String owerphone = "";
		if(type == BaseDtoType.INFO) {
			Store store = storeDao.findOne(order.getSellerId());
			if(store != null) {
				if (StringUtils.isBlank(store.getOwnerMob())) {
					if (StringUtils.isNotBlank(store.getOwnerTel())) {
						store.setOwnerMob(store.getOwnerTel());
					}
				} else {
					owerphone = store.getOwnerMob();
				}
			}
			baseDto.setStorePhone(owerphone);
			baseDto.setRefundStatus("");//TODO线上是否需要
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
			baseDto.setShippingtype(shippingtype);
			baseDto.setPayType(OrderPayTypeEnum.parseType(order.getPayType()).getType());
			baseDto.setInvoiceHeader(order.getInvoiceHeader());
			baseDto.setPostscript(order.getPostscript());
		}

		return baseDto;
	}
	
	@Override
	public OrderListRespDto getOrderInfo(int userId, String orderSnMain, int flag, int orderId) {
		OrderListRespDto resp = new OrderListRespDto(200, "");
		if (userId <= 0 || flag <= 0 || StringUtils.isEmpty(orderSnMain)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		List<Order> orderList = orderDao.findByOrderSnMain(orderSnMain);
		if (CollectionUtils.isEmpty(orderList)) {
			resp.setMessage("订单为空");
			return resp;
		}
		
//		List<OrderReturn> orderReturns = orderRDao.findByOrderSnMainAndOrderStatus(orderSnMain, OrderReturnOrderStatus.NORMAL);
//		
//		if (!CollectionUtils.isEmpty(orderReturns)) {
//			flag = FlagType.REFUND;
//		}
		OrderListResultDto resultDto = new OrderListResultDto();
		List<OrderListDto> orderListResult = new ArrayList<OrderListDto>();
		List<Integer> orderIds = new ArrayList<Integer>();
		for(Order order : orderList) {
			orderIds.add(order.getOrderId());
		}
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderIdIn(orderIds);
		
		Map<String, OrderListDto> orderListMap = new HashMap<String, OrderListDto>();
		for(Order order : orderList) {
			//orderListDto
			//baseDto
			OrderListDto orderListDto = new OrderListDto();
			OrderListBaseDto baseDto = createBaseDto(order, BaseDtoType.INFO);
			orderListDto.setBase(baseDto);
			//goodslistDto
			List<GoodsListDto> goodsListDtoList = new ArrayList<GoodsListDto>();
			
			for(OrderGoods og : orderGoodsList) {
				if(og.getOrderId() == order.getOrderId()) {
					GoodsListDto goodsListDto = new GoodsListDto();
					BeanUtils.copyProperties(og, goodsListDto);
					goodsListDtoList.add(goodsListDto);
				}
			}
			orderListDto.setGoodsList(goodsListDtoList);
			//收货信息
			List<OrderExtm> extmList = orderExtmDao.findByOrderSnMain(orderSnMain);
			ExtmMsgDto extmMsgDto = new ExtmMsgDto();
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
			orderListDto.setExtmMsg(extmMsgDto);
			//物流信息
			if(order.getStatus() >= OrderStatusEnum.STATUS_REVIEWED.getId()
					|| (order.getStatus() == OrderStatusEnum.STATUS_NEW.getId() 
						&& order.getPayStatus() == PayStatusEnum.STATUS_PAYED.getId())) {
				getLogistics(order, orderListDto);
			}
			
			orderListMap.put(order.getTaoOrderSn(), orderListDto);
		}
		
		if(orderListMap.isEmpty()) {
			return resp;
		} else {
			orderListResult.addAll(orderListMap.values());
			//合并未支付或者orderid为空的的子单
			boolean forceMerge = false;
			if(orderId == 0) {
				forceMerge = true;
			}
			List<OrderListDto> finalListDto = mergeUnpayOrderDto(orderListResult, forceMerge);
			if (finalListDto.size() > 1 && forceMerge == false) {
				List<OrderListDto> listDto = new ArrayList<OrderListDto>();
				for(OrderListDto dto : finalListDto) {
					if(dto.getBase().getOrderId() == orderId) {
						listDto.add(dto);
					}
				}
				resultDto.setOrderList(listDto);
			} else {
				resultDto.setOrderList(finalListDto);
			}
			
			resultDto.setOrderCount(1);
			resp.setCode(200);
			resp.setResult(resultDto);
		}
		return resp;
	}

	//获取物流信息
	private void getLogistics(Order order, OrderListDto orderListDto) {
		ShippingMsgRespDto shippingDto = getShippingResult(order.getTaoOrderSn());
		ShippingMsgDto shippingMsgDto = new ShippingMsgDto();
		if(shippingDto.getInnerCode() == 0) {
			List<ShippingListDto> shippingList = shippingDto.getResult().getShippingList();
			if(!CollectionUtils.isEmpty(shippingList)) {
				if(StringUtils.isBlank(shippingList.get(0).getDescription())) {
					if(order.getStatus() == OrderStatusEnum.STATUS_DELIVERIED.getId()
							&& order.getStatus() == OrderStatusEnum.STATUS_14.getId()
							) {
						shippingMsgDto.setDescription(ShippingMsgDesc.DELIEVER);
						if(order.getShipTime() == null || order.getShipTime() == 0) {
							shippingMsgDto.setCreatTime("");
						} else {
							String shipTime = DateUtils.dateTimeToStr(order.getShipTime());
							shippingMsgDto.setCreatTime(shipTime);
						}
					} else if (order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
						shippingMsgDto.setDescription(ShippingMsgDesc.FINISH);
						if(order.getFinishedTime() == null || order.getFinishedTime() == 0) {
							shippingMsgDto.setCreatTime("");
						} else {
							String finishedTime = DateUtils.dateTimeToStr(order.getFinishedTime());
							shippingMsgDto.setCreatTime(finishedTime);
						}
					}
				} else {
					shippingMsgDto.setDescription(shippingList.get(0).getDescription());
					if(order.getAddTime() == null || order.getAddTime() == 0) {
						shippingMsgDto.setCreatTime("");
					} else {
						String addTime = DateUtils.dateTimeToStr(order.getAddTime());
						shippingMsgDto.setCreatTime(addTime);
					}
				}
			}
			
		} else {
			if(order.getStatus() >= OrderStatusEnum.STATUS_DELIVERIED.getId()
					&& order.getStatus() <= OrderStatusEnum.STATUS_14.getId()) {
				shippingMsgDto.setDescription(ShippingMsgDesc.DELIEVER);
				if(order.getShipTime() == null || order.getShipTime() == 0) {
					shippingMsgDto.setCreatTime("");
				} else {
					String shipTime = DateUtils.dateTimeToStr(order.getShipTime());
					shippingMsgDto.setCreatTime(shipTime);
				}
			} else if (order.getStatus() == OrderStatusEnum.STATUS_FINISHED.getId()) {
				shippingMsgDto.setDescription(ShippingMsgDesc.FINISH);
				if(order.getFinishedTime() == null || order.getFinishedTime() == 0) {
					shippingMsgDto.setCreatTime("");
				} else {
					String finishedTime = DateUtils.dateTimeToStr(order.getFinishedTime());
					shippingMsgDto.setCreatTime(finishedTime);
				}
			}
		}

		orderListDto.setShippingmsg(shippingMsgDto);
	
	}
	
	@Override
	public CouponListRespDto getCouponList(int cityId, int userId, int storeId,
			String openId) {
		CouponListRespDto resp = new CouponListRespDto(200, "");
		if (cityId <= 0 || userId <= 0 || storeId <= 0
				|| StringUtils.isEmpty(openId)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		// FIXME 查询语句
		List<CoupList> coupLists = coupListDao.getValidCoupLists(userId);// 以及其他的一些过滤条件
		if (coupLists.size() == 0) {
			resp.setCode(200);
			return resp;
		}
		List<Integer> couponIds = new ArrayList<Integer>();
		for (CoupList couplist : coupLists) {
			couponIds.add(couplist.getCouponId());
		}
		List<CoupRule> coupRules = coupRuleDao.findByIdIn(couponIds);
		Map<Integer, CoupRule> ruleMap = Maps.newHashMap();
		for (CoupRule coupRule : coupRules) {
			ruleMap.put(coupRule.getId(), coupRule);
		}
		// List<Integer> coupTypeIds = new ArrayList<Integer>();
		// for(CoupRule coupRule : coupRules) {
		// coupTypeIds.add(coupRule.getTypeid());
		// }
		// List<CoupType> coupTypes = coupTypeDao.findByIdIn(coupTypeIds);
		List<CoupList> validCoupList = Lists.newArrayList();
		CoupList recommendCoupList = null; // 推荐用的券
		CartRespDto cart = cartService.getCart(userId, openId, cityId, storeId);
		for (CoupList coupList : coupLists) {
			CoupRule coupRule = ruleMap.get(coupList.getCouponId());
			if (verifyCoup(userId, openId, cityId, storeId, coupList, cart,
					coupRule)) {
				validCoupList.add(coupList);
				if (recommendCoupList == null) {
					recommendCoupList = coupList;
				} else if (coupList.getMoney() > recommendCoupList.getMoney()) {
					recommendCoupList = coupList;
				}
			}
		}

		CouponListResultDto result = resp.getResult();
		// 组装 dto
		if (validCoupList.size() > 0) {
			List<CouponListDto> couponListDtos = result.getCouponList();
			for (CoupList coupList : validCoupList) {
				String couponName = "";
				CoupRule coupRule = ruleMap.get(coupList.getCouponId());
				if (coupRule.getCoupontypeid() == 1) {
					couponName = "现金券";
				} else {
					couponName = String.format("满%.1f减%.1f",
							coupList.getMinprice(), coupList.getMoney());
				}
				CouponListDto couponListDto = new CouponListDto();
				couponListDto.setCouponId(coupList.getId());
				couponListDto.setCommoncode(coupList.getCommoncode());
				couponListDto.setCouponName(couponName);
				couponListDto.setMoney(coupList.getMoney());
				couponListDto.setCouponMsg(coupRule.getCouponName());
				couponListDto.setEndtime(DateUtils.date2DateStr2(coupList.getEndtime()));
				couponListDtos.add(couponListDto);

				if (coupList == recommendCoupList) {
					result.getRecommend().add(couponListDto);
				}
			}
		}

		// 能否使用券
		int canUse = 1;
		Date now = new Date();
		Date start = DateUtils.getStartofDate(now);
		int count = coupListDao.getUsedCoupNumber(userId, start);
		if (count > LIMIT_COUPON_PER_DAY) {
			// 一天最多只能用20张券
			canUse = 2;
		}
		result.setCanUse(canUse);
		result.setEverydayNum(String.valueOf(20));
		result.setEverydayMsg(String.format("每天限使用%d张优惠券，明天再来吧",
				LIMIT_COUPON_PER_DAY));

		return resp;
	}

	public boolean verifyCoup(int userId, String openId, int cityId,
			int storeId, CoupList coupList, CartRespDto cart, CoupRule coupRule) {
		if (coupList == null || coupRule == null || cart == null) {
			return false;
		}
		
		if (cart.getTotalPrice() < coupList.getMinprice()) {
			// 商品金额不足优惠券最小金额
			return false;
		}
		
		if (coupRule.getCouponType() == CouponType.ALL) {
			// 全场通用
			return true;
		}
		else if (coupRule.getCouponType() == CouponType.STORE) {

			// 店铺券
//			int outIds = getOutId(coupRule);
			// FIXME 目前没有店铺券，不实现
		} else if (coupRule.getCouponType() == CouponType.GOODS) {
			// FIXME 目前没有商品券，不实现
		} else if (coupRule.getCouponType() == CouponType.BRAND) {
			// FIXME 目前没有品牌券，不实现
		}
		else if (coupRule.getCouponType() == CouponType.GOODS) {
			// 分类券可用的分类可以是一级和二级分类
			// 如果商品包含其他分类的商品，不能使用分类优惠券
			List<Integer> cateIds = getOutId(coupRule);
			// 获取所有一级类目
			List<GoodsCateDto> cateOnes = goodsSearchService.getSubCateGoodsList(cityId, storeId, 0);
			Set<Integer> cateOneIds = Sets.newHashSet();
			for (GoodsCateDto g: cateOnes) {
				cateOneIds.add(g.getCateId());
			}
			
			Set<Integer> validsCateIds = Sets.newHashSet();	// 分类券所有的二级分类
			for (Integer c: cateIds) {
				if (cateOneIds.contains(c)) {
					// 一级分类获取二级分类
					List<GoodsCateDto> cateTwos = goodsSearchService.getSubCateGoodsList(cityId, storeId, c);
					for (GoodsCateDto g: cateTwos) {
						validsCateIds.add(g.getCateId());
					}
				}
				else {
					// 二级分类
					validsCateIds.add(c);
				}
			}
			
			// 遍历购物车所有商品，与分类券的二级分类做比较
			for (PackageRespDto p: cart.getPackageList()) {
				for (CartGoodsRespDto g: p.getGoodsList()) {
					if (!validsCateIds.contains(g.getNewCateIdTwo())) {
						return false;
					}
				}
			}
			return true;
		}

		return false;
	}

	@Override
	@Transactional
	public SubmitOrderRespDto submitOrder(SubmitOrderReqDto req) {
		// 校验参数
		if (req == null || req.getUserId() <= 0
				|| StringUtils.isEmpty(req.getOpenId())
				|| req.getStoreId() <= 0 || req.getCityId() <= 0
				|| req.getAddressId() <= 0) {
			return new SubmitOrderRespDto(400, "参数有误");
		}
		// os
		int os = 21; // Android
		if (OS.ANDROID.equals(req.getOs())) {
			os = 21;
		} else if (OS.IOS.equals(req.getOs())) {
			os = 30;
		} else {
			return new SubmitOrderRespDto(400, "目前只支持Android 和iOS 系统");
		}
				
		// 地址
		Address address = addressDao.findOne(req.getAddressId());
		if (!Validate(address)) {
			return new SubmitOrderRespDto(400, "地址有误");
		}

		// 购物车
		CartRespDto cartRespDto = cartService.getCart(req.getUserId(),
				req.getOpenId(), req.getCityId(), req.getStoreId());
		int packageNum = cartRespDto.getPackageList().size();
		if (packageNum == 0) {
			return new SubmitOrderRespDto(400, "购物车是空的");
		}
		// 校验库存
		for (PackageRespDto p : cartRespDto.getPackageList()) {
			for (CartGoodsRespDto g : p.getGoodsList()) {
				if (g.getAmount() > g.getStock()) {
					return new SubmitOrderRespDto(400, "部分商品库存不足");
				}
				if (g.getOverdue() == 1) {
					return new SubmitOrderRespDto(400, "部分预售商品预售时间已过");
				}
			}
		}
		// 校验配送时间
		List<String> materialShippingTime = req.getShippingTimes()
				.getMaterial();
		List<SpecShippingTime> bookingShippingTime = req.getShippingTimes()
				.getBooking();
		Map<Integer, String> bookingShippingTimeMap = Maps.newHashMap();
		for (SpecShippingTime st : bookingShippingTime) {
			bookingShippingTimeMap.put(st.getSpecId(), st.getTime());
		}
		for (PackageRespDto p : cartRespDto.getPackageList()) {
			String needShippingTime = null;
			if (PackageType.MATERIAL.equals(p.getPackageType())) {
				if (materialShippingTime == null
						|| materialShippingTime.size() == 0) {
					return new SubmitOrderRespDto(400, "商品配送时间有误");
				}
				needShippingTime = materialShippingTime.get(0);
			} else if (PackageType.BOOKING.equals(p.getPackageType())) {
				int specId = p.getGoodsList().get(0).getSpecId();
				needShippingTime = bookingShippingTimeMap.get(specId);
				if (needShippingTime == null) {
					return new SubmitOrderRespDto(400, "送货日期错误，请重新选择!");
				}
			}
		}

		// 通过会员接口，获取会员信息
		UserRespDto user = userService.getByUserId(req.getUserId());
		if (user == null) {
			return new SubmitOrderRespDto(400, "用户不存在");
		}

		Site site = siteDao.findOne(req.getCityId());

		// 优惠券, 目前只有全场券和品类券
		double needPay = DoubleUtils.add(cartRespDto.getTotalPrice(),
				cartRespDto.getShippingFeeTotal()); // 还需付多少钱
		int couponId = req.getCouponId();
		CoupList coupList = null;
		if (couponId > 0) {
			coupList = coupListDao.getValidCoupList(req.getUserId(), couponId);
			if (coupList == null) {
				return new SubmitOrderRespDto(400, "优惠券不可用");
			}

			// 校验优惠券是否可用
			if (coupList.getMinprice() > cartRespDto.getTotalPrice()) {
				return new SubmitOrderRespDto(400, String.format(
						"使用优惠券最小金额为%.2f. 优惠券不可用", coupList.getMinprice()));
			}
			// 更新优惠券状态为已使用
			boolean used = coupListService.useCoupon(req.getUserId(), couponId);
			if (!used) {
				// 使用优惠券失败
				String msg = String.format(
						"use coupon FAILED, userId=%d, couponId=%d",
						req.getUserId(), couponId);
				LOGGER.error(msg);
				throw new RuntimeException(msg);
			}
			needPay = DoubleUtils.mul(needPay, coupList.getMoney(), 2);
		}

		// 新建订单
		final String orderSnMain = generateOrderSnMain();
		double usedDsicount = 0.0; // 计算过的折扣
		for (int i = 0; i < packageNum; i++) {

			PackageRespDto pl = cartRespDto.getPackageList().get(i);
			double goodsAmount = 0.0; // 该包裹商品总额
			for (CartGoodsRespDto g : pl.getGoodsList()) {
				goodsAmount = DoubleUtils.add(goodsAmount,
						DoubleUtils.mul(g.getPrice(), g.getAmount(), 2));
			}

			// 计算优惠券，每个包裹的折扣是按照包裹金额比例来分配
			double discount = 0.0; // 折扣
			if (coupList != null) {
				double totalDiscount = coupList.getMoney();
				if (i == cartRespDto.getPackageList().size() - 1) {
					// 最后一个包裹的折扣
					discount = DoubleUtils.sub(totalDiscount, usedDsicount);
				} else {
					// 该包裹折扣 = 该包裹商品总额 * 折扣总金额/所有商品总额
					discount = DoubleUtils.div(
							DoubleUtils.mul(goodsAmount, totalDiscount),
							cartRespDto.getTotalPrice(), 1);
				}
				// 如果折扣金额大于总额，折扣=总额
				if (discount > goodsAmount) {
					discount = goodsAmount;
				}
				usedDsicount = DoubleUtils.add(usedDsicount, discount);
			}

			Order order = new Order();
			order.setOrderSnMain(orderSnMain);
			String taoOrderSn = orderSnMain;
			if (packageNum > 1) {
				taoOrderSn = String.format("%s-%d-%d", orderSnMain, packageNum, i + 1);
			}
			order.setOrderSn(generateOrderSn());
			order.setTaoOrderSn(taoOrderSn);
			if (PackageType.MATERIAL.equals(pl.getPackageType())) {
				order.setShippingFee(cartRespDto.getShippingFeeTotal());
			} else {
				order.setShippingFee(0);
			}
			String needShippingTime = null;
			if (PackageType.MATERIAL.equals(pl.getPackageType())) {
				needShippingTime = materialShippingTime.get(0);
			} else if (PackageType.BOOKING.equals(pl.getPackageType())) {
				int specId = pl.getGoodsList().get(0).getSpecId();
				needShippingTime = bookingShippingTimeMap.get(specId);
			}
			if (needShippingTime != null) {
				String[] strs = needShippingTime.split(" ");
				order.setNeedShiptime(DateUtils.str2Date(strs[0].trim()));
				order.setNeedShiptimeSlot(strs[1].trim());
			}

			int storeId = req.getStoreId();
			StoreRespDto store = null;
			GoodsRespDto goods = null;
			GoodsSpecRespDto spec = null;
			if (PackageType.SELF_SALES.equals(pl.getPackageType())) {
				int specId = pl.getGoodsList().get(0).getSpecId();
				spec = goodsSpecService.get(specId);
				goods = goodsService.getGoods(spec.getGoodsId());
				storeId = goods.getStoreId();
			}
			store = storeService.getByStoreId(storeId);
			order.setSellerId(storeId);
			order.setSellerName(store.getStoreName());
			order.setType(pl.getPackageType());
			if (PackageType.MATERIAL.equals(order.getType())) {
				// 如果是material, 类型跟店铺类型走有个两种 wei_wh, wei_self
				order.setType(store.getStoreType());
			}
			order.setBuyerId(req.getUserId());
			order.setBuyerName(user.getName());
			order.setPayType(OrderPayType.PAY_ONLINE);
			order.setAddTime(DateUtils.getTime());
			order.setGoodsAmount(goodsAmount);
			order.setDiscount(discount);
			order.setOrderAmount(DoubleUtils.sub(goodsAmount, discount));
			if (coupList != null) {
				order.setUseCouponNo(coupList.getCommoncode());
				order.setUseCouponValue(coupList.getMoney());
			}
			if (!StringUtils.isEmpty(req.getInvoiceHeader())) {
				order.setNeedInvoice(1);
				order.setInvoiceHeader(req.getInvoiceHeader());
			} else {
				order.setNeedInvoice(0);
			}
			order.setInvoiceType(req.getInvoiceType());
			order.setPostscript(req.getPostScript());
			if (PackageType.BOOKING.equals(pl.getPackageType())) {
				order.setShippingId(0); // 小黄蜂配送
			} else {
				order.setShippingId(1); // 商家自送
			}
			if (PackageType.MATERIAL.equals(pl.getPackageType())) {
				order.setShippingFee(cartRespDto.getShippingFeeTotal());
			} else {
				order.setShippingFee(0);
			}
			order.setSource(os);
			order.setSellSite(site.getShortCode());
			// 如果有用优惠券，设置已支付金额
			if (discount > 0) {
				order.setPayTime(DateUtils.getTime());
				order.setOrderPayed(discount);
			}
			// 如果优惠券全部抵订单，则修改订单状态，自动审核
			order.setStatus(OrderStatusEnum.STATUS_NEW.getId());
			if (order.getOrderAmount() <= 0 && order.getShippingFee() <= 0) {
				order.setStatus(OrderStatusEnum.STATUS_REVIEWED.getId());
			}

			Order newOrder = orderDao.save(order);

			// 新建tcz_order_goods
			List<OrderGoods> orderGoodsList = Lists.newArrayList();
			double priceDiscount = 0.0; // 商品折扣后的价格
			for (CartGoodsRespDto g : pl.getGoodsList()) {
				// 为了兼容老系统，如果是打包商品(整箱购) 订单商品要转成标准规格的商品
				GoodsStockInfoRespDto info = goodsSpecService.getGoodsStockInfo(g.getGoodsId(), g.getSpecId());
				if (info.getStockSpecId() != g.getSpecId()) {
					// 整箱购商品，用标准规格的商品来记录
					int stockBase = info.getStockBase();
					
					GoodsSpecRespDto goodsSpec = goodsSpecService.get(info.getStockSpecId());
					g.setSpecId(goodsSpec.getSpecId());
					g.setSpecName(goodsSpec.getSpecOne());
					g.setPrice(DoubleUtils.div(g.getPrice(), stockBase, 8));
					g.setAmount(g.getAmount() * stockBase);
					g.setTaosku(goodsSpec.getTaosku());
					g.setBn(goodsSpec.getBn());
				}
				
				
				priceDiscount = g.getPrice();
				if (discount > 0) {
					// 单个商品折扣, priceDiscount = price -
					// (price/goodsAmount)*discount
					priceDiscount = DoubleUtils.sub(g.getPrice(), DoubleUtils
							.mul(DoubleUtils.div(g.getPrice(), goodsAmount),
									discount, 8));
				}

				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setOrderId(newOrder.getOrderId());
				orderGoods.setGoodsId(g.getGoodsId());
				orderGoods.setSpecId(g.getSpecId());
				orderGoods.setGoodsName(g.getGoodsName());
				orderGoods.setSpecification(g.getSpecName());
				orderGoods.setStoreId(storeId);
				orderGoods.setPrice(g.getPrice()); // TODO 现在不需要佣金了把？
				orderGoods.setPricePurchase(g.getPrice());
				orderGoods.setPriceDiscount(priceDiscount); // 计算折扣价格
				orderGoods.setQuantity(g.getAmount());
				orderGoods.setPoints(0); // TODO 计算积分，目前积分不要了
				orderGoods.setProType(g.getFlag());
				orderGoods.setPackageId(0); // TODO 组合购买的组合ID 或套餐ID, 不知道怎么算
				orderGoods.setCommission(g.getCommission());
				orderGoods.setGoodsImage(g.getGoodsImage());
				orderGoods.setTaosku(g.getTaosku());
				orderGoods.setBn(g.getBn());

				orderGoodsList.add(orderGoods);

			}
			orderGoodsDao.save(orderGoodsList);

			// 新增优惠券支付记录
			if (discount > 0) {
				OrderPay orderPay = new OrderPay();
				orderPay.setOrderId(newOrder.getOrderId());
				orderPay.setOrderSnMain(newOrder.getOrderSnMain());
				orderPay.setPaymentId(14); // 优惠券
				orderPay.setMoney(discount);
				orderPay.setPayTime(DateUtils.getTime());
				orderPay.setStatus("succ");

				orderPayDao.save(orderPay);
			}
		}

		// 统一更新 更新已售商品 冻结商品库存
		goodsService.freeze(req.getUserId(), req.getOpenId(), req.getCityId(),
				req.getStoreId());

		// 添加收货地址信息
		OrderExtm orderExtm = new OrderExtm();
		BeanUtils.copyProperties(address, orderExtm, "id");
		orderExtm.setOrderSnMain(orderSnMain);
		orderExtmDao.save(orderExtm);

		String lnglat = String.format("lat:%s,lng:%s", address.getLatitude(),
				address.getLongitude());
		OrderLnglat orderLnglat = new OrderLnglat();
		orderLnglat.setOrderSnMain(orderSnMain);
		orderLnglat.setLnglat(lnglat);
		orderLnglatDao.save(orderLnglat);

		// 添加tcz_order_action
		OrderAction orderAction = new OrderAction();
		orderAction.setAction(0); // 下单
		orderAction.setOrderSnMain(orderSnMain);
		orderAction.setActor(user.getName());
		orderAction.setActionTime(new Date());
		orderAction.setNotes("下单");
		orderActionDao.save(orderAction);

		try {
			// 清空购物车
			cartService
					.clear(req.getUserId(), req.getOpenId(), req.getCityId());
		} catch (Exception e) {
			LOGGER.warn("Clear cart FAILED!", e);
		}
		SubmitOrderRespDto dto = new SubmitOrderRespDto(200, "");
		SubmitOrderResultDto result = dto.getResult();
		result.setOrderSnMain(orderSnMain);
		result.setNeedPay(needPay);
		return dto;
	}

	private boolean Validate(Address address) {
		if (address == null) {
			return false;
		}
		if (StringUtils.isEmpty(address.getConsignee())
				|| address.getRegionId() <= 0
				|| StringUtils.isEmpty(address.getRegionName())
				|| StringUtils.isEmpty(address.getAddress())
				|| StringUtils.isEmpty(address.getPhoneMob())) {
			return false;
		}

		return true;
	}

	private List<Integer> getOutId(CoupRule coupRule) {
		List<Integer> ids = Lists.newArrayList();
		String[] strs = coupRule.getOutId().split(",");
		if (strs.length > 0) {
			ids.add(Integer.valueOf(strs[0]));
		}
		return ids;
	}

	/**
	 * 生成主订单号 orderSnMain，'年月日时分+5位随机数'
	 * 
	 * @return
	 */
	private String generateOrderSnMain() {
		int min = 10000;
		int max = 99999;
		String date = DateUtils.date2DateStr3(new Date());
		Random random = new Random();
		int r = random.nextInt(max) % (max - min + 1) + min;
		String orderSnMain = date + r;
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		// 如果orderSnMain 已经存在，递归
		if (orders.size() > 0) {
			return generateOrderSnMain();
		}

		return orderSnMain;
	}

	/**
	 * FIXME 生成ordersn
	 * 
	 * @return
	 */
	private String generateOrderSn() {
		// global $G_SHOP;
		// /* 选择一个随机的方案 */
		// mt_srand((double) microtime() * 1000000);
		// $timestamp = gmtime();
		// $y = date('y', $timestamp);
		// $z = date('z', $timestamp);
		// $order_sn = $y . str_pad($z, 3, '0', STR_PAD_LEFT) .
		// str_pad(mt_rand(1, 99999), 5, '0', STR_PAD_LEFT);
		//
		// $strSQL="SELECT order_sn FROM tcz_order WHERE order_sn='".$order_sn."'";
		// $orders=$G_SHOP->DBCA->getOne($strSQL);
		//
		// if (empty($orders))
		// {
		// /* 否则就使用这个订单号 */
		// return $order_sn;
		// }
		//
		// /* 如果有重复的，则重新生成 */
		// return $this->_gen_order_sn();
		return "";
	}

	@Override
	public PayOrderResultRespDto getPayOrderMsg(int userId, String orderSnMain) {
		PayOrderResultRespDto resp = new PayOrderResultRespDto(200, "");
		PayOrderMsgDto result = new PayOrderMsgDto();
		if (userId <= 0 || StringUtils.isEmpty(orderSnMain)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		if (CollectionUtils.isEmpty(orders)) {
			return resp;
		}

		double orderTotal = 0;
		double shippingFee = 0;
		for (Order o : orders) {
			orderTotal = DoubleUtils.add(orderTotal, o.getGoodsAmount());
			orderTotal = DoubleUtils.add(orderTotal, o.getShippingFee());
			shippingFee = DoubleUtils.add(shippingFee, o.getShippingFee());
		}

		Double payedMoney = orderPayDao
				.getPayedAmountByOrderSnMain(orderSnMain);
		if (payedMoney == null) {
			payedMoney = 0.0;
		}
		double txkValue = AccountTxkProcessor.getProcessor()
				.getTxkBalanceByUserId(userId);
		double vCountValue = VirtualAccountProcessor.getProcessor()
				.getVirtualBalanceByUserId(userId);

		result.setDiscountAmount(payedMoney);
		result.setOrderSnMain(orderSnMain);
		result.setOrderTotal(DoubleUtils.sub(orderTotal, shippingFee));
		result.setShippingFee(shippingFee);
		result.setTotal(DoubleUtils.sub(orderTotal, payedMoney));
		result.setTxkNum(txkValue);
		result.setVcount(vCountValue);
		PayOrderMsgRespDto payOrderMsgRespDto = new PayOrderMsgRespDto();
		payOrderMsgRespDto.setOrderMsg(result);
		resp.setResult(payOrderMsgRespDto);

		return resp;

	}
	
	private String trimall(String str)// 删除空格
	{
		String[] searchList = { " ", "  ", "\t", "\n", "\r" };
		String[] replacementList = { "", "", "", "", "" };

		return StringUtils.replaceEach(str, searchList, replacementList);
	}

	// 物流信息
	// 传参数 列表详情调用
	// 不传参数 接口调用
	@Override
	public ShippingMsgRespDto getShippingResult(String taoOrderSn) {
		ShippingMsgRespDto resp = new ShippingMsgRespDto(200, "");
		ShippingListResultDto resultDto = new ShippingListResultDto();
		if (StringUtils.isEmpty(taoOrderSn)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
		Express express = expressDao.findByCodeNum(order.getShippingCompany());
		StringBuilder sb = new StringBuilder();
		if (order.getShippingId() == 0 || order.getShippingId() > 5) {
			sb.append("淘常州小黄蜂配送");
		} else {
			sb.append("商家自送");
		}
		if ( express != null && StringUtils.isNotBlank(express.getExpressName())) {
			sb.append("(").append(express.getExpressName()).append(")");
//			resultDto.setShippingName(sb.toString());
		}

		resultDto.setShippingName(sb.toString());
		
		if (order.getPayType() == OrderPayTypeEnum.TYPE_ARRIVAL.getId()) {
			resultDto.setPayType("货到付款");
		} else if (order.getPayType() == OrderPayTypeEnum.TYPE_ONLINE.getId()) {
			resultDto.setPayType("在线支付");
		}
		List<ShippingListDto> shippingList = new ArrayList<ShippingListDto>();
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
		
		resultDto.setShippingList(shippingList);
		resp.setResult(resultDto);
		resp.setCode(200);
		resp.setInnerCode(0);//内部调用需要 FIXME
		return resp;
	}


	/**
	 * 返回退款状态 flag 1:全部 2:待付款 3:待收货 4:退货
	 */
//	private String getRefundStatus(List<OrderReturn> orderReturns, int flag) {
//		StringBuilder result = new StringBuilder("");
//		if(flag == RefundStatusEnum.STATUS_RETURNED.getFlag()) {
//			for (OrderReturn orderReturn : orderReturns) {
//				if (orderReturn.getRefundStatus() == 0) {
//					result.append(orderReturn.getOrderId()).append(" ")
//							.append("未退款 ");
//				} else {
//					result.append(orderReturn.getOrderId()).append(" ")
//							.append("已退款 ");
//				}
//			}
//		}
//		return result.toString();
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


	/**
	 * 判断是否显示 确认收货
	 *
	 * 返回 1 显示 ==商家已发货 且 没有回单(商家自己配送的) 0 不显示 2 显示等待收货==审单通过且商家未发货
	 *
	 * status : ok
	 */
	private int getReciveStatus(Order order) {
//		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
		if (null == order) {
			return 0;
		}

		if (order.getStatus() >= OrderStatusEnum.STATUS_REVIEWED.getId() && 
				order.getStatus() <= OrderStatusEnum.STATUS_DELIVERIED.getId()) {// 审核通过 未发货
			return 2;
		} else if (order.getStatus() >= OrderStatusEnum.STATUS_14.getId() 
				&& order.getStatus() < OrderStatusEnum.STATUS_FINISHED.getId()
				&& order.getShippingId() == 1) {
			// 商家已发货 且 没有回单(商家自己配送的)
			return 1;
		}
		return 0;
	}

	/**
	 * 用户app接口
	 */
	@Override
	@Transactional
	public OrderCancelRespDto cancelOrder(int userId, String orderSnMain) {
		OrderCancelRespDto resp = new OrderCancelRespDto(200, "");
		if (userId <= 0 || StringUtils.isBlank(orderSnMain)) {
			return new OrderCancelRespDto(400, "非法请求");
		}
		List<Order> orders = orderDao.findByOrderSnMain(
				orderSnMain);
		if (CollectionUtils.isEmpty(orders)) {
			return new OrderCancelRespDto(400, "订单号无效");
		}

		double returnAmountVcount = 0;
		double returnAmountTxk = 0;
		double returnAmountCoupon = 0;
		double returnAmount = 0;
		double orderPayed = 0;
		
		boolean isAllPayed = true;
		for (Order order : orders) {
			if (order.getStatus() == OrderStatusEnum.STATUS_CANCELED.getId()
					|| order.getStatus() == OrderStatusEnum.STATUS_REVIEWED
							.getId()) {
				return new OrderCancelRespDto(400, "订单不可取消");
			}

			isAllPayed &= (order.getPayStatus() == PayStatusEnum.STATUS_PAYED.getId());
			if (order.getOrderPayed() > 0) {
				orderPayed = DoubleUtils.add(orderPayed, order.getOrderPayed());
				
			}
		}
		if (isAllPayed) {
			return new OrderCancelRespDto(400, "已付款订单不可取消,请联系客服");
		}

		if (orderPayed > 0) {
			for (Order order : orders) {
				Map<Integer, Double> paymentIdMoneyMap = getPay(order
						.getOrderId());
				
				String useCouponNo = order.getUseCouponNo();
				
				for (Map.Entry<Integer, Double> entry : paymentIdMoneyMap
						.entrySet()) {
					if (entry.getKey() == PaymentEnum.PAY_VACOUNT.getId()) { // 虚拟账户
						returnAmountVcount = DoubleUtils.add(returnAmountVcount, entry.getValue());
					} else if (entry.getKey() == PaymentEnum.PAY_TXK.getId()) {
						returnAmountTxk = DoubleUtils.add(returnAmountTxk, entry.getValue());
					} else if (entry.getKey() == PaymentEnum.PAY_YHQ.getId()) {
						returnAmountCoupon = DoubleUtils.add(returnAmountCoupon, entry.getValue());
					} else {
						returnAmount = DoubleUtils.add(returnAmount, entry.getValue());
					}
				}
				
				double returnSum = DoubleUtils.add(returnAmountVcount, returnAmountTxk);
				returnSum = DoubleUtils.add(returnSum, returnAmountCoupon);
				returnSum = DoubleUtils.add(returnSum, returnAmount);

				if (returnAmount > 0) {
					// 有采用在线支付，则生成未退款单及应退明细记录
					int orderIdR = getOrderReturnAdd(orderSnMain,
							order.getOrderId(), order.getBuyerId(),
							order.getSellerId(), returnSum, 0, 0);
					for (Map.Entry<Integer, Double> entry : paymentIdMoneyMap
							.entrySet()) {
						OrderPayR orderPayR = new OrderPayR();
						orderPayR.setOrderIdR(orderIdR);
						orderPayR.setRepayWay(0);
						orderPayR.setPaymentId(entry.getKey());
						orderPayR.setValue(entry.getValue());
						orderPayRDao.save(orderPayR);
					}

				} else {
					// 否则安照原先支付记录自动退款，并生成已退款单
					int orderIdR = getOrderReturnAdd(orderSnMain,
							order.getOrderId(), order.getBuyerId(),
							order.getSellerId(), returnSum, 1, 1);
					if (returnAmountVcount > 0) {// 虚拟帐户原额退
						OrderPayR orderPayR = new OrderPayR();
						orderPayR.setOrderIdR(orderIdR);
						orderPayR.setRepayWay(0);
						orderPayR.setPaymentId(2);
						orderPayR.setValue(returnAmountVcount);
						orderPayRDao.save(orderPayR);
						String addTime = DateUtils.date2DateStr2(new Date());
						String noteStr = String.format("取消订单退款:%s", addTime);
						VaccountUpdateRespVO vAccountResp = VirtualAccountProcessor
								.getProcessor().refund(userId,
										order.getOrderId(), orderSnMain,
										returnAmountVcount,
										addTime, 0,
										noteStr,
										order.getBuyerName());

					}
					if (returnAmountTxk > 0) {// 淘心卡原额退
						OrderPayR orderPayR = new OrderPayR();
						orderPayR.setOrderIdR(orderIdR);
						orderPayR.setPaymentId(PaymentEnum.PAY_TXK.getId());
						orderPayR.setRepayWay(0);
						orderPayR.setValue(returnAmountTxk);
						orderPayRDao.save(orderPayR);
						// TODO test
						TxkCardRefundRespVO txkCardResp = AccountTxkProcessor
								.getProcessor().refund(returnAmountTxk,
										orderSnMain, userId,
										order.getBuyerName());
					}

					if (returnAmountCoupon > 0) {// 优惠券状态改成未使用
						
						if(useCouponNo != null && !StringUtils.equals(useCouponNo, "0")) {
							int couponId = coupListDao.refundCouponList(useCouponNo, userId);
							if(couponId <= 0) {
								resp.setCode(400);
								resp.setMessage("返回优惠券失败");
								return resp;
							}
						}
					}
				}
				
				releaseAndLog(order, resp);
				
			} // end of for loop

		} else {
			// 全部未支付，只需改订单状态及操作记录即可
			for (Order order : orders) {
				releaseAndLog(order, resp);
			}
		}
		return resp;
	}
	
	
	private void releaseAndLog(Order order, OrderCancelRespDto resp) {
		// 修改订单status为1
		orderDao.updateOrderStatus(order.getOrderId(),
				OrderStatusEnum.STATUS_CANCELED.getId());
		// 释放库存
		releaseFreezStock(order.getOrderId(), order.getType(),
				OpearteTypeEnum.OPERATE_CANCEL.getType());

		// order_action 只插入一条记录
		OrderAction orderAction = new OrderAction();
		orderAction.setAction(1);
		orderAction.setOrderSnMain(order.getOrderSnMain());
		orderAction.setTaoOrderSn(order.getTaoOrderSn());
		orderAction.setOrderId(order.getOrderId());
		orderAction.setActor(order.getBuyerName());
		orderAction.setActionTime(new Date());
		orderAction.setNotes("取消");
		orderActionDao.save(orderAction);

		if (order.getPayStatus() == PayStatusEnum.STATUS_UNPAY.getId()) {// 0未支付 1已支付
			resp.setCode(200);
			resp.setResult("订单取消成功");
			// return resp;
		} else {
			resp.setCode(200);
			resp.setResult("订单取消成功,您已支付的金额将在三个工作日内返还到您的账户中！");
			// return resp;
		}
	}

	/**
	 * 释放锁定库存
	 *
	 * @param order_id
	 *            订单ID,必填
	 * @param order_type
	 *            订单类型,wei_wh(微仓订单(总仓进货)),wei_self(微仓订单(独立进货)),booking(预购订单),
	 *            self_sales(商家独立销售)
	 * @param operate_type
	 *            操作类型,1取消,2作废,6核验/发货
	 */
	private boolean releaseFreezStock(int orderId, String orderType,
			int operateType) {
		if (orderId < 1 || StringUtils.equals(orderType, "") || 
				operateType < OpearteTypeEnum.OPERATE_CANCEL.getType()) {
			return false;
		}
		Order order = orderDao.findByOrderId(orderId);
		List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(orderId);
		if (order.getSellerId() > 0 && !CollectionUtils.isEmpty(orderGoodsList)) {
			for (OrderGoods orderGoods : orderGoodsList) {
				if (StringUtils.equals(orderType, OrderTypeEnums.TYPE_WEI_WH.getType())
						|| StringUtils.equals(orderType, OrderTypeEnums.TYPE_WEI_SELF.getType())) {
					// 微仓订单
					// LkWhGoodsStore whStore = lkWhGoodsStoreDao.
					// findBySpecIdAndStoreId(orderGoods.getSpecId(),
					// order.getSellerId());
//					String updateTime = DateUtils.date2DateStr2(new Date());
					if (operateType == OpearteTypeEnum.OPEARTE_CHECK_DELIVER.getType()) {// 发货
						
						lkWhGoodsStoreDao.updateBySpecIdAndStoreIdAndUpdateTime(
								orderGoods.getSpecId(), order.getSellerId(),
								new Date(),
								orderGoods.getQuantity(),
								orderGoods.getQuantity());
					} else {
						
						lkWhGoodsStoreDao.updateBySpecIdAndStoreIdAndUpdateTime(
								orderGoods.getSpecId(), order.getSellerId(),
								orderGoods.getQuantity(), new Date());
					}
				} else {
					// GoodsSpec goodsSpec =
					// goodsSpecDao.findBySpecId(orderGoods.getSpecId());
					if (operateType == OpearteTypeEnum.OPEARTE_CHECK_DELIVER.getType()) {// 发货
						goodsSpecDao.updateBySpecId(orderGoods.getSpecId(),
								orderGoods.getQuantity(),
								orderGoods.getQuantity());
					} else { // 取消
						goodsSpecDao.updateBySpecId(orderGoods.getSpecId(), orderGoods.getQuantity());
					}
				}
			}
			return true;
		}
		return false;
	}

	// 退款
	private int getOrderReturnAdd(String orderSnMain, int orderId, int buyerId,
			int sellerId, double returnSum, int refundStatus, int returnStatus) {
		OrderReturn orderReturn = new OrderReturn();
		orderReturn.setOrderSnMain(orderSnMain);
		orderReturn.setOrderId(orderId);
		orderReturn.setBuyerId(buyerId);
		orderReturn.setSellerId(sellerId);
		orderReturn.setReturnAmount(returnSum);
		orderReturn.setActor("system");
		String addTime = DateUtils.date2DateStr2(new Date());
		orderReturn.setAddTime(addTime);
		orderReturn.setGoodsType(ReturnGoodsType.GOODS);
		orderReturn.setOrderType(OrderReturnGoodsType.TYPE_SELF_CANCEL.getId());
		orderReturn.setOrderStatus(ReturnOrderStatus.NORMAL.getId());
		orderReturn.setGoodsStatus(ReturnGoodsStatus.ASSIGN.getId());
		orderReturn.setRefundStatus(refundStatus);
		orderReturn.setStatementStatus(0);
		if (returnStatus == 1) {
			orderReturn.setRepayTime(addTime);
		}
		orderReturn.setPostscript("客户自已取消订单");
		OrderReturn or = orderRDao.save(orderReturn);
		if(or != null) {
			return or.getOrderIdR();
		} else {
			return 0;
		}
	}

	/*
	 * 订单支付明细 orderId 订单ID
	 */
	private Map<Integer, Double> getPay(int orderId) {
		List<OrderPay> orderPays = orderPayDao.findByOrderIdAndStatus(orderId,
				"succ");
		Map<Integer, Double> paymentIdMoney = new HashMap<Integer, Double>();
		if (!CollectionUtils.isEmpty(orderPays)) {
			// 相同支付方式的支付金额合并
			for (OrderPay orderPay : orderPays) {
				// if(orderPay.getPaymentId() != 14) {
				if (paymentIdMoney.containsKey(orderPay.getPaymentId())) {
					paymentIdMoney.put(orderPay.getPaymentId(),
							paymentIdMoney.get(orderPay.getPaymentId())
									+ orderPay.getMoney());
				} else {
					paymentIdMoney.put(orderPay.getPaymentId(),
							orderPay.getMoney());
				}
				// }
				// else {//需退优惠券 判断是否用优惠券支付过
				//
				// }
			}
		}

		return paymentIdMoney;
	}

	@Override
	public ShareRespDto shareAfterPay(String orderSnMain) {
		ShareRespDto resp = new ShareRespDto(200, "");
		if (StringUtils.isBlank(orderSnMain)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		StringBuilder md5time = new StringBuilder();
		md5time.append("share").append(orderSnMain)
				.append(new Date().getTime() / 1000).append("friend");

		StringBuilder shareUrl = new StringBuilder();
		shareUrl.append(
				"http://wap.loukou.com/weixin.wxact-sharefriend.html?time=")
				.append(new Date().getTime() / 1000)
				.append("&order_id=")
				.append(orderSnMain)
				.append("&scode=")
				.append(DigestUtils.md5DigestAsHex(md5time.toString()
						.getBytes()));
		ShareDto shareDto = new ShareDto();
		shareDto.setContent("楼口全场代金券来啊，速抢");
		shareDto.setIcon("");
		shareDto.setUrl(shareUrl.toString());

		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		int count = orders.size();

		ShareResultDto resultDto = new ShareResultDto();
		if (count > 1) {
			resultDto.setArrivalCode("");
			resultDto.setDesc("到我的订单内查看收货码进行收货哦~");
		} else if (count == 1) {
			resultDto.setArrivalCode(orders.get(0).getShippingNo());
			resultDto.setDesc("凭借收货码进行收货哦~");
		}

		resultDto.setImage("http://pic1.taocz.cn//201505061136566745.jpg");
		resultDto.setShare(shareDto);

		resp.setResult(resultDto);
		return resp;
	}


	@Override
	public PayBeforeRespDto getPayInfoBeforeOrder(int userId, String openId,
			int cityId, int storeId, int couponId) {

		double couponMoney = 0.0;
		if (couponId > 0) {
			CoupList coupList = coupListDao.getValidCoupList(userId, couponId);
			if (coupList == null) {
				return new PayBeforeRespDto(400, "无效的优惠券");
			}
			couponMoney = coupList.getMoney();
		}

		// 购物车
		CartRespDto cart = cartService.getCart(userId, openId, cityId, storeId);
		double orderTotal = cart.getTotalPrice();
		double shippingFee = cart.getShippingFeeTotal();
		if (cart.getPackageList().size() == 0) {
			return new PayBeforeRespDto(400, "购物车没有商品");
		}

		double total = 0.0; // 订单总价（订单金额+运费-优惠）
		double discountAmount = 0.0; // 折扣金额
		if (couponMoney > orderTotal) {
			total = shippingFee;
			discountAmount = orderTotal;
		} else {
			total = DoubleUtils.sub(DoubleUtils.add(orderTotal, shippingFee),
					couponMoney);
			discountAmount = couponMoney;
		}

		// 获取淘心卡
		double txkNum = accountTxkProcessor.getTxkBalanceByUserId(userId); 
		// 获取虚账户
		double vcount = virtualAccountProcessor
				.getVirtualBalanceByUserId(userId);

		PayBeforeRespDto resp = new PayBeforeRespDto(200, "");
		PayOrderMsgDto orderMsgDto = resp.getResult().getOrderMsg();
		orderMsgDto.setDiscountAmount(discountAmount);
		orderMsgDto.setOrderTotal(orderTotal);
		orderMsgDto.setShippingFee(shippingFee);
		orderMsgDto.setTotal(total);
		orderMsgDto.setTxkNum(txkNum);
		orderMsgDto.setVcount(vcount);

		return resp;
	}
	
	/**
	 * 订单详情
	 */
    @Override
    public OResponseDto<OrderInfoDto> getOrderGoodsInfo(String orderNo) {
       return OrderInfoService.getOrderGoodsInfo(orderNo);
    }

    /**
     * 订单列表
     */
    @Override
    public OResponseDto<OrderListInfoDto> getOrderListInfo(OrderListParamDto param) {
        return OrderInfoService.getOrderListInfo(param);
    }

    /**
     * 打包完成
     */
    @Override
    public OResponseDto<String> finishPackagingOrder(String taoOrderSn,String userName,int senderId) {
        return orderOperationProcessor.finishPackagingOrder(taoOrderSn, userName, senderId);
    }
    /**
     * 拒单
     */
    @Override
    public OResponseDto<String> refuseOrder(String taoOrderSn,String userName,int refuseId,String refuseReason) {
        return orderOperationProcessor.refuseOrder(taoOrderSn, userName, refuseId, refuseReason);
    }
    /**
     * 回单
     */
    @Override
    public OResponseDto<String> confirmRevieveOrder(String taoOrderSn, String gps,String userName) {
         return orderOperationProcessor.confirmRevieveOrder(taoOrderSn, gps, userName);
    }
    /**
     * 预售到货
     */
    @Override
    public OResponseDto<String> confirmBookOrder(String taoOrderSn,String userName,int senderId) {
      return orderOperationProcessor.confirmBookOrder(taoOrderSn, userName,senderId);
    }

	/**
	 * 退货入库
	 */
    @Override
    @Transactional
	public ReturnStorageRespDto returnStorage(ReturnStorageReqDto returnStorageReqDto){
		//预售商品退货时，修改订单状态，新建退款单
		//操作库存，包括库存操作流水（退货状态）
		//触发退款（退款状态）
		
		Order order = orderDao.findByTaoOrderSn(returnStorageReqDto.getTaoOrderSn());
		if(order==null){
			return new ReturnStorageRespDto(402,"订单不存在");
		}
		
		if(order.getSellerId()!=returnStorageReqDto.getStoreId()){
			return new ReturnStorageRespDto(403,"订单与微仓不一致");
		}

		List<OrderReturn> orderReturnList = getGoodsReturnList(order.getOrderId(),order.getTaoOrderSn(),returnStorageReqDto.getStoreId());
		if(orderReturnList.size()==0){
			return new ReturnStorageRespDto(404,"退货单不存在");
		}
		
		if(isGoodsReturned(orderReturnList)){
			return new ReturnStorageRespDto();
		}
		
		//修改订单退货状态
		updateOrderReturnGoodsStatus(orderReturnList,OrderReturnGoodsStatusEnum.STATUS_RETURNED);
		
		//创建操作日志
		createAction(order,OrderActionTypeEnum.TYPE_RETURN_STORAGE,"","退货入库");
		
		//生成退货库存记录，增加库存
		LKWhStockIn whStockIn = createLKWhStockIn(order);
		
		List<LKWhStockInGoods> stockInGoodsList = createLKWhStockInGoodsList(whStockIn,returnStorageReqDto);

		//增加库存
		updateGoodsStock(whStockIn,stockInGoodsList);
		
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
			stockInGoods.setSpecId(returnStorageGoods.getSpecId());
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
			int tempCount = lkWhGoodsStoreDao.updateBySpecIdAndStoreId(stockInGoods.getSpecId(),
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
		weiCangGoodsStore.setSpecId(stockInGoods.getSpecId());
		weiCangGoodsStore.setGoodsId(stockInGoods.getGoodsId());
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
	
	/**
	 * 创建异步任务
	 * @param order
	 * @param action
	 */
	private AsyncTask createAsyncTask(Order order,int actionKey,AsyncTaskActionEnum action){
		AsyncTask task = new AsyncTask();
		task.setAction(action.getId());
		task.setCreateTime(new Date());
		task.setActionKey(actionKey);
		task.setOrderId(order.getOrderId());
		task.setOrderSnMain(order.getOrderSnMain());
		task.setTaoOrderSn(order.getTaoOrderSn());
		task.setStatus(AsyncTaskStatusEnum.STATUS_NEW.getId());
		asyncTaskDao.save(task);
		
		return task;
	}

	

	@Override
	public RespDto<OrderBonusRespDto> getCurrentMonthBonusInfo(int storeId) {
		
		
		// 获取当月第一天以及下月第一天
		int currentYear;
		int currentMonth;
		int start;
		int end;
		
		Calendar cal=Calendar.getInstance();
		currentYear = cal.get(Calendar.YEAR);
		currentMonth = cal.get(Calendar.MONTH) + 1;
		
		cal.set(Calendar.DAY_OF_MONTH,1);
		start = (int) (cal.getTimeInMillis() / 1000);
		cal.add(Calendar.MONTH,1);
		end = (int) (cal.getTimeInMillis() / 1000);
		// 计算本月总体订单数
		int orderNum = orderDao.countValidOrderBetweenAddTime(storeId, start, end);
		// 计算已回订单金额和运费
		double feedback = 0;
		double feedbackDelivery = 0;
		List<Order> orderList = orderDao.findByStatusAndAddTimeBetween(15, start, end);
		for (Order order : orderList) {
			feedback = DoubleUtils.add(feedback, order.getOrderAmount());
			feedbackDelivery = DoubleUtils.add(feedbackDelivery,order.getShippingFee());
		}
		
		OrderBonusRespDto bonusDto = new OrderBonusRespDto();
		bonusDto.setOrderNum(orderNum);
		bonusDto.setFeedback(feedback);
		bonusDto.setFeedbackDelivery(feedbackDelivery);
		bonusDto.setTimeStr(String.format("%d年%d月", currentYear, currentMonth ));
		
		return new RespDto(200, "ok", bonusDto);
	}
	
	private Map<Integer,LkStatus> getLkStatusMap(){
		Iterable<LkStatus> lkStatusList = lkStatusDao.findAll();
		Map<Integer,LkStatus> statusMap=new HashMap<Integer,LkStatus>();
		for (LkStatus lkStatus : lkStatusList) {
			statusMap.put(lkStatus.getId(), lkStatus) ;
		}
		
		return statusMap;
	}
	
	public Map<String,List<LkStatusItemDto>> getLkStatusItemMap(){
		Map<String,List<LkStatusItemDto>> result = new HashMap<String,List<LkStatusItemDto>>();
		Iterable<LkStatusItem> lkStatusItemList = lkStatusItemDao.findAll();
		Map<Integer,LkStatus> statusMap = getLkStatusMap();
		
		for (LkStatusItem lkStatusItem : lkStatusItemList) {
			LkStatus lkStatus = statusMap.get(lkStatusItem.getStatusId());
			if(lkStatus == null){
				continue;
			}
			
			List<LkStatusItemDto> statusItemList = result.get(lkStatus.getStatusName());
			if(statusItemList==null){
				statusItemList = new ArrayList<LkStatusItemDto>();
			}
			
			statusItemList.add(new LkStatusItemDto(lkStatusItem.getStatusValue(),lkStatusItem.getStatusTitle()));
			result.put(lkStatus.getStatusName(), statusItemList);
		}
		
		return result;
	}
	
	public Map<String,Object> getLkConfigureMap(){
		Iterable<LkConfigure> lkConfigureList = lkConfigureDao.findAll();
		Map<String,Map<String,Object>> listConfigMap = new HashMap<String, Map<String,Object>>();
		
		Map<String,Object> resultConfig = new HashMap<String, Object>();
		
		for (LkConfigure lkConfigure : lkConfigureList) {
			Object value = getValue(lkConfigure);
			
			if(org.springframework.util.StringUtils.isEmpty(lkConfigure.getConfigureName())){
				resultConfig.put(lkConfigure.getItemName(), value);
				continue;
			}
			
			Map<String,Object> configMap = listConfigMap.get(lkConfigure.getConfigureName());
			
			if(configMap==null){
				configMap = new HashMap<String,Object>();
			}
			
			configMap.put(lkConfigure.getItemName(),value);
			
			listConfigMap.put(lkConfigure.getConfigureName(), configMap);
		}
		
		for (Entry<String, Map<String, Object>> entry : listConfigMap.entrySet()) {
			resultConfig.put(entry.getKey(), entry.getValue());
		}
		
		return resultConfig;
	}
	
	private Object getValue(LkConfigure configure){
		String value = configure.getItemValue();
		switch(configure.getItemType()){
			case "int":
				return Integer.parseInt(value);
			case "double":
				return Double.parseDouble(value);
		}
		
		return value;
	}
}
