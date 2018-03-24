package com.menyi.aio.web.systemset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.wechat.common.util.WXSetting;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.ReportsDetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.ReportField;
import com.menyi.web.util.SystemState;

/**
 * <p>Title: </p>
 *
 * <p>Description: 报表设置的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class SystemSetMgt extends DBManager {

	
	public Result querySysDeployByCode(final String sysCode) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//查询所有系统配置 SysName=zh_TW:是否允许文件传输;zh_CN:是否允许文件传输;
							String sql = "select a.id,a.ModuleType,a.SysChValue,a.createTime,a.DefaultValue,a.createBy,a.listOrder,a.statusId,a.Remark,a.IsUsld,a.paramType,a.groupId,'zh_TW:'+isnull(zh_TW,'')+';zh_CN:'+isnull(zh_CN,'')+';en:'+isnull(en,'')+';' as sysName " +
									" from tblSysDeploy a left join tblLanguage b on a.SysName=b.id   " +
									" where a.sysCode='"+sysCode+"' ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							HashMap<String, String> dmap = new HashMap<String, String>();
							if (rss.next()) {
								dmap.put("id", rss.getString("id"));
								dmap.put("ModuleType", rss.getString("ModuleType"));
								dmap.put("SysChValue", rss.getString("SysChValue"));
								dmap.put("createTime", rss.getString("createTime"));
								dmap.put("DefaultValue", rss.getString("DefaultValue"));
								dmap.put("createBy", rss.getString("createBy"));
								dmap.put("listOrder", rss.getString("listOrder"));
								dmap.put("statusId", rss.getString("statusId"));
								dmap.put("Remark", rss.getString("Remark"));
								dmap.put("IsUsld", rss.getString("IsUsld"));
								dmap.put("SysName", rss.getString("sysName"));
								dmap.put("paramType", rss.getString("paramType"));
								dmap.put("groupId", rss.getString("groupId"));
							}
							rs.setRetVal(dmap);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	 * 查询部门全称
	 * @param reportNumber
	 * @return
	 */
	public Result querySysDeploy(final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//查询所有系统配置
							String sql = "select a.ModuleType,a.paramType,groupId,b."+locale+" as SysName,a.SysCode,a.Setting,a.statusId ,a.Remark  "
									+ " from tblSysDeploy a join tblLanguage b on a.SysName=b.id " +
											" where a.IsUsld=1 order by a.ModuleType ,isnull(a.listOrder,0) ,a.SysCode ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							ArrayList<Object[]> list = new ArrayList<Object[]>();
							while (rss.next()) {
								Object[] os = new Object[10];
								os[0] = rss.getString("ModuleType");
								os[1] = rss.getString("groupId");
								os[3] = rss.getString("SysName");
								os[4] = rss.getString("SysCode");
								os[5] = rss.getString("Setting");
								os[6] = rss.getString("statusId");
								os[7] = rss.getString("Remark");
								os[8] = rss.getString("paramType");
								
								list.add(os);
							}
							sql = " select c.ModuleType,a.SysCode,(case len(isnull("+locale+",''))  when  0 then a.SysChValue else "+locale+" end) as valueName,SysNumerValue " +
									" from tblSysValue a join tblSysDeploy c on a.SysCode=c.SysCode left join tblLanguage b on a.SysChValue=b.id   where c.IsUsld=1 order by a.listOrder";
							ps = conn.prepareStatement(sql);
							rss = ps.executeQuery();
							while (rss.next()) {
								String moduleType = rss.getString("ModuleType");
								String sysCode = rss.getString("SysCode");
								for(Object[] os :list){
									if(os[4].equals(sysCode)){
										ArrayList al = (ArrayList)os[9];
										if(al == null){
											al = new ArrayList();
											os[9] = al;
										}
										al.add(new String[]{rss.getString("valueName"),rss.getString("SysNumerValue")});
										break;
									}
								}
							}
							
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	 * 查目录分组
	 * @param locale
	 * @return
	 */
	public Result querySysGroup(final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//查询所有系统配置
							String sql = "select id,groupName from tblSysGroup order by orderList ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							ArrayList<Object[]> list = new ArrayList<Object[]>();
							ArrayList<Object[]> list1 = new ArrayList<Object[]>();
							ArrayList<Object[]> list2 = new ArrayList<Object[]>();
							while (rss.next()) {
								Object[] os = new Object[2];
								os[0] = rss.getString("id");
								os[1] = rss.getString("groupName");									
								list.add(os);
							}							
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	 * 启动远程助手
	 * @param locale
	 * @return
	 */
	public Result startRemote(final String dogId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//先删除对应加密狗的记录
							String sql = "delete from tblRemoteAssis where dogId=? ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, dogId);
							ps.execute();
							
							sql = "insert into tblRemoteAssis(dogId,isStart) values(?,?) ";
							ps = conn.prepareStatement(sql);
							ps.setString(1, dogId);
							ps.setInt(2, 0);
							ps.execute();
							
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	 * 启动远程助手
	 * @param locale
	 * @return
	 */
	public boolean getRemote(final String dogId) {
		final Result rs = new Result();
		
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//先删除对应加密狗的记录
							String sql = "select isStart from tblRemoteAssis where dogId=? ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, dogId);
							ResultSet rset =ps.executeQuery();
							if(rset.next()){
								int isStart = rset.getInt(1);
								if(isStart == 0){
									rs.retVal = "true";
								}
							}
														
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs.retVal != null && rs.retVal.equals("true");
	}
	
	/**
	 * 获取微信企业号配置
	 * @return
	 */
	public Result getWxSet() {
		final Result rs = new Result();	
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							HashMap<String, Map> maps = new HashMap<String, Map>(); 
							String sql = "select * from tblWxSet";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rset =ps.executeQuery();
							if(rset.next())
								do {
									HashMap<String, String> map =new HashMap<String, String>();
									map.put("Flag", rset.getString("Flag"));
									map.put("CorpID", rset.getString("CorpID"));
									map.put("Secret", rset.getString("Secret"));
									map.put("AgentId_check", rset.getString("AgentId_check"));
									map.put("RemoteUrl", rset.getString("RemoteUrl"));
									maps.put(rset.getString("KeyName"), map);
								} while (rset.next());
							rs.setRetVal(maps);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
	 * 微信企业号配置更新
	 * @return
	 */
	public Result updateWxSet(final String Flag,final String CorpID,final String Secret,final String AgentId_check,final String RemoteUrl, final String keyName) {
		final Result rs = new Result();	
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblWxSet where KeyName = ?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, keyName);
							ResultSet rset = ps.executeQuery();
							if (rset.next()) {
								sql = "update tblWxSet set Flag=?,CorpID=?,Secret=?,AgentId_check=?,RemoteUrl=? where KeyName = ?";
							} else {
								sql = "insert into tblwxset(Flag, CorpID, Secret, AgentId_check, RemoteUrl, KeyName) values(?, ?, ?, ?, ?, ?)";
							}
							ps = conn.prepareStatement(sql);
							ps.setString(1, Flag);
							ps.setString(2, CorpID);
							ps.setString(3, Secret);
							ps.setString(4, AgentId_check);
							ps.setString(5, RemoteUrl);
							ps.setString(6, keyName);
							ps.execute();					
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
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
}
