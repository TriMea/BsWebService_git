package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dl.datasource.DBPool;
import com.dl.pojo.HotelidBean;
import com.dl.pojo.VipInfoBean;
import com.dl.util.CommonTool;

public class VipCardDao {
	/*
	 * 单例模式
	 */
	private VipCardDao() {}  
    //已经自行实例化   
    private static final VipCardDao vipCardDao = new VipCardDao();  
    //静态工厂方法   
    public static VipCardDao getInstance() {  
        return vipCardDao;  
    }  
    
    /*
	 * 根据集团号获取分店列表
	 */
	public List<HotelidBean> findHotleidListByGtId(Integer gt_id)
	{
			Connection conn = null;
			Statement st  = null;
			ResultSet rs = null;
			HotelidBean rh = null;
			List<HotelidBean> list = new ArrayList<HotelidBean>();
			try {
				conn = DBPool.getPool().getConnection();
				String sql = "select id,name from hotelid where gt_id="+gt_id;
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while(rs.next())
				{
				    rh = new HotelidBean();
					rh.setHotelid(rs.getString(1).trim());
					rh.setName(rs.getString(2).trim());
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
