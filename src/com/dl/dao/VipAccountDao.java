package com.dl.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.dl.datasource.DBPool;

import com.dl.pojo.VipAccountBean;
import com.dl.util.CommonTool;


public class VipAccountDao {

	/*
	 * 单例模式
	 */
	private VipAccountDao() {}  
    //已经自行实例化   
    private static final VipAccountDao vipAccountDao = new VipAccountDao();  
    //静态工厂方法   
    public static VipAccountDao getInstance() {  
        return vipAccountDao;  
    }  
    
  //根据集团卡号检索出需要下载的账务记录
	public List<VipAccountBean> findVipAccountByAccnt(String accnt,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		
		try {
			conn = DBPool.getPool().getConnection();
		    String sql = "select *  from vip_account  where gt_id="+gt_id+" and accnt ='"+accnt+"'"; 
		    System.out.println("SQL_LYY："+sql);
		    st = conn.createStatement();
		    rs = st.executeQuery(sql);
//		    if(isNative)
//		    {
//		    	 while(rs.next())
//				    {
//		    		 VipAccountBean gf = new VipAccountBean();
//					    gf.setAccnt(accnt_old.trim().substring(0, 7));
//					    gf.setNumber(rs.getInt(2));
//					    gf.setSysdate(rs.getString(3));
//					    gf.setCode(rs.getString(4));
//					    gf.setSubcode(rs.getString(5));
//					    gf.setCharge(rs.getBigDecimal(6));
//					    gf.setCredit(rs.getBigDecimal(7));
//					    gf.setBalance(rs.getBigDecimal(8));
//					    gf.setSign(rs.getString(9));
//					    gf.setRoomno(rs.getString(10));
//					    gf.setRef1(rs.getString(11));
//					    gf.setRef2(rs.getString(12));
//					    gf.setRef3(rs.getString(13));
//					    gf.setTofrom(rs.getString(14));
//					    gf.setAccntof(rs.getString(15));
//					    gf.setBillno(rs.getString(16));
//					    gf.setLog_id(rs.getString(17));
//					    gf.setLog_shift(rs.getString(18));
//					    gf.setLog_ip(rs.getString(19));
//					    gf.setLog_date(rs.getString(20));
//					    gf.setLast_id(rs.getString(21));
//					    gf.setLast_shift(rs.getString(22));
//					    gf.setLast_ip(rs.getString(23));
//					    gf.setLast_date(rs.getString(24));
//					    gf.setHotelid(rs.getString(25));
//					    list.add(gf);
//					   
//				    }
//		    }else
//		    {
		    	 while(rs.next())
				    {
		    		 VipAccountBean gf = new VipAccountBean();
					    gf.setAccnt(rs.getString(1).trim().substring(0, 7));
					    gf.setNumber(rs.getInt(2)+"");
					    gf.setSysdate(rs.getString(3));
					    gf.setCode(rs.getString(4));
					    gf.setSubcode(rs.getString(5));
					    gf.setCharge(rs.getBigDecimal(6)+"");
					    gf.setCredit(rs.getBigDecimal(7)+"");
					    gf.setBalance(rs.getBigDecimal(8)+"");
					    gf.setSign(rs.getString(9));
					    gf.setRoomno(rs.getString(10));
					    gf.setRef1(rs.getString(11));
					    gf.setRef2(rs.getString(12));
					    gf.setRef3(rs.getString(13));
					    gf.setTofrom(rs.getString(14));
					    gf.setAccntof(rs.getString(15));
					    gf.setBillno(rs.getString(16));
					    gf.setLog_id(rs.getString(17));
					    gf.setLog_shift(rs.getString(18));
					    gf.setLog_ip(rs.getString(19));
					    gf.setLog_date(rs.getString(20));
					    gf.setLast_id(rs.getString(21));
					    gf.setLast_shift(rs.getString(22));
					    gf.setLast_ip(rs.getString(23));
					    gf.setLast_date(rs.getString(24));
					    gf.setHotelid(rs.getString(25));
					    gf.setGt_id(rs.getInt(26)+"");
					    list.add(gf);
					   
				    }
//		    }
				
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
	
	
	//根据不定条件查询交班报表所需数据
	public List<VipAccountBean> findVipAccount(JSONArray ja,Integer gt_id,String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		
		try {
			conn = DBPool.getPool().getConnection();
		    String head = "select a.*,b.sno,b.sta,b.gname from vip_account a,vip_info b  where a.accnt = b.no and a.sysdate>='"+ja.getJSONObject(0).getString("bdate").trim()+"' and a.sysdate<='"+ja.getJSONObject(0).getString("edate").trim()+"' and a.hotelid='"+hotelid.trim()+"' and a.gt_id="+gt_id+" ";
		    StringBuilder log_id = new StringBuilder("and ( 1=0 ");
		    StringBuilder log_shift = new StringBuilder("and ( 1=0 ");
		    StringBuilder mode = new StringBuilder("and ( 1=0 ");
		    if(!ja.getJSONObject(0).getString("log_id").contains(";"))
		    {
		    	log_id.replace(0, log_id.length(), "");
		    }else{
		    	String str[] = ja.getJSONObject(0).getString("log_id").split(";");
		    	for(int i=0;i<str.length;i++)
		    	{
		    		log_id.append(" or a.log_id='"+str[i]+"'");
		    	}
		    	log_id.append(")");
		    }
		    if(!ja.getJSONObject(0).getString("log_shift").contains(";"))
		    {
		    	log_shift.replace(0, log_shift.length(), "");
		    }else{
		    	String str[] = ja.getJSONObject(0).getString("log_shift").split(";");
		    	for(int i=0;i<str.length;i++)
		    	{
		    		log_shift.append(" or a.log_shift="+str[i]);
		    	}
		    	log_shift.append(")");
		    }
		    if(ja.getJSONObject(0).getString("mode").trim().contains("A"))
		    {//消费
		    	mode.append(" or a.code<90 )");
		    	
		    }else if(ja.getJSONObject(0).getString("mode").trim().contains("P")){
		    	//充值
		    	mode.append(" or a.code>=90 )");
		    }else{
		    	mode.replace(0, mode.length(), "");
		    }
		    String sql = head+log_id.toString()+log_shift.toString()+mode.toString();
		    System.out.println("SQL_LYY："+sql);
		    st = conn.createStatement();
		    rs = st.executeQuery(sql);

		    	 while(rs.next())
				    {
		    		 VipAccountBean gf = new VipAccountBean();
					    gf.setAccnt(rs.getString(1).trim().substring(0, 7));
					    gf.setNumber(rs.getInt(2)+"");
					    gf.setSysdate(rs.getString(3));
					    gf.setCode(rs.getString(4));
					    gf.setSubcode(rs.getString(5));
					    gf.setCharge(rs.getBigDecimal(6)+"");
					    gf.setCredit(rs.getBigDecimal(7)+"");
					    gf.setBalance(rs.getBigDecimal(8)+"");
					    gf.setSign(rs.getString(9));
					    gf.setRoomno(rs.getString(10));
					    gf.setRef1(rs.getString(11));
					    gf.setRef2(rs.getString(12));
					    gf.setRef3(rs.getString(13));
					    gf.setTofrom(rs.getString(14));
					    gf.setAccntof(rs.getString(15));
					    gf.setBillno(rs.getString(16));
					    gf.setLog_id(rs.getString(17));
					    gf.setLog_shift(rs.getString(18));
					    gf.setLog_ip(rs.getString(19));
					    gf.setLog_date(rs.getString(20));
					    gf.setLast_id(rs.getString(21));
					    gf.setLast_shift(rs.getString(22));
					    gf.setLast_ip(rs.getString(23));
					    gf.setLast_date(rs.getString(24));
					    gf.setHotelid(rs.getString(25));
					    gf.setGt_id(rs.getInt(26)+"");
					    gf.setSno(rs.getString(27));
					    gf.setSta(rs.getString(28));
					    gf.setGname(rs.getString(29));
					    list.add(gf);
					   
				    }
//		    }
				
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
	 * 判断是否是本店取消预授权
	 */
	public boolean isAbleCancel(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "SELECT hotelid FROM vip_account  where accnt ='"+(jo.getString("accnt").trim()+gt_id)+"' and number="+Integer.valueOf(jo.getString("number"))+" and gt_id="+gt_id; 
			st = conn.createStatement();
			
			rs = st.executeQuery(sql);
			rs.next();
			System.out.println("SQL:"+sql+" hotelid："+jo.getString("hotelid").trim()+"#"+rs.getString(1).trim());
		    if(jo.getString("hotelid").trim().equals(rs.getString(1).trim()))
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
	
	/*
	 * 判断本店是否已经取消过预授权
	 */
	public boolean isHaveCanceled(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "select IFNULL(billno,'null') from vip_account where accnt='"+(jo.getString("accnt").trim()+gt_id)+"' and number="+Integer.valueOf(jo.getString("number"))+" and gt_id="+gt_id;
//			String sql = "SELECT hotelid FROM vip_account  where accnt ='"+(jo.getString("accnt").trim()+gt_id)+"' and number="+Integer.valueOf(jo.getString("number"))+" and gt_id="+gt_id; 
			st = conn.createStatement();
			
			rs = st.executeQuery(sql);
			rs.next();
//			System.out.println("SQL:"+sql+" hotelid："+jo.getString("hotelid").trim()+"#"+rs.getString(1).trim());
		    if(!"null".equals(rs.getString(1).trim()))
		     	//取消过，不能进行预授权取消
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
	/*
	 * 获取最大number
	 */
	public synchronized Integer getMaxNumber(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		Integer maxNumber = 0;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "select  IFNULL(max(number)+1,1) num  from vip_account where accnt='"+(jo.getString("accnt").trim()+gt_id)+"' and gt_id="+gt_id; 
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
		    maxNumber = rs.getInt(1);
		    
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
		
		return maxNumber;
	}
	/*
	 *预授权取消1
	 */
	public synchronized boolean addAndUpdateVipAccount(JSONObject jo,Integer gt_id,Integer maxNumber)
	{
		Connection conn = null;
		Statement st  = null;
		//select  IFNULL(max(number)+1,1) num  from vip_account where accnt='"+(jo.getString("accnt").trim()+gt_id)
	    String hotelid = jo.getString("hotelid");
	    boolean flag = false;
	    try {
	    	conn = DBPool.getPool().getConnection();
	    	conn.createStatement();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			String sql = "insert into vip_account(accnt,number,sysdate,code,subcode,charge,credit,balance,sign,roomno,ref1,ref2,ref3,tofrom,accntof,billno,log_id,log_shift,log_ip,log_date,last_id,last_shift,last_ip,last_date,hotelid,gt_id) values('"+(jo.getString("accnt")+gt_id)+"',"+maxNumber
			+",'"+jo.getString("sysdate").trim()+"','89','6',"+new BigDecimal("-"+jo.getString("charge").trim())+",0"
			+",1,'','"+jo.getString("roomno").trim()
			+"','预授权取消','"+jo.getString("ref2").trim()+"','"+jo.getString("ref3").trim()+"','','','"+jo.getString("billno").trim()+"','"+jo.getString("log_id")+"','"+jo.getString("log_shift")+"','"+jo.getString("log_ip")
			+"','"+jo.getString("log_date")+"','"+jo.getString("log_id").trim()+"','"+jo.getString("log_shift").trim()+"','"+jo.getString("log_ip").trim()+"','"+jo.getString("log_date").trim()+"','"+jo.getString("hotelid").trim()+"',"+gt_id+")";
			System.out.println("sql:"+sql);
			st.addBatch(sql);
			st.addBatch(" update vip_account set billno='"+jo.getString("billno").trim()+"',last_id='"+jo.getString("log_id").trim()+"',last_shift='"+jo.getString("log_shift").trim()+"',last_ip='"+jo.getString("log_ip").trim()+"',last_date='"+jo.getString("log_date").trim()+"' where accnt='"+(jo.getString("accnt").trim()+gt_id)+"'and number="+Integer.valueOf(jo.getString("number"))+" and gt_id="+gt_id);
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
				// TODO Auto-generated catch block
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
	 *预授权取消2
	 */
	public synchronized void updateVipInfo(JSONObject jo,Integer gt_id)
	{
		CallableStatement  cs = null;
		 Connection conn = null;
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_xx_info_update(?)}");
			 cs.setString(1, (jo.getString("accnt").trim()+gt_id));
			 cs.execute();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
