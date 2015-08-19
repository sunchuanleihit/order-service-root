package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_gcategory_new")
public class GCategoryNew {

	@Id
	@Column(name = "cate_id")
	private int cateId = 0;

	@Column(name = "store_id")
	private int storeId = 0;

	@Column(name = "cate_name")
	private String cateName = "";

	@Column(name = "parent_id")
	private int parentId = 0;

	@Column(name = "sort_order")
	private int sortOrder = 0;

	@Column(name = "seo_key")
	private String seoKey = "";

	@Column(name = "seo_description")
	private String seoDescription = "";

	@Column(name = "if_show")
	private Integer ifShow = 0;

	@Column(name = "if_recom")
	private Integer ifRecom = 0;

	@Column(name = "is_red")
	private Integer isRed = 0;

	@Column(name = "is_view")
	// 1.左边 0.右边
	private Integer isView = 0;

	@Column(name = "num_goods")
	private int numGoods = 0;

	@Column(name = "gstatus")
	private int gstatus = 0;

	@Column(name = "keyword")
	private String keyword = "";

	@Column(name = "gcatelog")
	private String gcatelog = "";

	@Column(name = "if_show_zj0")
	private Integer ifShowZj0 = 0;

	@Column(name = "is_view_zj0")
	private Integer isViewZj0 = 0;

	@Column(name = "if_show_nj0")
	private Integer ifShowNj0 = 0;

	@Column(name = "is_view_nj0")
	private Integer isViewNj0 = 0;

	@Column(name = "cate_new")
	private Integer cateNew = 0;

	@Column(name = "commission_ratio")
	private String commissionRatio = "";

	@Column(name = "commission_max_money")
	private String commissionMaxMoney = "";

	@Column(name = "about")
	private int about = 0;// 左右显示（1-左 0-右）

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSeoKey() {
		return seoKey;
	}

	public void setSeoKey(String seoKey) {
		this.seoKey = seoKey;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public Integer getIfShow() {
		return ifShow;
	}

	public void setIfShow(Integer ifShow) {
		this.ifShow = ifShow;
	}

	public Integer getIfRecom() {
		return ifRecom;
	}

	public void setIfRecom(Integer ifRecom) {
		this.ifRecom = ifRecom;
	}

	public Integer getIsRed() {
		return isRed;
	}

	public void setIsRed(Integer isRed) {
		this.isRed = isRed;
	}

	public Integer getIsView() {
		return isView;
	}

	public void setIsView(Integer isView) {
		this.isView = isView;
	}

	public int getNumGoods() {
		return numGoods;
	}

	public void setNumGoods(int numGoods) {
		this.numGoods = numGoods;
	}

	public int getGstatus() {
		return gstatus;
	}

	public void setGstatus(int gstatus) {
		this.gstatus = gstatus;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getGcatelog() {
		return gcatelog;
	}

	public void setGcatelog(String gcatelog) {
		this.gcatelog = gcatelog;
	}

	public Integer getIfShowZj0() {
		return ifShowZj0;
	}

	public void setIfShowZj0(Integer ifShowZj0) {
		this.ifShowZj0 = ifShowZj0;
	}

	public Integer getIsViewZj0() {
		return isViewZj0;
	}

	public void setIsViewZj0(Integer isViewZj0) {
		this.isViewZj0 = isViewZj0;
	}

	public Integer getIfShowNj0() {
		return ifShowNj0;
	}

	public void setIfShowNj0(Integer ifShowNj0) {
		this.ifShowNj0 = ifShowNj0;
	}

	public Integer getIsViewNj0() {
		return isViewNj0;
	}

	public void setIsViewNj0(Integer isViewNj0) {
		this.isViewNj0 = isViewNj0;
	}

	public Integer getCateNew() {
		return cateNew;
	}

	public void setCateNew(Integer cateNew) {
		this.cateNew = cateNew;
	}

	public String getCommissionRatio() {
		return commissionRatio;
	}

	public void setCommissionRatio(String commissionRatio) {
		this.commissionRatio = commissionRatio;
	}

	public String getCommissionMaxMoney() {
		return commissionMaxMoney;
	}

	public void setCommissionMaxMoney(String commissionMaxMoney) {
		this.commissionMaxMoney = commissionMaxMoney;
	}

	public int getAbout() {
		return about;
	}

	public void setAbout(int about) {
		this.about = about;
	}

}
