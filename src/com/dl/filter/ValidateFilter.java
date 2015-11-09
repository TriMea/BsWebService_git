package com.dl.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dl.dao.CommonDao;

import net.sf.json.JSONObject;

public class ValidateFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		  HttpServletResponse response = (HttpServletResponse) res;
          HttpServletRequest request = (HttpServletRequest) req;
          JSONObject json_data = JSONObject.fromObject(request.getParameter("data"));
          CommonDao commonDao = CommonDao.getInstance();
          response.setHeader("Access-Control-Allow-Origin", "*");
  		  PrintWriter out = response.getWriter();
//  		  if(!"18".equals(json_data.getString("gt_id").trim()))
//  		  {
  			 if(json_data.has("licence"))
  	          {
  	        	  String licence = json_data.getString("licence");
  	        	  if(licence==null)
  	              {//δ�ṩ���ܹ���
  	            	  out.write(commonDao.generateResponseJSon(json_data.getString("serviceName"), "1", "0007", "���󱻾ܾ�������Ч����", null));
  	            	  out.flush();
  	            	  out.close();
  	              }else if("dlhis123".equals(licence.trim()))
  	              {
  	            	  //ͨ��������幷������ͨ�������֤��
  	            	chain.doFilter(req, res);
  	              }
  	        	  else if(licence.trim().length()!=16)
  	              {//δ�ṩ��Ч���ܹ���
  	            	  out.write(commonDao.generateResponseJSon(json_data.getString("serviceName"), "1", "0007", "���󱻾ܾ�������Ч����", null));
  	            	  out.flush();
  	            	  out.close();     	  
  	              }else{
  	            	  String hotelid = json_data.getString("hotelid");
//  	            	  Integer gt_id = Integer.valueOf(json_data.getString("gt_id").trim());
  	            	
  	            	  if(commonDao.isValid(hotelid.trim(), licence.trim()))
  	            	  {//��Ч����
  	            		  chain.doFilter(req, res);
  	            	  }else{
  	            		  //��Ч����
  	            		  out.write(commonDao.generateResponseJSon(json_data.getString("serviceName"), "1", "0007", "���󱻾ܾ�������Ч����", null));
  	                	  out.flush();
  	                	  out.close();
  	            	  }
  	              }
  	          }else{
  	        	  out.write(commonDao.generateResponseJSon(json_data.getString("serviceName"), "1", "0007", "���󱻾ܾ������ܹ��Ų�ƥ�䣩", null));
  	        	  out.flush();
  	        	  out.close();
  	          }
//  		  }else{
//  			 chain.doFilter(req, res);
//  		  }
         
         
          
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
