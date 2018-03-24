package com.menyi.aio.web.logManage;

import java.net.Inet4Address;
import java.sql.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.LoginLogBean;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:日志管理Mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2013-9-22
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class LogManageMgt extends AIODBManager{

	
	/**
	 * 查询登录日志的数据 表tblLoginLog
	 * @param searchForm
	 * @return
	 */
	public Result queryLoginLogList(final LoginlogSearchForm searchForm){
		String sql = "select bean.id,bean.userid,bean.createTime,bean.ip,bean.userName,bean.operation,bean.terminal,bean.opResult,bean.reason,ROW_NUMBER() over(order by bean.createTime desc) as row_id from tblLoginLog bean,tblEmployee a,tblDepartment c where 1=1 and a.id=bean.userId and a.DepartmentCode=c.classCode";
		
		//开始时间
		if(searchForm.getBeginTimeSearch() != null && !"".equals(searchForm.getBeginTimeSearch())) {
			sql += " and bean.createTime >= '"+searchForm.getBeginTimeSearch()+" 00:00:00'";
		}
		//结束时间
		if(searchForm.getEndTimeSearch() != null && !"".equals(searchForm.getEndTimeSearch())){
			sql += " and bean.createTime <= '"+searchForm.getEndTimeSearch()+" 23:59:59'";
	    }
		//用户Id
		if(searchForm.getSearchUserId() != null && !"".equals(searchForm.getSearchUserId())){
			sql += " and bean.userId='"+searchForm.getSearchUserId()+"'";
		}
		//用户名称
		if(searchForm.getSearchUserName() != null && !"".equals(searchForm.getSearchUserName())){
			sql += " and (a.empFullName like '%"+searchForm.getSearchUserName()+"%'";
			sql += " or a.empFullNamePYM like '%"+searchForm.getSearchUserName()+"%')";
		}
		//操作（登入，登出）
		if(searchForm.getSearchOperation() != null && !"".equals(searchForm.getSearchOperation())){
			sql += " and bean.operation='"+searchForm.getSearchOperation()+"'";
		}
		//终端
		if(searchForm.getSearchTerminal() != null && !"".equals(searchForm.getSearchTerminal())){
			sql += " and bean.terminal like '%"+searchForm.getSearchTerminal()+"%'";
		}
		//备注
		if(searchForm.getSearchReason() != null && !"".equals(searchForm.getSearchReason())){
			sql += " and bean.reason like '%"+searchForm.getSearchReason()+"%'";
		}
		//部门查询
		if(searchForm.getSearchDeptName()!=null&&!"".equals(searchForm.getSearchDeptName())){
			sql+="  and c.DeptFullName like '%"+searchForm.getSearchDeptName()+"%'";
		}
		//时间类型
		if(searchForm.getSearchDateType() != 0){
			switch (searchForm.getSearchDateType()) {
				case 1: // 一天以内
					sql += " and DateDiff(day,bean.createTime,getdate())=0 ";
					break;
				case 2: // 一周以内
					sql += " and DateDiff(day,bean.createTime,getdate())<=6 ";
					break;
				case 3: // 一个月以内
					sql += " and DateDiff(day,bean.createTime,getdate())<=30 ";
					break;
				case 4: // 三个月以内
					sql += " and DateDiff(month,bean.createTime,getdate())<=3";
					break;
				case 5: // 三个月以外
					sql += " and DateDiff(month,bean.createTime,getdate())>3 ";
					break;
				default:
			}
		}
		BaseEnv.log.debug("登陆日志查询语句："+sql);
		return sqlListMaps(sql, null, searchForm.getPageNo(), searchForm.getPageSize());
	}
	
	/**
	 * 操作日志查询
	 * @param searchForm
	 * @return
	 */
	public Result queryLogList(final LogSearchForm searchForm){
		
		String sql = "select bean.id,bean.userid,bean.billType,bean.operation,bean.operationTime,bean.userName,bean.billTypeName,bean.content,bean.extendFun,ROW_NUMBER() over(order by bean.operationTime desc) as row_id from tblLog bean,tblEmployee a where 1=1 and a.id=bean.userId";
		
		//开始时间
		if(searchForm.getBeginTimeSearch() != null && !"".equals(searchForm.getBeginTimeSearch())) {
			sql += " and bean.operationTime >= '"+searchForm.getBeginTimeSearch()+" 00:00:00'";
		}
		//结束时间
		if(searchForm.getEndTimeSearch() != null && !"".equals(searchForm.getEndTimeSearch())){
			sql += " and bean.operationTime <= '"+searchForm.getEndTimeSearch()+" 23:59:59'";
	    }
		//用户
		if(searchForm.getSearchUserId() != null && !"".equals(searchForm.getSearchUserId())){
			sql += " and bean.userId='"+searchForm.getSearchUserId()+"'";
		}
		//用户名称
		if(searchForm.getSearchUserName() != null && !"".equals(searchForm.getSearchUserName())){
			sql += " and (a.empFullName like '%"+searchForm.getSearchUserName()+"%'";
			sql += " or a.empFullNamePYM like '%"+searchForm.getSearchUserName()+"%')";
		}
		//操作
		if(searchForm.getSearchOperation() != null && !"".equals(searchForm.getSearchOperation())){
			sql += " and bean.operation='"+searchForm.getSearchOperation()+"'";
		}
		//单据类型和名称
		if(searchForm.getSearchBill() != null && !"".equals(searchForm.getSearchBill())){
			sql += " and (bean.billTypeName like '%"+searchForm.getSearchBill()+"%' or bean.billType like '%"+searchForm.getSearchBill()+"%')";
		}
		
		//内容
		if(searchForm.getSearchContent() != null && !"".equals(searchForm.getSearchContent())){
			sql += " and bean.content like '%"+searchForm.getSearchContent()+"%'";
		}
		//时间类型（按特定的时间段查询）
		if(searchForm.getSearchDateType() != 0){
			switch (searchForm.getSearchDateType()) {
				case 1: // 一天以内
					sql += " and DateDiff(day,bean.operationTime,getdate())=0 ";
					break;
				case 2: // 一周以内
					sql += " and DateDiff(day,bean.operationTime,getdate())<=6 ";
					break;
				case 3: // 一个月以内
					sql += " and DateDiff(day,bean.operationTime,getdate())<=30 ";
					break;
				case 4: // 三个月以内
					sql += " and DateDiff(month,bean.operationTime,getdate())<=3";
					break;
				case 5: // 三个月以外
					sql += " and DateDiff(month,bean.operationTime,getdate())>3 ";
					break;
				default:
			}
		}
		return sqlListMaps(sql, null, searchForm.getPageNo(), searchForm.getPageSize());
	}
	
	
	
	/**
	 * 增加
	 * @param userId
	 * @param userName
	 * @param operation
	 * @param terminal 终端如果为空，则直接从request中读取，
	 * @param opResult
	 * @param reason
	 * @param request
	 * @return
	 */
	public Result addLog(String userId,String userName,String operation,String terminal,String opResult,String reason,HttpServletRequest request){
		LoginLogBean bean = new LoginLogBean();
		bean.setId(IDGenerater.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        bean.setUserId(userId);
        bean.setUserName(userName);
        String ips = request.getRemoteAddr(); 
        String a = "";
		try {
			if ("127.0.0.1".equals(ips)) {// 如果是在本机，改成实际的IP
				a = Inet4Address.getLocalHost().toString();
				ips = a.substring(a.indexOf('/') + 1);
			}
		} catch (Exception e1) {
		}
        bean.setIp(ips);
        
        bean.setOperation(operation);
        bean.setOpResult(opResult);
        bean.setReason(reason);
        
        if(terminal==null || terminal.length() ==0){
        	//
        	String user_agent = request.getHeader("user-agent");
        	//检查是否IE
        	Pattern pattern = Pattern.compile("MSIE ([\\d]*[\\.]{0,1}[\\d]*)");
        	Matcher matcher = pattern.matcher(user_agent);	
	        if (matcher.find()){
	        	bean.setTerminal("IE"+matcher.group(1));
	        	return addBean(bean);
        	}
	        //Firefox
	        pattern = Pattern.compile("Firefox/([\\d]*[\\.]{0,1}[\\d]*)");
        	matcher = pattern.matcher(user_agent);	
	        if (matcher.find()){
	        	bean.setTerminal("Firefox"+matcher.group(1));
	        	return addBean(bean);
        	}
	        //Opera
	        pattern = Pattern.compile("Opera[/|\\s]([\\d]*[\\.]{0,1}[\\d]*)");
        	matcher = pattern.matcher(user_agent);	
	        if (matcher.find()){
	        	bean.setTerminal("Opera"+matcher.group(1));
	        	return addBean(bean);
        	}
	        //Chrome
	        pattern = Pattern.compile("Chrome/([\\d]*[\\.]{0,1}[\\d]*)");
        	matcher = pattern.matcher(user_agent);	
	        if (matcher.find()){
	        	bean.setTerminal("Chrome"+matcher.group(1));
	        	return addBean(bean);
        	}
	        //Safari //Mozilla/5.0 (iPhone; U; CPU like Mac OS X) AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A93 Safari/419.3
	        pattern = Pattern.compile("Version/([\\d]*[\\.]{0,1}[\\d]*).*Safari/");
        	matcher = pattern.matcher(user_agent);	
	        if (matcher.find()){
	        	bean.setTerminal("Safari"+matcher.group(1));
	        	return addBean(bean);
        	}	   
	        bean.setTerminal(user_agent);
        	return addBean(bean);
        }else{
        	bean.setTerminal(terminal);
        	return addBean(bean);
        }
	}
	
	/**
	 * 删除登录日志（删除30天前的记录）
	 * @return
	 */
	public Result deleteLoginLog(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("delete tblLoginLog where DateDiff(day,createTime,getdate())>30");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("LogManageMgt deleteLoginLog:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
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
	 * 删除操作日志（删除30天前的记录）
	 * @return
	 */
	public Result deleteLog(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("delete tblLog where DateDiff(day,operationTime,getdate())>30");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.executeUpdate();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("LogManageMgt deleteLog:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
}
