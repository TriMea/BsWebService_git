package com.dl.pojo;

public class Sys_charge_code {

	private String code;
	private String subcode;
	private String descript;
	private String pccode;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSubcode() {
		return subcode;
	}
	public void setSubcode(String subcode) {
		this.subcode = subcode;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getPccode() {
		return pccode;
	}
	public void setPccode(String pccode) {
		this.pccode = pccode;
	}
	@Override
	public String toString() {
		return "Sys_charge_code [code=" + code + ", descript=" + descript
				+ ", pccode=" + pccode + ", subcode=" + subcode + "]";
	}
	
	
	
}
