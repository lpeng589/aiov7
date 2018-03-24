
package com.koron.oa.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.JsonObject;
import com.koron.oa.bean.MessageBean;
import com.koron.oa.bean.MessageBean2;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2012-3-26
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class MessageMgt extends DBManager  {

	
//	/**
//     * 客户端群组聊天
//     * @param arrayId
//     * @param bean
//     * @return
//     */
//    public Result sendGroupMsg2(final HashMap arrayId,final MessageBean bean) {
//    	final Result rs=new Result();
//    	int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//            	session.doWork(new Work() {
//                    public void execute(Connection conn) throws SQLException {
//                        try { 
//                        	MessageBean message = bean ;
//                        	Statement state = conn.createStatement() ;
//                        	String sql= "select emp.id,emp.empFullName from msgGroupUser groups join tblEmployee emp on emp.id=groups.userID where groups.f_ref='"+bean.getReceive()+"'" ;
//                        	
//                        	ResultSet rss = state.executeQuery(sql) ;
//                        	int num=0;
//                        	while(rss.next()){
//                        		String userId = rss.getString("id") ;
//                        		String receiveId=(String) arrayId.get(userId);
//                        		message.setId(receiveId) ;
//                        		message.setReceive(userId) ;
//                        		message.setReceiveName(rss.getString("empFullName")) ;
//                        		message.setCreateBy(userId) ;
//                        		String tableName = "OAMessage" ;
//                        		if(!bean.getSend().equals(userId)){
//                        			message.setStatus("noRead") ;
//                        		}else{
//                        			tableName = "OAMessage2" ;
//                        		}
//                        		
//                        		sql = "insert into "+tableName+"(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,relationId,relationName) " +
//                				"select '"+message.getId()+"','"+message.getReceive()+"','"+message.getReceiveName()+"',?,'"+message.getOperType()+"','"+message.getStatus()+"','"+message.getSend()+"','"+message.getSendName()+"','"+message.getCreateBy()+"','"+message.getCreateTime()+"','"+message.getRelationId()+"', groupName from msgGroup where id = '"+message.getRelationId()+"'" ;
//                    	        PreparedStatement pss = conn.prepareStatement(sql) ;
//	                        	pss.setString(1, message.getContent()) ;
//	                        	pss.executeUpdate() ;
//	                        	num++;
//                        	}
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//            	return rs.retCode ;
//            }
//    	});
//    	rs.retCode = retCode ;
//        return rs ;
//    }
//    
//	/**
//     * 客户端部门聊天
//     * @param arrayId
//     * @param bean
//     * @return
//     */
//    public Result sendDeptMsg2(final HashMap arrayId,final MessageBean bean) {
//    	final Result rs=new Result();
//    	int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//            	session.doWork(new Work() {
//                    public void execute(Connection conn) throws SQLException {
//                        try { 
//                        	MessageBean message = bean ;
//                        	Statement state = conn.createStatement() ;
//                        	String sql= "SELECT id,empFullName FROM tblemployee WHERE openFlag='1' AND statusId!='-1' and DepartmentCode LIKE '"+bean.getRelationId()+"%'";
//                        	ResultSet rss = state.executeQuery(sql) ;
//                        	while(rss.next()){
//                        		String userId = rss.getString("id") ;
//                        		String receiveId=(String) arrayId.get(userId);
//                        		message.setId(receiveId) ;
//                        		message.setReceive(userId) ;
//                        		message.setReceiveName(rss.getString("empFullName")) ;
//                        		message.setCreateBy(userId) ;
//                        		String tableName = "OAMessage" ;
//                        		if(!bean.getSend().equals(userId)){
//                        			message.setStatus("noRead") ;
//                        		}else{
//                        			tableName = "OAMessage2" ;
//                        		}
//                        		
//                        		sql = "insert into "+tableName+"(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,relationId,relationName) " +
//                				"select '"+message.getId()+"','"+message.getReceive()+"','"+message.getReceiveName()+"',?,'"+message.getOperType()+"','"+message.getStatus()+"','"+message.getSend()+"','"+message.getSendName()+"','"+message.getCreateBy()+"','"+message.getCreateTime()+"','"+message.getRelationId()+"',deptFullName from tblDepartment where classCode = '"+message.getRelationId()+"'" ;
//                    	        PreparedStatement pss = conn.prepareStatement(sql) ;
//	                        	pss.setString(1, message.getContent()) ;
//	                        	pss.executeUpdate() ;
//                        	}
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//            	return rs.retCode ;
//            }
//    	});
//    	rs.retCode = retCode ;
//        return rs ;
//    }
//    
//    /**
//     * 网页端好友聊天
//     * @param id long
//     * @param name String
//     * @return Result
//     */
//    public Result sendMsg(final String receDataId,final MessageBean bean) {
//    	final Result rs=new Result();
//    	int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//            	session.doWork(new Work() {
//                    public void execute(Connection conn) throws SQLException {
//                        try {
//                        	bean.setRelationId(bean.getReceive());
//                        	String sql = "insert into OAMessage2(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,affix,relationId,relationName) " +
//                        			"values('"+bean.getId()+"','"+bean.getReceive()+"','"+bean.getReceiveName()+"',?,'"+bean.getOperType()+"','"+bean.getStatus()+"','"+bean.getSend()+"','"+bean.getSendName()+"','"+bean.getCreateBy()+"','"+bean.getCreateTime()+"','"+bean.getAffix()+"','"+bean.getRelationId()+"','"+bean.getReceiveName()+"')" ;
//                        	PreparedStatement pss = conn.prepareStatement(sql) ;
//                        	pss.setString(1, bean.getContent()) ;
//                        	int i = pss.executeUpdate() ;
//                        	MessageBean receive = bean ;
//                        	receive.setCreateBy(bean.getReceive()) ;
//                        	receive.setRelationId(bean.getSend());
//                        	receive.setId(receDataId) ;
//                        	receive.setStatus("noRead") ;
//                        	sql = "insert into OAMessage(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,affix,relationId,relationName) " +
//                				"values('"+receive.getId()+"','"+receive.getReceive()+"','"+receive.getReceiveName()+"',?,'"+receive.getOperType()+"','"+receive.getStatus()+"','"+receive.getSend()+"','"+receive.getSendName()+"','"+receive.getCreateBy()+"','"+receive.getCreateTime()+"','"+bean.getAffix()+"','"+bean.getRelationId()+"','"+bean.getSendName()+"')" ;
//                        	pss = conn.prepareStatement(sql) ;
//                        	pss.setString(1, bean.getContent()) ;
//                        	pss.executeUpdate() ;
//
//                        	if (MSGConnectCenter.sessionPool.get(bean.getReceive()) == null) {	// KK不在线才推送到手机
//	                        	/* 查询收信人是否已经注册了iPhone */
//	                        	HashMap<String, ArrayList<String>> tokenMap = new HashMap<String, ArrayList<String>>() ;
//	                			Result result = new PublicMgt().queryTokenByUserIds(bean.getReceive(),conn) ;
//	                			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//	                				tokenMap = (HashMap<String, ArrayList<String>>) result.retVal ;
//	                			}
//	                			if(tokenMap!=null && tokenMap.get(bean.getReceive())!=null){
//	                				ArrayList<String> tokenList = tokenMap.get(bean.getReceive());
//	                				String strContent = bean.getContent();
//	        						strContent = bean.getSendName()+"发私信给你：" + GlobalsTool.replaceHTML(strContent);
//	        						for(String token : tokenList){
//	        							JsonObject json = new JsonObject();
//	        							json.addProperty("keyId", bean.getId());
//	        							json.addProperty("type", "chat");
//	        							new AppleApnsSend(String.valueOf(SystemState.instance.dogId), token, strContent, json.toString()).start();
//	        						}
//	        					}
//                        	}
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//            	return rs.retCode ;
//            }
//    	});
//    	rs.retCode = retCode ;
//        return rs ;
//    }
//    
//    /**
//     * 网页端部门聊天
//     * @param id long
//     * @param name String
//     * @return Result
//     */
//    public Result sendDeptMsg(final MessageBean bean) {
//    	final Result rs=new Result();
//    	int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//            	session.doWork(new Work() {
//                    public void execute(Connection conn) throws SQLException {
//                        try { 
//                        	MessageBean message = bean ;
//                        	Statement state = conn.createStatement() ;
//                        	String sql = "select * from tblEmployee where openFlag='1' AND statusId!='-1' and DepartmentCode LIKE '"+bean.getReceive()+"%'";
//                        	ResultSet rss = state.executeQuery(sql) ;
//                        	while(rss.next()){
//                        		String userId = rss.getString("id") ;
//                        		message.setId(IDGenerater.getId()) ;
//                        		message.setReceive(userId) ;
//                        		message.setReceiveName(rss.getString("empFullName")) ;
//                        		message.setCreateBy(userId) ;
//                        		String tableName = "OAMessage" ;
//                        		if(!bean.getSend().equals(userId)){
//                        			message.setStatus("noRead") ;
//                        		}else{
//                        			tableName = "OAMessage2" ;
//                        		}
//                        	
//                        		
//                        		sql = "insert into "+tableName+"(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,relationId,relationName) " +
//                				"select '"+message.getId()+"','"+message.getReceive()+"','"+message.getReceiveName()+"','"+message.getContent()+"','"+message.getOperType()+"','"+message.getStatus()+"','"+message.getSend()+"','"+message.getSendName()+"','"+message.getCreateBy()+"','"+message.getCreateTime()+"','"+message.getRelationId()+"', deptFullName from tblDepartment where classCode = '"+message.getRelationId()+"'" ;
//                        		state.addBatch(sql) ;
//                        		
//                        	}
//                        	state.executeBatch() ;
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//            	return rs.retCode ;
//            }
//    	});
//    	rs.retCode = retCode ;
//        return rs ;
//    }
//    
//    /**
//     * 网页端群组聊天
//     * @param id long
//     * @param name String
//     * @return Result
//     */
//    public Result sendGroupMsg(final MessageBean bean) {
//    	final Result rs=new Result();
//    	int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//            	session.doWork(new Work() {
//                    public void execute(Connection conn) throws SQLException {
//                        try { 
//                        	MessageBean message = bean ;
//                        	Statement state = conn.createStatement() ;
//                        	String sql= "select emp.id,emp.empFullName from msgGroupUser groups join tblEmployee emp on emp.id=groups.userID where groups.f_ref='"+bean.getReceive()+"'" ;
//                        	
//                        	ResultSet rss = state.executeQuery(sql) ;
//                        	int num=0;
//                        	while(rss.next()){
//                        		String userId = rss.getString("id") ;
//                        		message.setId(IDGenerater.getId()) ;
//                        		message.setReceive(userId) ;
//                        		message.setReceiveName(rss.getString("empFullName")) ;
//                        		message.setCreateBy(userId) ;
//                        		String tableName = "OAMessage" ;
//                        		if(!bean.getSend().equals(userId)){
//                        			message.setStatus("noRead") ;
//                        		}else{
//                        			tableName = "OAMessage2" ;
//                        		}
//                        		
//                        		sql = "insert into "+tableName+"(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,relationId,relationName) " +
//                				"select '"+message.getId()+"','"+message.getReceive()+"','"+message.getReceiveName()+"',?,'"+message.getOperType()+"','"+message.getStatus()+"','"+message.getSend()+"','"+message.getSendName()+"','"+message.getCreateBy()+"','"+message.getCreateTime()+"','"+message.getRelationId()+"', groupName from msgGroup where id = '"+message.getRelationId()+"'" ;
//                    	        PreparedStatement pss = conn.prepareStatement(sql) ;
//	                        	pss.setString(1, message.getContent()) ;
//	                        	pss.executeUpdate() ;
//	                        	num++;
//                        	}
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//            	return rs.retCode ;
//            }
//    	});
//    	rs.retCode = retCode ;
//        return rs ;
//    }
//    
//    /**
//     * 查一条单位的详细信息
//     * @param notepadId long 代号
//     * @return Result 返回结果
//     */
//    public Result EmpleoyeeDetail(String id) {
//        return loadBean(id, EmployeeBean.class);
//    }
//
//    public Result getEmployee(String id) {
//       return loadBean(id, EmployeeBean.class);
//    }
    
    /**
     * 加载消息
     * @param id
     * @return
     */
    public Result loadMsg(String id){
    	Result result = loadBean(id,MessageBean2.class) ;
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		MessageBean2 message = (MessageBean2) result.retVal ;
    		if(message.getStatusId()==0){
	    		message.setStatusId(1) ;
	    		updateBean(message) ;
	    		MessageBean send = new MessageBean() ;
	    		send.setId(IDGenerater.getId()) ;
	    		send.setSend(message.getReceive()) ;
	    		send.setSendName(message.getReceiveName()) ;
	    		send.setCreateBy(message.getSend()) ;
	    		send.setReceive(message.getSend()) ;
	    		send.setReceiveName(message.getSendName()) ;
	    		send.setOperType("person") ;
	    		send.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
	    		send.setStatus("noRead") ;
	    		send.setContent("我已成功接收文件："+message.getAffix()) ;
	    		result = addBean(send) ;
    		}
    	}
    	return result ;
    }
    
    /**
     * 
     * @param ids
     * @return
     */
    public Result queryEmployee(String[] ids){
    	Result result = new Result() ;
    	ArrayList paramList = new ArrayList() ;
    	String sql = "select bean from EmployeeBean bean where " ;
    	for(String str : ids){
    		sql += "bean.id=? or " ;
    		paramList.add(str) ;
    	}
    	sql = sql.substring(0,sql.length()-3) ;
    	return list(sql, paramList) ;
    }
    
    
    /**
     * 得到登陆者未查看过的消息
     * @param loginId String
     * @return Result
     */
    public Result getCurrLoginMsgCount(final LoginBean login){
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
//                        	String sql = "select count(sendName) asCount,send,sendName from OAMessage where receive=? and createBy=? and operType in ('person', 'note') and status='noRead'  group by send,sendName" ;
//                        	 pss = conn.prepareStatement(sql) ;
//                        	pss.setString(1, login.getId()) ;
//                        	pss.setString(2, login.getId()) ;
//                        	ResultSet rss = pss.executeQuery() ;
                        	StringBuilder msgList = new StringBuilder() ;
//                        	while(rss.next()){
//                        		if(!login.getId().equals(rss.getString("send"))){
//                        			msgList.append("<a id=\"show"+rss.getString("send")+"\" href=\"javascript:openMsgDialog('person','"+rss.getString("send")+"','"+rss.getString("sendName")+"')\">"+rss.getString("sendName")+" <font color=red>("+rss.getString("asCount")+")</font></a>") ;
//                        		}
//                        	}
//                        	sql = "select groups.id,groups.groupName from msgGroup groups join msgGroupUser guser on guser.f_ref=groups.id where guser.userId=?" ;
//                        	pss = conn.prepareStatement(sql) ;
//                        	pss.setString(1, login.getId()) ;
//                        	rss = pss.executeQuery() ;
//                        	HashMap<String, String> group = new HashMap<String, String>() ;
//                        	while(rss.next()){
//                        		group.put(rss.getString("id"), rss.getString("groupName")) ;
//                        	}
//                        	/*查询部门或分组的未读消息*/
                        	String sql = "select count(relationId) asCount,relationId,relationName,operType from OAMessage where  createBy=? and len(isNull(relationId,''))>0 group by relationId,relationName,operType" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, login.getId()) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	while(rss.next()){
                        		String operType = rss.getString("operType") ;
                        		String relationId = rss.getString("relationId") ;
                        		String relationName = rss.getString("relationName") ;

                        		msgList.append("<a id=\"show"+relationId+"\" href=\"javascript:openMsgDialog('"+operType+"','"+relationId+"','"+relationName+"')\">"+relationName+" <font color=red>("+rss.getString("asCount")+")</font></a>") ;

                        	}
                        	rs.setRetVal(msgList) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            return ;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
    	rs.retCode = retCode ;
        return rs;
    }
//
//   /**
//    * 得到登陆者未查看过的消息
//    * @param loginId String
//    * @return Result
//    */
//   public Result getCurrLoginMsg(String loginId){
//       String sql="select bean.id,bean.Content,bean.createTime  from MessageBean bean where bean.Receive=? and bean.Status=? and (bean.exist='all' or bean.exist='receive')  order by bean.createTime desc";
//       ArrayList param=new ArrayList();
//       param.add(loginId);
//       param.add("noRead");
//       Result rs=this.list(sql,param);
//       return rs;
//   }
//   
//   /**
//    * 删除消息(MessageBean)
//    * 传id
//    * @param loginId String
//    * @return Result
//    */
//   public Result delMessage(final String receiveId,final String userId) {
//       final Result rs = new Result();
//       int retCode = DBUtil.execute(new IfDB() {
//           public int exec(Session session) {
//               session.doWork(new Work() {
//                   public void execute(Connection conn) throws  SQLException {
//                       try {
//                    	   String sql = "" ;
//                    	   if("person".equals(operType)){
//                    		   sql = "delete from OAMessage2 where (send=? or send=?) and (receive=? or receive=?) and createBy=? and operType='person'";
//                    		   PreparedStatement pss = conn.prepareStatement(sql) ;
//                    		   pss.setString(1, userId) ;
//                    		   pss.setString(2, receiveId) ;
//                    		   pss.setString(3, userId) ;
//                    		   pss.setString(4, receiveId) ;
//                    		   pss.setString(5, userId) ;
//                    		   pss.executeUpdate() ;
//                    	   }else{
//                    		   sql = "delete from OAMessage2 where operType=? and relationId=? and createBy=?" ;
//                    		   PreparedStatement pss = conn.prepareStatement(sql) ;
//                    		   pss.setString(1, operType) ;
//                    		   pss.setString(2, receiveId) ;
//                    		   pss.setString(3, userId) ;
//                    		   pss.executeUpdate() ;
//                    	   }
//                       } catch (SQLException ex) {
//                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                           ex.printStackTrace();
//                           return;
//                       }
//                   }
//               });
//               return rs.getRetCode();
//           }
//       });
//       rs.retCode = retCode;
//       return rs;
//   }
//   
//   /**
//    * 根据即时消息群ID查找群名
//    * 传id
//    * @param loginId String
//    * @return Result
//    */
//   public Result selectMSGroupNameByMSID(final String mid) {
//       final Result rs = new Result();
//       int retCode = DBUtil.execute(new IfDB() {
//           public int exec(Session session) {
//               session.doWork(new Work() {
//                   public void execute(Connection conn) throws SQLException {
//                       try {
//                    	   String sql ="select groupName from tblMSGroup where id=?";
//                           PreparedStatement pss = conn.prepareStatement(sql) ;
//                           pss.setString(1, mid) ;
//                           ResultSet rss = pss.executeQuery(sql);
//                           String groupName="";
//                           if(rss.next()){
//                        	   groupName=rss.getString("groupName");
//                           }
//                           rs.setRetVal(groupName);
//                       } catch (SQLException ex) {
//                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                           ex.printStackTrace();
//                           return ;
//                       }
//                   }
//               });
//               return rs.getRetCode();
//           }
//       });
//       rs.setRetCode(retCode) ;
//       return rs;
//   }
   
   /**
    * 根据部门classCode查找职员
    * 传id
    * @param loginId String
    * @return Result
    */
   public Result selectDeptByClassCode(final String classCode) {
       ArrayList paramlist = new ArrayList() ;
       String hql = "select bean from EmployeeBean bean where bean.openFlag=1 and bean.statusId=0 and  bean.DepartmentCode like ?" ;
       paramlist.add(""+classCode+"%") ;
       return list(hql, paramlist) ;
   }
   
   /**
    * 查询分组中人
    * @param ids
    * @return
    */
   public Result queryGroupEmp(final String groupId) {
       final Result rs = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws  SQLException {
                       try {
                    	   String sql = "select emp.id,emp.empFullName from msgGroupUser groups join tblEmployee emp on emp.id=groups.userID where groups.f_ref=? order by emp.empFullName" ;
                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
                    	   pss.setString(1, groupId) ;
                    	   ResultSet rss = pss.executeQuery() ;
                    	   List<EmployeeBean> empList = new ArrayList<EmployeeBean>() ;
                    	   while(rss.next()){
                    		   EmployeeBean emp = new EmployeeBean() ;
                    		   emp.setId(rss.getString("id")) ;
                    		   emp.setEmpFullName(rss.getString("empFullName")) ;
                    		   empList.add(emp) ;
                    	   }
                    	   rs.setRetVal(empList) ;
                       } catch (SQLException ex) {
                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           ex.printStackTrace();
                           return ;
                       }
                   }
               });
               return rs.getRetCode();
           }
       });
      rs.setRetCode(retCode) ;
      return rs;
   }
   
   /**
    * 查询最近联系人
    * @param ids
    * @return
    */
   public Result historyEmp(final String userId) {
       final Result rs = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws  SQLException {
                       try {
                    	   Calendar calendar = Calendar.getInstance() ;
               			   calendar.add(Calendar.DAY_OF_YEAR, -15) ;
               			   String sql = "select * from ( select relationId as id , max(createTime) time from OAMessage2 where createBy =? and relationId is not null and (operType='person' or operType='note') and createTime>? group by relationId ) a  order by a.time desc";
                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
                    	   pss.setString(1, userId) ;
                    	   String createTime = BaseDateFormat.format(calendar.getTime(),BaseDateFormat.yyyyMMddHHmmss) ;
                    	   pss.setString(2, createTime);
                    	   ResultSet rss = pss.executeQuery() ;
                    	   List<String> listEmp = new ArrayList<String>() ;
							while (rss.next()) {
								listEmp.add(rss.getString("id"));
							}
                    	   rs.setRetVal(listEmp) ;
                       } catch (SQLException ex) {
                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           ex.printStackTrace();
                           return ;
                       }
                   }
               });
               return rs.getRetCode();
           }
       });
      rs.setRetCode(retCode) ;
      return rs;
   }
//   
//   /**
//    * 查询未读消息
//    * @param ids
//    * @return
//    */
//   public Result receiveMsg(final String userId,final String receiveType,final String sendId,final String receiveTime) {
//	   ArrayList param=new ArrayList();
//	   String sql="select bean  from MessageBean bean where bean.receive=? and bean.send=? and bean.operType=? and bean.createBy=? " ;
//	   if ("person".equals(receiveType)) {
//		   sql="select bean  from MessageBean bean where bean.receive=? and bean.send=? and (bean.operType='person' or bean.operType='note') and bean.createBy=? " ;
//		   param.add(userId);
//	       param.add(sendId) ;
//	       param.add(userId) ;
//	   } else {
//		   param.add(userId);
//	       param.add(sendId) ;
//	       param.add(receiveType) ;
//	       param.add(userId) ;
//	   }
//	   if(receiveTime==null || receiveTime.length()==0){
//		   sql += " and bean.createTime>? " ;
//		   param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" 00:00:00") ;
//	   }
//	   sql += " order by bean.createTime asc";
//       final Result rs=this.list(sql,param);
//       if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.realTotal>0){
//    	   int retCode = DBUtil.execute(new IfDB() {
//           public int exec(Session session) {
//               session.doWork(new Work() {
//                   public void execute(Connection conn) throws  SQLException {
//                       try {
//                    	   List msgList = (List) rs.getRetVal() ;
//                    	   String msgIds = "" ;
//                    	   for(int i=0;i<msgList.size();i++){
//                    		   MessageBean message = (MessageBean) msgList.get(i) ;
//                    		   msgIds += "'"+message.getId()+"'," ;
//                    		   
//                    		   //更改网页端图片路径
//                    		   if(message.getContent().contains("<img") && message.getContent().contains("zoomImage")){
//                    			   String img=message.getContent();
//	                    		   message.setContent(img.replace("onclick=\"zoomImage(this)\" src=\"", "onclick=\"zoomImage(this)\" src=\"/upload/"));
//                    		   }
//                    		   //替换客户端的QQ表情
//                    		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"face\"")){
//                    			   String img=message.getContent();
//                    			   message.setContent(img.replace("type=\"face\" src=\"", "src=\"/js/plugins/emoticons/images/"));
//                    		   }
//                    		   
//                    		   //此处替换客户端传来的包含图片的信息
//                    		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"img\"")){
//	                    		   String img=message.getContent();
//	                    		   message.setContent(img.replace("type=\"img\" src=\"", "style=\"cursor: pointer;\" onclick=\"zoomImage(this)\" title=\"单击放大\" alt=\"单击放大\"  src=\"/ReadFile.jpg?type=PIC&tempFile=path&path=/msgPic/&fileName="));
//                    		   }
//                    	   }
//                    	   msgIds = msgIds.substring(0,msgIds.length()-1) ;
//                    	   String sql = "insert into OAMessage2 select * from OAMessage where id in ("+msgIds+")" ;
//                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
//                    	   int num = pss.executeUpdate() ;
//                    	   if(num>0){
//                    		   sql = "delete from OAMessage where id in ("+msgIds+")" ;
//                    		   pss = conn.prepareStatement(sql) ;
//                    		   pss.executeUpdate() ;
//                    	   }
//                    	   /**
//                    	   String sql = "update OAMessage set status='read' where receive=? and send=? and operType=?" ;
//                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
//                    	   pss.setString(1, userId) ;
//                    	   pss.setString(2, sendId) ;
//                    	   pss.setString(3, receiveType) ; 
//                    	   pss.executeUpdate() ;
//                    	   */
//                       } catch (SQLException ex) {
//                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                           ex.printStackTrace();
//                           return ;
//                       }
//                   }
//               });
//               return rs.getRetCode();
//           }
//    	   });
//    	   rs.setRetCode(retCode) ;
//      }
//      return rs;
//   }
//   
//   /**
//    * 查询部门或分组未读消息
//    * @param ids
//    * @return
//    */
//   public Result receiveDeptMsg(final String userId,final String receiveType,final String sendId,final String receiveTime) {
//	   ArrayList param=new ArrayList();
//	   String sql="select bean  from MessageBean bean where bean.relationId=? and bean.operType=? and bean.createBy=? " ;
//       param.add(sendId);
//       param.add(receiveType) ;
//       param.add(userId) ;
//	   if(receiveTime==null || receiveTime.length()==0){
//		   //sql += " and bean.createTime>? " ;
//		  // param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+" 00:00:00") ;
//	   }
//	   sql += " order by bean.createTime asc";
//       final Result rs=this.list(sql,param);
//       if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.realTotal>0){
//       int retCode = DBUtil.execute(new IfDB() {
//           public int exec(Session session) {
//               session.doWork(new Work() {
//                   public void execute(Connection conn) throws  SQLException {
//                       try {
//                    	   List msgList = (List) rs.getRetVal() ;
//                    	   String msgIds = "" ;
//                    	   for(int i=0;i<msgList.size();i++){
//                    		   MessageBean message = (MessageBean) msgList.get(i) ;
//                    		   msgIds += "'"+message.getId()+"'," ;
//                    		   
//                    		   //更改网页端图片路径
//                    		   if(message.getContent().contains("<img") && message.getContent().contains("zoomImage")){
//                    			   String img=message.getContent();
//	                    		   message.setContent(img.replace("onclick=\"zoomImage(this)\" src=\"", "onclick=\"zoomImage(this)\" src=\"/upload/"));
//                    		   }
//                    		   //替换客户端的QQ表情
//                    		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"face\"")){
//                    			   String img=message.getContent();
//                    			   message.setContent(img.replace("type=\"face\" src=\"", "src=\"/js/plugins/emoticons/images/"));
//                    		   }
//                    		   
//                    		   //此处替换客户端传来的包含图片的信息
//                    		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"img\"")){
//	                    		   String img=message.getContent();
//	                    		   message.setContent(img.replace("type=\"img\" src=\"", "style=\"cursor: pointer;\" onclick=\"zoomImage(this)\" title=\"单击放大\" alt=\"单击放大\"  src=\"/ReadFile.jpg?type=PIC&tempFile=path&path=/msgPic/&fileName="));
//                    		   }
//                    	   }
//                    	   msgIds = msgIds.substring(0,msgIds.length()-1) ;
//                    	   String sql = "insert into OAMessage2 select * from OAMessage where id in ("+msgIds+")" ;
//                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
//                    	   int num = pss.executeUpdate() ;
//                    	   if(num>0){
//                    		   sql = "delete from OAMessage where id in ("+msgIds+")" ;
//                    		   pss = conn.prepareStatement(sql) ;
//                    		   pss.executeUpdate() ;
//                    	   }
//                    	   /**
//                    	   String sql = "update OAMessage set status='read' where createBy=? and relationId=? and operType=?" ;
//                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
//                    	   pss.setString(1, userId) ;
//                    	   pss.setString(2, sendId) ;
//                    	   pss.setString(3, receiveType) ; 
//                    	   pss.executeUpdate() ;**/
//                       } catch (SQLException ex) {
//                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                           ex.printStackTrace();
//                           return ;
//                       }
//                   }
//               });
//               return rs.getRetCode();
//           }
//      });
//      rs.setRetCode(retCode) ;
//      }
//      return rs;
//   }
   
   
   /**
    * 查询 历史记录
    * @param ids
    * @return
    */
   public Result history(final String keyWord,final int pageSize,final int pageNo,
		   final String relationId,final String createBy) {
		// 创建参数
		List<String> param = new ArrayList<String>();
		String hql = "select bean  from MessageBean2 bean where bean.createBy=? and bean.relationId=?";
		param.add(createBy);
		param.add(relationId);

		if (keyWord != null && keyWord.trim().length() > 0) {
			hql += " and bean.content like ?";
			param.add("%" + keyWord + "%");
		}
		hql += " order by bean.createTime desc";
		// 调用list返回结果

		return list(hql, param, pageNo, pageSize, true);
   }
//   
//  /**
//   * 更新消息状态 mj
//   * @param userId
//   * @param receiveType
//   * @param sendId
//   * @param receiveTime
//   * @return
//   */
//   public Result updateMsgByApp(final String UUID) {
//	   	
//	    //如果是客户端登陆 发现有未读消息，当客户端读取后 进入此方法的时候 。由于此刻UUID传过来的是 代表的是MessageBean的。所以需要进行另外处理
//		Result result = loadBean(UUID, MessageBean2.class);
//		MessageBean2 message = null;
//		MessageBean message1 = null;
//		
//		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			message = (MessageBean2) result.retVal;
//		}
//		if (message == null){//说明符合上文注释 此刻是messaageBean id
//			result =  loadBean(UUID, MessageBean.class);
//			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				message1 = (MessageBean) result.retVal;
//			}
//			if (message1 == null){
//				result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//				return  result;
//			}
//		}
//		
//		ArrayList param = new ArrayList();
//		String sql = "select bean  from MessageBean bean where bean.receive=? and bean.send=? and bean.operType=? and bean.createBy=?";
//		param.add(message1 != null ? message1.getReceive(): message.getReceive());
//		param.add(message1 != null ? message1.getSend(): message.getSend());
//		param.add(message1 != null ? message1.getOperType(): message.getOperType());
//		param.add(message1 != null ? message1.getReceive(): message.getReceive());
//		
//	       final Result rs=this.list(sql,param);
//	       if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.realTotal>0){
//	    	   int retCode = DBUtil.execute(new IfDB() {
//	           public int exec(Session session) {
//	               session.doWork(new Work() {
//	                   public void execute(Connection conn) throws  SQLException {
//	                       try {
//	                    	   List msgList = (List) rs.getRetVal() ;
//	                    	   String msgIds = "" ;
//	                    	   for(int i=0;i<msgList.size();i++){
//	                    		   MessageBean message = (MessageBean) msgList.get(i) ;
//	                    		   msgIds += "'"+message.getId()+"'," ;
//	                    	   }
//	                    	   msgIds = msgIds.substring(0,msgIds.length()-1) ;
//	                    	   String sql = "insert into OAMessage2 select * from OAMessage where id in ("+msgIds+")" ;
//	                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
//	                    	   int num = pss.executeUpdate() ;
//	                    	   //System.out.println("接口同步 insert OAMessage2是否成功："+num);
//	                    	   if(num>0){
//	                    		   sql = "delete from OAMessage where id in ("+msgIds+")" ;
//	                    		   pss = conn.prepareStatement(sql) ;
//	                    		   int r = pss.executeUpdate() ;
//	                    		   //System.out.println("接口同步 删除oaMessage是否成功："+r);
//	                    		   if (r > 0 ) {
//	                    			   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
//	                    		   } else {
//	                    			   rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//	                    		   }
//	                    		   
//	                    	   } else {
//	                    		   
//	                    	   }
//	                    	 
//	                       } catch (SQLException ex) {
//	                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//	                           ex.printStackTrace();
//	                           return ;
//	                       }
//	                   }
//	               });
//	               return rs.getRetCode();
//	           }
//	    	   });
//	    	   rs.setRetCode(retCode) ;
//	      }
//	      return rs;
//		
//	}
//
//   	/**
//   	 * mj查询得到所有的系统用户
//     */
//	 public List<EmployeeBean> getAllSysEmp() {
//			//创建参数
//			List<String> param = new ArrayList<String>();
//			String hql = " select bean from EmployeeBean bean where bean.openFlag= '1' and bean.statusId= '0' and defsys != ''";
//			//调用list返回结果
//			Result rs = list(hql, param);
//			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				return (List<EmployeeBean>) rs.getRetVal();
//			} else {
//				return new ArrayList<EmployeeBean>();
//			}
//		}
//	  /**
//	    * 得到登陆者未查看过的消息 mj
//	    * @param loginId String
//	    * @return Result
//	    */
//	 public Result getCurrNoReadMsg(String loginId){
//	       String sql="select bean.id,bean.content,bean.send,bean.receive  from MessageBean bean where bean.receive=? and bean.status=?   order by bean.createTime desc";
//	       ArrayList param=new ArrayList();
//	       System.out.println(loginId);
//	       param.add(loginId);
//	       param.add("noRead");
//	       Result rs=this.list(sql,param);
//	       return rs;
//	 }
}