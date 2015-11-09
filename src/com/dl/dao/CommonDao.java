package com.dl.dao;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import com.dl.datasource.DBPool;




import com.dl.pojo.CompanyBean;
import com.dl.pojo.Jf_def;
import com.dl.pojo.VipAccountBean;
import com.dl.util.CommonTool;















public class CommonDao {
	/*
	 * 单例模式
	 */
	private CommonDao() {}  
    //已经自行实例化   
    private static final CommonDao commonDao = new CommonDao();  
    //静态工厂方法   
    public static CommonDao getInstance() {  
        return commonDao;  
    }  
	
	

	public String generateResponseJSon(String responseOp,String status,String responseCode,String responseDec,JSONArray responseData)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		String dec = null;
		try {
			dec = URLEncoder.encode(responseDec, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jo_response.put("responseDec",dec);
		if(responseData==null)
		{
			jo_response.put("responseData", "no data");
		}else{
			jo_response.put("responseData", responseData);
		}
		
		return jo_response.toString();
	} 
	
	public String generateResponseJSon_viplist(String responseOp,String status,String responseCode,String responseDec,JSONArray responseData1,Integer counts)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		jo_response.put("counts", counts);
		String dec = null;
		try {
			dec = URLEncoder.encode(responseDec, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jo_response.put("responseDec",dec);
		if(responseData1==null)
		{
			jo_response.put("responseData1", "no data");
		}else{
			jo_response.put("responseData1", responseData1);
		}
		
			
		
		
		return jo_response.toString();
	} 
	
	public String generateResponseJSon_double(String responseOp,String status,String responseCode,String responseDec,JSONArray responseData1,JSONArray responseData2)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		String dec = null;
		try {
			dec = URLEncoder.encode(responseDec, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jo_response.put("responseDec",dec);
		if(responseData1==null)
		{
			jo_response.put("responseData1", "no data");
		}else{
			jo_response.put("responseData1", responseData1);
		}
		if(responseData2==null)
		{
			jo_response.put("responseData2", "no data");
		}else{
			jo_response.put("responseData2", responseData2);
		}
		
		return jo_response.toString();
	} 
	
	//营业数据初始化响应数据生成
	public String rep_hotel_all_init_generateResponseJSon(String responseOp,String status,String responseCode,String responseDec,String jf_code,JSONArray jf_def)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		jo_response.put("responseDec", responseDec);
		jo_response.put("jf_code", jf_code);
		if(jf_def==null)
		{
			jo_response.put("jf_def", "no data");
		}else{
			jo_response.put("jf_def", jf_def);
		}
		
		return jo_response.toString();
	}
	
	//夜审营业数据响应数据生成
	public String rep_hotel_all_generateResponseJSon(String responseOp,String status,String responseCode,String responseDec,String jf_code,JSONArray jf_def,JSONArray company)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		jo_response.put("responseDec", responseDec);
		jo_response.put("jf_code", jf_code);
		if(jf_def==null)
		{
			jo_response.put("jf_def", "no data");
		}else{
			jo_response.put("jf_def", jf_def);
		}
		if(company==null)
		{
			jo_response.put("company", "no data");
		}else{
			jo_response.put("company", company);
		}
		
		return jo_response.toString();
	}
	
	//根据old_accnt检索出accnt_new
	public String findAccnt_newByAccnt_old(String hotelid,String accnt_old)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		String accnt_new = null;
		try {
			conn = DBPool.getPool().getConnection();
			System.out.println("select accnt_new  from sync_accnt  where hotelid ='"+hotelid+"' and accnt_old='"+accnt_old+"' and class='vip_info'");
		    String sql = "select accnt_new  from sync_accnt  where hotelid ='"+hotelid+"' and accnt_old='"+accnt_old+"' and class='vip_info'"; 
		    st = conn.createStatement();
		    rs = st.executeQuery(sql);
		    if(rs.next())
		    {
		    	 accnt_new = rs.getString("accnt_new");
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
		return accnt_new;
	}
	
//	//根据accnt_new检索出accnt_old
//	public String findAccnt_oldByAccnt_new(String hotelid,String accnt_new) throws SQLException
//	{
//		Connection conn = DBPool.getPool().getConnection();
//		Statement st  = null;
//		ResultSet rs = null;
//		String accnt_old = "";
//		
//			
//	    String sql = "select accnt_old  from sync_accnt  where hotelid ='"+hotelid+"' and accnt_new='"+accnt_new+"' and class='vip_info'"; 
//	    st = conn.createStatement();
//	    rs = st.executeQuery(sql);
//	    if(rs.next())
//	    {
//	    	accnt_old = rs.getString("accnt_old");
//	    }
//		  
//	    CommonTool.closeResultSet(rs);
//		CommonTool.closeStatement(st);
//		CommonTool.closeConnection(conn);
//		return accnt_old;
//	}
//	
	/*
	 * 根据hotelid获取集团号
	 */
	public  Integer selGt_idbyHotelid(String hotelid)
	{
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		
		Integer gt_id = 0;
		try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			String sql = "select gt_id from hotelid where id='"+hotelid+"' and ISNULL(haw)";
            rs = st.executeQuery(sql);
            if(rs.next())
            {
            	gt_id = rs.getInt("gt_id");
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
		
		return gt_id;
	}
	
	
	//获取集团新编码（卡号或档案号）
	public String  getGT_coder(String flag,Integer gt_id)
	{
		Connection conn = null;
		CallableStatement  cs = null;
		ResultSet rs = null;
		String accnt_new = "";
	
		try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_getno(?,?)}");
			 cs.setString(1, flag);
			 cs.setInt(2, gt_id);
			 if(cs.execute())
			 {
				 rs = cs.getResultSet();
				 rs.next();
				 accnt_new = rs.getString(1);	
			 }
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return accnt_new;
	}
	
	//插入sync_accnt表对应新旧编号
	public synchronized boolean addSync_accnt(String accnt_new,String accnt_old,String hotelid,String type)
	{
		Connection conn = null;
		Statement st  = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("delete from sync_accnt where class='gainfo' and hotelid='"+hotelid+"' and accnt_old='"+accnt_old+"'");		
			st.addBatch("insert into sync_accnt(class,accnt_old,accnt_new,hotelid) values('"+type+"','"+accnt_old
							+"','"+accnt_new+"','"+hotelid+"')");
			int[] f = st.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			if(f[0]>=0)
			{
//				Insert_Error_Info.addErrorInfo(hotelid+"插入sync_accnt信息成功");
				flag = true;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	
	public synchronized boolean addRep_hotel(JSONArray ja,Integer gt_id,String sysdate,String hotelid)
	{
		Connection conn = null;
		Statement st = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			st.addBatch("delete from rep_hotel_all where sysdate ='"+sysdate+"' and hotelid='"+hotelid+"'");
			for(int i=0;i<ja.size();i++)
			{
				JSONObject jo = ja.getJSONObject(i);
				//icoffeechx
				st.addBatch("insert into rep_hotel_all(hotelid,sysdate,order_,itemno,mode,class,descript1,descript2,rectype,toop,toclass,toclass1,day,month,year,show1,pmonth,lmonth,pyear,lyear,gt_id) values('"+jo.getString("hotelid").trim()+"','"+jo.getString("sysdate").trim()+
						"','"+jo.getString("order_")+"','"+jo.getString("itemno")+"','"+jo.getString("mode")+"','"+jo.getString("class_lyy")
						+"','"+jo.getString("descript1")+"','"+jo.getString("descript2")+"','"+jo.getString("rectype")
						+"','"+jo.getString("toop")+"','"+jo.getString("toclass")+"','"+jo.getString("toclass1")+"',"+jo.getDouble("day")
						+","+jo.getDouble("month")+","+jo.getDouble("year")+",'"+jo.getString("show")+"',"+jo.getDouble("pmonth")+
						","+jo.getDouble("lmonth")+","+jo.getDouble("pyear")+","+jo.getDouble("lyear")+","+gt_id+")");
				
			}
			int[] f = st.executeBatch();
			conn.commit();
			if(f[0]>=0)
			{
				flag = true;
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
	
	//上传rep_hotel_all之后，对该表进行计数+1
	public  void updateCtr_index_jyo(String hotelid)
	{
		System.out.println("分店号："+hotelid);
		String sql = "UPDATE ctrl_index_jyo SET `index`=`index`+1 WHERE catalog='tos_rep_hotel_all' and item='"+hotelid+"'";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(hotelid+"增加index+1出异常");
			e.printStackTrace();
		}finally
		{
			try {
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public StringBuffer getJf_Def_codeBygt_id(Integer gt_id) 
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		StringBuffer sb =new StringBuffer("");
		
			String sql = "SELECT DISTINCT code from jf_def where gt_id="+gt_id;
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while(rs.next())
				{
				   sb.append(rs.getString(1).trim()+";");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
				try {
			    	CommonTool.closeResultSet(rs);
			    	CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		    
		return sb;
	}
	
	public List<Jf_def> getJf_DefByhotelid_gt_id(Integer gt_id) 
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Jf_def> list = new ArrayList<Jf_def>();
		try {
			conn = DBPool.getPool().getConnection();
				String sql = "select code,type,name,amount from jf_def where gt_id="+gt_id;
			    System.out.println("消费积分比例："+sql);
			    st = conn.createStatement();
			    rs = st.executeQuery(sql);
			    while(rs.next())
			    {
			    	Jf_def si = new Jf_def();
			    	si.setCode(rs.getString(1));
			    	si.setType(rs.getString(2));
			    	si.setName(rs.getString(3));
			    	si.setAmount(rs.getBigDecimal(4));
			    	list.add(si);
			    }
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	//增加jf_detail
	public synchronized boolean addJF_detailByOne(JSONArray ja,String accnt,Integer gt_id,boolean flag,String hotelid,String sysdate )
	{
		Connection conn = null;
		Statement st  = null;
		boolean result = false;
		try{
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch("delete from jf_detail where sysdate='"+sysdate.trim()+"' and hotelid='"+hotelid.trim()+"'");
			for(int i=0;i<ja.size();i++)
			{
				if(flag)
				{
					
				    st.addBatch("insert into jf_detail(sysdate,hotelid,accnt,type,rm,fb,en,ot,dtotal,total,gt_id) values('"+ja.getJSONObject(i).getString("sysdate").trim()+"','"+ja.getJSONObject(i).getString("hotelid")
							+"','"+ja.getJSONObject(i).getString("accnt").trim()+gt_id+"','"+ja.getJSONObject(i).getString("type")+"',"+new BigDecimal(ja.getJSONObject(i).getString("rm"))+","+new BigDecimal(ja.getJSONObject(i).getString("fb"))+","+new BigDecimal(ja.getJSONObject(i).getString("en"))+","+new BigDecimal(ja.getJSONObject(i).getString("ot"))
					+","+new BigDecimal(ja.getJSONObject(i).getString("dtotal"))+","+new BigDecimal(ja.getJSONObject(i).getString("total"))+","+gt_id+")");
//						st.addBatch("insert into jf_detail(sysdate,hotelid,accnt,type,rm,fb,en,ot,dtotal,total,gt_id) values('"+jo.getString("sysdate").trim()+"','"+jo.getString("hotelid")
//								+"','"+jo.getString("accnt")+"','"+jo.getString("type")+"',"+new BigDecimal(jo.getString("rm"))+","+new BigDecimal(jo.getString("fb"))+","+new BigDecimal(jo.getString("en"))+","+new BigDecimal(jo.getString("ot"))
//						+","+new BigDecimal(jo.getString("dtotal"))+","+new BigDecimal(jo.getString("total"))+","+gt_id+")");
			
				}else{
					
					st.addBatch("insert into jf_detail(sysdate,hotelid,accnt,type,rm,fb,en,ot,dtotal,total,gt_id) values('"+ja.getJSONObject(i).getString("sysdate").trim()+"','"+ja.getJSONObject(i).getString("hotelid")
							+"','"+findAccnt_newByAccnt_old(hotelid, ja.getJSONObject(i).getString("accnt")).trim()+"','"+ja.getJSONObject(i).getString("type")+"',"+new BigDecimal(ja.getJSONObject(i).getString("rm"))+","+new BigDecimal(ja.getJSONObject(i).getString("fb"))+","+new BigDecimal(ja.getJSONObject(i).getString("en"))+","+new BigDecimal(ja.getJSONObject(i).getString("ot"))
					+","+new BigDecimal(ja.getJSONObject(i).getString("dtotal"))+","+new BigDecimal(ja.getJSONObject(i).getString("total"))+","+gt_id+")");

				}
			}
			
			int[]i = st.executeBatch();
			conn.commit();
			if(i[0]>0)
			{
				result = true;
			}
	
	}catch (Exception e) {
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
		 
		return result;
	}
	
	//增加jf_use
	public synchronized boolean delInsertJF_useBySysdate_accnt_gt_id(String hotelid,Integer gt_id,String accnt,JSONArray ja,boolean flag)
	{
		Connection conn = null;
		Statement st  = null;
		boolean result = false;
		try{
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
//				 String sql = "delete from jf_use where sysdate='"+sysdate+"' and accnt='"+accnt+"' and gt_id="+gt_id;
//				 st.addBatch(sql);
			System.out.println();
				//icoffeechx
			if(flag)
			{
				
				for(int i=0;i<ja.size();i++)
				{
//				String accnt_new = ja.getJSONObject(i).getString("accnt").trim()+gt_id;
				
					st.addBatch("delete from jf_use where sysdate='"+ja.getJSONObject(i).getString("sysdate").trim()+"' and accnt='"+(ja.getJSONObject(i).getString("accnt").trim()+gt_id)+"' and id="+ja.getJSONObject(i).getInt("id")+" and type='"+ja.getJSONObject(i).getString("type")+"' and hotelid='"+hotelid+"' and gt_id="+gt_id);
					st.addBatch("insert into jf_use(hotelid,accnt,type,id,sta,used,amount,ref1,ref2,sysdate,log_id,log_date,last_id,last_date,gt_id) values('"+hotelid+"','"+(ja.getJSONObject(i).getString("accnt").trim()+gt_id)+"','"+ja.getJSONObject(i).getString("type")+"',"+ja.getJSONObject(i).getInt("id")+",'"+ja.getJSONObject(i).getString("sta")+"',"+ja.getJSONObject(i).getInt("used")+","+new BigDecimal(ja.getJSONObject(i).getString("amount"))
					+",'"+ja.getJSONObject(i).getString("ref1")+"','"+ja.getJSONObject(i).getString("ref2")+"','"+ja.getJSONObject(i).getString("sysdate").trim()+"','"+ja.getJSONObject(i).getString("log_id")+"','"+ja.getJSONObject(i).getString("log_date")+"','"+ja.getJSONObject(i).getString("last_id")+"','"+ja.getJSONObject(i).getString("last_date")+"',"+gt_id+")");
				}

				
			
			
			}else{

				for(int i=0;i<ja.size();i++)
				{
					st.addBatch("delete from jf_use where sysdate='"+ja.getJSONObject(i).getString("sysdate").trim()+"' and accnt='"+findAccnt_newByAccnt_old(hotelid, ja.getJSONObject(i).getString("accnt").trim())+"' and id="+ja.getJSONObject(i).getInt("id")+" and type='"+ja.getJSONObject(i).getString("type")+"' and hotelid='"+hotelid+"' and gt_id="+gt_id);
					st.addBatch("insert into jf_use(hotelid,accnt,type,id,sta,used,amount,ref1,ref2,sysdate,log_id,log_date,last_id,last_date,gt_id) values('"+hotelid+"','"+findAccnt_newByAccnt_old(hotelid, ja.getJSONObject(i).getString("accnt").trim())+"','"+ja.getJSONObject(i).getString("type")+"',"+ja.getJSONObject(i).getInt("id")+",'"+ja.getJSONObject(i).getString("sta")+"',"+ja.getJSONObject(i).getInt("used")+","+new BigDecimal(ja.getJSONObject(i).getString("amount"))
					+",'"+ja.getJSONObject(i).getString("ref1")+"','"+ja.getJSONObject(i).getString("ref2")+"','"+ja.getJSONObject(i).getString("sysdate").trim()+"','"+ja.getJSONObject(i).getString("log_id")+"','"+ja.getJSONObject(i).getString("log_date")+"','"+ja.getJSONObject(i).getString("last_id")+"','"+ja.getJSONObject(i).getString("last_date")+"',"+gt_id+")");

				}
			
			}
			int[] f = st.executeBatch();
			conn.commit();	
			if(f[0]>0)
			{
				result = true;
			}
		
	}catch (Exception e) {
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
				e.printStackTrace();
			}
	}
	
	
	return result;

	}
	
	//计算统计积分
	public void execPreparedList(String hotelid,Integer gt_id,String sysdate,String accnt_new,boolean flag)
	{
		Connection conn = null;
		CallableStatement  cs = null;
		
		List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		try {
			conn = DBPool.getPool().getConnection();
			if(flag)
			{
				cs = conn.prepareCall("{call p_xx_jf_update_crs(?,?,?,?,?)}");
				 cs.setInt(1, gt_id);
				 cs.setString(2, hotelid);
				 cs.setString(3, sysdate);
				 cs.setString(4, "%");
				 cs.setString(5, "A");
				 cs.execute();
			}else{
				cs = conn.prepareCall("{call p_xx_jf_update_crs(?,?,?,?,?)}");
				 cs.setInt(1, gt_id);
				 cs.setString(2, hotelid);
				 cs.setString(3, sysdate);
				 cs.setString(4, accnt_new);
				 cs.setString(5, "A");
				 cs.execute();
			}
			 
				
		} catch (SQLException e) {
			e.printStackTrace();
			
		}finally{
			try {
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	//检索出本分店的所有协议单位
	public List<CompanyBean> findCompanyItemsByhotelid(Integer gt_id)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<CompanyBean> list = new ArrayList<CompanyBean>();
		try {
			conn = DBPool.getPool().getConnection();
			    
				String sql = "select `no`,sno,sta,class,agreement,gname,ename,native,address,linkman,phone,fax,postcode,remark,limit1,datebegin,dateend,log_id,log_shift,log_date,saleid,logmark,extra,char100,char200,date01,date02,mone01,mone02 FROM company WHERE gt_id="+gt_id;
//		    String sql = "SELECT a.accnt_old,b.sno,b.sta,b.class,b.agreement,b.gname,b.ename,b.native,b.address,b.linkman,b.phone,b.fax,b.postcode,b.remark,b.limit1,b.datebegin,b.dateend,b.log_id,b.log_shift,b.log_date,b.saleid,b.logmark,b.extra,b.char100,b.char200,b.date01,b.date02,b.mone01,b.mone02 FROM sync_accnt a,company b WHERE a.accnt_new=b.`no` AND a.hotelid='"+hotelid+"'AND a.class='company' UNION ALL "+
//                           "SELECT a.accnt_new,b.sno,b.sta,b.class,b.agreement,b.gname,b.ename,b.native,b.address,b.linkman,b.phone,b.fax,b.postcode,b.remark,b.limit1,b.datebegin,b.dateend,b.log_id,b.log_shift,b.log_date,b.saleid,b.logmark,b.extra,b.char100,b.char200,b.date01,b.date02,b.mone01,b.mone02 FROM sync_accnt a,company b WHERE a.accnt_new=b.`no` AND a.hotelid!='"+hotelid+"'AND a.class='company'"; 
			    st = conn.createStatement();
			    rs = st.executeQuery(sql);
			    while(rs.next())
			    {
			    	CompanyBean cp = new CompanyBean();
			    	cp.setNo(rs.getString(1));
			    	cp.setSno(rs.getString(2).trim());
			    	cp.setSta(rs.getString(3).trim());
			    	cp.setClass_lyy(rs.getString(4));
			    	cp.setAgreement(rs.getString(5));
			    	cp.setGanme(rs.getString(6));
			    	cp.setEname(rs.getString(7));
			    	cp.setNative_lyy(rs.getString(8));
			    	cp.setAddress(rs.getString(9));
			    	cp.setLinkman(rs.getString(10));
			    	cp.setPhone(rs.getString(11));
			    	cp.setFax(rs.getString(12));
			    	cp.setPostcode(rs.getString(13));
			    	cp.setRemark(rs.getString(14));
			    	cp.setLimit(rs.getBigDecimal(15));
			    	cp.setDatebegin(rs.getString(16));
			    	cp.setDateend(rs.getString(17));
			    	cp.setLog_id(rs.getString(18));
			    	cp.setLog_shift(rs.getString(19));
			    	cp.setLog_date(rs.getString(20));
			    	cp.setSaleid(rs.getString(21));
			    	cp.setLogmark(rs.getInt(22));
			    	cp.setExtra(rs.getString(23));
			    	cp.setChar100(rs.getString(24));
			    	cp.setChar200(rs.getString(25));
			    	cp.setDate01(rs.getString(26));
			    	cp.setDate02(rs.getString(27));
			    	cp.setMone01(rs.getBigDecimal(28));
			    	cp.setMone02(rs.getBigDecimal(29));
					list.add(cp);
			    }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	//添加营业数据
	public synchronized boolean addRep_hotel_audit(JSONArray ja,Integer gt_id,String sysdate,String hotelid)
	{
		Connection conn = null;
		Statement st = null;
		boolean flag = false;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			st.addBatch("delete from rep_hotel_all where sysdate >'"+sysdate+"' and hotelid='"+hotelid+"'");
			for(int i=0;i<ja.size();i++)
			{
				JSONObject jo = ja.getJSONObject(i);
				//icoffeechx
				st.addBatch("insert into rep_hotel_all(hotelid,sysdate,order_,itemno,mode,class,descript1,descript2,rectype,toop,toclass,toclass1,day,month,year,show1,pmonth,lmonth,pyear,lyear,gt_id) values('"+jo.getString("hotelid").trim()+"','"+jo.getString("sysdate").trim()+
						"','"+jo.getString("order_")+"','"+jo.getString("itemno")+"','"+jo.getString("mode")+"','"+jo.getString("class_lyy")
						+"','"+jo.getString("descript1")+"','"+jo.getString("descript2")+"','"+jo.getString("rectype")
						+"','"+jo.getString("toop")+"','"+jo.getString("toclass")+"','"+jo.getString("toclass1")+"',"+jo.getDouble("day")
						+","+jo.getDouble("month")+","+jo.getDouble("year")+",'"+jo.getString("show")+"',"+jo.getDouble("pmonth")+
						","+jo.getDouble("lmonth")+","+jo.getDouble("pyear")+","+jo.getDouble("lyear")+","+gt_id+")");

				
			}
			int[] f = st.executeBatch();
			conn.commit();
			if(f[0]>0)
			{
				flag = true;
			}
			
//				
			
				
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
				e.printStackTrace();
			}
		}
//		
		return flag;
	}
	//房态房型上传标志
	/*
	 * flag:avl_show_index房型  roommap_index房态
	 */
	public  void updateCtr_index_jyo(String timeNow,String hotelid,String flag)
	{
		System.out.println("分店号："+hotelid);
//		"UPDATE ctrl_index_jyo SET `index`=`index`+1 WHERE catalog='avl_show_index' and item='"+hotelid+"'";
//		String sql = "UPDATE ctrl_index_jyo SET `index`=`index`+1 WHERE catalog='roommap_index' and item='"+hotelid+"'";
		String sql = "UPDATE ctrl_index_jyo SET `index`=`index`+1,value='"+timeNow+"' WHERE catalog='"+flag+"' and item='"+hotelid+"'";
		Connection conn = null;
		Statement st = null;
		try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(hotelid+"增加index+1出异常");
			e.printStackTrace();
		}finally
		{
			try {
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * 请求是否有效
	 */
	public  boolean isValid(String hotelid,String dogNo)
	{
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		
	    boolean isValid = false;
		try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			String sql = "select count(*) from newgt_dogno where hotelid='"+hotelid+"' and dogno='"+dogNo+"'";
            rs = st.executeQuery(sql);
            if(rs.next())
            {
            	if(rs.getInt(1)==1)
            	{
            		isValid = true;
            	}
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
		
		return isValid;
	}
	
}
