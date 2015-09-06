package com.loukou.order.service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_member")
public class Member {
	
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private int userId;

	@Column(name = "user_name")
	private String userName = "";
	
	@Column(name = "real_name")
	private String realName = "";

	@Column(name = "gender")
	private int gender = 0;
	
	@Column(name = "birthday")
	private Date birthday = new Date();;

	@Column(name = "password")
	private String password = "";
	
	@Column(name = "address")
	private String address = "";

	@Column(name = "reg_time")
	private Long regTime = 0L;
	
	@Column(name = "phone_mob")
	private String phoneMob = "";

	@Column(name = "PhoneChecked")
	private Integer phoneChecked = 0;

	@Column(name = "PhoneChecktime")
	private Long phoneChecktime = 0L;

	@Column(name = "PhoneCheckCode")
	private String phoneCheckCode = "";

	@Column(name = "site_type")
	private String siteType = "";
	
	@Column(name = "card_no")
	private String cardNo = "";

	@Column(name = "card_secret")
	private String cardSecret = "";
	
	@Column(name = "card_status")
	private int cardStatus = 0;

	@Column(name = "verify_amount")
	private double verifyAmount = 0.0;

	@Column(name = "region_id")
	private int regionId = 0;

	@Column(name = "points")
	private int points = 0;

	@Column(name = "freezpoint")
	private int freezpoint = 0;

	@Column(name = "source")
	private Integer source = 0;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getRegTime() {
		return regTime;
	}

	public void setRegTime(Long regTime) {
		this.regTime = regTime;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public Integer getPhoneChecked() {
		return phoneChecked;
	}

	public void setPhoneChecked(Integer phoneChecked) {
		this.phoneChecked = phoneChecked;
	}

	public Long getPhoneChecktime() {
		return phoneChecktime;
	}

	public void setPhoneChecktime(Long phoneChecktime) {
		this.phoneChecktime = phoneChecktime;
	}

	public String getPhoneCheckCode() {
		return phoneCheckCode;
	}

	public void setPhoneCheckCode(String phoneCheckCode) {
		this.phoneCheckCode = phoneCheckCode;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardSecret() {
		return cardSecret;
	}

	public void setCardSecret(String cardSecret) {
		this.cardSecret = cardSecret;
	}

	public int getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(int cardStatus) {
		this.cardStatus = cardStatus;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public double getVerifyAmount() {
		return verifyAmount;
	}

	public void setVerifyAmount(double verifyAmount) {
		this.verifyAmount = verifyAmount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getFreezpoint() {
		return freezpoint;
	}

	public void setFreezpoint(int freezpoint) {
		this.freezpoint = freezpoint;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
}
