package com.menyi.sms.setting;

import com.menyi.web.util.BaseEnv;
import java.util.Hashtable;
import java.util.Iterator;
import com.dbfactory.Result;
import com.menyi.web.util.ErrorCanst;
import java.util.List;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.TimingMsgBean;
import java.util.ArrayList;
import com.menyi.web.util.DefineAlertBean;
import org.w3c.dom.Node;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Date;
import java.sql.Connection;
import org.w3c.dom.NodeList;
import java.util.Collection;
import java.sql.Statement;
import com.menyi.aio.bean.SystemSettingBean;
import java.sql.ResultSet;
import com.menyi.web.util.IDGenerater;
import org.w3c.dom.NamedNodeMap;
import java.sql.PreparedStatement;

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
public class TimingNoteBean{
    private String marker;
    private String condition;
    private String template;
    private String name;
    private ArrayList sysList = new ArrayList();
    private ArrayList sessList = new ArrayList();
    /**
   * 通过xml分析出一个具体的自定义实体
   * 此方法当在SqlConfig 中调用，生成实体后通过HashMap保存在内存中
   * @param node Node XML结节
   * @return DefineSQLBean
   */
  public static TimingNoteBean parse(Node node,HashMap alertMap) {
      TimingNoteBean bean = new TimingNoteBean();
      NamedNodeMap nnm = node.getAttributes();
      Node nodeName = nnm.getNamedItem("name");
      bean.name=nodeName.getNodeValue();

      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++) {
          Node temp = list.item(i);

          if(temp.getNodeName().equalsIgnoreCase("sql"))
          {
              Node typeNode = temp.getAttributes().getNamedItem("type");

              String type=typeNode.getNodeValue();
              if (type.equalsIgnoreCase("condition")) {
                 bean.marker=temp.getAttributes().getNamedItem("marker").getNodeValue();
                 bean.condition=temp.getTextContent();
                 bean.parseSentenceGetParam(bean.condition);
              }else if(type.equalsIgnoreCase("template"))
              {
                 bean.template=temp.getTextContent();
                 bean.parseSentenceGetParam(bean.template);
              }
          }
      }
      alertMap.put(bean.name,bean);
      return bean;
  }

  /**
   * 分析传入的语句是否包含系统变量，查询结果变量 若有保存到sysList,returnList中
   * @param sentence String
   * @return Result
   */
  private static String mem="@MEM:";
  private static String SqlReturn="@SqlReturn:";
  private static String Sess="@Sess:";
  private static String SCID="@SCID:";
  private ArrayList parseSentenceGetParam(String sentence)
  {
      boolean flag=true;
      String temp=sentence;
      ArrayList senParam=new ArrayList();
      //如果采用分支机构
      int index_scid=temp.indexOf(SCID);
      if(index_scid>0)
      {
          senParam.add("SCompanyID");
      }
      while(flag)
      {
         int index_sys=temp.indexOf(mem);
         int index_query=temp.indexOf(SqlReturn);
         int index_sess=temp.indexOf(Sess);

         int index_start=-1;

         int index=-1;
         String type="";
         if((index_sys>=0)&&(index_sys<index_query||index_query<0)&&
            (index_sys<index_sess||index_sess<0))
        {
           index = index_sys;
           type="sys";
           index_start=index+mem.length();
        }
        else if((index_query>=0)&&(index_query<index_sys||index_sys<0)&&
            (index_query<index_sess||index_sess<0))
        {
            index=index_query;
            type="query";
            index_start=index+SqlReturn.length();
        }else if((index_sess>=0)&&(index_sess<index_sys||index_sys<0)&&
            (index_sess<index_query||index_query<0))
        {
            index=index_sess;
            type="sess";
            index_start=index+Sess.length();
        }
         if(index>=0)
        {
           temp=temp.substring(index_start);
           int index_end=-1;
           String indexStr="@a,a)a a>a<a=a!a+a-a*a/a&a|";//用a连接字符变量后面可能出现的标识
           String []indexList=indexStr.split("a");
           int []indexs=new int[indexList.length];
           for(int i=0;i<indexList.length;i++)
           {
               indexs[i]=temp.indexOf(indexList[i]);
           }

           for(int i=0;i<indexs.length-1;i++)
           {
               for(int j=i+1;j<indexs.length;j++)
               {
                   if(indexs[i]>indexs[j])
                   {
                       int tempIndex=indexs[i];
                       indexs[i]=indexs[j];
                       indexs[j]=tempIndex;
                   }
               }
           }
           for(int i=0;i<indexs.length;i++)
           {

               if(indexs[i]>0)
               {
                   index_end=indexs[i];
                   break;
               }
           }

           String param = "";

           if(index_end>0)
           {
               param = temp.substring(0, index_end).trim();
               temp = temp.substring(index_end + 1);
           }
           else
           {
               param=temp.trim();
               temp="";
           }
           if (param.length() > 0) {
               flag = true;
               if(type.equals("sys"))
               {
                   sysList.add(param);
               }else if(type.equals("sess")){
                   sessList.add(param);
               }
               senParam.add(param);
           } else {
               flag = false;
           }
        }else
        {
           flag=false;
        }
     }
     return senParam;
  }

 /**
  *
  * @param sysParams ArrayList 解析XML的时候分析的系统变量
  * @return HashMap 键为系统变量，值为变量值
  * @throws Exception
  */
 private HashMap getSystemParam(ArrayList sysList)throws Exception
  {
      HashMap sysParamMap=new HashMap();
      try {
          for (int i = 0; i < sysList.size(); i++) {
              String param = sysList.get(i).toString();
              Hashtable<String,SystemSettingBean> systemSet=BaseEnv.systemSet;
              String value=null;
              SystemSettingBean setbean=systemSet.get(param);
              if(setbean!=null)
                value =setbean.getDefaultValue();
              sysParamMap.put(param,value);
          }
      }catch (Exception ex) {
         throw ex;
      }

      return sysParamMap;
  }

  //从登录时取的sess变量，放在BaseEnv中
  private HashMap getSessParam(String userId, ArrayList sessParams) throws Exception {
      HashMap sessParamMap = new HashMap();
      try {
          Hashtable sessionSet = BaseEnv.sessionSet;
          Hashtable session = (Hashtable) sessionSet.get(userId);
          for (int i = 0; i < sessParams.size(); i++) {
              String param = sessParams.get(i).toString();
              Object sess = session.get(param);
              if (sess != null)
                  sessParamMap.put(param, sess);
          }
      } catch (Exception ex) {
          throw ex;
      }
      return sessParamMap;
  }


  /**
   *
   * @param conn Connection
   * @param id String 定时模板表ID
   * @param loginId String  登陆用户ID
   * @return Result
   */
  public Result execute(Connection conn,String curSCompanyId)
  {
      Result rs=new Result();
     // String SCompanyID = (String)((Hashtable)BaseEnv.sessionSet.get(loginId)).get("SCompanyID");
      try {
          HashMap sysMap = getSystemParam(sysList);
          HashMap sessMap =new HashMap();
          HashMap queryMap=null;
          List values=new ArrayList();
          sessMap.put("SCompanyID",curSCompanyId);
          ArrayList isAvailList= getConditionResult(conn,sysMap,sessMap,curSCompanyId);
          //符合条件查询的结果集
          for(int i=0;i<isAvailList.size();i++)
          {
              queryMap=(HashMap)isAvailList.get(i);
             String value= setNoteData(sysMap,queryMap,sessMap);
             values.add(value);
          }
       rs.setRetVal(values);

      } catch (Exception ex) {
          BaseEnv.log.error("TimingNoteBean execute() ", ex);
          rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
          rs.setRetVal(ex.getMessage());
      }

      return rs;
  }
  /**
   * 根据传入的SQL查询结果如果有结果返回第一条记录，如果没有结果则此以别名命名保存在queryParamMap中的为“null”字符
   * @param conn Connection
   * @param condition String  条件语句
   * @param tableParamMap HashMap define中引用表列名、列值
   * @param sysMap HashMap 系统参数名称，值
   * @param sessMap HashMap sess值
   * @param queryMap HashMap 条件语句查询的结果列名，列值
   * @return Result
   */
  private ArrayList getConditionResult(Connection conn,HashMap sysMap,HashMap sessMap,String scompanyId)throws Exception
  {
      ArrayList isAvailList=new ArrayList();
      try
      {
          ArrayList senParam=parseSentenceGetParam(this.condition);//顺序存放要执行的语句中的参数
          String tempSentence="";
          String tempCondition=this.condition;
          for(int i=0;i<senParam.size();i++)
          {
            String param=senParam.get(i).toString();
            int paramIndex=tempCondition.indexOf(param);
            if(tempCondition.indexOf(Sess+param) > 0){
                paramIndex = tempCondition.indexOf(Sess+param)+Sess.length();
            }
            if(tempCondition.indexOf(mem+param) > 0){
                paramIndex = tempCondition.indexOf(mem+param)+mem.length();
            }
            if(tempCondition.indexOf(SqlReturn+param) > 0){
                              paramIndex = tempCondition.indexOf(SqlReturn+param)+SqlReturn.length();
            }
            if(tempCondition.indexOf(SCID)>0){
                 paramIndex = tempCondition.indexOf(SCID+param)+SCID.length();
            }
             tempSentence=tempSentence+tempCondition.substring(0,paramIndex+param.length());
             tempSentence=tempSentence.replace(Sess+param,Sess+"?");
             tempSentence=tempSentence.replace(mem+param,mem+"?");
             tempSentence=tempSentence.replace(SqlReturn+param,SqlReturn+"?");
             tempSentence=tempSentence.replace(SCID+param,SCID+"'"+scompanyId+"'");
             tempCondition=tempCondition.substring(paramIndex+param.length());
          }
          tempCondition=tempSentence+tempCondition;
          tempCondition=tempCondition.replaceAll(mem,"").replaceAll(SqlReturn,"").replace(Sess,"").replace(SCID,"");
          System.out.println("-----------------------------自定义执行的语句显示： "+tempCondition);
          System.out.println("-----------------------------上述自定义执行的语句参数显示： ");

          PreparedStatement st = conn.prepareStatement(tempCondition);
          for(int i=1;i<=senParam.size();i++)
          {
            String param=senParam.get(i-1).toString();
                //得到参数的值
                String value = "";
                Object temp = sysMap.get(param);
                if (temp != null)
                    value = temp.toString(); //为系统内存值
                else {
                    temp = sessMap.get(param);
                    if (temp != null)
                        value = temp.toString(); //为sess的值
                }
                System.out.print(i + " 值：" + value);
                st.setString(i, value);
         }

         ResultSet rs =st.executeQuery();
         HashMap queryMap=null;
         java.sql.ResultSetMetaData rsm=rs.getMetaData();
         int colCount=rsm.getColumnCount();
         while(rs.next())
          {
              queryMap=new HashMap();
              for(int i=1;i<=colCount;i++)
              {
                  String name=rsm.getColumnName(i);
                  queryMap.put(name,rs.getString(i));
              }
              isAvailList.add(queryMap);
          }

      }catch(Exception ex)
      {
         throw ex;
      }
      return isAvailList;
  }
  /**
   *
   * @param conn Connection
   * @param alertId String 定时模板表ID
   * @param loginId String 登陆ID
   * @param sysMap HashMap 系统参数
   * @param queryMap HashMap 查询结果参数
   * @param sessMap HashMap sess参数
   * @return Result
   * @throws Exception
   */
  private String setNoteData(HashMap sysMap,HashMap queryMap,HashMap sessMap)throws Exception
  {
      Result rs=new Result();
      String sentence=this.template;
      ArrayList senParam=parseSentenceGetParam(sentence);//顺序存放要执行的语句中的参数
      //String SCompanyID = (String)((Hashtable)BaseEnv.sessionSet.get(loginId)).get("SCompanyID");
      String tempSentence="";
      for(int i=0;i<senParam.size();i++)
      {
          String param=senParam.get(i).toString();
          //得到参数的值
            String value="";
            Object temp=sysMap.get(param);
            if(temp!=null)
            {
                value = temp.toString().trim(); //为系统内存值
            }
            else
            {
                temp = queryMap.get(param);
                if(temp!=null)
                {
                    value=temp.toString().trim();//为查询结果值
                }else{
                    temp = sessMap.get(param);
                    if(temp!=null){
                        value=temp.toString().trim();//为sess的值
                    }
                }
            }

          int paramIndex=sentence.indexOf(param);
          tempSentence=tempSentence+sentence.substring(0,paramIndex+param.length()).replace(param,value);
          sentence=sentence.substring(paramIndex+param.length());
      }
      sentence=tempSentence+sentence;
      sentence=sentence.replaceAll(mem,"").replaceAll(SqlReturn,"");
      System.out.println("--------------------自定义模板解析后的消息结果："+sentence);

      return sentence;
  }

}
