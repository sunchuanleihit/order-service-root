package com.loukou.order.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tcz_tosu_handle")
public class TosuHandle {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "tid")
	private int tid;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "content")
	private String content = "";
	
	@Column(name = "dateline")
	private Integer dateline = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getDateline() {
		return dateline;
	}

	public void setDateline(Integer dateline) {
		this.dateline = dateline;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	@Column(name = "actor")
	private String actor = "";
}
