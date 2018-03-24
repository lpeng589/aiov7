/**
 * 科荣软件 手机平台
 */
package com.koron.mobile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.bsf.BSFException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koron.mobile.bean.AddClientBean;
import com.koron.mobile.bean.MobileAttrs;
import com.koron.mobile.bean.MobileCount;
import com.koron.mobile.bean.MobileCountList;
import com.koron.mobile.bean.MobileEmail;
import com.koron.mobile.bean.MobileEmailDate;
import com.koron.mobile.bean.MobileEmailList;
import com.koron.mobile.bean.MobileEmails;
import com.koron.mobile.bean.MobileFlowAction;
import com.koron.mobile.bean.MobileFlowNode;
import com.koron.mobile.bean.MobileFolder;
import com.koron.mobile.bean.MobileMessage;
import com.koron.mobile.bean.MobileMessageBean;
import com.koron.mobile.bean.MobileOnlineUser;
import com.koron.mobile.bean.MobileWorkFlow;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.OAWorkLogBean;
import com.koron.oa.bean.OAWorkLogDetBean;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.calendar.DateBean;
import com.koron.oa.util.DateUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ChineseSpelling;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
import com.taobao.api.domain.User;
/**
 * <p>Title:手机服务端 后台数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 3, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class MOfficeMgt  extends AIODBManager{

	/**
	 * 直接将结果集封装成JSON--PWY
	 * @param sql
	 * @return
	 */
	public Result sqlListJson(final String sql) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						PreparedStatement pstmt;
						ResultSet rs;
						try {
							pstmt = conn.prepareStatement(sql);
							BaseEnv.log.debug(sql);
							rs = pstmt.executeQuery();
							JsonArray jarr = new JsonArray();
							while (rs.next()) {
								JsonObject json = new JsonObject();
								for (int i = 1; i <= rs.getMetaData()
										.getColumnCount(); i++) {
									Object obj = rs.getObject(i);
									if (obj == null) {
										json.addProperty(rs.getMetaData()
												.getColumnName(i), "");
									} else if ("0E-8".equals(obj.toString())) {
										json.addProperty(rs.getMetaData()
												.getColumnName(i), "0");
									} else {
										json.addProperty(rs.getMetaData()
												.getColumnName(i), obj
												.toString());
									}
								}
								jarr.add(json);
							}
							rst.setRetVal(jarr);
							rst.setRealTotal(jarr.size());
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.debug(sql);
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
	 * 按orderBy排序，跳过前面count条，获取之后的size条记录
	 * @param sql
	 * @param count 
	 * @param size
	 * @param orderBy
	 * @return
	 */
	public Result sqlListJson(String sql, int count, int size, String orderBy) {

    	String condition = "select * from ( select row_number() over( order by " + orderBy + " ) as row_id, " + sql.substring(sql.indexOf("select") + 6) + ") as tbl1";
    	condition += " where row_id between " + (count + 1)+" and " + (count + size) + " order by row_id ";    
		return sqlListJson(condition);
	}
		
	/**
	 * 根据指定顺序排序的结果集，返回指定ID后的数据--PWY
	 * 调用这方法时sql的select只能是字段名或as形式的别名,所有语句关键字采用小写
	 * 不同表下面的字段名不能重复，比如a.id,b.id，需将其中一个改成别名，如：a.id,b.id as bid
	 * @param sql
	 * @param keyId
	 * @param lastId
	 * @param nSize
	 * @param orderBy
	 * @return
	 */
	public Result sqlListJson(final String sql, final String keyId, final String lastId, final int nSize, final String orderBy) {
		String fields = sql.substring(sql.indexOf("select") + 6, sql.indexOf("from"));
		
		if (nSize <= 0) {
			return sqlListJson(sql);			
		}
		
		// 补上主键
		if (!fields.contains(keyId)) {
			fields += ", " + keyId + " ";
		}
		String fields2 = fields;
		
		// 补上排序
		String[] orderBys = orderBy.split(",");
		for (String str : orderBys) {
			if (str.contains(" desc")) {
				str = str.replace(" ", "");
				str = str.substring(0, str.lastIndexOf("desc"));
			}			
			if (!fields.contains(str)) {
				fields2 += ", " + str + " ";
			}			
		}
		
 		String sql2 = "select " + fields2 + " from " + sql.substring(sql.indexOf("from") + 4);
		
		// 别名替换
		String[] list = fields.split(",");
		fields = "";
 		String orderBy2 = orderBy;
		for (String str : list) {
			if (str.contains(" as ")) {
				orderBy2 = orderBy2.replace(str.split(" as ")[0].trim() + " ", str.split(" as ")[1].trim() + " ");
				str = str.split(" as ")[1];
			}
			fields += str + ", ";
		}
		fields = fields.substring(0, fields.lastIndexOf(","));	
		
		 list = fields2.split(",");
			fields2 = "";
			for (String str : list) {
				if (str.contains(" as ")) {
					str = str.split(" as ")[1];
				}
				fields2 += str + ", ";
			}
			fields2 = fields2.substring(0, fields2.lastIndexOf(","));	
 		
 		// 剔除表名		
 		list = fields.split(",");
		fields = "";
		for (String str : list) {
			if (str.contains(".")) {
				str = str.substring(str.lastIndexOf(".") + 1);
			}
			fields += str + ", ";
		}
		fields = fields.substring(0, fields.lastIndexOf(","));		
		
 		list = fields2.split(",");
		fields2 = "";
		for (String str : list) {
			if (str.contains(".")) {
				str = str.substring(str.lastIndexOf(".") + 1);
			}
			fields2 += str + ", ";
		}
		fields2 = fields2.substring(0, fields2.lastIndexOf(","));	
		
		// 排序的也要剔除表名
 		list = orderBy2.split(",");
		orderBy2 = "";
		for (String str : list) {
			if (str.contains(".")) {
				str = str.substring(str.lastIndexOf(".") + 1);
			}
			orderBy2 += str + ", ";
		}
		orderBy2 = orderBy2.substring(0, orderBy2.lastIndexOf(","));
		
		StringBuilder condition = new StringBuilder();
		condition.append(" select top ").append(nSize).append(" ").append(fields).append(" from ( ");	
		condition.append(" select ROW_NUMBER() over(order by ").append(orderBy2).append(" ) as row_id,").append(fields2).append(" from ( ").append(sql2).append(" ) as tbl1 ");		
		condition.append(" ) as tbl2 ");
		if (null != lastId && lastId.length() > 0) {
			condition.append(" where row_id > ");
			condition.append(" (select row_id from ( ");
			condition.append(" select ROW_NUMBER() over(order by ").append(orderBy2).append(" ) as row_id,").append(fields2).append(" from ( ").append(sql2).append(" ) as tbl3 ");
			condition.append(" ) as tbl4 where ").append(keyId).append(" = '").append(lastId).append("')");
		}
		condition.append(" order by ").append(orderBy2);
			
		return sqlListJson(condition.toString());
	}
	
	/**
	 * 根据系统ID查询用户信息
	 * 
	 * @param userName
	 * @return
	 */
    public Result queryEmployeeById(String id){
    	// 创建参数
    	List<String> param = new ArrayList<String>() ;
    	String hql = "from EmployeeBean as bean where bean.id=? and openFlag=1 and  statusId!=-1" ;
    	param.add(id) ;
    	Result result = list(hql, param);
    	if(result.realTotal>0){
    		List<EmployeeBean> listEmp = (List<EmployeeBean>) result.retVal ;
    		result.setRetVal(listEmp.get(0)) ;
    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
    	}else{
    		result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
    	}
    	return result ;
    }
	/**
	 * 根据系统用户名查询用户信息
	 * 
	 * @param userName
	 * @return
	 */
    public Result queryEmployeeByEmpName(String userName){
    	// 创建参数
    	List<String> param = new ArrayList<String>() ;
    	String hql = "from EmployeeBean as bean where bean.sysName=? and openFlag=1 and  statusId!=-1" ;
    	param.add(userName) ;
    	Result result = list(hql, param);
    	if(result.realTotal>0){
    		List<EmployeeBean> listEmp = (List<EmployeeBean>) result.retVal ;
    		result.setRetVal(listEmp.get(0)) ;
    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
    	}else{
    		result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
    	}
    	return result ;
    }
    
    /**
     * 查询私信数,系统消息数,邮件数,工作审批数
     * @param userId
     * @return
     */
    public Result queryMessageByUserId(final String userId){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							MobileCount count = new MobileCount() ;
							count.setUserid(userId) ;
							/*我的私信*/
							String sql = "select count(id) asCount from OAMessage where receive=? and status='noRead'" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								count.setMsgCount(rss.getInt("asCount")) ;
							}
							/*系统消息*/
							sql = "select count(id) asCount from tblAdvice where receive=? and status='noRead'" ;
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							rss = pss.executeQuery() ;
							if(rss.next()){
								count.setTipsCount(rss.getInt("asCount")) ;
							}
							/*我的邮件*/
							sql = "select count(*) as count from OAMailInfo a "
									+ "where a.userId = ? and a.state=0 and a.groupId='1' and "
									+ "((EmailType = 0) or (EmailType = 1 and a.account in (select b.id from MailInfoSetting b where b.createBy = ? and b.statusId = 1))) ";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							pss.setString(2, userId) ;
							rss = pss.executeQuery() ;
							if(rss.next()){
								count.setMailCount(rss.getInt("count")) ;
							}
							/*我的审批*/
							sql = "select count(id) asCount from OAMyWorkFlow where statusId=0 and checkPerson like ? and tableName in (select templateFile from OAWorkFlowTemplate where templateStatus=1 and statusId=0)" ;
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, "%;"+userId+";%") ;
							rss = pss.executeQuery() ;
							if(rss.next()){
								count.setWfCount(rss.getInt("asCount")) ;
							}
							result.setRetVal(count) ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryMessageByUserId mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    /**
   	 * 查询模块信息
   	 * @param userName
   	 * @return
   	 */
    public Result queryModuleInfo(final String moduleId){
       	final Result result = new Result();
   		int retCode = DBUtil.execute(new IfDB() {
   			public int exec(Session session) {
   				session.doWork(new Work() {
   					public void execute(Connection conn) throws SQLException {
   						try {
   							String sql = "select tblName from tblModules where id=?" ;
   							PreparedStatement pss = conn.prepareStatement(sql) ;
   							pss.setString(1, moduleId) ;
   							ResultSet rss = pss.executeQuery() ;
   							if(rss.next()){
   								result.setRetVal(rss.getString("tblName")) ;
   							}
   						}catch (Exception ex) {
   							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
   							ex.printStackTrace();
   							BaseEnv.log.error("MOfficeMgt queryModuleInfo mehtod",ex);
   						}
   					}
   				});
   				return result.getRetCode();
   			}
   		});
   		result.setRetCode(retCode);
   		return result;
    }   
    
    /**
   	 * 添加iPhone的token
   	 * @param userName
   	 * @return
   	 */
    public Result addIPhoneToken(final String userId,final String token, final String serverKey){
       	final Result result = new Result();
   		int retCode = DBUtil.execute(new IfDB() {
   			public int exec(Session session) {
   				session.doWork(new Work() {
   					public void execute(Connection conn) throws SQLException {
   						try {
   							String sql = "delete from tblAppleToken where token=?" ;
   							PreparedStatement pss = conn.prepareStatement(sql) ;
   							pss.setString(1, token) ;
   							pss.executeUpdate() ;
   						
   							sql = "delete from tblAppleToken where userId=? and sendType=?" ;
   							pss = conn.prepareStatement(sql) ;
   							pss.setString(1, userId) ;
   							pss.setString(2, token.length()==64?"iphone":"android");
   							pss.executeUpdate() ;
   							
   							sql = "insert into tblAppleToken(id,userId,token,sendType,serverKey) values(?,?,?,?,?)" ;
   							pss = conn.prepareStatement(sql) ;
   							pss.setString(1, IDGenerater.getId()) ;
   							pss.setString(2, userId) ;
   							pss.setString(3, token) ;
   							pss.setString(4, token.length()==64?"iphone":"android");
   							pss.setString(5, serverKey);
   							pss.executeUpdate() ;
   						}catch (Exception ex) {
   							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
   							ex.printStackTrace();
   							BaseEnv.log.error("MOfficeMgt addIPhoneToken mehtod",ex);
   						}
   					}
   				});
   				return result.getRetCode();
   			}
   		});
   		result.setRetCode(retCode);
   		return result;
    }   
    
    /**
   	 * 查询用户iPhone的token
   	 * @param userName
   	 * @return
   	 */
    public Result queryTokenByUserIds(final String userIds){
       	final Result result = new Result();
   		int retCode = DBUtil.execute(new IfDB() {
   			public int exec(Session session) {
   				session.doWork(new Work() {
   					public void execute(Connection conn) throws SQLException {
   						Result rs = queryTokenByUserIds(userIds, conn);
   						result.setRetVal(rs.getRetVal());
   						result.setRetCode(rs.retCode);
   					}
   				});
   				return result.getRetCode();
   			}
   		});
   		result.setRetCode(retCode);
   		return result;
    }   
    
    public Result queryTokenByUserIds(String userIds,Connection conn){
    	Result result = new Result();
    	try {
			String strUserIds = "";
			String[] popedomUser = userIds.split(",") ;
			for (int i = 0; i < popedomUser.length; i++) {
				strUserIds += "'"+popedomUser[i]+"'," ;
			}
			strUserIds = strUserIds.substring(0,strUserIds.length()-1) ;
			String sql = "select userId,token,sendType from tblAppleToken where userId in ("+strUserIds+")" ;
			PreparedStatement pss = conn.prepareStatement(sql) ;
			ResultSet rss = pss.executeQuery() ;
			HashMap<String, ArrayList<String>> tokenMap = new HashMap<String, ArrayList<String>>() ;
			while(rss.next()){
				String userId = rss.getString("userId");
				if(tokenMap.get(userId)==null){
					ArrayList<String> userList = new ArrayList<String>();
					userList.add(rss.getString("token"));
					tokenMap.put(rss.getString("userId"), userList) ;
				}else{
					ArrayList<String> userList = tokenMap.get(userId);
					userList.add(rss.getString("token"));
					tokenMap.put(rss.getString("userId"), userList) ;
				}
			}
			result.setRetVal(tokenMap) ;
		}catch (Exception ex) {
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("MOfficeMgt addIPhoneToken mehtod",ex);
		}
		return result;
    }

    /**
     * 查询所有需要我审批的工作流
     * @param userId
     * @return
     */
    public Result queryMyWorkFlow(final String userId,final String lastwid){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select top 20 a.keyId,applyDate,applyBy,currentNode,nextNodeIds,applyType,a.createTime,applyContent,lastCheckPerson";
							sql += " from OAMyWorkFlow a join OAWorkFlowTemplate t on t.id=a.applyType " +
									" join OAMyWorkFlowPerson p on p.keyId=a.keyId and p.tableName=a.tableName  ";
							sql += " where t.templateStatus=1 and a.statusId=0 ";	
							sql += " and a.currentNode!='-1' and ((a.createBy='"+userId+"' and a.currentNode='0') or (p.curApprover=1 and p.checkPerson='"+userId+"' )) ";
							if(lastwid!=null && lastwid.trim().length()>0){
								sql += "and a.createTime<(select createTime from OAMyWorkFlow where id='"+lastwid+"')" ;
							}
							sql += " order by a.createTime desc" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss = pss.executeQuery() ;
							List<MobileWorkFlow> listFlow = new ArrayList<MobileWorkFlow>() ;
							while(rss.next()){
								MobileWorkFlow flow = new MobileWorkFlow() ;
								flow.setId(rss.getString("keyId")) ;
								flow.setTitle(rss.getString("applyContent")) ;   
								if (BaseEnv.workFlowDesignBeans.get(rss.getString("applyType")) == null)
									continue;
								FlowNodeBean nodeBean = BaseEnv.workFlowDesignBeans.get(rss.getString("applyType")).getFlowNodeMap().get(rss.getString("currentNode")) ;
								flow.setCurrentNode(nodeBean.getDisplay());
								//String nextNodeId = rss.getString("nextNodeIds") ;
								//String detId = rss.getString("detID") ;
								flow.setFlag(0) ;
								flow.setCreateTime(rss.getString("createTime")) ;
								flow.setCreator(rss.getString("applyBy")) ;
								OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(rss.getString("applyType"));
								if(workFlow != null && workFlow.getTemplateName() != null){									
									flow.setType(workFlow.getTemplateName()) ;
								} else {
									flow.setType("");
								}
								String lastCheckPerson = rss.getString("lastCheckPerson") ;
								if(userId.equals(lastCheckPerson) && nodeBean.isAllowCancel()){
									flow.setCancelEnabled(true) ;
								}else{
									flow.setCancelEnabled(false) ;
								}
								listFlow.add(flow) ;
							}
							result.setRetVal(listFlow) ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryMyWorkFlow mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    /**
     * 查询我的个人应用或商务智能
     * @param userId
     * @return
     */
    public Result queryBusinessModule(final String userId,final String mobileType){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select module.id,module.tblName,module.ICON,module.linkAddress,(select zh_CN from tblLanguage where id=modelName) moduleName " 
									+ " from tblModules module join tblModelOperations oper on oper.f_ref=module.id  where linkAddress like ? " ;
							/*admin拥有全部的商业智能和个人应用*/
							if(!"1".equals(userId)){
								sql += " and oper.moduleOpId in (select moduleOpId from tblRoleModule where (select roleIds from tblUserSunCompany where userid=?) like '%'+roleId+';%')" ;
							}
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, "%mobileType="+mobileType+"%") ;
							if(!"1".equals(userId)){
								pss.setString(2, userId) ;
							}
							ResultSet rss = pss.executeQuery() ;
							List<Properties> listModule = new ArrayList<Properties>() ;
							while(rss.next()){
								Properties properties = new Properties() ;
								properties.setProperty("id", rss.getString("id")) ;
								String linkAddress = rss.getString("linkAddress") ;
								if(linkAddress!=null && linkAddress.contains("&condtion=true")){
									if("OAKnowledgeCenterFile".equals(rss.getString("tblName"))){
										properties.setProperty("target", "doQueryKnowledge") ;
									}else if("companyGoal".equals(rss.getString("tblName"))){
										properties.setProperty("target", "employeeGoal") ;
									}else{
										properties.setProperty("target", "doQueryview") ;
									}
								}else{
									properties.setProperty("target", "doListview") ;
								}
								properties.setProperty("name", rss.getString("moduleName")) ;
								String icon = rss.getString("ICON") ;
								properties.setProperty("icon",icon==null?"oa_office.png":icon) ;
								listModule.add(properties) ;
							}
							result.setRetVal(listModule) ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryBusinessModule mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    /**
     * 跟据模块id查询报表
     * @param userId
     * @return
     */
    public Result queryReportByModule(final String moduleId){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select tblName from tblModules where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, moduleId) ;
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								result.setRetVal(rss.getString("tblName")) ;
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryReportByModule mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    /**
     * 查询工作流已经审批的信息
     * @param userId
     * @return
     */
    public Result queryWorkFlowNodeInfo(final String keyId,final String userId,final String strHttp,
    		final LoginBean lg,final Hashtable map,final Hashtable props){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							MobileWorkFlow workFlow = new MobileWorkFlow() ;
							/*查询单据信息*/
							String sql = "select applyType,keyId,applyContent,createTime,applyBy,applyType,currentNode,checkPerson,tableName from OAMyWorkFlow where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, keyId) ;
							ResultSet rss = pss.executeQuery() ;
							String designId = "" ;
							String checkPerson = "" ;
							String currentNode = "" ;
							String tableName = "" ;
							if(rss.next()){
								designId = rss.getString("applyType") ;
								workFlow.setId(designId) ;
								workFlow.setCreator(rss.getString("applyBy")) ;
								workFlow.setTitle(rss.getString("applyContent")) ;
								workFlow.setCreateTime(rss.getString("createTime")) ;
								tableName = rss.getString("tableName") ;
								OAWorkFlowTemplate tableFlow = BaseEnv.workFlowInfo.get(rss.getString("applyType"));
								if(tableFlow != null && tableFlow.getTemplateName() != null){
									workFlow.setType(tableFlow.getTemplateName()) ;
								} else {
									workFlow.setType("");
								}
								currentNode = rss.getString("currentNode") ;
								if("-1".equals(currentNode)){
									workFlow.setCurrentNode("审核完毕") ;
								}else{
									workFlow.setCurrentNode("审核中") ;
								}
								checkPerson = rss.getString("checkPerson") ;
							}
							
							/*查询当前工作流已经审批的结点信息*/
							WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
							sql = "select nodeID,emp.empFullName,startTime,nodeType,b.deliverance,a.statusId,a.workFlowNode,a.checkPerson from OAMyWorkFlowDet a "
									+ "left join tblAuditeDeliverance b on a.id=b.f_ref left join tblEmployee emp on emp.id=a.checkPerson "
									+ "where a.f_ref=? order by sortOrder " ;
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, keyId) ;
							rss = pss.executeQuery() ;
							int stepNum = 1 ;
							List<MobileFlowNode> nodeList = new ArrayList<MobileFlowNode>() ;
							String lastCheckPerson = "" ;
							while(rss.next()){
								String nodeId = rss.getString("nodeID") ;
								String nodeType = rss.getString("nodeType") ;
								String fullName = rss.getString("empFullName") ;
								String deliverance = rss.getString("deliverance") ;
								
								MobileFlowNode flowNode = new MobileFlowNode() ;
								flowNode.setStepid(stepNum++) ;
								FlowNodeBean nodeBean = designBean.getFlowNodeMap().get(nodeId) ;
								StringBuilder display = new StringBuilder() ;
								if (null != nodeBean) {
									display.append(nodeBean.getDisplay()) ;
								}
								if("back".equals(nodeType)){
									display.append(" " + fullName + " 回退") ;
								}else if("cancel".equals(nodeType)){
									display.append(" " + fullName + " 撤回") ;
								}else{
									display.append(" " + fullName + " 转交") ;
								}
								if(deliverance!=null && deliverance.length()>0){
									display.append("，会签意见：" + deliverance) ;
								}
								String lastCheck = rss.getString("checkPerson") ;
								String workFlowNode = rss.getString("workFlowNode") ;
								int statusId = rss.getInt("statusId") ;
								if(lastCheck!=null && lastCheck.length()>0 && statusId==0 
										&& currentNode.equals(workFlowNode)){
									lastCheckPerson = lastCheck ;
								}
								flowNode.setMemo(display.toString()) ;
								flowNode.setApprovalTime(rss.getString("startTime")) ;
								nodeList.add(flowNode) ;
							}
							FlowNodeBean nodeBean = designBean.getFlowNodeMap().get(currentNode) ;
							if(nodeBean!=null && nodeBean.isAllowCancel() 
											&& userId.equals(lastCheckPerson)){
								workFlow.setCancelEnabled(true) ;
							}
							workFlow.setNodes(nodeList) ;
							OAWorkFlowTemplate tWorkFlow = BaseEnv.workFlowInfo.get(designId) ;
							String content = workFlow.getTitle() ;
							if(tWorkFlow != null && tWorkFlow.getFileContent()!=null && tWorkFlow.getFileContent().length()>0){
								sql = "select "+tWorkFlow.getFileContent()+" from "+tableName + " where id='" + keyId +"'" ;
								pss = conn.prepareStatement(sql) ;
								rss = pss.executeQuery() ;
								if(rss.next()){
									content = rss.getString(1) ;
									if(content!=null){
										content = content.replaceAll("src=\"/upload/", "src=\""+strHttp+"/upload/") ;
									}else{
										content = workFlow.getTitle()  ;
									}
								}
							}
							ArrayList childList = DDLOperation.getChildTables(tableName, map) ;
							if(childList!=null && childList.size()>0){
								content = billHtml(tableName,map, lg, props, keyId, "zh_CN",conn) ;
							}
							List<Properties> attrs = new ArrayList<Properties>() ;
							Properties attrsTitle = new Properties() ;
							attrsTitle.setProperty("key", "标题|txt") ;
							attrsTitle.setProperty("value", workFlow.getTitle()) ;
							attrs.add(attrsTitle) ;
							Properties attrsContent = new Properties() ;
							attrsContent.setProperty("key", "内容|html") ;
							attrsContent.setProperty("value", content) ;
							attrs.add(attrsContent) ;
							Properties attrsStatus = new Properties() ;
							attrsStatus.setProperty("key", "状态|txt") ;
							attrsStatus.setProperty("value", workFlow.getCurrentNode()) ;
							attrs.add(attrsStatus) ;
							workFlow.setAttrs(attrs) ;
							result.setRetVal(workFlow) ;
							if(checkPerson!=null && !checkPerson.contains(";"+userId+";")){
								result.setRetCode(ErrorCanst.RET_NO_RIGHT_ERROR) ;
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryMyWorkFlow mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    public String billHtml(String tableName,Hashtable map,LoginBean lg,Hashtable props,
    		String keyId,String locale,final Connection conn){
    	BaseEnv.log.debug("MofficeMgt.billHtml 取工作流内容：keyId="+keyId+";tableName="+tableName);
    	String html_str = "" ;
        //判断是否启用分支机构
        String sunClassCode = lg.getSunCmpClassCode();
        boolean hasPic = false;
        //取得查询时的范围权限
        Result rs = new DynDBManager().detail(tableName, map, keyId, sunClassCode,props,lg.getId(),false,"",conn);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	HashMap values = (HashMap) rs.getRetVal();
        	DBTableInfoBean mainTable = DDLOperation.getTableInfo(map,tableName);
        	html_str += "<STYLE type=text/css>";
            html_str += "BODY {FONT-SIZE: 12px; MARGIN: 0px; SCROLLBAR-SHADOW-COLOR: #999999; SCROLLBAR-ARROW-COLOR: #555555; SCROLLBAR-DARKSHADOW-COLOR: #ffffff; SCROLLBAR-BASE-COLOR: #e0e0e0; HEIGHT: 100%; BACKGROUND-COLOR: #ffffff}\n";
            html_str +=
                    "A:link {COLOR: #000000; TEXT-DECORATION: none}\n";
            html_str +=
                    "A:visited {COLOR: #000000; TEXT-DECORATION: none}\n";
            html_str +=
                    "A:hover {COLOR: #0060ff; TEXT-DECORATION: none}\n";
            html_str +=
                    "A:active {COLOR: #0060ff; TEXT-DECORATION: none}\n";
            html_str += "IMG {BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px}\n";
            html_str += "LI {LIST-STYLE-TYPE: none}\n";
            html_str += ".scroll_function_small {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 3px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; OVERFLOW: auto; BORDER-LEFT: #e0e0e0 1px solid; BORDER-BOTTOM: #e0e0e0 1px solid; }\n";
            html_str += ".scroll_function_small_1 {BORDER-RIGHT: #e0e0e 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; OVERFLOW: auto; BORDER-LEFT: #e0e0e0 1px solid; BORDER-BOTTOM: #e0e0e0 1px solid; }\n";
            html_str += ".scroll_function_small_2 {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
            html_str += ".scroll_function_small_3 {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 886px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
            html_str += ".scroll_function_small_repotlist {BORDER-RIGHT: #e0e0e0 1px solid; BORDER-TOP: #e0e0e0 1px solid; MARGIN-TOP: 2px; BACKGROUND: #f9f9f9; FLOAT: left; MARGIN-LEFT: 3px; BORDER-LEFT: #e0e0e0 1px solid; WIDTH: 885px; BORDER-BOTTOM: #e0e0e0 1px solid}\n";
            html_str += ".listRange_list_function {BORDER-RIGHT: #d2d2d2 1px solid}\n";
            html_str += ".listRange_list_function TBODY TD {BORDER-TOP-WIDTH: 0px; PADDING-RIGHT: 1px; PADDING-LEFT: 5px; FONT-SIZE: 12px; BORDER-LEFT: #d2d2d2 1px solid; BORDER-BOTTOM: #d2d2d2 1px solid; WHITE-SPACE: nowrap; HEIGHT: 22px}\n";
            html_str += ".listRange_list_function THEAD TR {}\n";
            html_str += ".listRange_list_function THEAD TD {PADDING-RIGHT: 5px; PADDING-LEFT: 5px; FONT-SIZE: 12px; FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#cccccc,endColorStr=#ffffff); BORDER-LEFT: #d2d2d2 1px solid; COLOR: #42789c; PADDING-TOP: 4px; BORDER-BOTTOM: #d2d2d2 1px solid; WHITE-SPACE: nowrap; HEIGHT: 22px; TEXT-ALIGN: center}\n";
            html_str += ".listRange_list_function INPUT {BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; TEXT-ALIGN: left; BORDER-RIGHT-WIDTH: 0px}\n";
            html_str += ".listRange_list_function SELECT {BORDER-TOP-WIDTH: 0px; PADDING-RIGHT: 0px; PADDING-LEFT: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; PADDING-BOTTOM: 0px; WIDTH: 100%; PADDING-TOP: 0px; TEXT-ALIGN: left; BORDER-RIGHT-WIDTH: 0px}\n";
            html_str += ".listRange_list_statistic {BORDER-RIGHT: #c0c0c0 1px solid; BORDER-BOTTOM: #c0c0c0 1px solid; BACKGROUND-COLOR: #fefef4}\n";
            html_str += ".listRange_list_statistic TD {PADDING-RIGHT: 5px; PADDING-LEFT: 5px; FONT-WEIGHT: bold; BORDER-LEFT: #d2d2d2 1px solid; PADDING-TOP: 4px; HEIGHT: 22px}\n";
            html_str += ".HeadingTitle {font-size:14px;font-weight:bold;MARGIN-TOP: 5px; PADDING-LEFT: 10px; FLOAT: left; WIDTH: 150px; PADDING-TOP: 8px; BORDER-BOTTOM: #81b2e3 1px solid; HEIGHT: 17px; TEXT-ALIGN: left}\n";

            html_str += ".scroll_function_big {FLOAT: left; MARGIN-BOTTOM: 3px; WIDTH: 800px; HEIGHT:auto;padding-bottom:10px;}\n";
            html_str += ".listRange_1 {BORDER-RIGHT: #e0e0e0 1px solid;PADDING-RIGHT: 0px;BORDER-TOP: #e0e0e0 1px solid;MARGIN-TOP: 5px; PADDING-LEFT: 0px;FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#E6F4FD,endColorStr=#ffffff);FLOAT: left;PADDING-BOTTOM: 2px;MARGIN-LEFT: 3px;BORDER-LEFT: #e0e0e0 1px solid;WIDTH: 886px;PADDING-TOP: 2px;BORDER-BOTTOM: #e0e0e0 1px solid;HEIGHT: auto; TEXT-ALIGN: left}\n";
            html_str += ".listRange_1 LI {FLOAT: left; WIDTH:190px}\n";
            html_str += ".listRange_1 BUTTON {VERTICAL-ALIGN: top}\n";
            html_str += ".listRange_1_button LI {MARGIN-TOP: -5px; FLOAT: left; MARGIN-BOTTOM: 5px; VERTICAL-ALIGN: top; WIDTH: 827px; TEXT-ALIGN: right}\n";
            html_str += ".listRange_1 LI SPAN {MARGIN-TOP: 2px; FLOAT: left; WIDTH: 85px; TEXT-ALIGN: right}\n";
            html_str += ".listRange_1 LI DIV {MARGIN-TOP: 6px; FLOAT: left; MARGIN-LEFT: 3px; WIDTH: auto}\n";
            html_str += ".listRange_1 INPUT {FLOAT: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"left\":\"\"); ; MARGIN-LEFT: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"3px\":\"\"); ; WIDTH: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"13px\":\"100px\"); ; BORDER: expression((this.type==\"checkbox\" || this.type==\"radio\") ?\"\":\"1px solid #42789C\"); TEXT-ALIGN: left}\n";
            html_str +=
                    ".listRange_1 LI SELECT {WIDTH: 100px; TEXT-ALIGN: left}\n";
            html_str += "\n";
            html_str += "</STYLE>\n";
            
            html_str += "<DIV class=scroll_function_big  id=mainDiv>\n";
            //html_str += "<DIV class=HeadingTitle>" +mainTable.getDisplay().get(locale) + "</DIV>\n";
            html_str += "<UL class=listRange_1>\n";

            //leep load
            List<DBFieldInfoBean> mainFields = mainTable.getFieldInfos();
            for (DBFieldInfoBean df : mainFields) {
            	if(!GlobalsTool.canImportField(df) || df.getInputType()==3 || df.getInputType()==6){
            		continue ;
            	}
            	Object o = df.getDisplay() == null ? "" :df.getDisplay().get(locale);
                if(df.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE 
                		|| (df.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && df.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE)){
                	for (int j = 0; j < df.getSelectBean().getViewFields().size(); j++) {
						PopField posf = ((PopField) df.getSelectBean().getViewFields().get(j));
						
						String 	asName = posf.asName;
						if(asName.startsWith("@TABLENAME")){
							asName = asName.replaceAll("@TABLENAME", tableName);
						}
						DBFieldInfoBean fb = null;
						String display = posf.getDisplay();
						if(display != null){
							if(display.startsWith("@TABLENAME")){
								display = display.replaceAll("@TABLENAME", tableName);
							}
							String t = display.indexOf(".")>-1? display.substring(0, display.indexOf(".")):"";
							String f = display.indexOf(".")>-1? display.substring(display.indexOf(".") + 1):display;
							fb =  DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
						}
						if(fb==null){
							String t = asName.indexOf(".")>-1? asName.substring(0, asName.indexOf(".")):"";
							String f = asName.indexOf(".")>-1? asName.substring(asName.indexOf(".") + 1):asName;
							fb =  DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
						}
						
						html_str += "<LI><SPAN>" + (null == fb ? "" :fb.getDisplay().get(locale)) +"：</SPAN>" 
							+ (values.get(asName) != null ?values.get(asName).toString(): "&nbsp;")+ "</LI>";
					}
                	
                }else if(df.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE){
                	ArrayList listEnum = (ArrayList) GlobalsTool.getEnumerationItems(df.getRefEnumerationName(), locale) ;
                	String strValue = String.valueOf(values.get(df.getFieldName())) ;
                	for(int j=0;j<listEnum.size();j++){
                		KeyPair key = (KeyPair) listEnum.get(j) ;
                		if(strValue!=null && strValue.equals(key.getValue())){
                			html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"：</SPAN>" 
                            + key.getName()+ "</LI>";
                			break ;
                		}
                	}
                }else if(df.getFieldType() == DBFieldInfoBean.FIELD_PIC){
                	hasPic = true;
                	String imgValue = values.get(df.getFieldName())+"";
                	if(imgValue == null || imgValue.length() == 0){
                		html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"：</SPAN></LI>";
                	}else{
	                	String imgUrl= imgValue;
	                	if(imgValue != "" && imgValue.toLowerCase().indexOf("http")==-1){
	                		imgUrl = "/ReadFile.jpg?tempFile=false&type=PIC&YS=true&tableName=" + tableName + "&fileName="+imgValue;
	                	}
	                	String imgUrl2= imgUrl;
	                	if(imgUrl2.indexOf(";")> 0){
	                		imgUrl2 = imgUrl2.substring(0,imgUrl2.indexOf(";"));
	                	}
	                	html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"：</SPAN>" +
	                			"<a target='_blank' href="+imgUrl2+">" 
	                       + imgUrl2 + "</a>" +
	                       		"</LI>";
                	}
                }else{
                	html_str += "<LI><SPAN>" + (null == o ? "" :o.toString()) +"：</SPAN>" 
                    		 + (values.get(df.getFieldName()) != null ? GlobalsTool.formatNumber(values.get(df.getFieldName()), false, false, 
                            		"true".equals(BaseEnv.systemSet.get("intswitch").getSetting()),
                            		tableName, df.getFieldName(), true): "&nbsp;")+ "</LI>";
                }
                

            }

            html_str += "</UL>\n";
        	 //寻找子表
            ArrayList rowsList = new ArrayList();
            ArrayList childTableList = DDLOperation.getChildTables(tableName, map);
            for (int k = 0;childTableList != null && k < childTableList.size(); k++) {
                DBTableInfoBean childTable = (DBTableInfoBean)childTableList.get(k);
                ArrayList cols = new ArrayList();
                ArrayList rows = new ArrayList();
                for (int i = 0;  childTable != null &&
                             i < childTable.getFieldInfos().size(); i++) {
                    DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
                    if (!GlobalsTool.canImportField(fi) || fi.getInputType()==3 || "f_ref".equals(fi.getFieldName())) {
                        continue;
                    }
                    if(fi.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE 
                    			&& fi.getInputValue()!=null && fi.getInputValue().length()>0){
                    	if(!fi.getSelectBean().getRelationKey().hidden){
                        	String[] ss = new String[5];
	                        Object o = fi.getDisplay().get(locale);
	                        ss[0] = o == null ? fi.getFieldName() : o.toString();
	                        switch (fi.getFieldType()) {
	                        case DBFieldInfoBean.FIELD_INT:
	                            ss[2] = "int";
	                            break;
	                        case DBFieldInfoBean.FIELD_DOUBLE:
	                            ss[2] = "float";
	                            break;
	                        case DBFieldInfoBean.FIELD_DATE_LONG:
	                        case DBFieldInfoBean.FIELD_DATE_SHORT:
	                            ss[2] = "date";
	                            break;
	                        default:
	                            ss[2] = "string";
	                            break;
	                        }
	                        ss[3] = fi.getFieldName();
	                        ss[4] = childTable.getTableName();
	                        cols.add(ss);
                    	}
                    	ArrayList popList = fi.getSelectBean().getViewFields() ;
                    	for(int m=0;m<popList.size();m++ ){
                    		PopField field = (PopField) popList.get(m) ;
                    		if(!"true".equals(field.hiddenInput)){
                        		DBFieldInfoBean dbField=GlobalsTool.getFieldBean(field.getFieldName().substring(0,field.getFieldName().indexOf(".")), 
                        								field.getFieldName().substring(field.getFieldName().indexOf(".")+1));  
                        		if (dbField == null)
                        			continue;
                        		String[] ss = new String[4];
                        		if(field.getDisplay()!=null && field.getDisplay().length()>0){
                        			if(field.getDisplay().contains("@TABLENAME")){
                        				DBFieldInfoBean db = DDLOperation.getFieldInfo(map, childTable.getTableName(),field.getDisplay().substring(field.getDisplay().indexOf(".")+1)) ;
                        				if(db!=null){
                        					ss[0] = db.getDisplay().get(locale) ;
                        				}
                        			}else{
                        				DBFieldInfoBean db = DDLOperation.getFieldInfo(map, field.getFieldName().substring(0,field.getFieldName().indexOf(".")),
  								 				 		field.getFieldName().substring(field.getFieldName().indexOf(".")+1)) ;
			               				if(db!=null){
			               					ss[0] = db.getDisplay().get(locale) ;
			               				}
                        			}
                        		}else{
                        			Object obj = dbField.getDisplay() == null ? "" :dbField.getDisplay().get(locale);
                        			ss[0] = obj == null ? dbField.getFieldName() : obj.toString();
                        		}
                                 switch (dbField.getFieldType()) {
                                 case DBFieldInfoBean.FIELD_INT:
                                     ss[2] = "int";
                                     break;
                                 case DBFieldInfoBean.FIELD_DOUBLE:
                                     ss[2] = "float";
                                     break;
                                 case DBFieldInfoBean.FIELD_DATE_LONG:
                                 case DBFieldInfoBean.FIELD_DATE_SHORT:
                                     ss[2] = "date";
                                     break;
                                 default:
                                     ss[2] = "string";
                                     break;
                                 }
                                 ss[3] = field.getFieldName();
                                 cols.add(ss);
                    		}
                    	}
                    }else{
                    	 String[] ss = new String[4];
                         Object o = fi.getDisplay() == null ? "" :
                                    fi.getDisplay().get(locale);
                         ss[0] = o == null ? fi.getFieldName() : o.toString();
                         switch (fi.getFieldType()) {
                         case DBFieldInfoBean.FIELD_INT:
                             ss[2] = "int";
                             break;
                         case DBFieldInfoBean.FIELD_DOUBLE:
                             ss[2] = "float";
                             break;
                         case DBFieldInfoBean.FIELD_DATE_LONG:
                         case DBFieldInfoBean.FIELD_DATE_SHORT:
                             ss[2] = "date";
                             break;
                         case DBFieldInfoBean.FIELD_PIC:
                             ss[2] = "pic";
                             hasPic = true;
                             break;    
                         default:
                             ss[2] = "string";
                             break;
                         }
                         ss[3] = fi.getFieldName();
                         cols.add(ss);
                    }
                   
                }

                HashMap hm = (HashMap) rs.retVal;
                if (childTable != null) {
                    List childList = (List) hm.get("TABLENAME_" + childTable.getTableName());
                    HashMap moreLan=(HashMap)hm.get("LANGUAGEQUERY");
                    for (int i = 0; i < childList.size(); i++) {
                        HashMap os = (HashMap) (childList).get(i);
                        String[] ss = new String[2];
                        ss[0] = "";
                        for (int j = 0; j < cols.size(); j++) {
                        	String fieldName = ((String[]) cols.get(j))[3] ;
                        	if(os.get(fieldName)==null){
                        		ss[0] = ss[0] +"''%koron%" ;
                        	}else{
                        		DBFieldInfoBean dbField = null ;
                        		if(fieldName.contains(".")){
                        			dbField = DDLOperation.getFieldInfo(map, fieldName.substring(0,fieldName.indexOf(".")), fieldName.substring(fieldName.indexOf(".")+1)) ;
                        		}
                        		if(dbField!=null &&(dbField.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE||dbField.getInputTypeOld()==DBFieldInfoBean.INPUT_LANGUAGE)){
                        			ss[0] = ss[0] +"'"+((KRLanguage)moreLan.get(os.get(fieldName))).get(locale)+"'%koron%" ;
                        		}else if("pic".equals(((String[]) cols.get(j))[2])){ //这是图片字段
                        			if(os.get(fieldName)!=null && !os.get(fieldName).equals("")){
                        				ss[0] = ss[0] + "'<span onclick=showPic('"+os.get(fieldName)+"') >查看图片</span>'%koron%";
                        			}else{
                        				ss[0] = ss[0] + "'&nbsp;'%koron%";
                        			}
                        		}else{
                        			ss[0] = ss[0] + "'" +  GlobalsTool.formatNumber(os.get(fieldName), false, false, 
                        					"true".equals(BaseEnv.systemSet.get("intswitch").getSetting()),tableName, fieldName, true) + "'%koron%";
                        		}
                        	}
                        }
                        ss[0] = ss[0].substring(0, ss[0].lastIndexOf("%koron%"));
                        ss[1] = os.get("id").toString();
                        rows.add(ss);
                    }
                    rowsList.add(new Object[] {childTable.getTableName(), rows});
                }
                if (childTableList != null) {
                	Collections.sort(childTableList, new SortDBTable()) ;
                }
                html_str += "<DIV class=scroll_function_small >\n";
                html_str += "<TABLE class=listRange_list_function id=tblSort cellSpacing=0 cellPadding=0 border=0 name=\"table\">\n";
                html_str += "<THEAD>\n";
                html_str += "<tr>";

                int length_cols = cols.size();
                int length_rows = rows.size();
                for (int i = 0; i < length_cols; i++) {

                    String[] col_str = (String[]) cols.get(i);
                    if (col_str[3] != "id") {
                        html_str += "<td>";
                        html_str += col_str[0];
                        html_str += "</td>";
                    }
                }
                html_str += "</tr>";
                html_str += "</THEAD>";
                html_str += "<TBODY>";

                for (int i = 0; i < length_rows; i++) {
                    html_str += "<tr>";
                    String[] row_str = (String[]) rows.get(i);
                    String[] row_str2 = row_str[0].split("%koron%");
                    int leng = row_str2.length;
                    for (int j = 0; j < leng; j++) {
                        html_str += "<td>";
                        html_str += (row_str2[j].substring(1,row_str2[j].length() - 1).equals("") ?
                                 	"&nbsp;" :row_str2[j].substring(1,row_str2[j].length() - 1));
                        html_str += "</td>";

                    }

                    html_str += "</tr>";

                }
                html_str += "<TBODY>";
                html_str += "</table>";
                html_str += "</DIV>";
                html_str += "</DIV>";

            }
        } 
        if(hasPic){
        	html_str += "<DIV id=picDiv style='display:none'>\n" ;
        	html_str += "	<h1 style='width:100%;position: fixed;display:block;'><span style='float:right;margin-right:20px;border-radius: 5px; background-color: #98B0BD;' onclick =hidePic()>返回</span></h1>\n" ;
        	html_str += "<DIV ><img id=imgdiv src=''/></DIV>\n" ;
        	html_str += "</DIV>	\n" ;
        	html_str += "<script>\n" ;
        	html_str += "	function showPic(pic){\n" ;
        	html_str += "		document.getElementById('mainDiv').style.display = 'none';\n" ;
        	html_str += "		document.getElementById('picDiv').style.display = 'block';\n" ;
        	html_str += "		document.getElementById('imgdiv').src = pic;\n" ;
        	html_str += "	}\n" ;
        	html_str += "	function hidePic(){\n" ;
        	html_str += "		document.getElementById('mainDiv').style.display = 'block';\n" ;
        	html_str += "		document.getElementById('picDiv').style.display = 'none';\n" ;
        	html_str += "	}\n" ;
        	html_str += "</script>\n" ;
        }
        return html_str ;
    }
    
    /**
	* DBTableInfoBean 按tbleName排序
	*/
	public class SortDBTable implements Comparator {
		public int compare(Object o1, Object o2) {
			DBTableInfoBean table1 = (DBTableInfoBean) o1;
			DBTableInfoBean table2 = (DBTableInfoBean) o2;
			
			if(table1==null || table2==null){
				return 0 ;
			}
			
			String tableName1 = table1.getTableName() ;
			String tableName2 = table2.getTableName() ;
			
			return tableName1.compareToIgnoreCase(tableName2) ;
		}
	}
    
    /**
	 * 查询我的系统消息
	 * @param userName
	 * @return
	 */
    public Result queryMyAdvice(final String userId,final String lastmid, final String type, final String status){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					@SuppressWarnings("unchecked")
					public void execute(Connection conn) throws SQLException {
						List<MobileMessage> list = new ArrayList<MobileMessage>();
						try {
//							String sql = "select top 20 * from tblAdvice where Receive=?";
//							if(null!=lastmid && !"".equals(lastmid)){
//							sql += " and createTime < (select createTime from tblAdvice where Receive=? and id =?)";
//							}
//							sql +=" order by createTime desc";
//							PreparedStatement pss = conn.prepareStatement(sql);
//							pss.setString(1, userId);
//							if(null!=lastmid && !"".equals(lastmid)){
//								pss.setString(2, userId);
//								pss.setString(3, lastmid);
//							}							
							
							String sql = "select top 20 * from tblAdvice where Receive='" + userId + "' ";
							String sWhere = "";
							if(null!=type && !"".equals(type)){
								if("mail".equals(type)) {
									sWhere += " and type = 'email' ";
								} else if ("bbs".equals(type)) {
									sWhere += " and (type = 'bbs' or type = 'attention') ";
								} else if ("approval".equals(type)) {
									sWhere += " and (type = 'approve' or type = 'notApprove') ";
								} else if ("advice".equals(type) || "plan".equals(type) || "clientHandOver".equals(type) || "clientShare".equals(type)) {
									sWhere += " and type = '" + type + "' ";
								}
								
							}
							if(null!=status && !"".equals(status)){
								sWhere += " and status = '" + status + "' ";
							}
							if(null!=lastmid && !"".equals(lastmid)){
								sql += " and createTime < (select createTime from tblAdvice where id ='" + lastmid + "' " + sWhere + ") ";
							}
							sql += sWhere + " ";
							sql +=" order by createTime desc";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();							
							while(rss.next()){
								MobileMessage message = new MobileMessage();
								message.setId(rss.getString("id"));
								message.setContent(GlobalsTool.replaceA(rss.getString("Title")));
								String strType = rss.getString("type") ;
								if("notApprove".equals(strType) || "approve".equals(strType)){
									message.setType("approval");
								}else if("email".equals(strType)){
									message.setType("mail") ;
								}else{
									message.setType(strType) ;
								}
								message.setRefid(rss.getString("relationId"));
								if(message.getRefid()==null || message.getRefid().trim().length()==0){
									if(message.getType().equals("mail")){
										//外部邮件通知
										String refid="outmail";
										String accounts=message.getContent().replace("RES<oa.mail.msg.newMail>", "");
										
										
										
										String sql1="select id,MailAddress,account from MailinfoSetting where (createBy=? " +
												"or id in(select f_ref from MailinfoSettingUser where userId=?)) and MailAddress=? and statusId=1";
										PreparedStatement ps = conn.prepareStatement(sql1);
										ps=conn.prepareStatement(sql1);
										ps.setString(1, userId);
										ps.setString(2, userId);
										ps.setString(3, accounts);
										ResultSet rs=ps.executeQuery();
										if(rs.next()){
											Map<String, String> outmail=new HashMap<String, String>();
											outmail.put("bid", rs.getString("id"));
											outmail.put("name", rs.getString("account") );
											outmail.put("accounts", accounts);
											message.setRefid(refid+new Gson().toJson(outmail));
										}
										ps.close();				
									}else{
										message.setType("other") ;
									}
								}
								message.setFlag("noRead".equals(rss.getString("status"))?0:1);
								message.setCreateTime(rss.getString("createTime"));
								list.add(message);
							}
							result.setRetVal(list);
							result.setRealTotal(list.size());
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryMyAdvice mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    /**
	 * 查询系统提醒详细信息
	 * 
	 * @param userId
	 * @param type
	 * @param refid
	 * @return
	 */
	public Result queryGetInfo(String id, String type) {
		String sql = "";
		if ("advice".equals(type)) {
			// advice 通知通告
			sql = "select adviceTitle as title, adviceContext as content, createBy, createTime as time from OAAdviceInfo "
				+ "where id = '" + id + "'";
		} else if ("ordain".equals(type)) {
			// ordain 规章制度
			sql = "select ordainTitle as title, content, createBy, createTime as time from OAOrdainInfo "
				+ "where id = '" + id + "'";
		} else if ("news".equals(type)) {
			// news 新闻中心
			sql = "select newsTitle as title, newsContext as content, createBy, createTime as time from OACompanyNewsInfo "
				+ "where id = '" + id + "'";
		} else if ("knowledge".equals(type)) {
			// knowledge 知识中心
			sql = "select fileTitle as title, description as content, createBy, createTime as time from OAKnowledgeCenterFile "
				+ "where id = '" + id + "'";
		} else {
			Result rs = new Result();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		}

		return sqlListJson(sql);
	}
     
    /**
	 * 返回我的私信列表
	 * 
	 * @param userId
	 * @param senderid
	 * @param lastmid
	 * @return
	 */
	public Result listMessages(final String userId, final String senderid,final String lastmid) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						List<MobileMessageBean> list = new ArrayList<MobileMessageBean>();
						try {
							String sql = "select top 20 * from OAMessage2 where   " +
									"((send=? and Receive=?) or (send=? and Receive=?)) and (operType='person' or operType = 'note') and createBy=?";
							if (null != lastmid && lastmid.trim().length()>0) {
								sql += " and createTime < (select createTime from OAMessage2 where id =?)";
							}
							sql += " Order by createTime desc";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, userId);
							pss.setString(2, senderid) ;
							pss.setString(3, senderid) ;
							pss.setString(4, userId) ;
							pss.setString(5, userId) ;
							if (null != lastmid && lastmid.trim().length()>0) {
								pss.setString(6, lastmid);
							}
							ResultSet rs = pss.executeQuery();
							while (rs.next()) {
								MobileMessageBean messageBean = new MobileMessageBean();
								messageBean.setId(rs.getString("id"));
								messageBean.setContent(GlobalsTool.replaceHTML(rs.getString("Content")));
								messageBean.setFrom(rs.getString("Send"));
								messageBean.setSendTime(rs.getString("createTime"));
								list.add(messageBean);
							}
							Collections.sort(list, new SortMsgTime());
							result.setRetVal(list);
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt listMessages mehtod", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	* OAMessage createTime 升序
	*/
	public class SortMsgTime implements Comparator {
		public int compare(Object o1, Object o2) {
			MobileMessageBean msg1 = (MobileMessageBean) o1;
			MobileMessageBean msg2 = (MobileMessageBean) o2;
			
			if(msg1==null || msg2==null){
				return 0 ;
			}
			
			String mesage1 = msg1.getSendTime() ;
			String mesage2 = msg2.getSendTime() ;
			
			return mesage1.compareToIgnoreCase(mesage2) ;
		}
	}
//	
//	/**
//	 * 发送私信
//	 * 
//	 * @param usetId
//	 * @param to
//	 * @param content
//	 * @return
//	 */
//	public Result sendMessage(final String userId, final String to,final String content,final String sendType) {
//		final Result result =new Result();
//		try {
//			MessageMgt msgMgt = new MessageMgt() ;
//			OnlineUser fromUser = OnlineUserInfo.getUser(userId);
//			if (to != null && to.contains(",")) {
//				String[] receives = to.split(",");
//				for (String strId : receives) {
//					strId = getUserName(strId);
//					OnlineUser toUser = OnlineUserInfo.getUserByEmpName(strId);
//					if (toUser == null || fromUser == null)
//						continue;
//					MessageBean bean = new MessageBean() ;
//					
//			        bean.setId(IDGenerater.getId()) ;
//			        bean.setSend(userId) ;
//			        bean.setSendName(fromUser.getName()) ;
//			        bean.setReceive(toUser.getId()) ;
//			        bean.setReceiveName(toUser.getName()) ;
//			        bean.setContent(content) ;
//			        bean.setOperType(sendType) ;
//			        bean.setCreateBy(userId) ;
//			        bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
//			        bean.setStatus("read") ;
//			        bean.setRelationId(strId) ;
//			        bean.setAffix("") ;
//			    	String receiveId=IDGenerater.getId();
//					Result result2 = msgMgt.sendMsg(receiveId,bean) ;
//					result.setRetCode(result2.getRetCode()) ;
//				}
//			} else {
//				String strUser = getUserName(to);
//				OnlineUser toUser = OnlineUserInfo.getUser(strUser);
//				if (toUser == null || fromUser == null){
//					toUser = OnlineUserInfo.getUserByEmpName(strUser);
//					if(toUser==null){
//						return result ;
//					}
//				}
//				MessageBean bean = new MessageBean() ;
//		        bean.setId(IDGenerater.getId()) ;
//		        bean.setSend(userId) ;
//		        bean.setSendName(fromUser.getName()) ;
//		        bean.setReceive(toUser.getId()) ;
//		        bean.setReceiveName(toUser.getName()) ;
//		        bean.setContent(content) ;
//		        bean.setOperType(sendType) ;
//		        bean.setCreateBy(userId) ;
//		        bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
//		        bean.setStatus("read") ;
//		        bean.setRelationId(userId) ;
//		        bean.setAffix("") ;
//		        String receiveId=IDGenerater.getId();
//		        Result result2 = msgMgt.sendMsg(receiveId,bean) ;
//				result.setRetCode(result2.getRetCode()) ;
//			}
//		} catch (Exception ex) {
//			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//			ex.printStackTrace();
//			BaseEnv.log.error("MOfficeMgt sendMessage mehtod", ex);
//		}
//		return result;
//	}
	
	/**
     * //to:"文小钱"<7237978a_0912251612007550187>, 返回7237978a_0912251612007550187
	 * //to:"廖琳琳"<liaolinlin@cmbc.com.cn>, 返回liaolinlin@cmbc.com.cn
     * @param str
     * @return
     */
    private String getEmailOrUserId(String str) {
        Pattern pattern = Pattern.compile("<([/\\?&\\=\\@\\.\\w\\:\\-\\_]+)>");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return str;
    }
    /**
     * //to:"文小钱"<7237978a_0912251612007550187>, 返回 文小钱
	 * //to:"廖琳琳"<liaolinlin@cmbc.com.cn>, 返回 廖琳琳
     * @param str
     * @return
     */
    private String getUserName(String str) {
    	if(str!=null && str.contains("@")){
    		return str.replace("\"", "") ;
    	}
    	Pattern pattern = Pattern.compile("\"([/\\?&\\=\\@\\.\\w\\:\\_\\-\\u4e00-\\u9fa5]+)\"");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        if(!matcher.find() && str.indexOf("<")!=-1){
        	return str.substring(0, str.indexOf("<")) ;
        }
        return str;
    }

	/**
	 * 我的私信目录列表 mList.setType的值为0 个人，1 部门，2 群组
	 * 
	 * @param userId
	 * @return
	 */
	public Result listMessageBox(final String userId, final String lastId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						List<MobileCountList> list = new ArrayList<MobileCountList>();
						try {
							/* 查询我的私信 */
							String sql = "select tblEmployee.id id,tblEmployee.EmpFullName,OAMessage.asCount count from tblEmployee,("
									+ "select send,count(id) asCount from OAMessage where createBy = ?  and OperType = 'person' group by send having count(id)>0) OAMessage "
									+ "where tblEmployee.id=OAMessage.send ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, userId);
							ResultSet rs = pss.executeQuery();
							String existUserId = "";
							while (rs.next()) {
								MobileCountList mList = new MobileCountList();
								mList.setFrom(rs.getString("id"));
								mList.setType(0);
								mList.setDisplayName(rs.getString("EmpFullName"));
								mList.setCount(rs.getInt("count"));
								list.add(mList);
								existUserId += "'" + mList.getFrom() + "',";
							}
							/* 查询我的私信记录 */
							if (existUserId.length() > 0) {
								existUserId = existUserId.substring(0,existUserId.length() - 1);
							}
							sql = "select top 20 sendName,receiveName,send,receive from OAMessage2  "
									+ "where createBy=? and operType='person'  " ;
							if(existUserId!=null && existUserId.trim().length()>0){
								sql += " and receive not in ("+existUserId+") and send not in ("+existUserId+") " ;
							}
							sql += "order by createTime desc";
							pss = conn.prepareStatement(sql);
							pss.setString(1, userId);
							ResultSet rss = pss.executeQuery();
							HashMap<String, String> empMap = new HashMap<String, String>() ;
							while (rss.next()) {
								String sendName = rss.getString("sendName");
								String receiveName = rss.getString("receiveName");
								String send = rss.getString("send");
								String receive = rss.getString("receive");
								if (!userId.equals(send)  && !empMap.containsKey(send)) {
									MobileCountList mList = new MobileCountList();
									empMap.put(send, send) ;
									mList.setFrom(send);
									mList.setType(0);
									mList.setDisplayName(sendName);
									mList.setCount(0);
									list.add(mList);
								}
								if (!userId.equals(receive) && !empMap.containsKey(receive)) {
									MobileCountList mList = new MobileCountList();
									empMap.put(receive, receive) ;
									mList.setFrom(receive);
									mList.setType(0);
									mList.setDisplayName(receiveName);
									mList.setCount(0);
									list.add(mList);
								}
								
							}
							result.setRetVal(list);
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt listMessageBox mehtod", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

    public Result cancelWorkflow (final String flowId){
    	final Result result = new Result();
    	int retCode = DBUtil.execute(new IfDB() {
   			public int exec(Session session) {
   				session.doWork(new Work() {
   					public void execute(Connection conn) throws SQLException {
   						String sql = "select a.applyType,a.currentNode,b.nodeID,a.tableName from OAMyWorkFlow a," +
   								"OAMyWorkFlowDet b where a.currentNode=b.workFlowNode and a.keyid=?";
   						HashMap<String,String> OAMyWorkFlow = new HashMap<String, String>();
   						try{
   							PreparedStatement ps=conn.prepareStatement(sql);
   							ps.setString(1, flowId);
							ResultSet rs=ps.executeQuery();
							if(rs.next()){
								OAMyWorkFlow.put("id", flowId);
								OAMyWorkFlow.put("lastNodeId", rs.getString("nodeID"));
								OAMyWorkFlow.put("currentNode", rs.getString("currentNode"));
								OAMyWorkFlow.put("designId",rs.getString("applyType"));
								OAMyWorkFlow.put("tableName", rs.getString("tableName")) ;
							}
							result.setRetVal(OAMyWorkFlow);
   							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
   						}catch(Exception ex){
   							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
   							ex.printStackTrace();
   							BaseEnv.log.error("MOfficeMgt cancelWorkflow mehtod",ex);
   						}
   					}
   				});
   				return result.getRetCode();
   			}
   		});
   		result.setRetCode(retCode);
   		return result;
    }
    
    /**
	 * 返回用户详情信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
    public Result getUserInfo(final String userid){
    	final Result result = new Result();
    	int retCode = DBUtil.execute(new IfDB() {
   			public int exec(Session session) {
   				session.doWork(new Work() {
   					public void execute(Connection conn) throws SQLException {
   						String sql = "select a.id,a.empFullName,a.Photo,b.DeptFullName,a.sysName,a.SIGNATURE from tblEmployee a,tblDepartment b " +
   								"where a.DepartmentCode=b.classCode and a.sysName=?";
   						try {
   							PreparedStatement ps=conn.prepareStatement(sql);
   							ps.setString(1, userid);
   							ResultSet rs=ps.executeQuery();
   							MobileOnlineUser onlineUser = new MobileOnlineUser();
   							if(rs.next()){
   								onlineUser.setUid(rs.getString("sysName"));
   					   			onlineUser.setName(rs.getString("EmpFullName"));
   					   			onlineUser.setTitle(rs.getString("DeptFullName"));
   					   			onlineUser.setSign(rs.getString("SIGNATURE"));
   					   			if(null != rs.getString("Photo") && !"".equals(rs.getString("Photo"))){
//	   					   			String icon = rs.getString("Photo").substring(0,rs.getString("Photo").indexOf(":"));
//	   					   			String path = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/";
//	   					   			/* 压缩后的文件名 */
//	   								String name = icon.substring(0, icon.lastIndexOf('.')) + "_small";
//	   					   			doCompress(path+icon, name, path, 51) ;
//	   								String smallIcon = name + icon.substring(icon.lastIndexOf('.'));
//	   					   			icon = GlobalsTool.imageBase64(path+smallIcon);
//	   					   			new File(path+smallIcon).delete() ;
	   					   			onlineUser.setIcon("");
   					   			}else{
   					   				onlineUser.setIcon("") ;
   					   			}
   							}
   							result.setRetVal(onlineUser);
   							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
   						}catch (Exception ex) {
   							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
   							ex.printStackTrace();
   							BaseEnv.log.error("MOfficeMgt getUserInfo mehtod",ex);
   						}
   					}
   				});
   				return result.getRetCode();
   			}
   		});
   		result.setRetCode(retCode);
   		return result;
    }
//    
//    /**
//	 * 压缩图片方法
//	 * 
//	 * @param oldFile 将要压缩的图片
//	 * @param width 压缩宽
//	 * @param height 压缩高
//	 * @param quality 压缩清晰度 <b>建议为1.0</b>
//	 * @param smallIcon 压缩图片后,添加的扩展名（在图片后缀名前添加）
//	 * @param percentage 是否等比压缩 若true宽高比率将将自动调整
//	 * @author zhengsunlei
//	 * @return 如果处理正确返回压缩后的文件名 null则参数可能有误
//	 */
//	public static String doCompress(String oldFile, String newName, String newPath, int nSize) {
//		if (oldFile != null) {
//
//			File fPath = new File(newPath);
//			if (!fPath.exists()) {
//				fPath.mkdirs();
//			}
//			
//			/* 压缩后的文件名 */
//			String newImage = null;
//			if (newName.indexOf(".") == -1) {
//				newName += ".jpg";
//			}
//			newImage = newPath + newName;
//
//			Image srcFile = null;
//			File file = new File(oldFile);
//			if(!file.exists()) return null ;
//			if (null == newPath && "".equals(newPath))
//				newPath = oldFile.substring(0, oldFile.lastIndexOf('/'));
//			
//			try {
//				/* 读取图片信息 */
//				srcFile = ImageIO.read(file);
//				if (null == srcFile) {
//					return null;
//				}
//				if (oldFile.equals(newImage) && srcFile.getWidth(null) <= nSize && srcFile.getHeight(null) <= nSize) {
//					// 已经符合规定的大小
//					return newImage;
//				}
//				
//				int new_w = nSize;
//				int new_h = nSize;
//				// 为等比缩放计算输出的图片宽度及高度
//				double rate1 = ((double) srcFile.getWidth(null))
//							/ (double) nSize;
//				double rate2 = ((double) srcFile.getHeight(null))
//							/ (double) nSize;
//				double rate = rate1 > rate2 ? rate1 : rate2;
//				new_w = (int) (((double) srcFile.getWidth(null)) / rate);
//				new_h = (int) (((double) srcFile.getHeight(null)) / rate);
//				
//				/* 宽高设定 */
//				BufferedImage tag = new BufferedImage(new_w, new_h,
//						BufferedImage.TYPE_INT_RGB);
//				tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);
//
//
//				/* 压缩之后临时存放位置 */
//				File f = new File(newImage);
//				if (!f.getParentFile().exists()) {
//					f.mkdirs();
//				}
//				FileOutputStream out = new FileOutputStream(newImage);
//
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
//
//				/* 压缩质量 */
//				jep.setQuality(1, true);
//				encoder.encode(tag, jep);
//
//				out.close();
//
//			}catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (null != srcFile)
//					srcFile.flush();
//			}
//			return newImage;
//		} else {
//			return null;
//		}
//	}
//    
    /**
 	 * 列举指定邮箱指定文件夹的所有邮件
 	 * @param userName
 	 * @return
 	 */
    public Result querylistMail(final String userId,final String bid,final String fid,final String lastmid){
     	final Result result = new Result();
 		int retCode = DBUtil.execute(new IfDB() {
 			public int exec(Session session) {
 				session.doWork(new Work() {
 					public void execute(Connection conn) throws SQLException {
 						List<MobileEmail> list = new ArrayList<MobileEmail>();
 						try {
 							String sql = "select top 20 * from OAMailInfo where userId=? and groupId=? and account=?";
 							if(null!=lastmid && lastmid.trim().length()>0){	
 								sql += " and createTime<(select createTime from OAMailInfo where id='"+lastmid+"')" ;
 							}
 							sql+=" order by createTime desc";
 							PreparedStatement pss = conn.prepareStatement(sql);
 							pss.setString(1, userId);
 							pss.setString(2, fid);
 							pss.setString(3, bid);
 							ResultSet rs = pss.executeQuery();	
 							while(rs.next()){
 								MobileEmail email = new MobileEmail();
 								email.setId(rs.getString("id"));
 								email.setSubject(rs.getString("MailTitle"));
// 								email.setContent(GlobalsTool.replaceHTML(rs.getString("MailContent")));
 								email.setTo(rs.getString("mailTo"));
 								email.setFrom(rs.getString("mailFrom"));
 								email.setReceivedTime(rs.getString("mailTime"));
 								email.setIntranet("1".equals(rs.getString("emailType"))?true:false);
 								email.setUnread("1".equals(rs.getString("state"))?true:false);
 								String attach = rs.getString("MailAttaches") ;
 								email.setAttach((attach!=null && attach.length()>0)?true:false);
 								email.setImportance("1");
 								list.add(email);
 							}
 							result.setRetVal(list);
 							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
 						}catch (Exception ex) {
 							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
 							ex.printStackTrace();
 							BaseEnv.log.error("MOfficeMgt querylistMail mehtod",ex);
 						}
 					}
 				});
 				return result.getRetCode();
 			}
 		});
 		result.setRetCode(retCode);
 		return result;
    }  
  
  /**
 	 * 返回邮件详细数据
 	 * @param userName
 	 * @return
 	 */
    public Result getMail(final String userId,final String mid,final String strHttp){
     	final Result result = new Result();
 		int retCode = DBUtil.execute(new IfDB() {
 			public int exec(Session session) {
 				session.doWork(new Work() {
 					public void execute(Connection conn) throws SQLException {
// 						List<MobileEmailDate> list = new ArrayList<MobileEmailDate>();
 						String sql="";
 						try {
 							sql = "select * from OAMailInfo where userId=? and id=?";
 							PreparedStatement pss = conn.prepareStatement(sql);
 							pss.setString(1, userId);
 							pss.setString(2, mid);
 							ResultSet rs = pss.executeQuery();	
 							MobileEmailDate email = new MobileEmailDate();
 							if(rs.next()){
 								email.setId(rs.getString("MailTitle"));
 								email.setSubject(rs.getString("MailTitle"));
 								String content = rs.getString("MailContent").replaceAll("src=\"/upload/", "src=\""+strHttp+"/upload/");
 								email.setContent(content);
 								email.setContentMime("html");
 								email.setTo(rs.getString("mailTo"));
 								email.setFrom(rs.getString("mailFrom"));
 								email.setCc(rs.getString("mailCc"));
 								email.setReceivedTime(rs.getString("mailTime"));
 								email.setIntranet("1".equals(rs.getString("emailType"))?true:false);
 								email.setUnread("1".equals(rs.getString("state"))?true:false);
 								String attach = rs.getString("MailAttaches") ;
 								if (attach!=null && attach.length()>0) {
 									email.setAttach(true);
 	 								List<HashMap<String, String>> listAtts = new ArrayList<HashMap<String,String>>();
 	  								String[] atts = attach.split(";");
 	 								for (String name : atts) {
 	 	 								HashMap<String, String> map = new HashMap<String, String>();
 	 	 								map.put("name", name);
 	 									if (rs.getInt("emailType") > 0) {	// 外部邮件是1	
 	 	 									map.put("path", strHttp + "/ReadFile?mobile=true&tempFile=email&emlfile=" + rs.getString("emlfile") + "&charset=&attPath=" + rs.getString("account") + "&fileName=" + name);	
 	 									} else {	// 内部邮件是0
 	 	 									map.put("path", strHttp + "/ReadFile?mobile=true&tempFile=email&emlfile=&charset=&attPath=inner" + userId + "&fileName=" + name); 									
 	 									}
 	 									listAtts.add(map);
 	 								}
 	 								email.setListAttach(listAtts);
 								} else {
 									email.setAttach(false);
 								}
 								
 								/*MobileAttrs attrs = new MobileAttrs();
								attrs.setKey("标题|txt");
								attrs.setValue(rs.getString("MailTitle"));
								lists.add(attrs);
								MobileAttrs attrs2 = new MobileAttrs();
								attrs2.setKey("内容|html");
								attrs2.setValue(rs.getString("MailContent"));
								lists.add(attrs2);
								email.setAttrs(lists);*/
// 								list.add(email);
 	 							result.setRetVal(email);
 	 							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
 							} else {
 	 							result.setRetCode(ErrorCanst.DEFAULT_FAILURE); 								
 							}
 							
 							sql = "update OAMailInfo set state=1 where id=?" ;
 							pss = conn.prepareStatement(sql) ;
 							pss.setString(1, mid) ;
 							pss.executeUpdate() ;
 						}catch (Exception ex) {
 							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
 							ex.printStackTrace();
 							BaseEnv.log.error("MOfficeMgt getMail mehtod",ex);
 						}
 					}
 				});
 				return result.getRetCode();
 			}
 		});
 		result.setRetCode(retCode);
 		return result;
    }  
 
    /**
	 * 返回联系人搜索结果
	 * @param userName
	 * @return
	 */
    public Result listAddress(){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						List<MobileEmails> list = new ArrayList<MobileEmails>();
						String sql="";
						try {
							sql="select EmpFullName,sysName,email from tblEmployee where openFlag=1 and statusId=0";
							PreparedStatement ps=conn.prepareStatement(sql);
							ResultSet st=ps.executeQuery();
							while(st.next()){
								MobileEmails mob=new MobileEmails();
								mob.setName(st.getString("EmpFullName"));
								mob.setUid(st.getString("sysName"));
								mob.setKeyword(mob.getName()+","+ChineseSpelling.getSelling(mob.getName())) ;
								list.add(mob);
							}
							result.setRetVal(list);
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt searchAddress mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
 		return result;
  	} 
    
    /**
	 * 返回联系人搜索结果
	 * @param userName
	 * @return
	 */
    public Result listUsers(){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						List<MobileOnlineUser> list = new ArrayList<MobileOnlineUser>();
						String sql="";
						try {
							sql="select EmpFullName,id,email,departmentCode,sysName from tblEmployee where openFlag=1 and statusId=0";
							PreparedStatement ps=conn.prepareStatement(sql);
							ResultSet st=ps.executeQuery();
							while(st.next()){
								MobileOnlineUser user = new MobileOnlineUser() ;
								user.setName(st.getString("EmpFullName"));
								user.setUid(st.getString("sysName"));
								user.setTitle(st.getString("departmentCode")) ;
								user.setIcon("") ;
								user.setSign("") ;
								user.setKeyword(user.getName()+","+ChineseSpelling.getSelling(user.getName())) ;
								list.add(user);
							}
							result.setRetVal(list);
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt searchAddress mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
 		return result;
  	} 
    
    /**
	 * 跟据关键字查询通讯录
	 * @param userName
	 * @return
	 */
    public Result listAddress2(final String keyWord,final String userId){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						List<MobileEmails> list = new ArrayList<MobileEmails>();
						String sql="";
						try {
							sql="select * from tblEmployee where (empFullName like ? or email like ?) and len(isnull(email,''))>0";
							PreparedStatement ps=conn.prepareStatement(sql);
							ps.setString(1, userId) ;
							ps.setString(2, "%"+keyWord+"%") ;
							ps.setString(3, "%"+keyWord+"%") ;
							ResultSet st=ps.executeQuery();
							while(st.next()){
								MobileEmails mob=new MobileEmails();
								mob.setName(st.getString("empFullName"));
								mob.setUid(st.getString("email"));
								mob.setKeyword(mob.getName()+","+ChineseSpelling.getSelling(mob.getName())) ;
								list.add(mob);
							}
							result.setRetVal(list);
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt searchAddress mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
 		return result;
  	} 
  
  
  	/**
	 * 返回邮箱列表带文件夹
	 * @param userName
	 * @return
	 */
  	public Result listMailBox(final String sid){
	  	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							HashMap<String, Integer> countMap2 = new HashMap<String, Integer>() ;
							//内部邮件
							//String sql="select account,count(*)asCount,emailType from OAMailInfo where account in" +
									//"(select id from MailinfoSetting where createBy=?) and emailType='0' group by account,emailType";
							String sql="select groupId,count(*) asCount,emailType from OAMailInfo where emailType='0' " +
									"and userId=? and state=0 group by groupId,emailType order by groupId";
							PreparedStatement ps=conn.prepareStatement(sql);
							ps.setString(1, sid);
							ResultSet rs=ps.executeQuery();
							List<MobileEmailList> accountList = new ArrayList<MobileEmailList>();
							while(rs.next()){
								countMap2.put("-"+rs.getString("groupId"), rs.getInt("asCount"));
							}
							MobileEmailList emails2 = new MobileEmailList();
							emails2.setId("");
							emails2.setName("内部邮件");
							emails2.setIntranet(true);
							OnlineUser lineUser = OnlineUserInfo.getUser(sid) ;
							emails2.setAccounts(lineUser.getName());
							
							List<MobileFolder> listFolder2 = new ArrayList<MobileFolder>() ;
							MobileFolder mfolder = new MobileFolder();
							mfolder.setId("1");
							mfolder.setName("收件箱");
							mfolder.setIntranet(true);
							if(null==countMap2.get(emails2.getId()+"-1")){
								mfolder.setCount(0) ;	
							}else{
								mfolder.setCount(countMap2.get(emails2.getId()+"-1")) ;
							}
							listFolder2.add(mfolder);
							
							MobileFolder mfolder1 = new MobileFolder();
							mfolder1.setId("2");
							mfolder1.setName("草稿箱");
							mfolder1.setIntranet(true);
							//if(null==countMap2.get(emails2.getId()+"-2")){
								mfolder1.setCount(0);
							//}else{
							//	mfolder1.setCount(countMap2.get(emails2.getId()+"-2"));
							//}
							listFolder2.add(mfolder1);
							
							MobileFolder mfolder2=new MobileFolder();
							mfolder2.setId("3");
							mfolder2.setName("已发送邮件箱");
							mfolder2.setIntranet(true);
							//if(null==countMap2.get(emails2.getId()+"-3")){
								mfolder2.setCount(0);
							//}else{
							//	mfolder2.setCount(countMap2.get(emails2.getId()+"-3"));
							//}
							listFolder2.add(mfolder2);
							
							MobileFolder mfolder3 = new MobileFolder();
							mfolder3.setId("4");
							mfolder3.setName("垃圾邮件箱");
							mfolder3.setIntranet(true);
							//if(null==countMap2.get(emails2.getId()+"-4")){
								mfolder3.setCount(0);
							//}else{
							//	mfolder3.setCount(countMap2.get(emails2.getId()+"-4"));
							//}
							listFolder2.add(mfolder3);
							
							MobileFolder mfolder4 = new MobileFolder();
							mfolder4.setId("5");
							mfolder4.setName("废件箱");
							mfolder4.setIntranet(true);
							//if(null==countMap2.get(emails2.getId()+"-5")){
								mfolder4.setCount(0);
							//}else{
							//	mfolder4.setCount(countMap2.get(emails2.getId()+"-5"));
							//}
							listFolder2.add(mfolder4);
							emails2.setFolders(listFolder2);
							accountList.add(emails2);
							
							
							HashMap<String, Integer> countMap = new HashMap<String, Integer>() ;
							//外部邮件
							sql="select account,groupId,count(*) asCount from OAMailInfo where account in" +
									"(select id from MailinfoSetting where createBy=?) and state=0  group by account,groupId";
							ps=conn.prepareStatement(sql);
							ps.setString(1, sid);
							rs=ps.executeQuery();
							while(rs.next()){
								countMap.put(rs.getString("account")+"-"+rs.getString("groupId"), rs.getInt("asCount"));
							}
							String sql1="select id,MailAddress,account from MailinfoSetting where (createBy=? " +
									"or id in(select f_ref from MailinfoSettingUser where userId=?)) and statusId=1";
							ps=conn.prepareStatement(sql1);
							ps.setString(1, sid);
							ps.setString(2, sid);
							rs=ps.executeQuery();
							while(rs.next()){
								MobileEmailList emails=new MobileEmailList();
								emails.setId(rs.getString("id"));
								emails.setName(rs.getString("account"));
								emails.setAccounts(rs.getString("MailAddress"));

								List<MobileFolder> listFolder = new ArrayList<MobileFolder>() ;
								MobileFolder folder = new MobileFolder();
								folder.setId("1");
								folder.setName("收件箱");
								if(null==countMap.get(emails.getId()+"-1")){
									folder.setCount(0) ;	
								}else{
									folder.setCount(countMap.get(emails.getId()+"-1")) ;
								}
								listFolder.add(folder);
								
								MobileFolder folder1 = new MobileFolder();
								folder1.setId("2");
								folder1.setName("草稿箱");
								//if(null==countMap.get(emails.getId()+"-2")){
									folder1.setCount(0);
								//}else{
								//	folder1.setCount(countMap.get(emails.getId()+"-2"));
								//}
								listFolder.add(folder1);
								
								MobileFolder folder2=new MobileFolder();
								folder2.setId("3");
								folder2.setName("已发送邮件箱");
								//if(null==countMap.get(emails.getId()+"-3")){
									folder2.setCount(0);
								//}else{
								//	folder2.setCount(countMap.get(emails.getId()+"-3"));
								//}
								listFolder.add(folder2);
								
								MobileFolder folder3 = new MobileFolder();
								folder3.setId("4");
								folder3.setName("垃圾邮件箱");
								//if(null==countMap.get(emails.getId()+"-4")){
									folder3.setCount(0);
								//}else{
								//	folder3.setCount(countMap.get(emails.getId()+"-4"));
								//}
								listFolder.add(folder3);
								
								MobileFolder folder4 = new MobileFolder();
								folder4.setId("5");
								folder4.setName("废件箱");
								//if(null==countMap.get(emails.getId()+"-5")){
									folder4.setCount(0);
								//}else{
								//	folder4.setCount(countMap.get(emails.getId()+"-5"));
								//}
								listFolder.add(folder4);
								String str=rs.getString("id");
								if(null!=str&&str.trim().length()>0){
									sql="select id,GroupName from OAMailGroup where account=?";
									ps=conn.prepareStatement(sql);
									ps.setString(1, str);
									ResultSet rst=ps.executeQuery();
									while(rst.next()){
										MobileFolder folder6=new MobileFolder();
										folder6.setId(rst.getString("id"));
										folder6.setName(rst.getString("GroupName"));
										//if(null==countMap.get(emails.getId())){
											folder6.setCount(0);
										//}else{
										//	folder6.setCount(countMap.get(emails.getId()+"-"+rst.getString("id")));	
										//}
										listFolder.add(folder6);
									}
								}
								emails.setFolders(listFolder);
								accountList.add(emails);
										
							}
								
												
							result.setRetVal(accountList);
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt searchAddress mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
	 	return result;
	} 
  
  	
	  
    /**
     * 更新信息状态
     * @param type
     * @param refid
     * @param flag
     * @return
     */
    public Result updateMail(final String mailId,final String flag){
  	    final Result result = new Result();
  	    int retCode = DBUtil.execute(new IfDB() {
 			public int exec(Session session) {
 				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {

						try {
							String sql = "";
							String[] mailIds = mailId.split(",");
							String ids = "";
							for (String mailId : mailIds) {
								ids += "'" + mailId + "',";
							}
							ids += "''";
							if ("2".equals(flag)) {
								sql = "delete from OAMailInfo where id in ("
										+ ids + ")";
							} else if ("1".equals(flag)) {
								sql = "update OAMailInfo set state = 1 where id in ("
										+ ids + ")";
							}
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.executeUpdate();
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt updateFlag mehtod",
									ex);
						}

					}
 				});
 				return result.getRetCode();
 			}
 		});
 		result.setRetCode(retCode);
 		return result;
    }
    
    /**
	 * 得到当前节点之前所有审核过的节点
	 * @param id
	 * @param nextNode
	 * @param checkPersons
	 * @param currNode
	 * @param loginBean
	 * @param designId
	 * @param appDepartment
	 * @param locale
	 * @param deliverance
	 * @param affix
	 * @return
	 * @throws BSFException
	 */
	public Result getBackNodeBean(final String id,final FlowNodeBean currNB,
			final List<MobileFlowAction> actionList,final WorkFlowDesignBean bean){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try{
							String lastNodeId="";
							String sql="select nodeID,checkPerson from OAMyWorkFlowDet a where f_ref=? "+
									"and sortOrder<(select top 1 sortOrder from OAMyWorkFlowDet b where b.f_ref=? "+
									"and statusId=0 and nodeID=? order by sortOrder) and len(endTime)>0  and statusId=0 order by sortOrder";
							PreparedStatement pss=conn.prepareStatement(sql);
							pss.setString(1, id);
							pss.setString(2, id);
							pss.setString(3, currNB.getKeyId());
							ResultSet rs=pss.executeQuery();
							HashMap nodeChecks=new HashMap();
							ArrayList nodeIdSort=new ArrayList();
							String person="";
							while(rs.next()){
								lastNodeId=rs.getString(1);
								person=rs.getString(2);
								OnlineUser users=OnlineUserInfo.getUser(person);
								ArrayList<String []> checkPersons;
								if(nodeChecks.get(lastNodeId)==null){
									checkPersons=new ArrayList();	
									nodeChecks.put(lastNodeId, checkPersons);
									nodeIdSort.add(lastNodeId);
								}else{
									checkPersons=(ArrayList<String []>)nodeChecks.get(lastNodeId);
								}
								checkPersons.add(new String[]{person,users.getName()});
							}
							
							for(int i=0;i<nodeIdSort.size();i++){
								String nodeId=nodeIdSort.get(i).toString();
								ArrayList<String []> checkPersons=(ArrayList<String []>)nodeChecks.get(nodeId);
								FlowNodeBean nodeBean = bean.getFlowNodeMap().get(nodeId);
								MobileFlowAction flowAction = new MobileFlowAction() ;
								flowAction.setCancel(true) ;
								flowAction.setId(nodeId) ;
								flowAction.setName(nodeBean.getDisplay()) ;
								flowAction.setIdeaRequired(nodeBean.getIdeaRequired()==false?1:0) ;
								String strPersons =  "" ;
								if(checkPersons.size()>0){
									for(String[] sperson : checkPersons){
										strPersons += sperson[1] + "("+sperson[0]+")," ;
									}
								}
								if(strPersons.length()>1){
									strPersons = strPersons.substring(0, strPersons.length()-1) ;
								}
								flowAction.setTo(strPersons) ;
								if(strPersons.split(",").length>1){
									flowAction.setFixedApp(false) ;
								}else{
									flowAction.setFixedApp(true) ;
								}
								actionList.add(flowAction);
							}
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
							BaseEnv.log.error("MOfficeMgt getBackNodeBean method:", e) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
     * 查询公司目标
     * @param userId
     * @return
     */
    public Result queryCompanyGoal(){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Calendar calendar = Calendar.getInstance() ;
							String sql = "select [month],complete,goal,lowGoal,highGoal from tblCompanyGoalDet " +
									"where f_ref=(select id from tblCompanyGoal where year=?  and goalType=0) order by [month]" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setInt(1, calendar.get(Calendar.YEAR)) ;
							ResultSet rss = pss.executeQuery() ;
							List<Long[]> listGoal = new ArrayList<Long[]>() ;
							while(rss.next()){
								Long[] goal = new Long[5] ; 
								goal[0] = rss.getLong("month") ;
								goal[1] = rss.getLong("complete") ;
								goal[2] = rss.getLong("goal") ;
								goal[3] = rss.getLong("lowGoal") ;
								goal[4] = rss.getLong("highGoal") ;
								listGoal.add(goal) ;
							}
							result.setRetVal(listGoal) ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("MOfficeMgt queryCompanyGoal mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
    
    /**
     * 加载 表结构
     * @param tableName
     * @return
     */
    public Result loadTableInfo(String tableName){
    	//创建参数
    	List<String> param = new ArrayList<String>() ;
    	String hql = "select bean from DBTableInfoBean as bean where bean.tableName=?" ;
    	param.add(tableName) ;
    	Result result = list(hql, param);
    	if(result.realTotal>0){
    		List<DBTableInfoBean> listEmp = (List<DBTableInfoBean>) result.retVal ;
    		result.setRetVal(listEmp.get(0)) ;
    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
    	}else{
    		result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
    	}
    	return result ;
    }
    
    /**
     * 根据表名获取表所有的数据
     * @param tableName
     * @return
     */
    public Result getTableData(final DBTableInfoBean tableInfo){
    	final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	StringBuilder dataJson = new StringBuilder() ;
                        	String sql = "select a.ClassCode, a.GoodsNumber,a.GoodsFullName,a.GoodsSpec,a.BarCode,isnull(c.UnitName,'') UnitName, FacePrice"
                        			+" from tblGoods a left join tblUnit c on a.BaseUnit=c.id where a.statusId=0";
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	while(rss.next()){
                        		dataJson.append("{") ;
                    			Object object = rss.getObject("ClassCode") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("ClassCode:'"+object+"',") ;
                        		
                    			object = rss.getObject("GoodsNumber") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("GoodsNumber:'"+object+"',") ;
                    			
                    			object = rss.getObject("GoodsFullName") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("GoodsFullName:'"+object+"',") ;
                    			
                    			object = rss.getObject("GoodsSpec") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("GoodsSpec:'"+object+"',") ;
                    			
                    			object = rss.getObject("BarCode") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("BarCode:'"+object+"',") ;
                    			
                    			object = rss.getObject("UnitName") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("UnitName:'"+object+"',") ;
                    			
                    			object = rss.getObject("FacePrice") ;
                    			if(object!=null && object.toString()!=null){
                    				object = object.toString().replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;") ;
                    			}
                    			dataJson.append("Price:'"+object+"'") ;
                        		dataJson.append("},") ;
                        	}
                        	if(dataJson.toString().endsWith(",")){
                        		dataJson.delete(dataJson.length()-1, dataJson.length()) ;
                        	}
                        	res.setRetVal(dataJson) ;
                        } catch (Exception ex) {
                        	ex.printStackTrace() ;
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("IPadMgt getTableData method:",ex) ;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res ;
    }
    
    /**
     * 序列号返回单价和商品属性
     * @param tableName
     * @return
     */
    public Result getPrice(final String seq){
    	final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "select GoodsCode,OutStorePrice,isnull(BatchNo,'') BatchNo,isnull(Inch,'') Inch, "
                        			+ " isnull((select PropItemName from tblGoodsPropItem where PropName='yearNo' and PropItemID=a.yearNO),'') yearNo,"
                        			+ " isnull((select PropItemName from tblGoodsPropItem where PropName='Hue' and PropItemID=a.Hue),'') Hue, "
                        			+ " isnull(ProDate,'') ProDate,isnull(Availably,'') Availably from tblStockDet as a where Seq=? " ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, seq) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	HashMap<String, String> values = new HashMap<String, String>() ;
                        	if(rss.next()){
                        		values.put("GoodsCode", rss.getString("GoodsCode")) ;
                        		values.put("Price", String.valueOf(GlobalsTool.round(rss.getDouble("OutStorePrice"), GlobalsTool.getDigitsOrSysSetting("tblStockDet","OutStorePrice")))) ;
                        		values.put("BatchNo", rss.getString("BatchNo")) ;
                        		values.put("Inch", rss.getString("Inch")) ;
                        		values.put("yearNo", rss.getString("yearNo")) ;
                        		values.put("Hue", rss.getString("Hue")) ;
                        		values.put("ProDate", rss.getString("ProDate")) ;
                        		values.put("Availably", rss.getString("Availably")) ;
                        		res.setRetVal(values) ;
                        	}
                        } catch (Exception ex) {
                        	ex.printStackTrace() ;
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("IPadMgt getPrice method:",ex) ;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res ;
    }
    
	/**
	 * 检查是否有管辖全公司权限
	 * @param scopeRight
	 * @return
	 */
	public String checkExistAllDeptScope(ArrayList scopeRight){
		String retStr = "";
		if (scopeRight != null && scopeRight.size()>0) {
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if(lsb!=null && "5".equals(lsb.getFlag())){
					for(String strId : lsb.getScopeValue().split(";")){
						if("ALL".equals(strId)){
							retStr = "ALL";
							break;
						}
					}
					if("ALL".equals(retStr)){
						break;
					}
				}
			}
		}
		return retStr;
	}
    
	private String getClientShareSql(final LoginBean lb, final MOperation mop) {
		StringBuilder condition = new StringBuilder();
		String userId = lb.getId();

		//单据是否启用了审核流
		boolean flowOpen = true;
		String flowCondition = " and (CRMClientInfo.workFlowNode!='0') ";//审核条件
		
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
		scopeRight.addAll(lb.getAllScopeRight());
		
		String allDeptScopeStr = checkExistAllDeptScope(scopeRight);
		/* 设置范围权限 */
		if (!"1".equals(userId)) {
			/*查看某字段值单据*/
			String fieldValueSQL = "" ;
			
			condition.append(" and ");
			
			if(flowOpen){
				condition.append(" ( ");
			}
			
			condition.append(" (CRMClientInfo.createBy ='").append(userId).append("' or ");
			
			if(flowOpen){
				condition.append(" ( ");
			}
			condition.append(" CRMClientInfo.id in(select f_ref from CRMClientInfoEmp " +
					"where employeeId ='").append(userId).append("') ") ;
			
			if(flowOpen){
				condition.append(flowCondition).append(" ) ");
			}
			if(mop!=null){
				/*
				新版获取权限例子
				ArrayList scopeRight = new ArrayList();
				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
				scopeRight.addAll(this.getLoginBean(request).getAllScopeRight());
				*/
				
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						String strDeptIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
							condition.append(" or ");
							
							if(flowOpen){
								condition.append(" ( ");
							}
							condition.append(" CRMClientInfo.createBy in (").append(strUserIds).append(")");
							
							if(flowOpen){
								condition.append(flowCondition).append(" ) ");
							}
							
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strDeptIds += "or departmentCode like '" +strId + "%' ";
								//strDeptIds += "'"+strId+"'," ;
								//condition.append(" or departmentCode like '").append(strId).append("%' ") ;
							}
							if(!"".equals(strDeptIds)){
								
								condition.append(" or ");
								
								if(flowOpen){
									condition.append("( ");
								}
								
								condition.append("(").append(strDeptIds.substring(2)).append(")");
								
								if(flowOpen){
									condition.append(flowCondition).append(")");
								}
							}
							
						}
						if(lsb!=null && "6".equals(lsb.getFlag()) && "CRMClientInfo".equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if(lsb.getScopeValue().contains(";")){
									String[] scopes = lsb.getScopeValue().split(";") ;
									String scopeSQL = "(" ;
									for(String str : scopes){
										scopeSQL += "'" + str + "'," ; 
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length()-1) ;
									scopeSQL += ")" ;
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " +scopeSQL ;
								}
				            }
						}
					}
					
				}
			}
			condition.append("))") ;
			
			
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
			
			
			//获取查看单据修改权限的所有职员ID,共享修改权限
			//this.getAllowUpdateUserIds(mop, request);
		}
		condition.append(" and status != '1' ");
		return condition.toString();
    }
    
	/**
	 * 查询客户信息
	 * @param userId
	 * @param keyword
	 * @param mop
	 * @param lastId
	 * @return
	 */
	public Result queryClient(final LoginBean lb, final String keyword, final MOperation mop, final String lastId) {
		/* 设置范围权限 */
		StringBuilder condition = new StringBuilder();
		condition.append("select id,clientName as name from CRMClientInfo  with(index(Inx_CRMClientInfo_ClientName)) where status != '1' ") ;
		if(keyword!=null && keyword.length()>0){
			condition.append(" and clientName like '%"+keyword+"%' ") ;
		}
		condition.append(getClientShareSql(lb, mop));
		return sqlListJson(condition.toString(), "id", lastId, 20, "lastContractTime");

	}
	
	/**
	 * Adroid轮循拿消息
	 * @param userId
	 * @return
	 */
    public Result getMessage(final String userId){
    	final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "select content from tblAdroidMsg where createBy=?" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, userId) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	StringBuffer sb = new StringBuffer() ;
                        	sb.append("{message:[") ;
                        	while(rss.next()){
                        		sb.append("{content:'"+rss.getString("content")+"'},") ;
                        	}
                        	if(sb.toString().endsWith(",")) {
                        		sb.delete(sb.length()-1, sb.length()) ;
                        	}
                        	sb.append("]") ;
                        	res.setRetVal(sb.toString()) ;
                        	sql = "delete from tblAdroidMsg where createBy=?" ;
                        	pss = conn.prepareStatement(sql) ;
                        	pss.executeUpdate() ;
                        } catch (Exception ex) {
                        	ex.printStackTrace() ;
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("MofficeMgt getMessage method:",ex) ;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res ;
    }
    
    /**
     * 添加Adroid轮循拿消息
     * @param userId
     * @param content
     * @param relationId
     * @return
     */
    public Result addtMessage(final String userId,final String content,final String relationId){
    	final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "insert into tblAdroidMsg(id,content,createBy,relationId) values(?,?,?,?)" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, IDGenerater.getId()) ;
                        	pss.setString(2, content) ;
                        	pss.setString(3, userId) ;
                        	pss.setString(4, relationId) ;
                        	pss.executeUpdate() ;
                        } catch (Exception ex) {
                        	ex.printStackTrace() ;
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("MofficeMgt addtMessage method:",ex) ;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res ;
    }
    
    /**
     * 返回客户列表联系人
     * @param userId
     * @param dateTime
     * @param mop
     * @return
     */
    public Result getCRMClientDet(final LoginBean lb, final String dateTime, MOperation mop){

    	/*设置范围权限*/

		StringBuilder condition = new StringBuilder();
    	condition.append("select e.id, e.userName, e.mobile, e.telephone, m.id, m.clientName, m.lastUpdateTime, m.lastContractTime, m.createTime,m.moduleId from CRMClientInfoDet e, CRMClientInfo m where e.f_ref = m.id and m.id in ("
    	  + "select id from CRMClientInfo  with(index(Inx_CRMClientInfo_ClientName)) where status != '1' ") ;
		condition.append(getClientShareSql(lb, mop));
    	condition.append(")");
    	if (null != dateTime && !"".equals(dateTime) && !"null".equals(dateTime))
    		condition.append(" and (m.lastUpdateTime > '" + dateTime + "' or m.lastContractTime > '" + dateTime + "')");
    	condition.append(" order by m.lastContractTime desc");
    	Result res = sqlList(condition.toString(), new ArrayList());
        return res ;
    }
    
    /**
     * 返回某个模板下所有客户
     * @param userId
     * @param ModuleId
     * @param mop
     * @param lastId
     * @return
     */
    public Result getCRMClientInModule(final LoginBean lb, final String ModuleId, MOperation mop, final String lastId){
		StringBuilder condition = new StringBuilder();
    	condition.append("select id from CRMClientInfo  with(index(Inx_CRMClientInfo_ClientName)) where status != '1' ") ;
		condition.append(getClientShareSql(lb, mop));
		condition.append("and moduleId = '").append(ModuleId).append("'") ;
		return sqlListJson(condition.toString(), "id", lastId, 20, "lastContractTime desc");
	}
    
	
	/**
	 * 返回客户列表模板字段名
	 * @param tableName
	 * @return
	 */
	public Result getCRMClientFieldInfo(List<String> fields,
			final String tableName) {
		String fieldNames = new String();
		for (int i = 0; i < fields.size(); i++) {
			fieldNames += "'" + fields.get(i) + "'";
			if (i != fields.size() - 1)
				fieldNames += ',';
		}
		
		String condition = "select tblDBFieldInfo.fieldName,tbllanguage.zh_CN from tblDBFieldInfo" 
			+ " join tblDBEnumerationItem on tblDBEnumerationItem.enumValue = tblDBFieldInfo.groupName" 
			+ " and tblDBEnumerationItem.enumId =(select id from tblDBEnumeration where enumName='" + tableName + "')" 
			+ " join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id" 
			+ " where tableId=(select id from tblDBTableInfo where tableName='" + tableName + "') and tblDBFieldInfo.fieldName in (" + fieldNames + ")"
			+ " order by tblDBEnumerationItem.enumOrder";
		
    	Result res = sqlList(condition, new ArrayList());
        return res ;
	}
	
	/**
	 * 添加客户
	 * @param userId
	 * @param ClientNo
	 * @param moduleId
	 * @param bean
	 * @return
	 */
	public Result addClient(final LoginBean loginBean,  final String moduleId, final AddClientBean bean) {
		final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {                        	
                        	String ClientNo = BillNoManager.find("CRMClientInfo_ClientNo", loginBean,conn);
                    		String ClientId = IDGenerater.getId();
                    		String strDay = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) ;
                    		StringBuilder condition = new StringBuilder();
                    		condition.append("insert into CRMClientInfo(id, ModuleId, ClientNo, ClientName, Address, BriefContent, Attachment, CreateBy, LastUpdateTime, LastContractTime, CreateTime) values(");
                    		condition.append("'").append(ClientId).append("',");
                    		condition.append("'").append(moduleId).append("',");
                    		condition.append("'").append(ClientNo).append("',");
                    		condition.append("'").append(bean.getClientName()).append("',");
                    		condition.append("'").append(bean.getAddress()).append("',");
                    		condition.append("'").append(bean.getClientRemark()).append("',");
                    		condition.append("'").append(bean.getAttachment()).append("',");
                    		condition.append("'").append(loginBean.getId()).append("',");
                    		condition.append("'").append(strDay).append("',");
                    		condition.append("'").append(strDay).append("',");
                    		condition.append("'").append(strDay).append("'");
                    		condition.append(")");
                        	PreparedStatement pss = conn.prepareStatement(condition.toString()) ;
                        	pss.executeUpdate() ;
                        	
                        	condition = new StringBuilder();
                    		condition.append("insert into CRMClientInfoDet(id, UserName, TelePhone, mainUser, f_ref) values(");
                    		condition.append("'").append(IDGenerater.getId()).append("',");
                    		condition.append("'").append(bean.getUserName()).append("',");
                    		condition.append("'").append(bean.getTelephone()).append("',");
                    		condition.append("'1',");
                    		condition.append("'").append(ClientId).append("'");
                    		condition.append(")");
                    		pss = conn.prepareStatement(condition.toString()) ;
                        	pss.executeUpdate() ;

                        	condition = new StringBuilder();
                    		condition.append("insert into CRMShareClient(id, ClientId, PopedomUserIds) values(");
                    		condition.append("'").append(IDGenerater.getId()).append("',");
                    		condition.append("'").append(ClientId).append("',");
                    		condition.append("'").append(loginBean.getId()).append(",'");
                    		condition.append(")");
                    		pss = conn.prepareStatement(condition.toString()) ;
                        	pss.executeUpdate() ;


                        	condition = new StringBuilder();
                    		OnlineUser user = OnlineUserInfo.getUser(loginBean.getId());
                    		condition.append("insert into CRMClientInfoEmp(id, f_ref, DepartmentCode, EmployeeId, SCompanyID) values(");
                    		condition.append("'").append(IDGenerater.getId()).append("',");
                    		condition.append("'").append(ClientId).append("',");
                    		condition.append("'").append(user.getDeptId()).append("',");
                    		condition.append("'").append(loginBean.getId()).append("',");
                    		condition.append("'00001'");
                    		condition.append(")");
                    		pss = conn.prepareStatement(condition.toString()) ;
                        	pss.executeUpdate() ;
                        		
                        } catch (Exception ex) {
                        	ex.printStackTrace() ;
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("MofficeMgt addtMessage method:",ex) ;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res ;
	}

	/**
	 * 工作计划列表
	 * @param userId
	 * @param type
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Result queryMyWorkPlanList(final String userId, final String type, final String beginTime, final String endTime) {
		String type2 = type;
		if ("week".equals(type) || "month".equals(type)) {
			type2 = "day";
		}
		
		StringBuilder condition = new StringBuilder();
		condition.append("select b.id as empId, b.empFullName as empName,a.id as planId, a.planType, a.title, a.createTime, a.statusId from tblDayWorkPlan a ");
		condition.append("left join tblEmployee b on a.employeeId = b.id ") ;
		condition.append("where a.createBy = '").append(userId).append("' ") ;	// 自己创建
		condition.append("and a.beginDate >= '").append(beginTime).append("' and a.endDate <= '").append(endTime).append("' ");
		condition.append("and (a.planType = '").append(type).append("' or a.planType = '").append(type2).append("') ");
		condition.append("order by a.planType desc, a.createTime");
		Result res = sqlListJson(condition.toString());
	    return res ;
	}

	/**
	 * 工作计划列表
	 * @param userId
	 * @param type
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Result queryKnowPersonWorkPlanList(final String userId, final String type, final String beginTime, final String endTime) {
		String type2 = type;
		if ("week".equals(type) || "month".equals(type)) {
			type2 = "day";
		}
		
		StringBuilder condition = new StringBuilder();
		condition.append("select b.id as empId, b.empFullName as empName,a.id as planId, a.planType, a.title, a.createTime, a.statusId from tblDayWorkPlan a ");
		condition.append("left join tblEmployee b on a.employeeId = b.id ") ;
		condition.append("where a.id in ( select f_ref as id from tblPlanAssItem where keyId = '").append(userId).append("' and AssociateId = 2 ) ") ;	// 知晓人
		condition.append("and a.beginDate >= '").append(beginTime).append("' and a.endDate <= '").append(endTime).append("' ");
		condition.append("and (a.planType = '").append(type).append("' or a.planType = '").append(type2).append("') ");
		condition.append("order by a.employeeId, a.planType desc, a.createTime");
		Result res = sqlListJson(condition.toString());
	    return res ;
	}
	
	/**
	 * 工作计划详情
	 * @param Id
	 * @return
	 */
	public Result queryWorkPlan(final String Id) {
		StringBuilder condition = new StringBuilder();
		condition.append("select b.id as empId, b.empFullName as empName,a.id as planId, a.planType, a.title, a.content, a.createTime, a.beginDate, a.endDate, a.time, a.summary, a.summaryTime, a.statusId from tblDayWorkPlan a ");
		condition.append("left join tblEmployee b on a.employeeId = b.id ");
		condition.append("where a.id = '").append(Id).append("' ");
		Result res = sqlListJson(condition.toString());
		return res;
	}
	
	/**
	 * 工作计划评论
	 * @param Id
	 * @return
	 */
	public Result queryWorkPlanCommit(final String Id) {		
		StringBuilder condition = new StringBuilder();
		condition.append("select a.id, a.remarkType, a.commitId, b.empFullName, a.createTime, a.content from tblPlanRemark a ");
		condition.append("join tblemployee b on a.employeeId = b.id ");
		condition.append("where a.f_ref = '").append(Id).append("' ");
		condition.append("order by a.remarkType, a.createTime");
		Result res = sqlListJson(condition.toString());
	    return res ;
	}

	/**
	 * 知晓人、联系人、客户
	 * @param Id
	 * @return
	 */
	public Result queryWorkPlanAssItem(final String Id) {		
		StringBuilder condition = new StringBuilder();
		condition.append(" select b.name as assName, keyId as id, keyName as name from tblPlanAssItem a, tblPlanAssociate b ");
		condition.append("where a.associateId = b.id and a.f_ref = '").append(Id).append("' ");
		condition.append("order by a.assTime desc");
		Result res = sqlListJson(condition.toString());
	    return res ;
	}
	
	
	/**
	 * 根据关键字查询职员
	 * @param userId
	 * @param keyword
	 * @return
	 */
	public Result queryEmployee(final String keyword, final String lastId) {
		String condition = "select id, EmpFullName as name from tblEmployee where openFlag=1 and statusId!='-1'" ;
		if (null != keyword && !"".equals(keyword)) {
			condition += "and EmpFullName like '%"+keyword+"%'";
		}
		
//		if (null != lastId && !"".equals(lastId)) {
//			condition += "and empnumber > (select empnumber from tblEmployee where id = '" + lastId + "') ";
//		}
//		condition += "order by empnumber";
//		Result res = sqlListJson(condition);
//	    return res ;
		return sqlListJson(condition.toString(), "id", lastId, 20, "empnumber");
	}

	/**
	 * 根据关键字查询联系人
	 * @param userId
	 * @param keyword
	 * @return
	 */
	public Result queryLinkPerson(final String keyword, final String lastId) {
		String condition = "select id, empFullname from tblEmployee where 1=1 " ;
		if (null != keyword && !"".equals(keyword)) {
			condition += "and empFullname like '%"+keyword+"%'";
		}
		Result res = sqlListJson(condition, "id", lastId, 20, "id");
	    return res ;
	}

	
	/**
	 * 根据ID查询人员详情
	 * @param Id
	 * @return
	 */
	public Result loadAddress(final String userId){
		//String condition = "select a.name, d.deptFullName, a.phone, a.sex, a.email, a.birthday, a.qq, a.msn, a.homeAddress, "
//				+ "a.unitName, a.unitAddress, a.unitFax, a.unitPostNumber, a.unitTel, a.unitTelExtension, a.remark "
//				+ "from OACommunicationNoteInfo a left join OACommunicationNoteGroup g on g.id=a.GroupID "
//				+ "left join tblEmployee e on a.id = e.id "
//				+ "left join tblDepartment d on  e.departmentCode = d.classCode "
//				+ "where a.isPublic=2 or a.createby = '" + userId + "' "
//				+ "order by name ";
		String condition = "select empFullname as name,d.deptFullName,a.mobile,a.gender,a.email,a.bornDate,a.qq,a.familyAddress,";
		condition += "a.remark from tblEmployee a left join tblDepartment d on a.departmentCode = d.classCode ";
		condition += "where a.id='"+userId+"' order by empFullname";
		Result res = sqlListJson(condition);
		return res;
	}

	/**
	 * 查询商品
	 * @param userId
	 * @param keyword
	 * @return
	 */
	public Result queryGoods(final String goodsFullName, final String goodsNumber, final String barCode, final String lastId) {
		String condition = "select id, GoodsFullName, GoodsNumber, Barcode, GoodsSpec from tblGoods where statusId = 0 " ;
		if (null != goodsFullName && !"".equals(goodsFullName)) {
			condition += "and GoodsFullName like '%"+goodsFullName+"%' ";
		}
		if (null != goodsNumber && !"".equals(goodsNumber)) {
			condition += "and GoodsNumber like '%"+goodsNumber+"%' ";
		}
		if (null != barCode && !"".equals(barCode)) {
			condition += "and BarCode like '%"+barCode+"%' ";
		}
		Result res = sqlListJson(condition, "id", lastId, 20, "GoodsNumber");
	    return res ;
	}

	/**
	 * 查询仓库
	 * @param userId
	 * @param keyword
	 * @return
	 */
	public Result queryStock(final String keyword, final String lastId) {
		String condition = "select id, StockFullName from tblStock where isCatalog = 0 " ;
		if (null != keyword && !"".equals(keyword)) {
			condition += "and StockFullName like '%"+keyword+"%'";
		}
		Result res = sqlListJson(condition, "id", lastId, 20, "classCode");
	    return res ;
	}

	/**
	 * 查询商品库存
	 * @param stockCode
	 * @param goodsCode
	 * @param lastId
	 * @return
	 */
	public Result queryStoreDetail(final String storeId, final String goodId,
			final String lastId, final boolean bAll) {
		String condition = "select b.id, c.StockFullName, a.GoodsNumber, a.GoodsFullName, a.GoodsSpec, d.UnitName, b.lastQty from tblGoods a "
				+ "left join tblStocks b on b.GoodsCode = a.classCode "
				+ "left join tblStock c on c.classCode = b.StockCode "
				+ "left join tblUnit d on d.id = a.BaseUnit ";
		if (!bAll) {
			condition += "where b.lastQty > 0 ";
		}
		if (null != storeId && storeId.length() > 0) {
			condition += "and c.id = '" + storeId + "' ";
		}
		if (null != goodId && goodId.length() > 0) {
			condition += "and a.id = '" + goodId + "' ";
		}
		return sqlListJson(condition, "id", lastId, 20,
				"c.StockFullName, a.GoodsNumber");
	}
	
	/**
	 * 查询客户移交
	 * @param userId
	 * @param lastId
	 * @return
	 */
	public Result queryCRMClientTransfer(final String userId,
			final String lastId) {
		String condition = "select a.id, a.TransferDate, a.ClientId, b.id as infoId, b.ClientName, c.EmpfullName from CRMClientTransfer a "
				+ "left join CRMClientInfo b on b.id = a.ClientId "
				+ "left join tblEmployee c on c.id = a.TransFerMan "
				+ "where a.TransferTo = '" + userId + "' and a.statusId = '0' ";

		return sqlListJson(condition, "b.id", lastId, 20, "a.TransferDate desc");
	}

	
	/**
	 * 执行自定义语句
	 * @param defineNaem
	 * @param userId
	 * @param values
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result execDefineSql(final String defineName, final String userId,
			final HashMap values, final MessageResources resources,
			final Locale locale) {

		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						// 执行define
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap
								.get(defineName);
						Result rs = defineSqlBean.execute(conn, values, userId,
								resources, locale, null);
						res.setRetCode(rs.getRetCode());
						res.setRetVal(rs.getRetVal());
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}
	
	/**
	 * 加载商品信息
	 * @param goodsId
	 * @param StockCode
	 * @return
	 */
    public Result loadGoods(final String goodsId, final String StockCode){

		String sql = "select a.classCode as GoodsCode,a.BaseUnit as Unit,b.BatchNo,b.lastQty,a.GoodsSpec,a.Spec1,a.Spec2,a.Spec3,a.GoodsFullNamePYM "
				+ "from tblGoods a "
				+ "left join tblStocks b on b.GoodsCode=a.classCode and b.StockCode='"
				+ StockCode
				+ "'  and (b.id not in ((select id from tblStocks where (len(tblStocks.BatchNo)!=0 or len(tblStocks.Hue) !=0 or len(tblStocks.Inch)!=0 or len(tblStocks.yearNo)!=0) and tblStocks.LastQty =0 and tblStocks.StockCode='"
				+ StockCode
				+ "'  )))"
				+ "where  a.statusid!='-1' and  a.id = '"
				+ goodsId
				+ "'";
		return sqlListMap(sql, null, 0, 0);
    }
	
	/**
	 * ID转编号
	 * @param keyId
	 * @return
	 */
    public Result getClassCodeById(final String id, final String tbl){
		String sql = "select classCode from " + tbl + " where id = '" + id + "'";
		return sqlList(sql, new ArrayList());
    }

    /**
     * 获取用户信息
     * @param lastUpdateTime
     * @return
     */
    public Result getAllUsersAfterTime(final String lastUpdateTime){

		String sql = "select e.id, e.empFullName as name, d.deptFullName as department, e.titleId as title, e.tel as telephone, e.mobile, e.email, "
				+ "e.empNumber, e.gender, e.signature as sign, e.photo as icon, e.statusId, e.lastUpdateTime "
				+ "from tblEmployee e "
				+ "left join tblDepartment d on e.departmentCode = d.classCode "
				+ "where 1=1  ";
		if (null != lastUpdateTime && lastUpdateTime.length() > 0) {
			sql += "and e.lastUpdateTime > '" + lastUpdateTime + "' ";
		}
		sql += "order by e.empNumber ";
		return sqlListJson(sql);
    }


    /**
     * 获取部门和群组
     * @param lastUpdateTime
     * @return
     */
    public Result listDeptAndGroup(final String userId){
		String sqlGroup = " select g.id, g.groupName as name, g.groupRemark as remark, 'group' as type from msgGroup g "
				+ "left join msgGroupUser u on u.f_ref = g.id "
				+ "where u.userId = '" + userId + "' ";

    	OnlineUser user = OnlineUserInfo.getUser(userId);
    	String classCodes = "";
    	for (int i = 5; i <= user.getDeptId().length(); i += 5) {
    		classCodes += "'" + user.getDeptId().substring(0, i) + "',";
    	}
    	if (classCodes.endsWith(",")) {
    		classCodes = classCodes.substring(0, classCodes.length() - 1);
    	}
		String sqlDept = " select classCode as id, deptFullName as name, remark, 'dept' as type from tblDepartment where classCode in (" + classCodes + ") ";
		
		return sqlListJson(sqlDept + " union " + sqlGroup);
    }
    

	/**
	 * 我的私信目录列表
	 * 
	 * @param userId
	 * @return
	 */
	public Result listChatNewly(final String userId) {
		Result result;

		// 查询未读的
		String sql = "select a.sendName,a.relationName,a.content,a.operType as type , b.* from OAMessage a "
				+ " left join ( "
				+ "select relationId as id , max(createTime) time, count(id) count from OAMessage where createBy =  '"
				+ userId
				+ "' group by relationId ) b on a.relationId = b.id and a.createTime = b.time "
				+ "where a.createBy = '"
				+ userId
				+ "' and b.id is not NULL order by b.time desc";
		result = sqlListJson(sql);

		// 已存在的ID
		JsonArray jarrNoRead = (JsonArray) result.getRetVal();
		String existId = "('0',";
		for (int i = 0; i < jarrNoRead.size(); i++) {
			JsonObject json = (JsonObject) jarrNoRead.get(i);
			existId += "'" + json.get("id").toString().replaceAll("\"", "")	+ "',";
		}
		existId = existId.substring(0, existId.length() - 1);
		existId += ")";

		JsonArray jarrRead = new JsonArray();
		if (jarrNoRead.size() < 20) {
		
			int n = 20 - jarrNoRead.size();
			
			// 已读的
			sql = "select top " + n + " a.sendName,a.relationName,a.content,a.operType as type , b.* from OAMessage2 a "
					+ " left join ( "
					+ "select relationId as id , max(createTime) time, count(id) count from OAMessage2 where createBy =  '"
					+ userId
					+ "' and relationId not in "
					+ existId
					+ "  group by relationId ) b on a.relationId = b.id and a.createTime = b.time "
					+ "where a.createBy = '"
					+ userId
					+ "' and b.id is not NULL order by b.time desc";
			Result rsRead = sqlListJson(sql);
			
			if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {	// 上面查询有可能为空
				result.setRetCode(rsRead.retCode);
			}
			
			jarrRead = (JsonArray) rsRead.getRetVal();
			for (int i = 0; i < jarrRead.size(); i++) {
				JsonObject json = (JsonObject) jarrRead.get(i);
				json.addProperty("count", "0");
			}
			jarrNoRead.addAll(jarrRead);
		}

		return result;
	}

    /**
	 * 返回我的私信列表
	 * 
	 * @param userId
	 * @param senderid
	 * @param lastmid
	 * @return
	 */
	public Result listChatMsg(final String userId, final String relationId, final String lastId) {
		String sql = "select send, sendName, createTime as time, content as msg from OAmessage2 where createby = '"
				+ userId + "' and relationId = '" + relationId + "' ";
		return sqlListJson(sql, "id", lastId, 20, "createTime desc");
	}

    /**
	 * 返回我的私信列表
	 * 
	 * @param userId
	 * @param senderid
	 * @param lastmid
	 * @return
	 */
	public Result listChatMsg(final String userId, final String relationId, final int count) {
		String sql = "select top " + count + " id, send, sendName, createTime as time, content as msg from OAmessage2 where createby = '"
				+ userId + "' and relationId = '" + relationId + "' "
				+ "order by createTime desc ";
		return sqlListJson(sql);
	}

	/**
	 * 返回所有仓库
	 * 
	 * @return
	 */
	public Result listStock(final String lastId, final int pageSize,
			final String lastUpdateTime) {
		String sql = "select classCode,StockFullName as name "
				+ "from tblStock "
				+ "where statusid!=-1 and workFlowNodeName='finish' and isCatalog=0 ";
//		if (null != lastUpdateTime && lastUpdateTime.length() > 0) {
//			sql += "and lastUpdateTime > '" + lastUpdateTime + "' ";
//		}
		return sqlListJson(sql);
	}
	
	/**
	 * 返回所有往来单位
	 * 
	 * @return
	 */
	public Result listCompany(final String lastId, final int pageSize,
			final String lastUpdateTime) {
		String sql = "select classCode,ComFullName as name,case when ClientFlag=1 then '供应商' else (case when ClientFlag=2 then '客户' else '客户供应商' end) end type "
				+ "from tblCompany "
				+ "where (statusId!=-1 and ClientFlag!=4) and isCatalog=0 ";
//		if (null != lastUpdateTime && lastUpdateTime.length() > 0) {
//			sql += "and a.lastUpdateTime > '" + lastUpdateTime + "' ";
//		}
		return sqlListJson(sql);
	}

	/**
	 * 返回所有商品信息
	 * 
	 * @return
	 */
	public Result listGoods(final String lastId, final int pageSize, final String lastUpdateTime) {
		String sql = "select id, ClassCode, GoodsNumber, GoodsFullName, GoodsSpec, Barcode, BaseUnit as UnitName, ProSalesPrice as Price from tblGoods where 1=1 ";
		if (null != lastUpdateTime && lastUpdateTime.length() > 0) {
			sql += "and lastUpdateTime > '" + lastUpdateTime + "' ";
		}
		return sqlListJson(sql, "id", lastId, pageSize, "id");
	}
	

	/**
	 * 返回仓库名
	 * 
	 * @return
	 */
	public Result queryStock(final String classCode) {
		String sql = "select StockFullName from tblStock "
				+ "where classCode = '" + classCode + "' ";
		return sqlList(sql, new ArrayList());
	}
	
	/**
	 * 返回往来单位名
	 * 
	 * @return
	 */
	public Result queryCompany(final String classCode) {
		String sql = "select ComFullName from ViewCompanyTotal "
				+ "where classCode = '" + classCode + "' ";
		return sqlList(sql, new ArrayList());
	}

	/**
     * 添加移动签到记录
	 * @param userId
	 * @param address
	 * @param point
	 * @param time
	 * @return
	 */
    public Result addSignIn(final String userId,final String address,final String point,final String time){
    	final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "insert into tblMobileSignIn(id,createBy,address,point,time) values(?,?,?,?,?)" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	String id = IDGenerater.getId();
                        	pss.setString(1, id) ;
                        	pss.setString(2, userId) ;
                        	pss.setString(3, address) ;
                        	pss.setString(4, point) ;
                        	pss.setString(5, time) ;
                        	pss.executeUpdate() ;
                        	res.setRetVal(id);
                        } catch (Exception ex) {
                        	ex.printStackTrace() ;
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("MofficeMgt addSignIn method:",ex) ;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res ;
    }
    	

    /**
     * 返回待办
     * @param userId
     * @param status
     * @param lastId
     * @return
     */
	public Result listMyTodo(final String userId, final String status, final int count) {
		String sql = "select a.id, a.title, a.alertTime as time, a.type, '#' + b.color as typeColor, a.createTime, a.finishTime from OATodo a "
				+ "left join OATodoType b on a.type = b.type and (a.createBy = b.userId or b.userId = '') "
				+ "where a.createBy = '" + userId + "' and a.status = '" + status + "'";
		if ("0".equals(status)) {
			return sqlListJson(sql, count, 20, "a.createTime desc");
		} else {
			return sqlListJson(sql, count, 20, "a.finishTime desc");
		}
	}
	
	/**
	 * 返回日程
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Result listMySchedule(String userId, String beginTime, String endTime) {
		String sql = "select a.id, a.title, a.type, '#' + b.color as typeColor, a.stratTime as beginTime, a.finishTime as endTime from OACalendar a "
				+ "left join OACalendarType b on a.type = b.type and (a.userId = b.userId or b.userId = '') "
				+ "where (a.stratTime < '" + beginTime + "' and a.finishTime >= '" + beginTime + "' "
						+ "or (a.stratTime between '" + beginTime + "' and '" + endTime + "')) "
								+ "and a.userId = '" + userId + "' ";
		return sqlListJson(sql);
	}

	private String getItemsSelectString() {
		return "select id, executor, status, title, remark, beginTime, endTime, participant, affix, "
				+ "(select count(*) from OAItemsLog b where b.f_ref = a.id) as logCount, "
				+ "(select count(*) from OATask c where c.itemId = a.id and taskId = '') as taskCount ";
	}

	/**
	 * 返回项目
	 * @param itemId
	 * @return
	 */
	public Result getMyItems(String itemId) {
		String sql = getItemsSelectString() + "from OAItems a where id = '" + itemId + "' ";		
		return sqlListJson(sql);
	}

	/**
	 * 返回项目
	 * @param userId
	 * @param type
	 * @param status
	 * @param count
	 * @return
	 */
	public Result listMyItems(final String userId, final int type, final String status, final int count) {
		Result rs = new Result();
		rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		String sql = getItemsSelectString() + "from OAItems a where 1 = 1 ";
		switch (type) {
		case 0:
			sql += "and (executor = '" + userId + "' or participant like '%" + userId + ",%') ";
			break;
		
		case 1:
			sql += "and executor = '" + userId + "' ";
			break;

		case 2:
			sql += "and participant like '%" + userId + ",%' ";
			break;

		default:
			return rs;
		}
		
		if (!"0".equals(status)) {
			sql += "and status = '" + status + "'";
		}
		
		return sqlListJson(sql, count, 20, "lastUpdateTime desc");
	}
	
	/**
	 * 返回项目动态
	 * @param itemId
	 * @param count
	 * @return
	 */
	public Result listMyItemsLog(String itemId) {
		String sql = "select id, content, createBy, createTime,commentId, replyId from OAItemsLog "
				+ "where f_ref = '" + itemId + "' " + " and isnull(commentId,'') = '' order by createTime desc ";
		Result rs = sqlListJson(sql);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		JsonArray jarrParent = (JsonArray) rs.getRetVal();
		
		sql = "select id, content, createBy, createTime,commentId, replyId from OAItemsLog "
				+ "where f_ref = '" + itemId + "' " + " and isnull(commentId,'') != '' order by commentId, createTime ";
		rs = sqlListJson(sql);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		JsonArray jarrSon = (JsonArray) rs.getRetVal();
		
		JsonArray jarrSorted = new JsonArray();
		for (int i = 0; i < jarrParent.size(); i++) {
			JsonObject json = (JsonObject) jarrParent.get(i);
			jarrSorted.add(json);
			String id = json.get("id").toString().replaceAll("\"", "");
			for (int j = 0; j < jarrSon.size(); j++) {
				JsonObject json2 = (JsonObject) jarrSon.get(j);
				if (id.equals(json2.get("commentId").toString()
						.replaceAll("\"", ""))) {
					jarrSorted.add(json2);
				}
			}

		}
		
		rs = new Result();
		rs.setRetVal(jarrSorted);
		return rs;
	}
	
	private String getTaskSelectString() {
		return "select id, executor, surveyor, status, title, remark, beginTime, endTime, isCatalog, participant, affix, "
				+ "(select count(*) from OATaskLog b where b.f_ref = a.id) as logCount, "
				+ "(select count(*) from OATask c where c.taskId = a.id) as taskCount ";
	}
	
	/**
	 * 返回项目任务
	 * @param itemId
	 * @param count
	 * @return
	 */
	public Result listMyItemsTask(String itemId) {
		String sql = getTaskSelectString() 
				+ "from OATask a where itemId = '" + itemId + "' and taskId = '' "
				+ "order by status, createTime desc ";
		return sqlListJson(sql);
	}
	


	/**
	 * 返回任务
	 * @param taskId
	 * @return
	 */
	public Result getMyTask(String taskId) {
		String sql = getTaskSelectString() 
				+ "from OATask a where id = '" + taskId + "' ";
		
		return sqlListJson(sql);
	}

	/**
	 * 返回任务
	 * @param userId
	 * @param type
	 * @param status
	 * @param count
	 * @return
	 */
	public Result listMyTask(final String userId, final int type, final String status, final int count) {
		Result rs = new Result();
		rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		String sql = getTaskSelectString() 
				+ "from OATask a where 1 = 1 ";
		switch (type) {
		case 0:
			sql += "and (createBy = '" + userId + "' or executor = '" + userId + "' or participant like '%" + userId + ",%' or surveyor = '" + userId + "') ";
			break;

		case 1:
			sql += "and executor = '" + userId + "' ";
			break;

		case 2:
			sql += "and participant like '%" + userId + ",%' ";
			break;

		case 3:
			sql += "and createBy = '" + userId + "' ";
			break;

		case 4:
			sql += "and surveyor = '" + userId + "' ";
			break;

		default:
			return rs;
		}
		
		if (!"0".equals(status)) {
			sql += "and status = '" + status + "'";
		}
		
		return sqlListJson(sql, count, 20, "lastUpdateTime desc");
	}
	
	/**
	 * 返回任务动态
	 * @param taskId
	 * @param count
	 * @return
	 */
	public Result listMyTaskLog(String taskId) {

		String sql = "select id, content, createBy, createTime,commentId, replyId from OATaskLog "
				+ "where f_ref = '" + taskId + "' " + " and isnull(commentId,'') = '' order by createTime desc ";
		Result rs = sqlListJson(sql);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		JsonArray jarrParent = (JsonArray) rs.getRetVal();
		
		sql = "select id, content, createBy, createTime,commentId, replyId from OATaskLog "
				+ "where f_ref = '" + taskId + "' " + " and isnull(commentId,'') != '' order by commentId, createTime ";
		rs = sqlListJson(sql);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		JsonArray jarrSon = (JsonArray) rs.getRetVal();
		
		JsonArray jarrSorted = new JsonArray();
		for (int i = 0; i < jarrParent.size(); i++) {
			JsonObject json = (JsonObject) jarrParent.get(i);
			jarrSorted.add(json);
			String id = json.get("id").toString().replaceAll("\"", "");
			for (int j = 0; j < jarrSon.size(); j++) {
				JsonObject json2 = (JsonObject) jarrSon.get(j);
				if (id.equals(json2.get("commentId").toString()
						.replaceAll("\"", ""))) {
					jarrSorted.add(json2);
				}
			}

		}
		
		rs = new Result();
		rs.setRetVal(jarrSorted);
		return rs;
	
//		
//		
//		String sql = "select id, content, createBy, createTime,commentId, replyId from OATaskLog "
//				+ "where f_ref = '" + taskId + "'" + "order by createTime ";
//		return sqlListJson(sql);
	}
	
	/**
	 * 返回子任务
	 * @param taskId
	 * @param count
	 * @return
	 */
	public Result listSonTask(String taskId) {
		String sql = getTaskSelectString() 
				+ "from OATask a where taskId = '" + taskId + "' "
				+ "order by status, createTime desc ";
		return sqlListJson(sql);
	}
	/**
	 * 返回我的日志一周
	 * @param creater
	 * @param date
	 * @return
	 */
	public Result listMyLog(String creater,String date) {
		try {
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			String endDate = sdf.format(new Date(sdf.parse(date).getTime()+7*24*3600000));
			String sql = "select l.id,l.type,l.workLogDate as date,d.id as contentId,d.contents,d.schedule,d.contentType from OAWorkLogDet d left join OAWorkLog l on d.workLogId=l.id where l.workLogDate>='"+date+"' and l.workLogDate<'"+endDate+"' and createBy='"+creater+"' order by d.id";
			return sqlListJson(sql);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;		
	}
	/**
	 * 添加日志明细
	 * @param userId 
	 * @param workLogId
	 * @param workLogType 日志类型 day,week
	 * @param workLogDate 日志日期
	 * @param workLogDetId
	 * @param contentType 类型 1 总结 2计划
	 * @param content 内容
	 * @return
	 */
	public Result addUpdateLog(final String userId,final String workLogId,final String workLogType,final String workLogDate,final String workLogDetId,final String contentType,final String content){
		final Result rst = new Result();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);				
	      	    OAWorkLogBean workLogBean = new OAWorkLogBean();		
	      		workLogBean.setType(workLogType);
	      		workLogBean.setWorkLogDate(workLogDate);
	      		workLogBean.setCreateBy(userId);
	      		workLogBean.setLastUpdateBy(userId);
	      		workLogBean.setCreateTime(nowDate);
	      		workLogBean.setLastUpdateTime(nowDate);
	      		JsonObject retJson = new JsonObject();
	        	if("".equals(workLogId)){
	        		workLogBean.setId(IDGenerater.getId());
	        		retJson.addProperty("workLogId", workLogBean.getId());
	      			addBean(workLogBean, session);
	      		}else{
	      			retJson.addProperty("workLogId","");
	      			workLogBean.setId(workLogId);
	        	}
	        	
	        	OAWorkLogDetBean detBean = new OAWorkLogDetBean();
	        	detBean.setContentType(contentType);
	        	detBean.setWorkLogId(workLogBean.getId());
				detBean.setCreateTime(nowDate);
				detBean.setId(IDGenerater.getId());
				
				if("1".equals(contentType)){					
					String[] sumArr = content.split("&&");										
					detBean.setSchedule(sumArr[1]);
					detBean.setContents(sumArr[0]);
				}else {
					detBean.setContents(content);
					
				}
				if("".equals(workLogDetId)){
					detBean.setId(IDGenerater.getId());
					retJson.addProperty("workLogDetId", detBean.getId());
					addBean(detBean, session);  
				}else{
					retJson.addProperty("workLogDetId", "");
					detBean.setId(workLogDetId);
					updateBean(detBean, session);  
				}
						
				rst.setRetVal(retJson);
	            return rst.getRetCode();	                
	         }
	     });
		return rst;
	}
	
	
	
	
	/**
	 * 根据logid返回评论
	 * @param logId
	 * @return
	 */
	public Result getWorkLogDiscuss(String logId) {
		String sql = "select id,content,createBy,createTime,commentId,replyId from OAWorkLogDiscuss where f_ref='"+logId+"' order by id desc";
		return sqlListJson(sql);	
	}
	/**
	 * 获得所有下属id
	 * @param userIds
	 * @param followIds
	 * @return
	 */
	public Result getFollowIds(String userIds,String followIds) {			
		if(userIds.equals("")){
			JsonArray jarrSorted = new JsonArray();
			JsonObject json =new JsonObject();
			json.addProperty("id", followIds.replaceAll("'", followIds));
			jarrSorted.add(json);
			Result rs=new Result();
			rs.setRetVal(jarrSorted);
			return rs;
		}
		String sql = "SELECT id from tblEmployee WHERE statusId = '0'  and directBoss IN ("+userIds+")";
		Result rs= sqlListJson(sql);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		JsonArray jarrSon = (JsonArray) rs.getRetVal();
		String newuserIds="";
		for(int i=0;i<rs.getRealTotal();i++){
			String id = ((JsonObject) jarrSon.get(i)).get("id").toString().replaceAll("\"", "");
			if(followIds.indexOf(id)<0){
				followIds+=id+",";
				newuserIds+="'"+id+"',";				
			}
		}
		if(!newuserIds.equals(""))
			newuserIds=newuserIds.substring(0, newuserIds.length()-1);
		return getFollowIds(newuserIds,followIds);
		
	}
	/**
	 * 提醒写日志
	 * @param followId   提醒职员ID
	 * @param beFollowId   被提醒职员ID
	 * @param teamLogDate 日期
	 * @param teamLogType 类型week 周,day日
	 * @return
	 */
	public Result warmToWriteLog(String followId,String beFollowId,String teamLogDate,String teamLogType) {
		
		String content = GlobalsTool.getEmpFullNameByUserId(followId)+"提醒你写"+teamLogDate+"的日计划";
		if(teamLogType!=null && "week".equals(teamLogType)){
			String monday;
			try {
				monday = new DateUtil().getMondayBySelTime(teamLogDate);//获取某个时间周一的时间
				//获取周日日期
				Calendar ca = Calendar.getInstance();
				ca.setTime(BaseDateFormat.parse(monday, BaseDateFormat.yyyyMMdd));
				ca.add(Calendar.DAY_OF_MONTH,6);
				String sunday = BaseDateFormat.format(ca.getTime(),BaseDateFormat.yyyyMMdd);//存放周日
				
				content = GlobalsTool.getEmpFullNameByUserId(followId)+"提醒你写"+monday+"至"+sunday+"的周计划";//内容
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		String url = "<a href=\"javascript:mdiwin('/OAWorkLogAction.do?operation=4','我的日志')\">"+content+"</a>";
		new AdviceMgt().add(followId, content, url, beFollowId, IDGenerater.getId(), "OALogPoint");
		
		return new Result();
	}
}

