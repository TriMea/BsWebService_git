package com.dl.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;


import com.dl.util.CommonTool;



public class RoomResourceDao {

	/*
	 * 单例模式
	 */
	private RoomResourceDao() {}  
    //已经自行实例化   
    private static final RoomResourceDao roomResourceDao = new RoomResourceDao();  
    //静态工厂方法   
    public static RoomResourceDao getInstance() {  
        return roomResourceDao;  
    }  
    
    //添加房型资源
    public synchronized boolean addRoomTypeInfo(JSONArray ja,Integer gt_id,String hotelid)
	{
		Connection conn = null;
		Statement st = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("delete from room_avl_show where hotelid = '"+hotelid.trim()+"'");
			st.addBatch("alter table room_avl_show auto_increment=1");
			for(int i=0;i<ja.size();i++)
			{
				JSONObject jo = ja.getJSONObject(i);
				st.addBatch("insert into room_avl_show(Hotelid,Hotel,Rmcode,Rmcode_Des,Type,Type_Des,Remark,Arr,Dep,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22,T23,T24,T25,T26,T27,T28,T29,T30,T31,Mkt,Src,Package,gt_id) values('"+jo.getString("hotelid") +"','"+jo.getString("hotel")+"','"+jo.getString("rmcode")+"','"+jo.getString("rmcode_Des")+"','"+jo.getString("type")+"','"+jo.getString("type_Des")
						+"','"+jo.getString("remark")+"','"+jo.getString("arr")+"','"+jo.getString("dep")+"','"+jo.getString("t1")+"','"+jo.getString("t2")+"','"
						+jo.getString("t3")+"','"+jo.getString("t4")+"','"+jo.getString("t5")+"','"+jo.getString("t6")+"','"+jo.getString("t7")+"','"+jo.getString("t8")+"','"
						+jo.getString("t9")+"','"+jo.getString("t10")+"','"+jo.getString("t11")+"','"+jo.getString("t12")+"','"+jo.getString("t13")+"','"+jo.getString("t14")+"','"+jo.getString("t15")+"','"+jo.getString("t16")+"','"+jo.getString("t17")+"','"+jo.getString("t18")+"','"+jo.getString("t19")+"','"
						+jo.getString("t20")+"','"+jo.getString("t21")+"','"+jo.getString("t22")+"','"+jo.getString("t23")+"','"+jo.getString("t24")+"','"+jo.getString("t25")+"','"+jo.getString("t26")+"','"+jo.getString("t27")+"','"+jo.getString("t28")+"','"+jo.getString("t29")+"','"+jo.getString("t30")+"','"
						+jo.getString("t31")+"','"+jo.getString("mkt")+"','"+jo.getString("src")+"','"+jo.getString("package")+"',"+gt_id+")");
				
			}
			int[] f = st.executeBatch();
			conn.commit();
			if(f[0]>=0)
			{
				flag = true;
			}
				
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
				e.printStackTrace();
			}
		}

		return flag;
	}
    //添加房态信息
    public synchronized boolean addRoomMapStatus(JSONArray ja,Integer gt_id,String hotelid)
	{
		Connection conn = null;
		Statement st = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("delete from map_roomno where hotelid = '"+hotelid.trim()+"'");
			for(int i=0;i<ja.size();i++)
			{
				JSONObject jo = ja.getJSONObject(i);//new BigDecimal(jo.getString("days"))
//				st.addBatch("insert into map_roomno(roomno,base,area,floor,type,status,sta,accnt,link,mkt,flag,num,rmk,flag2,linkf,hotelid,date,gt_id) values('"+jo.getString("roomno")+"','"+jo.getString("base")
//						+"','"+jo.getString("area")+"','"+jo.getString("floor")+"','"+jo.getString("type")+"','"+jo.getString("status")
//						+"','"+jo.getString("sta")+"','"+jo.getString("accnt")+"','"+jo.getString("link")
//						+"','"+jo.getString("mkt")+"','"+jo.getString("flag")+"','"+jo.getString("num")+"','"+jo.getString("rmk")
//						+"','"+jo.getString("flag2")+"',"+jo.getInt("linkf")+",'"+jo.getString("hotelid")+"',now(),"+gt_id+")");
				
				st.addBatch("insert into map_roomno(roomno,base,area,floor,type,status,sta,accnt,link,mkt,flag,num,rmk,flag2,linkf,hotelid,date,gt_id,price0,price,payment,expend,banlance) values('"+jo.getString("roomno")+"','"+jo.getString("base")
						+"','"+jo.getString("area")+"','"+jo.getString("floor")+"','"+jo.getString("type")+"','"+jo.getString("status")
						+"','"+jo.getString("sta")+"','"+jo.getString("accnt")+"','"+jo.getString("link")
						+"','"+jo.getString("mkt")+"','"+jo.getString("flag")+"','"+jo.getString("num")+"','"+jo.getString("rmk")
						+"','"+jo.getString("flag2")+"',"+jo.getInt("linkf")+",'"+jo.getString("hotelid")+"',now(),"+gt_id+","+new BigDecimal(jo.getString("price0"))+","+new BigDecimal(jo.getString("price"))+","+new BigDecimal(jo.getString("payment"))+","+new BigDecimal(jo.getString("expend"))+","+new BigDecimal(jo.getString("banlance"))+")");
				
			}
			int[] f = st.executeBatch();
			conn.commit();

			if(f[0]>=0)
			{			
				flag = true;
			}
				
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
				e.printStackTrace();
			}
		}

		return flag;
	}
}
