package com.koron.oa.publicMsg.newordain;

import java.util.List;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.koron.oa.bean.OAKnowledgeCenterFolder;
import com.koron.oa.bean.OAOrdainGroup;

import java.util.ArrayList;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.IfDB;
import com.dbfactory.hibernate.DBUtil;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import com.menyi.web.util.IDGenerater;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 科荣软件</p>
 *
 * @author 雷永辉
 * @version 1.0
 */
public class OAOrdainGroupMgt extends DBManager {
	static List<OAOrdainGroup> list = new ArrayList<OAOrdainGroup>();

	OAOrdainMgt mgt = new OAOrdainMgt();

	public OAOrdainGroupMgt() {
	}

	//根据上一级组ID得到组信息
	public List<OAOrdainGroup> getGroupByParentGrouopId(String parentFolderId) {
		List<OAOrdainGroup> list = null;
		Result rs = queryAllGroup(parentFolderId);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			list = (List<OAOrdainGroup>) rs.getRetVal();
		}
		return list;
	}

	public Result queryAllGroup(final String parentGroupId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String strsql = "select * from OAOrdainGroup where 1=1";
						if (null != parentGroupId && !"".equals(parentGroupId)) {
							strsql += " and classCode like (select classCode from OAOrdainGroup where id ='"+parentGroupId+"')+'%'";
						}
						try {
							Statement s = conn.createStatement();
							ResultSet rs = s.executeQuery(strsql);
							List<OAOrdainGroup> list = new ArrayList<OAOrdainGroup>();
							while (rs.next()) {
								OAOrdainGroup obj = new OAOrdainGroup();
								obj.setid(rs.getString("id"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setGroupName(rs.getString("GroupName"));
								obj.setparentGroupId(rs
										.getString("parentGroupId"));
								obj.setDescription(rs.getString("Description"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs
										.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs
										.getString("lastUpdateTime"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								list.add(obj);
							}
							rst.setRetVal(list);
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

	//插入语句
	public Result Insert(final OAOrdainGroup oaordaingroup) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("insert into OAOrdainGroup (id,classCode,GroupName,parentGroupId,Description,createBy,lastUpdateBy,createTime,lastUpdateTime,SCompanyID,PopedomUserIds) values(?,?,?,?,?,?,?,?,?,?,?)");
							pstmt.setString(1, oaordaingroup.getid());
							pstmt.setString(2, oaordaingroup.getclassCode());
							pstmt.setString(3, oaordaingroup.getGroupName());
							pstmt.setString(4, oaordaingroup.getparentGroupId());
							pstmt.setString(5, oaordaingroup.getDescription());
							pstmt.setString(6, oaordaingroup.getcreateBy());
							pstmt.setString(7, oaordaingroup.getlastUpdateBy());
							pstmt.setString(8, oaordaingroup.getcreateTime());
							pstmt.setString(9, oaordaingroup.getlastUpdateTime());
							pstmt.setString(10, oaordaingroup.getSCompanyID());
							pstmt.setString(11, oaordaingroup.getPopedomUserIds());
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetVal(false);
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

	//删除语句
	public Result Delete(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("delete from OAOrdainGroup where 1=1 and id=?");
							pstmt.setString(1, id);
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetVal(false);
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

	//根据父组目录删除子目录
	public Result DeleteByGroupId(final String parentGroupId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("delete from OAOrdainGroup where 1=1 and parentGroupId=?");
							pstmt.setString(1, parentGroupId);
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetVal(false);
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

	//修改语句
	public Result Update(final OAOrdainGroup oaordaingroup) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("update OAOrdainGroup set id=?,classCode=?,GroupName=?," +
											"parentGroupId=?,Description=?,createBy=?,lastUpdateBy=?,createTime=?," +
											"lastUpdateTime=?,SCompanyID=?,PopedomUserIds=? where 1=1 and id=?");
							pstmt.setString(1, oaordaingroup.getid());
							pstmt.setString(2, oaordaingroup.getclassCode());
							pstmt.setString(3, oaordaingroup.getGroupName());
							pstmt
									.setString(4, oaordaingroup
											.getparentGroupId());
							pstmt.setString(5, oaordaingroup.getDescription());
							pstmt.setString(6, oaordaingroup.getcreateBy());
							pstmt.setString(7, oaordaingroup.getlastUpdateBy());
							pstmt.setString(8, oaordaingroup.getcreateTime());
							pstmt.setString(9, oaordaingroup
									.getlastUpdateTime());
							pstmt.setString(10, oaordaingroup.getSCompanyID());
							pstmt.setString(11, oaordaingroup
									.getPopedomUserIds());
							pstmt.setString(12, oaordaingroup.getid());
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetVal(false);
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

	//查询语句
	public OAOrdainGroup getGroup(String id) {
		OAOrdainGroup obj = null;
		Result rs = Select(id);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			obj = (OAOrdainGroup) rs.getRetVal();
		}
		return obj;
	}

	//查询语句
	public Result Select(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("select * from OAOrdainGroup where 1=1 and id=?  and (isCatalog is null or isCatalog!=1)");
							OAOrdainGroup obj = new OAOrdainGroup();
							pstmt.setString(1, id);
							ResultSet rs = pstmt.executeQuery();
							if (rs.next()) {
								obj.setid(rs.getString("id"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setGroupName(rs.getString("GroupName"));
								obj.setparentGroupId(rs
										.getString("parentGroupId"));
								obj.setDescription(rs.getString("Description"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs
										.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs
										.getString("lastUpdateTime"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								obj.setPopedomUserIds(rs
										.getString("popedomUserIds"));
							}
							rst.setRetVal(obj);
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

	//根据用户名，父级节点，及组名判断是否有相同数据
	public boolean isExist(OAOrdainGroup group) {
		boolean talg = false;
		Result rs = isExist_DAl(group.getparentGroupId(), group.getGroupName());
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			OAOrdainGroup newgroup = (OAOrdainGroup) rs.getRetVal();
			if (null != newgroup) {
				if (!newgroup.getid().equals(group.getid())) {
					talg = true;
				}
			}
		}
		return talg;
	}

	public Result isExist_DAl(final String parentGroupId, final String groupName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						boolean talg = false;
						OAOrdainGroup obj = null;
						try {
							PreparedStatement pstmt = conn
									.prepareStatement("select * from OAOrdainGroup where 1=1 and parentGroupId = ? and GroupName = ?");
							pstmt.setString(1, parentGroupId);
							pstmt.setString(2, groupName);
							java.sql.ResultSet rs = pstmt.executeQuery();
							if (rs.next()) {
								obj = new OAOrdainGroup();
								obj.setid(rs.getString("id"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setGroupName(rs.getString("GroupName"));
								obj.setparentGroupId(rs
										.getString("parentGroupId"));
								obj.setDescription(rs.getString("Description"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs
										.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs
										.getString("lastUpdateTime"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								obj.setPopedomUserIds(rs
										.getString("popedomUserIds"));
							}
							rst.setRetVal(obj);
						} catch (Exception ex) {
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

	//得到一个文件夹及其子文件夹
	public List<OAOrdainGroup> getFolderAndChildenFolder(String folderId) {
		list.clear();
		addGroupList(folderId);
		return list;
	}

	public void addGroupList(String groupId) {
		List<OAOrdainGroup> newList = this.getGroupByParentGrouopId(groupId);
	    list.addAll(newList);
	}

	//得到知识中心根目录文件夹
	public OAOrdainGroup getRootGroup(String userId ,String groupName,String description) {
		OAOrdainGroup obj = null;
		List<OAOrdainGroup> list = getGroupByParentGrouopId("0");
		if (null != list) {
			if (list.size() > 0) {
				obj = list.get(0);
			}
		}
		if (null == obj) {
			//增加根目录
			OAOrdainGroup folder = new OAOrdainGroup();
			String id = IDGenerater.getId(); //自动生成一个ID
			String creatTime = BaseDateFormat.format(new java.util.Date(),
					BaseDateFormat.yyyyMMddHHmmss); //当前时间
			folder.setid(id);
			folder.setcreateBy(userId);
			folder.setparentGroupId("0");
			folder.setGroupName(groupName);
			folder.setDescription(description);
			folder.setlastUpdateBy(userId);
			folder.setcreateTime(creatTime);
			folder.setlastUpdateTime(creatTime);
			Insert(folder);
			list = getGroupByParentGrouopId("0");
			if (null != list) {
				if (list.size() > 0) {
					obj = list.get(0);
				}
			}

		}
		return obj;

	}

	//删除文件目录
	public boolean del(String groupId) {
		boolean talg = true;
		//得到该文件及其子文件
		try {
			List<OAOrdainGroup> list = this.getFolderAndChildenFolder(groupId);
			if (null != list) {
				if (list.size() > 0) {
					for (OAOrdainGroup g : list) {
						//删除文件夹下的文件
						/*mgt.DeleteOrdainByGroupId(g.getid());*/
						//删除文件夹目录
						this.Delete(g.getid());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			talg = false;
		}
		return talg;
	}

	//判断一个用户是否有权限
	public boolean hasPopemdom(String folderId, String userId) {
		boolean talg = false;
		Result rs = isHasPopemdom_DAl(folderId, userId);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			talg = Boolean.parseBoolean(rs.getRetVal().toString());
		}
		return talg;
	}

	public Result isHasPopemdom_DAl(final String folderId, final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						boolean talg = false;
						try {
							PreparedStatement pstmt = conn
									.prepareStatement("select * from OAOrdainGroup where 1=1and id = ? and PopedomUserIds like ?");
							pstmt.setString(1, folderId);
							pstmt.setString(2, "%" + userId + "%");
							java.sql.ResultSet rs = pstmt.executeQuery();
							if (rs.next()) {
								talg = true;
							}
							rst.setRetVal(talg);
						} catch (Exception ex) {
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
