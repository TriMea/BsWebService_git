package com.dl.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.Jf_detailBean;
import com.dl.pojo.Jf_useBean;
import com.dl.pojo.Jf_detail_new;
import com.dl.util.CommonTool;

public class JfDetailDao {

	/*
	 * 单例模式
	 */
	private JfDetailDao() {}  
    //已经自行实例化   
    private static final JfDetailDao jfDetailDao = new JfDetailDao();  
    //静态工厂方法   
    public static JfDetailDao getInstance() {  
        return jfDetailDao;  
    }  
    
    /*
	 * 添加jf_detail信息
	 */
	public synchronized boolean addJfDetail(JSONObject jo)
	{
		CallableStatement  cs = null;
		boolean flag = false;
		
		 Connection conn = null;
	     try {
			conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_xx_jf_update_crs_lyy(?,?,?,?,?,?,?,?,?,?,?)}");
			 cs.setInt(1, Integer.valueOf(jo.getString("gt_id")));
			 cs.setString(2, jo.getJSONArray("requestData").getJSONObject(0).getString("cardno").trim()+jo.getString("gt_id"));
			 cs.setString(3, jo.getString("hotelid").trim());
			 cs.setString(4, dateFormater(jo.getJSONArray("requestData").getJSONObject(0).getString("sysdate").trim()));
			 cs.setBigDecimal(5,new BigDecimal(jo.getJSONArray("requestData").getJSONObject(0).getString("rm").trim()));
			 cs.setBigDecimal(6,new BigDecimal(jo.getJSONArray("requestData").getJSONObject(0).getString("fb").trim()));
			 cs.setBigDecimal(7,new BigDecimal(jo.getJSONArray("requestData").getJSONObject(0).getString("en").trim()));
			 cs.setBigDecimal(8,new BigDecimal(jo.getJSONArray("requestData").getJSONObject(0).getString("ot").trim()));
			 cs.setString(9,jo.getJSONArray("requestData").getJSONObject(0).getString("billno").trim());
			 cs.setString(10,jo.getJSONArray("requestData").getJSONObject(0).getString("log_date").trim());
			 cs.setString(11,jo.getJSONArray("requestData").getJSONObject(0).getString("faccnt").trim());
//			 System.out.println("参数："+jo.getString("op_id")+","+jo.getJSONObject("requestData").getString("cardno").trim()+jo.getInt("gt_id")
//					 +","+jo.getString("hotelid").trim()+","+jo.getJSONObject("requestData").getString("sysdate").trim()+","+new BigDecimal(jo.getJSONObject("requestData").getString("rm").trim())+","+new BigDecimal(jo.getJSONObject("requestData").getString("fb").trim())
//			 +","+new BigDecimal(jo.getJSONObject("requestData").getString("en").trim())+","+new BigDecimal(jo.getJSONObject("requestData").getString("ot").trim()));
			 if(cs.execute())
			 {
				 flag = true;
			 }
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}

		return flag;
	}
	
	/*
	 * 根据集团号和账号积分明细
	 */
	public List<Jf_detailBean> searchJfdetail(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<Jf_detailBean> list = new ArrayList<Jf_detailBean>();
	    String sql = "SELECT sysdate,hotelid,accnt,type,rm,fb,en,ot,dtotal,total,gt_id from jf_detail where gt_id = "
	    	+gt_id+" and accnt='"+(jo.getString("accnt").trim()+gt_id)+"'";
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Jf_detailBean si = new Jf_detailBean();
				si.setSysdate(rs.getString(1).trim());
				si.setHotelid(rs.getString(2).trim());
				si.setAccnt(rs.getString(3).trim().substring(0, 7));
				si.setType(rs.getString(4));
				si.setRm(rs.getBigDecimal(5)+"");
				si.setFb(rs.getBigDecimal(6)+"");
				si.setEn(rs.getBigDecimal(7)+"");
				si.setOt(rs.getBigDecimal(8)+"");
				si.setDtotal(rs.getBigDecimal(9)+"");
				si.setTotal(rs.getBigDecimal(10)+"");
				si.setGt_id(rs.getInt(11)+"");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   
		return list;
	}
	
	/*
	 * 根据sysdate和accnt查询指定卡的积分兑换明细
	 */
	public List<Jf_detail_new> searchJfdetailNew(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<Jf_detail_new> list = new ArrayList<Jf_detail_new>();
	    String sql = "SELECT * from jf_detail_new where sysdate = '"+jo.getJSONArray("requestData").getJSONObject(0).getString("sysdate")+"' and accnt='"+(jo.getString("no").trim()+gt_id)+"' and hotelid='"+jo.getJSONArray("requestData").getJSONObject(0).getString("hotelid").trim()+"'";
//	    	+gt_id+" and accnt='"+(jo.getString("accnt").trim()+gt_id)+"'";
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Jf_detail_new si = new Jf_detail_new();
				si.setSysdate(rs.getString(1).trim());
				si.setHotelid(rs.getString(2).trim());
				si.setAccnt(rs.getString(3).trim().substring(0, 7));
				si.setFaccnt(rs.getString(4).trim());
				si.setBillno(rs.getString(5).trim());
				si.setRm(rs.getBigDecimal(6)+"");
				si.setFb(rs.getBigDecimal(7)+"");
				si.setEn(rs.getBigDecimal(8)+"");
				si.setOt(rs.getBigDecimal(9)+"");
				si.setTotal(rs.getBigDecimal(10)+"");
				si.setJf_rm(rs.getInt(11)+"");
				si.setJf_fb(rs.getInt(12)+"");
				si.setJf_en(rs.getInt(13)+"");
				si.setJf_ot(rs.getInt(14)+"");
				si.setJf_total(rs.getInt(15)+"");
				si.setJf_rm_def(rs.getBigDecimal(16)+"");
				si.setJf_fb_def(rs.getBigDecimal(17)+"");
				si.setJf_en_def(rs.getBigDecimal(18)+"");
				si.setJf_ot_def(rs.getBigDecimal(19)+"");
				si.setLog_date(rs.getString(20).trim());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   
		return list;
	}
    
    
	private String dateFormater(String strDate)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(date);
	}
    
}
