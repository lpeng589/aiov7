package com.menyi.sms.setting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.BillMsgBean;
import com.menyi.aio.bean.BillMsgDetBean;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.bean.TimingMsgBean;
import com.menyi.aio.bean.TimingMsgDetBean;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.AIOTelecomCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.SecurityLock;
/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 付湘鄂
 * </p>
 * 
 * @author 付湘鄂
 * @version 1.0
 */
public class NoteCenterMgt extends DBManager {

	/**
	 * 获得defineNote.xml的解析数据
	 * 
	 * @param loginId
	 *            String
	 * @return Result
	 */

	public Result setNoteData() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							List<TimingMsgBean> list = getTimingMsgData(connection);
							rs.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 判断所传递的消息内容，在同一分钟内是否已经向某个手机或Email发送过
	 * 
	 * @param loginId
	 *            String
	 * @return Result
	 */

	public Result getISSendNote(final String msg, final String time,
			List<TimingMsgDetBean> phone) {
		String phones = "";
		for (int i = 0; i < phone.size(); i++) {
			phones += "'" + phone.get(i).getphoneNumber() + "',";
		}
		if (phones.length() > 0) {
			phones = "(" + phones.substring(0, phones.length() - 1) + ")";
		}
		final String phonestr = phones;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							boolean flag = true;
							String sql = "select count(id) from tblSendSMS where receiveMobile in "
									+ phonestr
									+ " and subString(sendTime,0,17)='"
									+ time
									+ "' and content='" + msg + "'";
							Statement st = connection.createStatement();
							ResultSet rst = st.executeQuery(sql);
							if (rst.next()) {
								if (rst.getInt(1) > 0) {
									flag = false;
								}
							}
							rs.setRetVal(flag);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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

	public Result getISSendEmail(final String msg, final String time,
			List<TimingMsgDetBean> email) {
		String phones = "";
		for (int i = 0; i < email.size(); i++) {
			phones += "'" + email.get(i).getemail() + "',";
		}
		if (phones.length() > 0) {
			phones = "(" + phones.substring(0, phones.length() - 1) + ")";
		}
		final String phonestr = phones;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							boolean flag = true;
							String sql = "select count(a.id) from OAInnersMailSend a,OAInnersMailInfo b where a.MailInfoId=b.id and a.ToUserId in "
									+ phonestr
									+ "and subString(a.createTime,0,17)='"
									+ time + "' and MailContent='" + msg + "'";
							Statement st = connection.createStatement();
							ResultSet rst = st.executeQuery(sql);
							if (rst.next()) {
								if (rst.getInt(1) > 0) {
									flag = false;
								}
							}
							rs.setRetVal(flag);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 获得发送内容
	 * 
	 * @param bean
	 *            TimingNoteBean
	 * @return Result
	 */
	public Result execute(final TimingNoteBean bean, final String scompanyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							rs.setRetVal(bean.execute(connection, scompanyId)
									.getRetVal());
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 定时邮件、短信的发送
	 */
	public void doTimingSend() {
		List<TimingMsgBean> list = (ArrayList) setNoteData().getRetVal();
		if (list.size() > 0) { // 如果存在定时模板
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getStatus() == 0) { // 如果定时模板启用
					String timing = list.get(i).getTimeSet();
					String timingH = timing.substring(0, 2);
					String timingM = timing.substring(3, 5);
					String sysTime = BaseDateFormat.format(new Date(),
							BaseDateFormat.yyyyMMddHHmmss);
					String curHour = sysTime.substring(11, 13);
					String curMinute = sysTime.substring(14, 16);
					if (timingH.equals(curHour) && timingM.equals(curMinute)) { // 如果到达定时时间
						TimingNoteBean bean = (TimingNoteBean) BaseEnv.defineNoteMap
								.get(list.get(i).getSqlDefineName());
						if (bean != null) { // 如果定时模板对应的define存在
							Result rss = execute(bean, list.get(i)
									.getSCompanyID());
							List values = (ArrayList) rss.getRetVal();
							if (values.size() > 0) { // 如果发送的内容不为空
								List<TimingMsgDetBean> timingDets = list.get(i)
										.getTimingMsgDetBean();
								StringBuffer msg = new StringBuffer();
								for (int j = 0; j < values.size(); j++) {
									msg.append(values.get(j).toString() + "\n");
								}
								if (list.get(i).getType().equals("sendEmail")) { // 发送邮件
									boolean flag = true;
									if (timingDets.size() > 0) {
										Result rs = this.getISSendEmail(msg
												.toString(), sysTime.substring(
												0, 16), timingDets);
										if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
											flag = false;
										} else {
											flag = Boolean.parseBoolean(rs
													.getRetVal().toString());
										}
									}
									if (flag) {
										EMailMgt emailMgt = new EMailMgt();
										for (int k = 0; k < timingDets.size(); k++) {
											TimingMsgDetBean tmdb = timingDets
													.get(k);
											String key = "oa.subjects";
											String language = tmdb.getLanguageSort();
											String theme = null;
											if(language == null||language.equals("")){
												language = BaseEnv.defaultLocale.toString();
											}
											theme = GlobalsTool.getMessage(language, key);
										
											if (!tmdb.getemail().equals("")) {
												// 得到主题
												String emailSub = theme;
												Result sub = this
														.getEmailSub(list
																.get(i)
																.getSqlDefineName());
												if (sub.getRetCode() != ErrorCanst.DEFAULT_FAILURE
														&& sub.getRetVal() != null) {
													emailSub = sub.getRetVal()
															.toString();
												}
												try {
													emailMgt
															.sendOuterMailInfter(
																	list
																			.get(
																					i)
																			.getEmployyeId(),
																	tmdb
																			.getemail(),
																	emailSub,
																	msg
																			.toString(),
																	null);
												} catch (Exception e) {
													// block
													// e.printStackTrace();
													continue;
												}
											}
										}
									}
								} else if (list.get(i).getType().equals(
										"sendNote")) { // 发送短信
									if (BaseEnv.telecomCenter != null) {
										boolean flag = true;
										if (timingDets.size() > 0) {
											Result rs = this.getISSendNote(msg
													.toString(), sysTime
													.substring(0, 16),
													timingDets);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
												flag = false;
											} else {
												flag = Boolean
														.parseBoolean(rs
																.getRetVal()
  																.toString());
											}
										}
										if (flag) {
											String mobstr ="";
											for (int k = 0; k < timingDets
													.size(); k++) {
												if (!timingDets.get(k)
														.getphoneNumber()
														.equals("")) {
													mobstr += timingDets.get(k).getphoneNumber()+",";													
												}  
											}
											if(mobstr.length() >0){
												mobstr = mobstr.substring(0,mobstr.length() -1);
												BaseEnv.telecomCenter.send(msg.toString(),mobstr.split(","),"1");
											}
										}
									}

								} else if (list.get(i).getType().equals(
										"sendEmailAndNote")) { // 发送邮件及短信
									EMailMgt emailMgt = new EMailMgt();
									boolean flag = true;
									if (timingDets.size() > 0) {
										Result rs = this.getISSendEmail(msg
												.toString(), sysTime.substring(
												0, 16), timingDets);
										if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
											flag = false;
										} else {
											flag = Boolean.parseBoolean(rs
													.getRetVal().toString());
										}
									}
									if (flag) {
										for (int k = 0; k < timingDets.size(); k++) {
											TimingMsgDetBean tmdb = timingDets
													.get(k);
											if (!tmdb.getemail().equals("")) {
												// 得到主题
												String language = tmdb.getLanguageSort();
												if(language == null||language.equals("")){
													language = BaseEnv.defaultLocale.toString();
												}
												String theme = GlobalsTool.getMessage(language, "oa.subjects");
												String emailSub = theme;
												Result sub = this
														.getEmailSub(list
																.get(i)
																.getSqlDefineName());
												if (sub.getRetCode() != ErrorCanst.DEFAULT_FAILURE
														&& sub.getRetVal() != null) {
													emailSub = sub.getRetVal()
															.toString();
												}
												try {
													emailMgt
															.sendOuterMailInfter(
																	list
																			.get(
																					i)
																			.getEmployyeId(),
																	tmdb
																			.getemail(),
																	emailSub,
																	msg
																			.toString(),
																	null);
												} catch (Exception e) {
													// block
													// e.printStackTrace();
													continue;
												}
											}
										}
									}

									if (BaseEnv.telecomCenter != null) {
										flag = true;
										if (timingDets.size() > 0) {
											Result rs = this.getISSendNote(msg
													.toString(), sysTime
													.substring(0, 16),
													timingDets);
											if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
												flag = false;
											} else {
												flag = Boolean
														.parseBoolean(rs
																.getRetVal()
																.toString());
											}
										}
										if (flag) {
											String mobstr ="";
											for (int k = 0; k < timingDets
													.size(); k++) {
												if (!timingDets.get(k)
														.getphoneNumber()
														.equals("")) {
													mobstr += timingDets.get(k).getphoneNumber()+",";													
												}
											}
											if(mobstr.length() >0){
												mobstr = mobstr.substring(0,mobstr.length() -1);
												BaseEnv.telecomCenter.send(msg.toString(),mobstr.split(","),"1");
											}											
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * 得到定时模板的内容
	 * 
	 * @return Result
	 */
	public List getTimingMsgData(Connection con) {
		Connection conn = con;
		List<TimingMsgBean> list = new ArrayList<TimingMsgBean>();
		try {
			String sql = "select * from tblTimingMsg where Status=0";
			ResultSet rss = conn.createStatement().executeQuery(sql);
			while (rss.next()) {
				TimingMsgBean tmb = new TimingMsgBean();
				tmb.setid(rss.getString("id"));
				tmb.setSqlDefineName(rss.getString("SqlDefineName"));
				tmb.setType(rss.getString("type"));
				tmb.setStatus(rss.getInt("Status"));
				tmb.setTimeSet(rss.getString("timeSet"));
				tmb.setcreateBy(rss.getString("createBy"));
				tmb.setlastUpdateBy(rss.getString("lastUpdateBy"));
				tmb.setcreateTime(rss.getString("createTime"));
				tmb.setlastUpdateTime(rss.getString("lastUpdateTime"));
				tmb.setSCompanyID(rss.getString("SCompanyID"));
				tmb.setEmployyeId(rss.getString("employeeId"));
				tmb.setTimingMsgDetBean(getTimingMsgDetData(con, tmb.getid()));
				list.add(tmb);
			}
			rss.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;

	}

	/**
	 * 得到定时模板地址分配信息
	 * 
	 * @param con
	 * @param r_ref
	 * @return
	 */
	public List getTimingMsgDetData(Connection con, String r_ref) {
		Connection conn = con;
		List<TimingMsgDetBean> list = new ArrayList<TimingMsgDetBean>();
		try {
			String sql = "select tblTimingMsgDet.id,f_ref,tblEmployee.email,Mobile, tblTimingMsgDet.languageSort from tblTimingMsgDet,tblEmployee where tblTimingMsgDet.employeeId=tblEmployee.id and  f_ref='"
					+ r_ref + "'";
			ResultSet rss = conn.createStatement().executeQuery(sql);
			while (rss.next()) {
				TimingMsgDetBean tmdb = new TimingMsgDetBean();
				tmdb.setid(rss.getString("id"));
				tmdb.setf_ref(rss.getString("f_ref"));
				tmdb.setemail(rss.getString("email"));
				tmdb.setphoneNumber(rss.getString("Mobile"));
				tmdb.setLanguageSort(rss.getString("languageSort"));
				list.add(tmdb);
			}
			rss.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;

	}

	/**
	 * 单据邮件、短信的发送
	 * 
	 * @param tableName
	 * @param scompanyId
	 * @param loginId
	 */
	public void doBillSend(String tableName, String scompanyId, String loginId) {
		List<BillMsgBean> list = (ArrayList) getBillMsgData(scompanyId,
				tableName).getRetVal();
		if (list.size() > 0) { // 如果该单据模板存在
			BillNoteEmailBean bean = (BillNoteEmailBean) BaseEnv.defineBillMsgMap
					.get(list.get(0).getSqlDefineName());
			if (bean != null) {
				Result rss = execute(bean, loginId);
				List values = (ArrayList) rss.getRetVal();
				if (values.size() > 0) { // 如果发送的内容不为空
					List<BillMsgDetBean> billDets = list.get(0)
							.getBillMsgDets();
					StringBuffer msg = new StringBuffer();
					for (int j = 0; j < values.size(); j++) {
						msg.append(values.get(j).toString() + "\n");
					}
					if (list.get(0).getType().equals("sendEmail")) { // 发送邮件
						EMailMgt emailMgt = new EMailMgt();
						for (int k = 0; k < billDets.size(); k++) {
							BillMsgDetBean tmdb = billDets.get(k);
							if (!tmdb.getemail().equals("")) {
								try {
									emailMgt
											.sendOuterMailInfter(loginId, tmdb
													.getemail(), list.get(0)
													.getDisplay(), msg
													.toString(), null);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									// e.printStackTrace();
								}
							}
						}

					} else if (list.get(0).getType().equals("sendNote")) { // 发送短信

						if (BaseEnv.telecomCenter != null) {
							String mobstr ="";
							for (int k = 0; k < billDets
									.size(); k++) {
								if (!billDets.get(k)
										.getphoneNumber()
										.equals("")) {
									mobstr += billDets.get(k).getphoneNumber()+",";													
								}
							}
							if(mobstr.length() >0){
								mobstr = mobstr.substring(0,mobstr.length() -1);
								BaseEnv.telecomCenter.send(msg.toString(),mobstr.split(","),"1");
							}
						}
					} else if (list.get(0).getType().equals("sendEmailAndNote")) { // 发送邮件及短信
						EMailMgt emailMgt = new EMailMgt();
						for (int k = 0; k < billDets.size(); k++) {
							BillMsgDetBean tmdb = billDets.get(k);
							if (!tmdb.getemail().equals("")) {
								try {
									emailMgt
											.sendOuterMailInfter(loginId, tmdb
													.getemail(), list.get(0)
													.getDisplay(), msg
													.toString(), null);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									// e.printStackTrace();
								}
							}
						}
						if (BaseEnv.telecomCenter != null) {
							String mobstr ="";
							for (int k = 0; k < billDets
									.size(); k++) {
								if (!billDets.get(k)
										.getphoneNumber()
										.equals("")) {
									mobstr += billDets.get(k).getphoneNumber()+",";													
								}
							}
							if(mobstr.length() >0){
								mobstr = mobstr.substring(0,mobstr.length() -1);
								BaseEnv.telecomCenter.send(msg.toString(),mobstr.split(","),"1" );
							}
						}
					}

				}

			}
		}

	}

	public Result execute(final BillNoteEmailBean bean, final String loginId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							rs.setRetVal(bean.execute(connection, loginId)
									.getRetVal());
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 获得单据模板信息
	 * 
	 * @param scompanyId
	 * @param tableName
	 * @return
	 */
	public Result getBillMsgData(final String scompanyId, final String tableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("select * from tblBillMsg where 1=1 and SCompanyID like ? and SqlDefineName like ?");
							List<BillMsgBean> list = new ArrayList<BillMsgBean>();
							pstmt.setString(1, scompanyId);
							pstmt.setString(2, tableName);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								BillMsgBean obj = new BillMsgBean();
								obj.setid(rs.getString("id"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setworkFlowNode(rs
										.getString("workFlowNode"));
								obj.setworkFlowNodeName(rs
										.getString("workFlowNodeName"));
								obj.setSqlDefineName(rs
										.getString("SqlDefineName"));
								obj.setDisplay(rs.getString("Display"));
								obj.setType(rs.getString("Type"));
								obj.setStatus(rs.getInt("Status"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs
										.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs
										.getString("lastUpdateTime"));
								obj.setstatusId(rs.getInt("statusId"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								obj.setBillMsgDets(getBillMsgDetData(conn, obj
										.getid()));
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

	/**
	 * 获得单据模板地址信息
	 * 
	 * @param con
	 * @param r_ref
	 * @return
	 */
	public List getBillMsgDetData(Connection con, String r_ref) {
		Connection conn = con;
		List<BillMsgDetBean> list = new ArrayList<BillMsgDetBean>();
		try {
			String sql = "select * from tblBillMsgDet where f_ref='" + r_ref
					+ "'";
			ResultSet rss = conn.createStatement().executeQuery(sql);
			while (rss.next()) {
				BillMsgDetBean bmdb = new BillMsgDetBean();
				bmdb.setid(rss.getString("id"));
				bmdb.setf_ref(rss.getString("f_ref"));
				bmdb.setemail(rss.getString("email"));
				bmdb.setphoneNumber(rss.getString("phoneNumber"));
				list.add(bmdb);
			}
			rss.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;

	}

	/**
	 * 获得定时发送时的邮件主题
	 * 
	 * @param enumValue
	 * @return
	 */
	public Result getEmailSub(final String enumValue) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							String refEnumerationName = "";
							String sql = "select refEnumerationName from tblDBFieldInfo where  tableId=(select id from tblDBTableInfo where tableName='tblTimingMsg') and fieldName='SqlDefineName'";
							PreparedStatement psmt = connection
									.prepareStatement(sql);
							ResultSet rss = psmt.executeQuery();
							if (rss.next()) {
								refEnumerationName = rss
										.getString("refEnumerationName");
							}
							if (!"".equals(refEnumerationName)) {
								sql = "select zh_CN as display from tbllanguage where id=(select languageId from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='TimingMsg') and enumValue=?)";
								psmt = connection.prepareStatement(sql);
								psmt.setString(1, enumValue);
								rss = psmt.executeQuery();
								if (rss.next()) {
									rs.setRetVal(rss.getString("display"));
								}
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rs.setRetVal(null);
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
