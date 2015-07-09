package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.CoupRule;


public interface CoupRuleDao extends CrudRepository<CoupRule, Integer>{

	List<CoupRule> findByIdIn(List<Integer> couponIds);

	
}
