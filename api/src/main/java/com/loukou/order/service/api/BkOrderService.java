package com.loukou.order.service.api;

import java.util.List;

import com.loukou.order.service.bo.BaseRes;
import com.loukou.order.service.req.dto.BkOrderRemarkReqDto;
import com.loukou.order.service.req.dto.CssOrderReqDto;
import com.loukou.order.service.resp.dto.BkCouponListRespDto;
import com.loukou.order.service.resp.dto.BkOrderActionRespDto;
import com.loukou.order.service.resp.dto.BkOrderListRespDto;
import com.loukou.order.service.resp.dto.BkOrderPayDto;
import com.loukou.order.service.resp.dto.BkOrderRemarkListRespDto;
import com.loukou.order.service.resp.dto.BkOrderReturnDto;
import com.loukou.order.service.resp.dto.BkOrderReturnListRespDto;
import com.loukou.order.service.resp.dto.BkTxkDto;
import com.loukou.order.service.resp.dto.BkTxkRecordListRespDto;
import com.loukou.order.service.resp.dto.BkVaccountListResultRespDto;
import com.loukou.order.service.resp.dto.BkOrderRemarkDto;
import com.loukou.order.service.resp.dto.GoodsListDto;

/**
 * 客服系统用到的订单服务
 * @author sunchuanlei
 *
 */
public interface BkOrderService {
	
	public BkOrderListRespDto orderDetail(String orderSnMain);
	/**
	 * 查找所有订单
	 */
	public BkOrderListRespDto queryBkOrderList(int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);
	
	/**
	 * 查找未生成退款单的订单
	 * @param sort
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @param cssOrderReqDto
	 * @return
	 */
	public BkOrderListRespDto queryBkOrderNoReturnList(String sort, String order, int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);
	
	/**
	 * 查找待退款订单
	 * @param sort
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @param cssOrderReqDto
	 * @return
	 */
	public BkOrderReturnListRespDto queryBkOrderToReturn(String sort, String order, int pageNum, int pageSize, CssOrderReqDto cssOrderReqDto);
	
	/**
	 * 取消反向订单
	 * @param orderIdR
	 * @return
	 */
	public String cancelOrderReturn(Integer orderIdR);

	public List<GoodsListDto> getOrderGoodsList(int orderId);
	
	public List<BkOrderReturnDto>  getOrderReturnsByIds(List<Integer> ids);
	/**
	 * 查找订单的操作详情
	 * @param orderSnMain
	 * @return
	 */
	public List<BkOrderActionRespDto> getOrderActions(String orderSnMain);
	
	public BkOrderListRespDto queryBkOrderListByBuyerId(int pageNum, int pageSize, Integer buyerId);
	/**
	 * 查询虚拟账户流水
	 * @param pageNum
	 * @param pageSize
	 * @param buyerId
	 * @return
	 */
	public BkVaccountListResultRespDto queryBkVaccountResult(int pageNum, int pageSize, Integer buyerId);
	
	/**
	 * 查找某用户的优惠券
	 * @param pageNum
	 * @param pageSize
	 * @param buyerId
	 * @return
	 */
	public BkCouponListRespDto queryCouponListByUserId(int pageNum, int pageSize, Integer buyerId);
	
	public List<BkTxkDto> queryTxkListByUserId(Integer userId);
	
	/**
	 * 查询淘心卡的支付明细
	 * @param buyerId
	 * @return
	 */
	public BkTxkRecordListRespDto queryTxkRecordListByUserId(Integer pageNum, Integer pageSize,Integer buyerId);

	public List<BkOrderPayDto> getOrderPayList(String orderSnMain);
	
	public List<BkOrderPayDto> getAllOrderPayList(String orderSnMain);

	BkOrderListRespDto orderReturnMsg(String orderSnMain);
	
	public BaseRes<String> generateReturn(String actor,int orderId,String postScript,String orderSnMain,int returnType,int payId,double shippingFee,
			int[] goodsIdList,
			int[] specIdList,
			int[] proTypeList,
			int[] recIdList,
			int[] goodsReturnNumList,
			double[] goodsReturnAmountList,
			int[] goodsReasonList,
			String[] goodsNameList,
			int[] paymentIdList,
			double[] returnAmountList);
	
	public BaseRes<String> cancelOrder(String orderSnMain,String actor);
	
	public BaseRes<String> resetCancelOrder(String orderSnMain,String actor);
	
	public double getMultiplePaymentRefundMsg(String orderSnMain);
	
	public BaseRes<String> generatePaymentRefund(int reason,String actor,String orderSnMain,String postScript,int[] paymentIdList,double[] returnAmountList);
	
	public BaseRes<String> generateSpecialPaymentRefund(int reason,String actor,String orderSnMain,String postScript,int[] paymentIdList,double[] returnAmountList);
	
	public BaseRes<String> generateComplaint(String actor,String orderSnMain,String content1,String addTime,String userName,String mobile,int type,int status,String content2,String[] sellerNameList,String[] goodsNameList);
	
	public BaseRes<String> changeOrder(String orderSnMain,String needShiptime,String needShiptimeSlot);
	
	/**
	 * 查询订单交接
	 * @param page
	 * @param rows
	 * @param reqDto
	 * @return
	 */
	public BkOrderRemarkListRespDto queryHandover(int page, int rows, BkOrderRemarkReqDto reqDto);
	
	/**
	 * 交接订单
	 * @param idList
	 */
	public int closeOrderRemark(String userName , Integer id);
	
	/**
	 * 添加交接任务
	 * @param orderSnMain
	 * @param content
	 * @param type
	 */
	public void addOrderRemark(String userName, String orderSnMain, String content, Integer type);
	
	/**
	 * 根据主订单编号获取其下所有交接
	 * @param orderSnMain
	 * @return
	 */
	public List<BkOrderRemarkDto> queryHandoverByOrderSnMain(String orderSnMain);

	public BaseRes<String> cancelSubOrder(int orderId,String actor);
	
	public BaseRes<String> resetCancelSubOrder(int orderId,String actor);
	
	public BaseRes<String> paySubOrder(String orderSnMain,String actor,int payId);
	
	public List<BkOrderPayDto> getPaymentList();
}
