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
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;

import com.dl.datasource.DBPool;
import com.dl.pojo.Rep_CountsBean;
import com.dl.pojo.Rep_CreditBean;
import com.dl.pojo.Rep_CreditChargeBean;
import com.dl.pojo.Rep_CreditCharge_TotalBean;
import com.dl.pojo.Rep_Gt_DayBean;
import com.dl.pojo.Rep_hotel_allBean;
import com.dl.pojo.RoomResourceCount;
import com.dl.util.CommonTool;

public class Rep_hotel_allDao {

	/*
	 * 单例模式
	 */
	private Rep_hotel_allDao() {}  
    //已经自行实例化   
    private static final Rep_hotel_allDao rep_hotel_allDao = new Rep_hotel_allDao();  
    //静态工厂方法   
    public static Rep_hotel_allDao getInstance() {  
        return rep_hotel_allDao;  
    }  
    
    /*
	 * 营业总表数据
	 */
	public synchronized boolean addRepHotel(JSONObject jo,JSONArray ja)
	{
		Connection conn = null;
		Statement st  = null;
		
	    boolean flag = false;
	    
		
		try {
			conn = DBPool.getPool().getConnection(); 
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("delete from rep_hotel_all where sysdate ='"+ja.getJSONObject(0).getString("sysdate").trim()+"' and hotelid='"+jo.getString("hotelid").trim()+"'");
			for(int i=0;i<ja.size();i++)
			{
				st.addBatch("insert into rep_hotel_all(hotelid,sysdate,order_,itemno,mode,class,descript1,descript2,rectype,toop,toclass,toclass1,day,month,year,show1,pmonth,lmonth,pyear,lyear,gt_id) values('"+jo.getString("hotelid").trim()+"','"+ja.getJSONObject(i).getString("sysdate").trim()+
						"','"+ja.getJSONObject(i).getString("order_")+"','"+ja.getJSONObject(i).getString("itemno")+"','"+ja.getJSONObject(i).getString("mode").trim()+"','"+ja.getJSONObject(i).getString("class_lyy").trim()
						+"','"+ja.getJSONObject(i).getString("descript1")+"','"+ja.getJSONObject(i).getString("descript2")+"','"+ja.getJSONObject(i).getString("rectype")
						+"','"+ja.getJSONObject(i).getString("toop")+"','"+ja.getJSONObject(i).getString("toclass")+"','"+ja.getJSONObject(i).getString("toclass1")+"',"+ja.getJSONObject(i).getDouble("day")
						+","+ja.getJSONObject(i).getDouble("month")+","+ja.getJSONObject(i).getDouble("year")+",'"+ja.getJSONObject(i).getString("show")+"',"+ja.getJSONObject(i).getDouble("pmonth")+
						","+ja.getJSONObject(i).getDouble("lmonth")+","+ja.getJSONObject(i).getDouble("pyear")+","+ja.getJSONObject(i).getDouble("lyear")+","+jo.getInt("gt_id")+")");
			}
			
			st.executeBatch();
			conn.commit();
			flag = true;
		} catch (SQLException e) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return flag;
	}
	
	
	/*
	 * 根据hotelid和sysdate获取营业数据
	 */
	public  List<Rep_hotel_allBean> getRepHotel(JSONObject jo)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
	    List<Rep_hotel_allBean> list = new ArrayList<Rep_hotel_allBean>();
		
		try {
			conn = DBPool.getPool().getConnection(); 
			st = conn.createStatement();
			String sql = "select itemno,class,descript1,day,month,lmonth,year,lyear from rep_hotel_all where show1='T' and class!='500' and hotelid='"+jo.getString("hotelid")+"' and sysdate='"+jo.getJSONArray("requestData").getJSONObject(0).getString("sysdate").trim()+"' order by class";
			rs = st.executeQuery(sql);
			DecimalFormat df = new DecimalFormat("#.00");
		    while(rs.next())
		    {
		    	Rep_hotel_allBean rh = new Rep_hotel_allBean();
		    	rh.setItemno(rs.getString(1));
		    	rh.setClass1(rs.getString(2));
		    	rh.setDescript1(rs.getString(3));
		    	if(".00".equals(df.format(rs.getBigDecimal(4)).trim()))
		    	{
		    		rh.setDay("");
		    	}else{
		    		rh.setDay(df.format(rs.getBigDecimal(4)));
		    	}
		    	if(".00".equals(df.format(rs.getBigDecimal(5)).trim()))
		    	{
		    		rh.setMonth("");
		    	}else{
		    		rh.setMonth(df.format(rs.getBigDecimal(5)));
		    	}
		    	if(".00".equals(df.format(rs.getBigDecimal(6)).trim()))
		    	{
		    		rh.setLmonth("");
		    	}else{
		    		rh.setLmonth(df.format(rs.getBigDecimal(6)));
		    	}
		    	if(".00".equals(df.format(rs.getBigDecimal(7)).trim()))
		    	{
		    		rh.setYear("");
		    	}else{
		    		rh.setYear(df.format(rs.getBigDecimal(7)));
		    	}
		    	if(".00".equals(df.format(rs.getBigDecimal(8)).trim()))
		    	{
		    		rh.setLyear("");
		    	}else{
		    		rh.setLyear(df.format(rs.getBigDecimal(8)));
		    	}
//		    	rh.setMonth(df.format(rs.getBigDecimal(5)));
		    	
		    	
		    	
		    	list.add(rh);
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
	 * 根据sysdate区间和hotelid获取营业数据
	 */
	public  List<Rep_CountsBean> getRepCounts(JSONObject jo)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
	    List<Rep_CountsBean> list = new ArrayList<Rep_CountsBean>();
		
		try {
			conn = DBPool.getPool().getConnection(); 
			st = conn.createStatement();
			String sql = "select sysdate,day,b.name,a.hotelid from rep_hotel_all a,hotelid b where a.hotelid=b.id and class='099999' and sysdate>='"+jo.getJSONArray("requestData").getJSONObject(0).getString("bsysdate")+"' and sysdate<='"+jo.getJSONArray("requestData").getJSONObject(0).getString("esysdate")+"' and a.gt_id='"+Integer.valueOf(jo.getString("gt_id").trim())+"' and hotelid='"+jo.getString("hotelid").trim()+"' order by sysdate desc";
			rs = st.executeQuery(sql);
			DecimalFormat df = new DecimalFormat("#.00");
		    while(rs.next())
		    {
		    	Rep_CountsBean rh = new Rep_CountsBean();
		    	rh.setSysdate(rs.getString(1).trim());
		    	if(".00".equals(df.format(rs.getBigDecimal(2)).trim()))
		    	{
		    		rh.setDay("0.00");
		    	}else{
		    		rh.setDay(df.format(rs.getBigDecimal(2)));
		    	}
		    	rh.setName(rs.getString(3));
		    	rh.setHotelid(rs.getString(4));
		    	list.add(rh);
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
	
	
	//根据日期和集团号获取集团日报表
	
	public List<Rep_Gt_DayBean> getRepGtDay(Integer gt_id,String sysdate)
	{
	
		 CallableStatement  cs = null;
		
		 Connection conn = null;
		 DecimalFormat df = new DecimalFormat("#.00");
		 List<Rep_Gt_DayBean> list = new ArrayList<Rep_Gt_DayBean>();
		 try {
			conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_gt_rep_lyy(?,?)}");
			 cs.setString(2, sysdate);
			 cs.setInt(1, gt_id);
			 if(cs.execute())
			 {
				
				 ResultSet rs = cs.getResultSet();
				 while(rs.next())
				 {
//					 new Thread().start();
					 Rep_Gt_DayBean rep_Gt_DayBean = new Rep_Gt_DayBean();
					 rep_Gt_DayBean.setItemno(rs.getString(1));
					 rep_Gt_DayBean.setItem(rs.getString(2));
					 if(rs.getBigDecimal(3)!=null)
					 {
						 if(".00".equals(df.format(rs.getBigDecimal(3)).trim()))
					      {
							 rep_Gt_DayBean.setDay("0.00");
					      }else{
					    	  rep_Gt_DayBean.setDay(df.format(rs.getBigDecimal(3)));
					      }
					 }else{
						  rep_Gt_DayBean.setDay("0.00");
					 }
					
					 if(rs.getBigDecimal(4)!=null)
					 {
						 if(".00".equals(df.format(rs.getBigDecimal(4)).trim()))
					      {
							 rep_Gt_DayBean.setMonth("0.00");
					      }else{
					    	  rep_Gt_DayBean.setMonth(df.format(rs.getBigDecimal(4)));
					      }
					 }else{
						 rep_Gt_DayBean.setMonth("0.00");
					 }
					 
					 if(rs.getBigDecimal(5)!=null)
					 {
						 if(".00".equals(df.format(rs.getBigDecimal(5)).trim()))
					      {
							 rep_Gt_DayBean.setLmonth("0.00");
					      }else{
					    	  rep_Gt_DayBean.setLmonth(df.format(rs.getBigDecimal(5)));
					      }
					 }else{
						 rep_Gt_DayBean.setLmonth("0.00");
					 }
					

					 rep_Gt_DayBean.setTag(rs.getString(6));
					 rep_Gt_DayBean.setFromclass(rs.getString(7));
					 list.add(rep_Gt_DayBean);
				 }
				 
				
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
		
		
		 
		return list;
	}
	
	
//根据日期，集团号或分店号获取贵宾卡充值明细表
	
	public List<Rep_CreditBean> getRepCredit(Integer gt_id,String bdate,String edate,String hotelid)
	{
	
		 CallableStatement  cs = null;
		
		 Connection conn = null;
		 DecimalFormat df = new DecimalFormat("#.00");
		 List<Rep_CreditBean> list = new ArrayList<Rep_CreditBean>();
		 try {
			conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_credit_rep_lyy(?,?,?,?)}");
			 cs.setString(1, bdate);
			 cs.setString(2, edate);
			 cs.setString(3, hotelid);
			 cs.setInt(4, gt_id);
			 if(cs.execute())
			 {
				
				 ResultSet rs = cs.getResultSet();
				 while(rs.next())
				 {
//					 new Thread().start();
					 Rep_CreditBean rep_CreditBean = new Rep_CreditBean();
					 rep_CreditBean.setGt_id(rs.getInt(1));
					 rep_CreditBean.setCname(rs.getString(2));
					 rep_CreditBean.setNo(rs.getString(3));
					 rep_CreditBean.setNumber(rs.getInt(4));
					 rep_CreditBean.setSysdate(rs.getString(5));
					 rep_CreditBean.setPccode(rs.getString(6));
					 rep_CreditBean.setSta(rs.getString(7));
					 rep_CreditBean.setGname(rs.getString(8));
					 rep_CreditBean.setSno(rs.getString(9));
					 rep_CreditBean.setRef1(rs.getString(10));
					 rep_CreditBean.setRef2(rs.getString(11));
					 rep_CreditBean.setRef3(rs.getString(12));
					 rep_CreditBean.setCredit(df.format(rs.getBigDecimal(13)));
					 rep_CreditBean.setLog_id(rs.getString(14));
					 rep_CreditBean.setLog_date(rs.getString(15));
					 rep_CreditBean.setFlag(rs.getString(16));
					 
					
					 list.add(rep_CreditBean);
				 }
				 
				
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
		
		
		 
		return list;
	}
	
	
//根据日期，集团号或分店号获取贵宾卡借贷统计
	
	public List<Rep_CreditCharge_TotalBean> getRepCreditChargeTotal(Integer gt_id,String bdate,String edate,String hotelid)
	{
	
		 CallableStatement  cs = null;
		
		 Connection conn = null;
		 DecimalFormat df = new DecimalFormat("#.00");
		 List<Rep_CreditCharge_TotalBean> list = new ArrayList<Rep_CreditCharge_TotalBean>();
		 try {
			conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_credit_charge_total_rep_lyy(?,?,?,?)}");
			 cs.setString(1, bdate);
			 cs.setString(2, edate);
			 cs.setString(3, hotelid);
			 cs.setInt(4, gt_id);
			 if(cs.execute())
			 {
				
				 ResultSet rs = cs.getResultSet();
				 while(rs.next())
				 {
//					 new Thread().start();
					 Rep_CreditCharge_TotalBean rep_CreditBean = new Rep_CreditCharge_TotalBean();
					
					 rep_CreditBean.setName(rs.getString(1));
					 rep_CreditBean.setPccode(rs.getString(2));
					 rep_CreditBean.setRef1(rs.getString(3));
					 rep_CreditBean.setMoney(df.format(rs.getBigDecimal(4)));
					 rep_CreditBean.setFlag(rs.getString(5));
					 list.add(rep_CreditBean);
				 }
				 
				
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
		
		
		 
		return list;
	}
	
	/*
	 * 根据sysdate区间和hotelid获取充值消费记录
	 */
	public  List<Rep_CreditChargeBean> getCreditChargeRecords(JSONObject jo)
	{
		 CallableStatement  cs = null;
			
		 Connection conn = null;
		 DecimalFormat df = new DecimalFormat("#.00");
		 List<Rep_CreditChargeBean> list = new ArrayList<Rep_CreditChargeBean>();
		 try {
			conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_rep_credit_charge_lyy(?,?,?,?,?)}");
			 cs.setString(1,jo.getJSONArray("requestData").getJSONObject(0).getString("type"));
			 cs.setString(2,jo.getJSONArray("requestData").getJSONObject(0).getString("bsysdate"));
			 cs.setString(3,jo.getJSONArray("requestData").getJSONObject(0).getString("esysdate"));
			 cs.setString(4,jo.getString("hotelid"));
			 cs.setInt(5, Integer.valueOf(jo.getString("gt_id")));
			 if(cs.execute())
			 {
				
				 ResultSet rs = cs.getResultSet();
				 while(rs.next())
				 {
//					 new Thread().start();
					 Rep_CreditChargeBean rep_CreditChargeBean = new Rep_CreditChargeBean();
					 rep_CreditChargeBean.setSno(rs.getString(1).trim());
					 rep_CreditChargeBean.setGname(rs.getString(2));
					 rep_CreditChargeBean.setSta(rs.getString(3).trim());
					 rep_CreditChargeBean.setAccnt(rs.getString(4).trim());
					 rep_CreditChargeBean.setNumber(rs.getInt(5)+"");
					 rep_CreditChargeBean.setSysdate(rs.getString(6).trim());
					 rep_CreditChargeBean.setCode(rs.getString(7));
					 rep_CreditChargeBean.setSubcode(rs.getString(8));
					 rep_CreditChargeBean.setCharge(rs.getBigDecimal(9)+"");
					 rep_CreditChargeBean.setCredit(rs.getBigDecimal(10)+"");
					 rep_CreditChargeBean.setBalance(rs.getBigDecimal(11)+"");
					 rep_CreditChargeBean.setSign(rs.getString(12));
					 rep_CreditChargeBean.setRoomno(rs.getString(13));
					 rep_CreditChargeBean.setRef1(rs.getString(14));
					 rep_CreditChargeBean.setRef2(rs.getString(15));
					 rep_CreditChargeBean.setRef3(rs.getString(16));
					 rep_CreditChargeBean.setTofrom(rs.getString(17));
					 rep_CreditChargeBean.setAccntof(rs.getString(18));
					 rep_CreditChargeBean.setBillno(rs.getString(19));
					 rep_CreditChargeBean.setLog_id(rs.getString(20));
					 rep_CreditChargeBean.setLog_shift(rs.getString(21));
					 rep_CreditChargeBean.setLog_ip(rs.getString(22));
					 rep_CreditChargeBean.setLog_date(rs.getString(23));
					 rep_CreditChargeBean.setLast_id(rs.getString(24));
					 rep_CreditChargeBean.setLast_shift(rs.getString(25));
					 rep_CreditChargeBean.setLast_ip(rs.getString(26));
					 rep_CreditChargeBean.setLast_date(rs.getString(27));
					 rep_CreditChargeBean.setHotelid(rs.getString(28));
					 rep_CreditChargeBean.setGt_id(rs.getInt(29)+"");
					 rep_CreditChargeBean.setHotelname(rs.getString(30));
					 
					 
//					 if(rs.getBigDecimal(3)!=null)
//					 {
//						 if(".00".equals(df.format(rs.getBigDecimal(3)).trim()))
//					      {
//							 rep_Gt_DayBean.setDay("0.00");
//					      }else{
//					    	  rep_Gt_DayBean.setDay(df.format(rs.getBigDecimal(3)));
//					      }
//					 }else{
//						  rep_Gt_DayBean.setDay("0.00");
//					 }
					
					
					 list.add(rep_CreditChargeBean);
				 }
				 
				
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

		return list;
	}
	
    
}
