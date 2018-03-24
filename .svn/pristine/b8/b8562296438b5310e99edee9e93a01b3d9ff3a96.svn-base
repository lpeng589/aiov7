package com.koron.oa.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.koron.oa.bean.ApproveBean;
import com.koron.oa.bean.ConditionBean;
import com.koron.oa.bean.ConditionsBean;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.SystemState;

public class ReadXML {
	
	/**
     * 通过xml分析出一个具体的工作流实体
     * @return 
     */
    public static WorkFlowDesignBean parse(String fileName) {
    	WorkFlowDesignBean bean=new WorkFlowDesignBean();
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
        	File flowFile =null;
        	flowFile = new File(getUserWokeFlowPath(fileName)) ;
        	if(flowFile==null||!flowFile.exists()){
        		flowFile = new File(getWokeFlowPath(fileName)) ;
        	}
//        	if (SystemState.instance.dogState == SystemState.DOG_FORMAL
//    				&& "0".equals(SystemState.instance.dogId)) {
//        		flowFile = new File(getWokeFlowPath(fileName)) ;
//        	} else{
//        		flowFile = new File(getUserWokeFlowPath(fileName)) ;
//        	}
        	
            document = dbf.newDocumentBuilder().parse(flowFile);
        } catch (Exception ex) {
            BaseEnv.log.error(ex) ;
            return null;
        }
        Node config = document.getFirstChild();
        NodeList nodeList = config.getChildNodes();
        
        for (int i = 0; i < nodeList.getLength(); i++) {
        	Node tempNode = nodeList.item(i);
            if (tempNode.getNodeName().equalsIgnoreCase("FlowNodes")) {
            	if(parseNode(tempNode,bean.getFlowNodeMap(),"")==-100){
            		return null;
            	}
            }
        }
        return bean;
    }     
    
    
    /**
     * 通过xml分析出一个具体的工作流实体
     * @return 
     */
    public static WorkFlowDesignBean parse(File flowFile) {
    	WorkFlowDesignBean bean=new WorkFlowDesignBean();
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = dbf.newDocumentBuilder().parse(flowFile);
        } catch (Exception ex) {
            BaseEnv.log.error(ex) ;
            return null;
        }
        Node config = document.getFirstChild();
        NodeList nodeList = config.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
        	Node tempNode = nodeList.item(i);
            if ("FlowNodes".equalsIgnoreCase(tempNode.getNodeName())) {
            	if(parseNode(tempNode,bean.getFlowNodeMap(),"upload")==-100){
            		return null;
            	}
            }
        }
        return bean;
    }
    
    /**
     * 通过xml分析出一个具体的工作流实体
     * @return 
     */
    public static String parseJson(File flowFile) {
    	
    	String json = "";
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = dbf.newDocumentBuilder().parse(flowFile);
        } catch (Exception ex) {
            BaseEnv.log.error(ex) ;
            return null;
        }
        Node config = document.getFirstChild();
        NodeList nodeList = config.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
        	Node tempNode = nodeList.item(i);
            if("FlowJson".equalsIgnoreCase(tempNode.getNodeName())){
            	Node flowNode = tempNode.getChildNodes().item(0);
        		if("json".equalsIgnoreCase(flowNode.getNodeName())){
        			json = flowNode.getTextContent();
        		}
            }
        }
        return json;
    }
    
    static int parseNode(Node node,HashMap<String,FlowNodeBean> flowNodeMap,String type){
    	NodeList list = node.getChildNodes();
    	for(int i=0;i<list.getLength();i++){
    		Node flowNode=(Node)list.item(i);
    		if(flowNode.getNodeName().equalsIgnoreCase("flowNode")){
	    		NamedNodeMap flowAtt = flowNode.getAttributes();
	    		FlowNodeBean fdBean=new FlowNodeBean();
	    		if(flowAtt.getNamedItem("id")==null||(flowAtt.getNamedItem("id")!=null&&flowAtt.getNamedItem("id").getNodeValue().length()==0)){
	    			return -100;
	    		}
	    		flowNodeMap.put(flowAtt.getNamedItem("id").getNodeValue(), fdBean);
	    		//加载文件中flowNode节点的属性值
	    		fdBean.setKeyId(flowAtt.getNamedItem("id").getNodeValue());
	    		fdBean.setAllowBack(flowAtt.getNamedItem("allowBack")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("allowBack").getNodeValue()));
	    		fdBean.setAllowCancel(flowAtt.getNamedItem("allowCancel")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("allowCancel").getNodeValue()));
	    		fdBean.setAllowJump(flowAtt.getNamedItem("allowJump")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("allowJump").getNodeValue()));
	    		fdBean.setAllowStop(flowAtt.getNamedItem("allowStop")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("allowStop").getNodeValue()));
	    		fdBean.setApprovePeople(flowAtt.getNamedItem("approvePeople")==null?"":flowAtt.getNamedItem("approvePeople").getNodeValue());
	    		fdBean.setDisplay(flowAtt.getNamedItem("display")==null?"":flowAtt.getNamedItem("display").getNodeValue());
	    		fdBean.setKeyId(flowAtt.getNamedItem("id").getNodeValue());
	    		fdBean.setNoteRate(flowAtt.getNamedItem("noteRate")==null||(flowAtt.getNamedItem("noteRate")!=null&&flowAtt.getNamedItem("noteRate").getNodeValue().length()==0)?0:Float.parseFloat(flowAtt.getNamedItem("noteRate").getNodeValue()));
	    		fdBean.setNoteRateUnit(flowAtt.getNamedItem("noteRateUnit")==null||(flowAtt.getNamedItem("noteRateUnit")!=null&&flowAtt.getNamedItem("noteRateUnit").getNodeValue().length()==0)?0:Float.parseFloat(flowAtt.getNamedItem("noteRateUnit").getNodeValue()));
	    		fdBean.setNoteTime(flowAtt.getNamedItem("noteTime")==null||(flowAtt.getNamedItem("noteTime")!=null&&flowAtt.getNamedItem("noteTime").getNodeValue().length()==0)?0:Float.parseFloat(flowAtt.getNamedItem("noteTime").getNodeValue()));
	    		fdBean.setNoteTimeUnit(flowAtt.getNamedItem("noteTimeUnit")==null||(flowAtt.getNamedItem("noteTimeUnit")!=null&&flowAtt.getNamedItem("noteTimeUnit").getNodeValue().length()==0)?0:Float.parseFloat(flowAtt.getNamedItem("noteTimeUnit").getNodeValue()));
	    		fdBean.setTimeLimit(flowAtt.getNamedItem("timeLimit")==null||(flowAtt.getNamedItem("timeLimit")!=null&&flowAtt.getNamedItem("timeLimit").getNodeValue().length()==0)?0:Float.parseFloat(flowAtt.getNamedItem("timeLimit").getNodeValue()));
	    		fdBean.setTimeLimitUnit(flowAtt.getNamedItem("timeLimitUnit")==null||(flowAtt.getNamedItem("timeLimitUnit")!=null&&flowAtt.getNamedItem("timeLimitUnit").getNodeValue().length()==0)?0:Float.parseFloat(flowAtt.getNamedItem("timeLimitUnit").getNodeValue()));
	    		fdBean.setTo(flowAtt.getNamedItem("to")==null?"":flowAtt.getNamedItem("to").getNodeValue());
	    		fdBean.setUseAllApprove(flowAtt.getNamedItem("useAllApprove")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("useAllApprove").getNodeValue()));
	    		fdBean.setZAction(flowAtt.getNamedItem("zAction").getNodeValue());
	    		fdBean.setPassExec(flowAtt.getNamedItem("passExec")==null?"":flowAtt.getNamedItem("passExec").getNodeValue());
	    		fdBean.setBackExec(flowAtt.getNamedItem("backExec")==null?"":flowAtt.getNamedItem("backExec").getNodeValue());
	    		fdBean.setStopExec(flowAtt.getNamedItem("stopExec")==null?"":flowAtt.getNamedItem("stopExec").getNodeValue());
	    		fdBean.setFilterSet(flowAtt.getNamedItem("filterSet")==null||(flowAtt.getNamedItem("filterSet")!=null&&flowAtt.getNamedItem("filterSet").getNodeValue().length()==0)?0:Integer.parseInt(flowAtt.getNamedItem("filterSet").getNodeValue()));
	    		fdBean.setAutoSelectPeople(flowAtt.getNamedItem("autoSelectPeople")==null||(flowAtt.getNamedItem("autoSelectPeople")!=null&&flowAtt.getNamedItem("autoSelectPeople").getNodeValue().length()==0)?0:Integer.parseInt(flowAtt.getNamedItem("autoSelectPeople").getNodeValue()));
	    		fdBean.setForwardTime(flowAtt.getNamedItem("forwardTime")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("forwardTime").getNodeValue()));
	    		fdBean.setIdeaRequired(flowAtt.getNamedItem("ideaRequired")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("ideaRequired").getNodeValue())) ;
	    		fdBean.setStandaloneNoteSet(flowAtt.getNamedItem("standaloneNoteSet")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("standaloneNoteSet").getNodeValue()));
	    		fdBean.setNextSMS(flowAtt.getNamedItem("nextSMS")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("nextSMS").getNodeValue()));
	    		fdBean.setNextMobile(flowAtt.getNamedItem("nextMobile")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("nextMobile").getNodeValue()));
	    		fdBean.setNextMail(flowAtt.getNamedItem("nextMail")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("nextMail").getNodeValue()));
	    		fdBean.setStartSMS(flowAtt.getNamedItem("startSMS")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("startSMS").getNodeValue()));
	    		fdBean.setStartMobile(flowAtt.getNamedItem("startMobile")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("startMobile").getNodeValue()));
	    		fdBean.setStartMail(flowAtt.getNamedItem("startMail")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("startMail").getNodeValue()));
	    		fdBean.setAllSMS(flowAtt.getNamedItem("allSMS")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("allSMS").getNodeValue()));
	    		fdBean.setAllMail(flowAtt.getNamedItem("allMail")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("allMail").getNodeValue()));
	    		fdBean.setSetSMS(flowAtt.getNamedItem("setSMS")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("setSMS").getNodeValue()));
	    		fdBean.setSetMobile(flowAtt.getNamedItem("setMobile")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("setMobile").getNodeValue()));
	    		fdBean.setSetMail(flowAtt.getNamedItem("setMail")==null?false:Boolean.parseBoolean(flowAtt.getNamedItem("setMail").getNodeValue()));
	    		fdBean.transMinute();
	    		
	    		/******************************加载文件中flowNode节点的子节点值******************************/
	    		NodeList flowNodeC=flowNode.getChildNodes();
	    		for(int j=0;j<flowNodeC.getLength();j++){
	    			Node childNode=flowNodeC.item(j);
	    			if(childNode.getNodeName().equals("fields")){
	    				ArrayList<FieldBean> fields=new ArrayList<FieldBean>();
	    	    		fdBean.setFields(fields);
	    	    		
	    	    		NodeList fieldsNode=childNode.getChildNodes();
	    	    		for(int k=0;k<fieldsNode.getLength();k++){
	    	    			Node fieldNode=fieldsNode.item(k);
	    	    			if(fieldNode.getNodeName().equals("field")){
	    	    				FieldBean field=new FieldBean();
		    	    			NamedNodeMap fieldAtt = fieldNode.getAttributes();
		    	    			field.setFieldName(fieldAtt.getNamedItem("fieldName").getNodeValue());
		    	    			if(fieldAtt.getNamedItem("readOnly")!=null&&fieldAtt.getNamedItem("readOnly").getNodeValue().equals("true")){
		    	    				field.setInputType((byte)8);
		    	    			}
		    	    			if(fieldAtt.getNamedItem("hidden")!=null&&fieldAtt.getNamedItem("hidden").getNodeValue().equals("true")){
		    	    				field.setInputType((byte)3);
		    	    			}
		    	    			if(fieldAtt.getNamedItem("notnull")!=null&&fieldAtt.getNamedItem("notnull").getNodeValue().equals("true")){
		    	    				field.setNotNull(true);
		    	    			}else{
		    	    				field.setNotNull(false);
		    	    			}
		    	    			fields.add(field);
	    	    			}
	    	    			
	    	    		}
	    			}else if(childNode.getNodeName().equals("hiddenFields")){
	    				ArrayList hiddenFields=new ArrayList();
	    				fdBean.setHiddenFields(hiddenFields);
	    				
	    	    		NodeList fieldsNode=childNode.getChildNodes();
	    	    		for(int k=0;k<fieldsNode.getLength();k++){
	    	    			Node fieldNode=fieldsNode.item(k);
	    	    			if(fieldNode.getNodeName().equals("field")){	    	    				
		    	    			NamedNodeMap fieldAtt = fieldNode.getAttributes();
		    	    			if(fieldAtt.getNamedItem("endPublic").getNodeValue().equals("true")){
		    	    				hiddenFields.add(fieldAtt.getNamedItem("fieldName").getNodeValue());
		    	    			}
	    	    			}
	    	    			
	    	    		}
	    			}else if(childNode.getNodeName().equals("approvers")){
	    				ArrayList<ApproveBean> approvers=new ArrayList<ApproveBean>();
	    	    		fdBean.setApprovers(approvers);
	    	    		
	    	    		NodeList approversNode=childNode.getChildNodes();
	    	    		for(int k=0;k<approversNode.getLength();k++){
	    	    			Node approveNode=approversNode.item(k);
	    	    			if(approveNode.getNodeName().equals("approve")){
	    	    				ApproveBean approve=new ApproveBean();
		    	    			approvers.add(approve);
		    	    			NamedNodeMap fieldAtt = approveNode.getAttributes();
		    	    			approve.setType(fieldAtt.getNamedItem("type").getNodeValue());
		    	    			approve.setTypeName(fieldAtt.getNamedItem("typeName").getNodeValue());
		    	    			approve.setUser(fieldAtt.getNamedItem("user").getNodeValue());
		    	    			approve.setUserName(fieldAtt.getNamedItem("userName").getNodeValue());
	    	    			}
	    	    		}
	    	    		
	    			}else if(childNode.getNodeName().equals("notePeople")){
	    				ArrayList<ApproveBean> notePeoples=new ArrayList<ApproveBean>();
	    	    		fdBean.setNotePeople(notePeoples);
	    	    		
	    	    		NodeList peoplesNode=childNode.getChildNodes();
	    	    		for(int k=0;k<peoplesNode.getLength();k++){
	    	    			Node peopleNode=peoplesNode.item(k);
	    	    			if(peopleNode.getNodeName().equals("people")){
	    	    				ApproveBean notePeopleBean = new ApproveBean();
	    	    				notePeoples.add(notePeopleBean);
		    	    			NamedNodeMap fieldAtt = peopleNode.getAttributes();
		    	    			notePeopleBean.setType(fieldAtt.getNamedItem("type").getNodeValue());
		    	    			notePeopleBean.setTypeName(fieldAtt.getNamedItem("typeName").getNodeValue());
		    	    			notePeopleBean.setUser(fieldAtt.getNamedItem("user").getNodeValue());
		    	    			notePeopleBean.setUserName(fieldAtt.getNamedItem("userName").getNodeValue());
	    	    			}
	    	    		}
	    	    		
	    			}else if(childNode.getNodeName().equals("conditions")){
	    				ConditionsBean conditions=new ConditionsBean();
	    				fdBean.getConditionList().add(conditions);
	    				
	    				NamedNodeMap conditionsAtt=childNode.getAttributes();
	    				conditions.setDisplay(conditionsAtt.getNamedItem("display").getNodeValue());
	    				conditions.setTo(conditionsAtt.getNamedItem("to").getNodeValue());
	    				
	    				NodeList conditionsNode=childNode.getChildNodes();
	    	    		for(int k=0;k<conditionsNode.getLength();k++){
	    	    			Node conditionNode=conditionsNode.item(k);
	    	    			if(conditionNode.getNodeName().equals("condition")){
	    	    				ConditionBean condition=new ConditionBean();
		    	    			conditions.getConditions().add(condition) ;
		    	    			NamedNodeMap fieldAtt = conditionNode.getAttributes();
		                		if(fieldAtt.getNamedItem("andOr")==null||fieldAtt.getNamedItem("andOr").getNodeValue()==null||
		                			(fieldAtt.getNamedItem("andOr").getNodeValue()!=null&&fieldAtt.getNamedItem("andOr").getNodeValue().equals("and"))){
		                			condition.setAndOr("&&");
		                		}else{
		                			condition.setAndOr("||");
		                		}
		                		
		    	    			condition.setDisplay(fieldAtt.getNamedItem("display").getNodeValue());
		    	    			condition.setFieldName(fieldAtt.getNamedItem("fieldName").getNodeValue());
		    	    			if(fieldAtt.getNamedItem("relation").getNodeValue().equals("=")){
		    	    				condition.setRelation("==");
		    	    			}else{
		    	    				condition.setRelation(fieldAtt.getNamedItem("relation").getNodeValue());
		    	    			}
		    	    			condition.setRelationDisplay(fieldAtt.getNamedItem("relationDisplay")==null?"":fieldAtt.getNamedItem("relationDisplay").getNodeValue());
		    	    			condition.setValue(fieldAtt.getNamedItem("value")==null?"":fieldAtt.getNamedItem("value").getNodeValue());
		    	    			condition.setValueDisplay(fieldAtt.getNamedItem("valueDisplay")==null?"":fieldAtt.getNamedItem("valueDisplay").getNodeValue());
	    	    			}
	    	    		}
	    			}
	    		}
	    		if(!"upload".equals(type) 
	    				&& "CHECK".equals(fdBean.getZAction())
	    				&& fdBean.getAutoSelectPeople()==0 
	    				&& (fdBean.getApprovers()==null || (fdBean.getApprovers()!=null && fdBean.getApprovers().size()==0)) ){
	    			return -100;
	    		}
    		}
    	}
    	return 0;
    }

    public static String getWokeFlowPath(String fileName) {
        String path = BaseEnv.FILESERVERPATH;
        if (!path.trim().endsWith("/")) {
            path = path.trim() + "/";
        }
        String dir = path += "wokeflow/";
        File file = new File(dir);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception ex) {
            }
        }
        path = dir + fileName;
        return path;
    }
    
    public static String getUserWokeFlowPath(String fileName) {
        String path = BaseEnv.FILESERVERPATH;
        if (!path.trim().endsWith("/")) {
            path = path.trim() + "/";
        }
        String dir = path += "userWokeflow/";
        File file = new File(dir);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception ex) {
            }
        }
        path = dir + fileName;
        return path;
    }
}

