package com.menyi.web.util;

import java.text.ParseException;
import java.util.ArrayList;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.sysAcc.SysAccMgt;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;

import org.apache.struts.util.MessageResources;

public class ConfigParse {
    /**
     * @param sentence String
      * @param tableParams ArrayList
      * @param sysParams ArrayList
      * @return Result
      */
     private static String mem="@MEM:";
     private static String ValueofDB="@ValueofDB:";
     private static String SqlReturn="@SqlReturn:";
     private static String Sess="@Sess:";
     private static String Code="@Code:";
     private static String Locale="@Locale:";
     private static String COND="@condition:";
     private static String total="@TotalDB:";

     public static ArrayList parseSentenceGetParam(String sentence ,ArrayList tableParams,
                                                   ArrayList sysParams,ArrayList sessParams,ArrayList codeParams
                                                   ,ArrayList queryParams,ArrayList condParams)
     {
         boolean flag=true;
         String temp=sentence;
         ArrayList senParam=new ArrayList();
         while(flag)
         {
        	 int index_sys=temp.indexOf(mem);
             int index_table=(tableParams==null?-1:temp.indexOf(ValueofDB));
             int index_query=(queryParams==null?-1:temp.indexOf(SqlReturn));
             int index_sess=temp.indexOf(Sess);
             int index_code=temp.indexOf(Code);
             int index_Locale=temp.indexOf(Locale);
             int index_total=(tableParams==null?-1:temp.indexOf(total));
             int index_cond=(condParams==null?-1:temp.indexOf(COND));
             int index_start=-1;            

            int index=-1;
            String type="";
            if((index_sys>=0)&&(index_sys<index_table||index_table<0)&&(index_sys<index_query||index_query<0)
                    &&(index_sys<index_sess||index_sess<0)&&(index_sys<index_code||index_code<0)
                    &&(index_sys<index_Locale||index_Locale<0)&&(index_sys<index_cond||index_cond<0)
                    &&(index_sys<index_total||index_total<0)){
              index = index_sys;
              type="sys";
              index_start=index+mem.length();
           }else if((index_table>=0)&&(index_table<index_sys||index_sys<0)&&(index_table<index_query||index_query<0)
                   &&(index_table<index_sess||index_sess<0)&&(index_table<index_code||index_code<0)
                   &&(index_table<index_Locale||index_Locale<0)&&(index_table<index_cond||index_cond<0)
                   &&(index_table<index_total||index_total<0)){
              index = index_table;
              type="table";
              index_start=index+ValueofDB.length();
           }else if((index_query>=0)&&(index_query<index_sys||index_sys<0)&&(index_query<index_table||index_table<0)
                   &&(index_query<index_sess||index_sess<0)&&(index_query<index_code||index_code<0)
                   &&(index_query<index_Locale||index_Locale<0)&&(index_query<index_cond||index_cond<0)
                   &&(index_query<index_total||index_total<0)){
               index=index_query;
               type="query";
               index_start=index+SqlReturn.length();
           }else if((index_sess>=0)&&(index_sess<index_sys||index_sys<0)&&(index_sess<index_table||index_table<0)
                   &&(index_sess<index_query||index_query<0)&&(index_sess<index_code||index_code<0)
                   &&(index_sess<index_Locale||index_Locale<0)&&(index_sess<index_cond||index_cond<0)
                   &&(index_sess<index_total||index_total<0)){
               index=index_sess;
               type="sess";
               index_start=index+Sess.length();
           }else if((index_code>=0)&&(index_code<index_sys||index_sys<0)&&(index_code<index_table||index_table<0)
                   &&(index_code<index_query||index_query<0)&&(index_code<index_sess||index_sess<0)
                   &&(index_code<index_Locale||index_Locale<0)&&(index_code<index_cond||index_cond<0)
                   &&(index_code<index_total||index_total<0)){
               index=index_code;
               type="code";
               index_start=index+Code.length();
           }else if((index_Locale>=0)&&(index_Locale<index_sys||index_sys<0)&&(index_Locale<index_table||index_table<0)
                   &&(index_Locale<index_query||index_query<0)&&(index_Locale<index_sess||index_sess<0)
                   &&(index_Locale<index_code||index_code<0)&&(index_Locale<index_cond||index_cond<0)
                   &&(index_Locale<index_total||index_total<0)){
        	   index=index_Locale;
               type="locale";
               index_start=index+Locale.length();
           }else if((index_cond>=0)&&(index_cond<index_sys||index_sys<0)&&(index_cond<index_table||index_table<0)
                   &&(index_cond<index_query||index_query<0)&&(index_cond<index_sess||index_sess<0)
                   &&(index_cond<index_code||index_code<0)&&(index_cond<index_Locale||index_Locale<0)
                   &&(index_cond<index_total||index_total<0)){
        	   index=index_cond;
               type="cond";
               index_start=index+COND.length();
           }else if((index_total>=0)&&(index_total<index_sys||index_sys<0)&&(index_total<index_table||index_table<0)
                   &&(index_total<index_query||index_query<0)&&(index_total<index_sess||index_sess<0)
                   &&(index_total<index_code||index_code<0)&&(index_total<index_Locale||index_Locale<0)
                   &&(index_total<index_cond||index_cond<0)){
        	   index=index_total;
               type="total";
               index_start=index+total.length();
           }


            if(index>=0)
           {
              temp=temp.substring(index_start);
              int index_end=-1;
              //String indexStr="@a,a)a a>a<a=a!a+a-a*a/a&a|a%a'a\"a[a]";
              String []indexList={"@",",",")"," ",">","<","=","!","+","-","*","/","&","|","%","'","\\","\"","[","]"};
              int []indexs=new int[indexList.length];
              for(int i=0;i<indexList.length;i++){
                  indexs[i]=temp.indexOf(indexList[i]);
              }

              for(int i=0;i<indexs.length-1;i++){
                  for(int j=i+1;j<indexs.length;j++){
                      if(indexs[i]>indexs[j]){
                          int tempIndex=indexs[i];
                          indexs[i]=indexs[j];
                          indexs[j]=tempIndex;
                      }
                  }
              }
              for(int i=0;i<indexs.length;i++) {
                  if(indexs[i]>0){
                      index_end=indexs[i];
                      break;
                  }
              }

              
              if ("code".equals(type)) {
                  index_end = temp.indexOf("]");
                  temp = temp.substring(1, temp.length());
              }

              String param = "";

              if(index_end>0){
                  param = temp.substring(0, index_end).trim();
                  temp = temp.substring(index_end + 1);
              }
              else{
                  param=temp.trim();
                  temp="";
              }
              if (param.length() > 0) {
                  flag = true;
                  if(type.equals("table")&&tableParams!=null&&!tableParams.contains(param)){
                      tableParams.add(param);
                  }else if(type.equals("sys")&&sysParams!=null&&!sysParams.contains(param)){
                      sysParams.add(param);
                  }else if(type.equals("sess")&&sessParams!=null&&!sessParams.contains(param)){
                      sessParams.add(param);
                  }else if(type.equals("code")&&codeParams!=null&&!codeParams.contains(param)){
                      codeParams.add(param);
                  }else if(type.equals("query")&&queryParams!=null&&!queryParams.contains(param)){
                      queryParams.add(param);
                  }else if(type.equals("cond")&&condParams!=null&&!condParams.contains(param)){
                	  condParams.add(param);
                  }
                  
                  if(type.equals("table"))
                      senParam.add(ValueofDB+param);
                  else if(type.equals("sys"))
                      senParam.add(mem+param);
                  else if(type.equals("query"))
                      senParam.add(SqlReturn+param);
                  else if(type.equals("sess"))
                      senParam.add(Sess+param);
                  else if(type.equals("code"))
                      senParam.add(Code+"["+param+"]");
                  else if(type.equals("locale"))
                      senParam.add(Locale+param);
                  else if(type.equals("cond"))
                      senParam.add(COND+param);
                  else if(type.equals("total"))
                      senParam.add(total+param);
              } else {
                  flag = false;
              }
           }else{
              flag=false;
           }
        }
        
        return senParam;
     }
     /**
      * 解决报表显示字段，得到表名，字段名
      * @param str
      * @return
      */
     public static ArrayList<String[]> getTableFieldByReportField(String str) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]+\\.[a-zA-Z0-9]+");
		Matcher matcher = pattern.matcher(str);
		int findIndex = 0;
		while (matcher.find(findIndex)) {
			int index = str.indexOf(matcher.group(0), findIndex)+ matcher.group(0).length();
			String strMatcher = matcher.group(0) ;
			String[] strTable = strMatcher.split("\\.") ;
			list.add(strTable);
			findIndex = index;
		}
		return list;
	 }
     /**
		 * 
		 * @param str
		 *            String
		 * @return ArrayList
		 */
     public static ArrayList getTableField(String str){
         ArrayList list=new ArrayList();
         String indexStr = "@a,a(a)a a>a<a=a!a+a-a*a/a&a|a'";
         int index=-1;
         while (str.length() > 0) {
             int indexDot = str.indexOf(".");
             if(indexDot<0){
                 break;
             }
             String temp=str.substring(0,indexDot);
             String[] indexList = indexStr.split("a");
             int[] indexs = new int[indexList.length];
             for (int i = 0; i < indexList.length; i++) {
                 indexs[i] = temp.indexOf(indexList[i]);
             }

            
             for (int i = 0; i < indexs.length - 1; i++) {
                 for (int j = i + 1; j < indexs.length; j++) {
                     if (indexs[i] > indexs[j]) {
                         int tempIndex = indexs[i];
                         indexs[i] = indexs[j];
                         indexs[j] = tempIndex;
                     }
                 }
             }

           
             for(int i=indexs.length-1;i>=0;i--)
             {
                 if(indexs[i]>=0)
                 {
                     index=indexs[i];
                     break;
                 }
             }


             String[] obj = new String[2];
             if (index > -1) {
                 obj[0] = str.substring(index + 1, indexDot);
             } else {
                 obj[0] = str.substring(0, indexDot);
             }
             temp = str.substring(indexDot + 1);
             index = -1;
             for (int i = 0; i < indexList.length; i++) {
                 indexs[i] = temp.indexOf(indexList[i]);
             }

           
             for (int i = 0; i < indexs.length - 1; i++) {
                 for (int j = i + 1; j < indexs.length; j++) {
                     if (indexs[i] > indexs[j]) {
                         int tempIndex = indexs[i];
                         indexs[i] = indexs[j];
                         indexs[j] = tempIndex;
                     }
                 }
             }

          
             for (int i = 0; i < indexs.length; i++) {
                 if (indexs[i] > 0) {
                     index = indexs[i];
                     break;
                 }
             }

             if (index > 0) {
                 obj[1] = temp.substring(0, index);
             } else {
                 obj[1] = temp.substring(0);
             }

             list.add(obj);
             str = str.substring(str.indexOf(obj[0] + "." + obj[1]) +
                                 (obj[0] + "." + obj[1]).length());

             }
         return list;
     }
     /**
         *
         * @param sysParams ArrayList  
         * @return HashMap 
         * @throws Exception
         */
        public static HashMap getSystemParam(ArrayList sysParams,String sCompanyId) throws Exception {
            HashMap sysParamMap = new HashMap();
            try {
                for (int i = 0; i < sysParams.size(); i++) { 
                    String param = sysParams.get(i).toString();
                    Hashtable<String, SystemSettingBean>
                            systemSet = BaseEnv.systemSet;
                    String value = null;
                    SystemSettingBean setbean = systemSet.get(param);
                    if (setbean != null){
                        value = setbean.getSetting() == null ? "" :
                                setbean.getSetting();
                        if (value.equals("")) {
                            value = setbean.getDefaultValue();
                        }
                        if(setbean.getSysCode().equals("defaultTime")){
                        	switch (Integer.parseInt(value)){
                        	    //当天
                        		case 11:
                        			value = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
                        			break;
                        		//开帐日期
                        		case 12:
                        			value = BaseEnv.beginDate;
                        			break;
                        		//当月1号
                        		case 13:
                        			value = (BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd).substring(0,7))+"-01";
                        			break;
                        		//当年1月1号
                        		case 14:
                        			value = (BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd).substring(0,4))+"-01-01";
                        			break;
                        		//月结会计期间1号
                        		case 15:
                        			value = getAccMonth(sCompanyId) ;
                        			break;
                        			//30天
                        		case 16:
                        			value = BaseDateFormat.format(new Date(new Date().getTime()-30*24*60*60000l),BaseDateFormat.yyyyMMdd) ;
                        			break;	
                        	}
                        }
                        sysParamMap.put(param, value);
                    }
                }
                sysParamMap.put("sysShortDate",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd));
                sysParamMap.put("sysLongDate", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
            } catch (Exception ex) {
                throw ex;
            }

            return sysParamMap;
        }
        
        /**
         * 月结会计期间1号
         * @return
         */
        public static String getAccMonth(String sCompanyId){
        	HashMap<String, AccPeriodBean> accMap = BaseEnv.accPerios ;
        	HashMap<String, AccPeriodBean> beginMap = BaseEnv.beginPerios ;
        	
        	String strAccPeriod = "" ;
        	AccPeriodBean accPeriod = accMap.get(sCompanyId) ;
        	AccPeriodBean beginPeriod = beginMap.get(sCompanyId) ;
        	if(accPeriod==null){
        		return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd) ;
        	}else if(accPeriod.getAccYear()==-1){
        		return "";
        	}
        
        	
        	try {
        		Date beginDate = BaseDateFormat.parse(beginPeriod.getAccYear()+"-"+beginPeriod.getAccPeriod()+"-01", BaseDateFormat.yyyyMMdd) ;
        		String strAcc = "" ;
        		if(accPeriod.getAccYear()==12){
        			strAcc = (accPeriod.getAccYear()-1)+"-01-01" ;
        		}else{
        			strAcc = accPeriod.getAccYear() + "-" + (accPeriod.getAccPeriod()-1) + "-01" ;
        		}
				Date accDate = BaseDateFormat.parse(strAcc, BaseDateFormat.yyyyMMdd) ;
				if(accDate.getTime()<beginDate.getTime()){
					accDate = BaseDateFormat.parse(accPeriod.getAccYear()+"-"+accPeriod.getAccPeriod()+"-01", BaseDateFormat.yyyyMMdd) ;
				}
				strAccPeriod = BaseDateFormat.format(accDate, BaseDateFormat.yyyyMMdd) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
        	return strAccPeriod ;
        }

        public static String getSystemParam(String param) throws Exception {
            Hashtable<String, SystemSettingBean> systemSet = BaseEnv.systemSet;
            String value = null;
            SystemSettingBean setbean = systemSet.get(param);
            if (setbean != null) {
               value= setbean.getSetting()==null?"":setbean.getSetting();
               if(value.equals("")){
                   value = setbean.getDefaultValue();
               }
            }
            if(param.equals("sysShortDate")){
                value=BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd) ;
            }
            if(param.equals("sysLongDate")){
                value=BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss) ;
            }

            return value;
        }

        
        public static HashMap getSessParam(String userId, ArrayList sessParams) throws
                Exception {
            HashMap sessParamMap = new HashMap();
            try {
                Hashtable sessionSet = BaseEnv.sessionSet;
                Hashtable session = (Hashtable) sessionSet.get(userId);
                for (int i = 0; session!=null && i < sessParams.size(); i++) {
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

       
        public static HashMap getCodeParam(ArrayList codeParams,Connection conn) throws Exception {
            HashMap codeParamMap = new HashMap();
            try {
                for (int i = 0; i < codeParams.size(); i++) {
                    String param = codeParams.get(i).toString();
                    String code = CodeGenerater.generater(param,conn);
                    if (code != null)
                        codeParamMap.put(param, code);
                }
            } catch (Exception ex) {
                throw ex;
            }
            return codeParamMap;
        }


    /**
    *  
    * @param sentence String
    * @param tableParamMap HashMap define 
    * @param sysParamMap HashMap
    * @param queryParamMap HashMap  
    * @return String  
    */
   public static String parseSentencePutParam(String sentence,HashMap tableParamMap,HashMap sysParamMap,
                                              HashMap queryParamMap,HashMap sessParamMap,HashMap codeParamMap,
                                              HashMap condParamMap,MessageResources resources,Locale locale)
   {
       ArrayList tableParams=(tableParamMap==null?null:new ArrayList());
       ArrayList sysParams=new ArrayList();
       ArrayList sessParams=new ArrayList();
       ArrayList codeParams=new ArrayList();
       ArrayList queryParams=(queryParamMap==null?null:new ArrayList());
       ArrayList condParams=new ArrayList();
       ArrayList senParam=parseSentenceGetParam(sentence,tableParams,sysParams,sessParams,codeParams,queryParams,condParams);
       String tempSentence="";
       for(int i=0;i<senParam.size();i++)
       {
           String param=senParam.get(i).toString();
           int paramIndex=sentence.indexOf(param);
           if(paramIndex>-1)
           {
            
             String tempParam="";
             int index_sys=param.indexOf(mem);
             int index_table=param.indexOf(ValueofDB);
             int index_query=param.indexOf(SqlReturn);
             int index_sess=param.indexOf(Sess);
             int index_code=param.indexOf(Code);
             int index_locale=param.indexOf(Locale);
             int index_cond=param.indexOf(COND);
             
             if(index_sys>=0)
                tempParam=param.substring(mem.length());
             else if(index_table>=0)
                tempParam=param.substring(ValueofDB.length());
             else if(index_query>=0)
                tempParam=param.substring(SqlReturn.length());
             else if(index_sess>=0)
                tempParam=param.substring(Sess.length());
             else if(index_locale>=0)
                tempParam=param.substring(Locale.length());
             else if(index_code>=0){
                 tempParam = param.substring(Code.length() + 1);
                 tempParam = tempParam.substring(0,tempParam.length()-1);
             }else if(index_cond>=0)
                 tempParam=param.substring(COND.length());
             
              Object temp=null;
              if(tableParamMap!=null)temp=tableParamMap.get(tempParam);
              String value="";
              if(temp!=null)
                  value=temp.toString();
              else
              {	  if(sysParamMap!=null)temp=sysParamMap.get(tempParam);
                  if(temp!=null)
                    value=temp.toString();
                  else
                  {
                      if(queryParamMap!=null)temp=queryParamMap.get(tempParam);
                      if(temp!=null)
                         value=temp.toString();
                      else{
                          if(sessParamMap!=null)temp = sessParamMap.get(tempParam);
                          if (temp != null)
                              value = temp.toString();
                          else{                        	  
                              if(codeParamMap!=null)temp = codeParamMap.get(tempParam);
                              if(temp != null)
                                  value = temp.toString();
                              else{
                            	  if(resources!=null){
                               	   temp=resources.getMessage(locale,tempParam);
                               	   if(temp!=null)
                               		   value=temp.toString();
	                               	else{
	                              	  if(condParamMap!=null){
	                                 	   temp=condParamMap.get(tempParam);
	                                 	   if(temp!=null)
	                                 		   value=temp.toString();
	                         		     }
	                                }
                       		     }
                              }
                          }
                      }
                  }
              }
              tempSentence+=sentence.substring(0,param.length()+paramIndex).replace(param,value);
              sentence=sentence.substring(param.length()+paramIndex);
           }
       }
       sentence=tempSentence+sentence;
       return sentence;
   }

   /**
    *  
    * @param sentence String
    * @param tableParamMap HashMap define 
    * @param sysParamMap HashMap
    *  * @param queryParamMap HashMap  
    * @return String  
    **/
   //(CompanyCode=@ValueofDB:CompanyCode and OrderStatus = 0 and tblBuyOrder.BillDate &lt;=@ValueofDB:BillDate)

   public static String parseConditionSentencePutParam(String sentence,HashMap tableParamMap,
           HashMap sysParamMap,HashMap queryParamMap,HashMap sessParamMap,HashMap codeParamMap,HashMap condParamMap)
   {
	   String oldSen = sentence;
       ArrayList tableParams=new ArrayList();
       ArrayList sysParams=new ArrayList();
       ArrayList sessParams = new ArrayList();
       ArrayList codeParams = new ArrayList();
       ArrayList queryParams=new ArrayList();
       ArrayList condParams=new ArrayList();
       ArrayList senParam=parseSentenceGetParam(sentence,tableParams,sysParams,sessParams,codeParams,queryParams,condParams);
       String tempSentence="";
       ArrayList params=new ArrayList();
       HashMap paramMap=new HashMap();
       for(int i=0;i<senParam.size();i++)
       {
           String param=senParam.get(i).toString();
           int paramIndex=sentence.indexOf(param);
           if(paramIndex>-1)
           {
               
               String tempParam = "";
               int index_sys = param.indexOf(mem);
               int index_table = param.indexOf(ValueofDB);
               int index_query = param.indexOf(SqlReturn);
               int index_sess = param.indexOf(Sess);
               int index_code = param.indexOf(Code);
               int index_cond=param.indexOf(COND);
               if (index_sys >= 0)
                   tempParam = param.substring(mem.length());
               else if (index_table >= 0)
                   tempParam = param.substring(ValueofDB.length());
               else if (index_query >= 0)
                   tempParam = param.substring(SqlReturn.length());
               else if (index_sess >= 0)
                   tempParam = param.substring(Sess.length());
               else if (index_cond >= 0)
                   tempParam = param.substring(COND.length());
               else if (index_code >= 0) {
                   tempParam = param.substring(Code.length() + 1);
                   tempParam = tempParam.substring(0, tempParam.length() - 1);
               }
               Object temp=null;
               String value="";
               if(index_table>=0&&tableParamMap!=null){
            	   temp=tableParamMap.get(tempParam);
            	   //if(temp != null && temp.toString().indexOf("#;#") > 0){
            		   //部分单据有默认值如@Sess:StockCode;@Sess:StockFullName加分号是新增时显示默认的弹出窗名字,这里要去掉
            		   //temp  = temp.toString().substring(0,temp.toString().indexOf(";"));
            	   //}
               }
               if(index_query>=0&&queryParamMap!=null)
            	   temp=queryParamMap.get(tempParam);
               if(index_sess>=0&&sessParamMap!=null)
            	   temp = sessParamMap.get(tempParam);
               if(index_sys>=0&&sysParamMap!=null)
            	   temp=sysParamMap.get(tempParam);
               if(index_code>=0&&codeParamMap!=null)
            	   temp = codeParamMap.get(tempParam);
               if(index_cond>=0&&condParamMap!=null)
            	   temp = condParamMap.get(tempParam);
               
               if(temp!=null){
            	   value=temp.toString();
               }
         	   boolean flag=false;
         	   
         	   params.add(tempParam);
         	   paramMap.put(tempParam, value);
               if(value.length()==0){    
            	   //当值为空时，要去掉这一个条件，原来下面的算法有问题，改为正则表达式算法
            	  String d=sentence.substring(0,param.length()+paramIndex);
            	  /*
            	  Pattern pattern = Pattern.compile("(and|or|\\()[\\s]*[\\w.]+[\\s]*=[\\s]*[']?"+param, Pattern.CASE_INSENSITIVE);
				  Matcher matcher = pattern.matcher(d);
				  if (matcher.find()) {
						String all = matcher.group(0);
						int mpos = matcher.start() ; 
						if(all.startsWith("(")){
							tempSentence+=sentence.substring(0,mpos+1);
						}else{
							tempSentence+=sentence.substring(0,mpos);
						}
				  }else{
					  throw new RuntimeException("弹出窗语句在去掉条件"+param+"时不能正确解释,请在该条件前加 1=1 and ,原sql="+oldSen);
				  }*/
            	  
            	  int andi=d.lastIndexOf("and ");
            	  if(andi == -1){
            		  andi = d.lastIndexOf("and\n");
            	  }
            	  int ori=d.lastIndexOf("or ");
            	  if(ori == -1){
            		  ori = d.lastIndexOf("or\n");
            	  }
            	  int brai=d.lastIndexOf("(");
            	  
            	  //判断是否有子查询
            	  if(brai>andi&&brai>ori&&d.substring(brai).contains("select")){
            		  andi=d.substring(0,brai).lastIndexOf("and ");
            		  ori=d.substring(0,brai).lastIndexOf("or ");
            		  brai=d.substring(0,brai).lastIndexOf("(");
            		  flag=true;
            	  }
            	  
            	  if(andi>ori&&andi>brai){
            		  tempSentence+=sentence.substring(0,andi);
            	  }else if(ori>andi&&ori>brai){
            		  tempSentence+=sentence.substring(0,ori);
            	  }else if(brai>andi&&brai>ori){
            		  tempSentence+=sentence.substring(0,brai+1);
            	  }
            	  
              }else{
            	  //处理特殊字符时要把单引号转为‘’，这里还需把@acute;转为单引号，这个字段被定义来转化原本不需要转的单引号
            	  tempSentence+=sentence.substring(0,param.length()+paramIndex).replace(param,value.replaceAll("'", "''").replaceAll("@acute;", "'"));     
            	  
            	  
              }                
               
              boolean tempflag=false;
              int tempInd=0;
              
              String tempsentence=sentence.substring(param.length()+paramIndex,param.length()+paramIndex+1);
              if(tempsentence.equals("%")){
            	  tempflag=true;
            	  tempInd=1;
              }else if(tempsentence.equals("'")){
            	 tempflag=true;
                 tempInd=0;
              }
              
              if(value.length()==0 && tempflag){
            	  if(flag){
            		  sentence=sentence.substring(param.length()+paramIndex+2+tempInd);
            	  }else{
            		  sentence=sentence.substring(param.length()+paramIndex+1+tempInd);
            	  }
              }else{
            	  sentence=sentence.substring(param.length()+paramIndex);
              }
           }
       }
       sentence=tempSentence+sentence;
       for(int i=0;i<params.size();i++){
    	   String value=paramMap.get(params.get(i)).toString();
    	   //如果这个条件的前后包含了@condsent_:[]并且用户没有输入条件值，那么把@condsent_:[...]去掉,如果用户输入了值，把@condsent_:[]去掉
    	   while (sentence.indexOf("@condsent_" +params.get(i)) >= 0) {
    		   	int indexs = sentence.indexOf("@condsent_"+ params.get(i) + ":[");
    		   	int indexe = sentence.indexOf("]", indexs);		
    		   	if(indexe< 0){
    		   		sentence = sentence.replaceFirst("@condsent_"+ params.get(i) + ":\\[", "@condsent_"+ params.get(i) + ":[]");
    		   		indexe = sentence.indexOf("]", indexs);	
    		   	}
    		   	if(value.length()==0){				
    		   		sentence = sentence.substring(0, indexs)+ sentence.substring(indexe + 1);
    		   	}else{
    		   		sentence = sentence.substring(0, indexs)+ sentence.substring(indexs+("@condsent_"+ params.get(i) + ":[").length(),indexe)
						+ sentence.substring(indexe + 1);
    		   	}
    	   }
       }
       
       tempSentence="";

       while(true){
    	   String temp="";
    	   int andi=sentence.indexOf(" and");
    	   int ori=sentence.indexOf(" or");
    	   
    	   if(andi==-1 && ori==-1){
    		   break;
    	   }
    	  
    	   if((andi<ori || ori==-1) && andi>-1){
    		   temp=sentence.substring(0,andi).trim();
    		   if(temp.length()>0 && temp.substring(temp.length()-1).equals("(")){
    			   tempSentence+=sentence.substring(0,sentence.substring(0,andi).length());
    		   }else{
    			   tempSentence+=sentence.substring(0,andi+" and".length());
    		   }
    		   sentence=sentence.substring(andi+" and".length());
    	   }else{
    		   temp=sentence.substring(0,ori).trim();
    		   if(temp.substring(temp.length()-1).equals("(")){
    			   tempSentence+=sentence.substring(0,sentence.substring(0,ori).length());
    		   }else{
    			   tempSentence+=sentence.substring(0,ori+" or".length());
    		   }
    		   sentence=sentence.substring(ori+" or".length());
    	   }
       }
       sentence=tempSentence+sentence;
       sentence=sentence.replace("GETDATE()", "GETDATE( )");
       sentence=sentence.replace("getdate()", "getdate( )");
       sentence=sentence.replace("()", "");
       sentence=sentence.replaceAll("\\(\\s+\\)", "(1=1)");
       if(sentence.indexOf(" and")==0){
    	   sentence = sentence.substring(5,sentence.length()) ;
       }
       if("+'%'".equals(sentence)){sentence = "" ;}
       return sentence;
   }

}
