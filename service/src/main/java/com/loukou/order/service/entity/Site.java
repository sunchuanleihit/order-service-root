package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 店铺所属分站
 * @author YY
 * @since 2015-02-04
 */
@Entity
@Table(name = "tcz_site")
public class Site {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	/**
	 * 站点代号
	 */
	@Column(name = "site_code")
	private String siteCode;
	/**
	 * 站点名称
	 */
	@Column(name = "site_name")
	private String siteName;
	/**
	 * 简称-订单前缀用
	 */
	@Column(name = "short_code")
	private String shortCode;
	/**
	 * 分站对应的host地址
	 */
	@Column(name = "site_host")
	private String siteHost;
	/**
	 * 城市区域第一级ID,如常州是530
	 */
	@Column(name = "region_id")
	private Integer regionId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getSiteHost() {
		return siteHost;
	}
	public void setSiteHost(String siteHost) {
		this.siteHost = siteHost;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
}
