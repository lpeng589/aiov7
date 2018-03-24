package com.koron.oa.discuss;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OATaskBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>Title:通用评论数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class DiscussMgt extends AIODBManager{

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
	 * 根据评论ID查询所有回复ID
	 * @param commentIds 评论ID
	 * @param tableName 物理表名
	 * @return
	 */
	public Result queryReplyByCommentIds(final String commentIds,final String tableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							String sql = "SELECT id,f_ref,content,createBy,createTime,commentId,replyId,delFlag FROM "+tableName+" WHERE isNull(commentId,'') <> '' and commentId in ("+commentIds+") ORDER BY createTime ASC";
							
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							
							HashMap<String,ArrayList<String[]>> rsHashMap = new HashMap<String, ArrayList<String[]>>();
							while(rss.next()){
								String relationId = rss.getString("commentId");//评论ID
								
								//一个回复显示的信息
								String[] str=new String[6];
								str[0]=rss.getString("id");//id
								str[1]=rss.getString("content");//内容
								str[2]=rss.getString("createBy");//创建人
								str[3]=rss.getString("createTime");//创建时间
								str[4]=rss.getString("replyId");//回复评论人的ID
								str[5]=rss.getString("delFlag");//删除标志 1:不删除 0:可以删除
								
								if(rsHashMap.get(relationId)==null){
									ArrayList<String[]> list = new ArrayList<String[]>();
									list.add(str);
									rsHashMap.put(relationId, list);
								}else{
									rsHashMap.get(relationId).add(str);
								}
							}
							rst.setRetVal(rsHashMap) ;
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
	 * 删除评论或回复
	 * @param tableName
	 * @param keyId
	 * @return
	 */
	public Result delDiscuss(String tableName,String keyId){
		String sql = "DELETE FROM "+tableName+" WHERE id=?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		return this.operationSql(sql, param);
		
	}
	
	/**
	 * 获取联系记录创建人
	 * @param keyId
	 * @return
	 */
	public Object queryFollowUpCreateBy(String keyId){
		Object obj = null;
		String sql = "SELECT createBy,content FROM CRMSaleFollowUp WHERE id = ?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		
		Result rs = this.sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				obj = list.get(0);
			}
		}
		
		return obj;
	}
	
	/**
	 * 获取客户信息
	 * @param keyId
	 * @return
	 */
	public Object queryClientCreateBy(String keyId){
		Object obj = null;
		String sql = "SELECT createBy,clientName FROM CRMClientInfo WHERE id = ?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		
		Result rs = this.sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				obj = list.get(0);
			}
		}
		
		return obj;
	}
	
	
}


