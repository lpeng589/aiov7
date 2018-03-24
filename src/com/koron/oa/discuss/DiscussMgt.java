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
 * <p>Title:ͨ���������ݿ������</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: �����п���������޹�˾
 * @Author ��ܿ�
 */
public class DiscussMgt extends AIODBManager{

	/**
	 * ����sql��ѯ
	 * @param sql
	 * @param param
	 * @return
	*/
	public Result publicSqlQuery(String sql,ArrayList param){
		return sqlList(sql,param);
	}
	
	
	/**
	 * ��������ID��ѯ���лظ�ID
	 * @param commentIds ����ID
	 * @param tableName �������
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
								String relationId = rss.getString("commentId");//����ID
								
								//һ���ظ���ʾ����Ϣ
								String[] str=new String[6];
								str[0]=rss.getString("id");//id
								str[1]=rss.getString("content");//����
								str[2]=rss.getString("createBy");//������
								str[3]=rss.getString("createTime");//����ʱ��
								str[4]=rss.getString("replyId");//�ظ������˵�ID
								str[5]=rss.getString("delFlag");//ɾ����־ 1:��ɾ�� 0:����ɾ��
								
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
	 * jdbc���ù��������޸�ɾ������
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
	 * ɾ�����ۻ�ظ�
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
	 * ��ȡ��ϵ��¼������
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
	 * ��ȡ�ͻ���Ϣ
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


