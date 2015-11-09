package com.dl.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonTool {

	public static String getToday()
	{
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(ca.getTime());
		
	}
	
	/*
	 * 关闭数据库执行语句对象
	 */
	public static void closeStatement(Statement st) throws SQLException
	{
		 if(st!=null)
		 {
			 
				st.close();
				st = null;
			
			
		 }
	}
	
	/*
	 * 关闭数据库连接对象
	 */
	public static void closeConnection(Connection conn) throws SQLException
	{
		 if(conn!=null)
		 {
			
				 conn.close();
				 conn = null;
			
			
		 }
	}
	/*
	 * 关闭数据库结果集对象
	 */
	public static void closeResultSet(ResultSet rs) throws SQLException
	{
		 if(rs!=null)
		 {
			
				 rs.close();
				 rs = null;
			
			
		 }
	}
	
	/*
	 * 关闭数据库过程执行对象
	 */
	public static void closeCallableStatement(CallableStatement cs) throws SQLException
	{
		 if(cs!=null)
		 {
			
			 cs.close();
			 cs = null;

		 }
	}
	
}
