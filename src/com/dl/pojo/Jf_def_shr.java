package com.dl.pojo;

import java.math.BigDecimal;

public class Jf_def_shr {

	private String code;
	private String type;
	private String name;
	private String amount;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Jf_def_shr [amount=" + amount + ", code=" + code + ", name="
				+ name + ", type=" + type + "]";
	}
	
	
}
