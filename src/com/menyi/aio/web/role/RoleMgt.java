package com.menyi.aio.web.role;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbfactory.Result;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.aio.bean.ModuleBean;
import org.hibernate.Session;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.RoleScopeBean;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.menyi.web.util.BaseEnv;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import java.sql.PreparedStatement;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.RightType;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.bean.RoleModuleBean;
import com.menyi.aio.bean.ModuleOperationBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginScopeBean;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class RoleMgt extends AIODBManager {

	/**
	 * @param id long
	 * @param name String
	 * @return Result
	 */
	public Result add(RoleBean bean) {
		return addBean(bean);
	}

	//向tblRightType表中添加权限类型(允许负库存过账,允许超订单数出库,允许超订单数入库,允许少于成本价出库)
	public Result addRightType(final List<RightType> list) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "insert into tblRightType values(?,?,?)";
							PreparedStatement pst = conn.prepareStatement(sql);
							for (RightType rt : list) {
								pst.setString(1, rt.getId());
								pst.setString(2, rt.getRightType());
								pst.setString(3, rt.getHasRight());
								pst.execute();
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	// 修改tblRightType表中权限类型(允许负库存过账,允许超订单数出库,允许超订单数入库,允许少于成本价出库)
	public Result updateRightType(final RightType rt) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "update tblRightType set hasRight=? where id=? and rightType=?";
							PreparedStatement pst = conn.prepareStatement(sql);
							pst.setString(1, rt.getHasRight());
							pst.setString(2, rt.getId());
							pst.setString(3, rt.getRightType());
							int number = pst.executeUpdate();
							if (number == 0) {
								sql = "insert into tblRightType(id,rightType,hasRight) values(?,?,?)";
								pst = conn.prepareStatement(sql);
								pst.setString(1, rt.getId());
								pst.setString(2, rt.getRightType());
								pst.setString(3, rt.getHasRight());
								int m = pst.executeUpdate();
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	// 删除tblRightType表中权限类型(允许负库存过账,允许超订单数出库,允许超订单数入库,允许少于成本价出库)
	public Result deleteRightType(final String roleId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "delete tblRightType  where id=? ";
							PreparedStatement pst = conn.prepareStatement(sql);
							pst.setString(1, roleId);
							pst.executeUpdate();
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	//根绝用户角色的id在tblRightType表中查询该用户的该用户的权限类型(允许负库存过账,允许超订单数出库,允许超订单数入库,允许少于成本价出库)
	public Result queryRightTypeById(final String roleId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select * from tblRightType where id=?";
							PreparedStatement pst = conn.prepareStatement(sql);
							pst.setString(1, roleId);
							ResultSet rss = pst.executeQuery();
							Map<Integer, String> map = new HashMap<Integer, String>();
							while (rss.next()) {
								String type = rss.getString("rightType");
								String right = rss.getString("hasRight");
								if ("allowLittleStocks".equals(type) && "1".equals(right))
									map.put(1, "1");
								else if ("allowMoreOrderOut".equals(type) && "1".equals(right))
									map.put(2, "1");
								else if ("allowMoreOrderIn".equals(type) && "1".equals(right))
									map.put(3, "1");
								else if ("allowLittleCostOut".equals(type) && "1".equals(right))
									map.put(4, "1");
								else if ("SalesAllowNegative".equals(type) && "1".equals(right))
									map.put(5, "1");
								else if ("allowCustomerCreditLimit".equals(type) && "1".equals(right)) {
									map.put(6, "1");
								} else if ("UnderLimitprice".equals(type) && "1".equals(right)) {
									map.put(7, "1");
								} else if ("WithOutSettleCys".equals(type) && "1".equals(right)) {
									map.put(8, "1");
								}
							}
							rs.setRetVal(map);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result queryByName(final String roleName, final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement stmt = conn.createStatement();
							String sql = "select count(roleName) from tblRole where roleName='" + roleName + "'";
							if (id.length() > 0) {
								sql += " and id!='" + id + "'";
							}
							stmt.execute(sql);
							ResultSet rss = stmt.executeQuery(sql);
							boolean exists = false;
							if (rss.next()) {
								if (rss.getInt(1) > 0) {
									exists = true;
								}
							}
							if (exists) {
								rs.setRetCode(ErrorCanst.DATA_ALREADY_USED);
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result add(final RoleBean bean, final String sourceRoleId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				//            	Result temp=addBean(bean,session);
				//            	if(temp.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
				//            		return temp.getRetCode();
				//            	}
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement stmt = conn.createStatement();
							String sql = "insert into tblRole (id,createby,createTime,lastUpdateBy,lastUpdateTime,roleDesc,roleName,SCompanyID,HiddenField) select '" + bean.getId() + "','" + bean.getCreateBy()
									+ "','" + bean.getCreateTime() + "','" + bean.getLastUpdateBy() + "','" + bean.getLastUpdateTime() + "','" + bean.getRoleDesc() + "','" + bean.getRoleName()
									+ "','" + bean.getSCompanyID() + "',HiddenField from tblRole where id='" + sourceRoleId + "' ";
							stmt.execute(sql);
							
							sql = "insert into tblRightType(id,rightType,hasRight) select '" + bean.getId() + "',rightType,hasRight from tblRightType where id='" + sourceRoleId + "'";
							stmt = conn.createStatement();
	                    	stmt.execute(sql);	
							
							sql = "insert into tblRoleModule(moduleOpId,roleid) select moduleOpId,'" + bean.getId() + "' from tblRoleModule where roleId='" + sourceRoleId + "'";
							stmt.execute(sql);
							sql = "select id from tblRoleScope where roleid='" + sourceRoleId + "'";
							ResultSet rss = stmt.executeQuery(sql);
							ArrayList<String> list = new ArrayList<String>();
							while (rss.next()) {
								list.add(rss.getString("id"));
							}
							for (int i = 0; i < list.size(); i++) {
								String oldscopeId = list.get(i);
								sql = "insert into tblRoleScope(roleId,flag,tableName,fieldName,scopeValue,escopeValue,isAddLevel,isAllModules,rightDelete,rightUpdate,tableNameDisplay,fieldNameDisplay,scopevaluedisplay) "
										+ " select '"
										+ bean.getId()
										+ "',flag,tableName,fieldName,scopeValue,escopeValue,isAddLevel,isAllModules,rightDelete,rightUpdate,tableNameDisplay,fieldNameDisplay,scopevaluedisplay "
										+ " from tblRoleScope where id='" + oldscopeId + "'";
								stmt.execute(sql);
								sql = " select @@identity ";
								ResultSet rfid = stmt.executeQuery(sql);
								String newId = "";
								if (rfid.next()) {
									newId = rfid.getString(1);
								} else {
									throw new Exception("无法获取最后的标识ID值");
								}

								sql = "insert into tblRoleModuleScope(scopeId,moduleOpId) select '" + newId + "',b.moduleOpId from tblRoleModuleScope a "
										+ " join tblModelOperations b on a.moduleOpId=b.moduleOpId " + " join tblRoleScope c on c.Id=a.scopeId and c.roleId='" + sourceRoleId
										+ "'    where a.scopeId='" + oldscopeId + "'";
								stmt.execute(sql);
							}

						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.add Error :", ex);
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * @param id long
	 * @param name String
	 * @return Result
	 */
	public Result update(RoleBean bean) {

		return updateBean(bean);
	}

	/**
	 * @param ids long[]
	 * @return Result
	 */
	public Result delete(String[] ids) {
		return deleteBean(ids, RoleBean.class, "id");
	}

	/**
	 * @param ids long[]
	 * @return Result
	 */
	public Result deleteScope(String[] ids) {
		final int[] idi = new int[ids.length];
		for (int i = 0; i < ids.length; i++) {
			idi[i] = Integer.parseInt(ids[i]);
		}
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				for (int i : idi) {
					Result rd = deleteBean(i, RoleScopeBean.class, "id", session);
					if (rd.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						return rd.getRetCode();
					}
				}
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {

							String sql = "";
							for (int i : idi) {
								sql = "delete from tblRoleModuleScope where scopeId=?";
								PreparedStatement stmt = conn.prepareStatement(sql);
								stmt.setInt(1, i);
								stmt.execute();
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.deleteScope Error :", ex);
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;

	}

	public Result query(String name, int pageNo, int pageSize, String sunCmpClassCode) {
		List param = new ArrayList();
		//'' as userName  是为了给查询角色时预留用户的位置，不能删
		String hql = "select bean.id,bean.roleName,bean.roleDesc,sun.sunCompanyName,'' as userName " + "from RoleBean as bean,SunCompanyBean as sun "
				+ "where bean.id<>'1' and bean.SCompanyID=sun.classCode and bean.SCompanyID like '" + sunCmpClassCode + "%'";
		if (name != null && name.length() != 0) {
			hql += " and upper(bean.roleName) like '%'||?||'%' ";
			param.add(name.trim().toUpperCase());
		}
		hql += " order by len(bean.SCompanyID),bean.SCompanyID,bean.roleName";
		return list(hql, param, pageNo, pageSize, true);
	}
	
	public Result queryRoleUser(String sunCmpClassCode) {
		List param = new ArrayList();
		//'' as userName  是为了给查询角色时预留用户的位置，不能删
		String hql = "select id,roleName,roleDesc,sunCompanyName from (" +
				"  select 0 orderby, bean.id,bean.roleName,bean.roleDesc,sun.sunCompanyName from tblRole as bean,tblSunCompanys as sun where bean.id<>'1' and bean.SCompanyID=sun.classCode and bean.SCompanyID like '" + sunCmpClassCode + "%' " +
				" union " +
				" select 1 orderby, bean.id,bean.EmpFullName,'' roleDesc,sun.sunCompanyName from tblEmployee as bean,tblSunCompanys as sun where bean.statusId=0 and ISNULL(bean.isPublic,0)=0 and bean.SCompanyID=sun.classCode and bean.SCompanyID like '" + sunCmpClassCode + "%' " +
						") a order by orderby, roleName";
		return sqlList(hql, param);
	}

	/**
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result queryScope(String roleId) {
		List param = new ArrayList();
		String hql = "select bean.id,bean.flag,bean.tableName,bean.fieldName,bean.scopeValue,bean.escopeValue from RoleScopeBean as bean where bean.roleId = ? ";
		param.add(roleId);
		return list(hql, param);

	}

	/**
	 * 跟据角色，权限类型（如查看部门单据）查询，所设置的高级权限
	 * @param roleId
	 * @param defSys
	 * @param type
	 * @return
	 */
	public Result queryScope(final String roleId, final String defSys, final String type, final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String str_sel = "";
						if ("0".equals(type)) { //分类资料，要跟据tableName 过滤 
							str_sel = "select  a.id,a.flag,a.tableName,a.fieldName,a.scopeValue,a.escopeValue,isAllModules,rightDelete,rightUpdate,isAddLevel,a.tableNameDisplay,a.fieldNameDisplay,a.scopeValueDisplay from tblRoleScope a where a.roleId='"
									+ roleId + "' and flag='" + type + "' and tableName='" + tableName + "'";
						} else {
							str_sel = "select  a.id,a.flag,a.tableName,a.fieldName,a.scopeValue,a.escopeValue,isAllModules,rightDelete,rightUpdate,isAddLevel,a.tableNameDisplay,a.fieldNameDisplay,a.scopeValueDisplay from tblRoleScope a where a.roleId='"
									+ roleId + "' and flag='" + type + "'";
						}
						try {

							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(str_sel);
							List ls = new ArrayList();
							while (rss.next()) {
								String[] o = new String[13];
								o[0] = rss.getString("id");
								o[1] = rss.getString("flag");
								o[2] = rss.getString("tableName");
								o[3] = rss.getString("fieldName");
								o[4] = rss.getString("scopeValue");
								o[5] = rss.getString("escopeValue");
								o[6] = rss.getString("isAllModules");
								o[7] = rss.getString("rightDelete");
								o[8] = rss.getString("rightUpdate");
								o[9] = rss.getString("isAddLevel");
								o[10] = rss.getString("tableNameDisplay");
								o[11] = rss.getString("fieldNameDisplay");
								o[12] = rss.getString("scopeValueDisplay");
								ls.add(o);
							}
							rs.retVal = ls;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + str_sel, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});

		return rs;
	}

	public Result queryAllRoleByClassCode(String classCode) {
		List param = new ArrayList();
		String hql = "select bean.id,bean.roleName,bean.SCompanyID from RoleBean as bean where " + "bean.SCompanyID like '" + classCode + "%'";
		return list(hql, param);
	}

	public Result queryUserSunCompanyRoles(final String userid) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String str_sel = "select userid,sunCompanyid,roleids from tblUserSunCompany" + " where userid ='" + userid + "'";
						try {

							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(str_sel);
							List ls = new ArrayList();
							while (rss.next()) {
								String[] o = new String[3];
								o[0] = rss.getString("userid");
								o[1] = rss.getString("sunCompanyid");
								o[2] = rss.getString("roleids");
								ls.add(o);
							}
							rs.retVal = ls;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + str_sel, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});

		return rs;
	}

	/**
	 * @param roleid String
	 * @return Result
	 */
	public Result queryRoleById(String roleid) {
		List param = new ArrayList();
		String hql = "select bean from RoleBean as bean where bean.id='" + roleid + "'";
		return list(hql, param);
	}

	/**
	 * @param roleIds String
	 * @return Result
	 */
	public Result queryRolesByRoleIds(String roleIds) {
		Result rs = new Result();
		List list = new ArrayList();
		if (roleIds != null && roleIds.length() > 0) {
			String[] roleids = roleIds.split(";");
			for (String roleid : roleids) {
				RoleBean role = (RoleBean) ((List) queryRoleById(roleid).getRetVal()).get(0);
				list.add(role);
			}
		}
		rs.setRetVal(list);
		return rs;
	}

	public Result detail(String id) {
		return loadBean(id, RoleBean.class);
	}

	public Result detailEmployee(String id) {
		return loadBean(id, EmployeeBean.class);
	}

	public Result addRoleScope(final RoleScopeBean bean, final String[] moduleOpIds) {
		//重复检查
//		if(!"5".equals(bean.getFlag())&&!"1".equals(bean.getFlag())){
//			List param = new ArrayList();
//			String sql = "select 1 from tblRoleScope where roleId = ? and flag = ? and tableName = ? and fieldName = ?";
//			param.add(bean.getRoleId());
//			param.add(bean.getFlag());
//			param.add(bean.getTableName());
//			param.add(bean.getFieldName());
//			Result rd = sqlList(sql, param);
//			if (rd.retCode == ErrorCanst.DEFAULT_SUCCESS && ((List) rd.retVal).size() > 0) {
//				Result rs = new Result();
//				rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
//				return rs;
//			}
//		}

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				Result r = addBean(bean, session);
				if (r.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return r.retCode;
				}
				rs.retVal = bean;
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;

						//如果为5时要先删除"DEPT":"ALL"类型的范围
						if ("5".equals(bean.getFlag())) {
							String sql = " delete tblrolescope where roleId=? and scopeValue in ('DEPT','ALL') ";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, bean.getRoleId());
							cs.execute();
						}

						String sql = " insert into tblrolemodulescope(scopeId,moduleOpId) values(?,?) ";

						try {
							if (moduleOpIds != null) {
								for (String moduleOpId : moduleOpIds) {
									if (moduleOpId != null) {
										PreparedStatement cs = conn.prepareStatement(sql);
										cs.setInt(1, bean.getId());
										cs.setInt(2, Integer.parseInt(moduleOpId));
										cs.execute();
									}
								}
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.addRoleScope Error :", ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 用于4.6时的批量添加
	 * @param beans
	 * @return
	 */
	public Result addRoleScope(final ArrayList<RoleScopeBean> beans) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				final ArrayList<RoleScopeBean> beanList = new ArrayList<RoleScopeBean>();
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							//先去掉重复的

							for (RoleScopeBean bean : beans) {
								boolean found = false;
								for (RoleScopeBean bean2 : beanList) {
									if (bean.getFieldName() != null && bean.getFieldName().equals(bean2.getFieldName())) {
										found = true;
									}
								}
								if (!found) {
									beanList.add(bean);
								}
							}
							//检查数据库中是否重复
							for (RoleScopeBean bean : beanList) {
								if(!bean.getFlag().equals("6") ){
									String sql = "select 1 from tblRoleScope where roleId = ? and flag = ? and tableName = ? and fieldName = ?";
									PreparedStatement cs = conn.prepareStatement(sql);
									cs.setString(1, bean.getRoleId());
									cs.setString(2, bean.getFlag());
									cs.setString(3, bean.getTableName());
									cs.setString(4, bean.getFieldName());
									ResultSet rq = cs.executeQuery();
									if (rq.next()) {
										rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
										return;
									}
								}
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.addRoleScope Error :", ex);
							return;
						}
					}
				});
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					for (RoleScopeBean bean : beanList) {
						Result r = addBean(bean, session);
						if (r.retCode != ErrorCanst.DEFAULT_SUCCESS) {
							return r.retCode;
						}
					}
				}

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result updateDeptRoleScope(final String roleId, final String deptRightType, final String deptRightTypeUpdate, final String deptRightTypeDelete) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {

				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							if (!"3".equals(deptRightType)) {
								String sql = " delete tblrolemodulescope where scopeId in (select id from tblrolescope where roleId=? and flag=5 ) ";
								PreparedStatement cs = conn.prepareStatement(sql);
								cs.setString(1, roleId);
								cs.execute();

								sql = " delete tblrolescope where roleId=? and flag=5 ";
								cs = conn.prepareStatement(sql);
								cs.setString(1, roleId);
								cs.execute();
							}
							if ("1".equals(deptRightType) || "2".equals(deptRightType)) {

								String sql = " insert into tblRoleScope(roleId,flag,tableName,fieldName,scopeValue,escopeValue,isAllModules,rightUpdate,rightDelete) " + " values(?,?,?,?,?,?,?,?,?) ";
								PreparedStatement cs = conn.prepareStatement(sql);
								cs.setString(1, roleId);
								cs.setString(2, "5");
								cs.setString(3, "");
								cs.setString(4, "");
								cs.setString(5, "1".equals(deptRightType) ? "DEPT" : "ALL");
								cs.setString(6, "");
								cs.setString(7, "1");
								cs.setString(8, deptRightTypeUpdate == null ? "0" : deptRightTypeUpdate);
								cs.setString(9, deptRightTypeDelete == null ? "0" : deptRightTypeDelete);
								cs.execute();

							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.addRoleScope Error :", ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result updateRoleScope(final RoleScopeBean bean, final String[] moduleOpIds) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				Result r = updateBean(bean, session);
				if (r.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return r.retCode;
				}

				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;

						String sql = " delete tblrolemodulescope where scopeId=? ";
						PreparedStatement cs = conn.prepareStatement(sql);
						cs.setInt(1, bean.getId());
						cs.execute();

						sql = " insert into tblrolemodulescope(scopeId,moduleOpId) values(?,?) ";

						try {
							if (moduleOpIds != null) {
								for (String moduleOpId : moduleOpIds) {
									if (moduleOpId != null) {
										cs = conn.prepareStatement(sql);
										cs.setInt(1, bean.getId());
										cs.setInt(2, Integer.parseInt(moduleOpId));
										cs.execute();
									}
								}
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.addRoleScope Error :", ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result loadScope(int id) {
		return loadBean(id, RoleScopeBean.class);
	}

	/**
	 * 根据范围权限的代号查询对应的所有的模块操作ID
	 * @param scopeId
	 * @return
	 */
	public Result getScopeModuleByScopeId(final int scopeId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;

						String querysql = "select  moduleOpId from tblrolemodulescope a where a.scopeId =? ";

						try {
							PreparedStatement cs = conn.prepareStatement(querysql);
							cs.setInt(1, scopeId);

							ResultSet rset = cs.executeQuery();
							ArrayList list = new ArrayList();
							while (rset.next()) {
								list.add(rset.getInt(1));
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public Result getUserName(final String values) {
		final Result rs = new Result();
		if (values == null || values.length() == 0) {
			return rs;
		}
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String[] vals = values.split(";");
						ArrayList paramList = new ArrayList();
						String strsql = "";
						for (String s : vals) {
							if (s != null && s.length() > 0) {
								paramList.add(s);
								strsql += "?,";
							}
						}
						strsql = strsql.substring(0, strsql.length() - 1);

						String querysql = "select id,EmpFullName from tblEmployee where id in(" + strsql + ") ";

						try {
							PreparedStatement cs = conn.prepareStatement(querysql);
							for (int i = 0; i < paramList.size(); i++) {
								cs.setString(i + 1, paramList.get(i).toString());
							}

							ResultSet rset = cs.executeQuery();
							ArrayList list = new ArrayList();
							while (rset.next()) {
								KeyPair kp = new KeyPair();
								kp.setName(rset.getString(2));
								kp.setValue(rset.getString(1));
								list.add(kp);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	/**
	 * @param values String
	 * @return Result
	 */
	public Result getDeptName(final String values) {
		final Result rs = new Result();
		if (values == null || values.length() == 0) {
			return rs;
		}
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String[] vals = values.split(";");
						ArrayList paramList = new ArrayList();
						String strsql = "";
						for (String s : vals) {
							if (s != null && s.length() > 0) {
								paramList.add(s);
								strsql += "?,";
							}
						}
						strsql = strsql.substring(0, strsql.length() - 1);

						String querysql = "select classCode,DeptFullName from tblDepartment where classCode in(" + strsql + ") ";

						try {
							PreparedStatement cs = conn.prepareStatement(querysql);
							for (int i = 0; i < paramList.size(); i++) {
								cs.setString(i + 1, paramList.get(i).toString());
							}

							ResultSet rset = cs.executeQuery();
							ArrayList list = new ArrayList();
							while (rset.next()) {
								KeyPair kp = new KeyPair();
								kp.setName(rset.getString(2));
								kp.setValue(rset.getString(1));
								list.add(kp);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	/**
	 * @return Result
	 */
	public Result queryAllSunCompany() {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String strsql = "select a.id,'zh_CN:'+b.zh_CN+';zh_TW:'+b.zh_TW+';en:'+b.en+';' from tblSunCompanys a left join tblLanguage b on a.sunCompanyName=b.id";
						try {
							Statement s = conn.createStatement();
							ResultSet rset = s.executeQuery(strsql);
							ArrayList list = new ArrayList();
							while (rset.next()) {
								String str[] = new String[2];
								str[0] = rset.getString(1);
								str[1] = rset.getString(2);
								list.add(str);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;

	}

	public Result queryDefaultSunCompany() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String strsql = "select a.id,'zh_CN:'+isnull(b.zh_CN,'')+';zh_TW:'+isnull(b.zh_TW,'')+';en:'+isnull(b.en,'')+';',a.classCode from tblSunCompanys a"
								+ " left join tblLanguage b on a.sunCompanyName=b.id where a.id='1'";
						try {
							Statement s = conn.createStatement();
							ResultSet rset = s.executeQuery(strsql);
							ArrayList list = new ArrayList();
							while (rset.next()) {
								String str[] = new String[3];
								str[0] = rset.getString(1);
								str[1] = rset.getString(2);
								str[2] = rset.getString(3);
								list.add(str);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * @return Result
	 */
	public Result queryAllSubSunCompany(final String classCode) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String strsql = "select id,sunCompanyName,classCode from tblSunCompanys where classCode like '" + classCode + "%'";
						try {
							Statement s = conn.createStatement();
							ResultSet rset = s.executeQuery(strsql);
							ArrayList list = new ArrayList();
							while (rset.next()) {
								String str[] = new String[3];
								str[0] = rset.getString(1);
								str[1] = rset.getString(2);
								str[2] = rset.getString(3);
								list.add(str);
							}
							rs.setRetVal(list);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}


	/**
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result queryAll(String type) {
		Result rs = new Result();
		ArrayList mbean = (ArrayList) BaseEnv.moduleMap.get(type);
		HashMap map = new HashMap();
		for (Object o : mbean) {
			ModuleBean bean = (ModuleBean) o;
			if (type.equals(bean.getMainModule()) || "0".equals(bean.getMainModule())) {
				ArrayList list = new ArrayList();
				getChildModuleList(bean, list, type);
				map.put(bean, list);
			}
		}
		rs.retVal = new Object[] { mbean, map };
		return rs;
	}

	/**
	 * @param bean ModuleBean
	 * @param list ArrayList
	 */
	private void getChildModuleList(ModuleBean bean, ArrayList list, String type) {
		if (bean == null || bean.getChildList() == null || bean.getChildList().size() == 0) {
			return;
		}
		for (Object o : bean.getChildList()) {
			ModuleBean childBean = (ModuleBean) o;
			if (("0".equals(childBean.getMainModule()) || type.equals(childBean.getMainModule())) && childBean.getLinkAddress() != null && childBean.getLinkAddress().length() > 0) {
				list.add(childBean);
			}
			getChildModuleList(childBean, list, type);
		}
	}

	/**
	 * 不包括公共模块
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result queryAllNoPub(String type) {
		Result rs = new Result();
		ArrayList mbeanList = new ArrayList();		
		HashMap map = new HashMap();
		ArrayList mbean = (ArrayList) BaseEnv.moduleMap.get(type);
		if(mbean != null){
			for (Object o : mbean) {
				ModuleBean bean = (ModuleBean) o;
				if (type.equals(bean.getMainModule()) || "0".equals(bean.getMainModule())) {
					ArrayList list = new ArrayList();
					getChildModuleListNoPub(bean, list, type);
					if (list.size() > 0) {
						map.put(bean, list);
						mbeanList.add(bean);
					}
				}
			}
		}
		rs.retVal = new Object[] { mbeanList, map };
		return rs;
	}

	/**
	 * @param bean ModuleBean
	 * @param list ArrayList
	 */
	private void getChildModuleListNoPub(ModuleBean bean, ArrayList list, String type) {
		if (bean == null || bean.getChildList() == null || bean.getChildList().size() == 0) {
			return;
		}
		for (Object o : bean.getChildList()) {
			ModuleBean childBean = (ModuleBean) o;
			if ((type.equals(childBean.getMainModule())) && childBean.getLinkAddress() != null && childBean.getLinkAddress().length() > 0) {
				list.add(childBean);
			}
			getChildModuleListNoPub(childBean, list, type);
		}
	}

	/**
	 * 查询所有公共模块
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result queryAllPub() {
		Result rs = new Result();
		ArrayList mbeanList = new ArrayList();
		ArrayList mbean = (ArrayList) BaseEnv.moduleMap.get("0");
		HashMap map = new HashMap();
		for (Object o : mbean) {
			ModuleBean bean = (ModuleBean) o;
			if ("1".equals(bean.getMainModule()) || "0".equals(bean.getMainModule())) {
				ArrayList list = new ArrayList();
				getChildModuleListPub(bean, list, "1");
				if (list.size() > 0) {
					map.put(bean, list);
					mbeanList.add(bean);
				}
			}
		}
		rs.retVal = new Object[] { mbeanList, map };
		return rs;
	}

	/**
	 * @param bean ModuleBean
	 * @param list ArrayList
	 */
	private void getChildModuleListPub(ModuleBean bean, ArrayList list, String type) {
		if (bean == null || bean.getChildList() == null || bean.getChildList().size() == 0) {
			return;
		}
		for (Object o : bean.getChildList()) {
			ModuleBean childBean = (ModuleBean) o;
			if (("0".equals(childBean.getMainModule())) && childBean.getLinkAddress() != null && childBean.getLinkAddress().length() > 0) {
				list.add(childBean);
			}
			getChildModuleListPub(childBean, list, type);
		}
	}
	
	//查询所有模块
	public Result queryAll(){
		Result rs = new Result();
		ArrayList list = new ArrayList();
		for(ModuleBean mb:BaseEnv.allModule){
				ModuleBean bean = mb;
				getChildModuleListById(bean, list);			
		}
		rs.retVal = list;
		return rs;
	}
	
	
	/**
	 * 查询所有公共模块
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result queryModuleById(String moduleId) {
		Result rs = new Result();
		ModuleBean bean = null;
		for(ModuleBean mb:BaseEnv.allModule){
			if(mb.getId().trim().equals(moduleId.trim())){
				bean = mb;
				break;
			}
		}
		ArrayList list = new ArrayList();
		getChildModuleListById(bean, list);
		rs.retVal = list;
		return rs;
	}

	/**
	 * @param bean ModuleBean
	 * @param list ArrayList
	 */
	private void getChildModuleListById(ModuleBean bean, ArrayList list) {
		if (bean == null || bean.getChildList() == null || bean.getChildList().size() == 0) {
			return;
		}
		for (Object o : bean.getChildList()) {
			ModuleBean childBean = (ModuleBean) o;
			if (childBean.getLinkAddress() != null && childBean.getLinkAddress().length() > 0) {
				list.add(childBean);
			}
			getChildModuleListById(childBean, list);
		}
	}

	/**
	 * @param id String
	 * @return Result
	 */
	public Result queryRoleModuleByRoleid(final String id, final String userId) {

		final ArrayList sList = new ArrayList();

		final HashMap<Integer, ArrayList> map = new HashMap();

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "";

						try {
							querysql = "select rm.*,mo.operationID,mo.id moid,mo.f_ref from tblRoleModule rm join tblModelOperations mo on rm.moduleOpId = mo.moduleOpId where rm.roleid=? ";
							PreparedStatement cs = conn.prepareStatement(querysql);
							cs.setString(1, id);
							ResultSet rset = cs.executeQuery();
							while (rset.next()) {
								RoleModuleBean sb = new RoleModuleBean();
								sb.setId(rset.getInt("id"));
								sb.setDisplayFlag(rset.getString("displayFlag"));
								ModuleOperationBean moBean = new ModuleOperationBean();
								moBean.setId(rset.getInt("moid"));
								moBean.setModuleOpId(rset.getInt("moduleOpId"));
								moBean.setOperationID(rset.getInt("operationID"));
								ModuleBean mb = new ModuleBean();
								mb.setId(rset.getString("f_ref"));
								moBean.setModuleBean(mb);
								sb.setModuleOpBean(moBean);
								sList.add(sb);
							}

						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		rs.setRetVal(sList);
		return rs;
	}

	/**
	 * @param id String
	 * @return Result
	 */
	public Result queryRoleScopyByRoleid(final String id, final String userId,final String userName,final String deptCode) {

		final HashMap<String, ArrayList> map = new HashMap();
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "";

						try {
							

								querysql = " select rms.moduleOpId,scope.* from tblRoleModuleScope rms  join tblRoleScope scope " + 
									"  on   rms.scopeId = scope.id  where scope.roleId = ?";

								PreparedStatement cs = conn.prepareStatement(querysql);
								cs.setString(1, id);
								ResultSet rset = cs.executeQuery();
								while (rset.next()) {
									String moduleOpId = rset.getString("moduleOpId");
									String flag = rset.getString("flag");
									String tableName = rset.getString("tableName");
									String escopeValue = rset.getString("escopeValue");
									String fieldName = rset.getString("fieldName");
									String roleId = rset.getString("roleId");
									String scopeValue = rset.getString("scopeValue");
									String isAddLevel = rset.getString("isAddLevel");
									String valuetype = rset.getString("valuetype");
									int id = rset.getInt("id");
									
									if(flag.equals("5") && scopeValue.equals("DEPT")){
										//如果是查看本部门单据，先翻释成用户所在部门ID
										scopeValue = deptCode;
									}

									if (flag.equals("0")) {//当为分类权限时	
										boolean exists = false;
										RoleScopeBean sb = new RoleScopeBean();
										sb.setEscopeValue(escopeValue);
										sb.setTableName(tableName);
										sb.setFieldName(fieldName);
										sb.setFlag(flag);
										sb.setId(id);
										sb.setRoleId(roleId);
										sb.setScopeValue(scopeValue);
										sb.setIsAddLevel(isAddLevel);
										sb.setValuetype(valuetype);

										//分类资料，不用加moduleOpId
										ArrayList templist = map.get(moduleOpId );
										if (templist == null) {
											templist = new ArrayList();
											map.put(moduleOpId , templist);
										}
										templist.add(sb);
										exists = true;

									} else {
										RoleScopeBean sb = new RoleScopeBean();
										sb.setEscopeValue(escopeValue);
										sb.setFieldName(fieldName);
										sb.setFlag(flag);
										sb.setId(id);
										sb.setRoleId(roleId);
										sb.setScopeValue(scopeValue);
										sb.setIsAddLevel(isAddLevel);
										sb.setTableName(tableName);
										sb.setValuetype(valuetype);

										if ("6".equals(sb.getFlag()) && "@userId".equals(sb.getScopeValue())) {
											sb.setScopeValue(userId);
										}
										if ("6".equals(sb.getFlag()) && "@userName".equals(sb.getScopeValue())) {
											sb.setScopeValue(userName);
										}
										ArrayList templist = map.get( moduleOpId);
										if (templist == null) {
											templist = new ArrayList();
											map.put(moduleOpId, templist);
										}
										templist.add(sb);
									}

								}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		rs.setRetVal(map);
		return rs;
	}

	public Result getTableNameByModuleId(final String moduleId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "select tblName from tblModules where id='" + moduleId + "'";
						try {
							Statement s = connection.createStatement();
							ResultSet rset = s.executeQuery(strsql);
							if (rset.next()) {
								rs.setRetVal(rset.getString(1));
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public Result getModuleIdsByScope(final String scopeId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "select distinct d.id from tblRoleModuleScope a,tblModelOperations c,tblModules d where " + " a.moduleOpId=c.moduleOpId and c.f_ref=d.id and a.scopeId='"
								+ scopeId + "'";
						try {
							Statement s = connection.createStatement();
							ResultSet rset = s.executeQuery(strsql);
							List<String> selectModuleIds = new ArrayList<String>();
							while (rset.next()) {
								selectModuleIds.add(rset.getString("id"));
							}
							rs.setRetVal(selectModuleIds);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	private Result query(String hql, Object... param) {
		ArrayList params = new ArrayList();
		for (Object obj : param) {
			params.add(obj);
		}
		return list(hql, params);
	}


	/**
	 * @param existFields
	 * @param tablename
	 * @return
	 */
	public Result queryScopeValues(final String existFields, final String tablename, final boolean isMoreLanguage, final String qStr) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String sql = "";
						if (isMoreLanguage) {
							sql = "select classCode,l.zh_CN as field_zh from tblAccTypeInfo left join tblLanguage l on " + existFields + "=l.Id where 1=1 ";
						} else if (!isMoreLanguage) {
							sql = "select classCode" + "," + existFields + " as field_zh from " + tablename + " where 1=1 ";
						}
						sql += " and classCode in (" + qStr + ")";

						try {
							PreparedStatement psmt = connection.prepareStatement(sql);
							ResultSet rss = psmt.executeQuery();
							List rowData = new ArrayList();
							HashMap map = new HashMap();
							while (rss.next()) {
								String[] strs = new String[2];
								strs[0] = rss.getString("classCode");
								strs[1] = rss.getString("field_zh");
								rowData.add(strs);
								map.put(strs[0], strs[1]);
							}
							rs.setRetVal(map);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	/**
	 * @param roleId
	 * @return
	 */
	public Result queryLanguageById(final String Id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "select 'zh_CN:'+zh_CN+';zh_TW:'+zh_TW+';en:'+en+';' from tblLanguage where id='" + Id + "'";
						try {
							Statement st = connection.createStatement();
							ResultSet rss = st.executeQuery(strsql);
							if (rss.next()) {
								String str = rss.getString(1);
								rs.setRetVal(str);
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	/**
	 * 模块管理只直接给解决授权时用
	 * @param moduleIds
	 * @param list
	 * @return
	 */
	public Result updateRoleModuleByModule(final String moduleIds, final ArrayList<String[]> list) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "delete tblRoleModule where moduleOpId in (select b.moduleOpId from   tblModules a,tblModelOperations b" + " where a.id=b.f_ref and a.id in ('" + moduleIds
								+ "') )";
						try {
							Statement st = connection.createStatement();
							st.executeUpdate(strsql);
							for (String[] ss : list) {
								strsql = " insert into tblRoleModule (moduleOpId,roleId) values(?,?)";
								PreparedStatement pst = connection.prepareStatement(strsql);
								pst.setString(1, ss[1]);
								pst.setString(2, ss[0]);
								pst.executeUpdate();
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("RoleMgt.updateRoleModuleByModule Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 跟据模块查询，该模块所有的角色和角色操作
	 * @param modOperIds
	 * @return
	 */
	public Result queryRoleModuleByModule(final String moduleIds) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "select c.roleId,b.operationId from  tblModules a,tblModelOperations b,tblRoleModule c" + " where a.id=b.f_ref and b.moduleOpId=c.moduleOpId and a.id in ('"
								+ moduleIds + "')";
						try {
							Statement st = connection.createStatement();
							ResultSet rss = st.executeQuery(strsql);
							ArrayList list = new ArrayList();
							while (rss.next()) {
								String[] obj = new String[2];
								obj[0] = rss.getString(1);
								obj[1] = rss.getString(2);
								list.add(obj);
							}

							rs.setRetVal(list);

							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 跟据模板查模块的所有操作
	 * @param modOperIds
	 * @return
	 */
	public Result queryModuleOperationByModule(final String moduleIds) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "select b.operationId,b.moduleOpId from  tblModules a,tblModelOperations b" + " where a.id=b.f_ref  and a.id in ('" + moduleIds + "')";
						try {
							Statement st = connection.createStatement();
							ResultSet rss = st.executeQuery(strsql);
							ArrayList list = new ArrayList();
							while (rss.next()) {
								list.add(new String[] { rss.getString(1), rss.getString(2) });
							}

							rs.setRetVal(list);

							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查询模信息
	 * @param moduleIds
	 * @return
	 */
	public Result queryModule(final String moduleIds, final String locale) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String strsql = "select " + locale + " from tblModules a join tblLanguage b on  a.modelName = b.id" + " where a.id in ('" + moduleIds + "')";
						try {
							Statement st = connection.createStatement();
							ResultSet rss = st.executeQuery(strsql);
							if (rss.next()) {
								rs.setRetVal(rss.getString(1));
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}




	public Result queryUserByRole(final String[] roleId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						String str_sel = "";
						try {
							Connection conn = connection;
							String roles = "";
							for (int i = 0; i < roleId.length; i++) {
								roles += " roleids like '%" + roleId[i] + "%' or ";
							}
							Statement st = conn.createStatement();

							if (roles.length() > 0) {
								roles = roles.substring(0, roles.length() - 3);

								str_sel = "select count(userId) from tblUserSunCompany where " + roles;
								ResultSet rst = st.executeQuery(str_sel);
								if (rst.next()) {
									rs.setRetVal(rst.getInt(1));
								}
							}

						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + str_sel, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 根据角色ID获取使用该角色的用户
	 * @param roleId
	 * @return
	 */
	public Result getUser(String roleId) {
		ArrayList<String> param = new ArrayList<String>();
		String hql = "SELECT id,empFullName FROM tblemployee WHERE id IN (SELECT userid FROM tblUserSunCompany tusc WHERE tusc.roleids LIKE '%'+?+'%')";
		param.add(roleId);
		return sqlList(hql, param);

	}

	/**
	 * 取所有主模块
	 * @return
	 */
	public boolean  queryModuleType(StringBuffer sb,String locale) {
		boolean hasData=false;
		ArrayList mbean = (ArrayList) BaseEnv.allModule;
		HashMap map = new HashMap();
		for (Object o : mbean) {
			ModuleBean bean = (ModuleBean) o;
			//去除资料和系统
			if (!bean.getClassCode().equals("00002")&& !bean.getClassCode().equals("00003")) {			
				if (getChildModuleType(bean,sb,locale)) {
					String type = "";
					if(bean.getMainModule().equals("1")){
						type="ERP:";
					}else if(bean.getMainModule().equals("2")){
						type="OA:";
					}else if(bean.getMainModule().equals("3")){
						type="CRM:";
					}
					sb.append(",{ id:'"+bean.getClassCode()+"', pId:'"+"', name:'"+type+bean.getModelDisplay().get(locale)+"',tbtype:'DIR'}");
					hasData = true;
				}
			}
		}
		return hasData;
	}
	
	private Result getParentLinkReport(String reportName,String locale){
		ArrayList<String> param = new ArrayList<String>();
		String hql = "SELECT ReportNumber,"+locale+" FROM tblReports a join tblLanguage b on a.ReportName=b.id where parentLinkReport=?";
		param.add(reportName);
		return sqlList(hql, param);
	}
	
	/**
	 * @param bean ModuleBean
	 * @param list ArrayList
	 */
	private boolean getChildModuleType(ModuleBean bean, StringBuffer sb, String locale) {
		if (bean == null || bean.getChildList() == null || bean.getChildList().size() == 0) {
			return false;
		}
		boolean hasData=false;
		for (Object o : bean.getChildList()) {
			ModuleBean childBean = (ModuleBean) o;
			if ( childBean.getLinkAddress() != null && childBean.getIsHidden()==2 && childBean.getLinkAddress().length() > 0) {
				if(childBean.getLinkAddress().indexOf("/ReportDataAction.do") >-1){
					Pattern pattern = Pattern.compile("reportNumber=([^&]*)");
					Matcher matcher = pattern.matcher(childBean.getLinkAddress());
					String tableName="";
					if (matcher.find()) {
						tableName = matcher.group(1);
					}
					sb.append(",{ id:'"+childBean.getClassCode()+"', pId:'"+bean.getClassCode()+"', name:'"+childBean.getModelDisplay().get(locale)+"',tableName:'"+tableName+"',tbtype:'REPORT'}");
					//是报表，必须把该报表的所有链接报表查询出来。因为现在有些链接报表是没有菜单的
					Result rs = getParentLinkReport(tableName,locale);
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal!= null){
						for(Object[] os:(List<Object[]>)rs.retVal){
							String ptn = (String)os[0];
							sb.append(",{ id:'"+childBean.getClassCode()+ptn+"', pId:'"+bean.getClassCode()+"', name:'"+os[1]+"',tableName:'"+ptn+"',tbtype:'REPORT'}");
						}
					}
					hasData = true;
				}else if(childBean.getLinkAddress().indexOf("/UserFunctionQueryAction.do") > -1){
					//如果表，要过滤掉基础表
					Pattern pattern = Pattern.compile("tableName=([^&]*)");
					Matcher matcher = pattern.matcher(childBean.getLinkAddress());
					String tableName="";
					if (matcher.find()) {
						tableName = matcher.group(1);
					}
					DBTableInfoBean tBean =GlobalsTool.getTableInfoBean(tableName);
					if(tBean != null && tBean.getIsBaseInfo() != 1){					
						sb.append(",{ id:'"+childBean.getClassCode()+"', pId:'"+bean.getClassCode()+"', name:'"+childBean.getModelDisplay().get(locale)+"',tableName:'"+tableName+"',tbtype:'TABLE'}");
						hasData = true;
					}
				}
			}
			if(getChildModuleType(childBean, sb, locale)){
				sb.append(",{ id:'"+childBean.getClassCode()+"', pId:'"+bean.getClassCode()+"', name:'"+childBean.getModelDisplay().get(locale)+"',tbtype:'DIR'}");
				hasData = true;
			}
		}
		return hasData;
	}

	

	//获取tblscopemodule中指定表的所有子表
	@SuppressWarnings("unchecked")
	public List<Object> queryAllReport(String tableName) {
		List<String> param = new ArrayList<String>();
		String strsql = " SELECT moduleNumber FROM tblscopemodule t left join tblReports r on  t.moduleNumber=r.ReportNumber "
				+ " WHERE  r.ReportType not in ('BILL','TABLELIST') and t.moduleNumber = ? ";
		param.add(tableName);
		Result result = sqlList(strsql, param);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (List<Object>) result.getRetVal();
		} else {
			return null;
		}
	}

	//获取tblscopemodule中指定表的所有子表
	@SuppressWarnings("unchecked")
	public List<Object> queryAllScope1(String tableName, String moduleId, String method) {
		List<String> param = new ArrayList<String>();
		String strsql = "SELECT t.moduleNumber,t.moduleDis,t2.zh_CN,'" + moduleId + "' " + " FROM tblscopemodule t,tblLanguage t2 WHERE t.moduleDis=t2.id AND t.moduleNumber like ? ";
		if ("fieldValue".equals(method)) //如果是在查看某字段值时调用，则隐藏单据表
			strsql += " and len(t.moduleNumber)>" + tableName.length() + "";
		param.add("%" + tableName + "%");
		Result result = sqlList(strsql, param);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (List<Object>) result.getRetVal();
		} else {
			return null;
		}
	}

	/**
	 * 为单个角色修改用户
	 * @param userIds
	 * @param roleId
	 * @return
	 */
	public boolean updateSingleRole(String userIds, String roleId, String type) {
		userIds = "," + userIds;

		List<String> param = new ArrayList<String>();
		String sql = "";
		roleId = roleId + ";";
		if ("del".equals(type)) {
			sql = "UPDATE tblUserSunCompany SET roleids = REPLACE(roleIds,?,'') WHERE ? LIKE '%,'+userid+',%'";
		} else {
			sql = "UPDATE tblUserSunCompany SET roleids = roleIds+? WHERE ? LIKE '%,'+userid+',%'";
		}
		param.add("" + roleId + "");
		param.add("" + userIds + "");
		Result rs = this.msgSql(sql, param);
		boolean flag = false;
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS ) {
			flag = true;
		}
		return flag;
	}

	//获取tblDBTableInfo中指定表的所有子表
	@SuppressWarnings("unchecked")
	public List<Object> queryAllScope2(String tableName, String moduleId, String method) {
		List<String> param = new ArrayList<String>();
		String strsql = "SELECT t.tableName,t.languageId,t2.zh_CN,'" + moduleId + "' " + " FROM tblDBTableInfo t,tblLanguage t2 WHERE t.languageId=t2.id  and  t.tableName not in ( "
				+ " SELECT t3.moduleNumber FROM tblscopemodule t3,tblLanguage t4 WHERE t3.moduleDis=t4.id  AND   t3.moduleNumber like ? )" + " AND  t.tableName like ?";
		if ("fieldValue".equals(method)) //如果是在查看某字段值时调用，则隐藏单据表
			strsql += " and len(t.tableName)>" + tableName.length() + "";

		param.add("%" + tableName + "%");
		param.add("%" + tableName + "%");
		Result result = sqlList(strsql, param);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (List<Object>) result.getRetVal();
		} else {
			return null;
		}
	}

	//获取选中的表的权限
	@SuppressWarnings("unchecked")
	public String getScope(String roleId, String moduleId) {
		List<String> param = new ArrayList<String>();
		String strsql = " SELECT  id FROM tblrolemodule WHERE   roleid= ? and moduleOpId IN ("
				+ " SELECT moduleOpId FROM tblModelOperations tmo WHERE tmo.f_ref =? AND tmo.OperationID IN ('1','2','3','4') ) ";
		param.add("" + roleId + "");
		param.add("" + moduleId + "");

		Result result = sqlList(strsql, param);
		String getOps = "";
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<Object> theOps = (List<Object>) result.getRetVal();
			for (int m = 0; m < theOps.size(); m++) {
				getOps += ((Object[]) theOps.get(m))[0].toString() + ";";
			}
		}
		return getOps;
	}

	/*根据用户Id查询出应用于所有模块的控制分类权限*/
	public Result queryAllModScope(String userId,String deptCode) {
		List<String> param = new ArrayList<String>();
		String sql = "SELECT id,roleId,flag,tableName,fieldName,scopeValue,escopeValue,isAddLevel,isAllModules,rightUpdate,rightDelete,valuetype " +
				" FROM tblrolescope WHERE isAllModules='1' AND (roleid=? or (SELECT roleids FROM tblUserSunCompany tu WHERE tu.userid=?) LIKE '%;'+roleid+';%'  " +
				" or (SELECT roleids FROM tblUserSunCompany tu WHERE tu.userid=?) LIKE roleid+';%' ) ";
		param.add("" + userId + "");
		param.add("" + userId + "");
		param.add("" + userId + "");
		Result rsAll = this.sqlList(sql, param);
		ArrayList scopeRight = new ArrayList();
		if (rsAll.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List objList = (List) rsAll.retVal;
			for (int i = 0; i < objList.size(); i++) {
				Object[] obj = (Object[]) objList.get(i);
				

				LoginScopeBean bean = new LoginScopeBean();
				bean.setId(Integer.parseInt(obj[0].toString()));
				bean.setRoleId((String)obj[1]);
				bean.setFlag(obj[2].toString());
				bean.setTableName((String)obj[3]);
				bean.setFieldName((String)obj[4]);
				bean.setScopeValue((String)obj[5]);
				bean.setEscopeValue((String)obj[6]);
				bean.setIsAddLevel((String)obj[7]);
				bean.setIsAllModules((String)obj[8]);
				bean.setRightUpdate((Integer)obj[9]);
				bean.setRightDelete((Integer)obj[10]);		
				bean.setValuetype((String)obj[11]);
				
				if(bean.getFlag().equals("5") && bean.getScopeValue().equals("DEPT")){
					//如果是查看本部门单据，先翻释成用户所在部门ID
					bean.setScopeValue(deptCode+";");
				}
				
				scopeRight.add(bean);
			}
		}
		rsAll.setRetVal(scopeRight);
		return rsAll;
	}


	/**
	 * jdbc调用公共方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result msgSql(final String sql, final List param) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pstmt = conn.prepareStatement(sql);
							for (int i = 1; i <= param.size(); i++) {
								pstmt.setObject(i, param.get(i - 1));
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
	 * 添加角色
	 */
	@SuppressWarnings("unchecked")
	protected Result addRole(final String sCompanyID,final String userId, final String roleName, final String roleDesc, final String[] hidField, final String[] hidField1,
			final String mop) throws Exception {
		final Result rst = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				//取得 隐藏成本价   隐藏销售价	  隐藏客户	  隐藏供应商   隐藏往来单位
				String hidFields = "";
				for (int i = 0; hidField != null && i < hidField.length; i++) {
					hidFields += hidField[i] + ",";
				}
				Result rup;
				
				
				final RoleBean bean = new RoleBean();
				final String roleId = IDGenerater.getId();
				bean.setId(roleId);
				//重复判断
				ArrayList param = new ArrayList();
				param.add(roleName);
				Result r2 = list("select bean from  RoleBean bean where roleName=? ", param, session);
				if (r2.retCode == ErrorCanst.DEFAULT_SUCCESS && ((List) r2.retVal).size() > 0) {
					return ErrorCanst.MULTI_VALUE_ERROR;
				}
				bean.setCreateBy(userId);
				bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				bean.setLastUpdateBy(userId);
				bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				bean.setRoleName(roleName);
				bean.setRoleDesc(roleDesc);
				bean.setSCompanyID(sCompanyID);
				
				bean.setHiddenField(hidFields);
				
				System.out.println("-------------------------------1:"+bean.getId());
				rup = addBean(bean, session);
					
				if (rup.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return rup.retCode;
				}

				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//2、更新 销售出库允许低于成本价销售   销售出库允许低于限售价销售等等
							String[] hasRight = new String[8];
							for (int i = 0; i < hasRight.length; i++) {
								if (hidField1 != null && hidField1.length > 0 && GlobalsTool.isExistInArray(hidField1, String.valueOf(i))) {
									hasRight[i] = "1";
								} else {
									hasRight[i] = "0";
								}
							}
							for (int i = 0; i < 8; i++) {
								RightType rt = new RightType();
								rt.setId(roleId);
								if (i == 0) {
									rt.setRightType("allowLittleStocks");
									rt.setHasRight(hasRight[0]);
								} else if (i == 1) {
									rt.setRightType("allowMoreOrderOut");
									rt.setHasRight(hasRight[1]);
								} else if (i == 2) {
									rt.setRightType("allowMoreOrderIn");
									rt.setHasRight(hasRight[2]);
								} else if (i == 3) {
									rt.setRightType("allowLittleCostOut");
									rt.setHasRight(hasRight[3]);
								} else if (i == 4) {
									rt.setRightType("SalesAllowNegative");
									rt.setHasRight(hasRight[4]);
								} else if (i == 5) {
									rt.setRightType("allowCustomerCreditLimit");
									rt.setHasRight(hasRight[5]);
								} else if (i == 6) {
									rt.setRightType("UnderLimitprice");
									rt.setHasRight(hasRight[6]);
								} else if (i == 7) {
									rt.setRightType("WithOutSettleCys");
									rt.setHasRight(hasRight[7]);
								}
								String sql = "insert into tblRightType(id,rightType,hasRight) values(?,?,?)";
								PreparedStatement	pst = conn.prepareStatement(sql);
								pst.setString(1, rt.getId());
								pst.setString(2, rt.getRightType());
								pst.setString(3, rt.getHasRight());
								int m = pst.executeUpdate();
							}
							//3、修改模块。
							String[] moduleOperations = mop.split(",");

							

							ArrayList roleModuleList = new ArrayList();
							List roleModuleScopes = new ArrayList();
							if (moduleOperations != null && moduleOperations.length > 0) {
								for (String moduleOperation : moduleOperations) {
									if (moduleOperation != null && moduleOperation.length() > 0) {
										String sql = "insert into tblRoleModule(roleid,moduleOpId) values(?,?) ";
										PreparedStatement cs = conn.prepareStatement(sql);
										cs.setString(1, roleId);
										System.out.println("-------------------------------2:"+roleId);
										cs.setString(2, moduleOperation);
										cs.executeUpdate();
									}
								}
							}

						} catch (Exception ex) {
							BaseEnv.log.error("RoleMgt.addRole error:",ex);

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
	 * 修改角色
	 */
	@SuppressWarnings("unchecked")
	protected Result updateRole(final String fromUser, final String userId, final String roleName, final String roleDesc, final String[] hidField, final String[] hidField1, final String roleId,
			final String mop) throws Exception {
		final Result rst = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				//取得 隐藏成本价   隐藏销售价	  隐藏客户	  隐藏供应商   隐藏往来单位
				String hidFields = "";
				for (int i = 0; hidField != null && i < hidField.length; i++) {
					hidFields += hidField[i] + ",";
				}
				Result rup;
				//1、跟据fromUser更新角色
				if ("true".equals(fromUser)) {
					Result r1 = loadBean(roleId, EmployeeBean.class, session);
					if (r1.retCode != ErrorCanst.DEFAULT_SUCCESS) {
						return ErrorCanst.NULL_OBJECT_ERROR;
					}
					EmployeeBean bean = (EmployeeBean) r1.retVal;
					bean.setHiddenField(hidFields);
					bean.setLastUpdateBy(userId);
					bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					rup = updateBean(bean, session);
				} else {
					Result r1 = loadBean(roleId, RoleBean.class, session);
					if (r1.retCode != ErrorCanst.DEFAULT_SUCCESS) {
						return ErrorCanst.NULL_OBJECT_ERROR;
					}
					RoleBean bean = (RoleBean) r1.retVal;
					//重复判断
					ArrayList param = new ArrayList();
					param.add(roleName);
					param.add(roleId);
					Result r2 = list("select bean from  RoleBean bean where roleName=? and id <> ?", param, session);
					if (r2.retCode == ErrorCanst.DEFAULT_SUCCESS && ((List) r2.retVal).size() > 0) {
						return ErrorCanst.MULTI_VALUE_ERROR;
					}
					bean.setHiddenField(hidFields);
					bean.setRoleName(roleName);
					bean.setRoleDesc(roleDesc);
					bean.setLastUpdateBy(userId);
					bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					rup = updateBean(bean, session);
				}
				if (rup.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return rup.retCode;
				}

				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//2、更新 销售出库允许低于成本价销售   销售出库允许低于限售价销售等等
							String[] hasRight = new String[8];
							for (int i = 0; i < hasRight.length; i++) {
								if (hidField1 != null && hidField1.length > 0 && GlobalsTool.isExistInArray(hidField1, String.valueOf(i))) {
									hasRight[i] = "1";
								} else {
									hasRight[i] = "0";
								}
							}
							for (int i = 0; i < 8; i++) {
								RightType rt = new RightType();
								rt.setId(roleId);
								if (i == 0) {
									rt.setRightType("allowLittleStocks");
									rt.setHasRight(hasRight[0]);
								} else if (i == 1) {
									rt.setRightType("allowMoreOrderOut");
									rt.setHasRight(hasRight[1]);
								} else if (i == 2) {
									rt.setRightType("allowMoreOrderIn");
									rt.setHasRight(hasRight[2]);
								} else if (i == 3) {
									rt.setRightType("allowLittleCostOut");
									rt.setHasRight(hasRight[3]);
								} else if (i == 4) {
									rt.setRightType("SalesAllowNegative");
									rt.setHasRight(hasRight[4]);
								} else if (i == 5) {
									rt.setRightType("allowCustomerCreditLimit");
									rt.setHasRight(hasRight[5]);
								} else if (i == 6) {
									rt.setRightType("UnderLimitprice");
									rt.setHasRight(hasRight[6]);
								} else if (i == 7) {
									rt.setRightType("WithOutSettleCys");
									rt.setHasRight(hasRight[7]);
								}
								String sql = "update tblRightType set hasRight=? where id=? and rightType=?";
								PreparedStatement pst = conn.prepareStatement(sql);
								pst.setString(1, rt.getHasRight());
								pst.setString(2, rt.getId());
								pst.setString(3, rt.getRightType());
								int number = pst.executeUpdate();
								if (number == 0) {
									sql = "insert into tblRightType(id,rightType,hasRight) values(?,?,?)";
									pst = conn.prepareStatement(sql);
									pst.setString(1, rt.getId());
									pst.setString(2, rt.getRightType());
									pst.setString(3, rt.getHasRight());
									int m = pst.executeUpdate();
								}
							}
							//3、修改模块。
							String[] moduleOperations = mop.split(",");

							String sql = "delete from tblRoleModule where roleId = ? ";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, roleId);
							cs.executeUpdate();

							ArrayList roleModuleList = new ArrayList();
							List roleModuleScopes = new ArrayList();
							if (moduleOperations != null && moduleOperations.length > 0) {
								for (String moduleOperation : moduleOperations) {
									if (moduleOperation != null && moduleOperation.length() > 0) {
										sql = "insert into tblRoleModule(roleid,moduleOpId) values(?,?) ";
										cs = conn.prepareStatement(sql);
										cs.setString(1, roleId);
										cs.setString(2, moduleOperation);
										cs.executeUpdate();
									}
								}
							}

						} catch (Exception ex) {
							BaseEnv.log.error("RoleMgt.updateRole error:",ex);

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
}
