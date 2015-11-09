package com.dl.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.dao.CommonDao;
import com.dl.dao.VipInfoDao;

import com.dl.service.VipService;

public class VipPostServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public VipPostServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		
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
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		//以json的格式获取参数
		
		JSONObject json_data = null;
		try {
			json_data = JSONObject.fromObject(request.getParameter("data"));
		} catch (Exception e) {
			CommonDao.getInstance().generateResponseJSon("vip_search","1","1000","参数转换出错("+e.getMessage()+")",null);
			return;
		}
		System.out.println("get:"+json_data);
		if("vip_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//集团卡搜索
			VipService vs = new VipService();
			try {
				vs.vipSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("vip_search","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}else if("vip_cardtype_search_shr".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团的所有贵宾卡型
			VipService vs = new VipService();
			try {
				vs.vipCardTypeSearch_shr(out,json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("vip_cardtype_search_shr","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}
		else if("vip_sys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团的付款码
			VipService vs = new VipService();
			try {
				vs.vip_charge_codeSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("vip_sys_charge_code","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}
		else if("room_price_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团下的房价码
			VipService vs = new VipService();
			try {
				vs.room_pricecodeSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("room_price_code","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}
		else if("vipAccount_down".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
		    //账务下载	
			VipService vs = new VipService();
			vs.vipAccount_download(out, json_data);
			
		}else if("vipAccountForReport_down".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//账务下载(交班报表)	
			VipService vs = new VipService();
			vs.vipAccount_download1(out, json_data);
		}else if("jf_use_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//积分奖励和消费记录查询
			VipService vs = new VipService();
			try {
				vs.jf_useSearch(out,json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("jf_use_search","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}else if("jf_use_searchBySysdate".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//积分奖励和消费记录查询
			VipService vs = new VipService();
			try {
				vs.jf_useSearchBySysdtae(out,json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("jf_use_searchBySysdate","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}
		else if("jf_detail_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//积分明细查询
			VipService vs = new VipService();
			try {
				vs.jf_detailSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("jf_detail_search","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}else if("vip_log_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//贵宾卡日志查询
			VipService vs = new VipService();
			try {
				vs.vip_logSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("vip_log_search","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
		}else if("jf_def_search_shr".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//根据集团号和卡型获取积分比例
			VipService vs = new VipService();
		    try {
			      vs.jfdefSearchByType(out, json_data);
		        } catch (Exception e) {
		         CommonDao.getInstance().generateResponseJSon("jf_def_search_shr","1","6000",e.getMessage(),null);
			      e.printStackTrace();
		        }

		}else if("vip_info_birthday".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
				//根据集团号和分店号以及出生年月获取相应的会员信息
				VipService vs = new VipService();
			   try {
				 vs.vipSearchByBirthday(out, json_data);
			   } catch (Exception e) {
				 CommonDao.getInstance().generateResponseJSon("vip_info_birthday","1","6000",e.getMessage(),null);
				 e.printStackTrace();
			   }

		}
		else if("hotelid_list".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取分店列表
			VipService vs = new VipService();
		   try {
			vs.getHotelidList(out,json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("hotelid_list","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
		}else if("rep_jf_use_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//积分兑换信息查询
			try {
				VipService vs = new VipService();
				vs.rep_jf_useSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("rep_jf_use_search","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
		}else if("rep_jf_use_detail_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//积分兑换明细查询
			try {
				VipService vs = new VipService();
				vs.rep_jf_useDetailSearch(out, json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("rep_jf_use_detail_search","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
		}else if("rep_credit_charge".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//本店卡在其他店的充值消费记录
			//其他店卡在本店的充值消费记录
			try {
				VipService vs = new VipService();
				vs.rep_credit_charge(out,json_data);
			} catch (Exception e) {
				CommonDao.getInstance().generateResponseJSon("rep_credit_charge","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
		}
		
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		
		
		PrintWriter out = response.getWriter();
		//以json的格式获取参数
		System.out.println("data:"+request.getParameter("data"));
		JSONObject json_data = null;
		try {
			json_data = JSONObject.fromObject(request.getParameter("data"));
		} catch (Exception e) {
			CommonDao.getInstance().generateResponseJSon("vip_search","1","1000","参数转换出错("+e.getMessage()+")",null);
			return;
		}
//		JSONObject json_data = JSONObject.fromObject(request.getParameter("data"));
		if("vip_post".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//集团卡消费
			VipService vs = new VipService();
			try {
				vs.VipConsume(out, json_data);
			} catch (SQLException e) {
				CommonDao.getInstance().generateResponseJSon("vip_post","1","6000",e.getMessage(),null);
				e.printStackTrace();
			}
			
			
		}else if("vip_info".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//集团卡发卡
			VipService vs = new VipService();
			vs.vipInit(out, json_data);
		}
		else if("vip_info_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//主单修改
			VipService vs = new VipService();
			vs.vipInfoUpdate(out, json_data);
		}
		else if("upload_jf_use".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//积分消费和奖励
			VipService vs = new VipService();
			vs.VipJfUse(out, json_data);
			
		}
		else if("rep_hotel_all_init".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			
			//上传营业数据模板
			VipService vs = new VipService();
			vs.rep_hotel_all_init(out, json_data);
		}
		else if("audit".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//夜审
			VipService vs = new VipService();
			vs.rep_hotel_all(out, json_data);
		}else if("vip_info_del".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			
			//集团卡发卡
			VipService vs = new VipService();
			vs.vipInit(out, json_data);
		}
		else if("pre_authorization_cancel".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//预授权取消
			VipService vs = new VipService();
			vs.pre_authorization_cancel(out,json_data);
		}else if("upload_jf_detail".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//上传集团卡积分信息
			VipService vs = new VipService();
			vs.upload_jf_detail(out,json_data);
		}
		

	}
	
	


	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	@Override
	public void init() throws ServletException {
		
	}

}
