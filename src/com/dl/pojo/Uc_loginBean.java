package com.dl.pojo;

public class Uc_loginBean {

	private String username;
	private String op_name;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOp_name() {
		return op_name;
	}
	public void setOp_name(String opName) {
		op_name = opName;
	}
	@Override
	public String toString() {
		return "Uc_loginBean [op_name=" + op_name + ", username=" + username
				+ "]";
	}
	
}
