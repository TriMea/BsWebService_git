package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.Rep_CountsBean;
import com.dl.pojo.Rep_jf_use;
import com.dl.util.CommonTool;

public class Rep_pmsDao {

	/*
	 * 单例模式
	 */
	private Rep_pmsDao() {}  
    //已经自行实例化   
    private static final Rep_pmsDao rep_pmsDao = new Rep_pmsDao();  
    //静态工厂方法   
    public static Rep_pmsDao getInstance() {  
        return rep_pmsDao;  
    }  
    
    /*
	 * 根据sysdate区间和hotelid获取集团卡积分兑换信息
	 */
	public  List<Rep_jf_use> getRepJfUse(JSONObject jo)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
	    List<Rep_jf_use> list = new ArrayList<Rep_jf_use>();
		
		try {
			conn = DBPool.getPool().getConnection(); 
			st = conn.createStatement();
			String sql = "SELECT b.sno,a.accnt,b.gname,a.id,c.descript1,b.sta,a.used,a.amount,a.ref1,a.ref2,a.sysdate,a.log_id,a.log_date,a.last_id,a.last_date,a.log_shift,a.hotelid FROM `jf_use` a,vip_info b,vip_cardtype c where a.accnt=b.`no` and b.type=c.id  and a.sysdate>='"+jo.getJSONArray("requestData").getJSONObject(0).getString("bsysdate")+"' and a.sysdate<='"+jo.getJSONArray("requestData").getJSONObject(0).getString("esysdate")+"' and a.hotelid='"+jo.getString("hotelid").trim()+"' and a.id!=1 ";
			StringBuilder log_id = new StringBuilder("and ( 1=0 ");
		    StringBuilder log_shift = new StringBuilder("and ( 1=0 ");
		    JSONArray ja = jo.getJSONArray("requestData");
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
		    		log_shift.append(" or a.log_shift='"+str[i]+"'");
		    	}
		    	log_shift.append(")");
		    }
//			if("".endsWith(jo.getJSONArray("requestData").getJSONObject(0).getString("log_shift").trim()))
//			{
//				sql = "SELECT b.sno,a.accnt,b.gname,a.id,c.descript1,b.sta,a.used,a.amount,a.ref1,a.ref2,a.sysdate,a.log_id,a.log_date,a.last_id,a.last_date,a.log_shift,a.hotelid FROM `jf_use` a,vip_info b,vip_cardtype c where a.accnt=b.`no` and b.type=c.id  and a.sysdate>='"+jo.getJSONArray("requestData").getJSONObject(0).getString("bsysdate")+"' and a.sysdate<='"+jo.getJSONArray("requestData").getJSONObject(0).getString("esysdate")+"' and a.hotelid='"+jo.getString("hotelid").trim()+"' and a.id!=1";
//			}else{
//				sql = "SELECT b.sno,a.accnt,b.gname,a.id,c.descript1,b.sta,a.used,a.amount,a.ref1,a.ref2,a.sysdate,a.log_id,a.log_date,a.last_id,a.last_date,a.log_shift,a.hotelid FROM `jf_use` a,vip_info b,vip_cardtype c where a.accnt=b.`no` and b.type=c.id  and a.sysdate>='"+jo.getJSONArray("requestData").getJSONObject(0).getString("bsysdate")+"' and a.sysdate<='"+jo.getJSONArray("requestData").getJSONObject(0).getString("esysdate")+"' and a.hotelid='"+jo.getString("hotelid").trim()+"' and a.id!=1 and a.log_shift="+Integer.valueOf(jo.getJSONArray("requestData").getJSONObject(0).getString("log_shift"));
//			}
           
//			String sql = "select sysdate,day,b.name,a.hotelid from rep_hotel_all a,hotelid b where a.hotelid=b.id and class='099999' and sysdate>='"+jo.getJSONArray("requestData").getJSONObject(0).getString("bsysdate")+"' and sysdate<='"+jo.getJSONArray("requestData").getJSONObject(0).getString("esysdate")+"' and a.gt_id='"+Integer.valueOf(jo.getString("gt_id").trim())+"' and hotelid='"+jo.getString("hotelid").trim()+"' order by sysdate desc";
			rs = st.executeQuery(sql+log_id.toString()+log_shift.toString());
			DecimalFormat df = new DecimalFormat("#.00");
		    while(rs.next())
		    {
		    	Rep_jf_use rh = new Rep_jf_use();
		    	rh.setSno(rs.getString(1).trim());
		    	rh.setAccnt(rs.getString(2).trim().substring(0, 7));
		    	rh.setGname(rs.getString(3).trim());
		    	rh.setId(rs.getInt(4)+"");
		    	rh.setDescript1(rs.getString(5).trim());
		    	rh.setSta(rs.getString(6).trim());
		    	rh.setUsed(rs.getBigDecimal(7)+"");
		    	rh.setAmount(rs.getBigDecimal(8)+"");
		    	rh.setRef1(rs.getString(9).trim());
		    	rh.setRef2(rs.getString(10));
		    	rh.setSysdate(rs.getString(11).trim());
		    	rh.setLog_id(rs.getString(12));
		    	rh.setLog_date(rs.getString(13));
		    	rh.setLast_id(rs.getString(14));
		    	rh.setLast_date(rs.getString(15));
		    	rh.setLog_shift(rs.getString(16));
		    	rh.setHotelid(rs.getString(17).trim());
//		    	if(".00".equals(df.format(rs.getBigDecimal(2)).trim()))
//		    	{
//		    		rh.setDay("0.00");
//		    	}else{
//		    		rh.setDay(df.format(rs.getBigDecimal(2)));
//		    	}
//		    	rh.setName(rs.getString(3));
//		    	rh.setHotelid(rs.getString(4));
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
}
