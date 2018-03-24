package com.menyi.web.util;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import com.dbfactory.Result;
import com.menyi.aio.web.usermanage.UserMgt;
/**
 * <p>Title: </p>
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
public class ConfigRefresh extends Thread {
    private static boolean go = true;
    private static InitMenData initMenData = new InitMenData();
    private ServletContext servletContext;
    private static long lastTime = 0;
    
    private static long lastInfoTime = 0; //最后读取消息的时间，正常，30分钟一次

    public ConfigRefresh(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.setDaemon(true);
    }

    public static void stopThread() {
        go = false;
    }
    
    public static void getLastInfo(String serverPort){
    	try{
	    	lastInfoTime = System.currentTimeMillis();                	
	    	Result rs = initMenData.getLastInfoId();
	    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
	        	ServerConnection connc = new ServerConnection(BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12, 0xF, 0x58,
	                (byte) 0x88, 0x10, 0x40, 0x38
	                , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
	                , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
	
	            String posStrc = "<operation>getInfo</operation>";
	            String retc = "";
	            
	           
	            posStrc += "<dogId>" + SystemState.instance.dogId + "</dogId>" +
	                "<infoId>"+rs.retVal+"</infoId>"+
	                "<port>" + serverPort + "</port>";
	            BaseEnv.log.debug("ConfigRefresh.getLastInfo -- and refresh ip");
	            retc = connc.send(posStrc);                  
	            
	        	if(retc!= null && retc.length() > 0){
	        		
	        		String remoteName = getValue(retc,"remoteName");
	        		if(remoteName != null && remoteName.length() > 0 ){
	        			SystemState.instance.remoteName = remoteName;
	        		}
	        		
	        		String id = getValue(retc,"id");
	        		String content = getValue(retc,"content");	                		
	        		if(id != null){
	        			
	        			content = URLDecoder.decode(content,"UTF-8");
	        			initMenData.saveInfo(id, content); 
	        			
	        			String popedomUserIds = "";   //通知对象
	    				List listEmp = (List) new PublicMgt().sel_allEmployee().getRetVal();
	    				for (int i = 0; i < listEmp.size(); i++) { // 循环发送
	    					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
	    				}
	    				String title = content;
	    				if(title.toLowerCase().indexOf("<a") > -1){
	    					int pos = title.indexOf(">")+1;
	    					title = title.substring(pos,title.indexOf("<",pos));
	    				}
	    				new Thread(new NotifyFashion("1", title,
								content, popedomUserIds, 4, "no",
								id, null, null, "system")).start();
	        		}
	        	}
	    	}
    	}catch(Exception e){
    		BaseEnv.log.error("ConfigRefresh.getLastInfo Error:",e);
    	}
    }

    public void run() {
        while (go) {
            try {
                //当系统正在执行重启过程时，线程只需等待
            	if(SystemState.instance.dogState == SystemState.DOG_RESTART){
            		BaseEnv.log.debug(
                            "==================================== restart ");
            		initMenData.initDog(servletContext);
                    continue ;
            	}
            	
            	//如果当整个系统的初始化不成功时，每10秒重新初始化一次
                if (SystemState.instance.lastErrorCode != SystemState.NORMAL) {
                    System.out.println(
                            "==================================== reconnect db");

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                    }
                    if (!go) {
                        break;
                    }
                    initMenData.initSystem(servletContext);  //重新初始化
                    continue ;
                }
                //加密狗错误，每2秒刷新一次
                if (SystemState.instance.dogState != SystemState.DOG_FORMAL &&
                    SystemState.instance.dogState != SystemState.DOG_EVALUATE &&
                    SystemState.instance.dogState != SystemState.DOG_ERROR_LOCK) {

                	BaseEnv.log.debug(
                            "==================================== refresh dog cur dogState="+SystemState.instance.dogState);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                    if (!go) {
                        break;
                    }
                    if (!initMenData.initDog(servletContext)) {
                        BaseEnv.log.error(
                                "--------Init Dog Failture-----------");
                    }
                    continue;
                }

                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException ex) {
                }
                if( (System.currentTimeMillis() - lastTime ) > 1*24*60*60000 && SystemState.instance.getDogState() == SystemState.DOG_FORMAL){
                	//每天刷新一次
                    ServerConnection connc = new ServerConnection(BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12, 0xF, 0x58,
                        (byte) 0x88, 0x10, 0x40, 0x38
                        , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                        , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});

                    String posStrc = "<operation>checkState</operation>";
                    String retc = "";
                   
                    posStrc += "<id>" + SystemState.instance.dogId + "</id>" +
                        "<regFlag>2</regFlag>";
                    retc = connc.send(posStrc);
                        
                    if (retc != null && "lock".equals(getValue(retc, "result"))) {
                        com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_ERROR_LOCK;
                    }
                    System.out.println("refresh-----------------------");
                	lastTime = System.currentTimeMillis();
                	
                	//---------不要删除以下代码---zxy-----
                    //DOG_CHECK_DEFINE_START
                    //DOG_CHECK_DEFINE_END
                }
                
                /**
                 * 这里每30分钟，取一次推送消息，同时更新一次动态IP
                 */
                if( (System.currentTimeMillis() - lastInfoTime ) > 30*60000 ){
                	//每30分钟刷新一次
                    //读最后一条消息
                	getLastInfo(servletContext.getAttribute("SERVERPORT")+"");
                	
                }
                
                //读取服务端信息 
                
                if (!go) {
                    break;
                }

                /**
                 * 以下代码为配置文件的定时读取更新到内存
                 * 本功能只在开发环境下有用，正式使用环境不需读取此配置文件，因为正式环境下配置文件是绝对稳定的
                 */
                if(SystemState.instance.develope){
                    initMenData.initConfigFile();
                }

                //定时刷新内存表
                //当数据库数据发生变化时，以下代码能自动检查数据库数据是否有更新，如果有更新，则重新读取到内存中
//                ArrayList list = initMenData.getRefreshTime();
//                for (Object o : list) {
//                    Object[] os = (Object[]) o;
//                    if (os[0].equals("Enumeration")) {
//                        long time = (Long) os[1];
//                        if (time > InitMenData.ENUMERATION) {
//                            InitMenData.ENUMERATION = time;
//                            Result rs = initMenData.initEnumerationInformation();
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh EnumerationBean Failture-----------" +
//                                        rs.retCode);
//                            }
//                        }
//                    } else if (os[0].equals("ModuleList")) {
//                        long time = (Long) os[1];
//                        if (time > InitMenData.MODULELIST) {
//                            InitMenData.MODULELIST = time;
//                            Result rs = initMenData.initModuleList();
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh ModuleList Failture-----------" +
//                                        rs.retCode);
//                            }
//                        }
//                    } else if (os[0].equals("tableInfo")) {
//                        long time = (Long) os[1];
//                        if (time > InitMenData.TABLEINFO) {
//                            InitMenData.TABLEINFO = time;
//                            Result rs = initMenData.initDBInformation(
//                                    servletContext,null);
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh DBInformation Failture-----------" +
//                                        rs.retCode);
//                            }                        }
//                    } else if (os[0].equals("propInfo")) {
//                        long time = (Long) os[1];
//                        if (time > InitMenData.PROPLIST) {
//                            InitMenData.PROPLIST = time;
//                            
//                            //修改某些属性要重读表信息
//                            Result rs = initMenData.initDBInformation(
//                                    servletContext,null);
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh DBInformation Failture-----------" +
//                                        rs.retCode);
//                            }       
//                            
//                            rs = initMenData.initPropInformation(
//                                    servletContext);
//                            if (rs != null &&
//                                rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh PropInfo Failture-----------" +
//                                        rs.retCode);
//                            }                        }
//                    } else if (os[0].equals("systemSet")) {
//                        long time = (Long) os[1];
//                        if (time > InitMenData.SYSTEMSET) {
//                            InitMenData.SYSTEMSET = time;
//                            
//                            //修改某些系统配置要重启服务
//                            Result rs = initMenData.initDBInformation(
//                                    servletContext,null);
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh DBInformation Failture-----------" +
//                                        rs.retCode);
//                            }       
//                            
//                            rs = initMenData.
//                                        initSystemSettingInformation();
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh System Setting Failture-----------" +
//                                        rs.retCode);
//                            }
//                            //刷新系统配置的同时，也刷新相应的分支机构和外币专项功能的启用与否
//                            initMenData.initModuleSysType();
//                        }
//                    } else if (os[0].equals("employee")) {
//                        long time = (Long) os[1];
//                        if (time > InitMenData.EMPLOYEE) {
//                            InitMenData.EMPLOYEE = time;
//                            Result rs = UserMgt.initOnlineUser();
//                            if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                                BaseEnv.log.error(
//                                        "--------Refresh employee Failture-----------" +
//                                        rs.retCode);
//                            }
//                        }
//                    }
//                }
            } catch (Exception ex1) {
                BaseEnv.log.error("---ConfigRefresh run() error -" , ex1);
            }
        }
    }
    
    public static String getValue(String xml, String name) {
        try {
            return xml.substring(xml.indexOf("<" + name + ">") +
                                 ("<" + name + ">").length(),
                                 xml.indexOf("</" + name + ">"));
        } catch (Exception ex) {
            return null;
        }
    }
}
