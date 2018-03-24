package com.koron.crm.weixin.client;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.client.CRMClientMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

/**
 * 
 * <p>Title:我的项目数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2013/12/30
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class ClientMgt extends AIODBManager{
	
	/**
	 * 模糊查询
	 * @param keyWord
	 * @return
	 */
	public Result clientQuery(final String keyWord) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "SELECT TOP 5 id,clientName FROM CRMClientInfo WHERE clientName like ?";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, "%" + keyWord + "%");
							ResultSet rss = ps.executeQuery();
							ArrayList<String> list = new ArrayList<String>() ;
							while(rss.next()){
								list.add(rss.getString("id")+"\n"+rss.getString("clientName"));
							}
							rst.setRetVal(list) ;
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
	
	
	public Result followUpQuery(final String clientId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "SELECT TOP 2 content FROM CRMSaleFollowUp WHERE isNull(content,'') <> '' and ClientId=?";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, clientId);
							ResultSet rss = ps.executeQuery();
							ArrayList<String> list = new ArrayList<String>() ;
							while(rss.next()){
								list.add(rss.getString("content"));
							}
							rst.setRetVal(list) ;
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
	 * 是否存在客户
	 * @param clientId
	 * @return
	 */
	public boolean isExistClient(String clientId){
		boolean isExistClient = false;
		String sql = "SELECT id FROM CRMClientInfo WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(clientId);
		Result rs = sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			if(list!=null && list.size()>0){
				isExistClient = true;
			}
		}
		return isExistClient;
	}
	
	public Result addFollowUp(final String clientId,final String clientName,final boolean isExistClient,final String contents,final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							LoginBean loginBean = new LoginBean();
							loginBean.setId(userId);
							String keyId = clientId;
							String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
							if(!isExistClient){
								String deptCode = GlobalsTool.getDeptCodeByUserId(userId);
								String clientNo = BillNoManager.find("CRMClientInfo_ClientNo",loginBean,connection);	
								String sql = "INSERT INTO CRMCLientInfo(id,workFlowNode,workFlowNodeName,ClientName,ClientNo,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,SCompanyID,LastContractTime,finishTime,ModuleId,departmentCode) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
								PreparedStatement ps = connection.prepareStatement(sql);
								ps.setString(1, keyId);
								ps.setString(2, "-1");
								ps.setString(3, "finish");
								ps.setString(4, clientName+keyId);
								ps.setString(5, clientNo);
								ps.setString(6, "1");
								ps.setString(7, "1");
								ps.setString(8, nowTime);
								ps.setString(9, nowTime);
								ps.setString(10, "0");
								ps.setString(11, "00001");
								ps.setString(12, nowTime);
								ps.setString(13, nowTime);
								ps.setString(14, "weixinModuleId");
								ps.setString(15, deptCode);
								ps.executeUpdate();
							}
							
							String followNo = BillNoManager.find("CRMSaleFollowUp_FollowNo",loginBean,connection);
							String sql ="INSERT INTO CRMSaleFollowUp(id,f_brother,workFlowNode,workFlowNodeName,FollowNo,EmployeeId,VisitTime,Content,createBy,lastUpdateBy,createTime,lastUpdateTime,ClientId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
							PreparedStatement ps = connection.prepareStatement(sql) ;
							
							ps.setString(1, IDGenerater.getId());
							ps.setString(2, keyId);
							ps.setString(3, "-1");
							ps.setString(4, "finish");
							ps.setString(5, followNo);
							ps.setString(6, "1");
							ps.setString(7, nowTime);
							ps.setString(8, contents);
							ps.setString(9, "1");
							ps.setString(10, "1");
							ps.setString(11, nowTime);
							ps.setString(12, nowTime);
							ps.setString(13, keyId);
							ps.executeUpdate();
							
							new CRMClientMgt().insertCRMCLientInfoLog(keyId, "history", "新增了一条联系记录", "1",connection);
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
	 * 绑定客户
	 * @param openId
	 * @param clientId
	 * @return
	 */
	public Result bindClient(final String openId,final String clientId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							//更新联系记录
							String sql2 = "UPDATE CRMSaleFollowUp SET ClientId=?,f_brother=? WHERE ClientId=? or f_brother=?";
							PreparedStatement ps2 = connection.prepareStatement(sql2);
							ps2.setString(1, clientId);
							ps2.setString(2, clientId);
							ps2.setString(3, openId);
							ps2.setString(4, openId);
							ps2.executeUpdate();
							
							//更新客户名称已绑定
							String sql = "UPDATE CRMClientInfo SET ClientName=ClientName+'【已绑定】' WHERE id=?";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, openId);
							ps.executeUpdate();
							
							/*
							//删除微信客户
							String sql = "DELETE FROM CRMClientInfo WHERE id=?";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, openId);
							ps.executeUpdate();
							
							//删除微信客户的日志
							String sql1 = "DELETE FROM CRMClientInfoLog WHERE clientId=? or relationId=?";
							PreparedStatement ps1 = connection.prepareStatement(sql1);
							ps1.setString(1, openId);
							ps1.setString(2, openId);
							ps1.executeUpdate();
							*/
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
}

