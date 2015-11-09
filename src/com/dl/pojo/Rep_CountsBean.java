package com.dl.pojo;

public class Rep_CountsBean {

	private String sysdate;//营业日期
	private String day;//当天合计
	private String name;//分店名
	private String hotelid;
	public String getSysdate() {
		return sysdate;
	}
	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	@Override
	public String toString() {
		return "Rep_CountsBean [day=" + day + ", hotelid=" + hotelid
				+ ", name=" + name + ", sysdate=" + sysdate + "]";
	}
	
	
	
}
