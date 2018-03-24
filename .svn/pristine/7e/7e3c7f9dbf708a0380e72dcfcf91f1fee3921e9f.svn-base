package com.koron.hr.workRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.SquadInfoBean;
import com.menyi.aio.bean.SquadSect;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;


public class DutyPeriodsMgt extends DBManager {

	WorkRuleReportMgt mgt = new WorkRuleReportMgt();

	public Result findAll(final DutyPeriodsForm periodsForm,final int startNo,final int endNo) {
		final Result rs = new Result();
		final StringBuffer sqlCount = new StringBuffer();
		final StringBuffer sql = new StringBuffer();
		
		switch (periodsForm.getPeriodsType()) {
		case 0:
			sqlCount.append("select count(0) from view_EmpArrangePeriods as periods where 1=1 ");
			sql.append("select * from (select row_number() over(order by date desc) as row_No,");
			sql.append("* from view_EmpArrangePeriods as periods where 1=1 ");
			
			if(!"".equals(periodsForm.getEmployeeNo()) && null != periodsForm.getEmployeeNo()){
				sqlCount.append(" and periods.EmpNumber = '"+periodsForm.getEmployeeNo()+"' ");
				sql.append(" and periods.EmpNumber = '"+periodsForm.getEmployeeNo()+"' ");
			}
			
			break;
		case 1:
			sqlCount.append("select count(0) from view_DecArrangePeriods as periods where 1=1 ");
			sql.append("select * from (select row_number() over(order by date desc) as row_No,");
			sql.append("* from view_DecArrangePeriods as periods where 1=1 ");
			
			if(!"".equals(periodsForm.getDepartmentNo()) && null != periodsForm.getDepartmentNo()){
				sqlCount.append(" and periods.DeptCode = '"+periodsForm.getDepartmentNo()+"' ");
				sql.append(" and periods.DeptCode = '"+periodsForm.getDepartmentNo()+"' ");
			}
			break;
		default:
			break;
		}
		if(!"".equals(periodsForm.getPeriodsNo()) && null != periodsForm.getPeriodsNo()){
			sqlCount.append(" and periods.periodsNo = '"+periodsForm.getPeriodsNo()+"' ");
			sql.append(" and periods.periodsNo = '"+periodsForm.getPeriodsNo()+"' ");
		}
		if(!"".equals(periodsForm.getSquadEnactmentNo()) && null != periodsForm.getSquadEnactmentNo()){
			sqlCount.append(" and periods.squadEnactmentNo = '"+periodsForm.getSquadEnactmentNo()+"' ");
			sql.append(" and periods.squadEnactmentNo = '"+periodsForm.getSquadEnactmentNo()+"' ");
		}
		sqlCount.append(" and periods.date >= '"+periodsForm.getStartDutyDate()+"' and periods.date <= '"+periodsForm.getEndDutyDate()+"' ");
		sql.append(" and periods.date >= '"+periodsForm.getStartDutyDate()+"' and periods.date <= '"+periodsForm.getEndDutyDate()+"' ");
		
		sql.append(" ) as temp where temp.row_No between "+startNo+" and "+endNo);
		
		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						
						try {
							Statement state = con.createStatement();
							
							ResultSet rss = state.executeQuery(sqlCount.toString());
							if(rss.next()){
								rs.setTotalPage(Integer.parseInt(rss.getString(1)));
							}
							
							rss = state.executeQuery(sql.toString());
							ArrayList<DutyPeriodsForm> periodses = new ArrayList<DutyPeriodsForm>();
							if(periodsForm.getPeriodsType() == 0){
								while (rss.next()) {
									periodses.add(new DutyPeriodsForm(rss
											.getString("periodsNo"),rss
											.getString("periodsName"),rss
											.getString("EmpNumber"), rss
											.getString("EmpFullName"), rss
											.getString("DeptFullName"), rss
											.getString("squadEnactmentNo"), rss
											.getString("squadEnactmentName"), rss
											.getString("date"),
											periodsForm.getPeriodsType()));
								}
							}else if(periodsForm.getPeriodsType() == 1){
								while (rss.next()) {
									periodses.add(new DutyPeriodsForm(rss
											.getString("periodsNo"),rss
											.getString("periodsName"),rss
											.getString("DeptCode"), rss
											.getString("DeptFullName"), rss
											.getString("squadEnactmentNo"), rss
											.getString("squadEnactmentName"), rss
											.getString("date"),
											periodsForm.getPeriodsType()));
								}
							}
							
							rs.setRetVal(periodses);
						} catch (Exception ex) {
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

	public String getDateByNull(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}

	public Result addPeriods(final List empList,final List decList,final List<KeyPair> periods, 
			final String id,final String periodsName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						StringBuffer sqlBuffer = new StringBuffer();
						sqlBuffer
								.append("insert into tblDutyPeriodsByDate (id,periodsNo,squadEnactmentNo,date,createBy,createTime)");
						sqlBuffer.append(" values(?,?,?,?,?,?)");
						try {
							PreparedStatement ps = con
									.prepareStatement(sqlBuffer.toString());
							String classCode = getClassCode(IDGenerater.getId(),"DP");
							String createTime = new SimpleDateFormat("yyyy-MM-dd")
							.format(new Date());
							for (KeyPair keyPair : periods) {
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, classCode);
								ps.setString(3, keyPair.getValue());
								ps.setString(4, keyPair.getName());
								ps.setString(5, id);
								ps.setString(6, createTime);
								ps.executeUpdate();
							}
							sqlBuffer = new StringBuffer();
							sqlBuffer.append("insert into tblPeriods (id,periodsNo,createDate,createBy,periodsName) values(?,?,?,?,?)");
							ps = con.prepareStatement(sqlBuffer.toString());
							ps.setString(1, IDGenerater.getId());
							ps.setString(2, classCode);
							ps.setString(3, createTime);
							ps.setString(4, id);
							ps.setString(5, periodsName);
							ps.executeUpdate();
							
							sqlBuffer = new StringBuffer();
							sqlBuffer.append("insert into tblDecArrangeSquadList (id,periodsNo,DepartmentNo,createBy,createTime) values (?,?,?,?,convert(varchar(19),getdate(),120))");
							ps = con.prepareStatement(sqlBuffer.toString());
							for (Object decNumber : decList) {
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, classCode);
								ps.setString(3, decNumber.toString());
								ps.setString(4, id);
								ps.executeUpdate();
							}
							
							sqlBuffer = new StringBuffer();
							sqlBuffer.append("insert into tblEmpArrangeSquadList (id,periodsNo,employeeNo,createBy,createTime) values (?,?,?,?,convert(varchar(19),getdate(),120))");
							ps = con.prepareStatement(sqlBuffer.toString());
							for (Object empNumber : empList) {
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, classCode);
								ps.setString(3, empNumber.toString());
								ps.setString(4, id);
								ps.executeUpdate();
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
						}

					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	

	public String getClassCode(String id,String No){
		StringBuffer classCode = new StringBuffer(No);
		classCode.append(new SimpleDateFormat("yyMMdd").format(new Date()));
		classCode.append(id.substring(id.length()-4));
		return classCode.toString();
		
	}
	

	public Result findSquadEnactment(final String squadEnactmentNo,final String squadEnactmentName,
			int startNo,int endNo){
		final Result result = new Result();
		final StringBuffer sqlCount = new StringBuffer("select count(0) from view_SquadEnactment where 1=1 ");
		final StringBuffer sql = new StringBuffer("select * from (");
		sql.append("select row_number() over(order by squadEnactmentNo) as rowNo,* from view_SquadEnactment where 1=1 ");
		if(!"".equals(squadEnactmentNo) && null != squadEnactmentNo){
			sqlCount.append(" and squadEnactmentNo = '"+squadEnactmentNo+"'");
			sql.append(" and squadEnactmentNo = '"+squadEnactmentNo+"'");
		}
		if(!"".equals(squadEnactmentName) && null != squadEnactmentName){
			sqlCount.append(" and squadEnactmentName = '"+squadEnactmentName+"'");
			sql.append(" and squadEnactmentName = '"+squadEnactmentName+"'");
		}
		sql.append(" ) as temp where rowNo between "+startNo+" and "+endNo);
		int retCode = DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con){
						try{
							Statement state = con.createStatement();
							ResultSet rs = state.executeQuery(sql.toString());
							List<SquadInfoBean> squads = new ArrayList<SquadInfoBean>();
							while (rs.next()){
								squads.add(new SquadInfoBean(
										rs.getString("squadEnactmentNo"),
										rs.getString("squadEnactmentName"),
										new SquadSect(
												rs.getString("onDutyTime"),
												rs.getString("offDutyTime"),
												rs.getInt("onDutyAvailabilityTime"),
												rs.getInt("offDutyAvailabilityTime"),
												rs.getString("squadSectType")
												)
										));
							}
							rs = state.executeQuery(sqlCount.toString());
							if(rs.next()){
								result.setTotalPage(Integer.parseInt(rs.getString(1)));
							}
							result.setRetVal(squads);
						}catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	

	public Result addSquadEnactment(final String squadEnactmentName,final List<SquadSect> squadSects,final String createBy){
		final Result result = new Result();
		final String sqlSE = "insert into tblSquadEnactment (id,squadEnactmentNo,squadEnactmentName,createBy) values (?,?,?,?)";
		final String sqlSL = "insert into tblSquadSect (id,classCode,onDutyTime,offDutyTime,onDutyAvailabilityTime,offDutyAvailabilityTime," +
				"squadSectType,createBy,createTime) values (?,?,?,?,?,?,?,?,getdate())";
		int retCode =DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) {
						String SEID = IDGenerater.getId();
						String squadEnactmentNo = getClassCode(SEID,"SE");
						try {
							PreparedStatement ps = con.prepareStatement(sqlSE);
							ps.setString(1, SEID);
							ps.setString(2, squadEnactmentNo);
							ps.setString(3, squadEnactmentName);
							ps.setString(4, createBy);
							ps.executeUpdate();
							
							ps = con.prepareStatement(sqlSL);
							
							for (SquadSect sect : squadSects) {
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, squadEnactmentNo);
								ps.setString(3, sect.getOnDutyTime());
								ps.setString(4, sect.getOffDutyTime());
								ps.setInt(5, sect.getOnDutyAvailabilityTime());
								ps.setInt(6, sect.getOffDutyAvailabilityTime());
								ps.setString(7, sect.getSquadSectType());
								ps.setString(8, createBy);
								ps.executeUpdate();
							}
						} catch (Exception e) {
							BaseEnv.log.error(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
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
