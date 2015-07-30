package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.LkWhDelivery;

public interface LkWhDeliveryDao extends CrudRepository<LkWhDelivery, Integer>{
    
        public LkWhDelivery findByDId(int dId);
}
