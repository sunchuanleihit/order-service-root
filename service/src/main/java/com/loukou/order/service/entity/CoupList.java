package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_coup_list")
public class CoupList {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "coupon_id")
	private int couponId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "begintime")
	private Date begintime;// 开始时间

	@Column(name = "endtime")
	private Date endtime;// 结束时间

	@Column(name = "commoncode")
	private String commoncode = "";

	@Column(name = "money")
	private double money;

	@Column(name = "minprice")
	private Double minprice;// 最低使用金额限制

	@Column(name = "issue")
	private int issue = 1;// 是否启用0，不启用，1，启用，2，停用

	@Column(name = "ischecked")
	private int ischecked;// 是否被使用。0，未使用；1，已使用

	@Column(name = "openid")
	private String openid;

	@Column(name = "createtime")
	private String createtime;// 领取时间

	@Column(name = "usedtime")
	private Date usedtime;// 使用时间yyyy-MM-dd HH:mm:ss

	@Column(name = "source")
	private Integer source;// 1，会员后台；2，购物车

	@Column(name = "sell_site")
	private String sellSite;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getCommoncode() {
		return commoncode;
	}

	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getMinprice() {
		if (minprice == null) {
			return 0.0;
		}
		return minprice;
	}

	public void setMinprice(Double minprice) {
		this.minprice = minprice;
	}

	public int getIssue() {
		return issue;
	}

	public void setIssue(int issue) {
		this.issue = issue;
	}

	public int getIschecked() {
		return ischecked;
	}

	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Date getUsedtime() {
		return usedtime;
	}

	public void setUsedtime(Date usedtime) {
		this.usedtime = usedtime;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}

}
