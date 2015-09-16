package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.loukou.order.service.enums.CoupNewUserEnum;
import com.loukou.order.service.enums.CoupRuleTypeEnum;
import com.loukou.order.service.enums.CoupTypeEnum;
import com.loukou.order.service.enums.CoupUseScopeEnum;
import com.loukou.order.service.req.dto.CoupRuleReqDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListRespDto;
import com.loukou.order.service.resp.dto.CoupRuleDto;
import com.loukou.order.service.resp.dto.CoupRuleRespDto;
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
				tt.setNewuser(CoupNewUserEnum.parseName(t.getNewuser()).getName());
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
		result.setReturnMoney(rule.getReturnmoney());
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
		if(tmp.getCreatetime() == null){
			dto.setCreatetime("");
		}else{
			dto.setCreatetime(DateUtils.date2DateStr(tmp.getCreatetime()));
		}
		if(tmp.getUsedtime()==null){
			dto.setUsedtime("");
			dto.setIsused("未使用");
		}else{
			dto.setUsedtime(DateUtils.date2DateStr(tmp.getUsedtime()));
			dto.setIsused("已使用");
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
}
