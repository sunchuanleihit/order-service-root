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
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.enums.ActivateCouponMessage;
import com.loukou.order.service.enums.CoupListReqTypeEnum;
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
import com.serverstarted.goods.service.resp.dto.ResultRespDto;

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
	private MemberDao memberDao;
	
	@Autowired 
	private GoodsSearchService goodsSearchService;

	private static final int LIMIT_COUPON_PER_DAY = 20; // 每天限用优惠券张数
	
	public CouponListRespDto getCouponList(int cityId, int userId, int storeId,
			String openId, int usable) {
		CouponListRespDto resp = new CouponListRespDto(200, "");
		if (cityId <= 0 || userId <= 0 || storeId <= 0
				|| StringUtils.isEmpty(openId)) {
			resp.setCode(400);
			resp.setMessage("参数有误");
			return resp;
		}
		// FIXME 查询语句
		List<CoupList> coupLists = coupListDao.getValidCoupLists(userId);// 以及其他的一些过滤条件
		if(usable == CoupListReqTypeEnum.ALL.getId()) {
			List<CoupList> invalidCoupLists = coupListDao.getInvalidCoupLists(userId);// 以及其他的一些过滤条件
			coupLists.addAll(invalidCoupLists);
		} 
//		else if (usable == CoupListReqTypeEnum.USABLE.getId()) {
//			
//		}
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
		ResultRespDto<CartRespDto> cart = cartService.getCart(userId, openId, cityId, storeId);
		for (CoupList coupList : coupLists) {
			CoupRule coupRule = ruleMap.get(coupList.getCouponId());
			if (verifyCoup(userId, openId, cityId, storeId, coupList, cart.getResult(),
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
			ids.add(Integer.valueOf(strs[0]));
		}
		return ids;
	}


	public OResponseDto<String> activateCoupon(int userId, String openId,
			String commoncode) {
		 OResponseDto<String> resp = new  OResponseDto<String>(200, "");
		 
		 if(userId <=0 || StringUtils.isBlank(openId) || StringUtils.isBlank(commoncode)) {
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
		if(StringUtils.equals(prefix, "LK")) { // 公用券码
			checkCode = checkCoupon(commoncode, CouponFormType.PUBLIC, userId, openId);
		} else {
			checkCode = checkCoupon(commoncode, CouponFormType.PRIVATE, userId, openId);
		}
		if(checkCode != ActivateCouponMessage.SUCCESS.getCode()) {
			String message = ActivateCouponMessage.parseCode(checkCode).getMessage();
			resp.setResult(message);
			return resp;
		}
		
		if(StringUtils.equals(prefix, "LK")) {
			// 领取公用券码
			
		} else {
			
		}
		 
		return null;
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
     * @author zhaozl
     * @since  2015-03-21
     */
    public boolean createCouponCode(int userId, int couponId, boolean type, boolean check, 
    		int num, String openId, double money) {

    	CoupRule coupRule = coupRuleDao.findOne(couponId);
    	
    	if(coupRule == null) {
    		recordLog();//TODO
    		return false;
    	} 
    	
    	int checkCode = checkCoupon(commoncode, type, userId, openId);
    	
    	if(checkCode != ActivateCouponMessage.SUCCESS.getCode()) {
    		recordLog();
    		return false;
    	}
    	
    	// 检测会员的领取数据量  田--20150405
    	if(num > 0) {
    		boolean checkCouponNum = checkCouponIdNum(couponId, userId, openId, num, CouponFormType.PUBLIC);
    		if(checkCouponNum == false) {
    			recordLog();
    			checkCode = 11;// c超出领取最大量（个人）
    			return false;
    		}
    		
    		if(StringUtils.isNotBlank(openId)) {
    			checkCouponNum = checkCouponIdNum(couponId, userId, openId, num, CouponFormType.PRIVATE);
    			if(checkCouponNum == false) {
    				checkCode = 12; // c超出领取最大量（设备）
    				return false;
    			}
    		}
    	}
    	
       
			
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
        	coupList = coupListDao.findByCouponIdAndOpenId(couponId, openId);
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
     * 
     * @author zhaozl
     * @since  2015-03-21
     */
    private int checkCoupon(String commoncode, int type, int userId, String openId) {

        if($type == 1){

            $ruleRes = $this->_coup_rule_mod->get(array(
                'fields' => 'id, typeid, issue, canusetype, canuseday, begintime, endtime, maxnum',
                'conditions' => "commoncode = '{$code}'"
            ));

            if(!$ruleRes){
                return 0; // COUPON_NO_EXIST
            }

            // 判断最大发放量
            $alreadySend =  $this->_coup_list_mod->get(array(
                'fields' => 'count(id) as total',
                'conditions' => "coupon_id = '{$ruleRes['id']}'"
            ));
            $alSends = !empty($alreadySend['total'])?$alreadySend['total']:0;

            if($ruleRes['maxnum'] > 0 && $ruleRes['maxnum'] <= $alSends) {
                return 5;
            }

            // 赋值优惠券规则ID
            $this->_counpon_id = $ruleRes['id'];

        }else{
            $codeRes = $this->_coup_list_mod->get(array(
                'fields' => 'user_id, endtime, coupon_id',
                'conditions' => "commoncode = '{$code}'"
            ));
            if(!$codeRes){
                return 0; // COUPON_NO_EXIST
            }else{
                if($codeRes['user_id']){
                    return 3; // COUPON_ALREADY_DRAW
                }

                if($codeRes['issue']){
                    return 4; // COUPON_ALREADY_DRAW
                }

                // 赋值优惠券规则ID
                $this->_counpon_id = $codeRes['coupon_id'];
            }
        }

        $rRes = $this->_coup_rule_mod->get("{$this->_counpon_id}");
        if($rRes) {
            // 判断可领取的天数
            if($rRes['canusetype'] == 1) {
                if(!$rRes['canuseday']){
                    return 2;
                }
            }else{
                // 判断是否过期
                if($rRes['canusetype'] == 0 && strtotime($rRes['endtime']) < time()){
                    return 2;
                }

                if(isset($codeRes['endtime']) && strtotime($codeRes['endtime']) < time()) {
                    return 2; // COUPON_OUT_DATE
                }      
            }

            if($rRes['issue'] != 1) { // 停用或者未启用
                return 4;
            }

            $typeRes = $this->_coup_type_mod->get("{$rRes['typeid']}");

            if($typeRes) {
                $this->_type_id = $typeRes['typeid'];
                $this->_usenum = $typeRes['usenum'];
                $this->_newuser = $typeRes['newuser'];
            }else{
                return 999; // 无法查找父节点，数据异常
            }

            // 该类型的券逻辑删除
            if($typeRes['status'] == 1) {
                return 4;
            }
            
        }else{
            return 999; // 无法查找父节点，数据异常
        }

        // 继续检查
        // 上面检测优惠券通过， 继续检测（新老用户，互斥量等信息）
        // 1: 新用户的优惠券，检测只有新用户能领
        if($this->_newuser == 1 && !$this->_check_user_new($user_id)) {
            return 6;
        }

        // 2: 老用户的优惠券，检测只有老用户能领
        if($this->_newuser == 2 && $this->_check_user_new($user_id)) {
            return 7;
        }

        // 如果存在互斥量限制则，检测互斥量
        if($this->_usenum > 0 && !$this->_check_user_coupon($user_id, $typeRes['id'])){
            return 8;
        }

        // 如果存在互斥量限制则，检测互斥量
        if($openid) {
            if($this->_usenum > 0 && !$this->_check_device_coupon($openid, $typeRes['id'])){
                return 9;
            }
        }

        return 1; // COUPON_ENABLE
            
    }
}
