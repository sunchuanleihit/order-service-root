package com.loukou.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
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
import com.loukou.order.service.dao.CoupRuleDao;
import com.loukou.order.service.entity.CoupRule;
import com.loukou.order.service.enums.CoupRuleTypeEnum;
import com.loukou.order.service.enums.CoupUseScopeEnum;
import com.loukou.order.service.req.dto.CoupRuleReqDto;
import com.loukou.order.service.resp.dto.CoupRuleDto;
import com.loukou.order.service.resp.dto.CoupRuleRespDto;
import com.loukou.order.service.util.DateUtils;

@Service("coupService")
public class CoupServiceImpl implements CoupService{
	@Autowired
	private CoupRuleDao coupRuleDao;

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
					Path<String> nameExp = root.get("name");  
					predicates.add(cb.like(nameExp, req.getRuleName()+"%")); 
				}
				if(req.getRuleType() != null){
					predicates.add(cb.equal(root.get("coupontypeid"), req.getRuleType()));
				}
				if(req.getTypeId()!=null){
					predicates.add(cb.equal(root.get("typeid"), req.getTypeId()));
				}
				if(req.getScope()!=null){
					predicates.add(cb.equal(root.get("couponType"), req.getScope()));
				}
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
		if(coupRule.getCanuseday() == 0){
			if(coupRule.getBegintime() !=null && coupRule.getEndtime() !=null){
				dto.setCanuseday(DateUtils.date2DateStr(coupRule.getBegintime()) + " ~ " + DateUtils.date2DateStr(coupRule.getEndtime()));
			}
		}else{
			dto.setCanuseday(coupRule.getCanuseday()+"天");
		}
		return dto;
	}
}




