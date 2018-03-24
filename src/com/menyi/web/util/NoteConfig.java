package com.menyi.web.util;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.HashMap;

import com.menyi.sms.setting.BillNoteEmailBean;
import com.menyi.sms.setting.TimingNoteBean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 付湘鄂</p>
 *
 * @author 付湘鄂
 * @version 1.0
 */
public class NoteConfig {

    public static boolean parseConfig() {
       HashMap noteMap = new HashMap();
       boolean result = true;
       for(String fileName :BaseEnv.defineNoteFileNames){
    	   result = parse(noteMap, fileName);
       }
       return result;
   }

   private static boolean parse(HashMap noteMap, String fileName) {
       try {
            int index=fileName.indexOf("defineBillMsg.xml");
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

                   if(index>0)
                   {
                       BillNoteEmailBean.parse(tempNode,noteMap);//单据模板
                   }else
                   {
                       TimingNoteBean.parse(tempNode, noteMap);//定时模板
                   }
               }
           }
           if(index>0)
           {
               BaseEnv.defineBillMsgMap = noteMap;
           }else
           {
              BaseEnv.defineNoteMap = noteMap;
           }
          // BaseEnv.log.debug("-----------NoteConfig parse file " + fileName + " Successful----------------");
           return true;
       } catch (Exception ex) {
           BaseEnv.log.error("-----------NoteConfig parse file " + fileName + " error----------------", ex);
           return false;
       }
   }

}
