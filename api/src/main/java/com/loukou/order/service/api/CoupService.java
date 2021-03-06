package com.loukou.order.service.api;
import java.util.List;

import com.loukou.order.service.req.dto.CoupRuleAddReqDto;
import com.loukou.order.service.req.dto.CoupRuleReqDto;
import com.loukou.order.service.req.dto.CoupTypeReqDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListDto;
import com.loukou.order.service.resp.dto.BkCouponTypeListRespDto;
import com.loukou.order.service.resp.dto.BkRespDto;
import com.loukou.order.service.resp.dto.CoupRuleDto;
import com.loukou.order.service.resp.dto.CoupRuleRespDto;
import com.loukou.order.service.resp.dto.CoupTypeRespDto;
import com.loukou.order.service.resp.dto.CouponListDto;

public interface CoupService {

	CoupRuleRespDto queryCoupRule(CoupRuleReqDto req, Integer pageNum, Integer pageSize);
	
	BkCouponTypeListRespDto queryCoupType(Integer pageNum, Integer pageSize);

	List<BkCouponTypeListDto> findAllCoupType();

	/**
	 * 根据ID查找优惠券规则
	 * @param ruleId
	 * @return
	 */
	CoupRuleDto queryCoupRuleById(Integer ruleId);

	/**
	 * 改变优惠券是否启用状态
	 * @param ruleId
	 * @param isuse
	 */
	void updateCoupRuleIsuse(Integer ruleId, Integer isuse);

	/**
	 * 增加券码
	 * @param ruleId
	 * @param number
	 */
	String addCouponNumber(Integer ruleId, Integer number);

	/**
	 * 根据规则ID获取券码列表
	 * @param ruleId
	 * @return
	 */
	List<CouponListDto> queryCoupListByRuleId(Integer ruleId);

	/**
	 * 添加优惠券规则
	 * @param dto
	 * @return
	 */
	String addCoupRule(CoupRuleAddReqDto dto);

	CoupTypeRespDto findCoupType(Integer pageSize, Integer pageNum);

	/**
	 * 根据ID查找优惠券类型
	 * @param typeId
	 * @return
	 */
	BkCouponTypeListDto queryCoupTypeById(Integer typeId);
	
	/**
	 * 添加或修改优惠券类型
	 * @param typeDto
	 * @return
	 */
	String addOrUpdateCoupType(CoupTypeReqDto typeDto);

	/**
	 * 停用优惠券规则
	 * @param id
	 */
	void deleteCoupType(Integer id);

	/**
	 * 查找优惠券
	 * @param commoncode
	 * @param username
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	BkRespDto queryCoupList(Integer ruleId, String commoncode, String username, Integer pageSize, Integer pageNum);

	/**
	 * 优惠券发放
	 * @param id
	 * @param username
	 * @return
	 */
	String sendCoupon(Integer id, String username);

	/**
	 * 删除优惠券
	 * @param id
	 * @return
	 */
	String deleteCoup(Integer id);
	
	/**
	 * 判断是否是客服专用券
	 * @param id
	 * @return
	 */
	boolean isCallCenterCoup(Integer id);
}
