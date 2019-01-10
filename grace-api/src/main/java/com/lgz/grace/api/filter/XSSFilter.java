package com.lgz.grace.api.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤替换script脚本符号
 * 注意：过滤不了enctype="multipart/form-data"类型的表单，需要单独实现
 * @author mouzongmin
 * @date 2016年4月6日
 */
public class XSSFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);  
		chain.doFilter(xssRequest, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
