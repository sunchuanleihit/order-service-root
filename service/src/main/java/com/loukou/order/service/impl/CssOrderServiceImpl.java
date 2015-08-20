package com.loukou.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Service;

import com.loukou.order.service.api.CssOrderService;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.CssOrderRespDto;

@Service("cssOrderService")
public class CssOrderServiceImpl implements CssOrderService{
    @Autowired
    private OrderDao orderDao;
    
	@Override
	public List<CssOrderRespDto> queryOrderList(int page, int rows, final CssOrderReqDto cssOrderReqDto) {
		Page<Order> orderPage = orderDao.findAll(new Specification<Order>(){
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Integer startTime = null;
				if(StringUtils.isNoneBlank(cssOrderReqDto.getStartTime())){
					startTime = (int) (new Date(cssOrderReqDto.getStartTime()).getTime()/1000);
				}
				Integer endTime = null;
				if(StringUtils.isNoneBlank(cssOrderReqDto.getEndTime())){
					endTime = (int) (new Date(cssOrderReqDto.getEndTime()).getTime()/1000);
				}
				List<Predicate> predicate = new ArrayList<>();
				if(StringUtils.isNoneBlank(cssOrderReqDto.getOrderSnMain())){
					predicate.add(cb.equal(root.get("orderSnMain").as(String.class), cssOrderReqDto.getOrderSnMain()));
				}
				if(cssOrderReqDto.getStatus()!=null){
					predicate.add(cb.equal(root.get("status").as(Integer.class), cssOrderReqDto.getStatus()));
				}
				if(StringUtils.isNoneBlank(cssOrderReqDto.getStartTime())){
					predicate.add(cb.greaterThanOrEqualTo(root.get("addTime").as(Integer.class), startTime));
				}
				if(StringUtils.isNoneBlank(cssOrderReqDto.getEndTime())){
					predicate.add(cb.lessThanOrEqualTo(root.get("addTime").as(Integer.class), endTime));
				}
				Predicate[] pre = new Predicate[predicate.size()];
				return query.where(predicate.toArray(pre)).getRestriction();
			}
		}, new PageRequest(0, 10, Sort.Direction.DESC, "addTime"));
		List<CssOrderRespDto> resultList = new ArrayList<CssOrderRespDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(Order order: orderPage){
			CssOrderRespDto cssOrderRespDto = new CssOrderRespDto();
			cssOrderRespDto.setOrderId(order.getOrderId());
			cssOrderRespDto.setOrderSnMain(order.getOrderSnMain());
			cssOrderRespDto.setSource(order.getSource());
			if(order.getNeedShiptime()!=null){
				cssOrderRespDto.setNeedShiptime(dateFormat.format(order.getNeedShiptime()) + " " + order.getNeedShiptimeSlot());
			}
			cssOrderRespDto.setStatus(order.getStatus());
			cssOrderRespDto.setNeedInvoice(order.getNeedInvoice());
			cssOrderRespDto.setInvoiceNo(order.getInvoiceNo());
			cssOrderRespDto.setBuyerName(order.getBuyerName());
			resultList.add(cssOrderRespDto); 
		}
		return resultList;
		
/*		
		List<CssOrderRespDto> resultList = new ArrayList<CssOrderRespDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(orderList!=null){
			for(Order order: orderList){
				CssOrderRespDto cssOrderRespDto = new CssOrderRespDto();
				cssOrderRespDto.setOrderId(order.getOrderId());
				cssOrderRespDto.setOrderSnMain(order.getOrderSnMain());
				cssOrderRespDto.setSource(order.getSource());
				if(order.getNeedShiptime()!=null){
					cssOrderRespDto.setNeedShiptime(dateFormat.format(order.getNeedShiptime()) + " " + order.getNeedShiptimeSlot());
				}
				cssOrderRespDto.setStatus(order.getStatus());
				cssOrderRespDto.setNeedInvoice(order.getNeedInvoice());
				cssOrderRespDto.setInvoiceNo(order.getInvoiceNo());
				cssOrderRespDto.setBuyerName(order.getBuyerName());
				resultList.add(cssOrderRespDto); 
			}
		}
		return resultList;
		*/
	}

}
