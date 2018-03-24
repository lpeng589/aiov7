package com.koron.oa.calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAMyCalendar;
import com.menyi.aio.bean.AlertBean;
import com.menyi.web.util.ErrorCanst;

/**
 * 日历数据操作管理类
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 21, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OAMyCalendarMgt extends DBManager {

	List<OAMyCalendar> list = new ArrayList<OAMyCalendar>();

	/**
	 * 添加日历
	 * @param oamycalendar
	 * @param msgWake
	 * @param alert
	 * @return
	 */
	public Result Insert(final OAMyCalendar oamycalendar,final String msgWake,
			final AlertBean alert) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				/*添加日历内容*/
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Result result = Insert(oamycalendar, msgWake,conn) ;
						rst.setRetCode(result.retCode) ;
						rst.setRetVal(result.getRetVal()) ;
					}
				});
				/*添加提醒*/
//				if(alert!=null){
//					addBean(alert, session) ;
//				}
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 删除提醒设置
	 * @param bean
	 * @return
	 */
	public Result deleteAlert(String alertId) {
		return deleteBean(alertId, AlertBean.class, "id") ;
	}
	
	/**
	 * 添加日历
	 * @param oamycalendar
	 * @param msgWake
	 * @param conn
	 * @return
	 */
	public Result Insert(OAMyCalendar oamycalendar ,String msgWake,Connection conn){
		Result rst = new Result() ;
		try {
			String sql = "insert into OAMyCalendar(id,CalendarDate,CalendarTitle,CalendarContext,assClient,assPeople,createTime,createBy) values(?,?,?,?,?,?,?,?)" ;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oamycalendar.getid());
			pstmt.setString(2, oamycalendar.getCalendarDate());
			pstmt.setString(3, oamycalendar.getCalendarTitle());
			pstmt.setString(4, oamycalendar.getCalendarContext());
			pstmt.setString(5, oamycalendar.getAssClient()) ;
			pstmt.setString(6, oamycalendar.getAssPeople()) ;
			pstmt.setString(7, oamycalendar.getcreateTime());
			pstmt.setString(8, oamycalendar.getcreateBy());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			rst.setRetVal(false);
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rst ;
		}
		return rst ;
	}
	

	/**
	 * 删除日历
	 * @param strIds
	 * @return
	 */
	public Result Delete(final String strIds) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from OAMyCalendar where id in ("+strIds+")" ;
							PreparedStatement pstmt = conn.prepareStatement(sql);
							int row = pstmt.executeUpdate();
							if (row > 0) {
								/*删除提醒设置*/
								sql = "delete from tblAlert where relationId in ("+strIds+")" ;
								pstmt = conn.prepareStatement(sql) ;
								pstmt.executeUpdate() ;
							}
							rst.setRetVal(true);
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

	public Result DeleteCalendarDate(final OAMyCalendar oamycalendar) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("delete from OAMyCalendar where 1=1 and CalendarDate=?");
							pstmt.setString(1, oamycalendar.getCalendarDate());
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

	public synchronized Result Update(final OAMyCalendar oamycalendar) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("update OAMyCalendar set id=?,classCode=?,CalendarTitle=?,CalendarContext=?,WakeUpBeginDate=?," +
											"WakeUpType=?,createBy=?,lastUpdateBy=?,createTime=?,lastUpdateTime=?,statusId=?,SCompanyID=?," +
											"BeforeDay=?,BeforeHour=?,BeforeMinute=?,NowWakeUpBeginDate=?,assClient=?,assPeople=? where 1=1 and id=?");
							pstmt.setString(1, oamycalendar.getid());
							pstmt.setString(2, oamycalendar.getclassCode());
							pstmt.setString(3, oamycalendar.getCalendarTitle());
							pstmt.setString(4, oamycalendar.getCalendarContext());
							pstmt.setString(5, oamycalendar.getWakeUpBeginDate());
							pstmt.setString(6, oamycalendar.getWakeUpType());
							pstmt.setString(7, oamycalendar.getcreateBy());
							pstmt.setString(8, oamycalendar.getlastUpdateBy());
							pstmt.setString(9, oamycalendar.getcreateTime());
							pstmt.setString(10, oamycalendar.getlastUpdateTime());
							pstmt.setInt(11, oamycalendar.getstatusId());
							pstmt.setString(12, oamycalendar.getSCompanyID());
							pstmt.setInt(13, oamycalendar.getBeforeDay());
							pstmt.setInt(14, oamycalendar.getBeforeHour());
							pstmt.setInt(15, oamycalendar.getBeforeMinute());
							pstmt.setString(16, oamycalendar.getNowWakeUpBeginDate());
							pstmt.setString(17, oamycalendar.getAssClient()) ;
							pstmt.setString(18, oamycalendar.getAssPeople()) ;
							pstmt.setString(19, oamycalendar.getid());
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

	public Result getMyCalendar(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("select * from OAMyCalendar where 1=1 and id=?");
							OAMyCalendar obj = new OAMyCalendar();
							pstmt.setString(1, id);
							ResultSet rs = pstmt.executeQuery();
							if (rs.next()) {
								obj.setid(rs.getString("id"));
								obj.setCalendarDate(rs.getString("CalendarDate"));
								obj.setCalendarTitle(rs.getString("CalendarTitle"));
								obj.setCalendarContext(rs.getString("CalendarContext"));
								obj.setWakeUpBeginDate(rs.getString("WakeUpBeginDate"));
								obj.setWakeUpType(rs.getString("WakeUpType"));
								obj.setWaleUpTimes(rs.getInt("WaleUpTimes"));
								obj.setSetp(rs.getInt("Setp")) ;
								obj.setstatusId(rs.getInt("statusId"));
								obj.setAssClient(rs.getString("assClient")) ;
								obj.setAssPeople(rs.getString("assPeople")) ;
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

	public Result SelectByCalendarDay(final String date, final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							String sql = "select id,CalendarDate,CalendarTitle,CalendarContext,WakeUpBeginDate from OAMyCalendar where 1=1 and datediff(day,CalendarDate,?)=0  and createBy=?";
							PreparedStatement pstmt = conn
									.prepareStatement(sql);
							List<OAMyCalendar> list = new ArrayList<OAMyCalendar>();
							pstmt.setString(1, date);
							pstmt.setString(2, userId);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								OAMyCalendar obj = new OAMyCalendar();
								obj.setid(rs.getString("id"));
								obj.setCalendarDate(rs.getString("CalendarDate"));
								obj.setCalendarTitle(rs.getString("CalendarTitle"));
								obj.setCalendarContext(rs.getString("CalendarContext"));
								obj.setWakeUpBeginDate(rs.getString("WakeUpBeginDate"));
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
	 * 查询当前月的日历
	 * @param date
	 * @param userId
	 * @return
	 */
	public Result SelectByCalendarMonth(final String date, final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							String sql = "select id,CalendarDate,CalendarTitle,CalendarContext,WakeUpBeginDate from OAMyCalendar where datediff(month,CalendarDate,?)=0  and createBy=?";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							List<OAMyCalendar> list = new ArrayList<OAMyCalendar>();
							pstmt.setString(1, date);
							pstmt.setString(2, userId);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								OAMyCalendar obj = new OAMyCalendar();
								obj.setid(rs.getString("id"));
								obj.setCalendarDate(rs.getString("CalendarDate"));
								obj.setCalendarTitle(rs.getString("CalendarTitle"));
								obj.setCalendarContext(rs.getString("CalendarContext"));
								obj.setWakeUpBeginDate(rs.getString("WakeUpBeginDate"));
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

	public Result SelectAllCalendar(final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							String sql = "select id,CalendarDate,CalendarTitle,CalendarContext,WakeUpBeginDate from OAMyCalendar where createBy = ?";
							PreparedStatement pstmt = conn
									.prepareStatement(sql);
							pstmt.setString(1, userId);

							List<OAMyCalendar> list = new ArrayList<OAMyCalendar>();
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								OAMyCalendar obj = new OAMyCalendar();
								obj.setid(rs.getString("id"));
								obj.setCalendarDate(rs
										.getString("CalendarDate"));
								obj.setCalendarTitle(rs
										.getString("CalendarTitle"));
								obj.setCalendarContext(rs
										.getString("CalendarContext"));
								obj.setWakeUpBeginDate(rs
										.getString("WakeUpBeginDate"));
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

	public List<OAMyCalendar> getMyCalendarByMonth(int year, int month,
			String userId) throws Exception {
		List<OAMyCalendar> newList = new ArrayList<OAMyCalendar>();
		int date = (int) getDays(year, month);
		for (int i = 1; i <= date; i++) {
			String d = year + "-"
					+ ((month < 10) ? "0" + month + "" : month + "") + "-"
					+ ((i < 10) ? "0" + i + "" : i + "");
			List<OAMyCalendar> list = (List<OAMyCalendar>) SelectByCalendarDay(
					d, userId).getRetVal();
			if (null != list) {
				for (OAMyCalendar calendar : list) {
					newList.add(calendar);
				}
			}
		}
		return newList;
	}

	public static long getDays(int year, int month) {
		Date d1 = new Date();
		Date d2 = new Date();
		d1.setYear(year);
		d1.setDate(1);
		d1.setMonth(month);

		d2.setYear(year);
		d2.setDate(1);
		d2.setMonth(month - 1);

		long l = d1.getTime() - d2.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		return day;

	}

	public Result updateWakeUpTimes(final String calendarId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update OAMyCalendar set WaleUpTimes=WaleUpTimes-1 where id = ?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, calendarId);
							ps.executeUpdate();
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
	 * 锟斤拷询要锟斤拷锟窖碉拷锟斤拷锟斤拷
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Result selectCalendarWakeUp(final String beginTime,final String endTime) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from OAMyCalendar where wakeUpBeginDate>? and wakeUpBeginDate<=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, beginTime);
							ps.setString(2, endTime) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<OAMyCalendar> listCalendar = new ArrayList<OAMyCalendar>() ;
							while(rss.next()){
								OAMyCalendar obj = new OAMyCalendar();
								obj.setid(rss.getString("id")) ;
								obj.setCalendarTitle(rss.getString("calendartitle")) ;
								obj.setCalendarContext(rss.getString("calendarcontext")) ;
								obj.setWakeUpType(rss.getString("wakeUpType")) ;
								obj.setcreateBy(rss.getString("createby")) ;
								listCalendar.add(obj) ;
							}
							rst.setRetVal(listCalendar) ;
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
	

	

}
