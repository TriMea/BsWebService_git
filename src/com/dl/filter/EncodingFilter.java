package com.dl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class EncodingFilter implements Filter {

	private String encoding;  
	
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httprequest = (HttpServletRequest) request; 
		response.setContentType("text/html;charset=" + encoding);  
        if ("GET".equals(httprequest.getMethod()))  
        {  
            // 将httpRequest进行包装   
            EncodingHttpServletRequest wrapper = new EncodingHttpServletRequest(httprequest, encoding);   
            chain.doFilter(wrapper, response);  
        }  
        else  
        {  
            request.setCharacterEncoding(encoding);  
            
            chain.doFilter(request, response);  
        }  

	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		encoding = fConfig.getInitParameter("encoding");  

	}

}
