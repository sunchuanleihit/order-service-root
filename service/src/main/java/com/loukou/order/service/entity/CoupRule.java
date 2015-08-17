package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_coup_rule")
public class CoupRule {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "typeid")
	private int typeid;// 优惠券类别id，与tcz_coup_type关联

	@Column(name = "coupon_name")
	private String couponName = "";// 优惠券名称

	@Column(name = "isnewuser")
	private int isnewuser = 0;// 是否限制新会员使用，0:否；1:是，默认为0（停用）

	@Column(name = "issue")
	private int issue = 0;// 是否启用0:不启用1:启用2:停用／删除

	@Column(name = "canusetype")
	private int canusetype = 0;// 有效期类型，0：开始begintime／结束endtime时间，1:有效天n天canuseday

	@Column(name = "canuseday")
	private int canuseday;// 优惠券有效期，即：领用开始起，n天有效；与开始／结束时间互斥

	@Column(name = "begintime")
	private Date begintime;// 开始时间

	@Column(name = "endtime")
	private Date endtime;// 结束时间

	@Column(name = "coupontypeid")
	private int coupontypeid = 0;// 优惠券类型，关联优惠券类型表
									// 0：满减券；1:现金券；2:折扣券；3:线下券；4自提点专用

	@Column(name = "commoncode")
	private String commoncode;// 公有券券码

	@Column(name = "maxnum")
	private int maxnum;// 最大发放量，0为不限制

	@Column(name = "usenum")
	private int usenum = 1;// 优惠券可以被同一个会员使用的次数。改值还受套tcz_coup_type表中的usenum影响。默认为1（停用）

	@Column(name = "num")
	private int num;// 优惠券的发放总量（统计）

	@Column(name = "residuenum")
	private int residuenum;// 未使用数量

	@Column(name = "lowemoney")
	private double lowemoney;// 最低使用金额。0为无限制

	@Column(name = "money")
	private double money;// 优惠券金额

	@Column(name = "prefix")
	private String prefix;// 券码前缀

	@Column(name = "timestamp")
	private Date timestamp = new Date();

	@Column(name = "coupon_type")
	private int couponType;// 0:全场券；1:店铺券；2:商品券；3:品牌券；4:分类券

	@Column(name = "out_id")
	private String outId;// 根据券类别分别对应（store_id, goods_id, brand_id, cat_id）

	@Column(name = "sys_type")
	private String sysType;// 0,网站；1，手机端（停用）

	@Column(name = "bank_id")
	private int bankId;// 0不限银行 其他－paymen_id

	@Column(name = "returnmoney")
	private double returnmoney;// 返利金额

	@Column(name = "sell_site")
	private String sellSite;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public int getIsnewuser() {
		return isnewuser;
	}

	public void setIsnewuser(int isnewuser) {
		this.isnewuser = isnewuser;
	}

	public int getIssue() {
		return issue;
	}

	public void setIssue(int issue) {
		this.issue = issue;
	}

	public int getCanusetype() {
		return canusetype;
	}

	public void setCanusetype(int canusetype) {
		this.canusetype = canusetype;
	}

	public int getCanuseday() {
		return canuseday;
	}

	public void setCanuseday(int canuseday) {
		this.canuseday = canuseday;
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

	public int getCoupontypeid() {
		return coupontypeid;
	}

	public void setCoupontypeid(int coupontypeid) {
		this.coupontypeid = coupontypeid;
	}

	public String getCommoncode() {
		return commoncode;
	}

	public void setCommoncode(String commoncode) {
		this.commoncode = commoncode;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

	public int getUsenum() {
		return usenum;
	}

	public void setUsenum(int usenum) {
		this.usenum = usenum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getResiduenum() {
		return residuenum;
	}

	public void setResiduenum(int residuenum) {
		this.residuenum = residuenum;
	}

	public double getLowemoney() {
		return lowemoney;
	}

	public void setLowemoney(double lowemoney) {
		this.lowemoney = lowemoney;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public double getReturnmoney() {
		return returnmoney;
	}

	public void setReturnmoney(double returnmoney) {
		this.returnmoney = returnmoney;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}

}
