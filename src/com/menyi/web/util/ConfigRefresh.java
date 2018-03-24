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
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class ConfigRefresh extends Thread {
    private static boolean go = true;
    private static InitMenData initMenData = new InitMenData();
    private ServletContext servletContext;
    private static long lastTime = 0;
    
    private static long lastInfoTime = 0; //����ȡ��Ϣ��ʱ�䣬������30����һ��

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
	        			
	        			String popedomUserIds = "";   //֪ͨ����
	    				List listEmp = (List) new PublicMgt().sel_allEmployee().getRetVal();
	    				for (int i = 0; i < listEmp.size(); i++) { // ѭ������
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
                //��ϵͳ����ִ����������ʱ���߳�ֻ��ȴ�
            	if(SystemState.instance.dogState == SystemState.DOG_RESTART){
            		BaseEnv.log.debug(
                            "==================================== restart ");
            		initMenData.initDog(servletContext);
                    continue ;
            	}
            	
            	//���������ϵͳ�ĳ�ʼ�����ɹ�ʱ��ÿ10�����³�ʼ��һ��
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
                    initMenData.initSystem(servletContext);  //���³�ʼ��
                    continue ;
                }
                //���ܹ�����ÿ2��ˢ��һ��
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
                	//ÿ��ˢ��һ��
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
                	
                	//---------��Ҫɾ�����´���---zxy-----
                    //DOG_CHECK_DEFINE_START
                    //DOG_CHECK_DEFINE_END
                }
                
                /**
                 * ����ÿ30���ӣ�ȡһ��������Ϣ��ͬʱ����һ�ζ�̬IP
                 */
                if( (System.currentTimeMillis() - lastInfoTime ) > 30*60000 ){
                	//ÿ30����ˢ��һ��
                    //�����һ����Ϣ
                	getLastInfo(servletContext.getAttribute("SERVERPORT")+"");
                	
                }
                
                //��ȡ�������Ϣ 
                
                if (!go) {
                    break;
                }

                /**
                 * ���´���Ϊ�����ļ��Ķ�ʱ��ȡ���µ��ڴ�
                 * ������ֻ�ڿ������������ã���ʽʹ�û��������ȡ�������ļ�����Ϊ��ʽ�����������ļ��Ǿ����ȶ���
                 */
                if(SystemState.instance.develope){
                    initMenData.initConfigFile();
                }

                //��ʱˢ���ڴ��
                //�����ݿ����ݷ����仯ʱ�����´������Զ�������ݿ������Ƿ��и��£�����и��£������¶�ȡ���ڴ���
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
//                            //�޸�ĳЩ����Ҫ�ض�����Ϣ
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
//                            //�޸�ĳЩϵͳ����Ҫ��������
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
//                            //ˢ��ϵͳ���õ�ͬʱ��Ҳˢ����Ӧ�ķ�֧���������ר��ܵ��������
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
