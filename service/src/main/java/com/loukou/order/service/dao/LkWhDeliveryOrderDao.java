package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.LkWhDeliveryOrder;

public interface LkWhDeliveryOrderDao  extends CrudRepository<LkWhDeliveryOrder, Integer>{
    
}
