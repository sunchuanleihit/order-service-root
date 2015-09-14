package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.CoupType;


public interface CoupTypeDao extends CrudRepository<CoupType, Integer>, JpaSpecificationExecutor<CoupType>{

	List<CoupType> findByIdIn(List<Integer> coupTypeIds);
}
