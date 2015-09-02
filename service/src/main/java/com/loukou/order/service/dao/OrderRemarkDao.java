package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.loukou.order.service.entity.OrderRemark;

public interface OrderRemarkDao extends PagingAndSortingRepository<OrderRemark, Integer>, JpaSpecificationExecutor<OrderRemark>{
	
	public List<OrderRemark> findByOrderSnMainIn(List<String> orderSnMains);

}
