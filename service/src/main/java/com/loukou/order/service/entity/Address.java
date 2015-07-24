package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_address")
public class Address {
	
	@Id
	@GeneratedValue
	@Column(name = "addr_id")
	private int id;
	
	@Column(name = "addr_id_old")
	private Integer addrIdOld = 0;
	
	@Column(name = "user_id")
	private int userId = 0;
	
	@Column(name = "user_id_old")
	private Integer userIdOld = 0;

	@Column(name = "consignee")
	private String consignee = "";
	
	@Column(name = "region_id")
	private int regionId = 0;
	
	@Column(name = "region_name")
	private String regionName = "";

	@Column(name = "address")
	private String address = "";

	@Column(name = "address_detail")
	private String addressDetail = "";
	
	@Column(name = "phone_tel")
	private String phoneTel = "";
	
	@Column(name = "phone_mob")
	private String phoneMob = "";
	
	@Column(name = "defaults")
	private int defaults = 0;
	
	@Column(name = "is_selected")
	private int isSelected = 0;
	
	@Column(name = "zipcode")
	private String zipcode = "";
	
	@Column(name = "sell_site")
	private String sellSite = "";

	@Column(name = "create_date")
	private Date createdTime = new  Date();
	
	private String latitude = "";
	private String longitude = "";
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Integer getUserIdOld() {
		return userIdOld;
	}

	public void setUserIdOld(Integer userIdOld) {
		this.userIdOld = userIdOld;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getPhoneTel() {
		return phoneTel;
	}

	public void setPhoneTel(String phoneTel) {
		this.phoneTel = phoneTel;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public int getDefaults() {
		return defaults;
	}

	public void setDefaults(int defaults) {
		this.defaults = defaults;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}
	

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public Integer getAddrIdOld() {
		return addrIdOld;
	}

	public void setAddrIdOld(Integer addrIdOld) {
		this.addrIdOld = addrIdOld;
	}
	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}
