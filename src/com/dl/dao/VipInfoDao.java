package com.dl.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.dl.datasource.DBPool;
import com.dl.pojo.Room_priceCodeBean;
import com.dl.pojo.Sys_charge_code;
import com.dl.pojo.VipCardDetail;
import com.dl.pojo.VipInfoBean;
import com.dl.pojo.VipSearchInfo;
import com.dl.pojo.Vip_cardtypeBean;
import com.dl.pojo.Vip_cardtype_rmcodeBean;
import com.dl.util.CommonTool;











public class VipInfoDao {

	
	/*
	 * 单例模式
	 */
	private VipInfoDao() {}  
    //已经自行实例化   
    private static final VipInfoDao vipInfoDao = new VipInfoDao();  
    //静态工厂方法   
    public static VipInfoDao getInstance() {  
        return vipInfoDao;  
    }  
	
	/*
	 * 添加会员信息
	 */
	public synchronized boolean addVip_infoByOne(JSONObject jo,String accnt_new,String gno_new,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    String hotelid = jo.getString("hotelid");
	    try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("delete from vip_info where hotelid='"+hotelid+"'and no='"+accnt_new+"'");
			st.addBatch("insert into vip_info(no,sno,sta,type,class,agreement,gname,ename,phone,remark,vinfo,password,limit1,datebegin,dateend,accnt,saleid,log_id,log_date,logmark,needpasswd,log_ip,payment,expaned,banlance,ybanlance,accntnumb,connected,hotelid,gno,log_update,gt_id,idcls,idno,sex,birthday,address,fancy,log_hotelid,log_sysdate,log_name,log_shift,last_hotelid,last_id,last_name,last_shift,last_date) values('"+accnt_new+"','"+jo.getString("sno")
					+"','"+jo.getString("sta")+"','"+jo.getString("type")+"','"+jo.getString("class1")+"','"+jo.getString("agreement")+"','"+jo.getString("gname")
					+"','"+jo.getString("ename")+"','"+jo.getString("phone")+"','"+jo.getString("remark")
					+"','"+jo.getString("vinfo")+"','"+jo.getString("password")+"',"+new BigDecimal(jo.getString("limit1"))+",'"+jo.getString("datebegin")+"','"
					+jo.getString("dateend")+"','"+jo.getString("accnt")+"','"+jo.getString("saleid")+"','"+jo.getString("log_id")+"','"+jo.getString("log_date")
					+"',"+jo.getInt("logmark")+",'"+jo.getString("needpasswd")+"','"+jo.getString("log_ip")+"',"+new BigDecimal(jo.getString("payment"))+","+new BigDecimal(jo.getString("expaned"))+","+new BigDecimal(jo.getString("banlance"))+","+new BigDecimal(jo.getString("ybanlance"))+","+jo.getInt("accntnumb")+",'"+jo.getString("connected")+"','"+hotelid+"','"
					+jo.getString("gstno")+"','"+CommonTool.getToday()+"',"+gt_id+",'"+jo.getString("idcls")+"','"+jo.getString("idno")+"','"+jo.getString("sex")+"','"+jo.getString("birthday")+"','"+jo.getString("address")+"','"+jo.getString("fancy")+"','"+jo.getString("log_hotelid")+"','"+jo.getString("log_sysdate")+"','"+jo.getString("log_name")+"','"+jo.getString("log_shift")+
					
			"','"+jo.getString("last_hotelid")+"','"+jo.getString("last_id")+"','"+jo.getString("last_name")+"','"+jo.getString("last_shift")+"','"+jo.getString("last_date")+"')");
			st.addBatch("insert into jf_use(hotelid,accnt,type,id,sta,used,amount,ref1,ref2,sysdate,log_id,log_date,last_id,last_date,gt_id)values('"+hotelid.trim()+"','"+accnt_new+"','A',1,'"+jo.getString("sta")+"',"+(Double.valueOf(jo.getString("ybanlance"))*(-1))+",0.0000,'期初积分','','"+jo.getString("log_sysdate")+"','"+jo.getString("log_id")+"','"+jo.getString("log_date")+"','',null,"+gt_id+")");
			int[] f = st.executeBatch();
			conn.commit();
			
			if(f[0]>=0)
			{
//			
				flag = true;
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				conn.setAutoCommit(true);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
	
		return flag;
	}
	
	
	
	
	/*
	 * 根据主单号检索出主单记录
	 */
	public VipInfoBean findVipinfoByVno(String vip_no_new,String hotelid,String vip_no_old,String log_date)
	{
			Connection conn = null;
			Statement st  = null;
			ResultSet rs = null;
			VipInfoBean rh = null;
			
			
		    String sql = "select a.no,a.sno,a.sta,a.type,a.class,a.agreement,a.gname,a.ename,a.phone,a.remark,a.vinfo,a.password,a.limit1,"+
"a.datebegin,a.dateend,a.accnt,b.accnt_old,a.saleid,a.log_id,a.log_date,a.logmark,a.needpasswd,a.log_ip,a.payment,a.expaned,a.banlance,"+
"a.ybanlance,a.accntnumb,a.connected  from vip_info a,sync_accnt b  where no ='"+vip_no_new+"' and  a.gstno=b.accnt_new and b.class='gainfo' and a.log_date>'"+log_date+"' and b.hotelid='"+hotelid+"'"; 
		    try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					    rh = new VipInfoBean();
						rh.setNo(vip_no_old);
						rh.setSno(rs.getString(2).trim());
						rh.setSta(rs.getString(3).trim());
						rh.setType(rs.getString(4));
						rh.setLyy_class(rs.getString(5));
						rh.setAgreement(rs.getString(6));
						rh.setGname(rs.getString(7));
						rh.setEname(rs.getString(8));
						rh.setPhone(rs.getString(9));
						rh.setRemark(rs.getString(10));
						rh.setVinfo(rs.getString(11));
						rh.setPassword(rs.getString(12));
						rh.setLimit(rs.getBigDecimal(13)+"");
						rh.setDatebegin(rs.getString(14));
						rh.setDateend(rs.getString(15));
					    rh.setAccnt(rs.getString(16));
						rh.setGstno(rs.getString(17));
						rh.setSaleid(rs.getString(18));
						rh.setLog_id(rs.getString(19));
						rh.setLog_date(rs.getString(20));
						rh.setLogmark(rs.getInt(21)+"");
						rh.setNeedpasswd(rs.getString(22));
						rh.setLog_ip(rs.getString(23));
						rh.setPayment(rs.getBigDecimal(24)+"");
						rh.setExpaned(rs.getBigDecimal(25)+"");
						rh.setBanlance(rs.getBigDecimal(26)+"");
						rh.setYbanlance(rs.getBigDecimal(27)+"");
						rh.setAccntnumb(rs.getInt(28)+"");
						rh.setConnected(rs.getString(29));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
			
		return rh;
	}
	
	/*
	 * 根据证件号，手机号或者卡号获取集团卡信息
	 */
	public List<VipSearchInfo> searchVipInfo(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<VipSearchInfo> list = new ArrayList<VipSearchInfo>();
	    String sql = "SELECT no,sno,sta,type,gname,ename,idcls,idno,sex,birthday,address,fancy,phone,remark,vinfo,needpasswd,password,gt_id,log_hotelid,log_sysdate,log_id,log_name,log_shift,log_date,payment,expaned,banlance,last_hotelid,last_id,last_name,last_shift,last_date,saleid,ybanlance from vip_info where gt_id = "
	    	+gt_id+" and( (phone = '"+jo.getString("phone")+"' and phone!='') or (idno = '"+jo.getString("idno")+"' and idno!='') or (sno = '"+jo.getString("sno")+"' and sno!='') or (type = '"+jo.getString("type")+"' and type!='') or (gname = '"+jo.getString("gname")+"' and gname!='')" +
	    			"or (no = '"+(jo.getString("no")+gt_id)+"' and no!='') or (saleid = '"+jo.getString("saleid")+"' and saleid!='') or (log_hotelid = '"+jo.getString("log_hotelid")+"' and log_hotelid!='') or (log_sysdate = '"+jo.getString("log_sysdate")+"' and log_sysdate!='')" +
	    					" or (log_id = '"+jo.getString("log_id")+"' and log_id!='') or (log_shift = '"+jo.getString("log_shift")+"' and log_shift!=''))";
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				VipSearchInfo si = new VipSearchInfo();
				si.setNo(rs.getString(1).trim().substring(0, 7));
				si.setSno(rs.getString(2));
				si.setSta(rs.getString(3));
				si.setType(rs.getString(4));
				si.setGname(rs.getString(5));
				si.setEname(rs.getString(6));
				si.setIdcls(rs.getString(7));
				si.setIdno(rs.getString(8));
				si.setSex(rs.getString(9));
				si.setBirthday(rs.getString(10));
				si.setAddress(rs.getString(11));
				si.setFancy(rs.getString(12));
				si.setPhone(rs.getString(13));
				si.setRemark(rs.getString(14));
				si.setVinfo(rs.getString(15));
				si.setNeedpasswd(rs.getString(16));
				si.setPassword(rs.getString(17));
				si.setGt_id(rs.getInt(18)+"");
				si.setLog_hotelid(rs.getString(19));
				si.setLog_sysdate(rs.getString(20));
				si.setLog_id(rs.getString(21));
				si.setLog_name(rs.getString(22));
				si.setLog_shift(rs.getString(23));
				si.setLog_date(rs.getString(24));
				si.setPayment(rs.getBigDecimal(25)+"");
				si.setExpaned(rs.getBigDecimal(26)+"");
				si.setBanlance(rs.getBigDecimal(27)+"");
				si.setLast_hotelid(rs.getString(28));
				si.setLast_id(rs.getString(29));
				si.setLast_name(rs.getString(30));
				si.setLast_shift(rs.getString(31));
				si.setLast_date(rs.getString(32));
				si.setSaleid(rs.getString(33));
				si.setYbanlance(rs.getBigDecimal(34)+"");
				list.add(si);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			 try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	   
		return list;
	}
	
	/*
	 * 集团卡消费
	 */
	public HashMap<String, String> vip_post(JSONObject jo,String accnt_new,Integer gt_id,String hotelid)
	{
	
		 CallableStatement  cs = null;
		 HashMap<String, String> mp = null;
		 Connection conn = null;
		 
		 try {
			conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_xx_accnt_post(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			 cs.setString(1, jo.getString("op_id"));
			 cs.setString(2, jo.getString("op_shift"));
			 cs.setString(3, jo.getString("ip"));
			 cs.setString(4, accnt_new);
			 cs.setInt(5, 0);
			 cs.setString(6, jo.getString("ls_mode"));
			 cs.setString(7, jo.getString("ls_pccode"));
			 cs.setString(8, jo.getString("ref2"));
			 cs.setString(9, jo.getString("ref3").trim());
			 cs.setDouble(10, Double.valueOf(jo.getString("ld_amt")));
			 cs.setString(11, "");
			 cs.setString(12, "");
			 cs.setString(13, jo.getString("sysdate"));
			 cs.setInt(14, gt_id);
			 cs.setString(15, hotelid);
			 cs.setString(16, jo.getString("roomno"));
			 System.out.println("参数："+jo.getString("op_id")+","+jo.getString("op_shift")+","+jo.getString("ip")+","+accnt_new+","+jo.getString("ls_mode")+","+jo.getString("ls_pccode")+","+jo.getString("ref2")+","+jo.getString("ref3").trim()+","+Double.valueOf(jo.getString("ld_amt"))+","+jo.getString("sysdate"));
			
			 if(cs.execute())
			 {
				 mp = new HashMap<String, String>();
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 mp.put("code", rs.getString(1));
				 mp.put("errMsg", rs.getString(2));
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		
		 
		return mp;
	}
	
//	/*
//	 * 根据分店vipno获取对应集团vipno
//	 */
//	public String findAccnt_newByAccnt_old(String hotelid,String accnt_old) throws SQLException
//	{
//		Connection conn = DBPool.getPool().getConnection();
//		Statement st  = null;
//		ResultSet rs = null;
//		String accnt_new = null;
//	    String sql = "select accnt_new  from sync_accnt  where hotelid ='"+hotelid+"' and accnt_old='"+accnt_old+"' and class='vip_info'"; 
//	    st = conn.createStatement();
//	    rs = st.executeQuery(sql);
//	    if(rs.next())
//	    {
//	    	 accnt_new = rs.getString("accnt_new");
//	    }
//
//	    CommonTool.closeResultSet(rs);
//	    CommonTool.closeStatement(st);
//	    CommonTool.closeConnection(conn);
//		
//		
//		return accnt_new;
//	}
	
	/**
	 * 判断上传上来的vip卡是否在中央编过码
	 * @return true 编码过 false 未编码
	 */
	public boolean isVipCoded(String hotelid,String vip_no,String type)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		    boolean flag = false;
			
		    String sql = "select count(*) num from sync_accnt where accnt_old ='"+vip_no+"' and class='"+type+"' and hotelid='"+hotelid+"'"; 
		    int num;
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				rs.next();
				num = rs.getInt("num");
				if(num>0)
		    	{
					flag = true;
		    	}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    
	    	

		return flag;
	}
	
	//判断log_date的时间是否要更新
	public boolean isUpdateVipInfo(String accnt_old,String log_date,String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean flag = false;
	    String sql = "select count(*)  from sync_accnt a,vip_info b where a.hotelid ='"+hotelid+"' and a.accnt_old='"+accnt_old+"' and a.accnt_new=b.no and b.log_date<'"+log_date+"'"; 
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			int num = rs.getInt(1);
			if(num>0)
			{
				flag = true;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
		}
				
		
		return flag;
	}
	

	
	//判断log_date的时间是否要更新
	public boolean isDownloadVipInfo(String vipno_new,String log_date)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
		    String sql = "select count(*)  from vip_info  where no ='"+vipno_new+"' and log_date>'"+log_date+"'"; 
		    st = conn.createStatement();
		    rs = st.executeQuery(sql);
		    rs.next();
		    int num = rs.getInt(1);
		    	if(num>0)
		    	{
		    		flag = true;
		    	}
		    	
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	
	//根据主单号检索出主单记录
	public VipInfoBean findVipinfoByVno(String vip_no_new,String hotelid)
	{
			Connection conn = null;
			Statement st  = null;
			ResultSet rs = null;
			VipInfoBean rh = null;
			try {
				conn = DBPool.getPool().getConnection();
				System.out.println("select *  from vip_info  where no ='"+vip_no_new+"' and hotelid='"+hotelid+"'");
				String sql = "select *  from vip_info  where no ='"+vip_no_new+"' and hotelid='"+hotelid+"'"; 
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					rh = new VipInfoBean();
					rh.setNo(rs.getString("no").trim().substring(0, 7));
					rh.setSno(rs.getString("sno").trim());
					rh.setSta(rs.getString("sta").trim());
					rh.setType(rs.getString("type"));
					rh.setLyy_class(rs.getString("class"));
					rh.setAgreement(rs.getString("agreement"));
					rh.setGname(rs.getString("gname"));
					rh.setEname(rs.getString("ename"));
					rh.setPhone(rs.getString("phone"));
					rh.setRemark(rs.getString("remark"));
					rh.setVinfo(rs.getString("vinfo"));
					rh.setPassword(rs.getString("password"));
					rh.setLimit(rs.getBigDecimal("limit1")+"");
					rh.setDatebegin(rs.getString("datebegin"));
					rh.setDateend(rs.getString("dateend"));
				    rh.setAccnt(rs.getString("accnt"));
					rh.setGstno(rs.getString("gstno"));
					rh.setSaleid(rs.getString("saleid"));
					rh.setLog_id(rs.getString("log_id"));
					rh.setLog_date(rs.getString("log_date"));
					rh.setLogmark(rs.getInt("logmark")+"");
					rh.setNeedpasswd(rs.getString("needpasswd"));
					rh.setLog_ip(rs.getString("log_ip"));
					rh.setPayment(rs.getBigDecimal("payment")+"");
					rh.setExpaned(rs.getBigDecimal("expaned")+"");
					rh.setBanlance(rs.getBigDecimal("banlance")+"");
					rh.setYbanlance(rs.getBigDecimal("ybanlance")+"");
					rh.setAccntnumb(rs.getInt("accntnumb")+"");
					rh.setConnected(rs.getString("connected"));
					rh.setIdcls(rs.getString("idcls"));
					rh.setIdno(rs.getString("idno"));
					rh.setSex(rs.getString("sex"));
					rh.setBirthday(rs.getString("birthday"));
					rh.setAddress(rs.getString("address"));
					rh.setFancy(rs.getString("fancy"));
					
				}
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		    	
		return rh;
	}
	
	
	//根据主单号检索出主单记录
	public VipInfoBean findVipinfoByVnoOneU(String vip_no_new,String hotelid,String vip_no_old,String log_date)
	{
			Connection conn = null;
			Statement st  = null;
			ResultSet rs = null;
			VipInfoBean rh = null;
			try {
				conn = DBPool.getPool().getConnection();
				String sql = "select *  from vip_info  where no ='"+vip_no_new+"' and hotelid='"+hotelid+"' and log_date>'"+log_date+"'"; 
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					    rh = new VipInfoBean();
						rh.setNo(vip_no_old);
						rh.setSno(rs.getString("sno").trim());
						rh.setSta(rs.getString("sta").trim());
						rh.setType(rs.getString("type"));
						rh.setLyy_class(rs.getString("class"));
						rh.setAgreement(rs.getString("agreement"));
						rh.setGname(rs.getString("gname"));
						rh.setEname(rs.getString("ename"));
						rh.setPhone(rs.getString("phone"));
						rh.setRemark(rs.getString("remark"));
						rh.setVinfo(rs.getString("vinfo"));
						rh.setPassword(rs.getString("password"));
						rh.setLimit(rs.getBigDecimal("limit1")+"");
						rh.setDatebegin(rs.getString("datebegin"));
						rh.setDateend(rs.getString("dateend"));
					    rh.setAccnt(rs.getString("accnt"));
						rh.setGstno(rs.getString("gstno"));
						rh.setSaleid(rs.getString("saleid"));
						rh.setLog_id(rs.getString("log_id"));
						rh.setLog_date(rs.getString("log_date"));
						rh.setLogmark(rs.getInt("logmark")+"");
						rh.setNeedpasswd(rs.getString("needpasswd"));
						rh.setLog_ip(rs.getString("log_ip"));
						rh.setPayment(rs.getBigDecimal("payment")+"");
						rh.setExpaned(rs.getBigDecimal("expaned")+"");
						rh.setBanlance(rs.getBigDecimal("banlance")+"");
						rh.setYbanlance(rs.getBigDecimal("ybanlance")+"");
						rh.setAccntnumb(rs.getInt("accntnumb")+"");
						rh.setConnected(rs.getString("connected"));
						rh.setIdcls(rs.getString("idcls"));
						rh.setIdno(rs.getString("idno"));
						rh.setSex(rs.getString("sex"));
						rh.setBirthday(rs.getString("birthday"));
						rh.setAddress(rs.getString("address"));
						rh.setFancy(rs.getString("fancy"));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
				  try {
				    	CommonTool.closeResultSet(rs);
						CommonTool.closeStatement(st);
						CommonTool.closeConnection(conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		   
		  
		return rh;
	}
	
	//判断该集团卡的卡号sno是否已生成过集团卡
	public boolean isAbleBeGtCard(String sno,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "SELECT COUNT(*) FROM `vip_info`  where sno ='"+sno+"' and gt_id="+gt_id; 
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
		    int num = rs.getInt(1);
	    	if(num>0)
	    	{
	    		flag = true;
	    	}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			  try {
			    	CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return flag;
	}
	
	public boolean updateVipInfo(Integer gt_id,JSONArray ja_vipInfo,String no)
	{
		Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_b = "update vip_info set ";
			String sql_e = " where no='"+(no+gt_id)+"' and gt_id="+gt_id;
			JSONObject jo = ja_vipInfo.getJSONObject(0);
			Iterator it = jo.keys();
			StringBuilder sb = new StringBuilder();
			String key = null;
			while(it.hasNext())
			{
				key = it.next().toString();
				sb.append(key+"='"+jo.getString(key)+"',");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			String sql = sql_b+sb.toString()+ sql_e;
			System.out.println("update sql:"+sql);
			st = conn.createStatement();
			int num = st.executeUpdate(sql);
		    
	    	if(num>0)
	    	{
	    		flag = true;
	    	}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			  try {
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return flag;
		
	}
	
	
	//是否可以消费
	public HashMap<String,Object> isAbleConsume(Double amount,Integer gt_id,String no)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("code", 0);
		
//		boolean flag = true;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "select limit1,payment,expaned,banlance from vip_info where no='"+no+"' and gt_id="+gt_id;
			System.out.println("update sql:"+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next())
			{
				
				if((rs.getDouble(1)+rs.getDouble(2)-rs.getDouble(3)-amount)<0)
				{
					//不能消费
					mp.put("code", 1);
					mp.put("banlance", rs.getDouble(4));
				}else{
					mp.put("code", 0);
					
				}
			}else{
				//找不到该集团卡不能消费
				mp.put("code", 1);
			}
		    
	    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			  try {
				    CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return mp;
	}
	/*
	 * 根据集团号获取集团卡型
	 */
	public List<Vip_cardtype_rmcodeBean> searchVipCardType_web(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Vip_cardtype_rmcodeBean> list = new ArrayList<Vip_cardtype_rmcodeBean>();
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql = " SELECT a.id,a.descript1,a.rmcode,b.descript FROM vip_cardtype a LEFT JOIN room_pricecode b on a.rmcode=b.id and b.gt_id="+gt_id+"  where a.gt_id="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Vip_cardtype_rmcodeBean si = new Vip_cardtype_rmcodeBean();
				si.setId(rs.getString(1));
				si.setDescript(rs.getString(2).trim());
				si.setRmcode(rs.getString(3).trim());
				si.setRmcode_dec(rs.getString(4));
				list.add(si);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return list;
	}
	
	/*
	 * 根据集团号获取集团卡型
	 */
	public List<Vip_cardtypeBean> searchVipCardType(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Vip_cardtypeBean> list = new ArrayList<Vip_cardtypeBean>();
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql = "SELECT id,descript1,rmcode,poscode,exchangeRate,exchangeRateFlag FROM vip_cardtype where gt_id="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Vip_cardtypeBean si = new Vip_cardtypeBean();
				si.setId(rs.getString(1));
				si.setDescript(rs.getString(2).trim());
				si.setRmcode(rs.getString(3).trim());
				si.setPoscode(rs.getString(4));
				si.setExchangeRate(rs.getInt(5)+"");
				si.setExchangeRateFlag(rs.getString(6));
				list.add(si);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return list;
	}
	
	/*
	 * 根据集团号获取付款码，中文描述
	 */
	public List<Sys_charge_code> searchSys_charge_code(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Sys_charge_code> list = new ArrayList<Sys_charge_code>();
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "SELECT code,subcode,descript1,pccode FROM `sys_charge_code` where code<98 and code!=96 and code>=90 and codetype=1 and gt_id="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Sys_charge_code si = new Sys_charge_code();
				si.setCode(rs.getString(1));
				si.setSubcode(rs.getString(2));
				si.setDescript(rs.getString(3).trim());
				si.setPccode(rs.getString(4).trim());
				list.add(si);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return list;
	}
	
	/*
	 * 根据集团号获取房价码，中文描述
	 */
	public List<Room_priceCodeBean> searchRoom_pricecode(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Room_priceCodeBean> list = new ArrayList<Room_priceCodeBean>();
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql = "SELECT id,descript FROM room_pricecode where type='P' and gt_id="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Room_priceCodeBean si = new Room_priceCodeBean();
				si.setId(rs.getString(1));
				si.setDescript(rs.getString(2));
				list.add(si);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return list;
	}
	
	
	
	/*
	 * 根据卡号或分店号获取集团卡信息
	 */
	public Integer gainVipInfoListCounts(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		Integer counts = 0;
		List<VipSearchInfo> list = new ArrayList<VipSearchInfo>();
		try {
			conn = DBPool.getPool().getConnection();
			StringBuilder sql = new StringBuilder("SELECT count(*) from vip_info where gt_id = "+gt_id);
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			if(!"".equals(jo.getString("hotelid")))
			{
				sql.append(" and hotelid='"+jo.getString("hotelid").trim()+"'");
			}
			if(!"".equals(jo.getString("type")))
			{
				sql.append(" and type='"+jo.getString("type").trim()+"'");
			}
			if(!"".equals(jo.getString("search").trim()))
			{
				 sql.append(" and (sno='"+jo.getString("search").trim()+"' or gname='"+jo.getString("search").trim()+"' or phone='"+jo.getString("search").trim()+"')");
			}
			
//			if(!"".equals(jo.getString("search").trim()))
//			{
//				 sql.append(" and (sno='"+jo.getString("search").trim()+"' or gname='"+jo.getString("search").trim()+"' or phone='"+jo.getString("search").trim()+"')");
//			}
//	    if(!"".equals(jo.getString("sno")))
//	    {
//	    	sql.append(" and sno='"+jo.getString("sno").trim()+"'");
//	    }
//	    if(!"".equals(jo.getString("gname")))
//	    {
//	    	sql.append(" and gname='"+jo.getString("gname").trim()+"'");
//	    }
//	    if(!"".equals(jo.getString("phone")))
//	    {
//	    	sql.append(" and phone='"+jo.getString("phone").trim()+"'");
//	    }
//	    sql.append(" limit "+(json_data.getInt("page")-1)+","+json_data.getInt("line"));
			System.out.println("拼接后的sql:"+sql.toString());
			rs = st.executeQuery(sql.toString());
			rs.next();
			counts = rs.getInt(1);
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			 try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   
	   
		return counts;
	}
	
	/*
	 * 根据卡号或分店号获取集团卡信息
	 */
	public List<VipSearchInfo> gainVipInfoList(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<VipSearchInfo> list = new ArrayList<VipSearchInfo>();
		
		try {
			conn = DBPool.getPool().getConnection();
			StringBuilder sql = new StringBuilder("SELECT no,sno,sta,type,gname,ename,idcls,idno,sex,birthday,address,fancy,phone,a.remark,vinfo,needpasswd,password,a.gt_id,log_hotelid,log_sysdate,log_id,log_name,log_shift,log_date,payment,expaned,banlance,last_hotelid,last_id,last_name,last_shift,last_date,saleid,class,ybanlance,IFNULL(b.descript1,type) from vip_info a LEFT JOIN vip_cardtype b on a.type=b.id and b.gt_id="+gt_id+"  where a.gt_id = "+gt_id);
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			if(!"".equals(jo.getString("hotelid")))
			{
				sql.append(" and hotelid='"+jo.getString("hotelid").trim()+"'");
			}
			if(!"".equals(jo.getString("type")))
			{
				sql.append(" and a.type='"+jo.getString("type").trim()+"'");
			}
			if(!"".equals(jo.getString("search").trim()))
			{
				 sql.append(" and (sno='"+jo.getString("search").trim()+"' or gname='"+jo.getString("search").trim()+"' or phone='"+jo.getString("search").trim()+"')");
			}
			
   
//	    if(!"".equals(jo.getString("sno")))
//	    {
//	    	sql.append(" and sno='"+jo.getString("sno").trim()+"'");
//	    }
//	    if(!"".equals(jo.getString("gname")))
//	    {
//	    	sql.append(" and gname='"+jo.getString("gname").trim()+"'");
//	    }
//	    if(!"".equals(jo.getString("phone")))
//	    {
//	    	sql.append(" and phone='"+jo.getString("phone").trim()+"'");
//	    }
			sql.append(" limit "+((jo.getInt("page")-1)*jo.getInt("line"))+","+jo.getInt("line"));
			System.out.println("拼接后的sql:"+sql.toString());
			rs = st.executeQuery(sql.toString());
			DecimalFormat df = new DecimalFormat("#.00");
			while(rs.next())
			{
				VipSearchInfo si = new VipSearchInfo();
				si.setNo(rs.getString(1).trim().substring(0, 7));
				si.setSno(rs.getString(2));
				si.setSta(rs.getString(3));
				si.setType(rs.getString(4));
				si.setGname(rs.getString(5));
				si.setEname(rs.getString(6));
				si.setIdcls(rs.getString(7));
				si.setIdno(rs.getString(8));
				si.setSex(rs.getString(9));
				si.setBirthday(rs.getString(10));
				si.setAddress(rs.getString(11));
				si.setFancy(rs.getString(12));
				si.setPhone(rs.getString(13));
				si.setRemark(rs.getString(14));
				si.setVinfo(rs.getString(15));
				si.setNeedpasswd(rs.getString(16));
				si.setPassword(rs.getString(17));
				si.setGt_id(rs.getInt(18)+"");
				si.setLog_hotelid(rs.getString(19));
				si.setLog_sysdate(rs.getString(20));
				si.setLog_id(rs.getString(21));
				si.setLog_name(rs.getString(22));
				si.setLog_shift(rs.getString(23));
				si.setLog_date(rs.getString(24));
				if(".00".equals(df.format(rs.getBigDecimal(25)).trim()))
				{
					si.setPayment("0.00");
				}else{
					si.setPayment(df.format(rs.getBigDecimal(25)));
				}
				if(".00".equals(df.format(rs.getBigDecimal(26)).trim()))
				{
					si.setExpaned("0.00");
				}else{
					si.setExpaned(df.format(rs.getBigDecimal(26)));
				}
				if(".00".equals(df.format(rs.getBigDecimal(27)).trim()))
				{
					si.setBanlance("0.00");
				}else{
					si.setBanlance(df.format(rs.getBigDecimal(27)));
				}
				si.setLast_hotelid(rs.getString(28));
				si.setLast_id(rs.getString(29));
				si.setLast_name(rs.getString(30));
				si.setLast_shift(rs.getString(31));
				si.setLast_date(rs.getString(32));
				si.setSaleid(rs.getString(33));
				si.setClass1(rs.getString(34));
				si.setYbanlance(rs.getBigDecimal(35).setScale(0, BigDecimal.ROUND_HALF_UP)+"");
				si.setDescript(rs.getString(36));
				list.add(si);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
		return list;
	}
	
	/**
	 * 判断上传上来的vip卡是否在中央编过码
	 * @return true 编码过 false 未编码
	 */
	public boolean isChargeCodeExist(Integer gt_id,String pccode)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		    boolean flag = false;
			
		    String sql = "select count(*) num from sys_charge_code where pccode ='"+pccode.trim()+"' and gt_id="+gt_id; 
		    int num;
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				rs.next();
				num = rs.getInt("num");
				if(num>0)
		    	{
					flag = true;
		    	}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    
	    	

		return flag;
	}
	
	/*
	 * 根据卡号获取集团卡信息
	 */
	public List<VipCardDetail> getVipInfo(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<VipCardDetail> list = new ArrayList<VipCardDetail>();
		
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "SELECT a.no,a.sno,a.sta,a.type,a.gname,a.ename,a.idcls,a.idno,a.sex,a.birthday,a.address,a.fancy,a.phone,a.remark,a.vinfo,a.needpasswd,a.password,a.gt_id,a.log_hotelid,a.log_sysdate,a.log_id,a.log_name,a.log_shift,a.log_date,a.payment,a.expaned,a.banlance,a.last_hotelid,a.last_id,a.last_name,a.last_shift,a.last_date,a.saleid,b.descript1,c.name,d.descript1 from vip_info a,sys_code b,hotelid c,vip_cardtype d where a.idcls = b.id and a.hotelid = c.id and b.class='idcls' and a.type =d.id and sno='"+jo.getString("sno").trim()+"' and a.gt_id ="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			DecimalFormat df = new DecimalFormat("#.00");
			while(rs.next())
			{
				VipCardDetail si = new VipCardDetail();
				si.setNo(rs.getString(1).trim().substring(0, 7));
				si.setSno(rs.getString(2));
				si.setSta(rs.getString(3));
				si.setType(rs.getString(4));
				si.setGname(rs.getString(5));
				si.setEname(rs.getString(6));
				si.setIdcls(rs.getString(7));
				si.setIdno(rs.getString(8));
				si.setSex(rs.getString(9));
				si.setBirthday(rs.getString(10));
				si.setAddress(rs.getString(11));
				si.setFancy(rs.getString(12));
				si.setPhone(rs.getString(13));
				si.setRemark(rs.getString(14));
				si.setVinfo(rs.getString(15));
				si.setNeedpasswd(rs.getString(16));
				si.setPassword(rs.getString(17));
				si.setGt_id(rs.getInt(18)+"");
				si.setLog_hotelid(rs.getString(19));
				si.setLog_sysdate(rs.getString(20));
				si.setLog_id(rs.getString(21));
				si.setLog_name(rs.getString(22));
				si.setLog_shift(rs.getString(23));
				si.setLog_date(rs.getString(24));
				si.setPayment(df.format(rs.getBigDecimal(25)));
				si.setExpaned(df.format(rs.getBigDecimal(26)));
				si.setBanlance(df.format(rs.getBigDecimal(27)));
				si.setLast_hotelid(rs.getString(28));
				si.setLast_id(rs.getString(29));
				si.setLast_name(rs.getString(30));
				si.setLast_shift(rs.getString(31));
				si.setLast_date(rs.getString(32));
				si.setSaleid(rs.getString(33));
				si.setIdTypeDec(rs.getString(34).trim());
				si.setHotelName(rs.getString(35).trim());
				si.setTypeDec(rs.getString(36).trim());
				list.add(si);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
		return list;
	}
	
	/*
	 * 根据账号，集团号获取积分余额
	 */
	public Double getYbanlanceByaccnt(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		Double jf = 0.00;
		
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "SELECT ybanlance from vip_info  where no='"+(jo.getString("accnt").trim()+gt_id)+"' and gt_id ="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			DecimalFormat df = new DecimalFormat("#.00");
			if(rs.next())
			{
				jf = rs.getDouble(1);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
		return jf;
	}
	
	/*
	 * 根据集团号，分店号以及出生年月日获取集团卡信息
	 */
	public List<VipSearchInfo> searchVipInfoByBirthday(JSONObject jo,Integer gt_id,String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<VipSearchInfo> list = new ArrayList<VipSearchInfo>();
	    String sql = "SELECT no,sno,sta,type,gname,ename,idcls,idno,sex,birthday,address,fancy,phone,remark,vinfo,needpasswd,password,gt_id,log_hotelid,log_sysdate,log_id,log_name,log_shift,log_date,payment,expaned,banlance,last_hotelid,last_id,last_name,last_shift,last_date,saleid,ybanlance from vip_info where gt_id = "
	    	+gt_id+" and hotelid='"+hotelid+"' and birthday like '%-"+jo.getString("birthday").trim()+"%' and birthday not like '1900%' and sta='I'";
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				VipSearchInfo si = new VipSearchInfo();
				si.setNo(rs.getString(1).trim().substring(0, 7));
				si.setSno(rs.getString(2));
				si.setSta(rs.getString(3));
				si.setType(rs.getString(4));
				si.setGname(rs.getString(5));
				si.setEname(rs.getString(6));
				si.setIdcls(rs.getString(7));
				si.setIdno(rs.getString(8));
				si.setSex(rs.getString(9));
				si.setBirthday(rs.getString(10));
				si.setAddress(rs.getString(11));
				si.setFancy(rs.getString(12));
				si.setPhone(rs.getString(13));
				si.setRemark(rs.getString(14));
				si.setVinfo(rs.getString(15));
				si.setNeedpasswd(rs.getString(16));
				si.setPassword(rs.getString(17));
				si.setGt_id(rs.getInt(18)+"");
				si.setLog_hotelid(rs.getString(19));
				si.setLog_sysdate(rs.getString(20));
				si.setLog_id(rs.getString(21));
				si.setLog_name(rs.getString(22));
				si.setLog_shift(rs.getString(23));
				si.setLog_date(rs.getString(24));
				si.setPayment(rs.getBigDecimal(25)+"");
				si.setExpaned(rs.getBigDecimal(26)+"");
				si.setBanlance(rs.getBigDecimal(27)+"");
				si.setLast_hotelid(rs.getString(28));
				si.setLast_id(rs.getString(29));
				si.setLast_name(rs.getString(30));
				si.setLast_shift(rs.getString(31));
				si.setLast_date(rs.getString(32));
				si.setSaleid(rs.getString(33));
				si.setYbanlance(rs.getBigDecimal(34)+"");
				list.add(si);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			 try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	   
		return list;
	}
}
