package com.menyi.web.util;

import java.io.*;
import java.util.*;

import javax.servlet.*;

import org.apache.struts.action.*;

/**
 *
 * <p>Title: ������servlet</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class BaseServlet extends ActionServlet {

    public void destroy() {
    }

    public void init() throws ServletException {
        super.init();
        System.setProperty("java.awt.headless","true");
    }
}
