package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupLog;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.CoupType;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.enums.ActivateCouponMessage;
import com.loukou.order.service.enums.CoupListReqTypeEnum;
import com.loukou.order.service.resp.dto.CouponListDto;
import com.loukou.order.service.resp.dto.CouponListRespDto;
import com.loukou.order.service.resp.dto.CouponListResultDto;
import com.loukou.order.service.resp.dto.ResponseDto;
import com.loukou.order.service.util.DateUtils;
import com.loukou.order.service.util.DoubleUtils;
import com.loukou.search.service.api.ProductSearchService;
import com.loukou.search.service.dto.product.CategoryDto;
import com.serverstarted.cart.service.api.CartService;
import com.serverstarted.cart.service.resp.dto.CartGoodsRespDto;
import com.serverstarted.cart.service.resp.dto.CartRespDto;
import com.serverstarted.cart.service.resp.dto.PackageRespDto;
import com.serverstarted.product.service.api.CategoryService;
import com.serverstarted.product.service.resp.dto.CategoryRespDto;

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
	private ProductSearchService productSearchService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private InviteOperationProcessor inviteOperationProcessor;

	private static final int LIMIT_COUPON_PER_DAY = 2; // 每天限用优惠券张数
	
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
		List<CoupList> invalidCoupLists = Lists.newArrayList();
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
		for (CoupList couplist : invalidCoupLists) {
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
				//  校验优惠券是否可用
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
					CoupRule coupRule = ruleMap.get(coupList.getCouponId());
					CouponListDto couponListDto = assembleDto(coupList, coupRule, 1);
					couponListDtos.add(couponListDto);
					
					if (coupList == recommendCoupList) {
						result.getRecommend().add(couponListDto);
					}
				}
			}
			
			// 能否使用券
			int canUse = 1;
			if (isCouponUseOverLimit(userId)) {
				canUse = 0;
			}
			
			result.setCanUse(canUse);
			result.setEverydayNum(String.valueOf(LIMIT_COUPON_PER_DAY));
			result.setEverydayMsg(String.format("每天限使用%d张优惠券，明天再来吧",
					LIMIT_COUPON_PER_DAY));

		} else {
			List<CouponListDto> couponListDtos = result.getCouponList();
			if(CollectionUtils.isNotEmpty(coupLists)) {
				for(CoupList coupList : coupLists) {
					CoupRule coupRule = ruleMap.get(coupList.getCouponId());
					CouponListDto couponListDto = assembleDto(coupList, coupRule, 1);
					couponListDtos.add(couponListDto);
				}
			}
			
			if(CollectionUtils.isNotEmpty(invalidCoupLists)) {
				for(CoupList coupList : invalidCoupLists) {
					CoupRule coupRule = ruleMap.get(coupList.getCouponId());
					CouponListDto couponListDto = assembleDto(coupList, coupRule, 0);
					couponListDtos.add(couponListDto);
				}
			}
		}
	
		return resp;
	}
	
	/**
	 * 用户每天使用优惠券数是否超过限制
	 * @param userId
	 * @return
	 */
	public boolean isCouponUseOverLimit(int userId) {
		Date now = new Date();
		Date start = DateUtils.getStartofDate(now);
		int count = coupListDao.getUsedCoupNumber(userId, start);
		if (count >= LIMIT_COUPON_PER_DAY) {
			// 一天最多只能用2张券
			return true;
		}
		
		return false;
	}
	
	public CouponListDto getCouponListDtoByUseCouponNo(String useCouponNo)
	{
		CouponListDto dto = new CouponListDto();
		if(StringUtils.isNotBlank(useCouponNo))
		{
			CoupList coupList = coupListDao.findByCommoncode(useCouponNo);
			if(coupList != null)
			{
				CoupRule coupRule = coupRuleDao.findOne(coupList.getCouponId());
				if(coupRule != null)
				{
					dto = assembleDto(coupList, coupRule, 1);
					dto.setCouponName(dto.getCouponName()+"  -"+dto.getMoney());
				}
				else
				{
					dto.setCouponId(coupList.getId());
					dto.setCommoncode(coupList.getCommoncode());
					dto.setCouponName("  -"+dto.getMoney());
					dto.setMoney(coupList.getMoney());
					dto.setStarttime(DateUtils.date2DateStr(coupList.getBegintime()));
					dto.setEndtime(DateUtils.date2DateStr(coupList.getEndtime()));
				}
			}
		}
		return dto;
	}
	
	public CouponListDto assembleDto(CoupList coupList, CoupRule coupRule, int isUsable) {
		String couponName = "";
		if(coupRule == null)
			return new CouponListDto();
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
		couponListDto.setStarttime(DateUtils.date2DateStr(coupList.getBegintime()));
		couponListDto.setEndtime(DateUtils.date2DateStr(coupList.getEndtime()));
		couponListDto.setIsUsable(isUsable);
		couponListDto.setCouponRange(generateCouponRange(coupRule));
		
		return couponListDto;
	}
	
	public String generateCouponRange(CoupRule coupRule) {
		StringBuilder result = new StringBuilder();;
		String resultStr = "";
		StringBuilder returnResultStr = new StringBuilder();
		if (coupRule.getCouponType() == CouponType.CATE) {
			List<CategoryRespDto> categories = null;
			List<Integer> cateIds = getOutId(coupRule);
			if (CollectionUtils.isNotEmpty(cateIds)) {
				categories = categoryService.getCategoryByIds(cateIds).getResult();
			}
			if (CollectionUtils.isNotEmpty(categories)) {
				result.append("限");
				for (CategoryRespDto c: categories) {
					result.append(c.getName()).append("、");
				}
				resultStr = result.toString();
				returnResultStr.append(StringUtils.removeEnd(resultStr, "、"));
				returnResultStr.append("品类使用。");
			}
			
		}
		
		return returnResultStr.toString();
	}
	
	public boolean verifyCoup(int userId, String openId, int cityId,
			int storeId, CoupList coupList, CartRespDto cart, CoupRule coupRule) {
		if (coupList == null || coupRule == null || cart == null) {
			return false;
		}
		
		double goodsPrice = cart.getTotalPrice();
		// 商品金额需要扣除代买服务的类目，代买服务二级类目cate_id = 75559，75563
		for (PackageRespDto p: cart.getPackageList()) {
			for (CartGoodsRespDto g: p.getGoodsList()) {
				if (g.getNewCateIdTwo() == 75559 || g.getNewCateIdTwo() == 75563) {
					goodsPrice = DoubleUtils.sub(goodsPrice, DoubleUtils.mul(g.getPrice(), g.getAmount()));
				}
			}
		}

		if (goodsPrice < coupList.getMinprice()) {
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
			List<CategoryDto> cateOnes = productSearchService.querySubCateList(cityId, storeId, 0);
			Set<Integer> cateOneIds = Sets.newHashSet();
			for (CategoryDto g: cateOnes) {
				cateOneIds.add(g.getCateId());
			}
			
			Set<Integer> validsCateIds = Sets.newHashSet();	// 分类券所有的二级分类
			for (Integer c: cateIds) {
				if (cateOneIds.contains(c)) {
					// 一级分类获取二级分类
					List<CategoryDto> cateTwos = productSearchService.querySubCateList(cityId, storeId, c);
					for (CategoryDto g: cateTwos) {
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

	public ResponseDto<String> activateCoupon(int userId, String openId,
			String commoncode) {
		 ResponseDto<String> resp = new ResponseDto<String>(200, "");
		 
		 if(userId <= 0 || StringUtils.isBlank(openId) || StringUtils.isBlank(commoncode)) {
			 resp.setCode(400);
			 resp.setMessage("请输入正确的优惠券/激活码");
			 return resp;
		 }
		Member user = memberDao.findOne(userId);
		if(user == null) {
			resp.setCode(400);
			resp.setMessage("用户不存在");
			return resp;
		}
		
		if(user.getPhoneChecked() != 1) {
			resp.setCode(400);
			resp.setMessage("用户未绑定手机,请先绑定手机号");
			return resp;
		}
		
		// 激活券码
        // 需要判断券的时间，互斥量，公用券发券等
		String prefix = StringUtils.substring(commoncode, 0, 2);
		int checkCode = 0;
		CheckCouponDto dto = new CheckCouponDto();
		
		if(StringUtils.equals(prefix, "LK")) { // 公用券码
			dto = checkCoupon(0, commoncode, CouponFormType.PRIVATE, userId, openId);
			checkCode = dto.getResult();
		//数字开头 为邀请码 进邀请码规则 
		}else if(Character.isDigit(commoncode.charAt(0))){
			commoncode=commoncode.toUpperCase();
			return inviteOperationProcessor.checkAppInviteCode(userId, openId, commoncode);
		} 
		else {
			dto = checkCoupon(0, commoncode, CouponFormType.PUBLIC, userId, openId);
			checkCode = dto.getResult();
		}
		if(checkCode != ActivateCouponMessage.SUCCESS.getCode()) {
			String message = ActivateCouponMessage.parseCode(checkCode).getMessage();
			resp.setCode(400);
			resp.setMessage(message);
			return resp;
		}
		
		if(StringUtils.equals(prefix, "LK")) {
			// 领取公用券码
			boolean createStatus = createCouponCode(userId, dto.getCouponId(), 0, false, 0, openId, 0);
			
			if(createStatus == false) {
				resp.setCode(400);
				resp.setMessage("激活失败");
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
		    	
		    	int code = coupListDao.update(userId, beginTime, endTime, openId, commoncode);
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
     * @param  [bol]      $num       [检测会员可以领取的张数] 0为不检测领取数量, 同时也是领券的张数
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
    	
    	CheckCouponDto dto = checkCoupon(couponId, coupRule.getCommoncode(), type, userId, openId);
    	int checkCode = dto.getResult();
    	if(checkCode != ActivateCouponMessage.SUCCESS.getCode()) {
    		recordLog(userId, couponId, "[检查结果]".concat(String.valueOf(checkCode)), checkCode, openId);
    		return false;
    	}
    	
    	// 检测会员的领取数据量
    	if(num > 0) {
    		boolean checkCouponNum = checkCouponIdNum(couponId, userId, openId, num, CheckCouponIdNum.USER);
    		if(checkCouponNum == false) {
    			recordLog(userId, couponId, "[检查结果]  (超出个人最大领取量 : ".concat(String.valueOf(num))
    					.concat(" ，无法领取了)"), 11, openId);
    			checkCode = 11;// c超出领取最大量（个人）
    			return false;
    		}
    		
    		if(StringUtils.isNotBlank(openId)) {
    			checkCouponNum = checkCouponIdNum(couponId, userId, openId, num, CheckCouponIdNum.DEVICE);
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
    		code = mkCode(pre);
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
   
    	
    	int coupNum = coupRule.getNum() + 1;//优惠券总张数
    	int sumResiduenum = coupRule.getResiduenum() + 1;//优惠券未使用张数
    	// 如果num = 2， 发两张券
    	if(num == 2){
    		CoupList coupL2 =new CoupList();
    		BeanUtils.copyProperties(coupL, coupL2);
    		//获取新的券码
    		coupL2.setCommoncode(mkCode(coupRule.getPrefix()));
    		List<CoupList>	lists= Lists.newArrayList(coupL,coupL2);  
    		coupListDao.save(lists);
    		coupNum = coupRule.getNum() + 2;//优惠券总张数
        	sumResiduenum = coupRule.getResiduenum() + 2;//优惠券未使用张数
    	}else{
    		coupListDao.save(coupL);
    	}
    	
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
    private String mkCode(String pre){
		String mtime = StringUtils.substring(String.valueOf((long)new Date().getTime()), 7);
		//随机生成券号
		String rcode = String.valueOf(new Random().nextInt(89999) + 10000);
    	
    	String code = pre + mtime + rcode;
    	CoupList coupList = coupListDao.findByCommoncode(code);
    	if(coupList != null) {
    		code = mkCode(pre);
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
        if (type == CheckCouponIdNum.USER) {
        	coupList = coupListDao.findByUserIdAndCouponId(userId, couponId);
        } else if (type == CheckCouponIdNum.DEVICE) {
        	coupList = coupListDao.findByCouponIdAndOpenid(couponId, openId);
        }
		int codeNum = CollectionUtils.size(coupList);
		
		if(codeNum >= num) {
			return false;
		}
		return true;
	}
	
	/**
     * 检测用户填写的优惠券是否有效，并获取一些优惠券相关的数据，如新老用户标识，类型，互斥量等
     *
     * @param  [string]     $code    [优惠券ID]
     * @param  [int]        $type    [优惠券类型： 1：公用券， 2， 私用券]（此处优惠券）
     * @param  [string]     $user_id [用户ID]
     * @param  [string]     $openid  [设备ID，检测设备互斥]
     * 
     * @return [int]                 [检测结果标识]
     */
    private CheckCouponDto checkCoupon(int couponId, String commoncode, int type, int userId, String openId) {
    	CheckCouponDto dto = new CheckCouponDto();
    	Date listEndTime = null;
        if(type == CouponFormType.PRIVATE) {

        	CoupRule coupRule = null;
        	if (couponId > 0) {
            	coupRule = coupRuleDao.findOne(couponId);
        	}
        	else {
        		coupRule = coupRuleDao.findByCommoncode(commoncode);
        	}
        	
        	if(coupRule == null) {
        		dto.setResult(ActivateCouponMessage.ERROR_COMMONCODE.getCode());
//        		dto.setCouponId(coupRule.getId());
        		return dto;
        	}
        	// 赋值优惠券规则ID
        	couponId = coupRule.getId();
        	Integer alreadySend = coupListDao.countCouponId(couponId);
        	 // 判断最大发放量
        	
        	if(alreadySend != null && coupRule.getMaxnum() > 0 && coupRule.getMaxnum() <= alreadySend) {
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
        	if(coupList.getUserId() > 0) {
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
    	
//    	List<Integer> statusList = new ArrayList<Integer>();
//    	statusList.add(OrderStatusEnum.STATUS_CANCELED.getId());
//    	statusList.add(OrderStatusEnum.STATUS_INVALID.getId());
//    	List<Order> orders = orderDao.findByBuyerIdAndStatusNotIn(userId, statusList);
//    	
//    	if(CollectionUtils.isEmpty(orders)) {
//    		return true;
//    	}
    	if(orderDao.IfExistOrder(userId)!=null){
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

class CheckCouponIdNum {
	public static int USER = 1;
	public static int DEVICE = 2;
	
}


