package com.koron.crm.brotherSetting;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.BrotherFieldDisplayBean;
import com.koron.crm.bean.BrotherFieldScopeBean;
import com.koron.crm.bean.BrotherPublicScopeBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;


/**
 * 
 * 
 * <p>Title:CRM兄弟表管理MGT</p> 
 * <p>Description: </p>
 *
 * @Date:2013-7-31
 * @Copyright: 科荣软件
 * @Author 徐洁俊
 */
public class CRMBrotherSettingMgt extends AIODBManager{
	
	/**
	 * 根据ID查找字段显示BEAN
	 * @param id
	 * @return
	 */
	public Result loadBrotherFieldDisplayBean(String id){
		return this.loadBean(id, BrotherFieldDisplayBean.class);
	}
	
	/**
	 * 新增字段显示
	 * @param id
	 * @return
	 */
	public Result addBrotherShowFieldsBean(BrotherFieldDisplayBean bean){
		return this.addBean(bean);
	}
	
	/**
	 * 更新字段显示
	 * @param id
	 * @return
	 */
	public Result updateBrotherShowFieldsBean(BrotherFieldDisplayBean bean){
		return this.updateBean(bean);
	}
	
	
	
	/**
	 * 查找字段权限控制Bean
	 * @param id
	 * @return
	 */
	public Result loadFieldScopeBean(String keyId){
		return this.loadBean(keyId, BrotherFieldScopeBean.class);
	}
	
	/**
	 * 添加字段权限控制Bean
	 * @param id
	 * @return
	 */
	public Result addFieldScopeBean(BrotherFieldScopeBean bean){
		return this.addBean(bean);
	}
	
	/**
	 * 删除字段权限控制Bean
	 * @param id
	 * @return
	 */
	public Result delFieldScopeBean(String[] keyIds){
		return deleteBean(keyIds, BrotherFieldScopeBean.class, "id");
	}
	
	/**
	 * 更新字段权限控制Bean
	 * @param id
	 * @return
	 */
	public Result updateFieldScopeBean(BrotherFieldScopeBean bean){
		return this.updateBean(bean);
	}
	
	/**
	 * 根据表名查找字段权限控制List
	 * @param id
	 * @return
	 */
	public Result fieldScopeQuery(String tableName){
		ArrayList param = new ArrayList();
		String hql = "from BrotherFieldScopeBean where tableName =? order by createTime desc";
		param.add(tableName);
		return this.list(hql, param);
	}
	
	/**
	 * 查找导入导出打印设置Bean
	 * @param tableName 表名
	 * @param scopeName 权限名称 import:导入 export：导出 print:打印
	 * @return
	 */
	public Result loadPublicScopeBean(String tableName,String scopeName){
		ArrayList param = new ArrayList();
		String hql = "FROM BrotherPublicScopeBean WHERE tableName=? and scopeName =?";
		param.add(tableName);
		param.add(scopeName);
		return this.list(hql,param);
	}
	
	/**
	 * 添加公共权限
	 * @param publicScopeBean
	 * @return
	 */
	public Result addPublicScope(final String tableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							
							String sql = "insert into CRMBrotherPublicScope(id,scopeFlag,scopeName,tableName,createBy,createTime,lastUpdateBy,lastUpdateTime) values(?,?,?,?,?,?,?,?)";
							PreparedStatement ps = connection.prepareStatement(sql);
							String[] scopeNames = {"import","export","print"};
							String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
							for(String scopeName : scopeNames){
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, "0");
								ps.setString(3, scopeName);
								ps.setString(4, tableName);
								ps.setString(5, "1");
								ps.setString(6, nowDate);
								ps.setString(7, "1");
								ps.setString(8, nowDate);
								ps.addBatch();
							}
							ps.executeBatch();
						} catch (SQLException ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("CRMBrotherSettingMgt---addPublicScope method :" + ex);
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
	 * 更新公共权限
	 * @param publicScopeBean
	 * @return
	 */
	public Result updatePublicScope(final HashMap<String,String[]> scopeMap,final String tableName,final LoginBean loginBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "UPDATE CRMBrotherPublicScope SET scopeFlag=?,deptIds=?,userIds=?,groupIds=?,lastUpdateBy=?,lastUpdateTime=? WHERE tableName=? and scopeName=?";
							PreparedStatement ps = connection.prepareStatement(sql);

							Set keys = scopeMap.keySet();
							if(keys != null) {
								Iterator iterator = keys.iterator();
								while(iterator.hasNext( )) {
									Object key = iterator.next();
									ps.setString(1, scopeMap.get(key.toString())[0]);
									if("0".equals(scopeMap.get(key.toString())[0])){
										//全部可看到将控制设为空
										ps.setString(2,"");
										ps.setString(3,"");
										ps.setString(4,"");
									}else{
										ps.setString(2, scopeMap.get(key.toString())[1]);
										ps.setString(3, scopeMap.get(key.toString())[2]);
										ps.setString(4, scopeMap.get(key.toString())[3]);
									}
									ps.setString(5, loginBean.getId());
									ps.setString(6, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
									ps.setString(7, tableName);
									ps.setString(8, key.toString());
									ps.addBatch();
								}
							}
							ps.executeBatch();
						} catch (SQLException ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("CRMBrotherSettingMgt---updatePublicScope method :" + ex);
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
	 * 根据ids查找分组中文
	 * @param groupIds
	 * @return
	 */
	public Result groupNameQueryByIds(String groupIds){
		
		String ids = "";
		for(String str : groupIds.split(",")){
			ids +="'"+str+"',";
		}
		if(ids.endsWith(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		
		ArrayList param = new ArrayList();
		String sql = "SELECT id,groupName FROM tblEmpGroup WHERE id in("+ids+")";
		return this.sqlList(sql,param);
	}
	
	/**
	 * 更新字段维护
	 * @param publicScopeBean
	 * @return
	 */
	public Result updateFieldMaintain(final ArrayList<String> updateSqlList,final ArrayList<DBFieldInfoBean> saveBeanList,final ArrayList<String> languageList,final ArrayList<String> statusStopSqlList) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				for(DBFieldInfoBean bean : saveBeanList){
					addBean(bean,session);
				}
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Statement state = connection.createStatement();
							
							for(String sql : updateSqlList){
								state.addBatch(sql);
								System.out.println(sql);
							}
							
							//插入多语言,物料表等语句
							for(String str : languageList){
								state.addBatch(str);
								System.out.println(str);
							}
							
							for(String str : statusStopSqlList){
								state.addBatch(str);
								System.out.println(str);
							}
							
							int[] a = state.executeBatch();
							for(int i=0;i<a.length;i++){
								System.out.println("aaaaaaaaa:"+a[i]);
							}
						} catch (SQLException ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("CRMBrotherSettingMgt---updatePublicScope method :" + ex);
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
	 * 根据枚举名称查找关联客户下拉框
	 * @param enumerName 枚举名称
	 * @return
	 */
    public Result relateClientQuery(String enumerName){
    	ArrayList param = new ArrayList();
    	String sql = "SELECT name,value FROM tblRelateClientEnumer WHERE enumerName = ?";
    	param.add(enumerName);
    	return this.sqlList(sql, param);
    }
	
    /**
     * 检测是否有该字段
     * @param colName
     * @param tableName
     * @return
     */
    public int checkCols(String colName,String tableName){
		String sql ="select count(*) from sys.columns where object_id in (select object_id from sys.tables where name='"+tableName+"') and (name like '"+colName+"%')";
		Result result =  this.sqlList(sql, new ArrayList());
		int colNum = 0;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			colNum = Integer.valueOf(((Object[])((List<Object>)result.retVal).get(0))[0].toString());
		}
		return colNum;
	}
    
    /**
     * 删除新增字段
     * @param sqlList
     * @return
     */
    public Result delField(final ArrayList<String> sqlList) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Statement state = connection.createStatement();
							for(String sql : sqlList){
								state.addBatch(sql);
							}
							state.executeBatch();
						} catch (SQLException ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("CRMBrotherSettingMgt---delField method :" + ex);
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
	 * 获取客户的导入，导出，打印的权限设置
	 * @return
	 */
	
	public String getPublicScope(LoginBean loginBean,String tableName){
		List<String> param=new ArrayList<String>();
		String sql="select scopeName from CRMBrotherPublicScope  where tableName=?"; 
		param.add(tableName);
		if(!"1".equals(loginBean.getId())){
			sql += " and (scopeFlag='0' or dbo.exist_dept(deptIds,?)='true' or ','+userIds like ? or ','+groupIds like ? ) ORDER BY id  DESC ";
			param.add("" + loginBean.getDepartCode()+ "");
			param.add("%," + loginBean.getId()+ ",%");
			String group=" ";
			if(!"".equals(loginBean.getGroupId()))
				group=loginBean.getGroupId();
			param.add("%," + group+ ",%");
		}
		Result rs = this.sqlList(sql, param);
		
		String retStr = "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				for(Object obj : list){
					retStr += GlobalsTool.get(obj,0)+",";
				}
			}
		}
		
		return retStr;
	}
    
}
