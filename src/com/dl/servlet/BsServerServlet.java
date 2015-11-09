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
			//���ӷ�����
			VipService vs = new VipService();
			
		   try {
			vs.room_pricecodeAdd(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			
			
		}else if("deleteRoomCode".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//ɾ��������
			VipService vs = new VipService();
		   try {
			vs.room_pricecodeDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("addSys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//���Ӹ�����
			VipService vs = new VipService();
		   try {
			vs.sys_charge_codeAdd(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("deleteSys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//ɾ��������
			VipService vs = new VipService();
		   try {
			vs.sys_charge_codeDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("addVip_cardtype".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//���ӿ���
			VipService vs = new VipService();
		   try {
			vs.vip_cardtypeAdd(out, json_data);
		      } catch (Exception e) {
			
			    e.printStackTrace();
		      }

		}
		else if("deleteVip_cardtype".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//ɾ������
			VipService vs = new VipService();
		   try {
			vs.vip_cardtypeDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("deleteJf_def".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//ɾ�����ֱ���
			VipService vs = new VipService();
		   try {
			vs.jf_defDelete(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		}else if("addJf_def".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//���ӻ��ֱ���
			VipService vs = new VipService();
		   try {
			vs.jfDefAdd(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("password_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//�޸�����
			VipService vs = new VipService();
		   try {
			vs.password_update(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("vip_jfToMoney_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//�޸Ŀ��ͻ��ֶһ�����
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
			//��ȡ������б�
			VipService vs = new VipService();
		   try {
			vs.vipListSearch(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			
		}else if("hotelid_list".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ�ֵ��б�
			VipService vs = new VipService();
		   try {
			vs.getHotelidList(out,json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("map_roomno".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ��̬��Ϣ
			VipService vs = new VipService();
		   try {
			vs.getMapRoomno(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("uc_login".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��¼��֤
			VipService vs = new VipService();
		   try {
			vs.uc_loginValidate(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("vipCard_detail".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ���ſ�������Ϣ
			VipService vs = new VipService();
		   try {
			vs.getVipInfoBysno(out,json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("rep_hotel".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//����hotelid��sysdate��ȡ�ֵ�ĳ���Ӫҵ����
			VipService vs = new VipService();
		   try {
			vs.getRepHotel(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		else if("rep_hotelCounts".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//����hotelid��sysdate�����ȡ�ֵ�ö�ʱ���ڵ�ͳ��
			VipService vs = new VipService();
		   try {
			vs.getRepHotelCounts(out, json_data);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}else if("jf_def_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//���ݼ��źŻ�ȡ���ֱ���
			VipService vs = new VipService();
		   try {
			vs.jfdefSearch(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("room_resource_count".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//������Դͳ��
			VipService vs = new VipService();
		   try {
			vs.roomResourceSearch(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("rep_gt_day".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//�����ձ���
			VipService vs = new VipService();
		   try {
			vs.getRep_Gt_day(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("rep_credit_detail".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//���ſ���ֵ��ϸ��
			VipService vs = new VipService();
		   try {
			vs.getRep_Credit_Detail(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}
		else if("rep_credit_charge_total".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//���ſ����ͳ��
			VipService vs = new VipService();
		   try {
			vs.getRep_CreditChargeTotal(out, json_data);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		}else if("vip_jfToMoney_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ�ü��ŵ����й�����͵Ļ��ֶһ���Ǯ����
			VipService vs = new VipService();
			try {
				vs.vipJfToMoneySearch(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("vip_sys_charge_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ�ü��ŵĸ�����
			VipService vs = new VipService();
			try {
				vs.vip_charge_codeSearch(out, json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}//Сޱ
		else if("vip_cardtype_search".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ�ü��ŵ����й������
			VipService vs = new VipService();
			try {
				vs.vipCardTypeSearch(out,json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("room_price_code".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			//��ȡ�ü����µķ�����
			VipService vs = new VipService();
			try {
				vs.room_pricecodeSearch(out, json_data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("vipAccount_down".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
		    //��������	
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