package com.menyi.web.util;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import com.menyi.aio.web.customize.PopupSelectBean;
import java.util.*;


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
public class PopSelectConfig {
	
	/**
	 * 解析弹出框文件。
	 * 解析出来的弹出框放进系统的popupSelectMap参数里
	 * @return　是否成功解析
	 */
    public static boolean parseConfig() {
        boolean result = true;
        HashMap<String,PopupSelectBean> map =new HashMap<String,PopupSelectBean>();
        HashMap pathMap =new HashMap();

        for (Object o : BaseEnv.popSelectFiles) {
            boolean r = parse(map,pathMap, o.toString());
            if (!r)
                result = false;
        }       

        BaseEnv.popupSelectMap = map;
        return result;
    }

    /**
     * 解析配置弹出框自定义文件名
     * @param map 解析出来的弹出框将会被加入此map中。
     * @param fileName　弹出框配置文件名
     * @return　是否成功解析，当出现异常的时候返回false
     */
    private static boolean parse(HashMap<String,PopupSelectBean> map, HashMap pathMap,String fileName) {
        boolean result = true;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document document = dbf.newDocumentBuilder().parse(fileName);
            Node config = document.getDocumentElement();
            NodeList nodeList = config.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tempNode = nodeList.item(i);
                if (tempNode.getNodeName().equalsIgnoreCase("select")) {
                    PopupSelectBean.parse(tempNode, map,pathMap,fileName);
                }
            }
         //   BaseEnv.log.debug("-----------PopSelectConfig parseFile " + fileName + " Successful----------------");
        } catch (Exception ex) {
            BaseEnv.log.error("-----------PopSelectConfig parseFile " + fileName + " error----------------", ex);
            result = false;
        }
        return result;
    }

}
