package com.menyi.web.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.email.EMailMgt;

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
public class DefineAlertBean {
    private String marker;
    private String condition;
    private String template;
    private String name;
    private ArrayList sysList=new ArrayList();
    private ArrayList sessList=new ArrayList();

    /**
     * 通过xml分析出一个具体的自定义实体
     * 此方法当在SqlConfig 中调用，生成实体后通过HashMap保存在内存中
     * @param node Node XML结节
     * @return DefineSQLBean
     */
    public static DefineAlertBean parse(Node node,HashMap alertMap) {
        DefineAlertBean bean = new DefineAlertBean();
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
    private static String UseParam="@UseParam:";
    private ArrayList parseSentenceGetParam(String sentence)
    {
        boolean flag=true;
        String temp=sentence;
        ArrayList senParam=new ArrayList();
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
     * @param id String  预警模板表ID
     * @param loginId String  登陆用户ID
     * @return Result
     */
    public Result execute(Connection conn,String id,String useParam,String mode)
    {
        Result rs=new Result();
        try {
            HashMap sysMap = getSystemParam(sysList);
            HashMap queryMap=null;
            ResultSet rss;
            //查询出此条预警模板的预警数据信息
            String msg="select MarkerValue,id from tblSysAlertData where AlertID=? and status=?";
            PreparedStatement st=conn.prepareStatement(msg);
            st.setString(1,id);
            st.setString(2,"0");
            rss=st.executeQuery();
            HashMap markerMap=new HashMap();
            while(rss.next())
            {
                markerMap.put(rss.getString(1),rss.getString(2));
            }
            
            msg="select MarkerValue,id,MessageInfo from tblSysAlertData where AlertID=? and status=?";
            st=conn.prepareStatement(msg);
            st.setString(1,id);
            st.setString(2,"1");
            rss=st.executeQuery();
            HashMap unMarkerMap=new HashMap();
            while(rss.next())
            {
            	String markVal=rss.getString(1);
            	Object[] strs=new Object[]{rss.getString(2),rss.getString(3)};
            	if(markerMap.get(markVal)==null||(markerMap.get(markVal)!=null&&!markerMap.get(markVal).equals(strs[0]))){
            		unMarkerMap.put(rss.getString(1),strs);
            	}
            }
            
            //查询可查询此预警信息的用户
            ArrayList userList=new ArrayList();
            st=conn.prepareStatement("select AlertUser from tblSysAlertDet where f_ref=?")  ;
            st.setString(1,id);
            rss=st.executeQuery();
            while(rss.next())
            {
                userList.add(rss.getString(1));
            }
            
            //预警配置文件自定义sql返回的结果集 ArrayList 的值为HashMap 键为字段名称，值为字段值
            rs=getConditionResult(conn,sysMap,useParam);
            ArrayList isAvailList=null;
            if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
            	return rs;
            }else{
            	isAvailList=(ArrayList)rs.retVal;
            }

            //符合条件查询的结果集
            for(int i=0;i<isAvailList.size();i++)
            {
                queryMap=(HashMap)isAvailList.get(i);
                setAlertData(conn,id,userList ,markerMap,sysMap,queryMap,unMarkerMap,mode);
            }

            //将不符合要求的数据改为无效
           Collection list=markerMap.values();
           Iterator it= list.iterator();
           String alertIds="";

           while(it.hasNext())
           {
               String tempId=it.next().toString();
               alertIds+=",'"+tempId+"'";
           }
           if(alertIds.length()>0)
           {
               String updateSql="update tblSysAlertData set status=1 where id in (";
               alertIds=alertIds.substring(1);
               updateSql+=alertIds+")";

               Statement sts=conn.createStatement();
               sts.execute(updateSql);
           }

        } catch (Exception ex) {
            BaseEnv.log.error("DefineAlertBean execute() ", ex);
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
    private  Result getConditionResult(Connection conn,HashMap sysMap,String useParam)throws Exception
    {
    	Result rst=new Result();
        ArrayList isAvailList=new ArrayList();
        rst.setRetVal(isAvailList);
        try
        {
            ArrayList senParam=parseSentenceGetParam(this.condition);//顺序存放要执行的语句中的参数
            String tempSentence="";
            int useparam_index=-1;
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
               tempSentence=tempSentence+tempCondition.substring(0,paramIndex+param.length());
               tempSentence=tempSentence.replace(Sess+param,Sess+"?");
               tempSentence=tempSentence.replace(mem+param,mem+"?");
               tempSentence=tempSentence.replace(SqlReturn+param,SqlReturn+"?");
               tempCondition=tempCondition.substring(paramIndex+param.length());
            }
            tempCondition=tempSentence+tempCondition;
            
            tempCondition=tempCondition.replaceAll(mem,"").replaceAll(SqlReturn,"").replace(Sess,"");
            useparam_index=tempCondition.indexOf(UseParam);//用户参数位置
            if(useparam_index!=-1){
            	if(useParam!=null&&useParam.length()>0){
            		tempCondition=tempCondition.replace(UseParam, useParam);
            	}else{
            		return rst;
            	}
            }
            PreparedStatement st = conn.prepareStatement(tempCondition);
            for(int i=1;i<=senParam.size();i++)
            {
              String param=senParam.get(i-1).toString();

              //得到参数的值
              String value="";
              Object temp=sysMap.get(param);
              if(temp!=null)
                 value=temp.toString();//为系统内存值
              st.setString(i,value);
           }
            ResultSet rs=null;
           try{
        	   rs =st.executeQuery();
           }catch(Exception e){
        	   BaseEnv.log.error("DefineAlertBean.getConditionResult Error sql :"+tempCondition);
        	   throw e;
           }
           HashMap queryMap=null;
           java.sql.ResultSetMetaData rsm=rs.getMetaData();
           int colCount=rsm.getColumnCount();
           String markerVal="";
           ArrayList markList=new ArrayList();
           while(rs.next())
            {
                queryMap=new HashMap();
                boolean flag=true;
                for(int i=1;i<=colCount;i++)
                {
                    String name=rsm.getColumnName(i);                     
                    queryMap.put(name,rs.getString(i));
                    if(name.equals(marker)&&queryMap.get(marker)!=null){
                    	markerVal=queryMap.get(marker).toString();
                    	if(markList.contains(markerVal)){
                    		flag=false;
                    		break;
                    	}else{
                    		markList.add(markerVal);
                    	}
                    }
                }
                if(flag){
                	isAvailList.add(queryMap);
                }
            }

        }catch(Exception ex){
        	rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            throw ex;
        }
        return rst;
    }
    /**
     *
     * @param conn Connection
     * @param alertId String 预警模板表ID
     * @param loginId String 登陆ID
     * @param sysMap HashMap 系统参数
     * @param queryMap HashMap 查询结果参数
     * @param sessMap HashMap sess参数
     * @return Result
     * @throws Exception
     */
    private Result setAlertData(Connection conn,String alertId,ArrayList userList,
                                HashMap markerMap,HashMap sysMap,HashMap queryMap,HashMap unMarkerMap,String mode)throws Exception
    {
        Result rs=new Result();
        String sentence=this.template;
        ArrayList senParam=parseSentenceGetParam(sentence);//顺序存放要执行的语句中的参数

        String tempSentence="";
        //将查询的结果应用到配置文件<sql type="template"></sql>中
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
                  }
              }

            int paramIndex=sentence.indexOf(param);
            tempSentence=tempSentence+sentence.substring(0,paramIndex+param.length()).replace(param,value);
            sentence=sentence.substring(paramIndex+param.length());
        }
        sentence=tempSentence+sentence;
        sentence=sentence.replaceAll(mem,"").replaceAll(SqlReturn,"");
        
       // BaseEnv.log.debug("--------------------自定义模板解析后的消息结果："+sentence);
        try {
        	if(queryMap.get(marker)!=null){
            String markerValue=queryMap.get(marker).toString();
            Object alertDataId=markerMap.get(markerValue);
            PreparedStatement st=null;
            String sql=null;
            String mainId="";
            if(alertDataId!=null&&alertDataId.toString().length()>0)//此条信息在预警中心已经存在，修改消息内容
            {
                mainId=alertDataId.toString();
                sql="update tblSysAlertData set MessageInfo=? where id=?";
                st = conn.prepareStatement(sql);
                st.setString(1,sentence);
                st.setString(2,alertDataId.toString());
                st.execute();
                markerMap.remove(markerValue);
                sql="delete from tblSysAlertDataDet where f_ref=?";
                st=conn.prepareStatement(sql);
                st.setString(1,mainId);
                st.execute();

            }else{
            	Object[] unAlertData=(Object[])unMarkerMap.get(markerValue);
            	if(unAlertData==null||(unAlertData[0]!=null&&unAlertData[0].toString().length()>0&&!unAlertData[1].equals(sentence))){
	            	  if(sentence!=null&&sentence.replaceAll(" ","").length()>0){
		                //此条信息不存在，或为无效新增记录并且内容不相同时
		                sql = "insert into tblSysAlertData (id,MessageInfo,AlertID,Marker,MarkerValue,createBy,lastUpdateBy,createTime,lastUpdateTime,SCompanyID) values (?,?,?,?,?,?,?,?,?,?)";
		
		                //向预警数据表主表中新增数据
		                st = conn.prepareStatement(sql);
		                mainId= IDGenerater.getId();
		                st.setString(1, mainId);
		                st.setString(2, sentence);
		                st.setString(3, alertId);
		                st.setString(4, marker);
		                st.setString(5, markerValue);
		                st.setString(6, queryMap.get("CreateBy").toString());
		                st.setString(7, queryMap.get("CreateBy").toString());
		                st.setString(8, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		                st.setString(9, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		                String scompanyId = "" ;
		                if(queryMap.get("SCompanyID")==null || queryMap.get("SCompanyID").toString().length()==0){
		                	scompanyId = "00001";
		                }else{
		                	scompanyId = queryMap.get("SCompanyID").toString()  ;
		                }
		                st.setString(10,scompanyId);
		                st.execute();
	            	  }
            	}
           }
           String Mobile="";
           String Email="";
           //第一次插入此条预警数据，要查询出用户的手机和email
           if((mode.indexOf("3")>=0||mode.indexOf("4")>=0)&&(alertDataId==null||(alertDataId!=null&&alertDataId.toString().length()==0))) {
        	   String users="";
        	   for(int i=0;i<userList.size();i++){
        		   users+="'"+userList.get(i)+"',";
        	   }
        	   if(users.length()>0){
        		   users=users.substring(0,users.length()-1);
        		   sql="select id,Mobile,Email from tblEmployee where id in ("+users+")";
        		   ResultSet rst= conn.createStatement().executeQuery(sql);
        		   HashMap map=new HashMap();
        		   while(rst.next()){
        			   map.put(rst.getString(1), new String[]{rst.getString(2),rst.getString(3)});
        		   }
        		   String strSen = replaceA(sentence) ;
        		   if(strSen!=null){
        			   sentence = strSen ;
        		   }
        		   for(int i=0;i<userList.size();i++){
        			   String[] str=(String[])map.get(userList.get(i));
        			   if(mode.indexOf("3")>=0&&str[1]!=null&&str[1].length()>0){//发邮件
        				   EMailMgt sendMgt = new EMailMgt();
        				   sendMgt.sendOuterMailInfter("1",str[1], sentence, sentence,conn) ;
        			   }
        			   if(mode.indexOf("4")>=0&&str[0]!=null&&str[0].length()>0){//发短信
        				   BaseEnv.telecomCenter.send(sentence,new String[]{str[0]},userList.get(i).toString(),conn);
        			   }
        		   }
        	   }
           }
            
           //向预警数据表从表中新增数据
           if((!mainId.equals(""))&&mode.indexOf("1")>=0){
              String userId=null;
              String alertDetSql=null;
              for(int i=0;i<userList.size();i++){
                  userId =userList.get(i).toString();
                  alertDetSql =
                       "insert into tblSysAlertDataDet (Id,f_ref,AlertUser,AlertTime,NextAlertTime) values(?,?,?,?,?)";
                  st = conn.prepareStatement(alertDetSql);
                  st.setString(1, IDGenerater.getId());
                  st.setString(2, mainId);
                  st.setString(3, userId);
                  String date=BaseDateFormat.format(new Date(),
                       BaseDateFormat.yyyyMMdd);
                  st.setString(4, date);
                  st.setString(5, date);
                  st.execute();
                  
              }
           }
        	}
        } catch (SQLException ex) {
            throw ex;
        }


        return rs;
    }
    
    public String replaceA(String str){
    	Pattern pattern = Pattern.compile("<a[/\":()\\?@&,'\\u4e00-\\u9fa5.=\\s\\w]+>") ;
		Matcher matcher = pattern.matcher(str) ;
		if(matcher.find()){
			str = str.replace(matcher.group(), "") ;
		}
		return str.replace("</a>", "") ;
    }
}
