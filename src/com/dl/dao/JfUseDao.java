package com.dl.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.dl.datasource.DBPool;
import com.dl.pojo.Jf_useBean;
import com.dl.pojo.Jf_useBySysdateBean;
import com.dl.pojo.VipAccountBean;
import com.dl.pojo.VipSearchInfo;
import com.dl.util.CommonTool;
import net.sf.json.JSONObject;


public class JfUseDao {

	
	/*
	 * 单例模式
	 */
	private JfUseDao() {}  
    //已经自行实例化   
    private static final JfUseDao jfUseDao = new JfUseDao();  
    //静态工厂方法   
    public static JfUseDao getInstance() {  
        return jfUseDao;  
    }  
    /*
     * 新增jf_use
     */
	public synchronized void delInsertJF_useBySysdate_accnt_gt_id(String hotelid,Integer gt_id,String accnt,JSONObject jo,Integer maxId)
	{
		Connection conn = null;
		Statement st  = null;
		try{
			
			    conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				System.out.println();
					//icoffeechx
					String accnt_new = jo.getString("accnt").trim()+gt_id;
					st.executeUpdate("insert into jf_use(hotelid,accnt,type,id,sta,used,amount,ref1,ref2,sysdate,log_id,log_date,last_id,last_date,gt_id,log_shift) values('"+hotelid+"','"+accnt_new+"','"+jo.getString("type")+"',"+maxId+",'"+jo.getString("sta")+"',"+Double.valueOf(jo.getString("used"))+","+new BigDecimal(jo.getString("amount"))
					+",'"+jo.getString("ref1")+"','"+jo.getString("ref2")+"','"+jo.getString("sysdate").trim()+"','"+jo.getString("log_id")+"','"+jo.getString("log_date")+"','"+jo.getString("last_id")+"','"+jo.getString("last_date")+"',"+gt_id+",'"+jo.getString("log_shift").trim()+"')");
						
		}catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			 try {
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			
		}

	}
	/*
	 * 调用过程计算统计积分
	 */
	public void execPreparedList(Integer gt_id,String accnt_new)
	{
		Connection conn = null;
		CallableStatement  cs = null;
		
		List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		try {
			conn = DBPool.getPool().getConnection();
			cs = conn.prepareCall("{call p_xx_jf_update_crs(?,?)}");
			 cs.setInt(1, gt_id);
			 cs.setString(2, accnt_new);
			 cs.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("出异常了");
		}finally{
			try {
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	/*
	 * 根据集团号和账号获取积分积分奖励和使用信息
	 */
	public List<Jf_useBean> searchJfuse(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<Jf_useBean> list = new ArrayList<Jf_useBean>();
	    String sql = "SELECT hotelid,accnt,type,id,sta,used,amount,ref1,ref2,sysdate,log_id,log_date,last_id,last_date,gt_id from jf_use where gt_id = "
	    	+gt_id+" and accnt='"+(jo.getString("accnt").trim()+gt_id)+"'";
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Jf_useBean si = new Jf_useBean();
				si.setHotelid(rs.getString(1).trim());
				si.setAccnt(rs.getString(2).trim().substring(0, 7));
				si.setType(rs.getString(3));
				si.setId(rs.getInt(4)+"");
				si.setSta(rs.getString(5));
				si.setUsed(rs.getInt(6)+"");
				si.setAmount(rs.getBigDecimal(7)+"");
				si.setRef1(rs.getString(8));
				si.setRef2(rs.getString(9));
				si.setSysdate(rs.getString(10));
				si.setLog_id(rs.getString(11));
				si.setLog_date(rs.getString(12));
				si.setLast_id(rs.getString(13));
				si.setLast_date(rs.getString(14));
				si.setGt_id(rs.getInt(15)+"");
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
	
	public synchronized Integer findMaxId(Integer gt_id,JSONObject jo)
	{
		
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		Integer flag = 0;
		
		String sql = "select IFNULL(max(id)+1,1) from jf_use where accnt='"+(jo.getString("accnt").trim()+gt_id)+"' and gt_id="+gt_id;
        System.out.println("搜索消息："+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next())
			{
				flag = rs.getInt(1);
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
	   
		return flag;
	}
	
	/*
	 * 根据集团号和账号,时间区间，工号，班次获取积分积分奖励和使用信息
	 */
	public List<Jf_useBySysdateBean> searchJfuseBySysdate(JSONObject jo,Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		
		List<Jf_useBySysdateBean> list = new ArrayList<Jf_useBySysdateBean>();
	    String sql = "SELECT b.sno,b.gname,b.sta,a.hotelid,a.accnt,a.type,id,a.sta,used,amount,ref1,ref2,sysdate,a.log_id,a.log_date,a.last_id,a.last_date,a.gt_id from jf_use a,vip_info b where a.id!=1 and a.accnt=b.no and a.gt_id = "+
	    	+gt_id+" and a.accnt='"+(jo.getString("accnt").trim()+gt_id)+"' and a.sysdate between '"+jo.getString("bsysdate").trim()+"' and '"+jo.getString("esysdate").trim()+"'";
	    StringBuilder log_id = new StringBuilder(" and ( 1=0 ");
	    StringBuilder log_shift = new StringBuilder(" and ( 1=0 ");
	    if(!jo.getString("log_id").contains(";"))
	    {
	    	log_id.replace(0, log_id.length(), "");
	    }else{
	    	String str[] = jo.getString("log_id").split(";");
	    	for(int i=0;i<str.length;i++)
	    	{
	    		log_id.append(" or a.log_id='"+str[i]+"'");
	    	}
	    	log_id.append(")");
	    }
	    if(!jo.getString("log_shift").contains(";"))
	    {
	    	log_shift.replace(0, log_shift.length(), "");
	    }else{
	    	String str[] = jo.getString("log_shift").split(";");
	    	for(int i=0;i<str.length;i++)
	    	{
	    		log_shift.append(" or a.log_shift="+str[i]);
	    	}
	    	log_shift.append(")");
	    }
	    sql = sql+log_id.toString()+log_shift.toString();
	    System.out.println("Jf_useBySysdate:"+sql);
        try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				Jf_useBySysdateBean si = new Jf_useBySysdateBean();
				si.setSno(rs.getString(1).trim());
				si.setGname(rs.getString(2));
				si.setSta(rs.getString(3));
				si.setHotelid(rs.getString(4).trim());
				si.setAccnt(rs.getString(5).trim().substring(0, 7));
				si.setType(rs.getString(6));
				si.setId(rs.getInt(7)+"");
				si.setSta1(rs.getString(8));
				si.setUsed(rs.getInt(9)+"");
				si.setAmount(rs.getBigDecimal(10)+"");
				si.setRef1(rs.getString(11));
				si.setRef2(rs.getString(12));
				si.setSysdate(rs.getString(13));
				si.setLog_id(rs.getString(14));
				si.setLog_date(rs.getString(15));
				si.setLast_id(rs.getString(16));
				si.setLast_date(rs.getString(17));
				si.setGt_id(rs.getInt(18)+"");
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
