package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loukou.order.service.entity.Address;


public interface AddressDao extends PagingAndSortingRepository<Address, Integer>{

	List<Address> findByUserIdAndSellSite(int userId,String sellSite);
	
	List<Address> findByUserIdAndDefaults(int userId,int defaults);
	
	List<Address> findByUserIdAndDefaultsAndSellSite(int userId,int defaults,String sellSite);
	
	List<Address> findByUserIdAndIsSelectedAndSellSite(int userId,int isSelected,String sellSite);
	
	List<Address> findByUserId(int userId);

	List<Address> findTop1ByUserIdAndSellSiteOrderByCreatedTimeDesc(int userId,String sellSite);
}
