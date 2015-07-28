package com.loukou.order.service.refund;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.filter.AutoLoad;
import com.loukou.order.service.cron.AsyncTaskExecuteResult;
import com.loukou.order.service.dao.CoupListDao;
import com.loukou.order.service.dao.OrderDao;
import com.loukou.order.service.dao.OrderReturnDao;
import com.loukou.order.service.entity.CoupList;
import com.loukou.order.service.entity.Order;
import com.loukou.order.service.entity.OrderPay;
import com.loukou.order.service.entity.OrderPayR;
import com.loukou.order.service.entity.OrderReturn;
import com.loukou.order.service.enums.CoupListIsCheckedEnum;
import com.loukou.order.service.enums.OrderStatusEnum;

/**
 * 优惠券支付
 * @author linhua
 *
 */
@Service
public class CouponPay implements IPay{
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired 
	private OrderDao orderDao;
	
	@Autowired 
	private CoupListDao coupListDao;

	@Autowired 
	private OrderReturnDao orderReturnDao;
	
	private boolean canRefundCoupon(Order order){
		int unCancelOrderCount = orderDao.countOrderByOrderSnMainAndStatusNot(order.getOrderSnMain(), OrderStatusEnum.STATUS_CANCELED.getId());
		return unCancelOrderCount==0;
	}
	
	private AsyncTaskExecuteResult refundCoupon(Order order){
		CoupList coupList = coupListDao.getByUserIdAndCommoncode(order.getBuyerId(), order.getUseCouponNo());
		
		if(coupList==null){
			logger.error(String.format("用户id【%d】,订单【%s】的优惠券不存在", order.getBuyerId(),order.getTaoOrderSn()));
			return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_FAILED.getId(), "优惠券不存在");
		}
		
		if(canRefund(coupList)){
			logger.error(String.format("用户id【%d】,订单【%s】的优惠券已经过期，不能退券", order.getBuyerId(),order.getTaoOrderSn()));
			return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_FAILED.getId(), "优惠券已经过期");
		}
		
		resetCoupon(coupList);
		return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_SUCCESS.getId(), "");
	}
	
	private void resetCoupon(CoupList coupList){
		if(coupList!=null){
			coupList.setUsedtime(null);
			coupList.setIschecked(CoupListIsCheckedEnum.UNCHECKED.getId());
			coupListDao.save(coupList);
		}
	}
	
	private boolean canRefund(CoupList coupList){
		return (coupList.getEndtime().compareTo(SDF.format(new Date()))>0);
	}

	@Override
	public AsyncTaskExecuteResult refund(OrderPayR orderPayR) {
		OrderReturn orderReturn = orderReturnDao.findOne(orderPayR.getOrderIdR());
		
		if(orderReturn==null){
			return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_FAILED.getId(), "退货单不存在");
		}
		
		Order order = orderDao.findByOrderId(orderReturn.getOrderId());
		
		if(order==null){
			return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_FAILED.getId(), "订单不存在");
		}
		
		boolean canRefund = canRefundCoupon(order);
		
		if(!canRefund){
			return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_SUCCESS.getId(), "优惠券不符合退券条件");
		}
		
		refundCoupon(order);
		
		return new AsyncTaskExecuteResult(AsyncTaskExecuteResult.AsyncTaskExecuteResultStatusEnum.STATUS_SUCCESS.getId(), "");
	}
}
