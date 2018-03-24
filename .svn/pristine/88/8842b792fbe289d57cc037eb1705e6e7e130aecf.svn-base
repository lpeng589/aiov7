package com.koron.oa.workflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * <p>
 * Title:工作流表单设计数据库操作 Description:
 * </p>
 * 
 * @Date:2013-04-11
 * @CopyRight:科荣软件
 * @Author:李文祥
 */
public class OAWorkFlowMgt extends AIODBManager {

	/**
	 * 根据表名获取最新的表单
	 * 
	 * @param tableId
	 * @param layOutHtml
	 * @param createBy
	 * @param createTime
	 * @return
	 */
	public HashMap getNewTable(String tableName,String designId) {
		List<String> param = new ArrayList<String>();
		String sql = "select top 1 layOutHtml from  OAWorkFlowTableVersion where designId=? and  tableId=(select id from tbldbtableInfo where tableName=?) order by createTime desc";
		param.add("" + designId + "");
		param.add("" + tableName + "");
		Result rs = sqlListMap(sql, param, 0, 0);
		if(rs.retVal!=null&&((ArrayList)rs.retVal).size()>0){
			return (HashMap)((ArrayList)rs.retVal).get(0);
		}
		return null;
	}
	/**
	 * 根据Id获得工作流
	 * @param keyId
	 * @param tableName	 工作流所在流程表名
	 * @return
	 */
	public Result loadWorkFlow(final String keyId,final DBTableInfoBean tableInfo) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							HashMap<String,String> map=new HashMap<String,String>();
						
							String sql="select *  from  "+tableInfo.getTableName()+"  where id= '"+keyId+"'";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							java.sql.ResultSetMetaData rsm=rss.getMetaData();
	                        int colCount=rsm.getColumnCount();
							while(rss.next()){
								for(int i=1;i<=colCount;i++){
									String columnName=rsm.getColumnName(i);
									String columnVal=rss.getString(columnName);
									if("null".equals(columnVal) || columnVal==null){
										columnVal="";
									}
									map.put(columnName,columnVal);
								}
							}
							rst.setRetVal(map);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 获得弹出框需要显示的名字
	 * @param type
	 * @param fieldName
	 * @param map
	 * @return
	 */
	public String showNames(String type, String fieldName, HashMap<String, String> map) {
		String names="";
		boolean  flag=false;
		if(map.get(fieldName).contains(";")){ //多选
			flag=true;
		}
		for(String Id:map.get(fieldName).split(";")){
			String temp="";
			if("employee".equals(type)){
				temp = GlobalsTool.getEmpFullNameByUserId(Id);
			}else if("dept".equals(type)){
				temp =(String) BaseEnv.deptMap.get(Id);
			}else if("client".equals(type)){
				temp =findClientNameById(Id);
			}
			if(flag){
				names+=Id+"%koron%"+temp+";";
			}else{
				names=temp;
			}
		}
		return names;
	}
	
	/**
	 * 根据流程Id获取流程名称
	 * @param id
	 * @return
	 */
	public String getWorkFlowNameById(String id){
		List<String> param=new ArrayList<String>();
		String sql="select  templateName from OAWorkFlowTemplate where id=? ";
		param.add(id);
		Result rs=this.sqlList(sql, param);
		String flowName="";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && ((List)rs.retVal).size()>0){
			List obj=(List) rs.retVal;
			Object[] objList=(Object[]) obj.get(0);
			flowName=objList[0].toString();
		}
		return flowName;
	}
	
	
	/**
	 * 根据客户Id获取客户名称
	 */
	public String findClientNameById(String keyId){
		List<String> param = new ArrayList<String>();
		String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE id=?";
		param.add(""+keyId+"");
		Result rs = this.sqlList(sql, param);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal() != null) {
			List obj=(List) rs.retVal;
			if(obj!=null && obj.size()>0){
				Object[] objList=(Object[]) obj.get(0);
				return objList[1].toString();
			}
		}
		return "";
	}
	
	/**
	 * 获取弹出框
	 * @param tableMap
	 * @param fieldBean
	 * @param loginBean
	 * @return
	 */
	public Result getPop(final HashMap<String, String> tableMap, final DBFieldInfoBean fieldBean,final LoginBean loginBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuilder popuValue = new StringBuilder();
							boolean  flag=false;
							if(tableMap.get(fieldBean.getFieldName()).contains(";")){ //多选
								flag=true;
							}
							for(String str : tableMap.get(fieldBean.getFieldName()).split(";")){
								String refsql = new DynDBManager().getRefSql(fieldBean,false,str, "00001", new Hashtable(),false,tableMap,loginBean.getId());
								
									if (refsql != null && fieldBean.getSelectBean()!=null) {
										ResultSet crset = conn.createStatement().executeQuery(refsql);
										if (crset.next()) {
											if(flag){//多选
												popuValue.append(str+"%koron%"+String.valueOf(crset.getObject(1))+";");
											}else{
												popuValue.append(String.valueOf(crset.getObject(1)));
											}
										}
									}
								
							}
							
							tableMap.put("popup_"+fieldBean.getFieldName(),popuValue.toString());
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
	 * 执行主表插入操作
	 * 
	 * @param conn
	 * @param tableMap
	 * @param tableName
	 */
	public void addMyWorkFlow(Connection conn,
			final HashMap<String, String> tableMap, final String tableName) throws Exception {

			List<String> param = new ArrayList<String>();
			Iterator iter = tableMap.entrySet().iterator();
			StringBuilder fieldStr = new StringBuilder("");
			StringBuilder valStr = new StringBuilder("");
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				param.add(""+entry.getValue()+"");

				valStr.append(" ? ,");
				fieldStr.append(entry.getKey() + ",");
			}
			String fieldStrNew = fieldStr.substring(0, fieldStr.length() - 1);
			String valStrNew = valStr.substring(0, valStr.length() - 1);

			String sql = "insert into " + tableName + " (" + fieldStrNew
					+ ") values (" + valStrNew + ")";
			BaseEnv.log.debug("OAWorkFlowMgt.addMyWorkFlow sql:" + sql);

			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i = 1; i <= param.size(); i++) {
				pstmt.setObject(i, param.get(i - 1));
			}
			pstmt.execute();
	}

	/**
	 * 执行主表修改操作
	 * 
	 * @param conn
	 * @param tableMap
	 * @param tableName
	 */
	public void updateMyWorkFlow(Connection conn,
			final HashMap<String, String> tableMap, final String tableName) {

		try {
			List<String> param = new ArrayList<String>();
			Iterator iter = tableMap.entrySet().iterator();
			
			StringBuilder updateStr=new StringBuilder("");
			String billId="";
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				if(!"id".equals(entry.getKey())){
					param.add(""+entry.getValue()+"");
					updateStr.append(entry.getKey()+"= ? ,");
				}else{
					billId=entry.getValue().toString();
				}
			}
			param.add(""+billId+"");
			String updateStrNew = updateStr.substring(0, updateStr.length() - 1);

			String sql = "update " + tableName + " set " + updateStrNew +" where id =? " ;
			//System.out.println("Updatesql的值:" + sql);
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i = 1; i <= param.size(); i++) {
				pstmt.setObject(i, param.get(i - 1));
			}
			pstmt.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * 工作流添加执行的方法
	 * 
	 * @param tableMap
	 * @param tableName
	 * @return
	 */
	public Result executeAddSQL(final DBTableInfoBean tableInfo,final HashMap<String, String> tableMap,
			final String tableName, final LoginBean loginBean,
			final MessageResources resources, final Locale locale,
			final String deliverTo) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							List fieldLists = tableInfo.getFieldInfos();
							for (int i = 0; i < fieldLists.size(); i++) {
								DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fieldLists.get(i);
								if (fieldInfo.getFieldIdentityStr() != null && DBFieldInfoBean.FIELD_IDENTIFIER.equals(fieldInfo.getFieldIdentityStr())) {
									// 单据编号
									String key = "";
									String defaultValue = fieldInfo.getDefaultValue();
									if (defaultValue != null && !"".equals(defaultValue)) {
										// 存在默认值
										key = defaultValue;
									} else {
										key = tableInfo.getTableName() + "_" + fieldInfo.getFieldName();
									}
									BillNo billno = BillNoManager.find(key);
									if (billno != null) {
										if (billno.isFillBack()) { //启用单据编号连续后，从数据库读编号
											String valStr = BillNoManager.find(key, tableMap, loginBean,conn);
											if ("".equals(valStr)) {
												/* 单据编号无法生成 */
												rst.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
												rst.setRetVal(resources.getMessage("erp.billno.error"));
												return;
											} else {
												tableMap.put(fieldInfo.getFieldName(), valStr);
											}
										}
									}
								}
							}
							
							
							// 执行主表插入操作
							addMyWorkFlow(conn, tableMap, tableName);
							OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
							Result rss = new OAMyWorkFlowMgt().addOAMyWorkFlow(workFlow.getId(),tableName,tableMap,loginBean,locale,resources,conn);
							if (rss.retCode == ErrorCanst.WORK_FLOW_NO_NEXT_NODE) {
								rst.setRetCode(rss.retCode);
							} else if (rss.retCode != ErrorCanst.DEFAULT_SUCCESS) {
								rst.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
							} else {
								rst.setRetCode(rss.getRetCode());
							}
						} catch (Exception ex) {
							BaseEnv.log.error("OAWorkFlowMgt.executeAddSQL add method :", ex);
							rst.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
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
	 * 工作流修改执行的方法
	 * 
	 * @param tableMap
	 * @param tableName
	 * @return
	 */
	public Result executeUpdateSQL(final HashMap<String, String> tableMap,
			final String tableName, final LoginBean loginBean,
			final MessageResources resources, final Locale locale,
			final String deliverTo) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {

							// 执行主表修改操作
							updateMyWorkFlow(conn, tableMap, tableName);
							
							OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
							String oaTitle="";
							if(workFlow.getTitleTemp()!=null&&workFlow.getTitleTemp().length()>0){
								DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName) ;
								oaTitle = new OAMyWorkFlowMgt().replaceFieldNameByValue(tableInfo, tableMap, workFlow.getTitleTemp(), locale.toString(),loginBean,conn) ;
							}
							
							//修改工作流主题
							String sql = "update OAMyWorkFlow set applyContent=?,lastupdateTime=? where id= ? "; 
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, oaTitle);
							pstmt.setString(2, BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
							pstmt.setString(3, tableMap.get("id"));
							pstmt.execute();
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);

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
}
