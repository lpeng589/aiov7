package com.menyi.web.util;

import javax.servlet.http.*;
import java.util.Locale;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.io.*;
import org.apache.struts.action.ActionServlet;

import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.web.login.LoginBean;

/**
 *
 * <p>Title: Locale处理类，可进行页面语言切换</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class LocaleServlet extends ActionServlet {

    /**
     * 初始化语言
     * @param config ServletConfig
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {

    }
    /**
     * 语言切换
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    public void service(HttpServletRequest req,HttpServletResponse res){
        String loc=req.getParameter("LOCALE");
        Hashtable ht= (Hashtable)req.getSession().getServletContext().getAttribute("LocaleTable");
        Locale locale=(Locale)ht.get(loc);
        if(loc.equals("zh_TW")){
            req.getSession().setAttribute("useLanguage",loc);
        }
        if(locale!=null){
            req.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY,locale);
        }
        try {
            req.getRequestDispatcher("/login.jsp").forward(req, res) ;
        } catch (Exception ex) {
        }
    }
}
