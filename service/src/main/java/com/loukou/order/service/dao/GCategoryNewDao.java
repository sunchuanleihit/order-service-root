package com.loukou.order.service.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loukou.order.service.entity.GCategoryNew;


public interface GCategoryNewDao extends PagingAndSortingRepository<GCategoryNew, Integer>{

	List<GCategoryNew> findByParentId(int parentId);

	List<GCategoryNew> findByCateIdIn(List<Integer> cateIds);

	
}
