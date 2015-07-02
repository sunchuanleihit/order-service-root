package com.loukou.order.service.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.loukou.order.service.entity.User;


public interface UserDao extends PagingAndSortingRepository<User,Integer>{

}
