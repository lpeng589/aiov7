package com.koron.crm.brother;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.BrotherFieldScopeBean;
import com.koron.crm.bean.CRMBrotherCommentBean;
import com.koron.crm.client.CRMClientMgt;
import com.koron.oa.OACalendar.OACalendarBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

/**
 * 
 * <p>Title:CRM兄弟表</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 17, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class CRMBrotherMgt extends AIODBManager{
	
	/**
	 * 兄弟表列表查询
	 * @param condition 条件
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Result query(String tableName,String childTableName,BrotherFieldDisplayBean fieldDisplayBean,String condition,String sortInfo,int pageNo, int pageSize,String keyInfo,String allocateStatus,String loginId){
		
		String listFields = tableName+".id,";
		for(String str : fieldDisplayBean.getListFields().split(",")){
			if("".equals(str)){
				continue;
			}
			if(str.indexOf("child") == -1){
				listFields += tableName +"."+str+",";
			}else{
				str = str.substring(5);
				listFields += childTableName +"."+str+",";
			}
		}
		
		String sql = "SELECT ";

		if(keyInfo!=null && keyInfo.indexOf("CRMPotentialClient")>-1){
			sql+=" CRMPotentialClient.clientName,";
		}else if(!"CRMPotentialClient".equals(tableName)){
			sql+=" CRMClientInfo.clientName,";
		}
		
		sql+=tableName+".checkPersons,"+tableName+".createBy,"+tableName+".workFlowNode,"+listFields+"row_number() over(order by ";
		
		if(sortInfo == null || "".equals(sortInfo)){
			//如果是线索，且有预约时间字段，则按照预约时间字段排序
			if(tableName!=null && tableName.indexOf("CRMPotentialClient")>-1){
//				如果是当天，则排在最前面
				if(GlobalsTool.getFieldBean("CRMPotentialClient", "yysj")!=null){				
					sql+="(case when subString(CRMPotentialClient.yysj,1,10)=CONVERT(varchar(100), GETDATE(), 23) then '9999-12-31' else '' end) desc,";
				}
				if(GlobalsTool.getFieldBean("CRMPotentialClient", "hqsj")!=null){				
					sql+="hqsj desc,";
				}
			}	
			sql += tableName+".lastUpdateTime desc";
		}else{
			sql +=tableName+"."+sortInfo.split(",")[1] + " " + sortInfo.split(",")[0];
		}
		
		sql+=") row_id FROM "+tableName;
		
		if(keyInfo!=null && keyInfo.indexOf("CRMPotentialClient")>-1){
			sql+=" LEFT JOIN CRMPotentialClient ON "+tableName+".ClientId = CRMPotentialClient.id ";
		}else if(!"CRMPotentialClient".equals(tableName)){
			sql+=" LEFT JOIN CRMClientInfo ON "+tableName+".ClientId = CRMClientInfo.id ";
		}

		
		/*
		if(childTableName!=null && !"".equals(childTableName)){
			sql+=" LEFT JOIN "+childTableName +" ON "+tableName+".id="+childTableName+".f_ref ";
		}
		*/	
		sql += " WHERE 1=1 "+condition;
		
		if((keyInfo==null || keyInfo.indexOf("CRMPotentialClient")==-1) && !"CRMPotentialClient".equals(tableName)){
			sql +=" and isnull(CRMClientInfo.clientName,'') !='' ";
		}
		//如果是线索，查询符合状态的数据
//		if("CRMPotentialClient".equals(tableName)){
//			if("hasAllocatee".equals(allocateStatus)){
//				//已分配：分配者中有当前登录用户记录，则为已分配记录
//				sql+=" and EmployeeID!='"+loginId+"'";//','+AllocateeS like '%,"+loginId+",%'";
//			}
//		}
		
		BaseEnv.log.debug(sql);
		return sqlListMaps(sql,new ArrayList(), pageNo, pageSize);
	} 
	/**
	 * 获取报表flash数据wyy
	 * @param condition
	 * @return
	 */
	public Result queryNumTol(String condition){
		ArrayList param = new ArrayList();
		String sql = "select count(SalesChancePhase) as num,isnull(sum(ExpectAmount),0) as toltal,SalesChancePhase, " +
				"1.00*count(SalesChancePhase)/(select case when count(SalesChancePhase)=0 then 1 else count(SalesChancePhase) end as k from CRMSalesChance LEFT JOIN CRMClientInfo ON CRMSalesChance.ClientId = CRMClientInfo.id WHERE 1=1 and CRMSalesChance.SalesChanceStatus='1' and cast(SalesChancePhase as int)>0 "+condition+") as numper,"+
				"1.00*sum(ExpectAmount)/(select case when isnull(cast(sum(ExpectAmount) as int),0)=0 then 1 else cast(sum(ExpectAmount) as int) end as kk from CRMSalesChance LEFT JOIN CRMClientInfo ON CRMSalesChance.ClientId = CRMClientInfo.id WHERE 1=1 and CRMSalesChance.SalesChanceStatus='1' and cast(SalesChancePhase as int)>0 "+condition+") as tolper from CRMSalesChance" +
						" LEFT JOIN CRMClientInfo ON CRMSalesChance.ClientId = CRMClientInfo.id";
		sql += " WHERE 1=? and CRMSalesChance.SalesChanceStatus='1' "+condition+" group by CRMSalesChance.SalesChancePhase order by num asc";
		param.add("1");
		System.out.println(sql);
		return sqlList(sql, param);
	} 
	/**
	 * 更加链接获取报表flash数据wyy
	 * @param condition
	 * @return
	 */
	public Result queryDetail(String prm,String tableName,String childTableName,BrotherFieldDisplayBean fieldDisplayBean,String condition,String sortInfo,int pageNo, int pageSize){
		String listFields = tableName+".id,";
		for(String str : fieldDisplayBean.getListFields().split(",")){
			if("".equals(str)){
				continue;
			}
			if(str.indexOf("child") == -1){
				listFields += tableName +"."+str+",";
			}else{
				str = str.substring(5);
				listFields += childTableName +"."+str+",";
			}
		}
		
		String sql = "SELECT CRMClientInfo.clientName,"+tableName+".checkPersons,"+tableName+".workFlowNode,"+listFields+"row_number() over(order by "+tableName+".";
		if(sortInfo == null || "".equals(sortInfo)){
			sql += " lastUpdateTime desc";
		}else{
			sql += sortInfo.split(",")[1] + " " + sortInfo.split(",")[0];
		}
		
		sql+=") row_id FROM "+tableName+" LEFT JOIN CRMClientInfo ON "+tableName+".ClientId = CRMClientInfo.id ";	
		sql += "WHERE 1=1 and CRMSalesChance.SalesChanceStatus='1' "+condition+" and CRMSalesChance.SalesChancePhase = "+prm;
		System.out.println(sql);
		return sqlListMaps(sql,new ArrayList(), pageNo, pageSize);
	} 
	/**
	 * 获取报表flash预测数据wyy
	 * @param condition
	 * @param beginTime
	 * @param endTime
	 * @param changeType
	 * @param flagValue
	 * @return
	 */
	public Result queryToltal(String condition,String beginTime,String endTime,String changeType,String flagValue){
		ArrayList param = new ArrayList();
		String sql = "select isnull(sum(ExpectAmount),0) as toltal from CRMSalesChance LEFT JOIN CRMClientInfo ON CRMSalesChance.ClientId = CRMClientInfo.id";					
		sql += " WHERE 1=? "+condition;
		if(!"".equals(beginTime) && beginTime !=null){
			sql += " and cast(expectsignbilltime as datetime) >='"+beginTime+"'";
		}
		if(!"".equals(endTime) && endTime !=null){
			sql += " and cast(expectsignbilltime as datetime) <='"+endTime+"'";
		}
		if(!"".equals(changeType) && changeType !=null){
			sql += " and cast(probability as int) >="+changeType;
		}
		if("2".equals(flagValue)){
			sql += " and SalesChanceStatus <> "+changeType;
		}
		param.add("1");
		return sqlList(sql, param);
	}
	
	/**
	 * 根据ID与时间获取最近几天的行程
	 * @param longinBean
	 * @param startTime
	 * @return
	 */
	public Result querySchedule(final LoginBean loginBean,final String today,final String endTime) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "select CRMSaleFollowUp.id,CRMClientInfo.clientName,CRMSaleFollowUp.NextVisitTime from CRMSaleFollowUp left join CRMClientInfo on CRMSaleFollowUp.ClientId = CRMClientInfo.id where employeeId =? and CRMSaleFollowUp.NextVisitTime >= ? and CRMSaleFollowUp.NextVisitTime <= ? ORDER BY CRMSaleFollowUp.NextVisitTime";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, loginBean.getId()) ;
							ps.setString(2, today);
							ps.setString(3, endTime);
							ResultSet rss = ps.executeQuery();
							
							LinkedHashMap<String,ArrayList<String[]>> rsMap = new LinkedHashMap<String, ArrayList<String[]>>();
							while(rss.next()){
								if(rsMap.get(rss.getString("NextVisitTime"))==null){
									ArrayList<String[]> list = new ArrayList<String[]>();
									String[] str = new String[2];
									str[0] = rss.getString("id");
									str[1] = rss.getString("clientName");
									list.add(str);
									rsMap.put(rss.getString("NextVisitTime"), list);
								}else{
									String[] str = new String[2];
									str[0] = rss.getString("id");
									str[1] = rss.getString("clientName");
									rsMap.get(rss.getString("NextVisitTime")).add(str);
								}
							}
							rst.setRetVal(rsMap) ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 添加
	 * @param tableName
	 * @param values
	 * @param loginBean
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result add(final String tableName,final String childTableName,final HashMap values,final LoginBean loginBean,final MessageResources resources,final Locale locale,final Map allTables,final String defineInfo,final String designId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
								//处理单据编号问题
							DBTableInfoBean tableInfo = (DBTableInfoBean)allTables.get(tableName);
								for(DBFieldInfoBean fieldBean : tableInfo.getFieldInfos()){
									if("BillNo".equals(fieldBean.getFieldIdentityStr())){
										if(values.get(fieldBean.getFieldName())==null || "".equals(values.get(fieldBean.getFieldName()))){
											String billNoValue = "";
											String name = tableName +"_"+fieldBean.getFieldName();
											if(fieldBean.getDefaultValue() != null && !"".equals(fieldBean.getDefaultValue())){
												name = fieldBean.getDefaultValue();
											}
											billNoValue = BillNoManager.find(name,loginBean,connection);
											values.put(fieldBean.getFieldName(), billNoValue);
										}
									}
								}
							
								String[] returnValue = new String[3] ;//返回值 
								Result defineRs = new Result();
								String id = values.get("id").toString();
								
								 //执行插入前自定义语句
					            defineRs = new DynDBManager().defineSql(connection, "add", false,tableName,(Hashtable)allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
					            if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					                rst.setRetCode(defineRs.getRetCode());
					                rst.setRetVal(defineRs.getRetVal());
					                BaseEnv.log.error("CRMBrotherMgt Before add defineSql Error code = " 
					                		+defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
					            }else{
					            	
					            	
					            	
					            	String mainSql = "INSERT INTO "+tableName +"(";
					            	String fields = "";//存放字段
					            	String character = "";//存放通配符
					            	Set keys = values.keySet();
					            	if(keys != null) {
					            		Iterator iterator = keys.iterator();
					            		while(iterator.hasNext()) {
					            			Object key = iterator.next();
					            			if(key.toString().indexOf("TABLENAME_")==-1 && !"tableName".equals(key.toString()) && values.get(key.toString()) !=null ){
					            				fields +=key.toString()+",";
					            				character += "?,";
					            			}
					            		}
					            	}
					            	
					            	if(fields.endsWith(",")){
					            		fields = fields.substring(0,fields.length()-1);
					            	}
					            	
					            	if(character.endsWith(",")){
					            		character = character.substring(0,character.length()-1);
					            	}
					            	
					            	mainSql +=fields +") VALUES("+character+")";
					            	
					            	System.out.println(mainSql);
					            	PreparedStatement ps = connection.prepareStatement(mainSql);
					            	int i=1;
					            	for(String field : fields.split(",")){
					            		String val = "";
					            		if(values.get(field) != null){
					            			val = values.get(field).toString();
					            		}
					            		ps.setString(i,val);
					            		i++;
					            	}
					            	ps.executeUpdate();
					            	

					            	//处理明细
					            	ArrayList<HashMap> childList = (ArrayList<HashMap>)values.get("TABLENAME_"+childTableName);
					            	if(childList!=null && childList.size()>0){
//					            		//删除明细
//					            		String delChildSql = "DELETE FROM "+childTableName +" WHERE f_ref='"+values.get("id").toString()+"'";
//						            	PreparedStatement delPs = connection.prepareStatement(delChildSql);
//						            	delPs.executeUpdate();
					            		
						            	
						            	//新增明细
						            	for(HashMap map : childList){
						            		String childSql = "INSERT INTO "+childTableName +"(id,f_ref,";
						            		String childFields = "";//存放字段
						            		String childCharacter = "";//存放通配符
						            		Set childKey = map.keySet();
							            	if(childKey != null) {
							            		Iterator iterator = childKey.iterator();
							            		while(iterator.hasNext()) {
							            			Object key = iterator.next(); 
							            			if(!"tableName".equals(key.toString())&& !"detOrderNo".equals(key.toString()) && !"id".equals(key.toString())   && map.get(key.toString()) !=null ){
							            				childFields +=key.toString()+",";
							            				childCharacter += "?,";
							            			}
							            		}
							            	}
							            	if(childFields.endsWith(",")){
							            		childFields = childFields.substring(0,childFields.length()-1);
							            	}
							            	
							            	if(childCharacter.endsWith(",")){
							            		childCharacter = childCharacter.substring(0,childCharacter.length()-1);
							            	}
							            	
							            	childSql +=childFields +") VALUES(?,?,"+childCharacter+")";
							            	
							            	PreparedStatement childPs = connection.prepareStatement(childSql);
							            	
							            	childPs.setString(1,IDGenerater.getId());
							            	childPs.setString(2,values.get("id").toString());
							            	int j=3;
							            	for(String field : childFields.split(",")){
							            		String val = "";
							            		if(map.get(field) != null){
							            			val = map.get(field).toString();
							            		}
							            		childPs.setString(j,val);
							            		j++;
							            	}
							            	childPs.executeUpdate();
						            	}
					            	}
					            	
					            	
					            	//处理审核流
					            	if(designId!=null && !"".equals(designId)){
					            		//启用了审核流插入数据
					            		new OAMyWorkFlowMgt().addOAMyWorkFlow(designId, tableName, values, loginBean, locale, resources,connection);
					            	}
					            	
					            	
					            	
					            	if(!"CRMPotentialClient".equals(tableName)){
					            		//插入客户历史记录
					            		DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(tableName);
					            		String tableDisplay = "添加了"+tableBean.getDisplay().get(locale.toString());
					            		insertCRMCLientInfoLog(tableDisplay, values.get("ClientId").toString(), loginBean, connection);
					            	}
					            	
					            	
					            	
					            	
					            	//执行表自定义语句
					            	defineRs = new DynDBManager().defineSql(connection, "add", true, tableName,(Hashtable) allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
					            	if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					            		rst.setRetCode(defineRs.getRetCode());
					            		rst.setRetVal(defineRs.getRetVal());
					            		if(defineRs.getRetVal() instanceof String[]){
					            			String retVals[] = (String[])defineRs.getRetVal();
					            			BaseEnv.log.error(
					            					"DynDBManager After add defineSql Error code = " +
					            					defineRs.getRetCode() + " Msg=" +
					            					new DynDBManager().getResource(resources,locale,retVals));
					            		}else{
					            			BaseEnv.log.error(
					            					"DynDBManager After add defineSql Error code = " +
					            					defineRs.getRetCode() + " Msg=" +defineRs.getRetVal());
					            		}
					            	}
					            	if(defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
					            		rst.setRetCode(defineRs.retCode) ;
					            		returnValue[1] = (String)defineRs.retVal ;
					            	}
					            }
							
							
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 修改
	 * @param keyId
	 * @param tableName
	 * @param values
	 * @param loginBean
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result update(final String keyId,final String tableName,final String childTableName,final HashMap values,final LoginBean loginBean,final MessageResources resources,final Locale locale,final Map allTables,final String defineInfo) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
								String[] returnValue = new String[3] ;//返回值 
								Result defineRs = new Result();
								String id = values.get("id").toString();
								
								 //执行插入前自定义语句
					            defineRs = new DynDBManager().defineSql(connection, "update", false,tableName,(Hashtable)allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
					            if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					                rst.setRetCode(defineRs.getRetCode());
					                rst.setRetVal(defineRs.getRetVal());
					                BaseEnv.log.error("CRMBrotherMgt Before update defineSql Error code = " 
					                		+defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
					            }else{
					            	String mainSql = "UPDATE "+tableName+" SET ";
					            	String fields = "";//存放字段
					            	Set keys = values.keySet();
					            	if(keys != null) {
					            		Iterator iterator = keys.iterator();
					            		while(iterator.hasNext()) {
					            			Object key = iterator.next();
					            			if(key.toString().indexOf("TABLENAME_")==-1 && values.get(key.toString())!=null && !"tableName".equals(key.toString())){
					            				mainSql +=key.toString()+"=?,";
					            				fields += key.toString()+",";
					            			}
					            		}
					            	}
					            	if(fields.endsWith(",")){
					            		fields = fields.substring(0,fields.length()-1);
					            	}
					            	
					            	if(mainSql.endsWith(",")){
					            		mainSql = mainSql.substring(0,mainSql.length()-1);
					            	}
					            	mainSql +=" WHERE id='"+keyId +"'";
					            	
					            	System.out.println(mainSql);
					            	PreparedStatement ps = connection.prepareStatement(mainSql);
					            	int i=1;
					            	for(String field : fields.split(",")){
					            		String val = "";
					            		if(values.get(field) != null){
					            			val = values.get(field).toString();
					            		}
					            		ps.setString(i,val);
					            		i++;
					            	}
					            	ps.executeUpdate();
					            	
					            	//处理明细
					            	ArrayList<HashMap> childList = (ArrayList<HashMap>)values.get("TABLENAME_"+childTableName);
					            	if(childList!=null && childList.size()>0){
//				            		//删除明细
					            		String delChildSql = "DELETE FROM "+childTableName +" WHERE f_ref='"+values.get("id").toString()+"'";
					            		PreparedStatement delPs = connection.prepareStatement(delChildSql);
					            		delPs.executeUpdate();
					            		
					            		
					            		//新增明细
					            		for(HashMap map : childList){
					            			String childSql = "INSERT INTO "+childTableName +"(id,f_ref,";
					            			String childFields = "";//存放字段
					            			String childCharacter = "";//存放通配符
					            			Set childKey = map.keySet();
					            			if(childKey != null) {
					            				Iterator iterator = childKey.iterator();
					            				while(iterator.hasNext()) {
					            					Object key = iterator.next();
					            					if(!"tableName".equals(key.toString()) && !"detOrderNo".equals(key.toString())){
					            						childFields +=key.toString()+",";
					            						childCharacter += "?,";
					            					}
					            				}
					            			}
					            			if(childFields.endsWith(",")){
					            				childFields = childFields.substring(0,childFields.length()-1);
					            			}
					            			
					            			if(childCharacter.endsWith(",")){
					            				childCharacter = childCharacter.substring(0,childCharacter.length()-1);
					            			}
					            			
					            			childSql +=childFields +") VALUES(?,?,"+childCharacter+")";
					            			
					            			PreparedStatement childPs = connection.prepareStatement(childSql);
					            			childPs.setString(1,IDGenerater.getId());
					            			childPs.setString(2,values.get("id").toString());
					            			int j=3;
					            			for(String field : childFields.split(",")){
					            				String val = "";
					            				if(map.get(field) != null){
					            					val = map.get(field).toString();
					            				}
					            				childPs.setString(j,val);
					            				j++;
					            			}
					            			childPs.executeUpdate();
					            		}
					            	}
					            	
					            	if(!"CRMPotentialClient".equals(tableName)){
					            		//插入客户历史记录
					            		DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(tableName);
					            		String tableDisplay = "修改了"+tableBean.getDisplay().get(locale.toString());
					            		insertCRMCLientInfoLog(tableDisplay, values.get("ClientId").toString(), loginBean, connection);
					            	}
					            	
					            	//执行表自定义语句
					            	defineRs = new DynDBManager().defineSql(connection, "update", true, tableName,(Hashtable) allTables, values,id, loginBean.getId(),defineInfo,resources,locale);
					            	if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					            		rst.setRetCode(defineRs.getRetCode());
					            		rst.setRetVal(defineRs.getRetVal());
					            		if(defineRs.getRetVal() instanceof String[]){
					            			String retVals[] = (String[])defineRs.getRetVal();
					            			BaseEnv.log.error(
					            					"DynDBManager After update defineSql Error code = " +
					            					defineRs.getRetCode() + " Msg=" +
					            					new DynDBManager().getResource(resources,locale,retVals));
					            		}else{
					            			BaseEnv.log.error(
					            					"DynDBManager After update defineSql Error code = " +
					            					defineRs.getRetCode() + " Msg=" +defineRs.getRetVal());
					            		}
					            	}
					            	if(defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
					            		rst.setRetCode(defineRs.retCode) ;
					            		returnValue[1] = (String)defineRs.retVal ;
					            	}
					            	
					            }
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	/**
	 * 线索分配
	 * @param keyId
	 * @param tableName
	 * @param values
	 * @param loginBean
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result PotentialAllocatee(final String[] keyIds,final String EmployeeID,final String loginId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st=conn.createStatement();
							String keyVals="(";
							for(int i=0;i<keyIds.length;i++){
								keyVals+="'"+keyIds[i]+"',";
							}
							keyVals=keyVals.substring(0,keyVals.length()-1)+")";
							String sql="update CRMPotentialClient set EmployeeID='"+EmployeeID+"',AllocateeS=isnull(AllocateeS,'')+'"+loginId+",' where id in "+keyVals+" and EmployeeID='"+loginId+"'";
							BaseEnv.log.debug("线索分配（修改所属者及记录分配人）："+sql);
							st.execute(sql);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	/**
	 * 线索回收
	 * @param keyId
	 * @param tableName
	 * @param values
	 * @param loginBean
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result PotentialCallback(final String[] keyIds,final String loginId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st=conn.createStatement();
							String keyVals="(";
							for(int i=0;i<keyIds.length;i++){
								keyVals+="'"+keyIds[i]+"',";
							}
							keyVals=keyVals.substring(0,keyVals.length()-1)+")";
							String sql="update CRMPotentialClient set EmployeeID='"+loginId+"',AllocateeS=case when CHARINDEX(',"+loginId+",',','+AllocateeS)>=1 then SUBSTRING(AllocateeS,1,CHARINDEX(',"+loginId+",',','+AllocateeS)-1) else AllocateeS end where id in "+keyVals+" ";
							BaseEnv.log.debug("线索回收（修改所属者为当前回收者及删除当前用户及之后的分配记录）："+sql);
							st.execute(sql);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 删除客户资料
	 * @param keyIds
	 * @return
	 */
    public Result delete(final String[] keyIds,final String tableName,final Hashtable allTables,final String userId,
    		final MessageResources resources,final Locale locale,final LoginBean loginBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							
							/*
							//插入客户历史记录
							for(String clienId : keyIds){
								DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(tableName);
								String tableDisplay = "删除了"+tableBean.getDisplay().get(locale.toString());
								insertCRMCLientInfoLog(tableDisplay, clienId, loginBean, conn);
							}
			            	*/
							
							//删除审核流信息
							String sql = "DELETE FROM OAMyWorkFlow WHERE id=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							
							String sql1 = "DELETE FROM OAMyWorkFlowDet WHERE f_ref=?";
							PreparedStatement ps1 = conn.prepareStatement(sql1);
							
							Result result=null;
							
							for(String keyId : keyIds){
								String[] files = new String[] { "", "" };
								/*调用自定义删除方法，删除从表，兄弟表*/
								result = new DynDBManager().delete(tableName, allTables, keyId, userId,
										 resources, locale,  files, new ArrayList(), conn,true) ;
								if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
									break;
								}
								ps.setString(1, keyId);
								ps1.setString(1, keyId);
								ps.addBatch();
								ps1.addBatch();
							}
							ps.executeBatch();
							ps1.executeBatch();
							
							
							if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
								
							}else{
								rst.setRetCode(result.retCode) ;
								rst.setRetVal(result.getRetVal()) ;
							}
						}catch (Exception ex) {
							BaseEnv.log.error("CRMBrotherMgt delete mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
    
    /**
     * 查询评论
     * @param keyId
     * @return
     */
    public Result queryCommentById(String keyId){
    	ArrayList list = new ArrayList();
    	String sql = "SELECT CRMBrotherComment.id,CRMBrotherComment.contents,CRMBrotherComment.employeeId,CRMBrotherComment.createTime,photo FROM CRMBrotherComment left join tblEmployee on CRMBrotherComment.employeeId = tblEmployee.id  where f_ref = ? and isnull(CRMBrotherComment.commentId,'') = '' ORDER BY CRMBrotherComment.createTime";
		//String sql = "from CRMBrotherCommentBean where f_ref = ? and isnull(commentId,'') = '' ORDER BY createTime";
		list.add(keyId);
		return sqlList(sql,list);
	} 
    
    
    /**
	 * 查询回复
	 * @param keyIds
	 * @return
	 */
    public Result queryReply(final String commentIds) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "SELECT CRMBrotherComment.id,CRMBrotherComment.contents,CRMBrotherComment.employeeId,CRMBrotherComment.createTime,CRMBrotherComment.commentId,CRMBrotherComment.commentBy,tblEmployee.photo from CRMBrotherComment left join tblEmployee on CRMBrotherComment.employeeId = tblEmployee.id  WHERE commentId in ("+commentIds+") ORDER BY CRMBrotherComment.createTime";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							HashMap<String,ArrayList<String[]>> rsMap = new HashMap<String, ArrayList<String[]>>();
							
							while(rss.next()){
								String[] str = new String[7];
								String commentId = rss.getString("commentId");
								str[0] = rss.getString("id");
								str[1] = rss.getString("contents");
								str[2] = rss.getString("employeeId");
								str[3] = rss.getString("createTime");
								str[4] = rss.getString("commentId");
								str[5] = rss.getString("commentBy");
								str[6] = rss.getString("photo");
								if(rsMap.containsKey(commentId)){
									rsMap.get(commentId).add(str);
								}else{
									ArrayList<String[]> list = new ArrayList<String[]>();
									list.add(str);
									rsMap.put(commentId, list);
								}
							}
							rst.setRetVal(rsMap);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt delete mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
    
    /**
     * 添加评论&回复
     * @param commentId
     * @return
     */
    public Result addComment(CRMBrotherCommentBean commentBean){
		return this.addBean(commentBean);
	}
    
    public Result addComment(final CRMBrotherCommentBean commentBean,final String billCreateBy,final String tableName,final LoginBean loginBean,final String clientName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//保存评论
							String sql = "INSERT INTO CRMBrotherComment(id,contents,createTime,employeeId,f_ref,commentId,commentBy) VALUES(?,?,?,?,?,?,?)";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1,commentBean.getId());
							ps.setString(2,commentBean.getContents());
							ps.setString(3,commentBean.getCreateTime());
							ps.setString(4,commentBean.getEmployeeId());
							ps.setString(5,commentBean.getF_ref());
							ps.setString(6,commentBean.getCommentId());
							ps.setString(7,commentBean.getCommentBy());
							ps.executeUpdate();
							
							

//							String billId =commentBean.getF_ref();//单据Id; 
//							String employeeId = commentBean.getEmployeeId();
//							
//							String commentStr = "评论";
//							if(commentBean.getCommentId() !=null && !"".equals(commentBean.getCommentId())){
//								commentStr = "回复";
//							}
//							
//							String title = GlobalsTool.getEmpFullNameByUserId(employeeId) + commentStr +"了你的销售跟单,请查看";//标题
//							String url ="/CRMBrotherAction.do?tableName="+tableName+"&isMessageEnter=true&operation=5&keyId="+billId;
//							String message = "<a href=javascript:mdiwin(''" + url + "'',''"+clientName+"'')>"	+title;
//							message+="</a>";//内容
//							
//							
//							String commentSuccess = "false";//判断是否有插入评论
//							String id = IDGenerater.getId();
//							if(!billCreateBy.equals(loginBean.getId())){
//								//插入评论通知消息
//								String commentSql = "insert into tblAdvice(id,relationId,send,title,receive,content,status,exist,createBy,createTime)"
//									+ "values('"+id+"','"+billId+"','"+employeeId+"','"+title+"','"+billCreateBy+"','"+message+"','noRead','all','"+employeeId+"','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"')" ;
//								
//								PreparedStatement ps1 = connection.prepareStatement(commentSql);
//								ps1.executeUpdate();
//								commentSuccess = "true";
//							}
//							
//							//插入回复通知消息
//							if(commentBean.getCommentId() !=null && !"".equals(commentBean.getCommentId()) && !billCreateBy.equals(commentBean.getCommentBy()) && !commentBean.getEmployeeId().equals(commentBean.getCommentBy())){
//								String replyTitle = GlobalsTool.getEmpFullNameByUserId(employeeId) +"回复了你的评论";
//								String replyId = IDGenerater.getId();
//								String reqplySql = "insert into tblAdvice(id,relationId,send,title,receive,content,status,exist,createBy,createTime)"
//									+ "values('"+replyId+"','"+billId+"','"+employeeId+"','"+replyTitle+"','"+commentBean.getCommentBy()+"','"+message+"','noRead','all','"+employeeId+"','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"')" ;
//								PreparedStatement ps2 = connection.prepareStatement(reqplySql);
//								ps2.executeUpdate();
//								MSGConnectCenter.sendAdvice(commentBean.getCommentBy(), replyId, replyTitle);//KK发送回复通知消息
//							}
//							
//							
//							if(commentSuccess.equals("true")){
//								//插入评论了,发送消息到KK
//								MSGConnectCenter.sendAdvice(billCreateBy, id, title);//KK发送评论通知消息
//							}
							
							
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
    
	/**
	 * 根据类型(自读,隐藏)用HQL查找隐藏或只读权限。
	 * @param loginBean
	 * @param type 类型(自读,隐藏)
	 * @return
	 */
	public Result findFieldScopeByType(LoginBean loginBean,String type){
		List list = new ArrayList();
		
		String sql="FROM BrotherFieldScopeBean where ";
		if("hidden".equals(type)){
			sql +=" (" + "dbo.exist_dept(hideDeptIds,'"+loginBean.getDepartCode()+"')='true' or ','+ hideUserIds  like '%," + loginBean.getId() +",%')";
		}else{
			sql +=" (" + "dbo.exist_dept(readDeptIds,'"+loginBean.getDepartCode()+"')='true' or ','+ readUserIds  like '%," + loginBean.getId() +",%')";
		}
 		return this.list(sql, list);
	}
	
	/**
	 * 分组查询出已隐藏的字段	 
	 * @param tableName 表名
	 * @param isMainTable 是否组表 true是 fasle不是
	 * @param fieldDisplayBean 字段显示bean
	 * @param hideFieldScopeList 隐藏字段权限list
	 * @return
	 */
    public Result queryHideFields(final String tableName,final boolean isMainTable,final BrotherFieldDisplayBean fieldDisplayBean,final List<BrotherFieldScopeBean> hideFieldScopeList,final HashMap<String,String> workFlowFieldMap) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							//String sql = "select fieldName,groupName from tblDBFieldInfo where tableId=(select id from tblDBTableInfo where tableName='"+tableName+"') and fieldName in (";
							String sql = "select fieldName,groupName from tblDBFieldInfo left join tblDBEnumerationItem on tblDBFieldInfo.groupName=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumid=(select id from tblDBEnumeration where enumName='"+tableName+"')" +
							" where tableId=(select id from tblDBTableInfo where tableName='"+tableName+"') and fieldName in (";
							
							String displayFields = fieldDisplayBean.getPageFields();
							if(isMainTable == false){
								displayFields = fieldDisplayBean.getPageChildFields();
							}
							
							if(displayFields!=null && !"".equals(displayFields)){
								//拼接显示字段
								String fielsStr = "";
								for(String str : displayFields.split(",")){
									fielsStr += "'"+str+"',";
								}
								
								if(fielsStr.endsWith(",")){
									fielsStr = fielsStr.substring(0,fielsStr.length()-1);
									
								}
								
								sql += fielsStr+") ";
								
								//拼接需要隐藏的字段
								String hideFieldsStr = "";
								if(hideFieldScopeList!=null && hideFieldScopeList.size()>0){
									for(BrotherFieldScopeBean bean : hideFieldScopeList){
										String hideFieldsName = bean.getFieldsName();
										for(String str : hideFieldsName.split(",")){
											if(isMainTable == false){
												str = str.substring(5);
											}
											hideFieldsStr +="'"+str+"',";
										}
									}
								}
								
								if(workFlowFieldMap!=null){
									String workFlowHideFields = workFlowFieldMap.get("hide");
									if(workFlowHideFields!=null && !"".equals(workFlowHideFields)){
										for(String str : workFlowHideFields.split(",")){
											if(isMainTable == true){
												hideFieldsStr +="'"+str+"',";
											}else{
												hideFieldsStr +="'"+str+"',";
											}
										}
									}
								}
								
								if(hideFieldsStr.endsWith(",")){
									hideFieldsStr = hideFieldsStr.substring(0,hideFieldsStr.length()-1);
									
								}
								if(!"".equals(hideFieldsStr)){
									sql +=" and fieldName not in ("+hideFieldsStr +")";
								}
								
								
								sql += " order by tblDBEnumerationItem.enumOrder,CHARINDEX(','+CONVERT(varchar(100),fieldName)+',',',"+displayFields+"')";
								System.out.println(sql);
								PreparedStatement ps = conn.prepareStatement(sql);
								ResultSet rss = ps.executeQuery();
								
								LinkedHashMap<String,List<String>> rsMap = new LinkedHashMap<String, List<String>>();
								while(rss.next()){
									String fieldName = rss.getString("fieldName");//字段名
									String groupName = rss.getString("groupName");//分组值
									//处理分组信息
									if(rsMap.get(groupName) == null){
										ArrayList<String> list = new ArrayList<String>();
										list.add(fieldName);
										rsMap.put(groupName, list);
									}else{
										rsMap.get(groupName).add(fieldName);
									}
								}
								rst.setRetVal(rsMap);
							}
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt delete mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
    
    /**
     * 执行define文件
     * @param rst
     * @param defineName
     * @param connection
     * @param values
     * @param loginBean
     * @param resources
     * @param locale
     */
    public void defineExec(Result rst,String defineName,Connection connection,HashMap values,LoginBean loginBean,MessageResources resources,Locale locale){
    	Result defineRs = null;
		//执行define
    	DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
    	if(defineSqlBean != null ){
    		defineRs = defineSqlBean.execute(connection, values, loginBean.getId(),resources,locale,null);
    		if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
    			rst.setRetCode(defineRs.getRetCode());
    			rst.setRetVal(defineRs.getRetVal());
    			BaseEnv.log.error("DynDBManager Before add defineSql Error code = " 
    					+defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
    		}else{
    			rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
    		}
    	}
    }
    
    /**
     * 获取商品树
     * @return
     */
    public Result goodsTreeQuery(){
		List list = new ArrayList();
		String sql="SELECT classCode,len(classCode) as length,goodsFullName,GoodsNumber FROM tblGoods WHERE isCatalog = ?  and tblGoods.statusId = 0";
		list.add("1");
 		return this.sqlList(sql, list);
	}
    
    /**
     * 根据classCode查找商品
     * @return
     */
    public Result goodsQueryByClassCode(String classCode,int pageSize,int pageNo,String keyWord,String selectOption){
		List list = new ArrayList();
		String sql="SELECT tblGoods.classCode,goodsFullName,GoodsNumber,GoodsSpec,proSalesPrice,tblGoods.baseUnit,tblUnit.unitName  from tblGoods left join tblUnit on tblGoods.baseUnit = tblUnit.id WHERE isCatalog = ? and tblGoods.statusId = 0";
		if(!"0".equals(classCode)){
			//0表示根目录
			sql +=  " and tblGoods.classCode like '"+classCode+"%'";
		}
		
		if(keyWord!=null && !"".equals(keyWord)){
			sql += " and ("+selectOption+ " like '%"+keyWord+"%'";
			//如果是商品名称查找也根据拼音码查找
			if("goodsFullName".equals(selectOption)){
				sql += " or GoodsFullNamePYM like '%"+CustomizePYM.getFirstLetter(keyWord)+"%'";
			}
			sql +=")";
		}
		
		list.add("0");
		return this.sqlList(sql, list, pageSize, pageNo, true);
	}
    
    /**
     * 根据枚举名称与枚举值查找sql语句
     * @return
     */
    public Result relateClientSqlQuery(String enumerName,String value){
    	ArrayList param = new ArrayList();
    	String sql = "SELECT sql,name,value FROM tblRelateClientEnumer WHERE enumerName = ? and value = ?";
    	param.add(enumerName);
    	param.add(value);
    	return this.sqlList(sql, param);
    }
    
    /**
     * 根据sql与客户id获取关联客户信息
     * @param clientId
     * @param sql
     * @return
     */
    public Result relateInfoQueryByClientId(String clientId,String sql){
    	ArrayList param = new ArrayList();
    	param.add(clientId);
    	return this.sqlList(sql, param);
    }
    /*
    public Result relateInfoQueryByClientId(final String clientId,final String sql) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, clientId) ;
							ResultSet rss = ps.executeQuery();
							
							String selectInfo = "<option>请选择</option>";
							while(rss.next()){
								String id = rss.getString(1);
								String name = rss.getString(2);
								selectInfo += "<option value='"+id+"'>"+name+"</option>";
							}
							rst.setRetVal(selectInfo) ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	*/
    
    /**
     * 获取所有关联客户类型的值，封装成Map<id,name>
     */
    public Result relateClientAllInfoQuery(final List<String> sqlList,final List rsList) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							StringBuilder sql = new StringBuilder() ;
							StringBuilder condition = new StringBuilder() ;

							//拼接客户id
							for(int i=0;i<rsList.size();i++){
								HashMap clientMap = (HashMap)rsList.get(i) ;
								condition.append("'").append(clientMap.get("ClientId")).append("',") ;
							}
							
							if(condition.length()>0){
								condition.deleteCharAt(condition.length()-1) ;
							}
							
							//封装数据库语句用union all 连接
							for(String str :sqlList){
								if(str.indexOf("=")>-1){
									str = str.substring(0,str.indexOf("="));
									sql.append(str).append(" in (").append(condition.toString()).append(" ) union all "); 	
								}
							}
							
							String sqlStr = sql.toString();
							
							if(sqlStr.length()>0){
								sqlStr = sqlStr.substring(0,sqlStr.length()-10);
								PreparedStatement ps = conn.prepareStatement(sqlStr);
								ResultSet rss = ps.executeQuery();
								HashMap<String,String> rsMap = new LinkedHashMap<String, String>();
								
								while(rss.next()){
									rsMap.put(rss.getString(1), rss.getString(2));
								}
								
								rst.setRetVal(rsMap);
							}
							
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt delete mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
    
    /**
     * 插入最新动态
     * @param tableDisplay
     * @param clientId
     * @param loginBean
     * @param connection
     */
    public void insertCRMCLientInfoLog(String tableDisplay,String clientId,LoginBean loginBean,Connection connection){
    	//插入客户历史记录
    	String context = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd) + loginBean.getEmpFullName()+ tableDisplay;
    	new CRMClientMgt().insertCRMCLientInfoLog(clientId, "history", context, loginBean.getId(), connection) ;//插入客户日志
    	
    }
    
    /**
     * 根据报表设置语句查询
     * @param sql
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Result reportSqlQuery(String sql,int pageNo, int pageSize) {
		System.out.println("query queryReportList sql:"+sql);
		return sqlListMaps(sql, new ArrayList(), pageNo, pageSize) ;
	}
    
    /**
     * 获取邻居表关联信息
     * @param keyId
     * @param sql
     * @return
     */
    public Result brotherRelateInfoQuery(String sql,String keyId){
    	ArrayList param = new ArrayList();
    	param.add(keyId);
    	return this.sqlList(sql, param);
    }
    
    /**
     * 检测是否满足撤回权限
     * @param tableName
     * @param keyId
     * @param loginBean
     * @return
     */
    public Result checkCancelFlow(String tableName,String keyId,LoginBean loginBean){
    	ArrayList param = new ArrayList();
    	String sql = "select id,(select top 1 nodeId from OAMyWorkFlowDet left join OAMyWorkFlow on OAMyWorkFlow.id=OAMyWorkFlowDet.f_ref" +
    			" where OAMyWorkFlow.keyID="+tableName+".id and OAMyWorkFlowDet.statusId=0" +
    			" and "+tableName+".workFlowNode!='-1' and OAMyWorkFlowDet.checkPerson='"+loginBean.getId()+"' " +
    			"and OAMyWorkFlowDet.workFlowNode="+tableName+".workFlowNode ) as lastNodeID from "+tableName+" where id='"+keyId+"'";
    	return this.sqlList(sql, param);
    } 
    
    /**
     * 潜在客户转客户资料操作
     * @param mainFields 主表字段
     * @param childFields 从表字段
     * @param mainValList
     * @param childValList
     * @param loginBean
     * @param resources
     * @param locale
     * @return
     */
    public Result addClientTransfer(final String mainFields,final String childFields,final List<HashMap<String,String>> mainValList,final HashMap<String,HashMap<String,String>> childValMap,final LoginBean loginBean,final MessageResources resources,final Locale locale,final String tableName,final HttpServletRequest request,final OAWorkFlowTemplate workFlow) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//插入的主表与子表,默认crm转erp
							
							//封装主表sql语句
							String sql = "INSERT INTO CRMClientInfo("+mainFields+") Values(";
							for(String field : mainFields.split(",")){
								sql +="?,";
							}
							if(sql.endsWith(",")){
								sql = sql.substring(0,sql.length()-1);
							}
							sql +=")";
							
							
							//封装从表sql语句
							
							String childSql = "INSERT INTO CRMClientInfoDet("+childFields+") Values(";
							for(String field : childFields.split(",")){
								childSql +="?,";
							}
							
							if(childSql.endsWith(",")){
								childSql = childSql.substring(0,childSql.length()-1);
							}
							childSql +=")";
							
							
							Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				            Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
				            MOperation mop = (MOperation) loginBean.getOperationMap().get("/CRMClientAction.do");
				            
							for(HashMap<String,String> mainMap : mainValList){
								mainMap.put("ClientNo", BillNoManager.find("CRMClientInfo_ClientNo",loginBean,connection));
								String keyId = mainMap.get("id");
								PreparedStatement ps = connection.prepareStatement(sql);
								int j=1;
								for(String field : mainFields.split(",")){
									ps.setString(j, mainMap.get(field));
									j++;
								}
								ps.executeUpdate();
								
								
								HashMap<String,String> childMap = childValMap.get(keyId);
								if(childMap!=null){
									//执行从表语句
									PreparedStatement ps1 = connection.prepareStatement(childSql);
									int k=1;
									for(String field : childFields.split(",")){
										ps1.setString(k, childMap.get(field));
										k++;
									}
									ps1.executeUpdate();
								}
								
							
								
								//执行插入前自定义语句
								HashMap values = new HashMap();
								values.put("clientId",keyId);
								values.put("employeeId",loginBean.getId());
								
								Result defineRs = null;
								//执行define
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("PotentialToClient_add");
								defineRs = defineSqlBean.execute(connection, values, loginBean.getId(),resources,locale,null);
					            if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					            	rst.setRetCode(defineRs.getRetCode());
					            	rst.setRetVal(defineRs.getRetVal());
					                BaseEnv.log.error("DynDBManager Before add defineSql Error code = " 
					                		+defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
					            }else{
					            	rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
					            }
					            
					            /*添加工作流数据*/
					            if (null != workFlow && 1 == workFlow.getTemplateStatus()) {
					            	try {
					            		if(BaseEnv.workFlowDesignBeans.get(workFlow.getId()) == null){
					            			rst.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
					            			rst.setRetVal(resources.getMessage("com.add.workfow.error"));
					            		}else{
					            			Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
					            			boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
					            			Result rsValue = new DynDBManager().detail("CRMClientInfo", allTables,keyId, loginBean.getSunCmpClassCode(), props, loginBean.getId(), isLastSunCompany,
					            					"", connection);
					            			HashMap hm = (HashMap) rsValue.retVal;
					            			
					            			Result rss = new OAMyWorkFlowMgt().addOAMyWorkFlow(workFlow.getId(), "CRMClientInfo", mainMap, loginBean, locale, resources, connection);
					            			if (rss.retCode == ErrorCanst.WORK_FLOW_NO_NEXT_NODE) {
					            				rst.setRetCode(rss.retCode);
					            			} else if (rss.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					            				rst.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
					            				rst.setRetVal(resources.getMessage("com.add.workfow.error"));
					            			} else {
					            				rst.setRetCode(rss.getRetCode());
					            			}
					            			//returnValue[2] = (String) rss.getRetVal();
					            		}
					            	} catch (Exception ex) {
					            		BaseEnv.log.error("UserFunctionAction.java add method :" + ex);
					            		ex.printStackTrace();
					            		rst.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
					            		rst.setRetVal(resources.getMessage("com.add.workfow.error"));
					            		connection.rollback();
					            	}
					            }
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
    
    /**
     * 根据关联ID获取日程bean
     * @param relationId
     * @return
     */
	public OACalendarBean getCalendarByRelationId(String relationId){
		OACalendarBean bean = null;
		String hql = "FROM OACalendarBean WHERE relationId =?";
		ArrayList param = new ArrayList();
		param.add(relationId);
		Result rs = list(hql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<OACalendarBean> list = (ArrayList<OACalendarBean>)rs.retVal;
			if(list!=null && list.size()>0){
				bean = list.get(0);
			}
		}
		return bean;
	}
	

    /**
	 * 公共sql查询
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result publicSqlQuery(String sql,ArrayList param){
		return sqlList(sql,param);
	}
	
	/**
	 * jdbc调用公共新增修改删除方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result operationSql(final String sql, final List param) {
      final Result rst = new Result();
      int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
              session.doWork(new Work() {
                  public void execute(Connection conn) throws
                          SQLException {
                      try {
                           PreparedStatement pstmt = conn.prepareStatement(sql);
                           for(int i = 1;i<=param.size();i++){
                               pstmt.setObject(i,param.get(i-1));
                           }
                          int row = pstmt.executeUpdate();
                          if (row > 0) {
                             rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                             rst.setRealTotal(row);
                          }
                     } catch (Exception ex) {
                          ex.printStackTrace();
                          
                          rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                          return;
                     }
                 }
             });
             return rst.getRetCode();
         }
     });
     rst.setRetCode(retCode);
     return rst;
	}
	
	/**
	 * 是否有获取信息权限
	 * @param loginBean
	 * @return
	 */
	protected String checkGetDatasScope(LoginBean loginBean){
		String hasGetDatasScope = "false";
		DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean("CRMPotentialClient","DefineStatus"); 
        if(fieldBean!=null){
        	String newSql = "SELECT id FROM CRMPotentialClient WHERE createBy = ? and DefineStatus = '1'";
        	ArrayList params = new ArrayList();
        	params.add(loginBean.getId());
        	Result rest = publicSqlQuery(newSql, params);
        	if(rest.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		ArrayList list = (ArrayList)rest.retVal;
        		if(list==null || list.size()==0){
        			hasGetDatasScope = "true";
        		}
        	}
        }
		
        return hasGetDatasScope;
	}
}
