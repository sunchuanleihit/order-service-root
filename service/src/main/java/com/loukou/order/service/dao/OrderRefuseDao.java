package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.OrderRefuse;

public interface OrderRefuseDao extends CrudRepository<OrderRefuse, Integer>{
         //别害怕 大胆用 unique的
        public OrderRefuse findByTaoOrderSn(String taoOrderSn);
        
        public List<OrderRefuse> findByTaoOrderSnIn(List<String> taoOrderSn);
}
