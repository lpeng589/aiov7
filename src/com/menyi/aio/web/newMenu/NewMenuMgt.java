package com.menyi.aio.web.newMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class NewMenuMgt extends AIODBManager {

	// 第一次点击界面设置的时候 查询这个模块是否设置过。如果设置过就显示在页面
	public Result getQuery(final String keyId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from newMenuTable where keyId=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							ResultSet rss = pss.executeQuery();
							ArrayList newMenu = new ArrayList();
							while (rss.next()) {
								String id = rss.getString(1);
								String tdNameImgs = rss.getString(2);
								String statusType = rss.getString(3);
								newMenu.add(id);
								newMenu.add(tdNameImgs);
								newMenu.add(statusType);
							}
							result.setRetVal(newMenu);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ",ex);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}

	// 添加新的菜单
	public Result addMenu(final String tdNameImgs, final String statusType,
			final String keyId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "INSERT INTO newMenuTable VALUES (?,?,?,?);";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, IDGenerater.getId());
							pss.setString(2, tdNameImgs);
							pss.setString(3, statusType);
							pss.setString(4, keyId);
							int rss = pss.executeUpdate();

						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ",ex);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}

	// 修改
	public Result updateMenu(final String tdNameImgs, final String keyId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete FROM newMenuTable WHERE keyId=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							int rss = pss.executeUpdate();

							sql = "INSERT INTO newMenuTable VALUES (?,?,?,?);";
							pss = conn.prepareStatement(sql);
							pss.setString(1, IDGenerater.getId());
							pss.setString(2, tdNameImgs);
							pss.setString(3, "1");
							pss.setString(4, keyId);
							rss = pss.executeUpdate();
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ",ex);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}

	public Result getLinkQuery(final String classCode) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "SELECT classCode,linkAddress,zh_cn,tblName FROM tblModules tbm,tblLanguage   tbl WHERE tbl.id in( select modelName from tblLanguage  where tbm.classCode in("
									+ classCode + ") )  and tbm.classCode in(" + classCode + ")";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							HashMap<String, List> moduType = new HashMap<String, List>();
							while (rss.next()) {
								List newMenuModu = new ArrayList();
								String moduCode = rss.getString(1);
								String moduLink = rss.getString(2);
								String moduZh = rss.getString(3);
								String moduName = rss.getString(4);
								newMenuModu.add(moduLink);
								newMenuModu.add(moduZh);
								newMenuModu.add(moduName);
								moduType.put(moduCode, newMenuModu);
							}
							result.setRetVal(moduType);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ",ex);
							return;
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
	 * 查询模块下的报表
	 * @param keyId
	 * @param locale
	 * @param login
	 * @return
	 */
	public Result getReportList(final String keyId,final String locale,final LoginBean login) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select lan."+locale+" language,module.linkAddress from tblReportNavigation det "
									+ " left join tblModules module on module.classCode=det.reportName "
									+ " left join tblLanguage lan on module.modelName=lan.id where det.f_ref=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							ResultSet rss = pss.executeQuery();
							String classCodes = "";
							ArrayList<String[]> reportList = new ArrayList<String[]>();
							while (rss.next()) {
								String link = rss.getString("linkAddress");
								MOperation mop = (MOperation) login.getOperationMap().get(link);
								if(mop!=null){
									String[] report = new String[2];
									report[0] = rss.getString("language");
									report[1] = link;
									reportList.add(report);
								}
							}
							result.setRetVal(reportList);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("NewMenuMgt getReportList : ",ex);
							return;
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
	 * 功能列表
	 * @param keyId
	 * @param locale
	 * @param login
	 * @return
	 */
	public Result getFunList(final String keyId,final String locale,final LoginBean login) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select groupName from tblModuleFlowDetailre group by groupName";
							//PreparedStatement pss = conn.prepareStatement(sql);
							//ResultSet rss = pss.executeQuery();
							//HashMap<String, ArrayList<String[]>> valueMap = new HashMap<String, ArrayList<String[]>>();
							//while(rss.next()){
								sql = "select lan."+locale+" language,module.linkAddress from tblModuleFlowDetailre det " 
									+ " left join tblModules module on module.classCode=det.reportName "
									+ " left join tblLanguage lan on module.modelName=lan.id where f_ref=?";
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								ResultSet rss = pss.executeQuery();
								ArrayList<String[]> reportList = new ArrayList<String[]>();
								while (rss.next()) {
									String link = rss.getString("linkAddress");
									String opLink = link;
									if(link!=null && link.indexOf("moduleType=")>-1){
										opLink = opLink.substring(0,opLink.lastIndexOf("&"));
									}
									MOperation mop = (MOperation) login.getOperationMap().get(opLink);
									if(mop!=null){
										String[] report = new String[2];
										report[0] = rss.getString("language");
										report[1] = link;
										reportList.add(report);
									}
								}
								//valueMap.put(rss.getString("groupName"), reportList);
							//}
							result.setRetVal(reportList);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("NewMenuMgt getReport : ",ex);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}

	public List<Object> getAllMenu(String locale, String keyId) {

		ArrayList<String> param = new ArrayList<String>();
		String hql = "select m.id,lan."
				+ locale
				+ ",m.MainModule from tblModuleFlow m,tblLanguage lan where lan.id=m.ModuleName "
				+ "and statusId=0 and m.MainModule=(select MainModule from tblModuleFlow where id='"
				+ keyId + "') order by listOrder";
		Result result = sqlList(hql, param);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (List<Object>) result.getRetVal();
		} else {
			return null;
		}
	}

	public Result getAllMenu2() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "SELECT tmf.id,tlg.zh_CN FROM tblModuleFlow tmf,tblLanguage tlg WHERE  tlg.id=tmf.ModuleName ";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							List<String[]> MenuList = new ArrayList<String[]>();
							while (rss.next()) {
								String[] idAndName = new String[2];
								idAndName[0] = rss.getString(1);
								idAndName[1] = rss.getString(2);
								MenuList.add(idAndName);
							}
							result.setRetVal(MenuList);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OABBSForumMgt queryTopics : ",ex);
							return;
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
	 * 根据所属模块的类型查询该下面所有的模块
	 * @param locale
	 * @param moduleType
	 * @return
	 */
	public List<Object> getModuleMenu(String locale, String moduleType) {

		ArrayList<String> param = new ArrayList<String>();
		String hql = "select m.id,lan."
				+ locale
				+ ",m.MainModule from tblModuleFlow m,tblLanguage lan where lan.id=m.ModuleName "
				+ "and statusId=0 and m.MainModule="+moduleType+" order by listOrder";
		Result result = sqlList(hql, param);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (List<Object>) result.getRetVal();
		} else {
			return null;
		}
	}
	
	/**
	 * 根据模块的名称查询模块的id
	 * @param moduleName
	 * @return
	 */
	public Result getMenuKeyId(final String moduleName,final String locale,final String moduleType) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "SELECT tmf.id FROM tblModuleFlow tmf,tblLanguage tlg WHERE tlg.id=tmf.ModuleName ";
							sql += " and tlg."+locale+"='"+moduleName+"' and tmf.mainModule="+moduleType;
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							String keyId = "";
							if(rss.next()) {
								keyId = rss.getString("id");
							}
							result.setRetVal(keyId);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("NewMenuMgt getMenuKeyId : ",ex);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	
}
