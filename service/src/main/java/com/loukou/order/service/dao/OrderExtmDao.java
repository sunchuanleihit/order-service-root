package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderExtm;


public interface OrderExtmDao extends CrudRepository<OrderExtm, Integer>{

	List<OrderExtm> findByOrderSnMain(String orderSnMain);

}
