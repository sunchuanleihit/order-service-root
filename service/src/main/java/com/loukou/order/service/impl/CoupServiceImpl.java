package com.loukou.order.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.loukou.order.service.api.CoupService;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.dao.CoupTypeDao;
import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.entity.CoupType;
import com.loukou.order.service.entity.Member;
import com.loukou.order.service.enums.CoupRuleTypeEnum;
import com.loukou.order.service.enums.CoupTypeEnum;
import com.loukou.order.service.enums.CoupUseScopeEnum;
import com.loukou.order.service.req.dto.CoupRuleAddReqDto;
import com.loukou.order.service.req.dto.CoupRuleReqDto;
import com.loukou.order.service.req.dto.CoupTypeReqDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListRespDto;
import com.loukou.order.service.resp.dto.BkRespDto;
import com.loukou.order.service.resp.dto.CoupRuleDto;
import com.loukou.order.service.resp.dto.CoupRuleRespDto;
import com.loukou.order.service.resp.dto.CoupTypeRespDto;
import com.loukou.order.service.resp.dto.CouponListDto;
import com.loukou.order.service.util.DateUtils;

@Service("coupService")
public class CoupServiceImpl implements CoupService{
	@Autowired
	private CoupRuleDao coupRuleDao;
	
	@Autowired
	private CoupTypeDao coupTypeDao;
	
	@Autowired
	private CoupListDao coupListDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public CoupRuleRespDto queryCoupRule(final CoupRuleReqDto req, Integer pageNum, Integer pageSize) {
		Sort pageSort = new Sort(Sort.Direction.ASC,"id");
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		Page<CoupRule> coupRulePage = coupRuleDao.findAll(new Specification<CoupRule>(){
			@Override
			public Predicate toPredicate(Root<CoupRule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if(req.getRuleId()!=null){
					predicates.add(cb.equal(root.get("id"), req.getRuleId()));
				}
				if(StringUtils.isNotBlank(req.getRuleName())){
					Path<String> nameExp = root.get("couponName");  
					predicates.add(cb.like(nameExp, req.getRuleName()+"%")); 
				}
				if(req.getRuleType() != null){
					predicates.add(cb.equal(root.get("coupontypeid"), req.getRuleType()));
				}
				if(req.getTypeId()!=null){
					predicates.add(cb.equal(root.get("typeid"), req.getTypeId()));
				}else{
					List<CoupType> typeList = coupTypeDao.queryAll();
					List<Integer> typeIds = new ArrayList<Integer>();
					if(typeIds!=null && typeIds.size()>0){
						Path<Integer> typeIdPath = root.get("typeid");
						In in = cb.in(typeIdPath);
						for(CoupType type: typeList){
							in.value(type.getId());
						}
						predicates.add(in);
					}
				}
				if(req.getScope()!=null){
					predicates.add(cb.equal(root.get("couponType"), req.getScope()));
				}
				//predicates.add(cb.notEqual(root.get("issue"), 2));
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
		}, pageable);
		CoupRuleRespDto respDto = new CoupRuleRespDto(200, "");
		respDto.setCount(coupRulePage.getTotalElements());
		List<CoupRule> coupRuleList = coupRulePage.getContent();
		List<CoupRuleDto> coupRuleDtoList = new ArrayList<CoupRuleDto>();
		if(coupRuleList!=null && coupRuleList.size()>0){
			for(CoupRule tmp: coupRuleList){
				CoupRuleDto dto = createCoupRuleDto(tmp);
				coupRuleDtoList.add(dto);
			}
		}
		respDto.setCoupRuleList(coupRuleDtoList);
		return respDto;
	}
	
	
	private CoupRuleDto createCoupRuleDto(CoupRule coupRule){
		CoupRuleDto dto = new CoupRuleDto();
		dto.setId(coupRule.getId());
		dto.setName(coupRule.getCouponName());
		dto.setType(CoupRuleTypeEnum.parseName(coupRule.getCoupontypeid()).getName());
		dto.setScope(CoupUseScopeEnum.parseName(coupRule.getCouponType()).getName());
		dto.setCommoncode(coupRule.getCommoncode());
		if(coupRule.getCanuseday() == 0){
			if(coupRule.getBegintime() !=null && coupRule.getEndtime() !=null){
				dto.setCanuseday(DateUtils.date2DateStr(coupRule.getBegintime()) + " ~ " + DateUtils.date2DateStr(coupRule.getEndtime()));
			}
		}else{
			dto.setCanuseday(coupRule.getCanuseday()+"天");
		}
		if(coupRule.getIssue()==0){
			dto.setIsuse("不启用");
		}else if(coupRule.getIssue()==1){
			dto.setIsuse("启用");
		}else if(coupRule.getIssue()==2){
			dto.setIsuse("停用");
		}
		return dto;
	}
	
	//优惠券类别列表
	@Override
	public BkCouponTypeListRespDto queryCoupType(Integer pageNum, Integer pageSize) {
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<CoupType> coupTypePage = coupTypeDao.findAll(new Specification<CoupType>(){
			@Override
			public Predicate toPredicate(Root<CoupType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.equal(root.get("status"),0));
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
			
		}, pageable);
		BkCouponTypeListRespDto respDto = new BkCouponTypeListRespDto(200,"");
		respDto.setCount(coupTypePage.getTotalElements());

		List<CoupType> coupRuleList = coupTypePage.getContent();
		List<BkCouponTypeListDto> coupTypeDtoList = new ArrayList<BkCouponTypeListDto>();
		if(coupRuleList!=null && coupRuleList.size()>0){
			for(CoupType t: coupRuleList){
				BkCouponTypeListDto tt = new BkCouponTypeListDto();
				tt.setId(t.getId());
				tt.setTitle(t.getTitle());
				tt.setDescription(t.getDescription());
				tt.setType(CoupTypeEnum.parseName(t.getTypeid()).getName());
				tt.setUsenum(t.getUsenum());
				tt.setNewuser(t.getNewuser());
				tt.setDescription(t.getDescription());
				tt.setSell_site(t.getSellSite());
				coupTypeDtoList.add(tt);
			}
		}
		respDto.setBkCouponTypeList(coupTypeDtoList);
		return respDto;
	}


	@Override
	public List<BkCouponTypeListDto> findAllCoupType() {
		List<CoupType> typeList = coupTypeDao.queryAll();
		List<BkCouponTypeListDto> list = new ArrayList<BkCouponTypeListDto>();
		if(typeList !=null && typeList.size()>0){
			for(CoupType tmp: typeList){
				BkCouponTypeListDto dto = new BkCouponTypeListDto();
				dto.setId(tmp.getId());
				dto.setTitle(tmp.getTitle());
				dto.setTypeid(tmp.getTypeid());
				list.add(dto);
			}
		}
		return list;
	}


	@Override
	public CoupRuleDto queryCoupRuleById(Integer ruleId) {
		CoupRule rule = coupRuleDao.findOne(ruleId);
		if(rule == null){
			return null;
		}
		CoupType coupType = coupTypeDao.findOne(rule.getTypeid());
		if(coupType == null){
			return null;
		}
		CoupRuleDto result = new CoupRuleDto();
		result.setId(rule.getId());
		result.setType(coupType.getTitle());
		result.setName(rule.getCouponName());
		if(rule.getIssue()==0){
			result.setIsuse("不启用");
		}else if(rule.getIssue()==1){
			result.setIsuse("启用");
		}else if(rule.getIssue()==2){
			result.setIsuse("停用");
		}
		if(rule.getCanuseday()!=0){
			result.setCanuseday("动态结束时间("+rule.getCanuseday()+"天结束)");
		}else{
			result.setCanuseday("固定结束时间("+DateUtils.date2DateStr(rule.getBegintime())+" ~ "+DateUtils.date2DateStr(rule.getEndtime())+")");
		}
		result.setMaxnum(rule.getMaxnum());
		result.setPrefix(rule.getPrefix());
		result.setCoupTypeName(CoupRuleTypeEnum.parseName(rule.getCoupontypeid()).getName());
		result.setMoney(rule.getMoney());
		result.setLowemoney(rule.getLowemoney());
		result.setScope(CoupUseScopeEnum.parseName(rule.getCouponType()).getName());
		result.setOutId(rule.getOutId());
		return result;
	}


	@Override
	public void updateCoupRuleIsuse(Integer ruleId, Integer isuse) {
		coupRuleDao.updateRuleIsuse(ruleId, isuse);
	}


	//增加优惠券
	@Override
	public String addCouponNumber(Integer ruleId, Integer number) {
		CoupRule rule = coupRuleDao.findOne(ruleId);
		if(StringUtils.isNotBlank(rule.getCommoncode())){
			return "公有券不能添加券码";
		}
		String pre = rule.getPrefix();
		for(int i=0; i<number; i++){
			String code = mkCode(pre);
			CoupList coup = new CoupList();
			coup.setCouponId(rule.getId());
			coup.setBegintime(rule.getBegintime());
			coup.setEndtime(rule.getEndtime());
			coup.setCommoncode(code);
			coup.setMoney(rule.getMoney());
			coup.setMinprice(rule.getLowemoney());
			coup.setIssue(rule.getIssue());
			coup.setSellSite(rule.getSellSite());
			coup.setOpenid("");
			coupListDao.save(coup);
		}
		return "success";
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


	@Override
	public List<CouponListDto> queryCoupListByRuleId(Integer ruleId) {
		CoupRule rule = coupRuleDao.findOne(ruleId);
		List<CoupList> couponList = coupListDao.findByCouponId(ruleId);
		List<CouponListDto> resultList = new ArrayList<CouponListDto>();
		if(couponList !=null && couponList.size()>0){
			for(CoupList tmp: couponList){
				CouponListDto dto = this.createCouponListDto(tmp);
				dto.setCouponName(rule.getCouponName());
				resultList.add(dto);
			}
		}
		return resultList;
	}


	private CouponListDto createCouponListDto(CoupList tmp) {
		CouponListDto dto = new CouponListDto();
		dto.setId(tmp.getId());
		dto.setCouponId(tmp.getCouponId());
		dto.setUserId(""+tmp.getUserId());
		if(tmp.getUserId()!=0){
			Member member = memberDao.findOne(tmp.getUserId());
			if(member!=null){
				dto.setUserName(member.getUserName());
			}
		}
		if(tmp.getBegintime() == null){
			dto.setStarttime("");
		}else{
			dto.setStarttime(DateUtils.date2DateStr(tmp.getBegintime()));
		}
		if(tmp.getEndtime() == null){
			dto.setEndtime("");
		}else{
			dto.setEndtime(DateUtils.date2DateStr(tmp.getEndtime()));
		}
		dto.setCommoncode(tmp.getCommoncode());
		dto.setMoney(tmp.getMoney());
		dto.setMinprice(tmp.getMinprice());
		if(tmp.getIschecked() == 0){
			dto.setIschecked("未使用");
		}else if(tmp.getIschecked() == 1){
			dto.setIschecked("已使用");
		}
		if(tmp.getCreatetime() == null){
			dto.setCreatetime("");
		}else{
			dto.setCreatetime(DateUtils.date2DateStr(tmp.getCreatetime()));
		}
		if(tmp.getUsedtime()==null){
			dto.setUsedtime("");
		}else{
			dto.setUsedtime(DateUtils.date2DateStr(tmp.getUsedtime()));
		}
		if(tmp.getIssue()==0){
			dto.setCanuse("未启用");
		}else if(tmp.getIssue()==1){
			dto.setCanuse("启用");
		}else if(tmp.getIssue()==2){
			dto.setCanuse("停用");
		}
		return dto;
	}


	@Override
	public String addCoupRule(CoupRuleAddReqDto dto) {
		CoupRule rule = new CoupRule();
		if(dto.getTypeId() == null){
			return "规则分类不能为空";
		}
		CoupType type = coupTypeDao.findOne(dto.getTypeId());
		if(type == null || type.getStatus() ==1 ){
			return "优惠券规则所属分类不存在或已被删除";
		}
		rule.setTypeid(dto.getTypeId());
		if(StringUtils.isBlank(dto.getCouponName())){
			return "规则名称不能为空";
		}
		rule.setCouponName(dto.getCouponName());
		rule.setIsnewuser(0);
		rule.setIssue(1);
		rule.setCanusetype(dto.getCanuseType());
		if(dto.getCanuseType() == 1){
			rule.setCanuseday(dto.getCanuseday());
		}else if(dto.getCanuseType() == 0){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(StringUtils.isNotBlank(dto.getBegintime())){
				try {
					rule.setBegintime(format.parse(dto.getBegintime()));
				} catch (ParseException e) {
					e.printStackTrace();
					return "开始时间格式错误";
				}
			}
			if(StringUtils.isNotBlank(dto.getEndtime())){
				try {
					rule.setEndtime(format.parse(dto.getEndtime()));
				} catch (ParseException e) {
					e.printStackTrace();
					return "结束时间格式错误";
				}
			}
		}
		rule.setCoupontypeid(dto.getCoupontypeid());
		if(type.getTypeid() == 2){
			if(StringUtils.isNotBlank(dto.getCommoncode())){
				String code = "LK"+dto.getCommoncode();
				CoupRule coupRule = coupRuleDao.findByCommoncode(code);
				if(coupRule!=null){
					return "优惠券码已被使用";
				}
				rule.setCommoncode(code);
			}else{
				return "缺少公用优惠券码";
			}
		}else{
			if(StringUtils.isNotBlank(dto.getPrefix())){
				if(dto.getPrefix().startsWith("LK")){
					return "前缀不能为LK";
				}
				rule.setPrefix(dto.getPrefix());
			}else{
				return "缺少券码前缀";
			}
		}
		rule.setMaxnum(dto.getMaxnum());
		if(dto.getCoupontypeid() == CoupRuleTypeEnum.ENOUGH.getId()){
			if(dto.getLowemoney() == null){
				return "缺少最低消费金额";
			}
			rule.setLowemoney(dto.getLowemoney());
		}
		rule.setMoney(dto.getMoney());
		rule.setCouponType(dto.getCouponType());
		if(dto.getCouponType() == CoupUseScopeEnum.KIND.getId()){//如果是分类券
			rule.setOutId(""+dto.getCategory());
		}
		coupRuleDao.save(rule);
		return "success";
	}

	@Override
	public CoupTypeRespDto findCoupType(Integer pageSize, Integer pageNum) {
		Sort pageSort = new Sort(Sort.Direction.ASC,"id");
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		Page<CoupType> page = coupTypeDao.queryByPage(pageable);
		List<CoupType> typeList = page.getContent();
		CoupTypeRespDto respDto = new CoupTypeRespDto(200, "");
		respDto.setCount(page.getTotalElements());
		List<BkCouponTypeListDto> dtoList = new ArrayList<BkCouponTypeListDto>();
		for(CoupType tmp: typeList){
			BkCouponTypeListDto dto = createCoupTypeDto(tmp);
			dtoList.add(dto);
		}
		respDto.setCoupTypeList(dtoList);
		return respDto;
	}

	private BkCouponTypeListDto createCoupTypeDto(CoupType tmp){
		BkCouponTypeListDto dto = new BkCouponTypeListDto();
		dto.setId(tmp.getId());
		dto.setNewuser(tmp.getNewuser());
		dto.setTitle(tmp.getTitle());
		dto.setTypeid(tmp.getTypeid());
		dto.setUsenum(tmp.getUsenum());
		dto.setDescription(tmp.getDescription());
		return dto;
	}
	
	@Override
	public BkCouponTypeListDto queryCoupTypeById(Integer typeId) {
		CoupType coupType = coupTypeDao.findOne(typeId);
		if(coupType.getStatus() == 0){
			BkCouponTypeListDto dto = createCoupTypeDto(coupType);
			return dto;
		}
		return null;
	}


	@Override
	public String addOrUpdateCoupType(CoupTypeReqDto typeDto) {
		if(StringUtils.isBlank(typeDto.getTitle())){
			return "类别名称不能为空";
		}
		if(typeDto.getTypeid() == null){
			return "优惠券类别没有数值";
		}
		if(typeDto.getUsenum() == null){
			return "互斥次数没有数值";
		}
		if(typeDto.getNewuser() ==null){
			return "会员限制没有数值";
		}
		CoupType type = new CoupType();
		type.setTitle(typeDto.getTitle());
		type.setTypeid(typeDto.getTypeid());
		type.setUsenum(typeDto.getUsenum());
		type.setNewuser(typeDto.getNewuser());
		type.setDescription(typeDto.getDescription());
		type.setSellSite("cz0");
		if(typeDto.getId() == null){
			coupTypeDao.save(type);
		}else{
			coupTypeDao.updateType(typeDto.getId(), typeDto.getTitle(), typeDto.getTypeid(), typeDto.getUsenum(), typeDto.getNewuser(), typeDto.getDescription());
		}
		return "success";
	}


	@Override
	public void deleteCoupType(Integer id) {
		coupTypeDao.stopCoupType(id);
		
	}


	@Override
	public BkRespDto queryCoupList(final Integer ruleId, final String commoncode, String username, Integer pageSize, Integer pageNum) {
		BkRespDto resultDto = new BkRespDto();
		Integer userIdTemp = null;
		List<Integer> userIds = new ArrayList<Integer>();//记录用户ID，后面查询用户名
		if(StringUtils.isNotBlank(username)){
			List<Member> memberList = memberDao.findByUserName(username);
			if(memberList == null || memberList.size() ==0){
				return resultDto;
			}
			userIdTemp = memberList.get(0).getUserId();
		}
		final Integer userId = userIdTemp;
		if(userId!=null){
			userIds.add(userId);
		}
		Sort pageSort = new Sort(Sort.Direction.DESC,"id");
		Pageable pageable = new PageRequest(pageNum, pageSize, pageSort);
		Page<CoupList> coupPage = coupListDao.findAll(new Specification<CoupList>(){
			@Override
			public Predicate toPredicate(Root<CoupList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if(ruleId != null){
					predicates.add(cb.equal(root.get("couponId"), ruleId));
				}
				if(userId != null){
					predicates.add(cb.equal(root.get("userId"), userId));
				}
				if(StringUtils.isNotBlank(commoncode)){
					predicates.add(cb.equal(root.get("commoncode"), commoncode));
				}
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
		},pageable);
		resultDto.setCount(coupPage.getTotalElements());
		List<CouponListDto> coupDtoList = new ArrayList<CouponListDto>();
		List<CoupList> coupList = coupPage.getContent();
		List<Integer> coupRuleIds = new ArrayList<Integer>();//优惠券规则
		for(CoupList tmp: coupList){
			CouponListDto dto = this.createCouponListDto(tmp);
			if(!userIds.contains(tmp.getUserId())){
				userIds.add(tmp.getUserId());
			}
			if(!coupRuleIds.contains(tmp.getCouponId())){
				coupRuleIds.add(tmp.getCouponId());
			}
			coupDtoList.add(dto);
		}
		List<Member> memberList = new ArrayList<Member>();
		if(userIds.size()>0){
			memberList = memberDao.findByUserIdIn(userIds);
		}
		List<CoupRule> ruleList = new ArrayList<CoupRule>();
		if(coupRuleIds.size()>0){
			ruleList = coupRuleDao.findByIdIn(coupRuleIds);
		}
		Map<Integer, Member> memberMap = new HashMap<Integer, Member>();
		Map<Integer, CoupRule> ruleMap = new HashMap<Integer, CoupRule>();
		for(Member member: memberList){
			memberMap.put(member.getUserId(), member);
		}
		for(CoupRule rule: ruleList){
			ruleMap.put(rule.getId(), rule);
		}
		for(CouponListDto tmp: coupDtoList){
			Member member = memberMap.get(tmp.getUserId());
			if(member!=null){
				tmp.setUserName(member.getUserName());
			}
			CoupRule rule = ruleMap.get(tmp.getCouponId());
			if(rule!=null){
				tmp.setCouponName(rule.getCouponName());
			}
		}
		resultDto.setList(coupDtoList);
		return resultDto;
	}


	@Override
	public String sendCoupon(Integer id, String username) {
		List<Member> memberList = memberDao.findByUserName(username);
		if(memberList == null || memberList.size() == 0 ){
			return "会员不存在";
		}
		CoupList coup = coupListDao.findOne(id);
		if(coup.getUserId() != 0){
			return "优惠券已被发放过";
		}
		CoupRule rule = coupRuleDao.findOne(coup.getCouponId());
		if(rule == null){
			return "规则不存在";
		}
		Integer userId = memberList.get(0).getUserId();
		Date begintime = null;
		Date endtime = null;
		if(rule.getCanusetype() == 0){
			begintime = rule.getBegintime();
			endtime = rule.getEndtime();
		}else if(rule.getCanusetype() == 1){
			Calendar calendar = Calendar.getInstance();
			Date now = new Date();
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, rule.getCanuseday());
			Date endDate = calendar.getTime();
			begintime = DateUtils.getStartofDate(now);
			endtime = DateUtils.getEndofDate(endDate);
		}
		coupListDao.sendCoup(id, userId, begintime, endtime);
		return "success";
	}

	@Override
	public String deleteCoup(Integer id) {
		CoupList coup = coupListDao.findOne(id);
		if(coup == null){
			return "优惠券不存在";
		}
		if(coup.getIschecked() == 1){
			return "优惠券已被使用";
		}
		coupListDao.deleteCoup(id);
		return "success";
	}
}
