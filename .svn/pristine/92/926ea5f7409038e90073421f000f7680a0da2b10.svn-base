package com.menyi.web.util;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import java.util.HashMap;
import org.xml.sax.*;
import java.util.Set;
import java.util.Iterator;


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
public class SqlConfig {
    public static boolean parseConfig() {
        HashMap sqlMap = new HashMap();
        HashMap pathMap = new HashMap();
        boolean result = true;
        for (Object o : BaseEnv.defineSqlFiles) {
            boolean r = parse(sqlMap,pathMap, o.toString());
            if (!r)
                result = false;
        }
        BaseEnv.defineSqlMap = sqlMap;

        return result;
    }

    public static boolean parse(HashMap sqlMap,HashMap pathMap, String fileName) {
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
                    DefineSQLBean.parse(tempNode, sqlMap,pathMap,fileName);
                }
            }
           // BaseEnv.log.debug("-----------SqlConfig parse file " + fileName + " Successful----------------");
            return true;
        } catch (Exception ex) {
            BaseEnv.log.error("-----------SqlConfig parse file " + fileName + " error----------------", ex);
            return false;
        }
    }
}
