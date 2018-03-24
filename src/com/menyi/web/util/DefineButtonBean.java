package com.menyi.web.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.dbfactory.Result;
import org.w3c.dom.*;
import com.menyi.web.util.ErrorCanst;
import java.sql.*;
import java.util.*;
import com.menyi.aio.bean.SystemSettingBean;
import java.util.Date;
import javax.xml.parsers.DocumentBuilderFactory;

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
public class DefineButtonBean {
	private static HashMap<String,DefineButtonBean> buttonMap = new HashMap<String,DefineButtonBean>();
	
    private String name;
    private String content;
    private String type;
    private String buttonName;
    private String defineName;
    private String isCheck;
    
   

	public String getIsCheck() {
		return isCheck;
	}


	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}


	public String getDefineName() {
		return defineName;
	}


	public void setDefineName(String defineName) {
		this.defineName = defineName;
	}
	
	
    public static boolean parseConfig() {
        boolean result = true;
        HashMap map =new HashMap();

        for (Object o : BaseEnv.buttonFiles) {
            boolean r = parse(map, o.toString());
            if (!r)
                result = false;
        }
        buttonMap = map;
        return result;
    }
    private static boolean parse(HashMap map,String fileName){
    	try{
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        Document document = dbf.newDocumentBuilder().parse(fileName);
	        Node config = document.getFirstChild();
	        NodeList nodeList = config.getChildNodes();	
	        for(int i=0;i<nodeList.getLength();i++){
	            Node node=nodeList.item(i);
	            if(node.getNodeName().equalsIgnoreCase("button")){
	                NamedNodeMap nnm = node.getAttributes(); 
	                DefineButtonBean bean=new DefineButtonBean();
                	bean.setContent(node.getTextContent());   
                	if(nnm.getNamedItem("name")==null){ 
                		continue;
                	}
                	bean.setName(nnm.getNamedItem("name").getNodeValue());                	
                	if(nnm.getNamedItem("buttonName")!=null){
                		bean.setButtonName(nnm.getNamedItem("buttonName").getNodeValue());
                	}                	
                	if(nnm.getNamedItem("type")!=null){
                		bean.setType(nnm.getNamedItem("type").getNodeValue());
                	}
                	if(nnm.getNamedItem("defineName")!=null){
                		bean.setDefineName(nnm.getNamedItem("defineName").getNodeValue());
                	}
                	if(nnm.getNamedItem("isCheck")!=null){
                		bean.setIsCheck(nnm.getNamedItem("isCheck").getNodeValue());
                	}
                	map.put(nnm.getNamedItem("name").getNodeValue(), bean);
	            }
	
	        }
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	
    	return true;
    }


	/**
     * 通过xml分析出一个具体的自定义实体
     * 此方法当在SqlConfig 中调用，生成实体后通过HashMap保存在内存中
     * @param node Node XML结节
     * @return DefineSQLBean
     */
    public static DefineButtonBean parse(String name)throws Exception {
        for(String key:buttonMap.keySet()){            
            if (key.equalsIgnoreCase(name)) {
            	return buttonMap.get(key);
            }
        }
        return null;
    }


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getButtonName() {
		return buttonName;
	}


	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}


}
