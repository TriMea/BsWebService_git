package com.dl.pojo;

public class Vip_cardtype_rmcodeBean {

	private String id;
	private String descript;
	private String rmcode;
	private String rmcode_dec;
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
	
	public String getRmcode() {
		return rmcode;
	}
	public void setRmcode(String rmcode) {
		this.rmcode = rmcode;
	}
	public String getRmcode_dec() {
		return rmcode_dec;
	}
	public void setRmcode_dec(String rmcodeDec) {
		rmcode_dec = rmcodeDec;
	}
	@Override
	public String toString() {
		return "Vip_cardtype_rmcodeBean [descript=" + descript + ", id=" + id
				+ ", rmcode=" + rmcode + ", rmcode_dec=" + rmcode_dec + "]";
	}
	
	
	
	
	
}
