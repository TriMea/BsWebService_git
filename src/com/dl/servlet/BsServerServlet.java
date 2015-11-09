package com.dl.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.bag.SynchronizedSortedBag;

import net.sf.json.JSONObject;

import com.dl.service.VipService;

public class BsServerServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public BsServerServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = resp.getWriter();
		JSONObject json_data = JSONObject.fromObject(req.getParameter("data"));
		System.out.println("POST:"+json_data);
		if("addRoomCode".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//增加房价码
			VipService vs = new VipService();
			
		   try {
			vs.room_pricecodeAdd(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			
			
		}else if("deleteRoomCode".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//删除房价码
			VipService vs = new VipService();
		   try {
			vs.room_pricecodeDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("addSys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//增加付款码
			VipService vs = new VipService();
		   try {
			vs.sys_charge_codeAdd(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("deleteSys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//删除付款码
			VipService vs = new VipService();
		   try {
			vs.sys_charge_codeDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("addVip_cardtype".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//增加卡型
			VipService vs = new VipService();
		   try {
			vs.vip_cardtypeAdd(out, json_data);
		      } catch (Exception e) {
			
			    e.printStackTrace();
		      }

		}
		else if("deleteVip_cardtype".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//删除卡型
			VipService vs = new VipService();
		   try {
			vs.vip_cardtypeDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("deleteJf_def".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//删除积分比例
			VipService vs = new VipService();
		   try {
			vs.jf_defDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		}else if("addJf_def".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//添加积分比例
			VipService vs = new VipService();
		   try {
			vs.jfDefAdd(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("password_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//修改密码
			VipService vs = new VipService();
		   try {
			vs.password_update(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("vip_jfToMoney_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//修改卡型积分兑换比率
			VipService vs = new VipService();
		   try {
			vs.JfToMoney_update(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
            
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		JSONObject json_data = JSONObject.fromObject(request.getParameter("data"));
		System.out.println("GET:"+json_data);
		if("vipList_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取贵宾卡列表
			VipService vs = new VipService();
		   try {
			vs.vipListSearch(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			
		}else if("hotelid_list".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取分店列表
			VipService vs = new VipService();
		   try {
			vs.getHotelidList(out,json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("map_roomno".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取房态信息
			VipService vs = new VipService();
		   try {
			vs.getMapRoomno(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("uc_login".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//登录验证
			VipService vs = new VipService();
		   try {
			vs.uc_loginValidate(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("vipCard_detail".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取集团卡具体信息
			VipService vs = new VipService();
		   try {
			vs.getVipInfoBysno(out,json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("rep_hotel".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//根据hotelid和sysdate获取分店某天的营业数据
			VipService vs = new VipService();
		   try {
			vs.getRepHotel(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		else if("rep_hotelCounts".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//根据hotelid和sysdate区间获取分店该段时间内的统计
			VipService vs = new VipService();
		   try {
			vs.getRepHotelCounts(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("jf_def_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//根据集团号获取积分比例
			VipService vs = new VipService();
		   try {
			vs.jfdefSearch(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("room_resource_count".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//房间资源统计
			VipService vs = new VipService();
		   try {
			vs.roomResourceSearch(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("rep_gt_day".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//集团日报表
			VipService vs = new VipService();
		   try {
			vs.getRep_Gt_day(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("rep_credit_detail".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//集团卡充值明细表
			VipService vs = new VipService();
		   try {
			vs.getRep_Credit_Detail(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("rep_credit_charge_total".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//集团卡借贷统计
			VipService vs = new VipService();
		   try {
			vs.getRep_CreditChargeTotal(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("vip_jfToMoney_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团的所有贵宾卡型的积分兑换金钱比率
			VipService vs = new VipService();
			try {
				vs.vipJfToMoneySearch(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("vip_sys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团的付款码
			VipService vs = new VipService();
			try {
				vs.vip_charge_codeSearch(out, json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}//小薇
		else if("vip_cardtype_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团的所有贵宾卡型
			VipService vs = new VipService();
			try {
				vs.vipCardTypeSearch(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("room_price_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//获取该集团下的房价码
			VipService vs = new VipService();
			try {
				vs.room_pricecodeSearch(out, json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("vipAccount_down".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
		    //账务下载	
			VipService vs = new VipService();
			vs.vipAccount_download(out, json_data);
			
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
