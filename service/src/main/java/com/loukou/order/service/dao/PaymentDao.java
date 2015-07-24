package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.Payment;


public interface PaymentDao extends CrudRepository<Payment, Integer>{

	
}
