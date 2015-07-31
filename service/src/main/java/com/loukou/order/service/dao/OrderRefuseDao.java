package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderRefuse;

public interface OrderRefuseDao extends CrudRepository<OrderRefuse, Integer>{
        public OrderRefuse findByTaoOrderSn(String taoOrderSn);
}
