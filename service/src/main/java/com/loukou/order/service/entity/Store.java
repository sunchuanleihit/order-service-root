package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_store")
public class Store {

	@Id
	@GeneratedValue
	@Column(name = "store_id")
	private int storeId;

	@Column(name = "store_name")
	private String storeName = "";

	@Column(name = "store_taotype")
	private int storeTaotype = 0;

	@Column(name = "owner_name")
	private String ownerName = "";

	@Column(name = "owner_card")
	private String ownerCard = "";

	@Column(name = "owner_tel")
	private String ownerTel = "";

	@Column(name = "owner_mob")
	private String ownerMob = "";

	@Column(name = "sell_site")
	private String sellSite = "";

	@Column(name = "store_type")
	private String storeType = "";

	@Column(name = "last_update")
	private int lastUpdate = (int) (new Date().getTime() / 1000);

	@Column(name = "state")
	private int status = 0;

	@Column(name = "close_reason")
	private String closeReason = "";

	@Column(name = "freight")
	private int freight = 0;

	@Column(name = "store_logo")
	private String storeLogo = "";

	@Column(name = "tel_business")
	private String telBusiness = "";

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getStoreTaotype() {
		return storeTaotype;
	}

	public void setStoreTaotype(int storeTaotype) {
		this.storeTaotype = storeTaotype;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerCard() {
		return ownerCard;
	}

	public void setOwnerCard(String ownerCard) {
		this.ownerCard = ownerCard;
	}

	public String getSellSite() {
		return sellSite;
	}

	public void setSellSite(String sellSite) {
		this.sellSite = sellSite;
	}

	public int getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(int lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getOwnerMob() {
		return ownerMob;
	}

	public void setOwnerMob(String ownerMob) {
		this.ownerMob = ownerMob;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public int getFreight() {
		return freight;
	}

	public void setFreight(int freight) {
		this.freight = freight;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getTelBusiness() {
		return telBusiness;
	}

	public void setTelBusiness(String telBusiness) {
		this.telBusiness = telBusiness;
	}

	public String getOwnerTel() {
		return ownerTel;
	}

	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}

}
