package com.dl.pojo;

public class Room_priceCodeBean {

	private String id;
	private String descript;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	@Override
	public String toString() {
		return "Room_priceCodeBean [descript=" + descript + ", id=" + id + "]";
	}
	
	
}
