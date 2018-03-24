package com.menyi.web.util;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.bsf.BSFManager;
import org.apache.log4j.PropertyConfigurator;

import com.dbfactory.hibernate.DBUtil;
import com.koron.oa.workflow.consignation.WorkConsignationThread;
import com.menyi.aio.web.bol88.AIOBOL88Mgt;
import com.menyi.aio.web.systemSafe.SystemSafeTimer;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;

/**
 *
 * <p>Title: 基本的过滤器</p>
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

public class BaseFilter implements Filter {

    private String encoding = null;
    private FilterConfig filterConfig = null;
    private boolean ignore = true;
    ConfigRefresh configRefresh = null;
    private TimingMsgThread timingMsg = null;
    private CareforThread carefor = null;
    private CalendarThread calendarThread = null ;
    private AlertThread alertThread = null ;
    private WorkConsignationThread consignThread = null ;
    private WorkFlowThread workFlowThread=null;
    private TaskThread taskThread = null ;

    public void destroy() {
        try {
            //最先销毁配置文件时钟
            BaseEnv.log.debug("--------Destroy Config Refresh Thread---------------");
            if (configRefresh != null) {
                configRefresh.stopThread();
            }
            //销毁定时Email、短信的发送
            if (timingMsg != null) {
                timingMsg.stopThread();
            }
            timingMsg = null;
            
            if (carefor != null) {
                carefor.stopThread();
            }
            carefor = null;
            configRefresh = null;
            this.encoding = null;

            //销毁BaseEnv 内存变量
            BaseEnv.defineSqlMap = new HashMap(); //用来标识自定义sql
            BaseEnv.functionInterface = new HashMap();
            BaseEnv.evalManager = new BSFManager(); //计算字符串
            BaseEnv.popupSelectMap = new HashMap(); //用来标识弹出选择框
            BaseEnv.defineAlertMap = new HashMap(); //保存自定义预警信息
            BaseEnv.systemSet = new Hashtable();
            BaseEnv.popSelectFiles = new ArrayList();
            BaseEnv.defineSqlFiles = new ArrayList();
            BaseEnv.defineAlertFileNames = new ArrayList();
            BaseEnv.moduleMap = new HashMap();

            /**
             * 销毁 hibernate
             */
            BaseEnv.log.debug("--------Destroy Hibernate---------------");
            try {
                DBUtil.destroyHibnate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * 销毁application变量
             */
            Enumeration emum = filterConfig.getServletContext().
                               getInitParameterNames();
            while (emum.hasMoreElements()) {
                String name = emum.nextElement().toString();
                filterConfig.getServletContext().removeAttribute(name);
            }

            this.filterConfig = null;

            try{
                 throw new Exception();
            }catch (Exception ex1) {
                 BaseEnv.log.info("测试执行路径",ex1);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException,
            ServletException {
        if (ignore || (request.getCharacterEncoding() == null)) {
            String encoding = selectEncoding(request);
            if (encoding != null)
                request.setCharacterEncoding(encoding);
        }
        Object o = ((HttpServletRequest) request).getSession(true).getAttribute(
                org.apache.struts.Globals.LOCALE_KEY);
        if (o == null) {
            Locale locale = (Locale) ((HttpServletRequest) request).getSession().
                            getServletContext().getAttribute("DefaultLocale");
            if (locale == null) {
                locale = Locale.getDefault();
            }

            ((HttpServletRequest) request).getSession(true).setAttribute(org.
                    apache.struts.Globals.LOCALE_KEY, locale);
        }

        HttpServletResponse httpRsp = (HttpServletResponse) response;
//        httpRsp.setHeader("Cache-control","private");
//        httpRsp.setHeader("Pragram","private");

        //强制页面无缓存
        httpRsp.setHeader("Cache-Control", "no-store");
        httpRsp.setHeader("Pragram", "no-cache");
        httpRsp.setDateHeader("Expires", 0);

//        httpRsp.setHeader("Expires","0");
//        httpRsp.setHeader("Cache-control","no-cache");
//        httpRsp.setHeader("Pragram","no-cache");
//        httpRsp.setHeader("Expires","0");
        //request.setl

        // Pass control on to the next filter
        chain.doFilter(request, response);

    }


    public void init(FilterConfig filterConfig) throws ServletException {
        try{
            this.filterConfig = filterConfig;
            this.encoding = filterConfig.getInitParameter("encoding");
            String value = filterConfig.getInitParameter("ignore");
            if (value == null)
                this.ignore = true;
            else if (value.equalsIgnoreCase("true"))
                this.ignore = true;
            else if (value.equalsIgnoreCase("yes"))
                this.ignore = true;
            else
                this.ignore = false;
            
            BaseEnv.systemRealPath = filterConfig.getServletContext().getRealPath("/");//设置系统运行时路径
            
            BaseEnv.servletContext = filterConfig.getServletContext();
            
            
            
            //拷贝文件到系统下面
            //初始化日志
            System.out.println("--------Init Logger---------------");
            initLog4j();
            
            long freeMemory = Runtime.getRuntime().freeMemory()/(1024*1024);
            long totalMemory = Runtime.getRuntime().totalMemory()/(1024*1024);
            long maxMemory = Runtime.getRuntime().maxMemory()/(1024*1024);
            
            BaseEnv.log.info("系统开始启动，最大内存："+maxMemory+";目前总内存："+totalMemory+";目前空闲内存："+freeMemory);

            //初始化安全U遁
            initkeyId();
            
            setPort(filterConfig.getServletContext());//设置系统的端口号

            //启动配置文件时钟
            configRefresh = new ConfigRefresh(filterConfig.getServletContext());
            InitMenData initMenData = new InitMenData();
            initMenData.initSystem(filterConfig.getServletContext());
            BaseEnv.initMenData = initMenData;
            configRefresh.start();
            
            //启动邮件自动接收线程
            MailAutoReceive mailAutoReceive = new MailAutoReceive(filterConfig.getServletContext());
            mailAutoReceive.start();
            
            //启动客户关怀事件 的自动发送短信和邮件
            CareforThread carefor = new CareforThread(filterConfig.getServletContext());
            carefor.start();
            
            //初始化电子商务邮件池
            AIOBOL88Mgt bol88 = new AIOBOL88Mgt();
            bol88.initMailSetPool();
            
            //初始化在线人员
            UserMgt.initOnlineUser();

            //定时消息,预警消息
            timingMsg = new TimingMsgThread(filterConfig.getServletContext());
            timingMsg.start();  //启动定时设置
            //日历提醒
            calendarThread = new CalendarThread() ;
            calendarThread.start() ;
            
            //提醒设置
            alertThread = new AlertThread(filterConfig.getServletContext()) ;
            alertThread.start() ;

            //任务管理
            taskThread = new TaskThread(filterConfig.getServletContext()) ;
            taskThread.start() ;
            
            //工作委托
            consignThread = new WorkConsignationThread();
            consignThread.start();
            
            //工作流提醒，如延时
            workFlowThread = new WorkFlowThread(filterConfig.getServletContext());
            workFlowThread.start(); 
            
            //系统安全备份
            SystemSafeTimer.init();
            
        }catch(Exception e){
            BaseEnv.log.error("BaseFilter.init error ",e);
            throw new ServletException("BaseFilter Error",e);
        }
        /**
         *   *****************************************************************
         *   ****此处原来需初始化较多东西，现转移至InitMenData的initSystem方法中****
         *   *****************************************************************
         */
    }
    
    //设置服务器的端口号
    private void setPort(ServletContext servletContext){
    	try {
			DataInputStream dis = new DataInputStream(new FileInputStream("../conf/server.xml"));
			byte[] bs = new byte[dis.available()];
			dis.readFully(bs);
			String s = new String(bs);
			//<Connector port="9081" protocol="HTTP/1.1"
			Pattern pattern = Pattern.compile("<Connector[\\s]*port=\"([\\d]*)\"[\\s]*protocol=\"HTTP/1.1\"",Pattern.CASE_INSENSITIVE);
	    	Matcher matcher = pattern.matcher(s);	
	        if (matcher.find()){
	        	String port = matcher.group(1);
	        	BaseEnv.log.info("===========Tomcat Port="+port);
	        	servletContext.setAttribute("SERVERPORT", port);
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    /**
     * 复制文件到指定目录下
     */
    private void copyFile(){
    	File path64 = new File(BaseEnv.systemRealPath + "\\WEB-INF\\jacob-1.17-M2-x64.dll");
    	File path32 = new File(BaseEnv.systemRealPath + "\\WEB-INF\\jacob-1.17-M2-x86.dll");
    	
		File file64 = new File(System.getProperty("user.dir") + "\\" + path64.getName());
		FileHandler.copyFile(path64.getPath(), file64.getPath());
		File file32 = new File(System.getProperty("user.dir") + "\\" + path32.getName());
		FileHandler.copyFile(path32.getPath(), file32.getPath());
		String cup = System.getProperty("sun.arch.data.model");
		if("64".equals(cup)){
			File desktop = new File("C:\\Windows\\SysWOW64\\config\\systemprofile\\Desktop");
			if(!desktop.exists()){
				desktop.mkdirs();
			}
		}
    }

    private void initLog4j() {
        try {
            Properties prop = new Properties();
            InputStream is = new FileInputStream(
                    "../../config/log4j.properties");
//                this.getClass().getResourceAsStream(
//                "/log4j.properties");
            prop.load(is);
            PropertyConfigurator.configure(prop);
        } catch (Exception ex) {
        }
    }

    private void initkeyId() {
        try {
            byte[] b1 = new byte[] {(byte) - 95, (byte) 72, (byte) 115,
                        (byte) 80, (byte) 108, (byte) - 16, (byte) - 53,
                        (byte) 118,
                        (byte) 70, (byte) 66, (byte) 43, (byte) - 31,
                        (byte) - 73, (byte) - 17, (byte) - 38, (byte) - 58,
                        (byte) 111, (byte) 35, (byte) - 21, (byte) 78,
                        (byte) 61, (byte) - 54, (byte) - 92, (byte) 52};
            byte[] b2 = new byte[] {(byte) - 105, (byte) 111, (byte) 119,
                        (byte) 111, (byte) - 30, (byte) 26, (byte) 74,
                        (byte) - 116,
                        (byte) - 54, (byte) - 62, (byte) - 103, (byte) 18,
                        (byte) 56, (byte) - 68, (byte) 71, (byte) - 69,
                        (byte) - 78,
                        (byte) - 119, (byte) - 53, (byte) - 109, (byte) - 60,
                        (byte) - 43, (byte) 85, (byte) - 114, (byte) - 10,
                        (byte) - 91,
                        (byte) - 18, (byte) - 30, (byte) - 25, (byte) 71,
                        (byte) 124, (byte) 67, (byte) 113, (byte) 115,
                        (byte) - 103,
                        (byte) - 103, (byte) 113, (byte) - 24, (byte) - 92,
                        (byte) 12};
            SecretKey deskey = new SecretKeySpec(b1, "DESede");
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] bb = c1.doFinal(b2);
            String bs = new String(bb, 1, bb.length - 4);
            UsbKey.keyId = bs;
        } catch (Exception ex) {
        }

    }


    protected String selectEncoding(ServletRequest request) {
        return (this.encoding);
    }

}
