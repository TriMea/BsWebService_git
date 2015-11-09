package com.dl.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.dl.datasource.DBPool;
import com.dl.util.CommonTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SystemSettingDao {

	/*
	 * 单例模式
	 */
	private SystemSettingDao() {}  
    //已经自行实例化   
    private static final SystemSettingDao systemSettingDao = new SystemSettingDao();  
    //静态工厂方法   
    public static SystemSettingDao getInstance() {  
        return systemSettingDao;  
    }  
    
    
    /*
     * 新增房价码
     */
    public boolean addRoomCode(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("insert into room_pricecode(id,type,used,descript,descript2,cat,mkt,src,rmpackage,commision,sequence,gt_id) values('"+jo.getString("id").trim()+"','P','T','"+jo.getString("descript").trim()+"','','','','','','',100,"+jo.getInt("gt_id")+")");
			if(rowCount>=1)
			{
				flag = true;
			}
				
		} catch (SQLException e) {
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

		return flag;
    }
    /*
     * 删除房价码
     */
    public boolean deleteRoomCode(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("delete FROM room_pricecode where id='"+jo.getString("id").trim()+"' and gt_id="+jo.getInt("gt_id"));
			if(rowCount>=1)
			{
				flag = true;
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
		return flag;
    }
    
    /*
     * 新增付款码
     */
    public boolean addSys_charge_code(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("insert into sys_charge_code(code,subcode,codetype,descript1,descript2,deptno,report,des,show1,pccode,rebate,hand,gt_id) values('"+jo.getString("code").trim()+"','"+jo.getString("subcode").trim()+"','1','"+jo.getString("descript").trim()+"','','','','','T','"+(jo.getString("code").trim()+jo.getString("subcode").trim())+"','F','T',"+jo.getInt("gt_id")+")");
			if(rowCount>=1)
			{
				flag = true;
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

		return flag;
    }
    /*
     * 删除付款码
     */
    public boolean deleteSys_charge_code(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("delete FROM sys_charge_code where pccode='"+jo.getString("pccode").trim()+"' and gt_id="+jo.getInt("gt_id"));
			if(rowCount>=1)
			{
				flag = true;
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
		return flag;
    }
    
    /*
     * 新增卡型
     */
    public boolean addVip_cardtype(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("insert into vip_cardtype(id,descript1,descript2,accnt,valid_days,rmcode,poscode,remark,sequence,extra,gt_id) values('"+jo.getString("id").trim()+"','"+jo.getString("descript").trim()+"','','T',365,'"+jo.getString("rmcode").trim()+"','','',100,'',"+jo.getInt("gt_id")+")");
			System.out.println("insert into vip_cardtype(id,descript1,descript2,accnt,valid_days,rmcode,poscode,remark,sequence,extra,gt_id) values('"+jo.getString("id").trim()+"','"+jo.getString("descript").trim()+"','','T',365,'"+jo.getString("rmcode").trim()+"','','',100,'',"+jo.getInt("gt_id")+")");
			if(rowCount>=1)
			{
				flag = true;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    /*
     * 删除卡型
     */
    public boolean deleteVip_cardtype(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("delete FROM vip_cardtype where id='"+jo.getString("id").trim()+"' and gt_id="+jo.getInt("gt_id"));
			if(rowCount>=1)
			{
				flag = true;
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
		return flag;
    }
    
    /*
     * 删除积分比例
     */
    public boolean deleteJf_def(JSONObject jo)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rowCount = st.executeUpdate("delete FROM jf_def where code='"+jo.getString("code").trim()+"' and gt_id="+jo.getInt("gt_id"));
			if(rowCount>=1)
			{
				flag = true;
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
		return flag;
    }
    
    /*
     * 新增卡型对应积分比例
     */
    public boolean addJfdef(JSONArray ja)
    {
    	Connection conn = null;
		Statement st  = null;
		boolean flag = false;
//		System.out.println("jsonobject:"+jo.toString());
	    try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			for(int i=0;i<ja.size();i++)
			{
				st.addBatch("INSERT INTO jf_def (code,type,name,amount,gt_id,pk) VALUES('"+ja.getJSONObject(i).getString("code").trim()+"','"+ja.getJSONObject(i).getString("type").trim()+"','"+ja.getJSONObject(i).getString("name").trim()+"',"+new BigDecimal(ja.getJSONObject(i).getString("amount"))+","+ja.getJSONObject(i).getInt("gt_id")+",null)");
			}
			int []rowCount = st.executeBatch();
			conn.commit();
			if(rowCount[0]>=0)
			{
				flag = true;
			}
				
		} catch (SQLException e) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return flag;
    }
	
}
