package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.loukou.order.service.constants.CouponFormType;
import com.loukou.order.service.constants.CouponType;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.CoupLogDao;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.CoupTypeDao;
import com.loukou.order.service.dao.GCategoryNewDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupLog;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.CoupType;
import com.loukou.order.service.entity.GCategoryNew;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.enums.ActivateCouponMessage;
import com.loukou.order.service.enums.CoupListReqTypeEnum;
import com.loukou.order.service.enums.OrderStatusEnum;
import com.loukou.order.service.resp.dto.CouponListDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.CouponListResultDto;
import com.loukou.order.service.resp.dto.OResponseDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.search.service.api.GoodsSearchService;
import com.loukou.search.service.dto.GoodsCateDto;
import com.serverstarted.cart.service.api.CartService;
import com.serverstarted.cart.service.resp.dto.CartGoodsRespDto;
import com.serverstarted.cart.service.resp.dto.CartRespDto;
import com.serverstarted.cart.service.resp.dto.PackageRespDto;

@Service
public class CouponOperationProcessor {

	@Autowired 
	private CoupListService coupListService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CoupRuleDao coupRuleDao;
	
	@Autowired
	private CoupListDao coupListDao;
	
	@Autowired
	private CoupLogDao coupLogDao;
	
	@Autowired
	private CoupTypeDao coupTypeDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired 
	private GoodsSearchService goodsSearchService;
	
	@Autowired
	private GCategoryNewDao gCategoryNewDao;

	private static final int LIMIT_COUPON_PER_DAY = 20; // 每天限用优惠券张数
	
	public CouponListRespDto getCouponList(int cityId, int userId, int storeId,
			String openId, int type) {
		CouponListRespDto resp = new CouponListRespDto(200, "");
		if (userId <= 0
				|| StringUtils.isEmpty(openId)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		// 可用优惠券，必须有storeId和cityId
		if (type == CoupListReqTypeEnum.USABLE.getId() && 
				(cityId <= 0 || storeId <= 0)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		
		// 查询语句
		List<CoupList> coupLists = coupListDao.getValidCoupLists(userId);
		List<CoupList> invalidCoupLists = null;
		if(type == CoupListReqTypeEnum.ALL.getId()) {
			invalidCoupLists = coupListDao.getInvalidCoupLists(userId);// 以及其他的一些过滤条件
			if(CollectionUtils.isEmpty(coupLists) && CollectionUtils.isEmpty(invalidCoupLists)) {
				resp.setCode(200);
				return resp;
			}
		} else {
			if (coupLists.size() == 0) {
				resp.setCode(200);
				return resp;
			}
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
		
		CouponListResultDto result = resp.getResult();
		
		if(type == CoupListReqTypeEnum.USABLE.getId()) {
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
					couponListDto.setIsUsable(CoupListReqTypeEnum.USABLE.getId());
					couponListDto.setCouponRange(generateCouponRange(coupRule));
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

		} else {
			List<CouponListDto> couponListDtos = result.getCouponList();
			if(CollectionUtils.isNotEmpty(coupLists)) {
				for(CoupList coupList : coupLists) {
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
					couponListDto.setIsUsable(CoupListReqTypeEnum.USABLE.getId());
					couponListDto.setCouponRange(generateCouponRange(coupRule));
					couponListDtos.add(couponListDto);
				}
			}
			
			if(CollectionUtils.isNotEmpty(invalidCoupLists)) {
				for(CoupList coupList : invalidCoupLists) {
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
					couponListDto.setIsUsable(CoupListReqTypeEnum.ALL.getId());
					couponListDto.setCouponRange(generateCouponRange(coupRule));
					couponListDtos.add(couponListDto);
				}
			}
			
		}
	
		return resp;
	}
	
	private String generateCouponRange(CoupRule coupRule) {
		StringBuilder result = new StringBuilder();
		if (coupRule.getCouponType() == CouponType.CATE) {
			List<GCategoryNew> gCateNews = null;
			List<Integer> cateIds = getOutId(coupRule);
			if (CollectionUtils.isNotEmpty(cateIds)) {
				gCateNews = gCategoryNewDao.findByCateIdIn(cateIds);
			}
			if (CollectionUtils.isNotEmpty(gCateNews)) {
				result.append("限");
				for (GCategoryNew g : gCateNews) {
					result.append(g.getCateName()).append("、");
				}
				
				result.append("品类使用。");
			}
			
		}
		String resultStr = result.toString();
		StringUtils.removeEnd(resultStr, "、");
		return resultStr;
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
		else if (coupRule.getCouponType() == CouponType.CATE) {
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
	
	private List<Integer> getOutId(CoupRule coupRule) {
		List<Integer> ids = Lists.newArrayList();
		String[] strs = coupRule.getOutId().split(",");
		if (strs.length > 0) {
			for (String id: strs) {
				ids.add(Integer.valueOf(id));
			}
		}
		return ids;
	}

	public OResponseDto<String> activateCoupon(int userId, String openId,
			String commoncode) {
		 OResponseDto<String> resp = new OResponseDto<String>(200, "");
		 
		 if(userId <= 0 || StringUtils.isBlank(openId) || StringUtils.isBlank(commoncode)) {
			 resp.setCode(400);
			 resp.setResult("参数有误");
			 return resp;
		 }
		Member user = memberDao.findOne(userId);
		if(user == null) {
			resp.setCode(400);
			resp.setResult("用户不存在");
			return resp;
		}
		
		if(user.getPhoneChecked() != 1) {
			resp.setCode(400);
			resp.setResult("用户未绑定手机,请先绑定手机号");
			return resp;
		}
		
		// 激活券码
        // 需要判断券的时间，互斥量，公用券发券等
		String prefix = StringUtils.substring(commoncode, 0, 2);
		int checkCode = 0;
		CheckCouponDto dto = new CheckCouponDto();
		
		if(StringUtils.equals(prefix, "LK")) { // 公用券码
			dto = checkCoupon(commoncode, CouponFormType.PUBLIC, userId, openId);
			checkCode = dto.getResult();
		} else {
			dto = checkCoupon(commoncode, CouponFormType.PRIVATE, userId, openId);
			checkCode = dto.getResult();
		}
		if(checkCode != ActivateCouponMessage.SUCCESS.getCode()) {
			String message = ActivateCouponMessage.parseCode(checkCode).getMessage();
			resp.setCode(400);
			resp.setResult(message);
			return resp;
		}
		
		if(StringUtils.equals(prefix, "LK")) {
			// 领取公用券码
			boolean createStatus = createCouponCode(userId, dto.getCouponId(), 0, false, 0, openId, 0);
			
			if(createStatus == false) {
				resp.setCode(400);
				resp.setResult("激活失败");
				return resp;
			}
			
		} else {
			 //激活券码
			CoupRule coupRule = coupRuleDao.findOne(dto.getCouponId());
			if(coupRule != null) {
				int canUseType = coupRule.getCanusetype();
				Date beginTime = null;
		    	Date endTime = null;
		    	Date date = new Date();
		    	if(canUseType == 1) {
		    		beginTime = DateUtils.getStartofDate(date);
		    		endTime = DateUtils.getDateAfter(date, coupRule.getCanuseday());
		    		endTime = DateUtils.getEndofDate(endTime);
		    	} else {
		    		beginTime = coupRule.getBegintime();
		    		endTime = coupRule.getEndtime();
		    	}
		    	
		    	int code = coupListDao.update(userId, beginTime, endTime, openId, coupRule.getCommoncode());
		    	if(code > 0) {
		    		resp.setCode(200);
		    		resp.setResult("激活成功");
					return resp;
		    	}
			}
			
		}
		 // 记录领券LOG
        // ****************?????********************** //
//        $this->json_result('恭喜！激活成功');
		return resp;
	}
	
	
	/**
     * 发放优惠券
     *
     * @param  [int]      $user_id   [用户ID]
     * @param  [string]   $code      [公用券码]
     * @param  [bol]      $type      [true: 私有券(default)， false: 公用券]
     * @param  [bol]      $check     [是否检测可领取(default: false)]
     * @param  [bol]      $num       [检测会员可以领取的张数] 0为不检测领取数量
	 * @param  [int]      $money     [自定义优惠券金额]
     * 
     * @return [type]     [description]
     */
    public boolean createCouponCode(int userId, int couponId, int type, boolean check, 
    		int num, String openId, double money) {

    	CoupRule coupRule = coupRuleDao.findOne(couponId);
    	
    	if(coupRule == null) {
    		recordLog(userId, couponId, "[检查结果] 找不到正确的券", 1, openId);
    		return false;
    	} 
    	
    	CheckCouponDto dto = checkCoupon(coupRule.getCommoncode(), type, userId, openId);
    	int checkCode = dto.getResult();
    	if(checkCode != ActivateCouponMessage.SUCCESS.getCode()) {
    		recordLog(userId, couponId, "[检查结果]".concat(String.valueOf(checkCode)), checkCode, openId);
    		return false;
    	}
    	
    	// 检测会员的领取数据量
    	if(num > 0) {
    		boolean checkCouponNum = checkCouponIdNum(couponId, userId, openId, num, CouponFormType.PUBLIC);
    		if(checkCouponNum == false) {
    			recordLog(userId, couponId, "[检查结果]  (超出个人最大领取量 : ".concat(String.valueOf(num))
    					.concat(" ，无法领取了)"), 11, openId);
    			checkCode = 11;// c超出领取最大量（个人）
    			return false;
    		}
    		
    		if(StringUtils.isNotBlank(openId)) {
    			checkCouponNum = checkCouponIdNum(couponId, userId, openId, num, CouponFormType.PRIVATE);
    			if(checkCouponNum == false) {
    				recordLog(userId, couponId, "[检查结果]  (超出设备最大领取量 : ".concat(String.valueOf(num))
    						.concat(" ，无法领取了)"), 12, openId);
    				checkCode = 12; // c超出领取最大量（设备）
    				return false;
    			}
    		}  
    	}
    	
    	int canUseType = coupRule.getCanusetype();//有效期类型。0:开始begintime/结束endtime时间;1:有效期n天canuseday
    	Date beginTime = null;
    	Date endTime = null;
    	Date date = new Date();
    	if(canUseType == 1) {
    		beginTime = DateUtils.getStartofDate(date);
    		endTime = DateUtils.getDateAfter(date, coupRule.getCanuseday());
    		endTime = DateUtils.getEndofDate(endTime);
    	} else {
    		beginTime = coupRule.getBegintime();
    		endTime = coupRule.getEndtime();
    	}
    	double totalMoney = 0;
    	
    	if(money > 0) {
    		totalMoney = money;
    	} else {
    		totalMoney = coupRule.getMoney();
    	}
    	
    	String code = null;
    	if(type == 1) {//私有券
    		String pre = coupRule.getPrefix();
    		String mtime = StringUtils.substring(String.valueOf((long)new Date().getTime()), 7, 13);
    		//随机生成券号
    		String rcode = String.valueOf(Math.random() * (99999 - 10000) + 10000);
    		code = mkCode(pre, mtime, rcode);
    	} else {//公有券
    		// 公用券领取，生成规则是当前公用券领取的次数code0001
    		String replaceCode = coupRule.getCommoncode().concat("N");
    		Integer coupCodeNum = coupListDao.findByCouponId(couponId, replaceCode);
    		if(coupCodeNum != null) {
    			code = coupRule.getCommoncode().concat("N").concat(String.valueOf(coupCodeNum + 1));
    		} else {
    			code = coupRule.getCommoncode().concat("N1");
    		}
    	}
    	
    	CoupList coupL = new CoupList();
    	
    	coupL.setCouponId(couponId);
    	coupL.setUserId(userId);
    	coupL.setBegintime(beginTime);
    	coupL.setEndtime(endTime);
    	coupL.setCommoncode(code);
    	coupL.setMoney(totalMoney);
    	coupL.setMinprice(coupRule.getLowemoney());
    	coupL.setIssue(coupRule.getIssue());
    	coupL.setCreatetime(new Date());
    	coupL.setSellSite(coupRule.getSellSite());
    	coupL.setOpenid(openId);
    	coupListDao.save(coupL);
    	
    	int coupNum = coupRule.getNum() + 1;//优惠券总张数
    	int sumResiduenum = coupRule.getResiduenum() + 1;//优惠券未使用张数
    	
    	coupRuleDao.update(couponId, coupNum, sumResiduenum);
    	recordLog(userId, couponId, "[检查结果]  领取成功", 1, openId);
		return true;
    }
    
    private void recordLog(int userId, int coupId, String msg, int res, String openId) {

    	CoupLog coupLog = new CoupLog();
    	coupLog.setUserId(userId);
    	coupLog.setCoupId(coupId);
    	coupLog.setOpenId(openId);
    	coupLog.setMsg(msg);
    	coupLog.setResult(res);
    	coupLog.setActiveTime(new Date());
    	
    	coupLogDao.save(coupLog);
    }
    
    /**
     * 生成随机券码
     */
    private String mkCode(String pre, String mtime, String rcode){
    	
    	String code = pre.concat(mtime).concat(rcode);
    	CoupList coupList = coupListDao.findByCommoncode(code);
    	if(coupList == null) {
    		String mtime2 = StringUtils.substring(String.valueOf((long)new Date().getTime()), 7, 13);
    		//随机生成券号
    		String rcode2 = String.valueOf(Math.random() * (99999 - 10000) + 10000);
    		code = mkCode(pre, mtime2, rcode2);
    	}
    	return code;
    }
    
    /**
     * 检测会员可领取的优惠券数量
     *
     * @param  [int]     $code    [优惠券ID]
     * @param  [int]     $userId   [用户ID]
     * @param  [String]     $openId   [设备ID]
     * @param  [string]     $num     [用户得到的最大优惠券数量]
	 * @param  [string]     $type	 [1: 用户， 2: 设备]
     * 
     * @return [int]                 [检测结果标识]
     * 
     * @author laphi
     * @since  2015-08-14
     */
    private boolean checkCouponIdNum(int couponId, int userId, String openId, int num, int type) {
    	
    	List<CoupList> coupList = null;
        if(type == 1) {
        	coupList = coupListDao.findByUserIdAndCouponId(userId, couponId);
        }else{
        	coupList = coupListDao.findByCouponIdAndOpenid(couponId, openId);
        }
		int codeNum = CollectionUtils.size(coupList);
		
		if(codeNum >= num || codeNum <=0) {
			return false;
		}
		return true;
	}
	
	/**
     * 检测用户填写的优惠券是否有效，并获取一些优惠券相关的数据，如新老用户标识，类型，互斥量等
     *
     * @param  [string]     $code    [优惠券ID]
     * @param  [int]        $type    [优惠券类型： 1：公用券， 2， 私用券]
     * @param  [string]     $user_id [用户ID]
     * @param  [string]     $openid  [设备ID，检测设备互斥]
     * 
     * @return [int]                 [检测结果标识]
     */
    private CheckCouponDto checkCoupon(String commoncode, int type, int userId, String openId) {
    	CheckCouponDto dto = new CheckCouponDto();
    	int couponId = 0;
    	Date listEndTime = null;
        if(type == CouponFormType.PUBLIC) {

        	CoupRule coupRule = coupRuleDao.findByCommoncode(commoncode);
        	if(coupRule == null) {
        		dto.setResult(ActivateCouponMessage.ERROR_COMMONCODE.getCode());
//        		dto.setCouponId(coupRule.getId());
        		return dto;
        	}
        	// 赋值优惠券规则ID
        	couponId = coupRule.getId();
        	List<CoupList> coupList = coupListDao.findByCouponId(couponId);
        	 // 判断最大发放量
        	int alreadySend = CollectionUtils.size(coupList);
        	
        	if(coupRule.getMaxnum() > 0 && coupRule.getMaxnum() <= alreadySend) {
        		dto.setResult(ActivateCouponMessage.FINISHED.getCode());
        		dto.setCouponId(coupRule.getId());
        		return dto;
        	}
        	

        } else {
        	
        	CoupList coupList = coupListDao.findByCommoncode(commoncode);
        	
        	if(coupList == null) {
        		dto.setResult(ActivateCouponMessage.ERROR_COMMONCODE.getCode());
//        		dto.setCouponId(coupList.getCouponId());
        		return dto;
        	}
        	listEndTime = coupList.getEndtime();
        	// 赋值优惠券规则ID
        	couponId = coupList.getCouponId();
        	if(coupList.getUserId() <= 0) {
        		dto.setResult(ActivateCouponMessage.USED.getCode());
        		dto.setCouponId(couponId);
        		return dto;
        	}
        	
        	if(coupList.getIssue() != 1) {
        		dto.setResult(ActivateCouponMessage.INVALID.getCode());
        		dto.setCouponId(couponId);
        		return dto;
        	}
        }
        
        CoupType coupType = null;
        
        CoupRule coupRule = coupRuleDao.findOne(couponId);
        
        if(coupRule != null) {
        	// 判断可领取的天数
        	if(coupRule.getCanusetype() == 1) {
        		if(coupRule.getCanuseday() <= 0) {
        			dto.setResult(ActivateCouponMessage.OVER_DUE.getCode());
        			dto.setCouponId(couponId);
        			return dto;
        		}
        	} else {
        		// 判断是否过期
        		if(coupRule.getCanusetype() == 0 && coupRule.getEndtime().getTime() < new Date().getTime()) {
            		dto.setResult(ActivateCouponMessage.OVER_DUE.getCode());
            		dto.setCouponId(couponId);
            		return dto;
            	}
        		
        		if(listEndTime != null && listEndTime.getTime() < new Date().getTime()) {
        			dto.setResult(ActivateCouponMessage.OVER_DUE.getCode());
        			dto.setCouponId(couponId);
        			return dto;
        		}
        	}
        	
        	if(coupRule.getIssue() != 1) { // 停用或者未启用
        		dto.setResult(ActivateCouponMessage.INVALID.getCode());
        		dto.setCouponId(couponId);
        		return dto;
        	}
        	
        	coupType = coupTypeDao.findOne(coupRule.getTypeid());
        	if(coupType == null) {
        		dto.setResult(999);// 无法查找父节点，数据异常
        		dto.setCouponId(couponId);
        		return dto;
        	} 
        	
        	// 该类型的券逻辑删除
        	if(coupType.getStatus() == 1) {
        		dto.setResult(ActivateCouponMessage.INVALID.getCode());
        		dto.setCouponId(couponId);
        		return dto;
        	}
        } else {
        	dto.setResult(999);// 无法查找父节点，数据异常
    		dto.setCouponId(couponId);
    		return dto;
        }

        // 继续检查
        // 上面检测优惠券通过， 继续检测（新老用户，互斥量等信息）
        // 1: 新用户的优惠券，检测只有新用户能领
        
        if(coupType.getNewuser() == 1 && !checkUserNew(userId)) {
        	dto.setResult(ActivateCouponMessage.NEW_USER_ONLY.getCode());
        	dto.setCouponId(couponId);
        	return dto;
        }

        // 2: 老用户的优惠券，检测只有老用户能领
        if(coupType.getNewuser() == 2 && checkUserNew(userId)) {
            dto.setResult(ActivateCouponMessage.OLD_USER_ONLY.getCode());
            dto.setCouponId(couponId);
            return dto;
        }

        // 如果存在互斥量限制则，检测互斥量
        if(coupType.getUsenum() > 0 && !checkUserCoupon(userId, coupType.getId(), coupType.getUsenum())) {
        	dto.setResult(ActivateCouponMessage.MUTEX.getCode());
        	dto.setCouponId(couponId);
        	return dto;
        }

        // 如果存在互斥量限制则，检测互斥量
        if(StringUtils.isNotBlank(openId)) {
            if(coupType.getUsenum() > 0 && !checkDeviceCoupon(openId, coupType.getId(), coupType.getUsenum())){
                dto.setResult(ActivateCouponMessage.MACHINE_USED.getCode());
                dto.setCouponId(couponId);
                return dto;
            }
        }

        dto.setResult(ActivateCouponMessage.SUCCESS.getCode()); // COUPON_ENABLE   
        dto.setCouponId(couponId);
        return dto;
    }
    
    /**
     * [检查设备互斥量]
     *
     * @return [type]     [description]
     */
    private boolean checkDeviceCoupon(String openId, int typeId, int usenum) {
    	
    	List<CoupList> coupLists = coupListDao.findByOpenid(openId);
    	
    	if(CollectionUtils.isEmpty(coupLists)) {
    		return true;
    	}
    	
    	int count = 0;
    	
    	CoupType coupType = coupTypeDao.findOne(typeId);
    	
    	if(coupType == null) {
    		return true;
    	}
    	
    	for(CoupList coupList : coupLists) {
    		CoupRule cr = coupRuleDao.findOne(coupList.getCouponId());
    		if(cr == null) {
    			continue;
    		}
    		if(cr.getTypeid() != typeId) {
    			continue;
    		}
    		count++;
    	}
    	
    	if(count == usenum) {
    		return false;
    	}

        return true;
    }
    
   /** 
    * 检测用户领券的条件是否满足, 互斥限制检测
    *
    * @param  [int]     $user_id [用户ID]
    * @return [int]              [检测结果集]
    */
   private boolean checkUserCoupon(int userId, int typeId, int usenum) {
	   
	   List<CoupList> coupLists = coupListDao.findByUserId(userId);
	   
	   if(CollectionUtils.isEmpty(coupLists)) {
		   return true;
	   }
	   
	   int count = 0;
	   for(CoupList c : coupLists) {
		   CoupRule coupRule = coupRuleDao.findOne(c.getCouponId());
		   if(coupRule == null) {
			   continue;
		   }
		   if(coupRule.getTypeid() != typeId) {
			   continue;
		   }
		   
		   CoupType coupType = coupTypeDao.findOne(coupRule.getTypeid());
		   if(coupType == null) {
			  continue;
	       }
		   count++;
	   }
	   
       if(count == usenum) {
    	   return false;
       }

       return true;
      
   }
    
    /**
     * 检测用户是否是新用户
     *
     * @param  [int]     $user_id [用户ID]
     * @return [bol]              [true: 新用户， false: 老用户]
     */
    private boolean checkUserNew(int userId) {
    	
    	List<Integer> statusList = new ArrayList<Integer>();
    	statusList.add(OrderStatusEnum.STATUS_CANCELED.getId());
    	statusList.add(OrderStatusEnum.STATUS_INVALID.getId());
    	List<Order> orders = orderDao.findByBuyerIdAndStatusNotIn(userId, statusList);
    	
    	if(CollectionUtils.isEmpty(orders)) {
    		return true;
    	}
    	
        return false;

    }

}

class CheckCouponDto {
	private int result = 0;
	private int couponId = 0;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}	
}


