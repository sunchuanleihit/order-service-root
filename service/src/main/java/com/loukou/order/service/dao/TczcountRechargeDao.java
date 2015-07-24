package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.TczcountRecharge;

public interface TczcountRechargeDao extends CrudRepository<TczcountRecharge, Integer>{

	TczcountRecharge findByOrderSnMain(String orderSnMain);
}
