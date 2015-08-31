package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.Payment;


public interface PaymentDao extends CrudRepository<Payment, Integer>{
	@Query(value="SELECT payment_name FROM tcz_payment WHERE payment_id = ?1", nativeQuery=true)
	String getPaymentNameByPaymentId(int paymentId);
	
	@Query(value="SELECT p FROM Payment p WHERE paymentCode!=''")
	List<Payment> findAllMsg();
}
