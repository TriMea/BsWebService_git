package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dl.datasource.DBPool;
import com.dl.pojo.Jf_def;
import com.dl.pojo.Jf_def_shr;
import com.dl.pojo.Vip_cardtype_rmcodeBean;
import com.dl.util.CommonTool;

public class JfDefDao {

	/*
	 * 单例模式
	 */
	private JfDefDao() {}  
    //已经自行实例化   
    private static final JfDefDao jfDefDao = new JfDefDao();  
    //静态工厂方法   
    public static JfDefDao getInstance() {  
        return jfDefDao;  
    }  
    
    /*
	 * 根据集团号获取积分比例
	 */
	public List<Jf_def> searchJfdef(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Jf_def> list = new ArrayList<Jf_def>();
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql = "SELECT a.code,a.type,a.name,a.amount,b.descript1 FROM jf_def a LEFT JOIN vip_cardtype b on a.code=b.id and b.gt_id="+gt_id+" where a.gt_id="+gt_id;
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Jf_def si = new Jf_def();
				si.setCode(rs.getString(1));
				si.setType(rs.getString(2).trim());
				si.setName(rs.getString(3).trim());
				si.setAmount(rs.getBigDecimal(4));
				si.setDescript(rs.getString(5));
				si.setGt_id(gt_id);
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
	 * 根据集团号和卡型获取积分比例
	 */
	public List<Jf_def_shr> searchJfdefByCardType(Integer gt_id,String cardType)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Jf_def_shr> list = new ArrayList<Jf_def_shr>();
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql = "SELECT code,type,name,amount FROM jf_def where gt_id="+gt_id+" and code='"+cardType.trim()+"'";
			System.out.println("搜索消息："+sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Jf_def_shr si = new Jf_def_shr();
				si.setCode(rs.getString(1));
				si.setType(rs.getString(2).trim());
				si.setName(rs.getString(3).trim());
				si.setAmount(rs.getBigDecimal(4)+"");
				
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
    
    
}
