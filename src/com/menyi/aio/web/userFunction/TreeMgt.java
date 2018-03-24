package com.menyi.aio.web.userFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.KRLanguageQuery;
import com.dbfactory.Result;

public class TreeMgt extends AIODBManager {
	
	public Result finish(final String tableName,final String keyId) {
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "update "+tableName + " set workflowNode='-1' ,workflowNodeName='finish' where id=? ";
							
							PreparedStatement pstmt = connection.prepareStatement(sql);
							pstmt.setString(1, keyId);
							pstmt.executeUpdate();
						} catch (Exception ex) {
							BaseEnv.log.error("TreeMgt.update Error ",ex);							
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}

	public Result update(final String tableName,final String classCode,final String nameField, final String numberField,
			final String nameFieldName, final String numberFieldName) {
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							DBFieldInfoBean nameF = GlobalsTool.getFieldBean(tableName, nameFieldName);
							if(nameF.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE){
								
								if(numberFieldName != null && numberFieldName.length() > 0){
									String sql = "update "+tableName + " set "+numberFieldName+"='"+numberField+"'";
									sql +=" where classCode='"+classCode+"'";
									PreparedStatement pstmt = connection.prepareStatement(sql);
									pstmt.executeUpdate();
								}
								String sql = "update tbllanguage set zh_CN='"+nameField+"'";
								sql +=" where id in (select "+nameFieldName+" from "+tableName + " where  classCode='"+classCode+"' )";
								PreparedStatement pstmt = connection.prepareStatement(sql);
								pstmt.executeUpdate();
								
							}else{
								
								
								String sql = "update "+tableName + " set "+nameFieldName+"='"+nameField+"'";
								if(numberFieldName != null && numberFieldName.length() > 0){
									sql +=","+numberFieldName+"='"+numberField+"'";
								}
								sql +=" where classCode='"+classCode+"'";
								PreparedStatement pstmt = connection.prepareStatement(sql);
								pstmt.executeUpdate();
							}
						} catch (Exception ex) {
							BaseEnv.log.error("TreeMgt.update Error ",ex);							
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}
	
	public Result delete(final String tableName,final String classCode) {
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "select count(0) from "+tableName +" where classCode='"+classCode+"'";
							PreparedStatement pstmt = connection.prepareStatement(sql);
							ResultSet rset = pstmt.executeQuery();
							if(rset.next()){
								int count = rset.getInt(1);
								if(count ==0){
									rs.retCode = ErrorCanst.ER_NO_DATA;
									rs.retVal = "数据不存在";
									return;
								}
							}
							sql = "select count(0) from "+tableName +" where classCode like '"+classCode+"_____'";
							pstmt = connection.prepareStatement(sql);
							rset = pstmt.executeQuery();
							if(rset.next()){
								int count = rset.getInt(1);
								if(count >0){
									rs.retCode = ErrorCanst.DATA_ALREADY_USED;
									rs.retVal = "存在下级数据，不可删除";
									return;
								}
							}
							sql =" delete "+tableName+" where classCode='"+classCode+"'";
							pstmt = connection.prepareStatement(sql);
							pstmt.executeUpdate();
						} catch (Exception ex) {
							BaseEnv.log.error("TreeMgt.delete Error ",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		return rs;
	}

	public Result getTree(String tableName, String moduleType, String SysType, String nameField, String numberField,boolean isLangauge, String userId,String subSql, ArrayList scopeRight) {
		String sql = "";
		if (tableName.equals("tblDepartment")) {
			//查询部门
			sql = " select classCode," + nameField + "," + numberField + ",id from " + tableName + " where statusId =0 "+(subSql != null && subSql.length() > 0?" and ("+subSql+") ":"");
			//有部门管辖范围的，取范围内数据.
			/*
			 boolean allDept = false;
			 String deptStr = "";
			 for (Object o : scopeRight) {
			 LoginScopeBean lsb = (LoginScopeBean) o;
			 if ("5".equals(lsb.getFlag())) {
			 if (lsb.getScopeValue().equals("ALL")) {
			 //设置有查看全公单据的权限,清空所有权限控制代码，退出
			 allDept = true;
			 break;
			 } else {
			 for (String sc : lsb.getScopeValue().split(";")) {
			 if (sc.length() > 0) {
			 deptStr += " OR CLASSCODE LIKE '" + sc + "%' ";
			 }
			 }
			 }
			 }
			 }
			 if(!allDept &&  deptStr.length() > 0){
			 sql += " AND ("+deptStr.substring(3)+")";
			 }  */
			sql += " order by "+(numberField==null||numberField.length()==0||numberField.equals("''")?nameField:numberField);
		} else if (tableName.equals("tblEmployee")) {
			sql = " select classCode,deptFullName,DeptCode,id from tblDepartment where statusId =0 order by DeptCode";
			Result deptRs = sqlList(sql, new ArrayList());
			if (deptRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				sql = " select id,empFullName,DepartmentCode,EmpNumber,id from tblEmployee where statusid=0  order by EmpNumber";
				Result empRs = sqlList(sql, new ArrayList());
				if (empRs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					Result rs = new Result();
					rs.retVal = new Object[] { deptRs.retVal, empRs.retVal };
					return rs;
				} else {
					return empRs;
				}
			} else {
				return deptRs;
			}

		} else if (tableName.equals("tblModules")) {
			sql = "select classCode,zh_CN," + numberField + ","+tableName+".id from "+tableName+" left join tblLanguage b " +
			" on "+tableName+"."+nameField+"=b.id where isCatalog=1 "+(subSql != null && subSql.length() > 0?" and ("+subSql+") ":"")+" order by OrderBy ";
		} else if (isLangauge) {
			String right = rightHandler(scopeRight,tableName);
			sql = "select classCode,zh_CN," + numberField + ","+tableName+".id from "+tableName+"  join tblLanguage b " +
					" on "+tableName+"."+nameField+"=b.id where statusId =0 "+(right.length() > 0?" AND "+right:"")+" and isCatalog=1 "+(subSql != null && subSql.length() > 0?" and ("+subSql+") ":"")+" order by "+(numberField==null||numberField.length()==0||numberField.equals("''")?nameField:numberField);
		} else {
			String right = rightHandler(scopeRight,tableName);
			sql = " select classCode," + nameField + "," + numberField + ",id from " + tableName + " where statusId =0 "+(right.length() > 0?" AND "+right:"")+"  and isCatalog=1 ";
			if ("tblCompany".equals(tableName)) {
				if ("Customer".equals(SysType)) {
					sql += " and moduleType in ('2','3') ";
				} else if ("Supplier".equals(SysType)) {
					sql += " and moduleType in ('1','3') ";
				}
			}

			/* 这段代码暂时屏蔽，因为象收款单会带有moduleType =xxx,这会导致收款单的moduleType传入到这个客户选择框中引起出错，
			 * 目前暂不明朗哪些地方需要用到这个moduleType参数，暂时屏蔽这段代码，代以后发现问题再做考虑 
			 * 这段代码曾经被屏蔽，导致的问题是客户，供应商强出窗不过滤类型，显示所有的了，但是上面的情况反而不存在 */
			DBFieldInfoBean dfb = GlobalsTool.getFieldBean(tableName, "moduleType");
			if (dfb != null && moduleType != null && moduleType.length() > 0) {
				sql += " and moduleType='" + moduleType + "' ";
			} 
			sql +=(subSql != null && subSql.length() > 0?" and ("+subSql+") ":"");
			sql += "order by "+(numberField==null||numberField.length()==0||numberField.equals("''") ?nameField:numberField);
		}
		
		BaseEnv.log.debug("TreeMgt.getTree sql=" + sql);
		return sqlList(sql, new ArrayList());
	}


	public String rightHandler(ArrayList scopeRight,String tableName){
		LoginScopeBean newls = null;
		for (Object o : scopeRight) {
			LoginScopeBean lsb = (LoginScopeBean) o;
			if ("0".equals(lsb.getFlag())) {
				if(tableName.equals(lsb.getTableName())){				
					if (newls == null) {
						//因为接下下会有合并scopeValue的行为，所以这里必须克隆，而不能简单赋值
						newls = new LoginScopeBean();
						newls.setTableName(tableName);
						newls.setFlag(lsb.getFlag());
						newls.setId(lsb.getId());
						newls.setIsAllModules(lsb.getIsAllModules());
						newls.setRoleId(lsb.getRoleId());
						newls.setScopeValue(lsb.getScopeValue());
					} else {
						//合并
						newls.setScopeValue(newls.getScopeValue() + lsb.getScopeValue());
					}
				}
			}
		}
		if (newls != null) {
			String inS = "";
			String[] svs = newls.getScopeValue().split(";");
			if (svs != null) {
				for (String sv : svs) {
					inS += "OR " + tableName.toUpperCase() + ".CLASSCODE LIKE '" + sv + "%' ";
				}
			}
			if (inS.length() > 0) {
				inS = " ( LEN(ISNULL(" + tableName.toUpperCase() + ".CLASSCODE,''))=0 " + inS + ")";			
				return inS;
			}
			
		}
		return "";
	}	
}
