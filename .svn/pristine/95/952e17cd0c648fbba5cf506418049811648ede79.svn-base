package com.menyi.web.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.WorkFlowDesignBean;

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
public class ParseReportBean {
    private String reportId;
    private String reportName;
    private ArrayList fieldInfo=new ArrayList();
    private String from;
    private String sql;
    private ReportField classCode;
    private ArrayList disFields=new ArrayList();//用户自定义配置显示字段
    private ArrayList disFields2 = new ArrayList() ;//报表显示字段
    private ArrayList conFields=new ArrayList();//条件字段
    private ArrayList queryFields=new ArrayList();//查询字段
    private boolean haveStat=false;
    private boolean isCross=false;
    private ReportField crossField;
    private ArrayList chartList = new ArrayList() ;
    private ArrayList fusionList = new ArrayList() ;
    
    public static void main(String[] args) throws Exception {		
		ParseReportBean bean=new ParseReportBean();
		//bean.parse("D:\\workspace\\aio\\report_gm\\OAOutLeaveBillSQL2.xml", "zh_CN");
	}


    public static void parse(File file,String locale,WorkFlowDesignBean workFlowBean)throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = dbf.newDocumentBuilder().parse(file);
        TransformerFactory fac=TransformerFactory.newInstance();
        Transformer former=fac.newTransformer();
        former.setOutputProperty("encoding", "utf-8");     
        
        Node config = document.getFirstChild();
        NodeList nodeList = config.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
           Node temp=nodeList.item(i);
           if(temp.getNodeName().equalsIgnoreCase("fieldInfo")){
               NodeList fieldList=temp.getChildNodes();
               for(int j=0;j<fieldList.getLength();j++){
                   Node fieldNode=fieldList.item(j);
                   if(fieldNode.getNodeName().equalsIgnoreCase("field")){//将field节点复制一份，作为审核节点字段
                	   HashMap<String, FlowNodeBean> map=workFlowBean.getFlowNodeMap(); 
                	   Iterator it=map.keySet().iterator();
                	   while(it.hasNext()){
                		   String id=it.next().toString();
                		   FlowNodeBean flowBean=(FlowNodeBean)map.get(id);
                		   if(!flowBean.getZAction().equals("CHECK")||id.equals("0")||id.equals("-1")){
                			   continue;
                		   }
                		   
                		   
                		   //审核人节点
                		   Node newChildCheckPerson=fieldNode.cloneNode(true);
                           NamedNodeMap fieldAtt=newChildCheckPerson.getAttributes();
                           fieldAtt.getNamedItem("fieldname").setNodeValue("''");
                           fieldAtt.getNamedItem("AsfieldName").setNodeValue("checkPerson"+flowBean.getKeyId());
                           fieldAtt.getNamedItem("groupByFlag").setNodeValue("0");
                           fieldAtt.getNamedItem("displayFlag").setNodeValue("1");
                           fieldAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_审核人");
                           fieldAtt.getNamedItem("fieldType").setNodeValue("2");
                           fieldAtt.getNamedItem("inputType").setNodeValue("0");
                           fieldAtt.getNamedItem("width").setNodeValue("0");
                           fieldAtt.getNamedItem("classCode").setNodeValue("1");
                           fieldAtt.getNamedItem("order").setNodeValue("13");
                           fieldAtt.getNamedItem("isStat").setNodeValue("0");
                           fieldAtt.getNamedItem("fixColName").setNodeValue("1");
                           fieldAtt.getNamedItem("isNull").setNodeValue("0");   
                           
                           NodeList list=newChildCheckPerson.getChildNodes();
                           for(int k=0;k<list.getLength();k++){
                               Node tempNode=list.item(k);
                               if(tempNode.getNodeName().equalsIgnoreCase("Displays")){
                                  NodeList disNodes=tempNode.getChildNodes();
                                  boolean flag=false;
                                  for(int l=0;l<disNodes.getLength();l++){
                                      Node disNode=disNodes.item(l);
                                      if(disNode.getNodeName().equalsIgnoreCase("Display")){
                                    	  if(!flag){
    	                                      NamedNodeMap disAtt=disNode.getAttributes();
    	                                      disAtt.getNamedItem("localStr").setNodeValue(locale) ;
    	                                      disAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_审核人") ;
    	                                      flag=true;
                                    	  }else{
                                    		  tempNode.removeChild(disNode);
                                    		  l--;
                                    	  }
                                      }
                                  }
                               }
                           }
                           temp.appendChild(newChildCheckPerson);
                           
                           //审核时间节点
                		   Node newChildCheckTime=fieldNode.cloneNode(true);
                		   fieldAtt=newChildCheckTime.getAttributes();
                           fieldAtt.getNamedItem("fieldname").setNodeValue("''");
                           fieldAtt.getNamedItem("AsfieldName").setNodeValue("checkTime"+flowBean.getKeyId());
                           fieldAtt.getNamedItem("groupByFlag").setNodeValue("0");
                           fieldAtt.getNamedItem("displayFlag").setNodeValue("1");
                           fieldAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_审核时间");
                           fieldAtt.getNamedItem("fieldType").setNodeValue("2");
                           fieldAtt.getNamedItem("inputType").setNodeValue("0");
                           fieldAtt.getNamedItem("width").setNodeValue("0");
                           fieldAtt.getNamedItem("classCode").setNodeValue("1");
                           fieldAtt.getNamedItem("order").setNodeValue("13");
                           fieldAtt.getNamedItem("isStat").setNodeValue("0");
                           fieldAtt.getNamedItem("fixColName").setNodeValue("1");
                           fieldAtt.getNamedItem("isNull").setNodeValue("0");
                           
                           list=newChildCheckTime.getChildNodes();
                           for(int k=0;k<list.getLength();k++){
                               Node tempNode=list.item(k);
                               if(tempNode.getNodeName().equalsIgnoreCase("Displays")){
                                  NodeList disNodes=tempNode.getChildNodes();
                                  boolean flag=false;
                                  for(int l=0;l<disNodes.getLength();l++){
                                      Node disNode=disNodes.item(l);
                                      if(disNode.getNodeName().equalsIgnoreCase("Display")){
                                    	  if(!flag){
    	                                      NamedNodeMap disAtt=disNode.getAttributes();
    	                                      disAtt.getNamedItem("localStr").setNodeValue(locale) ;
    	                                      disAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_审核时间") ;
    	                                      flag=true;
                                    	  }else{
                                    		  tempNode.removeChild(disNode);
                                    		  l--;
                                    	  }
                                      }
                                  }
                               }
                           }
                           temp.appendChild(newChildCheckTime);
                           
                           //会签意见节点
                		   Node newChildDeliverance=fieldNode.cloneNode(true);
                		   fieldAtt=newChildDeliverance.getAttributes();
                           fieldAtt.getNamedItem("fieldname").setNodeValue("''");
                           fieldAtt.getNamedItem("AsfieldName").setNodeValue("deliverance"+flowBean.getKeyId());
                           fieldAtt.getNamedItem("groupByFlag").setNodeValue("0");
                           fieldAtt.getNamedItem("displayFlag").setNodeValue("1");
                           fieldAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_会签意见");
                           fieldAtt.getNamedItem("fieldType").setNodeValue("2");
                           fieldAtt.getNamedItem("inputType").setNodeValue("0");
                           fieldAtt.getNamedItem("width").setNodeValue("0");
                           fieldAtt.getNamedItem("classCode").setNodeValue("1");
                           fieldAtt.getNamedItem("order").setNodeValue("13");
                           fieldAtt.getNamedItem("isStat").setNodeValue("0");
                           fieldAtt.getNamedItem("fixColName").setNodeValue("1");
                           fieldAtt.getNamedItem("isNull").setNodeValue("0");
                           
                           list=newChildDeliverance.getChildNodes();
                           for(int k=0;k<list.getLength();k++){
                               Node tempNode=list.item(k);
                               if(tempNode.getNodeName().equalsIgnoreCase("Displays")){
                                  NodeList disNodes=tempNode.getChildNodes();
                                  boolean flag=false;
                                  for(int l=0;l<disNodes.getLength();l++){
                                      Node disNode=disNodes.item(l);
                                      if(disNode.getNodeName().equalsIgnoreCase("Display")){
                                    	  if(!flag){
    	                                      NamedNodeMap disAtt=disNode.getAttributes();
    	                                      disAtt.getNamedItem("localStr").setNodeValue(locale) ;
    	                                      disAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_会签意见") ;
    	                                      flag=true;
                                    	  }else{
                                    		  tempNode.removeChild(disNode);
                                    		  l--;
                                    	  }
                                      }
                                  }
                               }
                           }
                           temp.appendChild(newChildDeliverance);
                           
                           //签名图片
                		   Node newChildSign=fieldNode.cloneNode(true);
                		   fieldAtt=newChildSign.getAttributes();
                           fieldAtt.getNamedItem("fieldname").setNodeValue("''");
                           fieldAtt.getNamedItem("AsfieldName").setNodeValue("sign"+flowBean.getKeyId());
                           fieldAtt.getNamedItem("groupByFlag").setNodeValue("0");
                           fieldAtt.getNamedItem("displayFlag").setNodeValue("1");
                           fieldAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_签名图片");
                           fieldAtt.getNamedItem("fieldType").setNodeValue("13");
                           fieldAtt.getNamedItem("inputType").setNodeValue("0");
                           fieldAtt.getNamedItem("width").setNodeValue("0");
                           fieldAtt.getNamedItem("classCode").setNodeValue("1");
                           fieldAtt.getNamedItem("order").setNodeValue("13");
                           fieldAtt.getNamedItem("isStat").setNodeValue("0");
                           fieldAtt.getNamedItem("fixColName").setNodeValue("1");
                           fieldAtt.getNamedItem("isNull").setNodeValue("0");
                           
                           list=newChildSign.getChildNodes();
                           for(int k=0;k<list.getLength();k++){
                               Node tempNode=list.item(k);
                               if(tempNode.getNodeName().equalsIgnoreCase("Displays")){
                                  NodeList disNodes=tempNode.getChildNodes();
                                  boolean flag=false;
                                  for(int l=0;l<disNodes.getLength();l++){
                                      Node disNode=disNodes.item(l);
                                      if(disNode.getNodeName().equalsIgnoreCase("Display")){
                                    	  if(!flag){
    	                                      NamedNodeMap disAtt=disNode.getAttributes();
    	                                      disAtt.getNamedItem("localStr").setNodeValue(locale) ;
    	                                      disAtt.getNamedItem("display").setNodeValue(flowBean.getDisplay()+"_签名图片") ;
    	                                      flag=true;
                                    	  }else{
                                    		  tempNode.removeChild(disNode);
                                    		  l--;
                                    	  }
                                      }
                                  }
                               }
                           }
                           temp.appendChild(newChildSign);
                	   }
            	   }
                	   
                       DOMSource source=new DOMSource(document);
                       StreamResult st=new StreamResult(file);
                       former.transform(source, st);
                       return ;
                   }

               }
           }
        return ;
    }
    
    /**
     * 用于得到打印数据时，将审核流节点设置成打印格式需要的结构
     * @param result
     * @param flowNodeBean
     */
    public void setPrintFieldType(StringBuffer result,HashMap<String, FlowNodeBean> flowNodeBeans){
    	Iterator it=flowNodeBeans.keySet().iterator();
    	while(it.hasNext()){
    		String id = String.valueOf(it.next());
    		if("0".equals(id) || "-1".equals(id)){
    			continue;
    		}
    		FlowNodeBean flowBean=flowNodeBeans.get(id);
    		result.append(" checkPerson"+flowBean.getKeyId() +"=\"2\"");
    		result.append(" checkTime"+flowBean.getKeyId()+"=\"2\"");
    		result.append(" deliverance"+flowBean.getKeyId()+"=\"2\"");
    		result.append(" sign"+flowBean.getKeyId()+"=\"13\"");
    	}
    }
    
}
