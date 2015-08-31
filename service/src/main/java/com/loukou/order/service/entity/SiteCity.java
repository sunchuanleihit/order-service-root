package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 城市
 * @author sunchuanlei
 *@since 2015-08-21
 */
@Entity
@Table(name = "tcz_site_city")
public class SiteCity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "site_city_id")
	private String siteCityId;
	@Column(name = "site_city_name")
	private String siteCityName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSiteCityId() {
		return siteCityId;
	}
	public void setSiteCityId(String siteCityId) {
		this.siteCityId = siteCityId;
	}
	public String getSiteCityName() {
		return siteCityName;
	}
	public void setSiteCityName(String siteCityName) {
		this.siteCityName = siteCityName;
	}
	
}
