package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lk_configure")
public class LkConfigure {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "configure_name")
	private String configureName;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_value")
	private String itemValue;
	
	@Column(name = "item_type")
	private String itemType;

	@Column(name = "item_desc")
	private String itemDesc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConfigureName() {
		return configureName;
	}

	public void setConfigureName(String configureName) {
		this.configureName = configureName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	
}
