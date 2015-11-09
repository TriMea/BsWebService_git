package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.dl.datasource.DBPool;
import com.dl.pojo.VipInfoBean;
import com.dl.util.CommonTool;

import net.sf.json.JSONObject;

public class UcLoginDao {

	/*
	 * 单例模式
	 */
	private UcLoginDao() {}  
    //已经自行实例化   
    private static final UcLoginDao ucLoginDao = new UcLoginDao();  
    //静态工厂方法   
    public static UcLoginDao getInstance() {  
        return ucLoginDao;  
    }  
    
    public HashMap<String, Object> validate_login(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("op", 1);
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "select count(*),op_name,gt_id from uc_login where username='"+jo.getString("username").trim()+"' and password='"+jo.getString("password").trim()+"' and locked='F'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			if(rs.getInt(1)==1)
			{
				map.put("op", 0);
				map.put("op_name", rs.getString(2).trim());
				map.put("username", jo.getString("username").trim());
				map.put("gt_id", rs.getInt(3));
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
    	
		
	return map;
    }
    /*
     * 修改登录密码
     */
    public boolean updatePwd(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean isSucceeed = false;
//		ResultSet rs = null;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "update  uc_login set password='"+jo.getString("password").trim()+"' where username='"+jo.getString("username").trim()+"'";
			st = conn.createStatement();
			int rows = st.executeUpdate(sql);
			if(rows>0)
			{
				isSucceeed = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
		
	return isSucceeed;
    }
}
