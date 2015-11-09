package com.dl.pojo;

public class HotelidBean {

	private String hotelid;
	private String name;
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "HotelidBean [hotelid=" + hotelid + ", name=" + name + "]";
	}
	
	
	
}
