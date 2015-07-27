package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class ShareDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7572260352506561110L;
	private String icon;
	private String content;
	private String url;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
