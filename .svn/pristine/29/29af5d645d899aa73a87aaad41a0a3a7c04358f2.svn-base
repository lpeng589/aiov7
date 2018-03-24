package com.koron.oa.oaCollection;

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
import com.koron.oa.toDo.OAToDoBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class OACollectionMgt extends AIODBManager{
	
	/**
	 * 外部添加接口
	 * @param title 内容
	 * @param type 分类
	 * @param url 链接
	 * @param empId 创建人
	 * @param relationId 关联Id
	 * @return
	 */
	public Result addCollection(final String title,final String type,
			final String url,final String createBy,final String relationId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {
                    		//判断是否为空
                    		int num=0;
                    		
                			String sql = "select count(*) as idSum from tblattention where oTopicId = ? and empId=? and type=?";
            		  		PreparedStatement ps = conn.prepareStatement(sql);	
            		  		ps.setString(1, relationId);
            		  		ps.setString(2, createBy);
            		  		ps.setString(3, type);
            		  		ResultSet rss = ps.executeQuery() ;
                  		  	if(rss.next()){
                    			num = rss.getInt("idSum");
                    		}
                  		    if(num<=0){
                  		    	OACollectionBean bean = new OACollectionBean();
                      			bean.setTitle(title);
                      			bean.setType(type);
                      			bean.setUrl(url);
                      			bean.setEmpId(createBy);
                      			bean.setOTopicId(relationId);
                      			bean.setId(IDGenerater.getId());
                      			//bean.setFinishTime("");
                      			bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                      			bean.setCreateBy(createBy);
                      			bean.setLastUpdateBy(createBy);
                      			bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                      			addBean(bean,session); 
                  		    }                  			               		
              		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("OACollectionMgt addCollection : ", ex) ;
        				}
                    }
                });				
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;	
	}
	/***
	 * 外部删除
	 * @param oTopicId
	 * @param loginId
	 * @return
	 */
	public Result outDelCollection(final String oTopicId,final String loginId,final String tableName){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {                  		                   		
                    		String sql="delete from tblAttention where OTopicId=? and empId=? and type=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, oTopicId);
							pss.setString(2, loginId);
							pss.setString(3, tableName);	
							pss.executeUpdate();
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("OACollectionMgt outDelCollection : ", ex) ;
        				}
                    }
                });				
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;			
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Result delCollection(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {    
                    		//取消邮件收藏
                    		OACollectionBean bean = (OACollectionBean)loadBean(id, OACollectionBean.class, session).retVal;
                    		String sql="update OAMailInfo set collectionType = '0' where id=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, bean.getOTopicId());							
							pss.executeUpdate();
							deleteBean(id, OACollectionBean.class, "id",session);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("OACollectionMgt outDelCollection : ", ex) ;
        				}
                    }
                });				
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	

	/**
	 * 跟新FinishTime
	 * @param id
	 * @return
	 */
	public Result upCollection(OACollectionBean bean){
		return updateBean(bean);
	}
	
	public Result loadCollection(String id){
		return loadBean(id, OACollectionBean.class);
	}
	
	/**
	 * 查询
	 * @param loginId
	 * @param lvForm
	 * @return
	 */
	public Result queryCollection(String loginId,OACollectionForm lvForm){
		String sql = "from OACollectionBean where empId=?";
		ArrayList param = new ArrayList();
		param.add(loginId);
		if(lvForm.getContext() !=null && !"".equals(lvForm.getContext())){
			sql += " and title like '%"+lvForm.getContext()+"%'";			
		}
		if(lvForm.getTabFlag() !=null && !"".equals(lvForm.getTabFlag())){
			String[] types = lvForm.getTabFlag().split(";");
			sql += " and type in(";
			for (String key : types) {
				sql +="'"+key+"',";
			}
			sql += "'')";
		}
		sql += " order by createTime desc";
		System.out.println(sql);
		return list(sql, param, lvForm.getPageNo(), 15, true);
	}
	
	
	public Result queryType(){
		String sql = "select type,count(type) from tblattention where 1=? GROUP BY  type";
		ArrayList param = new ArrayList();
		param.add("1");		
		return sqlList(sql, param);
	}
	
	
	public int queryCount(String loginId,OACollectionForm lvForm){
		String sql = "select count(id) from tblattention where empId = ? ";
		ArrayList param = new ArrayList();
		param.add(loginId);	
		if(lvForm.getContext() !=null && !"".equals(lvForm.getContext())){
			sql += " and title like ?";
			param.add("'%"+lvForm.getContext()+"%'");
		}
		if(lvForm.getTabFlag() !=null && !"".equals(lvForm.getTabFlag())){
			sql += " and type = ?";
			param.add(lvForm.getTabFlag());
		}
		Result rs = sqlList(sql, param);
		ArrayList res = (ArrayList)rs.retVal;
		if(res !=null && res.size()>0){
			String mun = ((Object[])res.get(0))[0].toString();
			return Integer.parseInt(mun);
		}else{
			return 0;
		}
	}
	
	/**
	 * 工作台调
	 * @param loginId
	 * @param lvForm
	 * @return
	 */
	public Result oaMydsCollection(String loginId,String title){
		String sql = "from OACollectionBean where empId=?";
		ArrayList param = new ArrayList();
		param.add(loginId);
		if(title !=null && !"".equals(title)){
			sql += " and title like ?";
			param.add("'%"+title+"%'");
		}		
		sql += " order by createTime desc";		
		return list(sql, param, 1, 15, true);
	}

}