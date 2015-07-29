package com.loukou.order.pay.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loukou.order.service.dao.MemberDao;
import com.loukou.order.service.dao.OrderActionDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderPayDao;
import com.loukou.order.service.dao.OrderPaySignDao;
import com.loukou.order.service.dao.TczcountRechargeDao;

@Service
public class GetDaoProcessor {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private OrderPaySignDao orderPaySignDao;
	@Autowired
	private OrderActionDao orderActionDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderPayDao orderPayDao;
	@Autowired
	private TczcountRechargeDao rechargeDao;
	public MemberDao getMemberDao()
	{
		return memberDao;
	}
	
	public OrderPaySignDao getOrderPaySignDao()
	{
		return orderPaySignDao;
	}
	
	public OrderActionDao getOrderActionDao()
	{
		return orderActionDao;
	}
	
	public OrderDao getOrderDao()
	{
		return orderDao;
	}
	
	public OrderPayDao getOrderPayDao()
	{
		return orderPayDao;
	}
	public TczcountRechargeDao getTczcountRechargeDao()
	{
		return rechargeDao;
	}
}
