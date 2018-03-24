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
import com.koron.oa.bean.OAMyCalendarBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;


/**
 * 
 * 
 * <p>Title:我的日历Mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-26
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class OAMyCalendarsMgt extends DBManager{

	
	
	/**
	 * 页面移动日历时调用
	 * @param oamycalendar
	 * @param msgWake
	 * @param alert
	 * @return
	 */
	public Result updateCalendar(final String start, final String end,final String eventid) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update OAMyCalendar set CalendarDate=?,endDate=? where id=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, start);
							ps.setString(2, end);
							ps.setString(3, eventid) ;
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
	 * 添加日历
	 * @param oamycalendar
	 * @return
	 */
	public Result addMyCalendar(final OAMyCalendarBean oamycalendar){
		return addBean(oamycalendar);
	}
	
	/**
	 * 根据日历Id查找日历信息
	 * @param eventid
	 * @return
	 */
	public Result queryCalendar(final String eventid){
		return loadBean(eventid, OAMyCalendarBean.class); 
	}
	
	/**
	 * 修改日历
	 * @param oamycalendar
	 * @return
	 */
	public Result updateMyCalendar(OAMyCalendarBean oamycalendar) {
		return updateBean(oamycalendar);
	}
	
	/**
	 * 删除日历
	 * @param eventid
	 * @return
	 */
	public Result deleteMyCalendar(final String eventid){
		return deleteBean(eventid, OAMyCalendarBean.class, "id");
	}
	
	/**
	 * 根据开始时间和结束时间查找数据
	 * @param start
	 * @param end
	 * @return
	 */
	public Result queryMonth(final String start, final String end, final String userId){
		List param = new ArrayList();
		//String hql = "from OAMyCalendarBean bean where bean.createBy=? and ( not (bean.calendarDate>? and bean.endDate>?) and not (bean.calendarDate<? and bean.endDate<?))";
		String hql = "from OAMyCalendarBean bean where bean.createBy=? and bean.calendarDate>? and bean.endDate<?";
		param.add(userId);
		param.add(datesCard(start,1));
		param.add(datesCard(end,2));
		return list(hql, param);
	}
	
	public String datesCard(final String dates,final int type){
		String date="";
		String s = "";
		if(type == 1){
			s = Integer.toString(Integer.parseInt(dates.substring(5, 7))-1);
		}else if(type == 2){
			s = Integer.toString(Integer.parseInt(dates.substring(5, 7))+1);
		}
		if(s.length()==1){
			s = "0"+s;
		}
		date = dates.substring(0, 5)+s+dates.substring(7,10);
		return date;
	}
	
	/**
	 * 查询日历信息
	 * @param userId
	 * @return
	 */
	public Result SelectAllCalendar(final String userId){
		List param = new ArrayList();
		String hql = "from OAMyCalendarBean bean where bean.createBy=?";
		param.add(userId);
		return list(hql, param);
	}
	/**
	 * 删除日历
	 * @param strIds
	 * @return
	 */
	public Result deleteCalaendars(String[] ids) {
		return deleteBean(ids, OAMyCalendarBean.class, "id");
	}
	
	/**
	 * 查询信息
	 * @param start
	 * @param end
	 * @param userId
	 * @return
	 */
	public Result queryCalendar(String userId, OAMyCalendarSearchForm oaForm){
		List param = new ArrayList();
		String hql = "from OAMyCalendarBean bean where 1=1 and bean.createBy=?";
		param.add(userId);
		if(oaForm.getCalendarDate() != null && !"".equals(oaForm.getCalendarDate())){
			hql += " and bean.calendarDate>=?";
			param.add(oaForm.getCalendarDate());
		}
		if(oaForm.getEndDate() != null && !"".equals(oaForm.getEndDate())){
			hql += " and bean.endDate<=?";
			param.add(oaForm.getEndDate());
		}
		if(oaForm.getKeyWord() != null && !"".equals(oaForm.getKeyWord())){
			hql += " and (bean.calendarTitle like '%"+ oaForm.getKeyWord() +"%' or bean.calendarContext like '%"+ oaForm.getKeyWord() +"%')";
		}
		hql += " order by bean.calendarDate desc";
		return list( hql, param, oaForm.getPageNo(), oaForm.getPageSize(), true);
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

	
	public Result queryAssPeopleName(final String keyIds){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	String sql = "select empFullName from tblEmployee where id in "+keyIds;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	String peopleName = "" ;
                        	while(rss.next()){
                        		peopleName += rss.getString("empFullName")+"," ;
                        	}
                        	rs.setRetVal(peopleName) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssPeopleName method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 查询客户的名称
     * @param userId
     * @return
     */
    public Result queryAssClient(final String clientIds){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	String sql = "select clientName from CRMClientInfo where id in "+clientIds;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	String clientName = "" ;
                        	while(rss.next()){
                        		clientName += rss.getString("clientName")+"," ;
                        	}
                        	rs.setRetVal(clientName) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssClient method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
	
//	/**
//	 * 删除日历
//	 * @param strIds
//	 * @return
//	 */
//	public Result Delete(final String strIds) {
//		final Result rst = new Result();
//		int retCode = DBUtil.execute(new IfDB() {
//			public int exec(Session session) {
//				session.doWork(new Work() {
//					public void execute(Connection conn) throws SQLException {
//						try {
//							String sql = "delete from OAMyCalendar where id in ("+strIds+")" ;
//							PreparedStatement pstmt = conn.prepareStatement(sql);
//							int row = pstmt.executeUpdate();
//							if (row > 0) {
//								/*删除提醒设置*/
//								sql = "delete from tblAlert where relationId in ("+strIds+")" ;
//								pstmt = conn.prepareStatement(sql) ;
//								pstmt.executeUpdate() ;
//							}
//							rst.setRetVal(true);
//						} catch (Exception ex) {
//							ex.printStackTrace();
//							rst.setRetVal(false);
//							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//							return;
//						}
//					}
//				});
//				return rst.getRetCode();
//			}
//		});
//		rst.setRetCode(retCode);
//		return rst;
//	}
//	
	
	
//	public Result getMyCalendar(final String id) {
//		final Result rst = new Result();
//		int retCode = DBUtil.execute(new IfDB() {
//			public int exec(Session session) {
//				session.doWork(new Work() {
//					public void execute(Connection connection)
//							throws SQLException {
//						try {
//							Connection conn = connection;
//							PreparedStatement pstmt = conn
//									.prepareStatement("select * from OAMyCalendar where 1=1 and id=?");
//							OAMyCalendar obj = new OAMyCalendar();
//							pstmt.setString(1, id);
//							ResultSet rs = pstmt.executeQuery();
//							if (rs.next()) {
//								obj.setid(rs.getString("id"));
//								obj.setCalendarDate(rs.getString("CalendarDate"));
//								obj.setCalendarTitle(rs.getString("CalendarTitle"));
//								obj.setCalendarContext(rs.getString("CalendarContext"));
//								obj.setWakeUpBeginDate(rs.getString("WakeUpBeginDate"));
//								obj.setWakeUpType(rs.getString("WakeUpType"));
//								obj.setWaleUpTimes(rs.getInt("WaleUpTimes"));
//								obj.setSetp(rs.getInt("Setp")) ;
//								obj.setstatusId(rs.getInt("statusId"));
//								obj.setAssClient(rs.getString("assClient")) ;
//								obj.setAssPeople(rs.getString("assPeople")) ;
//							}
//							rst.setRetVal(obj);
//						} catch (Exception ex) {
//							ex.printStackTrace();
//							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//							return;
//						}
//					}
//				});
//				return rst.getRetCode();
//			}
//		});
//		rst.setRetCode(retCode);
//		return rst;
//	}
 
}
