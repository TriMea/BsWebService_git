package com.dl.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;





import com.dl.dao.CommonDao;
import com.dl.dao.JfDefDao;
import com.dl.dao.JfDetailDao;
import com.dl.dao.JfToMoneyDao;
import com.dl.dao.JfUseDao;
import com.dl.dao.LogRecordDao;
import com.dl.dao.Rep_hotel_allDao;
import com.dl.dao.Rep_pmsDao;
import com.dl.dao.RoomMapDao;
import com.dl.dao.RoomResourceDao;
import com.dl.dao.SystemSettingDao;
import com.dl.dao.UcLoginDao;
import com.dl.dao.VipAccountDao;
import com.dl.dao.VipCardDao;
import com.dl.dao.VipInfoDao;

import com.dl.pojo.HotelidBean;
import com.dl.pojo.JfToMoneyBean;
import com.dl.pojo.Jf_def;
import com.dl.pojo.Jf_def_shr;
import com.dl.pojo.Jf_detailBean;
import com.dl.pojo.Jf_useBean;
import com.dl.pojo.Jf_useBySysdateBean;
import com.dl.pojo.Rep_CountsBean;
import com.dl.pojo.Rep_CreditBean;
import com.dl.pojo.Rep_CreditChargeBean;
import com.dl.pojo.Rep_CreditCharge_TotalBean;
import com.dl.pojo.Rep_Gt_DayBean;
import com.dl.pojo.Rep_hotel_allBean;
import com.dl.pojo.Rep_jf_use;
import com.dl.pojo.RoomMapBean;
import com.dl.pojo.RoomResourceCount;
import com.dl.pojo.Room_priceCodeBean;
import com.dl.pojo.Sys_charge_code;
import com.dl.pojo.VipAccountBean;
import com.dl.pojo.VipCardDetail;
import com.dl.pojo.VipInfoBean;
import com.dl.pojo.VipSearchInfo;
import com.dl.pojo.Vip_cardtypeBean;
import com.dl.pojo.Vip_cardtype_rmcodeBean;
import com.dl.pojo.Jf_detail_new;




public class VipService {

	/*
	 * ���ſ�����
	 */
	public void VipConsume(PrintWriter out,JSONObject json_data) throws SQLException
	{
		//===============================
		//���ݷֵ�Ż�ȡ���ź�
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = commonDao.selGt_idbyHotelid(json_data.getString("hotelid").trim());
		String hotelid = "";
		JSONArray ja = json_data.getJSONArray("requestData");//���յ����ѿ��Ļ�����Ϣ
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
//		for(int l=0;l<ja.size();l++)
//		{
		
			try {
				//===============================2013-10-30 �¼ӵ�
				hotelid = ja.getJSONObject(0).getString("hotelid");
				if(vipInfoDao.isChargeCodeExist(gt_id, ja.getJSONObject(0).getString("ls_pccode").trim()))
				{
					if("A".equalsIgnoreCase(ja.getJSONObject(0).getString("ls_mode").trim()))
					{
						HashMap<String, Object> mp1 = vipInfoDao.isAbleConsume(Double.valueOf(ja.getJSONObject(0).getString("ld_amt")), gt_id, ja.getJSONObject(0).getString("ls_no").trim()+gt_id);
						//���Ѳ���
						if((Integer)mp1.get("code")==0)
						{
							//����㹻����
							//==================================

							HashMap<String, String> mp = vipInfoDao.vip_post(ja.getJSONObject(0),ja.getJSONObject(0).getString("ls_no").trim()+gt_id,gt_id,json_data.getString("hotelid"));
							if(Integer.valueOf(mp.get("code"))==0)
							{
								
								//���ѳɹ�
								out.write(commonDao.generateResponseJSon("vip_post","0","0000","�����ɹ�",null));
								out.flush();
								out.close();
							}else
							{
								//����ʧ��
								out.write(commonDao.generateResponseJSon("vip_post","1","6000","δ֪����",null));
								out.flush();
								out.close();
							}
						}else{
							//����ʧ��
							System.out.println("mp:"+mp1);
							out.write(commonDao.generateResponseJSon("vip_post","1","7000","���㣬����ʧ��,ʣ����"+(Double)mp1.get("banlance"),null));
							out.flush();
							out.close();
						}
					}else{
						//��ֵ����
						HashMap<String, String> mp = vipInfoDao.vip_post(ja.getJSONObject(0),ja.getJSONObject(0).getString("ls_no").trim()+gt_id,gt_id,json_data.getString("hotelid"));
						if(Integer.valueOf(mp.get("code"))==0)
						{
							
							//��ֵ�ɹ�
							out.write(commonDao.generateResponseJSon("vip_post","0","0000","��ֵ�ɹ�",null));
							out.flush();
							out.close();
						}else
						{
							//��ֵʧ��
							out.write(commonDao.generateResponseJSon("vip_post","1","6000","δ֪����",null));
							out.flush();
							out.close();
						}
					}
				}else{
					//����ʧ��
					out.write(commonDao.generateResponseJSon("vip_post","1","7000","�����ڸø����룺"+ja.getJSONObject(0).getString("ls_pccode").trim(),null));
					out.flush();
					out.close();
				}
	
			} catch (Exception e) {
				out.write(commonDao.generateResponseJSon("vip_post","1","6000","δ֪����",null));
				out.flush();
				out.close();
				e.printStackTrace();
			}
		}
		//================================
		
//	}
	/*
	 * ���ſ�����
	 */
	public void vipSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
			List<VipSearchInfo> list = vipInfoDao.searchVipInfo(ja.getJSONObject(0),gt_id);

			JSONArray ja1 = JSONArray.fromObject(list);
			System.out.println("������Ϣ��"+ja1.toString());
			out.write(commonDao.generateResponseJSon("vip_search","0","0000","�����ɹ�",ja1));
			out.flush();
			out.close();
			
			
		
	}
	
	/*
	 *����������Ϣ
	 *
	 */
	public void vipInit(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//vip_info����
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = commonDao.selGt_idbyHotelid(json_data.getString("hotelid").trim());
		for(int l=0;l<ja.size();l++)
		{
//			if(vipInfoDao.isVipCoded(ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("no"),"vip_info"))//˵���Ѿ��������
//			{
			if(!"".equals(ja.getJSONObject(l).getString("no").trim()))//˵�����ѱ������
			{
				if(vipInfoDao.isUpdateVipInfo(ja.getJSONObject(l).getString("no"), ja.getJSONObject(l).getString("log_date"), ja.getJSONObject(l).getString("hotelid")))
				{
					//log_dateʱ��������Ҫ�£���Ҫ����
					//��������Ӧ��new_accnt
//					String accntNew = vipInfoDao.findAccnt_newByAccnt_old(ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("no"),"vip_info");

					try {
						
						//���ݷֵ�Ż�ȡ���ź�
						
						//ִ�в���vip_info�Ĳ���
						if(vipInfoDao.addVip_infoByOne(ja.getJSONObject(l),ja.getJSONObject(l).getString("no")+gt_id, null,gt_id))
						{
							JSONObject jo = new JSONObject();
							jo.put("no", ja.getJSONObject(l).getString("no").trim());
							jo.put("hotelid", ja.getJSONObject(l).getString("hotelid").trim());
							jo.put("gt_id", json_data.getString("gt_id").trim());
							out.write(commonDao.generateResponseJSon("vip_info","0","0000","�����ɹ�",JSONArray.fromObject(jo)));
							out.flush();
							out.close();
						}else{
							out.write(commonDao.generateResponseJSon("vip_info","1","6000","δ֪����",null));
							out.flush();
							out.close();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						out.write(commonDao.generateResponseJSon("vip_info","1","6000","δ֪����",null));
						out.flush();
						out.close();
						
					}
				}
				continue;
		    }else
		    {
		    	try {
		    		
					if(vipInfoDao.isAbleBeGtCard(ja.getJSONObject(l).getString("sno").trim(),gt_id))
					{
						out.write(commonDao.generateResponseJSon("vip_info","1","7000","��Ա�����ظ�",null));
						out.flush();
						out.close();
					}else{
						//����
						String accnt_new = commonDao.getGT_coder("VIP",gt_id)+gt_id;
						//��sync_accnt��������
//						if(commonDao.addSync_accnt(accnt_new, ja.getJSONObject(l).getString("no"), ja.getJSONObject(l).getString("hotelid"),"vip_info"))
//						{
							if(vipInfoDao.addVip_infoByOne(ja.getJSONObject(l),accnt_new, null,gt_id))
							{
								JSONObject jo = new JSONObject();
								jo.put("no", accnt_new.substring(0, 7));
								jo.put("hotelid", json_data.getString("hotelid").trim());
								jo.put("gt_id", json_data.getString("gt_id").trim());
								out.write(commonDao.generateResponseJSon("vip_info","0","0000","�����ɹ�",JSONArray.fromObject(jo)));
								out.flush();
								out.close();
								return;
							}else{
								out.write(commonDao.generateResponseJSon("vip_info","1","6000","δ֪����",null));
								out.flush();
								out.close();
								return;
							}
					}
				

					
				} catch (Exception e) {
					e.printStackTrace();
//					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"));
					out.write(commonDao.generateResponseJSon("vip_info","1","6000","δ֪����",null));
					out.flush();
					out.close();
					return;
				}
			
			}
			
			
		}
//		out.write(commonDao.generateResponseJSon("vip_info","0","0000","�����ɹ�",null));
//		out.flush();
//		out.close();
	
//		
	}
	
	/*
	 * �ϴ����ſ�������Ϣ
	 */
	public void VipJfUse(PrintWriter out,JSONObject json_data)
	{
		JfUseDao jfUseDao = JfUseDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		JSONArray ja = json_data.getJSONArray("requestData");
		if(Double.valueOf(ja.getJSONObject(0).getString("used"))<=0.00)//˵���ǽ�������
		{
			jfUseDao.delInsertJF_useBySysdate_accnt_gt_id(json_data.getString("hotelid").trim(),Integer.valueOf(json_data.getString("gt_id").trim()), null, ja.getJSONObject(0),jfUseDao.findMaxId(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0)));
			jfUseDao.execPreparedList(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0).getString("accnt").trim()+Integer.valueOf(json_data.getString("gt_id").trim()));
			out.write(commonDao.generateResponseJSon("upload_jf_use","0","0000","�����ɹ�",null));
			out.flush();
			out.close();
		}else{
			//˵�������ѻ���
			Double jf_balance = vipInfoDao.getYbanlanceByaccnt(ja.getJSONObject(0), Integer.valueOf(json_data.getString("gt_id").trim()));
			if(Double.valueOf(ja.getJSONObject(0).getString("used"))>jf_balance)
			{//���ѵĻ��ִ���ʣ�����
				out.write(commonDao.generateResponseJSon("upload_jf_use","1","7000","�������㣬ʣ����֣�"+jf_balance+"��",null));
				out.flush();
				out.close();
			}else{
				jfUseDao.delInsertJF_useBySysdate_accnt_gt_id(json_data.getString("hotelid").trim(),Integer.valueOf(json_data.getString("gt_id").trim()), null, ja.getJSONObject(0),jfUseDao.findMaxId(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0)));
				jfUseDao.execPreparedList(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0).getString("accnt").trim()+Integer.valueOf(json_data.getString("gt_id").trim()));
				out.write(commonDao.generateResponseJSon("upload_jf_use","0","0000","�����ɹ�",null));
				out.flush();
				out.close();
			}
		}
		

	
	}
	
	
	
	
	
///*
// * ��������
// * 
// */
//	public void vipInfoDownload(PrintWriter out,JSONObject json_data)
//	{
//		JSONArray ja = json_data.getJSONArray("requestData");//�����ţ��ֵ�ţ������������
//		CommonDao commonDao = CommonDao.getInstance();
//		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
//		List<VipInfoBean> list = new ArrayList<VipInfoBean>();
//		for(int l=0;l<ja.size();l++)//ѭ���ӷֵ��ϴ��������ţ�����ڶ�����ĸ����P��Z֮�䣬Ҫ��accnt���л�ȡ�µı���
//		{
//			
//              String compare = getSecondChar(ja.getJSONObject(l).getString("vipno"));
//              if("P".equals(compare.trim()))//���������±�����
//              {
//            	  if(vipInfoDao.isDownloadVipInfo(ja.getJSONObject(l).getString("vipno")+ja.getJSONObject(l).getString("gt_id"), ja.getJSONObject(l).getString("log_date")))
//            	  {
//            		  //��Ҫ����
//            		  //��ȡ����������¼
//            		  System.out.println("��Ҫ��������");
//            		  try {
//            			  VipInfoBean vi = vipInfoDao.findVipinfoByVno(ja.getJSONObject(l).getString("vipno")+ja.getJSONObject(l).getString("gt_id"), ja.getJSONObject(l).getString("hotelid"));
//						 list.add(vi);
//					} catch (Exception e) {
//						e.printStackTrace();
//						out.write(commonDao.generateResponseJSon("vipInfo_down","1","6000","δ֪����",null));
//						out.flush();
//						out.close();
//						return;
//					}
//            	  }
//            	  continue;
//              }else//��Ҫ�±���
//              {
//            	  try {
//            		 System.out.println("��Ҫ���������");
//					String accnt_new = commonDao.findAccnt_newByAccnt_old(ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("vipno"));//��gt_id��
//					if(accnt_new==null )
//					{
//						
//						out.write(commonDao.generateResponseJSon("vipInfo_down","1","7000","�Ҳ�����Ӧvipno",null));
//						out.flush();
//						out.close();
//						return;
//					}else{
//						VipInfoBean vi = vipInfoDao.findVipinfoByVnoOneU(accnt_new, ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("vipno"),ja.getJSONObject(l).getString("log_date"));
//						if(vi!=null)
//						list.add(vi);
//					}
//					
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				    out.write(commonDao.generateResponseJSon("vipInfo_down","1","6000","δ֪����",null));
//					out.flush();
//					out.close();
//					return;
//				}
//              }
//             
////              BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"));
//             
//		}
//		  System.out.println("��������");
//          JSONArray ja4 = JSONArray.fromObject(list);
//          out.write(commonDao.generateResponseJSon("vipInfo_down","0","0000","��Ӧ�ɹ�",ja4));
//		  out.flush();
//		  out.close();
//	}
	
	/*
	 * ���ؼ��ſ�����
	 */
	public void vipAccount_download(PrintWriter out,JSONObject json_data)
	{
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		JSONArray ja = json_data.getJSONArray("requestData");
		VipAccountDao vipAccountDao = VipAccountDao.getInstance();


			
			List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		    
			for(int l=0;l<ja.size();l++)//ѭ���ӷֵ��ϴ��������ţ�����ڶ�����ĸ����P��Z֮�䣬Ҫ��accnt���л�ȡ�µı���
			{
				
//                  String compare = getSecondChar(ja.getJSONObject(l).getString("accnt"));
//                  if("P".equals(compare.trim()))//���������±�����
//                  {
                	  System.out.println("PPPPPPPPPPP!!!!!");
                	  VipAccountBean va = null;
						List<VipAccountBean> list_account = vipAccountDao.findVipAccountByAccnt(json_data.getString("no").trim()+gt_id, gt_id);
						for(int e=0;e<list_account.size();e++)
						{
							list.add(list_account.get(e));
						}

		}
			
				JSONArray ja4 = JSONArray.fromObject(list);
				System.out.println("ja410-30:"+ja4.toString()+"��С��"+ja4.size());
//	            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"));
				out.write(commonDao.generateResponseJSon("vipAccount_down","0","0000","��������ɹ�",ja4));
				out.flush();
				out.close();
			
		   
			
//		}
		
	}
	
	
	
	public void rep_hotel_all_init(PrintWriter out,JSONObject json_data)
	{
		CommonDao commonDao = CommonDao.getInstance();
		if(json_data.getInt("counts")==0)
		{
			
			out.write(commonDao.rep_hotel_all_init_generateResponseJSon("rep_hotel_all_init","1","1000","��Ӫҵ����ģ��",null,null));
			out.flush();
			out.close();
			return;
		}
		JSONArray ja = json_data.getJSONArray("requestData");//Rep_hotel��Ϣ
		
		String hotelid = json_data.getString("hotelid").trim();
		//���ݷֵ�Ż�ȡ���ź�
	
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id").trim());
	
		
//		boolean flag1 = rhs.addRep_hotel(ja,gt_id);
		boolean flag1 = commonDao.addRep_hotel(ja,gt_id,json_data.getString("sysdate").trim(),hotelid);
		List<Jf_def> list_jf_def = null;
		JSONArray ja_jf_def = null;
		StringBuffer sb = null;
		System.out.println("flag1:"+flag1);
		if(flag1)
		{
			commonDao.updateCtr_index_jyo(hotelid);
			sb = commonDao.getJf_Def_codeBygt_id(gt_id);
			list_jf_def = commonDao.getJf_DefByhotelid_gt_id(gt_id);
			ja_jf_def = JSONArray.fromObject(list_jf_def);
			out.write(commonDao.rep_hotel_all_init_generateResponseJSon("rep_hotel_all_init","0","0000","Ӫҵ�����ϴ��ɹ�",sb.toString(),ja_jf_def));
			out.flush();
			out.close();
		}else{
			out.write(commonDao.rep_hotel_all_init_generateResponseJSon("rep_hotel_all_init","1","6000","δ֪����",null,null));
			out.flush();
			out.close();
		}
	}
	
	
	public void rep_hotel_all(PrintWriter out,JSONObject json_data)
	{
		int resultCode = 0;
		JSONArray ja = json_data.getJSONArray("rep_hotel_all");//Rep_hotel��Ϣ
		CommonDao commonDao = CommonDao.getInstance();
		String hotelid = json_data.getString("hotelid");
		
		Integer gt_id =Integer.valueOf(json_data.getString("gt_id"));
		//======================================add by 7.24
		
		List<Jf_def> list_jf_def = null;
		JSONArray ja_jf_def = null;
		StringBuffer sb = null;
		sb = commonDao.getJf_Def_codeBygt_id(gt_id);
		list_jf_def = commonDao.getJf_DefByhotelid_gt_id(gt_id);
		ja_jf_def = JSONArray.fromObject(list_jf_def);

		JSONArray ja3 = null;
		JSONArray ja4 = null;
		
		
//		JSONArray ja4 = JSONArray.fromObject(infos[6]);
		JSONArray ja_detail_back = null;
		JSONArray ja_list_back = null;
		JSONArray ja_company_back = null;
		//��ɾ��
		JSONArray ja1 = json_data.getJSONArray("jf_detail_nolocal");//detail_not
		
			
		if(ja1.size()>0)
		{
			for(int a=0;a<ja1.size();a++)
			{
				//ֱ�Ӳ��뵽jf_datail
				commonDao.addJF_detailByOne(ja1,null,gt_id,true,hotelid,json_data.getString("sysdate"));
			}
				
		}
		
		
		
		    JSONArray ja2 = json_data.getJSONArray("jf_detail_local");
			if(ja2.size()>0)
			{
				
					try {
//						String accnt_new = commonDao.findAccnt_newByAccnt_old(hotelid, ja2.getJSONObject(b).getString("accnt"));
						commonDao.addJF_detailByOne(ja2,null,gt_id,false,hotelid,json_data.getString("sysdate"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				
			}
		
		

//		if(!"null".equals(handledMsg[5]))
//		{
			if(ja3.size()>0)
			{
			  ja3 = json_data.getJSONArray("jf_use_nolocal");//use_not
				//ֱ�Ӳ��뵽jf_use
			  commonDao.delInsertJF_useBySysdate_accnt_gt_id(hotelid.trim(),gt_id, null, ja3,true);
		
			}
		  ja4 = json_data.getJSONArray("jf_use_local");;
		 
				try {
//					String accnt_new  = rhs.findAccnt_newByAccnt_old(ja4.getJSONObject(d).getString("hotelid").trim(), ja4.getJSONObject(d).getString("accnt").trim());
					commonDao.delInsertJF_useBySysdate_accnt_gt_id(hotelid.trim(),gt_id, null, ja4,false);
//					String accnt_new = rhs.findAccnt_newByAccnt_old(hotelid, ja4.getJSONObject(d).getString("accnt"));
//					rhs.addJF_useByOne(ja4.getJSONObject(d),accnt_new,gt_id,false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			//ִ�����ܵĸ���jf_list�Ĺ���
		    commonDao.execPreparedList(hotelid, gt_id, json_data.getString("sysdate"),null,true);
		
		
		//========================================
		//==========================================add by  7.25
		
			ja_company_back = JSONArray.fromObject(commonDao.findCompanyItemsByhotelid(gt_id));
			boolean flag1 = commonDao.addRep_hotel_audit(ja,gt_id,json_data.getString("gt_sysdate"),hotelid);
			System.out.println("flag1:"+flag1);
			if(flag1)
			{
				commonDao.updateCtr_index_jyo(hotelid);				
				out.write(commonDao.rep_hotel_all_generateResponseJSon("audit","0","0000","δ֪����",sb.toString(),ja_jf_def,ja_company_back));
				out.flush();
				out.close();
			}else{//����ʧ��
				out.write(commonDao.rep_hotel_all_generateResponseJSon("audit","1","7000","Ӫҵ���ݲ���ʧ��",null,null,null));
				out.flush();
				out.close();
			}
	}
	
	/*
	 * �����޸�
	 */
	public synchronized void vipInfoUpdate(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//vip_info����
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		Iterator it = ja.getJSONObject(0).keys();
		boolean isSno = false;
		while(it.hasNext())
		{
			
			if("sno".equalsIgnoreCase(it.next().toString()))
			{
				if(vipInfoDao.isAbleBeGtCard(ja.getJSONObject(0).getString("sno"), Integer.valueOf(json_data.getString("gt_id"))))
				{
					//���޸ĵĿ����Ѵ���
					isSno = true;
				}
				break;
			}
			
		}
		
		if(isSno)
		{
			out.write(commonDao.generateResponseJSon("vip_info_update","1","7000","���޸ĵĿ���:"+ja.getJSONObject(0).getString("sno")+"�Ѵ���",null));
			out.flush();
			out.close();
		}else{
			if(vipInfoDao.updateVipInfo(Integer.valueOf(json_data.getString("gt_id")),ja,json_data.getString("no")))
			{
				out.write(commonDao.generateResponseJSon("vip_info_update","0","0000","�޸������ɹ�",null));
				out.flush();
				out.close();
				System.out.println("�޸ĳɹ�");
			}else{
				out.write(commonDao.generateResponseJSon("vip_info_update","1","1000","�޸�����ʧ��",null));
				out.flush();
				out.close();
				System.out.println("�޸�ʧ��");
			}
		}
		System.out.println("�޸����");
		
		
		
	}
	/**
	 * 
	 */
	public synchronized void uploadRoomMapAndType(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja_avl = json_data.getJSONArray("avl");//������Դ����
		JSONArray ja_rmmp = json_data.getJSONArray("roomMap");//��̬����
		RoomResourceDao roomResourceDao = RoomResourceDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		if(roomResourceDao.addRoomTypeInfo(ja_avl, Integer.valueOf(json_data.getString("gt_id")), json_data.getString("hotelid"))&&roomResourceDao.addRoomMapStatus(ja_rmmp, Integer.valueOf(json_data.getString("gt_id")), json_data.getString("hotelid")))
		{
			commonDao.updateCtr_index_jyo(getSysTime(),json_data.getString("hotelid"),"avl_show_index");
			commonDao.updateCtr_index_jyo(getSysTime(),json_data.getString("hotelid"),"roommap_index");
			out.write(commonDao.generateResponseJSon("avl","0","0000","�ϴ����ͷ�̬�ɹ�",null));
			out.flush();
			out.close();
		}else{
			out.write(commonDao.generateResponseJSon("avl","1","7000","�ϴ����ͻ��߷�̬ʧ��",null));
			out.flush();
			out.close();
		}
	}
	
	/*
	 * ���ؽ��౨����������
	 */
	public void vipAccount_download1(PrintWriter out,JSONObject json_data)
	{
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = commonDao.selGt_idbyHotelid(json_data.getString("hotelid_card"));
		JSONArray ja = json_data.getJSONArray("requestData");
		VipAccountDao vipAccountDao = VipAccountDao.getInstance();


			
			List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		    
			for(int l=0;l<ja.size();l++)//ѭ���ӷֵ��ϴ��������ţ�����ڶ�����ĸ����P��Z֮�䣬Ҫ��accnt���л�ȡ�µı���
			{
				
//                  String compare = getSecondChar(ja.getJSONObject(l).getString("accnt"));
//                  if("P".equals(compare.trim()))//���������±�����
//                  {
                	  System.out.println("PPPPPPPPPPP!!!!!");
                	  VipAccountBean va = null;
						List<VipAccountBean> list_account = vipAccountDao.findVipAccount(ja, gt_id, json_data.getString("hotelid"));
						for(int e=0;e<list_account.size();e++)
						{
							list.add(list_account.get(e));
						}

		}
			
				JSONArray ja4 = JSONArray.fromObject(list);
				System.out.println("ja410-30:"+ja4.toString()+"��С��"+ja4.size());
				out.write(commonDao.generateResponseJSon("vipAccountForReport_down","0","0000","��������ɹ�(���౨��)",ja4));
				out.flush();
				out.close();
	}
	
	/*
	 * ��ȡ�ü����µ����й������
	 */
	public void vipCardTypeSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Vip_cardtype_rmcodeBean> list = vipInfoDao.searchVipCardType_web(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon_double("vip_cardtype_search","0","0000","�����ɹ�",ja1,ja_rmpricecode));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü����µ����й������
	 */
	public void vipCardTypeSearch_shr(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Vip_cardtypeBean> list = vipInfoDao.searchVipCardType(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
//		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_cardtype_search_shr","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü����¸�����
	 */
	public void vip_charge_codeSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Sys_charge_code> list = vipInfoDao.searchSys_charge_code(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_sys_charge_code","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü����·�����
	 */
	public void room_pricecodeSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Room_priceCodeBean> list = vipInfoDao.searchRoom_pricecode(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("room_price_code","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	//=====================================Сޱ
	/*
	 * ���ӷ�����
	 */
	public void room_pricecodeAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addRoomCode(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("addRoomCode","0","0000","���ӳɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addRoomCode","1","1000","����ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * ɾ��������
	 */
	public void room_pricecodeDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteRoomCode(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("deleteRoomCode","0","0000","ɾ���ɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteRoomCode","1","1000","ɾ��ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * ���Ӹ�����
	 */
	public void sys_charge_codeAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addSys_charge_code(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("addSys_charge_code","0","0000","���ӳɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addSys_charge_code","1","1000","����ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * ɾ��������
	 */
	public void sys_charge_codeDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteSys_charge_code(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("deleteSys_charge_code","0","0000","ɾ���ɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteSys_charge_code","1","1000","ɾ��ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * ���ӹ��������
	 */
	public void vip_cardtypeAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addVip_cardtype(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("addVip_cardtype","0","0000","���ӳɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addVip_cardtype","1","1000","����ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * ɾ�����������
	 */
	public void vip_cardtypeDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteVip_cardtype(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("deleteVip_cardtype","0","0000","ɾ���ɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteVip_cardtype","1","1000","ɾ��ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}

	/*
	 * ��ȡ���ſ��б�
	 */
	public void vipListSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<VipSearchInfo> list = vipInfoDao.gainVipInfoList(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon_viplist("vipList_search","0","0000","�����ɹ�",ja1,counts));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * Ԥ��Ȩȡ��
	 */
	public void pre_authorization_cancel(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		VipAccountDao vipAccountDao = VipAccountDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		if(vipAccountDao.isAbleCancel(ja.getJSONObject(0), gt_id)&&!vipAccountDao.isHaveCanceled(ja.getJSONObject(0), gt_id))//�Ƿ��Ǳ������Ԥ��Ȩȡ��
		{
			synchronized(vipAccountDao)
			{
				if(vipAccountDao.addAndUpdateVipAccount(ja.getJSONObject(0), gt_id,vipAccountDao.getMaxNumber(ja.getJSONObject(0), gt_id)))//���������ȡ����¼������
				{
					//���¹�������
					vipAccountDao.updateVipInfo(ja.getJSONObject(0),gt_id);
					 //Ԥ��Ȩȡ���ɹ�
					  out.write(commonDao.generateResponseJSon("pre_authorization_cancel","0","0000","Ԥ��Ȩȡ���ɹ�",null));
					  out.flush();
					  out.close();
				}else{
					out.write(commonDao.generateResponseJSon("pre_authorization_cancel","1","7000","���������������ʧ��",null));
					out.flush();
					out.close();
				}
			}
			
		}else{
			out.write(commonDao.generateResponseJSon("pre_authorization_cancel","1","7000","����ȡ���Ǳ��귢�еĹ����������߸�Ԥ��Ȩ�Ѿ�ȡ������",null));
			  out.flush();
			  out.close();
		}
		
		
	}
	
	/*
	 * ��ȡ���ŷֵ��б�
	 */
	public void getHotelidList(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		VipCardDao vipCardDao = VipCardDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		List<HotelidBean> hotelid_list = vipCardDao.findHotleidListByGtId(gt_id);
		JSONArray ja_hotelid = JSONArray.fromObject(hotelid_list);
		out.write(commonDao.generateResponseJSon("hotelid_list","0","0000","��ȡ�ɹ�",ja_hotelid));
		out.flush();
		out.close();
		
		
		
	}
	
	/*
	 * ��ȡ���ŷֵ��б�
	 */
	public void getMapRoomno(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		RoomMapDao roomMapDao = RoomMapDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		List<RoomMapBean> hotelid_list = roomMapDao.getRoomMapByHotelid(ja.getJSONObject(0).getString("hotelid"), gt_id);
		JSONArray ja_Maproomno = JSONArray.fromObject(hotelid_list);
		out.write(commonDao.generateResponseJSon("map_roomno","0","0000","��ȡ�ɹ�",ja_Maproomno));
		out.flush();
		out.close();
		
		
		
	}
	
	/*
	 * ��¼��֤
	 */
	public void uc_loginValidate(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		UcLoginDao ucLoginDao = UcLoginDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		HashMap<String, Object> mp = ucLoginDao.validate_login(ja.getJSONObject(0));
		if((Integer)mp.get("op")==0)
		{//ͨ����¼��֤
			JSONObject jo = JSONObject.fromObject(mp);
			out.write(commonDao.generateResponseJSon("uc_login","0","0000","��¼�ɹ�",JSONArray.fromObject(jo)));
			out.flush();
			out.close();
			
		}else{
			//��¼ʧ��
			out.write(commonDao.generateResponseJSon("uc_login","1","7000","��¼ʧ��",null));
			out.flush();
			out.close();
		}
		
	}
	
	public void upload_jf_detail(PrintWriter out, JSONObject jsonData) {
		
		JfDetailDao  jfDetailDao = JfDetailDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		if(jsonData.getJSONArray("requestData")!=null)
		{
			if(jfDetailDao.addJfDetail(jsonData))
			{
				out.write(commonDao.generateResponseJSon("upload_jf_detail","0","0000","�ϴ��ɹ�",null));
				out.flush();
				out.close();
			}else{
				out.write(commonDao.generateResponseJSon("upload_jf_detail","1","0000","�ϴ�ʧ��",null));
				out.flush();
				out.close();
			}
		}else{
			out.write(commonDao.generateResponseJSon("upload_jf_detail","0","0000","�ϴ��ɹ�",null));
			out.flush();
			out.close();
		}
		
		
	}
	
	public void audit(PrintWriter out, JSONObject jsonData) {
			
			Rep_hotel_allDao  rep_hotel_allDao = Rep_hotel_allDao.getInstance();
			JSONArray ja = jsonData.getJSONArray("requestData");
			CommonDao commonDao = CommonDao.getInstance();
			if(rep_hotel_allDao.addRepHotel(jsonData, ja))
			{
				out.write(commonDao.generateResponseJSon("audit","0","0000","ҹ��ɹ�",null));
				out.flush();
				out.close();
			}else{
				out.write(commonDao.generateResponseJSon("audit","1","0000","ҹ��ʧ��",null));
				out.flush();
				out.close();
			}
			
		}
	
	/*
	 * ���ݿ��Ż�ȡ���ſ���Ϣ
	 */
	public void getVipInfoBysno(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<VipCardDetail> list = vipInfoDao.getVipInfo(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("vipCard_detail","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ���ݷֵ�ź����ڻ�ȡӪҵ�ܱ�����
	 */
	public void getRepHotel(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_hotel_allBean> list = rep_hotel_allDao.getRepHotel(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_hotel","0","0000","��ȡӪҵ�ܱ��ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ���ݷֵ�ź����������ȡӪҵ�ܱ�����
	 */
	public void getRepHotelCounts(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_CountsBean> list = rep_hotel_allDao.getRepCounts(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_hotelCounts","0","0000","��ȡ�ֵ�ͳ�Ƴɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü����µ����п��͵Ļ��ֱ���
	 */
	public void jfdefSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfDefDao jfDefDao = JfDefDao.getInstance();
		List<Jf_def> list = jfDefDao.searchJfdef(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_def_search","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ɾ�����ֱ���
	 */
	public void jf_defDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteJf_def(ja.getJSONObject(0)))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("deleteJf_def","0","0000","ɾ���ɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteJf_def","1","1000","ɾ��ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	/*
	 * ���ӻ��ֱ���
	 */
	public void jfDefAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������¼
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addJfdef(ja))
		  {
			  //���ӳɹ�
			  out.write(commonDao.generateResponseJSon("addJf_def","0","0000","���ӳɹ�",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addJf_def","1","1000","����ʧ��",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * ��ȡ�ü����µ�ָ���˺��˺ŵ����л��ֽ��������Ѽ�¼
	 */
	public void jf_useSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfUseDao jfUseDao = JfUseDao.getInstance();
		List<Jf_useBean> list = jfUseDao.searchJfuse(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_use_search","0","0000","��ѯ�ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü����µ�ָ���˺�,ʱ�����䣬���ţ�����µ����л��ֽ��������Ѽ�¼
	 */
	public void jf_useSearchBySysdtae(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfUseDao jfUseDao = JfUseDao.getInstance();
		List<Jf_useBySysdateBean> list = jfUseDao.searchJfuseBySysdate(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_use_searchBySysdate","0","0000","��ѯ�ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü����µ�ָ���˺ŵ����л�����ϸ��¼
	 */
	public void jf_detailSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfDetailDao jfDetailDao = JfDetailDao.getInstance();
		List<Jf_detailBean> list = jfDetailDao.searchJfdetail(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_detail_search","0","0000","��ѯ�ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ������Դͳ����Ϣ
	 */
	public void roomResourceSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		RoomMapDao roomMapDao = RoomMapDao.getInstance();
		RoomResourceCount roomResourceCount = roomMapDao.getRoomResource(gt_id, json_data.getString("hotelid").trim());
		List<RoomResourceCount> list = new ArrayList<RoomResourceCount>();
		list.add(roomResourceCount);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("room_resource_count","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * �޸ĵ�¼����
	 */
	public void password_update(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		UcLoginDao ucLoginDao = UcLoginDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		
		if(ucLoginDao.updatePwd(ja.getJSONObject(0)))
		{//�޸ĳɹ�
			out.write(commonDao.generateResponseJSon("password_update","0","0000","�޸ĳɹ�",null));
			out.flush();
			out.close();
			
		}else{
			//�޸�ʧ��
			out.write(commonDao.generateResponseJSon("password_update","1","7000","�޸�ʧ��",null));
			out.flush();
			out.close();
		}
		
	}
	
	/*
	 * �������ںͼ��źŻ�ȡ�����ձ���
	 */
	public void getRep_Gt_day(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_Gt_DayBean>  list = rep_hotel_allDao.getRepGtDay(json_data.getInt("gt_id"), json_data.getJSONArray("requestData").getJSONObject(0).getString("sysdate").trim());
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_gt_day","0","0000","��ȡ�����ձ����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��������,���źŻ�ֵ�Ż�ȡ�������ֵ��ϸ��
	 */
	public void getRep_Credit_Detail(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_CreditBean>  list = rep_hotel_allDao.getRepCredit(json_data.getInt("gt_id"), json_data.getJSONArray("requestData").getJSONObject(0).getString("bdate").trim(),json_data.getJSONArray("requestData").getJSONObject(0).getString("edate").trim(),json_data.getJSONArray("requestData").getJSONObject(0).getString("hotelid").trim());
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_credit_detail","0","0000","��ȡ��ֵ��ϸ���ݳɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	/*
	 * ��������,���źŻ�ֵ�Ż�ȡ���Ž��ͳ��
	 */
	public void getRep_CreditChargeTotal(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_CreditCharge_TotalBean>  list = rep_hotel_allDao.getRepCreditChargeTotal(json_data.getInt("gt_id"), json_data.getJSONArray("requestData").getJSONObject(0).getString("bdate").trim(),json_data.getJSONArray("requestData").getJSONObject(0).getString("edate").trim(),json_data.getJSONArray("requestData").getJSONObject(0).getString("hotelid").trim());
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_credit_charge_total","0","0000","��ȡ���ͳ�����ݳɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	/*
	 * ��ȡ�ü����µ�ָ�����ŵ���־��¼
	 */
	public void vip_logSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("������"+json_data);
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		LogRecordDao logRecordDao = LogRecordDao.getInstance();
		List<com.dl.pojo.Vip_info_logBean> list = logRecordDao.searchVipLog(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_log_search","0","0000","��ѯ��־�ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�ü��ŵ����й�����͵Ļ��ֶһ���Ǯ����
	 */
	public void vipJfToMoneySearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfToMoneyDao jfToMoneyDao = JfToMoneyDao.getInstance();
		List<JfToMoneyBean> list = jfToMoneyDao.searchJfToMoney(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
//		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_jfToMoney_search","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * �޸Ŀ��ͻ��ֶһ�����
	 */
	public void JfToMoney_update(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		JfToMoneyDao jfToMoneyDao = JfToMoneyDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		
		if(jfToMoneyDao.updateJfToMoney(ja.getJSONObject(0), json_data.getInt("gt_id")))
		{//�޸ĳɹ�
			out.write(commonDao.generateResponseJSon("vip_jfToMoney_update","0","0000","�޸ĳɹ�",null));
			out.flush();
			out.close();
			
		}else{
			//�޸�ʧ��
			out.write(commonDao.generateResponseJSon("vip_jfToMoney_update","1","7000","�޸�ʧ��",null));
			out.flush();
			out.close();
		}
		
	}
	
	/*
	 * ��ȡ�ü����µ����п��͵Ļ��ֱ���
	 */
	public void jfdefSearchByType(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("������"+json_data);
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfDefDao jfDefDao = JfDefDao.getInstance();
		List<Jf_def_shr> list = jfDefDao.searchJfdefByCardType(gt_id, ja.getJSONObject(0).getString("code").trim());
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_def_search_shr","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ��ȡ�������յļ��Ż�Ա��Ϣ
	 */
	public void vipSearchByBirthday(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		String hotelid = json_data.getString("hotelid").trim();
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
			List<VipSearchInfo> list = vipInfoDao.searchVipInfoByBirthday(ja.getJSONObject(0), gt_id, hotelid);

			JSONArray ja1 = JSONArray.fromObject(list);
			System.out.println("������Ϣ��"+ja1.toString());
			out.write(commonDao.generateResponseJSon("vip_info_birthday","0","0000","�����ɹ�",ja1));
			out.flush();
			out.close();
			
			
		
	}
	//���ֶһ���Ϣ��ѯ
	public void rep_jf_useSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("������"+json_data);
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		Rep_pmsDao rep_pmsDao = Rep_pmsDao.getInstance();
		List<Rep_jf_use> list = rep_pmsDao.getRepJfUse(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_jf_use_search","0","0000","��ѯ�ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	//���ֶһ���ϸ��ѯ
	public void rep_jf_useDetailSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("������"+json_data);
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		 JfDetailDao jfDetailDao = JfDetailDao.getInstance();
		List<Jf_detail_new> list = jfDetailDao.searchJfdetailNew(json_data,Integer.valueOf(json_data.getString("gt_id").trim()));
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_jf_use_detail_search","0","0000","��ѯ�ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * ����ʱ����Լ��ֵ�Ż�ȡԓ�ֵ꼯�ſ���������ĳ�ֵ�������
	 * ���������꼯�ſ��ڱ���ĳ�ֵ�������
	 */
	public void rep_credit_charge(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//���յ��������Ļ�����Ϣ
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
		List<Rep_CreditChargeBean> list = rep_hotel_allDao.getCreditChargeRecords(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
//		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("������Ϣ��"+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_credit_charge","0","0000","�����ɹ�",ja1));
		out.flush();
		out.close();
			
			
		
	}
	//��ȡ��ǰʱ��
	private String getSysTime() {
		Date today = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(today);

	}

	
}