package com.loukou.order.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.loukou.order.service.api.OrderService;
import com.loukou.order.service.dao.ExpressDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderExtmDao;
import com.loukou.order.service.dao.OrderGoodsDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.dao.StoreDao;
import com.loukou.order.service.entity.Express;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderAction;
import com.loukou.order.service.entity.OrderExtm;
import com.loukou.order.service.entity.OrderGoods;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.entity.Store;
import com.loukou.order.service.resp.dto.ExtmMsgDto;
import com.loukou.order.service.resp.dto.GoodsListDto;
import com.loukou.order.service.resp.dto.OrderListBaseDto;
import com.loukou.order.service.resp.dto.OrderListDto;
import com.loukou.order.service.resp.dto.OrderListRespDto;
import com.loukou.order.service.resp.dto.OrderListResultDto;
import com.loukou.order.service.resp.dto.ResultDto;
import com.loukou.order.service.resp.dto.ShippingListDto;
import com.loukou.order.service.resp.dto.ShippingMsgDto;
import com.loukou.order.service.resp.dto.ShippingResultDto;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("###,###.##");
	

	@Autowired OrderDao orderDao;
	
	@Autowired OrderReturnDao orderRDao;
	
	@Autowired StoreDao storeDao;
	
	@Autowired OrderPayDao orderPayDao;
	
	@Autowired OrderGoodsDao orderGoodsDao;
	
	@Autowired ExpressDao expressDao;//快递公司代码及名称
	
	@Autowired OrderActionDao orderActionDao;
	
	@Autowired OrderExtmDao orderExtmDao;
	
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
			List<OrderReturn> orderReturns = orderRDao.findByUserIdAndOrderStatus(userId, 0);
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
	public void getCouponList(int cityId, int userId, int storeId, int openId) {
		
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
//		int payId = 0;
		int pStatus = 0;
		
//		int ishd = 1;//不是活动
		//是否可付款 立即付款按钮的显示
		for(Order order : ordersList) {
			if(order.getStatus() == 1) {
				orderPayStatus = 1;//订单初始状态
			}
			if(order.getPayType() != 1) {
				pType = 1;//非货到付款
			}
//			if(order.getPayId() == 0) {
//				payId = 1;//未选支付方式
//			}
			if(order.getPayStatus() != 1) {
				pStatus = 1;//非已支付
			}
		}
		
		if(orderPayStatus == 1 && pType == 1 && pStatus ==1) {
			payStatus = 0;//需要支付
		}
		
		String source = "";
		for(Order order : ordersList) {
			//0网站1移动公司2电视终端3集团用户下单4移动礼盒5活动6中奖 8快餐 9快餐代购 10优惠券活动 11.铁通年终回馈 12 自提点代下单 20=iphone 21=android 100=外站数据 25苹果 26 大宗采购 27社区点 30 iphone
			switch (order.getSource()) {
				case 0:
					source="网站";
					break;

				case 1:
					source="移动公司";
					break;

				case 2:
					source="电视终端";
					break;

				case 3:
					source="集团用户下单";
					break;

				case 4:
					source="移动礼盒";
					break;

				case 5:
					source="活动";
					break;

				case 6:
					source="中奖";
					break;

				case 8:
					source="快餐";
					break;

				case 9:
					source="快餐代购";
					break;

				case 10:
					source="优惠券活动";
					break;

				case 11:
					source="铁通年终回馈";
					break;

				case 12:
					source="自提点代下单";
					break;

				case 20:
					source="手机活动";
					break;

				case 21:
					source="手机";
					break;

				case 100:
					source="外站数据";
					break;

				case 25:
					source="苹果";
					break;

				case 26:
					source="大宗采购";
					break;

				case 27:
					source="社区点";
					break;

				case 30:
					source="手机";
					break;
				
				default:
					source="网站";
					break;
			}
			//0 小黄蜂配送，全支持；1 商家自送，全支持；2 商家自送，不支持货到付款；3 商家自送，不支持在线支付；
			//4 独立算运费小黄蜂配送，全支持；5 签约小黄蜂，不支持货到付款；'
			int freight = storeDao.findOne(order.getSellerId()).getFreight();
            String shippingtype = "淘常州自送";
		
			if(freight != 0) {
				freight = 1;
				shippingtype = "第三方配送";
			}
			
//			StringBuilder shippingmsg = new StringBuilder();
//			if(order.getType() == "booking") {
//				shippingmsg.append("预售商品").append(order.getNeedShiptime()).append(" ").append(order.getNeedShiptimeSlot()).append("、");
//			} else if(StringUtils.equals(order.getType(), "wei_wh") || StringUtils.equals(order.getType(), "wei_self")) {
//				shippingmsg.append("普通商品").append(order.getNeedShiptime()).append(" ").append(order.getNeedShiptimeSlot()).append("、");
//			} else if(StringUtils.equals(order.getType(), "self_sales")) {
//				shippingmsg.append("配送单预计1-3日内送达  、");
//			}
//			
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
	private ShippingResultDto getShippingResult(ShippingResultDto shippingResultDto, String taoOrderSn) {
		
		ResultDto resultDto = new ResultDto();
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
    	if(flag == 4) {
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
		switch (status){
		case 0: return "未审核";
		case 1: return "已取消";
		case 2: return "无效";
		case 3: return "已审核";
		case 5: return "已提货";
		case 6: return "已拣货";
		case 8: return "打包";
		case 13: return "已发货";
		case 14: return "已发货";
		case 15: return "已收货";
		case 16: return "拒收";
		default: return "";
		}	
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

	
	
	
	
	
	

}
