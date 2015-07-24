package com.loukou.order.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.constants.CouponType;
import com.loukou.order.service.constants.OS;
import com.loukou.order.service.constants.OrderPayType;
import com.loukou.order.service.dao.AddressDao;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.CoupTypeDao;
import com.loukou.order.service.dao.CouponSnDao;
import com.loukou.order.service.dao.ExpressDao;
import com.loukou.order.service.dao.GoodsSpecDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderLnglatDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderPayRDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.PaymentDao;
import com.loukou.order.service.dao.SiteDao;
import com.loukou.order.service.dao.StoreDao;
import com.loukou.order.service.dao.TczcountRechargeDao;
import com.loukou.order.service.dao.WeiCangGoodsStoreDao;
import com.loukou.order.service.entity.Address;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.Express;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderLnglat;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.Site;
import com.loukou.order.service.entity.Store;
import com.loukou.order.service.enums.OrderSourceEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.enums.OrderTypeEnums;
import com.loukou.order.service.enums.RefundStatusEnum;
import com.loukou.order.service.enums.ReturnStatusEnum;
import com.loukou.order.service.req.dto.SpecShippingTime;
import com.loukou.order.service.req.dto.SubmitOrderReqDto;
import com.loukou.order.service.resp.dto.CouponListDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.CouponListResultDto;
import com.loukou.order.service.req.dto.OrderListParamDto;
import com.loukou.order.service.resp.dto.DeliveryInfo;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsInfoDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.resp.dto.OrderInfoDto;
import com.loukou.order.service.resp.dto.OrderListBaseDto;
import com.loukou.order.service.resp.dto.OrderListDto;
import com.loukou.order.service.resp.dto.OrderListInfoDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.OrderListResultDto;
import com.loukou.order.service.resp.dto.PayBeforeRespDto;
import com.loukou.order.service.resp.dto.PayOrderMsgDto;
import com.loukou.order.service.resp.dto.PayOrderResultRespDto;
import com.loukou.order.service.resp.dto.ShareDto;
import com.loukou.order.service.resp.dto.ShareRespDto;
import com.loukou.order.service.resp.dto.ShareResultDto;
import com.loukou.order.service.resp.dto.ShippingListDto;
import com.loukou.order.service.resp.dto.ShippingListResultDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.ShippingResultDto;
import com.loukou.order.service.resp.dto.SubmitOrderRespDto;
import com.loukou.order.service.resp.dto.SubmitOrderResultDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.order.service.resp.dto.SpecDto;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.pos.client.txk.processor.AccountTxkProcessor;
import com.loukou.pos.client.vaccount.processor.VirtualAccountProcessor;
import com.serverstarted.cart.service.api.CartService;
import com.serverstarted.cart.service.constants.PackageType;
import com.serverstarted.cart.service.resp.dto.CartGoodsRespDto;
import com.serverstarted.cart.service.resp.dto.CartRespDto;
import com.serverstarted.cart.service.resp.dto.PackageRespDto;
import com.serverstarted.goods.service.api.GoodsService;
import com.serverstarted.goods.service.api.GoodsSpecService;
import com.serverstarted.goods.service.resp.dto.GoodsRespDto;
import com.serverstarted.goods.service.resp.dto.GoodsSpecRespDto;
import com.serverstarted.store.service.api.StoreService;
import com.serverstarted.store.service.resp.dto.StoreRespDto;
import com.serverstarted.user.api.UserService;
import com.serverstarted.user.resp.dto.UserRespDto;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);
	
	private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("###,###.##");
	private VirtualAccountProcessor virtualAccountProcessor = VirtualAccountProcessor.getProcessor();
	private AccountTxkProcessor accountTxkProcessor = AccountTxkProcessor.getProcessor();
	
	private static final int LIMIT_COUPON_PER_DAY = 20;	// 每天限用优惠券张数
	
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
	private ExpressDao expressDao;//快递公司代码及名称
	
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
	private CouponSnDao couponSnDao;
	
	@Autowired 
	private GoodsSpecDao goodsSpecDao;
	
	@Autowired 
	private WeiCangGoodsStoreDao lkWhGoodsStoreDao;
	
	@Autowired(required=false)
	private CartService cartService;

	@Autowired 
	private MemberDao memberDao;
	
	@Autowired 
	private TczcountRechargeDao tczcountRechargeDao;
	
	@Autowired 
	private PaymentDao paymentDao;
	
	@Autowired AddressDao addressDao;
	
	@Autowired StoreService storeService;
	
	@Autowired GoodsService goodsService;
	@Autowired GoodsSpecService goodsSpecService;
	@Autowired CoupListService coupListService;
	@Autowired OrderLnglatDao orderLnglatDao;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Override
	public OrderListRespDto getOrderList(int userId, int flag) {
		OrderListRespDto resp = new OrderListRespDto();
		if(userId <= 0 || flag <= 0) {
			resp.setCode(400);
			return resp;
		}
		
		List<Order> orderList = orderDao.findByBuyerIdAndIsDel(userId, 0);
		int orderSnMainlistCount = 0;//orderSnMain的总数,用于返回值
		int payCount = 0;
		int noPayCount = 0;
		Set<String> set = new HashSet<String>();//用于计算orderSnMain的总数
		List<String> orderSnMains = new ArrayList<String>();//保存所有不同的orderSnMain
		switch(flag) {
		case 1:
			for(Order order : orderList) {
				if(order.getStatus() == 0) {
					if(!set.contains(order.getOrderSnMain())) {
						set.add(order.getOrderSnMain());
						noPayCount++;
					}
				} else {
					if(!set.contains(order.getOrderSnMain())) {
						set.add(order.getOrderSnMain());
						payCount++;
					}
				}
			}
			orderSnMains.addAll(set);
			orderSnMainlistCount = noPayCount + payCount;
			break;
		case 2:
			for(Order order : orderList) {
				if(order.getStatus() == 0) {
					if(!set.contains(order.getOrderSnMain())) {
						set.add(order.getOrderSnMain());
						noPayCount++;
					}
				} 
			}
			orderSnMains.addAll(set);
			orderSnMainlistCount = noPayCount;
			break;
		case 3:
			for(Order order : orderList) {
				if(order.getStatus() >=3 && order.getStatus() < 14) {
					if(!set.contains(order.getOrderSnMain())) {
						set.add(order.getOrderSnMain());
					}
				} 
			}
			orderSnMains.addAll(set);
			orderSnMainlistCount = ((List<Order>)orderDao.findByOrderSnMainIn(orderSnMains)).size();
			break;
		case 4:
			List<OrderReturn> orderReturns = orderRDao.findByBuyerIdAndOrderStatus(userId, 0);
			for(OrderReturn orderReturn : orderReturns) {
				set.add(orderReturn.getOrderSnMain());
			}
			orderSnMains.addAll(set);
			orderSnMainlistCount = ((List<Order>)orderDao.findByOrderSnMainIn(orderSnMains)).size();
			break;
		default:
			break;
		}
		Map<String, OrderListDto> mainOrderMap = new HashMap<String, OrderListDto>();
		for(String orderSnMain : orderSnMains) {
			getOrderAttr(mainOrderMap, orderSnMain, flag);
		}
		
		OrderListResultDto resultDto = new OrderListResultDto();
		resultDto.setOrderCount(orderSnMainlistCount);//主订单总数
		List<OrderListDto> orderListDtos = new ArrayList<OrderListDto>();//orderlist列表
		for(Map.Entry<String, OrderListDto> entry : mainOrderMap.entrySet()) {
			orderListDtos.add(entry.getValue());
		}
		resultDto.setOrderList(orderListDtos);
		resp.setCode(200);
		resp.setResult(resultDto);
		return resp;
	}

	@Override
	public CouponListRespDto getCouponList(int cityId, int userId, int storeId, String openId) {
		CouponListRespDto resp = new CouponListRespDto();
		if(cityId <= 0 || userId <= 0 || storeId <= 0 || StringUtils.isEmpty(openId)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		// FIXME 查询语句
		List<CoupList> coupLists = coupListDao.getValidCoupLists(userId);//以及其他的一些过滤条件
		if (coupLists.size() == 0) {
			resp.setCode(200);
			return resp;
		}
		List<Integer> couponIds = new ArrayList<Integer>();
		for(CoupList couplist : coupLists) {
			couponIds.add(couplist.getCouponId());
		}
		List<CoupRule> coupRules = coupRuleDao.findByIdIn(couponIds);
		Map<Integer, CoupRule> ruleMap = Maps.newHashMap();
		for (CoupRule coupRule: coupRules) {
			ruleMap.put(coupRule.getId(), coupRule);
		}
//		List<Integer> coupTypeIds = new ArrayList<Integer>();
//		for(CoupRule coupRule : coupRules) {
//			coupTypeIds.add(coupRule.getTypeid());
//		}
//		List<CoupType> coupTypes = coupTypeDao.findByIdIn(coupTypeIds);
		List<CoupList> validCoupList = Lists.newArrayList();
		CoupList recommendCoupList = null;	// 推荐用的券
		CartRespDto cart = cartService.getCart(userId, openId, cityId, storeId);
		for (CoupList coupList: coupLists) {
			CoupRule coupRule = ruleMap.get(coupList.getCouponId());
			if (verifyCoup(userId, openId, cityId, storeId, coupList, cart, coupRule)) {
				validCoupList.add(coupList);
				if (recommendCoupList == null) {
					recommendCoupList = coupList;
				}
				else if (coupList.getMoney() > recommendCoupList.getMoney()) {
					recommendCoupList = coupList;
				}
			}
		}
		
		CouponListResultDto result = resp.getResult();
		// 组装 dto
		if (validCoupList.size() > 0) {
			List<CouponListDto> couponListDtos = result.getCouponList();
			for (CoupList coupList: validCoupList) {
				String couponName = "";
				CoupRule coupRule = ruleMap.get(coupList.getCouponId());
				if (coupRule.getCoupontypeid() == 1) {
					couponName = "现金券";
				}
				else {
					couponName = String.format("满%.1f减%.1f", coupList.getMinprice(), coupList.getMoney());
				}
				CouponListDto couponListDto = new CouponListDto();
				couponListDto.setCouponId(coupList.getId());
				couponListDto.setCommoncode(coupList.getCommoncode());
				couponListDto.setCouponName(couponName);
				couponListDto.setMoney(String.format("%.1f", coupList.getMoney()));
				couponListDto.setCouponMsg(coupRule.getCouponName());
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
		result.setEverydayMsg(String.format("每天限使用%d张优惠券，明天再来吧", LIMIT_COUPON_PER_DAY));
		
		return resp;
	}
	
	
	public boolean verifyCoup(int userId, String openId, int cityId, int storeId, CoupList coupList, CartRespDto cart, CoupRule coupRule) {
		if (coupList == null || coupRule == null || cart == null) {
			return false;
		}
		
		if (coupRule.getCouponType() == CouponType.ALL) {
			// 全场通用
			double total = DoubleUtils.add(cart.getTotalPrice(), cart.getShippingFeeTotal());
			if (total < coupList.getMinprice()) {
				return false;
			}
			else {
				return true;
			}
		}
		else if (coupRule.getCouponType() == CouponType.STORE) {
			// 店铺券
			int outId = getOutId(coupRule);
			// FIXME 目前没有店铺券，不实现
		}
		else if (coupRule.getCouponType() == CouponType.GOODS) {
			// FIXME 目前没有商品券，不实现
		}
		else if (coupRule.getCouponType() == CouponType.BRAND) {
			// FIXME 目前没有品牌券，不实现
		}
		else if (coupRule.getCouponType() == CouponType.GOODS) {
			// FIXME 分类券
			// FIXME 分类券，第几级分类？
			// FIXME 如果有其他分类的商品，不能购买？
			int cateId = getOutId(coupRule);
		}
		
		return false;
	}
	
	@Override
	@Transactional
	public SubmitOrderRespDto submitOrder(SubmitOrderReqDto req) {
		// 校验参数
		if (req == null || req.getUserId() <= 0 || StringUtils.isEmpty(req.getOpenId()) ||
				req.getStoreId() <= 0 || req.getCityId() <= 0 || req.getAddressId() <= 0) {
			return new SubmitOrderRespDto(400, "参数有误");
		}
		// os
		int os = 21;	// Android
		if (OS.ANDROID.equals(req.getOs())) {
			os = 21;
		}
		else if (OS.IOS.equals(req.getOs())) {
			os = 30;
		}
		else {
			return new SubmitOrderRespDto(400, "目前只支持Android 和iOS 系统");
		}
		// shippingtime
		if (req.getShippingTimes() == null || (req.getShippingTimes().getBooking().size() == 0 &&
				req.getShippingTimes().getMaterial().size() == 0)) {
			return new SubmitOrderRespDto(400, "配送时间有误");
		}
		// 地址
		Address address = addressDao.findOne(req.getAddressId());
		if (!Validate(address)) {
			return new SubmitOrderRespDto(400, "地址有误");
		}
		
		// 购物车
		CartRespDto cartRespDto = cartService.getCart(req.getUserId(), req.getOpenId(), req.getCityId(), req.getStoreId());
		int packageNum = cartRespDto.getPackageList().size();
		if (packageNum == 0) {
			return new SubmitOrderRespDto(400, "购物车是空的");
		}
		// 校验库存
		for (PackageRespDto p: cartRespDto.getPackageList()) {
			for (CartGoodsRespDto g: p.getGoodsList()) {
				if (g.getAmount() > g.getStock()) {
					return new SubmitOrderRespDto(400, "部分商品库存不足");
				}
				if (g.getOverdue() == 1) {
					return new SubmitOrderRespDto(400, "部分预售商品预售时间已过");
				}
			}
		}
		// 校验配送时间
		List<String> materialShippingTime = req.getShippingTimes().getMaterial();
		List<SpecShippingTime> bookingShippingTime = req.getShippingTimes().getBooking();
		Map<Integer, String> bookingShippingTimeMap = Maps.newHashMap();
		for (SpecShippingTime st: bookingShippingTime) {
			bookingShippingTimeMap.put(st.getSpecId(), st.getTime());
		}
		for (PackageRespDto p: cartRespDto.getPackageList()) {
			String needShippingTime = null;
			if (PackageType.MATERIAL.equals(p.getPackageType())) {
				if (materialShippingTime == null || materialShippingTime.size() == 0) {
					return new SubmitOrderRespDto(400, "商品配送时间有误");
				}
				needShippingTime = materialShippingTime.get(0);
			}
			else if (PackageType.BOOKING.equals(p.getPackageType())) {
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
		
		// 优惠券, 目前只有全场券
		double needPay = DoubleUtils.add(cartRespDto.getTotalPrice(), cartRespDto.getShippingFeeTotal());	// 还需付多少钱
		int couponId = req.getCouponId();
		CoupList coupList = null;
		if (couponId > 0) {
			coupList = coupListDao.getValidCoupList(req.getUserId(), couponId);
			if (coupList == null) {
				return new SubmitOrderRespDto(400, "优惠券不可用");
			}
			
			// 校验优惠券是否可用
			if (coupList.getMinprice() > cartRespDto.getTotalPrice()) {
				return new SubmitOrderRespDto(400, String.format("使用优惠券最小金额为%.2f. 优惠券不可用", coupList.getMinprice()));
			}
			// 更新优惠券状态为已使用
			boolean used = coupListService.useCoupon(req.getUserId(), couponId);
			if (!used) {
				// 使用优惠券失败
				String msg = String.format("use coupon FAILED, userId=%d, couponId=%d", req.getUserId(), couponId);
				LOGGER.error(msg);
				throw new RuntimeException(msg);
			}
			needPay = DoubleUtils.mul(needPay, coupList.getMoney(), 2);
		}
		

		// 新建订单
		final String orderSnMain = generateOrderSnMain();
		double usedDsicount = 0.0;	// 计算过的折扣
		for (int i = 0; i < packageNum; i ++) {
			
			PackageRespDto pl = cartRespDto.getPackageList().get(i);
			double goodsAmount = 0.0;	// 该包裹商品总额
			for (CartGoodsRespDto g: pl.getGoodsList()) {
				goodsAmount = DoubleUtils.add(goodsAmount, DoubleUtils.mul(g.getPrice(), g.getAmount(), 2));
			}
			
			// 计算优惠券，每个包裹的折扣是按照包裹金额比例来分配
			double discount = 0.0;	// 折扣
			if (coupList != null) {
				double totalDiscount = coupList.getMoney();
				if (i == cartRespDto.getPackageList().size()-1) {
					// 最后一个包裹的折扣
					discount = DoubleUtils.sub(totalDiscount, usedDsicount);
				}
				else {
					// 该包裹折扣 = 该包裹商品总额 * 折扣总金额/所有商品总额
					discount = DoubleUtils.div(DoubleUtils.mul(goodsAmount, totalDiscount), cartRespDto.getTotalPrice(), 1);
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
				String.format("%s-%d-%d", orderSnMain, packageNum, i+1);
			}
			order.setOrderSn(generateOrderSn());
			order.setTaoOrderSn(taoOrderSn);
			if (PackageType.MATERIAL.equals(pl.getPackageType())) {
				order.setShippingFee(cartRespDto.getShippingFeeTotal());
			}
			else {
				order.setShippingFee(0);
			}
			String needShippingTime = null;
			if (PackageType.MATERIAL.equals(pl.getPackageType())) {
				needShippingTime = materialShippingTime.get(0);
			}
			else if (PackageType.BOOKING.equals(pl.getPackageType())) {
				int specId = pl.getGoodsList().get(0).getSpecId();
				needShippingTime = bookingShippingTimeMap.get(specId);
			}
			String[] strs = needShippingTime.split(" ");
			order.setNeedShiptime(DateUtils.str2Date(strs[0].trim()));
			order.setNeedShiptimeSlot(strs[1].trim());
			order.setType(pl.getPackageType());
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
			store = storeService.getByStoreId(req.getStoreId());
			order.setSellerId(storeId);
			order.setSellerName(store.getStoreName());
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
			}
			else {
				order.setNeedInvoice(0);
			}
			order.setInvoiceType(req.getInvoiceType());
			order.setPostscript(req.getPostScript());
			if (PackageType.BOOKING.equals(pl.getPackageType())) {
				order.setShippingId(0);	// 小黄蜂配送
			}
			else {
				order.setShippingId(1);	// 商家自送
			}
			if (PackageType.MATERIAL.equals(pl.getPackageType())) {
				order.setShippingFee(cartRespDto.getShippingFeeTotal());
			}
			else {
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
			double priceDiscount = 0.0;	// 商品折扣后的价格
			for (CartGoodsRespDto g: pl.getGoodsList()) {
				// FIXME 为什么记录订单商品的时候，还要转成标准规格的商品？
				priceDiscount = g.getPrice();
				if (discount > 0) {
					// 单个商品折扣, priceDiscount = price - (price/goodsAmount)*discount
					priceDiscount = DoubleUtils.sub(g.getPrice(), DoubleUtils.mul(DoubleUtils.div(g.getPrice(), goodsAmount), discount, 8));
				}
				
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setOrderId(newOrder.getOrderId());
				orderGoods.setGoodsId(g.getGoodsId());
				orderGoods.setGoodsName(g.getGoodsName());
				orderGoods.setSpecification(g.getSpecName());
				orderGoods.setStoreId(storeId);
				orderGoods.setPrice(g.getPrice());	// TODO 现在不需要佣金了把？
				orderGoods.setPricePurchase(g.getPrice());
				orderGoods.setPriceDiscount(priceDiscount); // 计算折扣价格
				orderGoods.setQuantity(g.getAmount());
				orderGoods.setPoints(0); 	// TODO 计算积分，目前积分不要了
				orderGoods.setProType(g.getFlag());
				orderGoods.setPackageId(0);	// TODO 组合购买的组合ID 或套餐ID, 不知道怎么算
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
				orderPay.setPaymentId(14);	// 优惠券
				orderPay.setMoney(discount);
				orderPay.setPayTime(DateUtils.getTime());
				orderPay.setStatus("succ");
				
				orderPayDao.save(orderPay);
			}
		}
		
		// 统一更新 更新已售商品 冻结商品库存
		goodsService.freeze(req.getUserId(), req.getOpenId(), req.getCityId(), req.getStoreId());

		
		// 添加收货地址信息
		OrderExtm orderExtm = new OrderExtm();
		BeanUtils.copyProperties(address, orderExtm);
		orderExtm.setOrderSnMain(orderSnMain);
		orderExtmDao.save(orderExtm);
		
		String lnglat = String.format("lat:%s,lng:%s", address.getLatitude(), address.getLongitude());
		OrderLnglat orderLnglat = new OrderLnglat();
		orderLnglat.setOrderSnMain(orderSnMain);
		orderLnglat.setLnglat(lnglat);
		orderLnglatDao.save(orderLnglat);
		
		// 添加tcz_order_action
		OrderAction orderAction = new OrderAction();
		orderAction.setAction(0);	//  下单
		orderAction.setOrderSnMain(orderSnMain);
		orderAction.setActor(user.getName());
		orderAction.setActionTime(new Date());
		orderAction.setNotes("下单");
		orderActionDao.save(orderAction);
		
		try {
			// 清空购物车
			cartService.clear(req.getUserId(), req.getOpenId(), req.getCityId());
		}
		catch (Exception e) {
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
		if (StringUtils.isEmpty(address.getConsignee()) || 
				address.getRegionId() <= 0 ||
				StringUtils.isEmpty(address.getRegionName()) ||
				StringUtils.isEmpty(address.getAddress()) ||
				StringUtils.isEmpty(address.getPhoneMob())) {
			return false;
		}
			
		return true;
	}

	private int getOutId(CoupRule coupRule) {
		String[] strs = coupRule.getOutId().split(",");
		if (strs.length > 0) {
			return Integer.valueOf(strs[0]);
		}
		return 0;
	}

	@Override
	public OrderListRespDto getOrderInfo(int userId, String orderSnMain,
			int flag) {
		OrderListRespDto resp = new OrderListRespDto();
		if(userId <= 0 || flag <=0 || StringUtils.isEmpty(orderSnMain)) {
			resp.setCode(400);
			return null;
		}
		if(CollectionUtils.isEmpty(orderDao.findByOrderSnMain(orderSnMain))) {
			resp.setCode(400);
			return null;
		}
		List<OrderReturn> orderReturns = orderRDao.findByOrderSnMainAndOrderStatus(orderSnMain, 0);
		if(!CollectionUtils.isEmpty(orderReturns)) {
			flag = 4;
		}
		Map<String, OrderListDto> mainOrderMap = new HashMap<String, OrderListDto>();
		getOrderAttr(mainOrderMap, orderSnMain, flag);
		return resp;
	}
	
	/**
	 * 生成主订单号 orderSnMain，'年月日时分+5位随机数'
	 * @return
	 */
	private String generateOrderSnMain() {
		int min = 10000;
		int max = 99999;
		String date = DateUtils.date2DateStr3(new Date());
		Random random = new Random();
		int r = random.nextInt(max)%(max-min+1) + min;
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
	 * @return
	 */
	private String generateOrderSn() {
//    	global $G_SHOP;
//        /* 选择一个随机的方案 */
//        mt_srand((double) microtime() * 1000000);
//        $timestamp = gmtime();
//        $y = date('y', $timestamp);
//        $z = date('z', $timestamp);
//        $order_sn = $y . str_pad($z, 3, '0', STR_PAD_LEFT) . str_pad(mt_rand(1, 99999), 5, '0', STR_PAD_LEFT);
//
//        $strSQL="SELECT order_sn FROM tcz_order WHERE order_sn='".$order_sn."'";
//        $orders=$G_SHOP->DBCA->getOne($strSQL);
//
//        if (empty($orders))
//        {
//            /* 否则就使用这个订单号 */
//            return $order_sn;
//        }
//
//        /* 如果有重复的，则重新生成 */
//        return $this->_gen_order_sn();
		return "";
	}
	
		
	@Override
	public PayOrderResultRespDto getPayOrderMsg(int userId, String orderSnMain) {
		PayOrderResultRespDto resp = new PayOrderResultRespDto();
		PayOrderMsgDto result = new PayOrderMsgDto();
		if(userId <= 0 || StringUtils.isEmpty(orderSnMain)) {
			resp.setCode(400);
			return resp;
		}
		if(CollectionUtils.isEmpty(orderDao.findByOrderSnMain(orderSnMain))) {
			resp.setCode(400);
			return resp;
		}
		
		List<Order> orders = (List<Order>)orderDao.findByOrderSnMain(orderSnMain);
		double orderTotal = 0, shippingFee = 0;
		for(Order o:orders)
		{
			orderTotal = DoubleUtils.add(orderTotal, o.getGoodsAmount());
			orderTotal = DoubleUtils.add(orderTotal, o.getShippingFee());
			shippingFee += o.getShippingFee();
		}
		
		double payedMoney = orderPayDao.getPayedAmountByOrderSnMain(orderSnMain);
		double txkValue = AccountTxkProcessor.getProcessor().getTxkBalanceByUserId(userId);
		double vCountValue = VirtualAccountProcessor.getProcessor().getVirtualBalanceByUserId(userId);
		
		result.setDiscountAmount(payedMoney);
		result.setOrderSnMain(orderSnMain);
		result.setOrderTotal(orderTotal-shippingFee);
		result.setShippingFee(shippingFee);
		result.setTotal(orderTotal-payedMoney);
		result.setTxkNum(txkValue);
		result.setVcount(vCountValue);
		resp.setResult(result);
		
		return resp;
		
	}
	
	
	/*
	 *  返回列表页展示的订单信息
	 */
	private Map<String, OrderListDto> getOrderAttr(Map<String, OrderListDto> mainOrderMap, String orderSnMain, int flag) {
		List<Order> ordersList = orderDao.findByOrderSnMain(orderSnMain);
		if(CollectionUtils.isEmpty(ordersList)) {
			return null;
		}
		int payStatus = 1;//默认不显示立即付款按钮
		int orderPayStatus = 0;
		int pType = 0;
		int pStatus = 0;
		//是否可付款 立即付款按钮的显示
		for(Order order : ordersList) {
			if(order.getStatus() == 1) {
				orderPayStatus = 1;//订单初始状态
			}
			if(order.getPayType() != 1) {
				pType = 1;//非货到付款
			}
			if(order.getPayStatus() != 1) {
				pStatus = 1;//非已支付
			}
		}
		
		if(orderPayStatus == 1 && pType == 1 && pStatus ==1) {
			payStatus = 0;//需要支付
		}
		
		String source = "";
		for(Order order : ordersList) {
			
			source = OrderSourceEnum.parseSource(order.getSource()).getSource();
			//0网站1移动公司2电视终端3集团用户下单4移动礼盒5活动6中奖 8快餐 9快餐代购 10优惠券活动 11.铁通年终回馈 12 自提点代下单 20=iphone 21=android 100=外站数据 25苹果 26 大宗采购 27社区点 30 iphone
				//0 小黄蜂配送，全支持；1 商家自送，全支持；2 商家自送，不支持货到付款；3 商家自送，不支持在线支付；
			//4 独立算运费小黄蜂配送，全支持；5 签约小黄蜂，不支持货到付款；'
			int freight = storeDao.findOne(order.getSellerId()).getFreight();
            String shippingtype = "淘常州自送";
		
			if(freight != 0) {
				freight = 1;
				shippingtype = "第三方配送";
			}
			
			String payType = "";
			if(order.getPayType() == 1) {
				payType = "货到付款";
			} else if (order.getPayType() == 2) {
				payType = "在线支付";
			}
			
			OrderListBaseDto baseDto = new OrderListBaseDto();
			if(payStatus == 0) {//未支付，不分包裹
				//已付金额
				double payedAmount = orderPayDao.getPayedAmountByOrderSnMain(orderSnMain);
				baseDto.setOrderId(order.getOrderId());
				baseDto.setOrderSnMain(orderSnMain);
				baseDto.setSellerId(order.getSellerId());
				baseDto.setSource(source);
				baseDto.setState("未付款");
				if(order.getAddTime() != 0) {
					baseDto.setAddTime(SDF.format(order.getAddTime() * 1000));
				}
				if(order.getPayTime() != 0) {
					baseDto.setPayTime(SDF.format(order.getPayTime() * 1000));
				}
				if(order.getShipTime() != 0) {
					baseDto.setShipTime(SDF.format(order.getShipTime() * 1000));
				}
				baseDto.setStatus(order.getStatus());
				baseDto.setPayStatus(payStatus);//立即付款的判断
				baseDto.setTaoOrderSn(order.getOrderSnMain());//包裹id
				baseDto.setIsshouhuo(getReciveStatus(order.getOrderSnMain()));//确认收货的判断
				baseDto.setTotalPrice(DECIMALFORMAT.format(getTotalPriceStr(orderSnMain)));
				baseDto.setNeedPayPrice(DECIMALFORMAT.format(getTotalPriceStr(orderSnMain) - payedAmount));//还需支付金额
				baseDto.setShippingFee(getTotalShippingFee(orderSnMain));//订单总运费
				baseDto.setPackageStatus(getPackageStatus(orderSnMain, 1));//包裹的状态
				baseDto.setShipping(getShippingMsg(orderSnMain, ""));//显示小黄蜂配送信息
				baseDto.setArrivalCode(order.getShippingNo());//收货码
				baseDto.setCommentStatus(0);//hardcode
				baseDto.setStorePhone("");
				baseDto.setRefundStatus(getRefundStatus(orderSnMain, flag));
				baseDto.setDiscount(DECIMALFORMAT.format(getTotalDiscount(orderSnMain, "")));//订单优惠总金额
				baseDto.setShippingtype(shippingtype);
				baseDto.setPayType(payType);
				baseDto.setInvoiceHeader(order.getInvoiceHeader());
				baseDto.setPostscript(order.getPostscript());
				
//				baseDto.setIsOrder("1");//由于未分包裹，所以为1，（如果订单号和包裹号一样则为订单，反之则为包裹）//TODO
				List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(order.getOrderId());
				List<GoodsListDto> goodsList = new ArrayList<GoodsListDto>();
				for(OrderGoods orderGoods : orderGoodsList) {
					GoodsListDto goodsListDto = new GoodsListDto();
					BeanUtils.copyProperties(orderGoods, goodsListDto);
					goodsList.add(goodsListDto);
				}
				
				if(mainOrderMap.containsKey(orderSnMain)) {
					if(mainOrderMap.get(orderSnMain) == null) {
						OrderListDto orderListDto = new OrderListDto();
						orderListDto.setGoodsList(goodsList);
						orderListDto.setBase(baseDto);
						mainOrderMap.put(orderSnMain, orderListDto);
					} else {
						mainOrderMap.get(orderSnMain).setGoodsList(goodsList);
						mainOrderMap.get(orderSnMain).setBase(baseDto);
					}
				} else {
					OrderListDto orderListDto = new OrderListDto();
					orderListDto.setGoodsList(goodsList);
					orderListDto.setBase(baseDto);
					mainOrderMap.put(orderSnMain, orderListDto);
				}
				//如果订单号和包裹号一样则为订单，反之则为包裹
				if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
					if(StringUtils.equals(mainOrderMap.get(order.getTaoOrderSn()).getBase().getOrderSnMain(), 
							mainOrderMap.get(order.getTaoOrderSn()).getBase().getTaoOrderSn())) {
						mainOrderMap.get(order.getTaoOrderSn()).getBase().setIsOrder("1");
					} else {
						mainOrderMap.get(order.getTaoOrderSn()).getBase().setIsOrder("2");
					}
				}
			} else {//已支付，分包裹
				Store store = storeDao.findOne(order.getSellerId());
				if(StringUtils.isBlank(store.getOwnerMob())) {
					if(StringUtils.isNotBlank(store.getOwnerTel())) {
						store.setOwnerMob(store.getOwnerTel());
					}
				}
				baseDto.setOrderId(order.getOrderId());
				baseDto.setOrderSnMain(orderSnMain);
				baseDto.setSellerId(order.getSellerId());
				baseDto.setSource(source);
				baseDto.setState(getPackageStatus(order.getTaoOrderSn(), 0));
				if(order.getAddTime() != 0) {
					baseDto.setAddTime(SDF.format(order.getAddTime() * 1000));
				}
				if(order.getPayTime() != 0) {
					baseDto.setPayTime(SDF.format(order.getPayTime() * 1000));
				}
				if(order.getShipTime() != 0) {
					baseDto.setShipTime(SDF.format(order.getShipTime() * 1000));
				}
				baseDto.setStatus(order.getStatus());
				baseDto.setPayStatus(payStatus);//立即付款的判断
				baseDto.setTaoOrderSn(order.getTaoOrderSn());//包裹id
				baseDto.setIsshouhuo(getReciveStatus(order.getTaoOrderSn()));//确认收货的判断
				baseDto.setTotalPrice(DECIMALFORMAT.format(getPackagePrice(order.getTaoOrderSn())));
//				baseDto.setNeedPayPrice(DECIMALFORMAT.format(getTotalPriceStr(orderSnMain) - payedAmount));//还需支付金额
				baseDto.setShippingFee(order.getShippingFee());//包裹总运费
				if(getReciveStatus(order.getTaoOrderSn()) == 0) {
					baseDto.setPackageStatus(getPackageStatus(orderSnMain, 1));
				}
				
//				baseDto.setPackageStatus(getPackageStatus(orderSnMain, 1));//包裹的状态
				baseDto.setShipping(getShippingMsg(orderSnMain, order.getTaoOrderSn()));//显示小黄蜂配送信息
				baseDto.setArrivalCode(order.getShippingNo());//收货码
				baseDto.setCommentStatus(0);//hardcode
				baseDto.setStorePhone(store.getOwnerMob());
				baseDto.setRefundStatus(getRefundStatus(orderSnMain, flag));
				baseDto.setDiscount(DECIMALFORMAT.format(getTotalDiscount(orderSnMain, order.getTaoOrderSn())));//订单优惠总金额
				baseDto.setShippingtype(shippingtype);
				baseDto.setPayType(payType);
				baseDto.setInvoiceHeader(order.getInvoiceHeader());
				baseDto.setPostscript(order.getPostscript());
						
				
				List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(order.getOrderId());
				List<GoodsListDto> goodsList = new ArrayList<GoodsListDto>();
				for(OrderGoods orderGoods : orderGoodsList) {
					GoodsListDto goodsListDto = new GoodsListDto();
					BeanUtils.copyProperties(orderGoods, goodsListDto);
					goodsList.add(goodsListDto);
				}
				
				if(order.getStatus() >= 3 || order.getStatus() == 0) {
					if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
						mainOrderMap.get(order.getTaoOrderSn()).setBase(baseDto);
						if(CollectionUtils.isEmpty(mainOrderMap.get(order.getTaoOrderSn()).getGoodsList())) {
							mainOrderMap.get(order.getTaoOrderSn()).setGoodsList(goodsList);
						} else {
							mainOrderMap.get(order.getTaoOrderSn()).getGoodsList().addAll(goodsList);
						}
			
						if(mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg() != null) {
							mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setDescription("订单已支付，等待商家发货");
							mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime(SDF.format(order.getPayTime() * 1000));
						}
					} else {
						OrderListDto orderListDto = new OrderListDto();
						ShippingMsgDto shippingMsgDto = new ShippingMsgDto();
						shippingMsgDto.setDescription("订单已支付，等待商家发货");
						shippingMsgDto.setCreatTime(SDF.format(order.getPayTime() * 1000));
						orderListDto.setShippingmsg(shippingMsgDto);
						orderListDto.setBase(baseDto);
						mainOrderMap.get(order.getTaoOrderSn()).setGoodsList(goodsList);
						mainOrderMap.put(orderSnMain, orderListDto);
					}
				}
				
				//如果订单号和包裹号一样则为订单，反之则为包裹
				if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
					if(StringUtils.equals(mainOrderMap.get(order.getTaoOrderSn()).getBase().getOrderSnMain(), 
							mainOrderMap.get(order.getTaoOrderSn()).getBase().getTaoOrderSn())) {
						mainOrderMap.get(order.getTaoOrderSn()).getBase().setIsOrder("1");
					} else {
						mainOrderMap.get(order.getTaoOrderSn()).getBase().setIsOrder("2");
					}
				}
			}
			//已审核的订单返回最新的物流信息
			ShippingResultDto shippingResultDto = new ShippingResultDto();
			if(order.getStatus() >= 3) {
				
				if(payStatus == 0) {
					shippingResultDto = getShippingResult(shippingResultDto, orderSnMain);
				} else {
					shippingResultDto = getShippingResult(shippingResultDto, order.getTaoOrderSn());
				}
				
				//TODO getShippingResult 有参数时
				if(shippingResultDto.getCode() == 0) {
					if(StringUtils.isBlank(shippingResultDto.getResult().getShippingList().get(0).getDescription())) {
						if(order.getStatus() == 13 || order.getStatus() == 14) {
							if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
								if(mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg() != null) {
									mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setDescription("商家已发货");
									if(order.getShipTime() != 0) {
										mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime(SDF.format(order.getShipTime() * 1000));
									} else {
										mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime("");
									}
								}
							}
						} else if (order.getStatus() == 15) {
							if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
								if(mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg() != null) {
									mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setDescription("订单已完成，欢迎再来逛逛");
									if(order.getShipTime() != 0) {
										mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime(SDF.format(order.getFinishedTime() * 1000));
									} else {
										mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime("");
									}
								}
							}
						}
					} else {
						if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
							if(mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg() == null) {
								ShippingMsgDto msgDto = new ShippingMsgDto();
								msgDto.setDescription(shippingResultDto.getResult().getShippingList().get(0).getDescription());
								if(order.getFinishedTime() != 0) {
									msgDto.setCreatTime(SDF.format(order.getAddTime() * 1000));
								}
								
								mainOrderMap.get(order.getTaoOrderSn()).setShippingmsg(msgDto);
							}
						}
					}
				} else {//getShippingResult没有参数时
					if(order.getStatus() == 13 || order.getStatus() == 14) {
						if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
							if(mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg() != null) {
								mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setDescription("商家已发货");
								if(order.getShipTime() != 0) {
									mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime(SDF.format(order.getShipTime() * 1000));
								} else {
									mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime("");
								}
							}
						}
					} else if (order.getStatus() == 15) {
						if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
							if(mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg() != null) {
								mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setDescription("订单已完成，欢迎再来逛逛");
								if(order.getShipTime() != 0) {
									mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime(SDF.format(order.getFinishedTime() * 1000));
								} else {
									mainOrderMap.get(order.getTaoOrderSn()).getShippingmsg().setCreatTime("");
								}
							}
						}
					}
				}
			}
			//收货信息
			List<OrderExtm> orderExtms = orderExtmDao.findByOrderSnMain(orderSnMain);
			ExtmMsgDto extmMsgDto = new ExtmMsgDto();
			
			if(payStatus == 0) {//未支付，不分包裹
				if(mainOrderMap.containsKey(orderSnMain)) {
					if(mainOrderMap.get(orderSnMain).getExtmMsg() == null) {
						extmMsgDto.setAddress(trimall(orderExtms.get(0).getRegionName() + orderExtms.get(0).getAddress()));
						extmMsgDto.setConsignee(orderExtms.get(0).getConsignee());
						extmMsgDto.setPhone_mob(orderExtms.get(0).getPhoneMob());
						mainOrderMap.get(orderSnMain).setExtmMsg(extmMsgDto);
					} else {
						mainOrderMap.get(orderSnMain).getExtmMsg().setAddress(
								trimall(orderExtms.get(0).getRegionName() + orderExtms.get(0).getAddress()));
						mainOrderMap.get(orderSnMain).getExtmMsg().setConsignee(orderExtms.get(0).getConsignee());
						mainOrderMap.get(orderSnMain).getExtmMsg().setPhone_mob(orderExtms.get(0).getPhoneMob());
					}
				}
			} else {//已支付，分包裹
				if(mainOrderMap.containsKey(order.getTaoOrderSn())) {
					if(mainOrderMap.get(order.getTaoOrderSn()).getExtmMsg() == null) {
						extmMsgDto.setAddress(trimall(orderExtms.get(0).getRegionName() + orderExtms.get(0).getAddress()));
						extmMsgDto.setConsignee(orderExtms.get(0).getConsignee());
						extmMsgDto.setPhone_mob(orderExtms.get(0).getPhoneMob());
						mainOrderMap.get(order.getTaoOrderSn()).setExtmMsg(extmMsgDto);
					} else {
						mainOrderMap.get(order.getTaoOrderSn()).getExtmMsg().setAddress(
								trimall(orderExtms.get(0).getRegionName() + orderExtms.get(0).getAddress()));
						mainOrderMap.get(order.getTaoOrderSn()).getExtmMsg().setConsignee(orderExtms.get(0).getConsignee());
						mainOrderMap.get(order.getTaoOrderSn()).getExtmMsg().setPhone_mob(orderExtms.get(0).getPhoneMob());
					}
				}
			}
			
		}
		
		return mainOrderMap;
	}
	
	 private String trimall(String str)//删除空格
		{
		 	String[] searchList = {" ", "  ", "\t", "\n", "\r"};
		 	String[] replacementList = {"", "", "", "", ""};
		 	
			return StringUtils.replaceEach(str, searchList, replacementList); 
		}
	
	//物流信息
    //传参数 列表详情调用
    //不传参数 接口调用
	@Override
	public ShippingResultDto getShippingResult(ShippingResultDto shippingResultDto, String taoOrderSn) {
		
		ShippingListResultDto resultDto = new ShippingListResultDto();
		if(StringUtils.isEmpty(taoOrderSn)) {
			return null;
		}
		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
		Express express = expressDao.findByCodeNum(order.getShippingCompany());
		if(express == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if(order.getShippingId() == 0 || order.getShippingId() > 5) {
			sb.append("淘常州小黄蜂配送");
		} else {
			sb.append("商家自送");
		}
		if(StringUtils.isNotBlank(express.getExpressName())) {
			sb.append(express.getExpressName());
			resultDto.setShippingName(sb.toString());
		}
		
		if(order.getPayType() == 1) {
			resultDto.setPayType("货到付款");
		} else if(order.getPayType() == 2) {
			resultDto.setPayType("在线支付");
		}
		List<ShippingListDto> shippingList = new ArrayList<ShippingListDto>();
		for(OrderAction orderAction : orderActionDao.findByOrderSnMain(order.getOrderSnMain())) {
			if(!(orderAction.getAction() == 33 || orderAction.getAction() == 20 || orderAction.getAction() == 3)
					&& StringUtils.isNotBlank(orderAction.getTaoOrderSn())) {
				ShippingListDto shippingListDto = new ShippingListDto();
				shippingListDto.setCreateTime(orderAction.getTimestamp().toString());
				shippingListDto.setDescription(orderAction.getNotes());
				shippingListDto.setTaoOrderSn(taoOrderSn);
				shippingList.add(shippingListDto);
			}
		}
		resultDto.setShippingList(shippingList);
		shippingResultDto.setResult(resultDto);
		shippingResultDto.setCode(0);//TODO 区分传参数和不传参数
		return shippingResultDto;
	}
	
    /**
	 * 订单商品优惠总金额
	 * $v['goods_amount'] + $v['shipping_fee']
	 * taoOrderSn默认为“”
	 */
	private double getTotalDiscount(String orderSnMain, String taoOrderSn) {
		double discount = 0;
		if(StringUtils.isBlank(taoOrderSn)) {
			discount = orderDao.findByTaoOrderSn(taoOrderSn).getDiscount();
		} else {
			List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
			for(Order order : orders) {
				discount += order.getDiscount();
			}
		}
		return discount;
	}
	
	 /**
     * 返回退款状态 flag 1:全部 2:待付款 3:待收货 4:退货
     */
    private String getRefundStatus(String orderSnMain, int flag) {
    	StringBuilder result = new StringBuilder("");
    	if(flag == RefundStatusEnum.STATUS_RETURNED.getFlag()) {
    		List<OrderReturn> orderReturns = orderRDao.findByOrderSnMainAndOrderStatus(orderSnMain, 0);
    		for(OrderReturn orderReturn : orderReturns) {
    			if(orderReturn.getRefundStatus() == 0) {
    				result.append(orderReturn.getOrderId()).append(" ").append("未退款 ");
        		} else {
        			result.append(orderReturn.getOrderId()).append(" ").append("已退款 ");
        		}
    		}
    	}
    	return result.toString();
    }
	
	/**
	 * 默认taoOrderSn传入“”
	 */
	private String getShippingMsg(String orderSnMain, String taoOrderSn) {
		List<Order> orders = new ArrayList<Order>();
		StringBuilder shippingMsg = new StringBuilder();
		if(StringUtils.isNotBlank(taoOrderSn)) {
			Order order = orderDao.findByTaoOrderSn(taoOrderSn);
			if(null != order) {
				if(order.getType() == "booking") {
					shippingMsg.append("预售商品").append(order.getNeedShiptime()).append(" ").append(order.getNeedShiptimeSlot()).append("、");
				} else if(StringUtils.equals(order.getType(), "wei_wh") || StringUtils.equals(order.getType(), "wei_self")) {
					shippingMsg.append("普通商品").append(order.getNeedShiptime()).append(" ").append(order.getNeedShiptimeSlot()).append("、");
				} else if(StringUtils.equals(order.getType(), "self_sales")) {
					shippingMsg.append("配送单预计1-3日内送达  、");
				}
			}
		} else {
			orders = orderDao.findByOrderSnMain(orderSnMain);
			for(Order order : orders) {
				if(order.getType() == "booking") {
					shippingMsg.append("预售商品").append(order.getNeedShiptime()).append(" ").append(order.getNeedShiptimeSlot()).append("、");
				} else if(StringUtils.equals(order.getType(), "wei_wh") || StringUtils.equals(order.getType(), "wei_self")) {
					shippingMsg.append("普通商品").append(order.getNeedShiptime()).append(" ").append(order.getNeedShiptimeSlot()).append("、");
				} else if(StringUtils.equals(order.getType(), "self_sales")) {
					shippingMsg.append("配送单预计1-3日内送达  、");
				}
			}
		}
		
		return StringUtils.removeEnd(shippingMsg.toString(), "、");
	}
	
	/*
	 * 默认需传入isOrder ＝ 1
	 */
	private String getPackageStatus(String orderSnMain, int isOrder) {
		List <Order> orders = new ArrayList<Order>();
		Order order = new Order();
		if(isOrder == 1) {
			orders = orderDao.findByOrderSnMain(orderSnMain);
		} else {
			order = orderDao.findByTaoOrderSn(orderSnMain);
		}
		/**
		 *  只要 有 一个 0 就返回  1
		 *  都没有 返回  0
		 *  屏蔽 00 2 的情况
		 */
		int state;
		if(CollectionUtils.isEmpty(orders)) {
			state = order.getStatus();
		} else {
			state = orders.get(0).getStatus();
			for(Order o : orders) {
				if(o.getStatus() == 0) {
					state = 0;
				}
			}
		}

		return getReturnStatus(state);
	}
	
	private String getReturnStatus(int status) {
		return ReturnStatusEnum.parseType(status).getComment();
	}
	
    /**
	 * 订单总运费-----为0显示免邮
	 * $v['shipping_fee']
	 */
	private double getTotalShippingFee(String orderSnMain) {
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		if(CollectionUtils.isEmpty(orders)) {
			return 0;
		}
		double shippingFee = 0;
		for(Order order : orders) {
			shippingFee += order.getShippingFee();
		}
		
		return shippingFee;
	}
	
	
    /**
	 * 包裹的总金额
	 */
	private double getPackagePrice(String taoOrderSn){
		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
		if(null == order) {
			return 0;
		}
		return order.getGoodsAmount() + order.getShippingFee();
	}
	
    /**
     * 订单总金额
     * $v['goods_amount'] + $v['shipping_fee']
     */
	private double getTotalPriceStr(String orderSnMain) {
		double price = 0;
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		for(Order order: orders) {
			price += order.getGoodsAmount();
			price += order.getShippingFee();
		}
		return price;
	}
	
    //-------------------------------------------------shouhuo---------------------------------
	/**
	 * 判断是否显示    确认收货
	 *
	 * 返回 1 显示         ==商家已发货 且  没有回单(商家自己配送的)
	 *     0 不显示
	 *     2 显示等待收货==审单通过且商家未发货
	 *
	 * status : ok
	 */
	private int getReciveStatus(String taoOrderSn) {
		Order order = orderDao.findByTaoOrderSn(taoOrderSn);
		if(null == order) {
			return 0;
		}
		
		if (order.getStatus() >= 3 && order.getStatus() < 14) {// 审核通过 未发货
			return 2;
		} else if (order.getStatus() >= 14 && order.getStatus() < 15
				&& order.getShippingId() == 1) {
			// 商家已发货 且 没有回单(商家自己配送的)
			return 1;
		}
		return 0;
	}

	
	/**
	 * 用户app接口，暂时不用
	 */
//	@Override
//	public OrderCancelRespDto cancelOrder(int userId, String orderSnMain) {
//		OrderCancelRespDto resp = new OrderCancelRespDto();
//		if(userId <= 0 || StringUtils.isBlank(orderSnMain)) {
//			resp.setCode(400);
//			return resp;
//		}
//		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
//		if(CollectionUtils.isEmpty(orders)) {
//			resp.setCode(400);
//			return resp;
//		}
//		//0未支付 1已支付
//		int payedStatus = orders.get(0).getPayStatus();
//		
//		
//		for(Order order : orders) {
//			List<String> couponCodes = new ArrayList<String>();
//			double orderPayed = 0;
//			if(order.getStatus() == 3 || order.getStatus() == 1) {
//				resp.setCode(400);//TODO errorcode
//			} else if(order.getOrderPayed() > 0) {
//				orderPayed += order.getOrderPayed();
//				
//				//TODO 原php代码中为if($order['use_coupon_no'] <> "")
//				if(StringUtils.isNotBlank(order.getUseCouponNo()) && StringUtils.equals(order.getUseCouponNo(), "0")) {
//					couponCodes.add(order.getUseCouponNo());
//				}
//			}
//			if(orderPayed > 0) {//部分或全部支付
//				Map<Integer, Double> paymentIdMoneyMap = getPay(order.getOrderId());//支付明细
//				double returnAmountVcount = 0;
//				double returnAmountTxk = 0;
//				double returnAmountCoupon = 0;
//				double returnAmount = 0;
//				double returnSum = 0;
//				for(Map.Entry<Integer, Double> entry : paymentIdMoneyMap.entrySet()) {
//					if(entry.getKey() == 2) { //虚拟账户
//						returnAmountVcount += entry.getValue();
//					} else if(entry.getKey() == 6) {
//						returnAmountTxk += entry.getValue();
//					} else if(entry.getKey() == 14) {
//						returnAmountCoupon += entry.getValue();
//					} else {
//						returnAmount += entry.getValue();
//					}
//				}
//				returnSum = returnAmountVcount + returnAmountTxk + returnAmountCoupon + returnAmount;
//				
//				if(returnAmount > 0) {
//					//有采用在线支付，则生成未退款单及应退明细记录
//					int orderIdR = getOrderReturnAdd(orderSnMain, order.getOrderId(), order.getBuyerId(), order.getSellerId(), 
//							returnSum, 0, 0);
//					for(Map.Entry<Integer, Double> entry : paymentIdMoneyMap.entrySet()) {
//						OrderPayR  orderPayR = new OrderPayR();
//						orderPayR.setOrderIdR(orderIdR);
//						orderPayR.setRepayWay(0);
//						orderPayR.setPaymentId(entry.getKey());
//						orderPayR.setValue(entry.getValue());
//						orderPayRDao.save(orderPayR);
//					}
//				} else {
//					 //否则安照原先支付记录自动退款，并生成已退款单
//					int orderIdR = getOrderReturnAdd(orderSnMain, order.getOrderId(), order.getBuyerId(), 
//							order.getSellerId(), returnSum, 1, 1);
//					if(returnAmountVcount > 0) {//虚拟帐户原额退
//						OrderPayR  orderPayR = new OrderPayR();
//						orderPayR.setOrderIdR(orderIdR);
//						orderPayR.setRepayWay(0);
//						orderPayR.setPaymentId(2);
//						orderPayR.setValue(returnAmountVcount);
//						orderPayRDao.save(orderPayR);
//						VaccountUpdateRespVO vAccountResp = VirtualAccountProcessor.getProcessor()
//								.refund(userId, order.getOrderId(), orderSnMain, returnAmountVcount, 
//								SDF.format(new Date()), 0, "取消订单退款:" + SDF.format(new Date()), order.getBuyerName());
//						
//					}
//					if(returnAmountTxk > 0) {//淘心卡原额退
//						OrderPayR orderPayR = new OrderPayR();
//						orderPayR.setOrderIdR(orderIdR);
//						orderPayR.setPaymentId(6);
//						orderPayR.setRepayWay(0);
//						orderPayR.setValue(returnAmountTxk);
//						orderPayRDao.save(orderPayR);
//						//TODO
//						TxkCardRefundRespVO txkCardResp = AccountTxkProcessor.getProcessor()
//								.refund(returnAmountTxk, orderSnMain, userId, order.getBuyerName());
//					}
//					
//					if(returnAmountCoupon > 0) {//优惠券状态改成未使用
//						if(CollectionUtils.isEmpty(couponCodes)) {
//							for(String couponCode : couponCodes) {//TODO couponCode待确定
//								couponSnDao.refundCouponSn(couponCode, userId);
//							}
//						}
//					}
//				}
//				
//				
//				
////				$payment = $this->_get_pay(order.);//支付明细
////                foreach($payment['pay'] as $v)
////                {
////                    $order_pay_r_mod->add(array("order_id_r" => $order_id_r, "repay_way" => 0, "payment_id" => $v['payment_id'], "value" => $v['money']));
////                }
//				
//			} else {
//				//全部未支付，只需改订单状态及操作记录即可
//			}
//			
//			/*更改订单状态、解冻库存*/
//			orderDao.updateOrderStatus(order.getOrderId(), 1);
//			releaseFreezStock(order.getOrderId(), order.getType(), 1);
//			
//			//order_action 只插入一条记录
//			OrderAction orderAction = new OrderAction();
//			orderAction.setAction(1);
//			orderAction.setOrderSnMain(orderSnMain);
//			orderAction.setTaoOrderSn(order.getTaoOrderSn());
//			orderAction.setOrderId(order.getOrderId());
//			orderAction.setActor(order.getBuyerName());
//			orderAction.setActionTime(new Date());
//			orderAction.setNotes("取消");
//			orderActionDao.save(orderAction);
//			
//			if(order.getPayStatus() == 0) {//0未支付 1已支付
//				resp.setCode(200);
//				resp.setMessage("订单取消成功");
////				return resp;
//			} else {
//				resp.setCode(200);
//				resp.setMessage("订单取消成功,您已支付的金额将在三个工作日内返还到您的账户中！");
////				return resp;
//			}
//		}
//	
//		return resp;
//	}
	
	/**
     *释放锁定库存
     *@param order_id 订单ID,必填 
     *@param order_type 订单类型,wei_wh(微仓订单(总仓进货)),wei_self(微仓订单(独立进货)),booking(预购订单),self_sales(商家独立销售)
     *@param operate_type 操作类型,1取消,2作废,6核验/发货
     */
    private boolean releaseFreezStock(int orderId, String orderType, int operateType) {
    	if(orderId < 1 || StringUtils.equals(orderType, "") || operateType < 1 ){
    		return false;
    	}
    	Order order = orderDao.findByOrderId(orderId);
    	List<OrderGoods> orderGoodsList = orderGoodsDao.findByOrderId(orderId);
    	if(order.getSellerId() > 0 && !CollectionUtils.isEmpty(orderGoodsList)) {
    		for(OrderGoods orderGoods : orderGoodsList) {
    			if(StringUtils.equals(orderType, OrderTypeEnums.TYPE_WEI_WH.getType()) || 
    					StringUtils.equals(orderType, OrderTypeEnums.TYPE_WEI_SELF.getType())) {//微仓订单
//    				LkWhGoodsStore whStore = lkWhGoodsStoreDao.
//							findBySpecIdAndStoreId(orderGoods.getSpecId(), order.getSellerId());
    				if(operateType == 6) {//发货
    					lkWhGoodsStoreDao.updateBySpecIdAndStoreId(orderGoods.getSpecId(), order.getSellerId(), 
    							orderGoods.getQuantity(), orderGoods.getQuantity());
    				} else {
    					lkWhGoodsStoreDao.updateBySpecIdAndStoreId(orderGoods.getSpecId(), order.getSellerId(), 
    							orderGoods.getQuantity());
    				}
    			} else {
//    				GoodsSpec goodsSpec = goodsSpecDao.findBySpecId(orderGoods.getSpecId());
    				if(operateType == 6) {//发货
    					goodsSpecDao.updateBySpecId(orderGoods.getSpecId(), orderGoods.getQuantity(), 
    							orderGoods.getQuantity());
    				} else { //取消
    					goodsSpecDao.updateBySpecId(orderGoods.getSpecId(), orderGoods.getQuantity());
    				}
    			}
    		}
    		return true;
    	}
    	return false;
    }
	
	  //退款
    private int getOrderReturnAdd(String orderSnMain, int orderId, int buyerId, int sellerId, double returnSum, int refundStatus, int returnStatus)
    {
    	OrderReturn orderReturn = new OrderReturn();
    	orderReturn.setOrderSnMain(orderSnMain);
    	orderReturn.setOrderId(orderId);
    	orderReturn.setBuyerId(buyerId);
    	orderReturn.setSellerId(sellerId);
    	orderReturn.setReturnAmount(returnSum);
    	orderReturn.setActor("system");
    	orderReturn.setAddTime(SDF.format(new Date()));
    	orderReturn.setGoodsType(0);
    	orderReturn.setOrderType(6);
    	orderReturn.setOrderStatus(0);
    	orderReturn.setGoodsStatus(1);
    	orderReturn.setRefundStatus(refundStatus);
    	orderReturn.setStatementStatus(0);
    	if(returnStatus == 1) {
    		orderReturn.setRepayTime(SDF.format(new Date()));
    	}
    	orderReturn.setPostscript("客户自已取消订单");
    	return orderRDao.save(orderReturn).getOrderIdR();
    }

	/*
	 * 订单支付明细 orderId 订单ID
	 */
	private Map<Integer, Double> getPay(int orderId) {
		List<OrderPay> orderPays = orderPayDao.findByOrderIdAndStatus(
				orderId, "succ");
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
		ShareRespDto resp = new ShareRespDto();
		if(StringUtils.isBlank(orderSnMain)) {
			resp.setCode(400);
			return resp;
		}
		StringBuilder md5time = new StringBuilder();
		md5time.append("share").append(orderSnMain).append(new Date().getTime() / 1000).append("friend");
		
		StringBuilder shareUrl = new StringBuilder();
		shareUrl.append("http://wap.loukou.com/weixin.wxact-sharefriend.html?time=").append(new Date().getTime() / 1000)
					.append("&order_id=").append(orderSnMain).append("&scode=")
					.append(DigestUtils.md5DigestAsHex(md5time.toString().getBytes()));
		ShareDto shareDto = new ShareDto();
		shareDto.setContent("楼口全场代金券来啊，速抢");
		shareDto.setIcon("");
		shareDto.setUrl(shareUrl.toString());
		
		List<Order> orders = orderDao.findByOrderSnMain(orderSnMain);
		int count = orders.size();
		
		ShareResultDto resultDto = new ShareResultDto();
		if(count > 1) {
			resultDto.setArrivalCode("");
			resultDto.setDesc("到我的订单内查看收货码进行收货哦~");
		} else if(count == 1) {
			resultDto.setArrivalCode(orders.get(0).getShippingNo());
			resultDto.setDesc("凭借收货码进行收货哦~");
		}
		
		resultDto.setImage("http://pic1.taocz.cn//201505061136566745.jpg");
		resultDto.setShare(shareDto);
		
		resp.setCode(200);
		resp.setShareResultDto(resultDto);
		return resp;
	}

	@Override
	public PayBeforeRespDto getPayInfoBeforeOrder(int userId, String openId, int cityId,
			int storeId, int couponId) {

		double couponMoney = 0.0;
		if (couponId > 0) {
			CoupList coupList = coupListDao.findOne(couponId);
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
		
		double total = 0.0;	// 订单总价（订单金额+运费-优惠）
		double discountAmount = 0.0;	// 折扣金额
		if (couponMoney > orderTotal) {
			total = shippingFee;
			discountAmount = orderTotal;
		}
		else {
			total = DoubleUtils.sub(DoubleUtils.add(orderTotal, shippingFee), couponMoney);
			discountAmount = couponMoney;
		}
		
		// 获取淘心卡
		double txkNum = virtualAccountProcessor.getVirtualBalanceByUserId(userId);
		// 获取虚账户
		double vcount = accountTxkProcessor.getTxkBalanceByUserId(userId);

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
    @Override
    public OResponseDto<OrderInfoDto> getOrderGoodsInfo(String orderNo) {
        Order order = orderDao.findByTaoOrderSn(orderNo);
        OResponseDto<OrderInfoDto> oResultDto = new OResponseDto<OrderInfoDto>();
        OrderInfoDto orderInfoDto = new OrderInfoDto();
        List<SpecDto> specList = new ArrayList<SpecDto>();
        List<OrderGoods> goods = orderGoodsDao.findByOrderId(order.getOrderId());
        for(OrderGoods good :goods){
            SpecDto spec = new SpecDto();
            spec.setGoodInfo(new GoodsInfoDto(good.getGoodsId(), good.getGoodsName(), good.getGoodsImage()));
            specList.add(spec);
        }
        //实际上一个主单只有一个收货人
        List<OrderExtm> orderExtmList = orderExtmDao.findByOrderSnMain(order.getOrderSnMain());
        OrderExtm orderExtm = orderExtmList.get(0);
        
        //封装收货人等信息
        DeliveryInfo deliveryInfo =  new DeliveryInfo();
        deliveryInfo.setAddress(orderExtm.getRegionName()+orderExtm.getAddress());
        deliveryInfo.setConsignee(orderExtm.getConsignee());
        deliveryInfo.setTel(orderExtm.getPhoneMob());
        
        orderInfoDto.setCreateTime(SDF.format(new Date((long)(order.getAddTime())*1000)));
        orderInfoDto.setGoodsAmount(order.getOrderAmount());
        orderInfoDto.setTaoOrderSn(order.getTaoOrderSn());
        orderInfoDto.setOrderStatus(order.getStatus());
        
       
        orderInfoDto.setSpecList(specList);
        orderInfoDto.setDeliveryInfo(deliveryInfo);
        oResultDto.setCode(200);
        oResultDto.setResult(orderInfoDto);
        return oResultDto;
    }

    @Override
    public OResponseDto<OrderListInfoDto> getOrderListInfo(OrderListParamDto param) {
        PageRequest pagenation = new PageRequest(param.getPageNum(), param.getPageSize());
        Page<Order> orders  =orderDao.findBySellerId(param.getStoreId(), pagenation);
        OrderListInfoDto orderListInfoDto = new OrderListInfoDto();
        List<OrderInfoDto> orderInfoDtos = new ArrayList<OrderInfoDto>();
        for(Order order :orders.getContent()){
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setCreateTime(SDF.format(new Date((long)(order.getAddTime())*1000)));
            orderInfoDto.setGoodsAmount(order.getOrderAmount());
            orderInfoDto.setTaoOrderSn(order.getTaoOrderSn());
            orderInfoDto.setOrderStatus(order.getStatus());
            orderInfoDtos.add(orderInfoDto);
        }
        orderListInfoDto.setOrders(orderInfoDtos);
        orderListInfoDto.setStoreId(param.getStoreId());
        orderListInfoDto.setTotalNum(orders.getTotalElements());
        return new OResponseDto<OrderListInfoDto>(200,orderListInfoDto);
    }

    @Override
    public OResponseDto<String> finishPackagingOrder(String orderSnMain) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OResponseDto<String> refuseOrder(String orderSnMain) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OResponseDto<String> confirmRevieveOrder(String orderSnMain, String Gps) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OResponseDto<String> confirmBookOrder(String orderSnMain) {
        // TODO Auto-generated method stub
        return null;
    }

	
	

}
