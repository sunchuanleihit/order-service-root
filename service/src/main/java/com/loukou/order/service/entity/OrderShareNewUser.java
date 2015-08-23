package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_ordershare_newuser")
public class OrderShareNewUser {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	/*
	 * 手机号
	 */
	@Column(name = "phone_mob")
	private String phoneMob = "";

	/*
	 * money
	 */
	@Column(name = "money")
	private double money = 0;
	

	/*
	 * 状态
	 */
	@Column(name = "status")
	private int status = 0;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone_mob() {
		return phoneMob;
	}

	public void setPhone_mob(String phone_mob) {
		this.phoneMob = phone_mob;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
