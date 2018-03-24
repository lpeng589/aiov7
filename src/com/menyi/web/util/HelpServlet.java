package com.menyi.web.util;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionForward;
import java.util.Locale;
import org.apache.struts.action.ActionForm;
import java.util.Enumeration;

/**
 *
 * <p>Title: 帮助的处理类</p>
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
public class HelpServlet extends ActionServlet{


    /**
     * 处理帮助
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest req, HttpServletResponse resp) throws
        ServletException, IOException {
        //获得session中当前模块的帮助url
        Object obj = req.getSession().getAttribute(BaseEnv.CUR_HELP_URL);
        if (obj != null) {
            resp.sendRedirect("/" + GlobalsTool.getLocale(req).toString() + obj.toString());
        }
        else{
            resp.sendRedirect("/" + GlobalsTool.getLocale(req).toString() + "/welcome.jsp");
        }
    }

}
