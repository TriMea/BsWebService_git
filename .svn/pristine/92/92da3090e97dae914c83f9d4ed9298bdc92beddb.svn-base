package com.dl.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.VipInfoBean;
import com.dl.service.VipService;

public class BsTransportServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public BsTransportServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		response.setContentType("text/html;charset=utf-8");
//		response.setCharacterEncoding("utf-8");
//		request.setCharacterEncoding("utf-8"); 
		PrintWriter out = response.getWriter();
		
//		try {
//			Connection conn = DBPool.getPool().getConnection();
//			ResultSet rs = conn.createStatement().executeQuery("select *from uc_login");
//			while(rs.next())
//			{
//				System.out.println(rs.getString("username"));
//			}
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//		}
//		List<VipInfoBean> list = new ArrayList<VipInfoBean>();
//		VipInfoBean v = new VipInfoBean();
//		v.setAccnt("asasa");
//		v.setAddress("sadafa");
//		VipInfoBean v1 = new VipInfoBean();
//		v1.setAccnt("wewewe");
//		v1.setAddress("ewewe");
//		list.add(v);
//		list.add(v1);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("op", "vip_post");
//		map.put("hotelid", "01a");
//		map.put("data", JSONArray.fromObject(list));
////		out.print(request.getParameter("id"));
//		JSONObject jo = JSONObject.fromObject(map);
//		out.print(jo.get("op")+"||"+jo.getJSONArray("data").getJSONObject(0).getString("address"));
		out.write(request.getParameter("id"));
		out.flush();
		out.close();
//		out
//				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the GET method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
//		out.flush();
//		out.close();
	}
	
	
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); 
		PrintWriter out = response.getWriter();
		//以json的格式获取参数
		System.out.println(request.getParameter("data"));
		JSONObject json_data = JSONObject.fromObject(request.getParameter("data"));
		System.out.println("参数："+json_data.toString());
		if("avl".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//上传房型房态
			VipService vs = new VipService();
			vs.uploadRoomMapAndType(out,json_data);
		}
		else if("rep_hotel_all_init".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//上传前一天的营业数据
			VipService vs = new VipService();
			vs.rep_hotel_all_init(out,json_data);
		}else if("upload_jf_detail".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//上传集团卡积分信息
			VipService vs = new VipService();
			vs.upload_jf_detail(out,json_data);
		}
		else if("audit".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//夜审
			VipService vs = new VipService();
			vs.audit(out,json_data);
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	@Override
	public void init() throws ServletException {
		// Put your code here
	}

}
