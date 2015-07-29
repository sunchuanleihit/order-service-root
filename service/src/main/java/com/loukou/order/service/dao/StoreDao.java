package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.loukou.order.service.entity.Store;


public interface StoreDao extends CrudRepository<Store, Integer>{

	/**
	 * 所有生鲜的店铺
	 * @return
	 */
	@Query("SELECT storeId FROM Store s WHERE storeTaotype = 1")
	List<Integer> getAllFreshStore();

	List<Store> findByStoreType(String storeType);

	List<Store> findByTelBusiness(String telBusiness);

	List<Store> findByStoreIdsIn(List<Integer> storeIds);
}
