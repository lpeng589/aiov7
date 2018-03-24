package com.koron.crm.client;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.bsf.BSFManager;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.IndexWriter;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.BrotherFieldScopeBean;
import com.koron.crm.bean.CRMLabelBean;
import com.koron.crm.bean.CRMShareClientBean;
import com.koron.crm.bean.ClientInfoRecordBean;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.koron.crm.bean.WorkProfessionBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.oaTask.OATaskAction;
import com.koron.oa.util.AttentionMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.email.util.EMailMessage;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseCustomFunction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.Obj;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 
 * <p>Title:客户列表数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 23, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class CRMClientMgt extends AIODBManager{
	public static String addSql = ""; //存放插入的脚本
	public static ArrayList childsIns = new ArrayList(); //更新操作时,存放插入子表的脚本
	/**
     * 查询客户最新动态
     * @param defineId
     * @return
     */
	public Result queryClientLog(final String clientId,final int type) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//String newsOrder = BaseEnv.systemSet.get("newsOrder").getSetting();
							//String dealingOrder = BaseEnv.systemSet.get("dealingOrder").getSetting();
							//String order = "" ;
							//if(newsOrder!=null && type==0){
							//	order = "top "+newsOrder ;
							//}
							//if(dealingOrder!=null && type==-1){
							//	order = "top "+dealingOrder ;
							//}
							String sql = "select top 100 billType,context,relationId,createTime,createBy from CRMClientInfoLog where clientId=? and statusId=? and context not like '%修改客户%' order by createTime desc;";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, clientId);
							ps.setInt(2, type) ;
							ResultSet rss = ps.executeQuery();
							ArrayList<String[]> logList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] str=new String[5];
								str[0]=rss.getString("billType");
								str[1]=rss.getString("context");
								str[2]=rss.getString("relationId");
								str[3]=rss.getString("createTime");
								str[4]=rss.getString("createBy");
								logList.add(str) ;
							}
							rst.setRetVal(logList) ;
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
	 * 客户共享
	 * @param clientIds 用户ids
	 * @param allEmployList 所有用户信息
	 * @param saveEmployList 需要新增的employeeIds
	 * @param delEmployList 需要删除的employeeIds
	 * @param moduleId 模板ID
	 * @param viewId 视图ID
	 * @param clientMap 客户Beanmap
	 * @param login  
	 * @param isShareClient 是否共享
	 * @param isSingle 单个or批量
	 * @param shareBeanMap 更新共享bean
	 * @return
	 */
    public Result shareClientEmp(final String clientIds,final List<Object> allEmployList,final List<Object> saveEmployList,final List<Object> delEmployList,final String moduleId,final String viewId,final HashMap<String,String> clientMap,final LoginBean login,final String isShareClient,final String isSingle,final HashMap<String, CRMShareClientBean> shareBeanMap,final String[] allIds) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							List<String[]> listAdvice = new ArrayList<String[]>();
							Statement state = conn.createStatement();
							String sql = "";
							String allUserIds = ",";//记录insert所有用户Id,用于删除通知消息判断ID
							for(String clientId : clientIds.split(",")){
								state.addBatch("DELETE FROM CRMClientInfoEmp WHERE f_ref='"+clientId+"' and empType=1");
								for(Object obj : allEmployList){
									//插入跟单人
									state.addBatch("insert into CRMClientInfoEmp(id,f_ref,employeeID,DepartmentCode,empType) values('"+IDGenerater.getId()+"','"+clientId+"','"+ ((Object[])obj)[0].toString()+"','"+ ((Object[])obj)[1].toString()+"',1)");
									allUserIds += ((Object[])obj)[0].toString() + ",";
								}
								
								if(shareBeanMap.get(clientId) == null){
									//若发现没有就查询客户信息
									sql = "INSERT INTO CRMShareClient(id,clientId,popedomUserIds,popedomDeptIds,popedomEmpGroupIds,popedomTitleIds) values('"+IDGenerater.getId()+"','"+clientId+"','"+allIds[0]+"','"+allIds[1]+"','','"+allIds[2]+"')";
								}else{
									//更新共享字段信息
									sql = "UPDATE CRMShareClient SET popedomUserIds = '"+shareBeanMap.get(clientId).getPopedomUserIds()+"',popedomDeptIds='"+shareBeanMap.get(clientId).getPopedomDeptIds()+"',popedomTitleIds='"+shareBeanMap.get(clientId).getPopedomTitleIds()+"' WHERE clientId='"+clientId+"'";	
								}
								state.addBatch(sql);
								
								//发送通知消息
								if("1".equals(isShareClient) && saveEmployList !=null && saveEmployList.size() >0){
									for(Object obj : saveEmployList){
										if(clientMap.get(clientId)!=null && clientMap.get(clientId).length()>0 && !login.getId().equals(((Object[])obj)[0].toString())){
											String title=login.getEmpFullName()+"共享了客户‘" + clientMap.get(clientId) + "’，请查看。";
											String url = "/CRMClientAction.do?operation=5&type=detailNew&keyId="+clientId+"&moduleId="+moduleId+"&viewId="+viewId;
											String message = "<a href=javascript:mdiwin(''" + url + "'',''" + clientMap.get(clientId) + "'')>"
											+login.getEmpFullName()+"共享了客户‘" + clientMap.get(clientId) + "’，请查看。" ;
											message+="</a>";
//											String id = IDGenerater.getId();
//											sql = "insert into tblAdvice(id,relationId,send,title,receive,content,status,exist,createBy,createTime,type)"
//												+ "values('"+id+"','"+clientId+"','"+login.getId()+"','"+title+"','"+((Object[])obj)[0].toString()+"','"+message+"','noRead','all','"+login.getId()+"','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"','clientShare')" ;
//											state.addBatch(sql) ;
//
//							    	        // 给客户端用户发送通知消息
//											MSGConnectCenter.sendAdvice(((Object[])obj)[0].toString(), id, title);
											
//											new AdviceMgt().add(userId, title, content, receiveIds, relationId, type);
											String[] item = {login.getId(), title, message, ((Object[])obj)[0].toString(), clientId, "clientShare"};
											listAdvice.add(item);
										}
									}
								}
								
								//删除通知消息
								if(delEmployList !=null && delEmployList.size()>0){
									String delShareIds = "";
									for(Object obj : delEmployList){
										if(allUserIds.indexOf(","+((Object[])obj)[0].toString()+",")==-1){
											delShareIds += "'" + ((Object[])obj)[0].toString() +"',";
										}
									}
									if(delShareIds.endsWith(",")){
										delShareIds = delShareIds.substring(0,delShareIds.length()-1);
									}
									//删除通知消息
									if(!"".equals(delShareIds)){
//										state.addBatch("DELETE FROM tblAdvice WHERE relationId='" + clientId + "' AND receive IN ("+delShareIds+")");
//										new AdviceMgt().deleteByRelationId(relationIds, userIds);
										String[] item = {clientId, delShareIds};
										listAdvice.add(item);
									}
								}								
							}
							state.executeBatch();
							
							for(String clientId : clientIds.split(",")){
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("CRMClientInfoEmp_add");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();
									map.put("clientId", clientId);
									Result rs3 = defineSqlBean.execute(conn, map,login.getId(), null, null, "");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										BaseEnv.log.debug(rs3.retVal);
										rs.setRetVal(rs3.retVal);
										rs.setRetCode(rs3.retCode);
										return;
									}
								}
							}
							rs.setRetVal(listAdvice);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt shareClientEmp mehtod:", ex) ;
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询客户的跟单人
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
    public Result queryClientEmp(final String clientId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select distinct emp.employeeID from CRMClientInfoEmp emp " +
								"join CRMClientInfo info on emp.f_ref=info.id where info.id=?";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, clientId);
						ResultSet rss = pss.executeQuery();
						ArrayList<String> empList = new ArrayList<String>();
						while(rss.next()){
							empList.add(rss.getString("employeeID"));
						}
						rs.setRetVal(empList);
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
    
	/**
	 * 启用或停用客户状态
	 * @param keyIds
	 * @return
	 */
    public Result updateStatus(final String[] keyIds,final String status,final LoginBean login,final String openValue,final String stopValue,final String client) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							StringBuilder sb = new StringBuilder() ;
							for(String str : keyIds){
								sb.append("'").append(str).append("',") ;
							}
							String strKeyId = sb.toString().substring(0, sb.toString().length()-1) ;
							String sql = "" ;
							Statement state = conn.createStatement() ;
							if("0".equals(status) && !"1".equals(login.getId())){
								sql = "select count(*) from CRMClientInfo where id in ("+strKeyId+") and Status='3' " ;
								ResultSet rss = state.executeQuery(sql) ;
								if(rss.next()){
									/*是否启用了意向客户限制*/
									String willQty = GlobalsTool.getSysSetting("WillQty") ;
									if("true".equals(willQty)){
										String checkSQL2 = "select willQty from tblWillCustomerQty where employeeId='"+login.getId()+"' " +
														   "and willQty-"+rss.getInt(1)+">=(select count(*) from CRMClientInfo a left join CRMClientInfoEmp b on a.id=b.f_ref "+  
														   "where b.employeeId='"+login.getId()+"' and a.Status='3' and a.statusId=0)" ;
										rss = state.executeQuery(checkSQL2) ;
										if(!rss.next()){
											rst.setRetCode(ErrorCanst.OVER_WILL_CLIENT_QTY) ;
											return ;
										}
									}
								}
							}
							sql = "update CRMClientInfo set statusId="+status+" where id in ("+strKeyId+")" ;
							state.executeUpdate(sql) ;
							String createTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd) ;
							for(String str : keyIds){
								String context = createTime + " " + login.getEmpFullName()+ ("0".equals(status)?openValue:stopValue)+client ;
								insertCRMCLientInfoLog(str, "history", context, login.getId(), conn) ;
							}
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt updateClientStatus mehtod:", ex) ;
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
     * 跟据客户Id查询联系人信息
     * @param keyIds 客户的Id
     * @return
     */
    public Result selectClientDetById(final String[] keyIds,String msgType) {    	
    	final String type = "sms".equals(msgType)?"mobile":"clientEmail";
    	String temp="";
    	for(int i=0;i<keyIds.length;i++){    		
    		temp+="'"+keyIds[i]+"',";
    	}
    	if(temp.endsWith(","))temp=temp.substring(0,temp.length()-1);
    	final String keyTemp=temp;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select userName,mobile,clientEmail,mainUser,id from CRMClientInfoDet where f_ref in ("+keyTemp+") and (len("+type+")>0 ) " ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<String[]> strList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] arrayStr = new String[5] ;
								arrayStr[0] = rss.getString("userName") ;
								arrayStr[1] = rss.getString("mobile") ;
								arrayStr[2] = rss.getString("clientEmail") ;
								arrayStr[3] = rss.getString("mainUser") ;
								arrayStr[4] = rss.getString("id") ;
								strList.add(arrayStr) ;
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("CRMClientMgt---selectClientDetById method :"+ ex);
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
	 * 更改客户生命周期
	 * @param keyIds
	 * @return
	 */
    public Result updateClientStatus(final String[] keyIds,final String status,
    		final String ispublic,final String deptCode,final LoginBean login,final String change,final List<ClientInfoRecordBean> recordList) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "" ;
							Statement state = conn.createStatement() ;
							String transferDate = new BaseDateFormat().format(new Date(), BaseDateFormat.yyyyMMdd) ;
							StringBuilder sb = new StringBuilder() ;
							/*是否启用了意向客户限制*/
							String willQty = GlobalsTool.getSysSetting("WillQty") ;
							if("3".equals(status) && "true".equals(willQty) && !"1".equals(login.getId())){
								String checkSQL2 = "select willQty from tblWillCustomerQty where employeeId='"+login.getId()+"' " +
												   "and willQty-"+keyIds.length+">=(select count(*) from CRMClientInfo a left join CRMClientInfoEmp b on a.id=b.f_ref "+  
												   "where b.employeeId='"+login.getId()+"' and a.Status='3' and a.statusId=0)" ;
								ResultSet rss = state.executeQuery(checkSQL2) ;
								if(!rss.next()){
									rst.setRetCode(ErrorCanst.OVER_WILL_CLIENT_QTY) ;
									return ;
								}
							}
							
							for(String str : keyIds){
								/*Statement state2 = conn.createStatement() ;
								判断是否是成交客户，成交客户不能转为其它生命周期的客户
								sql = "select status from CRMClientInfo where id='"+str+"'" ;
								ResultSet rss = state2.executeQuery(sql) ;
								if(rss.next()){
									String status = rss.getString("status") ;
									if("4".equals(status)){
										rst.setRetCode(ErrorCanst.COMPLETE_CLIENT_NO_CHANGE) ;
										return ;
									}
								}*/
								sb.append("'").append(str).append("',") ;
							}
							String strKeyId = sb.toString().substring(0, sb.toString().length()-1) ;
							String updateTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
							sql = "update CRMClientInfo set status="+status+",transferer='"+login.getId()+"',transferDate='"+transferDate+"',lastUpdateTime='"+updateTime+"',LastContractTime='"+updateTime+"' where id in("+strKeyId+")" ;
							state.addBatch(sql) ;
							
							/*如果是公共池客户，则删除原来跟单人，插入新的跟单人*/
							if("public".equals(ispublic)){
								sql = "delete from CRMClientInfoEmp where f_ref in (" + strKeyId +")";
								state.addBatch(sql) ;
								/*回归客户池时间*/
								int backDay = Integer.parseInt(GlobalsTool.getSysSetting("ReturnPoolTime")) ;
								for(String str : keyIds){
									Statement state2 = conn.createStatement() ;
//									String sql2 = "select clientName,transferer,transferDate from CRMClientInfo where id='"+str+"'" ;
//									ResultSet rss = state2.executeQuery(sql2) ;
//									if(rss.next()){
//										String transferer   = rss.getString("transferer") ;
//										String transDate = rss.getString("transferDate") ;
//										String clientName = rss.getString("clientName") ;
//										Calendar transTime = Calendar.getInstance() ;
//										if(transDate==null || transDate.length()==0){
//											transDate = new BaseDateFormat().format(new Date(), BaseDateFormat.yyyyMMddHHmmss) ;
//										}
//										transTime.setTime(new BaseDateFormat().parse(transDate, BaseDateFormat.yyyyMMdd)) ;
//										transTime.add(Calendar.DAY_OF_YEAR, backDay) ;
//										Calendar currTime = Calendar.getInstance() ;
//										/*如果还没到回归客户池时间*/
//										long longTime = transTime.getTimeInMillis()-currTime.getTimeInMillis() ;
//										if(login.getId().equals(transferer) && longTime>0){
//											rst.setRetCode(ErrorCanst.BACK_PUBLIC_CLIENT_TIME) ;
//											rst.setRetVal(clientName) ;
//											break ;
//										}
//									}
									sql = "insert into CRMClientInfoEmp(id,f_ref,departmentCode,employeeId) values('"+IDGenerater.getId()+"','"+str+"','"+deptCode+"','"+login.getId()+"')" ;
									state.addBatch(sql) ;
								}
							}
							state.executeBatch() ;
							String createTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd) ;
							for(String str : keyIds){
								String context = createTime + " " + login.getEmpFullName()+ change +GlobalsTool.getEnumerationItemsDisplay("ClientStatus", status, login.getDefLoc()) ;
								insertCRMCLientInfoLog(str, "history", context, login.getId(), conn) ;
							}
						}catch (Exception ex) {
							BaseEnv.log.error("ClientMgt updateClientStatus mehtod:", ex) ;
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
						
					}
					
				});
				//转换生命周期添加历史记录
				for(ClientInfoRecordBean bean : recordList){
					addBean(bean, session);	
				}		
				return rst.getRetCode();
				
			}
			
		});
		rst.setRetCode(retCode);
		return rst ;
	}
    
	/**
	 * 客户移交
	 * @param keyIds
	 * @return
	 */
    public Result clientHandOver(final ArrayList<String[]> filterClientIdsList,final String userId,final LoginBean login,
    		final String handOver,final String content,final String moduleId,final Hashtable allTables,final MessageResources resources,
			final Locale local,final String sqlName) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							List<String[]> listAdvice = new ArrayList<String[]>();
							String[] userIds = userId.split("\\|") ;
							Statement state = conn.createStatement() ;
							boolean isOK = true ;
							
							String strDate = new BaseDateFormat().format(new Date(), BaseDateFormat.yyyyMMdd) ;
							String createTime = new BaseDateFormat().format(new Date(), BaseDateFormat.yyyyMMddHHmmss) ;								
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(sqlName);																								
							if (defineSqlBean == null) {
								BaseEnv.log
										.error("Define Sql Not Exist :Name = "
												+ sqlName);
								result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							}else{											
								String[] strArray = userIds[0].split(";") ;
								String ClientName="";
								String ID = "";	
								for(String[] clientInfo : filterClientIdsList){		
									ClientName += "【"+clientInfo[1]+"】," ;								
									ID = IDGenerater.getId();									
									String sql = "insert into CRMClientTransfer(id,ClientId,TransferMan,TransferTo,TransferDate,createBy,createTime,SCompanyID)" +
									" values('"+ID+"','"+clientInfo[0]+"','"+login.getId()+"','"+strArray[0]+"','"+strDate+"','"+login.getId()+"','"+createTime+"','00001')" ;																			
									state.execute(sql) ;
									HashMap values = new HashMap();
									//存放插入日志内容
									String logContent = createTime+" "+GlobalsTool.getEmpFullNameByUserId(login.getId())+"移交了客户给"+GlobalsTool.getEmpFullNameByUserId(strArray[0]);
									values.put("id",ID);
									values.put("CRMClientTransfer_id", ID);
									values.put("ClientId", clientInfo[0]);
									//values.put("createTime",createTime);
									values.put("logContent",logContent);
									
									
									Result ret = defineSqlBean.execute(conn, values,login.getId(), resources, local, "");
									result.setRetCode(ret.getRetCode());
									result.setRetVal(ret.getRetVal());	
									
								}
								if(ClientName.endsWith(",")){
									ClientName = ClientName.substring(0,ClientName.length()-1);
								}
								//发送邮件到移交对象
								String url = "";
								String title=login.getEmpFullName()+"把" + ClientName + "客户移交给你。";
								if(filterClientIdsList.size()==1){
									url = "/CRMClientAction.do?opertaion=5&type=detailNew&keyId="+filterClientIdsList.get(0)[0];
								}else{
									url = "/CRMClientAction.do?opertaion=4&ModuleId="+moduleId;
								}
								String message = "<a href=\"javascript:mdiwin('" + url + "','客户列表')\">"+title;
								
								if(content!=null && content.length()>0){
									message += "提醒内容："+content ;
								}
								message += "</a>";
								
								String[] item = {login.getId(), title, message, strArray[0], ID, "clientHandOver"};
								listAdvice.add(item);
							}
							result.setRetVal(listAdvice);
						}catch (Exception ex) {
							BaseEnv.log.error("ClientMgt clientHandOver mehtod:", ex) ;
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result ;
	}
    
    /**
	 * 插入客户最新动态
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
	public Result insertCRMCLientInfoLog(String clientId, String billType,
			String context, String loginId, Connection conn) {
		Result rs = new Result();
		try {
			
			String sql = "insert into CRMClientInfoLog(id,clientId,billType,context,createBy,createTime,relationId) values(?,?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, IDGenerater.getId());
			ps.setString(2, clientId);
			ps.setString(3, billType);
			ps.setString(4, context);
			ps.setString(5, loginId);
			ps.setString(6, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			ps.setString(7, clientId);
			ps.executeUpdate();
			
			//更新客户上次联系时间	
			String sql1 = "update CRMClientInfo set LastContractTime=? where id=?";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			ps1.setString(2, clientId);
			ps1.executeUpdate();
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("CRMClientMgt---insertCRMCLientInfoLog method :" + ex);
		}
		return rs;
	}
	
	/**
	 * 插入客户最新动态
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
    public Result insertCRMCLientInfoLog(final String clientId,final String billType,
    		final String context,final String loginId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Result reslt = insertCRMCLientInfoLog(clientId, billType, context, loginId, conn) ;
						rs.setRetCode(reslt.getRetCode()) ;
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
    
	/**
	 * 删除客户资料
	 * @param keyIds
	 * @return
	 */
    public Result delete(final String[] keyIds,final Hashtable allTables,final String userId,
    		final MessageResources resources,final Locale locale,final Boolean flowFlag) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							/*删除客户关怀信息*/
							Statement state = conn.createStatement() ;
							Result result = null;
							for(String str : keyIds){
								/*调用自定义删除方法，删除从表，兄弟表*/
								String[] files = new String[] { "", "" };
								result = new DynDBManager().delete("CRMClientInfo", allTables, str, userId,
										resources, locale,  files, new ArrayList(), conn,true) ;
								if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
									break;
								}
								if(flowFlag){
									//判断是否启用了审核流,审核中与审核完毕不得删除
									String sql = "SELECT count(*) as rows FROM CRMClientInfo WHERE workFlowNode <> '0' and id='"+str+"'";
									PreparedStatement ps = conn.prepareStatement(sql);
									ResultSet rss = ps.executeQuery();
									rss.next();
									int rowCount = rss.getInt("rows");
									if(rowCount > 0){
										String[] msgArr = new String[2];
										msgArr[0]="CRMClientInfo.del.AuditingError";
										rst.setRetCode(-2102);
										rst.setRetVal(msgArr);
										return ;
									}
								}
								
								String sql = "delete from tblCareforActionDel where ref_id in (select id from tblCareforAction where clientId='"+str+"')" ;
								state.addBatch(sql) ;
								sql = "delete from tblCareforAction where clientId='"+str+"'" ;
								state.addBatch(sql) ;
							}
							
							state.executeBatch() ;
							
							if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
								rst.setRetCode(result.retCode) ;
								rst.setRetVal(result.getRetVal()) ;
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
	 * 查询客户信息
	 * @param id
	 * @param param 字段值
	 * @param fields 字段名
	 * @return
	 */
	public Result queryClient(String condition, String fields,String keyIds,
			int pageNo, int pageSize,String keyWord,String keyFields,String keyOption,
			String sortInfo,String keySql) {
		
		String sql = "select CRMClientInfo.id,labelId,CRMClientInfo.workFlowNode,CRMClientInfo.checkPersons,CRMClientInfo.workFlowNodeName,WorkFlowStatus,tblCompany.classCode as CompanyCode,"+fields ;
		sql += " row_number() over(order by ";
			
		if(sortInfo == null || "".equals(sortInfo)){
			sql += " lastContractTime desc";
		}else{
			sql += sortInfo.split(",")[1] + " " + sortInfo.split(",")[0];
		}	
		sql +=") row_id from CRMClientInfo left join (select id as cid,classCode from tblCompany) as tblCompany on CRMClientInfo.id=tblCompany.cid " ;
		if(StringUtils.isNotBlank(keySql)){
			sql += keySql ;
		}else{
			sql += " where 1=1 " ; 
		}
		if(StringUtils.isNotBlank(condition)){
			sql += condition ;
		}
		if(StringUtils.isNotBlank(keyWord) && StringUtils.isBlank(keySql)){
			sql += " and (" ;
			if(keyIds!=null && keyIds.contains("'")){
				sql += " id in("+keyIds+"))";
			}else{
				if(keyOption != null && !"all".equals(keyOption)){
					if(keyOption.indexOf("contact") == -1){
						sql += keyOption +" like '%" + keyWord + "%')";
					}else{
						sql += "id in (select f_ref from CRMClientInfoDet where " + keyOption.replace("contact", "") + " like '%" + keyWord +"%'))";
					}
				}else{
					String contact = "" ;
					for(String field : keyFields.split(",")){
						if(field.contains("contact")){
							field = field.substring(7,field.length());
							contact += field + " like '%"+keyWord+"%' or " ;
						}else{
							sql += field+" like '%" + keyWord + "%' or " ;
						}
					}
					if(contact.endsWith("or ")){
						sql += " id in (select f_ref from CRMClientInfoDet where " + contact.substring(0,contact.length()-3) +") ";
					}
					sql += ") " ;
				}
			}
		}
		
		BaseEnv.log.debug("query client sql:"+sql);
		return sqlListMaps(sql, new ArrayList(), pageSize, pageNo) ;
	}
	public Result queryPeriodBegin(String condition, String fields,String keyIds,
			int pageNo, int pageSize,String keyWord,String keyFields,String keyOption,
			String sortInfo,String keySql) {
		
		String sql = "select id,labelId,CRMClientInfo.workFlowNode,CRMClientInfo.checkPersons,CRMClientInfo.workFlowNodeName,WorkFlowStatus,"+fields ;
		sql += " row_number() over(order by ";
			
		if(sortInfo == null || "".equals(sortInfo)){
			sql += " lastContractTime desc";
		}else{
			sql += sortInfo.split(",")[1] + " " + sortInfo.split(",")[0];
		}	
		sql +=") row_id from CRMClientInfo " ;
		if(StringUtils.isNotBlank(keySql)){
			sql += keySql ;
		}else{
			sql += " where 1=1 " ;
		}
		if(StringUtils.isNotBlank(condition)){
			sql += condition ;
		}
		if(StringUtils.isNotBlank(keyWord) && StringUtils.isBlank(keySql)){
			sql += " and (" ;
			if(keyIds!=null && keyIds.contains("'")){
				sql += " id in("+keyIds+"))";
			}else{
				if(keyOption != null && !"all".equals(keyOption)){
					if(keyOption.indexOf("contact") == -1){
						sql += keyOption +" like '%" + keyWord + "%')";
					}else{
						sql += "id in (select f_ref from CRMClientInfoDet where " + keyOption.replace("contact", "") + " like '%" + keyWord +"%'))";
					}
				}else{
					String contact = "" ;
					for(String field : keyFields.split(",")){
						if(field.contains("contact")){
							field = field.substring(7,field.length());
							contact += field + " like '%"+keyWord+"%' or " ;
						}else{
							sql += field+" like '%" + keyWord + "%' or " ;
						}
					}
					if(contact.endsWith("or ")){
						sql += " id in (select f_ref from CRMClientInfoDet where " + contact.substring(0,contact.length()-3) +") ";
					}
					sql += ") " ;
				}
			}
		}
		
		BaseEnv.log.debug("query client sql:"+sql);
		return sqlListMaps(sql, new ArrayList(), pageSize, pageNo) ;
	}
	/**
	 * 返回需要创建索引的客户
	 * @param module
	 * @return
	 */
	public void queryModuleData(final ClientModuleBean module,final IndexWriter indexWriter){
	    DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	             session.doWork(new Work() {
	                   public void execute(Connection conn) {
	                	   try{/*
	                		   String queryField = module.getListFields();
	                		   String detSQL = "";
	                		   for(String field : module.getKeyFields().split(",")){
	                			   if(field.startsWith("contact")){
	                				   detSQL += field.substring(7, field.length()) + ",";
	                			   }else{
	                				   if(queryField.contains(field+","))continue;
	                				   queryField += field+",";
	                			   }
	                		   }
	                		   long long1 = System.currentTimeMillis();
	                		   String sql = "select "+queryField + "id from CRMClientInfo where moduleId=?" ;
	                		   PreparedStatement pss = conn.prepareStatement(sql);
	                		   pss.setString(1, module.getId());
	                		   ResultSet rss = pss.executeQuery();
	                		   
	                		   List<String[]> clientList = new ArrayList<String[]>();
                			   String[] fieldList = queryField.split(",");
                			   
	                		   while(rss.next()){
	                			   String[] strArray = new String[fieldList.length+1];
	                			   strArray[0] = rss.getString("id");
	                			   for(int i=0;i<fieldList.length;i++){
	                				   strArray[i+1] = rss.getString(fieldList[i]);
	                			   }
	                			   clientList.add(strArray);
	                		   }
	                		   long long2 = System.currentTimeMillis();
	                		   System.out.println("cast client time1: "+(long2-long1));
	                		   sql = "select " + detSQL + "f_ref from CRMClientInfoDet where f_ref in(select id from CRMClientInfo where moduleId=?)";
	                		   pss = conn.prepareStatement(sql);
	                		   pss.setString(1, module.getId());
	                		   rss = pss.executeQuery();
	                		   HashMap<String, List<String[]>> detMap = new HashMap<String, List<String[]>>();
                			   String[] detFieldList = detSQL.split(",");
	                		   while(rss.next()){
	                			   List<String[]> detList = detMap.get(rss.getString("f_ref"));
	                			   String[] detArray = new String[detFieldList.length+1];
	                			   if(detList==null){
	                				   detList = new ArrayList<String[]>();
	                				   detMap.put(rss.getString("f_ref"), detList);
	                			   }
	                			   for(int i=0;i<detFieldList.length;i++){
	                				   detArray[i] = rss.getString(detFieldList[i]);
	                			   }
	                			   detList.add(detArray);
	                			   System.out.println("client contact:"+detList.size());
	                		   }
	                		   long long3 = System.currentTimeMillis();
	                		   System.out.println("cast client time2: "+(long3-long2));
	                		   
	                		   for(String[] array : clientList){
	                			   Document doc = new Document();
	                			   doc.add(new Field("id",array[0]==null?"":array[0],Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
	                			   for(int i=0;i<fieldList.length;i++){
	                				   doc.add(new Field(fieldList[i],array[i+1]==null?"":array[i+1],Store.YES,Field.Index.ANALYZED));
	                			   }
	                			   if(detMap!=null && detMap.size()>0){
	                				   for(int i=0;i<detFieldList.length-1;i++){
	                					   if(detMap.get(array[0])==null)continue;
	                					   String fieldValue = "";
	                					   for(String[] valueArr : detMap.get(array[0])){
	                						   fieldValue += (valueArr[i]==null?"":valueArr[i])+":";
	                					   }
	                					   doc.add(new Field(detFieldList[i],fieldValue,Store.YES,Field.Index.ANALYZED));
	                				   }
	                			   }
	                			   indexWriter.addDocument(doc);
	                		   }
	                		   long long4 = System.currentTimeMillis();
	                		   System.out.println("cast client time3: "+(long4-long3));
	                		   System.out.println("cast all time:"+(long4-long1));
	                		   indexWriter.close();*/
		                   }catch (Exception ex) {
								BaseEnv.log.error("ClientMgt queryModuleData mehtod:", ex);
								ex.printStackTrace();
		                   }
	                   }
				});
				return 0;
			}
		});
	}
	
	/**
	 * 查询联系人信息
	 * @param id
	 * @param param 字段值
	 * @param fields 字段名
	 * @return
	 */
	public Result queryContacts(List clientList,String fields) {
		Result result = new Result() ;
		StringBuilder condition = new StringBuilder() ;
		for(int i=0;i<clientList.size();i++){
			HashMap clientMap = (HashMap) clientList.get(i) ;
			condition.append("'").append(clientMap.get("id")).append("',") ;
		}
		if(condition.length()>0){
			condition.deleteCharAt(condition.length()-1) ;
		}
		StringBuilder strFields = new StringBuilder(); ;
		for(String field : fields.split(",")){
			DBFieldInfoBean dbField = GlobalsTool.getDetField(field) ;
			if(dbField!=null){
				strFields.append(field.substring(7, field.length())).append(",") ; 
			}
		}
		String sql = "select id,f_ref,"+strFields.toString() 
				+ " row_number() over(order by detOrderNo asc) row_id from CRMClientInfoDet " 
				+ " where 1=1 and f_ref in (" +condition+")";
		
		result = sqlListMaps(sql, new ArrayList(),1,0) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List detList = (List) result.retVal ;
			/*把联系人封装在相应的客户id下的HashMap中*/
			HashMap contactMap = new HashMap() ;
			for(int j=0;j<detList.size();j++){
				HashMap detMap = (HashMap) detList.get(j) ;
				String clientId = (String) detMap.get("f_ref") ;
				if(contactMap.get(clientId)==null){
					List contactList = new ArrayList() ;
					contactList.add(detMap) ;
					contactMap.put(clientId, contactList) ; 
				}else{
					List contactList = (List) contactMap.get(clientId) ;
					contactList.add(detMap) ;
				}
			}
			result.retVal = contactMap ;
		}
		return result ;
	}
	
	/**
	 * 查找所有的模板
	 * @return
	 */
	public Result findModuleNames(){
		List list = new ArrayList();
		String sql = "select id,moduleName from CRMClientModule" ;
		return this.sqlList(sql, list);
	}
	
	/**
	 * 根据模板ID查找模板信息
	 * @param moduleId
	 * @return
	 */
	public Result findCRMModule(String moduleId){
		List list = new ArrayList();
		String sql = "select pageFields from CRMClientModule where id='" + moduleId + "'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 添加客户信息
	 * @param clientCols 
	 * @param clientVals
	 * @param contactCols
	 * @param contactVals
	 * @param clientId
	 * @return
	 */
	public Result addClientInfo(final Map allTables,final HashMap valueMap,final LoginBean loginBean,
			final MessageResources resources,final Locale locale){
		 final Result rs = new Result();
	       int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) {
	                	   try{
	                		   DynDBManager dbmgt = new DynDBManager();
	                		   final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId()) ;
	                		   Result result = dbmgt.execAdd("CRMClientInfo", allTables, valueMap,sessionSet, loginBean.getId(), "", "", conn, resources, locale, "") ;
	                		   rs.setRetCode(result.retCode);
	                		   rs.setRetVal(result.retVal);
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
	                		   BaseEnv.log.debug("UserFunctionMgt addClientInfo Error");
	                	   }
	                   }
	               });
	               return ErrorCanst.DEFAULT_SUCCESS;
	           }
	       });
	       if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
				BaseEnv.log.error("EMailMgt.addMailCRM() Error ");
				rs.retCode = retCode;
				return rs;
		   }
	       return rs;
	}
	
	/**
	 * 根据列名，值查找客户或联系人信息用于ajax
	 * @param colName
	 * @param colVal
	 * @return
	 */
	public Result checkContactCol(String colName,String colVal,String clientId){
		String sql="select ";
		List param = new ArrayList();
		if("ClientName".equals(colName)){
			sql += "id,createBy,Status from CRMClientInfo where ";
		}else{
			sql += "CRMClientInfoDet.id,b.createBy,UserName from CRMClientInfoDet,CRMClientInfo b where f_ref=b.id and ";
		}
		sql +=colName + " =?" ;
		param.add(""+colVal+"");
		
		if(clientId!=null && !"".equals(clientId)){
			sql += " and id <> ?" ;
			param.add(""+clientId+"");
		}
		
		return this.sqlList(sql, param);
	}
	
	/**
	 * 根据moduleId获得分组信息
	 * @param tableNAME
	 * @return
	 */
	public Result findTableGroup(String moduleId){
		List list = new ArrayList();
//		String sql="SELECT distinct zh_CN FROM tblLanguage tl WHERE id IN(SELECT groupName FROM tblDBFieldInfo tdi WHERE tdi.tableId IN (SELECT id FROM tbldbtableinfo WHERE tableNAME = '" + tableName + "'))";
		String sql="select groupNames from CRMClientModule where id = '" + moduleId + "'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 用HQL查找隐藏或只读权限。
	 * @param deptId 部门id
	 * @param userId 用户id
	 * @param type hidden表示隐藏 read表示只读
	 * @return
	 */
	public Result findFieidScopes(String deptId,String userId,String type,String viewId){
		List list = new ArrayList();
		
		String sql="FROM FieldScopeSetBean where viewId ='" + viewId + "' and ";
		
		if("hidden".equals(type)){
			sql +=" (" + "dbo.exist_dept(hiddenpopedomDeptIds,'"+deptId+"')='true' or ','+ hiddenpopedomUserIds  like '%," + userId +",%')";
		}else{
			sql +=" (" + "dbo.exist_dept(readpopedomDeptIds,'"+deptId+"')='true' or ','+ readpopedomUserIds  like '%," + userId +",%')";
		}
 		return this.list(sql, list);
	}
	
	
	/**
	 * 根据ID查找区域信息
	 * @param deptId
	 * @return
	 */
	public Result findDistrict(String districtId){
		List list = new ArrayList();
		String sql = "select DistrictFullName FROM tblDistrict where classCode='" + districtId + "'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 根据ID查找行业信息
	 * @param deptId
	 * @return
	 */
	public Result findCRMWorkProfession(String professionId){
		return this.loadBean(professionId, WorkProfessionBean.class);
	}
	
	/**
	 * 根据联系人Id查询联系人信息
	 * @param id
	 * @return
	 */
	public Result detailClientDetById(String id){
		List<String> param = new ArrayList<String>();
		String sql = "select userName,mobile,clientEmail,mainUser,id from CRMClientInfoDet where id= ?";
		param.add(""+id+"");
		return this.sqlList(sql, param);
	}
	
	/**
	 * 获得所有从表的附件信息 
	 * @param keyIds
	 * @return
	 */
    public Result findBrotherAttachment(final List<String> list,final String keyId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							HashMap<String, List<String>> attachmentMap = new HashMap<String, List<String>>();
							for(int i=0;i<list.size();i++){
								String sql = "select attachment from " + list.get(i) + " where f_brother = '" + keyId + "'";
								PreparedStatement pss = conn.prepareStatement(sql);
								ResultSet rss = pss.executeQuery();
								while(rss.next()){
									String str = rss.getString("attachment");
									if(str !=null && !"".equals(str)){
										List<String> tempList = new ArrayList<String>();
										for(int j=0;j<str.split(";").length;j++){
											tempList.add(str.split(";")[j]); 
										}
										attachmentMap.put(list.get(i),tempList);
									}
								}
								rst.setRetVal(attachmentMap);
							}
							
							
							
						}catch (Exception ex) {
							BaseEnv.log.error("ClientMgt findAllAttachment mehtod:", ex) ;
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
	 * 获取客户的导入，导出，打印的权限设置
	 * @return
	 */
	
	public HashMap<String,Boolean> getCRmScope(LoginBean bean,String viewId){
		List<String> param=new ArrayList<String>();
		String sql="select id from CRMCommonTable  where viewId='"+viewId+"'"; 
		if(!("1".equals(bean.getId()))){
			sql += " and (fields1='0' or dbo.exist_dept(fields2,?)='true' or ','+fields3 like ? or ','+fields4 like ? ) ORDER BY id  DESC ";
			param.add("" + bean.getDepartCode()+ "");
			param.add("%," + bean.getId()+ ",%");
			String group=" ";
			if(!"".equals(bean.getGroupId()))
				group=bean.getGroupId();
			param.add("%," + group+ ",%");
		}
		
		Result rsScope= sqlList(sql, param);
		HashMap<String,Boolean> rsMap = new HashMap<String, Boolean>();
		if(rsScope.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List scopeList=(List) rsScope.retVal;
			if(scopeList!=null && scopeList.size()>0){
				Boolean printFlag=false;//打印权限
				Boolean importFlag=false;//导入权限
				Boolean exportFlag=false;//导出权限
				for(int i=0;i<scopeList.size();i++){
					String scope=new GlobalsTool().get(scopeList.get(i),0).toString();
					if(scope.indexOf("printScope")>-1){
						printFlag=true;
					}
					if(scope.indexOf("importScope")>-1){
						importFlag=true;
					}
					if(scope.indexOf("exportScope")>-1){
						exportFlag=true;
					}
				}
				
				rsMap.put("printFlag", printFlag);
				rsMap.put("importFlag", importFlag);
				rsMap.put("exportFlag", exportFlag);
			}
		}
		
		return rsMap;
	}
	
	/**
	 * 查询客户的地址
	 * @param keyIds
	 * @return
	 */
    public Result getClientAddress(final String keyId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "select Address from CRMClientInfo where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							ResultSet rss = pss.executeQuery();
							if(rss.next()){
								rst.setRetVal(rss.getString("Address"));
							}
						}catch (Exception ex) {
							BaseEnv.log.error("ClientMgt getClientAddress mehtod:", ex) ;
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
	 * 获取创建人信息
	 * @return
	 */
	public Result findEmployee(String id){
		return this.loadBean(id,EmployeeBean.class);
	}
	
	
	/**
	 * 模板转移
	 * @param keyIds
	 * @return
	 */
    public Result moduleTransfer(final String[] keyIds,final String moduleId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
								Statement state = conn.createStatement() ;
								for(String str : keyIds){
									String sql = "update CRMClientInfo set moduleId ='" + moduleId +"' where id='" + str +"'";  
									state.addBatch(sql) ;
								}
								state.executeBatch() ;
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt moduleTransfer mehtod:", ex) ;
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
	 * 设置默认模板
	 * @param keyIds
	 * @return
	 */
    public Result moduleTransfer(final String defModuleId,final String loginId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "update tblemployee set moduleId =? where id =?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, defModuleId);
							pss.setString(2, loginId);
							pss.execute() ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt moduleTransfer mehtod:", ex) ;
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
    
	public Result findEnumeration(String enumerName){
		List list = new ArrayList();
		String sql = "select a.id,b.zh_CN from tblDBEnumeration a ,tbllanguage b where a.languageId = b.id and a.enumName = '" + enumerName + "'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 新增选项数据
	 * @param keyIds
	 * @return
	 */
    public Result addEnumer(final String languageSql,final String enumerSql) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							PreparedStatement s = conn.prepareStatement(languageSql);
							s.executeUpdate();
							s = conn.prepareStatement(enumerSql);
							s.executeUpdate();
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt moduleTransfer mehtod:", ex) ;
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
	 * 根据ID查找客户
	 * @param id
	 * @return
	 */
	public Result findClient(String clientId){
		List list = new ArrayList();
		String sql = "select clientName from CRMClientInfo where id= '" + clientId + "'";
		return this.sqlList(sql, list);
	}
    
	/**
	 * 查看选项数据是否值
	 * @param moduleId
	 * @return
	 */
	public Result findEnumer(String enumerId,String enumerVal){
		List list = new ArrayList();
		String sql = "select * from tblDBEnumerationItem where enumId = '"+enumerId+"' and enumValue='" + enumerVal +"'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 根据枚举名称与选项值查找EnumerItem
	 * @param moduleId
	 * @return
	 */
	public Result findEnumerItem(String selName,String enumerVal){
		List list = new ArrayList();
		String sql = "select id from tblDBEnumerationItem where enumId = (select id from tblDBEnumeration where enumName = '"+selName+"') and enumValue='" + enumerVal + "'";
		return this.sqlList(sql, list);
	}
	
	/**
	 * 排序查找分组信息
	 * @param id
	 * @return
	 */
	public Result findEnumersByOrder(String enumName){
		List<String> param = new ArrayList<String>();
		String sql = "select a.enumValue,b.zh_CN from tblDBEnumerationItem a,tblLanguage b where enumId = (select id from tblDBEnumeration where enumName=?) and a.languageId = b.id order by a.enumOrder";
		param.add(""+enumName+"");
		return this.sqlList(sql, param);
	}
	
	
	//保存<update saveField="">中要保存的字段值
    HashMap savedFields = new HashMap();
	/**
     * 修改数据
     * @param tableName String
     * @param allTables Map
     * @param values HashMap
     * @return Result
     */
    public Result update(final ClientModuleBean moduleBean, final Map allTables,
                         final HashMap str_values, final String userId,final String defineInfo,
                         final MessageResources resources,final Locale locale,final String saveType,
                         final String[] flows,final LoginBean loginBean,final OAWorkFlowTemplate workFlow,
                         final Hashtable props,final MOperation mop,final ClientInfoRecordBean recordBean) {
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	
                    	//ERP客户同步CRM客户
						updateClientSynchro(moduleBean, str_values, conn);
						
						
                    	String tableName = moduleBean.getTableInfo().split(":")[0];
                    	DynDBManager manager = new DynDBManager();
                    	HashMap values = str_values ;
                        Obj beforeObject = new Obj();
                        //转换特殊字符
                        DBTableInfoBean tableInfo = DDLOperation.getTableInfo((Hashtable) allTables,tableName);
                    	Result sp_values =  GlobalsTool.conversionSpecialCharacter(tableInfo.getFieldInfos(),values) ;
                        values = (HashMap) sp_values.getRetVal() ;
                        Result defineRs = null ;
                        //执行表自定义语句
                        if(!"saveDraft".equals(saveType)){
	                        defineRs = manager.defineSql(conn, "update", false,
	                                tableName, (Hashtable) allTables, values,
	                                values.get("id").toString(), userId,defineInfo, resources,locale);
	                        if (defineRs.getRetCode() <
	                            ErrorCanst.DEFAULT_SUCCESS) {
	                            rs.setRetCode(defineRs.getRetCode());
	                            rs.setRetVal(defineRs.getRetVal());
	                            BaseEnv.log.error(
	                                    "DynDBManager before update defineSql Error code = " +
	                                    defineRs.getRetCode() + " Msg=" +
	                                    defineRs.getRetCode());
	                            return;
	                        }
                        }
                        //执行修改前的接口
                        BaseCustomFunction impl = (BaseCustomFunction)BaseEnv.functionInterface.get(tableName);
                        if (impl != null) {
                            Result interfaceRs = impl.onBeforUpdate(conn,tableName,allTables, values, beforeObject);
                            if (interfaceRs.getRetCode() < 0) {
                                rs.setRetCode(interfaceRs.getRetCode());
                                BaseEnv.log.error("DynDBManager onBeforeUpdate Error code = " +
                                        interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetCode());
                                return;
                            }
                        }
                        final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId()) ;
                        //执行主表修改
                        Result ires =  manager.execUpdate(conn, tableName, allTables,values,sessionSet);
                        if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
                            BaseEnv.log.debug("Update Table " + tableName + " Info: " + ires.getRetVal());
                            rs.setRetCode(ires.getRetCode());
                            rs.setRetVal(ires.getRetVal());
                            return;
                        }
                        ArrayList childTableList = DDLOperation.getChildTables(tableName,allTables);
                        for (int i = 0; i < childTableList.size(); i++) {
                            DBTableInfoBean childTb = (DBTableInfoBean)childTableList.get(i);
                            ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
                            if (childList == null) {
                                continue;
                            }
                            
                            
                            String insertTableName = new DynDBManager().getInsertTableName(childTb.getTableName());//插入数据库的表名,用于处理新增模板表结构的表名
                            
                            //删除多语言
                            KRLanguageQuery.delete(conn,childTb,values.get("id").toString(),"f_ref");
                            //先删除从表
                            ires = manager.execDelete(conn, insertTableName,values.get("id").toString(),childList);
                            if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
                                BaseEnv.log.debug("Delete child Table " + insertTableName + " Info: " + ires.getRetVal());
                                rs.setRetCode(ires.getRetCode());
                                rs.setRetVal(ires.getRetVal());
                                return;
                            }

                            for (int j = 0; j < childList.size(); j++) {
                                HashMap childMap = (HashMap) childList.get(j);
                                childMap.put("id", IDGenerater.getId());
                                childMap.put("f_ref", values.get("id"));
                                //转换特殊字符
                                //sp_values =  conversionSpecialCharacter(childTb.getFieldInfos(),childMap) ;
                                //childMap = (HashMap) sp_values.getRetVal() ;
                                //执行从表插入
                                ires = manager.execInert(conn, insertTableName,allTables, childMap,"",resources,locale);
                                if (ires.getRetCode() !=
                                    ErrorCanst.DEFAULT_SUCCESS) {
                                    BaseEnv.log.debug("Insert child Table " + insertTableName + " Info: " + ires.getRetVal());
                                    rs.setRetCode(ires.getRetCode());
                                    rs.setRetVal(ires.getRetVal());
                                    return;
                                }
                                childsIns.add(addSql);
                                addSql = "";
                            }
                        }

                        if(!"saveDraft".equals(saveType)){
	                        //执行表自定义语句
	                        defineRs = manager.defineSql(conn, "update", true,tableName,(Hashtable) allTables, values,
	                                             values.get("id").toString(), userId,defineInfo,resources,locale);
	                        if (defineRs.getRetCode() <
	                            ErrorCanst.DEFAULT_SUCCESS) {
	                            rs.setRetCode(defineRs.getRetCode());
	                            rs.setRetVal(defineRs.getRetVal());
	                            BaseEnv.log.error("DynDBManager after update defineSql Error code = " +
	                                    defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
	                            return;
	                        }
	                        if(defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT){
	        	            	rs.setRetCode(defineRs.retCode) ;
	        	            	rs.setRetVal(defineRs.retVal) ;
	        	            }
	                        //执行修改后的接口
	                        if (impl != null) {
	                            Result interfaceRs = impl.onAfterUpdate(conn,tableName,allTables, values, beforeObject);
	                            if (interfaceRs.getRetCode() < 0) {
	                                rs.setRetCode(interfaceRs.getRetCode());
	                                BaseEnv.log.error("DynDBManager onAfterUpdate Error code = " + interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetVal());
	                                return;
	                            }
	                        }
	
	                        //update之后，将saveField的值恢复
	                        String fieldCalculate = tableInfo.getFieldCalculate();
	                        while (fieldCalculate.indexOf("<sql") > -1) {
	                            String defineSql = fieldCalculate.substring(
	                                    fieldCalculate.indexOf("<sql"),
	                                    fieldCalculate.indexOf(">"));
	                            fieldCalculate = fieldCalculate.substring(
	                                    fieldCalculate.indexOf(">") + 1);
	                            String post = manager.getAttribute("post", defineSql);
	                            String saveField = manager.getAttribute("saveField",
	                                    defineSql);
	                            String selfKey = manager.getAttribute("selfKey", defineSql);
	                            String matchKey = manager.getAttribute("matchKey",
	                                    defineSql);
	
	                            boolean postIsAfter = "before".equalsIgnoreCase(post) ? false : true;
	                            if (postIsAfter) {
	                            	 //位置是after。则执行保恢复saveField操作
	                                if (saveField != null && saveField.length() > 0 &&
	                                    selfKey != null &&
	                                    selfKey.length() > 0 &&
	                                    matchKey != null && matchKey.length() > 0) {
	                                    Object fieldValue = values.get(selfKey);
	
	                                    for (int i = 0; i < savedFields.size(); i++) {
	                                    	if(savedFields.get(savedFields.keySet().toArray()[i])!=null
	                                    			&&savedFields.get(savedFields.keySet().toArray()[i]).toString().length()>0){
	                                    		String sql = "";
	                                    		if (fieldValue == null) {  //如果是手工凭证
	                                    			fieldValue = values.get("id");
	                                    			sql = "update " + saveField.substring(0,saveField.indexOf("_")) 
	                                    			    + " set " + saveField.replace("_", ".") + "=" 
	                                    			    + savedFields.get(savedFields.keySet().toArray()[i]) 
	                                    			    + " where id='" + fieldValue + "'";
	                                    		} else {
	                                    			sql = "update " + saveField.substring(0,saveField.indexOf("_")) 
	                                    			    + " set " + saveField.replace("_", ".") + "=" 
	                                    			    + savedFields.get(savedFields.keySet().toArray()[i]) 
	                                    			    + " where " + matchKey.replace("_", ".") + "='" + fieldValue + "'";
	                                    		}
	                                    		Result myRs = new Result();
	                                    		try {
	                                    			PreparedStatement ps = conn.prepareStatement(sql);
	                                    			int result = ps.executeUpdate();
	                                    			myRs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
	                                    		} catch (Exception ex) {
	                                    			ex.printStackTrace();
	                                    			myRs.setRetCode(ErrorCanst.SQLEXCEPTION_ERROR);
	                                    		}
	                                    	}
	                                    }
	                                }
	                            }
	
	                        }
                        }
                        
                        if(!("no").equals(recordBean.getUpdateInfo())){
                        	String sql = "insert into CRMClientInfoRecord(id,clientId,updateInfo,mapInfo,tableInfo,moduleId,createTime,employeeId,ipAddress,viewId) values(?,?,?,?,?,?,?,?,?,?)";
                        	PreparedStatement ps = conn.prepareStatement(sql);
                        	ps.setString(1, recordBean.getId());
                        	ps.setString(2, recordBean.getClientId());
                        	ps.setString(3, recordBean.getUpdateInfo());
                        	ps.setString(4, recordBean.getMapInfo());
                        	ps.setString(5, recordBean.getTableInfo());
                        	ps.setString(6, recordBean.getModuleId());
                        	ps.setString(7, recordBean.getCreateTime());
                        	ps.setString(8, recordBean.getEmployeeId());
                        	ps.setString(9, recordBean.getIpAddress());
                        	ps.setString(10, recordBean.getViewId());
                        	ps.executeUpdate();
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retVal);
        return rs;
    }
    
    /**
	 * 根据ID获得所有历史记录
	 * @param moduleId
	 * @return
	 */
	public Result findHistory(String clientId){
		String hql = "from ClientInfoRecordBean where clientId = '"+clientId+"' order by createTime DESC";
		return this.list(hql,new ArrayList());
	}
    
    /**
	 * 根据ID获得历史记录
	 * @param moduleId
	 * @return
	 */
	public Result findClientRecord(String recordId){
		return this.loadBean(recordId,ClientInfoRecordBean.class);
	}
	
	/**
	 * 详情更新客户单个字段
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result updateField(final String id,final String fieldName,final String fieldValue,final boolean isContact,final ClientInfoRecordBean clientRecordBean,final LoginBean loginBean,final ClientModuleBean moduleBean,final HashMap values) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				if(!clientRecordBean.getUpdateInfo().equals("")){
					addBean(clientRecordBean,session);
				}	
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "";
							if(isContact){
								sql = "UPDATE CRMClientInfoDet SET "+fieldName+" = ? WHERE id = ?";	
							}else{
								sql = "UPDATE CRMClientInfo SET "+fieldName+"=? WHERE id=?";
							}
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, fieldValue);
							ps.setString(2, id);
							ps.executeUpdate();
							
							String updateTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
							sql = "UPDATE CRMClientInfo SET lastUpdateTime ='"+updateTime+"',LastContractTime='"+updateTime+"' WHERE id='"+id+"'";
							ps = connection.prepareStatement(sql);
							ps.executeUpdate();
							
							String context = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd) + loginBean.getName()+"修改客户" ;
							insertCRMCLientInfoLog(id, "history", context, loginBean.getId(), connection) ;//插入客户日志
							
							 //ERP客户同步CRM客户
	                        updateClientSynchro(moduleBean, values, connection);
							
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
	 * 根据ID查看是否有联系人相关信息
	 * @param moduleId
	 * @return
	 */
	public Result findContactByClientId(String clientId,String fieldName){
		String str = "";
		for (int i = 0; i < clientId.split(",").length; i++) {
			str +="'" + clientId.split(",")[i] +"',";
		}
		if(str.endsWith(",")){
			str = str.substring(0,str.length()-1);
		}
		String sql = "SELECT "+fieldName+" FROM CRMClientInfoDet WHERE f_ref in ("+str+") and "+fieldName+" !=''";
		return this.sqlList(sql,new ArrayList());
	}
	
	/**
	 * 新增CRM标签
	 * @param moduleId
	 * @return
	 */
	public Result addCRMLabel(CRMLabelBean labelBean){
		return this.addBean(labelBean);
	}
	
	/**
	 * 根据所有CRM标签信息
	 * @param moduleId
	 * @return
	 */
	public Result findCRMLabels(String employeeId,String moduleId){
		String hql = "from CRMLabelBean where employeeId = '"+employeeId+"' and moduleId = '"+moduleId+"' ORDER BY createTime";
		return this.list(hql, new ArrayList());
	}
	
	/**
	 * 更新标签
	 * @param moduleId
	 * @return
	 */
	/**
	 * 详情更新客户单个字段
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result updateLabel(final String clientId,final String labelId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "UPDATE CRMClientInfo SET labelId = ? WHERE id = ?";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, labelId);
							ps.setString(2, clientId);
							ps.executeUpdate();
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
	 * 删除标签
	 * @param moduleId
	 * @return
	 */
	public Result delLabel(final String labelId,final String isDelBean,final String clientId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Statement stmt = connection.createStatement();
							String sql = "UPDATE CRMClientInfo SET labelId = replace(labelId,'"+labelId+",','') where labelId is not null";
							if(isDelBean !=null && "true".equals(isDelBean)){
								String sql1 = "DELETE FROM CRMLabel where id ='" + labelId + "'"; 
								stmt.addBatch(sql1);
							}else{
								sql += " and id = '" + clientId + "'";
							}
							stmt.addBatch(sql);
							stmt.executeBatch();
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
	 * 根据ID查找标签
	 * @param moduleId
	 * @return
	 */
	public Result findLabel(String labelId){
		return this.loadBean(labelId, CRMLabelBean.class);
	}
	
	/**
	 * 更新CRM标签
	 * @param moduleId
	 * @return
	 */
	public Result updateLabel(CRMLabelBean labelBean){
		return this.updateBean(labelBean);
	}
	
	/**
	 * 详情更新客户单个字段
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result updateLabelUse(final String moduleId,final String useFlag) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "UPDATE CRMClientModule SET isUserLabel = '"+useFlag+"' where id = '"+moduleId+"'";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.executeUpdate();
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
	 * 根据clientId查找共享信息
	 * @param moduleId
	 * @return
	 */
	public Result findSharerByClientId(String clientId){
		String hql = "from CRMShareClientBean where clientId in ("+clientId+")";
		return this.list(hql, new ArrayList());
	}
	
	
	/**
	 * 根据部门、ID、分组、职位查找用户ID
	 * @param userIds 用户
	 * @param deptIds 部门
	 * @param empGroupIds 职员分组
	 * @param empGroupIds 职位
	 * @return
	 */
	public Result findEmpoyeeByAll(String userIds,String deptIds,String empGroupIds,String titleIds){
		String hql = "select id,departmentCode from tblEmployee where ";
		String userId = "";
		String deptId = "";
		String empGroupId = "";
		String titleId = "";
		if(userIds !=null && !"".equals(userIds)){
			for(String str : userIds.split(",")){
				userId += "'" + str +"',";
			}
			if(userId.endsWith(",")){
				userId = userId.substring(0,userId.length()-1);
			}
			hql += " id in(" + userId + ") ";
		}
		
		if(deptIds !=null && !"".equals(deptIds)){
			for(String str : deptIds.split(",")){
					deptId += "DepartmentCode like '"+str+"%' or ";
			}
			if(deptId.endsWith("or ")){
				deptId = deptId.substring(0,deptId.length()-3);
			}
			if(!hql.endsWith("where ")){
				hql += " or ";
			}
			hql +=deptId;
		}
		
		if(empGroupIds !=null && !"".equals(empGroupIds)){
			for(String str : empGroupIds.split(",")){
				empGroupId += "'" + str +"',";
			}
			if(empGroupId.endsWith(",")){
				empGroupId = empGroupId.substring(0,empGroupId.length()-1);
			}
			if(!hql.endsWith("where ")){
				hql += " or ";
			}
			hql +=" (id in (select userId from tblempgroupUser where f_ref in("+empGroupId+")))";
		}
		
		if(titleIds !=null && !"".equals(titleIds)){
			for(String str : titleIds.split(",")){
				titleId += "'" + str +"',";
			}
			if(titleId.endsWith(",")){
				titleId = titleId.substring(0,titleId.length()-1);
			}
			if(!hql.endsWith("where ")){
				hql += " or ";
			}
			hql += " titleId in(" + titleId + ") ";
		}
		return this.sqlList(hql, new ArrayList());
	}
	
	
	/**
	 * 根据ID查找客户名称
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result queryClientNameByIds(final String ids) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "SELECT id,clientName from CRMClientInfo where id in ("+ids+")";
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							ArrayList<String[]> logList = new ArrayList<String[]>() ;
							HashMap<String, String> clientMap = new HashMap<String, String>();
							while(rss.next()){
								clientMap.put(rss.getString("id"), rss.getString("clientName"));
							}
							rst.setRetVal(clientMap) ;
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
	 * 根据客户ID查找模板id与名称
	 * @param keyId
	 * @return
	 */
	public Result findClientById(String keyId){
		List param = new ArrayList();
		String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE id=?";
		param.add(""+keyId+"");
		return this.sqlList(sql, param);
		
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
	
	
	public String findClientNameById2(String keyId,Connection conn){
		String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE id='"+keyId+"'";
		Statement stmt;
		String temp="";
		try {
			stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				temp=rs.getString("clientName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        return temp;
	}
	
	/**
	 *根据IDS查找多个客户 
	 * @param ids
	 * @return
	 */
	public Result findClients(String ids){
		List param = new ArrayList();
		String sql = "SELECT id,clientName FROM CRMClientInfo WHERE id in ("+ids+")";
		return this.sqlList(sql, param);
	}
	
	
	
	/**
	 *  CRMERP客户转换添加
	 * @param mainFields 主表需要查询的字段名
	 * @param childFields 从表需要查询的字段名
	 * @param mainValList 主表P处理存放的值
	 * @param childValList 从表P处理存放的值
	 * @return
	 */
	public Result addTransfer(final String mainFields,final String childFields,final List<HashMap<String,String>> mainValList,final HashMap<String, List<HashMap<String,String>>> childMap,final String transferType,final LoginBean loginBean,final String ipAddress,final Hashtable allTables,final MessageResources resources,final Locale locale,final String parentClasscode) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//插入的主表与子表,默认crm转erp
							String tableName = "tblCompany";
							String childTableName = "tblCompanyEmployeeDet";
							String recordType = "1";//CRM转ERP
							if("erpToCrm".equals(transferType)){
								//erp转crm
								tableName = "CRMClientInfo";
								childTableName = "CRMClientInfoDet";
								recordType = "2";
							}
							
							//封装主表sql语句
							String sql = "INSERT INTO "+tableName+"("+mainFields+") Values(";
							for(String field : mainFields.split(",")){
								sql +="?,";
							}
							if(sql.endsWith(",")){
								sql = sql.substring(0,sql.length()-1);
							}
							sql +=")";
							
							
							//封装从表sql语句
							
							String childSql = "INSERT INTO "+childTableName+"("+childFields+") Values(";
							for(String field : childFields.split(",")){
								childSql +="?,";
							}
							
							if(childSql.endsWith(",")){
								childSql = childSql.substring(0,childSql.length()-1);
							}
							childSql +=")";
							
							//封装插入记录
							String recordSql = "INSERT INTO CRMTransferRecord(id,createBy,clientId,transferType,ipAddress,createTime) VALUES(?,?,?,?,?,?)";
							
							for(HashMap<String,String> mainMap : mainValList){
								if("crmToErp".equals(transferType)){
									String classCode = getLevelCode("tblCompany",parentClasscode, connection);
									mainMap.put("classCode",classCode);
									
									//copy附件 Attachment=天助网LOGO1.png;天助网LOGO-原始可印刷文件.png;a.zip
									String Attachment = mainMap.get("Attachment");
									if(Attachment != null && !Attachment.equals("")){
										String[] fs = Attachment.split(";");
										for(String f:fs){
											String sour = FileHandler.getPathReal("CRMClientInfo", FileHandler.TYPE_AFFIX, f);
											String des= FileHandler.getPathReal("tblCompany", FileHandler.TYPE_AFFIX, f);
											if(!(new File(des)).getParentFile().exists()){
												(new File(des)).getParentFile().mkdirs();
											}
											FileHandler.copyFile(sour, des);
										}
									}
								}
								
								//执行主表语句
								PreparedStatement ps = connection.prepareStatement(sql);
								int i=1;
								for(String field : mainFields.split(",")){
									ps.setString(i, mainMap.get(field));
									i++;
								}
								ps.executeUpdate();
								
								//执行从表语句
								List<HashMap<String,String>> childValList = childMap.get(mainMap.get("id"));//根据客户ID,拿到联系人LIST插入
								if(childValList!=null && childValList.size()>0){
									PreparedStatement ps2 = connection.prepareStatement(childSql);
									for(HashMap<String,String> childMap : childValList){
										int j=1;
										for(String field : childFields.split(",")){
											String value = childMap.get(field);
											
											//主要处理CRM转ERP,插入ERP联系人性别时若没有值默认给-100000
											if("Gender".equals(field) && "crmToErp".equals(transferType)){
												if(childMap.get(field) == null || "".equals(childMap.get(field))){
													value = "-100000";
												}
											}
											ps2.setString(j,value);
											j++;
										}
										ps2.addBatch();
									}
									ps2.executeBatch();
								}
								
								//插入转换记录信息
								PreparedStatement ps3 = connection.prepareStatement(recordSql);
								ps3.setString(1,IDGenerater.getId());
								ps3.setString(2,loginBean.getId());
								ps3.setString(3,mainMap.get("id").toString());
								ps3.setString(4,recordType);
								ps3.setString(5,ipAddress);
								ps3.setString(6,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
								ps3.executeUpdate();
								
								
								String updSql = "";
								if("erpToCrm".equals(transferType)){
									updSql = "UPDATE CRMClientInfo SET createBy=(SELECT EmployeeID FROM tblCompany WHERE id=?),departmentCode=(SELECT DepartmentCode FROM tblCompany WHERE id=?) WHERE id=?";
								}else{
									updSql = "UPDATE tblCompany SET EmployeeID=(SELECT createBy FROM CRMClientInfo WHERE id=?),DepartmentCode=(SELECT departmentCode FROM CRMClientInfo WHERE id=?) WHERE id=?";
								}
								String clientId = String.valueOf(mainMap.get("id"));
								PreparedStatement ps4 = connection.prepareStatement(updSql);
								ps4.setString(1,clientId);
								ps4.setString(2,clientId);
								ps4.setString(3,clientId);
								ps4.executeUpdate();
								
								if("erpToCrm".equals(transferType)){//插入CRMClientInfoEmp
									//updSql = "insert into CRMClientInfoEmp(id,f_ref,EmployeeID,DepartmentCode,empType) " + "values( ?,?,(SELECT EmployeeID FROM tblCompany WHERE id=?),(SELECT DepartmentCode FROM tblCompany WHERE id=?),0 )";
									updSql = "declare @EmployeeID varchar(50),@DepartmentCode varchar(50) select @EmployeeID = EmployeeID FROM tblCompany WHERE id=? select @DepartmentCode = DepartmentCode FROM tblCompany WHERE id=?  insert into CRMClientInfoEmp(id,f_ref,EmployeeID,DepartmentCode,empType) " 
											 + "values( ?,?,@EmployeeID,@DepartmentCode,0 )";
									
									ps4 = connection.prepareStatement(updSql);
									ps4.setString(1,clientId);
									ps4.setString(2,clientId);
									ps4.setString(4,clientId);
									ps4.setString(3,IDGenerater.getId());
									ps4.executeUpdate();
									/*
									ps4 = connection.prepareStatement(updSql);
									ps4.setString(1,IDGenerater.getId());
									ps4.setString(2,clientId);
									ps4.setString(3,clientId);
									ps4.setString(4,clientId);
									ps4.executeUpdate();
									*/
								}
								
								
								//执行插入前自定义语句
								HashMap values = new HashMap();
								values.put("clientId",clientId);
								values.put("employeeId",loginBean.getId());
								
								Result defineRs = null;
								//执行define
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(transferType+"_add");
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
							
							//更新分类的isCataLog
							if(parentClasscode!=null && !"".equals(parentClasscode)){
								if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
									String cataLogSql = "update tblCompany set isCataLog ='1' where classCode=?";
									PreparedStatement ps = connection.prepareStatement(cataLogSql);
									ps.setString(1,parentClasscode);
									ps.executeUpdate();
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
	 *根据IDS查找多个往来单位客户 
	 * @param ids
	 * @return
	 */
	public Result findCompanys(String ids){
		List param = new ArrayList();
		String sql = "SELECT id,ComFullName FROM tblCompany WHERE id in ("+ids+")";
		return this.sqlList(sql, param);
	}
	
	/**
	 *ERP，CRM互转已存在的客户 
	 * @param ids
	 * @return
	 */
	public Result findExistClientName(String clientIds,String transferType){
		List param = new ArrayList();
		String ids = "";
		for(String id : clientIds.split(",")){
			if(!"".equals(id)){
				ids += "'"+id+"',";
			}
		}
		if(ids.endsWith(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		//默认CRM转ERP
		String seltableName = "CRMClientInfo";//子查询的表名
		String selfieldName = "ClientName";//子查询的select字段名
		String tableName = "tblCompany";//插入表的表名
		String fieldName = "ComFullName";//插入表的字段名
		if("erpToCrm".equals(transferType)){
			seltableName = "tblCompany";
			selfieldName = "ComFullName";
			tableName = "CRMClientInfo";
			fieldName = "ClientName";
		}
		String sql = "select "+fieldName+" from "+tableName+" where "+fieldName+" in (select "+selfieldName+" from "+seltableName+" where id in ("+ids+"))";
		return this.sqlList(sql, param);
	}
	
	/**
	 * 查找CRM.ERP客户转交记录
	 * @param ids
	 * @return
	 */
	public Result findTransferRecord(LoginBean loginBean,int pageSize,int pageNo,HashMap<String,String> valueMap){
		List param = new ArrayList();
		
		String sql = "SELECT crmTransferRecord.id,crmTransferRecord.createBy,crmTransferRecord.createTime,crmTransferRecord.transferType,crmTransferRecord.ipAddress,CRMClientInfo.clientName,tblCompany.comfullname,case when isnull(CRMClientInfo.id,'') !='' then CRMClientInfo.id else tblCompany.id end as keyId,tblCompany.classCode,CRMClientModule.moduleName from crmTransferRecord left join CRMClientInfo on crmTransferRecord.clientId = CRMClientInfo.id left join tblCompany on crmTransferRecord.clientId = tblCompany.id left join CRMClientModule on CRMClientInfo.moduleId = CRMClientModule.id WHERE 1=1 ";
		
		//除了admin,只能看自己转交的
		if(!"1".equals(loginBean.getId())){
			sql += " and crmTransferRecord.createBy = ?";
			param.add(loginBean.getId());
		}
		
		//判断类型1表示CRM转ERP，2表示ERP转CRM 
		if(!"0".equals(valueMap.get("transferType"))){
			sql += " and crmTransferRecord.transferType = ?";
			param.add(valueMap.get("transferType"));
		}
		
		//模糊查询客户
		if(valueMap.get("clientName") !=null && !"".equals(valueMap.get("clientName"))){
			sql += " and clientName like '%"+valueMap.get("clientName")+"%'";
		}
		
		//查询开始时间
		if(valueMap.get("startTime") !=null && !"".equals(valueMap.get("startTime"))){
			sql += " and crmTransferRecord.createTime >= ?";
			param.add(valueMap.get("startTime"));
		}
		
		//结束时间
		if(valueMap.get("endTime") !=null && !"".equals(valueMap.get("endTime"))){
			String time = valueMap.get("endTime") + " 59:59:999";
			sql += " and crmTransferRecord.createTime <= ?";
			param.add(time);
		}
		
		//用户
		if(valueMap.get("userGroupIds") !=null && !"".equals(valueMap.get("userGroupIds"))){
			String ids = "";
			for(String employeeId : valueMap.get("userGroupIds").split(",")){
				if(!"".equals(employeeId)){
					ids += "'" +employeeId + "',";
				}
			}
			if(ids.endsWith(",")){
				ids = ids.substring(0,ids.length()-1);
			}
			if(!"".equals(ids)){
				sql += " and crmTransferRecord.createBy in("+ids+") ";
			}
		}
		
		sql +=" order by crmTransferRecord.createTime desc";
		BaseEnv.log.debug("CRMClientMgt.findTransferRecord sql ="+sql);
		return this.sqlList(sql, param, pageSize, pageNo, true);
	}
	
	 /*
	功能：获取需要分类表的分类代码
	参数：strTableName 表名
	参数：strParentCode 父结点
	*/
	protected String  getLevelCode(String strTableName ,String strParentCode,Connection conn)
	{
		
		final Result rs=new Result();
		String newCode = ""; 
		try {
			CallableStatement cs = conn.prepareCall("{call proc_getNewClassCode(?,?,?,?)}");
			cs.setString(1, strTableName);
			cs.setString(2, strParentCode);
	
			cs.registerOutParameter(3, Types.INTEGER);
	        cs.registerOutParameter(4, Types.VARCHAR, 50);
	      
	        cs.execute();
	        newCode=cs.getString(4);
	        
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace() ;
		}
		return newCode;
	}
	
	
	
	/**
	 * 分组查询出已隐藏的字段	 
	 * @param tableName 表结构表名
	 * @param isMainTable 是否组表 true是 fasle不是
	 * @param fieldDisplayBean 字段显示bean
	 * @param hideFieldScopeList 隐藏字段权限list
	 * @return
	 */
    public Result queryHideFields(final String tableName,final boolean isMainTable,final ClientModuleViewBean moduleViewBean,final List<FieldScopeSetBean> hideFieldScopeList,final HashMap<String,String> workFlowFieldMap,final String contactTableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							
							String sql = "select fieldName,groupName from tblDBFieldInfo left join tblDBEnumerationItem on tblDBFieldInfo.groupName=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumid=(select id from tblDBEnumeration where enumName='"+tableName+"')" +
									" where tableId=(select id from tblDBTableInfo where tableName='"+tableName+"') and fieldName <> 'cId' and fieldName in (";
							
							String displayFields = "";
							for(String str : moduleViewBean.getPageFields().split(",")){
								if(isMainTable == true){
									if(!str.startsWith("contact")){
										displayFields += str +",";
									}
								}else{
									if(str.startsWith("contact")){
										displayFields += str.substring(7) +",";
									}
								}
							}
							
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
								for(FieldScopeSetBean bean : hideFieldScopeList){
									String hideFieldsName = bean.getFieldsName();
									for(String str : hideFieldsName.split(",")){
										if(str!=null && !"".equals(str)){
											if(isMainTable == true){
												if(!str.startsWith("contact")){
													hideFieldsStr +="'"+str+"',";
												}
											}else{
												if(str.startsWith("contact")){
													str = str.substring(7);
													hideFieldsStr +="'"+str+"',";
												}
											}
										}
									}
								}
							}
								
							if(workFlowFieldMap!=null){
								String workFlowHideFields = workFlowFieldMap.get("hide");
								if(workFlowHideFields!=null && !"".equals(workFlowHideFields)){
									for(String str : workFlowHideFields.split(",")){
										if(isMainTable == true){
											if(!str.startsWith(contactTableName)){
												hideFieldsStr +="'"+str+"',";
											}
										}else{
											if(str.startsWith(contactTableName)){
												str = str.replace(contactTableName,"");
												hideFieldsStr +="'"+str+"',";
											}
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
							
							sql += "order by tblDBEnumerationItem.enumOrder,CHARINDEX(','+CONVERT(varchar(100),fieldName)+',',',"+displayFields+"')";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							
							LinkedHashMap<String,List<String>> rsMap = new LinkedHashMap<String, List<String>>();
							if(!isMainTable){
								ArrayList<String> list = new ArrayList<String>();
								rsMap.put("contact", list);
							}
							while(rss.next()){
								String fieldName = rss.getString("fieldName");//字段名
								String groupName = rss.getString("groupName");//分组值
								//处理分组信息
								if(isMainTable){
									if(rsMap.get(groupName) == null){
										ArrayList<String> list = new ArrayList<String>();
										list.add(fieldName);
										rsMap.put(groupName, list);
									}else{
										rsMap.get(groupName).add(fieldName);
									}
								}else{
									rsMap.get("contact").add(fieldName);
								}
							}
							rst.setRetVal(rsMap);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt queryHideFields mehtod:", ex) ;
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
     * 根据模板查找其下所有客户Id
     * @return
     */
    public Result queryClientIdsByModule(String moduleId){
    	ArrayList list = new ArrayList();
    	String sql = "SELECT id FROM CRMClientInfo where status != '1' and moduleId=?";
    	list.add(moduleId);
    	return sqlList(sql,list);
    }
    
    /**
     * 根据联系人邮箱找出相关邮件
     * @param contactMails
     * @return
     */
    public Result queryMailInfoByMails(String contactMails){
    	String indexTo = "";
    	String indexFrom = "";
    	String indexCC = "";
    	for(String email : contactMails.split(",")){
    		indexTo += " charindex('"+email+"', OAmailinfo.mailTo) > 0 or ";
    		indexFrom += " charindex('"+email+"', OAmailinfo.mailFrom) > 0 or ";
    		indexCC += " charindex('"+email+"', OAmailinfo.mailCC) > 0 or ";
    	}
    	
    	if(indexTo.endsWith("or ")){
    		indexTo = indexTo.substring(0,indexTo.length()-3);
    	}
    	
    	if(indexFrom.endsWith("or ")){
    		indexFrom = indexFrom.substring(0,indexFrom.length()-3);
    	}
    	
    	if(indexCC.endsWith("or ")){
    		indexCC = indexCC.substring(0,indexCC.length()-3);
    	}
    	
    	String sql = "SELECT id,mailTo,mailFrom,mailCC,mailTitle,emailType,mailTime,fromUserId,toUserId,CASE WHEN "+indexCC+" THEN 'BCC' WHEN ("+indexTo+") and groupId = '1' THEN 'To' WHEN ("+indexFrom+") and groupId = '3' THEN 'From' ELSE '' END AS sendMailType " +
    			" FROM OAMailInfo WHERE EmailType='1' AND ("+indexTo+" or "+indexFrom +" or "+indexCC+")";
    	
    	return this.sqlList(sql, new ArrayList(),50,1, true);
    }
    
    
    /**
     * 根据邮件查找联系人并封装
     * @param contactMails
     * @return
     */
    public Result queryContactByEmail(final String contactMails) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "SELECT ClientEMail,UserName FROM CRMClientInfoDet WHERE ClientEMail in ("+contactMails+") ";
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							HashMap<String,String> rsMap = new HashMap<String, String>();
							while(rss.next()){
								rsMap.put(rss.getString("ClientEMail"), rss.getString("UserName"));
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
     * 添加联系人
     * @param clientId
     * @param pageFields
     * @param valueList
     * @return
     */
    public Result addContact(final String clientId,final String pageFields,final ArrayList<HashMap<String,String>> valueList,final String contactId,final ClientModuleBean moduleBean,final HashMap clientValMap) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//修改联系人的时候有联系人ID，先删除
							if(contactId!=null && !"".equals(contactId)){
								String delSql = "DELETE FROM CRMClientInfoDet WHERE id=?";
								PreparedStatement pstmt = connection.prepareStatement(delSql);
								pstmt.setString(1,contactId);
								pstmt.executeUpdate();
							}
							
							String sql = "INSERT INTO CRMClientInfoDet(id,f_ref,SCompanyID,mainUser,";//插入语句
							String filterFields = "";//获取插入联系人字段
							String indexChar = "";//通配符
							for(String str : pageFields.split(",")){
								if(str.indexOf("contact")>-1){
									filterFields += str.replace("contact","")+",";
									indexChar +="?,";
								}
							}
							if(filterFields.endsWith(",")){
								filterFields = filterFields.substring(0,filterFields.length()-1);
							}
							if(indexChar.endsWith(",")){
								indexChar = indexChar.substring(0,indexChar.length()-1);
							}
							sql+=filterFields+") VALUES(?,?,?,?,"+indexChar+")";
							
							PreparedStatement ps = connection.prepareStatement(sql);
							ArrayList detIds=new ArrayList();
							for(HashMap map : valueList){
								String mainUser = map.get("mainUser")+"";
								String id=IDGenerater.getId();
								detIds.add(id);
								ps.setString(1,id);
								ps.setString(2, clientId);
								ps.setString(3, "000001");
								ps.setString(4, mainUser);
								int i =5;
								for(String str : filterFields.split(",")){
									String value = map.get(str)+"";
									ps.setString(i, value);
									i++;
								}
								ps.addBatch();
							}
							ps.executeBatch();
							
							
							String ClientSql = "UPDATE CRMClientInfo SET lastContractTime='" +BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"' WHERE id = '"+clientId+"'";
							PreparedStatement ps1 = connection.prepareStatement(ClientSql);
							ps1.executeUpdate();
							
							//ERP客户同步CRM客户
							updateClientSynchro(moduleBean, clientValMap, connection);
							
							for(int i=0;i<detIds.size();i++){
								DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("CRMClientInfoDet_add");
								if (defineSqlBean != null) {
									HashMap map = new HashMap();
									map.put("id", detIds.get(i));
									Result rs3 = defineSqlBean.execute(connection, map,"", null, null, "");
									if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
										BaseEnv.log.debug(rs3.retVal);
										rst.setRetVal(rs3.retVal);
										rst.setRetCode(rs3.retCode);
										return;
									}
								}
							}
							
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
	 * 公共sql查询
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result publicSqlQuery(String sql,ArrayList param){
		return sqlList(sql,param);
	}
	
	/**
	 * 同步ERP客户信息
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
	public Result updateClientSynchro(ClientModuleBean moduleBean,HashMap values,Connection conn) {
		Result rs = new Result();
		try {
			
			//首先判断CRM客户是否已转入ERP
			String sql = "SELECT count(*) as rows FROM tblCompany WHERE id='"+values.get("id")+"'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rss = ps.executeQuery();
			rss.next();
            int rowCount = rss.getInt("rows");
            
			if(rowCount>0){
				//ERP客户同步CRM客户
				String transferFields = moduleBean.getTransferFields();//获取隐射字段信息
				String childTableName = moduleBean.getTableInfo().split(":")[1];//获取从表名称
				if(transferFields!=null && !"".equals(transferFields)){
					//处理主表
					Statement state = conn.createStatement();
					String keyId = String.valueOf(values.get("id"));//主表ID
					String updateSql = "UPDATE tblCompany SET ";
					for(String str:transferFields.split(",")){
						if(str.indexOf("Contact_")==-1){
							String value = String.valueOf(values.get(str.split(":")[1]));
							if(value!=null && !"".equals(value) && !"null".equals(value)){
								updateSql+=str.split(":")[0]+"='"+value+"',";
							}
						}
					}
					if(updateSql.endsWith(",")){
						updateSql = updateSql.substring(0,updateSql.length()-1);
					}
					
					updateSql +=" WHERE id='"+keyId+"'";
					state.addBatch(updateSql);
					
					//删除明细信息
					String delChildSql = "DELETE FROM tblCompanyEmployeeDet WHERE f_ref='"+keyId+"'";
					state.addBatch(delChildSql);
					
					//新增明细信息
					List<HashMap<String,String>> childList = (List<HashMap<String,String>>)values.get("TABLENAME_"+childTableName);
					if(childList!=null && childList.size()>0){
						for(HashMap<String,String> map : childList){
							String childSql = "INSERT INTO tblCompanyEmployeeDet(id,f_ref,SCompanyID,";
							String fieldValues = "Values('"+IDGenerater.getId()+"','"+keyId+"','00001',";
							for(String str:transferFields.split(",")){
								if(str.indexOf("Contact_")>-1){
									str = str.replaceAll("Contact_","");
									String value = String.valueOf(map.get(str.split(":")[1]));
									if(value!=null && !"".equals(value) && !"null".equals(value)){
										childSql += str.split(":")[0]+",";
										fieldValues += "'"+value+"',";
									}
								}
							}
							if(childSql.endsWith(",")){
								childSql = childSql.substring(0,childSql.length()-1);
							}
							
							if(fieldValues.endsWith(",")){
								fieldValues = fieldValues.substring(0,fieldValues.length()-1);
							}
							childSql +=") "+fieldValues+")";
							state.addBatch(childSql);
						}
					}
					
					state.executeBatch();
					
				}
			}
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("CRMClientMgt---insertCRMCLientInfoLog method :" + ex);
		}
		return rs;
	}
	
	/**
	 * 根据ID获取名称
	 * @param clientId
	 * @return
	 */
	public String getClientNameById(String clientId){
		String clientName="";
		if(clientId!=null && !"".equals(clientId)){
			Result rs = findClientById(clientId);
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				clientName = String.valueOf(GlobalsTool.get(list.get(0),1));
			}
		}
		return clientName;
	} 
	
	/**
	 * 根据联系人ID查找客户ID
	 * @param contactId
	 * @return
	 */
	public Result queryClientIdByContactId(String contactId){
		String sql = "SELECT f_ref FROM CRMclientInfoDet WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(contactId);
		return sqlList(sql, param);
	}
	
	/**
	 * 根据联系人ID查找联系人信息
	 * @param contactId
	 * @return
	 */
	public Result queryContactById(String contactId){
		String sql = "select *,row_number() over(order by id desc) row_id from crmclientInfoDet WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(contactId);
		return sqlListMaps(sql, param, 1,5);
	}
	
	/**
	 * 详情页面查询客户常用信息
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result queryCommonInfoByClientId (final String clientId,final LoginBean loginBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							HashMap<String,ArrayList<String[]>> rsMap = new LinkedHashMap<String, ArrayList<String[]>>();
							
							//查询日程
							String sql = "SELECT TOP 3 id,title,stratTime,finishTime FROM OACalendar WHERE finishStatus<>'1' and clientId=? and userId=? ORDER BY stratTime DESC";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, clientId);
							ps.setString(2, loginBean.getId());
							ResultSet rss = ps.executeQuery();
							ArrayList<String[]> list = new ArrayList<String[]>();
							while(rss.next()){
								String[] str=new String[4];
								str[0]=rss.getString("id");
								str[1]=rss.getString("title");
								str[2]=rss.getString("stratTime");
								str[3]=rss.getString("finishTime");
								list.add(str) ;
							}
							rsMap.put("OACalendar", list);
							
							//查询任务分派
							
							sql = "SELECT TOP 3 id,title,endTime,surveyor FROM OATask WHERE clientId =? and executor=? and status='1' ORDER BY createTime";
							
							//sql = "SELECT TOP 3 id,content,finishTime FROM CRMTaskAssign WHERE taskStatus='-1' and Ref_id=? and userId=? ORDER BY lastUpdateTime DESC";
							PreparedStatement ps1 = connection.prepareStatement(sql);
							ps1.setString(1, clientId);
							ps1.setString(2, loginBean.getId());
							ResultSet rss1 = ps1.executeQuery();
							ArrayList<String[]> list1 = new ArrayList<String[]>();
							while(rss1.next()){
								String[] str=new String[4];
								str[0]=rss1.getString("id");	
								str[1]=rss1.getString("title");
								str[2]=rss1.getString("endTime");
								str[3]=rss1.getString("surveyor");
								list1.add(str) ;
							}
							rsMap.put("OATask", list1);
							
							//查询联系记录
							sql = "SELECT TOP 3 id,content,VisitTime FROM CRMSaleFollowUp WHERE ClientId=? and EmployeeID=? ORDER BY lastUpdateTime DESC";
							PreparedStatement ps2 = connection.prepareStatement(sql);
							ps2.setString(1, clientId);
							ps2.setString(2, loginBean.getId());
							ResultSet rss2 = ps2.executeQuery();
							ArrayList<String[]> list2 = new ArrayList<String[]>();
							while(rss2.next()){
								String[] str=new String[3];
								str[0]=rss2.getString("id");
								str[1]=rss2.getString("content");
								str[2]=rss2.getString("VisitTime");
								list2.add(str) ;
							}
							rsMap.put("CRMSaleFollowUp", list2);
							
							//查询日志记录 2014-12-17
							sql = "SELECT TOP 3 a.id id,a.workLogDate workLogDate,b.contents contents FROM OAWorkLog a left join OAWorkLogDet b on a.id=b.workLogId WHERE a.createBy=? and b.relationId=? ORDER BY a.workLogDate DESC";
							PreparedStatement ps3 = connection.prepareStatement(sql);
							String idString=loginBean.getId();
							ps3.setString(1, idString);
							ps3.setString(2, clientId);
							ResultSet rss21 = ps3.executeQuery();
							ArrayList<String[]> list21 = new ArrayList<String[]>();
							while(rss21.next()){
								String[] str=new String[3];
								str[0]=rss21.getString("id");
								str[1]=rss21.getString("workLogDate");
								str[2]=rss21.getString("contents");
								list21.add(str) ;
							}
							rsMap.put("OAWorkLogUp", list21);
							
							rst.setRetVal(rsMap);
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
	 * 详情页面查询客户常用信息
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result addCommonInfo(String tableName,String id,HashMap<String,String> conMap,LoginBean loginBean) {
		ArrayList param = new ArrayList();
		String userId = loginBean.getId();
		String clientId = conMap.get("clientId");
		String content = conMap.get("content");
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		//String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		//查询日程
		String sql = "INSERT INTO "+tableName+"(id";
		
		if("OACalendar".equals(tableName)){
			sql+=",type,userId,delStatus,title,stratTime,finishTime,clientId,finishStatus) VALUES(?,?,?,?,?,?,?,?,?)";
			param.add(id);
			param.add("客户日程");
			param.add(userId);
			param.add("0");
			param.add(content);
			param.add(conMap.get("startTime"));
			param.add(conMap.get("startTime"));
			param.add(clientId);
			param.add("0");
			return this.operationSql(sql, param);
		}else if("OATask".equals(tableName)){
			Result rs = new Result();
			if(new OATaskAction().addTask(loginBean.getId(), id, content, "", conMap.get("taskUserId"), BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd), conMap.get("finishTime"), "", "", "", "0", clientId, "","","A","1")){
				rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
			}else{
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			}
			return rs;
			
			/*
			sql+=",Ref_id,content,userId,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,taskStatus,finishTime,priority) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			param.add(id);
			param.add(clientId);
			param.add(content);
			param.add(conMap.get("taskUserId"));
			param.add(userId);
			param.add(userId);
			param.add(nowTime);
			param.add(nowTime);
			param.add("0");
			param.add("-1");
			param.add(conMap.get("finishTime"));
			param.add("middle");
			*/
		}
		//dfq 12-16
		else if("OAWorkLog".equals(tableName)){
			String worklogid;
			sql="select id from OAWorkLog where workLogDate=?";
			String time=conMap.get("time");
			param.add(time);
			AIODBManager manager=new AIODBManager();
			Result rs = manager.sqlList(sql, param);
			if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				List<Object[]> list=(List<Object[]>)rs.getRetVal();
				if(list.size()>0){
					worklogid=(String) list.get(0)[0];
					param = new ArrayList();
					sql="insert into OAWorkLogDet(id,contents,contentType,relationId,workLogId,createTime,relationType) values(?,?,?,?,?,?,?)";
					param.add(id);
					param.add(content);
					param.add("2");
					param.add(clientId);
					param.add(worklogid);
					param.add(nowTime);
					param.add("OAClient");
					return this.operationSql(sql, param);
				}else{
					worklogid=IDGenerater.getId();
					param = new ArrayList();
					sql="insert into OAWorkLog(id,type,workLogDate,clientId,createBy,createTime) values(?,?,?,?,?,?)";
					param.add(worklogid);
					param.add("day");
					param.add(time);
					param.add(clientId);
					param.add(userId);
					param.add(nowTime);
					rs =this.operationSql(sql, param);
					if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
						param = new ArrayList();
						sql="insert into OAWorkLogDet(id,contents,contentType,relationId,workLogId,createTime,relationType) values(?,?,?,?,?,?,?)";
						param.add(id);
						param.add(content);
						param.add("2");
						param.add(clientId);
						param.add(worklogid);
						param.add(nowTime);
						param.add("OAClient");
						return this.operationSql(sql, param);
					}
			}
			}
			return rs;
		}
		//dfq 12-16
		else{
			String FollowNo = BillNoManager.find("CRMSaleFollowUp_FollowNo",loginBean);
			sql+=",f_brother,workFlowNode,workFlowNodeName,FollowNo,EmployeeId,VisitTime,Content,createBy,lastUpdateBy,createTime,lastUpdateTime,ClientId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			param.add(id);
			param.add(clientId);
			param.add("-1");
			param.add("finish");
			param.add(FollowNo);
			param.add(userId);
			param.add(nowTime.substring(0,10));
			param.add(content);
			param.add(userId);
			param.add(userId);
			param.add(nowTime);
			param.add(nowTime);
			param.add(clientId);
			return this.operationSql(sql, param);	
		}
		
		
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
	 * 过滤自己建的客户
	 */
	public ArrayList<String[]> filterClientIdsByEmployeeId(LoginBean loginBean,String[] keyIds){
		String ids = "";
		for(String keyId : keyIds){
			ids += "'"+keyId+"',";
		}
		if(ids.endsWith(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		ArrayList<String[]> rsList = new ArrayList<String[]>();
		String sql = "SELECT id,clientName FROM CRMClientInfo WHERE id in("+ids+")";
		ArrayList param = new ArrayList();
		/*
		if(!"1".equals(loginBean.getId())){
			sql += " and createBy=?";
			param.add(loginBean.getId());
		}*/
		
		Result rs = sqlList(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				for(Object obj : list){
					String[] str = new String[2];
					str[0]=String.valueOf(GlobalsTool.get(obj,0));
					str[1]=String.valueOf(GlobalsTool.get(obj,1));
					rsList.add(str);
				}
			}
		}
		return rsList;
	}
	
	/**
	 * 检查CRM客户是否存在ERP
	 * @param clientId
	 * @return
	 */
	public String isExistERPClient(String clientId){
		String companyCode = "";
		String sql = "SELECT classCode FROM tblCompany WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(clientId);
		Result rs = sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			if(list!=null && list.size()>0){
				companyCode = String.valueOf(GlobalsTool.get(list.get(0),0));//表示存在
			}
		}
		return companyCode;
	}
	
	/**
	 * 投递状态
	 * @param keyIds
	 * @return
	 */
    public Result deliverGoods(final String clientId,final String mobileCode,final String deliverRemark,final LoginBean loginBean,final MessageResources resources
    		,final Locale locale) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "UPDATE CRMClientInfo SET sjcm=?,tdbz=?,fhzt='2' WHERE id=?";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, mobileCode);
							pstmt.setString(2, deliverRemark);
							pstmt.setString(3, clientId);
							pstmt.executeUpdate();
							
							
							//执行插入前自定义语句
							HashMap values = new HashMap();
							values.put("ClientId",clientId);
							values.put("employeeId",loginBean.getId());
							
		            		insertCRMCLientInfoLog(values.get("ClientId").toString(),"history","修改了客户资料",loginBean.getId(),conn);
							
							Result defineRs = null;
							//执行define
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("deliverGoods_add");
							defineRs = defineSqlBean.execute(conn, values, loginBean.getId(),resources,locale,null);
				            if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
				            	rst.setRetCode(defineRs.getRetCode());
				            	rst.setRetVal(defineRs.getRetVal());
				                BaseEnv.log.error("DynDBManager Before add defineSql Error code = " 
				                		+defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
				            }else{
				            	rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
				            }
							
							
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt deliverGoods mehtod:", ex) ;
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
}
