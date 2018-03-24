package com.menyi.web.util;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import java.util.HashMap;
import org.xml.sax.*;


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
public class AlertConfig {
    public static boolean parseConfig() {
        HashMap alertMap = new HashMap();
        boolean result = true;
        for(String defineAlertFileName :BaseEnv.defineAlertFileNames){
        	boolean r = parse(alertMap, defineAlertFileName);
        }

        BaseEnv.defineAlertMap = alertMap;
        return result;
    }

    private static boolean parse(HashMap alertMap, String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document document = null;
            Node config = null;
            NodeList nodeList = null;

            document = dbf.newDocumentBuilder().parse(fileName);
            config = document.getFirstChild();
            nodeList = config.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tempNode = nodeList.item(i);
                if (tempNode.getNodeName().equalsIgnoreCase("define")) {
                    DefineAlertBean.parse(tempNode,alertMap);
                }
            }

           // BaseEnv.log.debug("-----------AlertConfig parse file " + fileName + " Successful----------------");
            return true;
        } catch (Exception ex) {
            BaseEnv.log.error("-----------AlertConfig parse file " + fileName + " error----------------", ex);
            return false;
        }
    }
}
