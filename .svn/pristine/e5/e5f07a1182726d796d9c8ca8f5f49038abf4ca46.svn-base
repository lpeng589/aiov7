package com.menyi.web.util;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import java.util.ArrayList;


/**
 *
 * <p>Title: 基本配置类</p>
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
public class BaseConfig {
    public static boolean parseConfig(String cfgUrl) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document document = dbf.newDocumentBuilder().parse(cfgUrl);
            Node config = document.getFirstChild();
            NodeList nodeList = config.getChildNodes();         
            
        	
        	ArrayList defineList = new ArrayList();
        	ArrayList popupList = new ArrayList();
        	ArrayList buttonList = new ArrayList();
        	ArrayList alertList = new ArrayList();
        	ArrayList noteList = new ArrayList();
        	
        	
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tempNode = nodeList.item(i);
                if (tempNode.getNodeName().equalsIgnoreCase("System")) {
                    readSystem(tempNode,alertList,noteList);
                } else if (tempNode.getNodeName().equalsIgnoreCase("PopupSelectFile")) {
                    readPopupSelect(tempNode,popupList);
                } else if (tempNode.getNodeName().equalsIgnoreCase("buttonFile")) {
                    readButtonFile(tempNode,buttonList);
                } else if (tempNode.getNodeName().equalsIgnoreCase("DefineSqlFile")) {
                    readDefineSql(tempNode,defineList);
                }else if (tempNode.getNodeName().equalsIgnoreCase("PopupSelectFile_user")) {
                    readPopupSelectTrade(tempNode,popupList);
                }else if (tempNode.getNodeName().equalsIgnoreCase("DefineSqlFile_user")) {
                    readDefineSqlTrade(tempNode,defineList);
                }
            }
            readFilePath(BaseEnv.DEFINEPATH,defineList,popupList);            
            readFile("../../user_config/",defineList,popupList,buttonList,alertList,noteList);
            readFilePath("../../user_config/",defineList,popupList);            
            BaseEnv.popSelectFiles = popupList;
            BaseEnv.buttonFiles = buttonList;
            BaseEnv.defineSqlFiles = defineList;
            BaseEnv.defineAlertFileNames = alertList;
        	BaseEnv.defineNoteFileNames = noteList;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("-----------BaseConfig.parseConfig() error----------------");
            return false;
        }
    }
    /**
     * 本子函数为读某目录下规定子目录的数据 ，popupSelect，define
     * @param basePath
     * @param defineList
     * @param popupList
     * @param buttonList
     * @param alertList
     * @param noteList
     */
    private static void readFilePath(String basePath,ArrayList defineList,ArrayList popupList){
    	if(basePath==null || basePath.length() ==0 ){
    		return;
    	}
       	File path = new File(basePath);
    	if(path==null){
    		return;
    	}
    	//加载自定义目录
    	path = new File(basePath+"/define");
    	if(path==null){
    		return;
    	}    	
    	File[] fs = path.listFiles();
    	for(int i=0;fs != null &&i<fs.length;i++){
    		if(!fs[i].getName().endsWith(".xml") && !fs[i].isDirectory()){
    			continue;
    		}
    		defineList.add(fs[i].getAbsolutePath());
    	}
    	//加载弹出窗目录
    	path = new File(basePath+"/popupSelect");
    	if(path==null){
    		return;
    	}    	
    	BaseConfig.popupSelect(path,popupList);
    }
    
	private static void popupSelect(File path,ArrayList popupList){
		File[] fs = path.listFiles();
		for(int i=0;fs != null &&i<fs.length;i++){
			if(fs[i].isDirectory()){
		    	BaseConfig.popupSelect(fs[i],popupList);
		    }else{
		    	if(!fs[i].getName().endsWith(".xml")){
		    		continue;
		    	}
		    	popupList.add(fs[i].getAbsolutePath());
		    }
		}
	   
   }
    
    private static void readFile(String basePath,ArrayList defineList,ArrayList popupList,ArrayList buttonList,ArrayList alertList,ArrayList noteList){
    	//加载config目录
    	File path = new File(basePath);
    	if(path==null){
    		return;
    	}
    	//BaseEnv.log.debug("BaseConfig.readFile read Path :"+basePath);
    	File[] fs = path.listFiles();
    	for(int i=0;fs != null &&i<fs.length;i++){
    		if(!fs[i].getName().endsWith(".xml") && !fs[i].isDirectory()){
    			continue;
    		}
    		if(fs[i].isDirectory()){
    			readFile(fs[i].getAbsolutePath(),defineList,popupList,buttonList,alertList,noteList);
    		}else if(fs[i].getName().startsWith("Alert")){
    			alertList.add(fs[i].getAbsolutePath());
//    			BaseEnv.log.debug("BaseConfig.readFile read Alert :"+fs[i].getAbsolutePath());
    		}else if(fs[i].getName().startsWith("TimingMsg")){
    			noteList.add(fs[i].getAbsolutePath());
//    			BaseEnv.log.debug("BaseConfig.readFile read TimingMsg :"+fs[i].getAbsolutePath());
    		}else if(fs[i].getName().startsWith("Button")){
    			buttonList.add(fs[i].getAbsolutePath());
//    			BaseEnv.log.debug("BaseConfig.readFile read Button :"+fs[i].getAbsolutePath());
    		}else if(fs[i].getName().startsWith("popup")){
    			popupList.add(fs[i].getAbsolutePath());
//    			BaseEnv.log.debug("BaseConfig.readFile read popup :"+fs[i].getAbsolutePath());
    		}else if(fs[i].getName().startsWith("define")){
    			defineList.add(fs[i].getAbsolutePath());
//    			BaseEnv.log.debug("BaseConfig.readFile read define :"+fs[i].getAbsolutePath());
    		} 
    	}
    }

    private static void readSystem(Node node,ArrayList alertList,ArrayList noteList) throws Exception {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("FilePath")) {
                String s = temp.getFirstChild().getNodeValue();
                BaseEnv.FILESERVERPATH = s;
            } else if(temp.getNodeName().equalsIgnoreCase("ReportPath")){
            	String s = temp.getFirstChild().getNodeValue() ;
            	BaseEnv.REPORTPATH = s ;
            }else if(temp.getNodeName().equalsIgnoreCase("UserReportPath")){
            	String s = temp.getFirstChild().getNodeValue() ;
            	BaseEnv.USERREPORTPATH = s ;
            }else if (temp.getNodeName().equalsIgnoreCase("ConfigRefresh")) {
//                String s = temp.getFirstChild().getNodeValue();
//                BaseEnv.configRefresh = Integer.parseInt(s);
            }else if (temp.getNodeName().equalsIgnoreCase("StateConfig")) {
            	String s = temp.getFirstChild().getNodeValue();
            	BaseEnv.defineSateConfig = s ;
            }else if (temp.getNodeName().equalsIgnoreCase("DefineAlertFile")) {
               String s = temp.getFirstChild().getNodeValue();
               alertList.add(s);
           }else if(temp.getNodeName().equalsIgnoreCase("DefineTimingFile")){
               String s=temp.getFirstChild().getNodeValue();
               noteList.add(s);
           }else if(temp.getNodeName().equalsIgnoreCase("DefineBillFile")){
        	   //系统未用到
//               String s=temp.getFirstChild().getNodeValue();
//               BaseEnv.defineBillMsgFileName=s;
           }else if(temp.getNodeName().equalsIgnoreCase("VersionNumber")){
        	   //zxy修改，统一版本信息，不再从这里读取，改为数据库tblTradeInfo中信息， BaseEnv.SysVersion
//        	   String s=temp.getFirstChild().getNodeValue();
//        	   BaseEnv.versionNumber=s;
           }else if(temp.getNodeName().equalsIgnoreCase("DefaultStyle")){
        	   String s=temp.getFirstChild().getNodeValue();
        	   BaseEnv.DefaultStyle=s;
           }else if(temp.getNodeName().equalsIgnoreCase("DayStudyNum")){
        	   String s=temp.getFirstChild().getNodeValue();
        	   BaseEnv.dayStudyNum=Integer.parseInt(s);
           }else if(temp.getNodeName().equalsIgnoreCase("DefinePath")){
        	   String s=temp.getFirstChild().getNodeValue();
        	   BaseEnv.DEFINEPATH=s;
           }
        }
    }

    private static void readPopupSelect(Node node,ArrayList relist) throws Exception {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("File")) {
                String s = temp.getFirstChild().getNodeValue();
                relist.add(s);
            }
        }
    }
    private static void readButtonFile(Node node,ArrayList relist) throws Exception {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("File")) {
                String s = temp.getFirstChild().getNodeValue();
                relist.add(s);
            }
        }
    }

    private static void readPopupSelectTrade(Node node,ArrayList relist) throws Exception {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("File")) {
                String s = temp.getFirstChild().getNodeValue();
                relist.add(s);
            }
        }        
    }


    private static void readDefineSql(Node node,ArrayList relist) throws Exception {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("File")) {
                String s = temp.getFirstChild().getNodeValue();
                relist.add(s);
            }
        }
    }

    private static void readDefineSqlTrade(Node node,ArrayList relist) throws Exception {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("File")) {
                String s = temp.getFirstChild().getNodeValue();
                relist.add(s);
            }
        }
    }


}
