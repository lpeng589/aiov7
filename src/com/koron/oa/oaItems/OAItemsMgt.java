package com.koron.oa.oaItems;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAItemsBean;
import com.koron.oa.bean.OATaskBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>Title:我的项目数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/2
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OAItemsMgt extends AIODBManager{

	public Result itemsQuery(String condition,int pageNo,int pageSize){
		
		String hql = "FROM OAItemsBean WHERE 1=1 "+condition+" ORDER BY lastUpdateTime desc ";
		System.out.println(hql);
		return this.list(hql, new ArrayList(), pageNo, pageSize, true);
	}
	
	 
	/**
	 * 加载OAItemBean
	 * @param id
	 * @return
	 */
	public Result loadOAItem(String id){
		return loadBean(id, OAItemsBean.class);
	}
	
	/**
	 * 添加OAItemBean
	 * @param bean
	 * @return
	 */
	public Result addOAItem(final OAItemsBean bean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				addBean(bean,session);
				session.flush();//表示强制处理addBean().不然下面UPDATE无效
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "";
							//添加时默认添加任务编号(最大值+1)
							sql = "UPDATE OAItems SET itemOrder = ((SELECT MAX(itemOrder) FROM OAItems)+1) WHERE id=?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1,bean.getId());
							pstmt.executeUpdate();
						} catch (SQLException ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OATaskMgt---addChildTask method :" + ex);
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
	 * 更新OAItemBean
	 * @param bean
	 * @return
	 */
	public Result updateOAItem(OAItemsBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 删除OAItemBean
	 * @param moduleId
	 * @return
	 */
	public Result delItem(final String itemId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "";
							Statement stmt = connection.createStatement();
							
							sql = "DELETE FROM tblAdvice WHERE relationId in (SELECT id FROM OATask WHERE itemId ='"+itemId+"')";//删除项目下所有任务的通知消息
							stmt.addBatch(sql);
							
							sql = "DELETE FROM tblAdvice WHERE relationId='"+itemId+"'";//删除项目的通知消息
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OATaskLog WHERE f_ref in (SELECT id FROM OATask WHERE ItemId='"+itemId+"')";//删除任务的进展
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OATask WHERE itemId='"+itemId+"'";//删除项目下的任务
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OAItemsLog WHERE f_ref='"+itemId+"'";//删除项目进展
							stmt.addBatch(sql);
							
							sql = "DELETE FROM OAItems WHERE id='"+itemId+"'";//删除项目
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
	 * 添加OAItemBean
	 * @param bean
	 * @return
	 */
	public Result addAlertBean(AlertBean alertBean){
		return addBean(alertBean);
	}
	
	/**
	 * 删除OAItemBean
	 * @param moduleId
	 * @return
	 */
	public Result addWarn(final AlertBean alertBean ,final String itemId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				
				addBean(alertBean, session);
				
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "UPDATE OAItems SET nextAlertTime = ? WHERE id = ?";
							PreparedStatement pStmt = connection.prepareStatement(sql);
							pStmt.setString(1,alertBean.getNextAlertTime());
							pStmt.setString(2,itemId);
							pStmt.executeUpdate();
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
	 * 删除OAItemBean
	 * @param moduleId
	 * @return
	 */
	public Result delParticipant(final String itemId,final String employeeId,final String participants){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//update参与人
							String sql = "UPDATE OAItems SET participant = ? where id =?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1, participants);
							pstmt.setString(2, itemId);
							pstmt.executeUpdate();
							
							//删除参与人通知消息
							sql = "DELETE FROM tblAdvice WHERE relationId=? and Receive =?";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1, itemId);
							pstmt1.setString(2, employeeId);
							pstmt1.executeUpdate();
							
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
	 * 查询当前用户能选择的项目
	 * @param userId
	 * @param isNeedStatus 是否需要查询状态
	 * @return
	 */
	public ArrayList<Object> queryItemsByUserId(String userId,Boolean isNeedStatus){
		String itemSql ="SELECT id,title,endTime FROM OAItems WHERE (executor = ? or ','+participant like ?) ";
		if(isNeedStatus){
			itemSql +=" and status ='1' ";
		}
		itemSql +=" ORDER BY createTime DESC ";
		ArrayList param = new ArrayList();
		param.add(userId);
		param.add("%," + userId + ",%");
		Result rs = this.publicSqlQuery(itemSql,param);
		ArrayList<Object> itemList = (ArrayList<Object>)rs.retVal;
		return itemList;
	}
	
	/**
	 * 更新参与人
	 * @param employeeIds  参与人
	 * @param loginBean
	 * @param taskId 任务ID
	 * @return
	 */
	public Result updateParticipants(String employeeIds,LoginBean loginBean,String itemId){
		String sql = "UPDATE OAItems SET  participant= isNull(participant,'')+'"+employeeIds+"',lastUpdateBy=?,lastUpdateTime=? WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(loginBean.getId());
		param.add(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		param.add(itemId);
		return operationSql(sql, param);
	}
	
	/**
	 * 删除提醒
	 * @param moduleId
	 * @return
	 */
	public Result delAlert(final String itemId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							//删除提醒
							String sql = "DELETE FROM tblAlert WHERE relationId=?";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1,itemId);
							pstmt.executeUpdate();
							
							
							//修改OAItemBEAN
							sql = "UPDATE OAItems SET nextAlertTime = '' WHERE id = ?";
							PreparedStatement pstmt1 = connection.prepareStatement(sql);
							pstmt1.setString(1,itemId);
							pstmt1.executeUpdate();
							
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
	 * 根据客户Id获取客户名称
	 */
	public String findClientNameById(String keyId){
		String clientName = "";
		if(keyId!=null && !"".equals(keyId)){
			List<String> param = new ArrayList<String>();
			String sql = "SELECT clientName FROM CRMClientInfo WHERE id=?";
			param.add(keyId);
			Result rs = this.sqlList(sql, param);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
				if(list!=null && list.size()>0){
					clientName = String.valueOf(GlobalsTool.get(list.get(0),0));
				}
			}
		}
		return clientName;
	}
}

