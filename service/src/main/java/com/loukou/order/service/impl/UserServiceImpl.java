package com.loukou.order.service.impl;

import com.loukou.order.service.resp.dto.UserRespDto;
import com.loukou.order.service.api.UserService;
import com.loukou.order.service.dao.UserDao;
import com.loukou.order.service.entity.User;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;

    @Override
    public UserRespDto get(int id) {
    	if (id <= 0) {
    		return null;
    	}
    	User user = userDao.findOne(id);
    	if (user == null) {
    		return null;
    	}

    	UserRespDto userRespDto = new UserRespDto();
    	BeanUtils.copyProperties(user, userRespDto);

        return userRespDto;
    }
}
