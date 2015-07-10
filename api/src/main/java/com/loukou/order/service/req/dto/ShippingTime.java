package com.loukou.order.service.req.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class ShippingTime implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6301646771981076250L;
	List<SpecShippingTime> booking = Lists.newArrayList();
	List<Integer> material = Lists.newArrayList();
	public List<SpecShippingTime> getBooking() {
		return booking;
	}
	public void setBooking(List<SpecShippingTime> booking) {
		this.booking = booking;
	}
	public List<Integer> getMaterial() {
		return material;
	}
	public void setMaterial(List<Integer> material) {
		this.material = material;
	}
	
}
