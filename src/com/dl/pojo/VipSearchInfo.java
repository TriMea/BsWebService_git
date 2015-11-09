package com.dl.pojo;

import java.math.BigDecimal;

public class VipSearchInfo {

	private String no;//集团卡序列号V开头的
	private String sno;//集团6位卡号
    private String sta;
    private String type;
	private String phone;//手机号
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getExpaned() {
		return expaned;
	}
	public void setExpaned(String expaned) {
		this.expaned = expaned;
	}
	public String getBanlance() {
		return banlance;
	}
	public void setBanlance(String banlance) {
		this.banlance = banlance;
	}
	private String idno;//证件号
	private String gname;//姓名
	private String idcls;
	private String birthday;
	private String sex;
	private String ename;
	private String address;
	private String fancy;
	private String remark;
	private String vinfo;
	private String needpasswd;
	private String password;
	private String log_hotelid;
	private String log_sysdate;
	private String log_id;
	private String log_name;
	private String log_shift;
	private String log_date;
	private String payment;
	private String expaned;
	private String banlance;
	private String last_hotelid;
	private String last_id;
	private String last_name;
	private String last_shift;
	private String last_date;
	private String  gt_id;
	private String saleid;
	private String class1;
	private String ybanlance;
	private String descript;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	

	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIdcls() {
		return idcls;
	}
	public void setIdcls(String idcls) {
		this.idcls = idcls;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFancy() {
		return fancy;
	}
	public void setFancy(String fancy) {
		this.fancy = fancy;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getVinfo() {
		return vinfo;
	}
	public void setVinfo(String vinfo) {
		this.vinfo = vinfo;
	}
	public String getNeedpasswd() {
		return needpasswd;
	}
	public void setNeedpasswd(String needpasswd) {
		this.needpasswd = needpasswd;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLog_hotelid() {
		return log_hotelid;
	}
	public void setLog_hotelid(String logHotelid) {
		log_hotelid = logHotelid;
	}
	public String getLog_sysdate() {
		return log_sysdate;
	}
	public void setLog_sysdate(String logSysdate) {
		log_sysdate = logSysdate;
	}
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String logId) {
		log_id = logId;
	}
	public String getLog_name() {
		return log_name;
	}
	public void setLog_name(String logName) {
		log_name = logName;
	}
	public String getLog_shift() {
		return log_shift;
	}
	public void setLog_shift(String logShift) {
		log_shift = logShift;
	}
	public String getLog_date() {
		return log_date;
	}
	public void setLog_date(String logDate) {
		log_date = logDate;
	}
	
	public String getLast_hotelid() {
		return last_hotelid;
	}
	public void setLast_hotelid(String lastHotelid) {
		last_hotelid = lastHotelid;
	}
	public String getLast_id() {
		return last_id;
	}
	public void setLast_id(String lastId) {
		last_id = lastId;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String lastName) {
		last_name = lastName;
	}
	public String getLast_shift() {
		return last_shift;
	}
	public void setLast_shift(String lastShift) {
		last_shift = lastShift;
	}
	public String getLast_date() {
		return last_date;
	}
	public void setLast_date(String lastDate) {
		last_date = lastDate;
	}
	public String getGt_id() {
		return gt_id;
	}
	public void setGt_id(String gtId) {
		gt_id = gtId;
	}
	
	public String getSaleid() {
		return saleid;
	}
	public void setSaleid(String saleid) {
		this.saleid = saleid;
	}
	public String getClass1() {
		return class1;
	}
	public void setClass1(String class1) {
		this.class1 = class1;
	}
	public String getYbanlance() {
		return ybanlance;
	}
	public void setYbanlance(String ybanlance) {
		this.ybanlance = ybanlance;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	@Override
	public String toString() {
		return "VipSearchInfo [address=" + address + ", banlance=" + banlance
				+ ", birthday=" + birthday + ", class1=" + class1
				+ ", descript=" + descript + ", ename=" + ename + ", expaned="
				+ expaned + ", fancy=" + fancy + ", gname=" + gname
				+ ", gt_id=" + gt_id + ", idcls=" + idcls + ", idno=" + idno
				+ ", last_date=" + last_date + ", last_hotelid=" + last_hotelid
				+ ", last_id=" + last_id + ", last_name=" + last_name
				+ ", last_shift=" + last_shift + ", log_date=" + log_date
				+ ", log_hotelid=" + log_hotelid + ", log_id=" + log_id
				+ ", log_name=" + log_name + ", log_shift=" + log_shift
				+ ", log_sysdate=" + log_sysdate + ", needpasswd=" + needpasswd
				+ ", no=" + no + ", password=" + password + ", payment="
				+ payment + ", phone=" + phone + ", remark=" + remark
				+ ", saleid=" + saleid + ", sex=" + sex + ", sno=" + sno
				+ ", sta=" + sta + ", type=" + type + ", vinfo=" + vinfo
				+ ", ybanlance=" + ybanlance + "]";
	}
	
	
	
	
	
	
	
	
}
