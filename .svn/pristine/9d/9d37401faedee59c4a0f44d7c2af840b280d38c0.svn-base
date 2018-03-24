package com.menyi.sms.note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.bean.SendSMSBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;

public class NoteMgt {

	/**
	 * 查询已发送短信列表
	 * @param receiveMobile
	 * @param beginTime
	 * @param endTime
	 * @param state
	 * @return
	 */
	public Result querySendSMS(final String receiveMobile, final String beginTime, final String endTime, final String state, final String sender, final String content,
			final HttpServletRequest request, final int pageNo, final int pageSize) {
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String sql = "";
		if (!"0".equals(state)) {
			sql = "select a.id,b.EmpFullName as sender,a.sendTime,receiveMobile,receiveName,content,status,case status when 2 then '发送成功' when -1 then '发送失败' else '' end as statestr  ,ROW_NUMBER() over(order by a.sendtime desc) as row_id from tblSendSMS a left join tblEmployee b on a.createBy=b.id where 1=1 ";
			if (content != null && !"".equals(content)) {
				sql += "and content like '%" + content + "%'";
			}
			if (receiveMobile != null && receiveMobile.length() > 0) {
				sql += "and a.receiveMobile='" + receiveMobile + "' ";
			}
			if (beginTime != null && beginTime.length() > 0) {
				sql += "and substring(a.sendTime,1,10) >='" + beginTime + "' ";
			}
			if (endTime != null && endTime.length() > 0) {
				sql += "and substring(a.sendTime,1,10) <='" + endTime + "' ";
			}
			if (state != null && state.length() > 0) {
				sql += "and a.status=" + state;
			}
			if (sender != null && sender.length() > 0) {
				sql += "and b.EmpFullName like '%" + sender + "%'";
			} else {//没有查询具体用户，默认查自己的
				sql += "and b.EmpFullName = '" + lg.getEmpFullName() + "'";
			}
		}
		if ("0".equals(state) || "".equals(state)) {
			if (sql.length() > 0)
				sql += " \n union \n";
			sql += "select  a.id,b.EmpFullName as sender,a.sendTime,receiveMobile,receiveName,content,0 as status,'未发送' as statestr ,ROW_NUMBER() over(order by a.sendtime desc) as row_id from tblNoSendSMS a left join tblEmployee b on a.createBy=b.id where 1=1 ";
			if (content != null && !"".equals(content)) {
				sql += "and content like '%" + content + "%'";
			}
			if (receiveMobile != null && receiveMobile.length() > 0) {
				sql += "and a.receiveMobile='" + receiveMobile + "' ";
			}
			if (beginTime != null && beginTime.length() > 0) {
				sql += "and substring(a.sendTime,1,10) >='" + beginTime + "' ";
			}
			if (endTime != null && endTime.length() > 0) {
				sql += "and substring(a.sendTime,1,10) <='" + endTime + "' ";
			}
			if (sender != null && sender.length() > 0) {
				sql += "and b.EmpFullName like '%" + sender + "%'";
			} else {//没有查询具体用户，默认查自己的
				sql += "and b.EmpFullName = '" + lg.getEmpFullName() + "'";
			}
		}

		AIODBManager aioMgt = new AIODBManager();
		Result rs = aioMgt.sqlListMap(sql.toString(), null, pageNo, pageSize);
		List listMap = (List) rs.retVal;
		List list = new ArrayList();
		for (int i = 0; i < listMap.size(); i++) {
			HashMap map = (HashMap) listMap.get(i);
			String[] objs = new String[8];
			objs[0] = (String) map.get("id");
			objs[1] = map.get("status").toString();
			objs[2] = (String) map.get("statestr");
			objs[3] = (String) map.get("sender");
			objs[4] = (String) map.get("sendTime");
			objs[5] = (String) map.get("receiveMobile");
			objs[6] = (String) map.get("receiveName");
			objs[7] = (String) map.get("content");
			list.add(objs);
		}
		rs.setRetVal(list);
		rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
		return rs;
	}

	/**
	 * 查询已发送短信详细
	 * @param keyId
	 * @return
	 */
	public Result querySendSMSById(final String keyId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							String sql = "select * from tblSendSMS where id=? ";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, keyId);
							ResultSet rs = pstmt.executeQuery();
							List list = new ArrayList();
							while (rs.next()) {
								String[] obj = new String[8];
								obj[0] = rs.getString("smsType");
								obj[1] = rs.getString("receiveMobile");
								obj[2] = rs.getString("sendTime");
								obj[3] = rs.getString("status");
								obj[4] = rs.getString("content");
								obj[5] = rs.getString("remark");
								obj[6] = rs.getString("id");
								obj[7] = rs.getString("feeTimes");
								list.add(obj);
							}
							rst.setRetVal(list);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 查询已接收短信列表
	 * @param sendMobile
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Result queryReceivedSMS(final String sendMobile, final String beginTime, final String endTime) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							String sql = "select * from tblReceivedSMS where 1=1 ";
							if (sendMobile != null && sendMobile.length() > 0) {
								sql += "and deliverMobile='" + sendMobile + "' ";
							}
							if (beginTime != null && beginTime.length() > 0) {
								sql += "and deliverTime>='" + beginTime + "' ";
							}
							if (endTime != null && endTime.length() > 0) {
								sql += "and deliverTime<='" + endTime + "' ";
							}
							sql += " order by deliverTime desc";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							ResultSet rs = pstmt.executeQuery();
							List list = new ArrayList();

							while (rs.next()) {
								String[] obj = new String[5];
								obj[0] = rs.getString("id");
								obj[1] = rs.getString("BusinessType");
								obj[2] = rs.getString("deliverMobile");
								obj[3] = rs.getString("deliverTime");
								list.add(obj);
							}
							rst.setRetVal(list);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 查询已接收短信详细
	 * @param keyId
	 * @return
	 */
	public Result queryReceivedSMSById(final String keyId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							String sql = "select * from tblReceivedSMS where id=? ";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, keyId);
							ResultSet rs = pstmt.executeQuery();
							List list = new ArrayList();
							while (rs.next()) {
								String[] obj = new String[5];
								obj[0] = rs.getString("BusinessType");
								obj[1] = rs.getString("deliverMobile");
								obj[2] = rs.getString("deliverTime");
								obj[3] = rs.getString("content");
								list.add(obj);
							}
							rst.setRetVal(list);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 删除短信记录
	 * @param keyId
	 * @return
	 */
	public Result deleteSMS(final String[] keyIds) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							for (int j = 0; j < keyIds.length; j++) {
								String[] t = keyIds[j].split(":");
								String sql = "";
								if (t[0].equals("0")) {
									sql = "delete from tblNoSendSMS where id = ? ";
								} else {
									sql = "delete from tblSendSMS where id = ? ";
								}
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1, t[1]);
								int n = pss.executeUpdate();
								if (n > 0) {
									result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								} else {
									result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

	/**
	 * 查询短信记录
	 * @param keyId
	 * @return
	 */
	public Result querySMS(final String[] keyIds) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							List list = new ArrayList();
							for (int j = 0; j < keyIds.length; j++) {
								String[] t = keyIds[j].split(":");
								String sql = "";
								if (t[0].equals("0")) {
									sql = "select * from tblNoSendSMS where id = ? ";
								} else {
									sql = "select * from tblSendSMS where id = ? ";
								}								
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1, t[1]);
								ResultSet rss = pss.executeQuery();
								
								while (rss.next()) {
									String[] obj = new String[3];
									obj[0] = rss.getString("receiveMobile");
									obj[1] = rss.getString("content");
									list.add(obj);
								}
							}
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
}
