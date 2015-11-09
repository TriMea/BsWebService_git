package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.JfToMoneyBean;
import com.dl.pojo.Vip_cardtype_rmcodeBean;
import com.dl.util.CommonTool;

public class JfToMoneyDao {

	/*
	 * 单例模式
	 */
	private JfToMoneyDao() {}  
    //已经自行实例化   
    private static final JfToMoneyDao jfToMoneyDao = new JfToMoneyDao();  
    //静态工厂方法   
    public static JfToMoneyDao getInstance() {  
        return jfToMoneyDao;  
    }  
	
	/*
	 * 根据集团号获取卡型所对应的积分换钱比率
	 * 
	 */
	public List<JfToMoneyBean> searchJfToMoney(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<JfToMoneyBean> list = new ArrayList<JfToMoneyBean>();
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql = " SELECT id,descript1,rmcode,exchangeRate,exchangeRateFlag FROM vip_cardtype   where gt_id="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				JfToMoneyBean si = new JfToMoneyBean();
				si.setId(rs.getString(1));
				si.setDescript(rs.getString(2).trim());
				si.setRoomcode(rs.getString(3).trim());
				si.setExchangeRate(rs.getInt(4)+"");
				si.setExchangeRateFlag(rs.getString(5));
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
     * 修改卡型积分兑换比率
     */
    public boolean updateJfToMoney(JSONObject jo,Integer gt_id)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean isSucceeed = false;
//		ResultSet rs = null;
		try {
			conn = DBPool.getPool().getConnection();
			String sql = "update vip_cardtype set exchangeRate="+jo.getInt("exchangeRate")+", exchangeRateFlag='"+jo.getString("exchangeRateFlag").trim()+"' where id='"+jo.getString("id").trim()+"' and gt_id="+gt_id;
			st = conn.createStatement();
			int rows = st.executeUpdate(sql);
			if(rows>0)
			{
				isSucceeed = true;
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	
	return isSucceeed;
    }
}
