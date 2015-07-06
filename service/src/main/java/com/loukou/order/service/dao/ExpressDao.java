package com.loukou.order.service.dao;

import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.Express;


public interface ExpressDao extends CrudRepository<Express, Integer>{

	

	/**
	 * 快递公司代码及名称
	 * @return
	 */
	Express findByCodeNum(String shippingCompany);
}
