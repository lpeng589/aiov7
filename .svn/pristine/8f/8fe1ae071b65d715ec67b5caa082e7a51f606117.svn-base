package com.menyi.web.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccMainSettingBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.certTemplate.CertTemplateMgt;
import com.menyi.aio.web.certificate.CertificateBillBean;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;

import org.apache.bsf.BSFManager;
import org.apache.log4j.Level;
import org.apache.struts.util.MessageResources;
import org.w3c.dom.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p> Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class DefineSQLBean {
    private ArrayList sqlList = new ArrayList();
    private ArrayList tableParams=new ArrayList();//配置文件中的表的列字段集合
    private ArrayList sysParams=new ArrayList();//配置文件中的系统变量集合
    private ArrayList sessParams = new ArrayList();//sess中的变量集合
    private ArrayList codeParams = new ArrayList();//code编码序列
    private String name;
    
    public String path; //define所在文件

    /**
     * 通过xml分析出一个具体的自定义实体
     * 此方法当在SqlConfig 中调用，生成实体后通过HashMap保存在内存中
     * @param node Node XML结节
     * @return DefineSQLBean
     */
    public static DefineSQLBean parse(Node node,HashMap sqlMap,HashMap pathMap,String path) {
        DefineSQLBean bean = new DefineSQLBean();
        NamedNodeMap nnm = node.getAttributes();
        Node nodeName = nnm.getNamedItem("name");
        bean.setName(nodeName.getNodeValue());

        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);

            if(temp.getNodeName().equalsIgnoreCase("sql"))
            {
                Node typeNode = temp.getAttributes().getNamedItem("type");
                String type = null;
                String sentence = null;
                ArrayList condition = new ArrayList();
                ArrayList execs = null;

                if (typeNode != null) {
                    type = typeNode.getNodeValue();
                }

                if ((type == null) || (type != null && (type.equalsIgnoreCase("procedure")||type.equalsIgnoreCase("define")||type.equalsIgnoreCase("certificate")||type.equalsIgnoreCase("class")||type.equalsIgnoreCase("setValue")||type.equalsIgnoreCase("setInputType")))) {
                    sentence =temp.getTextContent().trim();
                    bean.parseSentenceGetParam(sentence);
                } else if (type != null && type.equalsIgnoreCase("condition")) {
                    execs = new ArrayList();
                    NodeList conNodes = temp.getChildNodes();
                    for (int j = 0; j < conNodes.getLength(); j++) {
                        Node tempNode = conNodes.item(j);
                        if (tempNode.getNodeName().equalsIgnoreCase("condition")) {
                            NodeList selNodes=tempNode.getChildNodes();
                            for(int k=0;k<selNodes.getLength();k++)
                            {
                                Node selNode=selNodes.item(k);
                                if(selNode.getNodeName().equalsIgnoreCase("select"))
                                {
                                    String select=selNode.getTextContent();
                                    condition.add(select);
                                    bean.parseSentenceGetParam(select);
                                }
                            }

                        } else if (tempNode.getNodeName().equalsIgnoreCase("exec")) {
                            String execCon = tempNode.getAttributes().getNamedItem(
                                    "condition").getNodeValue();
                            bean.parseSentenceGetParam(execCon);
                            NodeList conSqlNodes=tempNode.getChildNodes();
                            String error=null;
                            String testReturn=null;
                            String sucess=null ;
                            ArrayList sqls=new ArrayList();
                            ConfirmBean confirm=null;
                            
                            for(int k=0;k<conSqlNodes.getLength();k++)
                            {
                                Node conSqlNode=conSqlNodes.item(k);

                                if(conSqlNode.getNodeName().equalsIgnoreCase("sql"))
                                {
                                    Node sqlTypeNode=conSqlNode.getAttributes().getNamedItem("type");
                                    String sqlType=null;
                                    if(sqlTypeNode!=null)
                                    {
                                        sqlType = conSqlNode.getAttributes().
                                                  getNamedItem("type").
                                                  getNodeValue();
                                    }
                                    String sqlSentence=conSqlNode.getTextContent();
                                    bean.parseSentenceGetParam(sqlSentence);
                                    DefSql conSql=bean.new DefSql(sqlType,sqlSentence,null,null);
                                    sqls.add(conSql);
                                }else if(conSqlNode.getNodeName().equalsIgnoreCase("error")){
                                    error=conSqlNode.getTextContent();
                                    bean.parseSentenceGetParam(error);
                                }else if(conSqlNode.getNodeName().equalsIgnoreCase("return")){
                                    testReturn=conSqlNode.getTextContent();
                                    bean.parseSentenceGetParam(testReturn);
                                }else if(conSqlNode.getNodeName().equalsIgnoreCase("sucess")){
                                	sucess = conSqlNode.getTextContent() ;
                                	bean.parseSentenceGetParam(sucess) ;
                                }else if(conSqlNode.getNodeName().equalsIgnoreCase("confirm")){
                                	String yesDefine=conSqlNode.getAttributes().getNamedItem("yesDefine").getNodeValue();
                                	String noDefine=conSqlNode.getAttributes().getNamedItem("noDefine").getNodeValue();
                                	String content=conSqlNode.getTextContent();
                                	confirm=new ConfirmBean(yesDefine,noDefine,content);
                                	bean.parseSentenceGetParam(content);
                                }
                            }
                            String loop = "";
                            if(tempNode instanceof Element)
                            {
                            	loop = ((Element)tempNode).getAttribute("loop");
                            }
                            DefExec defExec = bean.new DefExec(execCon,error,sucess,sqls,confirm,loop);
                            defExec.testReturn = testReturn;
                            execs.add(defExec);
                        }
                    }
                }
                DefSql defSql = bean.new DefSql(type, sentence, condition, execs);
                bean.getSqlList().add(defSql);
            }
        }    
//        if(pathMap.get(bean.name) != null){
//        	BaseEnv.log.debug("DefineSQLBean.parse define 重复加载 name:"+bean.name +"; oldFile="+pathMap.get(bean.name)+"; newFile="+path);
//        }
        sqlMap.put(bean.name,bean);
        bean.path = path;
        pathMap.put(bean.name, path);
        
        return bean;
    }
    
   public Result getTableParamMap(HashMap values){
       Result rs = new Result();
       HashMap tableParamMap = new HashMap();
       List params = this.getTableParams();
       for (int i = 0; i < params.size(); i++) {
           String paramStr = params.get(i).toString();
           if (paramStr == null || paramStr.length() == 0) {
               break;
           }
           Object obj= values.get(paramStr);           
           if(obj==null){
        	   String  paramFieldName = paramStr.substring(paramStr.
                                                       indexOf("_") + 1).trim();     
        	   obj=values.get(paramFieldName);
           }
           
           tableParamMap.put(paramStr, obj==null?"":obj.toString());
       }
       rs.setRetVal(tableParamMap);
       return rs;
   }
    /**
     * 自定义语句的执行体：
     * 注意：在方法执行体中不能加入commit 或rollback的语句
     * 这里要执行所有自定义实体中所有的sql语句，能适应存储过程的执行，和分析条件
     * @param conn Connection 数库据连接
     * @param paramMap HashMap 外部参数值的哈希表。key 为自定义实体中的参数名。
     * @return Result 返回的执行结果
     */
    public Result execute(Connection conn, HashMap values,String userId,MessageResources resources,Locale locale,String defineInfo) {
        Result rs=new Result();
        //得到表参数值
        //取参数;
        rs=getTableParamMap(values);
        HashMap tableParamMap=null;
        if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
            tableParamMap=(HashMap)rs.getRetVal();
        }else{
            return rs;
        }

        try
        {
        	 long time=System.currentTimeMillis();
             HashMap sysParamMap=this.getSystemParam(sysParams,conn);
             HashMap sessParamMap=getSessParam(userId,sessParams);
             HashMap codeParamMap=getCodeParam(codeParams,conn,userId);
             HashMap proceReturnMap=new HashMap();
             for (int i = 0; i < sqlList.size(); i++) {            	
                DefSql defSql=(DefSql)sqlList.get(i);
                HashMap queryParamMap=new HashMap();
                if(defSql.type==null){
                	time=System.currentTimeMillis();
                    rs=execSql(defSql.sentence,conn,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap, resources, locale);
                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
                    {
                    	delBillNo(codeParamMap, codeParams,conn);
                       return rs;
                    }
                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("procedure")){
                	time=System.currentTimeMillis();
                    rs=execProcedure(defSql.sentence,conn,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap, resources, locale);
                   
                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS&&rs.getRetCode()!=-2222)
                    {       
                    	delBillNo(codeParamMap, codeParams,conn);
                        return rs;
                    }
                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("certificate")){
                	time=System.currentTimeMillis();
                    rs=execCertificate(userId,defSql.sentence,(String)values.get("id"),conn, resources, locale);
                   
                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS&&rs.getRetCode()!=-2222)
                    {       
                    	delBillNo(codeParamMap, codeParams,conn);
                        return rs;
                    }
                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("class")){
                	time=System.currentTimeMillis();
                    rs=execClass(defSql.sentence,conn,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap, resources, locale);
                   
                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS&&rs.getRetCode()!=-2222)
                    {       
                    	delBillNo(codeParamMap, codeParams,conn);
                        return rs;
                    }
                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("setValue")){
                	//设置表的内存值
                	time=System.currentTimeMillis();
                	rs=getConditionResult(conn,defSql.getSentence(),tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap,true,values);
                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
                    {
                        return rs;
                    }
                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("setInputType")){
                	time=System.currentTimeMillis();
                    rs=execInputType(defSql.sentence,conn,values,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap, resources, locale);
                   
                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS&&rs.getRetCode()!=-2222)
                    {       
                        return rs;
                    }
                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("define")){
                	time=System.currentTimeMillis();
                    DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.
                                                  defineSqlMap.get(defSql.
                            getSentence());
                    rs = defineSqlBean.execute(conn, values, userId,resources,locale,defineInfo);
                    if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
                    	delBillNo(codeParamMap, codeParams,conn);
                        return rs;
                    }
                }else if(defSql.type!=null&&defSql.type.equalsIgnoreCase("condition")){
                    ArrayList condition = defSql.getCondition();
                    for(int j=0;condition!=null&&j<condition.size();j++)
                    {
                       rs=getConditionResult(conn,condition.get(j).toString(),tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap,false,null);
                       if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
                       {
                    	   delBillNo(codeParamMap, codeParams,conn);
                           return rs;
                       }
                    }
                    time=System.currentTimeMillis();
                    ArrayList execs=defSql.getExecs();
                    int execCount = 0;
                    for(int j=0;j<execs.size() ;j++)
                    {
                        DefExec defExec=(DefExec)execs.get(j);
                        String exeCondition=parseSentencePutParam(defExec.condition,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap, resources, locale,true);
                        if(exeCondition.equals("@startSystem")){
                        	SystemState.instance.dogState = SystemState.DOG_RESTART;
                        	continue ;
                        }
                        time=System.currentTimeMillis();
                        //判断条件是否成立
                         BSFManager evalM=BaseEnv.evalManager;
                         BaseEnv.log.debug("exeCondition:"+name+" :"+exeCondition);
                         Boolean flag = false;
                         try{
                         flag=(Boolean)evalM.eval("javascript", "XX", 0, 0, exeCondition);
                         }catch(Exception e){
                        	 BaseEnv.log.error("DefineSQLBean.execute javaScript Error exeCondition="+defExec.condition+" :result="+exeCondition);
                        	 throw e;
                         }
                         BaseEnv.log.debug("flag:"+flag);
                         if(flag)
                        {
                        	 int execTimes = 1;
                        	 String tmp = "";
                        	 if(defExec.loopTimes!=null)
                        	 {
                    			 tmp = defExec.loopTimes;
                        		 if(defExec.loopTimes.startsWith(SqlReturn))
                        		 {
                        			 tmp = tmp.substring(SqlReturn.length());
                        			 tmp = String.valueOf(queryParamMap.get(tmp));
                        		 }
                        		 if(tmp!=null)
                        		 {
                        			 try {
										execTimes = Integer.parseInt(tmp);
									} catch (NumberFormatException e) {
									}
                        		 }
                        	 }
                        	 //tableParamMap
							do { 
								tableParamMap.put("loopCount", String.valueOf(execCount));
								values.put("loopCount", String.valueOf(execCount));
								
								if (defExec.testReturn != null) {
									String msg = defExec.testReturn.trim();
									rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
									rs.setRetVal(msg);
									BaseEnv.log.debug("---自定义流程测试返回，返回信息：" + msg);
									return rs;
								} else if (defExec.error != null) {
									String[] msg = defExec.error.trim().split(",");
									for (int k = 0; k < msg.length; k++) {
										msg[k] = parseSentencePutParam(msg[k], tableParamMap, sysParamMap, queryParamMap, sessParamMap, codeParamMap,
												proceReturnMap, resources, locale,false);
									}
									rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_ERROR);
									String[] obj = new String[2];
									String param = "";
									String url = "";
									for (int k = 0; k < msg.length; k++) {
										if (msg[k].contains("@AlertURL:")) {
											url = msg[k].substring("@AlertURL:".length());
										} else {
											param += msg[k].replaceAll(",", "@RepComma:") + ",";
										}
									}
									obj[0] = param.substring(0, param.length() - 1);
									obj[1] = url;
									BaseEnv.log.debug("---error，返回信息：" + obj[0]+"url="+obj[1]);
									rs.setRetVal(obj);
									return rs;
								} else if (defExec.confirm != null) {
									if (defineInfo.contains(":" + defExec.confirm.yesDefine + ";")) {
										DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defExec.confirm.yesDefine);
										return defineSqlBean.execute(conn, values, userId, resources, locale, defineInfo);
									}
									rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_CONFIRM);
									String[] msg = defExec.confirm.msgContent.trim().split(",");
									for (int k = 0; k < msg.length; k++) {
										msg[k] = parseSentencePutParam(msg[k], tableParamMap, sysParamMap, queryParamMap, sessParamMap, codeParamMap,
												proceReturnMap, resources, locale,false);
									}
									String msgContent = "";
									for (int k = 0; k < msg.length; k++) {
										msgContent += msg[k] + ",";
									}
									ConfirmBean confirm = new ConfirmBean(defExec.confirm.yesDefine, defExec.confirm.noDefine, msgContent);
									BaseEnv.log.debug("---confirm，返回信息：" + msgContent);
									rs.setRetVal(confirm);
									return rs;
								} else {
									time = System.currentTimeMillis();
									ArrayList conSqls = defExec.defSqls;
									for (int k = 0; k < conSqls.size(); k++) {
										DefSql conSql = (DefSql) conSqls.get(k);
										String conSqlType = conSql.type;
										if (conSqlType == null) {
											time = System.currentTimeMillis();
											rs = execSql(conSql.sentence, conn, tableParamMap, sysParamMap, queryParamMap, sessParamMap, codeParamMap,
													proceReturnMap, resources, locale);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
												delBillNo(codeParamMap, codeParams, conn);
												return rs;
											}
											BaseEnv.log.debug("************上述语句执行时间：" + (System.currentTimeMillis() - time));
										} else if (conSqlType.equals("define")) {
											time = System.currentTimeMillis();
											DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(conSql.getSentence());
											rs = defineSqlBean.execute(conn, values, userId, resources, locale, defineInfo);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
												delBillNo(codeParamMap, codeParams, conn);
												return rs;
											}
										} else if (conSqlType.equals("procedure")) {
											time = System.currentTimeMillis();
											rs = execProcedure(conSql.sentence, conn, tableParamMap, sysParamMap, queryParamMap, sessParamMap, codeParamMap,
													proceReturnMap, resources, locale);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != -2222) {
												delBillNo(codeParamMap, codeParams, conn);
												return rs;
											}
											BaseEnv.log.debug("************上述语句执行时间：" + (System.currentTimeMillis() - time));
										} else if (conSqlType.equals("certificate")) {
											time = System.currentTimeMillis();

											rs = execCertificate(userId, conSql.sentence, (String) values.get("id"), conn, resources, locale);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != -2222) {
												delBillNo(codeParamMap, codeParams, conn);
												return rs;
											}
											BaseEnv.log.debug("************上述语句执行时间：" + (System.currentTimeMillis() - time));
										} else if (conSqlType.equals("class")) {
											time = System.currentTimeMillis();
											rs = execClass(conSql.sentence, conn, tableParamMap, sysParamMap, queryParamMap, sessParamMap, codeParamMap,
													proceReturnMap, resources, locale);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != -2222) {
												delBillNo(codeParamMap, codeParams, conn);
												return rs;
											}
											BaseEnv.log.debug("************上述语句执行时间：" + (System.currentTimeMillis() - time));
										}else if(conSqlType.equalsIgnoreCase("setValue")){ 
						                	//设置表的内存值 
						                	time=System.currentTimeMillis();
						                	rs=getConditionResult(conn,conSql.sentence,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap,true,values);
						                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
						                    {
						                        return rs;
						                    }
						                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
						                }else if(conSqlType.equalsIgnoreCase("setInputType")){
						                	time=System.currentTimeMillis();
						                    rs=execInputType(conSql.sentence,conn,values,sysParamMap,queryParamMap,sessParamMap,codeParamMap,proceReturnMap, resources, locale);
						                   
						                    if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS&&rs.getRetCode()!=-2222)
						                    {       
						                        return rs;
						                    }
						                    BaseEnv.log.debug("************上述语句执行时间："+(System.currentTimeMillis()-time));
						                }
									}
									/* 成功提示 跟 失败有所区别， 执行完所有事物 */
									if (defExec.sucess != null) {
										rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_ALERT);
										String[] msg = defExec.sucess.trim().split(",");
										for (int k = 0; k < msg.length; k++) {
											msg[k] = parseSentencePutParam(msg[k], tableParamMap, sysParamMap, queryParamMap, sessParamMap, codeParamMap,
													proceReturnMap, resources, locale,false);
										}
										String[] obj = new String[2];
										String param = "";
										String url = "";
										for (int k = 0; k < msg.length; k++) {
											if (msg[k].contains("@AlertURL:")) {
												url = msg[k].substring("@AlertURL:".length());
											} else {
												param += msg[k].replaceAll(",", "@RepComma:") + ",";
											}
										}
										if(param.length() >0){
											obj[0] = param.substring(0, param.length() - 1);
										}else{
											obj[0]="";
										}
										obj[1] = url;
										BaseEnv.log.debug("---sucess，返回信息：" + obj[0]+";url="+obj[1]);
										rs.setRetVal(obj);
									}
								}
								execCount ++;
							} while (execCount < execTimes);
                      }
                   }
                }
            }
        }catch(Exception ex)
        {
            BaseEnv.log.error("DefineSQLBean execute() ", ex);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ex.getMessage());
        }
        return rs;
    }

    /**
     * 删除单据编号fjj
     * @param codeParamMap
     * @param codeParams
     */
    public void delBillNo(HashMap codeParamMap,ArrayList codeParams,Connection conn){
    	 for (int i = 0; i < codeParams.size(); i++) {
             String param = codeParams.get(i).toString();
             BillNo.billNoRemove(param, String.valueOf(codeParamMap.get(param)),conn);
    	 }
    }
    
    
    
    /**
     * 将传入的语句中的变量替换成值（引用表值，系统变量，查询结果变量）
     * @param sentence String
     * @param tableParamMap HashMap define中引用表列名、列值
     * @param sysParamMap HashMap 系统参数名称，值
     * @param queryParamMap HashMap 条件语句查询的结果列名，列值
     * @return String 替换后的结果
     */
    private String parseSentencePutParam(String sentence,HashMap tableParamMap,HashMap sysParamMap,
                                         HashMap queryParamMap,HashMap sessParamMap,HashMap codeParamMap,HashMap proceReturnMap,MessageResources resources,Locale locale,boolean isJs)
    {
        ArrayList senParam=parseSentenceGetParam(sentence);
        String tempSentence="";
        for(int i=0;i<senParam.size();i++)
        {
            String param=senParam.get(i).toString();
            String khparam = "";
            int paramIndex=sentence.indexOf(param);
            boolean haskh = false;
            if(paramIndex == -1){
            	khparam = param.substring(0,param.indexOf(":")+1)+"{"+(param.substring(param.indexOf(":")+1))+"}";
            	paramIndex=sentence.indexOf(khparam); //{}可作为参数分隔符
            	if(paramIndex > -1){
            		haskh = true;
            	}
            }
            if(paramIndex>-1)
            {
                //去掉标识
                String tempParam = "";
                int index_sys = param.indexOf(mem);
                int index_table = param.indexOf(ValueofDB);
                int index_query = param.indexOf(SqlReturn);
                int index_sess = param.indexOf(Sess);
                int index_code = param.indexOf(Code);
                int index_proce =param.indexOf(Proce);
                int index_locale =param.indexOf(Locale);
                if (index_sys >= 0)
                    tempParam = param.substring(mem.length());
                else if (index_table >= 0)                
                    tempParam = param.substring(ValueofDB.length());
                else if (index_proce>=0)
                	tempParam=param.substring(Proce.length());
                else if (index_query >= 0)
                    tempParam = param.substring(SqlReturn.length());
                else if (index_sess >= 0)
                    tempParam = param.substring(Sess.length());
                else if (index_locale >= 0)
                    tempParam = param.substring(Locale.length());
                else if (index_code >= 0) {
                    tempParam = param.substring(Code.length() + 1);
                    tempParam = tempParam.substring(0, tempParam.length() - 1);
                }

                //得到参数的值
               Object temp=tableParamMap.get(tempParam);
               String value="";
               if(temp!=null)
                   value=temp.toString();//为引用表列值
               else
               {
                   temp=sysParamMap.get(tempParam);
                   if(temp!=null)
                     value=temp.toString();//为系统内存值
                   else
                   {
                       temp=queryParamMap.get(tempParam);
                       if(temp!=null)
                          value=temp.toString();//为查询出的结果值
                       else{
                           temp=sessParamMap.get(tempParam);
                           if(temp!=null)
                               value=temp.toString();//为sess中的值
                           else{
                               temp=codeParamMap.get(tempParam);
                               if (temp != null)
                                   value = temp.toString(); //为code编码序列
                               else{
                            	   temp=proceReturnMap.get(tempParam);
                            	   if(temp!=null)
                            		   value=temp.toString();
                            	   else{
                            		   if(resources!=null){
	                                	   temp=resources.getMessage(locale,tempParam);
	                                	   if(temp!=null){
	                                		   value=temp.toString();
	                                	   }
                            		   }
                                   }
                               }
                           }
                       }
                   }
               }
               if(isJs){ //如果本表达式用于js运算，则要把单引号，双引号等处理成\',
            	   value = value.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
               }
               if(haskh){ //有{} 分隔符
            	   tempSentence+=sentence.substring(0,khparam.length()+paramIndex).replace(khparam,value);
	               sentence=sentence.substring(khparam.length()+paramIndex);
               }else{
	               tempSentence+=sentence.substring(0,param.length()+paramIndex).replace(param,value);
	               sentence=sentence.substring(param.length()+paramIndex);
               }
            }
        }
        sentence=tempSentence+sentence;
        return sentence;
    }

    /**
     * 分析传入的语句是否包含引用表字段，系统变量，查询结果变量 若有保存到tableParams中，sysParams中
     * @param sentence String
     * @return Result
     */
    private static String mem="@MEM:";
    private static String ValueofDB="@ValueofDB:";
    private static String SqlReturn="@SqlReturn:";
    private static String Sess="@Sess:";
    private static String Code="@CODE:";
    private static String Proce="@ProcReturn:";
    private static String Locale="@Locale:";
    private ArrayList parseSentenceGetParam(String sentence)
    {
        boolean flag=true;
        String temp=sentence;
        ArrayList senParam=new ArrayList();
        while(flag)
        {
           int index_sys=temp.indexOf(mem);
           int index_table=temp.indexOf(ValueofDB);
           int index_query=temp.indexOf(SqlReturn);
           int index_sess=temp.indexOf(Sess);
           int index_code=temp.indexOf(Code);
           int index_proce=temp.indexOf(Proce);
           int index_locale=temp.indexOf(Locale);
           int index_start=-1;

           int index=-1;
           String type="";
           if((index_sys>=0)&&(index_sys<index_table||index_table<0)&&(index_sys<index_query||index_query<0)
                   &&(index_sys<index_sess||index_sess<0)&&(index_sys<index_code||index_code<0)&&
                   (index_sys<index_proce||index_proce<0)&&(index_sys<index_locale||index_locale<0)){
             index = index_sys;
             type="sys";
             index_start=index+mem.length();
          }else if((index_table>=0)&&(index_table<index_sys||index_sys<0)&&(index_table<index_query||index_query<0)
                  &&(index_table<index_sess||index_sess<0)&&(index_table<index_code||index_code<0)
                  &&(index_table<index_proce||index_proce<0)&&(index_table<index_locale||index_locale<0)){
             index = index_table;
             type="table";
             index_start=index+ValueofDB.length();
          }else if((index_query>=0)&&(index_query<index_sys||index_sys<0)&&(index_query<index_table||index_table<0)
                  &&(index_query<index_sess||index_sess<0)&&(index_query<index_code||index_code<0)
                  &&(index_query<index_proce||index_proce<0)&&(index_query<index_locale||index_locale<0)){
              index=index_query;
              type="query";
              index_start=index+SqlReturn.length();
          }else if((index_sess>=0)&&(index_sess<index_sys||index_sys<0)&&(index_sess<index_table||index_table<0)
                  &&(index_sess<index_query||index_query<0)&&(index_sess<index_code||index_code<0)
                  &&(index_sess<index_proce||index_proce<0)&&(index_sess<index_locale||index_locale<0)){
              index=index_sess;
              type="sess";
              index_start=index+Sess.length();
          }else if((index_code>=0)&&(index_code<index_sys||index_sys<0)&&(index_code<index_table||index_table<0)
                  &&(index_code<index_query||index_query<0)&&(index_code<index_sess||index_sess<0)
                  &&(index_code<index_proce||index_proce<0)&&(index_code<index_locale||index_locale<0)){
              index=index_code;
              type="code";
              index_start=index+Code.length();
          }else if((index_proce>=0)&&(index_proce<index_sys||index_sys<0)&&(index_proce<index_table||index_table<0)
                  &&(index_proce<index_query||index_query<0)&&(index_proce<index_sess||index_sess<0)
                  &&(index_proce<index_code||index_code<0)&&(index_proce<index_locale||index_locale<0)){
              index=index_proce;
              type="proce";
              index_start=index+Proce.length();
          }else if((index_locale>=0)&&(index_locale<index_sys||index_sys<0)&&(index_locale<index_table||index_table<0)
                  &&(index_locale<index_query||index_query<0)&&(index_locale<index_sess||index_sess<0)
                  &&(index_locale<index_code||index_code<0)&&(index_locale<index_proce||index_proce<0)){
              index=index_locale;
              type="locale";
              index_start=index+Locale.length();
          }


           if(index>=0)
          {
             temp=temp.substring(index_start);
             int index_end=-1;
             String indexStr="@a,a)a a>a<a=a!a+a-a*a/a&a|a'a\"a\n";//用a连接字符变量后面可能出现的标识
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

             //@CODE:[@y2-@M]类型的，取“]”作为结束标识，去掉“[”
             if("code".equals(type)){
                 temp=temp.substring(1,temp.length());
                 index_end=temp.indexOf("]");
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
                 if(param.indexOf("{")== 0 && param.indexOf("}") > -1){
                	 //如果参数包含如@ValueofDB:{abc},则{}只是分隔符
                	 param = param.substring(1,param.indexOf("}"));
                 }
                 if(type.equals("table")&&!tableParams.contains(param)){
                     tableParams.add(param);
                 }else if(type.equals("sys")&&!sysParams.contains(param)){
                     sysParams.add(param);
                 }else if(type.equals("sess")&&!sessParams.contains(param)){
                     sessParams.add(param);
                 }else if (type.equals("code")&&!codeParams.contains(param)){
                     codeParams.add(param);
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
                 else if(type.equals("proce"))
                	 senParam.add(Proce+param);
                 else if(type.equals("locale"))
                	 senParam.add(Locale+param);
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
   private HashMap getSystemParam(ArrayList sysParams,Connection conn)throws Exception
    {
        HashMap sysParamMap=new HashMap();
        try {
            for (int i = 0; i < sysParams.size(); i++) {
                String param = sysParams.get(i).toString();
                Hashtable<String,SystemSettingBean> systemSet=BaseEnv.systemSet;
                String value=null;
                SystemSettingBean setbean=systemSet.get(param);
                if(setbean!=null)
                {
                    value=setbean.getSetting();
                    if(value==null||value.length()==0){
                        value = setbean.getDefaultValue();
                    }
                }

                sysParamMap.put(param,value);
            }
            ArrayList<String[]> isStarts=BaseEnv.isStartFlowInfo;
            for(int i=0;i<isStarts.size();i++){
            	String tableName=isStarts.get(i)[0].substring(0,isStarts.get(i)[0].indexOf("_"));
            	if(BaseEnv.workFlowInfo.get(tableName)!=null){
            		sysParamMap.put(isStarts.get(i)[0], BaseEnv.workFlowInfo.get(tableName).getTemplateStatus());
            	}
            }
            
        	//凭证设置
        	AccMainSettingBean settingBean = null;
        	Result result = new VoucherMgt().queryVoucherSetting(conn);
        	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		settingBean = (AccMainSettingBean)result.retVal;
        	}
        	sysParamMap.put("tblAccMain_IsStatart", settingBean.getIsAuditing());

            sysParamMap.put("sysShortDate",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd));
            sysParamMap.put("sysLongDate",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
            sysParamMap.put("currentTimeMillis",System.currentTimeMillis());
        }catch (Exception ex) {
           throw ex;
        }

        return sysParamMap;
    }

    //从登录时取的sess变量，放在BaseEnv中
    private HashMap getSessParam(String userId,ArrayList sessParams)throws Exception
    {
        HashMap sessParamMap=new HashMap();
        try {
            Hashtable sessionSet=BaseEnv.sessionSet;
            if(userId==null){
            	return sessParamMap ;
            }
            Hashtable session=(Hashtable)sessionSet.get(userId);
            for (int i = 0; i < sessParams.size(); i++) {
                String param = sessParams.get(i).toString();
                Object sess = session.get(param);
                if(sess!=null)
                    sessParamMap.put(param,sess);
            }
        }catch (Exception ex) {
           throw ex;
        }
        return sessParamMap;
    }

    //取到生成的code编码序列
    private HashMap getCodeParam(ArrayList codeParams,Connection conn,String userId) throws Exception {
        HashMap codeParamMap = new HashMap();
        try {
            for (int i = 0; i < codeParams.size(); i++) {
                String param = codeParams.get(i).toString();
                String code = "";
                //引用新规则的单据编号规则
                if(param.indexOf("_")>-1){
                	code = CodeGenerater.generater(param,conn,userId);
                }else{
                	code = CodeGenerater.generater(param,conn);
                }
                if(code != null)
                    codeParamMap.put(param,code);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return codeParamMap;
    }


    /**
     * 根据传入的SQL查询结果如果有结果返回第一条记录，如果没有结果则此以别名命名保存在queryParamMap中的为“null”字符
     * @param conn Connection
     * @param condition String  条件语句
     * @param tableParamMap HashMap define中引用表列名、列值
     * @param sysParamMap HashMap 系统参数名称，值
     * @param queryParamMap HashMap 条件语句查询的结果列名，列值
     * @return Result
     */
    private Result getConditionResult(Connection conn,String condition,HashMap tableParamMap,
                                      HashMap sysParamMap,HashMap queryParamMap,HashMap sessParamMap,HashMap codeParamMap,HashMap proceReturnMap,boolean setValue,HashMap values)
    {
    	long time=System.currentTimeMillis();
        Result rs=new Result();
        try
        {
            ArrayList senParam=parseSentenceGetParam(condition);//顺序存放要执行的语句中的参数
            String tempSentence="";
            for(int i=0;i<senParam.size();i++)
            {
              String param=senParam.get(i).toString();
              int paramIndex=condition.indexOf(param);
               tempSentence=tempSentence+condition.substring(0,paramIndex+param.length()).replace(param,"?");
               condition=condition.substring(paramIndex+param.length());
            }
            condition=tempSentence+condition;

            BaseEnv.log.debug("自定义执行的语句显示： "+name+" :"+condition);
            BaseEnv.log.debug("上述自定义执行的语句参数显示： ");

            PreparedStatement st = conn.prepareStatement(condition);
            for(int i=1;i<=senParam.size();i++)
            {
              String param=senParam.get(i-1).toString();
              //去掉标识
               String tempParam="";
               int index_sys=param.indexOf(mem);
               int index_table=param.indexOf(ValueofDB);
               int index_query=param.indexOf(SqlReturn);
               int index_sess = param.indexOf(Sess);
               int index_code = param.indexOf(Code);
               int index_proc=param.indexOf(Proce);
               if(index_sys>=0)
            	   tempParam=param.substring(mem.length());
               else if(index_table>=0)
            	   tempParam=param.substring(ValueofDB.length());
               else if(index_proc>=0)
            	   tempParam=param.substring(Proce.length());
               else if(index_query>=0)
                  tempParam=param.substring(SqlReturn.length());
               else if(index_sess>=0)
                  tempParam=param.substring(Sess.length());
               else if(index_code>=0){
                  tempParam = param.substring(Code.length() + 1);
                  tempParam = tempParam.substring(0,tempParam.length()-1);
              }

              //得到参数的值
              Object temp=tableParamMap.get(tempParam);
              String value="";
              if(temp!=null)
                  value=temp.toString();//为引用表列值
              else
              {
                  temp=sysParamMap.get(tempParam);
                  if(temp!=null)
                    value=temp.toString();//为系统内存值
                  else
                  {
                      temp=queryParamMap.get(tempParam);
                      if(temp!=null)
                         value=temp.toString();//为查询出的结果值
                      else{
                          temp=sessParamMap.get(tempParam);
                          if (temp != null)
                              value = temp.toString(); //为sess的值
                          else{
                              temp=codeParamMap.get(tempParam);
                              if(temp != null)
                                  value = temp.toString();//为code编码序列
                              else{
                            	  temp=proceReturnMap.get(tempParam);
                            	  if(temp!=null)
                            		  value=temp.toString();
                              }
                          }
                      }
                  }

              }
              if(DefineSQLBean.isInt(value)&&param.indexOf("@MEM:Digits")==0){//如果是系统参数小数位数，设置值用int,这样define文件不用做处理
            	  BaseEnv.log.debug(i+"整数 值："+value);
            	  st.setInt(i,Integer.parseInt(value));
              }else{
            	  BaseEnv.log.debug(i+"值："+value);
            	  st.setString(i,value);
              }
          }

          HashMap setMap = null;
          if(setValue){
        	  setMap =  tableParamMap;
	   	   }else{
	   		   setMap =  queryParamMap;
	   	   }	  
            
            ResultSet rss =st.executeQuery();

            java.sql.ResultSetMetaData rsm=rss.getMetaData();
            int colCount=rsm.getColumnCount();
            if(rss.next())
            {
                for(int i=1;i<=colCount;i++)
                {
                    String columnName=rsm.getColumnName(i);
                    int type=rsm.getColumnType(i);
                    if(type==Types.NUMERIC){
                       BigDecimal bigDec = rss.getBigDecimal(i) ;
                       if(bigDec!=null && bigDec.doubleValue()==0){
                    	   setMap.put(columnName,bigDec.doubleValue());
                    	   if(setValue){
                    		   values.put(columnName,bigDec.doubleValue());
                    	   }
                       }else{
                    	   setMap.put(columnName,bigDec);
                    	   if(setValue){
                    		   values.put(columnName,bigDec);
                    	   }
                       } 
                       BaseEnv.log.debug("query condition columnName:"+columnName+"----value:"+bigDec);
                    }else{
                       BaseEnv.log.debug("query condition columnName:"+columnName+"----value:"+rss.getString(i));
                       setMap.put(columnName,rss.getString(i));
                       if(setValue){
                		   values.put(columnName,rss.getString(i));
                	   }
                    }
                }
            }else if(!setValue) //设置值 时，为空的记录不能写
            {
                for(int i=1;i<=colCount;i++)
                {
                    String columnName=rsm.getColumnName(i);
                    setMap.put(columnName,"null");
                    if(setValue){
             		   values.put(columnName,"null");
             	   }
                }
            }
            BaseEnv.log.debug("---上述语句时间---"+(System.currentTimeMillis()-time));
        }catch(Exception ex)
        {
        	BaseEnv.log.error("DefineSQLBean.getConditionResult Error",ex);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ex.getMessage());
        }
        return rs;
    }

    /**
     * 执行传入的SQL语句
     * @param sentence String
     * @param conn Connection
     * @param tableParamMap HashMap define中引用表列名、列值
     * @param sysParamMap HashMap 系统参数名称，值
     * @param queryParamMap HashMap 条件语句查询的结果列名，列值
     * @return Result
     */
    private Result execSql(String sentence,Connection conn,HashMap tableParamMap,
                           HashMap sysParamMap,HashMap queryParamMap,HashMap sessParamMap,HashMap codeParamMap
                           ,HashMap proceReturnMap,MessageResources resources,Locale locale)
   {
       Result rs=new Result();
       try
       {
           ArrayList senParam=parseSentenceGetParam(sentence);//顺序存放要执行的语句中的参数
           String tempSentence="";
           for(int i=0;i<senParam.size();i++)
           {
               String param=senParam.get(i).toString();
               int paramIndex=sentence.indexOf(param);
               tempSentence=tempSentence+sentence.substring(0,paramIndex+param.length()).replace(param,"?");
               sentence=sentence.substring(paramIndex+param.length());

           }
           sentence=tempSentence+sentence;

           BaseEnv.log.debug("自定义执行的语句显示： "+name+":"+sentence);
           PreparedStatement cs = conn.prepareStatement(sentence);

           for(int i=1;i<=senParam.size();i++)
           {
               String param=senParam.get(i-1).toString();
               //去掉标识
               String tempParam="";
               int index_sys=param.indexOf(mem);
               int index_table=param.indexOf(ValueofDB);
               int index_query=param.indexOf(SqlReturn);
               int index_sess=param.indexOf(Sess);
               int index_code=param.indexOf(Code);
               int index_proce=param.indexOf(Proce);
               int index_locale=param.indexOf(Locale);
               if(index_sys>=0)
                  tempParam=param.substring(mem.length());
               else if(index_table>=0)
                  tempParam=param.substring(ValueofDB.length());
               else if(index_query>=0)
                  tempParam=param.substring(SqlReturn.length());
               else if(index_sess>=0)            	  
                   tempParam=param.substring(Sess.length());
               else if(index_proce>=0)
            	   tempParam=param.substring(Proce.length());
               else if(index_locale>=0)
            	   tempParam=param.substring(Locale.length());
               else if(index_code>=0){
                   tempParam = param.substring(Code.length() + 1);
                   tempParam = tempParam.substring(0,tempParam.length()-1);
               }

               //得到参数的值
               Object temp=tableParamMap.get(tempParam);
               String value="";
               if(temp!=null)
                   value=temp.toString();//为引用表列值
               else
               {
                   temp=sysParamMap.get(tempParam);
                   if(temp!=null)
                     value=temp.toString();//为系统内存值
                   else
                   {
                       temp=queryParamMap.get(tempParam);
                       if(temp!=null)
                          value=temp.toString();//为查询出的结果值
                       else{
                           temp=sessParamMap.get(tempParam);//为sess的值
                           if(temp!=null)
                               value=temp.toString();
                           else{
                               temp=codeParamMap.get(tempParam);//为code编码序列
                               if(temp!=null)
                                   value=temp.toString();
                               else{
                            	   temp=proceReturnMap.get(tempParam);
                            	   if(temp!=null)
                            		   value=temp.toString();
                            	   else{
                            		   if(resources!=null){
	                                	   temp=resources.getMessage(locale,tempParam);
	                                	   if(temp!=null)
	                                		   value=temp.toString();
                            		   }
                                   }
                               }
                           }
                       }
                   }
               }
               
               if(DefineSQLBean.isInt(value)&&param.indexOf("@MEM:Digits")==0){//如果是系统参数小数位数，设置值用int,这样define文件不用做处理
            	   BaseEnv.log.debug(i+"整数 值："+value);
            	   cs.setInt(i,Integer.parseInt(value));
               }else{
            	   BaseEnv.log.debug(i+"值："+value);
            	   cs.setString(i,value);
               }
           }

           int count = cs.executeUpdate();
           BaseEnv.log.debug("DefineSQLBean.execSql  define="+this.name+",  影响行数="+count);
       }
       catch(NumberFormatException ex)
       {
    	   BaseEnv.log.error("DefineSQLBean.execSql Error define="+this.name+", sql="+sentence,ex);
           rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
       }
       catch(Exception ex)
       {
    	   BaseEnv.log.error("DefineSQLBean.execSql Error define="+this.name+",  sql="+sentence,ex);
           rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
           rs.setRetVal(ex.getMessage());
       }
       return rs;
   }

   /**
    * 返回所输入的值是否是双精度型的
    * @param value
    * @return
    */
   public static boolean isDouble(String value){
	   try{
		   Double.parseDouble(value);
		   return true;
	   }catch(Exception ex){
		   return false;
	   }
   }
   
   /**
    * 返回所输入的值是否是整型的
    * @param value
    * @return
    */
   public static boolean isInt(String value){
	   try{
		   Integer.parseInt(value);
		   return true;
	   }catch(Exception ex){
		   return false;
	   }
   }

   /**
    * 执行传入的存储过程
    * @param sentence String
    * @param conn Connection
    * @param tableParamMap HashMap define中引用表列名、列值
    * @param sysParamMap HashMap 系统参数名称，值
    * @param queryParamMap HashMap 条件语句查询的结果列名，列值
    * @return Result
    */

    private Result execProcedure(String sentence,Connection conn ,HashMap tableParamMap,
                                 HashMap sysParamMap,HashMap queryParamMap,HashMap sessParamMap,
                                 HashMap codeParamMap,HashMap proceReturnMap,MessageResources resources,Locale locale)
    {
        Result rs=new Result();
        CallableStatement cs=null;
        try
        {	
        	String formSentence=sentence;
            String []paramsValue=sentence.substring(sentence.indexOf("(")+1,sentence.lastIndexOf(")")).split(",");
            for(int i=0;i<paramsValue.length;i++)
            {
                String tempk=this.parseSentencePutParam(paramsValue[i],tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,
                		proceReturnMap, resources, locale,false);
                paramsValue[i]=tempk.trim();
            }

            String[] params=sentence.substring(sentence.indexOf("(")+1,sentence.lastIndexOf(")")).split(",");
            String tempSentence="";
            boolean exeParam = false; //以变量的方式执行，一但以这种方式执行，则所有参数都必须以这种方式执行
            for(int i=0;i<params.length;i++)
            {
                String param=params[i];
                int paramIndex=sentence.indexOf(param);    		
                Pattern pattern = Pattern.compile("^@[\\w]+=.*");            	
        		Matcher matcher = pattern.matcher(paramsValue[i].trim());
        		if(matcher.find()){
                	exeParam = true;
                	
                	String pValue = paramsValue[i];
                	String tempStr= sentence.substring(0,paramIndex+param.length());
                	tempSentence=tempSentence+tempStr.substring(0,tempStr.indexOf("="))+"=?";
                    sentence=sentence.substring(paramIndex+param.length());
                    paramsValue[i] = pValue.substring(pValue.indexOf("=")+1);
                }else{
                	if(exeParam){
                		rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
                        rs.setRetVal("以变量方式执行的存储过程，必须所有参数都是变量");
                	}
                	String tempStr= sentence.substring(0,paramIndex+param.length());
                	tempSentence=tempSentence+tempStr.substring(0,tempStr.indexOf(param))+"?";
                    sentence=sentence.substring(paramIndex+param.length());
                }                
            }
            sentence=tempSentence+sentence;
            sentence=sentence.replaceAll(mem,"").replaceAll(ValueofDB,"").replaceAll(SqlReturn,"");

            BaseEnv.log.debug("自定义执行的语句显示： "+name+":"+sentence);

            cs = conn.prepareCall(sentence);

            for(int i=1;i<=paramsValue.length-2;i++)
            {
            	BaseEnv.log.debug(i+" 值："+paramsValue[i-1]==null?"null":paramsValue[i-1]);
                cs.setString(i,paramsValue[i-1]);
            }

            cs.registerOutParameter(params.length-1, Types.INTEGER);
            cs.registerOutParameter(params.length, Types.VARCHAR, 50);
          
            cs.execute();

            rs.setRetCode(cs.getInt(params.length-1));
            rs.setRetVal(cs.getString(params.length));
            
            String [] proce=(String[])formSentence.substring(formSentence.indexOf("(")+1,formSentence.lastIndexOf(")")).split(",");
            String retVal=proce[proce.length-1];
            String retCode=proce[proce.length-2];
            proceReturnMap.put(retVal, rs.getRetVal()) ;
            proceReturnMap.put(retCode, rs.getRetCode()) ;
            
            if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
	            SQLWarning warn = cs.getWarnings();
	            while(warn != null){  
	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
	            	}
	            	warn = warn.getNextWarning();
	            }
            }	
            
        }catch(SQLException ex){
        	try{
        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
    	            SQLWarning warn = cs.getWarnings();
    	            while(warn != null){  
    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
    	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
    	            	}
    	            	warn = warn.getNextWarning();
    	            }
                }	
        	}catch(SQLException ex2){
        		BaseEnv.log.error("DefineSQLBean.execProcedure Error",ex);
        	}
        	BaseEnv.log.error("DefineSQLBean.execProcedure Error",ex);
            if(ex.getErrorCode()==8115){
            	rs.setRetCode(ErrorCanst.RET_NUMERICAL_OVERFLOW);
            }else{
            	rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
                rs.setRetVal(ex.getMessage());
            }
        }catch(Exception ex)
        {
        	BaseEnv.log.error("DefineSQLBean.execProcedure Error",ex);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ex.getMessage());
        }
        return rs;
    }
    
    /**
     * 执行凭证生成
     * @param sentence String
     * @param conn Connection
     * @param tableParamMap HashMap define中引用表列名、列值
     * @param sysParamMap HashMap 系统参数名称，值
     * @param queryParamMap HashMap 条件语句查询的结果列名，列值
     * @return Result
     */

     private Result execCertificate(String userId,String sentence,String id,Connection conn ,MessageResources resources,Locale locale)
     {
		Result rs = new Result();
		CallableStatement cs = null;
		try {
			String autoGenerateAcc = GlobalsTool.getSysSetting("autoGenerateAcc");
			// 取模板
			rs = new CertTemplateMgt().detail(sentence.trim(), conn);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				// 取模板失败
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "取凭证模板[" + sentence.trim() + "]失败";
				return rs;
			} else if (rs.retVal == null) {
				// 模板不存在
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "凭证模板[" + sentence.trim() + "]不存在";
				return rs;
			}
			// 加载成功
			CertificateBillBean bean = (CertificateBillBean) rs.retVal;

			if (autoGenerateAcc == null || "true".equals(autoGenerateAcc) || "1".equals(bean.getType())) {
				BaseEnv.log.debug("自定义执行的语句显示： " + name + ":凭证模块--" + sentence);
				rs = new DynDBManager().genCertificate(userId, sentence.trim(),bean, id, conn, resources, locale, true);
			}else{
				BaseEnv.log.debug("自定义执行的语句显示： " + name + ":凭证模块--" + sentence +" ,手动产生凭证无需执行");
			}
		} catch (Exception ex) {

			BaseEnv.log.error("DefineSQLBean.execCertificate Error", ex);
			rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
			rs.setRetVal(ex.getMessage());
		}
		return rs;
	}
    /**
	 * 自定义语句的执行体： 注意：在方法执行体中不能加入commit 或rollback的语句
	 * 这里要执行所有自定义实体中所有的sql语句带condition 的返回的Result 中的值为Boolean
	 * 
	 * @param conn
	 *            Connection 数库据连接
	 * @param paramMap
	 *            HashMap 外部参数值的哈希表。key 为自定义实体中的参数名对于条件查询只存储当前表的id。
	 * @param userId
	 *            当前用户ID
	 * @return Result 返回的执行结果
	 */
    public Result getBooleanCond(Connection conn, HashMap tableParamMap,String userId,MessageResources resources,Locale locale) {
        Result rs=new Result();

        try
        {
             HashMap sysParamMap=this.getSystemParam(sysParams,conn);
             HashMap sessParamMap=getSessParam(userId,sessParams);
             HashMap codeParamMap=getCodeParam(codeParams,conn,userId);
             for (int i = 0; i < sqlList.size(); i++) {
                DefSql defSql=(DefSql)sqlList.get(i);
                HashMap queryParamMap=new HashMap();
                HashMap procReturnMap=new HashMap();
                int exeCount = 0;//如果是循环，最多循环五十次,此变量记录执行次数。
                if(defSql.type!=null&&defSql.type.equalsIgnoreCase("condition"))
                {
                    ArrayList condition = defSql.getCondition();

                    for(int j=0;condition!=null&&j<condition.size();j++)
                    {
                       rs=getConditionResult(conn,condition.get(j).toString(),tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,procReturnMap,false,null);
                       if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
                       {
                           return rs;
                       }
                    }

                    ArrayList execs=defSql.getExecs();
                    for(int j=0;j<execs.size();j++)
                    {
                        DefExec defExec=(DefExec)execs.get(j);
                        String exeCondition=parseSentencePutParam(defExec.condition,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,procReturnMap,resources,locale,true);

                        //判断条件是否成立
                         BSFManager evalM=BaseEnv.evalManager;
                         BaseEnv.log.debug("exeCondition:"+exeCondition);
                         Boolean flag=(Boolean)evalM.eval("javascript", "XX", 0, 0, exeCondition);
                         BaseEnv.log.debug("flag:"+flag);
                         if(flag)
                         {
                             if(defExec.error!=null)
                             {

                                 String [] msg=defExec.error.trim().split(",");
                                 for(int k=0;k<msg.length;k++)
                                 {
                                     msg[k]=parseSentencePutParam(msg[k],tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,procReturnMap,resources,locale,false);
                                 }
                                 rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_ERROR);
                                 String []obj=new String[2];
                                 String param="";
                                 String url="";
                                 for(int k=0;k<msg.length;k++){
                                     if(msg[k].contains("@AlertURL:")){
                                         url=msg[k].substring("@AlertURL:".length());
                                     }else{
                                         param+=msg[k]+",";
                                     }
                                 }
                                 obj[0]=param.substring(0,param.length()-1);
                                 obj[1]=url;
                                 rs.setRetVal(obj);
                                 return rs;
                             }else
                             {
                                 ArrayList conSqls=defExec.defSqls;
                                 for(int k=0;k<conSqls.size();k++)
                                 {
                                      DefSql conSql=(DefSql)conSqls.get(k);
                                      String conSqlType=conSql.type;
                                      if(conSqlType==null)
                                      {
                                          rs=execSql(conSql.sentence,conn,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,procReturnMap,resources,locale);
                                          if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
                                          {
                                              return rs;
                                          }

                                      }
                                      else if(conSqlType.equals("procedure"))
                                      {
                                          rs=execProcedure(conSql.sentence,conn,tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,procReturnMap,resources,locale);
                                          if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS)
                                          {
                                              return rs;
                                          }

                                      }
                                 }

                             }
                       }
                       rs.setRetVal(flag);
                   }
                }
            }
        }catch(Exception ex)
        {
            BaseEnv.log.error("DefineSQLBean execute() ", ex);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ex.getMessage());
        }
        return rs;
    }
    class DefSql
    {
        String type;
        String sentence;
        ArrayList condition;
        ArrayList execs;

        public DefSql(String type, String sentence, ArrayList condition,ArrayList execs) {
           this.type=type;
           this.sentence=sentence;
           this.condition = condition;
           this.execs=execs;
       }
       public void setExecs(ArrayList execs)
       {
           this.execs=execs;
       }
       public void setCondition(ArrayList condition)
       {
           this.condition=condition;
       }
       public void setSentence(String sentence)
       {
           this.sentence=sentence;
       }
       public void setType(String type)
       {
           this.type=type;
       }
       public String getType()
       {
           return type;
       }
       public String getSentence()
       {
           return sentence;
       }
       public ArrayList getCondition()
       {
           return condition;
       }
       public ArrayList getExecs()
       {
           return execs;
       }


    }
    
    class DefExec{
        String condition;
        String loopTimes;
        String error;
        String sucess ;
        String testReturn;
        ArrayList defSqls;
        ConfirmBean confirm;
        
        public DefExec(String condition,String error,String sucess,ArrayList defSqls,ConfirmBean confirm,String loopTimes){
            this.condition=condition;
            this.error=error;
            this.defSqls=defSqls;
            this.confirm=confirm;
            this.sucess=sucess;
            this.loopTimes = loopTimes;
        }
    }


    public String getName() {
        return name;
    }

    public ArrayList getTableParams() {
        return tableParams;
    }

    public ArrayList getSqlList() {
        return sqlList;
    }

    public ArrayList getSessParams() {
        return sessParams;
    }

    public ArrayList getCodeParams() {
        return codeParams;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTableParams(ArrayList tableParams) {
        this.tableParams = tableParams;
    }

    public void setSqlList(ArrayList sqlList) {
        this.sqlList = sqlList;
    }

    public void setSessParams(ArrayList sessParams) {
        this.sessParams = sessParams;
    }

    public void setCodeParams(ArrayList codeParams) {
        this.codeParams = codeParams;
    }
    public Boolean getBooleanCondtition(String conditions) {
         Boolean flag = false;
        try
        {
                        //判断条件是否成立
                        BSFManager evalM=BaseEnv.evalManager;
                        BaseEnv.log.debug("exeCondition:"+conditions);
                         flag=(Boolean)evalM.eval("javascript", "XX", 0, 0, conditions);


        }catch(Exception ex)
        {
            BaseEnv.log.error("DefineSQLBean execute() ", ex);
        }
        return flag;
   }
    
    /**
     * define执行java class方法, 要求传入的参数，都是string 类型，数据库连接参数固定为conn
     * @param sentence
     * @param conn
     * @param tableParamMap
     * @param sysParamMap
     * @param queryParamMap
     * @param sessParamMap
     * @param codeParamMap
     * @param proceReturnMap
     * @param resources
     * @param locale
     * @return
     */
    private Result execInputType(String sentence,Connection conn ,HashMap tableParamMap,
            HashMap sysParamMap,HashMap queryParamMap,HashMap sessParamMap,
            HashMap codeParamMap,HashMap proceReturnMap,MessageResources resources,Locale locale)
    {
    	Result rs=new Result();
        try
        {
        	if(sentence.indexOf("=") > 0){
        		ArrayList list = (ArrayList)tableParamMap.get("DEFINE_INPUTTYPE");
        		if(list==null){
        			list= new ArrayList();
        			tableParamMap.put("DEFINE_INPUTTYPE",list);
        		}
        		String[] ss=sentence.split(",|;");
        		for(String s:ss){
        			if(s.trim().length() >0){
        				list.add(s.trim());
        			}
        		}
        	}
        }catch(Exception ex)
        {
        	BaseEnv.log.error("DefineSQLBean.execInputType Error",ex);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ex.getMessage());
        }
    	return rs;
    }
    
    /**
     * define执行java class方法, 要求传入的参数，都是string 类型，数据库连接参数固定为conn
     * @param sentence
     * @param conn
     * @param tableParamMap
     * @param sysParamMap
     * @param queryParamMap
     * @param sessParamMap
     * @param codeParamMap
     * @param proceReturnMap
     * @param resources
     * @param locale
     * @return
     */
    private Result execClass(String sentence,Connection conn ,HashMap tableParamMap,
            HashMap sysParamMap,HashMap queryParamMap,HashMap sessParamMap,
            HashMap codeParamMap,HashMap proceReturnMap,MessageResources resources,Locale locale)
    {
    	Result rs=new Result();
        try
        {	
        	
        	String methodName = sentence.substring(0,sentence.indexOf("("));
        	String className = methodName.substring(0,methodName.lastIndexOf(".")).trim();
        	methodName = methodName.substring(methodName.lastIndexOf(".")+1).trim();
        	Class eclass = Class.forName(className);
        	if(eclass == null){
        		rs.retCode = ErrorCanst.DEFAULT_FAILURE;
        		rs.retVal = "类名不存在";
        		return rs;
        	}
        	
        	String []paramsValue=sentence.substring(sentence.indexOf("(")+1,sentence.lastIndexOf(")")).split(",");
        	Class paramsClass[] = new Class[paramsValue.length];
        	Object[] paramsObj = new Object[paramsValue.length];
        	String paramclassStr = "";
        	String paramobjStr = "";
            for(int i=0;i<paramsValue.length;i++)
            {
            	if("conn".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = conn;
            		paramsClass[i] = Connection.class;
            		paramclassStr += "Connection,";
            		paramobjStr += "conn,";
            	}else if("tableParamMap".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = tableParamMap;
            		paramsClass[i] = HashMap.class;
            		paramclassStr += "HashMap,";
            		paramobjStr += "tableParamMap,";
            	}else if("sysParamMap".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = sysParamMap;
            		paramsClass[i] = HashMap.class;
            		paramclassStr += "HashMap,";
            		paramobjStr += "sysParamMap,";
            	}else if("queryParamMap".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = queryParamMap;
            		paramsClass[i] = HashMap.class;
            		paramclassStr += "HashMap,";
            		paramobjStr += "queryParamMap,";
            	}else if("sessParamMap".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = sessParamMap;
            		paramsClass[i] = HashMap.class;
            		paramclassStr += "HashMap,";
            		paramobjStr += "sessParamMap,";
            	}else if("codeParamMap".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = codeParamMap;
            		paramsClass[i] = HashMap.class;
            		paramclassStr += "HashMap,";
            		paramobjStr += "codeParamMap,";
            	}else if("proceReturnMap".equalsIgnoreCase(paramsValue[i].trim())){
            		paramsObj[i] = proceReturnMap;
            		paramsClass[i] = HashMap.class;
            		paramclassStr += "HashMap,";
            		paramobjStr += "proceReturnMap,";
            	}else{
            		String tempk=this.parseSentencePutParam(paramsValue[i],tableParamMap,sysParamMap,queryParamMap,sessParamMap,codeParamMap,
                		proceReturnMap, resources, locale,false);
            		paramsObj[i] = tempk.trim();
            		paramsClass[i] = String.class;
            		paramclassStr += "String,";
            		paramobjStr += tempk.trim()+",";
            	}
                
            }
        	if(paramclassStr.length() > 0){
        		paramclassStr = paramclassStr.substring(0,paramclassStr.length()  -1);
        		paramobjStr = paramobjStr.substring(0,paramobjStr.length()  -1);
        	}
        	
        	Method method =eclass.getMethod(methodName,paramsClass);
        	if(method == null){
        		rs.retCode = ErrorCanst.DEFAULT_FAILURE;        		
        		rs.retVal = "方法不存在"+className+"."+method+"("+paramclassStr+")";
        		return rs;
        	}
        	
        	BaseEnv.log.debug("自定义执行的语句显示： " + name + ": 开始执行java方法"+className+"."+methodName+"("+paramobjStr+")");
        	
        	//初始化类
        	Object obj = eclass.newInstance();
        	Object res = method.invoke(obj, paramsObj);
        	
        	if(res != null && res instanceof Result){
        		rs = (Result)res;
        		BaseEnv.log.debug("自定义执行的语句显示： " + name + ": 执行java方法完毕返回：Result类型，retCode="+rs.retCode + ",retVal="+rs.retVal);
        	}else if(res == null){
        		BaseEnv.log.debug("自定义执行的语句显示： " + name + ": 执行java方法完毕返回：void");
        	}else{
        		BaseEnv.log.debug("自定义执行的语句显示： " + name + ": 执行java方法完毕返回："+res.getClass().getName()+"类型:"+res.toString());
        	}
        }catch(InvocationTargetException ix){
        	BaseEnv.log.error("DefineSQLBean.execClass Error",ix);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ix.getTargetException().getMessage());
        }
        catch(Exception ex)
        {
        	BaseEnv.log.error("DefineSQLBean.execClass Error",ex);
            rs.setRetCode(ErrorCanst.RET_DEFINE_SENTENCE_ERROR);
            rs.setRetVal(ex.getMessage());
        }
    	return rs;
    }
}
