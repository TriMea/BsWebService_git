package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.Jf_detailBean;
import com.dl.pojo.Vip_info_logBean;
import com.dl.util.CommonTool;

public class LogRecordDao {

	/*
	 * 单例模式
	 */
	private LogRecordDao() {}  
    //已经自行实例化   
    private static final LogRecordDao logRecordDao = new LogRecordDao();  
    //静态工厂方法   
    public static LogRecordDao getInstance() {  
        return logRecordDao;  
    }  
    
    
    /*
	 * 根据集团号和账号积分明细
	 */
	public List<Vip_info_logBean> searchVipLog(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<Vip_info_logBean> list = new ArrayList<Vip_info_logBean>();
	    String sql = "SELECT tablename,columname,colum,accnt,log_date,log_id,old,new,refer,log_hotelid FROM `table_log` where gt_id = "
	    	+gt_id+" and accnt='"+(jo.getString("accnt").trim()+gt_id)+"'";
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Vip_info_logBean si = new Vip_info_logBean();
				si.setTableName(rs.getString(1).trim());
				si.setColumName(rs.getString(2).trim());
				si.setColum(rs.getString(3));
				si.setAccnt(rs.getString(4).trim().substring(0, 7));
				si.setLog_date(rs.getString(5));
				si.setLog_id(rs.getString(6));
				si.setOldVal(rs.getString(7));
				si.setNewVal(rs.getString(8));
				si.setRefer(rs.getString(9));
				si.setLog_hotelid(rs.getString(10));
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
}
