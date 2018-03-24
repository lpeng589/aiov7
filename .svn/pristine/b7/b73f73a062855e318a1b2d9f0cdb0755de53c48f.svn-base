package com.menyi.aio.web.wxqy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 微信调用接口
 * @author 邓志鹏
 *
 */
public class WeixinApiServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 微信接口入口根据op判断操作类型
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operate =request.getParameter("operate");
		if (operate!=null) {
			if(operate.equals("get")){
				login(request,response);
			}
		}
	}
	/**
	 * 企业号登录aio接口
	 * @param request
	 * @param response
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("111");
	}
}
	