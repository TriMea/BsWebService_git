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
	 * 集团卡消费
	 */
	public void VipConsume(PrintWriter out,JSONObject json_data) throws SQLException
	{
		//===============================
		//根据分店号获取集团号
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = commonDao.selGt_idbyHotelid(json_data.getString("hotelid").trim());
		String hotelid = "";
		JSONArray ja = json_data.getJSONArray("requestData");//接收到消费卡的基本信息
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
//		for(int l=0;l<ja.size();l++)
//		{
		
			try {
				//===============================2013-10-30 新加的
				hotelid = ja.getJSONObject(0).getString("hotelid");
				if(vipInfoDao.isChargeCodeExist(gt_id, ja.getJSONObject(0).getString("ls_pccode").trim()))
				{
					if("A".equalsIgnoreCase(ja.getJSONObject(0).getString("ls_mode").trim()))
					{
						HashMap<String, Object> mp1 = vipInfoDao.isAbleConsume(Double.valueOf(ja.getJSONObject(0).getString("ld_amt")), gt_id, ja.getJSONObject(0).getString("ls_no").trim()+gt_id);
						//消费操作
						if((Integer)mp1.get("code")==0)
						{
							//余额足够消费
							//==================================

							HashMap<String, String> mp = vipInfoDao.vip_post(ja.getJSONObject(0),ja.getJSONObject(0).getString("ls_no").trim()+gt_id,gt_id,json_data.getString("hotelid"));
							if(Integer.valueOf(mp.get("code"))==0)
							{
								
								//消费成功
								out.write(commonDao.generateResponseJSon("vip_post","0","0000","操作成功",null));
								out.flush();
								out.close();
							}else
							{
								//消费失败
								out.write(commonDao.generateResponseJSon("vip_post","1","6000","未知错误",null));
								out.flush();
								out.close();
							}
						}else{
							//消费失败
							System.out.println("mp:"+mp1);
							out.write(commonDao.generateResponseJSon("vip_post","1","7000","余额不足，消费失败,剩余余额："+(Double)mp1.get("banlance"),null));
							out.flush();
							out.close();
						}
					}else{
						//充值操作
						HashMap<String, String> mp = vipInfoDao.vip_post(ja.getJSONObject(0),ja.getJSONObject(0).getString("ls_no").trim()+gt_id,gt_id,json_data.getString("hotelid"));
						if(Integer.valueOf(mp.get("code"))==0)
						{
							
							//充值成功
							out.write(commonDao.generateResponseJSon("vip_post","0","0000","充值成功",null));
							out.flush();
							out.close();
						}else
						{
							//充值失败
							out.write(commonDao.generateResponseJSon("vip_post","1","6000","未知错误",null));
							out.flush();
							out.close();
						}
					}
				}else{
					//操作失败
					out.write(commonDao.generateResponseJSon("vip_post","1","7000","不存在该付款码："+ja.getJSONObject(0).getString("ls_pccode").trim(),null));
					out.flush();
					out.close();
				}
	
			} catch (Exception e) {
				out.write(commonDao.generateResponseJSon("vip_post","1","6000","未知错误",null));
				out.flush();
				out.close();
				e.printStackTrace();
			}
		}
		//================================
		
//	}
	/*
	 * 集团卡搜索
	 */
	public void vipSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
			List<VipSearchInfo> list = vipInfoDao.searchVipInfo(ja.getJSONObject(0),gt_id);

			JSONArray ja1 = JSONArray.fromObject(list);
			System.out.println("返回信息："+ja1.toString());
			out.write(commonDao.generateResponseJSon("vip_search","0","0000","操作成功",ja1));
			out.flush();
			out.close();
			
			
		
	}
	
	/*
	 *添加主单信息
	 *
	 */
	public void vipInit(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//vip_info数据
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = commonDao.selGt_idbyHotelid(json_data.getString("hotelid").trim());
		for(int l=0;l<ja.size();l++)
		{
//			if(vipInfoDao.isVipCoded(ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("no"),"vip_info"))//说明已经编码过了
//			{
			if(!"".equals(ja.getJSONObject(l).getString("no").trim()))//说明是已编码过的
			{
				if(vipInfoDao.isUpdateVipInfo(ja.getJSONObject(l).getString("no"), ja.getJSONObject(l).getString("log_date"), ja.getJSONObject(l).getString("hotelid")))
				{
					//log_date时间比中央的要新，需要更新
					//检索出相应的new_accnt
//					String accntNew = vipInfoDao.findAccnt_newByAccnt_old(ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("no"),"vip_info");

					try {
						
						//根据分店号获取集团号
						
						//执行插入vip_info的操作
						if(vipInfoDao.addVip_infoByOne(ja.getJSONObject(l),ja.getJSONObject(l).getString("no")+gt_id, null,gt_id))
						{
							JSONObject jo = new JSONObject();
							jo.put("no", ja.getJSONObject(l).getString("no").trim());
							jo.put("hotelid", ja.getJSONObject(l).getString("hotelid").trim());
							jo.put("gt_id", json_data.getString("gt_id").trim());
							out.write(commonDao.generateResponseJSon("vip_info","0","0000","操作成功",JSONArray.fromObject(jo)));
							out.flush();
							out.close();
						}else{
							out.write(commonDao.generateResponseJSon("vip_info","1","6000","未知错误",null));
							out.flush();
							out.close();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						out.write(commonDao.generateResponseJSon("vip_info","1","6000","未知错误",null));
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
						out.write(commonDao.generateResponseJSon("vip_info","1","7000","会员卡号重复",null));
						out.flush();
						out.close();
					}else{
						//编码
						String accnt_new = commonDao.getGT_coder("VIP",gt_id)+gt_id;
						//向sync_accnt插入数据
//						if(commonDao.addSync_accnt(accnt_new, ja.getJSONObject(l).getString("no"), ja.getJSONObject(l).getString("hotelid"),"vip_info"))
//						{
							if(vipInfoDao.addVip_infoByOne(ja.getJSONObject(l),accnt_new, null,gt_id))
							{
								JSONObject jo = new JSONObject();
								jo.put("no", accnt_new.substring(0, 7));
								jo.put("hotelid", json_data.getString("hotelid").trim());
								jo.put("gt_id", json_data.getString("gt_id").trim());
								out.write(commonDao.generateResponseJSon("vip_info","0","0000","操作成功",JSONArray.fromObject(jo)));
								out.flush();
								out.close();
								return;
							}else{
								out.write(commonDao.generateResponseJSon("vip_info","1","6000","未知错误",null));
								out.flush();
								out.close();
								return;
							}
					}
				

					
				} catch (Exception e) {
					e.printStackTrace();
//					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"));
					out.write(commonDao.generateResponseJSon("vip_info","1","6000","未知错误",null));
					out.flush();
					out.close();
					return;
				}
			
			}
			
			
		}
//		out.write(commonDao.generateResponseJSon("vip_info","0","0000","操作成功",null));
//		out.flush();
//		out.close();
	
//		
	}
	
	/*
	 * 上传集团卡积分信息
	 */
	public void VipJfUse(PrintWriter out,JSONObject json_data)
	{
		JfUseDao jfUseDao = JfUseDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		JSONArray ja = json_data.getJSONArray("requestData");
		if(Double.valueOf(ja.getJSONObject(0).getString("used"))<=0.00)//说明是奖励积分
		{
			jfUseDao.delInsertJF_useBySysdate_accnt_gt_id(json_data.getString("hotelid").trim(),Integer.valueOf(json_data.getString("gt_id").trim()), null, ja.getJSONObject(0),jfUseDao.findMaxId(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0)));
			jfUseDao.execPreparedList(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0).getString("accnt").trim()+Integer.valueOf(json_data.getString("gt_id").trim()));
			out.write(commonDao.generateResponseJSon("upload_jf_use","0","0000","操作成功",null));
			out.flush();
			out.close();
		}else{
			//说明是消费积分
			Double jf_balance = vipInfoDao.getYbanlanceByaccnt(ja.getJSONObject(0), Integer.valueOf(json_data.getString("gt_id").trim()));
			if(Double.valueOf(ja.getJSONObject(0).getString("used"))>jf_balance)
			{//消费的积分大于剩余积分
				out.write(commonDao.generateResponseJSon("upload_jf_use","1","7000","积分余额不足，剩余积分（"+jf_balance+"）",null));
				out.flush();
				out.close();
			}else{
				jfUseDao.delInsertJF_useBySysdate_accnt_gt_id(json_data.getString("hotelid").trim(),Integer.valueOf(json_data.getString("gt_id").trim()), null, ja.getJSONObject(0),jfUseDao.findMaxId(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0)));
				jfUseDao.execPreparedList(Integer.valueOf(json_data.getString("gt_id").trim()),ja.getJSONObject(0).getString("accnt").trim()+Integer.valueOf(json_data.getString("gt_id").trim()));
				out.write(commonDao.generateResponseJSon("upload_jf_use","0","0000","操作成功",null));
				out.flush();
				out.close();
			}
		}
		

	
	}
	
	
	
	
	
///*
// * 主单下载
// * 
// */
//	public void vipInfoDownload(PrintWriter out,JSONObject json_data)
//	{
//		JSONArray ja = json_data.getJSONArray("requestData");//主单号，分店号，最近消费日期
//		CommonDao commonDao = CommonDao.getInstance();
//		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
//		List<VipInfoBean> list = new ArrayList<VipInfoBean>();
//		for(int l=0;l<ja.size();l++)//循环从分店上传的主单号，如果第二个字母不在P和Z之间，要在accnt表中获取新的编码
//		{
//			
//              String compare = getSecondChar(ja.getJSONObject(l).getString("vipno"));
//              if("P".equals(compare.trim()))//本身就是新编码了
//              {
//            	  if(vipInfoDao.isDownloadVipInfo(ja.getJSONObject(l).getString("vipno")+ja.getJSONObject(l).getString("gt_id"), ja.getJSONObject(l).getString("log_date")))
//            	  {
//            		  //需要下载
//            		  //获取该条主单记录
//            		  System.out.println("需要下载主单");
//            		  try {
//            			  VipInfoBean vi = vipInfoDao.findVipinfoByVno(ja.getJSONObject(l).getString("vipno")+ja.getJSONObject(l).getString("gt_id"), ja.getJSONObject(l).getString("hotelid"));
//						 list.add(vi);
//					} catch (Exception e) {
//						e.printStackTrace();
//						out.write(commonDao.generateResponseJSon("vipInfo_down","1","6000","未知错误",null));
//						out.flush();
//						out.close();
//						return;
//					}
//            	  }
//            	  continue;
//              }else//需要新编码
//              {
//            	  try {
//            		 System.out.println("需要编码的主单");
//					String accnt_new = commonDao.findAccnt_newByAccnt_old(ja.getJSONObject(l).getString("hotelid"), ja.getJSONObject(l).getString("vipno"));//含gt_id的
//					if(accnt_new==null )
//					{
//						
//						out.write(commonDao.generateResponseJSon("vipInfo_down","1","7000","找不到对应vipno",null));
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
//				    out.write(commonDao.generateResponseJSon("vipInfo_down","1","6000","未知错误",null));
//					out.flush();
//					out.close();
//					return;
//				}
//              }
//             
////              BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"));
//             
//		}
//		  System.out.println("发送主单");
//          JSONArray ja4 = JSONArray.fromObject(list);
//          out.write(commonDao.generateResponseJSon("vipInfo_down","0","0000","响应成功",ja4));
//		  out.flush();
//		  out.close();
//	}
	
	/*
	 * 下载集团卡账务
	 */
	public void vipAccount_download(PrintWriter out,JSONObject json_data)
	{
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		JSONArray ja = json_data.getJSONArray("requestData");
		VipAccountDao vipAccountDao = VipAccountDao.getInstance();


			
			List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		    
			for(int l=0;l<ja.size();l++)//循环从分店上传的主单号，如果第二个字母不在P和Z之间，要在accnt表中获取新的编码
			{
				
//                  String compare = getSecondChar(ja.getJSONObject(l).getString("accnt"));
//                  if("P".equals(compare.trim()))//本身就是新编码了
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
				System.out.println("ja410-30:"+ja4.toString()+"大小："+ja4.size());
//	            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"));
				out.write(commonDao.generateResponseJSon("vipAccount_down","0","0000","下载账务成功",ja4));
				out.flush();
				out.close();
			
		   
			
//		}
		
	}
	
	
	
	public void rep_hotel_all_init(PrintWriter out,JSONObject json_data)
	{
		CommonDao commonDao = CommonDao.getInstance();
		if(json_data.getInt("counts")==0)
		{
			
			out.write(commonDao.rep_hotel_all_init_generateResponseJSon("rep_hotel_all_init","1","1000","无营业数据模板",null,null));
			out.flush();
			out.close();
			return;
		}
		JSONArray ja = json_data.getJSONArray("requestData");//Rep_hotel信息
		
		String hotelid = json_data.getString("hotelid").trim();
		//根据分店号获取集团号
	
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
			out.write(commonDao.rep_hotel_all_init_generateResponseJSon("rep_hotel_all_init","0","0000","营业数据上传成功",sb.toString(),ja_jf_def));
			out.flush();
			out.close();
		}else{
			out.write(commonDao.rep_hotel_all_init_generateResponseJSon("rep_hotel_all_init","1","6000","未知错误",null,null));
			out.flush();
			out.close();
		}
	}
	
	
	public void rep_hotel_all(PrintWriter out,JSONObject json_data)
	{
		int resultCode = 0;
		JSONArray ja = json_data.getJSONArray("rep_hotel_all");//Rep_hotel信息
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
		//先删除
		JSONArray ja1 = json_data.getJSONArray("jf_detail_nolocal");//detail_not
		
			
		if(ja1.size()>0)
		{
			for(int a=0;a<ja1.size();a++)
			{
				//直接插入到jf_datail
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
				//直接插入到jf_use
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
			//执行杨总的更新jf_list的过程
		    commonDao.execPreparedList(hotelid, gt_id, json_data.getString("sysdate"),null,true);
		
		
		//========================================
		//==========================================add by  7.25
		
			ja_company_back = JSONArray.fromObject(commonDao.findCompanyItemsByhotelid(gt_id));
			boolean flag1 = commonDao.addRep_hotel_audit(ja,gt_id,json_data.getString("gt_sysdate"),hotelid);
			System.out.println("flag1:"+flag1);
			if(flag1)
			{
				commonDao.updateCtr_index_jyo(hotelid);				
				out.write(commonDao.rep_hotel_all_generateResponseJSon("audit","0","0000","未知错误",sb.toString(),ja_jf_def,ja_company_back));
				out.flush();
				out.close();
			}else{//插入失败
				out.write(commonDao.rep_hotel_all_generateResponseJSon("audit","1","7000","营业数据插入失败",null,null,null));
				out.flush();
				out.close();
			}
	}
	
	/*
	 * 主单修改
	 */
	public synchronized void vipInfoUpdate(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//vip_info数据
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
					//待修改的卡号已存在
					isSno = true;
				}
				break;
			}
			
		}
		
		if(isSno)
		{
			out.write(commonDao.generateResponseJSon("vip_info_update","1","7000","待修改的卡号:"+ja.getJSONObject(0).getString("sno")+"已存在",null));
			out.flush();
			out.close();
		}else{
			if(vipInfoDao.updateVipInfo(Integer.valueOf(json_data.getString("gt_id")),ja,json_data.getString("no")))
			{
				out.write(commonDao.generateResponseJSon("vip_info_update","0","0000","修改主单成功",null));
				out.flush();
				out.close();
				System.out.println("修改成功");
			}else{
				out.write(commonDao.generateResponseJSon("vip_info_update","1","1000","修改主单失败",null));
				out.flush();
				out.close();
				System.out.println("修改失败");
			}
		}
		System.out.println("修改完毕");
		
		
		
	}
	/**
	 * 
	 */
	public synchronized void uploadRoomMapAndType(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja_avl = json_data.getJSONArray("avl");//房型资源数据
		JSONArray ja_rmmp = json_data.getJSONArray("roomMap");//房态数据
		RoomResourceDao roomResourceDao = RoomResourceDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		if(roomResourceDao.addRoomTypeInfo(ja_avl, Integer.valueOf(json_data.getString("gt_id")), json_data.getString("hotelid"))&&roomResourceDao.addRoomMapStatus(ja_rmmp, Integer.valueOf(json_data.getString("gt_id")), json_data.getString("hotelid")))
		{
			commonDao.updateCtr_index_jyo(getSysTime(),json_data.getString("hotelid"),"avl_show_index");
			commonDao.updateCtr_index_jyo(getSysTime(),json_data.getString("hotelid"),"roommap_index");
			out.write(commonDao.generateResponseJSon("avl","0","0000","上传房型房态成功",null));
			out.flush();
			out.close();
		}else{
			out.write(commonDao.generateResponseJSon("avl","1","7000","上传房型或者房态失败",null));
			out.flush();
			out.close();
		}
	}
	
	/*
	 * 下载交班报表所需数据
	 */
	public void vipAccount_download1(PrintWriter out,JSONObject json_data)
	{
		CommonDao commonDao = CommonDao.getInstance();
		Integer gt_id = commonDao.selGt_idbyHotelid(json_data.getString("hotelid_card"));
		JSONArray ja = json_data.getJSONArray("requestData");
		VipAccountDao vipAccountDao = VipAccountDao.getInstance();


			
			List<VipAccountBean> list = new ArrayList<VipAccountBean>();
		    
			for(int l=0;l<ja.size();l++)//循环从分店上传的主单号，如果第二个字母不在P和Z之间，要在accnt表中获取新的编码
			{
				
//                  String compare = getSecondChar(ja.getJSONObject(l).getString("accnt"));
//                  if("P".equals(compare.trim()))//本身就是新编码了
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
				System.out.println("ja410-30:"+ja4.toString()+"大小："+ja4.size());
				out.write(commonDao.generateResponseJSon("vipAccountForReport_down","0","0000","下载账务成功(交班报表)",ja4));
				out.flush();
				out.close();
	}
	
	/*
	 * 获取该集团下的所有贵宾卡型
	 */
	public void vipCardTypeSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Vip_cardtype_rmcodeBean> list = vipInfoDao.searchVipCardType_web(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon_double("vip_cardtype_search","0","0000","操作成功",ja1,ja_rmpricecode));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团下的所有贵宾卡型
	 */
	public void vipCardTypeSearch_shr(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Vip_cardtypeBean> list = vipInfoDao.searchVipCardType(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
//		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_cardtype_search_shr","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团下付款码
	 */
	public void vip_charge_codeSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Sys_charge_code> list = vipInfoDao.searchSys_charge_code(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_sys_charge_code","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团下房价码
	 */
	public void room_pricecodeSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		List<Room_priceCodeBean> list = vipInfoDao.searchRoom_pricecode(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("room_price_code","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	//=====================================小薇
	/*
	 * 增加房价码
	 */
	public void room_pricecodeAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addRoomCode(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("addRoomCode","0","0000","添加成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addRoomCode","1","1000","添加失败",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * 删除房价码
	 */
	public void room_pricecodeDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteRoomCode(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("deleteRoomCode","0","0000","删除成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteRoomCode","1","1000","删除失败",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * 增加付款码
	 */
	public void sys_charge_codeAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addSys_charge_code(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("addSys_charge_code","0","0000","添加成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addSys_charge_code","1","1000","添加失败",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * 删除付款码
	 */
	public void sys_charge_codeDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteSys_charge_code(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("deleteSys_charge_code","0","0000","删除成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteSys_charge_code","1","1000","删除失败",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * 增加贵宾卡卡型
	 */
	public void vip_cardtypeAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addVip_cardtype(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("addVip_cardtype","0","0000","添加成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addVip_cardtype","1","1000","添加失败",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * 删除贵宾卡卡型
	 */
	public void vip_cardtypeDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteVip_cardtype(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("deleteVip_cardtype","0","0000","删除成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteVip_cardtype","1","1000","删除失败",null));
			  out.flush();
			  out.close();
		  }

	}

	/*
	 * 获取集团卡列表
	 */
	public void vipListSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<VipSearchInfo> list = vipInfoDao.gainVipInfoList(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon_viplist("vipList_search","0","0000","操作成功",ja1,counts));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 预授权取消
	 */
	public void pre_authorization_cancel(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		VipAccountDao vipAccountDao = VipAccountDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		if(vipAccountDao.isAbleCancel(ja.getJSONObject(0), gt_id)&&!vipAccountDao.isHaveCanceled(ja.getJSONObject(0), gt_id))//是否是本店进行预授权取消
		{
			synchronized(vipAccountDao)
			{
				if(vipAccountDao.addAndUpdateVipAccount(ja.getJSONObject(0), gt_id,vipAccountDao.getMaxNumber(ja.getJSONObject(0), gt_id)))//向账务表插取消记录并更新
				{
					//更新贵宾卡余额
					vipAccountDao.updateVipInfo(ja.getJSONObject(0),gt_id);
					 //预授权取消成功
					  out.write(commonDao.generateResponseJSon("pre_authorization_cancel","0","0000","预授权取消成功",null));
					  out.flush();
					  out.close();
				}else{
					out.write(commonDao.generateResponseJSon("pre_authorization_cancel","1","7000","向账务表添加数据失败",null));
					out.flush();
					out.close();
				}
			}
			
		}else{
			out.write(commonDao.generateResponseJSon("pre_authorization_cancel","1","7000","不能取消非本店发行的贵宾卡账务或者该预授权已经取消过了",null));
			  out.flush();
			  out.close();
		}
		
		
	}
	
	/*
	 * 获取集团分店列表
	 */
	public void getHotelidList(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		VipCardDao vipCardDao = VipCardDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		List<HotelidBean> hotelid_list = vipCardDao.findHotleidListByGtId(gt_id);
		JSONArray ja_hotelid = JSONArray.fromObject(hotelid_list);
		out.write(commonDao.generateResponseJSon("hotelid_list","0","0000","获取成功",ja_hotelid));
		out.flush();
		out.close();
		
		
		
	}
	
	/*
	 * 获取集团分店列表
	 */
	public void getMapRoomno(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		RoomMapDao roomMapDao = RoomMapDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		List<RoomMapBean> hotelid_list = roomMapDao.getRoomMapByHotelid(ja.getJSONObject(0).getString("hotelid"), gt_id);
		JSONArray ja_Maproomno = JSONArray.fromObject(hotelid_list);
		out.write(commonDao.generateResponseJSon("map_roomno","0","0000","获取成功",ja_Maproomno));
		out.flush();
		out.close();
		
		
		
	}
	
	/*
	 * 登录验证
	 */
	public void uc_loginValidate(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		UcLoginDao ucLoginDao = UcLoginDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		HashMap<String, Object> mp = ucLoginDao.validate_login(ja.getJSONObject(0));
		if((Integer)mp.get("op")==0)
		{//通过登录验证
			JSONObject jo = JSONObject.fromObject(mp);
			out.write(commonDao.generateResponseJSon("uc_login","0","0000","登录成功",JSONArray.fromObject(jo)));
			out.flush();
			out.close();
			
		}else{
			//登录失败
			out.write(commonDao.generateResponseJSon("uc_login","1","7000","登录失败",null));
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
				out.write(commonDao.generateResponseJSon("upload_jf_detail","0","0000","上传成功",null));
				out.flush();
				out.close();
			}else{
				out.write(commonDao.generateResponseJSon("upload_jf_detail","1","0000","上传失败",null));
				out.flush();
				out.close();
			}
		}else{
			out.write(commonDao.generateResponseJSon("upload_jf_detail","0","0000","上传成功",null));
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
				out.write(commonDao.generateResponseJSon("audit","0","0000","夜审成功",null));
				out.flush();
				out.close();
			}else{
				out.write(commonDao.generateResponseJSon("audit","1","0000","夜审失败",null));
				out.flush();
				out.close();
			}
			
		}
	
	/*
	 * 根据卡号获取集团卡信息
	 */
	public void getVipInfoBysno(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<VipCardDetail> list = vipInfoDao.getVipInfo(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("vipCard_detail","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 根据分店号和日期获取营业总表数据
	 */
	public void getRepHotel(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_hotel_allBean> list = rep_hotel_allDao.getRepHotel(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_hotel","0","0000","获取营业总表成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 根据分店号和日期区间获取营业总表数据
	 */
	public void getRepHotelCounts(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_CountsBean> list = rep_hotel_allDao.getRepCounts(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_hotelCounts","0","0000","获取分店统计成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团下的所有卡型的积分比例
	 */
	public void jfdefSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfDefDao jfDefDao = JfDefDao.getInstance();
		List<Jf_def> list = jfDefDao.searchJfdef(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_def_search","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 删除积分比例
	 */
	public void jf_defDelete(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.deleteJf_def(ja.getJSONObject(0)))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("deleteJf_def","0","0000","删除成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("deleteJf_def","1","1000","删除失败",null));
			  out.flush();
			  out.close();
		  }

	}
	/*
	 * 增加积分比例
	 */
	public void jfDefAdd(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到房价码记录
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
		  if(systemSettingDao.addJfdef(ja))
		  {
			  //添加成功
			  out.write(commonDao.generateResponseJSon("addJf_def","0","0000","添加成功",null));
			  out.flush();
			  out.close();
		  }else{
			  out.write(commonDao.generateResponseJSon("addJf_def","1","1000","添加失败",null));
			  out.flush();
			  out.close();
		  }

	}
	
	/*
	 * 获取该集团下的指定账号账号的所有积分奖励和消费记录
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
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_use_search","0","0000","查询成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团下的指定账号,时间区间，工号，班次下的所有积分奖励和消费记录
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
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_use_searchBySysdate","0","0000","查询成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团下的指定账号的所有积分详细记录
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
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_detail_search","0","0000","查询成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取房间资源统计信息
	 */
	public void roomResourceSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		RoomMapDao roomMapDao = RoomMapDao.getInstance();
		RoomResourceCount roomResourceCount = roomMapDao.getRoomResource(gt_id, json_data.getString("hotelid").trim());
		List<RoomResourceCount> list = new ArrayList<RoomResourceCount>();
		list.add(roomResourceCount);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("room_resource_count","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 修改登录密码
	 */
	public void password_update(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		UcLoginDao ucLoginDao = UcLoginDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		
		if(ucLoginDao.updatePwd(ja.getJSONObject(0)))
		{//修改成功
			out.write(commonDao.generateResponseJSon("password_update","0","0000","修改成功",null));
			out.flush();
			out.close();
			
		}else{
			//修改失败
			out.write(commonDao.generateResponseJSon("password_update","1","7000","修改失败",null));
			out.flush();
			out.close();
		}
		
	}
	
	/*
	 * 根据日期和集团号获取集团日报表
	 */
	public void getRep_Gt_day(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
//		Integer counts = vipInfoDao.gainVipInfoListCounts(ja.getJSONObject(0), gt_id);
		List<Rep_Gt_DayBean>  list = rep_hotel_allDao.getRepGtDay(json_data.getInt("gt_id"), json_data.getJSONArray("requestData").getJSONObject(0).getString("sysdate").trim());
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_gt_day","0","0000","获取集团日报表成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 根据日期,集团号或分店号获取贵宾卡充值明细表
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
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_credit_detail","0","0000","获取充值明细数据成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	/*
	 * 根据日期,集团号或分店号获取集团借贷统计
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
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_credit_charge_total","0","0000","获取借贷统计数据成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	/*
	 * 获取该集团下的指定卡号的日志记录
	 */
	public void vip_logSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("参数："+json_data);
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		LogRecordDao logRecordDao = LogRecordDao.getInstance();
		List<com.dl.pojo.Vip_info_logBean> list = logRecordDao.searchVipLog(ja.getJSONObject(0),gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_log_search","0","0000","查询日志成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取该集团的所有贵宾卡型的积分兑换金钱比率
	 */
	public void vipJfToMoneySearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfToMoneyDao jfToMoneyDao = JfToMoneyDao.getInstance();
		List<JfToMoneyBean> list = jfToMoneyDao.searchJfToMoney(gt_id);
		JSONArray ja1 = JSONArray.fromObject(list);
//		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("vip_jfToMoney_search","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 修改卡型积分兑换比率
	 */
	public void JfToMoney_update(PrintWriter out,JSONObject json_data)
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		JfToMoneyDao jfToMoneyDao = JfToMoneyDao.getInstance();
		CommonDao commonDao = CommonDao.getInstance();
		
		if(jfToMoneyDao.updateJfToMoney(ja.getJSONObject(0), json_data.getInt("gt_id")))
		{//修改成功
			out.write(commonDao.generateResponseJSon("vip_jfToMoney_update","0","0000","修改成功",null));
			out.flush();
			out.close();
			
		}else{
			//修改失败
			out.write(commonDao.generateResponseJSon("vip_jfToMoney_update","1","7000","修改失败",null));
			out.flush();
			out.close();
		}
		
	}
	
	/*
	 * 获取该集团下的所有卡型的积分比例
	 */
	public void jfdefSearchByType(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("参数："+json_data);
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		JfDefDao jfDefDao = JfDefDao.getInstance();
		List<Jf_def_shr> list = jfDefDao.searchJfdefByCardType(gt_id, ja.getJSONObject(0).getString("code").trim());
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("jf_def_search_shr","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 获取当日生日的集团会员信息
	 */
	public void vipSearchByBirthday(PrintWriter out,JSONObject json_data) throws Exception
	{
		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		String hotelid = json_data.getString("hotelid").trim();
		CommonDao commonDao = CommonDao.getInstance();
		VipInfoDao vipInfoDao = VipInfoDao.getInstance();
			List<VipSearchInfo> list = vipInfoDao.searchVipInfoByBirthday(ja.getJSONObject(0), gt_id, hotelid);

			JSONArray ja1 = JSONArray.fromObject(list);
			System.out.println("返回信息："+ja1.toString());
			out.write(commonDao.generateResponseJSon("vip_info_birthday","0","0000","操作成功",ja1));
			out.flush();
			out.close();
			
			
		
	}
	//积分兑换信息查询
	public void rep_jf_useSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("参数："+json_data);
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		Rep_pmsDao rep_pmsDao = Rep_pmsDao.getInstance();
		List<Rep_jf_use> list = rep_pmsDao.getRepJfUse(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_jf_use_search","0","0000","查询成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	//积分兑换明细查询
	public void rep_jf_useDetailSearch(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		System.out.println("参数："+json_data);
//		Integer gt_id = Integer.valueOf(json_data.getString("gt_id"));
		CommonDao commonDao = CommonDao.getInstance();
		 JfDetailDao jfDetailDao = JfDetailDao.getInstance();
		List<Jf_detail_new> list = jfDetailDao.searchJfdetailNew(json_data,Integer.valueOf(json_data.getString("gt_id").trim()));
		JSONArray ja1 = JSONArray.fromObject(list);
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_jf_use_detail_search","0","0000","查询成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	
	/*
	 * 根据时间段以及分店号获取該分店集团卡在其他店的充值消费情况
	 * 或者其他店集团卡在本店的充值消费情况
	 */
	public void rep_credit_charge(PrintWriter out,JSONObject json_data) throws Exception
	{
//		JSONArray ja = json_data.getJSONArray("requestData");//接收到搜索卡的基本信息
//		String hotelid_local = ja.getJSONObject(0).getString("hotelid");
		
		
		CommonDao commonDao = CommonDao.getInstance();
		Rep_hotel_allDao rep_hotel_allDao = Rep_hotel_allDao.getInstance();
		List<Rep_CreditChargeBean> list = rep_hotel_allDao.getCreditChargeRecords(json_data);
		JSONArray ja1 = JSONArray.fromObject(list);
//		JSONArray ja_rmpricecode = JSONArray.fromObject(vipInfoDao.searchRoom_pricecode(gt_id));
		System.out.println("返回信息："+ja1.toString());
		out.write(commonDao.generateResponseJSon("rep_credit_charge","0","0000","操作成功",ja1));
		out.flush();
		out.close();
			
			
		
	}
	//获取当前时间
	private String getSysTime() {
		Date today = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(today);

	}

	
}
