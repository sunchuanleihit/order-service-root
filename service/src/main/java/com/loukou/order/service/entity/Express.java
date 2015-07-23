package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_express")
public class Express {

	@Id
	@GeneratedValue
	@Column(name = "eid")
	private int eid;

	@Column(name = "code_num")
	private String codeNum = "";//快递公司代码

	@Column(name = "express_name")
	private String expressName = "";//快递公司名称

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

}
