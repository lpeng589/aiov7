package com.menyi.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.Key;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.WorkProfessionBean;
import com.koron.oa.bean.OAMyCalendar;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.workflow.ReadXML;
import com.menyi.aio.bean.AIOShopBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.GoodsAttributeBean;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.aioshop.AIOShopMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.upgrade.RegisterMgt;
import com.menyi.aio.web.upgrade.UpgradeMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.sms.setting.SMSsetMgt;
/**
*
* <p>Title: 初始化内存数据</p>
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
public class InitMenData extends AIODBManager {
    public static long ENUMERATION, MODULELIST, TABLEINFO, SYSTEMSET, PROPLIST,EMPLOYEE;
    private static FileLock flock=null;
    
    /**
     * 获取语言
     * @param ls String
     * @return Locale
     */
    private Locale getLocale(String ls) {
        Locale locale = Locale.getDefault();
        if (ls != null && ls.length() != 0) {
            String[] params = ls.split("_");
            if (params.length == 3) {
                locale = new Locale(params[0], params[1], params[2]);
            } else if (params.length == 2) {
                locale = new Locale(params[0], params[1]);
            } else {
                locale = new Locale(params[0]);
            }
        }
        return locale;
    }

    public boolean initLanguage(ServletContext servletContext) {

        Hashtable table = new Hashtable();
        BaseEnv.localeTable = table;
        servletContext.setAttribute("LocaleTable", table);
        
        //取系统默认语言
        String locStr = BaseEnv.systemSet.get("defaultlanguage").getSetting();
        Locale defloc = getLocale(locStr);
        servletContext.setAttribute("DefaultLocale", defloc);
        BaseEnv.defaultLocale = defloc;
        
        UpgradeMgt infoMgt = new UpgradeMgt();
        Result rs = infoMgt.queryLanuage();
        if(rs.retVal != null && ((ArrayList)rs.retVal).size() > 0){
        	//有系统配置的语言数
        	
        	for(int i=0;rs.retVal != null && i<((ArrayList)rs.retVal).size();i++){
        		Object o = ((ArrayList)rs.retVal).get(i);
        		String name =((Object[])o)[0].toString();
        		table.put(name, getLocale(name));
        	}
        }else{
        	//未经过系统配置，取系统默认语言        	
        	ArrayList<String> list = new ArrayList<String>();
        	try{
        	BufferedReader br = new BufferedReader(new InputStreamReader(new
                    FileInputStream("../../config/language.xml")));

            String str;
            while ((str = br.readLine()) != null) {
                if (str.indexOf("<language>") > -1 &&
                    str.indexOf("</language>") > -1) {
                    String name = str.substring(str.indexOf("<language>") +
                                                "<language>".length(),
                                                str.indexOf("</language>"));
                    list.add(name);
                }
            }
        	}catch(Exception e){}
        	
        	//先加入默认语言
        	table.put(locStr, defloc);
        	for(String s:list){
        		if (!s.equals(locStr) && table.size() < SystemState.instance.getLanguageNum()){
        			table.put(s, getLocale(s));
        		}
        	}        	
        }
        
        
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(new
//                    FileInputStream("../../config/language.xml")));
//
//            String str;
//            while ((str = br.readLine()) != null) {
//                if (str.indexOf("<usedLanguage>") > -1 &&
//                    str.indexOf("</usedLanguage>") > -1) {
//                    String name = str.substring(str.indexOf("<usedLanguage>") +
//                                                "<usedLanguage>".length(),
//                                                str.indexOf("</usedLanguage>"));
//                    Locale loc = getLocale(name);
//                    //不能超过加密狗最大数量
//                    if (table.size() < SystemState.instance.getLanguageNum())
//                        table.put(name, loc);
//                }
////                else if (str.indexOf("<defLanguage>") > -1 &&
////                           str.indexOf("</defLanguage>") > -1) {
////                    String name = str.substring(str.indexOf("<defLanguage>") +
////                                                "<defLanguage>".length(),
////                                                str.indexOf("</defLanguage>"));
////                    Locale loc = getLocale(name);
////                    servletContext.setAttribute("DefaultLocale", loc);
////                    BaseEnv.defaultLocale = loc;
////                }
//            }
//        } catch (Exception ex) {
//            BaseEnv.log.error("------initLanguage Error ", ex);
//            return false;
//        }

        return true;
    }

    public Result initDBInformation(ServletContext servletContext,final String tableName) {
        final Hashtable map = new Hashtable();
        //获取用户自定义列名信息
        final Hashtable<String, ColDisplayBean> userLanguage = (Hashtable<String,ColDisplayBean>) servletContext.getAttribute("userLanguage");
        final Hashtable<String, ColDisplayBean> userSettingWidth = (Hashtable<String, ColDisplayBean>) servletContext.getAttribute("userSettingWidth");
        //为有效提升启动速度，这里使用jdbc初始化数据
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws SQLException {
                        Connection conn = connection;
                        KRLanguageQuery krQuery = new KRLanguageQuery();

                        HashMap tinfoMapById = new HashMap();  //以表ID做mapKey方便其它表的索引
                        HashMap tfieldMapById = new HashMap(); //以表ID做mapKey方便其它表的索引

                        //查主表
                        String querysql = " select * from tblDBTableInfo order by TableName ";
                        if(tableName!=null && tableName.trim().length()>0){
                        	querysql = " select * from tblDBTableInfo where tableName = '"+tableName+"'";
                        }
                        try {
                        	 //执行加载前修改系统类型的输入类型
                            List sysTypeList = ((EnumerateBean) BaseEnv.enumerationMap.get("FieldSysType")).getEnumItem();
                            for (Object obj : sysTypeList) {
                                EnumerateItemBean item = (EnumerateItemBean)obj;
                                if("RowMarker".equals(item.getEnumValue())){ //行标识字段不需处理输入类型
                                	continue;
                                }
                                boolean sysBool = BaseEnv.systemSet.get(item.getEnumValue())==null?true:Boolean.parseBoolean(BaseEnv.systemSet.get(item.getEnumValue()).getSetting());
                                String updateSql = "";
                                
                                //如果是启动状态，检查之前这些类型是否是启用的,如果不是查看已经做了列配置的表中是否存在，如果不存在要加入
                                if(sysBool){
                                	updateSql="select a.tableName,b.fieldName,b.inputTypeOld,b.isNull from tblDBTableInfo a, tblDBFieldInfo b where a.id=b.tableId and fieldSysType='"
                                		+item.getEnumValue()+"' and (inputType=100 or inputType=3) and inputTypeOld !=100 and tableName in (select tableName from tblColConfig where colType='bill'  group by tableName)";
                                	Statement st=conn.createStatement();
                                	ResultSet rs=st.executeQuery(updateSql);
                                	ArrayList list=new ArrayList();
                                	while(rs.next()){
                                		list.add(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)});
                                	}
                                	st=conn.createStatement();
                                	for(int i=0;i<list.size();i++){
                                		String[] str=(String[])list.get(i); 
                                		st.addBatch("if not exists (select * from tblColConfig where colName='"+(str[2].equals("2")?"@TABLENAME.":"")+str[1]+"' and tableName='"+str[0]+"' and colType='bill') " +
                                				"begin insert into tblColConfig (id,colName,tableName,colType,colIndex,createBy,statusId,isNull,lock,fieldName,display) "+
                                				"values ('"+IDGenerater.getId()+"','"+(str[2].equals("2")?"@TABLENAME.":"")+str[1]+"','"+str[0]+"','bill','100','1','0','"+str[3]+"',0,'"+str[1]+"','"+(str[2].equals("2")?"@TABLENAME.":"")+str[1]+"') end");
                                	}
                                	st.executeBatch();
                                }
                                
                                if ("GoodsField".equals(item.getEnumValue())) {                                	
                                    if (!sysBool) {
                                        updateSql ="update tblDBFieldInfo set inputType=100 where fieldSysType = ?";
                                        PreparedStatement psUpdate = conn.
                                        prepareStatement(updateSql);
                                        psUpdate.setString(1, item.getEnumValue());
                                        psUpdate.executeUpdate();
                                    }else{
                                    	String sql="select propName,isUsed,* from tblGoodsAttribute";
                                    	Statement st=conn.createStatement();
                                    	ResultSet rs=st.executeQuery(sql);
                                    	List list=new ArrayList();
                                    	while(rs.next()){
                                    		list.add(new String[]{rs.getString(1),rs.getString(2)});
                                    	}
                                    	for(int i=0;i<list.size();i++){
                                    		String [] str=(String[])list.get(i);
                                    		if(str[1].equals("1")){
                                    			updateSql ="update tblDBFieldInfo set inputType=isnull(inputTypeOld,0) where fieldName ='"+str[0]+"' and fieldSysType='GoodsField'";
                                    		}else{
                                    			updateSql ="update tblDBFieldInfo set inputType=3 where fieldName = '"+str[0]+"' and fieldSysType='GoodsField'";
                                    		}
                                    		st.execute(updateSql);
                                    	}
                                    }
                                } else {
                                    if (sysBool) {
                                        updateSql = "update tblDBFieldInfo set inputType=isnull(inputTypeOld,0) where fieldSysType = ?";
                                    } else {
                                    	//很多根据系统参数是否显示的字段还需要在界面触发公式，如果改为不输入，无法触发公式
                                    	if(item.getEnumValue().equals("BillDiscount")||item.getEnumValue().equals("BillTax")||item.getEnumValue().equals("BillPresentSampleType")||item.getEnumValue().equals("ManyBillQuote")||item.getEnumValue().equals("SettleQty")||item.getEnumValue().equals("SalesCombined")
                                    			||item.getEnumValue().equals("CashBuy")||item.getEnumValue().equals("CashSale")||item.getEnumValue().equals("currency")){
                                    		updateSql = "update tblDBFieldInfo set inputType=3 where fieldSysType = ?";
                                    	}else{
                                    		updateSql = "update tblDBFieldInfo set inputType=100 where fieldSysType = ?";
                                        }
                                    }
                                    PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                                    psUpdate.setString(1, item.getEnumValue());
                                    psUpdate.executeUpdate();
                                }
                            }

                            PreparedStatement cs = conn.prepareStatement(querysql);
                            BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            while (rset.next()) {
                                DBTableInfoBean tableInfoBean = new DBTableInfoBean();
                                tableInfoBean.setIsUsed(rset.getInt("isUsed"));
                                tableInfoBean.setApproveField(rset.getString("ApproveField"));
                                tableInfoBean.setApproveFlow(rset.getString("ApproveFlow"));
                                tableInfoBean.setClassFlag(rset.getByte("ClassFlag"));
                                tableInfoBean.setClassCount(rset.getInt("classCount"));
                                tableInfoBean.setCreateBy(rset.getString("CreateBy"));
                                tableInfoBean.setCreateTime(rset.getString("CreateTime"));
                                tableInfoBean.setDraftFlag(rset.getByte("DraftFlag"));
                                tableInfoBean.setExtendButton(rset.getString("ExtendButton"));
                                tableInfoBean.setFieldCalculate(rset.getString("FieldCalculate"));
                                tableInfoBean.setFieldInfos(new ArrayList());
                                tableInfoBean.setId(rset.getString("Id"));
                                tableInfoBean.setLastUpdateBy(rset.getString("LastUpdateBy"));
                                tableInfoBean.setLastUpdateTime(rset.getString("LastUpdateTime"));
                                tableInfoBean.setPerantTableName(rset.getString("PerantTableName"));
                                tableInfoBean.setTableName(rset.getString("TableName"));
                                tableInfoBean.setTableType(rset.getByte("TableType"));
                                tableInfoBean.setUdType(rset.getByte("UdType"));
                                tableInfoBean.setUpdateAble(rset.getByte("UpdateAble"));
                                tableInfoBean.setDefRowCount(rset.getInt("DefRowCount"));
                                tableInfoBean.setSysParameter(rset.getString("SysParameter"));

                                //加载是否基础信息，及是否需要复制，是否分支机构共享
                                tableInfoBean.setIsBaseInfo(rset.getInt("isBaseInfo"));
                                tableInfoBean.setNeedsCopy(rset.getInt("needsCopy"));
                                tableInfoBean.setWakeUp(rset.getInt("wakeUp"));
                                tableInfoBean.setHasNext(rset.getInt("hasNext")) ;
                                tableInfoBean.setIsView(rset.getInt("isView")) ;
                                tableInfoBean.setTableSysType(rset.getString("tableSysType")) ;
                                tableInfoBean.setIsSunCmpShare(rset.getInt("isSunCmpShare"));
                                tableInfoBean.setLanguageId(rset.getString("languageId"));
                                map.put(tableInfoBean.getTableName(),tableInfoBean);
                                tinfoMapById.put(tableInfoBean.getId(),tableInfoBean);
                                krQuery.addLanguageId(tableInfoBean.getLanguageId());
                                tableInfoBean.setTriggerExpress(rset.getInt("triggerExpress"));
                                tableInfoBean.setRelationTable(rset.getString("relationTable"));
                                tableInfoBean.setRelationView(rset.getString("relationView"));
                                tableInfoBean.setTableDesc(rset.getString("tableDesc"));
                                tableInfoBean.setIsLayout(rset.getInt("isLayout")) ;
                                tableInfoBean.setLayoutHTML(rset.getString("layoutHTML")) ;
                                tableInfoBean.setIsNull(rset.getInt("isNull"));
                                tableInfoBean.setReAudit(rset.getByte("reAudit"));
                                tableInfoBean.setMainModule(rset.getString("MainModule"));
                                tableInfoBean.setTWidth(rset.getInt("tWidth"));
                                tableInfoBean.setTHeight(rset.getInt("tHeight"));
                                tableInfoBean.setBrotherType(rset.getInt("BrotherType"));
                                tableInfoBean.setCopyParent(rset.getInt("copyParent"));
                            }

                            //设置当前表所对应的视图信息，便于在使用的时候直接取数据
                            Iterator iterator=map.values().iterator();
                            while(iterator.hasNext())
                            {
                            	DBTableInfoBean tableInfoBean=(DBTableInfoBean)iterator.next();
                            	Iterator iteratorV=map.values().iterator();
                            	while(iteratorV.hasNext()){
                            		DBTableInfoBean tableInfoBeanV=(DBTableInfoBean)iteratorV.next();
                            		if(tableInfoBeanV.getRelationTable()!=null&&tableInfoBeanV.getRelationTable().equals(tableInfoBean.getTableName())){
                            			tableInfoBean.setRelationView(tableInfoBean.getRelationView()+";"+tableInfoBeanV.getTableName()+";");
                            		}
                            	}
                            }

                            //查所有字段信息
                            querysql = "select * from tblDBFieldInfo order by tableId,listOrder";
                            if(tableName!=null && tableName.trim().length()>0){
                            	querysql = "select * from tblDBFieldInfo where tableId = (select id from tblDBTableInfo where tableName='"+tableName+"') order by tableId,listOrder";
                            }
                            cs = conn.prepareStatement(querysql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
                            BaseEnv.log.debug(querysql);
                            rset = cs.executeQuery();
                            while (rset.next()) {
                                DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
                                fieldInfo.setCalculate(rset.getString("Calculate"));
                                fieldInfo.setDefaultValue(rset.getString("DefaultValue"));
                                fieldInfo.setFieldName(rset.getString("FieldName").trim());
                                fieldInfo.setFieldType(rset.getByte("FieldType"));
                                fieldInfo.setDigits(rset.getByte("digits"));
                                fieldInfo.setId(rset.getString("Id"));
                                fieldInfo.setInputType(rset.getByte("InputType"));
                                fieldInfo.setInputTypeOld(rset.getByte("InputTypeOld"));
                                fieldInfo.setInputValue(rset.getString("InputValue"));
                                fieldInfo.setIsNull(rset.getByte("IsNull"));
                                fieldInfo.setIsStat(rset.getByte("IsStat"));
                                fieldInfo.setIsCopy(rset.getByte("isCopy"));
                                fieldInfo.setCopyType(rset.getString("copyType")) ;
                                fieldInfo.setIsUnique(rset.getByte("IsUnique"));
                                fieldInfo.setListOrder(rset.getByte("ListOrder"));
                                fieldInfo.setMaxLength(rset.getInt("MaxLength"));
                                fieldInfo.setRefEnumerationName(rset.getString("RefEnumerationName"));
                                String tableId = rset.getString("tableId");
                                fieldInfo.setUdType(rset.getByte("UdType"));
                                fieldInfo.setIsReAudit(rset.getByte("isReAudit"));
                                fieldInfo.setStatusId(rset.getInt("statusId"));
                                fieldInfo.setIsLog(rset.getByte("isLog"));
                                fieldInfo.setIsMobile(rset.getByte("isMobile"));
                                DBTableInfoBean tableInfo = (DBTableInfoBean)tinfoMapById.get(tableId);
                                if(tableInfo == null){
                                	BaseEnv.log.error("InitMenData.initDBInformation 字段对应的表不存在 "+fieldInfo.getFieldName() +";id = "+fieldInfo.getId() );
                                	continue;
                                }
                                
                                if (userSettingWidth != null && userSettingWidth.size() > 0) {
                                	if(tableInfo!=null){
	                                    ColDisplayBean colBean = userLanguage.get(tableInfo.getTableName() +fieldInfo.getFieldName());
	                                    if (colBean != null && colBean.getColWidth() != null &&
	                                        colBean.getColWidth().length() > 0) {
	                                        fieldInfo.setWidth(Integer.parseInt(colBean.getColWidth()));
	                                    } else {
	                                        fieldInfo.setWidth(rset.getInt("Width"));
	                                    }
                                	}else{
                                		fieldInfo.setWidth(rset.getInt("Width"));
                                	}
                                } else {
                                    fieldInfo.setWidth(rset.getInt("Width"));
                                }

                                fieldInfo.setFieldSysType(rset.getString("fieldSysType"));
                                fieldInfo.setFieldIdentityStr(rset.getString("fieldIdentityStr"));
                                fieldInfo.setLogicValidate(rset.getString("logicValidate"));
                                fieldInfo.setPopupType(rset.getString("popupType")) ;
                                if (userLanguage != null && userLanguage.size() > 0) {
	                                if(tableInfo!=null){
	                                    ColDisplayBean colBean = userLanguage.get(
	                                    		tableInfo.getTableName() +fieldInfo.getFieldName());
	                                    if (colBean != null &&
	                                        colBean.getLanguageId() != null &&
	                                        colBean.getLanguageId().length() > 0) {
	                                        fieldInfo.setLanguageId(colBean.getLanguageId());
	                                    } else {
	                                        fieldInfo.setLanguageId(rset.getString("languageId"));
	                                    }
                                	}else{
                                		fieldInfo.setLanguageId(rset.getString("languageId"));
                                	}
                                } else {
                                    fieldInfo.setLanguageId(rset.getString("languageId"));
                                }
                                fieldInfo.setGroupName(rset.getString("groupName"));
                                fieldInfo.setInsertTable(rset.getString("insertTable"));
                                
                                fieldInfo.setTableBean(tableInfo);
                                tableInfo.getFieldInfos().add(fieldInfo);
                                tfieldMapById.put(fieldInfo.getId(), fieldInfo);
                                krQuery.addLanguageId(fieldInfo.getLanguageId());
                                if(fieldInfo.getGroupName() != null && fieldInfo.getGroupName().length() > 0){
                                	krQuery.addLanguageId(fieldInfo.getGroupName());
                                }
                            }
                            //多语言执行
                            HashMap krLanguageMap = krQuery.query(conn);
                            Iterator itar = map.values().iterator();
                            while (itar.hasNext()) {
                                DBTableInfoBean dTInfo = (DBTableInfoBean) itar.next();
                                if(krLanguageMap.get(dTInfo.getLanguageId())!=null){
                                	dTInfo.setDisplay((KRLanguage) krLanguageMap.get(dTInfo.getLanguageId()));
                                }else{
                                	KRLanguage kl=new KRLanguage();
                                	kl.putLanguage("zh_CN",dTInfo.getTableName());
                                	kl.putLanguage("en",dTInfo.getTableName());
                                	kl.putLanguage("zh_TW",dTInfo.getTableName());
                                	dTInfo.setDisplay(kl);
                                }
                                
                                //找到跟此表关联的其他表的字段
                                ArrayList<DBFieldInfoBean> relatFields=new ArrayList<DBFieldInfoBean>();
                                relatFields.addAll(dTInfo.getFieldInfos());
                                if(dTInfo.getTableType()!=0 && dTInfo.getPerantTableName()!=null){
                            		String[] pts=dTInfo.getPerantTableName().split(";");
                            		for(int i=0;i<pts.length;i++){
                            			DBTableInfoBean mainT=(DBTableInfoBean)map.get(pts[i]);
                            			if(mainT!=null){
                            				relatFields.addAll(mainT.getFieldInfos());
                            			}
                            		}
                            	}else{
                            		ArrayList<DBTableInfoBean> ct=DDLOperation.getChildTables(dTInfo.getTableName(), map);
                            		for(int i=0;i<ct.size();i++){
                            			relatFields.addAll(ct.get(i).getFieldInfos());
                            		}
                            	}
                                
                                for (int ii = 0;ii < dTInfo.getFieldInfos().size();ii++) {
                                    DBFieldInfoBean dFInfo = (DBFieldInfoBean)
                                            dTInfo.getFieldInfos().get(ii);
                                    if(krLanguageMap.get(dFInfo.getLanguageId())!=null){
                                    	dFInfo.setDisplay((KRLanguage)krLanguageMap.get(dFInfo.getLanguageId()));
                                    }else{
                                    	KRLanguage kl=new KRLanguage();
                                    	kl.putLanguage("zh_CN",dFInfo.getFieldName());
                                    	kl.putLanguage("en",dFInfo.getFieldName());
                                    	kl.putLanguage("zh_TW",dFInfo.getFieldName());
                                    	dFInfo.setDisplay(kl);
                                    }
                                    dFInfo.setGroupDisplay((KRLanguage)
                                            krLanguageMap.get(dFInfo.
                                            getGroupName()));
                                    //去掉不输入输入类型字段的公式
                                    if(dFInfo.getInputType()==100){
                                    	String fieldName=dFInfo.getFieldName();
                                        
                                        if(dFInfo.getTableBean().getTableType()!=0){
                                        	fieldName=dFInfo.getTableBean().getTableName()+"_"+fieldName;
                                        }
                                    	for (int jj = 0;jj < relatFields.size();jj++) {
                                            DBFieldInfoBean dFInfoc = (DBFieldInfoBean)relatFields.get(jj);                                            
                                            if(dFInfoc.getCalculate()!=null&&dFInfoc.getCalculate().contains("{"+fieldName+"}")){
                                            	String calculate=dFInfoc.getCalculate();
                                            	String temp="";
                                            	while(calculate.length()>0&&calculate.contains(fieldName)){
                                            		int indexs=calculate.substring(0,calculate.indexOf(fieldName)).lastIndexOf(";");
                                            		int indexe=calculate.indexOf(";",calculate.indexOf(fieldName));
                                            		temp+=calculate.substring(0,indexs==-1?0:indexs+1);
                                            		calculate=indexe==-1?"":calculate.substring(indexe+1);
                                            	}
                                            	dFInfoc.setCalculate(temp+calculate);
                                            }
                                        }
                                    }
                                }
                            }
                            

                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initDBInformation :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);

        BaseEnv.log.debug("------Init TableInfo record:" + map.size());
        if(tableName!=null && tableName.trim().length()>0){
        	Hashtable table = (Hashtable) servletContext.getAttribute(BaseEnv.TABLE_INFO);
        	table.put(tableName, map.get(tableName));
        	Hashtable table2 = BaseEnv.tableInfos ;
        	table2.put(tableName,map.get(tableName));
        }else{
        	servletContext.setAttribute(BaseEnv.TABLE_INFO, map);
        	BaseEnv.tableInfos = map;
        }
        //这里同时初始化公共公式
        DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        KRLanguageQuery krQuery = new KRLanguageQuery();
                        //查主表
                        String querysql ="select [calkey],[calculate] from [tblpupCalculate]";
                        try {
                            PreparedStatement cs = conn.prepareStatement(
                                    querysql);
                            ResultSet rset = cs.executeQuery();
                            HashMap map = new HashMap();
                            while (rset.next()) {
                            	map.put(rset.getString(1), rset.getString(2));                            	
                            } 
                            BaseEnv.pupCalculate=map;
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initDBInformation :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });        
        
        return res;
    }
    
    private Result initAccPeriod() {
        final HashMap<String,AccPeriodBean> accMap = new HashMap<String,AccPeriodBean>();
        final HashMap<String,AccPeriodBean> beginMap = new HashMap<String,AccPeriodBean>();
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        //查主表
                        String querysql ="select AccPeriod,AccMonth,AccYear,scompanyId,statusId,isBegin from tblAccPeriod where statusId=1 or isBegin=1";
                        try {
                            PreparedStatement cs = conn.prepareStatement(querysql);
                            BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            while (rset.next()) {
                            	AccPeriodBean bean = new AccPeriodBean();
    							bean.setAccPeriod(rset.getInt(1));
    							bean.setAccMonth(rset.getInt(2));
    							bean.setAccYear(rset.getInt(3));
    							bean.setStatusId(rset.getInt(5));
    							bean.setIsBegin(rset.getInt(6));
    							if(bean.getStatusId()==1 && bean.getIsBegin()==1){
    								accMap.put(rset.getString(4), bean);
    								beginMap.put(rset.getString(4), bean) ;
    							}
    							if(bean.getStatusId() == 1){
    								accMap.put(rset.getString(4), bean);
    							}
    							if(bean.getIsBegin() == 1){
    								beginMap.put(rset.getString(4), bean) ;
    							}
                            }
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initDBInformation :" +querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);

        BaseEnv.log.debug("------Init initAccPeriod record:" + accMap.size());
        BaseEnv.accPerios = accMap;
        BaseEnv.beginPerios = beginMap ;
        return res;
    }
    
    private Result initScopeCataData(ServletContext servletContext) {
        final ArrayList list = new ArrayList();

        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        KRLanguageQuery krQuery = new KRLanguageQuery();

                        //查主表
                        String querysql =
                                " select cataName,disName,module,tableName,nameField,codeField from tblScopeCataData order by orderby ";

                        try {
                            PreparedStatement cs = conn.prepareStatement(
                                    querysql);
                            BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            while (rset.next()) {
                                Object []obj=new Object[7];
                                obj[0]=rset.getString(1);
                                obj[1]=rset.getString(2);
                                obj[2]=rset.getString(3);
                                krQuery.addLanguageId(obj[1].toString());
                                obj[4]=rset.getString(4);
                                obj[5]=rset.getString(5);
                                obj[6]=rset.getString(6);
                                list.add(obj);
                            }
                            HashMap krLanguageMap = krQuery.query(conn);
                            for(int i=0;i<list.size();i++){
                            	Object [] obj=(Object[])list.get(i);
                            	obj[3]=(KRLanguage) krLanguageMap.get(obj[1].toString());
                            }
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initDBInformation :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);

        BaseEnv.log.debug("------Init TableInfo record:" + list.size());
        servletContext.setAttribute("scopeCata", list);
        return res;
    }
    public Result initPopDisSen(ServletContext servletContext) {
        final HashMap list = new HashMap();

        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String querysql =
                                " select tableName,sentence from tblPopDisSentence where statusId=0 order by tableName ";

                        try {
                            PreparedStatement cs = conn.prepareStatement(
                                    querysql);
                            BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            while (rset.next()) {
                                list.put(rset.getString(1), rset.getString(2));
                            }

                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initDBInformation :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);

        BaseEnv.log.debug("------Init TableInfo record:" + list.size());
        servletContext.setAttribute("PopDisSen", list);
        BaseEnv.popDisSentence=list;
        return res;
    }
    public Result initModuleField(ServletContext servletContext) {
    	final HashMap<String, HashMap<String,ArrayList<String[]>>> map=new HashMap<String, HashMap<String,ArrayList<String[]>>>();
    	final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        String querysql = "select moduleId,fieldName,tableName,fieldDisplay,moduleTypeNum,readOnley from tblModuleField a,tblModuleFieldDet b where a.id=b.f_ref";
                        try {
                            PreparedStatement cs = conn.prepareStatement(querysql);
                            ResultSet rset = cs.executeQuery();
                            KRLanguageQuery klQuery = new KRLanguageQuery();
                            while (rset.next()) {
                            	String moduleId = rset.getString("moduleId") ;
                            	String fieldName = rset.getString("fieldName");
                            	String tableName = rset.getString("tableName");
                            	String languageId = rset.getString("fieldDisplay") ;
                            	String moduleType = rset.getString("moduleTypeNum") ;
                            	String readOnley = rset.getString("readOnley") ;
                            	
                            	String linkAddress = "/UserFunctionQueryAction.do?tableName="+moduleId+"&moduleType="+moduleType ;
                            	HashMap<String,ArrayList<String[]>> tableMap;
                            	ArrayList<String[]> fieldList;
                            	if(map.containsKey(linkAddress)){
                            		tableMap = map.get(linkAddress);
                            	}else{
                            		tableMap=new HashMap<String,ArrayList<String[]>>();
                            		map.put(linkAddress, tableMap);
                            	}
                            	if(tableMap.containsKey(tableName)){
                            		fieldList = tableMap.get(tableName);
                            	}else{
                            		fieldList = new ArrayList<String[]>();
                            		tableMap.put(tableName, fieldList);
                                }
                            	fieldList.add(new String[]{fieldName,readOnley != null && readOnley.indexOf("Yes")>-1?"true":"false",languageId});
                            	
                            }
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initModuleField :" +querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        BaseEnv.log.debug("------Init initModuleField record:" + map.size());
        servletContext.setAttribute("ModuleField", map);
        BaseEnv.ModuleField = map;
        
        final HashMap<String, KRLanguage> Modulemap=new HashMap<String, KRLanguage>();
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        String querysql = "select moduleId,moduleTypeNum,linkAddress from tblModuleField";
                        try {
                        	KRLanguageQuery klQuery = new KRLanguageQuery();
                            PreparedStatement cs = conn.prepareStatement(querysql);
                            ResultSet rset = cs.executeQuery();
                            List<String[]> list = new ArrayList();
                            while (rset.next()) {
                            	String moduleId = rset.getString("moduleId") ;
                            	String moduleTypeNum = rset.getString("moduleTypeNum");
                            	String linkAddress = rset.getString("linkAddress");
                            	klQuery.addLanguageId(linkAddress);
                            	list.add(new String[]{moduleId+"_"+moduleTypeNum,linkAddress});
                            }
                            HashMap<String, KRLanguage> lanMap = klQuery.query(conn);
                            for(String[] ss :list){
                            	Modulemap.put(ss[0], (KRLanguage) lanMap.get(ss[1]));
                            }
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error InitMenDate.initModuleField :" +querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        servletContext.setAttribute("ModuleTable", Modulemap);
        BaseEnv.ModuleTable = Modulemap;
        
        return res;
    }
    public Result initPropInformation(ServletContext servletContext) {
        Object prop = BaseEnv.tableInfos.get("tblGoodsPropInfo");
        final Hashtable map = new Hashtable();
        if (prop != null) {
            final Result rs = list(
                    " select bean from GoodsPropInfoBean as bean order by bean.orderBy",
                    new ArrayList());
            if (rs == null) {
                return rs;
            }

            final ArrayList list = (ArrayList) rs.retVal;
            final ArrayList propList=new ArrayList();
            final HashMap propIgnoreCaseMap = new HashMap();
            if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS &&
                ((ArrayList) rs.getRetVal()).size() > 0) {
                int retVal = DBUtil.execute(new IfDB() {
                    public int exec(final Session session) {
                        session.doWork(new Work() {
                            public void execute(Connection connection) throws
                                    SQLException {
                                try {
                                    KRLanguageQuery kquery = new
                                            KRLanguageQuery();
                                    for (int i = 0;
                                                 i <
                                                 ((ArrayList) rs.getRetVal()).
                                                 size();
                                                 i++) {
                                        GoodsPropInfoBean gib = (
                                                GoodsPropInfoBean) ((ArrayList)
                                                rs.getRetVal()).get(i);
                                        kquery.addLanguageId(gib.getLanguageId());
                                        for (int j = 0;
                                                j < gib.getEnumItem().size(); j++) {
                                            GoodsPropEnumItemBean geb = (
                                                    GoodsPropEnumItemBean) gib.
                                                    getEnumItem().get(j);
                                            kquery.addLanguageId(geb.
                                                    getLanguageId());
                                        }
                                    }
                                    HashMap hashTable = kquery.query(connection);
                                    for (int k = 0; k < list.size(); k++) {
                                        GoodsPropInfoBean bean = (
                                                GoodsPropInfoBean) list.get(k);
                                        map.put(bean.getPropName(), bean);
                                        propIgnoreCaseMap.put(bean.getPropName().toLowerCase(), bean);
                                        
                                        bean.setDisplay((KRLanguage) hashTable.
                                                get(bean.getLanguageId()));
                                        for (int i = 0;
                                                i < bean.getEnumItem().size();
                                                i++) {
                                            GoodsPropEnumItemBean eib = (
                                                    GoodsPropEnumItemBean) bean.
                                                    getEnumItem().get(i);
                                            eib.setDisplay((KRLanguage)
                                                    hashTable.get(eib.
                                                    getLanguageId()));
                                        }
                                        propList.add(bean);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                    return;
                                }
                            }
                        });
                        return ErrorCanst.DEFAULT_SUCCESS;
                    }
                });
            }
            BaseEnv.log.debug("------Init PropInfo record:" + map.size());
            BaseEnv.propList=propList;
            servletContext.setAttribute(BaseEnv.PROP_INFO, map);
            servletContext.setAttribute(BaseEnv.PROP_INFOL, propList);
            BaseEnv.propMap = map; 
            BaseEnv.propIgnoreCaseMap =propIgnoreCaseMap;
            
            return rs;
        }
        return null;
    }
    public Result initAttInformation(ServletContext servletContext) {
        Object prop = BaseEnv.tableInfos.get("tblGoodsAttribute");
        if (prop != null) {
            final Result rs = list(
                    " select bean from GoodsAttributeBean as bean order by bean.orderBy",
                    new ArrayList());
            if (rs == null) {
                return rs;
            }

            final ArrayList list = (ArrayList) rs.retVal;
            final ArrayList propList=new ArrayList();
            if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS &&
                ((ArrayList) rs.getRetVal()).size() > 0) {
                int retVal = DBUtil.execute(new IfDB() {
                    public int exec(final Session session) {
                        session.doWork(new Work() {
                            public void execute(Connection connection) throws
                                    SQLException {
                                try {
                                    KRLanguageQuery kquery = new
                                            KRLanguageQuery();
                                    for (int i = 0;
                                                 i <
                                                 ((ArrayList) rs.getRetVal()).
                                                 size();
                                                 i++) {
                                    	GoodsAttributeBean gib = (
                                    			GoodsAttributeBean) ((ArrayList)
                                                rs.getRetVal()).get(i);
                                        kquery.addLanguageId(gib.getLanguageId());
                                    }
                                    HashMap hashTable = kquery.query(connection);
                                    for (int k = 0; k < list.size(); k++) {
                                    	GoodsAttributeBean bean = (
                                    			GoodsAttributeBean) list.get(k);                                        
                                        bean.setDisplay((KRLanguage) hashTable.
                                                get(bean.getLanguageId()));
                                        propList.add(bean);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                    return;
                                }
                            }
                        });
                        return ErrorCanst.DEFAULT_SUCCESS;
                    }
                });
            }
            BaseEnv.attList=propList;
            return rs;
        }
        
        return null;
    }
    private Result dropPrintData(ServletContext servletContext) {
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
             public int exec(final Session session) {
                 session.doWork(new Work() {
                     public void execute(Connection conn) throws
                             SQLException {
                         try {
                         	CallableStatement cbs= conn.prepareCall("{call proc_dropPrint(?)}");
                         	cbs.setString(1, "");
                         	cbs.execute();
                         } catch (Exception ex) {
                             ex.printStackTrace();
                             rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                             return;
                         }
                     }
                 });
                 return ErrorCanst.DEFAULT_SUCCESS;
             }
         });
        return rs;
    }
    private Result initHolidayInfo(ServletContext servletContext) {
    	final ArrayList list=new ArrayList();
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try {
                           String sql="select holiday from tblHolidaySetting order by holiday";
                           Statement st=conn.createStatement();
                           ResultSet rst=st.executeQuery(sql);
                           while(rst.next()){
                        	   list.add(rst.getString(1));
                           }                           
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
    	rs.setRetCode(retCode);
    	BaseEnv.holidayInfo=list;
    	return rs;
    }

    public Result initEnumerationInformation() { 
        final Result rs = list(" select bean from EnumerateBean bean ",
                               new ArrayList());
        if (rs == null) {
            return rs;
        }
        final Hashtable map = new Hashtable();
        final ArrayList list = (ArrayList) rs.retVal;

        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && list.size() > 0) {

            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                            final KRLanguageQuery klQuery = new KRLanguageQuery();
                            for (int k = 0; k < list.size(); k++) {
                                EnumerateBean bean = (EnumerateBean) list.get(k);
                                klQuery.addLanguageId(bean.getLanguageId());
                                for (int i = 0; i < bean.getEnumItem().size();
                                             i++) {
                                    EnumerateItemBean eib = (EnumerateItemBean)
                                            bean.getEnumItem().get(i);
                                    klQuery.addLanguageId(eib.getLanguageId());
                                }
                            }

                            HashMap klmap = klQuery.query(connection);

                            for (int k = 0; k < list.size(); k++) {
                                EnumerateBean bean = (EnumerateBean) list.get(k);
                                map.put(bean.getEnumName(), bean);
                                bean.setDisplay((KRLanguage) klmap.get(bean.
                                        getLanguageId()));
                                for (int i = 0; i < bean.getEnumItem().size();
                                             i++) {
                                    EnumerateItemBean eib = (EnumerateItemBean)
                                            bean.getEnumItem().get(i);
                                    eib.setDisplay((KRLanguage) klmap.get(eib.
                                            getLanguageId()));
                                }
                            }
                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }
        BaseEnv.log.debug("------Init Enumeration record:" + map.size());
        BaseEnv.enumerationMap = map;
        return rs;
    } 
    
    //存放所有的行业信息
    private Result initWorkProfessionBeanInformation(ServletContext servletContext) {
        final Result rs = list("from WorkProfessionBean",
                               new ArrayList());
        if (rs == null) {
            return rs;
        }
        final HashMap map = new HashMap();
        final ArrayList list = (ArrayList) rs.retVal;
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && list.size() > 0) {
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                        	for(int i=0;i<list.size();i++){
                        		WorkProfessionBean bean = (WorkProfessionBean)list.get(i);
                        		map.put(bean.getId(), bean.getName());
                        	}
                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }
        BaseEnv.log.debug("------Init WorkProfession record:" + map.size());
        BaseEnv.workProfessionMap = map;
        servletContext.setAttribute("workProfessionMap", map);
        return rs;
    }
    
    //存放所有的地区信息
    private Result initDistrictInformation(ServletContext servletContext) {
        final Result rs = sqlList("select classCode,districtfullName from tbldistrict",
                               new ArrayList());
        if (rs == null) {
            return rs;
        }
        final HashMap map = new HashMap();
    	final List<Object> list = (List<Object>)rs.retVal;
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && list.size() > 0) {
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                        	for(int i=0;i<list.size();i++){
                        		map.put(((Object[])list.get(i))[0].toString(), ((Object[])list.get(i))[1].toString());
                        	}
                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }
        BaseEnv.log.debug("------Init District record:" + map.size());
        BaseEnv.districtMap = map;
        servletContext.setAttribute("districtMap", map);
        return rs;
    }
    
    //存放所有的部门
    private Result initDeptInformation(ServletContext servletContext) {
        final Result rs = sqlList("select classCode,deptFullName from tbldepartment",
                               new ArrayList());
        if (rs == null) {
            return rs;
        }
        final HashMap map = new HashMap();
    	final List<Object> list = (List<Object>)rs.retVal;
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && list.size() > 0) {
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                        	for(Object obj : list){
                        		((Object[])obj)[0].toString();
                        		map.put(((Object[])obj)[0].toString(), ((Object[])obj)[1].toString());
                        	}
                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }
        BaseEnv.log.debug("------Init deptMap record:" + map.size());
        BaseEnv.deptMap = map;
        servletContext.setAttribute("deptMap", map);
        return rs;
    }

    private void initFunctionInterface() {
    	HashMap newMap = new HashMap();
    	if(BaseEnv.functionInterface.size()==0){ //这是只加载一次，如果有修改，必须重启服务器
	        java.util.Properties prop = new java.util.Properties();
	        try {
	            prop.load(this.getClass().getResourceAsStream(
	                    "/functionInterface.properties"));	        
		        java.util.Enumeration enume = prop.keys();
		        
		        while (enume.hasMoreElements()) {
		                String key = enume.nextElement().toString();
		                if (key.trim().length() > 0) {
		                    Class cs = this.getClass().getClassLoader().loadClass(prop.
		                            getProperty(key));
		                    Object o = cs.newInstance();
		                    if (o instanceof BaseCustomFunction) {
		                        newMap.put(key, o);
		                        BaseEnv.log.debug("------put BaseCustomFunction " + key +
		                                          "=" + prop.
		                                          getProperty(key));
		                    }
		                }
		            
		        }
	        } catch (Exception ex1) {
                BaseEnv.log.error("InitMenData.initFunctionInterface Error",ex1);
            }
    	}
        BaseEnv.functionInterface = newMap;
    }

    public Result initSystemSettingInformation() {
    	  /**查询系统设置**/
        Result rs = list(" select bean from SystemSettingBean bean ",
                         new ArrayList());
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null) {
            return rs;
        }
        Hashtable<String, SystemSettingBean>
                map = new Hashtable<String, SystemSettingBean>();
        ArrayList list = (ArrayList) rs.retVal;
        for (Object sysSet : list) {
            SystemSettingBean sysSetting = (SystemSettingBean) sysSet;
            map.put(sysSetting.getSysCode(), sysSetting);
        }
        BaseEnv.log.debug("------Init SystemSetting record:" + map.size());
        BaseEnv.systemSet = map;
        if(BaseEnv.systemSet.get("BOL88Adress") != null){
        	BaseEnv.bol88URL = BaseEnv.systemSet.get("BOL88Adress").getSetting().trim();
        	if(BaseEnv.bol88URL.endsWith("/")){
        		BaseEnv.bol88URL = BaseEnv.bol88URL.substring(0,BaseEnv.bol88URL.length() -1);
        	}
        }

        return rs;
    }
    
    private Result initSystemVersion() {
    	final Result rs = new Result();
		/**
		 * 查询软件版本号
		 */
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,TRADEINFO_NAME from tbltradeinfo";
							Statement cs = conn.createStatement();
							ResultSet rss = cs.executeQuery(sql);						
							if (rss.next()) {								
								BaseEnv.version = rss.getInt(1);
								BaseEnv.sysVersionName = rss.getString(2);
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								return;
							}
						} catch (Exception ex) {
						}
						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						return;
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
  }

    //根据系统参数类型，确定数据库中模块表系统类型是否启用，1为启用，2为禁用
    //属性1,电商2，下单4
    public Result initModuleSysType() {
        final Result rs = new Result();
        
    	final EnumerateBean eb = (EnumerateBean) BaseEnv.enumerationMap.get(
    	"MainModule");
    	
    	//未启用的模块要屏蔽	
		int retCode = DBUtil.execute(new IfDB() {
	        public int exec(Session session) {
	            session.doWork(new Work() {
	                public void execute(Connection connection) throws
	                        SQLException {
	                    Connection conn = connection;
	                    //按加密狗，屏蔽oa,crm，
	                    for (Object ebo : eb.getEnumItem()) {
	                		EnumerateItemBean ib = (EnumerateItemBean)ebo;
	                		if(!ib.getEnumValue().equals("0")){
	                			boolean found = false;
	            	    		for (Object mo : SystemState.instance.getModuleList()) {
	            	                String mstr = mo.toString();
	            	                if(ib.getEnumValue().equals(mstr)){
	            	                	found = true;
	            	                }
	            	    		}
	            	    		String sql ="update tblModules set " +
		                		"IsDisplay=" +(found ? "0" : "-1") +
			                	" where mainmodule='"+ib.getEnumValue()+"'  ";
	            	    		if("1".equals(ib.getEnumValue())){
					                sql +="or  linkaddress like '%tblBuyInStock%' "+
					                		"or  linkaddress like '%tblSalesOutStock%' "+
					                		"or  linkaddress like '%tblOtherOut%' "+
					                		"or  linkaddress like '%tblOtherIn%' ";
	            	    		}else if("2".equals(ib.getEnumValue())){
	            	    			sql +="or  (linkaddress like '/OAWorkFlow%' and linkaddress not like '/OAWorkFlowTempQueryAction%' ) " ;
	            	    		}else if("3".equals(ib.getEnumValue())){
	            	    			sql +="or  linkaddress like '/CRMClientAction%' " ;
		        	    		}else if("4".equals(ib.getEnumValue()) && BaseEnv.version!=12){
		        	    			sql +="or  linkaddress like '%HREmpinform%' " ;
		        	    		}
	            	    		try {
				                    PreparedStatement cs = conn.prepareStatement(sql);
				                    cs.executeUpdate();
				                } catch (Exception ex) {
				                }
	                		}
	                		
	                	}
	                }
	            });
	            return rs.getRetCode();
	        }
	    });
	    rs.setRetCode(retCode);
	    //---------------按系统配置时间屏蔽
        
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        List sysTypeList = ((EnumerateBean) BaseEnv.enumerationMap.get("FieldSysType")).getEnumItem();
                        for (Object obj : sysTypeList) {
                            EnumerateItemBean item = (EnumerateItemBean) obj;
                            if (!item.getEnumValue().equals("Normal")&& BaseEnv.systemSet.get(item.getEnumValue()) != null) {//当系统类型是普通时，不做任何修改
                            	
                                boolean boolSysType = Boolean.parseBoolean(
                                        BaseEnv.systemSet.get(item.getEnumValue()).getSetting());
                                //这里不能改display,因为这里即使设置为不启用，但后面的加密狗又可能把他改回来
                                final String sql ="update tblModules set IsUsed=" +
                                (boolSysType ? "1" : "2") +" where SystemParam='" +item.getEnumValue() + "'";

                                try {
                                    PreparedStatement cs = conn.prepareStatement(sql);
                                    //BaseEnv.log.debug(sql);
                                    cs.executeUpdate();
                                } catch (Exception ex) {
                                    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                    BaseEnv.log.error(
                                            "update tblModules Error :" + sql,
                                            ex);
                                    return;
                                }
                            }
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        
        


        return rs;
    }
    
    /**
     *  根据加密狗控制，确定数据库中模块表系统类型是否启用，1为启用，2为禁用
     *  用户自定义16，分支机构32，多币种64，生产128，属性1,电商2，下单4
     */
    private int initModuleDog(ServletContext servletContext) {
    	final Result rets = new Result();
        final Result rs = new Result();
        
        //免费版屏蔽功能 
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        //用户自定义 --
                        String sql ="update tblModules set " +
                        		"IsDisplay=" +(!SystemState.instance.isFree ? "0" : "-1") +
                        	" where  linkaddress like '/LogoSetAction.do%' ";
		                try {
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
		                } catch (Exception ex) {
		                }                       
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        
        //自定义 
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        //用户自定义 --
                        String sql ="update tblModules set " +
                        		"IsDisplay=" +(SystemState.instance.funUserDefine ? "0" : "-1") +
                        	" where (SystemParam='userDefine' or " +
                        			" linkaddress like '/AdvanceAction.do%' or " +
                        			" linkaddress like '/ReportSetQueryAction.do%' or " +
                        			" linkaddress like '%tblModules%') and linkaddress <> '/ReportSetQueryAction.do?styleType=print' ";
		                try {
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
		                } catch (Exception ex) {
		                }                       
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        
        //零售 
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        //用户自定义 --
                        String sql ="update tblModules set " +
                        		"IsDisplay=" +(SystemState.instance.funRetail ? "0" : "-1") +
                        	" where SystemParam='Retail' or " +
                        			" linkaddress like '%tblShop%' or " +
                        			" linkaddress like '%tblPOS%' or " +
                        			" linkaddress like '%tblShopCompany%' or " +
                        			" linkaddress like '/ReatailSingleAction%' ";
		                try {
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
		                } catch (Exception ex) {
		                }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        
        //专业版(配送流程，所有增强包),在烧专业版狗时，要求把所有增强功能打上，这里不做自动修改
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
		                try {
		                	//停止配送流程 SendFlow
		                    String sql ="update tblSysDeploy set IsUsld="+( SystemState.instance.funZY ? "1" : "0")+
		                    	" where SysCode='SendFlow' ";
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
		                    //配送菜单
		                    sql ="update tblModules set " +
	                		"IsDisplay=" +( SystemState.instance.funZY ? "0" : "-1") +
		                	" where linkAddress in ('/UserFunctionQueryAction.do?tableName=tblBuyApplication'," +
		                	"'/UserFunctionQueryAction.do?tableName=tblBuyApplicationTotal'," +
		                	"'/ReportDataAction.do?reportNumber=tblBuyApplicationDet'," +
		                	"'/ReportDataAction.do?reportNumber=ReportBuyApplicationTotal'," +
		                	"'/ReportDataAction.do?reportNumber=ReportSend'," +
		                	"'/ReportDataAction.do?reportNumber=ReportSendcheck'," +
		                	"'/ReportDataAction.do?reportNumber=ReportSendAnalyse') or " +		                	
		                	" id in (select a.id from tblModules a,tblLanguage b where a.modelName=b.id and b.zh_CN='配送管理') " ;
		                    cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
	                    
		                    
		                } catch (Exception ex) {
		                	ex.printStackTrace();
		                }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);

        //增强流程(采购换货，销售换货流程，固定资产,导入)
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
		                try {
	                        //属性			                
		                    
		                    //系统配置采购换货，销售换货流程，固定资产
		                    String sql ="update tblSysDeploy set IsUsld="+( SystemState.instance.funZQYW ? "1" : "0")+
		                    	" where SysCode='tblBuyReplaceFlow' or SysCode='tblSalesReplaceFlow' or SysCode='FixedAsset' ";
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
		                    //采购换货，销售换货,固定资产菜单
		                    sql ="update tblModules set " +
	                		"IsDisplay=" +( SystemState.instance.funZQYW ? "0" : "-1") +
		                	" where SystemParam='GoodsField' or " +
		                			" linkaddress like '%tblSalesReplace%' or " +
		                			" linkaddress like '%tblBuyReplace%' or "+
		                			" linkaddress like '%FixedAsset%' or "+
		                			" linkAddress like  '%importDataQueryAction.do%' or " +
		                			" id in (select a.id from tblModules a,tblLanguage b where a.modelName=b.id and b.zh_CN='固定资产') " ;
		                    
		                    
		                    cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
	                    
		                    
		                } catch (Exception ex) {
		                	ex.printStackTrace();
		                }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        
        //多币种
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;

                        boolean boolSysType = Boolean.parseBoolean(
                                BaseEnv.systemSet.get("currency").getSetting());
                        if(!SystemState.instance.funMoreCurrency){
                        	//无外币功能
                        	if(boolSysType){
                        		//停止外币
                        		//查询是否开账，如果开账，则不能停
                        		String sql="";
                    			try {
                    				sql = "select statusid as statAcc from tblaccperiod where  statusid='1'";
                    				PreparedStatement cs = conn.prepareStatement(sql);
                    				ResultSet rq = cs.executeQuery();
                    				int statusId = 0;
                    				if(rq.next()){
                    					statusId = rq.getInt(1);
                    				}
                    				if(statusId == 1){
                    					//已开账不能修改
                    					rets.retCode = SystemState.DOG_ERROR_MORE_CUR_USED;
                    					return;
                    				}
                            		sql = " update tblSysDeploy set Setting='false' where SysCode='currency'  ";
                            		
                                	
                                    cs = conn.prepareStatement(sql);
                                    //BaseEnv.log.debug(sql);
                                    cs.executeUpdate();
                                    //关闭外币后，往来科目关闭外币核算
                                    sql = " update tblAccTypeInfo set IsForCur=2 where AccNumber in ('1122','1123','2202','2203')  ";
                                    cs = conn.prepareStatement(sql);
                                    //BaseEnv.log.debug(sql);
                                    cs.executeUpdate();
                                    
                                } catch (Exception ex) {
                                    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                    BaseEnv.log.error(
                                            "update tblModules Error :" + sql,
                                            ex);
                                    return;
                                }
                                BaseEnv.systemSet.get("currency").setSetting("false");
                        		boolSysType = false;
                        	}
                        }
                        //停止和外币相的的菜单
		                try {
		                	String sql ="update tblSysDeploy set IsUsld="+( SystemState.instance.funMoreCurrency ? "1" : "0")+
	                    	" where SysCode='currency' ";
		                	PreparedStatement cs = conn.prepareStatement(sql);
		                	cs.executeUpdate();
		                	
		                	sql ="update tblModules set " +
	                    		"IsDisplay=" +(SystemState.instance.funMoreCurrency ? "0" : "-1") +
	                    		" where SystemParam='currency' or " +
                    			" linkaddress like '%tblCurrency%' or " +
                    			" linkaddress like '%tblSetExchange%' " ;
		                    cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();		                    
		                } catch (Exception ex) {
		                	ex.printStackTrace();
		                }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        //标准财务 --OK
        /*
	    if(BaseEnv.version != 12){ //v7不区分标准财务
	        retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        Connection conn = connection;
	
	                        boolean boolSysType = Boolean.parseBoolean(
	                                BaseEnv.systemSet.get("standardAcc").getSetting());
	                        if(!SystemState.instance.funFinancial){
	                        	//无标准财务功能
	                        	if(boolSysType){
	                        		//停止外币
	                        		//查询是否开账，如果开账，则不能停
	                        		String sql="";
	                    			try {
	                    				sql = "select statusid as statAcc from tblaccperiod where  statusid='1'";
	                    				PreparedStatement cs = conn.prepareStatement(sql);
	                    				ResultSet rq = cs.executeQuery();
	                    				int statusId = 0;
	                    				if(rq.next()){
	                    					statusId = rq.getInt(1);
	                    				}
	                    				if(statusId == 1){
	                    					//已开账不能修改
	                    					rets.retCode = SystemState.DOG_ERROR_FINANCIAL_USED;
	                    					return;
	                    				}
	                            		sql = " update tblSysDeploy set Setting='false' where SysCode='standardAcc'  ";
	                                	
	                                    cs = conn.prepareStatement(sql);
	                                    //BaseEnv.log.debug(sql);
	                                    cs.executeUpdate();
	                                } catch (Exception ex) {
	                                    rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                                    BaseEnv.log.error(
	                                            "update tblModules Error :" + sql,
	                                            ex);
	                                    return;
	                                }
	                                BaseEnv.systemSet.get("standardAcc").setSetting("false");
	                        		boolSysType = false;
	                        	}
	                        }
	                        
	                        String sql ="update tblModules set IsUsed=" +(boolSysType ? "1" : "2") +
	                        	" where SystemParam='standardAcc'";
	                        try {
	                            PreparedStatement cs = conn.prepareStatement(sql);
	                            //BaseEnv.log.debug(sql);
	                            cs.executeUpdate();
	                        } catch (Exception ex) {
	                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            BaseEnv.log.error(
	                                    "update tblModules Error :" + sql,
	                                    ex);
	                            return;
	                        }
	                        
	                        sql ="update tblModules set " +
	                        		"IsDisplay=" +(SystemState.instance.funFinancial ? "0" : "-1") +
	                        	" where SystemParam='standardAcc'  " ;
			                try {
			                    PreparedStatement cs = conn.prepareStatement(sql);
			                    cs.executeUpdate();
			                } catch (Exception ex) {
			                	ex.printStackTrace();
			                }
	                    }
	                });
	                return rs.getRetCode();
	            }
	        });
	        rs.setRetCode(retCode);
	    }
	    */
        //生产 --OK
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String sql ="update tblModules set " +
                        		"IsDisplay=" +(SystemState.instance.funProduct ? "0" : "-1") +
                        	" where SystemParam='product' or " +
                        			" linkaddress like '%tblBOM%' or " +
                        			" linkaddress like '%tblOutPrecessProducts%' or " +
                        			" linkaddress like '%tblOutPrecessMaterial%' or " +
                        			" linkaddress like '%tblOutMaterials%' or " +
                        			" linkaddress like '%tblInProducts%' or " +
                        			" linkaddress like '%tblInProductBP%' or " +
                        			" linkaddress like '%tblProduceCostBP%' or " +
                        			" linkaddress like '%tblProCheck%' or " +
                        			" linkaddress like '%tblPrecessFee%' or " +
                        			" linkaddress like '%tblProductMaterial%' or " +
                        			" linkaddress like '/Mrp.do%' or " +
                        			" linkaddress like '%tblOutPrecessCost%' ";
		                try {
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();
		                } catch (Exception ex) {
		                	ex.printStackTrace();
		                }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        
                
        //-----------------属性----先屏蔽所有与属性相关的菜单，再在初始化最后调initModuleDogAfter 执行define 停止所有属性---------------OK--------
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
		                try {
	                        //属性
			                String sql ="update tblModules set " +
			                		"IsDisplay=" +( SystemState.instance.funAttribute ? "0" : "-1") +
			                	" where SystemParam='GoodsField' or " +
			                			" linkaddress like '%tblGoodsAttribute%' or " +
			                			" linkaddress like '%tblGoodsOfProp%' or " +
			                			" linkaddress like '%tblGoodsPropItem%' ";
		                    PreparedStatement cs = conn.prepareStatement(sql);
		                    cs.executeUpdate();		                    
		                } catch (Exception ex) {
		                	ex.printStackTrace();
		                }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        //------------------属性------------------------
        
        //电商(暂未开发完成)
        //下单(暂未开发完成)
        return rets.retCode;
    }

    /**
     * 在初始化完成后，执行加密狗属性等停止define操作，为什么要分开放这里，因为执行define必须其它数据都初始化完成后才能执行
     * @param servletContext
     * @return
     */
    public int initModuleDogAfter(ServletContext servletContext) {
        final Result rs = new Result();        
        //处理商品属性，先查出所有启用的属性，先执行停止操作-----------------
        if(!SystemState.instance.funAttribute){
        	//无属性功能，先停止属性
        	final ArrayList<String> list = new ArrayList<String>();
        	int retCode = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                            Connection conn = connection;
                            //属性
    		                String sql =" select id from tblGoodsAttribute where isUsed=1 ";
    		                if((BaseEnv.version==3||BaseEnv.version==11)){
    		                	//布匹的，不能停用seq
    		                	sql += " and PropName != 'Seq' "; 
    		                }
    		                try {
    		                    PreparedStatement cs = conn.prepareStatement(sql);
    		                    ResultSet res = cs.executeQuery();
    		                    while(res.next()){
    		                    	
    		                    	list.add(res.getString(1));
    		                    }
    		                } catch (Exception ex) {
    		                }
                        }
                    });
                    return rs.getRetCode();
                }
            });
            rs.setRetCode(retCode);
            if(list.size() > 0){
            	//有启用的属性.
            	Object ob = servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
    	    	MessageResources resources = null;
    			if (ob instanceof MessageResources) {
    			    resources = (MessageResources) ob;
    			}
            	Result ra= new DynDBManager().defineDelSql("tblGoodsAttribute_System_RestartS", 
            			list.toArray(new String[]{}), "1",resources,Locale.getDefault(),"");
        		if(ra.retCode != ErrorCanst.DEFAULT_SUCCESS){
        			return SystemState.DOG_ERROR_PROP_USED;
        		}
            }
        }
        
        return 0;
    }

    public Result initModuleList() {
    	final Result rs = new Result();
    	GenJS.clearJS(); //清除自动生成的JS
    	
        Result result = new Result();
        HashMap newmap = new HashMap();

        UserMgt userMgt = new UserMgt();

        

        //查公共模块
        ArrayList allModule=new ArrayList();
        ArrayList<String> modules = new ArrayList<String>();        
        if(SystemState.instance.getModuleList().contains("3")){
        	modules.add("3");
        }
        if(SystemState.instance.getModuleList().contains("1")){
        	modules.add("1");
        }
        if(SystemState.instance.getModuleList().contains("2")){
        	modules.add("2");
        }
        if(SystemState.instance.getModuleList().contains("4")){
        	modules.add("4");
        }
        modules.add("0");
        for (String mstr : modules) {

            ArrayList list = new ArrayList();

            userMgt.getModuleList(mstr,list,null,result);


            if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                return result;
            } else {

                newmap.put(mstr, list);
                allModule.addAll(list);
            }
        }
        BaseEnv.allModule=allModule;
        BaseEnv.moduleMap = newmap;
        BaseEnv.log.debug("------Init ModuleList record:");
        return result;
    }


    private Result initRefreshTime() {
        ArrayList list = getRefreshTime();
        for (Object o : list) {
            Object[] os = (Object[]) o;
            if (os[0].equals("Enumeration")) {
                long time = (Long) os[1];
                if (time > InitMenData.ENUMERATION) {
                    InitMenData.ENUMERATION = time;
                }
            } else if (os[0].equals("ModuleList")) {
                long time = (Long) os[1];
                if (time > InitMenData.MODULELIST) {
                    InitMenData.MODULELIST = time;
                }
            } else if (os[0].equals("tableInfo")) {
                long time = (Long) os[1];
                if (time > InitMenData.TABLEINFO) {
                    InitMenData.TABLEINFO = time;
                }
            } else if (os[0].equals("propInfo")) {
                long time = (Long) os[1];
                if (time > InitMenData.PROPLIST) {
                    InitMenData.PROPLIST = time;
                }
            } else if (os[0].equals("systemSet")) {
                long time = (Long) os[1];
                if (time > InitMenData.SYSTEMSET) {
                    InitMenData.SYSTEMSET = time;
                }
            } else if (os[0].equals("employee")) {
            	long time = (Long) os[1] ;
            	if (time > InitMenData.EMPLOYEE) {
            		InitMenData.EMPLOYEE = time ;
            	}
            }
        }
        BaseEnv.log.debug("------Init RefreshTime ");
        return new Result();
    }

    public ArrayList getRefreshTime() {
        final Result res = new Result();
        res.setRetVal(new ArrayList());
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        ArrayList list = new ArrayList();
                        //查主表
                        String querysql = " select * from tblInitTime ";
                        try {
                            PreparedStatement cs = conn.prepareStatement(
                                    querysql);
                            //  BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            while (rset.next()) {
                                String name = rset.getString("initName");
                                long time = rset.getLong("lastTime");
                                list.add(new Object[] {name, time});
                            }
                            res.setRetVal(list);
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error(
                                    "Query data Error InitMenDate.initDBInformation :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);

        return (ArrayList) res.getRetVal();

    }
    
    public Result initReportShowSet() {
        final Result res = new Result();
        final ArrayList list =new ArrayList();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        //查主表
                        String querysql = " select tableName,fieldName,reportView,billView,popSel,keyword,popupView from tblShowSet  order by tableName,id";
                        try {
                            PreparedStatement cs = conn.prepareStatement(
                                    querysql);
                            //  BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            while (rset.next()) {                                
                                list.add(new String[] {rset.getString(1), rset.getString(2), rset.getString(3), rset.getString(4), rset.getString(5), rset.getString(6), rset.getString(7)});
                            }
                            res.setRetVal(list);
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error(
                                    "Query data Error InitMenDate.initReportShowSet :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        if(retCode== ErrorCanst.DEFAULT_SUCCESS){
        	BaseEnv.reportShowSet = list;
        }
        return res;

    }
    /**
     * 软件加密处理
     * @param servletContext
     * @return
     */
    private boolean readSoftCert(ServletContext servletContext){
    	File file = new File("aio.cert");
    	SystemState.instance.encryptionType = SystemState.ENCRYPTION_SOFT;
    	//读文件
    	byte[] bs = null;
    	try {
			FileInputStream fis  = new FileInputStream(file);
			int len = fis.available();
			bs = new byte[len];
			fis.read(bs);
		} catch (Exception e) {
			//加密狗或软加密错误
			SystemState.instance.dogState = SystemState.DOG_ERROR;
            return false;
		}
		//读日期和公司名称
		byte[] tempBs = new byte[20];
		System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
		SystemState.instance.registerDate = new String(tempBs).trim();
		tempBs = new byte[108];
		System.arraycopy(bs, 20, tempBs, 0, tempBs.length);
		try {
			SystemState.instance.companyName = new String(tempBs,"UTF-8").trim();
		} catch (Exception e1) {
		}
		tempBs = new byte[bs.length - 128];
		System.arraycopy(bs, 128, tempBs, 0, tempBs.length);
		//解码
		try{
			//读密钥
		    FileInputStream fisKey=new FileInputStream("../../website/WEB-INF/private1024.key");  
		    ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
		    Key key=(Key)oisKey.readObject();  
		    oisKey.close();  
		    fisKey.close();  
		    
			Cipher cipher =Cipher.getInstance("RSA");  
			  
		    //用私钥解密  
		    cipher.init(Cipher.DECRYPT_MODE, key);  
		    bs=cipher.doFinal(tempBs); 
		    //读序列号
		    tempBs = new byte[30];
		    System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
			SystemState.instance.dogId = new String(tempBs).trim();
			//证书版本，预留
			//读机器码
		    tempBs = new byte[32];
		    System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
			SystemState.instance.pcNo = new String(tempBs).trim();
	        //取机器码
	        Random rd = new Random();
	        rd.setSeed(System.currentTimeMillis());
	        int rdi = rd.nextInt(4000);        
	        String s =CallSoftDll.get(rdi+"");        
	        byte[] mb= new byte[16] ;
	        mb =SecurityLock2.hexStringToBytes(s);    	
	    	rdi +=5;
	    	for(int i=0;i<mb.length ;i++){    		
	    		mb[i] = (byte)(mb[i]-rdi);
	    		if(i>8){
	    			mb[i] =(byte)(mb[i] -2);
	    		}
	    	}    	  	
	        //校验机器码
	    	String realPcNo = SecurityLock2.bytesToHexString(mb);
	    	
	    	logCert(CallSoftDll.get("KoronSeward"),CallSoftDll.get("KoronSewardM"),s,realPcNo);
	    	
	    	
	        		        
			if(SystemState.instance.pcNo== null || !SystemState.instance.pcNo.equals(realPcNo)){
				//机器码不一致--OK
				//测试是否老一代有mac的机器特殊码
				s =CallSoftDll.get("mac");
				if(SystemState.instance.pcNo== null || !SystemState.instance.pcNo.equals(s)){				
					SystemState.instance.dogState = SystemState.DOG_ERROR_PCNO;
					return false;
				}
			}
			//这里需要效验远程校验机器码是否和服务器号一致，---OK
			try{
				ServerConnection connc = new ServerConnection(BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12, 0xF, 0x58,
                        (byte) 0x88, 0x10, 0x40, 0x38
                        , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                        , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});

                    String posStrc = "<operation>checkPcNo</operation>";
                    posStrc += "<dogId>" + SystemState.instance.dogId + "</dogId>" +
                        "<pcNo>" + SystemState.instance.pcNo + "</pcNo>";
                    String retc = "";
                    retc = connc.send(posStrc);
                    if(retc != null && retc.indexOf("<result>NoSame</result>") > -1){
                    	//证书远程效验失效
    					SystemState.instance.dogState = SystemState.DOG_ERROR_LOSE_EFFEC;
    					//为了避免证书失效后，不断刷新服务器，这里停止更新线程
    					ConfigRefresh.stopThread();
    					//file.delete();
    	                return false;
                    }
			}catch(Exception e){
				
			}
			
			int pos = 64;
			//检查版本 工贸版8，布匹版3
			if(bs[pos+0]!=BaseEnv.version){
				//机器码不一致---OK
				SystemState.instance.dogState = SystemState.DOG_ERROR_VERSIONNOMATCH;
                return false;
			}
			//进销存用户数
			SystemState.instance.userNum = SecurityLock2.bytesToShort(bs, pos+1);
			//生产用户数
			SystemState.instance.pudUserNum = SecurityLock2.bytesToShort(bs, pos+3);
			//电商用户数
			SystemState.instance.ecUserNum = SecurityLock2.bytesToShort(bs, pos+5);
			//OA用户数
			SystemState.instance.oaUserNum = SecurityLock2.bytesToShort(bs, pos+7);				
			//CRM用户
			SystemState.instance.crmUserNum = SecurityLock2.bytesToShort(bs, pos+9);
			//下单用户数
			SystemState.instance.ordersUserNum = SecurityLock2.bytesToShort(bs, pos+11);				
			//HR用户数
			SystemState.instance.hrUserNum = SecurityLock2.bytesToShort(bs, pos+13);
			
			//取授权期限
			int gd = SecurityLock2.bytesToInt(bs,pos+15);
			SystemState.instance.grantDate = gd==0?"":""+gd;
			//检查期限 --OK
			if(SystemState.instance.grantDate.length() > 0){
				if(System.currentTimeMillis() > BaseDateFormat.parse(SystemState.instance.grantDate+"235959", BaseDateFormat.yyyyMMddHHmmss2).getTime()){
					SystemState.instance.dogState = SystemState.DOG_ERROR_LIMIT_DATE;
	                return false;
				}
			}
			byte b = bs[pos+19];
			ArrayList list = new ArrayList();
			//进销存模块
			if((b&0x01)==1){
				list.add("1");//JXC
			}
			if((b&0x02)==2){
				list.add("2"); //OA
			}
			if((b&0x04)==4){
				list.add("3"); //CRM
			}
			if((b&0x08)==8){
//				list.add("4"); //HR
			}
	        SystemState.instance.moduleList = list;
	        if (SystemState.instance.moduleList == null) {
                SystemState.instance.dogState = SystemState.DOG_ERROR;
                return false;
            } else if (SystemState.instance.moduleList.size() == 0) {
                SystemState.instance.dogState = SystemState.DOG_ERROR_NOMODULE;
                return false;
            }
	        
	        //用户自定义
	        SystemState.instance.funUserDefine=(b&0x10)==16?true:false;
	        //增强业务
	        SystemState.instance.funZQYW=GlobalsTool.hasMainModule("1")&& (b&0x20)==32?true:false;
	        //多币种
	        SystemState.instance.funMoreCurrency=GlobalsTool.hasMainModule("1")&& (b&0x40)==64?true:false;
	        //生产
	        SystemState.instance.funProduct=GlobalsTool.hasMainModule("1")&& (b&0x80)==128?true:false;
	        //多语言
	        SystemState.instance.languageNum=bs[pos+20];
	        if (SystemState.instance.languageNum < 1) {
                SystemState.instance.dogState = SystemState.DOG_ERROR;
                return false;
            }
	        if (!initLanguage(servletContext)) {
                BaseEnv.log.error("--------Init Language Failture-----------");
                return false;
            }
	        
	        b = bs[pos+21];
	        //属性
	        SystemState.instance.funAttribute=GlobalsTool.hasMainModule("1")&& (b&0x01)==1?true:false;
	        // 电商
	        SystemState.instance.funEc=GlobalsTool.hasMainModule("1")&& (b&0x02)==2?true:false;
	        // 下单
	        SystemState.instance.funOrders=GlobalsTool.hasMainModule("1")&& (b&0x04)==4?true:false;
	        // 零售
	        SystemState.instance.funRetail=GlobalsTool.hasMainModule("1")&& (b&0x08)==8?true:false;
	        // VIP
	        SystemState.instance.funZY=GlobalsTool.hasMainModule("1")&& (b&0x10)==16?true:false;
	        //VIP
	        SystemState.instance.funFinancial=GlobalsTool.hasMainModule("1")&& (b&0x20)==32?true:false;
	        SystemState.instance.funQJ=GlobalsTool.hasMainModule("3")&& (b&0x40)==64?true:false;
	        
	        SystemState.instance.isFree = (b&0x80)==128?true:false; //是否免费版，只有软开密这个配置才有效
	        
	        SystemState.instance.serverCount = bs.length< pos+22 + 1  ? 1:bs[pos+22];	
	        //数据较验
			//-------------------------------------------------------
	        RegisterMgt rmg = new RegisterMgt();
	        String userdate = rmg.checkUserTime(SystemState.instance.dogId + "");
	        
		}catch(Exception e){
			BaseEnv.log.fatal("InitMenData.initDog 读证书文件错误。。。。",e);
			SystemState.instance.dogState = SystemState.DOG_ERROR;
			return false;
		}
		return true;
    }
    
    /**
     * 硬加密处理
     * @param servletContext
     * @return
     */
    private boolean readHardDog(ServletContext servletContext){
    	 
    	//检查加密狗版本。
    	int version  = SecurityLock.getVersion();	        	
    	if (version == 1) {
    		//第二代加密狗
    		SystemState.instance.encryptionType = SystemState.ENCRYPTION_DOG2;
        	
        	byte[] bs = null;	            	
				    			
			//解码
			try{
				//读密钥
			    FileInputStream fisKey=new FileInputStream("../../website/WEB-INF/private1024.key");  
			    ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
			    Key key=(Key)oisKey.readObject();  
			    oisKey.close();  
			    fisKey.close();  
			    
				Cipher cipher =Cipher.getInstance("RSA");  
				  
			    //用私钥解密  
			    cipher.init(Cipher.DECRYPT_MODE, key);  
			    byte[] tempBs = SecurityLock.getData();	    			    
			    bs=cipher.doFinal(tempBs); 
			    //读狗号
			    tempBs = new byte[30];
			    System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
				SystemState.instance.dogId = new String(tempBs).trim();
				//证书版本，预留
				//读机器码
			    tempBs = new byte[32];
			    System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
				SystemState.instance.pcNo = new String(tempBs).trim();
		        //校验机器码
		    	String[] realPcNo = SecurityLock.F_ReadKey(1);
		    	//返回来的值可能不够位
		    	for(int i=0;i<8-realPcNo[0].length();i++){
		    		realPcNo[0] = "0"+realPcNo[0];
		    	}
		    	for(int i=0;i<8-realPcNo[1].length();i++){
		    		realPcNo[1] = "0"+realPcNo[1];
		    	}
		        		        
				if(SystemState.instance.pcNo== null || !SystemState.instance.pcNo.equalsIgnoreCase(realPcNo[0]+"-"+realPcNo[1]) ||
						SystemState.instance.dogId== null || !SystemState.instance.dogId.equalsIgnoreCase(SecurityLock.readKeyId()+"")){
					//加密狗号或特殊码不一致 --OK
					SystemState.instance.dogState = SystemState.DOG_ERROR;
	                return false;
				}
				int pos = 64;
				//检查版本 工贸版8，布匹版3
				if(bs[pos+0]!=BaseEnv.version){
					//机器码不一致--OK
					SystemState.instance.dogState = SystemState.DOG_ERROR_VERSIONNOMATCH;
	                return false;
				}
				//进销存用户数
				SystemState.instance.userNum = SecurityLock2.bytesToShort(bs, pos+1);
				//生产用户数
				SystemState.instance.pudUserNum = SecurityLock2.bytesToShort(bs, pos+3);
				//电商用户数
				SystemState.instance.ecUserNum = SecurityLock2.bytesToShort(bs, pos+5);
				//OA用户数
				SystemState.instance.oaUserNum = SecurityLock2.bytesToShort(bs, pos+7);				
				//CRM用户
				SystemState.instance.crmUserNum = SecurityLock2.bytesToShort(bs, pos+9);
				//下单用户数
				SystemState.instance.ordersUserNum = SecurityLock2.bytesToShort(bs, pos+11);				
				//HR用户数
				SystemState.instance.hrUserNum = SecurityLock2.bytesToShort(bs, pos+13);
				
				//取授权期限
				int gd = SecurityLock2.bytesToInt(bs,pos+15);
				SystemState.instance.grantDate = gd==0?"":""+gd;
				//检查期限 --OK
				if(SystemState.instance.grantDate.length() > 0){
					if(System.currentTimeMillis() > BaseDateFormat.parse(SystemState.instance.grantDate+"235959", BaseDateFormat.yyyyMMddHHmmss2).getTime()){
						SystemState.instance.dogState = SystemState.DOG_ERROR_LIMIT_DATE;
		                return false;
					}
				}
				byte b = bs[pos+19];
				ArrayList list = new ArrayList();
				//进销存模块
				if((b&0x01)==1){
					list.add("1");//JXC
				}
				if((b&0x02)==2){
					list.add("2"); //OA
				}
				if((b&0x04)==4){
					list.add("3"); //CRM
				}
				if((b&0x08)==8){
//					list.add("4"); //HR
				}
		        SystemState.instance.moduleList = list;
		        if (SystemState.instance.moduleList == null) {
	                SystemState.instance.dogState = SystemState.DOG_ERROR;
	                return false;
	            } else if (SystemState.instance.moduleList.size() == 0) {
	                SystemState.instance.dogState = SystemState.DOG_ERROR_NOMODULE;
	                return false;
	            }
		        
		        //用户自定义
		        SystemState.instance.funUserDefine=(b&0x10)==16?true:false;
		        //分支机构
		        SystemState.instance.funZQYW=GlobalsTool.hasMainModule("1")&& (b&0x20)==32?true:false;
		        //多币种
		        SystemState.instance.funMoreCurrency=GlobalsTool.hasMainModule("1")&& (b&0x40)==64?true:false;
		        //生产
		        SystemState.instance.funProduct=GlobalsTool.hasMainModule("1")&& (b&0x80)==128?true:false;
		        //多语言
		        SystemState.instance.languageNum=bs[pos+20];
		        if (SystemState.instance.languageNum < 1) {
	                SystemState.instance.dogState = SystemState.DOG_ERROR;
	                return false;
	            }
		        if (!initLanguage(servletContext)) {
	                BaseEnv.log.error("--------Init Language Failture-----------");
	                return false;
	            }
		        
		        b = bs[pos+21];
		        //属性
		        SystemState.instance.funAttribute=GlobalsTool.hasMainModule("1")&& (b&0x01)==1?true:false;
		        // 电商
		        SystemState.instance.funEc=GlobalsTool.hasMainModule("1")&& (b&0x02)==2?true:false;
		        // 下单
		        SystemState.instance.funOrders=GlobalsTool.hasMainModule("1")&& (b&0x04)==4?true:false;
		        // 零售
		        SystemState.instance.funRetail=GlobalsTool.hasMainModule("1")&& (b&0x08)==8?true:false;
		        // VIP
		        SystemState.instance.funZY=GlobalsTool.hasMainModule("1")&& (b&0x10)==16?true:false;
		        //VIP
		        SystemState.instance.funFinancial=GlobalsTool.hasMainModule("1")&& (b&0x20)==32?true:false;
		        SystemState.instance.funQJ=GlobalsTool.hasMainModule("3")&& (b&0x40)==64?true:false;
		        
		        
		        
		        SystemState.instance.serverCount = bs.length< pos+22 + 1  ? 1:bs[pos+22];	
		        
		        //数据较验
				//-------------------------------------------------------
		        /**
				 * 检查是否注册 1、先检查加密狗有无注册信息 2、如果有注册信息，则检查注册表中有无记录，如果无，则补上
				 * 3、如果无注册信息，则要求注册
				 */
				RegisterMgt rmg = new RegisterMgt();
				String userdate = rmg.checkUserTime(SecurityLock.readKeyId() + "");
				String d = SecurityLock.readRegiste();
				if ((d == null || d.trim().length() == 0)) {
					// 狗中无注册信息
					// 这里检查正式使用时间如果不存在，则写入，如果存在则比较时间，如果超过10天，则必须注册才能继续使用
					
					SystemState.instance.noRegUseDate = userdate; // 记录系统的未注册最先使用时间
					if (userdate != null && userdate.trim().length() > 0) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
							long dl = sdf.parse(userdate).getTime();
							dl = System.currentTimeMillis() - dl;
							dl = dl / (24 * 60 * 60000);
							if (dl > 10) {
								SystemState.instance.dogState = SystemState.DOG_ERROR_FORMAL_REG;
								return false;
							}
						} catch (ParseException ex) {
							ex.printStackTrace();
						}
					}
				} else {
					// 有注册信息，则按狗中注册信息处理
					String comp = "";
					if (d.indexOf(":") > 0) {
						comp = d.substring(d.indexOf(":") + 1);
						d = d.substring(0, d.indexOf(":"));
					}
					SystemState.instance.companyName = comp;
					SystemState.instance.registerDate = d;

				}
			}catch(Exception e){	    				
				BaseEnv.log.fatal("InitMenData.initDog 加密狗数据错误。。。。",e);
				SystemState.instance.dogState = SystemState.DOG_ERROR;
				return false;
			}
    	}else{
			SystemState.instance.encryptionType = SystemState.ENCRYPTION_DOG1;
			// 第一代加密狗--------------
			SystemState.instance.dogId = SecurityLock.readKeyId() + "";
			// 读加密狗号，如果狗号为0，则说明是开发环境
			if (SecurityLock.readKeyId() == 0) {
				SystemState.instance.develope = true;
				
				//开发环境，重设日志系统
		        try {
		            Properties prop = new Properties();
		            InputStream is = new FileInputStream(
		                    "../../config/log4j.properties");
		            prop.load(is);
		            prop.setProperty("log4j.logger.MYLog", "debug,A2");
		            prop.setProperty("log4j.rootLogger", "warn,stdout");
		            PropertyConfigurator.configure(prop);
		            BaseEnv.log= Logger.getLogger("MYLog");
		        } catch (Exception ex) {
		        }
			}

			// 读取语言数
			SystemState.instance.languageNum = SecurityLock.getLanguageNum();
			if (SystemState.instance.languageNum < 1) {
				SystemState.instance.dogState = SystemState.DOG_ERROR;
				return false;
			}

			if (!initLanguage(servletContext)) {
				BaseEnv.log.error("--------Init Language Failture-----------");
				return false;
			}

			// 读取模块
			SystemState.instance.moduleList = SecurityLock.getModule();
			if (SystemState.instance.moduleList == null) {
				SystemState.instance.dogState = SystemState.DOG_ERROR;
				return false;
			} else if (SystemState.instance.moduleList.size() == 0) {
				SystemState.instance.dogState = SystemState.DOG_ERROR_NOMODULE;
				return false;
			}
			//数字转字符串
			ArrayList nml = new ArrayList(); 
			for(Object o : SystemState.instance.moduleList){
				nml.add(o.toString());
			}
			SystemState.instance.moduleList = nml;

			// 读取用户数,老版加密狗所有用户数一样
			SystemState.instance.userNum = SecurityLock.getUserNum();
			SystemState.instance.oaUserNum = SystemState.instance.userNum;
			SystemState.instance.crmUserNum = SystemState.instance.userNum;
			SystemState.instance.hrUserNum = SystemState.instance.userNum;
			SystemState.instance.ordersUserNum = 1;

			SystemState.instance.grantDate = ""; // 授权期限

			// 读取用户自定义功能
			SystemState.instance.funUserDefine = SecurityLock.getUserDefine();

			// 如果未打开自定义功能，必须计算自定义功能有没有被非法修改
			if (!SystemState.instance.funUserDefine) {
				// 暂时不控制
			}
			SystemState.instance.funZQYW =GlobalsTool.hasMainModule("1");
			SystemState.instance.funMoreCurrency =GlobalsTool.hasMainModule("1")&&  SecurityLock.getMoreCurrency();
			SystemState.instance.funProduct =GlobalsTool.hasMainModule("1")&&  SecurityLock.getProduct();
			// 老版未控制功能默认全给
			SystemState.instance.funAttribute = GlobalsTool.hasMainModule("1");
			SystemState.instance.funRetail = GlobalsTool.hasMainModule("1") ;
			SystemState.instance.funZY = GlobalsTool.hasMainModule("1") ;
			SystemState.instance.funFinancial = GlobalsTool.hasMainModule("1") ;
			SystemState.instance.funOrders = GlobalsTool.hasMainModule("1") ;
			SystemState.instance.funEc = GlobalsTool.hasMainModule("1") ;
			SystemState.instance.funQJ = false;
			
	        //以下功能未实现
	        SystemState.instance.funEc = false;
	        SystemState.instance.funOrders = false;
			
			SystemState.instance.serverCount = 2;

			/**
			 * 检查是否注册 1、先检查加密狗有无注册信息 2、如果有注册信息，则检查注册表中有无记录，如果无，则补上
			 * 3、如果无注册信息，则要求注册
			 */
			RegisterMgt rmg = new RegisterMgt();
			String userdate = rmg.checkUserTime(SecurityLock.readKeyId() + "");
			String d = SecurityLock.readRegiste();
			if ((d == null || d.trim().length() == 0)) {
				// 狗中无注册信息
				// 这里检查正式使用时间如果不存在，则写入，如果存在则比较时间，如果超过10天，则必须注册才能继续使用
				
				SystemState.instance.noRegUseDate = userdate; // 记录系统的未注册最先使用时间
				if (userdate != null && userdate.trim().length() > 0) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						long dl = sdf.parse(userdate).getTime();
						dl = System.currentTimeMillis() - dl;
						dl = dl / (24 * 60 * 60000);
						if (dl > 10) {
							SystemState.instance.dogState = SystemState.DOG_ERROR_FORMAL_REG;
							return false;
						}
					} catch (ParseException ex) {
						ex.printStackTrace();
					}
				}
			} else {
				// 有注册信息，则按狗中注册信息处理
				String comp = "";
				if (d.indexOf(":") > 0) {
					comp = d.substring(d.indexOf(":") + 1);
					d = d.substring(0, d.indexOf(":"));
				}
				SystemState.instance.companyName = comp;
				SystemState.instance.registerDate = d;

			}
		}
    	return true;
    }
    
    private boolean readEval(ServletContext servletContext){

    	SystemState.instance.encryptionType = SystemState.ENCRYPTION_EVAL;
    	SystemState.instance.isFree = true; //试用的都是免费版
    	// 无加密狗
        // 检查是否曾正式版注册,这里要检查数据库，因没法检查加密狗，所以如果用户系统重装，则会出现试用提示界面。
        RegisterMgt rmg = new RegisterMgt();	            
        // 检查是否试用版注册--OK
        Result drs = rmg.checkRegister();
        String d = (String)drs.retVal;
        if(drs.retCode == ErrorCanst.DATA_ALREADY_USED){
        	SystemState.instance.dogState = SystemState.DOG_ERROR_USED_NODOG;
            return false;
        }else {
        	if(d == null || d.length() ==0){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                d = sdf.format(new Date());
        	}
        	 // 设置系统的试用时间
            SystemState.instance.evaluate_date = d.substring(0,
                    d.indexOf(":"));
            SystemState.instance.evaluate_no = d.substring(d.indexOf(":") +
                    1);
            
			
			// 这里检查正式使用时间如果超过10天，则必须注册才能继续使用					
			SystemState.instance.noRegUseDate = d; // 记录系统的未注册最先使用时间
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				long dl = sdf.parse(d).getTime();
				dl = System.currentTimeMillis() - dl;
				dl = dl / (24 * 60 * 60000);
				if (dl > 10) {
					SystemState.instance.dogState = SystemState.DOG_ERROR_FORMAL_REG;
					return false;
				}
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
        }
        
        
        // 试用较验
        int r = checkEnv();
        if (r == 0) {
        	 // 试用版对模块，语言，人数，自定义不做限制
        	SystemState.instance.languageNum = 2;
        	if (!initLanguage(servletContext)) {
                BaseEnv.log.error(
                        "--------Init Language Failture-----------");
                return false;
            }
        	
        	SystemState.instance.moduleList = new ArrayList();
            SystemState.instance.moduleList.add("1");
            SystemState.instance.moduleList.add("2");
            SystemState.instance.moduleList.add("3");
            //SystemState.instance.moduleList.add("4"); //暂时试用没有HR

            
            SystemState.instance.userNum = 1;
//            SystemState.instance.pudUserNum = 5000;
//            SystemState.instance.ecUserNum = 5000;
            SystemState.instance.oaUserNum = 5;
            SystemState.instance.crmUserNum = 2;
            SystemState.instance.ordersUserNum = 0;
            SystemState.instance.hrUserNum = 0;
            SystemState.instance.grantDate = ""; //授权期限
            
//            SystemState.instance.funUserDefine = true;
//            SystemState.instance.funFenZhi = true;
//            SystemState.instance.funMoreCurrency = true;
//            SystemState.instance.funProduct = true;
//            SystemState.instance.funAttribute = true;
//            SystemState.instance.funEc = true;
//            SystemState.instance.funOrders = true;
            
            
//			SystemState.instance.funRetail = true;
//			SystemState.instance.funVIP = true;
//			SystemState.instance.funFinancial = true;
//			SystemState.instance.funCrmReport = false;
            
            //以下功能未实现
            SystemState.instance.funZQYW = false;
            SystemState.instance.funEc = false;
            SystemState.instance.funOrders = false;

            
            
        } else if (r == 1) {
            SystemState.instance.dogState = SystemState.DOG_ERROR_ENV_DATE;
            return false;
        } else if (r == 2) {
            SystemState.instance.dogState = SystemState.DOG_ERROR_ENV_BIS;
            return false;
        }
        return true;
    }

    /**
     * 1、初始化加密狗（第一代），
     * 2、检查有没有软证书
     * 2、如果加密狗未插入，则判断是否有过试用注册，如没有进行过试用注册，则提示用户是否试用。
     * 3、如果有试用注册，则进入试用初始化流程(判断试用时间和单据的量)
     * 4、如果试用到期或单据过量，则提示试用过期，要求购买正版.
     * 5、如果试用未到期，进入试用状态.
     * 6、用户试用注册，填写试用资料后，重新进入本流程
     * 7、加密狗初始化成功后，读取加密狗中，模块，用户数，语言数，自定义模块数。
     * 8、如果用户数异常，进入异常处理
     * 9、如果模块数异常，进入异常处理
     * @return boolean
     */
    public boolean initDog(ServletContext servletContext) {
    	GenJS.clearJS();
    	
    	if(flock != null){
    		//如果是锁定文件，则先解锁
            try {
				flock.release();
				flock = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    	
    	
    	//系统版本信息
        Result rs = initSystemVersion();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------Init initSystemVersion Failture-----------" +
                    rs.retCode);
        }
    	
    	//系统设置
        rs = initSystemSettingInformation();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error(
                    "--------Init SystemSettingInformation Failture-----------" +
                    rs.retCode);
        }
    	
        SystemState.instance.noRegUseDate = null; //清空最先使用时间，
        int initState = 0; 

        /**
         * 加密过程初始化
         * 默认情况下优先检查加密狗，用户可配置启用顺序
         * 1、先检查有无aio.cert文件，如果有，执行软加密流程
         * 2、检查是否第二代加密狗，
         * 3、检查是否第一代加密狗
         * 4、检查是否试用注册
         */ 
        boolean bootFromDog = true; //优先加密狗
        try {
			java.io.BufferedReader br = new java.io.BufferedReader(new FileReader("../../config/boot.ini"));
			String b=null;
			while ((b = br.readLine()) != null) {  
                if(b.trim().startsWith("first") && b.indexOf("soft") > -1){
                	bootFromDog = false;
                	break;
                }
            }  
		} catch (Exception e1) {
			
		}
        if(bootFromDog){//加密狗优先
        	BaseEnv.log.info("---------------------------------first read dog =======-----------");
		        boolean ret = SecurityLock.init();
		        if (ret) {
		        	if(this.readHardDog(servletContext)){
		        		initState = SystemState.DOG_FORMAL; 
		        	}else{
		        		return false;
		        	}
		        } else {
		        	File file = new File("aio.cert");
			        if(file.exists()){
			        	if(this.readSoftCert(servletContext)){
			        		initState = SystemState.DOG_FORMAL; 
			        	}else{
			        		return false;
			        	}
			        }else{ 
			        	if(this.readEval(servletContext)){
			        		initState = SystemState.DOG_EVALUATE;
			        	}else{
			        		return false;
			        	}	
			        }
		        }
        }else{	//软加密优先	
        	BaseEnv.log.info("---------------------------------first read soft cert =======-----------");
	        File file = new File("aio.cert");
	        if(file.exists()){
	        	if(this.readSoftCert(servletContext)){
	        		initState = SystemState.DOG_FORMAL; 
	        	}else{
	        		return false;
	        	}
	        }else{        	
		        BaseEnv.log.info("---------------------------------start read usb key=======-----------");
		        boolean ret = SecurityLock.init();
		        if (ret) {
		        	if(this.readHardDog(servletContext)){
		        		initState = SystemState.DOG_FORMAL; 
		        	}else{
		        		return false;
		        	}
		        } else {
		        	if(this.readEval(servletContext)){
		        		initState = SystemState.DOG_EVALUATE;
		        	}else{
		        		return false;
		        	}	        	
		        }
	        } 
        }
        
        if(SystemState.instance.encryptionType != SystemState.ENCRYPTION_EVAL){
	        //执行主帐套和查询帐套        --OK
			try {
				File f = new File("../../config/AccountType.txt");
		        if(f.exists()){
		        	FileInputStream fis;
					fis = new FileInputStream(f);
					byte[] bs = new byte[fis.available()];
		        	fis.read(bs);
		        	String con = new String(bs);
		        	if(con.indexOf("AccountType=Query")> -1){
		        		BaseEnv.log.info("----------------------帐套被设置为查询帐套---------------");
		        		SystemState.instance.accountType = SystemState.ACCOUNT_QUERY;
		        	}
		        }
			} catch (Exception e) {
			}
			if(SystemState.instance.accountType== SystemState.ACCOUNT_NORMAL){
				try {
					String[] paths = new String[]{ "C:/Windows/","D:/Windows/","E:/Windows/","F:/Windows/","C:/","D:/","E:/","F:/"};
					String path="C:/Windows/";
					for(String p:paths){
						File f = new File(p);
						if(f.exists()){
							path = p;
							break;
						}
					}
					boolean foundNormal=false;
					for(int i=0;i<SystemState.instance.serverCount;i++){
						FileOutputStream fis = new FileOutputStream(path+SystemState.instance.dogId+"_"+i+".txt");
				        FileChannel fc = fis.getChannel();
				        flock = fc.tryLock();
				        if(flock != null){
				        	foundNormal = true;
				        	BaseEnv.log.info("----------------------第"+SystemState.instance.dogId+"_"+i+"个帐套正常---------------");
				        	//把帐套路径写入数据库中
				        	writeNomalPath(SystemState.instance.dogId+"_"+i,new File("a").getAbsolutePath());
				        	
				            break;				            
				        }else{
				        	BaseEnv.log.info("----------------------第"+SystemState.instance.dogId+"_"+i+"个帐套已被占用---------------");
				        }
					}
					if(!foundNormal){
						BaseEnv.log.info("----------------------无可用正常帐套---------------");
			            SystemState.instance.accountType = SystemState.ACCOUNT_QUERY;
			        }
				} catch (Exception e) {
				}
			}
        }
        	
        
        
        //检查用户数是否被非法撰改,检查数据库中用户数是否超过狗的限制
        for(String ml:(ArrayList<String>)SystemState.instance.moduleList){        
	        int un = getDBUserNum(ml);
	        if ("1".equals(ml) && un > SystemState.instance.userNum) {
	            SystemState.instance.reluserNum = un;
	            SystemState.instance.userState = SystemState.DOG_ERROR_USER;
	            break;
	        }
	        if ("2".equals(ml)  && un > SystemState.instance.oaUserNum) {
	            SystemState.instance.reluserNum = un;
	            SystemState.instance.userState = SystemState.DOG_ERROR_USER_OA;
	            break;
	        }
	        if ("3".equals(ml)  && un > SystemState.instance.crmUserNum) {
	            SystemState.instance.reluserNum = un;
	            SystemState.instance.userState = SystemState.DOG_ERROR_USER_CRM;
	            break;
	        }
	        if ("4".equals(ml)  && un > SystemState.instance.hrUserNum) {
	            SystemState.instance.reluserNum = un;
	            SystemState.instance.userState = SystemState.DOG_ERROR_USER_HR;
	            break;
	        }
	        if ("10".equals(ml)  && un > SystemState.instance.ordersUserNum) {
	            SystemState.instance.reluserNum = un;
	            SystemState.instance.userState = SystemState.DOG_ERROR_USER_ORDERS;
	            break;
	        }
        }
        
       
        // 加载分支机构的期间数据
        rs = initAccPeriod();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init EnumerationBean Failture-----------" +
                    rs.retCode);
        }
        
        /*开帐日期*/
        rs = new SysAccMgt().getCurrentlyPeriod() ;
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	BaseEnv.beginDate = (String) rs.retVal ;
        }

        //加载所有的枚举值
        rs = initEnumerationInformation();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init EnumerationBean Failture-----------" +
                    rs.retCode);
        }
        
        //加载所有的行业
        rs = initWorkProfessionBeanInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init WorkProfession Failture-----------" +
                    rs.retCode);
        }
        
        //加载所有的地区
        rs = initDistrictInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init District Failture-----------" +
                    rs.retCode);
        }
        
        //加载所有的部门
        rs = initDeptInformation(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init Dept Failture-----------" +
                    rs.retCode);
        }

        //根据读到的系统参数，更改数据库中模块表相应的多币种和分支机构是否启用
        rs = initModuleSysType(); 
        int rd = initModuleDog(servletContext);
        if(rd != 0){
        	initState = rd;
        }
        rs = initModuleList();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init ModuleList Failture-----------" +
                              rs.retCode);
        }
        BaseEnv.log.debug(
                "--------Init ModuleList Successful---------------");
        //加载所有用户模块自定义列名设置信息
        rs = initModuleColLanuage(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init moduleColLanguage Failture-----------" +
                    rs.retCode);
        }

        
        //加载所有用户自定义列宽设置信息
        rs = initUserColWidth(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init userWidth Failture-----------" +
                              rs.retCode); 
        }
        
        //加载所有用户自定义列名设置信息
        rs = initUserLanuageInfo(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error(
                    "--------Init userLanguage Failture-----------" +
                    rs.retCode);
        }
        
        //加载所有的表信息
        rs = initDBInformation(servletContext,null);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init BaseConfig Failture-----------" +
                              rs.retCode);
        }
        //加载所有用户自定义列配置设置信息
        rs = initUserColConfig(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init userWidth Failture-----------" +
                              rs.retCode);
        }
        // 加载分类表信息
        rs = initScopeCataData(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init BaseConfig Failture-----------" +
                              rs.retCode);
        }
        
        //加载模块对应字段信息（主要用于多个模块使用同一张表，并且使用同一个define）
        rs = initModuleField(servletContext); 
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init BaseConfig Failture-----------" +
                              rs.retCode);
        }
        //加载弹出框显示所需语句
        rs = initPopDisSen(servletContext);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init BaseConfig Failture-----------" +
                              rs.retCode);
        }
        
        //加载OA工作流中所设置的假期信息
        rs = this.initHolidayInfo(servletContext);
        if (rs != null && rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init initHolidayInfo Failture-----------" +
                              rs.retCode);
        }
        
        //加载所有商品属性
        rs = initPropInformation(servletContext);
        if (rs != null && rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init PropInfo Failture-----------" +
                              rs.retCode);
        } 
        
        //      加载所有商品属性
        rs = initAttInformation(servletContext);
        if (rs != null && rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------Init PropInfo Failture-----------" +
                              rs.retCode);
        } 
        
		//删除所有打印数据
        rs = dropPrintData(servletContext);
        if (rs != null && rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.log.error("--------drop print data Failture-----------" +rs.retCode);
        }
        //初始化报表显示配置
        rs = initReportShowSet();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error("--------Init ReportShowSet Failture-----------");
        }
        
        //读配置文件
        initConfigFile();
        BaseEnv.log.debug("--------Init Config File ---------------");

        
        /**加载所有已启用的工作流信息**/
        rs = initWorkFlowInfo(servletContext) ;
        if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
        	BaseEnv.log.error("-----------Init WorkFlow Failture--------"+rs.getRetCode()) ;
        }        
        
        //系统数据库重要数据的更新时间，用于ConfigRefresh线程比较数据库内容是否有更新，如果有更新，则需重新读取到内存中
        rs = initRefreshTime();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error("--------Init RefreshTime Failture-----------");
        }
        
        rs = initFastQuery();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.log.error("--------Init initFastQuery Failture-----------");
        }
        

        
//        rs = initPinYin();
//        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//        	BaseEnv.log.error("--------Init PinYin Failture-----------");
//        }

        //初始化接口
        //initFunctionInterface();
        //BaseEnv.log.debug("--------Init Function Interface ---------------");
        
        //加密狗，执行define操作，如关闭属性
        int rda =initModuleDogAfter(servletContext);
        if(rda != 0){
        	initState = rda;
        }
        
        //初始电子商务默认网站地址
        Result result = new AIOShopMgt().queryAIOShop() ;
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	List<AIOShopBean> listShop = (List<AIOShopBean>) result.retVal ;
			if(listShop.size()>0){
				BaseEnv.AIO_SHOP = ((AIOShopBean)listShop.get(0)) ;
			}
        }
        //初始化短信功能
        if (initState == SystemState.DOG_FORMAL) {  //如果加密狗状态正常
        	BaseEnv.log.debug("--------Init SMS Start ---------------");
        	BaseEnv.telecomCenter = new AIOTelecomCenter();            
        }
        
        //初始化KK消息接口
        MSGConnectCenter msgCenter = new MSGConnectCenter();
        String msgPort = GlobalsTool.getSysSetting("msgPort");
        int port = 0;
        port = Integer.parseInt(msgPort);
        boolean init = msgCenter.init(Logger.getLogger("MSGLog"), port);
        if (!init) {
        	BaseEnv.log.error("--------Init MSGConnectCenter Failture-----------");
        }
        
        //所有初始化完成且成功后，置成功标志
        SystemState.instance.dogState = initState;
        if(SystemState.instance.dogState == SystemState.NORMAL){
        	BaseEnv.log.info("-------System init Secussful Set Normal status--------------");
        }else{
        	BaseEnv.log.info("-------System init Secussful Set EVALUATE status--------------");
        }
        SystemState.instance.lastErrorCode = SystemState.NORMAL;
        
        return SystemState.instance.dogState == SystemState.NORMAL ||
                SystemState.instance.dogState == SystemState.DOG_EVALUATE;
    }

    /**
    *
    * @return int 0 :正常，1超时，2单据过量
    */
    public int checkEnv() {    	
////    	try {
////			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
////			long ti = sdf.parse(SystemState.instance.evaluate_date).getTime();
////			String date = sdf.format(new Date());
////			if ((System.currentTimeMillis() - ti) / (24 * 60 * 60000) > 30) {
////				return 1;
////			}
////		} catch (Exception e) {
////			BaseEnv.log.fatal("InitMenData.checkEnv evaluate_date is Error:::"
////					+ SystemState.instance.evaluate_date);
////		}
//    	
//    	// 进销存模块的检查
//        // 查询采购，销售，库存表的，
//        final Result res = new Result();
//        int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//                session.doWork(new Work() {
//                    public void execute(Connection connection) throws
//                            SQLException {
//                        try {
//                            Connection conn = connection;
//                            //查出入库明细表时间 
//                            String sql;
//                            PreparedStatement psmt;
//                            ResultSet rst;
////                            String sql =
////                                " select  min(createTime) from tblStockDet ";
////                            PreparedStatement psmt = conn.prepareStatement(sql);     
////                            ResultSet rst = psmt.executeQuery();
////                            if (rst.next()) {
////                                String ct = rst.getString(1);
////                                if (ct != null) {
////                                    java.util.Date d = com.menyi.aio.bean.BaseDateFormat.parse(ct,
////                                            com.menyi.aio.bean.BaseDateFormat.
////                                            yyyyMMddHHmmss);
////                                    if ((System.currentTimeMillis() - d.getTime()) /
////                                        (24 * 60 * 60000) >
////                                        30) {
////                                        res.retVal = 1;
////                                        return;
////                                    }
////                                }
////                            }                            
//
//                            //查单据汇总表数量
//                            sql = " select  count(*) from tblSalesOutStock ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 200) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            } 
//                            sql = " select  count(*) from tblBuyInStock ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 200) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            } 
//                            
//                            //查客户列表时间
////                            sql = " select  min(createTime) from CRMClientInfo ";
////
////                            psmt = conn.prepareStatement(sql);
////                            rst = psmt.executeQuery();
////                            if (rst.next()) {
////                                String ct = rst.getString(1);
////                                if (ct != null) {
////                                    java.util.Date d = com.menyi.aio.bean.BaseDateFormat.parse(ct,
////                                            com.menyi.aio.bean.BaseDateFormat.
////                                            yyyyMMddHHmmss);
////                                    if ((System.currentTimeMillis() - d.getTime()) /
////                                        (24 * 60 * 60000) >
////                                        30) {
////                                        res.retVal = 1;
////                                        return;
////                                    }
////                                }
////                            }
//                            
//                            //查客户列表数量
//                            sql = " select  count(*) from CRMClientInfo ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 200) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            } 
//                            //查工作流时间
////                            sql = " select  min(createTime) from oamyworkFlow ";
////                            psmt = conn.prepareStatement(sql);
////                            rst = psmt.executeQuery();
////                            if (rst.next()) {
////                                String ct = rst.getString(1);
////                                if (ct != null) {
////                                    java.util.Date d = com.menyi.aio.bean.BaseDateFormat.parse(ct,
////                                            com.menyi.aio.bean.BaseDateFormat.
////                                            yyyyMMddHHmmss);
////                                    if ((System.currentTimeMillis() - d.getTime()) /
////                                        (24 * 60 * 60000) >
////                                        30) {
////                                        res.retVal = 1;
////                                        return;
////                                    }
////                                }
////                            }
//                            
//                            //查工作流数量
//                            sql = " select  count(*) from oamyworkFlow ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 200) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            } 
//                            
//                            //查人事时间
////                            sql = " select  min(createTime) from HREmpinform where employeeId<>'1' "; //
////                            psmt = conn.prepareStatement(sql);
////                            rst = psmt.executeQuery();
////                            if (rst.next()) {
////                                String ct = rst.getString(1);
////                                if (ct != null) {
////                                    java.util.Date d = com.menyi.aio.bean.BaseDateFormat.parse(ct,
////                                            com.menyi.aio.bean.BaseDateFormat.
////                                            yyyyMMddHHmmss);
////                                    if ((System.currentTimeMillis() - d.getTime()) /
////                                        (24 * 60 * 60000) >
////                                        30) {
////                                        res.retVal = 1;
////                                        return;
////                                    }
////                                }
////                            }
//                            //查用户数量 ERP3个，OA20个，CRM5个
//                            sql = " select COUNT(0) from tblEmployee where openFlag=1 and canJxc=1 ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 3) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            } 
//                            sql = " select COUNT(0) from tblEmployee where openFlag=1 and canoa=1 ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 20) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            }
//                            sql = " select COUNT(0) from tblEmployee where openFlag=1 and canCRM=1 ";
//                            psmt = conn.prepareStatement(sql);
//                            rst = psmt.executeQuery();
//                            if (rst.next()) {
//                                int ct = rst.getInt(1);
//                                if (ct > 5) {
//                                    res.retVal = 2;
//                                    return;
//                                }
//                            }
//                            
//                            
//                        } catch (Exception ex) {
//                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                        }
//                    }
//                });
//                return res.getRetCode();
//            }
//        });
//        res.setRetCode(retCode);
//
//        if (res.retCode == ErrorCanst.DEFAULT_SUCCESS && res.retVal != null) {
//            if ("1".equals(res.retVal.toString())) {
//                return 1;
//            } else if ("2".equals(res.retVal.toString())) {
//                return 2;
//            }
//        }
        return 0;
    }
    /**
     * 记录软加密认证日志
     * @param type
     * @return
     */
    private void logCert(final String certStr,final String md5,final String rs,final String pcNo) {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String[]  certs = certStr.split("Koron");
                        certs[1] = "["+certs[1]+"]";
                        certs[2] = "["+certs[2]+"]";
                        certs[3] = "["+certs[3]+"]";
                        //查询最后一行记录是否一致
                        String sql =
                                " select top 1 HDDID,MACID,CPUID from tblCertLog order by id desc";
                        PreparedStatement psmt = conn.prepareStatement(sql);
                        ResultSet rst = psmt.executeQuery();
                        boolean same = false;
                        if (rst.next()) {
                            if(certs[1].equals(rst.getString(1)) && certs[2].equals(rst.getString(2)) && certs[3].equals(rst.getString(3))){
                            	same = true;
                            }
                        } 
                        if(!same){
                        	sql = "insert into tblCertLog(HDDID,MACID,CPUID,MD5ID,PCNO,createTime,DLLNO) values(?,?,?,?,?,?,?) ";
                        	psmt = conn.prepareStatement(sql);
                        	psmt.setString(1, certs[1]);
                        	psmt.setString(2, certs[2]);
                        	psmt.setString(3, certs[3]);
                        	psmt.setString(4, md5);
                        	psmt.setString(5, pcNo);
                        	psmt.setString(6, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                        	psmt.setString(7, rs);
                            psmt.execute();
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
    }
    
    private int getModuleNum(final int type) {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String sql =
                                "select count(*) from tblmodules where classcode in ( " +
                                "select classcode from tblmodules where  classcode not in ( " +
                                "select distinct t1.classcode from tblmodules t1 " +
                                "        inner join tblmodules t2 on  " +
                                "                t1.classcode = substring(t2.classCode,1,len(t2.classCode)-5)  " +
                                ")   and substring(classCode,1,5) in  " +
                                "(select  t3.classcode from tblmodules t3 where len(t3.classCode) =5 and t3.mainmodule=?)) " +
                                "and isUsed=1 and  linkAddress <>''";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        psmt.setInt(1, type);
                        ResultSet rst = psmt.executeQuery();
                        if (rst.next()) {
                            int count = rst.getInt(1);
                            res.retVal = count;
                        } else {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res.retCode == ErrorCanst.DEFAULT_SUCCESS ?
                Integer.parseInt(res.retVal.toString()) : 0;
    }

    private int getDBUserNum(final String type) {
        final Result res = new Result();
        res.setRetVal(new ArrayList());
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        //查主表
                        String querysql =
                                " select count(*) from tblEmployee where openFlag = 1 and statusid=0 and id <> '1'";
                        if("1".equals(type)){
                        	querysql += " and canJxc=1 ";
                        }else if("2".equals(type)){
                        	querysql += " and canOa=1 ";
                        }else if("3".equals(type)){
                        	querysql += " and canCrm=1 ";
                        }else if("4".equals(type)){
                        	querysql += " and canHr=1 ";
                        }else if("10".equals(type)){
                        	querysql += " and canOrders=1 ";
                        }
                        try {
                            PreparedStatement cs = conn.prepareStatement(
                                    querysql);
                            BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            int count = 0;
                            if (rset.next()) {
                                count = rset.getInt(1);
                            }
                            res.setRetVal(count + "");
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.
                                           DEFAULT_FAILURE);
                            BaseEnv.log.error(
                                    "Query data Error InitMenDate.getDBUserNum :" +
                                    querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        if (res.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            return Integer.parseInt(res.getRetVal().toString());
        } else {
            return 0;
        }
    }

    /**
     * 初始化用户自定义列名设置
     * @return
     */
    public Result initUserLanuageInfo(ServletContext servletContext) {
        final Hashtable<String,
                        ColDisplayBean> map = new Hashtable<String,
                ColDisplayBean>();
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select * from tblUserLanguage";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rss = ps.executeQuery();
                            while (rss.next()) {
                                ColDisplayBean colBean = new ColDisplayBean();
                                colBean.setId(rss.getString("id"));
                                colBean.setTableName(rss.getString("tableName"));
                                colBean.setColName(rss.getString("colName"));
                                colBean.setLanguageId(rss.getString("languageId"));
                                map.put(colBean.getTableName() + colBean.getColName(), colBean);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        servletContext.setAttribute("userLanguage", map);
        BaseEnv.userLanguage=map;
        return rs;
    }
    

    /**
     * 初始化用户模块自定义列名设置
     * @return
     */
    public Result initModuleColLanuage(ServletContext servletContext) {
        final Hashtable<String,KRLanguage> map = new Hashtable<String, KRLanguage>();
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select * from tblModelColLanguage";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rss = ps.executeQuery();
                            while (rss.next()) {
                                String tableName = rss.getString("tableName") ;
                                String popupName = rss.getString("popupName") ;
                                String popFieldName = rss.getString("popFieldName") ;
                                String fieldType = rss.getString("fieldType") ;
                                String language = rss.getString("languageId") ;
                                KRLanguage display = KRLanguageQuery.create(GlobalsTool.getLocaleDisplay(language, "zh_CN"),
                                					GlobalsTool.getLocaleDisplay(language, "en"),GlobalsTool.getLocaleDisplay(language, "zh_TW"));
                                map.put(tableName+popupName+popFieldName+fieldType, display);
                            }
                        } catch (Exception ex) {
                        	BaseEnv.log.error("InitMenData initModuleColLanuage :",ex) ;
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        servletContext.setAttribute("moduleColLanguage", map);
        return rs;
    }

    /**
     * 初始化用户自定义列宽设置
     * @return
     */
    public Result initUserColWidth(ServletContext servletContext) {
        final Hashtable<String,
                        ColDisplayBean> map = new Hashtable<String,
                ColDisplayBean>();
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select * from tblUserWidth";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rss = ps.executeQuery();
                            while (rss.next()) {
                                ColDisplayBean colBean = new ColDisplayBean();
                                colBean.setId(rss.getString("id"));
                                colBean.setTableName(rss.getString("tableName"));
                                colBean.setColName(rss.getString("colName"));
                                colBean.setColWidth(rss.getString("colWidth"));
                                map.put(colBean.getTableName() +
                                        colBean.getColName(), colBean);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        servletContext.setAttribute("userSettingWidth", map);
        BaseEnv.userSettingWidth=map;
        return rs;
    }

    /**
     * 初始化所有已启用的工作流信息
     * @return
     */
    public Result initWorkFlowInfo(ServletContext servletContext) {
        final Hashtable<String,OAWorkFlowTemplate> map = new Hashtable<String,OAWorkFlowTemplate>();
        final Result rs = new Result();
        long start = System.currentTimeMillis();
        String hql = "from OAWorkFlowTemplate template ";
        ArrayList<String> param = new ArrayList<String>();
        Result result = list(hql, param);
        
        ArrayList<String[]> isStartInfos=new ArrayList();
        HashMap tempMap=new HashMap();
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal!=null){
        	ArrayList<OAWorkFlowTemplate> tempList = (ArrayList<OAWorkFlowTemplate>) result.retVal ;
        	PublicMgt pMgt = new PublicMgt();
        	for(OAWorkFlowTemplate template : tempList){
        		if(0==template.getStatusId()){
                	map.put(template.getTemplateFile(), template) ;
                }
        		map.put(template.getId(), template);
        		if(template.getLines()!=null && template.getLines().length()>0){
        				tempMap.put(template.getId(), pMgt.getWorkFlowDesign(template.getId(),null));
        		}else{
        			if(template.getWorkFlowFile()!=null && template.getWorkFlowFile().length()>0){ 
        				//加载OA的工作流设计文件
        				tempMap.put(template.getId(), ReadXML.parse(template.getWorkFlowFile()));
        			}
        		}
        		isStartInfos.add(new String[]{template.getTemplateFile()+"_IsStatart",template.getTemplateStatus()+""});
        		        		
        	}
        }
        BaseEnv.isStartFlowInfo = isStartInfos;
        BaseEnv.workFlowDesignBeans = tempMap;
        servletContext.setAttribute("workFlowInfo", map);
        servletContext.setAttribute("workFlowDesignBeans", BaseEnv.workFlowDesignBeans);
        BaseEnv.workFlowInfo=map;
        long end = System.currentTimeMillis();
        System.out.println("workflow init time:"+(end-start));
        return rs;
    }
    
    /**
     * 初始化用户自定义列配置设置
     * @return
     */
    public Result initUserColConfig(ServletContext servletContext) {
        final Hashtable<String,
        			ArrayList<ColConfigBean>> map = new Hashtable<String,ArrayList<ColConfigBean>>();
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select * from tblColConfig where isNull(userid,'')=''";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rss = ps.executeQuery();
                            while (rss.next()) {
                                ColConfigBean colBean = new ColConfigBean();
                                colBean.setId(rss.getString("id"));
                                colBean.setTableName(rss.getString("tableName"));
                                colBean.setColName(rss.getString("colName"));
                                colBean.setColType(rss.getString("colType"));
                                colBean.setColIndex(rss.getInt("colIndex")) ;
                                colBean.setIsNull(rss.getInt("isNull")) ;
                                colBean.setFieldName(rss.getString("fieldName"));
                                colBean.setDisplay(rss.getString("display"));
                                colBean.setLock(rss.getInt("lock"));
                                colBean.setPopupName(rss.getString("popupName"));
                                if(colBean.getPopupName()!=null && colBean.getPopupName().length()>0){
                                	if(map.containsKey(colBean.getPopupName()+colBean.getTableName()+colBean.getColType())){
	                                	ArrayList<ColConfigBean> configList = map.get(colBean.getPopupName()+colBean.getTableName()+colBean.getColType()) ;
										for (int i = 0; i < configList.size(); i++) {
											ColConfigBean colConfigBean = configList.get(i);
											if (colConfigBean.getColName().toLowerCase().equals(colBean.getColName().toLowerCase())) {
												configList.remove(i);
												i--;
											}
										}
	                                	configList.add(colBean) ;
	                                }else{
	                                	ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;
	                                	configList.add(colBean) ;
	                                	map.put(colBean.getPopupName()+colBean.getTableName()+colBean.getColType(), configList) ;
	                                }
                                }else{
	                                if(map.containsKey(colBean.getTableName()+colBean.getColType())){
	                                	ArrayList<ColConfigBean> configList = map.get(colBean.getTableName()+colBean.getColType()) ;
										for (int i = 0; i < configList.size(); i++) {
											ColConfigBean colConfigBean = configList.get(i);
											if (colConfigBean.getColName().toLowerCase().equals(colBean.getColName().toLowerCase())) {
												configList.remove(i);
												i--;
											}
										}
	                                	configList.add(colBean) ;
	                                }else{
	                                	ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;
	                                	configList.add(colBean) ;
	                                	map.put(colBean.getTableName()+colBean.getColType(), configList) ;
	                                }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        servletContext.setAttribute("userSettingColConfig", map);
        BaseEnv.colConfig=map;
        return rs;
    }
    
    /**
     * 初始化未办的日历
     * @return
     */
    private Result selectMyCalendar() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql =
                                    "select * from OAMyCalendar where calendarDate>Getdate() and waleUpTimes>0";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rss = ps.executeQuery();
                            ArrayList<OAMyCalendar>
                                    list = new ArrayList<OAMyCalendar>();
                            while (rss.next()) {
                                OAMyCalendar calendar = new OAMyCalendar();
                                calendar.setid(rss.getString("id"));
                                calendar.setCalendarDate(rss.getString(
                                        "CalendarDate"));
                                calendar.setWaleUpTimes(rss.getInt(
                                        "WaleUpTimes"));
                                calendar.setSetp(rss.getInt("Setp"));
                                calendar.setWakeUpType(rss.getString(
                                        "wakeuptype"));
                                calendar.setCalendarTitle(rss.getString(
                                        "calendartitle"));
                                calendar.setCalendarContext(rss.getString(
                                        "calendarcontext"));
                                calendar.setcreateBy(rss.getString("createby"));
                                list.add(calendar);
                            }
                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            rs.setRetVal(list);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        return rs;
    }
    
    
    /**
     * 取bol88推送消息，且传送端口
     * @return
     */
    public static Result getLastInfoId() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select max(id) from tblInfo";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rss = ps.executeQuery();                            
                            if (rss.next()) {
                            	rs.retVal = rss.getInt(1);
                            }
                            if(rs.retVal == null){
                            	rs.retVal = 0;
                            }
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            BaseEnv.log.error("InitMenData.getLastInfoId 取bol88消息，且传送端口 error",ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        return rs;
    }
    /**
     * 保存消息到数据库
     * @return
     */
    public static Result saveInfo(final String id,final String content) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "insert into tblInfo(id,IContent,createTime) values(?,?,?)";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, id);
                            ps.setString(2, content);
                            ps.setString(3, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                            ps.executeUpdate();
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        return rs;
    }
    
    /**
     * 保存消息到数据库
     * @return
     */
    public static Result initFastQuery() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "select title,url from tblFastQuery order by id";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ResultSet rset =ps.executeQuery();
                            ArrayList list = new ArrayList();
                            while(rset.next()){
                            	list.add(new String[]{rset.getString(1),rset.getString(2)});
                            }
                            rs.retVal = list;
                            BaseEnv.fastQuery= list;
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        return rs;
    }

    /**
     * 初始化拼音码
     * @return
     */
//    private Result initPinYin() {
//        final Result rs = new Result();
//        int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//                session.doWork(new Work() {
//                    public void execute(Connection conn) throws SQLException {
//                        try {
//                            String sql =
//                                    "select PY,HZ1,HZ2 from pinyin";
//                            PreparedStatement ps = conn.prepareStatement(sql);
//                            ResultSet rss = ps.executeQuery();
//                            ArrayList<String[]>
//                                    list = new ArrayList<String[]>();
//                            while (rss.next()) {
//                                list.add(new String[]{rss.getString("HZ1"),rss.getString("HZ2"),rss.getString("PY")});
//                            }
//                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
//                            BaseEnv.pinYinList=list;
//                            rs.setRetVal(list);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
//                            return;
//                        }
//                    }
//                });
//                return rs.getRetCode();
//            }
//        });
//        rs.setRetCode(retCode);
//        return rs;
//    }

    /**
     * 系统启动时进行的初始化活动，如果初始化不成功，则表ConfigRefresh定时刷新，直到成功为止
     * @param servletContext ServletContext
     */
    public void initSystem(ServletContext servletContext) {
        Logger log4j = Logger.getLogger("MYLog");

        
        /**
         * 初始化日志系统和 hibernate
         */
        try {
            DBUtil.initHibnate(log4j, new File("../../config/hibernate.cfg.xml"));
            ConnectionEnv.getConnection();
            //初始化多数据库
            //1、加载多数据库配置文件
            File file = new File("../../config/db.xml");
            if(file.exists()){
            	BaseEnv.log.info("*****************此系统配置有多个数据*************");
            	try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    Document document = dbf.newDocumentBuilder().parse(file);
                    Node config = document.getFirstChild();
                    NodeList nodeList = config.getChildNodes();         
                    
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node tempNode = nodeList.item(i);
                        if (tempNode.getNodeName().equalsIgnoreCase("db")) {
                            String name = tempNode.getAttributes().getNamedItem("name").getNodeValue();
                            String s = tempNode.getFirstChild().getNodeValue();
                            BaseEnv.log.info("多数据库 name="+name+"; config="+s);
                            DBUtil.initHibnate(new File(s),name);
                            
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    BaseEnv.log.error("-----------InitMemData.initSystem error----------------",ex);
                }
            }
            
            
        } catch (Exception e) {
            log4j.error("--------Init Hibernate Failture-------: ", e);
            SystemState.instance.lastErrorCode = SystemState.DB_CONNECT;
            //初始化数据库不成功，则退出初始化程序，交由ConfigRefresh线程的下一个周期完成
            return;
        }
        log4j.debug("--------Init Hibernate Successful---------------");
        

        /**
         * 初始化加密狗
         */
        try {
            if (!initDog(servletContext)) {
                log4j.error("--------Init Dog Failture-----------");
                //初始狗不成功后，要退出，等待下一次重启机会
                return;
            } else {
                log4j.debug("--------Init Dog Successful---------------");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    

    /**
     * 读取配置文件到内存中，包包括自定义sql语句，弹出窗等
     */
    public void initConfigFile() {

    	 //定时初始化系统配置文件
        if (!BaseConfig.parseConfig("../../config/Config.xml")) {
            BaseEnv.log.error(
                    "--------Refresh BaseConfig Failture---------------");
        }

        //定时初始化SQL配置文件
        if (!SqlConfig.parseConfig()) {
            BaseEnv.log.error(
                    "--------Refresh SqlConfig Failture---------------");
        }

        //定时初始化SQL配置文件
        if (!PopSelectConfig.parseConfig()) {
            BaseEnv.log.error(
                    "--------Refresh PopSelectConfig Failture---------------");
        }
        //定时初始化扩展按扭配置文件
        if (!DefineButtonBean.parseConfig()) {
            BaseEnv.log.error(
                    "--------Refresh DefineButton Failture---------------");
        }
        //定时初始化自定义告警文件
        if (!AlertConfig.parseConfig()) {
            BaseEnv.log.error(
                    "--------Refresh AlertConfig Failture---------------");
        }
        //定时初始化自定义邮件及短信配置文件
        if (!NoteConfig.parseConfig()) {
            BaseEnv.log.error(
                    "--------Init TimingConfig Failture---------------");
        }

    }
    
    private void writeNomalPath(final String dogId,String spath){
    	if(spath == null || spath.length() <1){
    		return;
    	}
    	spath = spath.substring(0,spath.length() -1);
    	final String path  = spath;
    	
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "SELECT   count(*)   FROM   master.dbo.sysobjects   where   name='tblServerAccount'";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        ResultSet rst = psmt.executeQuery();
                        if (rst.next()) {
                            int count = rst.getInt(1);
                            res.retVal = count;
                        } else {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        if(res.retCode == ErrorCanst.DEFAULT_SUCCESS && Integer.parseInt(res.retVal.toString()) == 0){
	        retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                        SQLException {
	                        Connection conn = connection;
	                        String sql =
	                            "                        CREATE TABLE master.dbo.[tblServerAccount] ( " +
	                            "                            [id] [int] IDENTITY (1, 1) NOT NULL , " +
	                            "                            [dogId] [varchar] (200) COLLATE Chinese_PRC_CI_AS NULL , " +
	                            "                            [spath] [varchar] (200) COLLATE Chinese_PRC_CI_AS NULL " +
	                            ") ON [PRIMARY] ";
	                        PreparedStatement psmt = conn.prepareStatement(sql);
	                        int r = psmt.executeUpdate();                        
	                        res.retVal = r;
	                    }
	                });
	                return res.getRetCode();
	            }
	        });
	        res.setRetCode(retCode);	        
        }
        retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            " select spath from  master.dbo.tblServerAccount where dogid=? ";
                        PreparedStatement psmt = conn.prepareStatement(sql);
                        psmt.setString(1, dogId);
                        ResultSet rst = psmt.executeQuery();
                        if (rst.next()) {
                        	sql =
	                            " update master.dbo.tblServerAccount set spath =? where dogid=? ";
	                        psmt = conn.prepareStatement(sql);
	                        psmt.setString(1, path);
	                        psmt.setString(2, dogId);	                        
	                        int r = psmt.executeUpdate();                        
	                        res.retVal = r;
                        }else{                        
	                        sql =
	                            " insert into master.dbo.tblServerAccount(dogId,spath) values(?,?) ";
	                        psmt = conn.prepareStatement(sql);
	                        psmt.setString(1, dogId);
	                        psmt.setString(2, path);
	                        int r = psmt.executeUpdate();                        
	                        res.retVal = r;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);	    
        
    }
    
    

}
