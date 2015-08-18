package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.CoupLog;


public interface CoupLogDao extends CrudRepository<CoupLog, Integer>{

	List<CoupLog> findByIdIn(List<Integer> couponIds);

	
}
